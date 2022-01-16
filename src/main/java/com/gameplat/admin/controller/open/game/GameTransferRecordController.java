package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.GameTransferRecord;
import com.gameplat.admin.model.dto.GameTransferRecordQueryDTO;
import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameAdminService;
import com.gameplat.admin.service.GameTransferRecordService;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/game/gameTransferRecord")
public class GameTransferRecordController {

  @Autowired
  private GameTransferRecordService gameTransferRecordService;

  @Resource
  private GameAdminService gameAdminService;

  @GetMapping(value = "/queryPage")
  public PageDtoVO<GameTransferRecord> queryGameTransferRecord(Page<GameTransferRecord> page,
      GameTransferRecordQueryDTO dto) {
    return gameTransferRecordService.queryGameTransferRecord(page, dto);
  }

  /**
   * 后台补单
   */
  @PostMapping(value = "/fillOrders")
  public void fillOrders(@RequestBody OperGameTransferRecordDTO dto) throws Exception {
    gameAdminService.fillOrders(dto);
  }
}
