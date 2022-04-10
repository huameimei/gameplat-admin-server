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
import com.gameplat.admin.model.dto.RecommendConfigDto;
import com.gameplat.admin.model.vo.FissionDivideConfigVo;
import com.gameplat.admin.model.vo.FissionDivideLevelVo;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.service.RecommendConfigService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.game.GameKind;
import com.gameplat.model.entity.proxy.RecommendConfig;
import com.gameplat.model.entity.sys.SysDictData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/** @Description : 层层代分红配置 @Author : cc @Date : 2022/4/2 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class RecommendConfigServiceImpl extends ServiceImpl<RecommendConfigMapper, RecommendConfig>
    implements RecommendConfigService {

  @Autowired private SysDictDataMapper sysDictDataMapper;

  @Autowired private GameKindMapper gameKindMapper;

  /**
   * 获取代理配置
   *
   * @return RecommendConfig
   */
  @Override
  public RecommendConfig getRecommendConfig() {
    return this.lambdaQuery()
        .orderByDesc(RecommendConfig::getId)
        .last("limit 1")
        .oneOpt()
        .orElseThrow(() -> new ServiceException("代理限制信息不存在!"));
  }

  /**
   * 获取层层代分红配置
   *
   * @return Map
   */
  @Override
  public Map<String, List<GameDivideVo>> getDefaultLayerDivideConfig() {
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
      layerPresetValue = this.initDivideConfig();
    }
    Map<String, JSONObject> ownerConfigMap = JSONUtil.toBean(layerPresetValue, Map.class);
    liveGameTypeList.forEach(
        gameType -> {
          List<GameDivideVo> divideLevelOneList = new ArrayList<>();
          for (Map.Entry<String, JSONObject> voMap : ownerConfigMap.entrySet()) {
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
   * 获取固定模式配置
   *
   * @return Map
   */
  @Override
  public Map<String, List<GameDivideVo>> getDefaultFixDivideConfig() {
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
      fixPresetValue = this.initDivideConfig();
    }
    Map<String, JSONObject> ownerFixConfigMap = JSONUtil.toBean(fixPresetValue, Map.class);
    liveGameTypeList.forEach(
        gameType -> {
          List<GameDivideVo> divideLevelOneList = new ArrayList<>();
          for (Map.Entry<String, JSONObject> voMap : ownerFixConfigMap.entrySet()) {
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
   *
   * @return Map
   */
  @Override
  public Map<String, Object> getDefaultFissionDivideConfig() {
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
      fissionPresetValue = this.initFissionDivideConfig();
    }
    Map<String, JSONObject> ownerFissionConfigMap = JSONUtil.toBean(fissionPresetValue, Map.class);
    liveGameTypeList.forEach(
        gameType -> {
          List<FissionDivideConfigVo> divideLevelOneList = new ArrayList<>();
          for (Map.Entry<String, JSONObject> voMap : ownerFissionConfigMap.entrySet()) {
            // 游戏大类编码相同
            if (gameType.getDictValue().equalsIgnoreCase(voMap.getValue().getStr("liveGameCode"))) {
              divideLevelOneList.add(
                  JSONUtil.toBean(voMap.getValue(), FissionDivideConfigVo.class));
            }
          }
          tmpMap.put(gameType.getDictLabel(), divideLevelOneList);
        });

    String recyclePresetValue = recommendConfig.getRecyclePresetValue();
    List<FissionDivideLevelVo> fissionConfigLevelVos = new ArrayList<>();
    if (StrUtil.isNotBlank(recyclePresetValue)) {
      fissionConfigLevelVos =
          JSONUtil.toList(JSONUtil.parseArray(recyclePresetValue), FissionDivideLevelVo.class);
    }
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put("tmpMap", tmpMap);
    returnMap.put("fissionConfigLevelVos", fissionConfigLevelVos);
    returnMap.put(
        "outRecyclePresetValue",
        recommendConfig.getOutRecyclePresetValue() == null
            ? BigDecimal.ZERO
            : recommendConfig.getOutRecyclePresetValue());
    return returnMap;
  }

  /**
   * 编辑代理配置
   *
   * @param dto RecommendConfigDto
   */
  @Override
  public void edit(RecommendConfigDto dto) {
    RecommendConfig recommendConfig = new RecommendConfig();
    BeanUtil.copyProperties(dto, recommendConfig);

    // 判断如果开启了固定比例模式预设并且分红模式为固定比例分红模式
    if (dto.getFixDevideIsPreset() == EnableEnum.ENABLED.code() && dto.getDivideModel() == 1) {
      // 固定比例模式
      Map<String, List<GameDivideVo>> ownerFixConfigMap = dto.getOwnerFixConfigMap();
      if (CollectionUtil.isNotEmpty(ownerFixConfigMap)) {
        Map<String, JSONObject> saveMap = new HashMap<>();
        for (Map.Entry<String, List<GameDivideVo>> map : ownerFixConfigMap.entrySet()) {
          List<GameDivideVo> value = map.getValue();
          for (GameDivideVo vo : value) {
            saveMap.put(vo.getCode(), JSONUtil.parseObj(vo));
          }
        }
        if (CollectionUtil.isNotEmpty(saveMap)) {
          recommendConfig.setFixPresetValue(JSONUtil.toJsonStr(saveMap));
        }
      }
    }

    // 判断如果开启了裂变模式预设并且分红模式为裂变分红模式
    if (dto.getFissionDevideIsPreset() == EnableEnum.ENABLED.code() && dto.getDivideModel() == 2) {
      // 裂变模式
      Map<String, List<FissionDivideConfigVo>> ownerFissionConfigMap =
          dto.getOwnerFissionConfigMap();
      if (CollectionUtil.isNotEmpty(ownerFissionConfigMap)) {
        Map<String, JSONObject> saveMap = new HashMap<>();
        for (Map.Entry<String, List<FissionDivideConfigVo>> map :
            ownerFissionConfigMap.entrySet()) {
          List<FissionDivideConfigVo> value = map.getValue();
          for (FissionDivideConfigVo vo : value) {
            saveMap.put(vo.getCode(), JSONUtil.parseObj(vo));
          }
        }
        if (CollectionUtil.isNotEmpty(saveMap)) {
          recommendConfig.setFissionPresetValue(JSONUtil.toJsonStr(saveMap));
        }
      }

      List<FissionDivideLevelVo> fissionConfigLevelVos = dto.getFissionConfigLevelVos();
      if (CollectionUtil.isNotEmpty(fissionConfigLevelVos)) {
        recommendConfig.setRecyclePresetValue(JSONUtil.toJsonStr(fissionConfigLevelVos));
      }
    }

    // 判断如果开启了层层代模式预设并且分红模式为层层代分红模式
    if (dto.getLayerDivideIsPreset() == EnableEnum.ENABLED.code() && dto.getDivideModel() == 3) {
      // 层层代模式
      Map<String, List<GameDivideVo>> ownerConfigMap = dto.getOwnerConfigMap();
      if (CollectionUtil.isNotEmpty(ownerConfigMap)) {
        Map<String, JSONObject> saveMap = new HashMap<>();
        for (Map.Entry<String, List<GameDivideVo>> map : ownerConfigMap.entrySet()) {
          List<GameDivideVo> value = map.getValue();
          for (GameDivideVo vo : value) {
            saveMap.put(vo.getCode(), JSONUtil.parseObj(vo));
          }
        }
        if (CollectionUtil.isNotEmpty(saveMap)) {
          recommendConfig.setLayerPresetValue(JSONUtil.toJsonStr(saveMap));
        }
      }
    }

    Assert.isTrue(this.updateById(recommendConfig), "修改代理配置失败！");
  }

  /**
   * 初始化层层代或固定比例分红模式配置预设
   *
   * @return String
   */
  @Override
  public String initDivideConfig() {
    List<SysDictData> liveGameTypeList = sysDictDataMapper.findDataByType("LIVE_GAME_TYPE", "1");
    if (CollectionUtil.isEmpty(liveGameTypeList)) {
      log.error("游戏大类字典数据为空！");
      return "";
    }
    Map<String, List<SysDictData>> dictMap =
        liveGameTypeList.stream().collect(Collectors.groupingBy(SysDictData::getDictValue));

    List<GameKind> levelOneList =
        gameKindMapper.selectList(
            new QueryWrapper<GameKind>().eq("enable", EnableEnum.ENABLED.code()));
    Map<String, JSONObject> saveMap = new HashMap<>();
    for (GameKind levelOne : levelOneList) {
      SysDictData sysDictData = dictMap.get(levelOne.getGameType()).get(0);
      if (BeanUtil.isEmpty(sysDictData)) {
        continue;
      }
      GameDivideVo saveVo =
          GameDivideVo.builder()
              .liveGameName(sysDictData.getDictLabel())
              .liveGameCode(sysDictData.getDictValue())
              .code(levelOne.getCode())
              .name(levelOne.getName())
              .settleType(2)
              .amountRatio(BigDecimal.ZERO)
              .divideRatio(BigDecimal.ZERO)
              .parentDivideRatio(BigDecimal.ZERO)
              .build();
      saveMap.put(levelOne.getCode(), JSONUtil.parseObj(saveVo));
    }
    return JSONUtil.toJsonStr(saveMap);
  }

  /**
   * 初始化裂变配置
   *
   * @return String
   */
  @Override
  public String initFissionDivideConfig() {
    List<SysDictData> liveGameTypeList = sysDictDataMapper.findDataByType("LIVE_GAME_TYPE", "1");
    if (CollectionUtil.isEmpty(liveGameTypeList)) {
      log.error("游戏大类字典数据为空！");
      return "";
    }
    Map<String, List<SysDictData>> dictMap =
        liveGameTypeList.stream().collect(Collectors.groupingBy(SysDictData::getDictValue));

    List<GameKind> levelOneList =
        gameKindMapper.selectList(
            new QueryWrapper<GameKind>().eq("enable", EnableEnum.ENABLED.code()));
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
      saveMap.put(levelOne.getCode(), JSONUtil.parseObj(saveVo));
    }
    return JSONUtil.toJsonStr(saveMap);
  }
}
