package com.gameplat.admin.controller.open.setting;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.cache.AdminCache;
import com.gameplat.admin.constant.Constants;
import com.gameplat.admin.enums.SysSettingEnum;
import com.gameplat.admin.model.vo.ListSortConfigVO;
import com.gameplat.admin.model.vo.SportConfigVO;
import com.gameplat.admin.model.vo.SysSettingVO;
import com.gameplat.admin.service.SysSettingService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.web.Result;
import com.gameplat.common.constant.CacheKey;
import com.gameplat.model.entity.setting.SysSetting;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 导航栏设置
 *
 * @author martin
 * @date 2022/3/10
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/setting")
public class SysSettingController {

    @Autowired
    private SysSettingService sysSettingService;

    @Resource
    private AdminCache adminCache;

    /**
     * 获取租户主题列表
     */
    @RequestMapping("/theme/list")
    @ApiOperation("租户主题列表查询")
    public List<SysSetting> getTenantThemeList() {
        SysSettingVO tenantSetting = new SysSettingVO();
        tenantSetting.setSettingType(Constants.TEMPLATE_CONFIG_THEME);
        tenantSetting.setDisplay(EnableEnum.ENABLED.code());
        return sysSettingService.getTenantSetting(tenantSetting);
    }

    /**
     * 获取租户导航列表
     * @param setting TenantSettingVO
     * @return List
     */
    @RequestMapping("/navigation/list")
    @ApiOperation("租户导航列表查询")
    public List<SysSetting> getTenantNavList(SysSettingVO setting) {
        if (StringUtils.isEmpty(setting.getSettingType())) {
            setting.setSettingType(Constants.SETTING_APP_NAVIGATION);
        }
        if (StringUtils.isNotEmpty(setting.getTheme())) {
            setting.setExtend4(setting.getTheme());
        }
        setting.setDisplay(EnableEnum.ENABLED.code());
        List<SysSetting> list = sysSettingService.getTenantSetting(setting);
        /* 查询到到导航栏为空，且传入的主题不为空，则为租户初使化对应主题的导航栏 */
        if (CollectionUtils.isEmpty(list) && StringUtils.isNotEmpty(setting.getTheme())) {
            if (sysSettingService.isExistTenantTheme(setting.getTheme())) {
                sysSettingService.initTenantNavigation(setting.getTheme(), setting.getSettingType());
            }
        }
        return list;
    }

    /**
     * 启动图配置列表查询
     */
    @RequestMapping("/getStartImagePage")
    @ApiOperation("启动图配置列表查询")
    @PreAuthorize("hasAuthority('banner:startImage:view')")
    public IPage<SysSetting> getStartImagePage(
            PageDTO<SysSetting> page, SysSetting sysSetting) {
        sysSetting.setSettingType(SysSettingEnum.START_UP_IMAGE.getCode());
        return sysSettingService.getStartImagePage(page, sysSetting);
    }

    /**
     * 启动图配置新增/修改
     */
    @RequestMapping("/insertStartImagePage")
    @ApiOperation("启动图配置新增/修改")
    @PreAuthorize("hasAuthority('banner:startImage:edit')")
    public Result<Object> insertStartImagePage(@RequestBody SysSetting sysSetting) {
        sysSetting.setSettingType(SysSettingEnum.START_UP_IMAGE.getCode());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(DateUtil.now());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(ObjectUtil.isNull(sysSetting.getId())){
            sysSetting.setCreateTime(date);
        }else if(ObjectUtil.isNotNull(sysSetting.getId())){
            sysSetting.setUpdateTime(date);
        }

        UserCredential user = SecurityUserHolder.getCredential();
        if (user != null) {
            sysSetting.setCreateBy(user.getUsername());
            sysSetting.setUpdateBy(user.getUsername());
        }
        sysSettingService.insertStartImagePage(sysSetting);
        adminCache.deleteByPrefix(CacheKey.getStartImgListKey());
        return Result.succeed();
    }

    /**
     * 启动图配置删除
     */
    @RequestMapping("/deleteStartImagePage")
    @ApiOperation("启动图配置删除")
    @PreAuthorize("hasAuthority('banner:startImage:remove')")
    public Result<Object> deleteStartImagePage(@RequestParam(value = "id") int id) {
        SysSetting sysSetting = new SysSetting();
        sysSetting.setSettingType(SysSettingEnum.START_UP_IMAGE.getCode());
        sysSetting.setId(id);
        sysSettingService.deleteStartImagePage(sysSetting);
        adminCache.deleteByPrefix(CacheKey.getStartImgListKey());
        return Result.succeed();
    }

    /**
     * 获取租户设置信息
     */
    @RequestMapping("/getTenantSettings")
    @ApiOperation("获取租户设置信息")
    public Result<Object> getTenantSettings(SysSettingVO tenantSettingVO) {
        // 查询租户主题
        if (Constants.TEMPLATE_CONFIG_THEME.equals(tenantSettingVO.getSettingType())) {
            tenantSettingVO.setDisplay(EnableEnum.ENABLED.code());
            return Result.succeedData(sysSettingService.getTenantSetting(tenantSettingVO));
        }
        return Result.succeedData(sysSettingService.getAppNavigation(tenantSettingVO));
    }

    /**
     * 修改显示与排序
     */
    @RequestMapping("/updateDisplayAndSort")
    @ApiOperation("修改显示与排序")
    public Result updateDisplayAndSort(@RequestBody SysSettingVO tenantSettingVO) {
        UserCredential user = SecurityUserHolder.getCredential();
        if (user != null) {
            tenantSettingVO.setUpdateBy(user.getUsername());
        }
        if (StringUtils.isBlank(tenantSettingVO.getSettingType())) {
            throw new ServiceException("导航栏类型不允许为空");
        }
        if (tenantSettingVO.getDisplay() != null
                && tenantSettingVO.getIsIndex() != null
                && tenantSettingVO.getDisplay() == 0
                && tenantSettingVO.getIsIndex() == 1) {
            throw new ServiceException("关闭的导航栏不能设置默认首页");
        }
        if (tenantSettingVO.getId() == null) {
            throw new ServiceException("id不允许为空");
        }
        sysSettingService.updateAppNavigation(tenantSettingVO);
        if (Constants.SQUARE_NAVIGATION.equals(tenantSettingVO.getSettingType())) {
            adminCache.deleteByPrefix(CacheKey.getSquareNavListKey());
        } else {
            adminCache.deleteByPrefix(CacheKey.getTenantNavPrefixKey());
        }
        return Result.succeed();
    }

    /**
     * 个人中心编辑
     */
    @RequestMapping("/updatePersonalCenter")
    @ApiOperation("个人中心编辑")
    public Result updatePersonalCenter(@RequestBody SysSettingVO tenantSettingVO) {
        UserCredential user = SecurityUserHolder.getCredential();
        if (user != null) {
            tenantSettingVO.setUpdateBy(user.getUsername());
        }
        if (StringUtils.isBlank(tenantSettingVO.getSettingType())) {
            throw new ServiceException("导航栏类型不允许为空");
        }
        if (tenantSettingVO.getDisplay() != null
                && tenantSettingVO.getIsIndex() != null
                && tenantSettingVO.getDisplay() == 0
                && tenantSettingVO.getIsIndex() == 1) {
            throw new ServiceException("关闭的导航栏不能设置默认首页");
        }
        if (tenantSettingVO.getId() == null) {
            throw new ServiceException("id不允许为空");
        }
        sysSettingService.updateAppNavigation(tenantSettingVO);
        adminCache.deleteByPrefix(CacheKey.getPersonCenterListKey());
        return Result.succeed();
    }

    /**
     * 批量修改排序
     */
    @RequestMapping("/updateBatchTenantSort")
    @ApiOperation("批量修改排序")
    public Result updateBatchTenantSetting(@RequestBody List<SysSetting> sysSettings) {
        sysSettingService.updateBatchTenantSetting(sysSettings);
        adminCache.deleteByPrefix(CacheKey.getTenantNavPrefixKey());
        return Result.succeed();
    }

    /**
     * 删除游戏浮窗类型
     */
    @PostMapping("/delById")
    @ApiOperation("删除游戏浮窗类型")
    public Result remove(Integer id) {
        sysSettingService.deleteSysFloatById(id);
        adminCache.deleteByPrefix(CacheKey.getTenantFloatPrefixKey());
        return Result.succeed();
    }

    /**
     * 广场的开关开启/关闭
     */
    @RequestMapping("/updateSquareSwitch")
    @CacheEvict(cacheNames = Constants.TENANT_SQUARE_SWITCH, allEntries = true)
    public Result updateSquareSwitch(SysSettingVO tenantSetting) {
        tenantSetting.setSettingType(Constants.SYSTEM_SETTING);
        tenantSetting.setSettingCode(Constants.SQUARE_SWITCH);
        if (org.apache.commons.lang3.StringUtils.isEmpty(tenantSetting.getSettingValue())) {
            return Result.failed("修改值不允许为空...");
        }
        sysSettingService.updateTenantSettingValue(tenantSetting);
        adminCache.deleteObject(CacheKey.getSquareEnableKey());
        return Result.succeed();
    }

    /**
     * 获取广场的开关
     */
    @RequestMapping("/getSquareSwitch")
    @PreAuthorize("hasAuthority('system:teant:..zgetSquareSwitch')")
    public Result getSquareSwitch(SysSettingVO sysTenantSetting) {
        sysTenantSetting.setSettingType(Constants.SYSTEM_SETTING);
        sysTenantSetting.setSettingCode(Constants.SQUARE_SWITCH);
        List<SysSetting> list = sysSettingService.getTenantSetting(sysTenantSetting);
        return Result.succeed(CollectionUtils.isNotEmpty(list) ? list.get(0) : null);
    }

    /**
     * 获取体育设置相关内容
     */
    @RequestMapping("/getSportConfig")
    public Result getSportConfig() {
        return Result.succeed(sysSettingService.getSportConfig());
    }

    /**
     * 修改体育配置排序开关列表
     */
    @RequestMapping("/updateListSortConfig")
    @CacheEvict(cacheNames = Constants.TENANT_LIST_SORT, allEntries = true)
    public Result updateListSortConfig(@RequestBody List<ListSortConfigVO> listSortConfigVOS){
        if (listSortConfigVOS == null || listSortConfigVOS.isEmpty()) {
            Result.failed("修改体育配置参数为空");
        }
        adminCache.deleteObject(CacheKey.getTenantListSortKey());
        return Result.succeed(sysSettingService.updateListSortConfig(listSortConfigVOS));
    }

    /**
     * 修改体育配置
     */
    @RequestMapping("/updateSportConfig")
    @CacheEvict(cacheNames = Constants.TENANT_SPORT_CONFIG, allEntries = true)
    public Result updateSportConfig(@RequestBody SportConfigVO sportConfigVo){
        if(sportConfigVo==null){
            Result.failed("修改体育配置参数为空");
        }
        adminCache.deleteObject(CacheKey.getSportConfigKey());
        return Result.succeed(sysSettingService.updateSportConfig(sportConfigVo));
    }
}
