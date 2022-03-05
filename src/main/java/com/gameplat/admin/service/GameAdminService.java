package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.model.entity.member.Member;

import java.math.BigDecimal;

public interface GameAdminService {

  BigDecimal getBalance(String liveCode, Member member) throws Exception;

  void transferOut(String transferIn, BigDecimal amount, Member member, boolean transferType)
      throws Exception;

  void transfer(
      String transferOut, String transferIn, BigDecimal amount, Member member, Boolean isWeb)
      throws Exception;

  void fillOrders(OperGameTransferRecordDTO liveTransferRecord) throws Exception;

  void transferIn(
      String curCode, BigDecimal amount, Member member, boolean isWeb, boolean transferType)
      throws Exception;

  void confiscated(String transferIn, BigDecimal amount, Member member) throws Exception;
}
