package com.gameplat.admin.controller.open.live;

import com.gameplat.admin.model.domain.LiveRebateConfig;
import com.gameplat.admin.model.dto.OperLiveRebateConfigDTO;
import com.gameplat.admin.service.LiveRebateConfigService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/live/liveRebateConfig/")
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
