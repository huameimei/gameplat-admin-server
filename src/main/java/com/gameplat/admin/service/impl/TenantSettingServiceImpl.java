package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.Constants;
import com.gameplat.admin.enums.ListSortTypeEnum;
import com.gameplat.admin.feign.SportFeignClient;
import com.gameplat.admin.mapper.TenantSettingMapper;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.TenantSettingService;
import com.gameplat.base.common.context.DyDataSourceContextHolder;
import com.gameplat.base.common.context.StrategyContext;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.setting.TenantSetting;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author james
 * @date 2022/3/10
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class TenantSettingServiceImpl extends ServiceImpl<TenantSettingMapper, TenantSetting>
        implements TenantSettingService {

    @Resource
    private TenantSettingMapper tenantSettingMapper;

    @Resource
    private SportFeignClient sportFeignClient;

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
        queryWrapper.orderByDesc(TenantSetting::getCreateTime);
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
            queryWrapper.eq(TenantSetting::getCreateTime, new Date());
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
            queryWrapper.eq(TenantSetting::getUpdateTime, new Date());
            this.updateById(tenantSetting);
        }
    }

    @Override
    public void deleteStartImagePage(TenantSetting tenantSetting) {
        LambdaQueryWrapper<TenantSetting> queryWrapper = new LambdaQueryWrapper<TenantSetting>();
        queryWrapper.eq(TenantSetting::getId, tenantSetting.getId());
        queryWrapper.eq(TenantSetting::getSettingType, tenantSetting.getSettingType());
        this.remove(queryWrapper);
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
                tenantSettingMapper.insetGameList(list);
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
            TenantSetting tenantSetting = new TenantSetting();
            tenantSetting.setSettingType(Constants.SETTING_SQUARE_NAVIGATION);
            tenantSetting.setIsIndex(0);
            tenantSettingMapper.updateIndex(tenantSetting);
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

    @Override

    public void updateBatchTenantSetting(List<TenantSetting> tenantSettings) {
        if (tenantSettings == null || tenantSettings.isEmpty()) {
            throw new ServiceException("数据错误");
        }
        LambdaQueryWrapper<TenantSetting> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantSetting::getId, tenantSettings.get(0).getId());
        this.getOne(queryWrapper);
        this.updateBatchById(tenantSettings);
    }

    @Override
    public void deleteSysFloatById(Integer id) {
        this.removeById(id);
    }

    @Override
    public void updateTenantSettingValue(TenantSettingVO tenantSetting) {
        LambdaUpdateWrapper<TenantSetting> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(TenantSetting::getSettingType, tenantSetting.getSettingType())
                .eq(TenantSetting::getSettingCode, tenantSetting.getSettingCode())
                .set(TenantSetting::getSettingValue, tenantSetting.getSettingValue())
                .set(TenantSetting::getUpdateTime, new Date());
        update(updateWrapper);
    }

    @Override
    public SportConfigValueVO getSportConfig() {
        TenantSetting sportConfig = tenantSettingMapper.getSportConfig();
        SportConfigValueVO sportConfigValueVO = null;
        if (sportConfig != null && StringUtils.isNotEmpty(sportConfig.getSettingValue())) {
            String json = sportConfig.getSettingValue();
            sportConfigValueVO = JSON.parseObject(json, SportConfigValueVO.class);
        }
        //没数据则初始化数据
        if (sportConfigValueVO == null) {
            initSportConfig();
            sportConfig = tenantSettingMapper.getSportConfig();
            String json = sportConfig.getSettingValue();
            sportConfigValueVO = JSON.parseObject(json, SportConfigValueVO.class);
        }
        Integer scene = sportConfigValueVO.getScene();
        if (scene != null && scene == 6) {
            TenantSetting tenantSetting;
            List<TenantSetting> listSortConfigs = tenantSettingMapper.getTenantSetting(new TenantSetting() {{
                setSettingType(Constants.SPORT_CONFIG_TYPE);
                setSettingCode(Constants.LIST_SORT_CODE);
            }});
            if (listSortConfigs.isEmpty()) {
                tenantSetting = initListSortConfig();
            } else {
                tenantSetting = listSortConfigs.get(0);
            }
            String valueJson = tenantSetting.getSettingValue();
            List<ListSortConfigVO> list = JSON.parseArray(valueJson, ListSortConfigVO.class);
            list.sort(Comparator.comparingInt(ListSortConfigVO::getSort));
            sportConfigValueVO.setListSortConfigs(list);
        }
        return sportConfigValueVO;
    }

    @Override
    public int updateListSortConfig(List<ListSortConfigVO> listSortConfigVOS) {
        ListSortTypeEnum[] values = ListSortTypeEnum.values();
        List<String> types = Arrays.stream(values).map(e -> e.type).collect(Collectors.toList());
        for (ListSortConfigVO configVO : listSortConfigVOS) {
            if (org.apache.commons.lang3.StringUtils.isBlank(configVO.getType()) || !types.contains(configVO.getType())) {
                throw new ServiceException(String.format("%s type错误", configVO.getName()));
            }
        }
        String settingValue = JSONObject.toJSONString(listSortConfigVOS);
        TenantSetting tenantSetting = new TenantSetting();
        tenantSetting.setSettingValue(settingValue);
        return tenantSettingMapper.updateListSortConfig(tenantSetting);
    }


    /**
     * 初始化体育数据
     */
    private void initSportConfig() {
        TenantSetting tenantSetting = new TenantSetting();
        SportConfigValueVO sportConfigValueVo = new SportConfigValueVO();
        sportConfigValueVo.setH5ActivityImg("");
        sportConfigValueVo.setAppActivityImg("");
        sportConfigValueVo.setCustomerUrl("");
        sportConfigValueVo.setCustomerDownloadUrl("");
        sportConfigValueVo.setScene(1);
        sportConfigValueVo.setHandicap(1);
        sportConfigValueVo.setCpChatEnable("off");
        sportConfigValueVo.setStyle("1");
        sportConfigValueVo.setBallHeadRule("1");
        sportConfigValueVo.setSportBallNavigation("1");
        sportConfigValueVo.setSportLeagueNavigation("0");
        //保存数据到体育服
        int rst = saveSportBallHead(sportConfigValueVo);
        if (rst == 1) {
            log.info("球头配置初始化写入到体育服成功");
        }
        String settingValue = JSONObject.toJSONString(sportConfigValueVo);
        tenantSetting.setSettingValue(settingValue);
        tenantSetting.setSettingType(Constants.SPORT_CONFIG_TYPE);
        tenantSetting.setSettingCode(Constants.SPORT_CONFIG_CODE);
        tenantSetting.setSettingLabel(Constants.SPORT_CONFIG_DESC);
        UserCredential userCredential = SecurityUserHolder.getCredential();
        if (userCredential != null) {
            tenantSetting.setCreateBy(userCredential.getUsername());
        }
        tenantSettingMapper.initSportConfig(tenantSetting);
    }

    /**
     * 插入球头信息
     * @param sportConfigValueVo
     * @return
     */
    public int saveSportBallHead(SportConfigValueVO sportConfigValueVo) {
        try {
            Map<String, String> params = new HashMap<>(8);
            params.put("style", sportConfigValueVo.getStyle());
            params.put("ballHeadRule", sportConfigValueVo.getBallHeadRule());
            params.put("sportBallNavigation", sportConfigValueVo.getSportBallNavigation());
            params.put("sportLeagueNavigation", sportConfigValueVo.getSportLeagueNavigation());
            params.put("tenant", getDBSuffix());
            HashMap<String, String> headers = new HashMap<>();
            headers.put("tenant", getDBSuffix());
            String result = sportFeignClient.updateAppConfig(headers, params);
            if (org.apache.commons.lang3.StringUtils.isBlank(result)) {
                log.info("体育插入球头配置接口响应结果为空{}", sportConfigValueVo);
                return 0;
            }
            JSONObject resultJson = JSONObject.parseObject(result);
            if ("1".equals(resultJson.getString("code"))) {
                log.info("体育插入球头配置接口响应成功{}", result);
                return 1;
            }
        } catch (Exception ex) {
            log.info("体育插入球头配置接口报错，{}, 异常{}", sportConfigValueVo, ex);
        }
        return 0;
    }

    /**
     * 初始化开关与排序列表数据
     */
    private TenantSetting initListSortConfig() {
        ArrayList<ListSortConfigVO> list = new ArrayList<>();
        list.add(new ListSortConfigVO(1, "导航", true,"备注:导航","navigation"));
        list.add(new ListSortConfigVO(2, "banner", true,"备注:banner","banner"));
        list.add(new ListSortConfigVO(3, "游戏列表", true,"备注:游戏列表","gameList"));
        list.add(new ListSortConfigVO(4, "彩系列表", true,"备注:彩系列表", "lotteryList"));
        list.add(new ListSortConfigVO(5, "中奖记录", true,"备注:中奖记录", "winList"));

        String settingValue = JSONObject.toJSONString(list);
        TenantSetting tenantSetting = new TenantSetting();
        tenantSetting.setSettingValue(settingValue);
        tenantSetting.setSettingType(Constants.SPORT_CONFIG_TYPE);
        tenantSetting.setSettingCode(Constants.LIST_SORT_CODE);
        tenantSetting.setSettingLabel(Constants.LIST_SORT_DESC);
        UserCredential userCredential = SecurityUserHolder.getCredential();
        if (userCredential != null) {
            tenantSetting.setCreateBy(userCredential.getUsername());
        }
        int i = tenantSettingMapper.initSportConfig(tenantSetting);
        if (i != 1) {
            throw new ServiceException("初始化数据异常");
        }
        return tenantSetting;
    }

    @Override
    public int updateSportConfig(SportConfigVO sportConfigVo) {
        TenantSetting tenantSetting = new TenantSetting();
        SportConfigValueVO sportConfigValueVo = new SportConfigValueVO();
        sportConfigValueVo.setH5ActivityImg(sportConfigVo.getH5ActivityImg());
        sportConfigValueVo.setAppActivityImg(sportConfigVo.getAppActivityImg());
        sportConfigValueVo.setCustomerUrl(sportConfigVo.getCustomerUrl());
        sportConfigValueVo.setCustomerDownloadUrl(sportConfigVo.getCustomerDownloadUrl());
        sportConfigValueVo.setScene(sportConfigVo.getScene());
        sportConfigValueVo.setHandicap(sportConfigVo.getHandicap());
        sportConfigValueVo.setCpChatEnable(sportConfigVo.getCpChatEnable());

        sportConfigValueVo.setStyle(sportConfigVo.getStyle());
        sportConfigValueVo.setBallHeadRule(sportConfigVo.getBallHeadRule());
        sportConfigValueVo.setSportBallNavigation(sportConfigVo.getSportBallNavigation());
        sportConfigValueVo.setSportLeagueNavigation(sportConfigVo.getSportLeagueNavigation());
        //保存数据到体育服
        int rst = saveSportBallHead(sportConfigValueVo);
        if (rst == 1) {
            log.info("球头配置写入到体育服成功");
        }
        String settingValue = JSONObject.toJSONString(sportConfigValueVo);
        tenantSetting.setSettingValue(settingValue);
        return tenantSettingMapper.updateSportConfig(tenantSetting);
    }
}
