CREATE TABLE `issues` (
  `id` int(11),
  `summary` varchar(255),
  `description` varchar(255),
  `priority` int(11) DEFAULT 1,
  `created_at` datetime,
  `closed_at` datetime,
  `created_by` varchar(50),
  `closed_by` varchar(50),
  `project_id` int(11),
  PRIMARY KEY (`id`)
);

INSERT INTO issues(id, closed_at, closed_by, project_id) VALUES
(1, NULL, NULL, 1),
(2, NULL, NULL, 1),
(3, NULL, NULL, 1),
(4, NOW(), 'user', 1),
(5, NOW(), 'user', 1);

CREATE TABLE `projects` (
  `id` int(11),
  `title` varchar(255),
  `description` varchar(255),
  `created_by` varchar(50),
  `created_at` datetime,
  PRIMARY KEY (`id`)
);

INSERT INTO projects(id) VALUES (1);

CREATE TABLE `users` (
  `username` varchar(50),
  `password` char(68),
  `enabled` tinyint(1) DEFAULT 1,
  `email` varchar(50),
  `created_at` datetime,
  PRIMARY KEY (`username`)
);

INSERT INTO users(username) VALUES ('user');

CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `roles` VALUES (1,'ROLE_USER'),(2,'ROLE_ADMIN'),(3,'ROLE_GUEST');

CREATE TABLE `users_roles` (
  `user_id` varchar(50) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `users_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`username`),
  CONSTRAINT `users_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
);

INSERT INTO `users_roles` VALUES ('user',1);