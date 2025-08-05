<!-- 客户详情 -->
<template>
  <ZSDetail isFoot width="75%" title="客户详情" @closed="emit('closed')">
    <el-descriptions title="客户基本信息" border style="margin-top: 20px" :column="2">
      <el-descriptions-item v-for="item, index in descriptions" :key="index" label-width="120" :label="item.label" :span="item.span || 1">
        <span>{{ row[item.value] }}</span>
      </el-descriptions-item>
    </el-descriptions>
    <template #foot>
      <el-button @click="emit('closed')">关闭</el-button>
    </template>
  </ZSDetail>
</template>

<script setup name="ClueDetail">
  import ZSDetail from '@/components/ZSDetail';

  const emit = defineEmits(['closed']);
  const { row } = defineProps({
    row: {
      type: Object,
      default: () => ({})
    }
  });

  const descriptions = [
    { label: '负责人', value: 'ownerName' },
    { label: '所属活动', value: 'activityName' },
    { label: '姓名', value: 'fullName' },
    { label: '称呼', value: 'appellationName' },
    { label: '手机', value: 'phone' },
    { label: '微信', value: 'weixin' },
    { label: 'QQ', value: 'qq' },
    { label: '邮箱', value: 'email' },
    { label: '年龄', value: 'age' },
    { label: '职业', value: 'job' },
    { label: '年收入', value: 'yearIncome' },
    { label: '住址', value: 'address' },
    { label: '贷款', value: 'needLoanName' },
    { label: '意向状态', value: 'intentionStateName' },
    { label: '意向产品', value: 'intentionProductName' },
    { label: '线索状态', value: 'stateName' },
    { label: '线索来源', value: 'sourceName' },
    { label: '下次联系时间', value: 'nextContactTime' },
    { label: '创建人', value: 'createName' },
    { label: '修改人', value: 'editName' },
    { label: '线索描述', value: 'description', span: 2 }
  ];

</script>

<style lang="scss" scoped>

</style>