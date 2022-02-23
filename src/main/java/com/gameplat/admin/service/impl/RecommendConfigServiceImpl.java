package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.GameKindMapper;
import com.gameplat.admin.mapper.RecommendConfigMapper;
import com.gameplat.admin.mapper.SysDictDataMapper;
import com.gameplat.admin.model.domain.GameKind;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.domain.proxy.RecommendConfig;
import com.gameplat.admin.model.dto.RecommendConfigDto;
import com.gameplat.admin.model.vo.FissionDivideConfigVo;
import com.gameplat.admin.model.vo.FissionDivideLevelVo;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.service.RecommendConfigService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
public class RecommendConfigServiceImpl extends ServiceImpl<RecommendConfigMapper, RecommendConfig> implements RecommendConfigService {

    @Autowired
    private RecommendConfigMapper recommendConfigMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Autowired
    private GameKindMapper gameKindMapper;

    /**
     * 获取代理配置
     * @return
     */
    @Override
    public RecommendConfig getRecommendConfig() {
        return this.lambdaQuery()
                .orderByDesc(RecommendConfig::getId)
                .last("limit 1")
                .oneOpt().orElseThrow(() -> new ServiceException("代理限制信息不存在!"));
    }

    /**
     * 获取层层代分红模式配置预设
     * @param country
     * @return
     */
    @Override
    public Map<String, List<GameDivideVo>> getDefaultLayerDivideConfig(String lang) {
        Map<String, List<GameDivideVo>> tmpMap = new TreeMap<>();
        List<SysDictData> liveGameTypeList = sysDictDataMapper.findDataByType("LIVE_GAME_TYPE", "1");
        if (CollectionUtil.isEmpty(liveGameTypeList)) {
            log.error("游戏大类字典数据为空！");
            return new TreeMap<>();
        }
        RecommendConfig recommendConfig = this.getRecommendConfig();
        // 获取得到层层代分红模式预设配置
        String layerPresetValue = recommendConfig.getLayerPresetValue();
        if (StrUtil.isBlank(layerPresetValue)) {
            // 初始化
            layerPresetValue = this.initDivideConfig(liveGameTypeList, lang);
        }
        Map<String, JSONObject> ownerConfigMap = JSONUtil.toBean(layerPresetValue, Map.class);
        liveGameTypeList.forEach(gameType -> {
            List<GameDivideVo> divideLevelOneList = new ArrayList<>();
            for (Map.Entry<String, JSONObject> voMap: ownerConfigMap.entrySet()) {
                // 游戏大类编码相同
                if (gameType.getDictValue().equalsIgnoreCase(voMap.getValue().getStr("liveGameCode"))) {
                    voMap.getValue().put("maxRatio", new BigDecimal("100"));
                    voMap.getValue().put("minRatio", BigDecimal.ZERO);
                    divideLevelOneList.add(JSONUtil.toBean(voMap.getValue(), GameDivideVo.class));
                }
            }
            tmpMap.put(gameType.getDictLabel(), divideLevelOneList);
        });

        return tmpMap;
    }



    /**
     * 获取固定模式初始预设值
     * @param lang
     * @return
     */
    @Override
    public Map<String, List<GameDivideVo>> getDefaultFixDivideConfig(String lang) {
        Map<String, List<GameDivideVo>> tmpMap = new TreeMap<>();
        List<SysDictData> liveGameTypeList = sysDictDataMapper.findDataByType("LIVE_GAME_TYPE", "1");
        if (CollectionUtil.isEmpty(liveGameTypeList)) {
            log.error("游戏大类字典数据为空！");
            return new TreeMap<>();
        }
        RecommendConfig recommendConfig = this.getRecommendConfig();
        // 获取得到层层代分红模式预设配置
        String fixPresetValue = recommendConfig.getFixPresetValue();
        if (StrUtil.isBlank(fixPresetValue)) {
            // 初始化
            fixPresetValue = this.initDivideConfig(liveGameTypeList, lang);
        }
        Map<String,JSONObject> ownerFixConfigMap = JSONUtil.toBean(fixPresetValue, Map.class);
        liveGameTypeList.forEach(gameType -> {
            List<GameDivideVo> divideLevelOneList = new ArrayList<>();
            for (Map.Entry<String, JSONObject> voMap: ownerFixConfigMap.entrySet()) {
                // 游戏大类编码相同
                if (gameType.getDictValue().equalsIgnoreCase(voMap.getValue().getStr("liveGameCode"))) {
                    voMap.getValue().put("maxRatio", new BigDecimal("100"));
                    voMap.getValue().put("minRatio", BigDecimal.ZERO);
                    divideLevelOneList.add(JSONUtil.toBean(voMap.getValue(), GameDivideVo.class));
                }
            }
            tmpMap.put(gameType.getDictLabel(), divideLevelOneList);
        });
        return tmpMap;
    }

    /**
     * 获取列表模式配置
     * @param lang
     * @return
     */
    @Override
    public Map<String, Object> getDefaultFissionDivideConfig(String lang) {
        Map<String, List<FissionDivideConfigVo>> tmpMap = new TreeMap<>();
        List<SysDictData> liveGameTypeList = sysDictDataMapper.findDataByType("LIVE_GAME_TYPE", "1");
        if (CollectionUtil.isEmpty(liveGameTypeList)) {
            log.error("游戏大类字典数据为空！");
            return new TreeMap<>();
        }
        RecommendConfig recommendConfig = this.getRecommendConfig();
        // 获取得到层层代分红模式预设配置
        String fissionPresetValue = recommendConfig.getFissionPresetValue();
        if (StrUtil.isBlank(fissionPresetValue)) {
            // 初始化
            fissionPresetValue = this.initFissionDivideConfig(liveGameTypeList, lang);
        }
        Map<String,JSONObject> ownerFissionConfigMap = JSONUtil.toBean(fissionPresetValue, Map.class);
        liveGameTypeList.forEach(gameType -> {
            List<FissionDivideConfigVo> divideLevelOneList = new ArrayList<>();
            for (Map.Entry<String, JSONObject> voMap: ownerFissionConfigMap.entrySet()) {
                // 游戏大类编码相同
                if (gameType.getDictValue().equalsIgnoreCase(voMap.getValue().getStr("liveGameCode"))) {
                    divideLevelOneList.add(JSONUtil.toBean(voMap.getValue(), FissionDivideConfigVo.class));
                }
            }
            tmpMap.put(gameType.getDictLabel(), divideLevelOneList);
        });

        String recyclePresetValue = recommendConfig.getRecyclePresetValue();
        List<FissionDivideLevelVo> fissionConfigLevelVos = new ArrayList<>();
        if (StrUtil.isNotBlank(recyclePresetValue)) {
            fissionConfigLevelVos = JSONUtil.toList(JSONUtil.parseArray(recyclePresetValue), FissionDivideLevelVo.class);
        }
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("tmpMap", tmpMap);
        returnMap.put("fissionConfigLevelVos", fissionConfigLevelVos);
        returnMap.put("outRecyclePresetValue", recommendConfig.getOutRecyclePresetValue() == null ? BigDecimal.ZERO : recommendConfig.getOutRecyclePresetValue());
        return returnMap;
    }

    /**
     * 编辑代理配置
     * @param recommendConfigDto
     */
    @Override
    public void edit(RecommendConfigDto recommendConfigDto) {
        RecommendConfig recommendConfig = new RecommendConfig();
        BeanUtil.copyProperties(recommendConfigDto, recommendConfig);

        // 判断如果开启了固定比例模式预设并且分红模式为固定比例分红模式
        if (recommendConfigDto.getFixDevideIsPreset() == EnableEnum.ENABLED.code() && recommendConfigDto.getDivideModel() == 1) {
            // 固定比例模式
            Map<String, List<GameDivideVo>> ownerFixConfigMap = recommendConfigDto.getOwnerFixConfigMap();
            if (CollectionUtil.isNotEmpty(ownerFixConfigMap)) {
                Map<String,JSONObject> saveMap = new HashMap<>();
                for (Map.Entry<String,List<GameDivideVo>> map : ownerFixConfigMap.entrySet()) {
                    List<GameDivideVo> value = map.getValue();
                    for (GameDivideVo vo : value) {
                        saveMap.put(vo.getCode(),JSONUtil.parseObj(vo));
                    }
                }
                if (CollectionUtil.isNotEmpty(saveMap)) {
                    recommendConfig.setFixPresetValue(JSONUtil.toJsonStr(saveMap));
                }
            }
        }

        // 判断如果开启了裂变模式预设并且分红模式为裂变分红模式
        if (recommendConfigDto.getFissionDevideIsPreset() == EnableEnum.ENABLED.code() && recommendConfigDto.getDivideModel() == 2) {
            // 裂变模式
            Map<String, List<FissionDivideConfigVo>> ownerFissionConfigMap = recommendConfigDto.getOwnerFissionConfigMap();
            if (CollectionUtil.isNotEmpty(ownerFissionConfigMap)) {
                Map<String,JSONObject> saveMap = new HashMap<>();
                for (Map.Entry<String,List<FissionDivideConfigVo>> map : ownerFissionConfigMap.entrySet()) {
                    List<FissionDivideConfigVo> value = map.getValue();
                    for (FissionDivideConfigVo vo : value) {
                        saveMap.put(vo.getCode(),JSONUtil.parseObj(vo));
                    }
                }
                if (CollectionUtil.isNotEmpty(saveMap)) {
                    recommendConfig.setFissionPresetValue(JSONUtil.toJsonStr(saveMap));
                }
            }

            List<FissionDivideLevelVo> fissionConfigLevelVos = recommendConfigDto.getFissionConfigLevelVos();
            if (CollectionUtil.isNotEmpty(fissionConfigLevelVos)) {
                recommendConfig.setRecyclePresetValue(JSONUtil.toJsonStr(fissionConfigLevelVos));
            }
        }

        // 判断如果开启了层层代模式预设并且分红模式为层层代分红模式
        if (recommendConfigDto.getLayerDivideIsPreset() == EnableEnum.ENABLED.code() && recommendConfigDto.getDivideModel() == 3) {
            // 层层代模式
            Map<String, List<GameDivideVo>> ownerConfigMap = recommendConfigDto.getOwnerConfigMap();
            if (CollectionUtil.isNotEmpty(ownerConfigMap)) {
                Map<String,JSONObject> saveMap = new HashMap<>();
                for (Map.Entry<String,List<GameDivideVo>> map : ownerConfigMap.entrySet()) {
                    List<GameDivideVo> value = map.getValue();
                    for (GameDivideVo vo : value) {
                        saveMap.put(vo.getCode(),JSONUtil.parseObj(vo));
                    }
                }
                if (CollectionUtil.isNotEmpty(saveMap)) {
                    recommendConfig.setLayerPresetValue(JSONUtil.toJsonStr(saveMap));
                }
            }
        }


        Assert.isTrue(this.updateById(recommendConfig),"修改代理配置失败！");
    }


    /**
     * 初始化层层代分红模式配置预设
     * @param country
     * @return
     */
    @Override
    public String initDivideConfig(List<SysDictData> dictDataList, String lang) {
        Map<String, List<SysDictData>> dictMap = dictDataList.stream().collect(Collectors.groupingBy(SysDictData::getDictValue));

        List<GameKind> levelOneList = gameKindMapper.selectList(
                new QueryWrapper<GameKind>().eq("enable", EnableEnum.ENABLED.code())
        );
        Map<String, JSONObject> saveMap = new HashMap<>();
        for (GameKind levelOne : levelOneList) {
            SysDictData sysDictData = dictMap.get(levelOne.getGameType()).get(0);
            if (BeanUtil.isEmpty(sysDictData)) {
                continue;
            }
            GameDivideVo saveVo = new GameDivideVo();
            saveVo.setLiveGameName(sysDictData.getDictLabel());
            saveVo.setLiveGameCode(sysDictData.getDictValue());
            saveVo.setCode(levelOne.getCode());
            saveVo.setName(levelOne.getName());
            // 默认 投注额
            saveVo.setSettleType(2);
            saveVo.setAmountRatio(BigDecimal.ZERO);
            // 此用户分配的比例设置成初始值0
            saveVo.setDivideRatio(BigDecimal.ZERO);
            // 上级所剩分红比例
            saveVo.setParentDivideRatio(BigDecimal.ZERO);
            saveMap.put(levelOne.getCode(),JSONUtil.parseObj(saveVo));
        }
        return JSONUtil.toJsonStr(saveMap);
    }

    /**
     * 初始化层层代分红模式配置预设
     * @param country
     * @return
     */
    @Override
    public String initFissionDivideConfig(List<SysDictData> dictDataList, String lang) {
        Map<String, List<SysDictData>> dictMap = dictDataList.stream().collect(Collectors.groupingBy(SysDictData::getDictValue));

        List<GameKind> levelOneList = gameKindMapper.selectList(
                new QueryWrapper<GameKind>().eq("enable", EnableEnum.ENABLED.code())
        );
        Map<String, JSONObject> saveMap = new HashMap<>();
        for (GameKind levelOne : levelOneList) {
            SysDictData sysDictData = dictMap.get(levelOne.getGameType()).get(0);
            if (BeanUtil.isEmpty(sysDictData)) {
                continue;
            }
            FissionDivideConfigVo saveVo = new FissionDivideConfigVo();
            saveVo.setLiveGameName(sysDictData.getDictLabel());
            saveVo.setLiveGameCode(sysDictData.getDictValue());
            saveVo.setCode(levelOne.getCode());
            saveVo.setName(levelOne.getName());
            // 默认 投注额
            saveVo.setSettleType(2);
            saveVo.setAmountRatio(BigDecimal.ZERO);
            saveMap.put(levelOne.getCode(),JSONUtil.parseObj(saveVo));
        }
        return JSONUtil.toJsonStr(saveMap);
    }
}
