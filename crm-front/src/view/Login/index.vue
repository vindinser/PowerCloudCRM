<!-- 登录页面 -->
<template>
  <div class="login">
    <vue-particles class="particle-background" id="tsparticles" :particlesInit="particlesInit" :options="options" />
    <div class="content">
      <div class="content-title">PowerCloudCRM</div>
      <el-form ref="ruleFormRef" :model="ruleForm" status-icon :rules="rules" label-width="120px" style="width: 80%;">
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
  </div>
</template>

<script setup name="Login">
  import axios from 'axios';
  import { loadFull } from 'tsparticles';
  
  // 定义粒子配置选项
  const options = reactive({
    fpsLimit: 60,
    interactivity: {
      events: {
        onClick: { enable: true, mode: 'push' },
        onHover: { enable: true, mode: 'repulse' },
        resize: true,
      },
      modes: {
        bubble: { distance: 400, duration: 2, opacity: 0.8, size: 40 },
        push: { quantity: 4 },
        repulse: { distance: 180, duration: 0.4 },
      },
      detectsOn: 'canvas',
    },
    particles: {
      color: { value: '#D6F5C9' },
      links: {
        color: '#D6F5C9',
        distance: 200,
        enable: true,
        opacity: 0.7,
        width: 2,
      },
      collisions: { enable: false },
      move: {
        direction: 'none',
        enable: true,
        outMode: 'bounce',
        random: false,
        speed: 2,
        straight: false,
      },
      number: {
        density: { enable: true, area: 950 },
        value: 50,
      },
      opacity: { value: 1 },
      shape: { type: 'star' },
      size: { random: true, value: 7 },
    },
    detectRetina: true,
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
  
  const submitForm = () => ruleFormRef.value.validate(valid => {
    if(!valid) return ElMessage.warning('请认真填写账号密码！');
    const loading = ElLoading.service({
      lock: true,
      text: 'Loading',
      background: 'rgba(0, 0, 0, 0.7)',
    });
    let formData = new FormData();
    formData.append('loginAct', 'admin');
    formData.append('loginPwd', 'aaa111');
    axios.post('http://101.43.158.81:8080/api/login', formData).then((res) => {
      console.log("🚀 ~ testAxios ~ res:", res);
    }).catch((err) => {
      console.log("🚀 ~ testAxios ~ err:", err);
    }).finally(() => loading.close());
  })

  const resetForm = () => ruleFormRef.value.resetFields();
</script>

<style lang="scss" scoped>
  /* 设置背景图片（如果需要的话） */
  .particle-background {
    position: relative; /* 确保背景图片和粒子效果正确叠加 */
    height: 100vh; /* 根据需要调整高度 */
    width: 100vw; /* 根据需要调整宽度 */
    background-size: cover;
    background-repeat: no-repeat;
    background-image: url('@/assets/bg.jpg'); /* 替换为您的背景图片路径 */
    // background: rgb(35, 39, 65);
    overflow: hidden; /* 防止粒子超出边界 */
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
    box-sizing: border-box;
    border-radius: 6px;
    padding: 32px;
    background-color: rgba(0, 0, 0, .5);
    width: 500px;
    height: 300px;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    display: flex;
    flex-direction: column;
    justify-content: space-around;

    &-title {
      color: #FFFFFF;
      font-size: 30px;
      height: 80px;
      line-height: 80px;
      text-align: center;
    }

    &-btns {
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
</style>