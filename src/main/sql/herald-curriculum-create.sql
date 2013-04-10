SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `herald_curriculum` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `herald_curriculum` ;

-- -----------------------------------------------------
-- Table `herald_curriculum`.`student`
-- -----------------------------------------------------
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
CREATE  TABLE IF NOT EXISTS `herald_curriculum`.`select` (
  `select_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `card_no` INT UNSIGNED NOT NULL ,
  `course_id` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`select_id`) ,
  INDEX `student_fk_idx` (`card_no` ASC) ,
  INDEX `course_fk_idx` (`course_id` ASC) ,
  CONSTRAINT `student_fk`
    FOREIGN KEY (`card_no` )
    REFERENCES `herald_curriculum`.`student` (`card_no` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `course_fk`
    FOREIGN KEY (`course_id` )
    REFERENCES `herald_curriculum`.`course` (`course_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `herald_curriculum`.`attend`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `herald_curriculum`.`attend` (
  `attend_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `select_id` INT UNSIGNED NOT NULL ,
  `day` ENUM('MON','TUE','WED','THU','FRI','SAT','SUN') NOT NULL ,
  `strategy` ENUM('NONE','ODD','EVEN') NOT NULL ,
  `place` VARCHAR(25) NOT NULL ,
  `period_from` TINYINT NOT NULL ,
  `period_to` TINYINT NOT NULL ,
  PRIMARY KEY (`attend_id`) ,
  INDEX `select_fk_idx` (`select_id` ASC) ,
  CONSTRAINT `select_fk`
    FOREIGN KEY (`select_id` )
    REFERENCES `herald_curriculum`.`select` (`select_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `herald_curriculum` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
