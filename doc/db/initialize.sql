use jhshop;

DROP TABLE IF EXISTS Review;
DROP TABLE IF EXISTS Inquire_Board;
DROP TABLE IF EXISTS Product_Orders;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Order_Status;
DROP TABLE IF EXISTS Cart;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS Product_Status;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Membership;

-- DDL
CREATE TABLE Membership (
    membership_id INT PRIMARY KEY AUTO_INCREMENT,
    membership_name VARCHAR(50) NOT NULL,
    min_amount INT NOT NULL,
    discount_rate INT NOT NULL
);

CREATE TABLE User (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    delivery_address VARCHAR(255) NOT NULL,
    reward_points INT NOT NULL,
	role VARCHAR(50) NOT NULL,
    membership_id INT NOT NULL,
    FOREIGN KEY (membership_id) REFERENCES Membership(membership_id)
);

CREATE TABLE Product_Status (
    status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(64) NOT NULL
);

CREATE TABLE Category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(64) NOT NULL,
    parent_category_id INT NULL,
    FOREIGN KEY (parent_category_id) REFERENCES Category(category_id)
);

CREATE TABLE Product (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    product_price VARCHAR(255) NOT NULL,
    product_image VARCHAR(255) NOT NULL,
    product_description VARCHAR(255) NOT NULL,
    product_remain INT NOT NULL,
    status_id INT NOT NULL,
    category_id INT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES Category(category_id),
    FOREIGN KEY (status_id) REFERENCES Product_Status(status_id)
);

CREATE TABLE Cart (
    cart_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    product_count INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE Order_Status (
    status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(64) NOT NULL
);

CREATE TABLE Orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    delivery_address VARCHAR(255) NOT NULL,
    purchase_price INT NOT NULL,
    order_date DATE NOT NULL,
    delivery_date DATE NULL,
    status_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (status_id) REFERENCES Order_Status(status_id)
);

CREATE TABLE Product_Orders (
    product_order_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    order_number INT NOT NULL,
    purchase_price INT NOT NULL,
    status_id INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (status_id) REFERENCES Order_Status(status_id)
);

CREATE TABLE Review (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_order_id INT NOT NULL,
    review_images VARCHAR(255) NOT NULL,
    review_description VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (product_order_id) REFERENCES Product_Orders(product_order_id)
);

CREATE TABLE Inquire_Board (
    post_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    post_date DATE NOT NULL,
    answer TEXT,
    answer_date DATE,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

-- DML

-- Membership 테이블 데이터 추가
INSERT INTO Membership (membership_name, min_amount, discount_rate) VALUES
('브론즈', 0, 0),
('실버', 50000, 3),
('골드', 100000, 5),
('플래티넘', 300000, 10);

-- Product_Status 테이블 데이터 추가
INSERT INTO Product_Status (status_name) VALUES
('정상'),
('세일'),
('품절');

-- Category 테이블 데이터 추가
INSERT INTO Category (category_name, parent_category_id) VALUES
('아우터', null),
('상의', null),
('하의', null),
('패딩', 1),
('코트', 1),
('긴팔', 2),
('반팔', 2),
('맨투맨', 6),
('후드티', 6),
('셔츠', 6),
('반팔티',7),
('반팔셔츠', 7),
('데님바지', 3),
('면바지', 3),
('트레이닝바지', 3);

-- Product 테이블 데이터 추가
INSERT INTO Product (product_name , product_price ,product_image ,product_description ,product_remain ,status_id ,category_id) VALUES
('NJ1DQ55A 남성 1996 에코 눕시 자켓', 399999, 'https://image.msscdn.net/images/goods_img/20220412/2482269/2482269_1_500.jpg', '노스페이스의 패딩', 11, 1, 4),
('NBNPD42913 / UNI 액티브 숏 구스 다운자켓 (BLACK)', 239000, 'https://image.msscdn.net/images/goods_img/20210929/2153567/2153567_16992556306646_500.jpg', '뉴발란스의 패딩', 25, 1, 4),
('다크베이지 핸드메이드 세미 오버핏 더블 코트 IECO2F002I3', 325000, 'https://image.msscdn.net/images/goods_img/20220913/2787789/2787789_1_500.jpg', '일꼬르소의 코트', 8, 1, 5),
(' 125CM 슈퍼 롱 오버핏 더블 코트', 186000, 'https://image.msscdn.net/images/goods_img/20211104/2217531/2217531_16722043899054_500.jpg', '어반드레스의 코트', 70, 2, 5),
('(23FW) EMBROIDERY UNIVERSITY DAN CREWNECK GRAY', 69000, 'https://image.msscdn.net/images/goods_img/20231102/3679136/3679136_16989050113141_500.jpg', 'YALE의 맨투맨', 58, 2, 8),
('OUR 1989 맨투맨 (STMSTD-0004)', 59900, 'https://image.msscdn.net/images/goods_img/20221220/2990325/2990325_16947551430787_500.jpg', '1989의 맨투맨', 62, 2, 8),
('오버핏 그래피티 로고 후디-멜란지그레이', 64000, 'https://image.msscdn.net/images/goods_img/20221101/2913656/2913656_2_500.jpg', '필루미네이트의 후드티', 76, 1, 9),
('SCRIPT LOGO HOODIE GREEN(MG2CFMM426A)', 74000, 'https://image.msscdn.net/images/goods_img/20220804/2697250/2697250_1_500.jpg', '어딘가의 패딩', 54, 2, 9),
('플란넬 버튼다운 셔츠_MSC4WC1901', 39900, 'https://image.msscdn.net/images/goods_img/20220829/2748365/2748365_16986431662431_500.jpg', '탑텐의 긴팔셔츠', 105, 2, 10),
(' 1301 무지 반팔티', 20900, 'https://image.msscdn.net/images/goods_img/20151104/274855/274855_17019281560623_500.jpg', '트리플에이의 반팔 티셔츠', 17, 2, 11),
('레이어드 크루 넥 반팔 티셔츠', 16900, 'https://image.msscdn.net/images/goods_img/20210520/1958994/1958994_2_500.jpg', '무탠다드의 반팔 티셔츠', 89, 2, 11),
('[2PACK] 021541 레이어드 반팔 티셔츠', 24800, 'https://image.msscdn.net/images/goods_img/20210225/1815503/1815503_16974346903486_500.jpg', '지노다오의 반팔 티셔츠', 75, 1, 11),
('시티보이 보트자수 옥스포드 오버핏 반팔셔츠', 47000, 'https://image.msscdn.net/images/goods_img/20210514/1951474/1951474_2_500.jpg', '트릴리온의 반팔 셔츠', 37, 1, 12),
('와이드 데님 팬츠 [미디엄 블루]', 69800, 'https://image.msscdn.net/images/goods_img/20230816/3467143/3467143_16998408414518_500.jpg', 'AGAINST ALL ODDS의 바지', 107, 2, 13),
('이지 와이드 데님 팬츠 그레이', 52800, 'https://image.msscdn.net/images/goods_img/20210826/2092852/2092852_17022592261039_500.jpg', 'GLW의 바지', 68, 2, 13),
('코튼 밴딩 와이드 팬츠', 49000, 'https://image.msscdn.net/images/goods_img/20230224/3107234/3107234_16786965599916_500.jpg', '토피의 바지', 80, 2, 14),
('원턱 와이드 스웨트팬츠 그레이', 52000, 'https://image.msscdn.net/images/goods_img/20210906/2112059/2112059_1_500.jpg', ' GAKKAI UNIONS의 바지', 3, 1, 15),
(' 카고 조거 트레이닝 팬츠_블랙', 60000, 'https://image.msscdn.net/images/goods_img/20220907/2778712/2778712_17000985549164_500.jpg', 'URBANAGE 패딩', 36, 2, 15);


-- Order_Status 테이블 데이터 추가
INSERT INTO Order_Status (status_name) VALUES
('상품 준비중'),
('도착'),
('구매확정'),
('환불');

