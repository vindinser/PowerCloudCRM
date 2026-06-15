<template>
  <div class="crm-welcome">
    <!-- 欢迎横幅 -->
    <section class="welcome-banner">
      <div class="welcome-text">
        <div class="welcome-greeting">
          <span class="greeting-time">{{ greetingText }}</span>
          <span class="greeting-name">{{ userStore.userInfo.name }}</span>
        </div>
        <h2>欢迎使用 PowerCloudCRM</h2>
        <p>动力云客系统 — 全面管理您的客户关系，提升销售效率，优化客户体验</p>
        <div class="welcome-actions">
          <el-button type="primary" size="large" @click="navigateTo('/dashboard/clue')">
            <el-icon><DataLine /></el-icon>
            开始工作
          </el-button>
          <el-button size="large" @click="navigateTo('/dashboard/statistic')">
            <el-icon><TrendCharts /></el-icon>
            查看数据
          </el-button>
        </div>
      </div>
      <div class="welcome-graphic">
        <div class="graphic-circle circle-1"/>
        <div class="graphic-circle circle-2"/>
        <div class="graphic-circle circle-3"/>
        <div class="graphic-icon">
          <el-icon :size="48"><OfficeBuilding /></el-icon>
        </div>
      </div>
    </section>

    <!-- 核心指标卡片 -->
    <section class="metrics-grid">
      <div
        v-for="(metric, index) in metrics"
        :key="metric.key"
        class="metric-card"
        :class="metric.class"
        :style="{ animationDelay: `${index * 0.1}s` }"
      >
        <div class="metric-icon">
          <el-icon :size="28">
            <component :is="metric.icon" />
          </el-icon>
        </div>
        <div class="metric-info">
          <h3>{{ metric.label }}</h3>
          <p class="metric-value">{{ metric.value }}</p>
          <div class="metric-trend" :class="metric.trendClass">
            <el-icon><component :is="metric.trendIcon" /></el-icon>
            <span>{{ metric.trend }}</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 功能导航区 -->
    <section class="function-section">
      <div class="section-header">
        <h2>功能导航</h2>
        <span class="section-desc">快速访问系统核心功能</span>
      </div>
      <div class="function-grid">
        <div
          v-for="(func, index) in functions"
          :key="func.path"
          class="function-card"
          :style="{ animationDelay: `${index * 0.08}s` }"
          @click="navigateTo(func.path)"
        >
          <div class="function-icon" :style="{ background: func.color }">
            <el-icon :size="24">
              <component :is="func.icon" />
            </el-icon>
          </div>
          <div class="function-info">
            <h4>{{ func.name }}</h4>
            <p>{{ func.desc }}</p>
          </div>
          <el-icon class="function-arrow"><ArrowRight /></el-icon>
        </div>
      </div>
    </section>

    <!-- 数据概览与快捷操作 -->
    <div class="bottom-section">
      <!-- 近期活动 -->
      <section class="activity-section">
        <div class="section-header">
          <h2>近期活动</h2>
          <el-button text type="primary" @click="navigateTo('/dashboard/activity')">
            查看全部
            <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
        <div class="activity-list">
          <div
            v-for="(activity, index) in recentActivities"
            :key="index"
            class="activity-item"
            :style="{ animationDelay: `${index * 0.1}s` }"
          >
            <div class="activity-dot" :class="activity.type"/>
            <div class="activity-content">
              <p class="activity-title">{{ activity.title }}</p>
              <p class="activity-time">{{ activity.time }}</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 快捷操作 -->
      <section class="quick-actions">
        <div class="section-header">
          <h2>快捷操作</h2>
        </div>
        <div class="action-list">
          <div
            v-for="(action, index) in quickActions"
            :key="index"
            class="action-item"
            :style="{ animationDelay: `${index * 0.1}s` }"
            @click="navigateTo(action.path)"
          >
            <div class="action-icon" :style="{ color: action.color }">
              <el-icon :size="20">
                <component :is="action.icon" />
              </el-icon>
            </div>
            <span>{{ action.name }}</span>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup name="Welecome">
  import { ref, computed, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import useUserStore from '@/store/modules/user';

  const router = useRouter();
  const userStore = useUserStore();

  const greetingText = computed(() => {
    const hour = new Date().getHours();

    if (hour < 6) {return '凌晨好'}
    if (hour < 9) {return '早上好'}
    if (hour < 12) {return '上午好'}
    if (hour < 14) {return '中午好'}
    if (hour < 17) {return '下午好'}
    if (hour < 19) {return '傍晚好'}
    return '晚上好';
  });

  const metrics = ref([
    {
      key: 'customers',
      label: '客户总数',
      value: '--',
      icon: 'User',
      class: 'card-pink',
      trend: '持续增长',
      trendIcon: 'TrendCharts',
      trendClass: 'trend-up'
    },
    {
      key: 'clues',
      label: '线索数量',
      value: '--',
      icon: 'DataLine',
      class: 'card-orange',
      trend: '待跟进',
      trendIcon: 'Clock',
      trendClass: 'trend-warning'
    },
    {
      key: 'activities',
      label: '市场活动',
      value: '--',
      icon: 'Promotion',
      class: 'card-green',
      trend: '进行中',
      trendIcon: 'VideoPlay',
      trendClass: 'trend-info'
    },
    {
      key: 'transactions',
      label: '交易金额',
      value: '--',
      icon: 'Wallet',
      class: 'card-purple',
      trend: '本月目标',
      trendIcon: 'Aim',
      trendClass: 'trend-up'
    }
  ]);

  const functions = ref([
    {
      name: '市场活动',
      desc: '管理营销推广活动',
      icon: 'Promotion',
      path: '/dashboard/activity',
      color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
    },
    {
      name: '线索管理',
      desc: '录入和跟踪销售线索',
      icon: 'DataLine',
      path: '/dashboard/clue',
      color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
    },
    {
      name: '客户管理',
      desc: '维护客户关系信息',
      icon: 'User',
      path: '/dashboard/customer',
      color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
    },
    {
      name: '数据分析',
      desc: '查看销售统计报表',
      icon: 'TrendCharts',
      path: '/dashboard/statistic',
      color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)'
    },
    {
      name: '用户管理',
      desc: '管理系统用户账号',
      icon: 'UserFilled',
      path: '/dashboard/user',
      color: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)'
    }
  ]);

  const recentActivities = ref([
    { title: '系统已就绪，欢迎使用', time: '刚刚', type: 'success' },
    { title: '百度推广活动进行中', time: '进行中', type: 'primary' },
    { title: '抖音短视频广告推广', time: '进行中', type: 'warning' },
    { title: '充话费送手机活动', time: '已结束', type: 'info' }
  ]);

  const quickActions = ref([
    { name: '录入线索', icon: 'Plus', path: '/dashboard/clue', color: '#409eff' },
    { name: '新建活动', icon: 'DocumentAdd', path: '/dashboard/activity', color: '#67c23a' },
    { name: '查看客户', icon: 'User', path: '/dashboard/customer', color: '#e6a23c' },
    { name: '数据报表', icon: 'DataAnalysis', path: '/dashboard/statistic', color: '#f56c6c' }
  ]);

  const navigateTo = (path) => {
    router.push(path);
  };

  onMounted(() => {
    // 可以在这里调用API获取真实数据
    // api.statistic.getSummary().then(res => {
    //   if (res.code === 200) {
    //     // 更新metrics数据
    //   }
    // })
  });
</script>

<style lang="scss" scoped>
$primary-color: #409eff;
$dark-blue: #1e3a8a;
$success-color: #67c23a;
$warning-color: #e6a23c;
$danger-color: #f56c6c;
$info-color: #909399;

$card-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
$card-hover-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
$card-radius: 12px;

.crm-welcome {
  padding: 0;
}

/* 欢迎横幅 */
.welcome-banner {
  display: flex;
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 50%, #60a5fa 100%);
  border-radius: $card-radius;
  padding: 40px;
  margin-bottom: 24px;
  position: relative;
  overflow: hidden;
  animation: fadeInUp 0.6s ease-out;

  &::before {
    content: '';
    position: absolute;
    top: -50%;
    right: -10%;
    width: 400px;
    height: 400px;
    background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
    border-radius: 50%;
  }

  .welcome-text {
    flex: 1;
    color: white;
    z-index: 1;

    .welcome-greeting {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 12px;
      font-size: 1rem;
      opacity: 0.9;

      .greeting-name {
        font-weight: 600;
      }
    }

    h2 {
      font-size: 2rem;
      font-weight: 700;
      margin-bottom: 12px;
      animation: slideInLeft 0.8s ease-out;
    }

    p {
      font-size: 1.1rem;
      opacity: 0.9;
      margin-bottom: 24px;
      max-width: 500px;
      animation: slideInLeft 0.9s ease-out;
    }

    .welcome-actions {
      display: flex;
      gap: 12px;
      animation: slideInLeft 1s ease-out;

      .el-button {
        border-radius: 8px;
        font-weight: 500;

        &.el-button--primary {
          background: white;
          border-color: white;
          color: $primary-color;

          &:hover {
            background: #f0f0f0;
            border-color: #f0f0f0;
          }
        }

        &:not(.el-button--primary) {
          background: rgba(255, 255, 255, 0.2);
          border-color: rgba(255, 255, 255, 0.3);
          color: white;

          &:hover {
            background: rgba(255, 255, 255, 0.3);
          }
        }
      }
    }
  }

  .welcome-graphic {
    flex: 0 0 200px;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;

    .graphic-circle {
      position: absolute;
      border-radius: 50%;
      border: 2px solid rgba(255, 255, 255, 0.2);

      &.circle-1 {
        width: 180px;
        height: 180px;
        animation: pulse 3s ease-in-out infinite;
      }

      &.circle-2 {
        width: 140px;
        height: 140px;
        animation: pulse 3s ease-in-out infinite 0.5s;
      }

      &.circle-3 {
        width: 100px;
        height: 100px;
        animation: pulse 3s ease-in-out infinite 1s;
      }
    }

    .graphic-icon {
      position: relative;
      z-index: 1;
      color: white;
      animation: float 3s ease-in-out infinite;
    }
  }
}

/* 核心指标卡片 */
.metrics-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;

  .metric-card {
    background: white;
    border-radius: $card-radius;
    padding: 20px;
    display: flex;
    align-items: center;
    gap: 16px;
    box-shadow: $card-shadow;
    transition: all 0.3s ease;
    animation: fadeInUp 0.6s ease-out both;
    cursor: pointer;

    &:hover {
      transform: translateY(-4px);
      box-shadow: $card-hover-shadow;
    }

    .metric-icon {
      width: 56px;
      height: 56px;
      border-radius: 12px;
      display: flex;
      justify-content: center;
      align-items: center;
      color: white;
      flex-shrink: 0;
    }

    .metric-info {
      h3 {
        font-size: 0.9rem;
        color: #909399;
        margin-bottom: 4px;
        font-weight: 500;
      }

      .metric-value {
        font-size: 1.75rem;
        font-weight: 700;
        color: #303133;
        margin-bottom: 4px;
      }

      .metric-trend {
        display: flex;
        align-items: center;
        gap: 4px;
        font-size: 0.8rem;
        font-weight: 500;

        &.trend-up {
          color: $success-color;
        }

        &.trend-warning {
          color: $warning-color;
        }

        &.trend-info {
          color: $primary-color;
        }
      }
    }

    &.card-pink .metric-icon {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }

    &.card-orange .metric-icon {
      background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
    }

    &.card-green .metric-icon {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }

    &.card-purple .metric-icon {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
  }
}

/* 功能导航区 */
.function-section {
  background: white;
  border-radius: $card-radius;
  padding: 24px;
  box-shadow: $card-shadow;
  margin-bottom: 24px;
  animation: fadeInUp 0.8s ease-out;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h2 {
      font-size: 1.2rem;
      font-weight: 600;
      color: #303133;
    }

    .section-desc {
      color: #909399;
      font-size: 0.9rem;
    }
  }

  .function-grid {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 16px;

    .function-card {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 16px;
      border-radius: 10px;
      background: #f8fafc;
      cursor: pointer;
      transition: all 0.3s ease;
      animation: fadeInUp 0.6s ease-out both;

      &:hover {
        background: #f0f7ff;
        transform: translateX(4px);

        .function-arrow {
          opacity: 1;
          transform: translateX(0);
        }
      }

      .function-icon {
        width: 44px;
        height: 44px;
        border-radius: 10px;
        display: flex;
        justify-content: center;
        align-items: center;
        color: white;
        flex-shrink: 0;
      }

      .function-info {
        flex: 1;
        min-width: 0;

        h4 {
          font-size: 0.95rem;
          font-weight: 600;
          color: #303133;
          margin-bottom: 2px;
        }

        p {
          font-size: 0.8rem;
          color: #909399;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }

      .function-arrow {
        color: #c0c4cc;
        opacity: 0;
        transform: translateX(-8px);
        transition: all 0.3s ease;
      }
    }
  }
}

/* 底部区域 */
.bottom-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;

  .activity-section,
  .quick-actions {
    background: white;
    border-radius: $card-radius;
    padding: 24px;
    box-shadow: $card-shadow;
    animation: fadeInUp 1s ease-out;
  }

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h2 {
      font-size: 1.2rem;
      font-weight: 600;
      color: #303133;
    }
  }
}

/* 活动列表 */
.activity-list {
  .activity-item {
    display: flex;
    align-items: flex-start;
    gap: 12px;
    padding: 12px 0;
    border-bottom: 1px solid #f2f3f5;
    animation: fadeInUp 0.6s ease-out both;

    &:last-child {
      border-bottom: none;
    }

    .activity-dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      margin-top: 6px;
      flex-shrink: 0;

      &.success {
        background: $success-color;
      }

      &.primary {
        background: $primary-color;
      }

      &.warning {
        background: $warning-color;
      }

      &.info {
        background: $info-color;
      }
    }

    .activity-content {
      flex: 1;

      .activity-title {
        font-size: 0.95rem;
        color: #303133;
        margin-bottom: 4px;
      }

      .activity-time {
        font-size: 0.8rem;
        color: #909399;
      }
    }
  }
}

/* 快捷操作 */
.action-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;

  .action-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 14px 16px;
    border-radius: 8px;
    background: #f8fafc;
    cursor: pointer;
    transition: all 0.3s ease;
    animation: fadeInUp 0.6s ease-out both;

    &:hover {
      background: #f0f7ff;
      transform: translateY(-2px);
    }

    .action-icon {
      display: flex;
      align-items: center;
      justify-content: center;
    }

    span {
      font-size: 0.9rem;
      font-weight: 500;
      color: #303133;
    }
  }
}

/* 动画关键帧 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 0.6;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.3;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .metrics-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .function-section .function-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .welcome-banner {
    flex-direction: column;
    gap: 24px;

    .welcome-graphic {
      display: none;
    }
  }

  .metrics-grid {
    grid-template-columns: 1fr;
  }

  .function-section .function-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .bottom-section {
    grid-template-columns: 1fr;
  }
}
</style>
