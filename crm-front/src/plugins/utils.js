export default {
  // 端端对象是否为空
  isEmptyObject(value) {
    if (value === null) {return true}
    if (typeof value !== 'object') {return false}
    if (Array.isArray(value)) {return value.length === 0}
    return Object.keys(value).length === 0;
  }
};