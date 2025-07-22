/**
 * 市场活动相关
 */
import http from '@/core/http.js';

export const getActivities = (data) => {
  return http.get('api/activities', data, {
    operaName: '获取市场活动列表'
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

export const getActivitieRemarks = (data) => {
  return http.get('api/activity/remark', data, {
    operaName: '获取市场活动备注'
  });
};

export const addActivitieRemark = (data) => {
  return http.post('api/activity/remark', data, {
    operaName: '新增市场活动备注'
  });
};

export const updateActivitieRemark = (data) => {
  return http.put('api/activity/remark', data, {
    operaName: '修改市场活动备注'
  });
};

// 删除活动单条备注
export const delActivityRemark = (id) => {
  return http.delete(`api/activities/remark/${ id }`, null, {
    operaName: '删除活动备注'
  });
};

// 批量删除活动备注
export const batchDelActivityRemarks = (data) => {
  return http.delete('api/activities/remark', data, {
    operaName: '删除活动备注'
  });
};