<template>
  <div class="code-learning-container">
    <div class="page-header">
      <h1>代码学习助手</h1>
      <p>提出你的编程问题，AI将为你提供解答和可视化文档</p>
    </div>

    <div class="content-layout">
      <!-- 左侧：问题列表 -->
      <div class="sidebar">
        <div class="sidebar-header">
          <h3>我的问题列表</h3>
          <el-button type="primary" size="small" @click="createNewDocument">
            <i class="el-icon-plus"></i> 新问题
          </el-button>
        </div>
        <div class="document-list">
          <el-empty v-if="documents.length === 0" description="暂无文档"></el-empty>
          <div
            v-for="doc in documents"
            :key="doc.id"
            :class="['document-item', { active: currentDocumentId === doc.id }]"
            @click="selectDocument(doc.id)"
          >
            <div class="document-title">{{ doc.title }}</div>
            <div class="document-date">{{ formatDate(doc.createdAt) }}</div>
            <div class="document-actions">
              <el-button
                type="text"
                size="small"
                @click.stop="deleteDocument(doc.id)"
                icon="Delete"
              ></el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：编辑和预览区域 -->
      <div class="main-content">
        <template v-if="currentDocument">
          <!-- 问题输入区域 -->
          <div class="question-section">
            <el-input
              v-model="currentDocument.title"
              placeholder="输入问题标题"
              @blur="saveCurrentDocument"
            >
              <template #prepend>问题标题</template>
            </el-input>
            <el-input
              v-model="currentDocument.question"
              type="textarea"
              :rows="4"
              placeholder="详细描述你的问题..."
              @blur="saveCurrentDocument"
            ></el-input>
            <div class="question-actions">
              <el-button
                type="primary"
                :disabled="isProcessing || !currentDocument.question"
                @click="askQuestion"
                :loading="isProcessing"
              >
                {{ isProcessing ? '生成中...' : '向AI提问' }}
              </el-button>
              <el-tooltip content="支持Markdown和Mermaid语法" placement="top">
                <el-button type="info" plain icon="QuestionFilled"></el-button>
              </el-tooltip>
            </div>
          </div>

          <!-- 文档预览区域 -->
          <div class="document-preview">
            <div class="preview-header">
              <h3>文档预览</h3>
              <div class="preview-actions">
                <el-tooltip content="导出为Markdown" placement="top">
                  <el-button
                    size="small"
                    @click="exportMarkdown"
                    icon="Download"
                  ></el-button>
                </el-tooltip>
                <el-tooltip content="复制内容" placement="top">
                  <el-button
                    size="small"
                    @click="copyContent"
                    icon="DocumentCopy"
                  ></el-button>
                </el-tooltip>
              </div>
            </div>
            <div class="preview-content">
              <markdown-viewer 
                v-if="currentDocument.content" 
                :content="currentDocument.content"
              />
              <el-empty v-else description="问题提交后，这里将显示AI生成的文档"></el-empty>
            </div>
          </div>
        </template>
        <el-empty
          v-else
          description="选择或创建一个问题开始学习"
        ></el-empty>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted, watch, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { codeLearningService } from '../services/codeLearningService';
import type { CodeDocument } from '../types/codelearning';
import MarkdownViewer from '../components/MarkdownViewer.vue';

export default defineComponent({
  name: 'CodeLearningPage',
  components: {
    MarkdownViewer
  },
  setup() {
    const documents = ref<CodeDocument[]>([]);
    const currentDocumentId = ref<string | null>(null);
    const currentDocument = ref<CodeDocument | null>(null);
    const isProcessing = ref(false);

    // 加载所有文档
    const loadDocuments = async () => {
      try {
        const docs = await codeLearningService.getAllDocuments();
        documents.value = docs;
        
        // 如果有文档但没有选中文档，则选择第一个
        if (docs.length > 0 && !currentDocumentId.value) {
          selectDocument(docs[0].id);
        }
      } catch (error) {
        console.error('加载文档失败:', error);
        ElMessage.error('加载文档列表失败');
      }
    };

    // 选择文档
    const selectDocument = async (id: string) => {
      try {
        const doc = await codeLearningService.getDocument(id);
        if (doc) {
          currentDocumentId.value = id;
          currentDocument.value = doc;
        }
      } catch (error) {
        console.error('加载文档详情失败:', error);
        ElMessage.error('无法加载所选文档');
      }
    };

    // 创建新文档
    const createNewDocument = async () => {
      try {
        const newDoc = await codeLearningService.createDocument({
          title: '新的编程问题',
          question: '',
          content: '',
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        });
        
        documents.value.unshift(newDoc);
        currentDocumentId.value = newDoc.id;
        currentDocument.value = newDoc;
      } catch (error) {
        console.error('创建文档失败:', error);
        ElMessage.error('创建新问题文档失败');
      }
    };

    // 保存当前文档
    const saveCurrentDocument = async () => {
      if (!currentDocument.value) return;
      
      try {
        currentDocument.value.updatedAt = new Date().toISOString();
        await codeLearningService.updateDocument(
          currentDocument.value.id,
          currentDocument.value
        );
        
        // 更新文档列表中的标题
        const index = documents.value.findIndex(doc => doc.id === currentDocument.value?.id);
        if (index !== -1) {
          documents.value[index].title = currentDocument.value.title;
        }
      } catch (error) {
        console.error('保存文档失败:', error);
        ElMessage.error('保存文档失败');
      }
    };

    // 删除文档
    const deleteDocument = async (id: string) => {
      try {
        await ElMessageBox.confirm('确定要删除这个问题文档吗？此操作不可撤销。', '确认删除', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
        
        await codeLearningService.deleteDocument(id);
        documents.value = documents.value.filter(doc => doc.id !== id);
        
        if (currentDocumentId.value === id) {
          currentDocumentId.value = documents.value.length > 0 ? documents.value[0].id : null;
          currentDocument.value = documents.value.length > 0 
            ? await codeLearningService.getDocument(documents.value[0].id) 
            : null;
        }
        
        ElMessage.success('文档已删除');
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除文档失败:', error);
          ElMessage.error('删除文档失败');
        }
      }
    };

    // 向AI提问
    const askQuestion = async () => {
      if (!currentDocument.value || !currentDocument.value.question) return;
      
      isProcessing.value = true;
      
      try {
        // 传递当前文档的 question 和 conversationId (如果存在)
        const { answer, conversationId: returnedConversationId } = await codeLearningService.generateDocumentation(
          currentDocument.value.question,
          currentDocument.value.conversationId || null // 如果没有，传递null
        );
        
        // 更新文档内容和 conversationId
        if (currentDocument.value) {
          currentDocument.value.content = answer; // 使用返回的 answer
          currentDocument.value.conversationId = returnedConversationId; // 存储返回的 conversationId
          currentDocument.value.updatedAt = new Date().toISOString();
          await saveCurrentDocument(); // 保存更新后的文档
        }
        
        ElMessage.success('文档生成成功');
      } catch (error) {
        console.error('生成文档失败:', error);
        // 使用服务层抛出的更具体的错误消息
        ElMessage.error((error as Error).message || 'AI生成文档失败，请检查服务或网络连接');
      } finally {
        isProcessing.value = false;
      }
    };

    // 导出Markdown
    const exportMarkdown = () => {
      if (!currentDocument.value?.content) return;
      
      const blob = new Blob([currentDocument.value.content], { type: 'text/markdown' });
      const url = URL.createObjectURL(blob);
      
      const a = document.createElement('a');
      a.href = url;
      a.download = `${currentDocument.value.title || 'document'}.md`;
      document.body.appendChild(a);
      a.click();
      
      // 清理
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    };

    // 复制内容到剪贴板
    const copyContent = () => {
      if (!currentDocument.value?.content) return;
      
      navigator.clipboard.writeText(currentDocument.value.content)
        .then(() => ElMessage.success('内容已复制到剪贴板'))
        .catch(err => {
          console.error('复制失败:', err);
          ElMessage.error('复制失败');
        });
    };

    // 格式化日期
    const formatDate = (dateString: string) => {
      const date = new Date(dateString);
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      });
    };

    // 组件挂载时加载文档
    onMounted(() => {
      loadDocuments();
    });

    // 监听当前文档ID变化
    watch(currentDocumentId, (newId) => {
      if (newId && documents.value.length > 0) {
        const doc = documents.value.find(d => d.id === newId);
        if (doc) {
          currentDocument.value = doc;
        }
      }
    });

    return {
      documents,
      currentDocumentId,
      currentDocument,
      isProcessing,
      selectDocument,
      createNewDocument,
      saveCurrentDocument,
      deleteDocument,
      askQuestion,
      exportMarkdown,
      copyContent,
      formatDate
    };
  }
});
</script>

<style scoped>
.code-learning-container {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  font-size: 24px;
  margin-bottom: 8px;
}

.page-header p {
  color: #666;
}

.content-layout {
  display: flex;
  height: calc(100vh - 180px);
  gap: 20px;
  overflow: hidden;
}

.sidebar {
  width: 280px;
  border-right: 1px solid #e6e6e6;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 0 0 15px 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
}

.sidebar-header h3 {
  margin: 0;
  font-size: 16px;
}

.document-list {
  flex: 1;
  overflow-y: auto;
  padding-top: 10px;
}

.document-item {
  padding: 12px;
  border-radius: 4px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
}

.document-item:hover {
  background-color: #f5f7fa;
}

.document-item.active {
  background-color: #ecf5ff;
}

.document-title {
  font-weight: 500;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.document-date {
  font-size: 12px;
  color: #909399;
}

.document-actions {
  position: absolute;
  right: 8px;
  top: 8px;
  display: none;
}

.document-item:hover .document-actions {
  display: block;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.question-section {
  margin-bottom: 20px;
}

.question-section :deep(.el-input__wrapper),
.question-section :deep(.el-textarea__inner) {
  box-shadow: none;
  border: 1px solid #dcdfe6;
}

.question-section :deep(.el-input) {
  margin-bottom: 10px;
}

.question-actions {
  margin-top: 10px;
  display: flex;
  gap: 10px;
}

.document-preview {
  flex: 1;
  display: flex;
  flex-direction: column;
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  overflow: hidden;
}

.preview-header {
  padding: 10px 15px;
  border-bottom: 1px solid #e6e6e6;
  background-color: #f5f7fa;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.preview-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
}

.preview-actions {
  display: flex;
  gap: 10px;
}

.preview-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #fff;
}
</style> 