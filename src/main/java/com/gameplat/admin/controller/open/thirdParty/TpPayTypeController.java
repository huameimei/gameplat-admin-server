package com.gameplat.admin.controller.open.thirdParty;

import com.gameplat.admin.model.vo.TpPayTypeVO;
import com.gameplat.admin.service.TpPayTypeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/thirdParty/tpPayTypes")
public class TpPayTypeController {

  @Autowired
  private TpPayTypeService payTypeService;

  @PostMapping("/queryTpPayTypes")
  @PreAuthorize("hasAuthority('thirdParty:tpPayTypes:queryTpPayTypes')")
  public List<TpPayTypeVO> getTpPayTypes(String interfaceCode) {
    return payTypeService.queryTpPayTypes(interfaceCode);
  }
}
