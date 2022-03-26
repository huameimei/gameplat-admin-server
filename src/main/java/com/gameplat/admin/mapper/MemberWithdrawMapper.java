package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gameplat.model.entity.member.MemberWithdraw;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

public interface MemberWithdrawMapper extends BaseMapper<MemberWithdraw> {

  @Select("select sum(cash_money) from member_withdraw ${ew.customSqlSegment}")
  BigDecimal summaryMemberWithdraw(@Param(Constants.WRAPPER) Wrapper<MemberWithdraw> wrapper);
}
