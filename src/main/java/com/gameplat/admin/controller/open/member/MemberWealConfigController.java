package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.service.MemberWealConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author lily
 * @description VIP轮播图配置
 * @date 2022/1/14
 */

@Api(tags = "VIP权益配置")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/wealConfig")
public class MemberWealConfigController {

    @Autowired
    private MemberWealConfigService memberWealConfigService;

    /** 增 */
    @PostMapping("/add")
    @ApiOperation(value = "新增权益配置")
    public void addWealConfig(@Validated @RequestBody MemberWealConfigAddDTO dto) {
        memberWealConfigService.addWealConfig(dto);
    }

    /** 删 */
    @ApiOperation(value = "删除权益配置")
    @DeleteMapping("/remove")
    public void removeWealConfig(Long id){
        memberWealConfigService.removeWealConfig(id);
    }

    /** 改 */
    @PutMapping("/edit")
    @ApiOperation(value = "修改权益配置")
    public void updateBanner(@Validated @RequestBody MemberWealConfigEditDTO dto) {
        memberWealConfigService.updateWealConfig(dto);
    }
}
