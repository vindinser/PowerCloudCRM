import { login } from '@/api/login.js';

const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: {}
  }),
  // 启用持久化
  persist: true,
  actions: {
    // 登录
    login(ruleForm) {
      let formData = new FormData();

      formData.append('loginAct', ruleForm.loginAct.trim());
      formData.append('loginPwd', ruleForm.loginPwd.trim());
      login(formData).then((res) => {
        this.userInfo = res.data;
        // 登录成功后的操作
        ElMessageBox.alert('开发中敬请期待！', '温馨提示', {
          confirmButtonText: 'OK'
        });
      });
    },
    // 退出系统
    logOut() {
      console.log('退出登录');
    }
  }
});

export default useUserStore;
