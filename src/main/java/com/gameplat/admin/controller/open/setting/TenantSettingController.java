package com.gameplat.admin.controller.open.setting;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.cache.AdminCache;
import com.gameplat.admin.constant.Constants;
import com.gameplat.admin.enums.TenantSettingEnum;
import com.gameplat.admin.model.vo.ListSortConfigVO;
import com.gameplat.admin.model.vo.SportConfigVO;
import com.gameplat.admin.model.vo.TenantSettingVO;
import com.gameplat.admin.service.TenantSettingService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.web.Result;
import com.gameplat.common.constant.CacheKey;
import com.gameplat.common.util.DateUtil;
import com.gameplat.model.entity.setting.TenantSetting;
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
public class TenantSettingController {

    @Autowired
    private TenantSettingService tenantSettingService;

    @Resource
    private AdminCache adminCache;

    /**
     * 获取租户主题列表
     */
    @RequestMapping("/theme/list")
    @ApiOperation("租户主题列表查询")
    public List<TenantSetting> getTenantThemeList() {
        TenantSettingVO tenantSetting = new TenantSettingVO();
        tenantSetting.setSettingType(Constants.TEMPLATE_CONFIG_THEME);
        tenantSetting.setDisplay(EnableEnum.ENABLED.code());
        return tenantSettingService.getTenantSetting(tenantSetting);
    }

    /**
     * 获取租户导航列表
     * @param setting TenantSettingVO
     * @return List
     */
    @RequestMapping("/navigation/list")
    @ApiOperation("租户导航列表查询")
    public List<TenantSetting> getTenantNavList(TenantSettingVO setting) {
        if (StringUtils.isEmpty(setting.getSettingType())) {
            setting.setSettingType(Constants.SETTING_APP_NAVIGATION);
        }
        if (StringUtils.isNotEmpty(setting.getTheme())) {
            setting.setExtend4(setting.getTheme());
        }
        setting.setDisplay(EnableEnum.ENABLED.code());
        List<TenantSetting> list = tenantSettingService.getTenantSetting(setting);
        /* 查询到到导航栏为空，且传入的主题不为空，则为租户初使化对应主题的导航栏 */
        if (CollectionUtils.isEmpty(list) && StringUtils.isNotEmpty(setting.getTheme())) {
            if (tenantSettingService.isExistTenantTheme(setting.getTheme())) {
                tenantSettingService.initTenantNavigation(setting.getTheme(), setting.getSettingType());
            }
        }
        return list;
    }

    /**
     * 启动图配置列表查询
     */
    @RequestMapping("/getStartImagePage")
    @ApiOperation("启动图配置列表查询")
    public IPage<TenantSetting> getStartImagePage(
            PageDTO<TenantSetting> page, TenantSetting tenantSetting) {
        tenantSetting.setSettingType(TenantSettingEnum.START_UP_IMAGE.getCode());
        return tenantSettingService.getStartImagePage(page, tenantSetting);
    }

    /**
     * 启动图配置新增/修改
     */
    @RequestMapping("/insertStartImagePage")
    @ApiOperation("启动图配置新增/修改")
    public Result<Object> insertStartImagePage(@RequestBody TenantSetting tenantSetting) {
        tenantSetting.setSettingType(TenantSettingEnum.START_UP_IMAGE.getCode());
        Date date = new Date();
        tenantSetting.setCreateTime(date);
        tenantSetting.setUpdateTime(date);
        UserCredential user = SecurityUserHolder.getCredential();
        if (user != null) {
            tenantSetting.setCreateBy(user.getUsername());
            tenantSetting.setUpdateBy(user.getUsername());
        }
        tenantSettingService.insertStartImagePage(tenantSetting);
        adminCache.deleteByPrefix(CacheKey.getStartImgListKey());
        return Result.succeed();
    }

    /**
     * 启动图配置删除
     */
    @RequestMapping("/deleteStartImagePage")
    @ApiOperation("启动图配置删除")
    public Result<Object> deleteStartImagePage(@RequestParam(value = "id") int id) {
        TenantSetting tenantSetting = new TenantSetting();
        tenantSetting.setSettingType(TenantSettingEnum.START_UP_IMAGE.getCode());
        tenantSetting.setId(id);
        tenantSettingService.deleteStartImagePage(tenantSetting);
        adminCache.deleteByPrefix(CacheKey.getStartImgListKey());
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
            tenantSettingVO.setDisplay(EnableEnum.ENABLED.code());
            return Result.succeedData(tenantSettingService.getTenantSetting(tenantSettingVO));
        }
        return Result.succeedData(tenantSettingService.getAppNavigation(tenantSettingVO));
    }

    /**
     * 修改显示与排序
     */
    @RequestMapping("/updateDisplayAndSort")
    @ApiOperation("修改显示与排序")
    public Result updateDisplayAndSort(@RequestBody TenantSettingVO tenantSettingVO) {
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
        tenantSettingService.updateAppNavigation(tenantSettingVO);
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
    public Result updatePersonalCenter(@RequestBody TenantSettingVO tenantSettingVO) {
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
        tenantSettingService.updateAppNavigation(tenantSettingVO);
        adminCache.deleteByPrefix(CacheKey.getPersonCenterListKey());
        return Result.succeed();
    }

    /**
     * 批量修改排序
     */
    @RequestMapping("/updateBatchTenantSort")
    @ApiOperation("批量修改排序")
    public Result updateBatchTenantSetting(@RequestBody List<TenantSetting> tenantSettings) {
        tenantSettingService.updateBatchTenantSetting(tenantSettings);
        adminCache.deleteByPrefix(CacheKey.getTenantNavPrefixKey());
        return Result.succeed();
    }

    /**
     * 删除游戏浮窗类型
     */
    @DeleteMapping("/delById")
    @ApiOperation("删除游戏浮窗类型")
    public Result remove(Integer id) {
        tenantSettingService.deleteSysFloatById(id);
        adminCache.deleteByPrefix(CacheKey.getTenantFloatPrefixKey());
        return Result.succeed();
    }

    /**
     * 广场的开关开启/关闭
     */
    @RequestMapping("/updateSquareSwitch")
    @CacheEvict(cacheNames = Constants.TENANT_SQUARE_SWITCH, allEntries = true)
    public Result updateSquareSwitch(TenantSettingVO tenantSetting) {
        tenantSetting.setSettingType(Constants.SYSTEM_SETTING);
        tenantSetting.setSettingCode(Constants.SQUARE_SWITCH);
        if (org.apache.commons.lang3.StringUtils.isEmpty(tenantSetting.getSettingValue())) {
            return Result.failed("修改值不允许为空...");
        }
        tenantSettingService.updateTenantSettingValue(tenantSetting);
        adminCache.deleteObject(CacheKey.getSquareEnableKey());
        return Result.succeed();
    }

    /**
     * 获取广场的开关
     */
    @RequestMapping("/getSquareSwitch")
    @PreAuthorize("hasAuthority('system:teant:..zgetSquareSwitch')")
    public Result getSquareSwitch(TenantSettingVO sysTenantSetting) {
        sysTenantSetting.setSettingType(Constants.SYSTEM_SETTING);
        sysTenantSetting.setSettingCode(Constants.SQUARE_SWITCH);
        List<TenantSetting> list = tenantSettingService.getTenantSetting(sysTenantSetting);
        return Result.succeed(CollectionUtils.isNotEmpty(list) ? list.get(0) : null);
    }

    /**
     * 获取体育设置相关内容
     */
    @RequestMapping("/getSportConfig")
    public Result getSportConfig() {
        return Result.succeed(tenantSettingService.getSportConfig());
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
        return Result.succeed(tenantSettingService.updateListSortConfig(listSortConfigVOS));
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
        return Result.succeed(tenantSettingService.updateSportConfig(sportConfigVo));
    }
}
