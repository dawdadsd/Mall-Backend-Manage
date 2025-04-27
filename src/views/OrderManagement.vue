<template>
  <el-container direction="vertical" class="order-management-container">
    <!-- 页面标题和创建按钮 -->
    <el-header height="auto" class="page-header">
      <div class="page-title-container">
        <h1>订单管理</h1>
        <el-button type="primary" :icon="Plus">创建订单</el-button>
      </div>
    </el-header>

    <!-- 统计卡片 -->
    <el-main class="main-content">
      <el-row :gutter="24" class="stats-grid">
        <el-col :xs="24" :sm="12" :md="6" v-for="stat in stats" :key="stat.title">
          <el-card shadow="never" class="stat-card">
            <div class="stat-title">{{ stat.title }}</div>
            <div class="stat-value">{{ stat.value }}</div>
            <div :class="['stat-trend', stat.trend === 'up' ? 'trend-up' : 'trend-down']">
              <el-icon v-if="stat.trend === 'up'"><Top /></el-icon>
              <el-icon v-else><Bottom /></el-icon>
              <span>{{ stat.change }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 过滤器 -->
      <el-card shadow="never" class="filters-card">
        <el-form :model="filterForm" label-position="top">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="订单状态：">
                <el-radio-group v-model="filterForm.status">
                  <el-radio-button v-for="(label, value) in orderStatusOptions" :key="value" :label="value">{{ label }}</el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="商家类型：">
                 <el-radio-group v-model="filterForm.merchantType">
                   <el-radio-button v-for="(label, value) in merchantTypeOptions" :key="value" :label="value">{{ label }}</el-radio-button>
                 </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="下单时间：">
                 <el-radio-group v-model="filterForm.timeRange">
                    <el-radio-button v-for="(label, value) in timeRangeOptions" :key="value" :label="value">{{ label }}</el-radio-button>
                 </el-radio-group>
                 <!-- 可以添加 el-date-picker 用于自定义时间 -->
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="搜索：" class="search-row">
                <el-input
                  v-model="filterForm.keyword"
                  placeholder="搜索订单号、商品名称、买家昵称"
                  :prefix-icon="Search"
                  clearable
                  style="flex: 1; margin-right: 10px;"
                />
                <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
                <el-button :icon="Refresh" @click="handleReset">重置</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-card>

      <!-- 订单表格 -->
      <el-card shadow="never" class="orders-card">
         <template #header>
           <div class="card-header">
             <span>订单列表</span>
             <div>
               <el-button :icon="Download">导出</el-button>
               <!-- <el-button :icon="Filter">筛选</el-button> -->
             </div>
           </div>
         </template>

        <el-table :data="filteredOrders" stripe style="width: 100%">
          <el-table-column prop="id" label="订单号" width="180">
            <template #default="{ row }">
              <span class="order-id">{{ row.id }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="productInfo" label="商品信息" min-width="200"></el-table-column>
          <el-table-column prop="merchant" label="商家" width="150">
             <template #default="{ row }">
               {{ row.merchant.name }}
               <el-tag
                 size="small"
                 :type="row.merchant.type === 'verified' ? 'success' : 'info'"
                 effect="light"
                 disable-transitions
                 class="merchant-badge"
               >
                 {{ row.merchant.type === 'verified' ? '认证' : '个人' }}
               </el-tag>
             </template>
          </el-table-column>
          <el-table-column prop="buyer" label="买家" width="100"></el-table-column>
          <el-table-column prop="amount" label="金额" width="100">
            <template #default="{ row }">
              ￥{{ row.amount.toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="orderTime" label="下单时间" width="160"></el-table-column>
          <el-table-column prop="status" label="状态" width="120">
            <template #default="{ row }">
              <el-tag :type="getStatusTagType(row.status)" effect="light" disable-transitions>
                 <el-icon style="vertical-align: middle; margin-right: 4px;">
                     <component :is="getStatusIcon(row.status)" />
                 </el-icon>
                 {{ orderStatusOptions[row.status] }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" :icon="View" size="small" @click="handleView(row)"></el-button>
              <el-button link type="primary" :icon="Edit" size="small" @click="handleEdit(row)"></el-button>
              <el-dropdown>
                 <el-button link type="primary" :icon="MoreFilled" size="small" @click.stop></el-button>
                 <template #dropdown>
                   <el-dropdown-menu>
                     <el-dropdown-item>操作 A</el-dropdown-item>
                     <el-dropdown-item>操作 B</el-dropdown-item>
                   </el-dropdown-menu>
                 </template>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

         <div class="card-footer">
           <div>显示 {{ (currentPage - 1) * pageSize + 1 }} - {{ Math.min(currentPage * pageSize, totalOrders) }} 条，共 {{ totalOrders }} 条</div>
           <el-pagination
             background
             layout="prev, pager, next"
             :total="totalOrders"
             :page-size="pageSize"
             :current-page="currentPage"
             @current-change="handlePageChange"
           />
         </div>
      </el-card>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue';
import {
  Plus, Top, Bottom, Search, Refresh, Download, Filter, View, Edit, MoreFilled,
  ShoppingCart, Van, CircleCheck, CircleClose, Coin, Clock, Box
} from '@element-plus/icons-vue';
import type { Component } from 'vue';
import { ElMessage } from 'element-plus';

// --- 类型定义 ---

type OrderStatus = 'pending_payment' | 'pending_shipment' | 'shipped' | 'delivered' | 'completed' | 'cancelled' | 'refunding' | 'refunded';
type MerchantType = 'verified' | 'regular';
type TimeRange = 'all' | 'today' | 'yesterday' | 'this_week' | 'this_month' | 'last_month' | 'custom';

interface Merchant {
  name: string;
  type: MerchantType;
}

interface Order {
  id: string;
  productInfo: string;
  merchant: Merchant;
  buyer: string;
  amount: number;
  orderTime: string; // ISO 8601 format or similar
  status: OrderStatus;
}

// --- 模拟数据 ---

const stats = ref([
  { title: '今日订单', value: 128, trend: 'up', change: '较昨日 +21.5%' },
  { title: '待发货', value: 86, trend: 'up', change: '较昨日 +12.3%' },
  { title: '退款申请', value: 24, trend: 'down', change: '较昨日 -5.8%' },
  { title: '今日交易额', value: '¥56,842', trend: 'up', change: '较昨日 +18.2%' },
]);

const allOrders = ref<Order[]>([
  { id: 'SH2023112500001', productInfo: 'iPhone 13 Pro 128GB 深空灰色 99新', merchant: { name: '数码优品', type: 'verified' }, buyer: '王小明', amount: 3899.00, orderTime: '2023-11-25 14:32:00', status: 'shipped' },
  { id: 'SH2023112500002', productInfo: 'Nike Air Force 1 白色 42码 8成新', merchant: { name: '李小华', type: 'regular' }, buyer: '张小强', amount: 399.00, orderTime: '2023-11-25 13:45:00', status: 'delivered' },
  { id: 'SH2023112400089', productInfo: '索尼 WH-1000XM4 无线降噪耳机 9成新', merchant: { name: '音乐发烧友', type: 'verified' }, buyer: '刘小红', amount: 1299.00, orderTime: '2023-11-24 18:20:00', status: 'refunded' },
  { id: 'SH2023112400065', productInfo: 'Switch 游戏主机 红蓝手柄 带马力欧游戏', merchant: { name: '游戏世界', type: 'verified' }, buyer: '赵小丽', amount: 1699.00, orderTime: '2023-11-24 15:10:00', status: 'pending_shipment' },
  { id: 'SH2023112400032', productInfo: '小米手环7 黑色 全新未拆封', merchant: { name: '科技潮流', type: 'verified' }, buyer: '孙小伟', amount: 199.00, orderTime: '2023-11-24 11:25:00', status: 'cancelled' },
  { id: 'SH2023112300198', productInfo: '戴森吹风机 HD03 紫色 95新', merchant: { name: '美丽优选', type: 'verified' }, buyer: '周小莉', amount: 1899.00, orderTime: '2023-11-23 20:36:00', status: 'shipped' },
  { id: 'SH2023112300156', productInfo: '宜家 ALEX 抽屉柜 白色 7成新', merchant: { name: '吴小芳', type: 'regular' }, buyer: '郑小明', amount: 349.00, orderTime: '2023-11-23 17:45:00', status: 'delivered' },
  // Add more mock orders for pagination
  { id: 'SH2023112200101', productInfo: 'Apple Watch SE GPS 40mm', merchant: { name: '数码优品', type: 'verified' }, buyer: '王小明', amount: 1500.00, orderTime: '2023-11-22 10:15:00', status: 'completed' },
  { id: 'SH2023112200102', productInfo: '无印良品 香薰机 大号', merchant: { name: '生活家', type: 'regular' }, buyer: '李小华', amount: 350.00, orderTime: '2023-11-22 11:00:00', status: 'pending_payment' },
  { id: 'SH2023112100050', productInfo: 'AirPods Pro 2代', merchant: { name: '音乐发烧友', type: 'verified' }, buyer: '刘小红', amount: 1450.00, orderTime: '2023-11-21 09:30:00', status: 'refunding' },
]);

// --- 过滤器选项 ---

const orderStatusOptions: Record<OrderStatus, string> = {
  pending_payment: '待付款',
  pending_shipment: '待发货',
  shipped: '运输中',
  delivered: '已送达',
  completed: '已完成',
  cancelled: '已取消',
  refunding: '退款中',
  refunded: '已退款',
};
const allOrderStatusOptions = { all: '全部', ...orderStatusOptions }; // Add 'all' option if needed for filter UI

const merchantTypeOptions: Record<MerchantType | 'all' | 'hot', string> = {
  all: '全部',
  verified: '认证商家',
  regular: '个人闲置',
  hot: '热门商家', // As per prototype
};

const timeRangeOptions: Record<TimeRange, string> = {
  all: '全部时间',
  today: '今天',
  yesterday: '昨天',
  this_week: '本周',
  this_month: '本月',
  last_month: '上月',
  custom: '自定义', // Placeholder
};

// --- 筛选和分页状态 ---

const filterForm = reactive({
  status: 'all' as OrderStatus | 'all',
  merchantType: 'all' as MerchantType | 'all' | 'hot',
  timeRange: 'all' as TimeRange,
  keyword: '',
});

const pageSize = ref(7); // 与原型图一致
const currentPage = ref(1);

// --- 计算属性：过滤后的订单 ---

const filteredOrders = computed(() => {
  // 1. Filter based on criteria
  let orders = allOrders.value.filter(order => {
    const statusMatch = filterForm.status === 'all' || order.status === filterForm.status;
    const merchantMatch = filterForm.merchantType === 'all' ||
                         (filterForm.merchantType === 'hot') || // 'hot' logic needs backend data
                         order.merchant.type === filterForm.merchantType;
    // Time range filtering would require date parsing and comparison (omitted for simplicity)
    const keywordMatch = !filterForm.keyword ||
                         order.id.includes(filterForm.keyword) ||
                         order.productInfo.includes(filterForm.keyword) ||
                         order.buyer.includes(filterForm.keyword);

    return statusMatch && merchantMatch && keywordMatch;
  });

  // 2. Apply pagination
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return orders.slice(start, end);
});

const totalOrders = computed(() => {
    // Calculate total based on *filtered* results before pagination
    return allOrders.value.filter(order => {
      const statusMatch = filterForm.status === 'all' || order.status === filterForm.status;
      const merchantMatch = filterForm.merchantType === 'all' ||
                          (filterForm.merchantType === 'hot') || // 'hot' logic needs backend data
                          order.merchant.type === filterForm.merchantType;
      // Time range filter omitted
      const keywordMatch = !filterForm.keyword ||
                          order.id.includes(filterForm.keyword) ||
                          order.productInfo.includes(filterForm.keyword) ||
                          order.buyer.includes(filterForm.keyword);
      return statusMatch && merchantMatch && keywordMatch;
  }).length;
});

// --- 事件处理 ---

const handleSearch = () => {
  console.log('Searching with filters:', filterForm);
  currentPage.value = 1; // Reset to first page on new search
  ElMessage.info('应用搜索条件');
  // In a real app, this would likely trigger an API call
};

const handleReset = () => {
  filterForm.status = 'all';
  filterForm.merchantType = 'all';
  filterForm.timeRange = 'all';
  filterForm.keyword = '';
  currentPage.value = 1;
  ElMessage.success('重置筛选条件');
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
};

const handleView = (row: Order) => {
  console.log('View order:', row);
  ElMessage.info(`查看订单 ${row.id}`);
};

const handleEdit = (row: Order) => {
  console.log('Edit order:', row);
   ElMessage.info(`编辑订单 ${row.id}`);
};

// --- 辅助函数 --- 

const getStatusTagType = (status: OrderStatus): ('success' | 'warning' | 'info' | 'danger' | '') => {
  switch (status) {
    case 'pending_payment': return 'warning';
    case 'pending_shipment': return 'warning';
    case 'shipped': return 'info';
    case 'delivered': return 'success';
    case 'completed': return 'success';
    case 'cancelled': return 'danger';
    case 'refunding': return 'warning';
    case 'refunded': return 'info';
    default: return '';
  }
};

const getStatusIcon = (status: OrderStatus): Component => {
   switch (status) {
    case 'pending_payment': return Coin;
    case 'pending_shipment': return Box;
    case 'shipped': return Van;
    case 'delivered': return CircleCheck;
    case 'completed': return CircleCheck;
    case 'cancelled': return CircleClose;
    case 'refunding': return Clock; // Using Clock for refunding
    case 'refunded': return Coin;    // Using Coin for refunded
    default: return ShoppingCart; // Default icon
  }
}

</script>

<style scoped>
.order-management-container {
  padding: 0; /* Remove default padding if container adds it */
}

.page-header {
  padding: 15px 20px;
  background-color: #fff;
  border-bottom: 1px solid #e0e0e0;
}

.page-title-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title-container h1 {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
}

.main-content {
  padding: 20px;
}

.stats-grid {
  margin-bottom: 24px;
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
  font-size: 24px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 8px;
}

.stat-trend {
  display: flex;
  align-items: center;
  font-size: 12px;
  gap: 4px;
}

.trend-up {
  color: #4CAF50;
}

.trend-down {
  color: #F44336;
}

.filters-card {
  margin-bottom: 24px;
  border: none;
  border-radius: 8px;
}

/* Customize radio buttons to look like pills */
.el-radio-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.el-radio-button {
   margin-bottom: 5px; /* Add spacing if they wrap */
}

.el-radio-button :deep(.el-radio-button__inner) {
  border-radius: 20px !important;
  border: 1px solid #dcdfe6;
  box-shadow: none !important;
  padding: 8px 15px;
}

.el-radio-button:first-child :deep(.el-radio-button__inner) {
   border-left: 1px solid #dcdfe6;
   border-radius: 20px !important;
}
.el-radio-button:last-child :deep(.el-radio-button__inner) {
   border-radius: 20px !important;
}

.el-radio-button :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background-color: var(--el-color-primary);
  border-color: var(--el-color-primary);
  color: #fff;
}

.search-row :deep(.el-form-item__content) {
  display: flex;
  align-items: center;
}

.orders-card {
  border: none;
  border-radius: 8px;
  overflow: hidden; /* Needed for border-radius */
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-size: 16px;
  font-weight: 600;
}

.order-id {
  color: var(--el-color-primary);
  cursor: pointer;
  font-weight: 500;
}

.merchant-badge {
  margin-left: 6px;
}

.el-table th {
  background-color: #f8f9fc !important;
  font-weight: 500;
  color: #888888;
}

.el-table td, .el-table th {
   padding: 14px 0;
}

.el-dropdown .el-button {
  margin-left: 5px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #fff;
  border-top: 1px solid #e0e0e0;
  font-size: 14px;
  color: #666;
}

</style> 