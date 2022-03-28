package com.gameplat.admin.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomMember implements Serializable {

  private String platCode; // 平台编码

  @NotNull(message = "聊天室ID不能空")
  private Integer roomId;

  @NotNull(message = "聊天室名称不能空")
  private String roomName;

  @NotNull(message = "会员ID不能空")
  private Long userId;

  @NotNull(message = "会员账号不能空")
  private String account;

  private String nickName;

  @NotNull(message = "会员类型不能空")
  private String userType;

  private Integer level = 0;

  private String hyLevel;

  private Double rechMoney;

  private Integer rechCount;
}
