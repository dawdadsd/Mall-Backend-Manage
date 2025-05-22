<template>
  <div class="dashboard">
    <!-- Top Stats Cards -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-title">总商品数</div>
          <div class="stat-value">{{ overviewStats.totalProducts.toLocaleString() }}</div>
          <div :class="['stat-trend', overviewStats.newProductsToday > 0 ? 'trend-up' : '']">
            <el-icon v-if="overviewStats.newProductsToday > 0"><Top /></el-icon>
            今日新增 {{ overviewStats.newProductsToday }}
        </div>
      </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-title">总用户/商家数</div>
          <div class="stat-value">{{ overviewStats.totalUsers.toLocaleString() }}</div>
           <div :class="['stat-trend', overviewStats.newUsersToday > 0 ? 'trend-up' : '']">
             <el-icon v-if="overviewStats.newUsersToday > 0"><Top /></el-icon>
            今日新增 {{ overviewStats.newUsersToday }}
        </div>
      </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-title">今日订单数</div>
          <div class="stat-value">{{ overviewStats.todayOrders.toLocaleString() }}</div>
           <div :class="['stat-trend', overviewStats.orderChangePercent !== 0 ? (overviewStats.orderChangePercent > 0 ? 'trend-up' : 'trend-down') : '']">
              <el-icon v-if="overviewStats.orderChangePercent > 0"><Top /></el-icon>
              <el-icon v-else-if="overviewStats.orderChangePercent < 0"><Bottom /></el-icon>
              {{ overviewStats.orderChangePercent > 0 ? '↑' : (overviewStats.orderChangePercent < 0 ? '↓' : '') }} {{ Math.abs(overviewStats.orderChangePercent) }}% vs 昨日
        </div>
      </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-title">待处理事项</div>
          <div class="stat-value">{{ overviewStats.pendingItems }}</div>
          <div class="stat-trend" style="font-size: 12px; color: #909399;">
            (商品审核, 纠纷, 清洗任务)
    </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Charts Section -->
    <el-row :gutter="20" class="charts-section">
      <el-col :xs="24" :md="12">
        <el-card shadow="never" class="chart-card">
          <div class="card-header">
            <span>商品分类分布</span>
          </div>
          <div class="chart-container" ref="categoryChartRef" />
      </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card shadow="never" class="chart-card">
           <div class="card-header">
            <span>近7日活跃用户/商家</span>
          </div>
          <div class="chart-container" ref="activityChartRef" />
      </el-card>
      </el-col>
    </el-row>

    <!-- Recent Listings & Cleaning Brief -->
     <el-row :gutter="20" class="list-section">
       <el-col :xs="24" :md="16">
         <el-card shadow="never">
           <template #header>
              <div class="card-header">
                <span>最近上新商品</span>
                <el-button type="primary" link>查看全部</el-button>
      </div>
            </template>
            <el-table :data="recentProducts" style="width: 100%">
              <el-table-column label="商品图" width="70">
                 <template #default="{ row }">
                    <el-image :src="row.image" fit="cover" style="width: 40px; height: 40px; border-radius: 4px;" />
          </template>
        </el-table-column>
              <el-table-column prop="name" label="名称" show-overflow-tooltip />
              <el-table-column prop="category" label="分类" width="100" />
              <el-table-column prop="price" label="价格" width="100">
                 <template #default="{ row }">￥{{ row.price.toFixed(2) }}</template>
        </el-table-column>
              <el-table-column prop="seller" label="卖家" width="120" />
              <el-table-column prop="time" label="上架时间" width="160" />
      </el-table>
         </el-card>
       </el-col>
       <el-col :xs="24" :md="8">
          <el-card shadow="never">
             <template #header>
                <div class="card-header">
                  <span>数据清洗简报</span>
                </div>
              </template>
              <div class="cleaning-brief">
                 <div class="brief-item">
                    <span class="brief-label">待处理任务:</span>
                    <span class="brief-value danger">{{ cleaningBrief.pendingTasks }}</span>
                 </div>
                 <div class="brief-item">
                    <span class="brief-label">今日成功率:</span>
                    <span class="brief-value success">{{ cleaningBrief.successRate }}%</span>
                 </div>
                 <div class="brief-item">
                    <span class="brief-label">今日异常数据:</span>
                    <span class="brief-value warning">{{ cleaningBrief.errorCount }}</span>
                 </div>
                  <el-button type="primary" plain style="width: 100%; margin-top: 15px;">查看清洗中心</el-button>
    </div>
          </el-card>
       </el-col>
     </el-row>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue';
import * as echarts from 'echarts';
import { Top, Bottom } from '@element-plus/icons-vue';

// --- Refs for Charts ---
const categoryChartRef = ref<HTMLElement | null>(null);
const activityChartRef = ref<HTMLElement | null>(null);

// --- Mock Data ---
const overviewStats = reactive({
  totalProducts: 8590,
  newProductsToday: 58,
  totalUsers: 2485,
  newUsersToday: 12,
  todayOrders: 128,
  orderChangePercent: 21.5,
  pendingItems: 42 + 38 // Example: pending reviews + pending disputes
});

const recentProducts = ref([
  { id: 'P001', name: 'Apple MacBook Air M2 13英寸 深空灰', category: '数码电子', price: 8999.00, seller: 'Apple官方店', time: '2025-04-27 10:30', image: 'https://placehold.co/40x40/AAB8C2/FFF?text=MB' },
  { id: 'P002', name: '夏季纯棉印花T恤 白色 L码', category: '服装鞋包', price: 129.00, seller: '潮流前线', time: '2025-04-27 09:15', image: 'https://placehold.co/40x40/FFFFFF/000?text=T' },
  { id: 'P003', name: '北欧风实木餐边柜 原木色', category: '家居家具', price: 1580.00, seller: '温馨家居', time: '2025-04-26 18:05', image: 'https://placehold.co/40x40/D2B48C/FFF?text=CB' },
  { id: 'P004', name: '索尼 Alpha 7 IV 全画幅微单相机', category: '数码电子', price: 16999.00, seller: '摄影器材专营', time: '2025-04-26 15:20', image: 'https://placehold.co/40x40/000000/FFF?text=SN' },
]);

const cleaningBrief = reactive({
  pendingTasks: 5,
  successRate: 98.6,
  errorCount: 12
});

// --- Chart Initialization Functions ---

const initCategoryChart = () => {
  if (!categoryChartRef.value) return;
  const chart = echarts.init(categoryChartRef.value);
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      bottom: '5%',
      left: 'center',
      data: ['数码电子', '服装鞋包', '家居家具', '美妆护肤', '图书音像', '其他'] // Example categories
    },
    series: [
      {
        name: '商品分类',
        type: 'pie',
        radius: ['45%', '70%'],
        center: ['50%', '45%'], // Adjust center to make space for legend
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 8,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: { show: false },
        emphasis: {
          label: { show: false }
        },
        labelLine: { show: false },
        data: [
          { value: 3350, name: '数码电子' },
          { value: 2100, name: '服装鞋包' },
          { value: 1540, name: '家居家具' },
          { value: 880, name: '美妆护肤' },
          { value: 420, name: '图书音像' },
          { value: 300, name: '其他' }
        ],
         color: ['#3E54AC', '#655DBB', '#FF9800', '#4CAF50', '#F44336', '#909399']
      }
    ]
  };
  chart.setOption(option);
   window.addEventListener('resize', () => chart.resize());
};

const initActivityChart = () => {
  if (!activityChartRef.value) return;
  const chart = echarts.init(activityChartRef.value);
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['活跃用户', '活跃商家'],
       top: '5%'
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
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'] // Example: last 7 days
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '活跃用户',
        type: 'line',
        data: [820, 932, 901, 934, 1290, 1330, 1320],
        smooth: true,
        itemStyle: { color: '#409EFF' }
      },
      {
        name: '活跃商家',
        type: 'line',
        data: [320, 382, 351, 374, 590, 610, 600],
        smooth: true,
         itemStyle: { color: '#67C23A' }
      }
    ]
  };
  chart.setOption(option);
  window.addEventListener('resize', () => chart.resize());
};

// --- Lifecycle Hook ---
onMounted(() => {
  nextTick(() => { // Ensure DOM is ready
    initCategoryChart();
    initActivityChart();
});
});

</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  border: none;
  border-radius: 8px;
}

.stat-title {
  color: #888888;
  font-size: 14px;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 26px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 8px;
}

.stat-trend {
  display: flex;
  align-items: center;
  font-size: 13px;
  gap: 4px;
}

.trend-up { color: var(--el-color-success); }
.trend-down { color: var(--el-color-danger); }

.charts-section {
  margin-bottom: 20px;
}

.chart-card {
   border: none;
  border-radius: 8px;
   height: 350px; /* Ensure cards have same height */
   display: flex;
   flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
}

.chart-container {
  flex-grow: 1;
  height: calc(100% - 40px); /* Adjust based on header/padding */
  width: 100%;
}

.list-section {
   margin-bottom: 20px;
}

.list-section .el-card {
    border: none;
  border-radius: 8px;
}

.el-table :deep(th) {
  background-color: #f8f9fc !important;
  font-weight: 500;
  color: #888888;
}

.el-table :deep(td), .el-table :deep(th) {
   padding: 10px 0;
   vertical-align: middle;
}

.cleaning-brief {
  font-size: 14px;
}

.brief-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f0f2f5;
}

.brief-item:last-child {
  border-bottom: none;
}

.brief-label {
  color: #666;
}

.brief-value {
  font-weight: 600;
}

.brief-value.danger { color: var(--el-color-danger); }
.brief-value.warning { color: var(--el-color-warning); }
.brief-value.success { color: var(--el-color-success); }

</style>
