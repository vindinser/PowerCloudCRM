import modal from './modal';
import * as cryptoUtils from './cryptoUtils';
import utils from './utils';

export default function installPlugins(app) {
  // 模态框对象
  app.config.globalProperties.$modal = modal;
  // 加密方法
  app.config.globalProperties.$crypto = cryptoUtils;
  // 常用方法
  app.config.globalProperties.$utils = utils;

}
