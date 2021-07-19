package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.SysEmailQueryDTO;
import com.gameplat.admin.model.entity.SysEmail;
import com.gameplat.admin.model.vo.SysEmailVO;
import com.gameplat.admin.service.SysEmailService;
import com.gameplat.common.constant.ServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServiceApi.API + "/sysEmail")
public class SysEmailController {

  @Autowired private SysEmailService sysEmailService;

  @GetMapping(value = "/queryAll")
  public IPage<SysEmailVO> queryPage(Page<SysEmail> page, SysEmailQueryDTO sysEmailQueryDto) {
    return sysEmailService.queryPage(page, sysEmailQueryDto);
  }
}
