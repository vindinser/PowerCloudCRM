<!-- 角色管理 -->
<template>
  <ZSList title="角色管理">
    <template #body-search>
      <el-form ref="formSearchRef" :model="formSearch" label-width="100px">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="关键字" prop="keyword">
              <el-input v-model="formSearch.keyword" placeholder="请输入角色名称" clearable />
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

    <ZSTable
      isSelection
      :columns="column"
      :list="tableData.list"
      :loading="tableData.loading"
      :list-query="tableData.listQuery"
      @selection-change="selecteChange"
      @pagination="pagination"
      @sort-change="tableSortChange"
    >
      <template #head-right>
        <el-button v-hasPermission="'role:add'" type="primary" :icon="Plus" @click="openForm({})">新增角色</el-button>
        <el-button v-hasPermission="'role:delete'" type="danger" @click="batchDelete">批量删除</el-button>
      </template>
      <template #table-oper>
        <el-table-column label="操作" min-width="200">
          <template #default="scope">
            <el-button v-hasPermission="'role:edit'" type="primary" link @click="openForm(scope.row)">编辑</el-button>
            <el-button v-hasPermission="'role:edit'" type="primary" link @click="openPermission(scope.row)">分配权限</el-button>
            <el-button v-hasPermission="'role:delete'" type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </template>
    </ZSTable>

    <!-- 角色表单弹窗 -->
    <RoleForm v-if="formVisible" :row="currentRow" @closed="formClosed" />
    <!-- 权限分配弹窗 -->
    <PermissionForm v-if="permVisible" :row="currentRow" @closed="permClosed" />
  </ZSList>
</template>

<script setup name="Role">
  import ZSList from '@/components/ZSList';
  import ZSTable from '@/components/ZSTable';
  import { getRolePage, delRole, batchDelRole } from '@/api/role.js';
  import RoleForm from './form.vue';
  import PermissionForm from './permission.vue';
  import { Search, Delete, Plus } from '@element-plus/icons-vue';

  const { proxy } = getCurrentInstance();
  const formSearchRef = ref(null);
  const formSearch = reactive({ keyword: '' });

  const column = ref([
    { show: true, prop: 'role', label: '角色标识', width: '150' },
    { show: true, prop: 'roleName', label: '角色名称', width: '150', type: 'button' }
  ]);

  const tableData = reactive({
    list: [],
    listQuery: { page: 1, size: 20, total: 0 },
    loading: false
  });

  const batchList = ref([]);
  const selecteChange = (rows) => { batchList.value = rows };

  // 排序状态
  const defaultSort = reactive({ prop: '', order: '' });

  const getTableData = async () => {
    tableData.loading = true;
    try {
      const data = await getRolePage({
        keyword: formSearch.keyword,
        page: tableData.listQuery.page,
        size: tableData.listQuery.size,
        sortField: defaultSort.prop,
        sortOrder: defaultSort.order
      });

      if (data.code === 200) {
        tableData.list = data.ListData || [];
        tableData.listQuery.total = data.data && data.data.total || 0;
      }
    } finally {
      tableData.loading = false;
    }
  };

  const onSearch = () => {
    tableData.listQuery.page = 1;
    getTableData();
  };
  const onReset = () => {
    formSearchRef.value.resetFields();
    onSearch();
  };

  // 分页变化
  const pagination = ({ page, size }) => {
    tableData.listQuery.page = page;
    tableData.listQuery.size = size;
    getTableData();
  };
  // 排序变化
  const tableSortChange = ({ prop, order }) => {
    defaultSort.prop = prop;
    defaultSort.order = order || '';
    onSearch();
  };

  // 表单相关
  const formVisible = ref(false);
  const currentRow = ref({});
  const openForm = (row) => {
    currentRow.value = { ...row };
    formVisible.value = true;
  };
  const formClosed = (isRefresh) => {
    formVisible.value = false;
    if (isRefresh) {onSearch()}
  };

  // 权限分配相关
  const permVisible = ref(false);
  const openPermission = (row) => {
    currentRow.value = { ...row };
    permVisible.value = true;
  };
  const permClosed = () => { permVisible.value = false };

  // 删除
  const handleDelete = (row) => {
    proxy.$modal.confirm('确定删除该角色吗?').then(async () => {
      const res = await delRole(row.id);

      if (res.code === 200) {
        proxy.$modal.msgSuccess('删除成功');
        onSearch();
      }
    });
  };

  const batchDelete = () => {
    if (batchList.value.length === 0) {
      return proxy.$modal.msgWarning('请选择批量操作数据！');
    }
    proxy.$modal.confirm('确定删除选中角色吗?').then(async () => {
      const ids = batchList.value.map(item => item.id);
      const res = await batchDelRole({ ids });

      if (res.code === 200) {
        proxy.$modal.msgSuccess('删除成功');
        onSearch();
      }
    });
  };

  onMounted(() => { getTableData() });
</script>

<style lang="scss" scoped>

</style>
