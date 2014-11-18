CREATE DATABASE multimania;

USE multimania;

CREATE TABLE `user` (
  `id` VARCHAR(255) NOT NULL,
  `last_updated` DATETIME NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `room` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `tag` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `talk` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `from` DATETIME NOT NULL,
  `to` DATETIME NOT NULL,
  `content` LONGTEXT NULL,
  `room_id` INT NULL,
  `isKeynote` BIT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `id_idx` (`id` ASC),
  CONSTRAINT `id`
  FOREIGN KEY (`room_id`)
  REFERENCES `room` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `talk_tag` (
  `talk_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  CONSTRAINT `talk_tag_constraint` UNIQUE ( `talk_id`,`tag_id`),
  INDEX `id_idx` (`talk_id` ASC),
  INDEX `id_idx1` (`tag_id` ASC),
  CONSTRAINT `idtalk`
  FOREIGN KEY (`talk_id`)
  REFERENCES `talk` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idtag`
  FOREIGN KEY (`tag_id`)
  REFERENCES `tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `user_talk` (
  `user_id` VARCHAR(255) NOT NULL,
  `talk_id` INT NOT NULL,
  INDEX `talkid_idx` (`talk_id` ASC),
  INDEX `userid_idx` (`user_id` ASC),
  CONSTRAINT `userid`
  FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `talkid`
  FOREIGN KEY (`talk_id`)
  REFERENCES `talk` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `news` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `img` VARCHAR(255) NULL,
  `short_description` LONGTEXT NULL,
  `long_description` LONGTEXT NULL,
  `importance` INT NULL DEFAULT 0,
  `order` INT NULL DEFAULT 0,
  PRIMARY KEY (`id`));

CREATE TABLE `speaker` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `talk_speaker` (
  `talk_id` INT NOT NULL,
  `speaker_id` INT NOT NULL,
  CONSTRAINT `talk_speaker_constraint` UNIQUE ( `talk_id`,`speaker_id`),
  PRIMARY KEY (`talk_id`, `speaker_id`),
  INDEX `speaker_id_idx` (`speaker_id` ASC),
  CONSTRAINT `talk_id`
  FOREIGN KEY (`talk_id`)
  REFERENCES `talk` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `speaker_id`
  FOREIGN KEY (`speaker_id`)
  REFERENCES `speaker` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


