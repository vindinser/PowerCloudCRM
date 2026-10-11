<!-- 菜单管理 -->
<template>
  <ZSList title="菜单管理">
    <template #body-search>
      <el-form ref="formSearchRef" :model="formSearch" label-width="100px">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="关键字" prop="keyword">
              <el-input v-model="formSearch.keyword" placeholder="请输入菜单名称" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="">
              <el-button type="primary" :icon="Search" @click="onSearch">查询</el-button>
              <el-button :icon="Delete" @click="onReset">清除</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>

    <el-table
      v-loading="loading"
      :data="filteredData"
      row-key="id"
      default-expand-all
      :tree-props="{ children: 'subPermissionList' }"
      border
    >
      <el-table-column prop="name" label="菜单名称" min-width="180" />
      <el-table-column prop="url" label="路由地址" min-width="150" />
      <el-table-column prop="icon" label="图标" width="100" />
      <el-table-column prop="orderNo" label="排序" width="80" />
      <el-table-column prop="type" label="类型" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.type === 'menu' ? 'primary' : 'warning'" size="small">
            {{ scope.row.type === 'menu' ? '菜单' : '按钮' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="code" label="权限标识" min-width="150" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button v-hasPermission="'permission:add'" type="primary" link @click="openForm(scope.row, 'add')">新增</el-button>
          <el-button v-hasPermission="'permission:edit'" type="primary" link @click="openForm(scope.row, 'edit')">编辑</el-button>
          <el-button v-hasPermission="'permission:delete'" type="danger" link @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 菜单表单弹窗 -->
    <PermissionForm v-if="formVisible" :row="currentRow" :type="formType" @closed="formClosed" />
  </ZSList>
</template>

<script setup name="Permission">
  import ZSList from '@/components/ZSList';
  import { getAllPermissions, delPermission } from '@/api/permission.js';
  import PermissionForm from './form.vue';
  import { Search, Delete } from '@element-plus/icons-vue';

  const { proxy } = getCurrentInstance();
  const formSearchRef = ref(null);
  const formSearch = reactive({ keyword: '' });

  const loading = ref(false);
  const allData = ref([]);

  const filteredData = computed(() => {
    if (!formSearch.keyword) {return allData.value}
    // 纯函数过滤：返回新对象，不修改 allData，保证清除关键字后子菜单可恢复
    const filterTree = (list) => {
      const result = [];

      (list || []).forEach(item => {
        const nameMatch = item.name.includes(formSearch.keyword);
        const children = filterTree(item.subPermissionList);

        if (nameMatch || children.length > 0) {
          result.push({ ...item, subPermissionList: children });
        }
      });
      return result;
    };

    return filterTree(allData.value);
  });

  const getTableData = async () => {
    loading.value = true;
    try {
      const res = await getAllPermissions();

      if (res.code === 200) {
        const list = res.ListData || res.data || [];
        // 构建树结构：先筛选一级菜单，再挂载子菜单
        const parentMenus = list.filter(item => item.parentId === 0);

        parentMenus.forEach(parent => {
          parent.subPermissionList = list.filter(item => item.parentId === parent.id);
        });
        allData.value = parentMenus;
      }
    } finally {
      loading.value = false;
    }
  };

  const onSearch = () => { getTableData() };
  const onReset = () => {
    formSearchRef.value.resetFields();
    getTableData();
  };

  // 表单相关
  const formVisible = ref(false);
  const currentRow = ref({});
  const formType = ref('add');
  const openForm = (row, type) => {
    currentRow.value = { ...row };
    formType.value = type;
    formVisible.value = true;
  };
  const formClosed = (isRefresh) => {
    formVisible.value = false;
    if (isRefresh) {getTableData()}
  };

  // 删除
  const handleDelete = (row) => {
    proxy.$modal.confirm('确定删除该菜单吗?').then(async () => {
      const res = await delPermission(row.id);

      if (res.code === 200) {
        proxy.$modal.msgSuccess('删除成功');
        getTableData();
      }
    });
  };

  onMounted(() => { getTableData() });
</script>

<style lang="scss" scoped>

</style>
