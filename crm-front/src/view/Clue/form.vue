<!-- 录入/修改 线索 -->
<template>
  <ZSDetail isFoot width="50%" :title="`${ isEdit ? '修改' : '录入' }线索`" @closed="close()">
    <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="108px" style="width: 75%;">
      <el-form-item v-for="item, index in descriptions" :key="index" :label="item.label" :prop="item.value">
        <el-select
          v-if="item.type === 'select'"
          v-model="ruleForm[item.value]"
          :placeholder="`请选择${ item.label }`"
          clearable
          filterable
          :disabled="item.value == 'ownerId' && !isAdmin"
        >
          <el-option v-for="item, index in dictCondition[item.value]" :key="index" :label="item.label" :value="item.value" />
        </el-select>
        <el-date-picker
          v-else-if="item.type === 'time'"
          v-model="ruleForm[item.value]"
          value-format="YYYY-MM-DD HH:mm:ss"
          :placeholder="`请选择${ item.label }`"
          type="datetime"
          align="right"
          style="width: 100%;"
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

<script setup name="ClueForm">
  import ZSDetail from '@/components/ZSDetail';
  import { addClue, checkPhone, updateClue, loadDicValue } from '@/api/clue.js';
  import useUserStore from '@/store/modules/user';

  const userStore = useUserStore();

  const { proxy } = getCurrentInstance();
  const emit = defineEmits(['closed']);
  const { row, ownerList } = defineProps({
    row: {
      type: Object,
      default: () => ({})
    },
    ownerList: {
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
    { label: '所属活动', value: 'activityId', type: 'select' },
    { label: '姓名', value: 'fullName' },
    { label: '称呼', value: 'appellation', type: 'select' },
    { label: '手机', value: 'phone' },
    { label: '微信', value: 'weixin' },
    { label: 'QQ', value: 'qq' },
    { label: '邮箱', value: 'email' },
    { label: '年龄', value: 'age' },
    { label: '职业', value: 'job' },
    { label: '年收入', value: 'yearIncome' },
    { label: '住址', value: 'address' },
    { label: '贷款', value: 'needLoan', type: 'select' },
    { label: '意向状态', value: 'intentionState', type: 'select' },
    { label: '意向产品', value: 'intentionProduct', type: 'select' },
    { label: '线索状态', value: 'state', type: 'select' },
    { label: '线索来源', value: 'source', type: 'select' },
    { label: '下次联系时间', value: 'nextContactTime', type: 'time' },
    { label: '线索描述', value: 'description' }
  ]);

  const ruleForm = ref({});
  const rules = ref({});

  const phoneValidator = (rule, value, callback) => {
    if (value) {
      checkPhone(value).then(() => {
        return callback();
      }).catch(() => {
        return callback(new Error('该手机号录入过了，不能再录入.'));
      });
    }
  };
  const regRules = {
    phone: [
      { required: true, message: '请输入手机号码', trigger: 'blur' },
      { pattern : /^1[3-9]\d{9}$/, message: '手机号码格式有误', trigger: 'blur'},
      { validator : phoneValidator, trigger: 'blur'}
    ],
    fullName: [
      { min: 2, message: '姓名至少2个汉字', trigger: 'blur'},
      { pattern : /^[\u4e00-\u9fa5]{0,}$/, message: '姓名必须为中文汉字', trigger: 'blur'}
    ],
    qq: [
      { min: 5, message: 'QQ号至少为5位', trigger: 'blur'},
      { pattern : /^\d+$/, message: 'QQ号码必须为数字', trigger: 'blur'}
    ],
    email: [
      { pattern : /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '邮箱格式有误', trigger: 'blur'}
    ],
    age: [
      { pattern : /^\d+$/, message: '年龄必须为数字', trigger: 'blur'}
    ],
    yearIncome: [
      { pattern : /^[0-9]+(\.[0-9]{2})?$/, message: '年收入必须是整数或者两位小数', trigger: 'blur'}
    ],
    description: [
      { min: 5, max: 255, message: '线索描述长度为5-255个字符', trigger: 'blur'}
    ]
  };

  ((arr) => {
    for(let k in descriptions.value) {
      const { label, value, type } = descriptions.value[k];

      ruleForm.value[value] = '';
      if(arr.includes(value)) {
        rules.value[value] = [
          { required: true, message: `请${ type === 'input' ? '输入' : '选择' }${ label }`, trigger: 'blur' },
          ...regRules[value]
        ];
      }
    }
  })(['phone', 'fullName', 'qq', 'email', 'age', 'yearIncome', 'description']);

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

      const res = isEdit.value ? await updateClue({
        id: row.id,
        ...ruleForm.value
      }) : await addClue(ruleForm.value);

      if(res.code === 200) {
        close(true);
      }
    });
  };

  const dictCondition = reactive({});

  // 获取搜索条件
  ((arr) => {
    dictCondition.ownerId = ownerList;
    for(let type of arr) {
      let key = type;

      if(type === 'product') {
        key = 'intentionProduct';
      } else if(type === 'clueState') {
        key = 'state';
      } else if(type === 'activity') {
        key = 'activityId';
      }
      loadDicValue(type).then(res => {

        if(res.code === 200) {
          dictCondition[key] = res.ListData.map(item => ({
            value: item.id,
            label: item.typeValue || item.name
          }));
        } else {
          dictCondition[key] = [];
        }
      }).catch(() => {
        dictCondition[key] = [];
      });
    }
  })(['appellation', 'needLoan', 'intentionState', 'clueState', 'source', 'activity', 'product']);

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

  const isAdmin = ref(false);

  onMounted(() => {
    const isAdd = proxy.$utils.isEmptyObject(row);

    const { id, roleList } = userStore.userInfo;

    isAdmin.value = roleList.includes('admin');

    if(isAdd) {
      ruleForm.value.ownerId = id;
    } else {
      isEdit.value = true;
      getEchoInfo();
      rules.value.phone.splice(3, 1);
    }
  });

</script>

<style lang="scss" scoped>

</style>