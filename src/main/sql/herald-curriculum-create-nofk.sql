SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `herald_curriculum` ;
CREATE SCHEMA IF NOT EXISTS `herald_curriculum` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `herald_curriculum` ;

-- -----------------------------------------------------
-- Table `herald_curriculum`.`student`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `herald_curriculum`.`student` ;

CREATE  TABLE IF NOT EXISTS `herald_curriculum`.`student` (
  `card_no` INT UNSIGNED NOT NULL ,
  `student_no` VARCHAR(8) NOT NULL ,
  `name` VARCHAR(20) NOT NULL ,
  PRIMARY KEY (`card_no`) ,
  UNIQUE INDEX `student_no_UNIQUE` (`student_no` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `herald_curriculum`.`course`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `herald_curriculum`.`course` ;

CREATE  TABLE IF NOT EXISTS `herald_curriculum`.`course` (
  `course_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(20) NOT NULL ,
  `lecturer` VARCHAR(20) NOT NULL ,
  `credit` FLOAT NOT NULL ,
  `week_from` TINYINT UNSIGNED NOT NULL ,
  `week_to` TINYINT UNSIGNED NOT NULL ,
  PRIMARY KEY (`course_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `herald_curriculum`.`select`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `herald_curriculum`.`select` ;

CREATE  TABLE IF NOT EXISTS `herald_curriculum`.`select` (
  `select_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `card_no` INT UNSIGNED NOT NULL ,
  `course_id` INT UNSIGNED NOT NULL ,
  `term` VARCHAR(7) NOT NULL ,
  PRIMARY KEY (`select_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `herald_curriculum`.`attend`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `herald_curriculum`.`attend` ;

CREATE  TABLE IF NOT EXISTS `herald_curriculum`.`attend` (
  `attend_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `select_id` INT UNSIGNED NOT NULL ,
  `day` ENUM('MON','TUE','WED','THU','FRI','SAT','SUN') NOT NULL ,
  `strategy` ENUM('NONE','ODD','EVEN') NOT NULL ,
  `place` VARCHAR(25) NOT NULL ,
  `period_from` TINYINT NOT NULL ,
  `period_to` TINYINT NOT NULL ,
  PRIMARY KEY (`attend_id`) )
ENGINE = InnoDB;

USE `herald_curriculum` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
