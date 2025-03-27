import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import AutoImportEl from 'unplugin-auto-import/vite'
import ComponentsEl from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

const createAutoImport = () =>
  AutoImport({
    imports: ['vue', 'vue-router', 'pinia'],
    dts: false
  })

const createAutoImportComponents = () =>
  Components({
    resolvers: [ElementPlusResolver()]
  })

const createAutoImportEl = () =>
  AutoImportEl({
    resolvers: [ElementPlusResolver()]
  })

const createAutoImportComponentsEl = () =>
  ComponentsEl({
    resolvers: [ElementPlusResolver({ ssr: false, directives: false })],
    dirs: ['src/components'],
    dts: true
  })

export {
  createAutoImport,
  createAutoImportComponents,
  createAutoImportEl,
  createAutoImportComponentsEl
}
