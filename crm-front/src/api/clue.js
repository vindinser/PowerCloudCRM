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
    operaName: '删除线索'
  });
};

export const importClues = (data) => {
  return http.post('api/clues/importExcel', data, {
    operaName: '导入线索'
  });
};

export const getClueTrackRecords = (data) => {
  return http.get('api/clues/record', data, {
    operaName: '获取线索跟踪记录'
  });
};

export const addClueTrackRecord = (data) => {
  return http.post('api/clues/record', data, {
    operaName: '新增线索跟踪记录'
  });
};

export const updateClueTrackRecord = (data) => {
  return http.put('api/clues/record', data, {
    operaName: '修改线索跟踪记录'
  });
};

// 删除线索跟踪记录
export const delClueTrackRecord = (id) => {
  return http.delete(`api/clues/record/${ id }`, null, {
    operaName: '删除线索跟踪记录'
  });
};

// 批量删除线索跟踪记录
export const batchDelClueTrackRecords = (data) => {
  return http.delete('api/clues/record', data, {
    operaName: '删除线索跟踪记录'
  });
};

export const transferCustomer = (data) => {
  return http.post('api/clue/customer', data, {
    operaName: '线索转客户'
  });
};