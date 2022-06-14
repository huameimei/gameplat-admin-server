DROP TABLE IF EXISTS activity_turntable;
CREATE TABLE  `activity_turntable` (
  `id` SMALLINT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) DEFAULT NULL COMMENT '活动标题',
  `begin_time` DATETIME DEFAULT NULL COMMENT '活动开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '活动结束时间',
  `status` TINYINT DEFAULT '1' COMMENT '活动状态（0关闭 1开启）',
  `prize_value` TEXT DEFAULT NULL COMMENT '奖品列表值',
  `prize_class` VARCHAR(255) DEFAULT NULL COMMENT '奖品类',
  `prize_count` SMALLINT(10) DEFAULT 5 COMMENT '奖品数量',
  `dml_type` TINYINT DEFAULT 0 COMMENT '打码量类型 0:固定金额 1:百分比',
  `dml_value` VARCHAR(255) DEFAULT NULL COMMENT '打码量值',
  `activity_lobby_id` BIGINT DEFAULT NULL COMMENT '活动大厅关联id',
  `ip_limit` TINYINT DEFAULT '0' COMMENT 'ip限制（0关闭 1开启）',
  `scope_begin` SMALLINT(10) DEFAULT 1 COMMENT '展示范围-开始',
  `scope_end` SMALLINT(10) DEFAULT 100 COMMENT '展示范围-结束',
  `special_effects` VARCHAR(255) DEFAULT NULL COMMENT '转盘特效',
  PRIMARY KEY (`id`),
  KEY `idx_activity_turntable_id` (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

ALTER TABLE `activity_turntable` ADD UNIQUE (`activity_lobby_id`);

CREATE TABLE `member_transfer_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serial_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '批次号 UUID',
  `type` tinyint NOT NULL COMMENT '类型 1：转代理数据；2：用户层级',
  `content` json NOT NULL COMMENT '备份内容',
  `status` tinyint DEFAULT '1' COMMENT '状态1:可用；0:不可用',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '备份人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后更新人',
  `create_time` datetime NOT NULL COMMENT '备份时间',
  `update_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_bak_type` (`type`) USING BTREE,
  KEY `idx_bak_batch_id` (`serial_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='会员转代理/层级记录表';

alter table member modify column game_account varchar(20);
alter table member_loan modify member_balance decimal(20,1) not null DEFAULT(0.0) COMMENT "借款金额/还款金额/回收金额";
alter table member_loan modify loan_money decimal(20,1) not null DEFAULT(0.0) COMMENT "借呗额度";
alter table member_loan modify overdraft_money decimal(20,1) not null DEFAULT(0.0) COMMENT "总欠款金额";
alter table member_loan modify remain_money decimal(20,1) not null DEFAULT(0.0) COMMENT "剩余未还";

update sys_dict_data set dict_value = 60 where dict_type = "TRANS_TYPE" and dict_label = "LOAN_REPAY";

CREATE TABLE `member_transfer_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serial_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '批次号 UUID',
  `type` tinyint NOT NULL COMMENT '类型 1：转代理数据；2：用户层级',
  `content` json NOT NULL COMMENT '备份内容',
  `status` tinyint DEFAULT '1' COMMENT '状态1:可用；0:不可用',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '备份人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后更新人',
  `create_time` datetime NOT NULL COMMENT '备份时间',
  `update_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_bak_type` (`type`) USING BTREE,
  KEY `idx_bak_batch_id` (`serial_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='会员转代理/层级记录表';