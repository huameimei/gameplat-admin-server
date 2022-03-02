package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGrowthConfigConvert;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.mapper.MemberGrowthConfigMapper;
import com.gameplat.admin.model.dto.GoldCoinDescUpdateDTO;
import com.gameplat.admin.model.dto.MemberGrowthConfigEditDto;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.member.MemberGrowthConfig;
import org.mapstruct.ap.internal.model.assignment.UpdateWrapper;
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

  /** 后台获取金币说明配置 */
  @Override
  public MemberGrowthConfig getGoldCoinDesc(String language) {
    if(StrUtil.isBlank(language)) {
      language = LanguageEnum.app_zh_CN.getCode();
    }
    return configMapper.getGoldCoinDesc(language);
  }

  /** 后台修改金币说明配置 */
  @Override
  public void updateGoldCoinDesc(GoldCoinDescUpdateDTO dto) {
    if(StrUtil.isBlank(dto.getLanguage())) {
      dto.setLanguage(LanguageEnum.app_zh_CN.getCode());
    }
    MemberGrowthConfig memberGrowthConfig = this.lambdaQuery()
                                                .eq(MemberGrowthConfig::getId, dto.getId()).one();
    if (ObjectUtil.isNotNull(memberGrowthConfig)){
      throw new ServiceException("此id不存在！");
    }
    String goldCoinDesc = memberGrowthConfig.getGoldCoinDesc();
    JSONObject jsonObject = new JSONObject();
    if (StringUtils.isNotBlank(goldCoinDesc)){
      jsonObject = JSONObject.parseObject(goldCoinDesc);
    }
    jsonObject.put(dto.getLanguage(),dto.getGoldCoinDesc());
    dto.setGoldCoinDesc(jsonObject.toJSONString());

    update(new LambdaUpdateWrapper<MemberGrowthConfig>()
            .set(MemberGrowthConfig::getGoldCoinDesc, dto.getGoldCoinDesc())
            .eq(MemberGrowthConfig::getId, dto.getId()));
  }
}
