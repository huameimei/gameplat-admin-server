package com.gameplat.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGrowthConfigConvert;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.mapper.MemberGrowthConfigMapper;
import com.gameplat.admin.model.dto.MemberGrowthConfigEditDto;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.member.MemberGrowthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberGrowthConfigServiceImpl
    extends ServiceImpl<MemberGrowthConfigMapper, MemberGrowthConfig>
    implements MemberGrowthConfigService {

  @Autowired private MemberGrowthConfigMapper configMapper;

  @Autowired private MemberGrowthConfigConvert configConvert;

  /** 只查询一条 */
  @Override
  public MemberGrowthConfigVO findOneConfig(String language) {

    if (StrUtil.isBlank(language)) {
      language = LanguageEnum.app_zh_CN.getCode();
    }
    MemberGrowthConfig config = configMapper.findOneConfig(language);
    return configConvert.toVo(config);
  }

  /** 只查询一条 */
  @Override
  public MemberGrowthConfig getOneConfig(String language) {
    if (StrUtil.isBlank(language)) {
      language = LanguageEnum.app_zh_CN.getCode();
    }
    return configMapper.findOneConfig(language);
  }

  /** 修改成长值配置 */
  @Override
  public void updateGrowthConfig(MemberGrowthConfigEditDto configEditDto) {
    MemberGrowthConfig before =
        this.lambdaQuery().eq(MemberGrowthConfig::getId, configEditDto.getId()).one();
    if (before == null) {
      throw new ServiceException("此id不存在");
    }
    if (StringUtils.isNotEmpty(configEditDto.getGoldCoinDesc())) {
      JSONObject jsonObject = new JSONObject();
      String goldCoinDesc = before.getGoldCoinDesc();
      if (StringUtils.isNotBlank(goldCoinDesc)) {
        jsonObject = JSONObject.parseObject(goldCoinDesc);
      }
      jsonObject.put(configEditDto.getLanguage(), configEditDto.getGoldCoinDesc());
      configEditDto.setGoldCoinDesc(jsonObject.toJSONString());
    }
    configMapper.updateGrowthConfig(configEditDto);
  }
}
