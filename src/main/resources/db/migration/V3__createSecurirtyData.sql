
insert into users(username, password, enabled)
    values ('admin', '$2a$12$97ERbR/JUbC24KyD7oo3QeKlAHAR8PBjH8CW00KOhItGMbCzehMX6', true);

insert into authorities (name)
    values
        ('ROLE_ADMIN'),
        ('ROLE_AGENT'),
        ('ROLE_MANAGER');

insert into users_authorities (user_id, authority_id)
    values (1, 1);
