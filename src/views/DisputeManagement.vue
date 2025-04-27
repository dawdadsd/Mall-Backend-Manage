<template>
  <el-container direction="vertical" class="dispute-management-container">
    <!-- Header -->
    <el-header height="auto" class="page-header">
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <h1 class="header-title">争议和纠纷处理中心</h1>
        <div class="page-actions">
          <el-button :icon="Document">纠纷统计报告</el-button>
          <el-button type="primary" :icon="Setting">纠纷处理规则设置</el-button>
        </div>
      </div>
    </el-header>

    <!-- Main Content -->
    <el-main class="main-content">
      <!-- Stats Row -->
      <el-row :gutter="20" class="stats-row">
        <el-col :xs="24" :sm="12" :md="6" v-for="stat in stats" :key="stat.title">
          <el-card shadow="never" class="stat-card">
            <div class="stat-title">{{ stat.title }}</div>
            <div class="stat-value">{{ stat.value }}</div>
            <div :class="['stat-trend', stat.trend === 'up' ? 'trend-up' : 'trend-down']">
              <!-- Note: Trend icon direction seems reversed in prototype -->
              <el-icon v-if="stat.trend === 'up'"><Top /></el-icon>
              <el-icon v-else><Bottom /></el-icon>
              <span>{{ stat.change }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- Tabs -->
      <el-tabs v-model="activeTab" class="dispute-tabs">
        <el-tab-pane label="纠纷列表" name="list">
          <el-card shadow="never" class="content-card">
             <template #header>
               <div class="card-header">
                 <span>争议纠纷列表</span>
               </div>
             </template>
            <!-- Filters -->
            <div class="filter-row">
              <el-select v-model="filters.disputeType" placeholder="纠纷类型" clearable class="filter-item">
                <el-option label="全部类型" value=""></el-option>
                <el-option v-for="(label, value) in disputeTypeMap" :key="value" :label="label" :value="value"></el-option>
                <el-option label="其他原因" value="other"></el-option>
              </el-select>
              <el-select v-model="filters.status" placeholder="状态" clearable class="filter-item">
                 <el-option label="全部状态" value=""></el-option>
                 <el-option v-for="(label, value) in statusMap" :key="value" :label="label" :value="value"></el-option>
              </el-select>
               <el-select v-model="filters.timeRange" placeholder="提出时间" clearable class="filter-item">
                  <el-option label="全部时间" value="all"></el-option>
                  <el-option label="今天" value="today"></el-option>
                  <el-option label="昨天" value="yesterday"></el-option>
                  <el-option label="近7天" value="last7days"></el-option>
                  <el-option label="近30天" value="last30days"></el-option>
               </el-select>
              <el-input
                v-model="filters.keyword"
                placeholder="搜索订单编号/用户名"
                :prefix-icon="Search"
                clearable
                class="filter-item search-input"
              />
               <el-button type="primary" :icon="Search" @click="applyFilters">搜索</el-button>
            </div>

            <!-- Table -->
            <el-table :data="paginatedDisputes" style="width: 100%">
              <el-table-column prop="id" label="编号" width="80"></el-table-column>
              <el-table-column label="商品信息" min-width="200">
                <template #default="{ row }">
                  <div class="product-info-cell">
                    <el-image :src="row.product.image" fit="cover" class="product-image" />
                    <div>
                      <div class="product-name">{{ row.product.name }}</div>
                      <div class="product-id">订单号: {{ row.orderId }}</div>
                    </div>
                  </div>
                </template>
              </el-table-column>
               <el-table-column label="买家" width="150">
                 <template #default="{ row }">
                   <div class="user-info-cell">
                     <el-avatar :size="32" :src="row.buyer.avatar" />
                     <div>
                       <div class="user-name">{{ row.buyer.name }}</div>
                       <div class="user-id">ID: {{ row.buyer.id }}</div>
                     </div>
                   </div>
                 </template>
              </el-table-column>
              <el-table-column label="卖家" width="150">
                 <template #default="{ row }">
                    <div class="user-info-cell">
                     <el-avatar :size="32" :src="row.seller.avatar" />
                      <div>
                       <div class="user-name">{{ row.seller.name }}</div>
                       <div class="user-id">ID: {{ row.seller.id }}</div>
                     </div>
                   </div>
                 </template>
              </el-table-column>
              <el-table-column label="纠纷类型" width="140">
                <template #default="{ row }">
                    <el-tag :type="getDisputeTypeTag(row.type)" size="small" effect="light">{{ disputeTypeMap[row.type] || '其他原因' }}</el-tag>
                 </template>
              </el-table-column>
              <el-table-column label="状态" width="100">
                <template #default="{ row }">
                   <el-tag :type="getStatusTag(row.status)" size="small" effect="light">{{ statusMap[row.status] }}</el-tag>
                 </template>
              </el-table-column>
              <el-table-column prop="submitTime" label="提交时间" width="160"></el-table-column>
              <el-table-column label="操作" width="100" fixed="right">
                <template #default="{ row }">
                  <el-button link type="primary" :icon="View" size="small" @click="handleView(row)"></el-button>
                  <el-button link type="primary" :icon="ChatDotRound" size="small" @click="handleChat(row)"></el-button> <!-- Chat icon -->
                  <el-dropdown>
                    <el-button link type="primary" :icon="MoreFilled" size="small" @click.stop></el-button>
                     <template #dropdown>
                       <el-dropdown-menu>
                         <el-dropdown-item>分配处理人</el-dropdown-item>
                         <el-dropdown-item>更新状态</el-dropdown-item>
                         <el-dropdown-item command="close" style="color: red;">关闭纠纷</el-dropdown-item>
                       </el-dropdown-menu>
                     </template>
                  </el-dropdown>
                </template>
              </el-table-column>
            </el-table>

            <!-- Pagination -->
             <div class="pagination-container">
                <div>显示 {{ (currentPage - 1) * pageSize + 1 }} 至 {{ Math.min(currentPage * pageSize, filteredDisputes.length) }} 条，共 {{ filteredDisputes.length }} 条</div>
                <el-pagination
                    background
                    layout="prev, pager, next"
                    :total="filteredDisputes.length"
                    :page-size="pageSize"
                    :current-page="currentPage"
                    @current-change="handlePageChange"
                />
            </div>
          </el-card>
        </el-tab-pane>

        <el-tab-pane label="举证材料管理" name="evidence">
           <el-card shadow="never" class="content-card">
             <template #header>举证材料管理</template>
             <p>查看和管理买卖双方提交的举证材料。</p>
             <!-- Add evidence management UI here -->
           </el-card>
        </el-tab-pane>

        <el-tab-pane label="退款/换货管理" name="refund">
             <el-card shadow="never" class="content-card">
               <template #header>退款/换货管理</template>
               <p>处理纠纷相关的退款和换货流程。</p>
               <!-- Add refund/exchange UI here -->
             </el-card>
        </el-tab-pane>

        <el-tab-pane label="调解记录" name="mediation">
             <el-card shadow="never" class="content-card">
               <template #header>调解记录</template>
               <p>记录平台客服介入调解的过程和结果。</p>
               <!-- Add mediation logs UI here -->
             </el-card>
        </el-tab-pane>

        <el-tab-pane label="仲裁决策" name="arbitration">
           <el-card shadow="never" class="content-card">
             <template #header>仲裁决策</template>
             <p>查看平台最终的仲裁结果和依据。</p>
             <!-- Add arbitration decisions UI here -->
           </el-card>
        </el-tab-pane>
      </el-tabs>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue';
import {
  Document, Setting, Search, Top, Bottom, View, ChatDotRound, MoreFilled
} from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import type { TagProps } from 'element-plus';

// --- Interfaces & Types ---
type DisputeType = 'condition_mismatch' | 'missing_parts' | 'shipping_damage' | 'functional_issue' | 'late_shipping' | 'other';
type DisputeStatus = 'pending_acceptance' | 'evidence_submission' | 'negotiation' | 'refund_processing' | 'exchange_processing' | 'arbitration' | 'resolved' | 'closed';

interface UserInfo {
  id: string;
  name: string;
  avatar: string;
}

interface ProductInfo {
  id: string;
  name: string;
  image: string;
}

interface Dispute {
  id: string;
  orderId: string;
  product: ProductInfo;
  buyer: UserInfo;
  seller: UserInfo;
  type: DisputeType;
  status: DisputeStatus;
  submitTime: string;
}

// --- Mappers ---
const disputeTypeMap: Record<DisputeType, string> = {
    condition_mismatch: '商品品相不符',
    missing_parts: '配件缺失',
    shipping_damage: '邮寄损坏',
    functional_issue: '功能异常',
    late_shipping: '未按时发货',
    other: '其他原因'
};

const statusMap: Record<DisputeStatus, string> = {
    pending_acceptance: '待受理',
    evidence_submission: '举证中',
    negotiation: '协商中',
    refund_processing: '退款处理',
    exchange_processing: '换货处理',
    arbitration: '仲裁中',
    resolved: '已解决',
    closed: '已关闭'
};

// --- Mock Data ---
const stats = ref([
  { title: '待处理纠纷', value: 42, trend: 'up', change: '较昨日 +8' }, // Trend icon seems reversed in prototype HTML
  { title: '已解决纠纷', value: '1,247', trend: 'up', change: '较昨日 +16' },
  { title: '平均解决时间', value: '1.8天', trend: 'down', change: '较上月 -0.3天' },
  { title: '纠纷解决率', value: '94.7%', trend: 'up', change: '较上月 +1.2%' },
]);

const allDisputes = ref<Dispute[]>([
  { id: '#D2308', orderId: 'O2023082401', product: { id: 'P1001', name: 'iPhone 13 Pro 256GB 海蓝色', image: 'https://placehold.co/48x48/3E54AC/FFF?text=iP' }, buyer: { id: 'U78945', name: '张小明', avatar: 'https://placehold.co/40x40/4CAF50/FFF?text=Z' }, seller: { id: 'S12345', name: '数码优品店', avatar: 'https://placehold.co/40x40/FF9800/FFF?text=S' }, type: 'condition_mismatch', status: 'evidence_submission', submitTime: '2023-08-24 14:32' },
  { id: '#D2307', orderId: 'O2023082305', product: { id: 'P1002', name: '戴森吸尘器 V11 紫色', image: 'https://placehold.co/48x48/9C27B0/FFF?text=Dy' }, buyer: { id: 'U65432', name: '李华', avatar: 'https://placehold.co/40x40/F44336/FFF?text=L' }, seller: { id: 'S45678', name: '优选家电', avatar: 'https://placehold.co/40x40/2196F3/FFF?text=Y' }, type: 'missing_parts', status: 'negotiation', submitTime: '2023-08-23 09:15' },
  { id: '#D2306', orderId: 'O2023082201', product: { id: 'P1003', name: 'Nike Air Force 1 \'07 白色 42码', image: 'https://placehold.co/48x48/FFFFFF/000?text=NK' }, buyer: { id: 'U23456', name: '王军', avatar: 'https://placehold.co/40x40/795548/FFF?text=W' }, seller: { id: 'S56789', name: '潮流鞋坊', avatar: 'https://placehold.co/40x40/607D8B/FFF?text=C' }, type: 'shipping_damage', status: 'refund_processing', submitTime: '2023-08-22 16:48' },
  { id: '#D2305', orderId: 'O2023082108', product: { id: 'P1004', name: '索尼 WH-1000XM4 耳机 黑色', image: 'https://placehold.co/48x48/000000/FFF?text=SN' }, buyer: { id: 'U34567', name: '赵丽', avatar: 'https://placehold.co/40x40/E91E63/FFF?text=Z' }, seller: { id: 'S67890', name: '音频天地', avatar: 'https://placehold.co/40x40/FFEB3B/000?text=A' }, type: 'functional_issue', status: 'arbitration', submitTime: '2023-08-21 11:27' },
  { id: '#D2304', orderId: 'O2023082003', product: { id: 'P1005', name: 'Nintendo Switch OLED 白色', image: 'https://placehold.co/48x48/FF5722/FFF?text=NS' }, buyer: { id: 'U45678', name: '陈明', avatar: 'https://placehold.co/40x40/00BCD4/FFF?text=C' }, seller: { id: 'S78901', name: '游戏世界', avatar: 'https://placehold.co/40x40/8BC34A/FFF?text=G' }, type: 'condition_mismatch', status: 'resolved', submitTime: '2023-08-20 10:55' },
  // Add more disputes
   { id: '#D2303', orderId: 'O2023081901', product: { id: 'P1006', name: 'MacBook Pro 14英寸 M1 Pro', image: 'https://placehold.co/48x48/BDC3C7/FFF?text=MB' }, buyer: { id: 'U56789', name: '孙强', avatar: 'https://placehold.co/40x40/34495E/FFF?text=S' }, seller: { id: 'S12345', name: '数码优品店', avatar: 'https://placehold.co/40x40/FF9800/FFF?text=S' }, type: 'late_shipping', status: 'closed', submitTime: '2023-08-19 18:10' },
]);

// --- State ---
const activeTab = ref('list');
const filters = reactive({
  disputeType: '',
  status: '',
  timeRange: 'all',
  keyword: '',
});
const currentPage = ref(1);
const pageSize = ref(5); // Adjust as needed

// --- Computed ---
const filteredDisputes = computed(() => {
  return allDisputes.value.filter(dispute => {
    const typeMatch = !filters.disputeType || dispute.type === filters.disputeType;
    const statusMatch = !filters.status || dispute.status === filters.status;
    // Add time range filtering logic here based on submitTime
    const keywordMatch = !filters.keyword ||
                         dispute.orderId.toLowerCase().includes(filters.keyword.toLowerCase()) ||
                         dispute.buyer.name.toLowerCase().includes(filters.keyword.toLowerCase()) ||
                         dispute.seller.name.toLowerCase().includes(filters.keyword.toLowerCase());
    return typeMatch && statusMatch && keywordMatch; // Add timeMatch when implemented
  });
});

const paginatedDisputes = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredDisputes.value.slice(start, end);
});

// --- Methods ---
const applyFilters = () => {
  currentPage.value = 1;
  ElMessage.success('筛选条件已应用');
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
};

const handleView = (row: Dispute) => {
  console.log('View dispute:', row);
  ElMessage.info(`查看纠纷 ${row.id}`);
  // Navigate to detail view or open modal
};

const handleChat = (row: Dispute) => {
  console.log('Chat about dispute:', row);
   ElMessage.info(`与纠纷 ${row.id} 相关方沟通`);
  // Open chat interface
};

// Helper to determine tag type based on dispute type (example logic)
const getDisputeTypeTag = (type: DisputeType): TagProps['type'] => {
    switch (type) {
        case 'condition_mismatch':
        case 'shipping_damage':
            return 'warning';
        case 'missing_parts':
        case 'functional_issue':
             return 'danger';
        case 'late_shipping':
             return 'info';
        default:
            return 'info';
    }
};

// Helper to determine tag type based on status (example logic)
const getStatusTag = (status: DisputeStatus): TagProps['type'] => {
    switch (status) {
        case 'pending_acceptance': return 'info';
        case 'evidence_submission': return 'primary';
        case 'negotiation': return 'primary';
        case 'refund_processing':
        case 'exchange_processing':
            return 'warning';
        case 'arbitration': return 'danger';
        case 'resolved': return 'success';
        case 'closed': return 'info';
        default: return 'info';
    }
};


</script>

<style scoped>
.dispute-management-container {
  padding: 0;
}

.page-header {
  padding: 15px 20px;
  background-color: #fff;
  border-bottom: 1px solid var(--el-border-color-light);
}

.header-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
}

.page-actions .el-button {
  margin-left: 10px;
}

.main-content {
  padding: 20px;
}

.stats-row {
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
/* Trend colors seem reversed in prototype, using standard colors */
.trend-up { color: var(--el-color-success); }
.trend-down { color: var(--el-color-danger); }

.dispute-tabs {
  margin-bottom: 20px;
}

.dispute-tabs :deep(.el-tabs__header) {
    margin-bottom: 0;
}

.content-card {
    border: none;
    border-top-left-radius: 0; /* Match tab style */
    box-shadow: none;
    border-top: 1px solid var(--el-border-color-light);
}

.filter-row {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
  align-items: center;
}

.filter-item {
   min-width: 140px;
}
.search-input {
    flex: 1;
    min-width: 200px;
    max-width: 300px;
}

.product-info-cell {
    display: flex;
    align-items: center;
    gap: 10px;
}
.product-image {
    width: 40px;
    height: 40px;
    border-radius: 4px;
    object-fit: cover;
    flex-shrink: 0;
}
.product-name {
    font-weight: 500;
    font-size: 13px;
    margin-bottom: 2px;
}
.product-id {
    font-size: 12px;
    color: #999;
}

.user-info-cell {
     display: flex;
    align-items: center;
    gap: 8px;
}
.user-name {
    font-weight: 500;
    font-size: 13px;
}
.user-id {
    font-size: 12px;
    color: #999;
}


.el-table th {
  background-color: #f8f9fc !important;
  font-weight: 500;
  color: #888888;
}

.el-table td, .el-table th {
   vertical-align: middle;
   padding: 10px 0; /* Adjust padding */
}

.pagination-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;
    font-size: 14px;
    color: #666;
}

</style> 