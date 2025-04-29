import axios from 'axios';
import type { ChatMessage, ChatSession } from '../types/chat';

// 聊天消息存储服务
export default class ChatStorageService {
  private apiBaseURL = '/learn/ask'; // 根据你的实际后端API路径进行调整

  // 保存聊天会话
  async saveChatSession(sessionId: string, messages: ChatMessage[]): Promise<boolean> {
    try {
      const response = await axios.post(`${this.apiBaseURL}/chat-sessions`, {
        sessionId,
        messages,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      });
      return response.status === 200 || response.status === 201;
    } catch (error) {
      console.error('保存聊天会话失败:', error);
      return false;
    }
  }

  // 更新已有聊天会话
  async updateChatSession(sessionId: string, messages: ChatMessage[]): Promise<boolean> {
    try {
      const response = await axios.put(`${this.apiBaseURL}/chat-sessions/${sessionId}`, {
        messages,
        updatedAt: new Date().toISOString()
      });
      return response.status === 200;
    } catch (error) {
      console.error('更新聊天会话失败:', error);
      return false;
    }
  }

  // 获取特定聊天会话
  async getChatSession(sessionId: string): Promise<ChatMessage[] | null> {
    try {
      const response = await axios.get(`${this.apiBaseURL}/chat-sessions/${sessionId}`);
      if (response.status === 200 && response.data) {
        return response.data.messages;
      }
      return null;
    } catch (error) {
      console.error('获取聊天会话失败:', error);
      return null;
    }
  }

  // 获取所有聊天会话
  async getAllChatSessions(): Promise<ChatSession[]> {
    try {
      const response = await axios.get(`${this.apiBaseURL}/chat-sessions`);
      return response.data || [];
    } catch (error) {
      console.error('获取所有聊天会话失败:', error);
      return [];
    }
  }

  // 删除聊天会话
  async deleteChatSession(sessionId: string): Promise<boolean> {
    try {
      const response = await axios.delete(`${this.apiBaseURL}/chat-sessions/${sessionId}`);
      return response.status === 200 || response.status === 204;
    } catch (error) {
      console.error('删除聊天会话失败:', error);
      return false;
    }
  }
}

// 创建单例实例
export const chatStorageService = new ChatStorageService(); 