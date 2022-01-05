package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysBannerInfoConvert;
import com.gameplat.admin.mapper.SysBannerInfoMapper;
import com.gameplat.admin.model.domain.SysBannerInfo;
import com.gameplat.admin.model.dto.SysBannerInfoAddDTO;
import com.gameplat.admin.model.dto.SysBannerInfoEditDTO;
import com.gameplat.admin.model.vo.SysBannerInfoVO;
import com.gameplat.admin.service.SysBannerInfoService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * banner业务处理
 *
 * @author admin
 */
@Service
public class SysBannerInfoServiceImpl
        extends ServiceImpl<SysBannerInfoMapper, SysBannerInfo> implements SysBannerInfoService {

    @Autowired
    private SysBannerInfoConvert sysBannerInfoConvert;

    @Override
    public List<SysBannerInfo> getByBanner(SysBannerInfo banner) {
        return this.lambdaQuery().eq(banner.getStatus() == null && banner.getStatus() != 0
                , SysBannerInfo::getStatus, banner.getStatus()).list();
    }

    @Override
    public boolean save(SysBannerInfo entity) {
        return this.save(entity);
    }

    @Override
    public IPage<SysBannerInfoVO> list(PageDTO<SysBannerInfo> page, String language) {
        if (StringUtils.isBlank(language)) {
            throw new ServiceException("");
        }
        return this.lambdaQuery().eq(SysBannerInfo::getLanguage, language).page(page).convert(sysBannerInfoConvert::toVo);
    }

    @Override
    public void add(SysBannerInfoAddDTO sysBannerInfoAddDTO) {
        if (StringUtils.isBlank(sysBannerInfoAddDTO.getLanguage())) {
            throw new ServiceException("语言必选");
        }
        SysBannerInfo sysBannerInfo = sysBannerInfoConvert.toEntity(sysBannerInfoAddDTO);
        if (!this.save(sysBannerInfo)) {
            throw new ServiceException("banner信息保存异常");
        }
    }

    @Override
    public void edit(SysBannerInfoEditDTO sysBannerInfoEditDTO) {
        SysBannerInfo sysBannerInfo = sysBannerInfoConvert.toEntity(sysBannerInfoEditDTO);
        if (!this.updateById(sysBannerInfo)) {
            throw new ServiceException("banner信息更新异常");
        }
    }

    @Override
    public void delete(String ids) {
        boolean flag = this.removeByIds(Arrays.asList(ids.split(",")));
        if (!flag) {
            throw new ServiceException("删除banner成功");
        }
    }
}
