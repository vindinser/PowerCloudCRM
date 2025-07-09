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
          <!-- <el-sub-menu :index="index" v-for="(menuPermission, index) in user.menuPermissionList" :key="menuPermission.id">
            <template #title>
              <el-icon><component :is="menuPermission.icon"/></el-icon>
              <span> {{menuPermission.name}} </span>
            </template>
            <el-menu-item v-for="subPermission in menuPermission.subPermissionList" :key="subPermission.id" :index="subPermission.url">
              <el-icon><component :is="subPermission.icon"/></el-icon>
              {{subPermission.name}}
            </el-menu-item>
          </el-sub-menu> -->
          <el-sub-menu index="1">
            <template #title>
              <el-icon>
                <component :is="`OfficeBuilding`"/>
              </el-icon>
              <span>市场活动</span>
            </template>
            <el-menu-item index="/dashboard/activity">
              <el-icon>
                <component :is="`Notification`"/>
              </el-icon>
              <span>市场活动</span>
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="7">
            <template #title>
              <el-icon>
                <component :is="`CreditCard`"/>
              </el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/dashboard/user">
              <el-icon>
                <component :is="`CreditCard`"/>
              </el-icon>
              <span>用户管理</span>
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
  // provide() {
  //   return {
  //     //提供一个函数（要求是箭头函数）
  //     reload: ()=> {
  //       this.isRouterAlive = false; //右侧内容隐藏
  //       this.$nextTick(() => { //$nextTick(), 当数据更新了，在dom中渲染后，自动执行该函数，
  //         this.isRouterAlive = true;
  //       })
  //     },

  //     //提供一个字符串
  //     content: "是对负荷计算东方红郡凯撒的合法户籍卡",

  //     //提供一个数字
  //     age: 28,

  //     //提供一个对象
  //     user : {id: 1098, name: "张三", age: 18},

  //     //提供一个数组
  //     arr : [12, 56, 109, 356, 8901]

  //     //......
  //   }
  // }

  const userStore = useUserStore();

  // 登录用户对象，初始值是空
  const user = ref({});

  console.log('🚀 ~ userInfo:', userStore.userInfo);

  //当前访问的路由路径
  const currentRouterPath = ref('');
  // 加载当前路由路径
  const loadCurrentRouterPath = () => {
    console.log('加载当前路由路径');
  // let path = this.$route.path; //   /dashboard/activity/add
  // let arr = path.split('/'); //   [  ,dashboard, activity, add]

  // if (arr.length > 3) {
  //   this.currentRouterPath = '/' + arr[1] + '/' + arr[2];
  // } else {
  //   this.currentRouterPath = path;
  // }
  };

  onMounted(() => {
    loadCurrentRouterPath();
  });
// watch(propData, (newVal, oldVal) => {})

// defineExpose({}) // 将方法、数据暴露给父组件
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