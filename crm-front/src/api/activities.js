/**
 * 市场活动相关
 */
import http from '@/core/http.js';

export const getActivities = (data) => {
  return http.get('api/activities', data, {
    operaName: '获取用户管理列表'
  });
};