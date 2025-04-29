import type { ChatMessage, ChatSession } from '../types/chat';

// 本地存储服务 - 在后端API实现前的临时解决方案
export default class LocalStorageService {
  private readonly SESSIONS_KEY = 'chat_sessions';
  
  // 保存会话到LocalStorage
  saveChatSession(sessionId: string, messages: ChatMessage[]): boolean {
    try {
      // 获取所有会话
      const sessions = this.getAllSessionsFromStorage();
      
      // 检查会话是否已存在
      const existingSessionIndex = sessions.findIndex(s => s.id === sessionId);
      const now = new Date().toISOString();
      
      if (existingSessionIndex >= 0) {
        // 更新现有会话
        sessions[existingSessionIndex].messages = messages;
        sessions[existingSessionIndex].updatedAt = now;
      } else {
        // 创建新会话
        const title = this.generateSessionTitle(messages);
        const newSession: ChatSession = {
          id: sessionId,
          messages,
          createdAt: now,
          updatedAt: now,
          title
        };
        sessions.push(newSession);
      }
      
      // 保存回本地存储
      localStorage.setItem(this.SESSIONS_KEY, JSON.stringify(sessions));
      return true;
    } catch (error) {
      console.error('保存到本地存储失败:', error);
      return false;
    }
  }
  
  // 从LocalStorage获取特定会话
  getChatSession(sessionId: string): ChatMessage[] | null {
    try {
      const sessions = this.getAllSessionsFromStorage();
      const session = sessions.find(s => s.id === sessionId);
      return session ? session.messages : null;
    } catch (error) {
      console.error('从本地存储获取会话失败:', error);
      return null;
    }
  }
  
  // 从LocalStorage获取所有会话
  getAllSessions(): ChatSession[] {
    return this.getAllSessionsFromStorage();
  }
  
  // 删除LocalStorage中的会话
  deleteChatSession(sessionId: string): boolean {
    try {
      const sessions = this.getAllSessionsFromStorage();
      const filteredSessions = sessions.filter(s => s.id !== sessionId);
      
      if (sessions.length === filteredSessions.length) {
        return false; // 没有找到要删除的会话
      }
      
      localStorage.setItem(this.SESSIONS_KEY, JSON.stringify(filteredSessions));
      return true;
    } catch (error) {
      console.error('从本地存储删除会话失败:', error);
      return false;
    }
  }
  
  // 从LocalStorage获取所有会话
  private getAllSessionsFromStorage(): ChatSession[] {
    const sessionsJson = localStorage.getItem(this.SESSIONS_KEY);
    return sessionsJson ? JSON.parse(sessionsJson) : [];
  }
  
  // 生成会话标题
  private generateSessionTitle(messages: ChatMessage[]): string {
    const userMessage = messages.find(msg => msg.role === 'user');
    if (userMessage) {
      const title = userMessage.content.slice(0, 30);
      return title.length < userMessage.content.length ? `${title}...` : title;
    }
    return `新对话 ${new Date().toLocaleString()}`;
  }
}

// 创建单例实例
export const localStorageService = new LocalStorageService(); 