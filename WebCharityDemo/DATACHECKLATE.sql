DROP DATABASE IF EXISTS web_charity ;

CREATE DATABASE web_charity;

USE web_charity;

CREATE TABLE `role` (
	`id` INT(11)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`role_name` VARCHAR(255) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
	`id` INT(11)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`address` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(255) NOT NULL,
    `note` VARCHAR(255) DEFAULT NULL,
    `password` VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(255) DEFAULT NULL,
    `status` int(255),
    `user_name` VARCHAR(255) NOT NULL ,
    `created`DATE ,
    `role_id` int(11),
     CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `donation` (
	`id` INT(11)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`code` VARCHAR(25) ,
    `local` VARCHAR(255),
    `description` VARCHAR(255) DEFAULT NULL,
    `end_date` DATE DEFAULT NULL,
    `money` BIGINT DEFAULT NULL,
    `name` VARCHAR(255) DEFAULT NULL,
    `organization_name` VARCHAR(255) DEFAULT NULL,
    `phone_number` VARCHAR(255) DEFAULT NULL ,
    `start_date` DATE DEFAULT NULL,
    `status` INT(11)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `user_donation` (
	`id` INT(11)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`created` DATE ,
    `money`	BIGINT ,
    `name` VARCHAR(255),
    `status` INT(11) DEFAULT 0,
    `text` VARCHAR(255),
	`donation_id` INT(11),
	`user_id` INT(11),
	FOREIGN KEY (`donation_id`) REFERENCES `donation` (`id`),
	FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


INSERT INTO `web_charity`.`role`(`role_name`) VALUES('ADMIN'),('USER');

INSERT INTO `web_charity`.`donation` (`code`, `local`, `description`, `end_date`, `money`, `name`, `organization_name`, `phone_number`, `start_date`, `status`) 
VALUES 
('BA1', 'Miền Trung' 	, 'BÃO LŨ MIỀNG TRUNG'	, '2020-05-19', '21000000'	, 'BÃO LŨ'		, 'HỘI VÌ NGƯỜI NGHÈO VIỆT NAM'	, '0905000888',	'2020-03-19', '1'),
('BCB2', 'Cao Bằng' 	, 'BÃO LŨ MIỀN TÂY'		, '2019-05-19', '0'			, 'BÃO'			, 'HỘI NGƯỜI GIẦU VIỆT NAM'		, '0903000881', '2020-04-19', '1'),
('AB3', 'Nghệ An' 		, 'BÃO LŨ MIỀN NÚI'		, '2021-05-19', '0'			, 'LŨ BÁO'		, 'HỘI NGƯỜI TRUNG VIỆT NAM'	, '0903000882', '2020-05-19', '1'),
('XLB4', 'Huế' 			, 'BÃO LŨ MIỀN NÚI'		, '2021-05-19', '0'			, 'BÁO LŨ'		, 'HỘI NGƯỜI TRUNG VIỆT NAM'	, '0903000882',	'2020-06-19', '2'),
('YLB5', 'Quảng Bỉnh' 	, 'BÃO LŨ MIỀN NÚI'		, '2021-05-19', '0'			, 'LŨ'			, 'HỘI NGƯỜI CAO TUỔI'			, '0903000883',	'2020-07-19', '3'),
('ZLB6', 'Quảng Trị' 	, 'BÃO LŨ MIỀN NÚI'		, '2022-05-19', '0'			, 'LŨ'			, 'HỘI NGƯỜI TRUNG NAM'			, '0903000883',	'2020-08-19', '3'),
('DDB7', 'Đăk Lắk' 		, 'BÃO LŨ MIỀN NÚI'		, '2022-05-19', '0'			, 'LŨ'			, 'HỘI NGƯỜI VIỆT NAM'			, '0903000884', '2020-09-19', '3'),
('EDB8', 'Nghệ Tĩnh' 	, 'Xạc Lỡ'				, '2023-05-19', '0'			, 'LŨ'			, 'HỘI NGƯỜI VÌ VIỆT NAM'		, '0903000884',	'2020-010-19', '2')

,
--
('EDC9', 'Nghệ Tĩnh' 	, 'Gió Mạnh'			, '2020-05-19', '0', 'LŨ'			, 'NHÓM NGƯỜI VIỆT NAM'	, '0903000884',	'2020-05-19', '2'), 
('OR10', 'Nghệ An' 		, 'Lũ Lụt'				, '2020-05-19', '0', 'LŨ'			, 'GROUP CÔNG KHAI TRUNG VIỆT NAM'	, '0903000884', '2020-05-19', '0'),
('BZC11', 'Nghệ An' 	, 'BÃO LŨ MIỀN NÚI'		, '2020-05-19', '0', 'LŨ'			, 'GROUP CÔNG VEO VIỆT NAM'	, '0903000884', '2020-05-19', '0'),
('KGC12', 'Nghệ An' 	, 'BÃO LŨ MIỀN NÚI'		, '2020-05-19', '0', 'LŨ'			, 'HỘI NGƯỜI TRUNG VIỆT NAM'	, '0903000884', '2020-05-19', '0'),
('CD13', 'Nghệ An' 		, 'BÃO LŨ MIỀN NÚI'		, '2020-05-19', '0', 'LŨ'			, 'HỘI NGƯỜI TRUNG VIỆT NAM'	, '0905000885', '2020-05-19', '0'),
('RL14', 'Nghệ An' 		, 'BÃO LŨ MIỀN NÚI'		, '2020-05-19', '0', 'LŨ'			, 'HỘI NGƯỜI TRUNG VIỆT NAM'	, '0905000886', '2020-05-19', '0')
;

INSERT INTO `web_charity`.`user` (`address`, `email`, `full_name`, `note`, `password`, `phone_number`, `status`,`user_name`, `created`,`role_id`) 
VALUES 
('10 Hùng Vương'	,'admin.admin@gmail.com', 'Nguyễn Văn Admin', 'Người làm thuê siêu OT '	, 'admin01'	, '0763717175', '1', 'admin01', '2019-02-1', '1'),
('26 Lê Duẫn'		,'user01@gmail.com' 	, 'Nguyễn Thị Tin'	, 'Siêu Driver'				, 'user01'	, '0763717171', '1', 'user01', 	'2020-05-19', '2'),
('30 Trên Trời'		,'user021@gmail.com' 	, 'Nguyễn Thị Kim'	, 'Siêu Tạch '				, 'user02'	, '0763717173', '1', 'user02', 	'2020-05-19', '2'),
('40 Dưới Đất'		,'user023@gmail.com' 	, 'Nguyễn Thị Tín'	, 'Siêu Tay Gẫy'			, 'user03'	, '0763717174', '1', 'user03', 	'2020-05-19', '2'),
('50 Dưới Đông'		,'user024@gmail.com' 	, 'Nguyễn Thị Son'	, 'Siêu Thức Khuya'			, 'user04'	, '0763717175', '1', 'user04', 	'2020-05-19', '2'),
('60 Dưới Tây'		,'user025@gmail.com' 	, 'Nguyễn Thị Tây'	, 'Siêu'					, 'user05'	, '0763717175', '0', 'user05', 	'2020-05-19', '2'),
('70 Dưới Nam'		,'user026@gmail.com' 	, 'Nguyễn Thị Nam'	, 'Siêu'					, 'user06'	, '0763717175', '1', 'user06', 	'2020-05-19', '2'),
('81 Dưới Bắc'		,'user027gmail.com' 	, 'Nguyễn Thị Bắc'	, 'Siêu Nhiều Tiền'			, 'test123'	, '0763717175', '1', 'user44', 	'2020-05-19', '2'),
('92 Dưới Đông'		,'user028@gmail.com' 	, 'Nguyễn Lan Nam'	, 'user07'					, 'test123'	, '0763717175', '1', 'user07',	'2020-05-19', '2')

,
--

('13 Dưới Đông'		,'user09@gmail.com' 	, 'Nguyễn Lan Bắc'	, 'user08'					, 'test123'	, '0763717175', '1', 'user08',	'2020-05-19', '2'),
('14 Dưới Tây'		,'user10@gmail.com' 	, 'Nguyễn Tây Bắc'	, 'user9'					, 'test123'	, '0763717175', '1', 'user09',	'2020-05-19', '2'),
('15 Dưới Tây'		,'user11@gmail.com' 	, 'Nguyễn Tây Nam'	, 'user10'					, 'test123'	, '0763717175', '1', 'user10',	'2020-05-19', '2')
;


INSERT INTO `web_charity`.`user_donation`(`created` ,`money`,`name`,  `status` , `text`,`donation_id` ,`user_id`) VALUES
('2020-10-7','20000000'		,'Nguyễn Thị Tin','1','Ủng Hộ','1','2'),
('2021-3-12','1000000'		,'Nguyễn Thị Tín','1','Ủng Hộ','1','3'),
('2021-10-7','3000000'		,'Nguyễn Thị Kim','0','Ủng Hộ','1','3'),
('2022-10-7','10000'		,'Nguyễn Thị Tín','0','Ủng Hộ','1','4'),
('2023-10-7','4000000'		,'Nguyễn Thị Son','0','Ủng Hộ','1','5')

,
--
('2023-10-7','5000000'		,'Nguyễn Thị Tin','0','Ủng Hộ','1','2'),
('2024-10-7','10000000'		,'Nguyễn Thị Tin','0','Ủng Hộ','1','2'),
('2024-10-7','10000000'		,'Nguyễn Thị Tin','0','Ủng Hộ','1','2'),
('2024-10-7','10000000'		,'Nguyễn Thị Tin','0','Ủng Hộ','1','2'),
('2024-10-7','10000000'		,'Nguyễn Thị Tin','0','Ủng Hộ','1','2');

use `web_charity`;
SELECT * FROM `user`;
SELECT * FROM `donation`;
SELECT * FROM `role`;
SELECT * FROM `web_charity`.`user_donation`;

