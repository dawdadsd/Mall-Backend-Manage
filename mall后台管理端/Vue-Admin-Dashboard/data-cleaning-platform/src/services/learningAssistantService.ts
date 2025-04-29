import axios from 'axios';
import type { ChatMessage } from '../types/chat';

// 后端学习助手 API 的基础 URL (假设与前端在同一域或已配置代理)
const API_BASE_URL = 'http://localhost:8080'; // 或者你的 Spring Boot 应用的完整 URL，例如 http://localhost:8080

// 定义后端响应的接口
interface LearningResponse {
  answer: string;
  conversationId: string;
  error?: string; // 可选的错误信息
}

/**
 * 调用后端代码学习助手服务的类
 */
class LearningAssistantService {

  /**
   * 向后端学习助手发送问题。
   * @param question 用户的问题。
   * @param conversationId 当前的会话 ID (可以为 null 或空字符串，后端会生成新的)。
   * @returns 包含助手回答和会话 ID 的对象。
   * @throws 如果 API 调用失败或返回错误，则抛出错误。
   */
  async askQuestion(question: string, conversationId: string | null | undefined): Promise<LearningResponse> {
    try {
      const response = await axios.post<LearningResponse>(`${API_BASE_URL}/learn/ask`, {
        question: question,
        conversationId: conversationId || null // 如果是空字符串或 undefined，发送 null
      });

      if (response.status === 200 && response.data && !response.data.error) {
        return response.data;
      } else {
        // 处理后端返回的错误信息
        const errorMsg = response.data?.error || `请求失败，状态码: ${response.status}`;
        console.error('后端学习助手 API 返回错误:', errorMsg);
        throw new Error(errorMsg);
      }
    } catch (error) {
      console.error('调用后端学习助手 API 时出错:', error);
      // 统一抛出错误，让调用方处理
      if (axios.isAxiosError(error) && error.response?.data?.error) {
          throw new Error(error.response.data.error);
      } else if (error instanceof Error) {
          throw error;
      } else {
          throw new Error('连接后端学习助手时发生未知错误');
      }
    }
  }
}

// 创建单例实例
export const learningAssistantService = new LearningAssistantService(); 