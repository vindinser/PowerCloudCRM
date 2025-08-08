<template>
  <div class="crm-welcome">
    <!-- 顶部导航栏 -->
    <header class="crm-header">
      <div class="logo">
        <i class="fas fa-chart-line"/>
        <h1>PowerCloudCRM</h1>
      </div>
      <div class="user-info">
        <span class="username">{{ `Welecome，${ userStore.userInfo.name }` }}</span>
        <div class="avatar">
          <i class="fas fa-user-circle"/>
        </div>
      </div>
    </header>

    <!-- 主体内容区 -->
    <main>
      <!-- 欢迎横幅 -->
      <section class="welcome-banner">
        <div class="welcome-text">
          <h2>欢迎使用 PowerCloudCRM</h2>
          <p>全面管理您的客户关系，提高销售效率，优化客户体验</p>
          <button class="btn-primary">立即开始 <i class="fas fa-arrow-right"/></button>
        </div>
        <div class="welcome-graphic">
          <div class="graphic-element"/>
        </div>
      </section>

      <!-- 核心指标卡片 -->
      <section class="metrics-grid">
        <div class="metric-card card-accent1">
          <div class="metric-icon">
            <i class="fas fa-users"/>
          </div>
          <div class="metric-info">
            <h3>客户总数</h3>
            <p>1,248</p>
            <div class="metric-trend">
              <i class="fas fa-arrow-up"/>
              <span>12% 增长</span>
            </div>
          </div>
        </div>

        <div class="metric-card card-accent2">
          <div class="metric-icon">
            <i class="fas fa-clipboard-list"/>
          </div>
          <div class="metric-info">
            <h3>本月销售</h3>
            <p>¥246,890</p>
            <div class="metric-trend">
              <i class="fas fa-chart-line"/>
              <span>接近目标</span>
            </div>
          </div>
        </div>

        <div class="metric-card card-accent3">
          <div class="metric-icon">
            <i class="fas fa-tasks"/>
          </div>
          <div class="metric-info">
            <h3>待办事项</h3>
            <p>17</p>
            <div class="metric-trend">
              <i class="fas fa-clock"/>
              <span>3项紧急</span>
            </div>
          </div>
        </div>

        <div class="metric-card card-accent4">
          <div class="metric-icon">
            <i class="fas fa-handshake"/>
          </div>
          <div class="metric-info">
            <h3>成交率</h3>
            <p>68%</p>
            <div class="metric-trend">
              <i class="fas fa-check-circle"/>
              <span>行业领先</span>
            </div>
          </div>
        </div>
      </section>

      <!-- 快捷导航 -->
      <section class="quick-nav">
        <h2>常用功能</h2>
        <div class="nav-grid">
          <div class="nav-item">
            <i class="fas fa-user-plus"/>
            <span>添加客户</span>
          </div>
          <div class="nav-item">
            <i class="fas fa-calendar-check"/>
            <span>预约安排</span>
          </div>
          <div class="nav-item">
            <i class="fas fa-file-invoice"/>
            <span>销售分析</span>
          </div>
          <div class="nav-item">
            <i class="fas fa-chart-pie"/>
            <span>数据报告</span>
          </div>
          <div class="nav-item">
            <i class="fas fa-bullhorn"/>
            <span>营销活动</span>
          </div>
          <div class="nav-item">
            <i class="fas fa-cog"/>
            <span>系统设置</span>
          </div>
        </div>
      </section>

      <!-- 活动时间线 -->
      <section class="activity-timeline">
        <div class="section-header">
          <h2>近期活动</h2>
          <a href="#">查看全部</a>
        </div>
        <div class="timeline">
          <div class="timeline-item">
            <div class="timeline-badge success"/>
            <div class="timeline-content">
              <p class="activity-title">签约了新客户：华兴科技</p>
              <p class="activity-time">今天 09:42</p>
            </div>
          </div>
          <div class="timeline-item">
            <div class="timeline-badge primary"/>
            <div class="timeline-content">
              <p class="activity-title">李明添加了新销售线索</p>
              <p class="activity-time">昨天 16:30</p>
            </div>
          </div>
          <div class="timeline-item">
            <div class="timeline-badge warning"/>
            <div class="timeline-content">
              <p class="activity-title">张经理跟进：3天后联系客户</p>
              <p class="activity-time">昨天 14:15</p>
            </div>
          </div>
          <div class="timeline-item">
            <div class="timeline-badge info"/>
            <div class="timeline-content">
              <p class="activity-title">系统更新至V2.3.0版本</p>
              <p class="activity-time">前天 10:00</p>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup name="Welecome">
  import useUserStore from '@/store/modules/user';

  const userStore = useUserStore();

</script>

<style lang="scss" scoped>
/* 基础样式 */
$primary-color: #2563eb;
$dark-blue: #1e3a8a;
$success-color: #10b981;
$warning-color: #f59e0b;
$info-color: #3b82f6;

$card-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
$card-radius: 16px;

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  background-color: #f8fafc;
  font-family: 'Segoe UI', 'Microsoft YaHei', sans-serif;
}

.crm-welcome {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

/* 顶部导航栏 */
.crm-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 25px;
  background: linear-gradient(135deg, $dark-blue 0%, $primary-color 100%);
  color: white;
  border-radius: $card-radius;
  box-shadow: $card-shadow;
  margin-bottom: 30px;

  .logo {
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 1.5rem;
    font-weight: 700;

    i {
      font-size: 1.8rem;
    }
  }

  .user-info {
    display: flex;
    align-items: center;
    gap: 15px;

    .username {
      font-size: 1.1rem;
      font-weight: 500;
    }

    .avatar {
      font-size: 2rem;
      cursor: pointer;
      transition: transform 0.3s;

      &:hover {
        transform: scale(1.1);
      }
    }
  }
}

/* 欢迎横幅 */
.welcome-banner {
  display: flex;
  background: white;
  border-radius: $card-radius;
  padding: 40px;
  box-shadow: $card-shadow;
  margin-bottom: 30px;

  .welcome-text {
    flex: 1;

    h2 {
      font-size: 2.5rem;
      font-weight: 700;
      margin-bottom: 15px;
      color: $dark-blue;
    }

    p {
      font-size: 1.2rem;
      color: #64748b;
      margin-bottom: 30px;
      max-width: 600px;
    }

    .btn-primary {
      background: $primary-color;
      color: white;
      border: none;
      padding: 14px 32px;
      border-radius: 12px;
      font-size: 1.1rem;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.3s;
      box-shadow: 0 4px 6px rgba(37, 99, 235, 0.3);

      i {
        margin-left: 8px;
        transition: transform 0.3s;
      }

      &:hover {
        background: $dark-blue;
        box-shadow: 0 6px 12px rgba(37, 99, 235, 0.4);

        i {
          transform: translateX(3px);
        }
      }
    }
  }

  .welcome-graphic {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;

    .graphic-element {
      width: 220px;
      height: 220px;
      background: linear-gradient(135deg, rgba($primary-color, 0.1) 0%, rgba($success-color, 0.15) 100%);
      border-radius: 50%;
      position: relative;
      display: flex;
      justify-content: center;
      align-items: center;

      &::before, &::after {
        content: "";
        position: absolute;
        border-radius: 50%;
      }

      &::before {
        width: 160px;
        height: 160px;
        background: rgba($primary-color, 0.08);
      }

      &::after {
        width: 100px;
        height: 100px;
        background: rgba($primary-color, 0.05);
      }
    }
  }
}

/* 数据指标卡片 */
.metrics-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 30px;

  .metric-card {
    background: white;
    border-radius: $card-radius;
    padding: 25px;
    display: flex;
    align-items: center;
    gap: 20px;
    box-shadow: $card-shadow;
    transition: transform 0.3s;

    &:hover {
      transform: translateY(-5px);
    }

    .metric-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 1.5rem;
      color: white;
    }

    .metric-info {
      h3 {
        font-size: 1.1rem;
        font-weight: 600;
        color: #64748b;
        margin-bottom: 5px;
      }

      p {
        font-size: 2rem;
        font-weight: 700;
        color: #1e293b;
        margin-bottom: 5px;
      }

      .metric-trend {
        display: flex;
        align-items: center;
        gap: 5px;
        font-size: 0.9rem;
        font-weight: 500;
      }
    }
  }

  .card-accent1 {
    .metric-icon {
      background: linear-gradient(135deg, #ec4899 0%, #f43f5e 100%);
    }
    .metric-trend {
      color: #ec4899;
    }
  }

  .card-accent2 {
    .metric-icon {
      background: linear-gradient(135deg, $warning-color 0%, #f97316 100%);
    }
    .metric-trend {
      color: $warning-color;
    }
  }

  .card-accent3 {
    .metric-icon {
      background: linear-gradient(135deg, $success-color 0%, #22c55e 100%);
    }
    .metric-trend {
      color: $success-color;
    }
  }

  .card-accent4 {
    .metric-icon {
      background: linear-gradient(135deg, #8b5cf6 0%, #6366f1 100%);
    }
    .metric-trend {
      color: #8b5cf6;
    }
  }
}

/* 快捷导航 */
.quick-nav {
  background: white;
  border-radius: $card-radius;
  padding: 30px;
  box-shadow: $card-shadow;
  margin-bottom: 30px;

  h2 {
    font-size: 1.5rem;
    font-weight: 700;
    margin-bottom: 25px;
    color: #1e293b;
  }

  .nav-grid {
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: 15px;

    .nav-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 12px;
      padding: 20px 0;
      border-radius: 12px;
      cursor: pointer;
      transition: all 0.3s;
      background: #f8fafc;

      i {
        font-size: 1.8rem;
        color: $primary-color;
      }

      span {
        font-weight: 500;
        color: #475569;
      }

      &:hover {
        background: rgba($primary-color, 0.1);
        transform: translateY(-3px);

        i {
          color: $dark-blue;
        }
      }
    }
  }
}

/* 活动时间线 */
.activity-timeline {
  background: white;
  border-radius: $card-radius;
  padding: 30px;
  box-shadow: $card-shadow;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 25px;

    h2 {
      font-size: 1.5rem;
      font-weight: 700;
      color: #1e293b;
    }

    a {
      color: $primary-color;
      text-decoration: none;
      font-weight: 500;

      &:hover {
        text-decoration: underline;
      }
    }
  }

  .timeline {
    .timeline-item {
      display: flex;
      position: relative;
      padding-bottom: 30px;

      &:last-child {
        padding-bottom: 10px;
        &::after {
          display: none;
        }
      }

      &::after {
        content: "";
        position: absolute;
        left: 26px;
        top: 36px;
        height: calc(100% - 15px);
        width: 2px;
        background: #e2e8f0;
      }

      .timeline-badge {
        width: 14px;
        height: 14px;
        border-radius: 50%;
        margin-right: 20px;
        flex-shrink: 0;
        position: relative;
        top: 5px;
        z-index: 1;
      }

      .success {
        background: $success-color;
      }

      .primary {
        background: $primary-color;
      }

      .warning {
        background: $warning-color;
      }

      .info {
        background: $info-color;
      }

      .timeline-content {
        flex: 1;

        .activity-title {
          font-size: 1.1rem;
          font-weight: 500;
          margin-bottom: 5px;
          color: #1e293b;
        }

        .activity-time {
          font-size: 0.9rem;
          color: #94a3b8;
        }
      }
    }
  }
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .metrics-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .quick-nav .nav-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .welcome-banner {
    flex-direction: column;
    gap: 30px;
  }

  .metrics-grid {
    grid-template-columns: 1fr;
  }

  .quick-nav .nav-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>