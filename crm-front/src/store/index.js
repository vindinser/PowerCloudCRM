// 无需import createPinia 已 AutoImport
// import { createPinia } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';

const store = createPinia();

store.use(piniaPluginPersistedstate); // 注册持久化插件

export default store;