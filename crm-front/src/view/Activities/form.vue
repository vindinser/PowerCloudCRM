<!-- 录入/修改 市场活动 -->
<template>
  <ZSDetail isFoot width="50%" :title="`${ isEdit ? '修改' : '录入' }市场活动`" @closed="close()">
    <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="108px" style="width: 75%;">
      <el-form-item v-for="item, index in descriptions" :key="index" :label="item.label" :prop="item.value">
        <el-select v-if="item.type === 'select'" v-model="ruleForm[item.value]" :placeholder="`请选择${ item.label }`" clearable filterable>
          <el-option v-for="item, index in ownerList" :key="index" :label="item.label" :value="item.value" />
        </el-select>
        <el-date-picker
          v-else-if="item.type === 'time'"
          v-model="ruleForm[item.value]"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="datetimerange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :shortcuts="shortcuts"
          clearable
        />
        <el-input
          v-else
          v-model="ruleForm[item.value]"
          :type="item.value === 'description' ? 'textarea' : 'text'"
          :autosize="{ minRows: 2 }"
          :placeholder="`请输入${ item.label }`"
          clearable
        />
      </el-form-item>
    </el-form>
    <template #foot>
      <el-button type="primary" @click="onSubmit">确 认</el-button>
      <el-button @click="close()">关 闭</el-button>
    </template>
  </ZSDetail>
</template>

<script setup name="ActivitiesForm">
  import ZSDetail from '@/components/ZSDetail';
  import { addActivities, updateActivities } from '@/api/activities.js';

  const { proxy } = getCurrentInstance();
  const emit = defineEmits(['closed']);
  const { row, ownerList, shortcuts } = defineProps({
    row: {
      type: Object,
      default: () => ({})
    },
    ownerList: {
      type: Array,
      default: () => []
    },
    shortcuts: {
      type: Array,
      default: () => []
    }
  });

  /**
   * 关闭页面
   * @param {boolean} isRefresh 是否刷新列表
   */
  const close = (isRefresh = false) => {
    emit('closed', isRefresh);
  };

  const descriptions = ref([
    { label: '负责人', value: 'ownerId', type: 'select' },
    { label: '活动名称', value: 'name' },
    { label: '活动有效期', value: 'time', type: 'time' },
    { label: '活动预算', value: 'cost' },
    { label: '活动描述', value: 'description' }
  ]);

  const ruleForm = ref({});
  const rules = ref({});

  // 初始化表单字段
  (() => {
    for(let k in descriptions.value) {
      const { label, value, type } = descriptions.value[k];

      ruleForm.value[value] = '';
      if(value === 'description') {continue}
      rules.value[value] = [{ required: true, message: `请${ type ? '选择' : '输入' }${ label }`, trigger: 'change' }];
    }
  })();

  const isEdit = ref(false);

  const ruleFormRef = ref(null);
  // 确认新增
  const onSubmit = () => {
    ruleFormRef.value.validate(async (valid, fields) => {
      if(!valid) {
        let msg = '';

        for(let k in fields) {
          const { message } = fields[k][0];

          msg ||= message;
          if(msg) {
            break;
          }
        }
        return proxy.$modal.msgWarning(msg || '请认真填写表单！');
      }
      const param = {
        ...ruleForm.value,
        startTime: ruleForm.value.time?.[0] ?? '',
        endTime: ruleForm.value.time?.[1] ?? ''
      };

      const res = isEdit.value ? await updateActivities({
        id: row.id,
        ...param
      }) : await addActivities(param);

      if(res.code === 200) {
        close(true);
      }
    });
  };

  // 获取回显信息
  const getEchoInfo = () => {
    for(let k in ruleForm.value) {
      if(k === 'time') {
        ruleForm.value[k] = [row.startTime || '', row.endTime || ''];
      } else {
        ruleForm.value[k] = row[k] || '';
      }
    }
  };

  onMounted(() => {
    const isAdd = proxy.$utils.isEmptyObject(row);

    if(!isAdd) {
      isEdit.value = true;
      getEchoInfo();
    }
  });

</script>

<style lang="scss" scoped>

</style>