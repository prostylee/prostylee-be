INSERT INTO email_template (id, created_at, created_by, updated_at, updated_by, content, template_file, title, type)
VALUES (nextval('email_template_seq'), '2021-02-02 23:53:16.000000', 1, '2021-02-02 23:53:22.000000', 1,
        'Chào [[${fullName}]],
        Đơn hàng của bạn đã được tạo thành công. Mã đơn hàng là [[${code}]].',
        null, '[ProStylee] Đơn hàng đã tạo thành công', 'CUSTOMER_NEW_ORDER');