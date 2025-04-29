<template>
  <div class="system-settings">
    <el-card class="settings-card">
      <template #header>
        <div class="card-header">
          <span>系统设置</span>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <!-- 基本设置 -->
        <el-tab-pane label="基本设置" name="basic">
          <el-form
            ref="basicFormRef"
            :model="basicForm"
            label-width="120px"
            :rules="basicRules"
          >
            <el-form-item label="系统名称" prop="systemName">
              <el-input v-model="basicForm.systemName" placeholder="请输入系统名称" />
            </el-form-item>

            <el-form-item label="系统描述" prop="systemDescription">
              <el-input
                v-model="basicForm.systemDescription"
                type="textarea"
                rows="3"
                placeholder="请输入系统描述"
              />
            </el-form-item>

            <el-form-item label="管理员邮箱" prop="adminEmail">
              <el-input v-model="basicForm.adminEmail" placeholder="请输入管理员邮箱" />
            </el-form-item>

            <el-form-item label="技术支持电话" prop="supportPhone">
              <el-input v-model="basicForm.supportPhone" placeholder="请输入技术支持电话" />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="onSaveBasicSettings">保存设置</el-button>
              <el-button @click="resetBasicForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 数据清洗设置 -->
        <el-tab-pane label="数据清洗设置" name="dataClean">
          <el-form
            ref="dataCleanFormRef"
            :model="dataCleanForm"
            label-width="160px"
            :rules="dataCleanRules"
          >
            <el-form-item label="最大并行任务数" prop="maxParallelTasks">
              <el-input-number
                v-model="dataCleanForm.maxParallelTasks"
                :min="1"
                :max="100"
                placeholder="请输入最大并行任务数"
              />
            </el-form-item>

            <el-form-item label="任务超时时间(分钟)" prop="taskTimeout">
              <el-input-number
                v-model="dataCleanForm.taskTimeout"
                :min="1"
                :max="1440"
                placeholder="请输入任务超时时间"
              />
            </el-form-item>

            <el-form-item label="数据保留天数" prop="dataRetentionDays">
              <el-input-number
                v-model="dataCleanForm.dataRetentionDays"
                :min="1"
                :max="365"
                placeholder="请输入数据保留天数"
              />
            </el-form-item>

            <el-form-item label="异常数据阈值(%)" prop="errorThreshold">
              <el-input-number
                v-model="dataCleanForm.errorThreshold"
                :min="0"
                :max="100"
                :precision="1"
                :step="0.1"
                placeholder="请输入异常数据阈值"
              />
            </el-form-item>

            <el-form-item label="开启自动备份" prop="enableAutoBackup">
              <el-switch v-model="dataCleanForm.enableAutoBackup" />
            </el-form-item>

            <el-form-item label="备份间隔(小时)" prop="backupInterval" v-if="dataCleanForm.enableAutoBackup">
              <el-input-number
                v-model="dataCleanForm.backupInterval"
                :min="1"
                :max="168"
                placeholder="请输入备份间隔"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="onSaveDataCleanSettings">保存设置</el-button>
              <el-button @click="resetDataCleanForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 通知设置 -->
        <el-tab-pane label="通知设置" name="notification">
          <el-form
            ref="notificationFormRef"
            :model="notificationForm"
            label-width="120px"
            :rules="notificationRules"
          >
            <el-form-item label="启用邮件通知" prop="enableEmailNotification">
              <el-switch v-model="notificationForm.enableEmailNotification" />
            </el-form-item>

            <template v-if="notificationForm.enableEmailNotification">
              <el-form-item label="SMTP服务器" prop="smtpServer">
                <el-input v-model="notificationForm.smtpServer" placeholder="请输入SMTP服务器地址" />
              </el-form-item>

              <el-form-item label="SMTP端口" prop="smtpPort">
                <el-input-number v-model="notificationForm.smtpPort" :min="1" :max="65535" />
              </el-form-item>

              <el-form-item label="发件人邮箱" prop="senderEmail">
                <el-input v-model="notificationForm.senderEmail" placeholder="请输入发件人邮箱" />
              </el-form-item>

              <el-form-item label="SMTP用户名" prop="smtpUsername">
                <el-input v-model="notificationForm.smtpUsername" placeholder="请输入SMTP用户名" />
              </el-form-item>

              <el-form-item label="SMTP密码" prop="smtpPassword">
                <el-input
                  v-model="notificationForm.smtpPassword"
                  type="password"
                  show-password
                  placeholder="请输入SMTP密码"
                />
              </el-form-item>
            </template>

            <el-form-item label="启用短信通知" prop="enableSmsNotification">
              <el-switch v-model="notificationForm.enableSmsNotification" />
            </el-form-item>

            <template v-if="notificationForm.enableSmsNotification">
              <el-form-item label="SMS API密钥" prop="smsApiKey">
                <el-input v-model="notificationForm.smsApiKey" placeholder="请输入SMS API密钥" />
              </el-form-item>

              <el-form-item label="SMS模板ID" prop="smsTemplateId">
                <el-input v-model="notificationForm.smsTemplateId" placeholder="请输入SMS模板ID" />
              </el-form-item>

              <el-form-item label="SMS签名" prop="smsSignName">
                <el-input v-model="notificationForm.smsSignName" placeholder="请输入SMS签名" />
              </el-form-item>
            </template>

            <el-form-item>
              <el-button type="primary" @click="onSaveNotificationSettings">保存设置</el-button>
              <el-button @click="resetNotificationForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 系统日志 -->
        <el-tab-pane label="系统日志" name="logs">
          <div class="logs-header">
            <div class="logs-filter">
              <el-date-picker
                v-model="logDateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />

              <el-select v-model="logLevelFilter" placeholder="日志级别" clearable>
                <el-option label="错误" value="error" />
                <el-option label="警告" value="warning" />
                <el-option label="信息" value="info" />
                <el-option label="调试" value="debug" />
              </el-select>

              <el-button type="primary" @click="fetchLogs">查询</el-button>
              <el-button @click="exportLogs">导出日志</el-button>
            </div>
          </div>

          <el-table :data="logList" style="width: 100%" height="400" border>
            <el-table-column prop="timestamp" label="时间" width="180" />
            <el-table-column prop="level" label="级别" width="100">
              <template #default="scope">
                <el-tag
                  :type="getLogLevelType(scope.row.level)"
                  size="small"
                >
                  {{ scope.row.level }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="module" label="模块" width="150" />
            <el-table-column prop="message" label="内容" />
            <el-table-column prop="username" label="操作用户" width="120" />
          </el-table>

          <div class="pagination">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="totalLogs"
              @size-change="onPageSizeChange"
              @current-change="onCurrentPageChange"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, FormInstance } from 'element-plus';
import type { FormRules } from 'element-plus';

// 当前激活的标签页
const activeTab = ref('basic');

// 基本设置表单
const basicFormRef = ref<FormInstance>();
const basicForm = reactive({
  systemName: '数据清洗平台',
  systemDescription: '企业级数据清洗和处理平台，提供高效、可靠的数据处理服务。',
  adminEmail: 'admin@example.com',
  supportPhone: '400-123-4567'
});

// 基本设置表单验证规则
const basicRules = reactive<FormRules>({
  systemName: [
    { required: true, message: '请输入系统名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  systemDescription: [
    { required: true, message: '请输入系统描述', trigger: 'blur' }
  ],
  adminEmail: [
    { required: true, message: '请输入管理员邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  supportPhone: [
    { required: true, message: '请输入技术支持电话', trigger: 'blur' },
    { pattern: /^\d{3,4}-?\d{7,8}$/, message: '请输入正确的电话格式', trigger: 'blur' }
  ]
});

// 数据清洗设置表单
const dataCleanFormRef = ref<FormInstance>();
const dataCleanForm = reactive({
  maxParallelTasks: 5,
  taskTimeout: 30,
  dataRetentionDays: 30,
  errorThreshold: 5.0,
  enableAutoBackup: true,
  backupInterval: 24
});

// 数据清洗设置表单验证规则
const dataCleanRules = reactive<FormRules>({
  maxParallelTasks: [
    { required: true, message: '请输入最大并行任务数', trigger: 'blur' }
  ],
  taskTimeout: [
    { required: true, message: '请输入任务超时时间', trigger: 'blur' }
  ],
  dataRetentionDays: [
    { required: true, message: '请输入数据保留天数', trigger: 'blur' }
  ],
  errorThreshold: [
    { required: true, message: '请输入异常数据阈值', trigger: 'blur' }
  ],
  backupInterval: [
    { required: true, message: '请输入备份间隔', trigger: 'blur' }
  ]
});

// 通知设置表单
const notificationFormRef = ref<FormInstance>();
const notificationForm = reactive({
  enableEmailNotification: true,
  smtpServer: 'smtp.example.com',
  smtpPort: 465,
  senderEmail: 'noreply@example.com',
  smtpUsername: 'noreply@example.com',
  smtpPassword: '********',
  enableSmsNotification: false,
  smsApiKey: '',
  smsTemplateId: '',
  smsSignName: ''
});

// 通知设置表单验证规则
const notificationRules = reactive<FormRules>({
  smtpServer: [
    { required: true, message: '请输入SMTP服务器', trigger: 'blur' }
  ],
  smtpPort: [
    { required: true, message: '请输入SMTP端口', trigger: 'blur' }
  ],
  senderEmail: [
    { required: true, message: '请输入发件人邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  smtpUsername: [
    { required: true, message: '请输入SMTP用户名', trigger: 'blur' }
  ],
  smtpPassword: [
    { required: true, message: '请输入SMTP密码', trigger: 'blur' }
  ],
  smsApiKey: [
    { required: true, message: '请输入SMS API密钥', trigger: 'blur' }
  ],
  smsTemplateId: [
    { required: true, message: '请输入SMS模板ID', trigger: 'blur' }
  ],
  smsSignName: [
    { required: true, message: '请输入SMS签名', trigger: 'blur' }
  ]
});

// 日志相关
const logDateRange = ref<string[]>([]);
const logLevelFilter = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const totalLogs = ref(100);

// 日志数据
const logList = ref([
  {
    timestamp: '2025-04-27 09:30:45',
    level: 'error',
    module: '任务管理',
    message: '任务执行失败：ID-12345，原因：连接超时',
    username: '张三'
  },
  {
    timestamp: '2025-04-27 08:45:20',
    level: 'warning',
    module: '数据导入',
    message: '导入文件格式不匹配：expected.csv',
    username: '李四'
  },
  {
    timestamp: '2025-04-27 08:30:15',
    level: 'info',
    module: '系统管理',
    message: '用户登录成功',
    username: '王五'
  },
  {
    timestamp: '2025-04-27 08:15:10',
    level: 'debug',
    module: '数据清洗',
    message: '开始处理数据批次：BATCH-20250427-001',
    username: '系统'
  },
  {
    timestamp: '2025-04-26 17:45:30',
    level: 'info',
    module: '用户管理',
    message: '新建用户：李四',
    username: '张三'
  }
]);

// 初始化
onMounted(() => {
  // 在真实环境中，这里应该从API加载设置
  console.log('System Settings component mounted');

  // 设置日志日期范围为最近7天
  const endDate = new Date();
  const startDate = new Date();
  startDate.setDate(startDate.getDate() - 7);

  logDateRange.value = [
    startDate.toISOString().split('T')[0],
    endDate.toISOString().split('T')[0]
  ];
});

// 保存基本设置
const onSaveBasicSettings = () => {
  basicFormRef.value?.validate((valid) => {
    if (valid) {
      // 在真实环境中，这里应该调用API保存设置
      ElMessage.success('基本设置保存成功');
    }
  });
};

// 重置基本设置表单
const resetBasicForm = () => {
  basicFormRef.value?.resetFields();
};

// 保存数据清洗设置
const onSaveDataCleanSettings = () => {
  dataCleanFormRef.value?.validate((valid) => {
    if (valid) {
      // 在真实环境中，这里应该调用API保存设置
      ElMessage.success('数据清洗设置保存成功');
    }
  });
};

// 重置数据清洗设置表单
const resetDataCleanForm = () => {
  dataCleanFormRef.value?.resetFields();
};

// 保存通知设置
const onSaveNotificationSettings = () => {
  notificationFormRef.value?.validate((valid) => {
    if (valid) {
      // 在真实环境中，这里应该调用API保存设置
      ElMessage.success('通知设置保存成功');
    }
  });
};

// 重置通知设置表单
const resetNotificationForm = () => {
  notificationFormRef.value?.resetFields();
};

// 获取日志级别样式
const getLogLevelType = (level: string): string => {
  const levelMap: Record<string, string> = {
    'error': 'danger',
    'warning': 'warning',
    'info': 'info',
    'debug': 'success'
  };
  return levelMap[level] || 'info';
};

// 分页变化处理
const onPageSizeChange = (size: number) => {
  pageSize.value = size;
  fetchLogs();
};

const onCurrentPageChange = (page: number) => {
  currentPage.value = page;
  fetchLogs();
};

// 获取日志数据
const fetchLogs = () => {
  // 在真实环境中，这里应该调用API获取日志数据
  console.log('Fetching logs with filters:');
  console.log({
    dateRange: logDateRange.value,
    level: logLevelFilter.value,
    page: currentPage.value,
    pageSize: pageSize.value
  });
};

// 导出日志
const exportLogs = () => {
  // 在真实环境中，这里应该调用API导出日志
  ElMessage.success('日志导出功能已触发');
};
</script>

<style scoped>
.system-settings {
  width: 100%;
}

.settings-card {
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 日志相关样式 */
.logs-header {
  margin-bottom: 20px;
}

.logs-filter {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
