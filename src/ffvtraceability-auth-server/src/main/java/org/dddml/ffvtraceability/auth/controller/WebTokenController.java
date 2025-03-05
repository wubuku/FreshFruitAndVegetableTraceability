package org.dddml.ffvtraceability.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/web-clients/oauth2")
public class WebTokenController {

    private static final Logger logger = LoggerFactory.getLogger(WebTokenController.class);
    private final OAuth2AuthorizationService authorizationService;
    private final RestTemplate restTemplate;
    @Value("${auth-server.web-clients.allowed-client-ids}")
    private String[] allowedClientIds;
    @Value("${auth-server.web-clients.client-secrets}")
    private String[] clientSecrets;

    public WebTokenController(OAuth2AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
        this.restTemplate = new RestTemplate();
    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("code") String code,
            @RequestParam("code_verifier") String codeVerifier,
            @RequestParam("redirect_uri") String redirectUri,
            HttpServletRequest request) {

        // 1. 验证 client_id 是否在允许列表中
        int clientIndex = -1;
        for (int i = 0; i < allowedClientIds.length; i++) {
            if (allowedClientIds[i].equals(clientId)) {
                clientIndex = i;
                break;
            }
        }

        if (clientIndex == -1) {
            logger.warn("Unauthorized client_id: {}", clientId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"unauthorized_client\"}");
        }
//        try {
        // 2. 构建请求头和请求体
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecrets[clientIndex]);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);
        body.add("code_verifier", codeVerifier);

        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(body, headers);

        // 3. 调用本地 token endpoint
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + request.getServerPort() + "/oauth2/token",
                requestEntity,
                String.class
        );

        // 4. 返回响应，移除手动的 CORS 头设置
        return ResponseEntity.status(response.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.getBody());

//        } catch (Exception e) {
//            logger.error("Token request failed", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body("{\"error\":\"internal_server_error\",\"error_description\":\""
//                            + e.getMessage() + "\"}");
//        }
    }
} 