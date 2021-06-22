package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gameplat.admin.convert.SysAuthIpConvert;
import com.gameplat.admin.model.dto.SysAuthIpAddDto;
import com.gameplat.admin.model.entity.SysAuthIp;
import com.gameplat.admin.model.vo.SysAuthIpVo;
import com.gameplat.admin.service.SysAuthIpService;
import com.gameplat.common.web.Result;
import java.util.List;
import java.util.stream.Collectors;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gameplat-admin-service/api/internal/authIp")
public class SysAuthIpController {

  @Autowired private SysAuthIpService sysAuthIpService;

  @Autowired private SysAuthIpConvert sysAuthIpConvert;

  /** 查询所有IP白名单 */
  @GetMapping(value = "/queryAll")
  @ResponseBody
  public Result queryAll(String allowIp) {
    return Result.succeed(sysAuthIpService.listByIp(allowIp).stream()
        .map(i -> sysAuthIpConvert.toVo(i))
        .collect(Collectors.toList()));
  }

  @GetMapping(value = "/save")
  @ResponseBody
  public Result saveOrUpdate(SysAuthIpAddDto sysAuthIpAddDto){
    if(StringUtil.isBlank(sysAuthIpAddDto.getAllowIp()) ) {
      return Result.failed("IP不能为空");
    }
    if (sysAuthIpService.isExist(sysAuthIpAddDto.getAllowIp())){
      return Result.failed("IP已经存在，请勿重复添加");
    }
    if(sysAuthIpAddDto.getIpType() == null ) {
      sysAuthIpAddDto.setIpType(0);
    }
    sysAuthIpService.saveOrUpdate(sysAuthIpConvert.toEntity(sysAuthIpAddDto));
    return Result.succeed();
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  @ResponseBody
  public Result delete(SysAuthIp authIp) {
    sysAuthIpService.removeById(authIp.getId());
    return Result.succeed();
  }
}
