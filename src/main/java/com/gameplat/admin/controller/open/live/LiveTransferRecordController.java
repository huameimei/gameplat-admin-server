package com.gameplat.admin.controller.open.live;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.LiveTransferRecord;
import com.gameplat.admin.model.dto.LiveTransferRecordQueryDTO;
import com.gameplat.admin.model.dto.OperLiveTransferRecordDTO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.LiveAdminService;
import com.gameplat.admin.service.LiveTransferRecordService;
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
@RequestMapping("/api/admin/live/liveTransferRecord")
public class LiveTransferRecordController {

  @Autowired
  private LiveTransferRecordService liveTransferRecordService;

  @Resource
  private LiveAdminService liveAdminService;

  @GetMapping(value = "queryPage")
  public PageDtoVO<LiveTransferRecord> queryLiveTransferRecord(Page<LiveTransferRecord> page,
      LiveTransferRecordQueryDTO dto) {
    return liveTransferRecordService.queryLiveTransferRecord(page, dto);
  }

  /**
   * 后台补单
   */
  @PostMapping(value = "/fillOrders")
  public void fillOrders(@RequestBody OperLiveTransferRecordDTO dto) throws Exception {
    liveAdminService.fillOrders(dto);
  }
}
