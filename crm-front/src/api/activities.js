/**
 * 市场活动相关
 */
import http from '@/core/http.js';

export const getActivities = (data) => {
  return http.get('api/activities', data, {
    operaName: '获取用户管理列表'
  });
};

export const addActivities = (data) => {
  return http.post('api/activities/add', data, {
    operaName: '录入市场活动'
  });
};

export const updateActivities = (data) => {
  return http.put('api/activities/update', data, {
    operaName: '修改市场活动信息'
  });
};

export const getActivity = (id) => {
  return http.get(`api/activities/${ id }`, null, {
    operaName: '获取市场活动信息'
  });
};

// 删除单个活动
export const delActivity = (id) => {
  return http.delete(`api/activities/${ id }`, null, {
    operaName: '删除活动'
  });
};

// 批量删除活动
export const batchDelActivities = (data) => {
  return http.delete('api/activities/del', data, {
    operaName: '删除活动'
  });
};