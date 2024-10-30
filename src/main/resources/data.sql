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
VALUES ('user', 'password', '김진호', '인천 계양구','50010호', 12000, '고객님'),
       ('user2', 'password', '황대영', '인천 연수구','50010호', 0, 'VIP고객님'),
       ('user3', 'password', '마정우', '인천 계양구','50010호', 0, 'VIP고객님');

INSERT INTO cart (user_id)
VALUES (1);
INSERT INTO product (name, price, description, thumbnail, remain, category_id, state)
VALUES ('매우트렌디한긴팔옷',10000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',12,2,'판매중'),
       ('매우트렌디한긴팔옷2',3000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',0,2,'세일중'),
       ('매우트렌디한긴팔옷3',2000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',11,2,'판매중'),
       ('매우트렌디한긴팔옷4',15000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',6,2,'판매중'),
       ('매우트렌디한반팔옷',10000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',4,3,'판매중'),
       ('매우트렌디한반팔옷2',30000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',55,3,'판매중'),
       ('매우트렌디한반팔옷3',40000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',55,3,'판매중'),
       ('매우긴바지',10000,'입으면 잘생겨짐','https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg',3,5,'판매중');

INSERT INTO product_image (product_id, url)
VALUES (1,'https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg'),
       (1,'https://zinoshop.s3.us-east-2.amazonaws.com/Product/ef03b0cd-463c-4bb3-9445-9138025f4f03_1.jpg');

