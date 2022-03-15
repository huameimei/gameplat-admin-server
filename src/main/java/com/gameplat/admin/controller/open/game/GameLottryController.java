package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.vo.GameLotteryVo;
import com.gameplat.admin.service.GameLotteryMapperService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author kb @Date 2022/3/1 19:35 @Version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/game/gameLottry")
public class GameLottryController {

  private final GameLotteryMapperService gameLotteryMapperService;

  /** 查询彩种 */
  @GetMapping("findGameLotteryType/{code}")
  public List<GameLotteryVo> findGameLotteryType(@PathVariable("code") int code) {
    if (StringUtils.isNull(code)) {
      throw new ServiceException("彩票类型不能为空！");
    }
    return gameLotteryMapperService.findGameLotteryType(code);
  }
}
