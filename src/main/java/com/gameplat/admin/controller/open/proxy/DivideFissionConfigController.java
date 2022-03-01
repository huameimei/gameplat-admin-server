package com.gameplat.admin.controller.open.proxy;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.proxy.DivideFissionConfig;
import com.gameplat.admin.model.domain.proxy.DivideFixConfig;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.service.DivideFissionConfigService;
import com.gameplat.admin.service.DivideFixConfigService;
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

@Api(tags = "裂变分红模式")
@RestController
@RequestMapping("/api/admin/divide/fission")
@SuppressWarnings("all")
public class DivideFissionConfigController {
    @Autowired
    private DivideFissionConfigService fissionConfigService;

    /**
     * 分页查询
     * @param page
     * @param queryObj
     * @return
     */
    @GetMapping("/pageList")
    public IPage<DivideFissionConfig> list(PageDTO<DivideFissionConfig> page, DivideConfigDTO queryObj) {
        LambdaQueryWrapper<DivideFissionConfig> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(StrUtil.isNotBlank(queryObj.getUserName()), DivideFissionConfig::getUserName, queryObj.getUserName());
        return fissionConfigService.page(page, queryWrapper);
    }

    /**
     * 添加
     * @param divideConfigDTO
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增裂变分红配置")
    @PreAuthorize("hasAuthority('agent:bonusFissionconfig:add')")
    @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "新增裂变分红配置")
    public void add(@Validated @RequestBody DivideConfigDTO divideConfigDTO) {
        fissionConfigService.add(divideConfigDTO.getUserName(), "zh-CN");
    }

    /**
     * 编辑裂变分红配置前获取
     * @param divideConfigDTO
     * @return
     */
    @GetMapping("/getFissionConfigForEdit")
    public Map<String,Object> getFissionConfigForEdit(DivideConfigDTO divideConfigDTO){
        return fissionConfigService.getFissionConfigForEdit(divideConfigDTO.getUserName(), "zh-CN");
    }

    /**
     * 编辑裂变分红配置
     * @param divideConfigDTO
     */
    @PostMapping("/edit")
    @ApiOperation(value = "编辑裂变分红配置")
    @PreAuthorize("hasAuthority('agent:bonusFissionconfig:edit')")
    @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "编辑裂变分红配置")
    public void edit(@Validated @RequestBody DivideConfigDTO divideConfigDTO) {
        fissionConfigService.edit(divideConfigDTO, "zh-CN");
    }

    @ApiOperation(value = "删除裂变分红配置")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('agent:bonusFissionconfig:remove')")
    public void remove(@RequestBody String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException("ids不能为空");
        }
        fissionConfigService.remove(ids);
    }
}
