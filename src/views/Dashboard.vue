<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-card class="stats-card">
        <div class="card-header">本周清洗任务</div>
        <div class="card-value">1,284</div>
        <div class="card-change increase">
          ↑ 12% 增长
        </div>
      </el-card>

      <el-card class="stats-card">
        <div class="card-header">清洗成功率</div>
        <div class="card-value">98.6%</div>
        <div class="card-change increase">
          ↑ 2.3% 增长
        </div>
      </el-card>

      <el-card class="stats-card">
        <div class="card-header">异常数据</div>
        <div class="card-value">36</div>
        <div class="card-change decrease">
          ↑ 4% 增长
        </div>
      </el-card>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <el-card class="chart-card trend-chart">
        <div class="chart-header">
          <div class="chart-title">数据清洗趋势</div>
          <div class="chart-actions">
            <el-radio-group v-model="timeRange" size="small">
              <el-radio-button label="周">周</el-radio-button>
              <el-radio-button label="月">月</el-radio-button>
              <el-radio-button label="年">年</el-radio-button>
            </el-radio-group>
          </div>
        </div>
        <div class="chart-container" ref="trendChartRef" />
      </el-card>

      <el-card class="chart-card quality-chart">
        <div class="chart-header">
          <div class="chart-title">数据质量分布</div>
          <div class="chart-actions">
            <el-button size="small" type="primary" plain>导出报告</el-button>
          </div>
        </div>
        <div class="chart-container" ref="qualityChartRef" />
      </el-card>
    </div>

    <!-- 最近任务 -->
    <div class="recent-tasks-section">
      <div class="section-header">
        <div class="section-title">最近任务</div>
        <el-button type="primary">
          ➕ 新建任务
        </el-button>
      </div>

      <el-table :data="recentTasks" style="width: 100%">
        <el-table-column prop="name" label="任务名称" />
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="endTime" label="结束时间" width="180" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default>
            <el-button link type="primary" size="small">查看</el-button>
            <el-button link type="primary" size="small">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import * as echarts from 'echarts';

const timeRange = ref('周');
const trendChartRef = ref<HTMLElement | null>(null);
const qualityChartRef = ref<HTMLElement | null>(null);

const recentTasks = ref([
  {
    name: '商品数据批量清洗',
    startTime: '2025-04-25 10:30:00',
    endTime: '2025-04-25 11:45:20',
    status: '已完成'
  },
  {
    name: '淘宝数据爬取与清洗',
    startTime: '2025-04-26 09:15:00',
    endTime: '-',
    status: '进行中'
  }
]);

const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    '已完成': 'success',
    '进行中': 'primary',
    '失败': 'danger',
    '等待中': 'info'
  };
  return statusMap[status] || 'info';
};

// 初始化趋势图表
const initTrendChart = () => {
  if (!trendChartRef.value) return;

  const chart = echarts.init(trendChartRef.value);

  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['清洗任务数', '异常数据量']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '清洗任务数',
        type: 'line',
        data: [150, 230, 224, 218, 135, 147, 260],
        smooth: true,
        lineStyle: {
          width: 3,
          color: '#409EFF'
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: 'rgba(64, 158, 255, 0.2)'
              },
              {
                offset: 1,
                color: 'rgba(64, 158, 255, 0)'
              }
            ]
          }
        }
      },
      {
        name: '异常数据量',
        type: 'line',
        data: [5, 8, 3, 12, 6, 2, 10],
        smooth: true,
        lineStyle: {
          width: 3,
          color: '#F56C6C'
        }
      }
    ]
  };

  chart.setOption(option);

  window.addEventListener('resize', () => {
    chart.resize();
  });
};

// 初始化质量图表
const initQualityChart = () => {
  if (!qualityChartRef.value) return;

  const chart = echarts.init(qualityChartRef.value);

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center',
      data: ['优质数据', '良好数据', '一般数据', '差质数据', '无效数据']
    },
    series: [
      {
        name: '数据质量',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 735, name: '优质数据', itemStyle: { color: '#67C23A' } },
          { value: 580, name: '良好数据', itemStyle: { color: '#409EFF' } },
          { value: 300, name: '一般数据', itemStyle: { color: '#E6A23C' } },
          { value: 150, name: '差质数据', itemStyle: { color: '#F56C6C' } },
          { value: 50, name: '无效数据', itemStyle: { color: '#909399' } }
        ]
      }
    ]
  };

  chart.setOption(option);

  window.addEventListener('resize', () => {
    chart.resize();
  });
};

onMounted(() => {
  initTrendChart();
  initQualityChart();
});
</script>

<style scoped>
.dashboard {
  width: 100%;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stats-card {
  border-radius: 8px;
}

.card-header {
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
}

.card-value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 10px;
}

.card-change {
  display: flex;
  align-items: center;
  font-size: 12px;
}

.card-change.increase {
  color: #67C23A;
}

.card-change.decrease {
  color: #F56C6C;
}

.charts-section {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.chart-card {
  border-radius: 8px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.chart-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.chart-container {
  height: 300px;
}

.recent-tasks-section {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}
</style>
