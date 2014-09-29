/*
SQLyog Ultimate v8.32 
MySQL - 5.0.18-nt : Database - androidserver
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`androidserver` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `androidserver`;

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` int(11) NOT NULL auto_increment,
  `newsid` int(11) default NULL,
  `userid` int(11) default NULL,
  `username` varchar(100) default NULL,
  `content` mediumtext,
  `createtime` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `homeimage` */

DROP TABLE IF EXISTS `homeimage`;

CREATE TABLE `homeimage` (
  `id` int(11) NOT NULL auto_increment,
  `path` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `newsaudio` */

DROP TABLE IF EXISTS `newsaudio`;

CREATE TABLE `newsaudio` (
  `id` int(11) NOT NULL auto_increment,
  `newsid` int(11) default NULL,
  `audiopath` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `newscategory` */

DROP TABLE IF EXISTS `newscategory`;

CREATE TABLE `newscategory` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

/*Table structure for table `newscategoryimage` */

DROP TABLE IF EXISTS `newscategoryimage`;

CREATE TABLE `newscategoryimage` (
  `id` int(11) NOT NULL auto_increment,
  `categoryid` int(11) default NULL,
  `imagepath` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `newscontent` */

DROP TABLE IF EXISTS `newscontent`;

CREATE TABLE `newscontent` (
  `id` int(11) NOT NULL auto_increment,
  `title` varchar(200) default NULL,
  `content` mediumtext,
  `category` int(11) default NULL,
  `viewnum` int(11) default NULL,
  `replynum` int(11) default NULL,
  `comefrom` varchar(100) default NULL,
  `time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

/*Table structure for table `newsimage` */

DROP TABLE IF EXISTS `newsimage`;

CREATE TABLE `newsimage` (
  `id` int(11) NOT NULL auto_increment,
  `newsid` int(11) default NULL,
  `imagepath` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

/*Table structure for table `pushmessage` */

DROP TABLE IF EXISTS `pushmessage`;

CREATE TABLE `pushmessage` (
  `id` int(11) NOT NULL auto_increment,
  `title` varchar(200) default NULL,
  `content` mediumtext,
  `sendtime` datetime default NULL,
  `author` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(100) default NULL,
  `password` varchar(100) default NULL,
  `sex` varchar(20) default NULL,
  `age` int(11) default NULL,
  `address` varchar(100) default NULL,
  `registertime` varchar(50) default NULL,
  `question` varchar(100) default NULL,
  `answer` varchar(100) default NULL,
  `marry` varchar(50) default NULL,
  `hobby` varchar(100) default NULL,
  `email` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
