<template>
  <el-container direction="vertical" class="merchant-credit-container">
    <!-- Header -->
    <el-header height="auto" class="page-header">
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <h1 class="header-title">商家信用评级体系</h1>
        <div class="page-actions">
          <el-button :icon="Document">信用数据分析</el-button> <!-- Assuming Document icon -->
          <el-button type="primary" :icon="Setting">评级标准设置</el-button>
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
              <el-icon v-if="stat.trend === 'up'"><Top /></el-icon>
              <el-icon v-else><Bottom /></el-icon>
              <span>{{ stat.change }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- Tabs -->
      <el-tabs v-model="activeTab" class="credit-tabs">
        <el-tab-pane label="商家列表" name="list">
          <el-card shadow="never" class="content-card">
             <template #header>
               <div class="card-header">
                 <span>商家信用评级列表</span>
               </div>
             </template>
            <!-- Filters -->
            <div class="filter-row">
              <el-select v-model="filters.merchantType" placeholder="商家类型" clearable class="filter-item">
                <el-option label="全部类型" value=""></el-option>
                <el-option label="品牌商家" value="brand"></el-option>
                <el-option label="认证个人" value="verified_individual"></el-option>
                <el-option label="普通个人" value="regular_individual"></el-option>
              </el-select>
              <el-select v-model="filters.creditRating" placeholder="信用评级" clearable class="filter-item">
                 <el-option label="全部" value=""></el-option>
                 <el-option label="优秀 (4.5-5.0)" value="excellent"></el-option>
                 <el-option label="良好 (3.5-4.4)" value="good"></el-option>
                 <el-option label="一般 (2.5-3.4)" value="average"></el-option>
                 <el-option label="较差 (1.0-2.4)" value="poor"></el-option>
              </el-select>
               <el-select v-model="filters.certificationStatus" placeholder="认证状态" clearable class="filter-item">
                  <el-option label="全部状态" value=""></el-option>
                  <el-option label="已认证" value="verified"></el-option>
                  <el-option label="认证中" value="pending"></el-option>
                  <el-option label="未认证" value="unverified"></el-option>
                  <el-option label="认证失败" value="failed"></el-option>
               </el-select>
              <el-input
                v-model="filters.keyword"
                placeholder="搜索商家名称/ID"
                :prefix-icon="Search"
                clearable
                class="filter-item search-input"
              />
               <el-button type="primary" :icon="Search" @click="applyFilters">搜索</el-button>
            </div>

            <!-- Table -->
            <el-table :data="paginatedMerchants" style="width: 100%">
              <el-table-column label="商家信息" min-width="250">
                <template #default="{ row }">
                  <div class="merchant-info-cell">
                    <el-avatar :size="48" :src="row.avatar" class="merchant-avatar" />
                    <div class="merchant-details">
                      <div class="merchant-name">{{ row.name }}</div>
                      <div class="merchant-id">ID: {{ row.id }}</div>
                       <el-tag v-if="row.isVerified" type="success" size="small" effect="light" disable-transitions class="cert-badge">
                         <el-icon><SuccessFilled /></el-icon>{{ row.typeName }}
                       </el-tag>
                        <el-tag v-else-if="row.certificationStatus === 'failed'" type="danger" size="small" effect="light" disable-transitions>{{ certificationStatusMap[row.certificationStatus] }}</el-tag>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="商家类型" width="120">
                 <template #default="{ row }">
                    <el-tag :type="getMerchantTypeTag(row.type)" size="small" effect="light">{{ merchantTypeMap[row.type] }}</el-tag>
                 </template>
              </el-table-column>
              <el-table-column label="信用评分" width="160">
                <template #default="{ row }">
                  <div class="rating-cell">
                    <span class="rating-value">{{ row.creditScore.toFixed(1) }}</span> {{ getRatingLevel(row.creditScore) }}
                    <el-progress
                      :percentage="(row.creditScore / 5) * 100"
                      :stroke-width="6"
                      :color="getRatingColor(row.creditScore)"
                      :show-text="false"
                      style="margin-top: 4px;"
                    />
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="交易履约率" width="120" align="center">
                <template #default="{ row }">{{ row.fulfillmentRate }}%</template>
              </el-table-column>
              <el-table-column label="准时发货率" width="120" align="center">
                 <template #default="{ row }">{{ row.onTimeShippingRate }}%</template>
              </el-table-column>
              <el-table-column label="售后满意度" width="120" align="center">
                <template #default="{ row }">{{ row.afterSalesSatisfaction.toFixed(1) }}/5.0</template>
              </el-table-column>
              <el-table-column label="操作" width="100" fixed="right">
                <template #default="{ row }">
                  <el-button link type="primary" :icon="View" size="small" @click="handleView(row)"></el-button>
                  <el-button link type="primary" :icon="Edit" size="small" @click="handleEdit(row)"></el-button>
                  <el-dropdown>
                    <el-button link type="primary" :icon="MoreFilled" size="small" @click.stop></el-button>
                     <template #dropdown>
                       <el-dropdown-menu>
                         <el-dropdown-item>调整信用分</el-dropdown-item>
                         <el-dropdown-item>查看违规记录</el-dropdown-item>
                         <el-dropdown-item command="disable" style="color: red;">禁用商家</el-dropdown-item>
                       </el-dropdown-menu>
                     </template>
                  </el-dropdown>
                </template>
              </el-table-column>
            </el-table>

            <!-- Pagination -->
             <div class="pagination-container">
                <div>显示 {{ (currentPage - 1) * pageSize + 1 }} 至 {{ Math.min(currentPage * pageSize, filteredMerchants.length) }} 条，共 {{ filteredMerchants.length }} 条</div>
                <el-pagination
                    background
                    layout="prev, pager, next"
                    :total="filteredMerchants.length"
                    :page-size="pageSize"
                    :current-page="currentPage"
                    @current-change="handlePageChange"
                />
            </div>
          </el-card>
        </el-tab-pane>

        <el-tab-pane label="身份认证管理" name="verification">
           <el-card shadow="never" class="content-card">
             <template #header>身份认证管理</template>
             <p>管理商家的身份认证申请和状态。</p>
             <!-- Add verification management table/form here -->
           </el-card>
        </el-tab-pane>

        <el-tab-pane label="评分标准设置" name="scoring">
             <el-card shadow="never" class="content-card">
               <template #header>评分标准设置</template>
               <p>配置信用评分的计算规则和各指标权重。</p>
               <!-- Add scoring standard configuration form here -->
             </el-card>
        </el-tab-pane>

        <el-tab-pane label="商家分级管理" name="levels">
             <el-card shadow="never" class="content-card">
               <template #header>商家分级管理</template>
               <p>定义不同信用等级的商家权益和限制。</p>
               <!-- Add merchant level configuration here -->
             </el-card>
        </el-tab-pane>

        <el-tab-pane label="违规处理" name="violations">
           <el-card shadow="never" class="content-card">
             <template #header>违规处理</template>
             <p>记录和处理商家违规行为，调整信用评分。</p>
             <!-- Add violation management table/form here -->
           </el-card>
        </el-tab-pane>
      </el-tabs>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue';
import {
  Document, Setting, Search, Top, Bottom, View, Edit, MoreFilled, SuccessFilled
} from '@element-plus/icons-vue';
import { ElMessage, ElProgress } from 'element-plus';
import type { TagProps } from 'element-plus';

// --- Interfaces & Types ---
type MerchantType = 'brand' | 'verified_individual' | 'regular_individual';
type CreditRating = 'excellent' | 'good' | 'average' | 'poor';
type CertificationStatus = 'verified' | 'pending' | 'unverified' | 'failed';

interface Merchant {
  id: string;
  name: string;
  avatar: string;
  type: MerchantType;
  typeName: string;
  isVerified: boolean;
  certificationStatus: CertificationStatus;
  creditScore: number;
  fulfillmentRate: number;
  onTimeShippingRate: number;
  afterSalesSatisfaction: number;
}

// --- Mappers ---
const merchantTypeMap: Record<MerchantType, string> = {
    brand: '品牌商家',
    verified_individual: '认证个人',
    regular_individual: '普通个人'
};

const certificationStatusMap: Record<CertificationStatus, string> = {
    verified: '已认证',
    pending: '认证中',
    unverified: '未认证',
    failed: '认证失败'
};

// --- Mock Data ---
const stats = ref([
  { title: '平台总商家数', value: '2,485', trend: 'up', change: '较上月 +126' },
  { title: '平均信用评分', value: '4.6', trend: 'up', change: '较上月 +0.2' },
  { title: '认证商家数量', value: '876', trend: 'up', change: '较上月 +42' },
  { title: '待审核认证申请', value: '38', trend: 'down', change: '较昨日 -5' },
]);

const allMerchants = ref<Merchant[]>([
  { id: 'M20230001', name: '数码优品专营店', avatar: 'https://placehold.co/48x48/3E54AC/FFF?text=DP', type: 'brand', typeName: '品牌商家', isVerified: true, certificationStatus: 'verified', creditScore: 4.9, fulfillmentRate: 99.2, onTimeShippingRate: 98.7, afterSalesSatisfaction: 4.8 },
  { id: 'M20230042', name: '李明', avatar: 'https://placehold.co/48x48/4CAF50/FFF?text=LM', type: 'verified_individual', typeName: '认证个人卖家', isVerified: true, certificationStatus: 'verified', creditScore: 4.2, fulfillmentRate: 95.8, onTimeShippingRate: 93.2, afterSalesSatisfaction: 4.3 },
  { id: 'M20230128', name: '轻奢优品', avatar: 'https://placehold.co/48x48/FF9800/FFF?text=LS', type: 'brand', typeName: '品牌商家', isVerified: true, certificationStatus: 'verified', creditScore: 4.7, fulfillmentRate: 97.5, onTimeShippingRate: 96.8, afterSalesSatisfaction: 4.6 },
  { id: 'M20230356', name: '张小华', avatar: 'https://placehold.co/48x48/F44336/FFF?text=ZH', type: 'regular_individual', typeName: '普通个人', isVerified: false, certificationStatus: 'unverified', creditScore: 3.2, fulfillmentRate: 87.3, onTimeShippingRate: 82.1, afterSalesSatisfaction: 3.4 },
  { id: 'M20230498', name: '王建国', avatar: 'https://placehold.co/48x48/2196F3/FFF?text=WG', type: 'regular_individual', typeName: '普通个人', isVerified: false, certificationStatus: 'failed', creditScore: 2.1, fulfillmentRate: 76.5, onTimeShippingRate: 68.2, afterSalesSatisfaction: 2.8 },
  // Add more merchants
   { id: 'M20230510', name: '潮流前线服饰', avatar: 'https://placehold.co/48x48/9C27B0/FFF?text=CF', type: 'brand', typeName: '品牌商家', isVerified: true, certificationStatus: 'verified', creditScore: 4.5, fulfillmentRate: 96.0, onTimeShippingRate: 95.5, afterSalesSatisfaction: 4.4 },
   { id: 'M20230622', name: '家居小铺', avatar: 'https://placehold.co/48x48/795548/FFF?text=HS', type: 'regular_individual', typeName: '普通个人', isVerified: false, certificationStatus: 'unverified', creditScore: 3.8, fulfillmentRate: 92.1, onTimeShippingRate: 90.0, afterSalesSatisfaction: 4.0 },
]);

// --- State ---
const activeTab = ref('list');
const filters = reactive({
  merchantType: '',
  creditRating: '',
  certificationStatus: '',
  keyword: '',
});
const currentPage = ref(1);
const pageSize = ref(5); // Match prototype

// --- Computed ---
const filteredMerchants = computed(() => {
  return allMerchants.value.filter(merchant => {
    const typeMatch = !filters.merchantType || merchant.type === filters.merchantType;
    const ratingMatch = !filters.creditRating || getRatingLevelEnum(merchant.creditScore) === filters.creditRating;
    const certMatch = !filters.certificationStatus || merchant.certificationStatus === filters.certificationStatus;
    const keywordMatch = !filters.keyword ||
                         merchant.name.toLowerCase().includes(filters.keyword.toLowerCase()) ||
                         merchant.id.toLowerCase().includes(filters.keyword.toLowerCase());
    return typeMatch && ratingMatch && certMatch && keywordMatch;
  });
});

const paginatedMerchants = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredMerchants.value.slice(start, end);
});

// --- Methods ---
const applyFilters = () => {
  currentPage.value = 1;
  ElMessage.success('筛选条件已应用');
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
};

const handleView = (row: Merchant) => {
  console.log('View merchant:', row);
  ElMessage.info(`查看商家 ${row.id}`);
  // Navigate to detail view or open modal
};

const handleEdit = (row: Merchant) => {
  console.log('Edit merchant:', row);
  ElMessage.info(`编辑商家 ${row.id}`);
  // Navigate to edit view or open modal
};

const getMerchantTypeTag = (type: MerchantType): TagProps['type'] => {
    if (type === 'brand') return 'primary';
    if (type === 'verified_individual') return 'success';
    return 'info';
};

const getRatingLevel = (score: number): string => {
    if (score >= 4.5) return '优秀';
    if (score >= 3.5) return '良好';
    if (score >= 2.5) return '一般';
    return '较差';
};

const getRatingLevelEnum = (score: number): CreditRating | null => {
    if (score >= 4.5) return 'excellent';
    if (score >= 3.5) return 'good';
    if (score >= 2.5) return 'average';
    if (score >= 1.0) return 'poor';
    return null;
}

const getRatingColor = (score: number): string => {
    if (score >= 4.5) return 'var(--el-color-success)';
    if (score >= 3.5) return 'var(--el-color-warning)';
    if (score >= 2.5) return 'var(--el-color-warning)'; // Still warning for average
    return 'var(--el-color-danger)';
};

</script>

<style scoped>
.merchant-credit-container {
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

.trend-up { color: var(--el-color-success); }
.trend-down { color: var(--el-color-danger); }

.credit-tabs {
  margin-bottom: 20px;
}

.credit-tabs :deep(.el-tabs__header) {
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

.merchant-info-cell {
    display: flex;
    align-items: center;
    gap: 12px;
}

.merchant-avatar {
    flex-shrink: 0;
}

.merchant-details {
    display: flex;
    flex-direction: column;
}

.merchant-name {
    font-weight: 500;
    margin-bottom: 2px;
}

.merchant-id {
    font-size: 12px;
    color: #999;
    margin-bottom: 4px;
}

.cert-badge {
    display: inline-flex; /* Align icon and text */
    align-items: center;
    gap: 4px;
}

.rating-cell {
    font-size: 13px;
}
.rating-value {
    font-weight: 600;
    margin-right: 4px;
}

.el-table th {
  background-color: #f8f9fc !important;
  font-weight: 500;
  color: #888888;
}

.el-table td, .el-table th {
   vertical-align: middle;
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