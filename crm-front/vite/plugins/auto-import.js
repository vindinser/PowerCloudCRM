import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import AutoImportEl from 'unplugin-auto-import/vite';
import ComponentsEl from 'unplugin-vue-components/vite';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';

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

const createAutoImportEl = () =>
  AutoImportEl({
    resolvers: [ElementPlusResolver()],
    eslintrc: {
      enabled: true, // 生成 ESLint 配置
      filepath: './.eslintrc-auto-import.json',
      globalsPropValue: true
    }
  });

const createAutoImportComponentsEl = () =>
  ComponentsEl({
    resolvers: [ElementPlusResolver({ ssr: false, directives: false })],
    dirs: ['src/components'],
    dts: true
  });

export {
  createAutoImport,
  createAutoImportComponents,
  createAutoImportEl,
  createAutoImportComponentsEl
};
