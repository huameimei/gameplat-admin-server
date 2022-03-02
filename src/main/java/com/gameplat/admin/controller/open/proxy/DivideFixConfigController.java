package com.gameplat.admin.controller.open.proxy;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.service.DivideFixConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.proxy.DivideFixConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "固定比例分红模式")
@RestController
@RequestMapping("/api/admin/divide/fix")
public class DivideFixConfigController {

    @Autowired
    private DivideFixConfigService fixConfigService;

    @GetMapping("/pageList")
    public IPage<DivideFixConfig> list(PageDTO<DivideFixConfig> page, DivideConfigDTO queryObj) {
        LambdaQueryWrapper<DivideFixConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(
                StrUtil.isNotBlank(queryObj.getUserName()),
                DivideFixConfig::getUserName,
                queryObj.getUserName());
        return fixConfigService.page(page, queryWrapper);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增固定比例分红配置")
    @PreAuthorize("hasAuthority('agent:bonusFixconfig:add')")
    @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "新增固定比例分红配置")
    public void add(@Validated @RequestBody DivideConfigDTO divideConfigDTO) {
        fixConfigService.add(divideConfigDTO.getUserName(), "zh-CN");
    }

    /**
     * 编辑层层代分红配置前获取
     *
     * @param divideConfigDTO
     * @return
     */
    @GetMapping("/getFixConfigForEdit")
    public Map<String, Object> getFixConfigForEdit(DivideConfigDTO divideConfigDTO) {
        return fixConfigService.getFixConfigForEdit(divideConfigDTO.getUserName(), "zh-CN");
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑固定比例分红配置")
    @PreAuthorize("hasAuthority('agent:bonusFixconfig:edit')")
    @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "新增固定比例分红配置")
    public void edit(@Validated @RequestBody DivideConfigDTO divideConfigDTO) {
        fixConfigService.edit(divideConfigDTO, "zh-CN");
    }

    @ApiOperation(value = "删除固定分红配置")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('agent:bonusFixconfig:remove')")
    public void remove(@RequestBody String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException("ids不能为空");
        }
        fixConfigService.remove(ids);
    }
}
