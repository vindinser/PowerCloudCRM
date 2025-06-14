import { login, getUserInfo } from '@/api/login.js';

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
      return new Promise((resolve) => {
        let formData = new FormData();

        formData.append('loginAct', ruleForm.loginAct.trim());
        formData.append('loginPwd', ruleForm.loginPwd.trim());
        formData.append('rememberMe', true);
        login(formData).then(async (res) => {
          if(res.code === 200) {
            this.token = res.data;
            const { data } = await getUserInfo();

            this.userInfo = data;
            resolve(res);
          } else {
            this.token = '';
          }
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
