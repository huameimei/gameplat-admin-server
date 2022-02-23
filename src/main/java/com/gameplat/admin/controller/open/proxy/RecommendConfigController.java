package com.gameplat.admin.controller.open.proxy;

import com.gameplat.admin.model.domain.proxy.RecommendConfig;
import com.gameplat.admin.model.dto.RecommendConfigDto;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.service.RecommendConfigService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description : 代理配置
 * @Author : cc
 * @Date : 2022/2/20
 */
@Api(tags = "代理配置")
@RestController
@RequestMapping("/api/admin/recommend/config")
@SuppressWarnings("all")
public class RecommendConfigController {
    @Autowired
    private RecommendConfigService recommendConfigService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/get.json")
    @ApiOperation(value = "获取层层代配置")
    public RecommendConfig getRecommendconfig(@RequestHeader(required = false, defaultValue = "zh-CN") String lang){
        return recommendConfigService.getRecommendConfig();
    }

    @GetMapping("/getLayerConfig.json")
    @ApiOperation(value = "获取层层代分红模式配置预设")
    public Map<String, List<GameDivideVo>> getLayerConfig(@RequestHeader(required = false,defaultValue = "zh-CN") String lang) {
        return recommendConfigService.getDefaultLayerDivideConfig(lang);
    }

    @GetMapping("/getFixConfig.json")
    @ApiOperation(value = "获取固定比例分红模式配置预设")
    public Map<String, List<GameDivideVo>> getFixConfig(@RequestHeader(required = false,defaultValue = "zh-CN") String lang) {
        return recommendConfigService.getDefaultFixDivideConfig(lang);
    }

    @GetMapping("/getFissionConfig.json")
    @ApiOperation(value = "获取裂变模式分红模式配置预设")
    public Map<String,Object> getFissionConfig(@RequestHeader(required = false,defaultValue = "zh-CN") String lang) {
        return recommendConfigService.getDefaultFissionDivideConfig(lang);
    }

    @PostMapping("/edit.json")
    @ApiOperation(value = "编辑层层代配置")
    @PreAuthorize("hasAuthority('recommendConfig:edit')")
    @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "编辑层层代配置")
    public void edit(@Validated @RequestBody RecommendConfigDto recommendConfigDto) {
        recommendConfigService.edit(recommendConfigDto);
    }
}
