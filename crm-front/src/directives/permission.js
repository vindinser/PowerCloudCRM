// src/directives/permission.js
/**
 * 权限指令
 */
/**
 * 检查是否存在指令
 * @param {Element} el 绑定自定义指令元素
 * @param {Object} binding 绑定自定义指令值
 * @param {pinia} pinia pinina
 */
function checkPermission(el, binding, pinia) {

  // 获取当前权限列表
  const permissionList = pinia.state.value?.user?.userInfo?.permissionList || [];

  // 支持数组形式的权限值（任意一个匹配即可）
  const requiredPermissions = Array.isArray(binding.value) ? binding.value : [binding.value];

  // 检查是否具备所需权限
  const hasPermission = requiredPermissions.some(permission =>
    permissionList.includes(permission)
  );

  // 应用权限控制
  if (!hasPermission) {
    // 更安全的隐藏方式 - 保留占位空间
    el.style.visibility = 'hidden';
    el.style.position = 'absolute';
    el.style.pointerEvents = 'none';

    // 添加特定类名，通过CSS控制
    el.classList.add('permission-denied');
  } else {
    el.style.visibility = '';
    el.style.position = '';
    el.style.pointerEvents = '';
    el.classList.remove('permission-denied');
  }
}

export const hasPermission = (pinia) => {
  return {
    mounted(el, binding) {
      checkPermission(el, binding, pinia);
    },
    updated(el, binding) {
      checkPermission(el, binding, pinia);
    }
  };
};