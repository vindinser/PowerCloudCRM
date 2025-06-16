import { login, getUserInfo, freeLogin } from '@/api/login.js';
import useAuthStore from './auth.js';

const authStore = useAuthStore();

const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: {},
    rememberLogin: false
  }),
  // 启用持久化
  persist: true,
  actions: {
    // 登录
    login(ruleForm) {
      return new Promise((resolve) => {
        let formData = new FormData();

        const rememberLogin = ruleForm.rememberLogin,
          loginAct = ruleForm.loginAct.trim(),
          loginPwd = ruleForm.loginPwd.trim();

        formData.append('loginAct', loginAct);
        formData.append('loginPwd', loginPwd);
        formData.append('rememberMe', rememberLogin);
        login(formData).then(async (res) => {
          if(res.code === 200) {
            this.token = res.data;
            const { data } = await getUserInfo();

            this.rememberLogin = rememberLogin;
            this.userInfo = data;
            if(rememberLogin) {
              authStore.remember({ loginAct, loginPwd });
            }
            resolve(res);
          } else {
            this.token = '';
          }
        });
      });
    },
    // 获取登录信息
    getLoginInfo() {
      const rememberLogin = this.rememberLogin;
      let loginInfo = {
        rememberLogin
      };

      if(rememberLogin) {
        const { loginAct, loginPwd } = authStore.getLoginInfo();

        loginInfo = {
          ...loginInfo,
          loginAct,
          loginPwd
        };
      }

      return loginInfo;
    },
    // 用户免登录
    freeLogin() {
      return new Promise(async (resolve) => {
        await freeLogin();
        resolve();
      });
    },
    // 退出系统
    logOut() {
      authStore.clearSecurityData();
    }
  }
});

export default useUserStore;
