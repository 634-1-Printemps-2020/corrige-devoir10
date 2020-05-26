CREATE TABLE `access_event` (
  `event_id` int(11) NOT NULL,
  `user_login` varchar(20) DEFAULT NULL,
  `event_etat` varchar(10) NOT NULL,
  `event_date` date NOT NULL,
  `event_message` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `access_event`
  ADD PRIMARY KEY (`event_id`),
  ADD KEY `user_login` (`user_login`);

ALTER TABLE `access_event`
  MODIFY `event_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

ALTER TABLE `access_event`
  ADD CONSTRAINT `access_event_ibfk_1` FOREIGN KEY (`user_login`) REFERENCES `user_account` (`user_name`);
COMMIT;
