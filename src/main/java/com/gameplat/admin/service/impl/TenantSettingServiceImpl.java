package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.Constants;
import com.gameplat.admin.mapper.TenantSettingMapper;
import com.gameplat.admin.model.vo.TenantSettingVO;
import com.gameplat.admin.service.TenantSettingService;
import com.gameplat.model.entity.setting.TenantSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author martin
 * @description
 * @date 2022/3/10
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class TenantSettingServiceImpl extends ServiceImpl<TenantSettingMapper, TenantSetting> implements TenantSettingService {

    @Override
    public List<TenantSetting> getTenantSetting(TenantSettingVO query) {
        LambdaQueryChainWrapper<TenantSetting> lambdaQuery = this.lambdaQuery();
        if (StringUtils.isNotEmpty(query.getSettingType())) {
            lambdaQuery.eq(TenantSetting::getSettingType, query.getSettingType());
        }
        if (StringUtils.isNotEmpty(query.getSettingCode())) {
            lambdaQuery.eq(TenantSetting::getSettingCode, query.getSettingCode());
        }
        if (Objects.nonNull(query.getDisplay())) {
            lambdaQuery.eq(TenantSetting::getDisplay, query.getDisplay());
        }
        if (StringUtils.isNotEmpty(query.getExtend4())) {
            lambdaQuery.eq(TenantSetting::getExtend4, query.getExtend4());
        }
        lambdaQuery.orderByAsc(TenantSetting::getSort);
        return lambdaQuery.list();
    }

    @Override
    public boolean isExistTenantTheme(String theme) {
        List<TenantSetting> list = this.lambdaQuery().
                eq(TenantSetting::getSettingType, Constants.TEMPLATE_CONFIG_THEME).
                eq(TenantSetting::getSettingCode, theme).list();
        return CollectionUtils.isNotEmpty(list) ? true : false;
    }

    @Override
    public int initTenantNavigation(String theme, String settingType) {
        return this.getBaseMapper().initTenantNavigation(theme, settingType);
    }
}
