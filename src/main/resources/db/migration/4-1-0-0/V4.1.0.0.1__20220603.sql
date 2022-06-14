ALTER TABLE `activity_turntable` ADD UNIQUE (`activity_lobby_id`);

alter table member modify column game_account varchar(20);
alter table member_loan modify member_balance decimal(20,1) not null DEFAULT(0.0) COMMENT "借款金额/还款金额/回收金额";
alter table member_loan modify loan_money decimal(20,1) not null DEFAULT(0.0) COMMENT "借呗额度";
alter table member_loan modify overdraft_money decimal(20,1) not null DEFAULT(0.0) COMMENT "总欠款金额";
alter table member_loan modify remain_money decimal(20,1) not null DEFAULT(0.0) COMMENT "剩余未还";

update sys_dict_data set dict_value = 60 where dict_type = "TRANS_TYPE" and dict_label = "LOAN_REPAY";