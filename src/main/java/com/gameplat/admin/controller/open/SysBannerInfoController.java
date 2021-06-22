package com.gameplat.admin.controller.open;

import com.gameplat.admin.convert.SysBannerInfoConvert;
import com.gameplat.admin.model.dto.SysBannerInfoAddDto;
import com.gameplat.admin.model.entity.SysBannerInfo;
import com.gameplat.admin.service.SysBannerInfoService;
import com.gameplat.common.web.Result;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gameplat-admin-service/api/internal/bannerInfo")
public class SysBannerInfoController {

  @Autowired private SysBannerInfoService sysBannerInfoService;

  @Autowired
  private SysBannerInfoConvert sysBannerInfoConvert;

  @GetMapping(value = "/queryAll")
  @ResponseBody
  public Result queryAll() {
    return Result.succeed(sysBannerInfoService.list().stream()
        .map(i -> sysBannerInfoConvert.toVo(i))
        .collect(Collectors.toList()));
  }

  @GetMapping(value = "/save")
  @ResponseBody
  public Result saveOrUpdate(SysBannerInfoAddDto sysBannerInfoAddDto){
    sysBannerInfoService.saveOrUpdate(sysBannerInfoConvert.toEntity(sysBannerInfoAddDto));
    return Result.succeed();
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  @ResponseBody
  public Result delete(SysBannerInfo sysBannerInfo) {
    sysBannerInfoService.removeById(sysBannerInfo.getId());
    return Result.succeed();
  }
}
