package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.DivideFixConfigMapper;
import com.gameplat.admin.mapper.GameKindMapper;
import com.gameplat.admin.mapper.SysDictDataMapper;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.service.DivideFixConfigService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.RecommendConfigService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.game.GameKind;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.proxy.DivideFixConfig;
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
public class DivideFixConfigServiceImpl extends ServiceImpl<DivideFixConfigMapper, DivideFixConfig>
    implements DivideFixConfigService {

  @Autowired private DivideFixConfigMapper fixConfigMapper;

  @Autowired private MemberService memberService;

  @Autowired private RecommendConfigService recommendConfigService;

  @Autowired private SysDictDataMapper sysDictDataMapper;

  @Autowired private GameKindMapper gameKindMapper;

  /**
   * 添加
   *
   * @param userName
   * @param lang
   */
  @Override
  public void add(String userName, String lang) {
    Assert.isTrue(StrUtil.isNotBlank(userName), "用户名参数缺失！");
    Member member =
        memberService
            .getAgentByAccount(userName)
            .orElseThrow(() -> new ServiceException("代理账号不存在!"));
    Assert.isTrue(member.getAgentLevel() == 1, "非一级代理！");
    Long count = this.lambdaQuery().eq(DivideFixConfig::getUserName, userName).count();
    Assert.isTrue(count <= 0, "不能重复添加！");
    DivideFixConfig saveObj =
        DivideFixConfig.builder()
            .userId(member.getId())
            .userName(member.getAccount())
            .divideConfig(recommendConfigService.initDivideConfig())
            .build();
    Assert.isTrue(this.save(saveObj), "添加失败！");
  }

  /**
   * 编辑前获取
   *
   * @param userName
   * @param s
   * @return
   */
  @Override
  public Map<String, Object> getFixConfigForEdit(String userName, String s) {
    Assert.isTrue(StrUtil.isNotBlank(userName), "用户名参数缺失！");
    Member member =
        memberService
            .getAgentByAccount(userName)
            .orElseThrow(() -> new ServiceException("代理账号不存在!"));
    Assert.isTrue(member.getAgentLevel() == 1, "非一级代理！");
    // 获取到自己的分红配置
    DivideFixConfig ownerFixConfig = fixConfigMapper.getByUserName(userName);
    if (BeanUtil.isEmpty(ownerFixConfig) || StrUtil.isBlank(ownerFixConfig.getDivideConfig())) {
      throw new ServiceException("分红配置为空！");
    }
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, JSONObject> ownerFixConfigMap =
        JSONUtil.toBean(ownerFixConfig.getDivideConfig(), Map.class);
    List<SysDictData> liveGameTypeList = sysDictDataMapper.findDataByType("LIVE_GAME_TYPE", "1");
    if (CollectionUtil.isEmpty(liveGameTypeList)) {
      throw new ServiceException("游戏大类数据为空");
    }
    List<GameKind> levelOneList =
        gameKindMapper.selectList(
            new QueryWrapper<GameKind>().eq("enable", EnableEnum.ENABLED.code()));
    Map<String, List<GameKind>> levelOneMap =
        levelOneList.stream().collect(Collectors.groupingBy(GameKind::getCode));
    Map<String, List<GameDivideVo>> tmpMap = new TreeMap<>();
    for (SysDictData dictData : liveGameTypeList) {
      List<GameDivideVo> divideLevelOneList = new ArrayList<>();
      for (Map.Entry<String, JSONObject> voMap : ownerFixConfigMap.entrySet()) {
        if (voMap.getValue() == null) {
          continue;
        }
        if (dictData.getDictValue().equalsIgnoreCase(voMap.getValue().getStr("liveGameCode"))) {
          voMap.getValue().put("maxRatio", new BigDecimal("100"));
          voMap.getValue().put("minRatio", BigDecimal.ZERO);

          List<GameKind> tmpLevelOneList = levelOneMap.get(voMap.getValue().getStr("code"));
          if (CollectionUtil.isNotEmpty(tmpLevelOneList)) {
            voMap.getValue().put("name", tmpLevelOneList.get(0).getName());
          }

          divideLevelOneList.add(JSONUtil.toBean(voMap.getValue(), GameDivideVo.class));
        }
      }
      tmpMap.put(dictData.getDictLabel(), divideLevelOneList);
    }
    returnMap.put("ownerFixConfigMap", tmpMap);
    return returnMap;
  }

  /**
   * 编辑固定比例分红配置
   *
   * @param divideConfigDTO
   * @param lang
   */
  @Override
  public void edit(DivideConfigDTO divideConfigDTO, String lang) {
    Assert.isTrue(divideConfigDTO.getId() != null, "主键参数丢失！");
    Assert.isTrue(divideConfigDTO.getUserId() != null, "用户ID丢失！");
    Assert.isTrue(StrUtil.isNotBlank(divideConfigDTO.getUserName()), "用户名参数丢失！");
    Assert.isTrue(CollectionUtil.isNotEmpty(divideConfigDTO.getOwnerFixConfigMap()), "分红配置参数丢失！");
    Map<String, List<GameDivideVo>> ownerFixConfigMap = divideConfigDTO.getOwnerFixConfigMap();
    Map<String, JSONObject> saveMap = new HashMap<>();
    for (Map.Entry<String, List<GameDivideVo>> map : ownerFixConfigMap.entrySet()) {
      List<GameDivideVo> value = map.getValue();
      for (GameDivideVo vo : value) {
        saveMap.put(vo.getCode(), JSONUtil.parseObj(vo));
      }
    }
    Assert.isTrue(BeanUtil.isNotEmpty(saveMap), "分红配置参数为空！");
    Member member =
        memberService
            .getAgentByAccount(divideConfigDTO.getUserName())
            .orElseThrow(() -> new ServiceException("代理账号不存在!"));
    Assert.isTrue(member.getAgentLevel() == 1, "非一级代理！");
    DivideFixConfig editObj =
        DivideFixConfig.builder()
            .id(divideConfigDTO.getId())
            .userId(divideConfigDTO.getUserId())
            .userName(divideConfigDTO.getUserName())
            .divideConfig(JSONUtil.toJsonStr(saveMap))
            .build();
    Assert.isTrue(this.updateById(editObj), "编辑失败！");
  }

  /**
   * 删除
   *
   * @param ids
   */
  @Override
  public void remove(String ids) {
    String[] idArr = ids.split(",");
    for (String idStr : idArr) {
      fixConfigMapper.deleteById(Long.valueOf(idStr));
    }
  }

  /**
   * 根据游戏编码获取
   *
   * @param superName
   * @param code
   * @return
   */
  @Override
  public GameDivideVo getConfigByGameCode(String superName, String code) {
    String configByFidAndCode = fixConfigMapper.getConfigByGameCode(superName, code);
    if (StrUtil.isBlank(configByFidAndCode)) {
      return new GameDivideVo();
    } else {
      return JSONUtil.toBean(configByFidAndCode, GameDivideVo.class);
    }
  }
}
