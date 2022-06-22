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

INSERT INTO `gameplat_center`.`sys_dict_data` (`id`, `dict_name`, `dict_label`, `dict_value`, `dict_type`, `dict_sort`, `css_class`, `list_class`, `is_visible`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (668, '', 'DIRECT_CHARGE', '{\"auditRemarks\":\"余额审核中！！！\",\"discountDmlMultiple\":1,\"discountPercentage\":3,\"discountType\":\"8080\",\"dmlFlag\":1,\"levels\":\"1,2,3,100,200,300,500,600,5,888,666,0\",\"normalDmlMultiple\":4,\"pointFlag\":1,\"remarks\":\"测试一下充值回复\",\"skipAuditing\":1}', 'SYSTEM_PARAMETER_CONFIG', 0, '', '', 1, 'N', 1, 'steve', '2022-06-01 19:00:39', 'steve', '2022-06-01 19:00:39', '');

INSERT INTO `gameplat_center`.`sys_menu` (`menu_id`, `menu_name`, `title`, `parent_id`, `parent_name`, `menu_sort`, `url`, `component`, `path`, `menu_type`, `status`, `perms`, `icon`, `visible`, `i_frame`, `cache_flag`, `target`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (901020632, '', '视图', 60108, '', 1, '', '', '', 'F', 1, 'member:loan:view', '', 0, 0, 1, '', 'steve', '2022-05-21 18:06:54', '', NULL, '');
INSERT INTO `gameplat_center`.`sys_menu` (`menu_id`, `menu_name`, `title`, `parent_id`, `parent_name`, `menu_sort`, `url`, `component`, `path`, `menu_type`, `status`, `perms`, `icon`, `visible`, `i_frame`, `cache_flag`, `target`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (901020633, '', '批量回收', 60108, '', 2, '', '', '', 'F', 1, 'member:loan:recycle', '', 0, 0, 1, '', 'steve', '2022-05-21 18:07:11', '', NULL, '');

INSERT INTO `gameplat_center`.`sys_menu` (`menu_id`, `menu_name`, `title`, `parent_id`, `parent_name`, `menu_sort`, `url`, `component`, `path`, `menu_type`, `status`, `perms`, `icon`, `visible`, `i_frame`, `cache_flag`, `target`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (60108, 'borrow', '借呗借款', 601, '', 8, '', 'recharge-manage/borrow/index', '/recharge-manage/borrow', 'C', 1, '', 'config', 0, 0, 1, '', 'steve', '2022-05-21 17:55:12', 'steve', '2022-05-21 18:03:09', '');
INSERT INTO `gameplat_center`.`sys_menu` (`menu_id`, `menu_name`, `title`, `parent_id`, `parent_name`, `menu_sort`, `url`, `component`, `path`, `menu_type`, `status`, `perms`, `icon`, `visible`, `i_frame`, `cache_flag`, `target`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (60109, 'yubao-interest', '余额宝收益', 601, '', 9, '', 'recharge-manage/yubao-interest/index', '/recharge-manage/yubao-interest', 'C', 1, '', 'gold', 0, 0, 1, '', 'steve', '2022-05-24 18:57:56', '', NULL, '');

INSERT INTO `gameplat_center`.`sys_menu` (`menu_id`, `menu_name`, `title`, `parent_id`, `parent_name`, `menu_sort`, `url`, `component`, `path`, `menu_type`, `status`, `perms`, `icon`, `visible`, `i_frame`, `cache_flag`, `target`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (901020630, '', '获取免提直充限制', 60105, '', 7, '', '', '', 'F', 1, 'system:directCharge:get', '', 0, 0, 1, '', 'steve', '2022-06-11 19:33:35', '', NULL, '');
INSERT INTO `gameplat_center`.`sys_menu` (`menu_id`, `menu_name`, `title`, `parent_id`, `parent_name`, `menu_sort`, `url`, `component`, `path`, `menu_type`, `status`, `perms`, `icon`, `visible`, `i_frame`, `cache_flag`, `target`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (901020631, '', '保存免提直充', 60105, '', 8, '', '', '', 'F', 1, 'system:directCharge:add', '', 0, 0, 1, '', 'steve', '2022-06-11 19:34:03', '', NULL, '');
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (7, 901020632);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (7, 60108);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (7, 60109);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (7, 901020630);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (8, 901020632);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (8, 60108);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (8, 60109);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (8, 901020630);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (9, 901020632);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (9, 60108);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (9, 60109);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (9, 901020630);


INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (1, 901020632);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (1, 901020633);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (1, 60108);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (1, 60109);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (1, 901020630);
INSERT INTO `gameplat_center`.`sys_role_menu` (`role_Id`, `menu_Id`) VALUES (1, 901020631);