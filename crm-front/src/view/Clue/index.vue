<!-- 线索管理 -->
<template>
  <component :is="list.List" title="线索管理">
    <template #body-search>
      <el-form ref="formSearchRef" :model="formSearch" label-width="100px">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="关键字" prop="keyword">
              <el-input v-model="formSearch.keyword" placeholder="请输入关键字" clearable />
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
        <!-- 手续导入 -->
        <input ref="ImportExcelInput" type="file" name="Excel" accept=".xls, .xlsx" style="display: none" @change="getExcelData($event)" >
        <el-button @click="importExcel">导入线索</el-button>
        <el-button type="primary" :icon="Plus" @click="list.openDetail('AddForm', {})">录入线索</el-button>
        <el-button type="danger" @click="list.openDetail('batch', {}, '删除')">批量删除</el-button>
      </template>
      <template #table-oper>
        <el-table-column label="操作" min-width="220" fixed="right">
          <template #default="scope">
            <el-button type="primary" link @click="list.openDetail('Detail', scope)">详情</el-button>
            <el-button type="warning" link @click="list.openDetail('EditForm', scope)">编辑</el-button>
            <el-button type="danger" link @click="list.openDetail('del', scope, '删除')">删除</el-button>
            <el-button
              type="success"
              :disabled="scope.row.state === -1"
              link
              @click="list.openDetail('TrackRecord', scope, '转客户')"
            >转客户</el-button>
          </template>
        </el-table-column>
      </template>
    </component>
    <template #list-detail>
      <component
        :is='componentMap[list.currentComponent.value]'
        :row="list.currentRow.value"
        :show="list.currentComponent.value"
        :ownerList="ownerList"
        :shortcuts="list.shortcuts"
        @closed="list.detailClose"
      />
    </template>
  </component>
</template>

<script setup name="Clue">
  import { getOwers } from '@/api/user.js';
  import { Search, Delete, Plus } from '@element-plus/icons-vue';
  import { useList } from '@/composables/useList.js';
  import ClueForm from './form.vue';
  import ClueDetail from './detail.vue';
  import { delClue, batchDelClues, importClues } from '@/api/clue.js';
  import TrackRecord from './TransferCustomer';

  const { proxy } = getCurrentInstance();
  const formSearchRef = ref(null);
  const formSearch = reactive({
    keyword: '',
    ownerIds: [],
    time: []
  });

  const column = ref([
    { show: true, prop: 'ownerName', label: '负责人', width: '80' },
    { show: true, prop: 'fullName', label: '姓名', width: '80', type: 'button' },
    { show: true, prop: 'appellationName', label: '称呼', width: '80' },
    { show: true, prop: 'phone', label: '手机', width: '120' },
    { show: true, prop: 'weixin', label: '微信', width: '120' },
    { show: true, prop: 'needLoanName', label: '是否贷款', width: '100' },
    { show: true, prop: 'intentionStateName', label: '意向状态', width: '100' },
    { show: true, prop: 'intentionProductName', label: '意向产品', width: '100' },
    { show: true, prop: 'stateName', label: '线索状态', width: '100' },
    { show: true, prop: 'sourceName', label: '线索来源', width: '100' },
    { show: true, prop: 'nextContactTime', label: '下次联系时间', width: '200', sortable: 'custom' },
    { show: true, prop: 'createTime', label: '创建时间', width: '200', sortable: 'custom' },
    { show: true, prop: 'createName', label: '创建人', width: '100' },
    { show: true, prop: 'editTime', label: '修改时间', width: '200', sortable: 'custom' },
    { show: true, prop: 'editName', label: '修改人', width: '100' }
  ]);

  const getTableDataParam = () => ({
    apiChain: ['clue', 'getClue'],
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

      return batchDelClues({ ids });
    }
    return delClue(row.id);

  };

  // 创建组件映射对象
  const componentMap = {
    Detail: markRaw(ClueDetail),
    AddForm: markRaw(ClueForm),
    EditForm: markRaw(ClueForm),
    TrackRecord: markRaw(TrackRecord)
  };

  const list = useList({
    formSearchRef,
    getTableDataParam,
    postRedOperaFn,
    column
  });


  const ImportExcelInput = ref(null);
  const importExcel = () => {
    ImportExcelInput.value.value = null;
    ImportExcelInput.value.click();
  };

  const getExcelData = async (data) => {
    const files = data.target?.files;

    if(!files) {return proxy.msg('请选择正确的Excel文件')}
    let formData = new FormData();

    formData.append('file', files?.[0]);
    const res = await importClues(formData);

    if(res.code === 200) {
      list.onSearch();
    }
  };

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