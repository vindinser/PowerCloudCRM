// CryptoJs的加密、解密方案
import CryptoJS from 'crypto-js';

// 主密钥
const SECRET_KEY = import.meta.env.VITE_CRYPT_SECRET;

// 加密
export const encrypt = (data, key) => {
  const secretKey = key ?? SECRET_KEY;

  return CryptoJS.AES.encrypt(data, secretKey).toString();
};

// 解密
export const decrypt = (ciphertext, key) => {
  const secretKey = key ?? SECRET_KEY;

  const bytes = CryptoJS.AES.decrypt(ciphertext, secretKey);

  return bytes.toString(CryptoJS.enc.Utf8);
};

// 生成秘钥
export const generateKey = () => {
  const array = new Uint8Array(16);

  crypto.getRandomValues(array);
  return Array.from(array).map(b => b.toString(16).padStart(2, '0')).join('');
};