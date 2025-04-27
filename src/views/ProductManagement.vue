<template>
  <el-container direction="vertical">
    <el-header style="height: auto; padding: 10px;">
      <el-card>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>商品管理</span>
          <el-button type="primary" :icon="Plus">添加商品</el-button>
        </div>
        <el-form :inline="true" :model="searchForm" style="margin-top: 15px;">
          <el-form-item label="商品分类">
            <el-select v-model="searchForm.category" placeholder="全部分类">
              <el-option label="全部分类" value=""></el-option>
              <el-option label="电子产品" value="electronics"></el-option>
              <el-option label="服装" value="clothing"></el-option>
              <el-option label="家居" value="home"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="商品状态">
            <el-select v-model="searchForm.status" placeholder="全部状态">
              <el-option label="全部状态" value=""></el-option>
              <el-option label="在售" value="on_sale"></el-option>
              <el-option label="下架" value="off_shelf"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="商家粉丝数">
             <el-input-number v-model="searchForm.minFollowers" :min="0" placeholder="最低粉丝数" controls-position="right" style="width: 140px;"></el-input-number>
             <span style="margin: 0 5px;">-</span>
             <el-input-number v-model="searchForm.maxFollowers" :min="0" placeholder="最高粉丝数" controls-position="right" style="width: 140px;"></el-input-number>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-header>

    <el-main style="padding: 10px;">
      <el-card>
        <el-table :data="filteredProducts" stripe style="width: 100%">
          <el-table-column prop="name" label="商品名称" width="180"></el-table-column>
          <el-table-column prop="category" label="分类" width="100">
            <template #default="{ row }">
              {{ categoryMap[row.category] }}
            </template>
          </el-table-column>
          <el-table-column prop="seller" label="商家" width="120"></el-table-column>
           <el-table-column prop="sellerFollowers" label="商家粉丝数" width="120"></el-table-column>
          <el-table-column prop="price" label="价格" width="100">
             <template #default="{ row }">
               ￥{{ row.price.toFixed(2) }}
             </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
             <template #default="{ row }">
               <el-tag :type="row.status === 'on_sale' ? 'success' : 'info'" disable-transitions>
                 {{ statusMap[row.status] }}
               </el-tag>
             </template>
          </el-table-column>
           <el-table-column prop="uploadDate" label="上传日期" width="150"></el-table-column>
          <el-table-column label="操作">
            <template #default="{ row }">
              <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue';
import { Plus } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';

// 模拟商品数据接口
interface Product {
  id: number;
  name: string;
  category: string;
  seller: string;
  sellerFollowers: number;
  price: number;
  status: 'on_sale' | 'off_shelf';
  uploadDate: string;
}

// 模拟分类和状态映射
const categoryMap: Record<string, string> = {
  electronics: '电子产品',
  clothing: '服装',
  home: '家居',
};
const statusMap: Record<string, string> = {
  on_sale: '在售',
  off_shelf: '下架',
};

// 搜索表单
const searchForm = reactive({
  category: '',
  status: '',
  minFollowers: undefined as number | undefined,
  maxFollowers: undefined as number | undefined,
});

// 模拟商品列表数据
const products = ref<Product[]>([
  { id: 1, name: '智能手机 Pro', category: 'electronics', seller: '数码旗舰店', sellerFollowers: 15000, price: 4999, status: 'on_sale', uploadDate: '2024-05-01' },
  { id: 2, name: '夏季T恤', category: 'clothing', seller: '潮流服饰', sellerFollowers: 8000, price: 129, status: 'on_sale', uploadDate: '2024-04-28' },
  { id: 3, name: '舒适沙发', category: 'home', seller: '温馨家居', sellerFollowers: 25000, price: 2599, status: 'off_shelf', uploadDate: '2024-03-15' },
  { id: 4, name: '笔记本电脑 Air', category: 'electronics', seller: '电脑专营', sellerFollowers: 500, price: 6999, status: 'on_sale', uploadDate: '2024-05-10' },
  { id: 5, name: '牛仔裤', category: 'clothing', seller: '潮流服饰', sellerFollowers: 8000, price: 299, status: 'on_sale', uploadDate: '2024-05-05' },
  { id: 6, name: '吸尘器 V10', category: 'home', seller: '电器大卖场', sellerFollowers: 55000, price: 1999, status: 'on_sale', uploadDate: '2024-04-20' },
]);

// 计算属性：根据搜索条件过滤产品
const filteredProducts = computed(() => {
  return products.value.filter(product => {
    const categoryMatch = !searchForm.category || product.category === searchForm.category;
    const statusMatch = !searchForm.status || product.status === searchForm.status;
    const minFollowersMatch = searchForm.minFollowers === undefined || product.sellerFollowers >= searchForm.minFollowers;
    const maxFollowersMatch = searchForm.maxFollowers === undefined || product.sellerFollowers <= searchForm.maxFollowers;
    return categoryMatch && statusMatch && minFollowersMatch && maxFollowersMatch;
  });
});

// 处理搜索事件（目前只是示意性，因为过滤是实时计算的）
const handleSearch = () => {
  console.log('搜索条件:', searchForm);
  // 实际应用中，这里可能会触发 API 请求
  ElMessage.success('搜索已应用');
};

// 处理编辑事件
const handleEdit = (row: Product) => {
  console.log('编辑商品:', row);
  ElMessage.info(`编辑商品 ID: ${row.id}`);
  // 这里可以弹出编辑对话框等
};

// 处理删除事件
const handleDelete = (row: Product) => {
  ElMessageBox.confirm(
    `确定要删除商品 "${row.name}" 吗?`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    console.log('删除商品:', row);
    // 实际应用中，这里会调用 API 删除数据
    // 模拟删除
    const index = products.value.findIndex(p => p.id === row.id);
    if (index !== -1) {
      products.value.splice(index, 1);
    }
    ElMessage.success('删除成功');
  }).catch(() => {
    ElMessage.info('取消删除');
  });
};
</script>

<style scoped>
.el-header {
  background-color: #f4f4f5;
}
.el-main {
  padding-top: 0;
}
</style> 