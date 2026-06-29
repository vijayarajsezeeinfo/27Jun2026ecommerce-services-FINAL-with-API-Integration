/*
SQLyog Community v13.3.1 (64 bit)
MySQL - 8.0.46 : Database - ecommerce_client
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ecommerce_client` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `ecommerce_client`;

/*Table structure for table `products` */

DROP TABLE IF EXISTS `products`;

CREATE TABLE `products` (
  `code` varchar(30) NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(300) NOT NULL,
  `price` decimal(12,2) NOT NULL,
  `brand_code` varchar(30) NOT NULL,
  `category_code` varchar(30) NOT NULL,
  `namespace_code` varchar(30) NOT NULL,
  `active_flag` tinyint NOT NULL DEFAULT '1',
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `products` */

insert  into `products`(`code`,`name`,`description`,`price`,`brand_code`,`category_code`,`namespace_code`,`active_flag`,`updated_at`) values 
('PRODUCT12880','Galaxy s24 Pro ultra ','can your phone do this?',190000.00,'BRAND22697','CAT78431','AMZN001',1,'2026-06-27 17:39:27'),
('PRODUCT22634','xperia','real phone',40000.00,'BRAND68503','CAT78431','AMZN001',1,'2026-06-27 17:39:27'),
('PRODUCT31177','xperia 2','real phone',48000.00,'BRAND68503','CAT78431','AMZN001',1,'2026-06-27 17:39:27'),
('PRODUCT38227','bravia','real tv to bring theater for your home.',55000.00,'BRAND68503','CAT78431','AMZN001',1,'2026-06-27 17:39:27'),
('PRODUCT55653','Galaxy s25','can your phone do this?',20000.00,'BRAND22697','CAT78431','AMZN001',1,'2026-06-27 17:39:27'),
('PRODUCT90194','Galaxy s24 ultra','can your phone do this?',180000.00,'BRAND22697','CAT78431','AMZN001',1,'2026-06-27 17:39:27');