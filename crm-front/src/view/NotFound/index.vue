<template>
  <div ref="containerRef" class="not-found-container">
    <div class="bg-base" :style="{ backgroundImage: `url(${bgUrl})` }" />
    <canvas ref="maskCanvas" class="ink-mask" />
    <div class="content">
      <div class="error-code">404</div>
      <div class="error-title">迷失在数字宇宙</div>
      <div class="error-desc">你访问的页面已经消失在黑洞中，或从未存在于这个数字宇宙</div>
      <div class="action-buttons">
        <button class="btn-primary" @click="goHome">
          <span>返回安全区域</span>
        </button>
        <button class="btn-ghost" @click="goBack">
          <span>返回上一站</span>
        </button>
      </div>
      <div class="tips">提示：检查网址拼写，或者联系我们的星际导航员</div>
    </div>
  </div>
</template>

<script setup>
  import { useRouter } from 'vue-router';
  import { useInkMask } from '@/composables/useInkMask';
  import { getRandomBg } from '@/composables/useRandomBg';

  const router = useRouter();
  const containerRef = ref(null);
  const maskCanvas = ref(null);
  const bgUrl = getRandomBg();

  const goHome = () => router.push('/');
  const goBack = () => router.go(-1);

  const { init, destroy } = useInkMask(containerRef, maskCanvas, { maskAlpha: 0.7 });

  onMounted(() => init());
  onUnmounted(() => destroy());
</script>

<style lang="scss" scoped>
  @import '@/assets/styles/ink-mask';

  .not-found-container {
    position: relative;
    width: 100vw;
    height: 100vh;
    overflow: hidden;
  }

  .content {
    position: absolute;
    top: 50%;
    left: 50%;
    text-align: center;
    transform: translate(-50%, -50%);
    z-index: 10;
    animation: content-fade-in 0.6s ease-out both;

    .error-code {
      margin: 0;
      font-size: 120px;
      font-weight: 800;
      line-height: 1;
      letter-spacing: 4px;
      text-shadow: none;
      filter: drop-shadow(0 4px 12px rgb(99 102 241 / 25%));
      background: linear-gradient(135deg, #4338ca 0%, #6366f1 40%, #7c3aed 70%, #a78bfa 100%);
      background-clip: text;
      -webkit-text-fill-color: transparent;
    }

    .error-title {
      margin: 16px 0 12px;
      font-size: 32px;
      font-weight: 600;
      letter-spacing: 2px;
      text-shadow: 0 2px 8px rgb(0 0 0 / 50%);
      color: rgb(255 255 255 / 85%);
    }

    .error-desc {
      margin: 0 auto;
      max-width: 500px;
      font-size: 16px;
      line-height: 1.6;
      color: rgb(255 255 255 / 45%);
    }

    .action-buttons {
      display: flex;
      justify-content: center;
      gap: 16px;
      margin-top: 32px;
    }

    .tips {
      margin-top: 48px;
      font-size: 13px;
      font-style: italic;
      color: rgb(255 255 255 / 28%);
    }
  }

  button {
    position: relative;
    border: none;
    border-radius: 50px;
    padding: 10px 28px;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    overflow: hidden;
    transition: all 0.3s ease;

    span {
      position: relative;
      z-index: 2;
    }
  }

  .btn-primary {
    background: linear-gradient(135deg, #4338ca, #6366f1);
    color: rgb(255 255 255 / 90%);
    box-shadow: 0 4px 14px rgb(99 102 241 / 25%);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgb(99 102 241 / 40%);
    }
  }

  .btn-ghost {
    border: 1px solid rgb(255 255 255 / 12%);
    background: rgb(255 255 255 / 6%);
    color: rgb(255 255 255 / 55%);
    backdrop-filter: blur(8px);

    &:hover {
      transform: translateY(-2px);
      background: rgb(255 255 255 / 12%);
      color: rgb(255 255 255 / 75%);
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
