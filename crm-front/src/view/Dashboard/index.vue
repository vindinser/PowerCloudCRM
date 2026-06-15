<!-- 仪表盘 -->
<template>
  <div class="dashboard">
    <el-container class="dashboard-container">
      <!--左侧-->
      <el-aside :width="userStore.isCollapse ? '64px' : '200px'" class="dashboard-aside">
        <div class="menuTitle">
          <span v-if="userStore.isCollapse" class="menuTitle-logo">P</span>
          <span v-else>PowerCloud CRM</span>
        </div>
        <el-menu
          active-text-color="#409EFF"
          background-color="#1e293b"
          class="dashboard-menu"
          :default-active="currentRouterPath"
          text-color="#cbd5e1"
          style="border-right: solid 0px;"
          :collapse="userStore.isCollapse"
          :collapse-transition="false"
          :router="true"
          :unique-opened="true"
        >
          <!-- 动态路由 -->
          <el-sub-menu v-for="(menuPermission, index) in userStore.userInfo.menuPermissionList" :key="menuPermission.id" :index="`${index}`">
            <template #title>
              <el-icon>
                <component :is="menuPermission.icon" />
              </el-icon>
              <span>{{ menuPermission.name }}</span>
            </template>
            <el-menu-item v-for="subPermission in menuPermission.subPermissionList" :key="subPermission.id" :index="subPermission.url">
              <el-icon>
                <component :is="subPermission.icon" />
              </el-icon>
              <span>{{ subPermission.name }}</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <!--右侧-->
      <el-container class="rightContent">
        <!--右侧：上-->
        <el-header class="right-header">
          <div class="header-left">
            <el-icon class="collapse-btn" @click="userStore.showMenu"><Fold /></el-icon>
          </div>
          <div class="header-right">
            <el-dropdown :hide-on-click="false">
              <span class="el-dropdown-link">
                <el-icon class="user-icon"><User /></el-icon>
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
          </div>
        </el-header>

        <!--右侧：中-->
        <el-main class="right-main">
          <router-view v-if="isRouterAlive" />
        </el-main>

        <!--右侧：下-->
        <el-footer class="right-footer">@版权所有 2025-2099 ZS</el-footer>
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
$sidebar-bg: #1e293b;
$sidebar-bg-light: #273548;
$sidebar-border: rgba(255, 255, 255, 0.06);

.dashboard {
  height: 100vh;
  overflow: hidden;
}

.dashboard-container {
  height: 100%;
}

.dashboard-aside {
  background: $sidebar-bg;
  overflow: hidden;
  transition: width 0.3s;

  &::-webkit-scrollbar {
    width: 0;
  }
}

:deep(.dashboard-menu) {
  border-right: none;

  .el-sub-menu__title,
  .el-menu-item {
    height: 46px;
    line-height: 46px;
    font-size: 14px;
    transition: background-color 0.2s, color 0.2s;

    &:hover {
      background-color: $sidebar-bg-light !important;
      color: #fff !important;
    }
  }

  .el-menu-item.is-active {
    background-color: $sidebar-bg-light !important;
    color: #409eff !important;
    position: relative;

    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 8px;
      bottom: 8px;
      width: 3px;
      border-radius: 0 3px 3px 0;
      background: #409eff;
    }
  }

  .el-sub-menu .el-menu {
    background-color: darken($sidebar-bg, 3%) !important;
  }

  .el-sub-menu__icon-arrow {
    color: #64748b;
  }
}

.rightContent {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden;
}

.right-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 50px;
  padding: 0 16px;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  z-index: 1;

  .header-left {
    display: flex;
    align-items: center;
  }

  .collapse-btn {
    font-size: 20px;
    cursor: pointer;
    color: #333;
    transition: color 0.3s;

    &:hover {
      color: $--color-primary;
    }
  }

  .header-right {
    display: flex;
    align-items: center;
  }
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  color: #333;
  font-size: 14px;

  .user-icon {
    font-size: 18px;
  }
}

.right-main {
  flex: 1;
  overflow: auto;
  padding: 16px;
  background: $bg-gray;
}

.right-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 36px;
  padding: 0;
  background: #fff;
  border-top: 1px solid #e8e8e8;
  color: #999;
  font-size: 12px;
}

.menuTitle {
  height: 50px;
  line-height: 50px;
  color: #f1f5f9;
  text-align: center;
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 1px;
  white-space: nowrap;
  overflow: hidden;
  border-bottom: 1px solid $sidebar-border;
  background: darken($sidebar-bg, 3%);
}

.menuTitle-logo {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 6px;
  background: #409eff;
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  margin-top: 9px;
}
</style>