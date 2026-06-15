<!-- 登录页面 -->
<template>
  <div ref="loginRef" class="login">
    <div class="bg-base" :style="{ backgroundImage: `url(${bgUrl})` }" />
    <vue-particles
      id="tsparticles"
      class="particle-layer"
      :particlesInit="particlesInit"
      :options="options"
    />
    <canvas ref="maskCanvas" class="ink-mask" />
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
        <el-form-item label="">
          <el-checkbox v-model="ruleForm.rememberLogin" label="记住我" />
        </el-form-item>
      </el-form>
      <div class="content-btns">
        <el-button type="primary" @click="submitForm">登 录</el-button>
        <el-button @click="resetForm">重 置</el-button>
      </div>
    </div>
    <div class="footer-info">
      <div class="support-text">
        <span>本网站由</span>
        <a class="author-link" href="https://gitee.com/vindinser/power-cloud-crm" target="_blank">ZhangShuang</a>
        <span>强力支持</span>
      </div>
      <div class="registration-number">
        <a class="beian-link" href="https://beian.miit.gov.cn/" target="_blank">冀ICP备2025106446号-2</a>
        <span class="beian-separator">|</span>
        <a
          class="beian-link"
          href="https://beian.mps.gov.cn/#/query/webSearch?code=13082502000149"
          target="_blank"
          rel="noreferrer"
        >
          <img class="police-badge" src="@/assets/police-badge.png" alt="公安备案" >
          冀公网安备13082502000149号
        </a>
      </div>
    </div>
  </div>
</template>

<script setup name="Login">
  import { loadFull } from 'tsparticles';
  import useUserStore from '@/store/modules/user';
  import { useInkMask } from '@/composables/useInkMask';
  import { getRandomBg } from '@/composables/useRandomBg';

  const userStore = useUserStore();
  const loginRef = ref(null);
  const maskCanvas = ref(null);
  const bgUrl = getRandomBg();

  const { init: initMask, destroy: destroyMask } = useInkMask(loginRef, maskCanvas, {
    maskAlpha: 0.45
  });

  const options = reactive({
    fpsLimit: 30,
    interactivity: {
      events: {
        onClick: { enable: true, mode: 'push' },
        onHover: { enable: true, mode: 'grab' },
        resize: true
      },
      modes: {
        push: { quantity: 3 },
        grab: { distance: 180, links: { opacity: 0.35 } }
      }
    },
    particles: {
      color: { value: ['#409EFF', '#67C23A', '#E6A23C', '#ffffff'] },
      links: {
        color: '#409EFF',
        distance: 180,
        enable: true,
        opacity: 0.25,
        width: 1
      },
      collisions: { enable: false },
      move: {
        direction: 'none',
        enable: true,
        outMode: 'bounce',
        random: true,
        speed: { min: 0.2, max: 0.8 },
        straight: false,
        angle: { offset: 30, value: 90 }
      },
      number: {
        density: { enable: true, area: 700 },
        value: 45
      },
      opacity: {
        value: { min: 0.3, max: 0.9 },
        animation: { enable: true, speed: 0.5, minimumValue: 0.1, sync: false }
      },
      shape: { type: 'circle' },
      size: {
        value: { min: 4, max: 12 },
        animation: { enable: true, speed: 2, minimumValue: 3, sync: false }
      },
      rotate: {
        value: { min: 0, max: 360 },
        direction: 'random',
        animation: { enable: true, speed: 3, sync: false }
      }
    },
    detectRetina: true
  });

  const particlesInit = async (engine) => {
    await loadFull(engine);
  };

  const ruleForm = reactive({
    loginAct: '',
    loginPwd: '',
    rememberLogin: false
  });
  const rules = {
    loginAct: [{ required: true, trigger: 'blur', message: '用户名不能为空' }],
    loginPwd: [{ required: true, trigger: 'blur', message: '密码不能为空' }]
  };

  const ruleFormRef = ref(null);

  const router = useRouter();
  const toDashboard = () => router.push({ path: '/dashboard' });

  const submitForm = () =>
    ruleFormRef.value.validate(async (valid) => {
      if (!valid) {
        return ElMessage.warning('请认真填写账号密码！');
      }
      await userStore.login(ruleForm);
      toDashboard();
    });

  const resetForm = () => ruleFormRef.value.resetFields();

  onMounted(async () => {
    const loginInfo = userStore.getLoginInfo();
    const rememberLogin = loginInfo.rememberLogin;

    if (rememberLogin) {
      ruleForm.loginAct = loginInfo.loginAct;
      ruleForm.loginPwd = loginInfo.loginPwd;
      ruleForm.rememberLogin = rememberLogin;
      await userStore.freeLogin();
      toDashboard();
    }

    initMask();
  });

  onUnmounted(() => {
    destroyMask();
  });
</script>

<style lang="scss" scoped>
  @import '@/assets/styles/ink-mask';

  .login {
    position: relative;
    width: 100vw;
    height: 100vh;
    overflow: hidden;
  }

  .particle-layer {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
  }

  .content {
    position: fixed;
    top: 50%;
    left: 50%;
    display: flex;
    border: 1px solid rgb(255 255 255 / 20%);
    border-radius: 16px;
    padding: 40px;
    width: 480px;
    height: 340px;
    transform: translate(-50%, -50%);
    background: rgb(255 255 255 / 12%);
    z-index: 10;
    backdrop-filter: blur(20px) saturate(1.2);
    box-shadow:
      0 8px 32px rgb(0 0 0 / 30%),
      inset 0 1px 0 rgb(255 255 255 / 15%);
    box-sizing: border-box;
    flex-direction: column;
    justify-content: space-around;
    animation: content-fade-in 0.6s ease-out both;

    &-title {
      height: 70px;
      font-size: 32px;
      font-weight: 600;
      line-height: 70px;
      letter-spacing: 1px;
      text-align: center;
      text-shadow: 0 2px 8px rgb(0 0 0 / 30%);
      color: #fff;
    }

    &-btns {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 16px;
    }
  }

  .footer-info {
    position: absolute;
    bottom: 16px;
    left: 50%;
    display: flex;
    white-space: nowrap;
    transform: translateX(-50%);
    z-index: 10;
    flex-direction: column;
    align-items: center;
    gap: 6px;
    animation: footer-slide-up 0.8s ease-out both;

    .support-text {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 12px;
      color: rgb(255 255 255 / 60%);
      animation: footer-fade-in 1s ease-out 0.3s both;

      .author-link {
        text-decoration: none;
        color: $--color-primary;
        transition: color 0.3s;

        &:hover {
          text-decoration: underline;
          color: #fff;
        }
      }
    }

    .registration-number {
      display: flex;
      align-items: center;
      gap: 8px;
      animation: footer-fade-in 1s ease-out 0.5s both;

      .beian-link {
        display: inline-flex;
        font-size: 12px;
        text-decoration: none;
        color: rgb(255 255 255 / 80%);
        align-items: center;
        gap: 4px;
        transition:
          color 0.3s,
          transform 0.3s;

        &:hover {
          text-decoration: underline;
          transform: translateY(-1px);
          color: $--color-primary;
        }
      }

      .beian-separator {
        font-size: 12px;
        color: rgb(255 255 255 / 40%);
      }

      .police-badge {
        width: 16px;
        height: 16px;
        flex-shrink: 0;
        animation: badge-pulse 2s ease-in-out infinite;
      }
    }
  }

  @keyframes footer-slide-up {
    from {
      opacity: 0;
      transform: translateX(-50%) translateY(20px);
    }

    to {
      opacity: 1;
      transform: translateX(-50%) translateY(0);
    }
  }

  @keyframes footer-fade-in {
    from {
      opacity: 0;
    }

    to {
      opacity: 1;
    }
  }

  @keyframes badge-pulse {
    0%,
    100% {
      transform: scale(1);
    }

    50% {
      transform: scale(1.15);
    }
  }

  @keyframes content-fade-in {
    from {
      opacity: 0;
      transform: translate(-50%, -50%) translateY(20px) scale(0.96);
    }

    to {
      opacity: 1;
      transform: translate(-50%, -50%) translateY(0) scale(1);
    }
  }
</style>
