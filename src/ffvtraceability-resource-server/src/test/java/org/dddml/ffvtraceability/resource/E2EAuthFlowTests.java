package org.dddml.ffvtraceability.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E2EAuthFlowTests {

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String AUTH_SERVER = "http://localhost:9000";
    private final String CLIENT_ID = "ffv-client";
    private final String CLIENT_SECRET = "secret";
    private final String REDIRECT_URI = "http://127.0.0.1:3000/callback";
    private final BasicCookieStore cookieStore = new BasicCookieStore();
    private final HttpClientContext context = HttpClientContext.create();

    @Test
    public void testFullAuthorizationCodeFlow() throws Exception {
        System.out.println("\n🚀 Starting OAuth2 Authorization Code Flow Test\n");
        
        context.setCookieStore(cookieStore);
        try (CloseableHttpClient client = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .disableRedirectHandling()
                .build()) {
            // 1. 生成 PKCE 参数
            String codeVerifier = generateCodeVerifier();
            System.out.println("🔑 Code Verifier: " + codeVerifier);
            
            String codeChallenge = generateCodeChallenge(codeVerifier);
            System.out.println("🔒 Code Challenge: " + codeChallenge);

            // 2. 获取授权码
            System.out.println("\n📨 Starting Authorization Code Request...");
            String authorizationCode = getAuthorizationCode(client, codeChallenge);
            System.out.println("✅ Authorization Code: " + authorizationCode);

            // 3. 交换访问令牌
            System.out.println("\n🔄 Exchanging Authorization Code for Access Token...");
            String accessToken = getAccessToken(client, authorizationCode, codeVerifier);
            System.out.println("✅ Access Token: " + accessToken.substring(0, 50) + "...");

            // 4. 测试资源访问
            System.out.println("\n🧪 Testing Resource Access...");
            testResourceAccess(client, accessToken);
            
            System.out.println("\n✨ All Tests Completed Successfully!\n");
        }
    }

    private String generateCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
    }

    private String generateCodeChallenge(String codeVerifier) throws Exception {
        byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes);
        byte[] digest = messageDigest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

    private String getAuthorizationCode(CloseableHttpClient client, String codeChallenge) throws Exception {
        // 1. 获取登录页面和 CSRF token
        System.out.println("📝 Getting login page and CSRF token...");
        HttpGet loginPageRequest = new HttpGet(AUTH_SERVER + "/login");
        loginPageRequest.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9");
        String csrfToken = null;
        
        try (CloseableHttpResponse response = client.execute(loginPageRequest, context)) {
            String html = EntityUtils.toString(response.getEntity());
            Document doc = Jsoup.parse(html);
            Element csrfElement = doc.selectFirst("input[name=_csrf]");
            if (csrfElement != null) {
                csrfToken = csrfElement.attr("value");
                System.out.println("🔐 CSRF Token: " + csrfToken);
            }
        }

        // 2. 执行登录
        System.out.println("\n🔑 Performing login...");
        HttpPost loginRequest = new HttpPost(AUTH_SERVER + "/login");
        loginRequest.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9");
        loginRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
        loginRequest.setHeader("Origin", AUTH_SERVER);
        loginRequest.setHeader("Referer", AUTH_SERVER + "/login");

        String formData = String.format("username=%s&password=%s&_csrf=%s",
                "admin", "admin", csrfToken);
        
        loginRequest.setEntity(new StringEntity(formData, ContentType.APPLICATION_FORM_URLENCODED));
        
        try (CloseableHttpResponse response = client.execute(loginRequest, context)) {
            System.out.println("📤 Login Response Status: " + response.getCode());
            System.out.println("📍 Login Response Location: " + response.getHeader("Location"));
        }

        // 3. 发起授权请求
        System.out.println("\n🔐 Initiating OAuth2 authorization request...");
        String authorizationUrl = AUTH_SERVER + "/oauth2/authorize?" +
                "response_type=code" +
                "&client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI +
                //"&scope=openid+read+write" +
                "&scope=openid+profile" +
                "&code_challenge=" + codeChallenge +
                "&code_challenge_method=S256";
        System.out.println("🌐 Authorization URL: " + authorizationUrl);

        HttpGet authorizationRequest = new HttpGet(authorizationUrl);
        authorizationRequest.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9");
        
        try (CloseableHttpResponse response = client.execute(authorizationRequest, context)) {
            System.out.println("📤 Authorization Response Status: " + response.getCode());
            
            if (response.getCode() == 200) {
                System.out.println("👉 Consent required, processing consent form...");
                String html = EntityUtils.toString(response.getEntity());
                Document doc = Jsoup.parse(html);
                Element csrfElement = doc.selectFirst("input[name=_csrf]");
                Element stateElement = doc.selectFirst("input[name=state]");
                
                if (csrfElement != null && stateElement != null) {
                    String consentCsrfToken = csrfElement.attr("value");
                    String state = stateElement.attr("value");
                    System.out.println("🔐 Consent CSRF Token: " + consentCsrfToken);
                    System.out.println("🔐 State: " + state);
                    
                    HttpPost consentRequest = new HttpPost(AUTH_SERVER + "/oauth2/authorize");
                    consentRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    consentRequest.setHeader("Origin", AUTH_SERVER);
                    consentRequest.setHeader("Referer", authorizationUrl);
                    
                    String consentData = String.format("client_id=%s&state=%s&scope=openid&scope=profile&_csrf=%s",
                            CLIENT_ID,
                            state,
                            consentCsrfToken);
                    
                    consentRequest.setEntity(new StringEntity(consentData, ContentType.APPLICATION_FORM_URLENCODED));
                    
                    try (CloseableHttpResponse consentResponse = client.execute(consentRequest, context)) {
                        System.out.println("📤 Consent Response Status: " + consentResponse.getCode());
                        System.out.println("📍 Consent Response Location: " + consentResponse.getHeader("Location"));
                        return extractCode(consentResponse.getHeader("Location").getValue());
                    }
                }
            } else if (response.getCode() == 302) {
                System.out.println("📍 Authorization Response Location: " + response.getHeader("Location"));
                return extractCode(response.getHeader("Location").getValue());
            }
        }
        
        throw new RuntimeException("Failed to get authorization code");
    }

    private String getAccessToken(CloseableHttpClient client, String code, String codeVerifier) throws Exception {
        System.out.println("\n🔄 Requesting Access Token...");
        HttpPost tokenRequest = new HttpPost(AUTH_SERVER + "/oauth2/token");
        String auth = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        tokenRequest.setHeader("Authorization", "Basic " + encodedAuth);

        String tokenRequestBody = "grant_type=authorization_code" +
                "&code=" + code +
                "&redirect_uri=" + REDIRECT_URI +
                "&code_verifier=" + codeVerifier;
        
        System.out.println("📝 Token Request Body: " + tokenRequestBody);
        tokenRequest.setEntity(new StringEntity(tokenRequestBody, ContentType.APPLICATION_FORM_URLENCODED));

        try (CloseableHttpResponse response = client.execute(tokenRequest)) {
            int statusCode = response.getCode();
            String json = EntityUtils.toString(response.getEntity());
            
            System.out.println("\n📤 Token Response Details:");
            System.out.println("Status Code: " + statusCode);
            System.out.println("Response Headers:");
            Arrays.stream(response.getHeaders()).forEach(header -> 
                System.out.println("  " + header.getName() + ": " + header.getValue())
            );
            System.out.println("Response Body: " + json);

            // 检查状态码
            if (statusCode < 200 || statusCode >= 300) {
                throw new RuntimeException(String.format(
                    "Token request failed with status %d. Response: %s", 
                    statusCode, 
                    json
                ));
            }

            // 解析响应
            JsonNode node = objectMapper.readTree(json);
            if (!node.has("access_token")) {
                throw new RuntimeException(
                    "Token response does not contain access_token. Response: " + json
                );
            }

            String accessToken = node.get("access_token").asText();
            System.out.println("\n✅ Successfully obtained access token");
            return accessToken;
        }
    }

    private void testResourceAccess(CloseableHttpClient client, String accessToken) throws Exception {
        // 解码并打印 JWT 内容
        String[] parts = accessToken.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        System.out.println("\n📝 Access Token Claims:");
        System.out.println(objectMapper.readTree(payload).toPrettyString());

        // 测试公开 API
        System.out.println("\n🧪 Testing Public API...");
        HttpGet publicRequest = new HttpGet("http://localhost:" + port + "/api/public/test");
        try (CloseableHttpResponse response = client.execute(publicRequest)) {
            System.out.println("📤 Response Status: " + response.getCode());
            System.out.println("📄 Response Body: " + EntityUtils.toString(response.getEntity()));
        }

        // 测试需要认证的 API
        System.out.println("\n🧪 Testing Protected API...");
        HttpGet protectedRequest = new HttpGet("http://localhost:" + port + "/api/test");
        protectedRequest.setHeader("Authorization", "Bearer " + accessToken);
        try (CloseableHttpResponse response = client.execute(protectedRequest)) {
            System.out.println("📤 Response Status: " + response.getCode());
            System.out.println("📄 Response Body: " + EntityUtils.toString(response.getEntity()));
        }

        // 测试管理员 API
        System.out.println("\n🧪 Testing Admin API...");
        HttpGet adminRequest = new HttpGet("http://localhost:" + port + "/api/admin/test");
        adminRequest.setHeader("Authorization", "Bearer " + accessToken);
        try (CloseableHttpResponse response = client.execute(adminRequest)) {
            System.out.println("📤 Response Status: " + response.getCode());
            System.out.println("📄 Response Body: " + EntityUtils.toString(response.getEntity()));
        }

        // 测试用户信息 API
        System.out.println("\n🧪 Testing User Info API...");
        HttpGet userInfoRequest = new HttpGet("http://localhost:" + port + "/api/user/me");
        userInfoRequest.setHeader("Authorization", "Bearer " + accessToken);
        try (CloseableHttpResponse response = client.execute(userInfoRequest)) {
            System.out.println("📤 Response Status: " + response.getCode());
            System.out.println("📄 Response Body: " + EntityUtils.toString(response.getEntity()));
        }
    }

    private String extractCode(String location) {
        int codeIndex = location.indexOf("code=");
        if (codeIndex == -1) return null;
        String code = location.substring(codeIndex + 5);
        int andIndex = code.indexOf("&");
        if (andIndex != -1) {
            code = code.substring(0, andIndex);
        }
        return code;
    }
} 