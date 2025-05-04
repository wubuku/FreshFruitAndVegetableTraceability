package org.dddml.ffvtraceability.auth.service;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.dddml.ffvtraceability.auth.config.WeChatConfig;
import org.dddml.ffvtraceability.auth.dto.UserDto;
import org.dddml.ffvtraceability.auth.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WeChatService {
    private static final Logger logger = LoggerFactory.getLogger(WeChatService.class);
    
    private final WxMpService wxMpService;
    private final WeChatConfig weChatConfig;
    private final UserService userService;
    private final UserIdentificationService userIdentificationService;
    private final UserDetailsService userDetailsService;

    public WeChatService(WxMpService wxMpService, 
                         WeChatConfig weChatConfig,
                         UserService userService,
                         UserIdentificationService userIdentificationService,
                         UserDetailsService userDetailsService) {
        this.wxMpService = wxMpService;
        this.weChatConfig = weChatConfig;
        this.userService = userService;
        this.userIdentificationService = userIdentificationService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Get the WeChat authorization URL
     * @param state Random state for CSRF protection
     * @return The authorization URL
     */
    public String getAuthorizationUrl(String state) {
        return wxMpService.getOAuth2Service().buildAuthorizationUrl(
                weChatConfig.getRedirectUri(),
                WxConsts.OAuth2Scope.SNSAPI_USERINFO,
                state);
    }

    /**
     * Process WeChat login
     * @param code The authorization code from WeChat
     * @param state The state parameter for CSRF validation
     * @return The authenticated user
     */
    @Transactional
    public Authentication processWeChatLogin(String code, String state) {
        try {
            // Get access token and OpenID from WeChat
            WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
            String openId = accessToken.getOpenId();
            
            // Find user by WeChat OpenID
            Optional<String> existingUser = userIdentificationService.findUsernameByIdentifier("WECHAT_OPENID", openId);
            
            if (existingUser.isPresent()) {
                // User exists, authenticate
                return authenticateUser(existingUser.get());
            } else {
                // User doesn't exist, get user info from WeChat and create new user
                return createNewWeChatUser(accessToken);
            }
        } catch (WxErrorException e) {
            logger.error("WeChat authentication error", e);
            throw new AuthenticationException("Failed to authenticate with WeChat: " + e.getMessage());
        }
    }

    /**
     * Create a new user from WeChat authentication
     * @param accessToken The OAuth2 access token from WeChat
     * @return The authentication token
     */
    private Authentication createNewWeChatUser(WxOAuth2AccessToken accessToken) throws WxErrorException {
        // Get user info from WeChat using OAuth2 user info API
        WxOAuth2UserInfo userInfo = wxMpService.getOAuth2Service().getUserInfo(accessToken, null);
        
        // Generate a random username and password
        String username = "wx_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String password = UUID.randomUUID().toString();
        
        // Create the user
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setEnabled(true);
        
        // Set user profile information
        userDto.setFirstName(userInfo.getNickname());
        // Use WeChat profile image if available
        if (userInfo.getHeadImgUrl() != null && !userInfo.getHeadImgUrl().isEmpty()) {
            userDto.setProfileImageUrl(userInfo.getHeadImgUrl());
        }
        
        // Create the user in the database
        userService.createUser(userDto, password);
        
        // Link the WeChat OpenID to the user
        userIdentificationService.addUserIdentification(
                username, "WECHAT_OPENID", accessToken.getOpenId(), true);
        
        // Also store the UnionID if available
        if (accessToken.getUnionId() != null && !accessToken.getUnionId().isEmpty()) {
            userIdentificationService.addUserIdentification(
                    username, "WECHAT_UNIONID", accessToken.getUnionId(), true);
        }
        
        // Authenticate the user
        return authenticateUser(username);
    }

    /**
     * Authenticate user with the UserDetailsService
     * @param username The username to authenticate
     * @return Authentication token
     */
    private Authentication authenticateUser(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
} 