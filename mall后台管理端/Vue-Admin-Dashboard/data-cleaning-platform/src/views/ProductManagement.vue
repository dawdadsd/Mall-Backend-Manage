<template>
  <el-container direction="vertical">
    <el-header style="height: auto; padding: 10px;">
      <el-card>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>商品管理</span>
          <el-button type="primary" :icon="Plus" @click="openProductDialog()">添加商品</el-button>
        </div>
        <el-form :inline="true" :model="searchForm" style="margin-top: 15px;">
          <el-form-item label="商品分类">
            <el-select v-model="searchForm.category" placeholder="全部分类">
              <el-option label="全部分类" value=""></el-option>
              <el-option 
                v-for="(name, code) in categories" 
                :key="code" 
                :label="name" 
                :value="code">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="商品状态">
            <el-select v-model="searchForm.status" placeholder="全部状态">
              <el-option label="全部状态" value=""></el-option>
              <el-option 
                v-for="(name, code) in statuses" 
                :key="code" 
                :label="name" 
                :value="code">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="商家粉丝数">
             <el-input-number v-model="searchForm.minFollowers" :min="0" placeholder="最低粉丝数" controls-position="right" style="width: 140px;"></el-input-number>
             <span style="margin: 0 5px;">-</span>
             <el-input-number v-model="searchForm.maxFollowers" :min="0" placeholder="最高粉丝数" controls-position="right" style="width: 140px;"></el-input-number>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-header>

    <el-main style="padding: 10px;">
      <el-card>
        <el-table 
          v-loading="loading"
          :data="products.content" 
          stripe 
          style="width: 100%">
          <el-table-column prop="name" label="商品名称" width="180"></el-table-column>
          <el-table-column prop="category" label="分类" width="100">
            <template #default="{ row }">
              {{ categories[row.category] || row.category }}
            </template>
          </el-table-column>
          <el-table-column prop="merchantName" label="商家" width="120"></el-table-column>
           <el-table-column prop="merchantFansCount" label="商家粉丝数" width="120"></el-table-column>
          <el-table-column prop="sellingPrice" label="价格" width="100">
             <template #default="{ row }">
               ￥{{ row.sellingPrice ? row.sellingPrice.toFixed(2) : '0.00' }}
             </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
             <template #default="{ row }">
               <el-tag :type="getStatusTagType(row.status)" disable-transitions>
                 {{ statuses[row.status] || row.status }}
               </el-tag>
             </template>
          </el-table-column>
           <el-table-column prop="publishTime" label="上传日期" width="150">
             <template #default="{ row }">
               {{ formatDate(row.publishTime) }}
             </template>
           </el-table-column>
          <el-table-column label="操作">
            <template #default="{ row }">
              <el-button link type="primary" size="small" @click="openProductDialog(row)">编辑</el-button>
              <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 分页控件 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="products.totalElements || 0"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </el-main>
    
    <!-- 商品编辑对话框 -->
    <el-dialog 
      v-model="productDialogVisible" 
      :title="editingProduct.id ? '编辑商品' : '添加商品'"
      width="600px">
      <el-form 
        ref="productFormRef"
        :model="editingProduct" 
        :rules="productRules" 
        label-width="100px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="editingProduct.name" placeholder="请输入商品名称"></el-input>
        </el-form-item>
        <el-form-item label="商品分类" prop="category">
          <el-select v-model="editingProduct.category" placeholder="请选择分类" style="width: 100%">
            <el-option 
              v-for="(name, code) in categories" 
              :key="code" 
              :label="name" 
              :value="code">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商家" prop="merchantName">
          <el-input v-model="editingProduct.merchantName" placeholder="请输入商家名称"></el-input>
        </el-form-item>
        <el-form-item label="商家粉丝数" prop="merchantFansCount">
          <el-input-number v-model="editingProduct.merchantFansCount" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="价格" prop="sellingPrice">
          <el-input-number v-model="editingProduct.sellingPrice" :min="0" :precision="2" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="原价" prop="originalPrice">
          <el-input-number v-model="editingProduct.originalPrice" :min="0" :precision="2" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="editingProduct.stock" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="editingProduct.status" placeholder="请选择状态" style="width: 100%">
            <el-option 
              v-for="(name, code) in statuses" 
              :key="code" 
              :label="name" 
              :value="code">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="editingProduct.description" type="textarea" :rows="3" placeholder="请输入商品描述"></el-input>
        </el-form-item>
        <el-form-item label="主图" prop="mainImage">
          <el-upload
            class="upload-demo"
            action="/api/upload"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :http-request="customUpload">
            <img v-if="editingProduct.mainImage" :src="editingProduct.mainImage" class="preview-image" />
            <el-button v-else type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="productDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveProduct">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </el-container>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue';
import { Plus } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import axios from 'axios';
import dayjs from 'dayjs';

// API基础路径
const API_BASE_URL = '/api/products';

// 产品类型定义
interface Product {
  id?: number;
  name: string;
  category: string;
  merchantName: string;
  merchantFansCount: number;
  sellingPrice: number;
  originalPrice?: number;
  status: string;
  stock?: number;
  description?: string;
  mainImage?: string;
  publishTime?: string;
  updateTime?: string;
  createTime?: string;
}

// 响应类型定义
interface ApiResponse<T> {
  success: boolean;
  code: number;
  message: string;
  data: T;
  timestamp: string;
  traceId?: string;
}

// 分页类型定义
interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

// 页面状态
const loading = ref(false);
const products = ref<PageResponse<Product>>({
  content: [],
  totalElements: 0,
  totalPages: 0,
  size: 10,
  number: 0,
  first: true,
  last: false,
  empty: true
});
const categories = ref<Record<string, string>>({});
const statuses = ref<Record<string, string>>({});
const currentPage = ref(1);
const pageSize = ref(10);

// 搜索表单
const searchForm = reactive({
  category: '',
  status: '',
  minFollowers: undefined as number | undefined,
  maxFollowers: undefined as number | undefined,
});

// 商品编辑对话框
const productDialogVisible = ref(false);
const productFormRef = ref();
const editingProduct = reactive<Product>({
  name: '',
  category: '',
  merchantName: '',
  merchantFansCount: 0,
  sellingPrice: 0,
  status: 'DRAFT'
});

// 表单验证规则
const productRules = {
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  merchantName: [
    { required: true, message: '请输入商家名称', trigger: 'blur' }
  ],
  sellingPrice: [
    { required: true, message: '请输入商品价格', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择商品状态', trigger: 'change' }
  ]
};

// 初始化
onMounted(() => {
  loadCategories();
  loadStatuses();
  fetchProducts();
});

// 加载商品分类
const loadCategories = async () => {
  try {
    const response = await axios.get<ApiResponse<Record<string, string>>>(`${API_BASE_URL}/categories`);
    if (response.data.success) {
      categories.value = response.data.data;
    }
  } catch (error) {
    console.error('加载分类失败:', error);
    ElMessage.error('加载分类失败');
  }
};

// 加载商品状态
const loadStatuses = async () => {
  try {
    const response = await axios.get<ApiResponse<Record<string, string>>>(`${API_BASE_URL}/statuses`);
    if (response.data.success) {
      statuses.value = response.data.data;
    }
  } catch (error) {
    console.error('加载状态失败:', error);
    ElMessage.error('加载状态失败');
  }
};

// 获取商品列表
const fetchProducts = async () => {
  loading.value = true;
  try {
    const params = {
      page: currentPage.value - 1, // 后端从0开始
      size: pageSize.value,
      category: searchForm.category,
      status: searchForm.status,
      minFollowers: searchForm.minFollowers,
      maxFollowers: searchForm.maxFollowers
    };
    
    const response = await axios.get<ApiResponse<PageResponse<Product>>>(API_BASE_URL, { params });
    if (response.data.success) {
      products.value = response.data.data;
    } else {
      ElMessage.error(response.data.message || '获取商品列表失败');
    }
  } catch (error) {
    console.error('获取商品列表失败:', error);
    ElMessage.error('获取商品列表失败');
  } finally {
    loading.value = false;
  }
};

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1;
  fetchProducts();
};

// 重置搜索条件
const resetSearch = () => {
  searchForm.category = '';
  searchForm.status = '';
  searchForm.minFollowers = undefined;
  searchForm.maxFollowers = undefined;
  handleSearch();
};

// 处理页面大小变化
const handleSizeChange = (size: number) => {
  pageSize.value = size;
  fetchProducts();
};

// 处理页码变化
const handleCurrentChange = (page: number) => {
  currentPage.value = page;
  fetchProducts();
};

// 获取状态标签类型
const getStatusTagType = (status: string) => {
  const typeMap: Record<string, string> = {
    'PUBLISHED': 'success',
    'DRAFT': 'info',
    'PENDING': 'warning',
    'APPROVED': 'primary',
    'UNPUBLISHED': 'danger',
    'DELETED': 'danger'
  };
  return typeMap[status] || 'info';
};

// 格式化日期
const formatDate = (dateStr?: string) => {
  if (!dateStr) return '';
  return dayjs(dateStr).format('YYYY-MM-DD');
};

// 打开商品对话框
const openProductDialog = (product?: Product) => {
  if (product) {
    // 编辑模式 - 复制对象避免直接修改表格数据
    Object.assign(editingProduct, {
      id: product.id,
      name: product.name,
      category: product.category,
      merchantName: product.merchantName,
      merchantFansCount: product.merchantFansCount,
      sellingPrice: product.sellingPrice,
      originalPrice: product.originalPrice,
      status: product.status,
      stock: product.stock,
      description: product.description,
      mainImage: product.mainImage
    });
  } else {
    // 新增模式 - 重置表单
    Object.assign(editingProduct, {
      id: undefined,
      name: '',
      category: '',
      merchantName: '',
      merchantFansCount: 0,
      sellingPrice: 0,
      originalPrice: 0,
      status: 'DRAFT',
      stock: 0,
      description: '',
      mainImage: ''
    });
  }
  productDialogVisible.value = true;
};

// 上传前验证
const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/');
  const isLt5M = file.size / 1024 / 1024 < 5;

  if (!isImage) {
    ElMessage.error('只能上传图片文件!');
    return false;
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!');
    return false;
  }
  return true;
};

// 自定义上传
const customUpload = async (options: any) => {
  try {
    // 实际项目中这里会调用上传API
    const formData = new FormData();
    formData.append('file', options.file);
    
    // 模拟上传成功，实际项目中应替换为实际API调用
    setTimeout(() => {
      const mockImageUrl = URL.createObjectURL(options.file);
      editingProduct.mainImage = mockImageUrl;
      options.onSuccess();
      ElMessage.success('图片上传成功');
    }, 500);
  } catch (error) {
    console.error('上传失败:', error);
    options.onError();
    ElMessage.error('图片上传失败');
  }
};

// 保存商品
const saveProduct = async () => {
  // 表单验证
  await productFormRef.value.validate().catch(() => { throw new Error('表单验证失败'); });
  
  try {
    let response;
    if (editingProduct.id) {
      // 更新商品
      response = await axios.put<ApiResponse<Product>>(
        `${API_BASE_URL}/${editingProduct.id}`, 
        editingProduct
      );
    } else {
      // 创建商品
      response = await axios.post<ApiResponse<Product>>(
        API_BASE_URL, 
        editingProduct
      );
    }
    
    if (response.data.success) {
      ElMessage.success(editingProduct.id ? '更新成功' : '创建成功');
      productDialogVisible.value = false;
      fetchProducts(); // 刷新列表
    } else {
      ElMessage.error(response.data.message || '操作失败');
    }
  } catch (error) {
    console.error('保存商品失败:', error);
    ElMessage.error('保存商品失败');
  }
};

// 处理删除
const handleDelete = (product: Product) => {
  ElMessageBox.confirm(
    `确定要删除商品 "${product.name}" 吗?`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      const response = await axios.delete<ApiResponse<void>>(`${API_BASE_URL}/${product.id}`);
      if (response.data.success) {
        ElMessage.success('删除成功');
        fetchProducts(); // 刷新列表
      } else {
        ElMessage.error(response.data.message || '删除失败');
    }
    } catch (error) {
      console.error('删除商品失败:', error);
      ElMessage.error('删除失败');
    }
  }).catch(() => {
    ElMessage.info('已取消删除');
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
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
.preview-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}
</style> 