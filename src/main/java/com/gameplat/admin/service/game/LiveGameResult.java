package com.gameplat.admin.service.game;

import java.io.Serializable;
import lombok.Data;

@Data
public class LiveGameResult implements Serializable {
  private boolean redirect;
  private String data;
}
