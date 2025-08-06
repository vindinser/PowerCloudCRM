// 从vue-router这个依赖库中导入createRouter()函数, createWebHistory()函数
import { createRouter, createWebHistory } from 'vue-router';

let router = createRouter({
  // 路由历史
  history: createWebHistory(),

  // 配置路由，是一个数组，里面可以配置多个路由
  routes: [
    {
      // 路由路径
      path: '/',
      // 路由路径所对应的页面
      component: () => import('@/view/Login')
    },
    {
      //路由路径
      path: '/dashboard',
      //路由路径所对应的页面
      component : () => import('@/view/Dashboard'),
      children: [
        {
          // 子路由路径不能以 / 开头
          path: 'user',
          component : () => import('@/view/User')
        }, {
          // 市场活动
          path: 'activity',
          component : () => import('@/view/Activities')
        }, {
          // 线索管理
          path: 'clue',
          component : () => import('@/view/Clue')
        }, {
          // 客户管理
          path: 'customer',
          component : () => import('@/view/Customer')
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*', // 匹配所有未定义路径
      name: 'NotFound',
      component: () => import('@/view/NotFound') // 404页面组件
    }
  ]
});

// 导出创建的路由对象
export default router;
