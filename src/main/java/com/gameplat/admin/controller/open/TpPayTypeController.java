package com.gameplat.admin.controller.open;

import com.gameplat.admin.model.vo.TpPayTypeVO;
import com.gameplat.admin.service.TpPayTypeService;
import com.gameplat.common.constant.ServiceApi;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServiceApi.OPEN_API + "/tpPayTypes")
public class TpPayTypeController {

  @Autowired private TpPayTypeService payTypeService;

  @PostMapping("/queryTpPayTypes")
  public List<TpPayTypeVO> getTpPayTypes(@RequestBody String interfaceCode) {
    return payTypeService.queryTpPayTypes(interfaceCode);
  }
}
