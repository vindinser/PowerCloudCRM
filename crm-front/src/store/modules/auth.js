import { encrypt, decrypt, generateKey } from '@/plugins/cryptoUtils.js';

const useAuthStore = defineStore('auth', {
  state: () => ({
    encryptionKey: null,
    username: '',
    password: ''
  }),
  // 启用持久化
  persist: true,
  actions: {
    // 生成加密密钥
    generateEncryptionKey() {
      this.encryptionKey = generateKey();
    },
    // 保存登录名、登录密码
    remember({ loginAct, loginPwd }) {
      this.clearSecurityData();
      this.generateEncryptionKey();
      this.username = loginAct;
      this.password = this.encryptPassword(loginPwd);
    },
    // 获取用户名密码
    getLoginInfo() {
      return {
        loginAct: this.username,
        loginPwd: this.decryptPassword()
      };
    },

    // 加密密码
    encryptPassword(password) {
      return encrypt(password, this.encryptionKey);
    },

    // 解密密码
    decryptPassword(encryptedPassword) {
      const password = encryptedPassword || this.password;

      return decrypt(password, this.encryptionKey);
    },

    // 清除所有数据
    clearSecurityData() {
      this.encryptionKey = null;
      this.username = '';
      this.password = '';
    }
  }
});

export default useAuthStore;