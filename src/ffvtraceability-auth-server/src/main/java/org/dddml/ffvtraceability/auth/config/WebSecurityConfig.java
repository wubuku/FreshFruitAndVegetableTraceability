package org.dddml.ffvtraceability.auth.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebSecurityConfig {

    @Bean
    public FilterRegistrationBean<Filter> rateLimitingFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter());
        registrationBean.addUrlPatterns("/web-clients/oauth2/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    // 实现一个简单的限流过滤器
    private static class RateLimitingFilter extends OncePerRequestFilter {
        private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
        
        private static class TokenBucket {
            private final int capacity;
            private int tokens;
            private long lastRefillTime;
            
            public TokenBucket(int capacity) {
                this.capacity = capacity;
                this.tokens = capacity;
                this.lastRefillTime = System.currentTimeMillis();
            }
            
            public synchronized boolean tryConsume() {
                refill();
                if (tokens > 0) {
                    tokens--;
                    return true;
                }
                return false;
            }
            
            private void refill() {
                long now = System.currentTimeMillis();
                long timePassed = now - lastRefillTime;
                int refill = (int) (timePassed / TimeUnit.SECONDS.toMillis(1));
                if (refill > 0) {
                    tokens = Math.min(capacity, tokens + refill);
                    lastRefillTime = now;
                }
            }
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, 
                                      HttpServletResponse response,
                                      FilterChain filterChain) 
                throws ServletException, IOException {
            
            String clientId = request.getParameter("client_id");
            if (clientId == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"missing_client_id\"}");
                return;
            }

            TokenBucket bucket = buckets.computeIfAbsent(clientId, 
                k -> new TokenBucket(10)); // 每个客户端每秒10个请求的限制
            
            if (!bucket.tryConsume()) {
                response.setStatus(429);
                response.getWriter().write("{\"error\":\"too_many_requests\"}");
                return;
            }
            
            filterChain.doFilter(request, response);
        }
    }
} 