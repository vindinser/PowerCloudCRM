<!-- 数据分析 -->
<template>
  <div class="dashboard">
    <div class="dashboard-header">
      <h2 class="dashboard-title">CRM 数据可视化大屏</h2>
      <div class="dashboard-time">{{ currentTime }}</div>
    </div>

    <!-- 概览统计 -->
    <div class="stat-cards">
      <div class="stat-card stat-card--activity">
        <div class="stat-card__icon">
          <el-icon :size="32"><DataAnalysis /></el-icon>
        </div>
        <div class="stat-card__info">
          <div class="stat-card__value">
            <span class="stat-card__num">{{ summaryData.effectiveActivityCount || 0 }}</span>
            <span class="stat-card__suffix">/{{ summaryData.totalActivityCount || 0 }}</span>
          </div>
          <div class="stat-card__label">市场活动</div>
        </div>
        <div class="stat-card__trend up">+12.5%</div>
      </div>

      <div class="stat-card stat-card--clue">
        <div class="stat-card__icon">
          <el-icon :size="32"><Connection /></el-icon>
        </div>
        <div class="stat-card__info">
          <div class="stat-card__value">
            <span class="stat-card__num">{{ summaryData.totalClueCount || 0 }}</span>
          </div>
          <div class="stat-card__label">线索总数</div>
        </div>
        <div class="stat-card__trend up">+8.3%</div>
      </div>

      <div class="stat-card stat-card--customer">
        <div class="stat-card__icon">
          <el-icon :size="32"><User /></el-icon>
        </div>
        <div class="stat-card__info">
          <div class="stat-card__value">
            <span class="stat-card__num">{{ summaryData.totalCustomerCount || 0 }}</span>
          </div>
          <div class="stat-card__label">客户总数</div>
        </div>
        <div class="stat-card__trend up">+5.7%</div>
      </div>

      <div class="stat-card stat-card--trade">
        <div class="stat-card__icon">
          <el-icon :size="32"><Money /></el-icon>
        </div>
        <div class="stat-card__info">
          <div class="stat-card__value">
            <span class="stat-card__num">{{ summaryData.successTranAmount || 0 }}</span>
            <span class="stat-card__suffix">/{{ summaryData.totalTranAmount || 0 }}</span>
          </div>
          <div class="stat-card__label">交易总额（万元）</div>
        </div>
        <div class="stat-card__trend up">+18.2%</div>
      </div>
    </div>

    <!-- 第二行图表 -->
    <div class="chart-row">
      <div class="chart-panel">
        <div class="chart-panel__title">
          <span class="chart-panel__dot" />
          销售漏斗图
        </div>
        <div id="saleFunnelChart" class="chart-panel__body" />
      </div>

      <div class="chart-panel chart-panel--wide">
        <div class="chart-panel__title">
          <span class="chart-panel__dot" />
          月度销售趋势
        </div>
        <div id="monthlyTrendChart" class="chart-panel__body" />
      </div>

      <div class="chart-panel">
        <div class="chart-panel__title">
          <span class="chart-panel__dot" />
          线索来源统计
        </div>
        <div id="sourcePieChart" class="chart-panel__body" />
      </div>
    </div>

    <!-- 第三行图表 -->
    <div class="chart-row">
      <div class="chart-panel">
        <div class="chart-panel__title">
          <span class="chart-panel__dot" />
          员工业绩排行
        </div>
        <div id="staffRankChart" class="chart-panel__body" />
      </div>

      <div class="chart-panel">
        <div class="chart-panel__title">
          <span class="chart-panel__dot" />
          客户转化率
        </div>
        <div id="conversionGauge" class="chart-panel__body" />
      </div>

      <div class="chart-panel">
        <div class="chart-panel__title">
          <span class="chart-panel__dot" />
          活动类型分布
        </div>
        <div id="activityBarChart" class="chart-panel__body" />
      </div>

      <div class="chart-panel">
        <div class="chart-panel__title">
          <span class="chart-panel__dot" />
          各区域客户分布
        </div>
        <div id="regionChart" class="chart-panel__body" />
      </div>
    </div>
  </div>
</template>

<script setup name="Static">
  import { getSummary, getSaleFunnel, getSource } from '@/api/statistic.js';
  import * as echarts from 'echarts';

  let summaryData = ref({});
  let currentTime = ref('');

  const updateTime = () => {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');

    currentTime.value = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  };

  const darkThemeColors = {
    bg: '#0f1923',
    panelBg: '#132a3e',
    border: '#1e4d7b',
    textPrimary: '#e0e6ed',
    textSecondary: '#8a9bb5',
    accent: '#00d4ff',
    accentGreen: '#00e396',
    accentOrange: '#feb019',
    accentPink: '#ff4560',
    accentPurple: '#9b59b6',
    accentBlue: '#4facfe'
  };

  const summaryDataApi = () => {
    getSummary().then(res => {
      if (res.code === 200) {
        summaryData.value = res.data;
      }
    });
  };

  const loadSaleFunnelChart = async () => {
    const res = await getSaleFunnel();

    if (res.code === 200) {
      const chartDom = document.getElementById('saleFunnelChart');
      const chart = echarts.init(chartDom);

      chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c}',
          backgroundColor: 'rgba(15, 25, 35, 0.9)',
          borderColor: darkThemeColors.border,
          textStyle: { color: darkThemeColors.textPrimary }
        },
        legend: {
          data: ['线索', '客户', '交易', '成交'],
          bottom: 10,
          textStyle: { color: darkThemeColors.textSecondary }
        },
        series: [
          {
            name: '销售漏斗数据统计',
            type: 'funnel',
            left: '10%',
            top: 30,
            bottom: 50,
            width: '80%',
            min: 0,
            max: 100,
            minSize: '0%',
            maxSize: '100%',
            sort: 'descending',
            gap: 4,
            label: {
              show: true,
              position: 'inside',
              color: '#fff',
              fontSize: 13
            },
            labelLine: {
              length: 10,
              lineStyle: { width: 1, type: 'solid' }
            },
            itemStyle: {
              borderColor: darkThemeColors.panelBg,
              borderWidth: 2
            },
            emphasis: {
              label: { fontSize: 16 }
            },
            data: res.ListData,
            color: ['#4facfe', '#00d4ff', '#00e396', '#feb019']
          }
        ]
      });
    }
  };

  const loadSourcePieChart = async () => {
    const res = await getSource();

    if (res.code === 200) {
      const chartDom = document.getElementById('sourcePieChart');
      const chart = echarts.init(chartDom);

      chart.setOption({
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(15, 25, 35, 0.9)',
          borderColor: darkThemeColors.border,
          textStyle: { color: darkThemeColors.textPrimary }
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          textStyle: { color: darkThemeColors.textSecondary }
        },
        series: [
          {
            name: '线索来源统计',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['40%', '50%'],
            avoidLabelOverlap: false,
            label: {
              show: false
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 14,
                fontWeight: 'bold',
                color: '#fff'
              },
              itemStyle: {
                shadowBlur: 15,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 212, 255, 0.4)'
              }
            },
            data: res.ListData,
            color: ['#4facfe', '#00d4ff', '#00e396', '#feb019', '#ff4560', '#9b59b6']
          }
        ]
      });
    }
  };

  const loadMonthlyTrendChart = () => {
    const chartDom = document.getElementById('monthlyTrendChart');
    const chart = echarts.init(chartDom);
    const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'];

    chart.setOption({
      tooltip: {
        trigger: 'axis',
        backgroundColor: 'rgba(15, 25, 35, 0.9)',
        borderColor: darkThemeColors.border,
        textStyle: { color: darkThemeColors.textPrimary }
      },
      legend: {
        data: ['线索量', '客户量', '成交额'],
        top: 5,
        textStyle: { color: darkThemeColors.textSecondary }
      },
      grid: {
        left: 50,
        right: 30,
        bottom: 30,
        top: 40
      },
      xAxis: {
        type: 'category',
        data: months,
        axisLine: { lineStyle: { color: darkThemeColors.border } },
        axisLabel: { color: darkThemeColors.textSecondary }
      },
      yAxis: [
        {
          type: 'value',
          name: '数量',
          axisLine: { lineStyle: { color: darkThemeColors.border } },
          axisLabel: { color: darkThemeColors.textSecondary },
          splitLine: { lineStyle: { color: 'rgba(30, 77, 123, 0.3)' } }
        },
        {
          type: 'value',
          name: '金额(万)',
          axisLine: { lineStyle: { color: darkThemeColors.border } },
          axisLabel: { color: darkThemeColors.textSecondary },
          splitLine: { show: false }
        }
      ],
      series: [
        {
          name: '线索量',
          type: 'bar',
          barWidth: 15,
          data: [320, 280, 350, 420, 380, 450, 510, 480, 520, 580, 610, 650],
          itemStyle: {
            borderRadius: [4, 4, 0, 0],
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#4facfe' },
              { offset: 1, color: 'rgba(79, 172, 254, 0.3)' }
            ])
          }
        },
        {
          name: '客户量',
          type: 'bar',
          barWidth: 15,
          data: [120, 150, 180, 200, 170, 210, 250, 230, 280, 310, 340, 370],
          itemStyle: {
            borderRadius: [4, 4, 0, 0],
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#00e396' },
              { offset: 1, color: 'rgba(0, 227, 150, 0.3)' }
            ])
          }
        },
        {
          name: '成交额',
          type: 'line',
          yAxisIndex: 1,
          smooth: true,
          symbol: 'circle',
          symbolSize: 8,
          lineStyle: {
            color: '#feb019',
            width: 3,
            shadowBlur: 8,
            shadowColor: 'rgba(254, 176, 25, 0.4)'
          },
          itemStyle: { color: '#feb019' },
          data: [85, 110, 130, 160, 145, 180, 210, 195, 240, 280, 310, 350]
        }
      ]
    });
  };

  const loadStaffRankChart = () => {
    const chartDom = document.getElementById('staffRankChart');
    const chart = echarts.init(chartDom);
    const staffNames = ['张三', '李四', '王五', '赵六', '钱七', '孙八', '周九'];
    const staffData = [320, 280, 250, 220, 190, 160, 130];

    chart.setOption({
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
        backgroundColor: 'rgba(15, 25, 35, 0.9)',
        borderColor: darkThemeColors.border,
        textStyle: { color: darkThemeColors.textPrimary }
      },
      grid: {
        left: 60,
        right: 30,
        bottom: 15,
        top: 15
      },
      xAxis: {
        type: 'value',
        axisLine: { lineStyle: { color: darkThemeColors.border } },
        axisLabel: { color: darkThemeColors.textSecondary },
        splitLine: { lineStyle: { color: 'rgba(30, 77, 123, 0.3)' } }
      },
      yAxis: {
        type: 'category',
        data: staffNames.reverse(),
        axisLine: { lineStyle: { color: darkThemeColors.border } },
        axisLabel: { color: darkThemeColors.textPrimary }
      },
      series: [
        {
          type: 'bar',
          barWidth: 14,
          data: staffData.reverse(),
          label: {
            show: true,
            position: 'right',
            color: darkThemeColors.textSecondary,
            fontSize: 12
          },
          itemStyle: {
            borderRadius: [0, 4, 4, 0],
            color: function (params) {
              const colors = [
                '#ff4560', '#feb019', '#00e396', '#4facfe',
                '#00d4ff', '#9b59b6', '#e74c3c'
              ];

              return colors[params.dataIndex] || '#4facfe';
            }
          }
        }
      ]
    });
  };

  const loadConversionGauge = () => {
    const chartDom = document.getElementById('conversionGauge');
    const chart = echarts.init(chartDom);

    chart.setOption({
      series: [
        {
          type: 'gauge',
          startAngle: 220,
          endAngle: -40,
          min: 0,
          max: 100,
          radius: '85%',
          center: ['50%', '55%'],
          progress: {
            show: true,
            width: 14,
            roundCap: true,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: '#4facfe' },
                { offset: 1, color: '#00e396' }
              ])
            }
          },
          axisLine: {
            lineStyle: {
              width: 14,
              color: [[1, 'rgba(30, 77, 123, 0.4)']]
            },
            roundCap: true
          },
          axisTick: { show: false },
          splitLine: { show: false },
          axisLabel: { show: false },
          pointer: { show: false },
          anchor: { show: false },
          title: {
            show: true,
            offsetCenter: [0, '30%'],
            fontSize: 14,
            color: darkThemeColors.textSecondary
          },
          detail: {
            valueAnimation: true,
            fontSize: 36,
            fontWeight: 'bold',
            offsetCenter: [0, '-5%'],
            formatter: '{value}%',
            color: darkThemeColors.accent,
            textShadowColor: 'rgba(0, 212, 255, 0.3)',
            textShadowBlur: 10
          },
          data: [{ value: 68.5, name: '线索转化率' }]
        }
      ]
    });
  };

  const loadActivityBarChart = () => {
    const chartDom = document.getElementById('activityBarChart');
    const chart = echarts.init(chartDom);

    chart.setOption({
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
        backgroundColor: 'rgba(15, 25, 35, 0.9)',
        borderColor: darkThemeColors.border,
        textStyle: { color: darkThemeColors.textPrimary }
      },
      grid: {
        left: 50,
        right: 20,
        bottom: 30,
        top: 20
      },
      xAxis: {
        type: 'category',
        data: ['电话', '拜访', '会议', '邮件', '线上'],
        axisLine: { lineStyle: { color: darkThemeColors.border } },
        axisLabel: { color: darkThemeColors.textSecondary }
      },
      yAxis: {
        type: 'value',
        axisLine: { lineStyle: { color: darkThemeColors.border } },
        axisLabel: { color: darkThemeColors.textSecondary },
        splitLine: { lineStyle: { color: 'rgba(30, 77, 123, 0.3)' } }
      },
      series: [
        {
          type: 'bar',
          barWidth: 24,
          data: [
            { value: 180, itemStyle: { color: '#4facfe' } },
            { value: 120, itemStyle: { color: '#00e396' } },
            { value: 85, itemStyle: { color: '#feb019' } },
            { value: 95, itemStyle: { color: '#ff4560' } },
            { value: 140, itemStyle: { color: '#9b59b6' } }
          ],
          itemStyle: { borderRadius: [6, 6, 0, 0] },
          label: {
            show: true,
            position: 'top',
            color: darkThemeColors.textSecondary,
            fontSize: 12
          }
        }
      ]
    });
  };

  const loadRegionChart = () => {
    const chartDom = document.getElementById('regionChart');
    const chart = echarts.init(chartDom);

    chart.setOption({
      tooltip: {
        trigger: 'item',
        backgroundColor: 'rgba(15, 25, 35, 0.9)',
        borderColor: darkThemeColors.border,
        textStyle: { color: darkThemeColors.textPrimary }
      },
      legend: {
        orient: 'vertical',
        right: 5,
        top: 'center',
        textStyle: { color: darkThemeColors.textSecondary, fontSize: 11 }
      },
      series: [
        {
          type: 'pie',
          radius: ['30%', '65%'],
          center: ['35%', '50%'],
          roseType: 'radius',
          label: { show: false },
          emphasis: {
            label: {
              show: true,
              fontSize: 13,
              fontWeight: 'bold',
              color: '#fff'
            }
          },
          data: [
            { value: 420, name: '华东' },
            { value: 350, name: '华南' },
            { value: 280, name: '华北' },
            { value: 190, name: '西南' },
            { value: 150, name: '华中' },
            { value: 80, name: '其他' }
          ],
          color: ['#4facfe', '#00d4ff', '#00e396', '#feb019', '#ff4560', '#9b59b6']
        }
      ]
    });
  };

  onMounted(() => {
    updateTime();
    setInterval(updateTime, 1000);

    summaryDataApi();
    loadSaleFunnelChart();
    loadSourcePieChart();
    loadMonthlyTrendChart();
    loadStaffRankChart();
    loadConversionGauge();
    loadActivityBarChart();
    loadRegionChart();

    window.addEventListener('resize', () => {
      const chartIds = [
        'saleFunnelChart', 'sourcePieChart', 'monthlyTrendChart',
        'staffRankChart', 'conversionGauge', 'activityBarChart', 'regionChart'
      ];

      chartIds.forEach(id => {
        const dom = document.getElementById(id);

        if (dom) {
          const instance = echarts.getInstanceByDom(dom);

          if (instance) {instance.resize()}
        }
      });
    });
  });
</script>

<style lang="scss" scoped>
  .dashboard {
    min-height: 100vh;
    background: linear-gradient(135deg, #0a1628 0%, #0f1923 50%, #0a1628 100%);
    color: #e0e6ed;
    padding: 16px 20px;
  }

  .dashboard-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 0 8px;
  }

  .dashboard-title {
    font-size: 24px;
    font-weight: 700;
    background: linear-gradient(90deg, #4facfe, #00e396);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    letter-spacing: 4px;
  }

  .dashboard-time {
    font-size: 14px;
    color: #8a9bb5;
    font-family: 'Courier New', monospace;
  }

  .stat-cards {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
    margin-bottom: 16px;
  }

  .stat-card {
    background: linear-gradient(135deg, rgba(19, 42, 62, 0.8), rgba(19, 42, 62, 0.4));
    border: 1px solid rgba(30, 77, 123, 0.5);
    border-radius: 10px;
    padding: 18px 20px;
    display: flex;
    align-items: center;
    gap: 14px;
    position: relative;
    overflow: hidden;
    transition: transform 0.3s, box-shadow 0.3s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
    }

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 3px;
      border-radius: 10px 10px 0 0;
    }

    &--activity::before { background: linear-gradient(90deg, #4facfe, #00d4ff); }
    &--clue::before { background: linear-gradient(90deg, #00e396, #00d4ff); }
    &--customer::before { background: linear-gradient(90deg, #feb019, #ff9f43); }
    &--trade::before { background: linear-gradient(90deg, #ff4560, #ff6b81); }
  }

  .stat-card__icon {
    width: 52px;
    height: 52px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;

    .stat-card--activity & { background: rgba(79, 172, 254, 0.15); color: #4facfe; }
    .stat-card--clue & { background: rgba(0, 227, 150, 0.15); color: #00e396; }
    .stat-card--customer & { background: rgba(254, 176, 25, 0.15); color: #feb019; }
    .stat-card--trade & { background: rgba(255, 69, 96, 0.15); color: #ff4560; }
  }

  .stat-card__info {
    flex: 1;
  }

  .stat-card__value {
    display: flex;
    align-items: baseline;
    gap: 4px;
  }

  .stat-card__num {
    font-size: 26px;
    font-weight: 700;
    color: #fff;
  }

  .stat-card__suffix {
    font-size: 14px;
    color: #8a9bb5;
  }

  .stat-card__label {
    font-size: 13px;
    color: #8a9bb5;
    margin-top: 2px;
  }

  .stat-card__trend {
    font-size: 12px;
    font-weight: 600;
    padding: 2px 8px;
    border-radius: 10px;
    flex-shrink: 0;

    &.up {
      color: #00e396;
      background: rgba(0, 227, 150, 0.1);
    }

    &.down {
      color: #ff4560;
      background: rgba(255, 69, 96, 0.1);
    }
  }

  .chart-row {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
    margin-bottom: 16px;

    &:nth-child(3) {
      grid-template-columns: 1fr 1.5fr 1fr;

      .chart-panel--wide {
        grid-column: span 1;
      }
    }
  }

  .chart-panel {
    background: linear-gradient(135deg, rgba(19, 42, 62, 0.8), rgba(19, 42, 62, 0.4));
    border: 1px solid rgba(30, 77, 123, 0.5);
    border-radius: 10px;
    padding: 14px;
    display: flex;
    flex-direction: column;
  }

  .chart-panel__title {
    font-size: 14px;
    font-weight: 600;
    color: #e0e6ed;
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 10px;
    flex-shrink: 0;
  }

  .chart-panel__dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: #00d4ff;
    box-shadow: 0 0 8px rgba(0, 212, 255, 0.6);
  }

  .chart-panel__body {
    flex: 1;
    min-height: 240px;
  }
</style>
