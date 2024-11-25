package org.dddml.ffvtraceability.restful.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/oauth2")
public class TokenController {
    
    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TOKEN_ENDPOINT = "http://localhost:9000/oauth2/token";
    private static final String CLIENT_ID = "ffv-client";
    private static final String CLIENT_SECRET = "secret";

    @PostMapping("/token")
    public ResponseEntity<String> getToken(
            @RequestParam("code") String code,
            @RequestParam("code_verifier") String codeVerifier,
            @RequestParam("redirect_uri") String redirectUri) {
            
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);
        body.add("code_verifier", codeVerifier);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        
        try {
            logger.info("Sending token request to {}", TOKEN_ENDPOINT);
            ResponseEntity<String> response = restTemplate.postForEntity(TOKEN_ENDPOINT, request, String.class);
            logger.info("Token response status: {}", response.getStatusCode());
            
            String responseBody = response.getBody();
            logger.info("Token response body: {}", responseBody);
            
            // 解析响应
            JsonNode tokenResponse = objectMapper.readTree(responseBody);
            
            // 确保包含所有必要的字段
            if (!tokenResponse.has("access_token") || 
                !tokenResponse.has("token_type") || 
                !tokenResponse.has("expires_in")) {
                throw new RuntimeException("Invalid token response format");
            }
            
            // 记录成功信息
            logger.info("✅ Token request successful!");
            logger.info("🔑 Access Token: {}", tokenResponse.get("access_token").asText().substring(0, 50) + "...");
            if (tokenResponse.has("refresh_token")) {
                logger.info("🔄 Refresh Token: {}", tokenResponse.get("refresh_token").asText().substring(0, 50) + "...");
            }
            if (tokenResponse.has("id_token")) {
                logger.info("🎫 ID Token: {}", tokenResponse.get("id_token").asText().substring(0, 50) + "...");
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseBody);
                    
        } catch (HttpClientErrorException e) {
            logger.error("Token request failed with status {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getResponseBodyAsString());
                    
        } catch (Exception e) {
            logger.error("Token request failed with error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"internal_server_error\",\"error_description\":\"" + e.getMessage() + "\"}");
        }
    }
} 