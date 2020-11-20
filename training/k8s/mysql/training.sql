create DATABASE `training`;
use `training`;


drop table IF EXISTS `todo`;
create TABLE `todo` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(10240) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` int(10) unsigned NOT NULL DEFAULT '0',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COMMENT='代办项目';
