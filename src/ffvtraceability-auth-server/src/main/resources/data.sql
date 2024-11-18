-- 清理现有数据
DELETE FROM group_members;
DELETE FROM group_authorities;
DELETE FROM groups;
DELETE FROM authorities;
DELETE FROM users;

-- 创建测试用户
INSERT INTO users (username, password, enabled, password_change_required, first_login) VALUES
    ('admin', '{bcrypt}$2a$10$eKBDBSf4DBNzRwbF7fx5IetdKKjqzkYoST0F7Dkro84eRiDTBJYky', false, false, false),  -- password=admin
    ('user', '{bcrypt}$2a$10$eKBDBSf4DBNzRwbF7fx5IetdKKjqzkYoST0F7Dkro84eRiDTBJYky', true, true, true);   -- password=admin

-- 创建用户组
INSERT INTO groups (id, group_name) VALUES 
    (1, 'ADMIN_GROUP'),
    (2, 'USER_GROUP');

-- 设置组权限
INSERT INTO group_authorities (group_id, authority) VALUES 
    (1, 'ROLE_ADMIN'),
    (1, 'ROLE_USER'),
    (2, 'ROLE_USER');

-- 将用户分配到组
INSERT INTO group_members (username, group_id) VALUES 
    ('admin', 1),
    ('user', 2);

-- 设置直接权限（可选）
INSERT INTO authorities (username, authority) VALUES 
    ('admin', 'DIRECT_ADMIN_AUTH');

-- 添加默认的OAuth2客户端
INSERT INTO oauth2_registered_client (
    id,
    client_id,
    client_id_issued_at,
    client_secret,
    client_name,
    client_authentication_methods,
    authorization_grant_types,
    redirect_uris,
    post_logout_redirect_uris,
    scopes,
    client_settings,
    token_settings
) VALUES (
    'ffv-client-static-id',
    'ffv-client',
    CURRENT_TIMESTAMP,
    '{bcrypt}$2a$10$RxycSRXenJ6CeGMP0.LGIOzesA2VwJXBOlmq33t9dn.yU8nX1fqsK',
    'FFV Client',
    'client_secret_basic',
    'authorization_code,refresh_token',
    'http://127.0.0.1:3000/callback,com.ffv.app://oauth2/callback,http://localhost:9000/oauth2-test-callback',
    'http://127.0.0.1:3000/logout,com.ffv.app://oauth2/logout',
    'openid,profile,read,write',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":true,"settings.client.require-authorization-consent":true}',
    '{"@class":"java.util.Collections$UnmodifiableMap",
    "settings.token.reuse-refresh-tokens":true,
    "settings.token.access-token-time-to-live":["java.time.Duration",3600.000000000],
    "settings.token.refresh-token-time-to-live":["java.time.Duration",86400.000000000],
    "settings.token.authorization-code-time-to-live":["java.time.Duration",600.000000000]}'
) ON CONFLICT (id) DO UPDATE SET
    client_secret = EXCLUDED.client_secret,
    client_name = EXCLUDED.client_name,
    client_authentication_methods = EXCLUDED.client_authentication_methods,
    authorization_grant_types = EXCLUDED.authorization_grant_types,
    redirect_uris = EXCLUDED.redirect_uris,
    post_logout_redirect_uris = EXCLUDED.post_logout_redirect_uris,
    scopes = EXCLUDED.scopes,
    client_settings = EXCLUDED.client_settings,
    token_settings = EXCLUDED.token_settings;
