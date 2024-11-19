package org.dddml.ffvtraceability.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.util.Base64;

public class CacheTestClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String RESOURCE_SERVER_URL = "http://localhost:8080";

    public static void main(String[] args) throws Exception {
        // 从环境变量获取访问令牌
        String accessToken = System.getenv("ACCESS_TOKEN");
        if (accessToken == null || accessToken.isEmpty()) {
            System.out.println("❌ No access token provided. Please set ACCESS_TOKEN environment variable.");
            System.out.println("You can get an access token by running the test.sh script in the auth-server project.");
            System.exit(1);
        }

        // 解码并打印 JWT 内容
        String[] parts = accessToken.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        System.out.println("\n📝 Access Token Claims:");
        System.out.println(objectMapper.readTree(payload).toPrettyString());

        // 创建 HTTP 客户端
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // 循环测试，每次间隔 2 秒
            while (true) {
                System.out.println("\n🔄 Testing endpoints...");
                testEndpoints(client, accessToken);
                Thread.sleep(2000);  // 等待2秒
            }
        }
    }

    private static void testEndpoints(CloseableHttpClient client, String accessToken) throws Exception {
        // 测试需要认证的 API
        System.out.println("\n🧪 Testing Protected API...");
        testEndpoint(client, "/api/test", accessToken);

        // 测试管理员 API
        System.out.println("\n🧪 Testing Admin API...");
        testEndpoint(client, "/api/admin/test", accessToken);

        // 测试用户信息 API
        System.out.println("\n🧪 Testing User Info API...");
        testEndpoint(client, "/api/user/me", accessToken);
    }

    private static void testEndpoint(CloseableHttpClient client, String path, String accessToken) throws Exception {
        HttpGet request = new HttpGet(RESOURCE_SERVER_URL + path);
        request.setHeader("Authorization", "Bearer " + accessToken);

        try (CloseableHttpResponse response = client.execute(request)) {
            System.out.println("📤 Response Status: " + response.getCode());
            System.out.println("📄 Response Body: " + EntityUtils.toString(response.getEntity()));
        }
    }
} 