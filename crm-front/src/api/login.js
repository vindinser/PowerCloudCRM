import http from '@/core/http.js';

export function login(params) {
  return http.post('/api/login', params, {
    token: false,
    operaName: '登录'
  });
}

export function getUserInfo() {
  return http.get('/api/login/info', null, {
    operaName: '获取登录用户信息'
  });
}

export function freeLogin() {
  return http.get('/api/login/free', null, {
    operaName: '登录'
  });
}

export function logout() {
  return http.get('/api/logout', null, {
    operaName: '退出登录'
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