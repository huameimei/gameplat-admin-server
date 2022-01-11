package com.gameplat.admin.controller.open.live;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.LiveBetRecordQueryDTO;
import com.gameplat.admin.model.vo.LiveBetRecordVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.LiveBetRecordService;
import com.gameplat.common.game.LiveGameResult;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/live/liveBetRecord/")
public class LiveBetRecordController {

  @Resource
  private LiveBetRecordService liveBetRecordService;

  @GetMapping(value = "queryPage")
  public PageDtoVO<LiveBetRecordVO> queryPage(Page<LiveBetRecordVO> page,LiveBetRecordQueryDTO dto) throws Exception {
    return liveBetRecordService.queryPageBetRecord(page, dto);
  }


  @GetMapping(value = "/getGameResult")
  public LiveGameResult getGameResult(LiveBetRecordQueryDTO dto,HttpServletResponse response) throws Exception {
    return liveBetRecordService.getGameResult(dto.getLiveCode(),dto.getBillNo());
  }
}
