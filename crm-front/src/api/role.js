import http from '@/core/http.js';

/**
 * 分页查询角色列表
 * @param {Object} [data] - 查询参数（page/size/keyword）
 * @returns {Promise<{code: number, msg: string, data: Object, ListData: Array}>}
 */
export const getRolePage = (data) => {
  return http.get('/api/role/page', data, {
    operaName: '获取角色列表',
    showMsg: false
  });
};

/**
 * 获取所有角色（下拉选择用）
 * @param {Object} [data] - 查询参数
 * @returns {Promise<{code: number, msg: string, ListData: Array}>}
 */
export const getRoles = (data) => {
  return http.get('/api/roles', data, {
    operaName: '获取角色列表',
    showMsg: false
  });
};

/**
 * 获取角色信息
 * @param {number} id - 角色ID
 * @returns {Promise<{code: number, msg: string, data: Object}>}
 */
export const getRole = (id) => {
  return http.get(`/api/role/${id}`, null, {
    operaName: '获取角色信息'
  });
};

/**
 * 新增角色
 * @param {Object} data - 角色表单
 * @returns {Promise<{code: number, msg: string}>}
 */
export const addRole = (data) => {
  return http.post('/api/role/add', data, {
    operaName: '新增角色'
  });
};

/**
 * 修改角色信息
 * @param {Object} data - 角色表单（含 id）
 * @returns {Promise<{code: number, msg: string}>}
 */
export const updateRole = (data) => {
  return http.put('/api/role/update', data, {
    operaName: '修改角色信息'
  });
};

/**
 * 删除角色
 * @param {number} id - 角色ID
 * @returns {Promise<{code: number, msg: string}>}
 */
export const delRole = (id) => {
  return http.delete(`/api/role/${id}`, null, {
    operaName: '删除角色'
  });
};

/**
 * 批量删除角色
 * @param {Object} data - { ids: number[] }
 * @returns {Promise<{code: number, msg: string}>}
 */
export const batchDelRole = (data) => {
  return http.delete('/api/role/del', data, {
    operaName: '批量删除角色'
  });
};

/**
 * 获取角色已授权的权限ID列表
 * @param {number} id - 角色ID
 * @returns {Promise<{code: number, msg: string, ListData: Array}>}
 */
export const getRolePermissions = (id) => {
  return http.get(`/api/role/${id}/permissions`, null, {
    operaName: '获取角色权限',
    showMsg: false
  });
};

/**
 * 分配权限
 * @param {number} id - 角色ID
 * @param {Object} data - { permissionIds: number[] }
 * @returns {Promise<{code: number, msg: string}>}
 */
export const assignPermissions = (id, data) => {
  return http.post(`/api/role/${id}/permissions`, data, {
    operaName: '分配权限'
  });
};
