/**
 * 线索管理相关
 */
import http from '@/core/http.js';

export const getClue = (data) => {
  return http.get('api/clues', data, {
    operaName: '获取线索管理列表'
  });
};

export const checkPhone = (phone) => {
  return http.get(`api/clues/${ phone }`, null, {
    showMsg: false
  });
};

export const loadDicValue = (type) => {
  return http.get(`api/dicvalue/${ type }`, null, {
    loading: false,
    showMsg: false
  });
};

export const addClue = (data) => {
  return http.post('api/clues', data, {
    operaName: '录入线索'
  });
};

export const updateClue = (data) => {
  return http.put('api/clues', data, {
    operaName: '修改线索活动信息'
  });
};

export const getClueDetail = (id) => {
  return http.get(`api/clues/detail/${ id }`, null, {
    operaName: '获取线索信息'
  });
};

export const delClue = (id) => {
  return http.delete(`api/clues/${ id }`, null, {
    operaName: '删除线索'
  });
};

export const batchDelClues = (data) => {
  return http.delete('api/clues', data, {
    operaName: '删除活动'
  });
};