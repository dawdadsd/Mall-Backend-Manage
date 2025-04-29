export interface ChatMessage {
  role: 'user' | 'assistant' | 'system';
  content: string;
  time?: string;
  id?: string; // 可选的消息ID，用于数据库识别
}

export interface ChatSession {
  id: string;
  messages: ChatMessage[];
  createdAt: string;
  updatedAt: string;
  title?: string; // 可选的会话标题，例如基于第一条消息
}

export interface ChatState {
  messages: ChatMessage[];
  isOpen: boolean;
  userInput: string;
  isLoading: boolean;
  sessionId: string; // 当前会话ID
  sessions: ChatSession[]; // 所有会话列表
  isSaving: boolean; // 是否正在保存
  lastSaved?: string; // 最后保存时间
} 