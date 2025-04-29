import axios from 'axios';
import { v4 as uuidv4 } from 'uuid';
import type { CodeDocument, DocumentGenerationRequest } from '../types/codelearning';

// 本地存储键
const STORAGE_KEY = 'code_learning_documents';

// AI文档生成API配置
const AI_API_URL = 'http://localhost:8080/learn/ask';

/**
 * 代码学习服务
 * 负责管理代码文档和AI文档生成
 */
class CodeLearningService {
  /**
   * 获取所有文档
   */
  async getAllDocuments(): Promise<CodeDocument[]> {
    try {
      // 从本地存储获取文档
      const storedDocs = localStorage.getItem(STORAGE_KEY);
      return storedDocs ? JSON.parse(storedDocs) : [];
    } catch (error) {
      console.error('获取文档失败:', error);
      return [];
    }
  }

  /**
   * 获取单个文档
   * @param id 文档ID
   */
  async getDocument(id: string): Promise<CodeDocument | null> {
    try {
      const docs = await this.getAllDocuments();
      return docs.find(doc => doc.id === id) || null;
    } catch (error) {
      console.error(`获取文档 ${id} 失败:`, error);
      return null;
    }
  }

  /**
   * 创建新文档
   * @param document 文档数据（不需要包含ID）
   */
  async createDocument(document: Omit<CodeDocument, 'id'>): Promise<CodeDocument> {
    try {
      const newDoc: CodeDocument = {
        ...document,
        id: uuidv4()
      };

      const docs = await this.getAllDocuments();
      docs.unshift(newDoc);
      localStorage.setItem(STORAGE_KEY, JSON.stringify(docs));

      return newDoc;
    } catch (error) {
      console.error('创建文档失败:', error);
      throw new Error('创建文档失败');
    }
  }

  /**
   * 更新文档
   * @param id 文档ID
   * @param document 更新的文档数据
   */
  async updateDocument(id: string, document: Partial<CodeDocument>): Promise<CodeDocument> {
    try {
      const docs = await this.getAllDocuments();
      const index = docs.findIndex(doc => doc.id === id);

      if (index === -1) {
        throw new Error(`文档 ${id} 不存在`);
      }

      // 更新文档
      docs[index] = {
        ...docs[index],
        ...document,
        updatedAt: new Date().toISOString()
      };

      localStorage.setItem(STORAGE_KEY, JSON.stringify(docs));
      return docs[index];
    } catch (error) {
      console.error(`更新文档 ${id} 失败:`, error);
      throw error;
    }
  }

  /**
   * 删除文档
   * @param id 文档ID
   */
  async deleteDocument(id: string): Promise<boolean> {
    try {
      const docs = await this.getAllDocuments();
      const filteredDocs = docs.filter(doc => doc.id !== id);

      if (docs.length === filteredDocs.length) {
        return false; // 文档不存在
      }

      localStorage.setItem(STORAGE_KEY, JSON.stringify(filteredDocs));
      return true;
    } catch (error) {
      console.error(`删除文档 ${id} 失败:`, error);
      throw error;
    }
  }

  /**
   * 调用AI生成文档内容
   * @param question 问题
   */
  async generateDocumentation(question: string, conversationId: string | null = null): Promise<{ answer: string, conversationId: string }> {
    try {
      // 实际上应该是这样：
      const response = await axios.post<{ answer: string, conversationId: string }>(AI_API_URL, { 
        question,
        conversationId // 传递 conversationId
       });
      // 从 response.data.answer 获取内容，并返回 answer 和 conversationId
      return { 
        answer: response.data.answer, 
        conversationId: response.data.conversationId 
      };
      
      // // 模拟API调用延迟
      // await new Promise(resolve => setTimeout(resolve, 1500));
      
      // // 返回模拟的Markdown文档
      // return this.generateMockDocumentation(question);
    } catch (error) {
      console.error('生成文档失败:', error);
      // 考虑更具体的错误处理
      if (axios.isAxiosError(error)) {
        if (error.response) {
          // 服务器返回了错误状态码
          console.error('服务器响应错误:', error.response.status, error.response.data);
          throw new Error(`AI 服务错误: ${error.response.status}`);
        } else if (error.request) {
          // 请求已发出，但没有收到响应 (例如网络错误)
          console.error('无法连接到AI服务:', error.request);
          throw new Error('无法连接到AI服务，请检查网络连接和后端服务状态。');
        } else {
          // 设置请求时发生错误
          console.error('请求设置错误:', error.message);
          throw new Error('发送请求时出错。');
        }
      } else {
        // 非 Axios 错误
        console.error('未知错误:', error);
        throw new Error('发生未知错误。');
      }
    }
  }

  /**
   * 生成模拟的Markdown文档（临时方案，实际应由API返回）
   * @param question 问题
   */
  private generateMockDocumentation(question: string): string {
    const title = question.length > 50 
      ? question.substring(0, 50) + '...' 
      : question;
      
    return `# ${title}

## 问题解析

你提出的问题与编程相关，下面我将提供详细的解答和相关代码示例。

## 核心概念

\`\`\`mermaid
graph TD
    A[问题理解] --> B[概念分析]
    B --> C[代码实现]
    C --> D[优化与改进]
    B --> E[替代方案]
\`\`\`

## 代码示例

\`\`\`javascript
// 这是一个示例代码
function example() {
  console.log("这是对你问题的示例代码");
  return "示例结果";
}

// 使用方法
const result = example();
console.log(result);
\`\`\`

## 详细说明

这是对你问题"${question}"的详细解答。实际API中，这里将包含基于你的具体问题生成的内容。

### 步骤分解

1. 第一步：分析问题
2. 第二步：设计解决方案
3. 第三步：实现代码
4. 第四步：测试和优化

## 可能遇到的问题

| 问题 | 解决方案 |
| --- | --- |
| 问题1 | 解决方案1 |
| 问题2 | 解决方案2 |
| 问题3 | 解决方案3 |

## 最佳实践

> 编写代码时，务必遵循以下最佳实践...

\`\`\`mermaid
sequenceDiagram
    participant User
    participant System
    User->>System: 调用函数
    System->>System: 处理请求
    System-->>User: 返回结果
\`\`\`

## 参考资源

- [官方文档](https://example.com)
- [相关教程](https://example.com/tutorial)
- [Stack Overflow](https://stackoverflow.com)

希望这些信息能帮助你解决问题！如有更多疑问，请随时提问。
`;
  }
}

// 创建单例实例
export const codeLearningService = new CodeLearningService(); 