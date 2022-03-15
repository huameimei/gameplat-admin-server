package com.gameplat.admin.controller.open.setting;

import com.alibaba.fastjson.JSON;
import com.gameplat.admin.model.vo.TenantFloatTypeVo;
import com.gameplat.admin.service.TenantFloatTypeService;
import com.gameplat.model.entity.setting.TenantFloatSetting;
import com.gameplat.model.entity.setting.TenantFloatType;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统消息
 *
 * @author key
 */
@Slf4j
@RestController
@RequestMapping("/api/open/float")
public class TenantFloatTypeController {
    @Autowired
    private TenantFloatTypeService tenantFloatTypeService;

     /**
     * 查询游戏浮窗类型列表
     */
    @GetMapping("/query")
    @ApiOperation("游戏浮窗类型列表查询")
    public List<TenantFloatTypeVo> list(TenantFloatTypeVo tenantFloatTypeVo) {
        return tenantFloatTypeService.selectSysFloatTypeList(tenantFloatTypeVo);
    }

    /**
     * 新增游戏浮窗
     */
    @PutMapping("/insert")
    @ApiOperation("游戏浮窗查询")
    @CacheEvict(cacheNames = "TENANT_FLOAT_LIST",allEntries = true)
    public void add(@RequestBody TenantFloatSetting tenantFloatSetting) {
        tenantFloatTypeService.insertSysFloat(tenantFloatSetting);
    }

    /**
     * 编辑游戏浮窗
     */
    // @PreAuthorize("@ss.hasPermi('kg:type:remove')")
    @PostMapping("/update")
    @ApiOperation("游戏浮窗编辑")
    @CacheEvict(cacheNames = "TENANT_FLOAT_LIST",allEntries = true)
    public void update(@RequestBody TenantFloatSetting tenantFloatSetting) {
        tenantFloatTypeService.updateFloat(tenantFloatSetting);
    }

    /**
     * 批量编辑排序
     */
    // @PreAuthorize("@ss.hasPermi('kg:type:remove')")
    @PostMapping("/updateBatch")
    @ApiOperation("批量编辑排序")
    @CacheEvict(cacheNames = "TENANT_FLOAT_LIST",allEntries = true)
    public void updateBatch(@RequestBody List<TenantFloatSetting> tenantFloatSettings) {
        tenantFloatTypeService.updateBatch(tenantFloatSettings);
    }

    /**
     * 编辑游戏浮窗类型
     */
    @PostMapping("/updateFloatType")
    @ApiOperation("游戏浮窗类型编辑")
    @CacheEvict(cacheNames = "TENANT_FLOAT_LIST",allEntries = true)
    public void updateFloatType(@RequestBody TenantFloatType tenantFloatType) {
        tenantFloatTypeService.updateFloatType(tenantFloatType);
    }

    /**
     * 编辑游戏浮窗类型
     */
    @PostMapping("/showPosition")
    @ApiOperation("游戏浮窗类型编辑")
    @CacheEvict(cacheNames = "TENANT_FLOAT_LIST",allEntries = true)
    public void showPosition(@RequestBody List<String> showPositionList) {
        tenantFloatTypeService.updateShowPosition(JSON.toJSONString(showPositionList));
    }
}
