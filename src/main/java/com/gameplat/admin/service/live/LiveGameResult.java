package com.gameplat.admin.service.live;

import java.io.Serializable;
import lombok.Data;

@Data
public class LiveGameResult implements Serializable {
  private boolean redirect;
  private String data;
}
