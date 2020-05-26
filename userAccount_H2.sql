CREATE TABLE IF NOT EXISTS USER_ACCOUNT(
  `user_name` VARCHAR(20) NOT NULL,
  `user_password` VARCHAR(20) NOT NULL,
  `is_active` TINYINT NOT NULL,
  `expiration_date` DATE NOT NULL,
  `user_roles` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`user_name`))
