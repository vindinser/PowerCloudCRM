<!-- 新增/编辑 跟踪记录 -->
<template>
  <div>
    <el-dialog v-model="dialogVisible" title="跟踪记录" width="500">
      <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="auto" style="width: 75%;">
        <el-form-item label="跟踪方式" prop="noteWay">
          <el-select v-model="ruleForm.noteWay" placeholder="请选择跟踪方式" clearable>
            <el-option v-for="item in noteWayOptions" :key="item.id" :label="item.typeValue" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="跟踪记录" prop="noteContent">
          <el-input v-model="ruleForm.noteContent" :autosize="{ minRows: 2, maxRows: 6 }" type="textarea"  placeholder="请填写跟踪记录" />
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

<script setup name="ClueTrackRecordForm">
  import { loadDicValue, addClueTrackRecord, updateClueTrackRecord } from '@/api/clue.js';

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
    clueRow: {
      type: Object,
      default: () => ({})
    }
  });

  const closeDia = (isRefresh = false) => {
    emit('closed', isRefresh);
  };

  const isEdit = ref(false);

  const ruleForm = reactive({
    noteWay: '',
    noteContent: ''
  });

  // 获取回显信息
  const getEchoInfo = () => {
    isEdit.value = true;
    ruleForm.noteContent = props.row?.noteContent ?? '';
    ruleForm.noteWay = props.row?.noteWay ?? '';
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

  const noteWayOptions = ref([]);

  (() => {
    loadDicValue('noteWay').then(res => {
      if(res.code === 200) {
        noteWayOptions.value = res.ListData;
      } else {
        noteWayOptions.value = [];
      }
    }).catch(() => {
      noteWayOptions.value = [];
    });
  })();

  const rules = reactive({
    noteWay: [{ required: true, message: '请选择跟踪方式', trigger: 'change' }],
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
        clueId: props.clueRow.id,
        noteWay: ruleForm.noteWay,
        noteContent: ruleForm.noteContent
      };

      const res = isEdit.value ? await updateClueTrackRecord({
        id: props.row.id,
        ...param
      }) : await addClueTrackRecord(param);

      if(res.code === 200) {
        closeDia(true);
      }
    });
  };


</script>

<style lang="scss" scoped>

</style>