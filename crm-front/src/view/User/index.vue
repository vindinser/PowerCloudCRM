<!-- 用户管理 -->
<template>
  <component :is="list.List" title="用户管理">
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
              <div v-if="list.currentComponent">1{{ list.currentComponent.value ? '-1-' : '2--' }}</div>
              <div v-else>2</div>
              <el-button type="primary" :icon="Search" @click="list.onSearch">查询</el-button>
              <el-button :icon="Delete" @click="list.onReset">清除</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>

    <component
      :is="list.Table"
      isSelection
      :columns="list.tableData.column"
      :list="list.tableData.list"
      :loading="list.tableData.loading"
      :list-query="list.tableData.listQuery"
      @pagination="list.pagination"
      @sort-change="list.tableSortChange"
      @custom-list="list.tableChange"
      @selection-change="list.selecteChange"
      @button-click="(({ row }) => list.openDetail('Detail', { row }))"
    >
      <template #head-right>
        <el-button type="primary" :icon="Plus" @click="list.openDetail('AddForm', {})">新增用户</el-button>
        <el-button type="danger" @click="list.openDetail('batch', {}, '删除')">批量删除</el-button>
      </template>
      <template #table-oper>
        <el-table-column label="操作" min-width="150">
          <template #default="scope">
            <el-button type="primary" link @click="list.openDetail('Detail', scope)">详情</el-button>
            <el-button type="primary" link @click="list.openDetail('EditForm', scope)">编辑</el-button>
            <el-button type="danger" link @click="list.openDetail('del', scope, '删除')">删除</el-button>
          </template>
        </el-table-column>
      </template>
    </component>
    <template #list-detail>
      <component :is='componentMap[list.currentComponent.value]' :row="list.currentRow.value" @closed="list.detailClose" />
    </template>
  </component>
</template>

<script setup name="User">
  import { delUser, batchDelUser } from '@/api/user.js';
  import UserDetail from './detail.vue';
  import UserForm from './form.vue';
  import { Search, Delete, Plus } from '@element-plus/icons-vue';
  import { useList } from '@/composables/useList.js';

  const formSearchRef = ref(null);
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

  const getTableDataParam = () => ({
    apiChain: ['user', 'getUsers'],
    param: {
      keyword: formSearch.keyword
    }
  });

  const postRedOperaFn = (row, isBatch, type, batchList) => {
    if(isBatch) {
      const ids = batchList.map(({ id }) => id);

      return batchDelUser({ ids });
    }
    return delUser(row.id);

  };

  // 创建组件映射对象
  const componentMap = {
    Detail: markRaw(UserDetail),
    AddForm: markRaw(UserForm),
    EditForm: markRaw(UserForm)
  };

  const list = useList({
    formSearchRef,
    getTableDataParam,
    postRedOperaFn,
    column
  });

</script>

<style lang="scss" scoped>

</style>