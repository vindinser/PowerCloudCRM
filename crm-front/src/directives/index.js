import { hasPermission } from './permission';

/**
 * 注册自定义指令
 * @param {*} app vue
 * @param {*} pinia pinina
 */
export function registerDirectives(app, pinia) {
  // 注册权限指令
  app.directive('hasPermission', hasPermission(pinia));
}