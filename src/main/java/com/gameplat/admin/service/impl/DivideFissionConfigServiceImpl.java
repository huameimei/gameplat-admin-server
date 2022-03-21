package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.DivideFissionConfigMapper;
import com.gameplat.admin.mapper.GameKindMapper;
import com.gameplat.admin.mapper.SysDictDataMapper;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.model.vo.FissionConfigLevelVo;
import com.gameplat.admin.model.vo.FissionDivideConfigVo;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.service.DivideFissionConfigService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.RecommendConfigService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.game.GameKind;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.proxy.DivideFissionConfig;
import com.gameplat.model.entity.sys.SysDictData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class DivideFissionConfigServiceImpl
    extends ServiceImpl<DivideFissionConfigMapper, DivideFissionConfig>
    implements DivideFissionConfigService {

  @Autowired private DivideFissionConfigMapper fissionConfigMapper;

  @Autowired private MemberService memberService;

  @Autowired private RecommendConfigService recommendConfigService;

  @Autowired private SysDictDataMapper sysDictDataMapper;

  @Autowired private GameKindMapper gameKindMapper;

  @Override
  public void add(String userName, String lang) {
    Assert.isTrue(StrUtil.isNotBlank(userName), "用户名参数缺失！");
    Member member =
        memberService
            .getAgentByAccount(userName)
            .orElseThrow(() -> new ServiceException("代理账号不存在!"));
    Assert.isTrue(member.getAgentLevel() == 1, "非一级代理！");
    Long count = this.lambdaQuery().eq(DivideFissionConfig::getUserName, userName).count();
    Assert.isTrue(count <= 0, "不能重复添加！");
    DivideFissionConfig saveObj =
        DivideFissionConfig.builder()
            .userId(member.getId())
            .userName(member.getAccount())
            .divideConfig(recommendConfigService.initFissionDivideConfig())
            .recycleConfig(new JSONArray().toString())
            .recycleOutConfig(BigDecimal.ZERO)
            .build();
    Assert.isTrue(this.save(saveObj), "添加失败！");
  }

  @Override
  public Map<String, Object> getFissionConfigForEdit(String userName, String lang) {
    Assert.isTrue(StrUtil.isNotBlank(userName), "用户名参数缺失！");
    Member member =
        memberService
            .getAgentByAccount(userName)
            .orElseThrow(() -> new ServiceException("代理账号不存在!"));
    Assert.isTrue(member.getAgentLevel() == 1, "非一级代理！");
    // 获取到自己的分红配置
    DivideFissionConfig ownerFissionConfig = fissionConfigMapper.getByUserName(userName);
    if (BeanUtil.isEmpty(ownerFissionConfig)
        || StrUtil.isBlank(ownerFissionConfig.getDivideConfig())) {
      throw new ServiceException("分红配置为空！");
    }
    Map<String, JSONObject> ownerFissionConfigMap =
        JSONUtil.toBean(ownerFissionConfig.getDivideConfig(), Map.class);

    List<SysDictData> liveGameTypeList = sysDictDataMapper.findDataByType("LIVE_GAME_TYPE", "1");
    if (CollectionUtil.isEmpty(liveGameTypeList)) {
      throw new ServiceException("游戏大类数据为空");
    }
    List<GameKind> levelOneList =
        gameKindMapper.selectList(
            new QueryWrapper<GameKind>().eq("enable", EnableEnum.ENABLED.code()));
    Map<String, List<GameKind>> levelOneMap =
        levelOneList.stream().collect(Collectors.groupingBy(GameKind::getCode));
    Map<String, List<FissionDivideConfigVo>> tmpMap = new TreeMap<>();
    for (SysDictData dictData : liveGameTypeList) {
      List<FissionDivideConfigVo> divideLevelOneList = new ArrayList<>();
      for (Map.Entry<String, JSONObject> voMap : ownerFissionConfigMap.entrySet()) {
        if (voMap.getValue() == null) {
          continue;
        }
        if (dictData.getDictValue().equalsIgnoreCase(voMap.getValue().getStr("liveGameCode"))) {
          List<GameKind> tmpLevelOneList = levelOneMap.get(voMap.getValue().getStr("code"));
          if (CollectionUtil.isNotEmpty(tmpLevelOneList)) {
            voMap.getValue().putOnce("name", tmpLevelOneList.get(0).getName());
          }
          divideLevelOneList.add(JSONUtil.toBean(voMap.getValue(), FissionDivideConfigVo.class));
        }
      }
      tmpMap.put(dictData.getDictLabel(), divideLevelOneList);
    }
    String fissionRecycleConfig = ownerFissionConfig.getRecycleConfig();
    List<FissionConfigLevelVo> fissionConfigLevelVos = new ArrayList<>();
    if (StrUtil.isNotBlank(fissionRecycleConfig)) {
      fissionConfigLevelVos =
          JSONUtil.toList(JSONUtil.parseArray(fissionRecycleConfig), FissionConfigLevelVo.class);
    }

    Map<String, Object> returnMap = new HashMap<>(3);
    returnMap.put("divideConfigMap", tmpMap);
    returnMap.put("recycleConfig", fissionConfigLevelVos);
    returnMap.put(
        "outDivideRatio",
        ownerFissionConfig.getRecycleOutConfig() == null
            ? 0
            : ownerFissionConfig.getRecycleOutConfig());
    return returnMap;
  }

  @Override
  public void edit(DivideConfigDTO divideConfigDTO, String lang) {
    Assert.isTrue(divideConfigDTO.getId() != null, "主键参数丢失！");
    Assert.isTrue(divideConfigDTO.getUserId() != null, "用户ID丢失！");
    Assert.isTrue(StrUtil.isNotBlank(divideConfigDTO.getUserName()), "用户名参数丢失！");
    Assert.isTrue(
        CollectionUtil.isNotEmpty(divideConfigDTO.getOwnerFissionConfigMap()), "分红配置参数丢失！");
    Member member =
        memberService
            .getAgentByAccount(divideConfigDTO.getUserName())
            .orElseThrow(() -> new ServiceException("代理账号不存在!"));
    Assert.isTrue(member.getAgentLevel() == 1, "非一级代理！");

    DivideFissionConfig editObj =
        DivideFissionConfig.builder()
            .id(divideConfigDTO.getId())
            .userId(divideConfigDTO.getUserId())
            .userName(divideConfigDTO.getUserName())
            .build();

    Map<String, List<FissionDivideConfigVo>> ownerFissionConfigMap =
        divideConfigDTO.getOwnerFissionConfigMap();
    if (CollectionUtil.isNotEmpty(ownerFissionConfigMap)) {
      Map<String, JSONObject> saveMap = new HashMap<>(ownerFissionConfigMap.size());
      for (Map.Entry<String, List<FissionDivideConfigVo>> map : ownerFissionConfigMap.entrySet()) {
        List<FissionDivideConfigVo> value = map.getValue();
        for (FissionDivideConfigVo vo : value) {
          saveMap.put(vo.getCode(), JSONUtil.parseObj(vo));
        }
      }
      if (CollectionUtil.isNotEmpty(saveMap)) {
        editObj.setDivideConfig(JSONUtil.toJsonStr(saveMap));
      }
    }

    List<FissionConfigLevelVo> fissionConfigLevelVos = divideConfigDTO.getFissionConfigLevelVos();
    if (CollectionUtil.isNotEmpty(fissionConfigLevelVos)) {
      editObj.setRecycleConfig(JSONUtil.toJsonStr(fissionConfigLevelVos));
    }
    editObj.setRecycleOutConfig(
        divideConfigDTO.getOutRecycleConfig() == null
            ? BigDecimal.ZERO
            : divideConfigDTO.getOutRecycleConfig());
    Assert.isTrue(this.updateById(editObj), "编辑失败！");
  }

  @Override
  public void remove(String ids) {
    String[] idArr = ids.split(",");
    for (String idStr : idArr) {
      fissionConfigMapper.deleteById(Long.valueOf(idStr));
    }
  }

  @Override
  public GameDivideVo getConfigByFirstCode(String superName, String code) {
    String configByFidAndCode = fissionConfigMapper.getConfigByGameCode(superName, code);
    if (StrUtil.isBlank(configByFidAndCode)) {
      return new GameDivideVo();
    } else {
      return JSONUtil.toBean(configByFidAndCode, GameDivideVo.class);
    }
  }

  @Override
  public DivideFissionConfig getByAccount(String account) {
    return fissionConfigMapper.getByUserName(account);
  }
}
