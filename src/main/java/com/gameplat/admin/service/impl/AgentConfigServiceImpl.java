package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gameplat.admin.model.vo.FissionDivideConfigVo;
import com.gameplat.admin.model.vo.FissionDivideLevelVo;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.service.AgentConfigService;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.GameKindService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.common.model.bean.AgentConfig;
import com.gameplat.model.entity.game.GameKind;
import com.gameplat.model.entity.sys.SysDictData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AgentConfigServiceImpl implements AgentConfigService {

  @Autowired private GameKindService gameKindService;

  @Autowired private ConfigService configService;

  @Autowired private SysDictDataService dictDataService;

  @Override
  public AgentConfig getAgentConfig() {
    return Optional.ofNullable(configService.get(DictTypeEnum.AGENT_CONFIG, AgentConfig.class))
        .orElseThrow(() -> new ServiceException("代理限制信息不存在!"));
  }

  @Override
  public Map<String, List<GameDivideVo>> getDefaultLayerDivideConfig() {
    List<SysDictData> gameTypeList = this.getLiveGameList();
    if (CollectionUtil.isEmpty(gameTypeList)) {
      return new TreeMap<>();
    }

    String layerPresetValue =
        configService.getValue(DictDataEnum.LAYER_PRESET_VALUE, this.initDivideConfig());
    Map<String, JSONObject> map =
        JSONUtil.toBean(layerPresetValue, new TypeReference<Map<String, JSONObject>>() {}, true);

    Map<String, List<GameDivideVo>> tmpMap = new TreeMap<>();
    gameTypeList.forEach(e -> tmpMap.put(e.getDictLabel(), getGameDivideVo(e.getDictValue(), map)));
    return tmpMap;
  }

  @Override
  public Map<String, List<GameDivideVo>> getDefaultFixDivideConfig() {
    List<SysDictData> gameTypeList = this.getLiveGameList();
    if (CollectionUtil.isEmpty(gameTypeList)) {
      return new TreeMap<>();
    }

    String fixPresetValue =
        configService.getValue(DictDataEnum.FIX_PRESET_VALUE, this.initDivideConfig());

    Map<String, JSONObject> map =
        JSONUtil.toBean(fixPresetValue, new TypeReference<Map<String, JSONObject>>() {}, true);

    Map<String, List<GameDivideVo>> tmpMap = new TreeMap<>();
    gameTypeList.forEach(e -> tmpMap.put(e.getDictLabel(), getGameDivideVo(e.getDictValue(), map)));
    return tmpMap;
  }

  @Override
  public Map<String, Object> getDefaultFissionDivideConfig() {
    List<SysDictData> gameTypeList = this.getLiveGameList();
    if (CollectionUtil.isEmpty(gameTypeList)) {
      return new TreeMap<>();
    }

    String fissionPresetValue =
        configService.getValue(DictDataEnum.FISSION_PRESET_VALUE, this.initFissionDivideConfig());

    Map<String, List<FissionDivideConfigVo>> fissionPresetValueMap = new TreeMap<>();

    Map<String, JSONObject> map =
        JSONUtil.toBean(fissionPresetValue, new TypeReference<Map<String, JSONObject>>() {}, true);

    gameTypeList.forEach(
        gameType -> {
          List<FissionDivideConfigVo> divideLevels =
              map.values().stream()
                  .filter(e -> gameType.getDictValue().equalsIgnoreCase(e.getStr("liveGameCode")))
                  .map(e -> JSONUtil.toBean(e, FissionDivideConfigVo.class))
                  .collect(Collectors.toList());
          fissionPresetValueMap.put(gameType.getDictLabel(), divideLevels);
        });

    // 周期预设
    List<FissionDivideLevelVo> recyclePresetValueList =
        Optional.ofNullable(configService.getValue(DictDataEnum.RECYCLE_PRESET_VALUE))
            .filter(StringUtils::isNotEmpty)
            .map(JSONUtil::parseArray)
            .map(e -> JSONUtil.toList(e, FissionDivideLevelVo.class))
            .orElse(new ArrayList<>());

    // 周期外
    BigDecimal outRecyclePresetValue =
        Optional.ofNullable(configService.getValue(DictDataEnum.OUT_RECYCLE_PRESET_VALUE))
            .map(BigDecimal::new)
            .orElse(BigDecimal.ZERO);

    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put("fissionPresetValue", fissionPresetValueMap);
    returnMap.put("recyclePresetValue", recyclePresetValueList);
    returnMap.put("outRecyclePresetValue", outRecyclePresetValue);
    return returnMap;
  }

  @Override
  public void edit(Map<String, Object> params) {
    params.forEach(
        (k, v) ->
            dictDataService.updateByTypeAndLabel(
                SysDictData.builder()
                    .dictType(DictTypeEnum.AGENT_CONFIG.getValue())
                    .dictLabel(k)
                    .dictValue(Optional.ofNullable(v).map(Object::toString).orElse(null))
                    .build()));
  }

  @Override
  public String initDivideConfig() {
    List<SysDictData> liveGameTypeList = this.getLiveGameList();
    if (CollectionUtil.isEmpty(liveGameTypeList)) {
      log.error("游戏大类字典数据为空！");
      return "";
    }

    Map<String, List<SysDictData>> dictMap =
        liveGameTypeList.stream().collect(Collectors.groupingBy(SysDictData::getDictValue));
    List<GameKind> gameKinds = gameKindService.getList();

    Map<String, JSONObject> saveMap = new HashMap<>();
    for (GameKind gameKind : gameKinds) {
      SysDictData sysDictData = dictMap.get(gameKind.getGameType()).get(0);
      if (BeanUtil.isEmpty(sysDictData)) {
        continue;
      }

      GameDivideVo saveVo =
          GameDivideVo.builder()
              .liveGameName(sysDictData.getDictLabel())
              .liveGameCode(sysDictData.getDictValue())
              .code(gameKind.getCode())
              .name(gameKind.getName())
              .settleType(2)
              .amountRatio(BigDecimal.ZERO)
              .divideRatio(BigDecimal.ZERO)
              .parentDivideRatio(BigDecimal.ZERO)
              .build();

      saveMap.put(gameKind.getCode(), JSONUtil.parseObj(saveVo));
    }

    return JSONUtil.toJsonStr(saveMap);
  }

  @Override
  public String initFissionDivideConfig() {
    List<SysDictData> gameTypeList = this.getLiveGameList();
    if (CollectionUtil.isEmpty(gameTypeList)) {
      log.error("游戏大类字典数据为空！");
      return "";
    }

    Map<String, List<SysDictData>> dictMap =
        gameTypeList.stream().collect(Collectors.groupingBy(SysDictData::getDictValue));

    Map<String, JSONObject> saveMap = new HashMap<>();

    gameKindService
        .getList()
        .forEach(
            gameKind -> {
              SysDictData sysDictData = dictMap.get(gameKind.getGameType()).get(0);
              if (Objects.nonNull(sysDictData)) {
                FissionDivideConfigVo saveVo = new FissionDivideConfigVo();
                saveVo.setLiveGameName(sysDictData.getDictLabel());
                saveVo.setLiveGameCode(sysDictData.getDictValue());
                saveVo.setCode(gameKind.getCode());
                saveVo.setName(gameKind.getName());
                // 默认 投注额
                saveVo.setSettleType(2);
                saveVo.setAmountRatio(BigDecimal.ZERO);
                saveMap.put(gameKind.getCode(), JSONUtil.parseObj(saveVo));
              }
            });

    return JSONUtil.toJsonStr(saveMap);
  }

  private List<GameDivideVo> getGameDivideVo(String dictType, Map<String, JSONObject> map) {
    return map.values().stream()
        .filter(e -> dictType.equalsIgnoreCase(e.getStr("liveGameCode")))
        .map(
            e -> {
              e.put("maxRatio", BigDecimal.valueOf(100));
              e.put("minRatio", BigDecimal.ZERO);
              return JSONUtil.toBean(e, GameDivideVo.class);
            })
        .collect(Collectors.toList());
  }

  private List<SysDictData> getLiveGameList() {
    return dictDataService.getDictDataByType(DictTypeEnum.LIVE_GAME_TYPE.getValue());
  }
}
