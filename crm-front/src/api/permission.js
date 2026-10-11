import http from '@/core/http.js';

/**
 * 获取菜单树（一级菜单 + 子菜单/按钮，用于权限分配）
 * @param {Object} [data] - 查询参数
 * @returns {Promise<{code: number, msg: string, ListData: Array}>}
 */
export const getMenuTree = (data) => {
  return http.get('/api/permissions/menuTree', data, {
    operaName: '获取菜单树',
    showMsg: false
  });
};

/**
 * 获取所有权限（菜单 + 按钮）
 * @param {Object} [data] - 查询参数
 * @returns {Promise<{code: number, msg: string, ListData: Array}>}
 */
export const getAllPermissions = (data) => {
  return http.get('/api/permissions', data, {
    operaName: '获取所有权限',
    showMsg: false
  });
};

/**
 * 获取权限信息
 * @param {number} id - 权限ID
 * @returns {Promise<{code: number, msg: string, data: Object}>}
 */
export const getPermission = (id) => {
  return http.get(`/api/permission/${id}`, null, {
    operaName: '获取权限信息'
  });
};

/**
 * 新增权限
 * @param {Object} data - 权限表单
 * @returns {Promise<{code: number, msg: string}>}
 */
export const addPermission = (data) => {
  return http.post('/api/permission/add', data, {
    operaName: '新增权限'
  });
};

/**
 * 修改权限信息
 * @param {Object} data - 权限表单（含 id）
 * @returns {Promise<{code: number, msg: string}>}
 */
export const updatePermission = (data) => {
  return http.put('/api/permission/update', data, {
    operaName: '修改权限信息'
  });
};

/**
 * 删除权限
 * @param {number} id - 权限ID
 * @returns {Promise<{code: number, msg: string}>}
 */
export const delPermission = (id) => {
  return http.delete(`/api/permission/${id}`, null, {
    operaName: '删除权限'
  });
};
