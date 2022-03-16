package com.gameplat.admin.controller.open.setting;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.constant.Constants;
import com.gameplat.admin.enums.TenantSettingEnum;
import com.gameplat.admin.model.vo.TenantSettingVO;
import com.gameplat.admin.service.SysTenantSettingService;
import com.gameplat.admin.service.TenantSettingService;
import com.gameplat.base.common.web.Result;
import com.gameplat.model.entity.setting.TenantSetting;
import com.gameplat.model.entity.sys.SysTenantSetting;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author martin
 * @date 2022/3/10
 * @desc 导航栏设置
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/setting")
public class TenantSettingController {

    @Autowired
    private TenantSettingService tenantSettingService;

    @Resource
    private SysTenantSettingService sysTenantSettingService;

    /**
     * 获取租户主题列表
     */
    @RequestMapping("/theme/list")
    @ApiOperation("租户主题列表查询")
    public Result<Object> getTenantThemeList() {
        TenantSettingVO tenantSetting = new TenantSettingVO();
        tenantSetting.setSettingType(Constants.TEMPLATE_CONFIG_THEME);
        tenantSetting.setDisplay(Constants.YES);
        List<TenantSetting> list = tenantSettingService.getTenantSetting(tenantSetting);
        return Result.succeedData(list);
    }

    /**
     * 获取租户导航列表
     * @param setting
     * @return
     */
    @RequestMapping("/navigation/list")
    @ApiOperation("租户导航列表查询")
    public Result<Object> getTenantNavList(TenantSettingVO setting) {
        if (StringUtils.isEmpty(setting.getSettingType())) {
            setting.setSettingType(Constants.SETTING_APP_NAVIGATION);
        }
        if (StringUtils.isNotEmpty(setting.getTheme())) {
            setting.setExtend4(setting.getTheme());
        }
        setting.setDisplay(Constants.YES);
        List<TenantSetting> list = tenantSettingService.getTenantSetting(setting);
        /**查询到到导航栏为空，且传入的主题不为空，则为租户初使化对应主题的导航栏*/
        if (CollectionUtils.isEmpty(list) && StringUtils.isNotEmpty(setting.getTheme())) {
            if (tenantSettingService.isExistTenantTheme(setting.getTheme())) {
                tenantSettingService.initTenantNavigation(setting.getTheme(), setting.getSettingType());
            }
        }
        return Result.succeedData(list);
    }

    /**
     * 启动图配置列表查询
     */
    @RequestMapping("/getStartImagePage")
    @ApiOperation("启动图配置列表查询")
    public IPage<TenantSetting> getStartImagePage(PageDTO<TenantSetting> page, TenantSetting tenantSetting){
        tenantSetting.setSettingType(TenantSettingEnum.START_UP_IMAGE.getCode());
        return tenantSettingService.getStartImagePage(page,tenantSetting);
    }

    /**
     * 启动图配置新增/修改
     */
    @RequestMapping("/insertStartImagePage")
    @ApiOperation("启动图配置新增/修改")
    public Result insertStartImagePage(@RequestBody TenantSetting tenantSetting){
        tenantSetting.setSettingType(TenantSettingEnum.START_UP_IMAGE.getCode());
        UserCredential user = SecurityUserHolder.getCredential();
        if (user != null) {
            tenantSetting.setCreateBy(user.getUsername());
        }
        tenantSettingService.insertStartImagePage(tenantSetting);
       return Result.succeed();
    }

    /**
     * 启动图配置删除
     */
    @RequestMapping("/deleteStartImagePage")
    @ApiOperation("启动图配置删除")
    public Result deleteStartImagePage(@RequestParam(value = "id", required = true) int id){
        TenantSetting tenantSetting = new TenantSetting();
        tenantSetting.setSettingType(TenantSettingEnum.START_UP_IMAGE.getCode());
        tenantSetting.setId(id);
        tenantSettingService.deleteStartImagePage(tenantSetting);
        return Result.succeed();
    }

    /**
     * 获取租户设置信息
     */
    @RequestMapping("/getTenantSettings")
    @ApiOperation("获取租户设置信息")
    public Result<Object> getTenantSettings(TenantSettingVO tenantSettingVO) {
        // 查询租户主题
        if (Constants.TEMPLATE_CONFIG_THEME.equals(tenantSettingVO.getSettingType())) {
            TenantSetting setting = new TenantSetting();
            setting.setSettingType(Constants.TEMPLATE_CONFIG_THEME);
            setting.setDisplay(Constants.YES);
            return Result.succeedData(tenantSettingService.getTenantSetting(tenantSettingVO));
        }
        return Result.succeedData(tenantSettingService.getAppNavigation(tenantSettingVO));
    }

    /**
     * 修改显示与排序
     */
    @RequestMapping("/updateDisplayAndSort")
    @ApiOperation("修改显示与排序")
    @CacheEvict(cacheNames = Constants.TENANT_NAVIGATION_LIST, allEntries = true)
    public Result updateDisplayAndSort(@RequestBody TenantSettingVO tenantSettingVO) {
        UserCredential user = SecurityUserHolder.getCredential();
        if (user != null) {
            tenantSettingVO.setUpdateBy(user.getUsername());
        }
        if (StringUtils.isBlank(tenantSettingVO.getSettingType())) {
            throw new ServiceException("导航栏类型不允许为空");
        }
        if (tenantSettingVO.getDisplay()!=null && tenantSettingVO.getIsIndex() !=null &&
                tenantSettingVO.getDisplay() == 0 && tenantSettingVO.getIsIndex() ==1) {
            throw new ServiceException("关闭的导航栏不能设置默认首页");
        }
        if (tenantSettingVO.getId() == null) {
            throw new ServiceException("id不允许为空");
        }
        tenantSettingService.updateAppNavigation(tenantSettingVO);
        return Result.succeed();
    }

    /**
     * 批量修改排序
     */
    @RequestMapping("/updateBatchTenantSort")
    @ApiOperation("批量修改排序")
    @CacheEvict(cacheNames = Constants.TENANT_NAVIGATION_LIST, allEntries = true)
    public Result updateBatchTenantSetting(@RequestBody List<TenantSetting> tenantSettings) {
        tenantSettingService.updateBatchTenantSetting(tenantSettings);
        return Result.succeed();
    }

    /**
     * 删除游戏浮窗类型
     */
    @DeleteMapping("/delById")
    @ApiOperation("删除游戏浮窗类型")
    @CacheEvict(cacheNames = Constants.TENANT_FLOAT_LIST,allEntries = true)
    public Result remove(Integer id) {
        tenantSettingService.deleteSysFloatById(id);
        return Result.succeed();
    }

    /**
     * 广场的开关开启/关闭
     */
    @RequestMapping("/updateSquareSwitch")
    @CacheEvict(cacheNames = Constants.TENANT_SQUARE_SWITCH,allEntries = true)
    public Result updateSquareSwitch(SysTenantSetting sysTenantSetting) {
        sysTenantSetting.setSettingType(Constants.SYSTEM_SETTING);
        sysTenantSetting.setSettingCode(Constants.SQUARE_SWITCH);
        if (org.apache.commons.lang3.StringUtils.isEmpty(sysTenantSetting.getSettingValue())) {
            return Result.failed("修改值不允许为空...");
        }
        sysTenantSettingService.updateTenantSettingValue(sysTenantSetting);
        return Result.succeed();
    }
}
