package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.RechargeOrderHistoryConvert;
import com.gameplat.admin.mapper.RechargeOrderHistoryMapper;
import com.gameplat.admin.model.dto.RechargeOrderHistoryQueryDTO;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.RechargeOrderHistoryService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.recharge.RechargeOrderHistory;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
@Log4j2
public class RechargeOrderHistoryServiceImpl
    extends ServiceImpl<RechargeOrderHistoryMapper, RechargeOrderHistory>
    implements RechargeOrderHistoryService {

  private final String RECH_TEST_TYPE = "P";
  private final String RECH_FORMAL_TYPE_QUERY = "M,A";
  /** 充值会员、代理 */
  private final String RECH_FORMAL_TYPE = "M";

  @Autowired private RechargeOrderHistoryConvert rechargeOrderHistoryConvert;

  @Autowired(required = false)
  private RechargeOrderHistoryMapper rechargeOrderHistoryMapper;

  @Override
  public IPage<RechargeOrderHistoryVO> findPage(
      Page<RechargeOrderHistory> page, RechargeOrderHistoryQueryDTO dto) {
    LambdaQueryWrapper<RechargeOrderHistory> query = buildSql(dto);
    query.orderBy(
        ObjectUtils.isNotEmpty(dto.getOrder()),
        !ObjectUtils.isEmpty(dto.getOrder()) && "ASC".equals(dto.getOrder()),
        "createTime".equals(dto.getOrderBy())
            ? RechargeOrderHistory::getCreateTime
            : RechargeOrderHistory::getAuditTime);
    return this.page(page, query).convert(rechargeOrderHistoryConvert::toVo);
  }

  @Override
  public void rechReport(
          RechargeOrderHistoryQueryDTO dto, HttpServletRequest request, HttpServletResponse response) {
    LambdaQueryWrapper<RechargeOrderHistory> query = buildSql(dto);
    query.orderBy(
            ObjectUtils.isNotEmpty(dto.getOrder()),
            !ObjectUtils.isEmpty(dto.getOrder()) && "ASC".equals(dto.getOrder()),
            "createTime".equals(dto.getOrderBy())
                    ? RechargeOrderHistory::getCreateTime
                    : RechargeOrderHistory::getAuditTime);
    List<RechargeOrderReportVo> list =
            this.list(query).stream()
                    .map(rechargeOrderHistoryConvert::toRechVo)
                    .collect(Collectors.toList());
    ExportParams exportParams = new ExportParams("入款数据", "入款数据");
    exportParams.setMaxNum(10000);
    try {
      response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = rechReport.xls");
      Workbook workbook =
              ExcelExportUtil.exportExcel(exportParams, RechargeOrderReportVo.class, list);
      workbook.write(response.getOutputStream());
    } catch (IOException e) {
      log.info("导出入款记录报错", e);
      throw new ServiceException("导出失败:" + e);
    }
  }

  @Override
  public RechargeHistorySummaryVO findSumRechargeOrderHistory(RechargeOrderHistoryQueryDTO dto) {
    dto.setAuditTime(DateUtil.endOfDay(DateUtil.yesterday()));
    LambdaQueryWrapper<RechargeOrderHistory> query = buildSql(dto);
    return rechargeOrderHistoryMapper.summaryRechargeOrderHistory(query);
  }

  private LambdaQueryWrapper<RechargeOrderHistory> buildSql(RechargeOrderHistoryQueryDTO dto) {
    LambdaQueryWrapper<RechargeOrderHistory> query = Wrappers.lambdaQuery();
    query
        .in(
            ObjectUtils.isNotNull(dto.getModeList()),
            RechargeOrderHistory::getMode,
            dto.getModeList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getPointFlag()),
            RechargeOrderHistory::getPointFlag,
            dto.getPointFlag())
        .in(
            ObjectUtils.isNotEmpty(dto.getDiscountTypes()),
            RechargeOrderHistory::getDiscountType,
            dto.getDiscountTypes())
        .in(
            ObjectUtils.isNotNull(dto.getPayTypeList()),
            RechargeOrderHistory::getPayType,
            dto.getPayTypeList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getStatus()),
            RechargeOrderHistory::getStatus,
            dto.getStatus())
        .in(
            ObjectUtils.isNotEmpty(dto.getPayAccountOwnerList()),
            RechargeOrderHistory::getPayAccountOwner,
            dto.getPayAccountOwnerList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getTpMerchantId()),
            RechargeOrderHistory::getTpMerchantId,
            dto.getTpMerchantId())
        .ge(
            ObjectUtils.isNotEmpty(dto.getAmountFrom()),
            RechargeOrderHistory::getAmount,
            dto.getAmountFrom())
        .le(
            ObjectUtils.isNotEmpty(dto.getAmountTo()),
            RechargeOrderHistory::getAmount,
            dto.getAmountTo())
        /*.eq(
        ObjectUtils.isNotEmpty(dto.getMemberType()),
        RechargeOrderHistory::getMemberType,
        dto.getMemberType())*/
        .in(
            ObjectUtils.isNotEmpty(dto.getMemberType())
                && dto.getMemberType().equalsIgnoreCase(RECH_FORMAL_TYPE),
            RechargeOrderHistory::getMemberType,
            RECH_FORMAL_TYPE_QUERY.split(","))
        .eq(
            ObjectUtils.isNotEmpty(dto.getMemberType())
                && dto.getMemberType().equalsIgnoreCase(RECH_TEST_TYPE),
            RechargeOrderHistory::getMemberType,
            dto.getMemberType())
        .in(
            ObjectUtils.isNotNull(dto.getMemberLevelList()),
            RechargeOrderHistory::getMemberLevel,
            dto.getMemberLevelList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getOrderNo()),
            RechargeOrderHistory::getOrderNo,
            dto.getOrderNo())
        .eq(
            ObjectUtils.isNotEmpty(dto.getSuperAccount()),
            RechargeOrderHistory::getSuperAccount,
            dto.getSuperAccount())
        .like(
            ObjectUtils.isNotEmpty(dto.getRemarks()),
            RechargeOrderHistory::getRemarks,
            dto.getRemarks())
        .like(
            ObjectUtils.isNotEmpty(dto.getAuditRemarks()),
            RechargeOrderHistory::getAuditRemarks,
            dto.getAuditRemarks())
        .eq(
            ObjectUtils.isNotEmpty(dto.getOrderNo()),
            RechargeOrderHistory::getOrderNo,
            dto.getOrderNo())
        .le(
            ObjectUtils.isNotNull(dto.getAuditTime()),
            RechargeOrderHistory::getAuditTime,
            dto.getAuditTime());
    if (ObjectUtils.isNotEmpty(dto.getAuditorAccounts())) {
      query.in(RechargeOrderHistory::getAuditorAccount, dto.getAuditorAccounts().split(","));
    }
    if (ObjectUtils.isEmpty(dto.getOrderBy()) || dto.getOrderBy().equals("createTime")) {
      query
          .ge(
              ObjectUtils.isNotEmpty(dto.getBeginDatetime()),
              RechargeOrderHistory::getCreateTime,
              dto.getBeginDatetime())
          .le(
              ObjectUtils.isNotEmpty(dto.getEndDatetime()),
              RechargeOrderHistory::getCreateTime,
              dto.getEndDatetime());
    } else {
      query
          .ge(
              ObjectUtils.isNotEmpty(dto.getBeginDatetime()),
              RechargeOrderHistory::getAuditTime,
              dto.getBeginDatetime())
          .le(
              ObjectUtils.isNotEmpty(dto.getEndDatetime()),
              RechargeOrderHistory::getAuditTime,
              dto.getEndDatetime());
    }
    if (ObjectUtils.isNotEmpty(dto.getAccounts())) {
      query.in(RechargeOrderHistory::getAccount, dto.getAccounts().split(","));
    }
    if (dto.isSuperPathLike()) {
      query.like(
          ObjectUtils.isNotEmpty(dto.getSuperAccount()),
          RechargeOrderHistory::getSuperPath,
          dto.getSuperAccount());
    } else {
      query.eq(
          ObjectUtils.isNotEmpty(dto.getSuperAccount()),
          RechargeOrderHistory::getSuperAccount,
          dto.getSuperAccount());
    }
    return query;
  }
}
