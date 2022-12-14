package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.GameBalanceQueryDTO;
import com.gameplat.admin.model.dto.GameKickOutDTO;
import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.admin.model.vo.GameBalanceVO;
import com.gameplat.admin.model.vo.GameKickOutVO;
import com.gameplat.admin.model.vo.GameRecycleVO;
import com.gameplat.model.entity.member.Member;
import java.math.BigDecimal;
import java.util.List;

public interface GameAdminService {

  BigDecimal getBalance(String platformCode, Member member) throws Exception;

  void transferOut(String transferIn, BigDecimal amount, Member member, boolean transferType)
      throws Exception;

  void transfer(
      String transferOut, String transferIn, BigDecimal amount, Member member, Boolean isWeb)
      throws Exception;

  void fillOrders(OperGameTransferRecordDTO gameTransferRecord) throws Exception;

  void transferIn(
      String curCode, BigDecimal amount, Member member, boolean isWeb, boolean transferType)
      throws Exception;

  void confiscated(String transferIn, Member member, BigDecimal amount) throws Exception;

  List<GameRecycleVO> recyclingAmountByAccount(String account);

  List<GameBalanceVO> selectGameBalanceByAccount(String account);

  List<GameRecycleVO> confiscatedGameByAccount(String account);

  GameBalanceVO selectGameBalance(GameBalanceQueryDTO dto);

  GameRecycleVO recyclingAmount(GameBalanceQueryDTO dto);

  GameRecycleVO confiscatedAmount(GameBalanceQueryDTO dto);

  void transferToGame(OperGameTransferRecordDTO record) throws Exception;

  List<GameKickOutVO> kickOutAll(GameKickOutDTO dto);

  void kickOut(GameKickOutDTO dto);

  List<GameKickOutVO> batchKickOut(GameKickOutDTO dto);
}
