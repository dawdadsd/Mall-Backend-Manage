# 代码学习API设计文档

## 概述

本文档描述了代码学习功能的后端API设计。这些API用于管理代码学习文档和生成AI文档。

## 数据模型

### CodeDocument（代码文档）

```json
{
  "id": "string",            // 文档唯一ID（UUID）
  "title": "string",         // 文档标题
  "question": "string",      // 用户提出的问题
  "content": "string",       // 生成的文档内容（Markdown格式）
  "createdAt": "string",     // 创建时间（ISO格式）
  "updatedAt": "string"      // 更新时间（ISO格式）
}
```

## API端点

### 1. 获取所有代码文档

- **URL**: `/api/code-learning/documents`
- **方法**: `GET`
- **响应**:
  - 200 OK
    ```json
    [
      {
        "id": "string",
        "title": "string",
        "question": "string",
        "content": "string",
        "createdAt": "string",
        "updatedAt": "string"
      }
    ]
    ```
  - 500 Internal Server Error

### 2. 获取特定代码文档

- **URL**: `/api/code-learning/documents/{documentId}`
- **方法**: `GET`
- **参数**: 
  - `documentId`: 文档ID
- **响应**:
  - 200 OK
    ```json
    {
      "id": "string",
      "title": "string",
      "question": "string",
      "content": "string",
      "createdAt": "string",
      "updatedAt": "string"
    }
    ```
  - 404 Not Found
  - 500 Internal Server Error

### 3. 创建代码文档

- **URL**: `/api/code-learning/documents`
- **方法**: `POST`
- **请求体**:
  ```json
  {
    "title": "string",
    "question": "string",
    "content": "string"
  }
  ```
- **响应**:
  - 201 Created
    ```json
    {
      "id": "string",
      "title": "string",
      "question": "string",
      "content": "string",
      "createdAt": "string",
      "updatedAt": "string"
    }
    ```
  - 400 Bad Request
  - 500 Internal Server Error

### 4. 更新代码文档

- **URL**: `/api/code-learning/documents/{documentId}`
- **方法**: `PUT`
- **参数**: 
  - `documentId`: 文档ID
- **请求体**:
  ```json
  {
    "title": "string",
    "question": "string",
    "content": "string"
  }
  ```
- **响应**:
  - 200 OK
    ```json
    {
      "id": "string",
      "title": "string",
      "question": "string",
      "content": "string",
      "createdAt": "string",
      "updatedAt": "string"
    }
    ```
  - 400 Bad Request
  - 404 Not Found
  - 500 Internal Server Error

### 5. 删除代码文档

- **URL**: `/api/code-learning/documents/{documentId}`
- **方法**: `DELETE`
- **参数**: 
  - `documentId`: 文档ID
- **响应**:
  - 204 No Content
  - 404 Not Found
  - 500 Internal Server Error

### 6. 生成文档内容

- **URL**: `/api/code-learning/generate`
- **方法**: `POST`
- **请求体**:
  ```json
  {
    "question": "string",
    "context": "string"  // 可选的上下文信息
  }
  ```
- **响应**:
  - 200 OK
    ```json
    {
      "content": "string",
      "status": "string"
    }
    ```
  - 400 Bad Request
  - 500 Internal Server Error

## 后端数据库设计

**code_documents 表**
```sql
CREATE TABLE code_documents (
  id VARCHAR(36) PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  question TEXT NOT NULL,
  content TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 安全考虑

1. **身份验证**: 所有API端点应要求用户身份验证
2. **授权**: 用户只能访问自己创建的文档
3. **输入验证**: 所有用户输入应进行验证，特别是Markdown内容
4. **跨站脚本防护**: 确保渲染Markdown内容时防止XSS攻击

## AI文档生成实现

### 技术方案

1. **模型选择**: 使用OpenAI GPT-4或同等能力的大型语言模型
2. **提示工程**: 设计特定的提示模板，确保生成高质量的Markdown文档
3. **Mermaid支持**: 在提示中明确要求生成Mermaid图表
4. **代码示例**: 确保生成的内容包含合适的代码示例

### 提示模板示例

```
你是一位专业的编程导师，请对以下编程问题提供详细解答，并使用Markdown格式。
解答应包含:
1. 问题分析
2. 核心概念解释
3. 至少一个Mermaid图表来可视化概念或流程
4. 代码示例（带有注释）
5. 步骤分解
6. 可能遇到的问题及解决方案
7. 最佳实践建议
8. 参考资源链接

问题: {question}
```

### 图表类型

文档应支持以下Mermaid图表类型:

1. 流程图 (Flowchart)
2. 序列图 (Sequence Diagram)
3. 类图 (Class Diagram)
4. 状态图 (State Diagram)
5. 甘特图 (Gantt Chart)

## 后端实现参考（Spring Boot）

```java
@RestController
@RequestMapping("/api/code-learning")
public class CodeLearningController {

    @Autowired
    private CodeLearningService codeLearningService;

    @GetMapping("/documents")
    public ResponseEntity<List<CodeDocument>> getAllDocuments() {
        List<CodeDocument> documents = codeLearningService.getAllDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<CodeDocument> getDocument(@PathVariable String id) {
        CodeDocument document = codeLearningService.getDocumentById(id);
        if (document == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @PostMapping("/documents")
    public ResponseEntity<CodeDocument> createDocument(@RequestBody CodeDocument document) {
        CodeDocument created = codeLearningService.createDocument(document);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/documents/{id}")
    public ResponseEntity<CodeDocument> updateDocument(
            @PathVariable String id,
            @RequestBody CodeDocument document) {
        CodeDocument updated = codeLearningService.updateDocument(id, document);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        boolean deleted = codeLearningService.deleteDocument(id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateDocumentation(
            @RequestBody Map<String, String> request) {
        String question = request.get("question");
        String context = request.get("context");
        
        if (question == null || question.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        String content = codeLearningService.generateDocumentation(question, context);
        
        Map<String, String> response = new HashMap<>();
        response.put("content", content);
        response.put("status", "success");
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
``` 