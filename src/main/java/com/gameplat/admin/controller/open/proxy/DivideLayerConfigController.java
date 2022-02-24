package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.proxy.DivideLayerConfig;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.model.vo.DivideLayerConfigVo;
import com.gameplat.admin.service.DivideLayerConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "层层代分红模式")
@RestController
@RequestMapping("/api/admin/divide/layer")
@SuppressWarnings("all")
public class DivideLayerConfigController {
    @Autowired
    private DivideLayerConfigService layerConfigService;

    @GetMapping("/list")
    public IPage<DivideLayerConfigVo> list(PageDTO<DivideLayerConfig> page, DivideConfigDTO divideConfigDTO) {
        return layerConfigService.page(page, divideConfigDTO);
    }

    @GetMapping("/getLayerConfigForEdit")
    public Map<String,Object> getLayerConfigForEdit(DivideConfigDTO divideConfigDTO){
        return layerConfigService.getLayerConfigForEdit(divideConfigDTO.getUserName(), "zh-CN");
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增层层代分红配置")
    @PreAuthorize("hasAuthority('agent:bonusconfig:add')")
    @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "新增层层代分红配置")
    public void add(@Validated @RequestBody DivideConfigDTO divideConfigDTO) {
        layerConfigService.add(divideConfigDTO.getUserName(), "zh-CN");
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑层层代分红配置")
    @PreAuthorize("hasAuthority('agent:bonusconfig:edit')")
    @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "新增层层代分红配置")
    public void edit(@Validated @RequestBody DivideConfigDTO divideConfigDTO) {
        layerConfigService.edit(divideConfigDTO, "zh-CN");
    }

    @ApiOperation(value = "删除分红配置")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('agent:bonusconfig:remove')")
    public void remove(@RequestBody String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException("ids不能为空");
        }
        layerConfigService.remove(ids);
    }
}
