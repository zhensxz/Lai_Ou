## Lai_Ou Backend API 文档

基础信息
- 基础地址: http://localhost:8080
- 所有业务接口前缀: /api
- 认证方式: JWT Bearer Token（登录获取token，后续放到请求头 Authorization: Bearer <token>）
- 跨域: 允许所有来源（开发环境）

认证与会话
- 登录: POST /api/auth/login
  - Body(JSON): {"username":"string","password":"string"}
  - Response(JSON): {"token":"string","user": {"id": number, "username": string, "role": "BOSS|EMPLOYEE|AUDITOR"}}
  - 备注: 拿到 token 后，调用其他 /api/** 接口时在请求头添加 Authorization: Bearer <token>
- 校验当前登录: GET /api/auth/me
  - Header: Authorization: Bearer <token>
  - Response(JSON): {"username": string, "uid": number, "role": string}

RBAC 概要
- BOSS: 拥有全部权限
- AUDITOR: 可查看全部；可审核报单；可分配和取消关联（依据服务层实现）
- EMPLOYEE: 受限权限，仅可操作与自己关联的数据；不可创建/更新/删除产品；可创建客户并自动与自己关联；报单仅可操作自己的且未审核的；无审核和打款权限

通用请求头
- Authorization: Bearer <token>
- Content-Type: application/json

错误返回
- 状态码: 400/401/403/404 等
- Body: 纯文本或JSON错误信息（如: "未授权"、"令牌无效"、业务错误消息）

### Users 用户
- POST /api/users
  - 创建用户（当前环境一般通过SQL创建；若启用此接口，密码将加密存储）
  - Body(JSON): User 实体字段（username, password）
  - Response: 201 Created, User

- GET /api/users/{id}
  - 获取用户详情

- GET /api/users/username/{username}
  - 通过用户名获取用户

- PUT /api/users/{id}
  - 更新用户（密码会被加密存储）

- DELETE /api/users/{id}
  - 删除用户

- GET /api/users
  - 获取用户列表

- GET /api/users/exists/{username}
  - 检查用户名是否存在

- POST /api/users/validate
  - 验证用户名/密码是否正确（返回 true/false）

示例
```
curl -X GET http://localhost:8080/api/users/5 -H "Authorization: Bearer <token>"
```

### Products 产品
- POST /api/products
  - 创建产品（EMPLOYEE 禁止）
  - Body(JSON): Product 实体

- GET /api/products/{id}
  - 获取产品详情（EMPLOYEE 仅可查看被分配的）

- GET /api/products/name/{name}
  - 按名称精确查询

- GET /api/products/search/name?name=关键词
  - 按名称模糊查询

- GET /api/products/search/manufacturer?manufacturer=厂商
  - 按生产公司查询

- PUT /api/products/{id}
  - 更新产品（EMPLOYEE 禁止）

- PUT /api/products/{id}/stock?stockQuantity=100
  - 更新库存

- DELETE /api/products/{id}
  - 删除产品（EMPLOYEE 禁止）

- GET /api/products
  - 获取产品列表（EMPLOYEE 仅返回与自己关联的）

- GET /api/products/count
  - 获取产品总数

### Customers 客户
- POST /api/customers
  - 创建客户（EMPLOYEE可创建，自动关联到自己）

- GET /api/customers/{id}
  - 获取客户详情（EMPLOYEE 仅可查看关联的）

- GET /api/customers/search/name?customerName=关键词
- GET /api/customers/search/company?company=公司
- GET /api/customers/search/province?province=省份
- GET /api/customers/search?customerName=&address=&company=&province=
  - 多条件搜索

- PUT /api/customers/{id}
  - 更新客户（EMPLOYEE 仅可更新与自己关联的）

- DELETE /api/customers/{id}
  - 删除客户（EMPLOYEE 仅可删除与自己关联的）

- GET /api/customers
  - 获取客户列表（EMPLOYEE 返回与自己关联的）

- GET /api/customers/count
  - 客户总数

### Sells 报单
- POST /api/sells
  - 创建报单（EMPLOYEE 仅能创建自己的）
  - Body(JSON): Sell 实体（包含 sellKind, payMethod 等）

- GET /api/sells/{id}
  - 获取报单详情（EMPLOYEE 仅可查看自己的；审核后不可修改/删除）

- PUT /api/sells/{id}
  - 更新报单（仅未审核；EMPLOYEE 仅可更新自己的）

- PUT /api/sells/{id}/payment?isPaid=true|false
  - 更新打款状态（BOSS 权限）

- PUT /api/sells/{id}/validity?isValid=true|false
  - 更新有效性（审核/撤销）（BOSS/AUDITOR 权限）

- PUT /api/sells/{id}/approve
  - 审核通过（EMPLOYEE 禁止）

- PUT /api/sells/{id}/reject
  - 审核拒绝（EMPLOYEE 禁止）

- PUT /api/sells/{id}/mark-paid
  - 标记已打款（仅 BOSS）

- PUT /api/sells/{id}/mark-unpaid
  - 标记未打款（仅 BOSS）

- DELETE /api/sells/{id}
  - 删除报单（仅未审核；EMPLOYEE 仅可删除自己的）

- GET /api/sells
  - 报单列表（EMPLOYEE 仅返回自己的）

- 搜索/筛选
  - GET /api/sells/search/seller?sellerName=
  - GET /api/sells/search/date?sellDate=YYYY-MM-DD
  - GET /api/sells/search/date-range?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
  - GET /api/sells/search/product?productName=
  - GET /api/sells/search/customer-name?customerName=
  - GET /api/sells/search/customer-company?customerCompany=
  - GET /api/sells/search/customer-province?customerProvince=
  - GET /api/sells/search/sell-kind?sellKind=
  - GET /api/sells/search/pay-method?payMethod=
  - GET /api/sells/search/price-range?minTotalPrice=0&maxTotalPrice=99999
  - GET /api/sells/search?sellerName=&productName=&productSpec=&customerName=&customerCompany=&customerProvince=&sellKind=&payMethod=&startDate=&endDate=&minTotalPrice=&maxTotalPrice=&isPaid=&isValid=

- 统计
  - GET /api/sells/count
  - GET /api/sells/paid
  - GET /api/sells/unpaid
  - GET /api/sells/valid
  - GET /api/sells/invalid

### Relations 关联（用户-产品 / 用户-客户）

用户-产品
- POST /api/user-product-relations/assign?username=&productId=
- DELETE /api/user-product-relations/unassign?username=&productId=
- POST /api/user-product-relations/batch-assign?username=  Body: [productId, ...]
- DELETE /api/user-product-relations/batch-unassign?username=  Body: [productId, ...]
- GET /api/user-product-relations/user/{username}/products
- GET /api/user-product-relations/product/{productId}/users
- GET /api/user-product-relations/check-permission?username=&productId=
- GET /api/user-product-relations/relation?username=&productId=
- GET /api/user-product-relations/user/{username}/relations
- GET /api/user-product-relations/product/{productId}/relations
- DELETE /api/user-product-relations/user/{username}
- DELETE /api/user-product-relations/product/{productId}
- GET /api/user-product-relations/user/{username}/count
- GET /api/user-product-relations/product/{productId}/count
- GET /api/user-product-relations/user/{username}/has-any
- GET /api/user-product-relations/product/{productId}/is-managed

用户-客户
- POST /api/user-customer-relations/assign?username=&customerId=
- DELETE /api/user-customer-relations/unassign?username=&customerId=
- POST /api/user-customer-relations/batch-assign?username=  Body: [customerId, ...]
- DELETE /api/user-customer-relations/batch-unassign?username=  Body: [customerId, ...]
- GET /api/user-customer-relations/user/{username}/customers
- GET /api/user-customer-relations/customer/{customerId}/users
- GET /api/user-customer-relations/check-permission?username=&customerId=
- GET /api/user-customer-relations/relation?username=&customerId=
- GET /api/user-customer-relations/user/{username}/relations
- GET /api/user-customer-relations/customer/{customerId}/relations
- DELETE /api/user-customer-relations/user/{username}
- DELETE /api/user-customer-relations/customer/{customerId}
- GET /api/user-customer-relations/user/{username}/count
- GET /api/user-customer-relations/customer/{customerId}/count
- GET /api/user-customer-relations/user/{username}/has-any
- GET /api/user-customer-relations/customer/{customerId}/is-managed

示例：分配产品
```
curl -X POST "http://localhost:8080/api/user-product-relations/assign?username=测试员工&productId=7" \
  -H "Authorization: Bearer <token>"
```

### 认证与安全要点
- 登录后返回JWT；放到请求头 Authorization: Bearer <token>
- 访问 /api/** 必须携带token；/api/auth/** 放行
- 建议：HTTPS 传输；Access Token短期有效；考虑Refresh Token；后端使用BCrypt存储密码


