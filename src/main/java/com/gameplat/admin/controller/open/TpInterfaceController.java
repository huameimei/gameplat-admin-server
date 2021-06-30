package com.gameplat.admin.controller.open;

import com.gameplat.admin.model.vo.TpInterfaceVO;
import com.gameplat.admin.service.TpInterfaceService;
import com.gameplat.common.constant.ServiceApi;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServiceApi.OPEN_API + "/tpInterfaces")
public class TpInterfaceController {

  @Autowired private TpInterfaceService tpInterfaceService;

  @GetMapping("/queryAll")
  public List<TpInterfaceVO> getAll() {
    return tpInterfaceService.queryAll();
  }
}
