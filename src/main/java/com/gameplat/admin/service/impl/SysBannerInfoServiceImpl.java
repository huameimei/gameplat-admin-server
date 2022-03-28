package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysBannerInfoConvert;
import com.gameplat.admin.enums.SysBannerInfoEnum;
import com.gameplat.admin.mapper.SysBannerInfoMapper;
import com.gameplat.admin.model.dto.SysBannerInfoAddDTO;
import com.gameplat.admin.model.dto.SysBannerInfoEditDTO;
import com.gameplat.admin.model.dto.SysBannerInfoUpdateStatusDTO;
import com.gameplat.admin.model.vo.SysBannerInfoVO;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.SysBannerInfoService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.model.entity.sys.SysBannerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * banner业务处理
 *
 * @author admin
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysBannerInfoServiceImpl extends ServiceImpl<SysBannerInfoMapper, SysBannerInfo>
    implements SysBannerInfoService {

  @Autowired private SysBannerInfoConvert sysBannerInfoConvert;

  @Autowired private ConfigService configService;

  @Override
  public List<SysBannerInfo> getByBanner(SysBannerInfo banner) {
    return this.lambdaQuery()
        .eq(banner.getStatus() == null, SysBannerInfo::getStatus, banner.getStatus())
        .list();
  }

  @Override
  public boolean saveSysBannerInfo(SysBannerInfo sysBannerInfo) {
    validBannerInfo(sysBannerInfo);
    return this.save(sysBannerInfo);
  }

  @Override
  public IPage<SysBannerInfoVO> list(PageDTO<SysBannerInfo> page, String language, Integer type) {
    return this.lambdaQuery()
        .eq(type != null && type != 0, SysBannerInfo::getType, type)
        .eq(StringUtils.isNotBlank(language), SysBannerInfo::getLanguage, language)
        .page(page)
        .convert(sysBannerInfoConvert::toVo);
  }

  @Override
  public void add(SysBannerInfoAddDTO sysBannerInfoAddDTO) {
    SysBannerInfo sysBannerInfo = sysBannerInfoConvert.toEntity(sysBannerInfoAddDTO);
    validBannerInfo(sysBannerInfo);
    if (!this.save(sysBannerInfo)) {
      throw new ServiceException("banner信息保存异常");
    }
  }

  /** 校验banner信息 */
  private void validBannerInfo(SysBannerInfo sysBannerInfo) {
    if (sysBannerInfo.getType() == SysBannerInfoEnum.Type.SPORT.getValue()) {
      // 校验选择不同类型的数据判断
      if (sysBannerInfo.getBannerType().equals(configService.getInteger(DictDataEnum.ACTIVITY))) {
        if (sysBannerInfo.getChildType() == null || sysBannerInfo.getChildType() == 0) {
          throw new ServiceException("活动优惠，子分类不能为空");
        }
      }
      // 配置跳转页
      else if (sysBannerInfo
          .getBannerType()
          .equals(configService.getInteger(DictDataEnum.JUMP_PAGES))) {
        if (StringUtils.isBlank(sysBannerInfo.getJumpUrl())) {
          throw new ServiceException("配置跳转页，调整地址不能为空");
        }
      }
      // 游戏分类
      else if (sysBannerInfo
          .getBannerType()
          .equals(configService.getInteger(DictDataEnum.GAME_CATEGORY))) {
        //                if (StringUtils.isBlank(sysBannerInfo.getGameKind())) {
        //                    throw new ServiceException("选择游戏分类，游戏类别不能为空");
        //                }
        //                if (StringUtils.isBlank(sysBannerInfo.getGameCode())) {
        //                    throw new ServiceException("选择游戏分类，关联游戏不能为空");
        //                }
      }
    }
  }

  @Override
  public void edit(SysBannerInfoEditDTO sysBannerInfoEditDTO) {
    SysBannerInfo sysBannerInfo1 = this.getById(sysBannerInfoEditDTO.getId());
    if (sysBannerInfo1 == null) {
      throw new ServiceException("banner信息不存在");
    }
    SysBannerInfo sysBannerInfo = sysBannerInfoConvert.toEntity(sysBannerInfoEditDTO);
    validBannerInfo(sysBannerInfo);
    if (!this.updateById(sysBannerInfo)) {
      throw new ServiceException("banner信息更新异常");
    }
  }

  @Override
  public void delete(String ids) {
    boolean flag = this.removeByIds(Arrays.asList(ids.split(",")));
    if (!flag) {
      throw new ServiceException("删除banner失败");
    }
  }

  @Override
  public void updateStatus(SysBannerInfoUpdateStatusDTO sysBannerInfoUpdateStatusDTO) {
    SysBannerInfo sysBannerInfo = this.getById(sysBannerInfoUpdateStatusDTO.getId());
    if (sysBannerInfo == null) {
      throw new ServiceException("banner信息不存在");
    }
    sysBannerInfo.setStatus(sysBannerInfoUpdateStatusDTO.getStatus());
    this.updateById(sysBannerInfo);
  }
}
