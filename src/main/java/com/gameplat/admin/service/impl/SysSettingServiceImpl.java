package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
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
import com.gameplat.admin.mapper.SysSettingMapper;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.GameConfigService;
import com.gameplat.admin.service.SysSettingService;
import com.gameplat.base.common.context.DyDataSourceContextHolder;
import com.gameplat.base.common.context.StrategyContext;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.setting.SysSetting;
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
 * @author lily
 * @description
 * @date 2022/2/16
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysSettingServiceImpl extends ServiceImpl<SysSettingMapper, SysSetting> implements SysSettingService {

    @Resource
    private SysSettingMapper sysSettingMapper;

    @Resource
    private SportFeignClient sportFeignClient;

    @Resource
    private GameConfigService gameConfigService;

    @Override
    public void updateChatEnable(String cpChatEnable) {
        SysSetting sportConfig = getSportConfigSetting();
        JSONObject json = JSONObject.parseObject(sportConfig.getSettingValue());
        json.remove("cpChatEnable");
        json.put("cpChatEnable", cpChatEnable);

        sportConfig.setSettingValue(json.toJSONString());
        updateSportConfig(sportConfig);
    }

    @Override
    public SysSetting getSportConfigSetting() {
        return this.lambdaQuery().eq(SysSetting::getSettingType, "sport_config").one();
    }

    @Override
    public void updateSportConfig(SysSetting sysSetting) {
        LambdaUpdateWrapper<SysSetting> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysSetting::getSettingType, "sport_config").set(ObjectUtil.isNotEmpty(sysSetting.getSettingValue()), SysSetting::getSettingValue, sysSetting.getSettingValue());
        update(updateWrapper);
    }

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
    public List<SysSetting> getTenantSetting(SysSettingVO query) {
        LambdaQueryChainWrapper<SysSetting> lambdaQuery = this.lambdaQuery();
        if (StringUtils.isNotEmpty(query.getSettingType())) {
            lambdaQuery.eq(SysSetting::getSettingType, query.getSettingType());
        }
        if (StringUtils.isNotEmpty(query.getSettingCode())) {
            lambdaQuery.eq(SysSetting::getSettingCode, query.getSettingCode());
        }
        if (Objects.nonNull(query.getDisplay())) {
            lambdaQuery.eq(SysSetting::getDisplay, query.getDisplay());
        }
        if (StringUtils.isNotEmpty(query.getExtend4())) {
            lambdaQuery.eq(SysSetting::getExtend4, query.getExtend4());
        }
        lambdaQuery.orderByAsc(SysSetting::getSort);
        return lambdaQuery.list();
    }

    @Override
    public boolean isExistTenantTheme(String theme) {
        return CollectionUtils.isNotEmpty(this.lambdaQuery().eq(SysSetting::getSettingType, Constants.TEMPLATE_CONFIG_THEME).eq(SysSetting::getSettingCode, theme).list());
    }

    @Override
    public int initTenantNavigation(String theme, String settingType) {
        return this.getBaseMapper().initTenantNavigation(theme, settingType);
    }

    @Override
    public IPage<SysSetting> getStartImagePage(IPage<SysSetting> page, SysSetting sysSetting) {
        LambdaQueryWrapper<SysSetting> queryWrapper = new LambdaQueryWrapper<SysSetting>();
        queryWrapper.eq(null != sysSetting.getId(), SysSetting::getId, sysSetting.getId());
        queryWrapper.eq(null != sysSetting.getDisplay(), SysSetting::getDisplay, sysSetting.getDisplay());
        queryWrapper.eq(StringUtils.isNotBlank(sysSetting.getSettingType()), SysSetting::getSettingType, sysSetting.getSettingType());
        queryWrapper.eq(StringUtils.isNotBlank(sysSetting.getSettingCode()), SysSetting::getSettingCode, sysSetting.getSettingCode());
        queryWrapper.eq(StringUtils.isNotBlank(sysSetting.getSettingValue()), SysSetting::getSettingValue, sysSetting.getSettingValue());
        queryWrapper.eq(StringUtils.isNotBlank(sysSetting.getSettingLabel()), SysSetting::getSettingLabel, sysSetting.getSettingLabel());
        queryWrapper.orderByDesc(SysSetting::getCreateTime);
        return this.page(page, queryWrapper);
    }

    @Override
    public void insertStartImagePage(SysSetting sysSetting) {
        LambdaQueryWrapper<SysSetting> queryWrapper = new LambdaQueryWrapper<SysSetting>();
        queryWrapper.eq(SysSetting::getSettingType, sysSetting.getSettingType());
        queryWrapper.eq(SysSetting::getDisplay, 1);
        List<SysSetting> list = this.list(queryWrapper);
        if (null == sysSetting.getId()) {
            if (null != sysSetting.getDisplay() && 1 == sysSetting.getDisplay()) {
                if (!CollectionUtils.isEmpty(list) && list.size() > 2) {
                    throw new ServiceException("开启的图片/视频已达上线 (最多开启三个)");
                }
            }
            queryWrapper.eq(SysSetting::getCreateTime, new Date());
            // 新增
            this.save(sysSetting);
        } else {
            boolean flag = true;
            if (!CollectionUtils.isEmpty(list) && list.size() > 2 && null != sysSetting.getDisplay() && 1 == sysSetting.getDisplay()) {
                for (SysSetting e : list) {
                    if (sysSetting.getId().equals(e.getId())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    throw new ServiceException("开启的图片/视频已达上线 (最多开启三个)");
                }
            }
            queryWrapper.eq(SysSetting::getUpdateTime, new Date());
            this.updateById(sysSetting);
        }
    }

    @Override
    public void deleteStartImagePage(SysSetting sysSetting) {
        LambdaQueryWrapper<SysSetting> queryWrapper = new LambdaQueryWrapper<SysSetting>();
        queryWrapper.eq(SysSetting::getId, sysSetting.getId());
        queryWrapper.eq(SysSetting::getSettingType, sysSetting.getSettingType());
        this.remove(queryWrapper);
    }

    @Override
    public List<SysSetting> getTenantSetting(SysSetting sysSetting) {
        LambdaQueryWrapper<SysSetting> queryWrapper = new LambdaQueryWrapper<SysSetting>();
        queryWrapper.eq(SysSetting::getDisplay, sysSetting.getDisplay());
        queryWrapper.eq(SysSetting::getSettingType, sysSetting.getSettingType());
        return this.list(queryWrapper);
    }

    @Override
    public List<SysSetting> getAppNavigation(SysSettingVO vo) {
        if (vo.getSettingType().equals(Constants.TEMPLATE_CONFIG_THEME)) {
            // 查询主题模板只取租户开启的主题
            vo.setDisplay(1);
        } else if (vo.getSettingType().equals(Constants.SETTING_H5_NAVIGATION) || vo.getSettingType().equals(Constants.SETTING_APP_NAVIGATION)) {
            // 如果没传主题，传默认模板
            if (StringUtils.isBlank(vo.getExtend4())) {
                vo.setTenant("default");
            }
        }
        List<SysSetting> list = sysSettingMapper.getBackendAppNavigationList(vo);
        if (list.isEmpty()) {
            // 如果查询为空，读取模板数据并插入返回
            LambdaQueryWrapper<SysSetting> queryWrapper = new LambdaQueryWrapper<SysSetting>();
            queryWrapper.eq(SysSetting::getTenant, "default");
            queryWrapper.eq(SysSetting::getSettingType, vo.getSettingType());

            list = this.list(queryWrapper);
            list.forEach(x -> {
                x.setExtend4(vo.getExtend4());
                x.setTenant(getDBSuffix());
            });
            if (!list.isEmpty()) {
                sysSettingMapper.insetGameList(list);
            }
            list = sysSettingMapper.getBackendAppNavigationList(vo);
        }
        return list;
    }

    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
    @Override
    public void updateAppNavigation(SysSettingVO vo) {
        // 查询游戏必要code
        if (Constants.SETTING_APP_NAVIGATION.equals(vo.getSettingType()) || Constants.SETTING_H5_NAVIGATION.equals(vo.getSettingType())) {
            if (StringUtils.isNotBlank(vo.getExtend2())) {
                GameKindVO gameList = sysSettingMapper.getGameList(vo.getExtend2());
                JSONObject json = new JSONObject();
                json.put("platformCode",gameList.getPlatformCode());
                json.put("playCode",gameList.getCode());
                json.put("gameType",gameList.getGameType());
                json.put("demoEnable",gameList.getDemoEnable());
                vo.setExtend3(json.toJSONString());
            }
        }
        // 广场导航栏选择一个首页后，其他选择的首页自动置为0
        else if (Constants.SETTING_SQUARE_NAVIGATION.equals(vo.getSettingType()) && vo.getIsIndex() == 1) {
            SysSetting sysSetting = new SysSetting();
            sysSetting.setSettingType(Constants.SETTING_SQUARE_NAVIGATION);
            sysSetting.setIsIndex(0);
            sysSettingMapper.updateIndex(sysSetting);
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

    public void updateBatchTenantSetting(List<SysSetting> sysSettings) {
        if (sysSettings == null || sysSettings.isEmpty()) {
            throw new ServiceException("数据错误");
        }
        LambdaQueryWrapper<SysSetting> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysSetting::getId, sysSettings.get(0).getId());
        this.getOne(queryWrapper);
        this.updateBatchById(sysSettings);
    }

    @Override
    public void deleteSysFloatById(Integer id) {
        this.removeById(id);
    }

    @Override
    public void updateTenantSettingValue(SysSettingVO tenantSetting) {
        LambdaUpdateWrapper<SysSetting> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysSetting::getSettingType, tenantSetting.getSettingType()).eq(SysSetting::getSettingCode, tenantSetting.getSettingCode()).set(SysSetting::getSettingValue, tenantSetting.getSettingValue()).set(SysSetting::getUpdateTime, new Date());
        update(updateWrapper);
    }

    @Override
    public SportConfigValueVO getSportConfig() {
        SysSetting sportConfig = sysSettingMapper.getSportConfig();
        SportConfigValueVO sportConfigValueVO = null;
        if (sportConfig != null && StringUtils.isNotEmpty(sportConfig.getSettingValue())) {
            String json = sportConfig.getSettingValue();
            sportConfigValueVO = JSON.parseObject(json, SportConfigValueVO.class);
        }
        //没数据则初始化数据
        if (sportConfigValueVO == null) {
            initSportConfig();
            sportConfig = sysSettingMapper.getSportConfig();
            String json = sportConfig.getSettingValue();
            sportConfigValueVO = JSON.parseObject(json, SportConfigValueVO.class);
        }
        Integer scene = sportConfigValueVO.getScene();
        if (scene != null && scene == 6) {
            SysSetting sysSetting;
            List<SysSetting> listSortConfigs = sysSettingMapper.getTenantSetting(new SysSetting() {{
                setSettingType(Constants.SPORT_CONFIG_TYPE);
                setSettingCode(Constants.LIST_SORT_CODE);
            }});
            if (listSortConfigs.isEmpty()) {
                sysSetting = initListSortConfig();
            } else {
                sysSetting = listSortConfigs.get(0);
            }
            String valueJson = sysSetting.getSettingValue();
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
        SysSetting sysSetting = new SysSetting();
        sysSetting.setSettingValue(settingValue);
        return sysSettingMapper.updateListSortConfig(sysSetting);
    }


    /**
     * 初始化体育数据
     */
    private void initSportConfig() {
        SysSetting sysSetting = new SysSetting();
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
        sysSetting.setSettingValue(settingValue);
        sysSetting.setSettingType(Constants.SPORT_CONFIG_TYPE);
        sysSetting.setSettingCode(Constants.SPORT_CONFIG_CODE);
        sysSetting.setSettingLabel(Constants.SPORT_CONFIG_DESC);
        UserCredential userCredential = SecurityUserHolder.getCredential();
        if (userCredential != null) {
            sysSetting.setCreateBy(userCredential.getUsername());
        }
        sysSettingMapper.initSportConfig(sysSetting);
    }

    /**
     * 插入球头信息
     *
     * @param sportConfigValueVo
     * @return
     */
    public int saveSportBallHead(SportConfigValueVO sportConfigValueVo) {
        try {
            JSONObject config = gameConfigService.getGameConfig("SB");
            String tenant = "";
            log.info("体育服配置结果为{}", config);
            if (config != null) {
                tenant = config.getString("tenant");
            }
            Map<String, String> params = new HashMap<>(8);
            params.put("style", sportConfigValueVo.getStyle());
            params.put("ballHeadRule", sportConfigValueVo.getBallHeadRule());
            params.put("sportBallNavigation", sportConfigValueVo.getSportBallNavigation());
            params.put("sportLeagueNavigation", sportConfigValueVo.getSportLeagueNavigation());
            params.put("tenant", tenant);
            HashMap<String, String> headers = new HashMap<>();
            headers.put("tenant", tenant);
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
    private SysSetting initListSortConfig() {
        ArrayList<ListSortConfigVO> list = new ArrayList<>();
        list.add(new ListSortConfigVO(1, "导航", true, "备注:导航", "navigation"));
        list.add(new ListSortConfigVO(2, "banner", true, "备注:banner", "banner"));
        list.add(new ListSortConfigVO(3, "游戏列表", true, "备注:游戏列表", "gameList"));
        list.add(new ListSortConfigVO(4, "彩系列表", true, "备注:彩系列表", "lotteryList"));
        list.add(new ListSortConfigVO(5, "中奖记录", true, "备注:中奖记录", "winList"));

        String settingValue = JSONObject.toJSONString(list);
        SysSetting sysSetting = new SysSetting();
        sysSetting.setSettingValue(settingValue);
        sysSetting.setSettingType(Constants.SPORT_CONFIG_TYPE);
        sysSetting.setSettingCode(Constants.LIST_SORT_CODE);
        sysSetting.setSettingLabel(Constants.LIST_SORT_DESC);
        UserCredential userCredential = SecurityUserHolder.getCredential();
        if (userCredential != null) {
            sysSetting.setCreateBy(userCredential.getUsername());
        }
        int i = sysSettingMapper.initSportConfig(sysSetting);
        if (i != 1) {
            throw new ServiceException("初始化数据异常");
        }
        return sysSetting;
    }

    @Override
    public int updateSportConfig(SportConfigVO sportConfigVo) {
        SysSetting sysSetting = new SysSetting();
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
        sysSetting.setSettingValue(settingValue);
        return sysSettingMapper.updateSportConfig(sysSetting);
    }
}
