INSERT INTO category (name, parent_id)
VALUES ('상의',NULL),
       ('긴팔', '1'),
       ('반팔', '1'),
       ('하의',NULL),
       ('긴바지', '4'),
       ('반바지', '4'),
       ('아우터',NULL),
       ('패딩', '7'),
       ('코트', '7'),
       ('기타',NULL),
       ('모자', '10'),
       ('신발', '10'),
       ('운동화', '12'),
       ('스니커즈', '12');

INSERT INTO cart (id)
VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12);

INSERT INTO user (username, password, nickname, delivery_address, detail_address, reward_points, role, version, cart_id)
VALUES ('user', 'password', '김진호', '인천 계양구','510호', 12000, '고객님', 1, 1),
       ('admin', 'password', '운영자', '인천 강화군','510호', 0, '관리자', 1, 2),
       ('user2', 'password', '부자1', '서울 강남구','210호', 40000, '고객님', 1, 3),
       ('user3', 'password', '부자2', '서울 강남구','210호', 40000, '고객님', 1, 4),
       ('user4', 'password', '부자3', '서울 강남구','210호', 40000, '고객님', 1, 5),
       ('user5', 'password', '부자4', '서울 강남구','210호', 40000, '고객님', 1, 6),
       ('user6', 'password', '부자5', '서울 강남구','210호', 40000, '고객님', 1, 7),
       ('user7', 'password', '부자6', '서울 강남구','210호', 40000, '고객님', 1, 8),
       ('user8', 'password', '부자7', '서울 강남구','210호', 40000, '고객님', 1, 9),
       ('user9', 'password', '부자8', '서울 강남구','210호', 40000, '고객님', 1, 10),
       ('user10', 'password', '부자9', '서울 강남구','210호', 40000, '고객님', 1, 11),
       ('user11', 'password', '부자10', '서울 강남구','210호', 40000, '고객님', 1, 12);



INSERT INTO product (name, price, description, remain, category_id, state, version)
VALUES ('긴팔옷',10000,'입으면 잘생겨짐',12,2,'판매중', 1);

INSERT INTO `order` (external_id, user_id, order_at, delivered_at, name, status, payment_key, total_price, discounted_price, final_price, recipient_name, recipient_address, payment_method, memo, discount_info, coupon_discount_price, reward_points_discount_price, coupon_id)
VALUES
('EXT1234567891', 1, '2025-03-31 10:15:30',null, 'Order 1', '결제전', 'PAY12345', 100000, 5000, 95000, 'Recipient A', '123 Main St, City', 'Credit Card', 'Quick delivery please', 'Autumn Sale', 0, 0, null);

INSERT INTO order_product (order_id, product_id, quantity, price)
VALUES(1, 1, 5, 10000);

INSERT INTO product_image (url, product_id)
VALUES  ('https://zinoshop.s3.us-east-2.amazonaws.com/Product/33.jpg', 1);
