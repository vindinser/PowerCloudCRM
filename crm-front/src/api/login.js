import http from '@/core/http.js';

export function login(params) {
  return http.post('/api/login', params, {
    operaName: '登录'
  });
}
// export default {
//   // 普通请求
//   login: (params) => http.post('/api/login', params, {
//     operaName: '登录'
//   })

//   // 加密请求
//   // login: (data) => http.post('/auth/login', data, {
//   //   encrypt: true,
//   //   encryptFields: ['password'],
//   //   showSuccess: false
//   // })
// };