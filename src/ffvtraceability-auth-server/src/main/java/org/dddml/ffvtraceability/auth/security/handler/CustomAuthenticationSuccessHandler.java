package org.dddml.ffvtraceability.auth.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dddml.ffvtraceability.auth.security.CustomUserDetails;
import org.dddml.ffvtraceability.auth.util.UrlStateEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    private final UrlStateEncoder urlStateEncoder;

    public CustomAuthenticationSuccessHandler(UrlStateEncoder urlStateEncoder) {
        this.urlStateEncoder = urlStateEncoder;
        setDefaultTargetUrl("/");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        String originalUrl = request.getParameter("continue");

        // 检查是否需要修改密码
        if (userDetails.isPasswordChangeRequired() ||
                userDetails.isFirstLogin() ||
                userDetails.isPasswordExpired()) {

            String redirectUrl = UriComponentsBuilder
                    .fromPath("/password/change")
                    .queryParam("state", urlStateEncoder.encode(originalUrl))
                    .build()
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            return;
        }

        // 如果有原始URL，使用它
        if (StringUtils.hasText(originalUrl)) {
            getRedirectStrategy().sendRedirect(request, response, originalUrl);
            return;
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
} 