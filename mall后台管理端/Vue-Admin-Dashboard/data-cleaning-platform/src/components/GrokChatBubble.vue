<template>
  <div class="chat-container">
    <!-- 聊天按钮 -->
    <div 
      class="chat-bubble" 
      :class="{ 'active': chatStore.isOpen }" 
      @click="chatStore.toggleChat()"
    >
      <i class="chat-icon">
        <svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
          <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
        </svg>
      </i>
    </div>

    <!-- 聊天窗口 -->
    <div v-if="chatStore.isOpen" class="chat-window">
      <div class="chat-header">
        <div class="chat-title">
          <div class="session-selector">
            <select v-model="selectedSessionId" @change="changeSession">
              <option :value="chatStore.sessionId">当前对话</option>
              <option v-for="session in chatStore.sessions" :key="session.id" :value="session.id">
                {{ session.title || '对话 ' + new Date(session.createdAt).toLocaleDateString() }}
              </option>
              <option value="new">+ 新对话</option>
            </select>
          </div>
        </div>
        <div class="header-actions">
          <button v-if="chatStore.sessionId !== 'new'" class="action-button" @click="deleteCurrentSession" title="删除当前对话">
            <svg viewBox="0 0 24 24" width="16" height="16" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="3 6 5 6 21 6"></polyline>
              <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
            </svg>
          </button>
          <button class="close-button" @click="chatStore.toggleChat()">
            <svg viewBox="0 0 24 24" width="18" height="18" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
              <line x1="18" y1="6" x2="6" y2="18"></line>
              <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </button>
        </div>
      </div>
      
      <div class="chat-messages" ref="messagesContainer">
        <!-- 保存状态提示 -->
        <div v-if="chatStore.isSaving" class="save-status saving">正在保存...</div>
        <div v-else-if="chatStore.lastSaved" class="save-status saved">
          已保存 {{ formatSaveTime(chatStore.lastSaved) }}
        </div>
        
        <template v-if="chatStore.messages.length > 1"> <!-- 忽略系统消息 -->
          <div 
            v-for="(message, index) in visibleMessages" 
            :key="index" 
            :class="['message', message.role === 'user' ? 'user-message' : 'assistant-message']"
          >
            <div class="message-content">
              <p v-html="formatMessage(message.content)"></p>
            </div>
            <div class="message-time">{{ message.time }}</div>
          </div>
        </template>
        <div v-else class="welcome-message">
          <p>你好！我是小巫商城的AI助手，有什么我能帮你的？</p>
        </div>
      </div>
      
      <div class="chat-input-container">
        <textarea 
          v-model="userInput" 
          @keyup.enter.ctrl="sendMessage" 
          placeholder="输入消息，按 Ctrl+Enter 发送" 
          :disabled="chatStore.isLoading"
        ></textarea>
        <button @click="sendMessage" :disabled="chatStore.isLoading || !userInput.trim()">
          <span v-if="!chatStore.isLoading">发送</span>
          <span v-else class="loading-spinner"></span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, watch, nextTick, onMounted } from 'vue';
import { useChatStore } from '../stores/chatStore';

export default defineComponent({
  name: 'GrokChatBubble',
  
  setup() {
    const chatStore = useChatStore();
    const userInput = ref('');
    const messagesContainer = ref<HTMLElement | null>(null);
    const selectedSessionId = ref(chatStore.sessionId);
    
    // 加载所有会话
    onMounted(async () => {
      await chatStore.loadAllSessions();
    });
    
    // 只显示非系统消息
    const visibleMessages = computed(() => {
      return chatStore.messages.filter((msg) => msg.role !== 'system');
    });

    // 格式化消息，处理换行符等
    function formatMessage(text: string): string {
      return text
        .replace(/\n/g, '<br>')
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/\*(.*?)\*/g, '<em>$1</em>');
    }
    
    // 格式化保存时间
    function formatSaveTime(isoTime: string): string {
      const date = new Date(isoTime);
      return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    }

    // 发送消息
    async function sendMessage() {
      if (!userInput.value.trim() || chatStore.isLoading) return;
      
      await chatStore.sendMessage(userInput.value);
      userInput.value = '';
      
      // 更新选择的会话ID
      selectedSessionId.value = chatStore.sessionId;
      
      // 滚动到底部
      nextTick(() => {
        scrollToBottom();
      });
    }
    
    // 切换会话
    async function changeSession() {
      if (selectedSessionId.value === 'new') {
        chatStore.createNewSession();
        selectedSessionId.value = chatStore.sessionId;
      } else {
        await chatStore.loadSession(selectedSessionId.value);
      }
    }
    
    // 删除当前会话
    async function deleteCurrentSession() {
      if (confirm('确定要删除当前对话吗？此操作无法撤销。')) {
        const currentId = chatStore.sessionId;
        await chatStore.deleteSession(currentId);
        selectedSessionId.value = chatStore.sessionId;
      }
    }
    
    // 滚动到底部
    function scrollToBottom() {
      if (messagesContainer.value) {
        messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
      }
    }

    // 监听消息变化，滚动到底部
    watch(() => chatStore.messages.length, () => {
      nextTick(() => {
        scrollToBottom();
      });
    });
    
    // 监听聊天窗口打开状态
    watch(() => chatStore.isOpen, (isOpen) => {
      if (isOpen) {
        nextTick(() => {
          scrollToBottom();
        });
      }
    });
    
    // 监听会话ID变化
    watch(() => chatStore.sessionId, (newId) => {
      selectedSessionId.value = newId;
    });

    return {
      chatStore,
      userInput,
      visibleMessages,
      messagesContainer,
      selectedSessionId,
      sendMessage,
      formatMessage,
      formatSaveTime,
      changeSession,
      deleteCurrentSession
    };
  }
});
</script>

<style scoped>
.chat-container {
  position: fixed;
  right: 20px;
  bottom: 20px;
  z-index: 1000;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
}

.chat-bubble {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: #4a6cf7;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.chat-bubble:hover {
  transform: scale(1.05);
  background-color: #3a5bd9;
}

.chat-bubble.active {
  transform: scale(0.9);
}

.chat-icon {
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chat-window {
  position: absolute;
  bottom: 80px;
  right: 0;
  width: 350px;
  height: 500px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chat-header {
  padding: 15px;
  background-color: #4a6cf7;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-title {
  font-weight: 600;
  font-size: 16px;
  flex: 1;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.action-button, .close-button {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.session-selector select {
  background-color: rgba(255, 255, 255, 0.2);
  color: white;
  border: none;
  border-radius: 4px;
  padding: 4px 8px;
  font-size: 14px;
  outline: none;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.session-selector select option {
  background-color: white;
  color: #333;
}

.chat-messages {
  flex: 1;
  padding: 15px;
  overflow-y: auto;
  background-color: #f8f9fa;
}

.save-status {
  text-align: center;
  font-size: 12px;
  padding: 4px;
  margin-bottom: 10px;
  border-radius: 4px;
}

.save-status.saving {
  background-color: #fff9c4;
  color: #856404;
}

.save-status.saved {
  background-color: #d4edda;
  color: #155724;
}

.message {
  max-width: 80%;
  margin-bottom: 15px;
  clear: both;
  overflow-wrap: break-word;
}

.assistant-message {
  float: left;
  background-color: #fff;
  border-radius: 18px 18px 18px 0;
  padding: 10px 15px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.user-message {
  float: right;
  background-color: #4a6cf7;
  color: white;
  border-radius: 18px 18px 0 18px;
  padding: 10px 15px;
}

.message-content p {
  margin: 0;
}

.message-time {
  font-size: 10px;
  margin-top: 5px;
  opacity: 0.7;
  clear: both;
}

.welcome-message {
  text-align: center;
  padding: 20px;
  color: #666;
}

.chat-input-container {
  padding: 10px;
  background-color: #fff;
  border-top: 1px solid #eaeaea;
  display: flex;
  align-items: center;
}

.chat-input-container textarea {
  flex: 1;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-family: inherit;
  resize: none;
  min-height: 60px;
  max-height: 120px;
  overflow-y: auto;
}

.chat-input-container button {
  margin-left: 10px;
  padding: 10px 15px;
  background-color: #4a6cf7;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
  min-width: 60px;
}

.chat-input-container button:hover {
  background-color: #3a5bd9;
}

.chat-input-container button:disabled {
  background-color: #b0b0b0;
  cursor: not-allowed;
}

.loading-spinner {
  display: inline-block;
  width: 12px;
  height: 12px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: #fff;
  animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style> 