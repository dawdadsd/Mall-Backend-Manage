<template>
  <div class="user-management">
    <!-- 数据统计卡片 -->
    <div class="stats-cards">
      <el-card class="stats-card">
        <div class="card-header">总用户数</div>
        <div class="card-value">128</div>
      </el-card>

      <el-card class="stats-card">
        <div class="card-header">本周新增用户</div>
        <div class="card-value">15</div>
        <div class="card-change increase">
          ↑ 12% 增长
        </div>
      </el-card>

      <el-card class="stats-card">
        <div class="card-header">活跃用户</div>
        <div class="card-value">86</div>
        <div class="card-change increase">
          ↑ 5% 增长
        </div>
      </el-card>

      <el-card class="stats-card">
        <div class="card-header">邮箱注册占比</div>
        <div class="card-value">65%</div>
      </el-card>
    </div>

    <!-- 最近注册用户和全部用户 -->
    <div class="user-section">
      <div class="section-header">
        <div class="section-title">最近注册用户</div>
        <el-button type="text" @click="tabActive = 'all'">查看全部</el-button>
      </div>

      <el-tabs v-model="tabActive" class="user-tabs">
        <el-tab-pane label="最近注册" name="recent">
          <div class="recent-users">
            <div class="recent-user-item" v-for="user in recentUsers" :key="user.id">
              <div class="user-avatar">{{ user.username.substring(0, 2).toUpperCase() }}</div>
              <div class="user-info">
                <div class="user-name">{{ user.name }}</div>
                <div class="user-email">{{ user.email }}</div>
              </div>
              <div class="register-time">{{ user.registrationTime }}</div>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="全部用户" name="all">
          <!-- 用户搜索和筛选 -->
          <div class="user-search-filters">
            <div class="search-input">
              <el-input placeholder="搜索用户名、手机号" v-model="searchText" prefix-icon="Search">
                <template #append>
                  <el-button>搜索</el-button>
                </template>
              </el-input>
            </div>

            <div class="filters">
              <el-select v-model="roleFilter" placeholder="角色" clearable>
                <el-option v-for="role in roles" :key="role.id" :label="role.name" :value="role.id" />
              </el-select>

              <el-select v-model="statusFilter" placeholder="状态" clearable>
                <el-option label="活跃" value="active" />
                <el-option label="已禁用" value="disabled" />
              </el-select>

              <el-select v-model="registrationTypeFilter" placeholder="注册方式" clearable>
                <el-option label="邮箱" value="email" />
                <el-option label="手机号" value="phone" />
                <el-option label="第三方" value="third-party" />
              </el-select>

              <el-button type="primary" @click="onAddUserClick">添加用户</el-button>
            </div>
          </div>

          <!-- 用户列表表格 -->
          <el-table :data="userList" style="width: 100%" border>
            <el-table-column prop="username" label="用户名" width="120" />
            <el-table-column prop="name" label="姓名" width="120" />
            <el-table-column prop="email" label="邮箱" width="180" />
            <el-table-column prop="phone" label="手机号" width="120" />
            <el-table-column prop="registrationType" label="注册方式" width="100">
              <template #default="scope">
                <el-tag size="small" :type="getRegistrationTypeTag(scope.row.registrationType)">
                  {{ getRegistrationTypeLabel(scope.row.registrationType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="registrationTime" label="注册时间" width="160" />
            <el-table-column prop="roleName" label="角色" width="100" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.status === 'active' ? 'success' : 'danger'" size="small">
                  {{ scope.row.status === 'active' ? '活跃' : '已禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="lastLogin" label="最后登录" width="160" />
            <el-table-column label="操作" width="160" fixed="right">
              <template #default="scope">
                <el-button link type="primary" size="small" @click="onEditUser(scope.row)">编辑</el-button>
                <el-button link type="primary" size="small" @click="onAssignRoles(scope.row)">分配角色</el-button>
                <el-button
                  link
                  :type="scope.row.status === 'active' ? 'danger' : 'success'"
                  size="small"
                  @click="onToggleUserStatus(scope.row)"
                >
                  {{ scope.row.status === 'active' ? '禁用' : '启用' }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页控件 -->
          <div class="pagination">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="totalUsers"
              @size-change="onPageSizeChange"
              @current-change="onCurrentPageChange"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 用户表单对话框 -->
    <el-dialog
      v-model="userFormVisible"
      :title="isEditing ? '编辑用户' : '添加用户'"
      width="500px"
    >
      <el-form :model="userForm" label-width="100px" :rules="userRules" ref="userFormRef">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" />
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="userForm.roleId" placeholder="选择角色">
            <el-option
              v-for="role in roles"
              :key="role.id"
              :label="role.name"
              :value="role.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio label="active">活跃</el-radio>
            <el-radio label="disabled">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="!isEditing" label="密码" prop="password">
          <el-input v-model="userForm.password" type="password" />
        </el-form-item>
        <el-form-item v-if="!isEditing" label="确认密码" prop="confirmPassword">
          <el-input v-model="userForm.confirmPassword" type="password" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="userFormVisible = false">取消</el-button>
          <el-button type="primary" @click="onSaveUser">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 角色分配对话框 -->
    <el-dialog v-model="roleAssignVisible" title="分配角色" width="400px">
      <el-form :model="roleAssignForm">
        <el-form-item label="用户">
          <span>{{ roleAssignForm.username }}</span>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="roleAssignForm.roleId" placeholder="选择角色">
            <el-option
              v-for="role in roles"
              :key="role.id"
              :label="role.name"
              :value="role.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="roleAssignVisible = false">取消</el-button>
          <el-button type="primary" @click="onSaveRoleAssign">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox, FormInstance } from 'element-plus';
import type { FormRules } from 'element-plus';

// 用户类型定义
interface User {
  id: string;
  username: string;
  name: string;
  email: string;
  phone: string;
  registrationType: string;
  registrationTime: string;
  roleId: string;
  roleName: string;
  status: string;
  lastLogin: string;
}

// 角色类型定义
interface Role {
  id: string;
  name: string;
  description: string;
  userCount: number;
  creationTime: string;
}

// 选项卡激活状态
const tabActive = ref('recent');

// 搜索和筛选条件
const searchText = ref('');
const roleFilter = ref('');
const statusFilter = ref('');
const registrationTypeFilter = ref('');

// 分页参数
const currentPage = ref(1);
const pageSize = ref(10);
const totalUsers = ref(28);

// 最近注册用户数据
const recentUsers = ref<User[]>([
  {
    id: '1',
    username: 'zhangsan',
    name: '张三',
    email: 'zhangsan@example.com',
    phone: '13800138001',
    registrationType: 'email',
    registrationTime: '2025-04-26 10:30:00',
    roleId: '1',
    roleName: '管理员',
    status: 'active',
    lastLogin: '2025-04-27 08:15:00'
  },
  {
    id: '2',
    username: 'lisi',
    name: '李四',
    email: 'lisi@example.com',
    phone: '13800138002',
    registrationType: 'phone',
    registrationTime: '2025-04-25 15:20:00',
    roleId: '2',
    roleName: '操作员',
    status: 'active',
    lastLogin: '2025-04-26 14:30:00'
  },
  {
    id: '3',
    username: 'wangwu',
    name: '王五',
    email: 'wangwu@example.com',
    phone: '13800138003',
    registrationType: 'email',
    registrationTime: '2025-04-24 09:10:00',
    roleId: '2',
    roleName: '操作员',
    status: 'active',
    lastLogin: '2025-04-26 11:45:00'
  }
]);

// 所有用户列表数据
const userList = ref<User[]>([...recentUsers.value,
  {
    id: '4',
    username: 'zhaoliu',
    name: '赵六',
    email: 'zhaoliu@example.com',
    phone: '13800138004',
    registrationType: 'third-party',
    registrationTime: '2025-04-23 11:20:00',
    roleId: '3',
    roleName: '访客',
    status: 'disabled',
    lastLogin: '2025-04-24 16:30:00'
  },
  {
    id: '5',
    username: 'sunqi',
    name: '孙七',
    email: 'sunqi@example.com',
    phone: '13800138005',
    registrationType: 'email',
    registrationTime: '2025-04-22 14:15:00',
    roleId: '2',
    roleName: '操作员',
    status: 'active',
    lastLogin: '2025-04-25 10:20:00'
  }
]);

// 角色列表
const roles = ref<Role[]>([
  { id: '1', name: '管理员', description: '系统管理员, 拥有所有权限', userCount: 3, creationTime: '2025-01-01 00:00:00' },
  { id: '2', name: '操作员', description: '数据操作员, 可以执行数据清洗任务', userCount: 15, creationTime: '2025-01-01 00:00:00' },
  { id: '3', name: '访客', description: '只读权限', userCount: 10, creationTime: '2025-01-01 00:00:00' }
]);

// 用户表单相关
const userFormVisible = ref(false);
const isEditing = ref(false);
const userFormRef = ref<FormInstance>();
const userForm = reactive({
  id: '',
  username: '',
  name: '',
  email: '',
  phone: '',
  roleId: '',
  status: 'active',
  password: '',
  confirmPassword: ''
});

// 用户表单验证规则
const validatePass = (rule: any, value: string, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'));
  } else {
    if (userForm.confirmPassword !== '') {
      userFormRef.value?.validateField('confirmPassword');
    }
    callback();
  }
};

const validatePass2 = (rule: any, value: string, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'));
  } else if (value !== userForm.password) {
    callback(new Error('两次输入密码不一致'));
  } else {
    callback();
  }
};

const userRules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  roleId: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ],
  password: [
    { validator: validatePass, trigger: 'blur' }
  ],
  confirmPassword: [
    { validator: validatePass2, trigger: 'blur' }
  ]
});

// 角色分配表单
const roleAssignVisible = ref(false);
const roleAssignForm = reactive({
  userId: '',
  username: '',
  roleId: ''
});

// 获取注册方式标签类型
const getRegistrationTypeTag = (type: string): string => {
  const typeMap: Record<string, string> = {
    'email': 'success',
    'phone': 'primary',
    'third-party': 'warning'
  };
  return typeMap[type] || 'info';
};

// 获取注册方式显示文本
const getRegistrationTypeLabel = (type: string): string => {
  const typeMap: Record<string, string> = {
    'email': '邮箱',
    'phone': '手机号',
    'third-party': '第三方'
  };
  return typeMap[type] || '未知';
};

// 初始化页面数据
onMounted(() => {
  // 真实环境中应该从API获取数据
  console.log('User Management component mounted');
});

// 分页变化处理
const onPageSizeChange = (size: number) => {
  pageSize.value = size;
  // 重新加载数据
  loadUserList();
};

const onCurrentPageChange = (page: number) => {
  currentPage.value = page;
  // 重新加载数据
  loadUserList();
};

// 加载用户列表（模拟）
const loadUserList = () => {
  // 真实环境中应该通过API获取数据
  console.log('Loading user list with filters:');
  console.log({
    search: searchText.value,
    role: roleFilter.value,
    status: statusFilter.value,
    registrationType: registrationTypeFilter.value,
    page: currentPage.value,
    pageSize: pageSize.value
  });
};

// 添加用户按钮点击处理
const onAddUserClick = () => {
  isEditing.value = false;
  resetUserForm();
  userFormVisible.value = true;
};

// 编辑用户处理
const onEditUser = (user: User) => {
  isEditing.value = true;
  Object.assign(userForm, {
    id: user.id,
    username: user.username,
    name: user.name,
    email: user.email,
    phone: user.phone,
    roleId: user.roleId,
    status: user.status,
    password: '',
    confirmPassword: ''
  });
  userFormVisible.value = true;
};

// 保存用户表单
const onSaveUser = () => {
  userFormRef.value?.validate((valid) => {
    if (valid) {
      // 真实环境中应该通过API保存数据
      if (isEditing.value) {
        // 更新已有用户
        const index = userList.value.findIndex(u => u.id === userForm.id);
        if (index !== -1) {
          const role = roles.value.find(r => r.id === userForm.roleId);
          userList.value[index] = {
            ...userList.value[index],
            username: userForm.username,
            name: userForm.name,
            email: userForm.email,
            phone: userForm.phone,
            roleId: userForm.roleId,
            roleName: role?.name || '',
            status: userForm.status
          };
          ElMessage.success('用户更新成功');
        }
      } else {
        // 创建新用户
        const now = new Date().toLocaleString();
        const role = roles.value.find(r => r.id === userForm.roleId);
        const newUser: User = {
          id: String(userList.value.length + 1),
          username: userForm.username,
          name: userForm.name,
          email: userForm.email,
          phone: userForm.phone,
          registrationType: userForm.email ? 'email' : 'phone',
          registrationTime: now,
          roleId: userForm.roleId,
          roleName: role?.name || '',
          status: userForm.status,
          lastLogin: '-'
        };
        userList.value.unshift(newUser);
        totalUsers.value++;
        ElMessage.success('用户创建成功');
      }
      userFormVisible.value = false;
    } else {
      return false;
    }
  });
};

// 重置用户表单
const resetUserForm = () => {
  Object.assign(userForm, {
    id: '',
    username: '',
    name: '',
    email: '',
    phone: '',
    roleId: '',
    status: 'active',
    password: '',
    confirmPassword: ''
  });
  userFormRef.value?.resetFields();
};

// 切换用户状态
const onToggleUserStatus = (user: User) => {
  const action = user.status === 'active' ? '禁用' : '启用';
  ElMessageBox.confirm(`确定要${action}用户 ${user.username} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 真实环境中应该通过API更新状态
    const index = userList.value.findIndex(u => u.id === user.id);
    if (index !== -1) {
      userList.value[index].status = user.status === 'active' ? 'disabled' : 'active';
      ElMessage.success(`用户${action}成功`);
    }
  }).catch(() => {
    // 取消操作
  });
};

// 分配角色
const onAssignRoles = (user: User) => {
  roleAssignForm.userId = user.id;
  roleAssignForm.username = user.username;
  roleAssignForm.roleId = user.roleId;
  roleAssignVisible.value = true;
};

// 保存角色分配
const onSaveRoleAssign = () => {
  // 真实环境中应该通过API更新角色
  const index = userList.value.findIndex(u => u.id === roleAssignForm.userId);
  if (index !== -1) {
    const role = roles.value.find(r => r.id === roleAssignForm.roleId);
    userList.value[index].roleId = roleAssignForm.roleId;
    userList.value[index].roleName = role?.name || '';
    ElMessage.success('角色分配成功');
  }
  roleAssignVisible.value = false;
};
</script>

<style scoped>
.user-management {
  width: 100%;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
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

.user-section {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.user-tabs {
  margin-top: 10px;
}

.recent-users {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.recent-user-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.recent-user-item:hover {
  background-color: #f5f7fa;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #6d5dfc;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  margin-right: 15px;
}

.user-info {
  flex: 1;
}

.user-name {
  font-weight: 500;
  margin-bottom: 5px;
}

.user-email {
  font-size: 12px;
  color: #909399;
}

.register-time {
  color: #909399;
  font-size: 12px;
}

.user-search-filters {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 20px;
}

.filters {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.filters .el-select {
  width: 120px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
