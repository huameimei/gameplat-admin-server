package com.gameplat.admin.model.dto;

import com.gameplat.admin.model.domain.Member;
import java.io.Serializable;
import java.util.Map;
import lombok.Data;

@Data
public class OperGameBalanceDTO implements Serializable {
  private Map<String, String> platform;
  private Member member;

}
