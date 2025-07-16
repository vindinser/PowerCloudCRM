<!-- 市场活动 -->
<template>
  <component :is="list.List" title="市场活动">
    <template #body-search>
      <el-form ref="formSearchRef" :model="formSearch" label-width="100px">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="关键字" prop="keyword">
              <el-input v-model="formSearch.keyword" placeholder="请输入活动名称/活动预算" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="负责人" prop="ownerIds">
              <el-select v-model="formSearch.ownerIds" multiple collapse-tags placeholder="请选择负责人" clearable filterable>
                <el-option v-for="item, index in ownerList" :key="index" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="时间范围" prop="time">
              <el-date-picker
                v-model="formSearch.time"
                value-format="YYYY-MM-DD HH:mm:ss"
                type="datetimerange"
                align="right"
                unlink-panels
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :shortcuts="list.shortcuts"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="">
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
        <el-button type="primary" :icon="Plus" @click="list.openDetail('AddForm', {})">录入市场活动</el-button>
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
      <component
        :is='componentMap[list.currentComponent.value]'
        :row="list.currentRow.value"
        :ownerList="ownerList"
        :shortcuts="list.shortcuts"
        @closed="list.detailClose"
      />
    </template>
  </component>
</template>

<script setup name="MarketingActivities">
  import { getOwers } from '@/api/user.js';
  import { Search, Delete, Plus } from '@element-plus/icons-vue';
  import { useList } from '@/composables/useList.js';
  import ActivitiesForm from './form.vue';
  import ActivitiesDetail from './detail.vue';
  import { delActivity, batchDelActivities } from '@/api/activities.js';

  const formSearchRef = ref(null);
  const formSearch = reactive({
    keyword: '',
    ownerIds: [],
    time: []
  });

  const column = ref([
    { show: true, prop: 'ownerName', label: '负责人', width: '100' },
    { show: true, prop: 'name', label: '活动名称', width: '120', type: 'button' },
    { show: true, prop: 'startTime', label: '开始时间', width: '200', sortable: 'custom' },
    { show: true, prop: 'endTime', label: '结束时间', width: '200', sortable: 'custom' },
    { show: true, prop: 'cost', label: '活动预算', width: '150', sortable: 'custom' },
    { show: true, prop: 'createTime', label: '创建时间', width: '200', sortable: 'custom' },
    // { show: true, prop: 'createBy', label: '创建人', width: '100' },
    { show: true, prop: 'editTime', label: '修改时间', width: '200', sortable: 'custom' }
    // { show: true, prop: 'editBy', label: '修改人', width: '100' }
  ]);

  const getTableDataParam = () => ({
    apiChain: ['activities', 'getActivities'],
    param: {
      keyword: formSearch.keyword,
      startTime: formSearch.time?.[0] ?? '',
      endTime: formSearch.time?.[1] ?? '',
      ownerIds: formSearch.ownerIds
    }
  });

  const postRedOperaFn = (row, isBatch, type, batchList) => {
    if(isBatch) {
      const ids = batchList.map(({ id }) => id);

      return batchDelActivities({ ids });
    }
    return delActivity(row.id);

  };

  // 创建组件映射对象
  const componentMap = {
    Detail: markRaw(ActivitiesDetail),
    AddForm: markRaw(ActivitiesForm),
    EditForm: markRaw(ActivitiesForm)
  };

  const list = useList({
    formSearchRef,
    getTableDataParam,
    postRedOperaFn,
    column
  });

  const ownerList = ref([]);
  // 获取负责人
  const getOwerList = async () => {
    const res = await getOwers();

    if(res.code === 200) {
      ownerList.value = res.ListData;
    }
  };

  getOwerList();

</script>

<style lang="scss" scoped>

</style>