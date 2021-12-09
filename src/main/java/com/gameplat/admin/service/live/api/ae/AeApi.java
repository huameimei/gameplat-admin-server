package com.gameplat.admin.service.live.api.ae;


import com.gameplat.admin.service.live.GameApi;
import com.gameplat.admin.service.live.api.ae.bean.AeConfig;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.live.TransferResource;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component(AeApi.LIVE_CODE + "Api")
public class AeApi  implements GameApi {

  public static final String LIVE_CODE = "ae";

  public static final String LIVE_NAME = "AE真人";

  private static final String DEFAULT_GAME_TYPE = "LIVE";

  private static final String DEFAULT_GAME_PLATFORM = "SEXYBCRT";

  private static final String DEFAULT_LANGUAGE = "cn";

  private static final int RETRY_DELAY = 1000;

  @Autowired
  private AeConfig aeConfig;

  @Override
  public BigDecimal getBalance(String account) throws Exception {
    return BigDecimal.TEN;
  }

  @Override
  public TransferResource transfer(String account, BigDecimal amount, String orderNum)
      throws Exception {
    return null;
  }

  @Override
  public String free(String gameType, String ip, Boolean isMobile, String url) throws Exception {
    return null;
  }

  @Override
  public String play(String account, String gameType, String ip, Boolean isMobile, String url)
      throws Exception {

    return null;
  }

  @Override
  public void isOpen() throws Exception {
    if (!aeConfig.isOpen()) {
      throw new ServiceException("游戏未接入");
    }
  }
}
