import http from '@/core/http.js';

export const getUsers = (data) => {
  return http.get('/api/users', data, {
    operaName: '获取用户管理列表'
  });
};