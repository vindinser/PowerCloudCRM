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

export const addUser = (data) => {
  return http.post('/api/user/add', data, {
    operaName: '新增用户'
  });
};

export const updateUser = (data) => {
  return http.put('/api/user/update', data, {
    operaName: '修改用户信息'
  });
};

// 删除单个用户
export const delUser = (id) => {
  return http.delete(`api/user/${ id }`, null, {
    operaName: '删除用户'
  });
};

// 批量删除用户
export const batchDelUser = (data) => {
  return http.delete('api/user/del', data, {
    operaName: '删除用户'
  });
};