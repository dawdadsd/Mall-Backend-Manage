import { defineStore } from 'pinia';
import { v4 as uuidv4 } from 'uuid'; // 需要安装: npm install uuid @types/uuid
import type { ChatMessage, ChatState, ChatSession } from '../types/chat';
// 移除旧的 Grok API 服务导入
// import { grokApiService } from '../services/grokApiService';
// 导入新的后端学习助手服务
import { learningAssistantService } from '../services/learningAssistantService'; 
// 使用本地存储服务替代后端API服务 (这个保留，用于存储会话列表)
import { localStorageService } from '../services/localStorageService';

// 生成会话标题
function generateSessionTitle(messages: ChatMessage[]): string {
  const userMessage = messages.find(msg => msg.role === 'user');
  if (userMessage) {
    const title = userMessage.content.slice(0, 30);
    return title.length < userMessage.content.length ? `${title}...` : title;
  }
  return `新对话 ${new Date().toLocaleString()}`;
}

export const useChatStore = defineStore('chat', {
  state: (): ChatState => ({
    messages: [
      {
        role: 'system',
        content: '我是小巫商城的AI助手，有什么问题可以问我',
        time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
      }
    ],
    isOpen: false,
    userInput: '',
    isLoading: false,
    sessionId: uuidv4(), // 生成新的会话ID
    sessions: [],
    isSaving: false
  }),
  
  actions: {
    toggleChat() {
      this.isOpen = !this.isOpen;
    },
    
    async sendMessage(content: string) {
      if (!content.trim() || this.isLoading) return;
      
      // 添加用户消息
      const userMessage: ChatMessage = {
        role: 'user',
        content,
        time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        id: uuidv4()
      };
      
      this.messages.push(userMessage);
      this.userInput = '';
      this.isLoading = true;
      
      try {
        // 调用新的后端服务
        const backendResponse = await learningAssistantService.askQuestion(content, this.sessionId);
        
        // 从后端响应创建助手消息
        const assistantMessage: ChatMessage = {
          role: 'assistant',
          content: backendResponse.answer, // 使用后端的回答
          time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
          id: uuidv4()
        };
        
        // 更新当前会话 ID 为后端返回的 ID
        this.sessionId = backendResponse.conversationId;
        // --- 修改结束 ---

        this.messages.push(assistantMessage);
        
        // 保存聊天会话到本地存储 (这部分逻辑可能需要调整，取决于你是否还想将会话保存在前端本地)
        this.saveSession(); // 注意：现在 sessionId 可能在调用后改变，saveSession 需要能处理
      } catch (error: any) {
        console.error('发送消息错误:', error);
        this.messages.push({
          role: 'assistant',
          // 显示后端或调用时抛出的错误信息
          content: `抱歉，发生了错误：${error.message || '请稍后再试。'}`,
          time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
          id: uuidv4()
        });
      } finally {
        this.isLoading = false;
      }
    },
    
    async saveSession() {
      if (this.messages.length <= 1) return; // 只有系统消息，不保存
      
      this.isSaving = true;
      
      try {
        // 保存到本地存储
        const success = localStorageService.saveChatSession(this.sessionId, this.messages);
        
        if (success) {
          this.lastSaved = new Date().toISOString();
          // 刷新会话列表
          await this.loadAllSessions();
        }
      } catch (error) {
        console.error('保存会话失败:', error);
      } finally {
        this.isSaving = false;
      }
    },
    
    // 加载特定会话
    async loadSession(sessionId: string) {
      this.isLoading = true;
      
      try {
        const messages = localStorageService.getChatSession(sessionId);
        
        if (messages && messages.length > 0) {
          this.messages = messages;
          this.sessionId = sessionId;
        }
      } catch (error) {
        console.error('加载会话失败:', error);
      } finally {
        this.isLoading = false;
      }
    },
    
    // 加载所有会话
    async loadAllSessions() {
      try {
        const sessions = localStorageService.getAllSessions();
        this.sessions = sessions;
      } catch (error) {
        console.error('加载所有会话失败:', error);
      }
    },
    
    // 创建新会话
    createNewSession() {
      // 保存旧会话 (如果需要)
      // if (this.messages.length > 1) { // 避免保存空的初始会话
      //   this.saveSession(); 
      // }
      this.sessionId = uuidv4(); // 生成新的会话ID
      this.messages = [
        {
          role: 'system',
          content: '我是小巫商城的AI助手，有什么问题可以问我',
          time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
        }
      ];
    },
    
    // 删除会话
    async deleteSession(sessionId: string) {
      try {
        const success = localStorageService.deleteChatSession(sessionId);
        
        if (success) {
          // 从本地会话列表中移除
          this.sessions = this.sessions.filter(session => session.id !== sessionId);
          
          // 如果删除的是当前会话，创建新会话
          if (this.sessionId === sessionId) {
            this.createNewSession();
          }
        }
      } catch (error) {
        console.error('删除会话失败:', error);
      }
    },
    
    clearMessages() {
      this.messages = [
        {
          role: 'system',
          content: '我是小巫商城的AI助手，有什么问题可以问我',
          time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
        }
      ];
      // 创建新会话
      this.createNewSession();
    }
  }
}); 