DROP TABLE IF EXISTS users, items;

CREATE TABLE `users`
(
    `id`    long PRIMARY KEY AUTO_INCREMENT,
    `email` varchar(50) unique,
    `name`  varchar(50)
);

CREATE TABLE `items`
(
    `id`          long PRIMARY KEY AUTO_INCREMENT,
    `name`        varchar(50),
    `description` varchar(200),
    `available`   varchar(20),
    `owner_id`    long

);

ALTER TABLE `items`
    ADD FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`);
