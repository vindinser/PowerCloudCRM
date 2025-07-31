<!-- 线索转换客户 -->
<template>
  <div>
    <el-dialog v-model="dialogVisible" title="线索转换客户" width="500" append-to-body>
      <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="auto" style="width: 75%;">
        <el-form-item label="意向产品" prop="product">
          <el-select v-model="ruleForm.product" placeholder="请选择意向产品" clearable>
            <el-option v-for="item in productOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="下次跟踪时间" prop="nextContactTime">
          <el-date-picker
            v-model="ruleForm.nextContactTime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择下次跟踪时间"
            type="datetime"
            align="right"
            style="width: 100%;"
            clearable
          />
        </el-form-item>
        <el-form-item label="客户描述" prop="noteContent">
          <el-input v-model="ruleForm.description" :autosize="{ minRows: 2, maxRows: 6 }" type="textarea"  placeholder="请填写客户描述" />
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

<script setup name="TransferCustomer">
  import { loadDicValue, transferCustomer } from '@/api/clue.js';

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
    }
  });

  const closeDia = (isRefresh = false) => {
    emit('closed', isRefresh);
  };

  const ruleForm = reactive({
    product: '',
    description: '',
    nextContactTime: ''
  });

  const dialogVisible = computed({
    get() {
      return Boolean(props.show);
    },
    set(val) {
      if(!val) {
        closeDia();
      }
    }
  });

  const productOptions = ref([]);

  (() => {
    loadDicValue('product').then(res => {
      if(res.code === 200) {
        productOptions.value = res.ListData;
      } else {
        productOptions.value = [];
      }
    }).catch(() => {
      productOptions.value = [];
    });
  })();

  const rules = reactive({
    product: [{ required: true, message: '请选择意向产品', trigger: 'change' }],
    nextContactTime: [{ required: true, message: '请选择下次跟踪时间', trigger: 'change' }],
    description: [{ required: true, message: '客户描述', trigger: 'blur' }]
  });

  const ruleFormRef = ref();
  // 确认转客户
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

      const res = await transferCustomer({
        clueId: props.row.id,
        product: ruleForm.product,
        nextContactTime: ruleForm.nextContactTime,
        description: ruleForm.description
      });

      if(res.code === 200) {
        closeDia(true);
      }
    });
  };


</script>

<style lang="scss" scoped>

</style>