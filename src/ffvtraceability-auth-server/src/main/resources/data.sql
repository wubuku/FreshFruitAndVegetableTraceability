-- 清理现有数据
DELETE FROM group_members;
DELETE FROM group_authorities;
DELETE FROM groups;
DELETE FROM authorities;
DELETE FROM users;

-- 创建测试用户
INSERT INTO users (username, password, enabled) VALUES
    ('admin', '{bcrypt}$2a$10$eKBDBSf4DBNzRwbF7fx5IetdKKjqzkYoST0F7Dkro84eRiDTBJYky', true),  -- password=admin
    ('user', '{bcrypt}$2a$10$eKBDBSf4DBNzRwbF7fx5IetdKKjqzkYoST0F7Dkro84eRiDTBJYky', true);   -- password=admin

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
