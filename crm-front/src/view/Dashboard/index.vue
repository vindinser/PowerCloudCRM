<!-- 仪表盘 -->
<template>
  <div>
    <el-container>
      <!--左侧-->
      <el-aside :width="userStore.isCollapse ? '64px' : '200px'">
        <div class="menuTitle">PowerCloudCRM</div>
        <el-menu
          active-text-color="#ffd04b"
          background-color="#334157"
          class="el-menu-vertical-demo"
          :default-active="currentRouterPath"
          text-color="#fff"
          style="border-right: solid 0px;"
          :collapse="userStore.isCollapse"
          :collapse-transition="false"
          :router="true"
          :unique-opened="true"
        >
          <!-- 动态路由 -->
          <el-sub-menu v-for="(menuPermission, index) in userStore.userInfo.menuPermissionList" :key="menuPermission.id" :index="`${ index }`">
            <template #title>
              <el-icon>
                <component :is="menuPermission.icon"/>
              </el-icon>
              <span>{{ menuPermission.name }}</span>
            </template>
            <el-menu-item v-for="subPermission in menuPermission.subPermissionList" :key="subPermission.id" :index="subPermission.url">
              <el-icon>
                <component :is="subPermission.icon"/>
              </el-icon>
              <span>{{ subPermission.name }}</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <!--右侧-->
      <el-container class="rightContent">
        <!--右侧：上-->
        <el-header>
          <el-icon class="show" @click="userStore.showMenu"><Fold /></el-icon>
          <el-dropdown :hide-on-click="false">
            <span class="el-dropdown-link">
              <span>{{ userStore.userInfo.name }}</span>
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>我的资料</el-dropdown-item>
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item divided @click="userStore.logOut">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

        </el-header>

        <!--右侧：中-->
        <el-main>
          <router-view v-if="isRouterAlive"/>
        </el-main>

        <!--右侧：下-->
        <el-footer>@版权所有 2025-2099 ZS</el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script setup name="Dashboard">
  import useUserStore from '@/store/modules/user';
  import { useRoute } from 'vue-router';

  // 控制仪表盘页面右侧内容体是否显示，true显示，false不显示
  const isRouterAlive = ref(true);

  // 定义唯一 Symbol 键名 避免冲突
  const ReloadKey = Symbol('reload');

  provide(ReloadKey, () => {
    isRouterAlive.value = false;
    nextTick(() => {
      isRouterAlive.value = true;
    });
  });

  const userStore = useUserStore();

  //当前访问的路由路径
  const currentRouterPath = ref('');
  // 加载当前路由路径
  const loadCurrentRouterPath = () => {
    const route = useRoute();

    currentRouterPath.value = route.path;
  };

  onMounted(() => {
    loadCurrentRouterPath();
  });
</script>

<style lang="scss" scoped>
.el-aside {
  background: #1a1a1a;
}
.el-header {
  background: azure;
  height: 35px;
  line-height: 35px;
}
.el-footer {
  background: aliceblue;
  height: 35px;
  line-height: 35px;
  text-align: center;
}
.rightContent {
  height: calc(100vh);
}
.menuTitle {
  height: 35px;
  line-height: 35px;
  color: #f9f9f9;
  text-align: center;
}
.show {
  cursor: pointer;
}
.el-dropdown {
  float: right;
  line-height: 35px;
}
</style>