package com.gameplat.admin.controller.open.setting;

import com.gameplat.admin.constant.Constants;
import com.gameplat.admin.model.vo.TenantSettingVO;
import com.gameplat.admin.service.TenantSettingService;
import com.gameplat.base.common.web.Result;
import com.gameplat.model.entity.setting.TenantSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 获取租户主题列表
     */
    @RequestMapping("/theme/list")
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

}
