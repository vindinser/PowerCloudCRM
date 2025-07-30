<!-- 跟踪记录 -->
<template>
  <div>
    <div class="title">跟踪记录</div>
    <component
      :is="list.Table"
      isSelection
      :columns="list.tableData.column"
      :list="list.tableData.list"
      :loading="list.tableData.loading"
      :list-query="list.tableData.listQuery"
      :height="500"
      @pagination="list.pagination"
      @sort-change="list.tableSortChange"
      @custom-list="list.tableChange"
      @selection-change="list.selecteChange"
      @button-click="(({ row }) => list.openDetail('Detail', { row }))"
    >
      <template #head-right>
        <el-button type="primary" :icon="Plus" @click="list.openDetail('AddForm', {})">填写跟踪记录</el-button>
        <el-button type="danger" @click="list.openDetail('batch', {}, '删除')">批量删除</el-button>
      </template>
      <template #table-oper>
        <el-table-column label="操作" min-width="120">
          <template #default="scope">
            <el-button type="primary" link @click="list.openDetail('EditForm', scope)">编辑</el-button>
            <el-button type="danger" link @click="list.openDetail('del', scope, '删除')">删除</el-button>
          </template>
        </el-table-column>
      </template>
    </component>
    <component
      :is='componentMap[list.currentComponent.value]'
      :show="list.currentComponent.value"
      :row="list.currentRow.value"
      :clue-row="row"
      @closed="list.detailClose"
    />
  </div>
</template>

<script setup name="ClueTrackRecords">
  import { useList } from '@/composables/useList.js';
  import ClueTrackRecordForm from './form.vue';
  import { Plus } from '@element-plus/icons-vue';
  import { delClueTrackRecord, batchDelClueTrackRecords } from '@/api/clue.js';

  const { row } = defineProps({
    row: {
      type: Object,
      default: () => ({})
    }
  });

  const column = ref([
    { show: true, prop: 'noteWayName', label: '跟踪方式', width: '100' },
    { show: true, prop: 'noteContent', label: '跟踪记录内容', width: '200' },
    { show: true, prop: 'createTime', label: '创建时间', width: '180', sortable: 'custom' },
    { show: true, prop: 'createName', label: '创建人', width: '100' },
    { show: true, prop: 'editTime', label: '修改时间', width: '180', sortable: 'custom' },
    { show: true, prop: 'editName', label: '修改人', width: '100' }
  ]);

  const getTableDataParam = () => ({
    apiChain: ['clue', 'getClueTrackRecords'],
    param: {
      clueId: row.id
    }
  });

  const postRedOperaFn = (currtRow, isBatch, type, batchList) => {
    if(isBatch) {
      const ids = batchList.map(({ id }) => id);

      return batchDelClueTrackRecords({ ids });
    }
    return delClueTrackRecord(currtRow.id);
  };

  // 创建组件映射对象
  const componentMap = {
    AddForm: markRaw(ClueTrackRecordForm),
    EditForm: markRaw(ClueTrackRecordForm)
  };

  const list = useList({
    getTableDataParam,
    postRedOperaFn,
    column
  });

</script>

<style lang="scss" scoped>
  .title {
    font-size: 16px;
    font-weight: bold;
    color: var(--el-text-color-primary);
    line-height: 48px;
  }
</style>