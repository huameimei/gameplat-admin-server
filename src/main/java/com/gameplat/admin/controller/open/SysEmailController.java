package com.gameplat.admin.controller.open;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.model.dto.SysEmailQueryDto;
import com.gameplat.admin.model.entity.SysEmail;
import com.gameplat.admin.model.vo.SysEmailVo;
import com.gameplat.admin.service.SysEmailService;
import com.gameplat.common.constant.ServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( ServiceApi.OPEN_API + "/sysEmail")
public class SysEmailController {

  @Autowired private SysEmailService sysEmailService;

  @GetMapping(value = "/queryAll")
  @ResponseBody
  public IPage<SysEmailVo> queryPage(IPage<SysEmail> page,SysEmailQueryDto sysEmailQueryDto) {
    return sysEmailService.queryPage(page, sysEmailQueryDto);
  }
}
