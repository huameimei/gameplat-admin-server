package com.gameplat.admin.model.dto;

import com.gameplat.model.entity.member.Member;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class OperGameBalanceDTO implements Serializable {
  private Map<String, String> platform;

  private Member member;
}
