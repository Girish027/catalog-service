/*
SQLyog Community v12.4.3 (64 bit)
MySQL - 5.7.13-6-log : Database - catalog
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`catalog` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `catalog`;

/*Table structure for table `stream_info` */

DROP TABLE IF EXISTS `stream_info`;

CREATE TABLE `stream_info` (
  `stream_id` varchar(50) NOT NULL,
  `stream_info` json DEFAULT NULL,
  PRIMARY KEY (`stream_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `stream_info` */

DROP TABLE IF EXISTS `stream_sinks`;

CREATE TABLE `stream_sinks` (
  `stream_id` varchar(50) NOT NULL,
  `sinks_configs` json DEFAULT NULL,
  PRIMARY KEY (`stream_id`),
  CONSTRAINT `stream_sinks_ibfk_1` FOREIGN KEY (`stream_id`) REFERENCES `stream_info` (`stream_id`) ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*Table structure for table `stream_validators` */

DROP TABLE IF EXISTS `stream_validators`;

CREATE TABLE `stream_validators` (
  `stream_id` varchar(50) NOT NULL,
  `validator_configs` json DEFAULT NULL,
  PRIMARY KEY (`stream_id`),
  CONSTRAINT `stream_validators_ibfk_1` FOREIGN KEY (`stream_id`) REFERENCES `stream_info` (`stream_id`) ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

