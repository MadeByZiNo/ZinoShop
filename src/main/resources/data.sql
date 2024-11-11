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

INSERT INTO user (username, password, nickname, delivery_address, detail_address, reward_points, role)
VALUES ('user', 'password', '김진호', '인천 계양구','510호', 12000, '고객님'),
       ('admin', 'password', '운영자', '인천 강화군','510호', 0, '관리자');

INSERT INTO cart (user_id)
VALUES (1);

INSERT INTO product (name, price, description, thumbnail, remain, category_id, state)
VALUES ('매우트렌디한긴팔옷',10000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',12,2,'판매중'),
       ('매우트렌디한긴팔옷2',3000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',0,2,'품절'),
       ('매우트렌디한긴팔옷3',2000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',11,2,'판매중'),
       ('매우트렌디한긴팔옷4',15000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',6,2,'판매중'),
       ('매우트렌디한반팔옷',10000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',4,3,'판매중'),
       ('매우트렌디한반팔옷2',30000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',55,3,'판매중'),
       ('매우트렌디한반팔옷3',40000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',55,3,'판매중'),
       ('매우긴바지',10000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',3,5,'판매중');

INSERT INTO product_image (product_id, url)
VALUES (1,'https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg'),
       (1,'https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg');


INSERT INTO `order` (external_id, user_id, order_at, delivered_at, name, status, payment_key, total_price, discounted_price, final_price, recipient_name, recipient_address, payment_method, memo, discount_info)
VALUES
('EXT1234567891', 1, '2023-10-01 10:15:30',null, 'Order 1', '결제전', 'PAY12345', 100000, 5000, 95000, 'Recipient A', '123 Main St, City', 'Credit Card', 'Quick delivery please', 'Autumn Sale'),
('EXT1234567892', 1, '2023-10-02 11:15:30',null, 'Order 2', '결제완료', 'PAY12346', 110000, 10000, 100000, 'Recipient B', '456 Park Ave, City', 'Credit Card', 'Leave at the door', 'Winter Sale'),
('EXT1234567893', 1, '2023-10-03 12:15:30',null, 'Order 3', '배송중', 'PAY12347', 150000, 7500, 142500, 'Recipient C', '789 Oak St, City', 'PayPal', 'Handle with care', 'Spring Sale'),
('EXT1234567894', 1, '2023-10-04 13:15:30', '2023-10-08 14:20:15', 'Order 4', '배송완료', 'PAY12348', 300000, 15000, 285000, 'Recipient D', '123 Pine St, City', 'Credit Card', 'Contact me before delivery', 'Summer Sale'),
('EXT1234567895', 1, '2023-10-05 14:15:30',null, 'Order 5', '취소', 'PAY12349', 275000, 20000, 255000, 'Recipient E', '321 Maple St, City', 'Credit Card', 'Leave with neighbor', 'Flash Sale'),
('EXT1234567896', 1, '2023-10-06 15:15:30',null, 'Order 6', '환불', 'PAY12350', 120000, 6000, 114000, 'Recipient F', '654 Elm St, City', 'PayPal', 'Ring the doorbell', 'Spring Sale'),
('EXT1234567897', 1, '2023-10-07 16:15:30',null, 'Order 7', '결제전', 'PAY12351', 80000, 4000, 76000, 'Recipient G', '987 Birch St, City', 'Credit Card', 'Fragile items', 'Autumn Sale'),
('EXT1234567898', 1, '2023-10-08 17:15:30',null, 'Order 8', '결제완료', 'PAY12352', 180000, 1000, 179000, 'Recipient H', '741 Cedar St, City', 'Credit Card', 'Leave at mailbox', 'Winter Sale'),
('EXT1234567899', 1, '2023-10-09 18:15:30',null, 'Order 9', '배송중', 'PAY12353', 180000, 0, 180000, 'Recipient I', '963 Spruce St, City', 'PayPal', 'Urgent delivery', 'Summer Sale'),
('EXT12345678910', 1, '2023-10-10 19:15:30', '2023-10-14 14:20:15', 'Order 10', '배송완료', 'PAY12354', 190000, 9500, 180500, 'Recipient J', '852 Fir St, City', 'Credit Card', 'Ring bell on arrival', 'Autumn Sale');


INSERT INTO order_product (order_id, product_id, quantity, price) VALUES
(1, 1, 5, 10000),
(1, 5, 5, 10000),
(2, 8, 10, 10000),
(2, 3, 5, 2000),
(3, 7, 3, 40000),
(3, 6, 1, 30000),
(4, 6, 10, 30000),
(5, 7, 5, 40000),
(5, 4, 5, 15000),
(6, 7, 3, 40000),
(7, 1, 3, 10000),
(7, 2, 10, 3000),
(8, 8, 12, 10000),
(8, 4, 4, 15000),
(9, 6, 5, 30000),
(9, 2, 10, 3000),
(10, 5, 10, 10000),
(10, 8, 9, 10000);

