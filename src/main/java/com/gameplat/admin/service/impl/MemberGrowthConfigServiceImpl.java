package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGrowthConfigConvert;
import com.gameplat.admin.mapper.MemberGrowthConfigMapper;
import com.gameplat.admin.model.dto.GoldCoinDescUpdateDTO;
import com.gameplat.admin.model.dto.MemberGrowthConfigEditDto;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.member.MemberGrowthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberGrowthConfigServiceImpl
    extends ServiceImpl<MemberGrowthConfigMapper, MemberGrowthConfig>
    implements MemberGrowthConfigService {

  private static final int MAX_LIMIT_LEVEL = 50;
  @Autowired private MemberGrowthConfigMapper configMapper;
  @Autowired private MemberGrowthConfigConvert configConvert;

  @Override
  public MemberGrowthConfigVO findOneConfig() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    return configConvert.toVo(configMapper.findOneConfig(language));
  }

  @Override
  public MemberGrowthConfig getOneConfig() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    return configMapper.findOneConfig(language);
  }

  @Override
  public void updateGrowthConfig(MemberGrowthConfigEditDto dto) {
    MemberGrowthConfig before = this.lambdaQuery().eq(MemberGrowthConfig::getId, dto.getId()).one();
    if (before == null) {
      throw new ServiceException("此id不存在");
    }

    if (StringUtils.isNotEmpty(dto.getGoldCoinDesc())) {
      JSONObject jsonObject = new JSONObject();
      String goldCoinDesc = before.getGoldCoinDesc();
      if (StringUtils.isNotBlank(goldCoinDesc)) {
        jsonObject = JSONObject.parseObject(goldCoinDesc);
      }
      jsonObject.put(dto.getLanguage(), dto.getGoldCoinDesc());
      dto.setGoldCoinDesc(jsonObject.toJSONString());
    }

    dto.setLanguage(LocaleContextHolder.getLocale().getLanguage());
    configMapper.updateGrowthConfig(dto);
  }

  @Override
  public MemberGrowthConfig getGoldCoinDesc() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    return configMapper.getGoldCoinDesc(language);
  }

  @Override
  public void updateGoldCoinDesc(GoldCoinDescUpdateDTO dto) {
    String language = LocaleContextHolder.getLocale().getLanguage();
    MemberGrowthConfig memberGrowthConfig =
        this.lambdaQuery().eq(MemberGrowthConfig::getId, dto.getId()).one();
    if (ObjectUtil.isNull(memberGrowthConfig)) {
      throw new ServiceException("此id不存在！");
    }

    String goldCoinDesc = memberGrowthConfig.getGoldCoinDesc();
    JSONObject jsonObject = new JSONObject();
    if (StringUtils.isNotBlank(goldCoinDesc)) {
      jsonObject = JSONObject.parseObject(goldCoinDesc);
    }

    jsonObject.put(language, dto.getGoldCoinDesc());
    dto.setGoldCoinDesc(jsonObject.toJSONString());

    update(
        new LambdaUpdateWrapper<MemberGrowthConfig>()
            .set(MemberGrowthConfig::getGoldCoinDesc, dto.getGoldCoinDesc())
            .eq(MemberGrowthConfig::getId, dto.getId()));
  }

  @Override
  public Integer getLimitLevel() {
    return Optional.ofNullable(this.findOneConfig())
        .map(MemberGrowthConfigVO::getLimitLevel)
        .orElse(MAX_LIMIT_LEVEL);
  }
}
