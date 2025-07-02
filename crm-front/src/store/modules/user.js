import { login, getUserInfo, freeLogin, logout } from '@/api/login.js';
import useAuthStore from './auth.js';
import modal from '@/plugins/modal.js';

const authStore = useAuthStore();

const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: {},
    rememberLogin: false,
    isCollapse: false // 控制左侧菜单左右的展开和折叠，true是折叠，false是展开
  }),
  // 启用持久化
  persist: true,
  actions: {
    // 登录
    login(ruleForm) {
      return new Promise((resolve, rej) => {
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
            this.rememberLogin = rememberLogin;
            try {
              const { data } = await getUserInfo();

              this.userInfo = data;
            } catch(e) {
              this.rememberLogin = false;
              rej(e);
            }

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
      logout().then(res => {
        if (res.code === 200) {
          this.removeToken();
          //跳到登录页
          window.location.href = '/';
        } else {
          this.onlyLogOut('退出异常，是否要强制退出？');
        }
      });
    },
    // 前端退出系统
    onlyLogOut(content, showCancelButton = true) {
      modal.confirm(content, { showCancelButton }).then(() => {
        // 既然后端验证token未通过，那么前端的token肯定是有问题的，那没必要存储在浏览器中，直接删除一下
        this.removeToken();
        // 跳到登录页
        window.location.href = '/';
      }).catch(() => {
        modal.msg('取消强制退出');
      });
    },
    // 清除token、user信息等
    removeToken() {
      this.userInfo = {};
      this.token = '';
      this.rememberLogin = false;
      authStore.clearSecurityData();
    },
    // 左侧菜单左右展开和折叠
    showMenu() {
      this.isCollapse = !this.isCollapse;
    }
  }
});

export default useUserStore;
