<!-- 新增/编辑 活动备注 -->
<template>
  <div>
    <el-dialog v-model="dialogVisible" title="活动备注" width="500">
      <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="auto" style="width: 75%;">
        <el-form-item label="活动备注" prop="noteContent">
          <el-input v-model="ruleForm.noteContent" :autosize="{ minRows: 2, maxRows: 6 }" type="textarea"  placeholder="请填写活动备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="onSubmit">确 认</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ActivityRemarksForm">
  import { addActivitieRemark, updateActivitieRemark } from '@/api/activities.js';

  const { proxy } = getCurrentInstance();

  const emit = defineEmits(['closed']);
  const props = defineProps({
    show: {
      type: String,
      default: ''
    },
    row: {
      type: Object,
      default: () => ({})
    },
    activityRow: {
      type: Object,
      default: () => ({})
    }
  });

  const closeDia = (isRefresh = false) => {
    emit('closed', isRefresh);
  };

  const isEdit = ref(false);

  const ruleForm = reactive({
    noteContent: ''
  });

  // 获取回显信息
  const getEchoInfo = () => {
    isEdit.value = true;
    ruleForm.noteContent = props.row?.noteContent ?? '';
  };

  const dialogVisible = computed({
    get() {
      const show = Boolean(props.show);
      const isAdd = proxy.$utils.isEmptyObject(props.row);

      if(!isAdd) {
        getEchoInfo();
      }
      return show;
    },
    set(val) {
      if(!val) {
        closeDia();
      }
    }
  });

  const rules = reactive({
    noteContent: [{ required: true, message: '请填写活动备注', trigger: 'blur' }]
  });

  const ruleFormRef = ref();
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
        activityId: props.activityRow.id,
        noteContent: ruleForm.noteContent
      };

      const res = isEdit.value ? await updateActivitieRemark({
        id: props.row.id,
        ...param
      }) : await addActivitieRemark(param);

      if(res.code === 200) {
        closeDia(true);
      }
    });
  };


</script>

<style lang="scss" scoped>

</style>