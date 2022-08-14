package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.vo.MemberWithdrawVO;
import com.gameplat.model.entity.member.MemberWithdraw;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

public interface MemberWithdrawMapper extends BaseMapper<MemberWithdraw> {

  @Select("select sum(cash_money) from member_withdraw ${ew.customSqlSegment}")
  BigDecimal summaryMemberWithdraw(@Param(Constants.WRAPPER) Wrapper<MemberWithdraw> wrapper);

  @Select("select w.id, w.cash_order_no, w.member_id, w.account, w.real_name, w.account_money, w.cash_money, w.bank_name, w.bank_card, " +
    "w.bank_address, w.cash_mode, w.cash_reason, w.cash_status, w.create_time, w.accept_account, w.accept_time, w.operator_account, w.operator_time," +
    "w.risk_opraccount, w.risk_oprtime, w.risk_status, w.approve_reason, w.approve_money, w.counter_fee, w.member_level, w.super_id," +
    "w.super_name, w.super_path, w.fk, w.police_flag, w.browser, w.mac_os, w.ip_address, w.user_agent, w.proxy_pay_status, w.proxy_pay_desc, " +
    "w.point_flag, w.pp_interface, w.pp_interface_name, w.pp_merchant_id, w.pp_merchant_name, w.member_memo, w.member_type, w.withdraw_type," +
    "w.currency_rate, w.currency_count, w.currency_protocol, w.approve_currency_rate, w.approve_currency_count, w.is_bank_blacklist, m.remark as userRemark " +
    " from member_withdraw as w left join member as m on w.member_id = m.id ${ew.customSqlSegment}")
  IPage<MemberWithdrawVO> findPage(Page<MemberWithdraw> page, @Param(Constants.WRAPPER) QueryWrapper<MemberWithdraw> queryWrapper);
}
