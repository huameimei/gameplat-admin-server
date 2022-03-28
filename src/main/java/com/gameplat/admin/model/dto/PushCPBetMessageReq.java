package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class PushCPBetMessageReq {

  private Long userId;
  private String account;
  private String gameId;
  private String gameName;
  private String turnNum;
  private String betContent;
  private String playName;
  private String playCode;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date betTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date betEndTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date betStatTime;

  private Double totalMoney;
  private String orderNo;
  private Double oneMoney;
  private Long heelUserId; // 跟投用户ID
  private String heelUserName; // 跟投用户名字
  private Long heelRoomId; // 跟投房间
}
