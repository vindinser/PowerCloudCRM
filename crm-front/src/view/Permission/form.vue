<!-- 菜单/权限表单 -->
<template>
  <ZSDetail isFoot width="50%" :title="formTitle" @closed="close()">
    <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="108px" style="width: 85%;">
      <el-form-item label="资源类型" prop="type">
        <el-radio-group v-model="ruleForm.type">
          <el-radio value="menu">菜单</el-radio>
          <el-radio value="button">按钮</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="上级菜单" prop="parentId">
        <el-select v-model="ruleForm.parentId" placeholder="请选择上级菜单" clearable style="width: 100%;">
          <el-option :value="0" label="无（一级菜单）" />
          <el-option
            v-for="item in parentMenuOptions"
            :key="item.id"
            :value="item.id"
            :label="item.name"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="菜单名称" prop="name">
        <el-input v-model="ruleForm.name" placeholder="请输入菜单名称" clearable />
      </el-form-item>
      <el-form-item v-if="ruleForm.type === 'button'" label="权限标识" prop="code">
        <el-input v-model="ruleForm.code" placeholder="如: user:list" clearable />
      </el-form-item>
      <el-form-item v-if="ruleForm.type === 'menu'" label="路由地址" prop="url">
        <el-input v-model="ruleForm.url" placeholder="如: /dashboard/user" clearable />
      </el-form-item>
      <el-form-item v-if="ruleForm.type === 'menu'" label="图标" prop="icon">
        <el-input v-model="ruleForm.icon" placeholder="Element Plus图标名称" clearable />
      </el-form-item>
      <el-form-item label="排序" prop="orderNo">
        <el-input-number v-model="ruleForm.orderNo" :min="0" :max="999" />
      </el-form-item>
    </el-form>
    <template #foot>
      <el-button type="primary" @click="onSubmit">确 认</el-button>
      <el-button @click="close()">关 闭</el-button>
    </template>
  </ZSDetail>
</template>

<script setup name="PermissionForm">
  import ZSDetail from '@/components/ZSDetail';
  import { getAllPermissions, addPermission, updatePermission } from '@/api/permission.js';

  const emit = defineEmits(['closed']);
  const { row, type } = defineProps({
    row: {
      type: Object,
      default: () => ({})
    },
    type: {
      type: String,
      default: 'add'
    }
  });

  const close = (isRefresh = false) => {
    emit('closed', isRefresh);
  };

  const isEdit = computed(() => type === 'edit');
  const formTitle = computed(() => {
    if (isEdit.value) {return '编辑菜单'}
    return row.id ? `新增子菜单 - ${row.name}` : '新增菜单';
  });

  const parentMenuOptions = ref([]);
  const ruleForm = ref({
    type: 'menu',
    parentId: 0,
    name: '',
    code: '',
    url: '',
    icon: '',
    orderNo: 0
  });

  const rules = ref({
    type: [{ required: true, message: '请选择资源类型', trigger: 'change' }],
    name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
    parentId: [{ required: true, message: '请选择上级菜单', trigger: 'change' }]
  });

  const ruleFormRef = ref(null);

  // 切换资源类型时清空另一类字段，避免残留脏数据
  watch(
    () => ruleForm.value.type,
    (newType) => {
      if (newType === 'button') {
        ruleForm.value.url = '';
        ruleForm.value.icon = '';
      } else {
        ruleForm.value.code = '';
      }
    }
  );

  const onSubmit = () => {
    ruleFormRef.value.validate(async (valid) => {
      if (!valid) {return}
      const res = isEdit.value ?
        await updatePermission({ id: row.id, ...ruleForm.value }) :
        await addPermission(ruleForm.value);

      if (res.code === 200) {
        close(true);
      }
    });
  };

  const loadParentMenus = async () => {
    const res = await getAllPermissions();

    if (res.code === 200) {
      const list = res.ListData || res.data || [];

      parentMenuOptions.value = list.filter(item => item.type === 'menu' && item.parentId === 0);
    }
  };

  onMounted(() => {
    loadParentMenus();
    if (type === 'edit' && row.id) {
      ruleForm.value.type = row.type || 'menu';
      ruleForm.value.parentId = row.parentId || 0;
      ruleForm.value.name = row.name || '';
      ruleForm.value.code = row.code || '';
      ruleForm.value.url = row.url || '';
      ruleForm.value.icon = row.icon || '';
      ruleForm.value.orderNo = row.orderNo || 0;
    } else if (row.id) {
      // 新增子菜单，自动设置上级
      ruleForm.value.parentId = row.id;
      ruleForm.value.type = row.type || 'menu';
    }
  });
</script>

<style lang="scss" scoped>

</style>
