
INSERT INTO user_entity(id, email, encrypted_password, account_enabled)
    VALUES (RANDOM_UUID(), 'sarah@Conner.com',
            '{bcrypt}\0442a\04410\0447R3KvnidSbUCatBBZ.gEEe4FtvZyUwQM6W7LyImwmPUQKB92x6J66', 1);

INSERT INTO user_roles(user_id, role_id)
    SELECT (SELECT id FROM user_entity WHERE user_entity.email = 'sarah@Conner.com') as user_id,
           (SELECT id FROM role_entity WHERE role_entity.name = 'ADMIN_ROLE')        as role_id
;
