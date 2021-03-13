INSERT INTO email_template (id, created_at, created_by, updated_at, updated_by, content, template_file, title, type)
VALUES (nextval('email_template_seq'), '2021-02-02 23:53:16.000000', 1, '2021-02-02 23:53:22.000000', 1, 'Bạn vừa yêu cầu đổi mật khẩu cho tài khoản [[${username}]]. Mật khẩu của bạn là [[${passwordInPlainText}]].



Mật khẩu này có hiệu lực trong vòng [[${expiredInMinutes}]] phút, vui lòng truy cập vào hệ thống ProStylee để thay đổi password.', null, 'Khôi phục mật khẩu', 'FORGOT_PASSWORD');



INSERT INTO email_template (id, created_at, created_by, updated_at, updated_by, content, template_file, title, type)
VALUES (nextval('email_template_seq'), '2021-02-02 23:53:16.000000', 1, '2021-02-02 23:53:22.000000', 1, 'Chào [[${fullName}]],
Chúc mừng bạn đã đăng ký thành công tài khoản ở ProStylee. Tên tài khoản đăng nhập hệ thống của bạn là [[${username}]].', null, 'Chào mừng [[${fullName}]]', 'WELCOME');

