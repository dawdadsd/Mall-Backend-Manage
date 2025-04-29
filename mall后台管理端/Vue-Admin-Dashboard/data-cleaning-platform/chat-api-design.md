# 聊天数据存储 API 设计文档

## 概述

本文档描述了聊天功能持久化所需的后端 API 设计。这些 API 用于存储、检索和管理聊天会话数据。

## 数据模型

### ChatSession（聊天会话）

```json
{
  "id": "string",           // 会话唯一ID（UUID）
  "messages": [             // 消息数组
    {
      "id": "string",       // 消息ID
      "role": "string",     // 消息角色 (user/assistant/system)
      "content": "string",  // 消息内容
      "time": "string"      // 消息时间
    }
  ],
  "createdAt": "string",    // 创建时间（ISO格式）
  "updatedAt": "string",    // 更新时间（ISO格式）
  "title": "string"         // 会话标题（可选）
}
```

## API 端点

### 1. 创建新的聊天会话

- **URL**: `/api/chat-sessions`
- **方法**: `POST`
- **请求体**:
  ```json
  {
    "sessionId": "string",
    "messages": [
      {
        "id": "string",
        "role": "string", 
        "content": "string",
        "time": "string"
      }
    ],
    "createdAt": "string",
    "updatedAt": "string",
    "title": "string"
  }
  ```
- **响应**:
  - 201 Created
    ```json
    {
      "id": "string",
      "messages": [...],
      "createdAt": "string",
      "updatedAt": "string",
      "title": "string"
    }
    ```
  - 400 Bad Request
  - 500 Internal Server Error

### 2. 获取特定聊天会话

- **URL**: `/api/chat-sessions/{sessionId}`
- **方法**: `GET`
- **参数**: 
  - `sessionId`: 会话ID
- **响应**:
  - 200 OK
    ```json
    {
      "id": "string",
      "messages": [...],
      "createdAt": "string",
      "updatedAt": "string",
      "title": "string"
    }
    ```
  - 404 Not Found
  - 500 Internal Server Error

### 3. 获取所有聊天会话

- **URL**: `/api/chat-sessions`
- **方法**: `GET`
- **响应**:
  - 200 OK
    ```json
    [
      {
        "id": "string",
        "messages": [...],
        "createdAt": "string",
        "updatedAt": "string",
        "title": "string"
      }
    ]
    ```
  - 500 Internal Server Error

### 4. 更新聊天会话

- **URL**: `/api/chat-sessions/{sessionId}`
- **方法**: `PUT`
- **参数**: 
  - `sessionId`: 会话ID
- **请求体**:
  ```json
  {
    "messages": [...],
    "updatedAt": "string"
  }
  ```
- **响应**:
  - 200 OK
    ```json
    {
      "id": "string",
      "messages": [...],
      "createdAt": "string",
      "updatedAt": "string",
      "title": "string"
    }
    ```
  - 400 Bad Request
  - 404 Not Found
  - 500 Internal Server Error

### 5. 删除聊天会话

- **URL**: `/api/chat-sessions/{sessionId}`
- **方法**: `DELETE`
- **参数**: 
  - `sessionId`: 会话ID
- **响应**:
  - 204 No Content
  - 404 Not Found
  - 500 Internal Server Error

## 后端数据库设计

### 1. 使用关系型数据库（如MySQL）

**chat_sessions 表**
```sql
CREATE TABLE chat_sessions (
  id VARCHAR(36) PRIMARY KEY,
  title VARCHAR(255),
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);
```

**chat_messages 表**
```sql
CREATE TABLE chat_messages (
  id VARCHAR(36) PRIMARY KEY,
  session_id VARCHAR(36) NOT NULL,
  role VARCHAR(10) NOT NULL,
  content TEXT NOT NULL,
  time VARCHAR(20),
  created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (session_id) REFERENCES chat_sessions(id) ON DELETE CASCADE
);
```

### 2. 使用 NoSQL 数据库（如MongoDB）

**chat_sessions 集合**
```json
{
  "_id": "ObjectId",
  "sessionId": "string",
  "title": "string",
  "messages": [
    {
      "id": "string",
      "role": "string",
      "content": "string",
      "time": "string"
    }
  ],
  "createdAt": "ISODate",
  "updatedAt": "ISODate"
}
```

## 安全考虑

1. **身份验证**: 所有API端点应要求用户身份验证
2. **授权**: 用户只能访问其自己的聊天会话
3. **数据加密**: 敏感数据应使用TLS/SSL加密传输
4. **输入验证**: 所有用户输入应进行验证，防止注入攻击
5. **速率限制**: 实施API速率限制，防止滥用

## 实现建议

1. 根据项目规模选择合适的数据库
2. 对于小型应用，可以考虑使用文件系统存储（JSON文件）
3. 对于大型应用，建议使用专用数据库（MySQL、MongoDB等）
4. 考虑实现消息队列以处理大量聊天请求
5. 定期备份聊天数据

## 后端实现参考（Spring Boot）

```java
@RestController
@RequestMapping("/api/chat-sessions")
public class ChatSessionController {

    @Autowired
    private ChatSessionService chatSessionService;

    @PostMapping
    public ResponseEntity<ChatSession> createChatSession(@RequestBody ChatSession chatSession) {
        ChatSession created = chatSessionService.saveChatSession(chatSession);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<ChatSession> getChatSession(@PathVariable String sessionId) {
        ChatSession session = chatSessionService.getChatSessionById(sessionId);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(session, HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<List<ChatSession>> getAllChatSessions() {
        List<ChatSession> sessions = chatSessionService.getAllChatSessions();
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<ChatSession> updateChatSession(
            @PathVariable String sessionId,
            @RequestBody ChatSessionUpdateRequest updateRequest) {
        ChatSession updated = chatSessionService.updateChatSession(sessionId, updateRequest);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteChatSession(@PathVariable String sessionId) {
        boolean deleted = chatSessionService.deleteChatSession(sessionId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
``` 