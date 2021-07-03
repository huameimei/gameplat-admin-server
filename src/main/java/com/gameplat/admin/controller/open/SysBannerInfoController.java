package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.model.dto.SysBannerInfoAddDto;
import com.gameplat.admin.model.dto.SysBannerInfoEditDto;
import com.gameplat.admin.model.dto.SysBannerInfoQueryDto;
import com.gameplat.admin.model.entity.SysBannerInfo;
import com.gameplat.admin.model.vo.SysBannerInfoVo;
import com.gameplat.admin.service.SysBannerInfoService;
import com.gameplat.common.constant.ServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServiceApi.OPEN_API + "/bannerInfo")
public class SysBannerInfoController {

  @Autowired private SysBannerInfoService sysBannerInfoService;

  @GetMapping(value = "/queryAll")
  public IPage<SysBannerInfoVo> queryAll(
      IPage<SysBannerInfo> page, SysBannerInfoQueryDto queryDto) {
    return sysBannerInfoService.queryPage(page, queryDto);
  }

  @PostMapping(value = "/save")
  public void save(@RequestBody SysBannerInfoAddDto sysBannerInfoAddDto) {
    sysBannerInfoService.save(sysBannerInfoAddDto);
  }

  @PostMapping(value = "/edit")
  public void update(@RequestBody SysBannerInfoEditDto sysBannerInfoEditDto) {
    sysBannerInfoService.update(sysBannerInfoEditDto);
  }

  @DeleteMapping(value = "/delete/{id}")
  public void delete(@PathVariable("id") Long id) {
    sysBannerInfoService.delete(id);
  }
}
