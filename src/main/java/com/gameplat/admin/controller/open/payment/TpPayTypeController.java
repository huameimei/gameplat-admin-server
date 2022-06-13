package com.gameplat.admin.controller.open.payment;

import com.gameplat.admin.model.vo.TpPayTypeVO;
import com.gameplat.admin.service.TpPayTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "第三方支付方式")
@RestController
@RequestMapping("/api/admin/thirdParty/tpPayTypes")
public class TpPayTypeController {

  @Autowired private TpPayTypeService payTypeService;

  @Operation(summary = "获取全部")
  @PostMapping("/queryTpPayTypes")
  @PreAuthorize("hasAuthority('thirdParty:tpPayTypes:queryTpPayTypes')")
  public List<TpPayTypeVO> getTpPayTypes(String tpInterfaceCode) {
    return payTypeService.queryTpPayTypes(tpInterfaceCode);
  }
}
