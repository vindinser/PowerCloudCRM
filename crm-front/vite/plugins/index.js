import vue from '@vitejs/plugin-vue';

import { createAutoImport, createAutoImportComponents, createAutoImportEl, createAutoImportComponentsEl } from './auto-import';
// import createRefOmit from './ref-omit';

export default function createVitePlugins(viteEnv, isBuild = false) {
  const vitePlugins = [vue()];
  vitePlugins.push(createAutoImport());
  vitePlugins.push(createAutoImportComponents());
  vitePlugins.push(createAutoImportEl());
  vitePlugins.push(createAutoImportComponentsEl());
  // vitePlugins.push(createRefOmit());
  // isBuild && vitePlugins.push(...createCompression(viteEnv));
  return vitePlugins;
}