import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import AutoImportEl from 'unplugin-auto-import/vite';
import ComponentsEl from 'unplugin-vue-components/vite';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';
import Icons from 'unplugin-icons/vite';
import IconsResolver from 'unplugin-icons/resolver';

const createAutoImport = () =>
  AutoImport({
    imports: ['vue', 'vue-router', 'pinia'],
    // 生成 .eslintrc-auto-import.json 文件
    eslintrc: {
      enabled: true,
      filepath: './.eslintrc-auto-import.json',
      globalsPropValue: true
    },
    // 生成 dts 文件
    dts: './auto-imports.d.ts'
  });

const createAutoImportComponents = () =>
  Components({
    resolvers: [ElementPlusResolver()]
  });

const autoInstallIcons = () =>
  Icons({
    autoInstall: true
  });

const createAutoImportEl = () =>
  AutoImportEl({
    resolvers: [
      // 自动导入 Element Plus 相关函数，如：ElMessage, ElMessageBox... (带样式)
      ElementPlusResolver(),
      // 自动导入图标组件
      IconsResolver({
        prefix: 'Icon'
      })
    ],
    eslintrc: {
      enabled: true, // 生成 ESLint 配置
      filepath: './.eslintrc-auto-import.json',
      globalsPropValue: true
    }
  });

const createAutoImportComponentsEl = () =>
  ComponentsEl({
    resolvers: [
      // 自动导入 Element Plus 组件
      ElementPlusResolver({ ssr: false, directives: false }),
      // 自动注册图标组件
      IconsResolver({
        enabledCollections: ['ep']
      })
    ],
    dirs: ['src/components'],
    dts: true
  });

export {
  createAutoImport,
  createAutoImportComponents,
  autoInstallIcons,
  createAutoImportEl,
  createAutoImportComponentsEl
};
