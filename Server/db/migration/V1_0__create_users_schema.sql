CREATE TABLE IF NOT EXISTS `USERS` (

    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(30),
    `hashed_password` varchar(256)

)ENGINE=InnoDB DEFAULT CHARSET=UTF8;