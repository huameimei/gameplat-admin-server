package com.gameplat.admin.controller.open.live;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.LiveRebateDetail;
import com.gameplat.admin.model.domain.LiveRebateReport;
import com.gameplat.admin.model.dto.LiveRebateReportQueryDTO;
import com.gameplat.admin.model.dto.OperLiveRebateDetailDTO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.LiveRebateReportService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/live/liveRebateReport")
public class LiveRebateReportController {

  @Autowired
  private LiveRebateReportService liveRebateReportService;


  @GetMapping(value = "queryPage")
  public PageDtoVO<LiveRebateDetail> queryPage(Page<LiveRebateDetail> page, LiveRebateReportQueryDTO dto) {
    return liveRebateReportService.queryPage(page, dto);
  }

  // TODO 真人返点统计导出


  @PostMapping(value = "reject")
  //@Log(type = LogType.LIVEREBATE, content = "'真人返水拒发:会员账号:'+#userAccount+',期数:'+#periodName")
  public void reject(@RequestBody OperLiveRebateDetailDTO dto){
    liveRebateReportService.reject(dto.getAccount(),dto.getPeriodName(), dto.getRemark());
  }

  @PutMapping(value = "modify")
  //@Log(type = LogType.LIVEREBATE, content = "'真人返水修改:期数:'+#periodName+',会员账号：'+#account+',返水金额:'+#realRebateMoney")
  public void modify(@RequestBody OperLiveRebateDetailDTO dto) {
    liveRebateReportService.modify(dto.getId(), dto.getRealRebateMoney(), dto.getRemark());
  }

  @GetMapping(value = "queryDetail")
  public List<LiveRebateReport> queryDetail(LiveRebateReportQueryDTO dto) {
    return liveRebateReportService.queryDetail(dto);
  }
}
