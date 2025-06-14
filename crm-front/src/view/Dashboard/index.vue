<!-- 仪表盘 -->
<template>
  <div>
    <el-container>
      <!--左侧-->
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <div class="menuTitle">PowerCloudCRM</div>
        <el-menu
          active-text-color="#ffd04b"
          background-color="#334157"
          class="el-menu-vertical-demo"
          :default-active="currentRouterPath"
          text-color="#fff"
          style="border-right: solid 0px;"
          :collapse="isCollapse"
          :collapse-transition="false"
          :router="true"
          :unique-opened="true">

          <el-sub-menu :index="index" v-for="(menuPermission, index) in user.menuPermissionList" :key="menuPermission.id">
            <template #title>
              <el-icon><component :is="menuPermission.icon"/></el-icon>
              <span> {{menuPermission.name}} </span>
            </template>
            <el-menu-item v-for="subPermission in menuPermission.subPermissionList" :key="subPermission.id" :index="subPermission.url">
              <el-icon><component :is="subPermission.icon"/></el-icon>
              {{subPermission.name}}
            </el-menu-item>
          </el-sub-menu>
        </el-menu>

      </el-aside>

      <!--右侧-->
      <el-container class="rightContent">
        <!--右侧：上-->
        <el-header>
          <el-icon class="show" @click="showMenu"><Fold /></el-icon>

          <el-dropdown :hide-on-click="false">
            <span class="el-dropdown-link">
              {{ user.name }}
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>我的资料</el-dropdown-item>
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
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

// 控制左侧菜单左右的展开和折叠，true是折叠，false是展开
const isCollapse = ref(false);
//左侧菜单左右展开和折叠
const showMenu = () => {
  isCollapse.value = !isCollapse.value;
};

const { userInfo } = useUserStore();

console.log('🚀 ~ userInfo:', userInfo);
// 登录用户对象，初始值是空
const user = ref({});

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

// 退出登录
const logout = () => {
  console.log('退出登录');
  // doGet("/api/logout", {}).then(resp => {
  //   if (resp.data.code === 200) {
  //     removeToken();
  //     messageTip("退出成功", "success");
  //     //跳到登录页
  //     window.location.href = "/";
  //   } else {
  //     messageConfirm("退出异常，是否要强制退出？").then(() => { //用户点击“确定”按钮就会触发then函数
  //       //既然后端验证token未通过，那么前端的token肯定是有问题的，那没必要存储在浏览器中，直接删除一下
  //       removeToken();
  //       //跳到登录页
  //       window.location.href = "/";
  //     }).catch(() => { //用户点击“取消”按钮就会触发then函数
  //       messageTip("取消强制退出", "warning");
  //     })
  //   }
  // })
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