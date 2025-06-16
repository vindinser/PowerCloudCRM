import modal from './modal';
import * as cryptoUtils from './cryptoUtils';

export default function installPlugins(app) {
  // 模态框对象
  app.config.globalProperties.$modal = modal;
  // 加密方法
  app.config.globalProperties.$crypto = cryptoUtils;
}
