<!-- 用户管理 -->
<template>
  <ZsList title="用户管理">
    <template #body-search>
      <el-form ref="formSearchRef" :model="formSearch" label-width="100px">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="关键字" prop="keyword">
              <el-input v-model="formSearch.keyword" placeholder="请输入关键字" clearable />
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

    <ZsTable
      :columns="column"
      :list="tableData.list"
      :loading="tableData.loading"
      :list-query="tableData.listQuery"
      @pagination="pagination"
      @sort-change="tableSortChange"
      @custom-list="tableChange"
      @button-click="(({ row }) => openDetail('Detail', { row }))"
    >
      <template #head-right>
        <el-button type="primary">新增用户</el-button>
        <el-button type="danger">批量删除</el-button>
      </template>
      <template #table-oper>
        <el-table-column label="操作" min-width="150">
          <template #default="scope">
            <el-button type="primary" link @click="openDetail('Detail', scope)">详情</el-button>
            <el-button type="primary" link>编辑</el-button>
            <el-button type="danger" link>删除</el-button>
          </template>
        </el-table-column>
      </template>
    </ZsTable>
    <template #list-detail>
      <component :is='currentComponent' v-if="currentComponent" :row="currentRow" @closed="currentComponent = null" />
    </template>
  </ZsList>
</template>

<script setup name="User">
  import { getUsers } from '@/api/user.js';
  import ZsList from '@/components/ZSList';
  import ZsTable from '@/components/ZSTable';
  import UserDetail from './detail.vue';
  import { Search, Delete } from '@element-plus/icons-vue';

  const formSearch = reactive({
    keyword: ''
  });

  const column = ref([
    { show: true, prop: 'loginAct', label: '账号', width: '120', type: 'button' },
    { show: true, prop: 'name', label: '姓名', width: '120', type: 'button' },
    { show: true, prop: 'phone', label: '手机', width: '120' },
    { show: true, prop: 'email', label: '邮箱', width: '150' },
    { show: true, prop: 'createTime', label: '创建时间', width: '200', sortable: 'custom' },
    { show: true, prop: 'editTime', label: '修改时间', width: '200', sortable: 'custom' }
  ]);

  const tableData = reactive({
    list: [],
    listQuery: {
      page: 1,
      size: 20,
      total: 0
    },
    defaultSort: {
      prop: '',
      order: ''
    } // 排序
  });

  const getTableData = () => {
    getUsers({
      page: tableData.listQuery.page,
      size: tableData.listQuery.size,
      keyword: formSearch.keyword,
      sortField: tableData.defaultSort.prop,
      sortOrder: tableData.defaultSort.order
    }).then((res) => {
      if(res.code === 200) {
        tableData.list = res.data.list;
        tableData.listQuery.total = res.data.total;
      } else {
        tableData.list = [];
        tableData.listQuery.total = 0;
      }
    }).catch(() => {
      tableData.list = [];
      tableData.listQuery.total = 0;
    });
  };

  const pagination = ({ page, size }) => {
    if(tableData.listQuery.size !== size) {
      tableData.listQuery.page = 1;
    } else {
      tableData.listQuery.page = page;
    }
    tableData.listQuery.size = size;
    getTableData();
  };

  const onSearch = () => {
    tableData.listQuery.page = 1;
    getTableData();
  };

  const formSearchRef = ref(null);
  const onReset = () => {
    formSearchRef.value.resetFields();
    onSearch();
  };

  // 表格排序改变
  const tableSortChange = ({ prop, order }) => {
    tableData.defaultSort.prop = prop;
    tableData.defaultSort.order = order;
    onSearch();
  };


  // 自定义列表
  const tableChange = (columns) => {
    column.value = columns;
  };

  // 创建组件映射对象
  const componentMap = {
    Detail: markRaw(UserDetail)
  };
  const currentComponent = shallowRef(null);
  const currentRow = shallowRef(null);
  // 打开详情
  const openDetail = (type = '', { row }) => {
    currentRow.value = row;
    currentComponent.value = componentMap[type];
  };

  onMounted(() => {
    getTableData();
  });
</script>

<style lang="scss" scoped>

</style>