// 代码文档接口定义
export interface CodeDocument {
  id: string;            // 文档唯一ID
  title: string;         // 文档标题
  question: string;      // 用户提出的问题
  content: string;       // 生成的文档内容（Markdown格式）
  conversationId?: string; // 添加可选的对话ID
  createdAt: string;     // 创建时间（ISO格式）
  updatedAt: string;     // 更新时间（ISO格式）
}

// AI文档生成请求参数
export interface DocumentGenerationRequest {
  question: string;      // 待回答的问题
  context?: string;      // 可选的上下文信息
}

// AI文档生成响应
export interface DocumentGenerationResponse {
  content: string;       // 生成的文档内容
  status: string;        // 生成状态
} 