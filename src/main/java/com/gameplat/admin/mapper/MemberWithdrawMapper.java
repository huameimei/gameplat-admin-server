package com.gameplat.admin.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gameplat.admin.model.domain.MemberWithdraw;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface MemberWithdrawMapper extends BaseMapper<MemberWithdraw> {

  @Select("select sum(cash_money) from member_withdraw ${ew.customSqlSegment}")
  BigDecimal summaryMemberWithdraw(@Param(Constants.WRAPPER) Wrapper<MemberWithdraw> wrapper);

}
