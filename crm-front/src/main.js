import { createApp } from 'vue';
// import './style.css';
import App from './App.vue';
// import ElementPlus from 'element-plus';
// import 'element-plus/dist/index.css';
import VueParticles from 'particles.vue3';
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import '@/assets/styles/index.scss'; // global css

//从router.js中导入router组件
import router from './router';

import store from './store';

// 注册指令
import plugins from './plugins'; // plugins

import 'viewerjs/dist/viewer.css'; // 引入样式
import VueViewer from 'v-viewer';

// 导入指令注册函数
import { registerDirectives } from '@/directives';

const app = createApp(App);

app.use(store);
app.use(plugins);

// 注册自定义指令
registerDirectives(app, store);

// app.use(ElementPlus);
// 注册所有图标（配置自动导入但未生效！！！）
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.use(VueViewer, {
  defaultOptions: {
    // 自定义默认配置
    'zIndex': 9999 // 提升层级，否则会被dialog覆盖
    // 'url': 'data-source', // 设置大图片的 url

    // 'inline': false, // 是否启用inline模式
    // 'button': true, // 是否显示右上角关闭按钮
    // 'navbar': true, // 是否显示缩略图底部导航栏
    // 'title': true, // 是否显示当前图片标题，默认显示alt属性内容和尺寸
    // 'toolbar': true, // 是否显示工具栏
    // 'tooltip': true, // 放大或缩小图片时，是否显示缩放百分比，默认true
    // 'fullscreen': true, // 播放时是否全屏，默认true
    // 'loading': true, // 加载图片时是否显示loading图标，默认true
    // 'loop': true, // 是否可以循环查看图片，默认true
    // 'movable': true, // 是否可以拖得图片，默认true
    // 'zoomable': true, // 是否可以缩放图片，默认true
    // 'rotatable': true, // 是否可以旋转图片，默认true
    // 'scalable': true, // 是否可以翻转图片，默认true
    // 'toggleOnDblclick': true, // 放大或缩小图片时，是否可以双击还原，默认true
    // 'transition': true, // 使用 CSS3 过度，默认true
    // 'keyboard': true, // 是否支持键盘，默认true
    // 'zoomRatio': 0.1, // 鼠标滚动时的缩放比例，默认0.1
    // 'minZoomRatio': 0.01, // 最小缩放比例，默认0.01
    // 'maxZoomRatio': 100 // 最大缩放比例，默认100
  }
});

app.use(VueParticles).use(router).mount('#app');
