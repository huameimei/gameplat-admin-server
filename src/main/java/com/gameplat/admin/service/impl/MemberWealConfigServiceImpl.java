package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberWealConfigConvert;
import com.gameplat.admin.mapper.MemberWealConfigMapper;
import com.gameplat.admin.model.dto.MemberWealConfigAddDTO;
import com.gameplat.admin.model.dto.MemberWealConfigEditDTO;
import com.gameplat.admin.service.MemberWealConfigService;
import com.gameplat.model.entity.member.MemberWealConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员权益
 *
 * @author lily
 * @date 2022/1/15
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberWealConfigServiceImpl
    extends ServiceImpl<MemberWealConfigMapper, MemberWealConfig>
    implements MemberWealConfigService {

  @Autowired private MemberWealConfigConvert memberWealConfigConvert;

  @Override
  public void addWealConfig(MemberWealConfigAddDTO dto) {
    this.save(memberWealConfigConvert.toEntity(dto));
  }

  @Override
  public void removeWealConfig(Long id) {
    this.removeById(id);
  }

  @Override
  public void updateWealConfig(MemberWealConfigEditDTO dto) {
    this.updateById(memberWealConfigConvert.toEntity(dto));
  }

  @Override
  public IPage<MemberWealConfig> page(PageDTO<MemberWealConfig> page) {
    String language = LocaleContextHolder.getLocale().getLanguage();
    return this.lambdaQuery().eq(MemberWealConfig::getLanguage, language).page(page);
  }
}
