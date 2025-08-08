/**
 * 统计相关
 */
import http from '@/core/http.js';

export const getSummary = () => {
  return http.get('api/summary/data', null, {
    operaName: '获取概览统计数据'
  });
};

export const getSaleFunnel = () => {
  return http.get('api/saleFunnel/data', null, {
    operaName: '获取销售统计数据'
  });
};

export const getSource = () => {
  return http.get('api/sourcePie/data', null, {
    operaName: '获取线索来源'
  });
};