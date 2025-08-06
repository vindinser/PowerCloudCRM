<template>
  <div class="not-found-container">
    <div class="error-content">
      <!-- 动画容器 -->
      <div class="animation-container">
        <div class="orbit">
          <div class="planet"/>
          <div class="astronaut">
            <div class="helmet"/>
            <div class="body"/>
          </div>
        </div>
      </div>

      <!-- 内容区 -->
      <div class="text-content">
        <h1 class="error-code">404</h1>
        <h2 class="error-title">迷失在数字宇宙</h2>
        <p class="error-description">
          你访问的页面已经消失在黑洞中，或从未存在于这个数字宇宙
        </p>

        <!-- 操作按钮 -->
        <div class="action-buttons">
          <button class="home-button" @click="goHome">
            <span>返回安全区域</span>
          </button>
          <button class="back-button" @click="goBack">
            <span>返回上一站</span>
          </button>
        </div>

        <!-- 趣味提示 -->
        <div class="tips">
          <p>提示：检查网址拼写，或者联系我们的星际导航员</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { useRouter } from 'vue-router';

  const router = useRouter();

  // 返回首页
  const goHome = () => {
    router.push('/');
  };

  // 返回上一页
  const goBack = () => {
    router.go(-1);
  };

</script>

<style scoped>
.not-found-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
  color: #fff;
  font-family: 'Segoe UI', system-ui;
  overflow: hidden;
}

.error-content {
  max-width: 1200px;
  width: 90%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3rem;
}

.animation-container {
  position: relative;
  width: 300px;
  height: 300px;
}

.orbit {
  position: relative;
  width: 100%;
  height: 100%;
  border: 1px dashed rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  animation: rotate 20s linear infinite;
}

@keyframes rotate {
  100% { transform: rotate(360deg); }
}

.planet {
  position: absolute;
  top: -20px;
  left: calc(50% - 25px);
  width: 50px;
  height: 50px;
  background: #4cc9f0;
  border-radius: 50%;
  box-shadow: 0 0 25px rgba(76, 201, 240, 0.8);
}

.astronaut {
  position: absolute;
  top: 60%;
  left: calc(50% - 15px);
  transform: translateY(-50%);
}

.helmet {
  width: 30px;
  height: 30px;
  background: white;
  border-radius: 50%;
  position: relative;
}

.helmet::before {
  content: '';
  position: absolute;
  top: 5px;
  left: 5px;
  width: 10px;
  height: 5px;
  background: #333;
  border-radius: 20px;
}

.body {
  width: 40px;
  height: 60px;
  background: white;
  margin-top: -5px;
  border-radius: 15px;
  position: relative;
}

.text-content {
  text-align: center;
}

.error-code {
  font-size: 8rem;
  margin: 0;
  background: linear-gradient(90deg, #4cc9f0, #f72585);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

.error-title {
  font-size: 2.5rem;
  margin: 1rem 0;
  letter-spacing: 1px;
}

.error-description {
  font-size: 1.2rem;
  max-width: 600px;
  margin: 0 auto 2rem;
  line-height: 1.6;
  color: #b8c1ec;
}

.action-buttons {
  display: flex;
  gap: 1.5rem;
  justify-content: center;
  margin-top: 2rem;
}

button {
  border: none;
  padding: 12px 30px;
  font-size: 1.1rem;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: bold;
  position: relative;
  overflow: hidden;
}

.home-button {
  background: linear-gradient(90deg, #4361ee, #4cc9f0);
  color: white;
  box-shadow: 0 5px 15px rgba(67, 97, 238, 0.4);
}

.back-button {
  background: transparent;
  color: #4cc9f0;
  border: 2px solid #4cc9f0;
}

button span {
  position: relative;
  z-index: 2;
}

.home-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(67, 97, 238, 0.6);
}

.back-button:hover {
  background: rgba(76, 201, 240, 0.1);
}

.tips {
  margin-top: 3rem;
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.6);
  font-style: italic;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .error-content {
    flex-direction: column;
  }

  .animation-container {
    width: 200px;
    height: 200px;
  }

  .error-code {
    font-size: 6rem;
  }

  .error-title {
    font-size: 2rem;
  }

  .action-buttons {
    flex-direction: column;
  }
}
</style>