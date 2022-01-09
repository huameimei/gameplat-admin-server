package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SpreadLinkInfoConvert;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.common.enums.DefaultEnums;
import com.gameplat.admin.mapper.SpreadLinkInfoMapper;
import com.gameplat.admin.model.domain.SpreadLinkInfo;
import com.gameplat.admin.model.dto.SpreadLinkInfoAddDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoEditDTO;
import com.gameplat.admin.model.vo.SpreadConfigVO;
import com.gameplat.admin.service.SpreadLinkInfoService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.base.common.validator.ValidatorUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import lombok.Cleanup;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

/**
 * 域名推广配置 服务实现层
 *
 * @author three
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SpreadLinkInfoServiceImpl extends ServiceImpl<SpreadLinkInfoMapper, SpreadLinkInfo>
    implements SpreadLinkInfoService {

  @Autowired private SpreadLinkInfoConvert spreadLinkInfoConvert;

  @Override
  public IPage<SpreadConfigVO> page(PageDTO<SpreadLinkInfo> page, SpreadLinkInfoDTO dto) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotNull(dto.getId()), SpreadLinkInfo::getId, dto.getId())
        .eq(
            ObjectUtils.isNotEmpty(dto.getAgentAccount()),
            SpreadLinkInfo::getAgentAccount,
            dto.getAgentAccount())
        .eq(
            ObjectUtils.isNotEmpty(dto.getSpreadType()),
            SpreadLinkInfo::getSpreadType,
            dto.getSpreadType())
        .eq(
            ObjectUtils.isNotNull(dto.getUserType()),
            SpreadLinkInfo::getUserType,
            dto.getUserType())
        .eq(ObjectUtils.isNotNull(dto.getStatus()), SpreadLinkInfo::getStatus, dto.getStatus())
        .eq(ObjectUtils.isNotEmpty(dto.getCode()), SpreadLinkInfo::getCode, dto.getCode())
        .orderBy(
            StringUtils.equals(dto.getOrderByColumn(), "createTime"),
            ValidatorUtil.isAsc(dto.getSortBy()),
            SpreadLinkInfo::getCreateTime)
        .orderBy(
            StringUtils.equals(dto.getOrderByColumn(), "visitCount"),
            ValidatorUtil.isAsc(dto.getSortBy()),
            SpreadLinkInfo::getVisitCount)
        .orderBy(
            StringUtils.equals(dto.getOrderByColumn(), "registCount"),
            ValidatorUtil.isAsc(dto.getSortBy()),
            SpreadLinkInfo::getRegistCount)
        .page(page)
        .convert(spreadLinkInfoConvert::toVo);
  }

  @Override
  public void exportList(SpreadLinkInfoDTO dto, HttpServletResponse response) {
    try{
      List<SpreadLinkInfo> list = this.lambdaQuery()
              .eq(ObjectUtils.isNotNull(dto.getId()), SpreadLinkInfo::getId, dto.getId())
              .eq(
                      ObjectUtils.isNotEmpty(dto.getAgentAccount()),
                      SpreadLinkInfo::getAgentAccount,
                      dto.getAgentAccount())
              .eq(
                      ObjectUtils.isNotEmpty(dto.getSpreadType()),
                      SpreadLinkInfo::getSpreadType,
                      dto.getSpreadType())
              .eq(
                      ObjectUtils.isNotNull(dto.getUserType()),
                      SpreadLinkInfo::getUserType,
                      dto.getUserType())
              .eq(ObjectUtils.isNotNull(dto.getStatus()), SpreadLinkInfo::getStatus, dto.getStatus())
              .eq(ObjectUtils.isNotEmpty(dto.getCode()), SpreadLinkInfo::getCode, dto.getCode())
              .orderBy(
                      StringUtils.equals(dto.getOrderByColumn(), "createTime"),
                      ValidatorUtil.isAsc(dto.getSortBy()),
                      SpreadLinkInfo::getCreateTime)
              .orderBy(
                      StringUtils.equals(dto.getOrderByColumn(), "visitCount"),
                      ValidatorUtil.isAsc(dto.getSortBy()),
                      SpreadLinkInfo::getVisitCount)
              .orderBy(
                      StringUtils.equals(dto.getOrderByColumn(), "registCount"),
                      ValidatorUtil.isAsc(dto.getSortBy()),
                      SpreadLinkInfo::getRegistCount)
              .list();
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-disposition", "attachment;filename = myExcel.xls");
      @Cleanup OutputStream outputStream = null;
      Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("域名推广列表导出", "域名推广列表"), SpreadLinkInfo.class, list);
      outputStream = response.getOutputStream();
      workbook.write(outputStream);
    }catch (IOException e) {
      throw new ServiceException("导出失败:"+e);
    }

  }

  /**
   * 新增推广信息
   *
   * @param dto 需求: 1、代理专属推广地址唯一 2、推广码唯一 3、公共推广地址下允许多个推广码 4、代理专属推广地址允许多个推广码
   */
  @Override
  public void add(SpreadLinkInfoAddDTO dto) {
    SpreadLinkInfo linkInfo = spreadLinkInfoConvert.toEntity(dto);
    if (StringUtils.isNotEmpty(linkInfo.getAgentAccount())
        && this.lambdaQuery()
            .ne(SpreadLinkInfo::getAgentAccount, linkInfo.getAgentAccount())
            .eq(SpreadLinkInfo::getExternalUrl, linkInfo.getExternalUrl())
            .eq(
                ObjectUtils.isNotEmpty(linkInfo.getCode()),
                SpreadLinkInfo::getCode,
                linkInfo.getCode())
            .or()
            .eq(
                ObjectUtils.isNotEmpty(linkInfo.getCode()),
                SpreadLinkInfo::getCode,
                linkInfo.getCode())
            .exists()) {
      throw new ServiceException("推广地址或推广码已被使用！");
    } else if (this.lambdaQuery()
        .eq(SpreadLinkInfo::getExternalUrl, linkInfo.getExternalUrl())
        .eq(ObjectUtils.isNotEmpty(linkInfo.getCode()), SpreadLinkInfo::getCode, linkInfo.getCode())
        .or()
        .eq(ObjectUtils.isNotEmpty(linkInfo.getCode()), SpreadLinkInfo::getCode, linkInfo.getCode())
        .exists()) {
      throw new ServiceException("推广地址或推广码已被使用！");
    }

    linkInfo.setExclusiveFlag(DefaultEnums.N.value());
    if (StringUtils.isNotBlank(linkInfo.getExternalUrl())
        && StringUtils.isNotBlank(linkInfo.getAgentAccount())) {
      // 设置专属
      linkInfo.setExclusiveFlag(DefaultEnums.Y.value());
    }

    if (!this.save(linkInfo)) {
      throw new ServiceException("创建失败");
    }
  }

  @Override
  public void update(SpreadLinkInfoEditDTO dto) {
    SpreadLinkInfo linkInfo = spreadLinkInfoConvert.toEntity(dto);
    if (StringUtils.isNotEmpty(linkInfo.getAgentAccount())
        && this.lambdaQuery()
            .ne(SpreadLinkInfo::getAgentAccount, linkInfo.getAgentAccount())
            .eq(SpreadLinkInfo::getExternalUrl, linkInfo.getExternalUrl())
            .eq(
                ObjectUtils.isNotNull(linkInfo.getCode()),
                SpreadLinkInfo::getCode,
                linkInfo.getCode())
            .or()
            .eq(
                ObjectUtils.isNotNull(linkInfo.getCode()),
                SpreadLinkInfo::getCode,
                linkInfo.getCode())
            .ne(SpreadLinkInfo::getId, linkInfo.getId())
            .exists()) {
      throw new ServiceException("推广地址或推广码已被使用！");
    } else if (this.lambdaQuery()
        .ne(SpreadLinkInfo::getId, linkInfo.getId())
        .eq(SpreadLinkInfo::getExternalUrl, linkInfo.getExternalUrl())
        .eq(ObjectUtils.isNotNull(linkInfo.getCode()), SpreadLinkInfo::getCode, linkInfo.getCode())
        .or()
        .eq(ObjectUtils.isNotNull(linkInfo.getCode()), SpreadLinkInfo::getCode, linkInfo.getCode())
        .ne(SpreadLinkInfo::getId, linkInfo.getId())
        .exists()) {
      throw new ServiceException("推广地址或推广码已被使用！");
    } else if (!this.updateById(linkInfo)) {
      throw new ServiceException("修改失败");
    }
  }

  @Override
  public void deleteById(Long id) {
    this.removeById(id);
  }

  @Override
  public void changeStatus(SpreadLinkInfoEditDTO dto) {
    if (!this.updateById(spreadLinkInfoConvert.toEntity(dto))) {
      throw new ServiceException("修改失败");
    }
  }

  @Override
  public void changeReleaseTime(Long id) {
    if (!this.updateById(SpreadLinkInfo.builder().id(id).createTime(new Date()).build())) {
      throw new ServiceException("修改失败");
    }
  }

  @Override
  public void batchEnableStatus(List<Long> ids) {
    if (!this.lambdaUpdate()
        .in(SpreadLinkInfo::getId, ids)
        .set(SpreadLinkInfo::getStatus, EnableEnum.ENABLED.code())
        .update(new SpreadLinkInfo())) {
      throw new ServiceException("批量修改状态失败!");
    }
  }

  @Override
  public void batchDisableStatus(List<Long> ids) {
    if (!this.lambdaUpdate()
        .in(SpreadLinkInfo::getId, ids)
        .set(SpreadLinkInfo::getStatus, EnableEnum.DISABLED.code())
        .update(new SpreadLinkInfo())) {
      throw new ServiceException("批量修改状态失败!");
    }
  }

  @Override
  public void batchDeleteByIds(List<Long> ids) {
    if (!this.removeByIds(ids)) {
      throw new ServiceException("批量删除失败");
    }
  }

  @Override
  public List<SpreadLinkInfo> getSpreadList(String agentAccount) {
    return this.lambdaQuery().eq(SpreadLinkInfo::getAgentAccount, agentAccount).list();
  }
}
