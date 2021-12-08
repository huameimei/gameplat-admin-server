package com.gameplat.admin.model.dto;

import com.gameplat.admin.model.domain.Member;
import java.io.Serializable;
import java.util.Map;
import lombok.Data;

@Data
public class OperLiveBalanceDTO implements Serializable {
  private Map<String, String> platform;
  private Member member;

}
