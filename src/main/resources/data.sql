INSERT INTO USER (USERNAME, PASSWORD, USER_ROLE)
SELECT 'admin',
       '$2a$10$PWs9jCQNvpo0DDK6./YP1.JxQxnTgiXsroKsZ6tDGzkWdWTSP9VJu', --BCrypt password admin
       'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT * FROM USER WHERE USERNAME='admin' AND USER_ROLE='ROLE_ADMIN');