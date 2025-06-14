import { createApp } from 'vue';
// import './style.css';
import App from './App.vue';
// import ElementPlus from 'element-plus';
// import 'element-plus/dist/index.css';
import VueParticles from 'particles.vue3';
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
// import './style.scss';

//从router.js中导入router组件
import router from './router';

import store from './store';

// 注册指令
import plugins from './plugins'; // plugins

const app = createApp(App);

app.use(store);
app.use(plugins);

// app.use(ElementPlus);
// 注册所有图标（配置自动导入但未生效！！！）
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.use(VueParticles).use(router).mount('#app');
