import { defineConfig, loadEnv } from 'vite'
import path from 'path'
import createVitePlugins from './vite/plugins'

// https://vite.dev/config/
export default defineConfig(({ mode, command }) => {
  const env = loadEnv(mode, process.cwd());
  const { VITE_APP_ENV } = env;
  const isProd = VITE_APP_ENV === 'production';

  return {
    base: isProd ? '/' : '/',
    plugins: createVitePlugins(env, command === 'build'),
    resolve: {
      // https://cn.vitejs.dev/config/#resolve-alias
      alias: {
        // 设置路径
        '~': path.resolve(__dirname, './'),
        // 设置别名
        '@': path.resolve(__dirname, './src')
      },
      // https://cn.vitejs.dev/config/#resolve-extensions
      extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
    },
    server: {
      host: '0.0.0.0', //ip地址
      port: 9527, // 设置服务启动端口号
      open: true, // 设置服务启动时是否自动打开浏览器
      proxy: {
        // https://cn.vitejs.dev/config/#server-proxy
        // 开发环境代理
        '/dev-api': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          rewrite: (p) => p.replace(/^\/dev-api/, '')
        },
        '/stage-api': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          rewrite: (p) => p.replace(/^\/stage-api/, '')
        },
        '/prod-api': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          rewrite: (p) => p.replace(/^\/prod-api/, '')
        }
      }
    },
    // 打包去掉console.log输出
    build: {
      minify: 'terser',
      terserOptions: {
        compress: {
          // drop_console: command === 'build' && isProd,
          drop_console: false,
          pure_funcs: ['console.log'],
          drop_debugger: command === 'build' && isProd
        }
      }
    }
  }
})
