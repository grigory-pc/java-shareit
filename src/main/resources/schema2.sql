DROP TABLE IF EXISTS users, items, bookings, requests, comments;

CREATE TABLE IF NOT EXISTS `users`
(
    `id`    long PRIMARY KEY AUTO_INCREMENT,
    `name`  varchar(50),
    `email` varchar(50) unique
    );

CREATE TABLE IF NOT EXISTS `items`
(
    `id`          long PRIMARY KEY AUTO_INCREMENT,
    `name`        varchar(50),
    `description` varchar(200),
    `available`   varchar(20),
    `owner_id`    long
--     `request_id`  long
    );

CREATE TABLE IF NOT EXISTS `bookings`
(
    `id`         long PRIMARY KEY AUTO_INCREMENT,
    `start_date` datetime,
    `end_date`   datetime,
    `status`     varchar(20),
    `item_id`    long,
    `booker_id`  long
    );

CREATE TABLE IF NOT EXISTS `requests`
(
    `id`           long PRIMARY KEY AUTO_INCREMENT,
    `description`  varchar(200),
    `requestor_id` long
    );

CREATE TABLE IF NOT EXISTS `comments`
(
    `id`        long PRIMARY KEY AUTO_INCREMENT,
    `text`      varchar(200),
    `item_id`   long,
    `author_id` long
    );

ALTER TABLE `items`
    ADD FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`);

-- ALTER TABLE `items`
--     ADD FOREIGN KEY (`request_id`) REFERENCES `requests` (`id`);

ALTER TABLE `bookings`
    ADD FOREIGN KEY (`item_id`) REFERENCES `items` (`id`);

ALTER TABLE `bookings`
    ADD FOREIGN KEY (`booker_id`) REFERENCES `users` (`id`);

ALTER TABLE `requests`
    ADD FOREIGN KEY (`requestor_id`) REFERENCES `users` (`id`);

ALTER TABLE `comments`
    ADD FOREIGN KEY (`item_id`) REFERENCES `items` (`id`);

ALTER TABLE `comments`
    ADD FOREIGN KEY (`author_id`) REFERENCES `users` (`id`);
