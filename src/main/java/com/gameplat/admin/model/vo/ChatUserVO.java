package com.gameplat.admin.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ChatUserVO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long userId;

  private String account;

  private String nickName;

  private String avatar;
}
