import { createApp } from 'vue';
// import './style.css';
import App from './App.vue';
// import ElementPlus from 'element-plus';
// import 'element-plus/dist/index.css';
import VueParticles from 'particles.vue3';

//从router.js中导入router组件
import router from './router'

const app = createApp(App);

// app.use(ElementPlus);
app.use(VueParticles).use(router).mount('#app');
