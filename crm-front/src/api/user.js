import http from '@/core/http.js';

export const getUsers = (data) => {
  return http.get('/api/users', data, {
    operaName: '获取用户管理列表'
  });
};

export const getUser = (id) => {
  return http.get(`api/user/${ id }`, null, {
    operaName: '获取用户信息'
  });
};