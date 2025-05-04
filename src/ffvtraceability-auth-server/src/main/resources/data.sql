-- 清理现有数据
DELETE FROM group_members;
DELETE FROM group_authorities;
DELETE FROM groups;
DELETE FROM authorities;
DELETE FROM users;

-- 重置组 ID 序列
ALTER SEQUENCE groups_id_seq RESTART WITH 1;

-- 创建用户组（不再手动指定 ID）
INSERT INTO groups (group_name, enabled) VALUES 
    ('ADMIN_GROUP', true),
    ('USER_GROUP', true);

-- 我们使用了 JDBC 来存储 session，在测试阶段，我们自动清理 session 表中的数据！
DELETE FROM SPRING_SESSION_ATTRIBUTES;
DELETE FROM SPRING_SESSION;

-- 创建特殊的基础权限用户
INSERT INTO users (username, password, enabled, password_change_required, first_login, password_last_changed,temp_password_last_generated) VALUES
    ('*', '{bcrypt}$2a$10$eKBDBSf4DBNzRwbF7fx5IetdKKjqzkYoST0F7Dkro84eRiDTBJYky', true, false, false, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

-- 创建测试用户
INSERT INTO users (username, password, enabled, password_change_required, first_login, password_last_changed,temp_password_last_generated) VALUES
    ('admin', '{bcrypt}$2a$10$eKBDBSf4DBNzRwbF7fx5IetdKKjqzkYoST0F7Dkro84eRiDTBJYky', true, false, false, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),  -- password=admin
    ('user', '{bcrypt}$2a$10$eKBDBSf4DBNzRwbF7fx5IetdKKjqzkYoST0F7Dkro84eRiDTBJYky', true, true, true, null,CURRENT_TIMESTAMP);   -- password=admin

-- 给 admin 用户添加 ROLE_ADMIN 权限（auth server 使用这个权限对特权操作进行保护）
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');

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

-- 删除原来的特殊基础权限用户相关的数据
DELETE FROM authorities WHERE username = '*';
DELETE FROM users WHERE username = '*';

-- 添加基础权限
INSERT INTO authority_definitions (authority_id, description, enabled) VALUES 
    ('Vendors_Read', 'Permission to read vendors', true),
    ('Vendors_Create', 'Permission to create vendors', true),
    ('Vendors_Update', 'Permission to update vendors', true),
    ('Vendors_Disable', 'Permission to disable vendors', true),
    ('Items_Read', 'Permission to read items', true),
    ('Items_Create', 'Permission to create items', true),
    ('Items_Update', 'Permission to update items', true),
    ('Items_Disable', 'Permission to disable items', true),
    ('Warehouses_Read', 'Permission to read warehouses', true),
    ('Warehouses_Create', 'Permission to create warehouses', true),
    ('Warehouses_Update', 'Permission to update warehouses', true),
    ('Warehouses_Disable', 'Permission to disable warehouses', true),
    ('Locations_Read', 'Permission to read locations', true),
    ('Locations_Create', 'Permission to create locations', true),
    ('Locations_Update', 'Permission to update locations', true),
    ('Locations_Disable', 'Permission to disable locations', true),
    ('Procurement_Read', 'Permission to read procurement', true),
    ('Procurement_Create', 'Permission to create procurement', true),
    ('Procurement_Update', 'Permission to update procurement', true),
    ('Procurement_RequestUpdates', 'Permission to request updates for procurement', true),
    ('Procurement_Cancel', 'Permission to cancel procurement', true),
    ('Procurement_ApproveUpdates', 'Permission to approve procurement updates', true),
    ('Receiving_Read', 'Permission to read receiving', true),
    ('Receiving_Create', 'Permission to create receiving', true),
    ('Receiving_Update', 'Permission to update receiving', true),
    ('Receiving_RequestUpdates', 'Permission to request updates for receiving', true),
    ('Receiving_ApproveUpdates', 'Permission to approve receiving updates', true),
    ('QA_Read', 'Permission to read quality assurance', true),
    ('QA_Create', 'Permission to create quality assurance', true),
    ('Users_Read', 'Permission to read users', true),
    ('Users_Create', 'Permission to create users', true),
    ('Users_Update', 'Permission to update users', true),
    ('Users_Disable', 'Permission to disable users', true),
    ('Roles_Read', 'Permission to read roles', true),
    ('Roles_Create', 'Permission to create roles', true),
    ('Roles_Update', 'Permission to update roles', true),
    ('Roles_Disable', 'Permission to disable roles', true),
    ('Basedata', 'Permission to access base data', true),
    ('Vendors', 'Permission to access vendors module', true),
    ('Items', 'Permission to access items module', true),
    ('Warehouses', 'Permission to access warehouses module', true),
    ('Locations', 'Permission to access locations module', true),
    ('Procurement', 'Permission to access procurement module', true),
    ('Receiving', 'Permission to access receiving module', true),
    ('QA', 'Permission to access quality assurance module', true),
    ('Users', 'Permission to access users module', true),
    ('Roles', 'Permission to access roles module', true);

-- 为测试用户添加一些初始权限
INSERT INTO authorities (username, authority) VALUES 
    ('user', 'Vendors_Read'),
    ('user', 'Vendors_Create'),
    ('user', 'Warehouses_Create');

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
    'https://fp.ablueforce.com/callback,http://127.0.0.1:3000/callback,com.ffv.app://oauth2/callback,http://localhost:9000/oauth2-test-callback,http://localhost:1023/api/index.html',
    'http://127.0.0.1:3000/logout,com.ffv.app://oauth2/logout',
    'openid,profile',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":true,"settings.client.require-authorization-consent":false}',
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

