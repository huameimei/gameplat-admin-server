package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.Constants;
import com.gameplat.admin.mapper.TenantSettingMapper;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.admin.model.vo.TenantSettingVO;
import com.gameplat.admin.service.TenantSettingService;
import com.gameplat.base.common.context.DyDataSourceContextHolder;
import com.gameplat.base.common.context.StrategyContext;
import com.gameplat.model.entity.setting.TenantSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author martin
 * @date 2022/3/10
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class TenantSettingServiceImpl extends ServiceImpl<TenantSettingMapper, TenantSetting>
    implements TenantSettingService {

  @Resource private TenantSettingMapper tenantSettingMapper;

  /**
   * 从当前访问线程中获取租户数据源标识设
   *
   * @return String
   */
  public static String getDBSuffix() {
    StrategyContext strategy = DyDataSourceContextHolder.getStrategyContext();
    return strategy == null ? null : strategy.getDbSuffix();
  }

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
    return CollectionUtils.isNotEmpty(
        this.lambdaQuery()
            .eq(TenantSetting::getSettingType, Constants.TEMPLATE_CONFIG_THEME)
            .eq(TenantSetting::getSettingCode, theme)
            .list());
  }

  @Override
  public int initTenantNavigation(String theme, String settingType) {
    return this.getBaseMapper().initTenantNavigation(theme, settingType);
  }

  @Override
  public IPage<TenantSetting> getStartImagePage(
      IPage<TenantSetting> page, TenantSetting tenantSetting) {
    LambdaQueryWrapper<TenantSetting> queryWrapper = new LambdaQueryWrapper<TenantSetting>();
    queryWrapper.eq(null != tenantSetting.getId(), TenantSetting::getId, tenantSetting.getId());
    queryWrapper.eq(
        null != tenantSetting.getDisplay(), TenantSetting::getDisplay, tenantSetting.getDisplay());
    queryWrapper.eq(
        StringUtils.isNotBlank(tenantSetting.getSettingType()),
        TenantSetting::getSettingType,
        tenantSetting.getSettingType());
    queryWrapper.eq(
        StringUtils.isNotBlank(tenantSetting.getSettingCode()),
        TenantSetting::getSettingCode,
        tenantSetting.getSettingCode());
    queryWrapper.eq(
        StringUtils.isNotBlank(tenantSetting.getSettingValue()),
        TenantSetting::getSettingValue,
        tenantSetting.getSettingValue());
    queryWrapper.eq(
        StringUtils.isNotBlank(tenantSetting.getSettingLabel()),
        TenantSetting::getSettingLabel,
        tenantSetting.getSettingLabel());
    return this.page(page, queryWrapper);
  }

  @Override
  public void insertStartImagePage(TenantSetting tenantSetting) {
    LambdaQueryWrapper<TenantSetting> queryWrapper = new LambdaQueryWrapper<TenantSetting>();
    queryWrapper.eq(TenantSetting::getSettingType, tenantSetting.getSettingType());
    queryWrapper.eq(TenantSetting::getDisplay, 1);
    List<TenantSetting> list = this.list(queryWrapper);
    if (null == tenantSetting.getId()) {
      if (null != tenantSetting.getDisplay() && 1 == tenantSetting.getDisplay()) {
        if (!CollectionUtils.isEmpty(list) && list.size() > 2) {
          throw new ServiceException("开启的图片/视频已达上线 (最多开启三个)");
        }
      }
      // 新增
      this.save(tenantSetting);
    } else {
      boolean flag = true;
      if (!CollectionUtils.isEmpty(list)
          && list.size() > 2
          && null != tenantSetting.getDisplay()
          && 1 == tenantSetting.getDisplay()) {
        for (TenantSetting e : list) {
          if (tenantSetting.getId().equals(e.getId())) {
            flag = false;
            break;
          }
        }
        if (flag) {
          throw new ServiceException("开启的图片/视频已达上线 (最多开启三个)");
        }
      }
      this.updateById(tenantSetting);
    }
  }

  @Override
  public void deleteStartImagePage(TenantSetting tenantSetting) {
    LambdaQueryWrapper<TenantSetting> queryWrapper = new LambdaQueryWrapper<TenantSetting>();
    queryWrapper.eq(TenantSetting::getId, tenantSetting.getId());
    queryWrapper.eq(TenantSetting::getSettingType, tenantSetting.getSettingType());
    this.removeById(queryWrapper);
  }

  @Override
  public List<TenantSetting> getTenantSetting(TenantSetting tenantSetting) {
    LambdaQueryWrapper<TenantSetting> queryWrapper = new LambdaQueryWrapper<TenantSetting>();
    queryWrapper.eq(TenantSetting::getDisplay, tenantSetting.getDisplay());
    queryWrapper.eq(TenantSetting::getSettingType, tenantSetting.getSettingType());
    return this.list(queryWrapper);
  }

  @Override
  public List<TenantSetting> getAppNavigation(TenantSettingVO vo) {
    if (vo.getSettingType().equals(Constants.TEMPLATE_CONFIG_THEME)) {
      // 查询主题模板只取租户开启的主题
      vo.setDisplay(1);
    } else if (vo.getSettingType().equals(Constants.SETTING_H5_NAVIGATION)
        || vo.getSettingType().equals(Constants.SETTING_APP_NAVIGATION)) {
      // 如果没传主题，传默认模板
      if (StringUtils.isBlank(vo.getExtend4())) {
        vo.setTenant("default");
      }
    }
    List<TenantSetting> list = tenantSettingMapper.getBackendAppNavigationList(vo);
    if (list.isEmpty()) {
      // 如果查询为空，读取模板数据并插入返回
      LambdaQueryWrapper<TenantSetting> queryWrapper = new LambdaQueryWrapper<TenantSetting>();
      queryWrapper.eq(TenantSetting::getTenant, "default");
      queryWrapper.eq(TenantSetting::getSettingType, vo.getSettingType());

      list = this.list(queryWrapper);
      list.forEach(
          x -> {
            x.setExtend4(vo.getExtend4());
            x.setTenant(getDBSuffix());
          });
      if (!list.isEmpty()) {
        this.saveBatch(list);
      }
      list = tenantSettingMapper.getBackendAppNavigationList(vo);
    }
    return list;
  }

  @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
  @Override
  public void updateAppNavigation(TenantSettingVO vo) {
    // 查询游戏必要code
    if (Constants.SETTING_APP_NAVIGATION.equals(vo.getSettingType())
        || Constants.SETTING_H5_NAVIGATION.equals(vo.getSettingType())) {
      if (StringUtils.isNotBlank(vo.getExtend2())) {
        GameKindVO gameList = tenantSettingMapper.getGameList(vo.getExtend2());
        vo.setExtend3(JSON.toJSONString(gameList));
      }
    }
    // 广场导航栏选择一个首页后，其他选择的首页自动置为0
    else if (Constants.SETTING_SQUARE_NAVIGATION.equals(vo.getSettingType())
        && vo.getIsIndex() == 1) {
      TenantSetting tenantSetting1 = new TenantSetting();
      tenantSetting1.setSettingType(Constants.SETTING_SQUARE_NAVIGATION);
      tenantSetting1.setIsIndex(0);
      tenantSettingMapper.updateIndex(tenantSetting1);
    }
    HashMap<Object, Object> map = new HashMap<>(8);
    map.put("en-US", vo.getEnUs());
    map.put("in-ID", vo.getInId());
    map.put("th-TH", vo.getThTh());
    map.put("vi-VN", vo.getViVn());
    map.put("zh-CN", vo.getZhCn());
    vo.setSettingValue(JSON.toJSONString(map));
    this.updateById(vo);
    // todo 刷新缓存
    // flushTenantSetting(tenantSetting);
  }

  @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
  @Override
  public void updateBatchTenantSetting(List<TenantSetting> tenantSettings) {
    if (tenantSettings == null || tenantSettings.isEmpty()) {
      throw new ServiceException("数据错误");
    }
    LambdaQueryWrapper<TenantSetting> queryWrapper = new LambdaQueryWrapper<TenantSetting>();
    queryWrapper.eq(TenantSetting::getId, tenantSettings.get(0).getId());
    TenantSetting sysTenantSetting = this.getOne(queryWrapper);
    this.updateBatchById(tenantSettings);
    // todo 刷新缓存
    // kgRedisService.flushTenantSetting(sysTenantSetting);
  }

  @Override
  public void deleteSysFloatById(Integer id) {
    this.removeById(id);
  }
}
