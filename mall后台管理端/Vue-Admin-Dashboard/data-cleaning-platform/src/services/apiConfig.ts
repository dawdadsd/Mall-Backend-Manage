/**
 * API配置文件
 * 集中管理所有API端点和基本配置
 */

// API基础URL
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/learn/ask';

// 聊天相关API
export const CHAT_API = {
  SESSIONS: `${API_BASE_URL}/learn/ask`,
  SESSION_BY_ID: (id: string) => `${API_BASE_URL}/learn/ask/${id}`
};

// 代码学习相关API
export const CODE_LEARNING_API = {
  DOCUMENTS: `${API_BASE_URL}/learn/ask`,
  DOCUMENT_BY_ID: (id: string) => `${API_BASE_URL}/learn/ask/${id}`,
  GENERATE: `${API_BASE_URL}/learn/ask`
};

// AI服务API
export const AI_API = {
  CHAT_COMPLETION: `${API_BASE_URL}/learn/ask`,
  DOCUMENT_GENERATION: `${API_BASE_URL}/learn/ask`
};

// API请求超时时间（毫秒）
export const API_TIMEOUT = 30000;

// API请求头配置
export const DEFAULT_HEADERS = {
  'Content-Type': 'application/json',
  'Accept': 'application/json'
};

// 请求重试配置
export const RETRY_CONFIG = {
  maxRetries: 3,
  retryDelay: 1000
}; 