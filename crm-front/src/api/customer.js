/**
 * 客户管理相关
 */
import http from '@/core/http.js';

export const getCustomers = (data) => {
  return http.get('api/customer', data, {
    operaName: '获取客户管理列表'
  });
};

export const delCustomer = (id) => {
  return http.delete(`api/customer/${id}`, null, {
    operaName: '删除客户'
  });
};

export const batchDelCustomer = (data) => {
  return http.delete('api/customer', data, {
    operaName: '删除客户'
  });
};