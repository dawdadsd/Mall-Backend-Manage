# AI 代码学习助手 CORS 修复与 API 调整

## 问题描述

前端 Vue 应用 (`http://localhost:5173`) 调用后端 Spring Boot AI 服务 (`http://localhost:8080/learn/ask`) 时，出现 CORS (Cross-Origin Resource Sharing) 错误。此外，前后端之间传递的数据格式不完全匹配。

## 解决方案

### 1. 后端 Spring Boot (`SpringAi`) 修改

- **目标文件:** `back/SpringAi/src/main/java/com/mall/springai/Demo/controller/LearningController.java`
- **修改内容:**
    - 在 `LearningController` 类上添加 `@CrossOrigin(origins = "http://localhost:5173")` 注解。
    - 这允许来自前端源 `http://localhost:5173` 的跨域请求访问该 Controller 下的所有端点。

```java
import org.springframework.web.bind.annotation.CrossOrigin;
// ... 其他 import

@RestController
@CrossOrigin(origins = "http://localhost:5173") // 新增注解
@RequestMapping("/learn")
public class LearningController {
    // ... Controller 内容 ...
}
```

### 2. 前端 Vue (`data-cleaning-platform`) 修改

#### a. 类型定义更新

- **目标文件:** `data-cleaning-platform/src/types/codelearning.ts`
- **修改内容:**
    - 在 `CodeDocument` 接口中添加可选的 `conversationId` 字段，用于存储 AI 对话的会话 ID。

```typescript
export interface CodeDocument {
  id: string;
  title: string;
  question: string;
  content: string;
  conversationId?: string; // 新增字段
  createdAt: string;
  updatedAt: string;
}
```

#### b. 服务层更新 (`codeLearningService.ts`)

- **目标文件:** `data-cleaning-platform/src/services/codeLearningService.ts`
- **修改内容:**
    - 修改 `generateDocumentation` 函数：
        - 接受可选的 `conversationId` 参数，并将其包含在发送给后端的 `POST` 请求体中（后端需要 `question` 和 `conversationId`）。
        - 修改 `axios.post` 的期望响应类型为 `{ answer: string, conversationId: string }`，以匹配后端实际返回的数据结构。
        - 从 `response.data.answer` 获取 AI 回答，而不是之前的 `response.data.content`。
        - 返回一个包含 `answer` 和 `conversationId` 的对象。
        - 改进了错误处理逻辑，提供更具体的错误信息。

```typescript
// ... import axios ...

const AI_API_URL = 'http://localhost:8080/learn/ask';

class CodeLearningService {
  // ... 其他方法 ...

  async generateDocumentation(question: string, conversationId: string | null = null): Promise<{ answer: string, conversationId: string }> {
    try {
      const response = await axios.post<{ answer: string, conversationId: string }>(AI_API_URL, { 
        question,
        conversationId // 传递 conversationId
       });
      return { 
        answer: response.data.answer, 
        conversationId: response.data.conversationId 
      };
    } catch (error) {
      console.error('生成文档失败:', error);
      // ... 改进的错误处理 ...
      if (axios.isAxiosError(error)) {
        // ... 更详细的错误处理分支 ...
         throw new Error('...'); // 抛出更具体的错误
      } else {
         throw new Error('发生未知错误。');
      }
    }
  }

  // ... 其他方法 ...
}

export const codeLearningService = new CodeLearningService();
```

#### c. 视图层更新 (`CodeLearningPage.vue`)

- **目标文件:** `data-cleaning-platform/src/views/CodeLearningPage.vue`
- **修改内容:**
    - 修改 `askQuestion` 函数：
        - 在调用 `codeLearningService.generateDocumentation` 时，传递当前文档的 `question` 和 `conversationId`（如果存在）。
        - 解构返回的对象，获取 `answer` 和 `returnedConversationId`。
        - 使用 `answer` 更新 `currentDocument.value.content`。
        - 使用 `returnedConversationId` 更新 `currentDocument.value.conversationId`，以便支持连续对话。
        - 在 `catch` 块中，使用从服务层抛出的更具体的错误消息来更新 `ElMessage.error`。

```vue
<script lang="ts">
// ... setup() ...
    const askQuestion = async () => {
      // ... 省略检查代码 ...
      isProcessing.value = true;
      try {
        const { answer, conversationId: returnedConversationId } = await codeLearningService.generateDocumentation(
          currentDocument.value.question,
          currentDocument.value.conversationId || null
        );
        
        if (currentDocument.value) {
          currentDocument.value.content = answer;
          currentDocument.value.conversationId = returnedConversationId;
          currentDocument.value.updatedAt = new Date().toISOString();
          await saveCurrentDocument();
        }
        ElMessage.success('文档生成成功');
      } catch (error) {
        console.error('生成文档失败:', error);
        ElMessage.error((error as Error).message || 'AI生成文档失败...');
      } finally {
        isProcessing.value = false;
      }
    };
// ...
</script>
```

## 后续步骤

1.  重新启动后端 Spring Boot (`SpringAi`) 应用以应用 CORS 配置。
2.  重新运行前端 Vue (`data-cleaning-platform`) 应用。
3.  测试代码学习助手功能，确认 CORS 错误已解决，并且 AI 文档能够正确生成和显示。
4.  测试连续对话功能（如果前端 UI 支持多次提问），确认 `conversationId` 是否正确传递和使用。 