import vue from '@vitejs/plugin-vue';

import {
  createAutoImport,
  createAutoImportComponents,
  autoInstallIcons,
  createAutoImportEl,
  createAutoImportComponentsEl
} from './auto-import';
// import createRefOmit from './ref-omit';
import { vitePluginStart, vitePluginBuild } from './bellswhistles';

export default function createVitePlugins(viteEnv, isBuild = false) {
  const vitePlugins = [vue()];

  vitePlugins.push(createAutoImport());
  vitePlugins.push(createAutoImportComponents());
  vitePlugins.push(autoInstallIcons());
  vitePlugins.push(createAutoImportEl());
  vitePlugins.push(createAutoImportComponentsEl());
  // vitePlugins.push(createRefOmit());
  // isBuild && vitePlugins.push(...createCompression(viteEnv));
  vitePlugins.push(vitePluginStart());
  vitePlugins.push(vitePluginBuild());
  return vitePlugins;
}
