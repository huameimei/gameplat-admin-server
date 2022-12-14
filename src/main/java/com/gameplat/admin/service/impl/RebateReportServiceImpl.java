package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.AgentDivideMapper;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.mapper.RebateReportMapper;
import com.gameplat.admin.model.dto.RebateReportDTO;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.AgentBaseService;
import com.gameplat.admin.service.AgentConfigService;
import com.gameplat.admin.service.RebateConfigService;
import com.gameplat.admin.service.RebateReportService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.NumberConstant;
import com.gameplat.common.enums.MemberEnums;
import com.gameplat.common.model.bean.AgentConfig;
import com.gameplat.model.entity.proxy.RebateConfig;
import com.gameplat.model.entity.proxy.RebateReport;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class RebateReportServiceImpl extends ServiceImpl<RebateReportMapper, RebateReport>
    implements RebateReportService {

  @Autowired private RebateReportMapper rebateReportMapper;

  @Autowired private AgentDivideMapper agentDivideMapper;

  @Autowired private MemberMapper memberMapper;

  @Autowired private AgentConfigService agentConfigService;

  @Autowired private RebateConfigService rebateConfigService;

  @Autowired private AgentBaseService agentBaseService;

  @Override
  public IPage<RebateReportVO> queryPage(
      PageDTO<AgentPlanVO> page, RebateReportDTO rebateReportDTO) {
    return rebateReportMapper.queryPage(page, rebateReportDTO);
  }

  @Override
  public List<RebateReportVO> getRebateReport(RebateReportDTO rebateReportDTO) {
    return rebateReportMapper.getRebateReport(rebateReportDTO);
  }

  @Override
  public void updateRebateReport(String countDate, String agentName) {
    // ?????????????????????????????????
    List<RebateReport> rebateReportPOList = agentDivideMapper.agentRebateReport(agentName);
    for (RebateReport rebateReport : rebateReportPOList) {
      // ????????????????????? ?????????????????? ??????????????????
      QueryWrapper queryWrapper = new QueryWrapper();
      queryWrapper.eq("user_type", MemberEnums.Type.AGENT.value());
      queryWrapper.like("super_path", "/".concat(rebateReport.getAgentName()).concat("/"));
      rebateReport.setSubAgent(memberMapper.selectCount(queryWrapper).intValue());

      QueryWrapper queryWrapper2 = new QueryWrapper();
      queryWrapper2.like("super_path", "/".concat(rebateReport.getAgentName()).concat("/"));
      queryWrapper2.eq("user_type", MemberEnums.Type.MEMBER.value());
      rebateReport.setSubMember(memberMapper.selectCount(queryWrapper2).intValue());
    }
    // ??????????????????????????????????????????
    List<String> agentNameList = rebateReportMapper.getSettlementAgent(countDate);
    rebateReportPOList =
        rebateReportPOList.stream()
            .filter(rebateReportPO -> !agentNameList.contains(rebateReportPO.getAgentName()))
            .collect(Collectors.toList());
    rebateReportPOList = Collections.synchronizedList(rebateReportPOList);
    if (CollectionUtil.isNotEmpty(rebateReportPOList)) {
      rebateReportPOList.parallelStream()
          .forEach(
              rebateReportPO -> {
                // ?????????
                rebateReportPO.setCountDate(countDate);
                rebateReportPO.setCreateBy("system");
                rebateReportPO.setUpdateBy("system");
                rebateReportPO.setCreateTime(DateUtil.parse(DateUtil.now(), "yyyy-MM-dd HH:mm:ss"));
                rebateReportPO.setUpdateTime(DateUtil.parse(DateUtil.now(), "yyyy-MM-dd HH:mm:ss"));
                // ????????????
                BigDecimal actualCommission;
                // ????????????
                BigDecimal turnoverRebate;
                // ???????????????
                BigDecimal lastNegativeProfit;

                // ??????????????????
                // 1???????????????????????????
                List<MemberReportVO> memberReportPOList =
                    getMemberReport(rebateReportPO.getAgentId(), countDate);
                Long efficientMember =
                    memberReportPOList.stream()
                        .filter(
                            memberReportPO -> memberReportPO.getEfficient() == NumberConstant.ONE)
                        .count();
                rebateReportPO.setEfficientMember(efficientMember.intValue());
                // 2???????????????
                BigDecimal waterAmount =
                    memberReportPOList.stream()
                        .map(MemberReportVO::getRebateAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                // 3???????????????
                BigDecimal dividendAmount =
                    memberReportPOList.stream()
                        .map(MemberReportVO::getDividendAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                // 4????????????????????????
                List<PlatformFeeVO> platformFeePOList =
                    getPlatformFee(rebateReportPO.getAgentId(), countDate);
                BigDecimal gameWin =
                    platformFeePOList.stream()
                        .map(PlatformFeeVO::getWinAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                rebateReportPO.setGameWin(gameWin);
                // 5?????????????????????
                BigDecimal platformFee =
                    platformFeePOList.stream()
                        .map(PlatformFeeVO::getGameFee)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                // 6???????????????????????????????????? = ???????????? + ?????? + ??????
                BigDecimal totalCost = waterAmount.add(dividendAmount).add(platformFee);
                rebateReportPO.setTotalCost(totalCost);
                // 7?????????????????????????????????????????? = ??????????????? - ????????????
                BigDecimal netProfit = gameWin.subtract(totalCost);
                // 8????????????????????????
                MemberCommissionVO memberCommissionVO =
                    rebateReportMapper.getMemberCommission(
                        rebateReportPO.getAgentId(),
                        DateUtil.format(
                            DateUtil.offsetMonth(DateUtil.parse(countDate, "yyyy-MM"), -1),
                            "yyyy-MM"));
                if (Objects.isNull(memberCommissionVO)) {
                  lastNegativeProfit = BigDecimal.ZERO;
                } else {
                  BigDecimal profitAmount =
                      memberCommissionVO.getNegativeProfit().add(memberCommissionVO.getNetProfit());
                  lastNegativeProfit =
                      profitAmount.compareTo(BigDecimal.ZERO) == -1
                          ? profitAmount
                          : BigDecimal.ZERO;
                }

                // ??????????????????
                // 1?????????????????????
                RebateConfig rebateConfigPO =
                    rebateConfigService.getRebateConfigByParam(
                        rebateReportPO.getPlanId(), netProfit, efficientMember.intValue());
                if (BeanUtil.isEmpty(rebateConfigPO)) {
                  return;
                }
                BigDecimal agentProportion = rebateConfigPO.getLowerCommission();
                BigDecimal turnoverProportion = rebateConfigPO.getTurnoverCommission();
                // 2?????????????????????
                BigDecimal subAgentCommission = BigDecimal.ZERO; // ??????????????????
                BigDecimal agentCommission =
                    subAgentCommission
                        .multiply(agentProportion)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                // 3????????????????????? = (??????????????? + ?????????????????????) * ????????????
                BigDecimal memberProportion =
                    rebateConfigPO.getCommission() == null
                        ? BigDecimal.ZERO
                        : rebateConfigPO.getCommission();
                BigDecimal memberCommission =
                    netProfit
                        .add(lastNegativeProfit)
                        .multiply(memberProportion)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                // 4??????????????? = ?????????????????? * ????????????
                BigDecimal totalValidAmount =
                    platformFeePOList.stream()
                        .map(PlatformFeeVO::getValidAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                turnoverRebate =
                    totalValidAmount
                        .multiply(turnoverProportion)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                // 5???????????????
                BigDecimal adjustmentAmount = BigDecimal.ZERO;
                RebateReportDTO rebateReportDTO = new RebateReportDTO();
                rebateReportDTO.setAgentName(agentName);
                rebateReportDTO.setCountDate(countDate);
                List<RebateReportVO> rebateReportVOS = getRebateReport(rebateReportDTO);
                if (rebateReportVOS.size() == 1) {
                  adjustmentAmount = rebateReportVOS.get(0).getAdjustmentAmount();
                }
                if (memberCommission.compareTo(BigDecimal.ZERO) <= 0) {
                  // ??????????????????????????????????????????????????? ???????????? = ?????????????????? + ???????????? + ????????????
                  actualCommission = agentCommission.add(turnoverRebate).add(adjustmentAmount);
                } else {
                  // ??????????????????????????? ???????????? = ?????????????????? + ?????????????????? + ???????????? + ????????????
                  actualCommission =
                      memberCommission
                          .add(agentCommission)
                          .add(turnoverRebate)
                          .add(adjustmentAmount);
                }
                rebateReportPO.setMemberCommission(memberCommission);
                rebateReportPO.setNegativeProfit(lastNegativeProfit);
                rebateReportPO.setTurnoverRebate(turnoverRebate);
                rebateReportPO.setConfigId(rebateConfigPO.getConfigId());
                rebateReportPO.setRebateLevel(rebateConfigPO.getRebateLevel());
                rebateReportPO.setMemberProportion(memberProportion);
                rebateReportPO.setAgentProportion(agentProportion);
                rebateReportPO.setTurnoverProportion(turnoverProportion);
                rebateReportPO.setActualCommission(actualCommission);
              });
    }

    if (StringUtils.isNotEmpty(rebateReportPOList)) {
      rebateReportMapper.batchAddRebateReport(rebateReportPOList);
    }
  }

  @Override
  public List<MemberReportVO> getMemberReport(Long agentId, String countMonth) {
    // ????????????????????????
    List<MemberReportVO> memberReportPOList =
        rebateReportMapper.getSubMemberReport(agentId, countMonth);
    if (!StringUtils.isEmpty(memberReportPOList)) {
      // ????????????????????????
      AgentConfig agentConfig = agentConfigService.getAgentConfig();
      BigDecimal limitRecharge = agentConfig.getRechargeAmountLimit();
      BigDecimal limitValidAmount = agentConfig.getValidAmountLimit();
      memberReportPOList.forEach(
          memberReportPO -> {
            // ???????????????????????????????????????
            if (memberReportPO.getRechargeAmount().compareTo(limitRecharge) >= 0
                && memberReportPO.getValidAmount().compareTo(limitValidAmount) >= 0) {
              memberReportPO.setEfficient(1);
            } else {
              memberReportPO.setEfficient(0);
            }
          });
    }
    return memberReportPOList;
  }

  @Override
  public IPage<MemberReportVO> pageMemberReport(
      PageDTO<MemberReportVO> page, Long agentId, String countMonth) {
    // ????????????????????????
    IPage<MemberReportVO> memberReportPOList =
        rebateReportMapper.pageSubMemberReport(page, agentId, countMonth);
    if (!StringUtils.isEmpty(memberReportPOList.getRecords())) {
      // ????????????????????????
      AgentConfig agentConfig = agentConfigService.getAgentConfig();
      BigDecimal limitRecharge = agentConfig.getRechargeAmountLimit();
      BigDecimal limitValidAmount = agentConfig.getValidAmountLimit();
      memberReportPOList
          .getRecords()
          .forEach(
              memberReportPO -> {
                // ???????????????????????????????????????
                if (memberReportPO.getRechargeAmount().compareTo(limitRecharge) >= 0
                    && memberReportPO.getValidAmount().compareTo(limitValidAmount) >= 0) {
                  memberReportPO.setEfficient(1);
                } else {
                  memberReportPO.setEfficient(0);
                }
              });
    }
    return memberReportPOList;
  }

  @Override
  public Page<PlatformFeeVO> gameWin(PageDTO<PlatformFeeVO> page, Long agentId, String countMonth) {
    Page<PlatformFeeVO> platformFeePOList =
        rebateReportMapper.pagePlatformFee(page, agentId, countMonth);
    platformFeePOList
        .getRecords()
        .forEach(
            platformFeePO -> {
              BigDecimal gameFee =
                  platformFeePO.getWinAmount().compareTo(BigDecimal.ZERO) == 1
                      ? platformFeePO
                          .getWinAmount()
                          .multiply(platformFeePO.getReportRate())
                          .setScale(2, BigDecimal.ROUND_DOWN)
                      : BigDecimal.ZERO;
              platformFeePO.setGameFee(gameFee);
            });
    return platformFeePOList;
  }

  @Override
  public List<PlatformFeeVO> getPlatformFee(Long agentId, String countMonth) {
    List<PlatformFeeVO> platformFeePOList = rebateReportMapper.getPlatformFee(agentId, countMonth);
    platformFeePOList.forEach(
        platformFeePO -> {
          BigDecimal gameFee =
              platformFeePO.getWinAmount().compareTo(BigDecimal.ZERO) == 1
                  ? platformFeePO
                      .getWinAmount()
                      .multiply(platformFeePO.getReportRate())
                      .setScale(2, BigDecimal.ROUND_DOWN)
                  : BigDecimal.ZERO;
          platformFeePO.setGameFee(gameFee);
        });
    return platformFeePOList;
  }

  @Override
  public GameWinVO getPlatformFeeSum(Long agentId, String countMonth) {
    return rebateReportMapper.getPlatformFeeSum(agentId, countMonth);
  }

  @Override
  public List<CompanyCostVO> pagePlatformCost(Long agentId, String countMonth) {
    List<CompanyCostVO> allCostVO = new ArrayList<>();
    // ????????????????????????
    List<MemberReportVO> memberReportPOList = getMemberReport(agentId, countMonth);
    // ????????????
    BigDecimal dividendAmount =
        memberReportPOList.stream()
            .map(MemberReportVO::getDividendAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    allCostVO.add(
        new CompanyCostVO() {
          {
            setCountMonth(countMonth);
            setItemName("??????");
            setFee(dividendAmount);
          }
        });
    // ????????????
    BigDecimal waterAmount =
        memberReportPOList.stream()
            .map(MemberReportVO::getRebateAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    allCostVO.add(
        new CompanyCostVO() {
          {
            setCountMonth(countMonth);
            setItemName("??????");
            setFee(waterAmount);
          }
        });
    // ???????????????
    List<PlatformFeeVO> platformFeePOList = getPlatformFee(agentId, countMonth);
    List<CompanyCostVO> companyCostVOS = BeanUtils.mapList(platformFeePOList, CompanyCostVO.class);
    allCostVO.addAll(companyCostVOS);
    return allCostVO;
  }

  @Override
  public MemberCommissionVO getMemberCommission(Long agentId, String countMonth) {
    return rebateReportMapper.getMemberCommission(agentId, countMonth);
  }

  @Override
  public AgentCommissionVO getAgentCommission(Long agentId, String countMonth) {
    return rebateReportMapper.getAgentCommission(agentId, countMonth);
  }

  @Override
  public Page<AgentCommissionVO> getSubAgentCommission(
      PageDTO<AgentCommissionVO> page, Long agentId, String countMonth) {
    return rebateReportMapper.pageSubAgentCommission(page, agentId, countMonth);
  }

  @Override
  public int reviewOrSettlement(Integer currentStatus, Long reportId) {
    UserCredential userCredential = SecurityUserHolder.getCredential();

    // 0????????? 1???????????? 2???????????? 3??????
    RebateReportVO report = rebateReportMapper.getReportByReportId(reportId, null);
    if (Objects.isNull(report)) {
      throw new ServiceException("????????????");
    } else if (currentStatus != report.getStatus() + 1) {
      throw new ServiceException("?????????????????????");
    } else if (report.getAccountStatus() == NumberConstant.ZERO) {
      throw new ServiceException("????????????????????????");
    } else if (currentStatus == NumberConstant.THREE) {
      // ??????
      RebateReportVO rebateReportVO = getRebateReportVO(reportId);
      if (rebateReportVO.getActualCommission().compareTo(BigDecimal.ZERO) == 1) {
        agentBaseService.settle(
            rebateReportVO.getCountDate(),
            userCredential.getUsername(),
            rebateReportVO.getAgentId(),
            rebateReportVO.getAgentName(),
            rebateReportVO.getActualCommission());
      }
    }
    return rebateReportMapper.updateStatus(reportId, currentStatus, userCredential.getUsername());
  }

  public RebateReportVO getRebateReportVO(Long reportId) {
    RebateReportDTO rebateReportDTO = new RebateReportDTO();
    rebateReportDTO.setReportId(reportId);
    List<RebateReportVO> rebateReportVOS = getRebateReport(rebateReportDTO);
    if (rebateReportVOS.size() != 1) {
      throw new ServiceException("?????????????????????");
    }
    return rebateReportVOS.get(0);
  }

  @Override
  public int batchReviewOrSettlement(Integer currentStatus, String countDate) {
    // ????????????
    if (currentStatus == NumberConstant.TWO) {
      RebateReportDTO rebateReportDTO = new RebateReportDTO();
      rebateReportDTO.setCountDate(countDate);
      rebateReportDTO.setStatus(NumberConstant.TWO);
      rebateReportDTO.setAccountStatus(NumberConstant.ONE);
      List<RebateReportVO> rebateReportVOS = getRebateReport(rebateReportDTO);
      if (rebateReportVOS.size() > 0) {
        rebateReportVOS.stream()
            .forEach(
                rebateReportVO -> {
                  if (rebateReportVO.getActualCommission().compareTo(BigDecimal.ZERO)
                      == NumberConstant.ONE) {
                    agentBaseService.settle(
                        rebateReportVO.getCountDate(),
                        SecurityUserHolder.getUsername(),
                        rebateReportVO.getAgentId(),
                        rebateReportVO.getAgentName(),
                        rebateReportVO.getActualCommission());
                  }
                });
      }
    }
    return rebateReportMapper.batchUpdateStatus(
        countDate, SecurityUserHolder.getUsername(), currentStatus);
  }

  @Override
  public int updateActualCommission(Long reportId) {
    RebateReportVO rebateReportVO = getRebateReportVO(reportId);
    // ????????????
    BigDecimal actualCommission;
    // ???????????????????????????????????????
    BigDecimal memberCommission = rebateReportVO.getMemberCommission();
    // ??????????????????
    BigDecimal agentCommission = rebateReportVO.getAgentCommission();
    // ????????????
    BigDecimal turnoverCommission = rebateReportVO.getTurnoverRebate();
    // ????????????
    BigDecimal adjustmentAmount = rebateReportVO.getAdjustmentAmount();
    if (memberCommission.compareTo(BigDecimal.ZERO) <= 0) {
      // ??????????????????????????? ???????????? = ?????????????????? + ???????????? + ????????????
      actualCommission = agentCommission.add(turnoverCommission).add(adjustmentAmount);
    } else {
      // ??????????????????????????? ???????????? = ?????????????????? + ?????????????????? + ???????????? + ????????????
      actualCommission =
          memberCommission.add(agentCommission).add(turnoverCommission).add(adjustmentAmount);
    }
    return rebateReportMapper.updateActualCommission(reportId, actualCommission);
  }
}
