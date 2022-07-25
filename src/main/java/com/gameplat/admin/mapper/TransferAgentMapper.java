package com.gameplat.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TransferAgentMapper {
    Integer transferActivityRecord(@Param("account") String account,
                                @Param("originSuperPath") String originSuperPath);

    Integer transferGameReport(@Param("account") String account,
                            @Param("originSuperPath") String originSuperPath);

    Integer transferMemberReport(@Param("account") String account,
                              @Param("originSuperPath") String originSuperPath);

    Integer transferMemberBill(@Param("account") String account,
                            @Param("originSuperPath") String originSuperPath);

    Integer transferRechargeRecord(@Param("account") String account,
                                @Param("originSuperPath") String originSuperPath);

    Integer transferWithdrawRecord(@Param("account") String account,
                                @Param("originSuperPath") String originSuperPath);

    Integer transferGameRebateReport(@Param("account") String account,
                                  @Param("originSuperPath") String originSuperPath);

    Integer transferWealRecord(@Param("account") String account,
                            @Param("originSuperPath") String originSuperPath);

  Integer transferRwRecord(
      @Param("account") String account, @Param("originSuperPath") String originSuperPath);
}
