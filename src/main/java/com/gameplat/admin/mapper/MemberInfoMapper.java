package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface MemberInfoMapper extends BaseMapper<MemberInfo> {

  /**
   * 更新余额
   *
   * @param memberId 会员账号
   * @param currentBalance 当前余额
   * @param newBalance 新的余额
   * @return int
   */
  int updateBalance(
      @Param("memberId") Long memberId,
      @Param("currentBalance") BigDecimal currentBalance,
      @Param("newBalance") BigDecimal newBalance);
}
