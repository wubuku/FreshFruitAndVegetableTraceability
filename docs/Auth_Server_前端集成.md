# Auth Server SPA前端集成指南

## 概述

本文档详细说明如何使用现代化的单页应用（SPA）替换授权服务器的默认页面。本文档假设读者可能不熟悉 OAuth2 和跨域请求处理等概念，将尽可能详细地解释每个步骤。

## 浏览器行为说明

### Cookie 处理
浏览器会自动处理 Cookie：
1. 当服务器在响应中包含 Set-Cookie 头时，浏览器会自动保存这些 cookie
2. 之后的请求会自动带上这些 cookie（在同域请求中）
3. 对于跨域请求，只有设置了 `credentials: 'include'` 才会发送 cookie

### CSRF 保护
1. 服务器会在响应中设置一个名为 XSRF-TOKEN 的 cookie
2. 前端需要从这个 cookie 中读取值，并在后续请求中通过 X-XSRF-TOKEN 头发送
3. 这个机制可以防止跨站请求伪造攻击

## 详细实现步骤

### 1. 初始化

在应用启动时，需要初始化 CSRF 保护：

```typescript
// utils/csrf.ts
export function getCsrfToken(): string {
    // 从 cookie 中读取 CSRF token
    const cookies = document.cookie.split(';');
    const csrfCookie = cookies.find(cookie => 
        cookie.trim().startsWith('XSRF-TOKEN=')
    );
    return csrfCookie 
        ? decodeURIComponent(csrfCookie.split('=')[1]) 
        : '';
}

// 初始化 CSRF 保护
export async function initializeCsrf(): Promise<void> {
    // 访问一个端点来触发服务器设置 CSRF cookie
    const response = await fetch('/api/csrf-token', {
        credentials: 'include'  // 必须设置！否则不会发送和接收 cookie
    });
    
    if (!response.ok) {
        throw new Error('Failed to initialize CSRF protection');
    }
    
    // 此时浏览器已经自动保存了 CSRF cookie
    const token = getCsrfToken();
    if (!token) {
        throw new Error('No CSRF token found in cookies');
    }
}
```

### 2. 创建 HTTP 客户端

创建一个统一的 HTTP 客户端来处理请求：

```typescript
// utils/http-client.ts
export class HttpClient {
    private static instance: HttpClient;
    
    private constructor() {}
    
    static getInstance(): HttpClient {
        if (!HttpClient.instance) {
            HttpClient.instance = new HttpClient();
        }
        return HttpClient.instance;
    }
    
    async fetch(url: string, options: RequestInit = {}): Promise<Response> {
        // 确保包含 credentials
        options.credentials = 'include';
        
        // 添加 CSRF token
        const csrfToken = getCsrfToken();
        if (csrfToken) {
            options.headers = {
                ...options.headers,
                'X-XSRF-TOKEN': csrfToken
            };
        }
        
        const response = await fetch(url, options);
        
        // 处理重定向
        if (response.redirected) {
            // 解析重定向 URL
            const redirectUrl = new URL(response.url);
            
            // 根据路径决定如何处理
            switch (redirectUrl.pathname) {
                case '/login':
                    // 显示登录页面
                    router.push('/auth/login');
                    break;
                case '/oauth2/authorize':
                    // 检查是否需要用户同意
                    const html = await response.text();
                    if (html.includes('consent_form')) {
                        router.push('/auth/consent');
                    }
                    break;
                // ... 其他重定向处理
            }
        }
        
        return response;
    }
}

// 使用示例
const httpClient = HttpClient.getInstance();
```

### 3. 登录页面实现

```typescript
// views/auth/LoginView.vue
<template>
  <form @submit.prevent="handleSubmit">
    <input v-model="username" type="text" required />
    <input v-model="password" type="password" required />
    <button type="submit">登录</button>
  </form>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import { HttpClient } from '@/utils/http-client';

export default defineComponent({
    setup() {
        const username = ref('');
        const password = ref('');
        const httpClient = HttpClient.getInstance();
        
        async function handleSubmit() {
            try {
                // 创建表单数据
                const formData = new FormData();
                formData.append('username', username.value);
                formData.append('password', password.value);
                
                const response = await httpClient.fetch('/login', {
                    method: 'POST',
                    body: formData
                });
                
                if (response.ok) {
                    // 登录成功
                    // 注意：此时浏览器已经自动保存了服务器发送的 cookie
                    // 不需要手动处理 cookie
                    
                    // 检查是否需要修改密码
                    const data = await response.json();
                    if (data.passwordChangeRequired) {
                        router.push('/auth/change-password');
                    } else {
                        // 继续 OAuth2 流程
                        startOAuth2Flow();
                    }
                }
            } catch (error) {
                console.error('Login failed:', error);
            }
        }
        
        return {
            username,
            password,
            handleSubmit
        };
    }
});
</script>
```

### 4. 同意页面实现

```typescript
// views/auth/ConsentView.vue
<template>
  <div v-if="consentData">
    <h2>授权请求</h2>
    <p>{{ consentData.clientName }} 请求访问您的账户</p>
    
    <form @submit.prevent="handleSubmit">
      <div v-for="scope in consentData.requestedScopes" :key="scope">
        <label>
          <input 
            type="checkbox" 
            v-model="selectedScopes" 
            :value="scope"
          />
          {{ scope }}
        </label>
      </div>
      
      <button type="submit">同意</button>
      <button type="button" @click="handleCancel">取消</button>
    </form>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { HttpClient } from '@/utils/http-client';

export default defineComponent({
    setup() {
        const consentData = ref(null);
        const selectedScopes = ref([]);
        const httpClient = HttpClient.getInstance();
        
        onMounted(async () => {
            // 获取同意页面数据
            const response = await httpClient.fetch('/oauth2/authorize', {
                method: 'GET'
            });
            
            if (response.ok) {
                const html = await response.text();
                // 解析 HTML 获取必要数据
                consentData.value = parseConsentHtml(html);
            }
        });
        
        async function handleSubmit() {
            try {
                const formData = new FormData();
                formData.append('client_id', consentData.value.clientId);
                formData.append('state', consentData.value.state);
                
                selectedScopes.value.forEach(scope => {
                    formData.append('scope', scope);
                });
                
                const response = await httpClient.fetch('/oauth2/authorize', {
                    method: 'POST',
                    body: formData
                });
                
                if (response.redirected) {
                    // 这个重定向包含授权码，应该允许它发生
                    window.location.href = response.url;
                }
            } catch (error) {
                console.error('Consent submission failed:', error);
            }
        }
        
        return {
            consentData,
            selectedScopes,
            handleSubmit
        };
    }
});
</script>
```

## 重要说明

### Cookie 和会话处理
1. 所有请求都必须设置 `credentials: 'include'`
2. 不要手动操作 cookie，让浏览器自动处理
3. 服务器会自动维护会话状态

### CSRF 保护机制

#### Token 生命周期
1. 服务器在响应中通过 `XSRF-TOKEN` cookie 设置 token
2. 前端需要在请求头 `X-XSRF-TOKEN` 中发送这个 token
3. 服务器会在需要时自动更新 token（比如会话更新时）

#### 实现示例

```typescript
// utils/csrf.ts

// 从 cookie 中读取当前的 CSRF token
export function getCsrfToken(): string {
    const cookies = document.cookie.split(';');
    const csrfCookie = cookies.find(cookie => 
        cookie.trim().startsWith('XSRF-TOKEN=')
    );
    return csrfCookie 
        ? decodeURIComponent(csrfCookie.split('=')[1]) 
        : '';
}

// 初始化 CSRF 保护 - 应用启动时调用一次
export async function initializeCsrf(): Promise<void> {
    const token = getCsrfToken();
    if (!token) {
        // 只有在没有 token 时才请求
        const response = await fetch('/api/csrf-token', {
            credentials: 'include'
        });
        
        if (!response.ok) {
            throw new Error('Failed to initialize CSRF protection');
        }
    }
}

// HTTP 客户端中处理 CSRF token
export class HttpClient {
    async fetch(url: string, options: RequestInit = {}): Promise<Response> {
        try {
            // 添加当前的 CSRF token
            const csrfToken = getCsrfToken();
            if (csrfToken) {
                options.headers = {
                    ...options.headers,
                    'X-XSRF-TOKEN': csrfToken
                };
            }
            
            const response = await fetch(url, {
                ...options,
                credentials: 'include'  // 必须，用于接收更新的 token
            });
            
            if (response.status === 403) {
                // 可能是 token 失效
                const newToken = getCsrfToken();
                if (newToken && newToken !== csrfToken) {
                    // token 已更新，重试请求
                    return this.fetch(url, options);
                }
            }
            
            return response;
        } catch (error) {
            console.error('Request failed:', error);
            throw error;
        }
    }
}

// 使用示例
export async function initializeApp() {
    // 1. 应用启动时初始化
    await initializeCsrf();
    
    // 2. 之后的请求会自动使用和更新 token
    const httpClient = new HttpClient();
    
    // 示例请求
    const response = await httpClient.fetch('/api/some-endpoint', {
        method: 'POST',
        body: JSON.stringify({ /* data */ })
    });
}

#### 注意事项

1. Token 初始化：
   - 应用启动时调用一次 `initializeCsrf()`
   - 不需要在每个请求前都初始化
   - 服务器会在需要时自动更新 token

2. Token 更新：
   - 服务器可能在任何响应中更新 token
   - 浏览器会自动更新 cookie 中的 token
   - 下一个请求会自动使用新的 token

3. Token 失效处理：
   - 如果请求返回 403，可能是 token 失效
   - 检查是否有新的 token
   - 如果有新 token，可以重试请求

4. 调试提示：
   - 使用浏览器开发工具观察 XSRF-TOKEN cookie 的变化
   - 检查请求头中的 X-XSRF-TOKEN 是否正确
   - 注意会话状态对 token 的影响

### 错误处理
1. 401 状态码表示未认证，需要重新登录
2. 403 状态码可能是 CSRF token 无效
3. 重定向需要根据具体情况处理

### 调试技巧
1. 使用浏览器开发工具的 Network 面板监控请求
2. 在 Application > Cookies 中查看 cookie
3. 检查请求头中是否包含正确的 CSRF token

## 常见问题

### Q: 为什么我的请求没有发送 cookie？
A: 检查是否设置了 `credentials: 'include'`，这是跨域请求发送 cookie 的必要条件。

### Q: CSRF token 无效怎么办？
A:
1. 确保从正确的 cookie 中读取 token
2. 检查请求头名称是否为 X-XSRF-TOKEN
3. 可能需要重新初始化 CSRF 保护

### Q: 重定向处理有什么注意事项？
A:
1. 某些重定向（如获取授权码）应该允许发生
2. 其他重定向应该在 SPA 内部处理
3. 使用路由来显示相应的页面
