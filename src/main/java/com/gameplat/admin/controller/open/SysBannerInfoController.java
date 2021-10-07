package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.SysBannerInfoAddDTO;
import com.gameplat.admin.model.dto.SysBannerInfoEditDTO;
import com.gameplat.admin.model.dto.SysBannerInfoQueryDTO;
import com.gameplat.admin.model.entity.SysBannerInfo;
import com.gameplat.admin.model.vo.SysBannerInfoVO;
import com.gameplat.admin.service.SysBannerInfoService;
import com.gameplat.common.constant.ServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ServiceApi.API + "/bannerInfo")
public class SysBannerInfoController {

    @Autowired
    private SysBannerInfoService sysBannerInfoService;

    @GetMapping(value = "/queryAll")
    public IPage<SysBannerInfoVO> queryAll(Page<SysBannerInfo> page, SysBannerInfoQueryDTO queryDto) {
        return sysBannerInfoService.queryPage(page, queryDto);
    }

    @PostMapping(value = "/save")
    public void save(@RequestBody SysBannerInfoAddDTO sysBannerInfoAddDto) {
        sysBannerInfoService.save(sysBannerInfoAddDto);
    }

    @PostMapping(value = "/edit")
    public void update(@RequestBody SysBannerInfoEditDTO sysBannerInfoEditDto) {
        sysBannerInfoService.update(sysBannerInfoEditDto);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        sysBannerInfoService.delete(id);
    }
}
