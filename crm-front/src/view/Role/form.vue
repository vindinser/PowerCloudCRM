<!-- 角色表单 -->
<template>
  <ZSDetail isFoot width="40%" :title="`${isEdit ? '修改角色' : '新增角色'}`" @closed="close()">
    <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="100px" style="width: 80%;">
      <el-form-item label="角色标识" prop="role">
        <el-input v-model="ruleForm.role" placeholder="请输入角色标识" clearable :disabled="isEdit" />
      </el-form-item>
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="ruleForm.roleName" placeholder="请输入角色名称" clearable />
      </el-form-item>
    </el-form>
    <template #foot>
      <el-button type="primary" @click="onSubmit">确 认</el-button>
      <el-button @click="close()">关 闭</el-button>
    </template>
  </ZSDetail>
</template>

<script setup name="RoleForm">
  import ZSDetail from '@/components/ZSDetail';
  import { addRole, updateRole } from '@/api/role.js';

  const emit = defineEmits(['closed']);
  const { row } = defineProps({
    row: {
      type: Object,
      default: () => ({})
    }
  });

  const close = (isRefresh = false) => {
    emit('closed', isRefresh);
  };

  const isEdit = ref(false);
  const ruleForm = ref({
    role: '',
    roleName: ''
  });
  const rules = ref({
    role: [
      { required: true, message: '请输入角色标识', trigger: 'blur' },
      { min: 1, max: 30, message: '角色标识最多30个字符', trigger: 'blur' }
    ],
    roleName: [
      { required: true, message: '请输入角色名称', trigger: 'blur' },
      { min: 1, max: 30, message: '角色名称最多30个字符', trigger: 'blur' }
    ]
  });

  const ruleFormRef = ref(null);
  const onSubmit = () => {
    ruleFormRef.value.validate(async (valid) => {
      if (!valid) {return}
      const res = isEdit.value ? await updateRole({ id: row.id, ...ruleForm.value }) : await addRole(ruleForm.value);

      if (res.code === 200) {
        close(true);
      }
    });
  };

  onMounted(() => {
    if (row.id) {
      isEdit.value = true;
      ruleForm.value.role = row.role || '';
      ruleForm.value.roleName = row.roleName || '';
    }
  });
</script>

<style lang="scss" scoped>

</style>
