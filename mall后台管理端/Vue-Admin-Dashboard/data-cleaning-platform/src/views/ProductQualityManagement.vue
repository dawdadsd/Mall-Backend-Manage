<template>
  <el-container direction="vertical" class="product-quality-container">
    <!-- Header -->
    <el-header height="auto" class="page-header">
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <h1 class="header-title">商品质量管理</h1>
        <div class="page-actions">
          <el-button :icon="Download">导出报告</el-button>
          <el-button type="primary" :icon="Setting">质量标准设置</el-button>
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
      <el-tabs v-model="activeTab" class="quality-tabs">
        <el-tab-pane label="待审核商品" name="pending">
          <el-card shadow="never" class="content-card">
            <template #header>
              <div class="card-header">
                <span>商品审核列表</span>
              </div>
            </template>
            <!-- Filters -->
            <div class="filter-row">
              <el-select v-model="filters.category" placeholder="全部分类" clearable class="filter-item">
                <el-option label="全部分类" value=""></el-option>
                <el-option label="数码电子" value="electronics"></el-option>
                <el-option label="服装鞋包" value="clothing"></el-option>
                 <el-option label="家居家具" value="home"></el-option>
              </el-select>
              <el-select v-model="filters.priority" placeholder="优先级" clearable class="filter-item">
                 <el-option label="全部" value=""></el-option>
                 <el-option label="高" value="high"></el-option>
                 <el-option label="中" value="medium"></el-option>
                 <el-option label="低" value="low"></el-option>
              </el-select>
              <el-select v-model="filters.merchantType" placeholder="商家类型" clearable class="filter-item">
                 <el-option label="全部商家" value=""></el-option>
                 <el-option label="认证商家" value="verified"></el-option>
                 <el-option label="个人卖家" value="regular"></el-option>
              </el-select>
              <el-input
                v-model="filters.keyword"
                placeholder="搜索商品名称/ID"
                :prefix-icon="Search"
                clearable
                class="filter-item search-input"
              />
               <el-button type="primary" :icon="Search" @click="applyFilters">搜索</el-button>
            </div>

            <!-- Table -->
            <el-table :data="paginatedProducts" stripe style="width: 100%">
              <el-table-column label="商品图" width="80">
                <template #default="{ row }">
                  <el-image :src="row.image" fit="cover" class="product-image" />
                </template>
              </el-table-column>
              <el-table-column label="商品信息" min-width="200">
                 <template #default="{ row }">
                   <div>{{ row.name }}</div>
                   <small style="color: #999;">ID: {{ row.id }}</small>
                 </template>
              </el-table-column>
              <el-table-column prop="categoryName" label="分类" width="100"></el-table-column>
              <el-table-column prop="price" label="价格" width="100">
                 <template #default="{ row }">
                    ￥{{ row.price.toFixed(2) }}
                 </template>
              </el-table-column>
               <el-table-column label="卖家" width="150">
                  <template #default="{ row }">
                    <div>{{ row.seller.name }}</div>
                    <el-tag :type="row.seller.type === 'verified' ? 'success' : 'info'" size="small" effect="light">{{ row.seller.typeName }}</el-tag>
                  </template>
               </el-table-column>
              <el-table-column prop="submitTime" label="提交时间" width="160"></el-table-column>
              <el-table-column label="优先级" width="80">
                 <template #default="{ row }">
                    <el-tag :type="getPriorityTagType(row.priority)" size="small">{{ row.priorityName }}</el-tag>
                 </template>
              </el-table-column>
              <el-table-column label="操作" width="120" fixed="right">
                <template #default="{ row }">
                  <el-button link type="primary" :icon="View" size="small" @click="handleView(row)"></el-button>
                  <el-button link type="primary" :icon="Edit" size="small" @click="handleEdit(row)"></el-button>
                   <el-dropdown>
                     <el-button link type="primary" :icon="MoreFilled" size="small" @click.stop></el-button>
                     <template #dropdown>
                       <el-dropdown-menu>
                         <el-dropdown-item>快速通过</el-dropdown-item>
                         <el-dropdown-item>标记问题</el-dropdown-item>
                       </el-dropdown-menu>
                     </template>
                   </el-dropdown>
                </template>
              </el-table-column>
            </el-table>

            <!-- Pagination -->
            <div class="pagination-container">
                <div>显示 {{ (currentPage - 1) * pageSize + 1 }} 至 {{ Math.min(currentPage * pageSize, filteredProducts.length) }} 条，共 {{ filteredProducts.length }} 条</div>
                <el-pagination
                    background
                    layout="prev, pager, next"
                    :total="filteredProducts.length"
                    :page-size="pageSize"
                    :current-page="currentPage"
                    @current-change="handlePageChange"
                />
            </div>

          </el-card>
        </el-tab-pane>

        <el-tab-pane label="品相评级标准" name="condition">
            <el-card shadow="never" class="content-card">
                <template #header>品相评级标准</template>
                <div class="condition-guide-box">
                    <h3 class="condition-title">品相评级参考指南</h3>
                    <el-row :gutter="12">
                        <el-col :span="4" v-for="cond in conditions" :key="cond.label">
                            <div class="condition-card">
                                <div class="condition-label">{{ cond.label }}</div>
                                <div class="condition-desc">{{ cond.desc }}</div>
                            </div>
                        </el-col>
                    </el-row>
                     <p style="margin-top: 15px; font-size: 13px; color: #666;">注：评级需综合考虑商品外观磨损、功能完好度、配件齐全度等因素。</p>
                </div>
                 <!-- Add more content related to condition standards if needed -->
            </el-card>
        </el-tab-pane>

        <el-tab-pane label="真伪鉴定流程" name="authenticity">
            <el-card shadow="never" class="content-card">
                 <template #header>真伪鉴定流程</template>
                 <div class="verification-process">
                     <div class="process-step" v-for="(step, index) in verificationSteps" :key="step.title">
                         <div class="step-circle">{{ index + 1 }}</div>
                         <div class="step-title">{{ step.title }}</div>
                         <div style="font-size: 12px; color: #888; text-align: center;">{{ step.desc }}</div>
                     </div>
                     <!-- Use arrows or connecting lines if desired -->
                 </div>
                  <!-- Add more content related to authenticity process -->
            </el-card>
        </el-tab-pane>

         <el-tab-pane label="图片质量管理" name="image">
             <el-card shadow="never" class="content-card">
                 <template #header>图片质量要求</template>
                 <p>图片质量直接影响用户购买意愿和平台形象，请确保上传的图片满足以下要求：</p>
                 <ul>
                     <li><strong>清晰度：</strong>图片清晰，主体突出，无明显模糊或噪点。</li>
                     <li><strong>光线：</strong>光线充足、均匀，避免过曝或过暗。</li>
                     <li><strong>背景：</strong>背景简洁、干净，避免杂乱干扰。</li>
                     <li><strong>角度：</strong>提供多角度图片，包括整体、细节、瑕疵（如有）。</li>
                     <li><strong>真实性：</strong>图片真实反映商品状况，无过度美化或修饰。</li>
                     <li><strong>尺寸：</strong>建议尺寸不小于 800x800 像素。</li>
                 </ul>
                 <p style="margin-top: 10px;">审核过程中，将对图片质量进行评估，不合格图片需要卖家重新上传。</p>
                 <!-- Add image examples (good/bad) or detailed guidelines -->
             </el-card>
         </el-tab-pane>

         <el-tab-pane label="描述准确性评分" name="description">
            <el-card shadow="never" class="content-card">
                 <template #header>描述准确性评分标准</template>
                 <p>商品描述应准确、完整地反映商品实际情况，评分标准如下：</p>
                 <el-rate v-model="descriptionScoreExample" disabled show-score text-color="#ff9900" score-template="{value} 分 - 示例：非常准确" />
                 <ul>
                     <li><strong>5分 (非常准确):</strong> 描述与实物完全一致，关键信息（品牌、型号、尺寸、材质、成色、瑕疵等）齐全且无误。</li>
                     <li><strong>4分 (比较准确):</strong> 描述基本准确，关键信息完整，可能有轻微疏漏或用词不够精确。</li>
                     <li><strong>3分 (基本准确):</strong> 描述大体符合实物，但缺少部分关键信息或存在少量不影响主要功能的描述偏差。</li>
                     <li><strong>2分 (部分准确):</strong> 描述与实物有较明显出入，关键信息缺失较多或存在误导性描述。</li>
                     <li><strong>1分 (严重不符):</strong> 描述与实物严重不符，存在虚假、夸大信息。</li>
                 </ul>
                  <p style="margin-top: 10px;">审核时将根据实际情况对描述进行评分，低分描述需要卖家修改。</p>
            </el-card>
         </el-tab-pane>
      </el-tabs>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue';
import {
  Download, Setting, Search, Top, Bottom, View, Edit, MoreFilled
} from '@element-plus/icons-vue';
import { ElMessage, ElRate } from 'element-plus';
import type { TagProps } from 'element-plus';

// --- Interfaces & Types ---
type Priority = 'high' | 'medium' | 'low';
type MerchantType = 'verified' | 'regular';

interface Seller {
  name: string;
  type: MerchantType;
  typeName: string;
}

interface Product {
  id: string;
  name: string;
  image: string;
  category: string;
  categoryName: string;
  price: number;
  seller: Seller;
  submitTime: string;
  priority: Priority;
  priorityName: string;
}

// --- Mock Data ---
const stats = ref([
  { title: '待审核商品', value: 86, trend: 'up', change: '较昨日 +12' },
  { title: '今日已审核', value: 42, trend: 'up', change: '完成率 48.8%' },
  { title: '质量不合格率', value: '15.3%', trend: 'down', change: '较上周 -2.1%' },
  { title: '平均审核时长', value: '4.2分钟', trend: 'down', change: '较上周 -0.8分钟' },
]);

const allProducts = ref<Product[]>([
  { id: 'P2023112501', name: 'Apple iPhone 12 128GB 黑色 二手', image: 'https://placehold.co/60x60/3E54AC/FFF?text=P1', category: 'electronics', categoryName: '数码电子', price: 3299, seller: { name: '科技优选', type: 'verified', typeName: '认证商家' }, submitTime: '2023-11-25 09:18', priority: 'high', priorityName: '高' },
  { id: 'P2023112502', name: 'Nike Air Jordan 1 High OG 红黑配色 42码', image: 'https://placehold.co/60x60/F44336/FFF?text=P2', category: 'clothing', categoryName: '服装鞋包', price: 1890, seller: { name: '张小明', type: 'regular', typeName: '个人卖家' }, submitTime: '2023-11-25 10:24', priority: 'medium', priorityName: '中' },
  { id: 'P2023112503', name: '戴森 Dyson V11 吸尘器 紫色', image: 'https://placehold.co/60x60/655DBB/FFF?text=P3', category: 'home', categoryName: '家居家具', price: 2450, seller: { name: '品质家电', type: 'verified', typeName: '认证商家' }, submitTime: '2023-11-25 11:05', priority: 'medium', priorityName: '中' },
  { id: 'P2023112504', name: '哈利·波特与魔法石 精装珍藏版', image: 'https://placehold.co/60x60/FF9800/FFF?text=P4', category: 'books', categoryName: '图书音像', price: 78, seller: { name: '李晓华', type: 'regular', typeName: '个人卖家' }, submitTime: '2023-11-25 13:42', priority: 'low', priorityName: '低' },
  { id: 'P2023112505', name: 'LV Neverfull MM 购物袋 棕色格纹', image: 'https://placehold.co/60x60/4CAF50/FFF?text=P5', category: 'clothing', categoryName: '服装鞋包', price: 6800, seller: { name: '轻奢优品', type: 'verified', typeName: '认证商家' }, submitTime: '2023-11-25 14:36', priority: 'high', priorityName: '高' },
   // Add more for pagination
   { id: 'P2023112601', name: 'Sony PlayStation 5 光驱版', image: 'https://placehold.co/60x60/2196F3/FFF?text=P6', category: 'electronics', categoryName: '数码电子', price: 3500, seller: { name: '游戏玩家', type: 'regular', typeName: '个人卖家' }, submitTime: '2023-11-26 08:00', priority: 'medium', priorityName: '中' },
   { id: 'P2023112602', name: 'Gucci Marmont 迷你链条包 黑色', image: 'https://placehold.co/60x60/000000/FFF?text=P7', category: 'clothing', categoryName: '服装鞋包', price: 8500, seller: { name: '奢品汇', type: 'verified', typeName: '认证商家' }, submitTime: '2023-11-26 09:15', priority: 'high', priorityName: '高' },
]);

const conditions = ref([
    { label: '全新', desc: '未使用，包装完好' },
    { label: '9成新', desc: '轻微使用痕迹，功能完好' },
    { label: '8成新', desc: '正常使用痕迹，轻微划痕/磨损' },
    { label: '7成新', desc: '明显使用痕迹，功能可能有小瑕疵' },
    { label: '6成新及以下', desc: '外观磨损严重或功能有缺陷' },
]);

const verificationSteps = ref([
    { title: '信息核对', desc: '检查品牌/型号/参数' },
    { title: '外观检查', desc: '对比图片与描述' },
    { title: '关键点鉴定', desc: 'Logo/序列号/材质' },
    { title: '辅助工具', desc: '查询官方数据/使用鉴定工具' },
    { title: '综合判断', desc: '给出鉴定结论' },
]);

const descriptionScoreExample = ref(4.5); // Example score for the rate component

// --- State ---
const activeTab = ref('pending');
const filters = reactive({
  category: '',
  priority: '',
  merchantType: '',
  keyword: '',
});
const currentPage = ref(1);
const pageSize = ref(5);

// --- Computed ---
const filteredProducts = computed(() => {
  return allProducts.value.filter(product => {
    const categoryMatch = !filters.category || product.category === filters.category;
    const priorityMatch = !filters.priority || product.priority === filters.priority;
    const merchantMatch = !filters.merchantType || product.seller.type === filters.merchantType;
    const keywordMatch = !filters.keyword ||
                         product.name.toLowerCase().includes(filters.keyword.toLowerCase()) ||
                         product.id.toLowerCase().includes(filters.keyword.toLowerCase());
    return categoryMatch && priorityMatch && merchantMatch && keywordMatch;
  });
});

const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredProducts.value.slice(start, end);
});

// --- Methods ---
const applyFilters = () => {
  currentPage.value = 1; // Reset page when filters change
  ElMessage.success('筛选条件已应用');
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
};

const handleView = (row: Product) => {
  console.log('View product:', row);
  ElMessage.info(`查看商品 ${row.id}`);
  // Navigate to detail/edit view or open modal
};

const handleEdit = (row: Product) => {
  console.log('Edit product:', row);
  ElMessage.info(`开始审核 ${row.id}`);
  // Navigate to detail/edit view or open modal
};

const getPriorityTagType = (priority: Priority): TagProps['type'] => {
  if (priority === 'high') return 'danger';
  if (priority === 'medium') return 'warning';
  return 'success'; // Low priority
};

</script>

<style scoped>
.product-quality-container {
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

.quality-tabs {
  margin-bottom: 20px;
}

.quality-tabs :deep(.el-tabs__header) {
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

.product-image {
  width: 50px;
  height: 50px;
  border-radius: 4px;
  object-fit: cover;
}

.el-table th {
  background-color: #f8f9fc !important;
  font-weight: 500;
  color: #888888;
}

.pagination-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;
    font-size: 14px;
    color: #666;
}

/* Styles for other tabs */
.condition-guide-box {
    background-color: #ecf5ff;
    border: 1px solid #d9ecff;
    border-radius: 8px;
    padding: 16px;
}
.condition-title {
    font-weight: 600;
    margin-bottom: 15px;
    font-size: 16px;
}
.condition-card {
    background-color: white;
    border-radius: 6px;
    padding: 12px;
    text-align: center;
    border: 1px solid var(--el-border-color-light);
    height: 100%; /* Ensure cards have same height */
}
.condition-label {
    font-weight: 600;
    margin-bottom: 6px;
    color: var(--el-color-primary);
}
.condition-desc {
    font-size: 12px;
    color: #888;
}

.verification-process {
    display: flex;
    justify-content: space-around; /* Adjust spacing */
    align-items: flex-start;
    padding: 20px 0;
}
.process-step {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    max-width: 18%; /* Limit width */
}
.step-circle {
    width: 45px;
    height: 45px;
    border-radius: 50%;
    background-color: var(--el-color-primary);
    color: white;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 600;
    margin-bottom: 10px;
    flex-shrink: 0;
}
.step-title {
    font-weight: 500;
    font-size: 14px;
    margin-bottom: 5px;
}

</style> 