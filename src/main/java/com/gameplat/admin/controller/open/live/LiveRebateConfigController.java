package com.gameplat.admin.controller.open.live;

import com.gameplat.admin.model.domain.LiveRebateConfig;
import com.gameplat.admin.model.dto.OperLiveRebateConfigDTO;
import com.gameplat.admin.service.LiveRebateConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/live/liveRebateConfig")
public class LiveRebateConfigController {

  @Autowired
  private LiveRebateConfigService liveRebateConfigService;

  @GetMapping(value = "queryAll/{userLevel}")
  public List<LiveRebateConfig> queryAll(@PathVariable(value = "userLevel",required = false)  String userLevel) {
    return liveRebateConfigService.queryAll(userLevel);
  }

  @GetMapping(value = "getById/{id}")
  public LiveRebateConfig getById(@PathVariable("id") Long id){
    return liveRebateConfigService.getById(id);
  }

  @PostMapping(value = "add")
  public void add(@RequestBody OperLiveRebateConfigDTO dto) {
    liveRebateConfigService.addLiveRebateConfig(dto);
  }

  @PutMapping(value = "update")
  public void update(@RequestBody OperLiveRebateConfigDTO dto){
    liveRebateConfigService.updateLiveRebateConfig(dto);
  }

  @DeleteMapping(value = "delete/{id}")
  public void delete(@PathVariable("id") Long id){
    liveRebateConfigService.delete(id);
  }
}
