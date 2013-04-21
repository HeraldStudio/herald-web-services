SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `herald_campus_info` ;
CREATE SCHEMA IF NOT EXISTS `herald_campus_info` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `herald_campus_info` ;

-- -----------------------------------------------------
-- Table `herald_campus_info`.`feed`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `herald_campus_info`.`feed` ;

CREATE  TABLE IF NOT EXISTS `herald_campus_info`.`feed` (
  `uuid` VARCHAR(40) NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `url` TINYTEXT NOT NULL ,
  `link` VARCHAR(128) NOT NULL ,
  `updated` DATETIME NOT NULL ,
  PRIMARY KEY (`uuid`) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `herald_campus_info`.`entry`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `herald_campus_info`.`entry` ;

CREATE  TABLE IF NOT EXISTS `herald_campus_info`.`entry` (
  `uuid` VARCHAR(40) NOT NULL ,
  `feed_uuid` VARCHAR(40) NOT NULL ,
  `title` TINYTEXT NOT NULL ,
  `url` VARCHAR(128) NOT NULL ,
  `updated` DATETIME NOT NULL ,
  `summary` TEXT NOT NULL ,
  PRIMARY KEY (`uuid`) ,
  INDEX `feed_uuid_fk_idx` (`feed_uuid` ASC) )
ENGINE = MyISAM;

USE `herald_campus_info` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
