<template>
  <div class="role-management">
    <!-- 角色列表区域 -->
    <div class="role-section">
      <div class="section-header">
        <div class="section-title">角色管理</div>
        <el-button type="primary" @click="onAddRoleClick">添加角色</el-button>
      </div>

      <!-- 角色列表表格 -->
      <el-table :data="roleList" style="width: 100%" border>
        <el-table-column prop="name" label="角色名称" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="userCount" label="用户数量" width="120" />
        <el-table-column prop="creationTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="onEditRole(scope.row)">编辑</el-button>
            <el-button link type="primary" size="small" @click="onSetPermissions(scope.row)">权限设置</el-button>
            <el-button
              link
              type="danger"
              size="small"
              @click="onDeleteRole(scope.row)"
              :disabled="scope.row.userCount > 0"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 角色表单对话框 -->
    <el-dialog
      v-model="roleFormVisible"
      :title="isEditing ? '编辑角色' : '添加角色'"
      width="500px"
    >
      <el-form :model="roleForm" label-width="100px" :rules="roleRules" ref="roleFormRef">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="roleForm.name" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="roleForm.description" type="textarea" rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="roleFormVisible = false">取消</el-button>
          <el-button type="primary" @click="onSaveRole">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 权限设置对话框 -->
    <el-dialog
      v-model="permissionSettingVisible"
      title="权限设置"
      width="700px"
    >
      <div class="permission-dialog-header">
        <span class="role-name">角色: {{ currentRole.name }}</span>
        <span class="role-desc">{{ currentRole.description }}</span>
      </div>

      <div class="permission-tree-container">
        <el-tree
          ref="permissionTreeRef"
          :data="permissionTree"
          :props="defaultProps"
          show-checkbox
          node-key="id"
          default-expand-all
          :default-checked-keys="checkedPermissions"
        />
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="permissionSettingVisible = false">取消</el-button>
          <el-button type="primary" @click="onSavePermissions">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox, FormInstance } from 'element-plus';
import type { FormRules } from 'element-plus';

// 角色类型定义
interface Role {
  id: string;
  name: string;
  description: string;
  userCount: number;
  creationTime: string;
  permissions?: string[];
}

// 权限树节点类型
interface PermissionNode {
  id: string;
  label: string;
  children?: PermissionNode[];
}

// 角色列表数据
const roleList = ref<Role[]>([
  {
    id: '1',
    name: '管理员',
    description: '系统管理员, 拥有所有权限',
    userCount: 3,
    creationTime: '2025-01-01 00:00:00',
    permissions: ['1', '1-1', '1-2', '1-3', '2', '2-1', '2-2', '3', '3-1', '3-2', '3-3']
  },
  {
    id: '2',
    name: '操作员',
    description: '数据操作员, 可以执行数据清洗任务',
    userCount: 15,
    creationTime: '2025-01-01 00:00:00',
    permissions: ['2', '2-1', '2-2', '3-1', '3-2']
  },
  {
    id: '3',
    name: '访客',
    description: '只读权限',
    userCount: 10,
    creationTime: '2025-01-01 00:00:00',
    permissions: ['2-1', '3-1']
  }
]);

// 角色表单相关
const roleFormVisible = ref(false);
const isEditing = ref(false);
const roleFormRef = ref<FormInstance>();
const roleForm = reactive({
  id: '',
  name: '',
  description: ''
});

// 角色表单验证规则
const roleRules = reactive<FormRules>({
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入角色描述', trigger: 'blur' }
  ]
});

// 权限设置相关
const permissionSettingVisible = ref(false);
const permissionTreeRef = ref();
const checkedPermissions = ref<string[]>([]);
const currentRole = ref<Role>({
  id: '',
  name: '',
  description: '',
  userCount: 0,
  creationTime: ''
});

// 权限树数据
const permissionTree = ref<PermissionNode[]>([
  {
    id: '1',
    label: '系统管理',
    children: [
      { id: '1-1', label: '用户管理' },
      { id: '1-2', label: '角色管理' },
      { id: '1-3', label: '系统设置' }
    ]
  },
  {
    id: '2',
    label: '数据操作',
    children: [
      { id: '2-1', label: '查看数据' },
      { id: '2-2', label: '编辑数据' }
    ]
  },
  {
    id: '3',
    label: '任务管理',
    children: [
      { id: '3-1', label: '查看任务' },
      { id: '3-2', label: '创建任务' },
      { id: '3-3', label: '删除任务' }
    ]
  }
]);

// 权限树配置
const defaultProps = {
  children: 'children',
  label: 'label'
};

// 初始化页面数据
onMounted(() => {
  // 真实环境中应该从API获取数据
  console.log('Role Management component mounted');
});

// 添加角色按钮点击处理
const onAddRoleClick = () => {
  isEditing.value = false;
  resetRoleForm();
  roleFormVisible.value = true;
};

// 编辑角色处理
const onEditRole = (role: Role) => {
  isEditing.value = true;
  Object.assign(roleForm, {
    id: role.id,
    name: role.name,
    description: role.description
  });
  roleFormVisible.value = true;
};

// 保存角色表单
const onSaveRole = () => {
  roleFormRef.value?.validate((valid) => {
    if (valid) {
      // 真实环境中应该通过API保存数据
      if (isEditing.value) {
        // 更新已有角色
        const index = roleList.value.findIndex(r => r.id === roleForm.id);
        if (index !== -1) {
          roleList.value[index] = {
            ...roleList.value[index],
            name: roleForm.name,
            description: roleForm.description
          };
          ElMessage.success('角色更新成功');
        }
      } else {
        // 创建新角色
        const now = new Date().toLocaleString();
        const newRole: Role = {
          id: String(roleList.value.length + 1),
          name: roleForm.name,
          description: roleForm.description,
          userCount: 0,
          creationTime: now,
          permissions: []
        };
        roleList.value.push(newRole);
        ElMessage.success('角色创建成功');
      }
      roleFormVisible.value = false;
    } else {
      return false;
    }
  });
};

// 重置角色表单
const resetRoleForm = () => {
  Object.assign(roleForm, {
    id: '',
    name: '',
    description: ''
  });
  roleFormRef.value?.resetFields();
};

// 设置权限
const onSetPermissions = (role: Role) => {
  currentRole.value = { ...role };
  checkedPermissions.value = role.permissions || [];
  permissionSettingVisible.value = true;
};

// 保存权限设置
const onSavePermissions = () => {
  // 获取选中的权限
  const selectedKeys = permissionTreeRef.value?.getCheckedKeys() || [];
  const halfSelectedKeys = permissionTreeRef.value?.getHalfCheckedKeys() || [];
  const allSelectedKeys = [...selectedKeys, ...halfSelectedKeys];

  // 真实环境中应该通过API更新权限
  const index = roleList.value.findIndex(r => r.id === currentRole.value.id);
  if (index !== -1) {
    roleList.value[index].permissions = allSelectedKeys;
    ElMessage.success('权限设置成功');
  }
  permissionSettingVisible.value = false;
};

// 删除角色
const onDeleteRole = (role: Role) => {
  // 如果角色下有用户，不允许删除
  if (role.userCount > 0) {
    ElMessage.warning(`角色 ${role.name} 下有 ${role.userCount} 个用户，无法删除`);
    return;
  }

  ElMessageBox.confirm(`确定要删除角色 ${role.name} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 真实环境中应该通过API删除
    const index = roleList.value.findIndex(r => r.id === role.id);
    if (index !== -1) {
      roleList.value.splice(index, 1);
      ElMessage.success('角色删除成功');
    }
  }).catch(() => {
    // 取消操作
  });
};
</script>

<style scoped>
.role-management {
  width: 100%;
}

.role-section {
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
  margin-bottom: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

/* 权限设置相关 */
.permission-dialog-header {
  margin-bottom: 20px;
}

.role-name {
  font-size: 16px;
  font-weight: 500;
  margin-right: 10px;
}

.role-desc {
  color: #909399;
  font-size: 14px;
}

.permission-tree-container {
  max-height: 400px;
  overflow-y: auto;
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  padding: 10px;
}
</style>
