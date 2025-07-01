<!-- 新增用户 -->
<template>
  <ZSDetail isFoot width="50%" title="新增用户" @closed="close()">
    <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="auto" style="width: 75%;">
      <el-form-item v-for="item, index in descriptions" :key="index" :label="item.label" :prop="item.value">
        <el-radio-group v-if="item.type === 'radio'" v-model="ruleForm[item.value]">
          <el-radio :value="1">是</el-radio>
          <el-radio :value="0">否</el-radio>
        </el-radio-group>
        <el-input v-else v-model="ruleForm[item.value]" :placeholder="`请输入${ item.label }`" clearable />
      </el-form-item>
    </el-form>
    <template #foot>
      <el-button type="primary" @click="onSubmit">确 认</el-button>
      <el-button @click="close()">关 闭</el-button>
    </template>
  </ZSDetail>
</template>

<script setup name="UserForm">
  import ZSDetail from '@/components/ZSDetail';
  import { addUser } from '@/api/user.js';

  const { proxy } = getCurrentInstance();
  const emit = defineEmits(['closed']);

  /**
   * 关闭页面
   * @param {boolean} isRefresh 是否刷新用户列表
   */
  const close = (isRefresh = false) => {
    emit('closed', isRefresh);
  };

  const descriptions = [
    { label: '账号', value: 'loginAct' },
    { label: '姓名', value: 'name' },
    { label: '密码', value: 'loginPwd' },
    { label: '手机号', value: 'phone' },
    { label: '邮箱', value: 'email' },
    { label: '账号未过期', value: 'accountNoExpired', type: 'radio' },
    { label: '密码未过期', value: 'credentialsNoExpired', type: 'radio' },
    { label: '账号未锁定', value: 'accountNoLocked', type: 'radio' },
    { label: '账号是否启用', value: 'accountEnabled', type: 'radio' }
  ];

  const ruleForm = ref({});
  const rules = ref({});

  const loginPwdValidator = (rule, value, callback) => {
    if (/[\u4e00-\u9fa5]/.test(value)) {
      // eslint-disable-next-line callback-return
      callback(new Error('密码不能包含中文'));
    } else {
      // eslint-disable-next-line callback-return
      callback();
    }
  };
  const regRules = {
    loginAct: [
      { min: 1, max: 20, message: '账号最多20个字符', trigger: 'blur' },
      { pattern: /^[a-zA-Z0-9_]+$/, message: '账号只能包含数字、字母、下划线', trigger: 'blur' }
    ],
    name: [
      { min: 1, max: 10, message: '姓名长度需在1到10个汉字之间', trigger: 'blur' },
      { pattern: /^[\u4e00-\u9fa5]+$/, message: '姓名必须为中文', trigger: 'blur' }
    ],
    loginPwd: [
      { min: 6, max: 20, message: '密码长度需在6到20个字符之间', trigger: 'blur' },
      { validator: loginPwdValidator, trigger: 'blur' }
    ],
    phone: [
      { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号码', trigger: 'blur' }
    ],
    email: [
      { type: 'email', message: '请输入有效的邮箱地址', trigger: ['blur', 'change'] }
    ]
  };

  // 初始化表单字段
  const initForm = () => {
    for(let k in descriptions) {
      const { label, value, type } = descriptions[k];

      if(type === 'radio') {
        ruleForm.value[value] = 1;
        rules.value[value] = [{ required: true, message: `请选择${ label }`, trigger: 'change' }];
      } else {
        ruleForm.value[value] = '';
        rules.value[value] = [
          { required: true, message: `请输入${ label }`, trigger: 'blur' },
          ...regRules[value]
        ];
      }
    }
  };

  initForm();

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
      const res = await addUser(ruleForm.value);

      if(res.code === 200) {
        close(true);
      }
    });
  };

</script>

<style lang="scss" scoped>

</style>