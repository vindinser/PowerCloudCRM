<!-- 登录页面 -->
<template>
  <div class="login">
    <vue-particles
      class="particle-background"
      id="tsparticles"
      :particlesInit="particlesInit"
      :options="options"
    />
    <div class="content">
      <div class="content-title">PowerCloudCRM</div>
      <el-form
        ref="ruleFormRef"
        :model="ruleForm"
        status-icon
        :rules="rules"
        label-width="120px"
        style="width: 80%"
      >
        <el-form-item label="用户名" prop="loginAct">
          <el-input v-model="ruleForm.loginAct" autocomplete="off" />
        </el-form-item>
        <el-form-item label="密码" prop="loginPwd">
          <el-input v-model="ruleForm.loginPwd" type="password" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div class="content-btns">
        <el-button type="primary" @click="submitForm">登 录</el-button>
        <el-button @click="resetForm">重 置</el-button>
      </div>
    </div>
    <div class="registration-number">
      <a class="beian-link" href="https://beian.miit.gov.cn/" target="_blank">冀ICP备2025106446号-1</a>
    </div>
  </div>
</template>

<script setup name="Login">
import { loadFull } from 'tsparticles';
import useUserStore from '@/store/modules/user';

const userStore = useUserStore();

// 定义粒子配置选项
const options = reactive({
  fpsLimit: 60,
  interactivity: {
    events: {
      onClick: { enable: true, mode: 'push' },
      onHover: { enable: true, mode: 'repulse' },
      resize: true
    },
    modes: {
      bubble: { distance: 400, duration: 2, opacity: 0.8, size: 40 },
      push: { quantity: 4 },
      repulse: { distance: 180, duration: 0.4 }
    },
    detectsOn: 'canvas'
  },
  particles: {
    color: { value: '#D6F5C9' },
    links: {
      color: '#D6F5C9',
      distance: 200,
      enable: true,
      opacity: 0.7,
      width: 2
    },
    collisions: { enable: false },
    move: {
      direction: 'none',
      enable: true,
      outMode: 'bounce',
      random: false,
      speed: 2,
      straight: false
    },
    number: {
      density: { enable: true, area: 950 },
      value: 50
    },
    opacity: { value: 1 },
    shape: { type: 'star' },
    size: { random: true, value: 7 }
  },
  detectRetina: true
});

// 初始化粒子库
const particlesInit = async (engine) => {
  await loadFull(engine);
};

const ruleForm = reactive({
  loginAct: '',
  loginPwd: ''
});
const rules = {
  loginAct: [{ required: true, trigger: 'blur', message: '用户名不能为空' }],
  loginPwd: [{ required: true, trigger: 'blur', message: '密码不能为空' }]
};

const ruleFormRef = ref(null);

const submitForm = () =>
  ruleFormRef.value.validate((valid) => {
    if (!valid) {
      return ElMessage.warning('请认真填写账号密码！');
    }

    userStore.login(ruleForm);
  });

const resetForm = () => ruleFormRef.value.resetFields();

</script>

<style lang="scss" scoped>
  /* 设置背景图片（如果需要的话） */
  .particle-background {
    position: relative; /* 确保背景图片和粒子效果正确叠加 */
    width: 100vw; /* 根据需要调整宽度 */
    height: 100vh; /* 根据需要调整高度 */
    // background: rgb(35, 39, 65);
    overflow: hidden; /* 防止粒子超出边界 */
    background-image: url('@/assets/bg.jpg'); /* 替换为您的背景图片路径 */
    background-repeat: no-repeat;
    background-size: cover;
  }

  /* 确保 vue-particles 容器能够正确显示 */
  #tsparticles {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: -1; /* 如果需要让粒子在背景图片下方显示，可以设置 z-index */
  }

  .content {
    position: fixed;
    top: 50%;
    left: 50%;
    display: flex;
    border-radius: 6px;
    padding: 32px;
    width: 500px;
    height: 300px;
    transform: translate(-50%, -50%);
    background-color: rgb(0 0 0 / 50%);
    box-sizing: border-box;
    flex-direction: column;
    justify-content: space-around;

    &-title {
      height: 80px;
      font-size: 30px;
      line-height: 80px;
      text-align: center;
      color: #fff;
    }

    &-btns {
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .registration-number {
    position: absolute;
    bottom: 16px;
    left: 50%;
    transform: translateX(-50%);

    .beian-link {
      color: #fff;
      text-decoration: none; /* 默认无下划线 */

      &:hover {
        color: #007bff; /* 悬停颜色 */
        text-decoration: underline; /* 悬停下划线 */
      }
    }
  }
</style>
