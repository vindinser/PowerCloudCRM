<!-- 活动详情 -->
<template>
  <ZSDetail isFoot width="75%" title="市场活动详情" @closed="emit('closed')">
    <el-descriptions title="市场活动基本信息" border style="margin-top: 20px" :column="2">
      <el-descriptions-item v-for="item, index in descriptions" :key="index" label-width="120" :label="item.label" :span="item.span || 1">
        <span>{{ echoInfo[item.value] }}</span>
      </el-descriptions-item>
    </el-descriptions>
    <ActivityRemark :row="row" />
    <template #foot>
      <el-button @click="emit('closed')">关闭</el-button>
    </template>
  </ZSDetail>
</template>

<script setup name="ActivitiesDetail">
  import ZSDetail from '@/components/ZSDetail';
  import { getActivity } from '@/api/activities.js';
  import ActivityRemark from './Remarks';

  const emit = defineEmits(['closed']);
  const { row } = defineProps({
    row: {
      type: Object,
      default: () => ({})
    }
  });

  const descriptions = [
    { label: '活动名称', value: 'name', span: 2 },
    { label: '负责人', value: 'ownerName' },
    { label: '活动预算', value: 'cost' },
    { label: '开始时间', value: 'startTime' },
    { label: '结束时间', value: 'endTime' },
    { label: '创建时间', value: 'createTime' },
    { label: '创建人', value: 'createName' },
    { label: '修改时间', value: 'editTime' },
    { label: '修改人', value: 'editName' }
  ];

  const echoInfo = ref({});
  const getEchoInfo = async () => {
    const res = await getActivity(row.id);

    if(res.code === 200) {
      echoInfo.value = res.data;
    }
  };

  onMounted(() => {
    getEchoInfo();
  });
</script>

<style lang="scss" scoped>

</style>