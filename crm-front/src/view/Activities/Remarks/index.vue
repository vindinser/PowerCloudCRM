<!-- 活动备注 -->
<template>
  <div>
    <div class="title">活动备注</div>
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
        <el-button type="primary" :icon="Plus" @click="list.openDetail('AddForm', {})">填写备注</el-button>
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
      :activityRow="row"
      @closed="list.detailClose"
    />
  </div>
</template>

<script setup name="ActivityRemark">
  import { useList } from '@/composables/useList.js';
  import ActivityRemarksForm from './form.vue';
  import { Plus } from '@element-plus/icons-vue';
  import { delActivityRemark, batchDelActivityRemarks } from '@/api/activities.js';

  const { row } = defineProps({
    row: {
      type: Object,
      default: () => ({})
    }
  });

  const column = ref([
    { show: true, prop: 'noteContent', label: '备注内容', width: '200' },
    { show: true, prop: 'createTime', label: '创建时间', width: '180', sortable: 'custom' },
    { show: true, prop: 'createName', label: '创建人', width: '100' },
    { show: true, prop: 'editTime', label: '修改时间', width: '180', sortable: 'custom' },
    { show: true, prop: 'editName', label: '修改人', width: '100' }
  ]);

  const getTableDataParam = () => ({
    apiChain: ['activities', 'getActivitieRemarks'],
    param: {
      activityId: row.id
    }
  });

  const postRedOperaFn = (currtRow, isBatch, type, batchList) => {
    if(isBatch) {
      const ids = batchList.map(({ id }) => id);

      return batchDelActivityRemarks({ ids });
    }
    return delActivityRemark(currtRow.id);
  };

  // 创建组件映射对象
  const componentMap = {
    AddForm: markRaw(ActivityRemarksForm),
    EditForm: markRaw(ActivityRemarksForm)
  };

  const list = useList({
    getTableDataParam,
    postRedOperaFn,
    column
  });

  // onMounted(() => {})
  // watch(propData, (newVal, oldVal) => {})

  // defineExpose({}) // 将方法、数据暴露给父组件
</script>

<style lang="scss" scoped>
  .title {
    font-size: 16px;
    font-weight: bold;
    color: var(--el-text-color-primary);
    line-height: 48px;
  }
</style>