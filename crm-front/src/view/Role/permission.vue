<!-- 分配权限弹窗 -->
<template>
  <ZSDetail isFoot width="50%" :title="`分配权限 - ${row.roleName || ''}`" @closed="close()">
    <div v-loading="loading" style="padding: 10px 0;">
      <el-tree
        ref="treeRef"
        :data="menuTree"
        show-checkbox
        node-key="id"
        :default-checked-keys="checkedKeys"
        :props="{ children: 'subPermissionList', label: 'name' }"
        default-expand-all
      />
    </div>
    <template #foot>
      <el-button type="primary" @click="onSubmit">保 存</el-button>
      <el-button @click="close()">关 闭</el-button>
    </template>
  </ZSDetail>
</template>

<script setup name="RolePermission">
  import ZSDetail from '@/components/ZSDetail';
  import { getMenuTree } from '@/api/permission.js';
  import { getRolePermissions, assignPermissions } from '@/api/role.js';

  const { proxy } = getCurrentInstance();
  const emit = defineEmits(['closed']);
  const { row } = defineProps({
    row: {
      type: Object,
      default: () => ({})
    }
  });

  const close = () => { emit('closed') };

  const loading = ref(false);
  const menuTree = ref([]);
  const checkedKeys = ref([]);
  const treeRef = ref(null);

  const loadData = async () => {
    loading.value = true;
    try {
      // 加载菜单树
      const treeRes = await getMenuTree();

      if (treeRes.code === 200) {
        menuTree.value = treeRes.ListData || treeRes.data || [];
      }

      // 加载当前角色已有的权限
      const permRes = await getRolePermissions(row.id);

      if (permRes.code === 200) {
        checkedKeys.value = permRes.ListData || permRes.data || [];
      }
    } finally {
      loading.value = false;
      // 数据到位后显式回填勾选（default-checked-keys 对异步数据不保证生效）
      treeRef.value?.setCheckedKeys(checkedKeys.value);
    }
  };

  const onSubmit = async () => {
    const checkedIds = treeRef.value.getCheckedKeys();
    const halfCheckedIds = treeRef.value.getHalfCheckedKeys();
    const permissionIds = [...checkedIds, ...halfCheckedIds];

    const res = await assignPermissions(row.id, { permissionIds });

    if (res.code === 200) {
      proxy.$modal.msgSuccess('权限分配成功');
      close();
    }
  };

  onMounted(() => { loadData() });
</script>

<style lang="scss" scoped>

</style>
