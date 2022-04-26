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
    // 使用平级分红方案的代理
    List<RebateReport> rebateReportPOList = agentDivideMapper.agentRebateReport(agentName);
    for (RebateReport rebateReport : rebateReportPOList) {
      // 填充每个代理的 总下级代理数 总下级会员数
      QueryWrapper queryWrapper = new QueryWrapper();
      queryWrapper.eq("user_type", MemberEnums.Type.MEMBER);
      queryWrapper.like("super_path", "/".concat(rebateReport.getAgentName()).concat("/"));
      rebateReport.setSubAgent(memberMapper.selectCount(queryWrapper).intValue());
      queryWrapper.eq("user_type", MemberEnums.Type.AGENT);
      rebateReport.setSubMember(memberMapper.selectCount(queryWrapper).intValue());
    }
    // 已结算的代理不再更新佣金报表
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
                // 初始化
                rebateReportPO.setCountDate(countDate);
                rebateReportPO.setCreateBy("system");
                rebateReportPO.setUpdateBy("system");
                rebateReportPO.setCreateTime(DateUtil.parse(DateUtil.now(), "yyyy-MM-dd HH:mm:ss"));
                rebateReportPO.setUpdateTime(DateUtil.parse(DateUtil.now(), "yyyy-MM-dd HH:mm:ss"));
                // 实际佣金
                BigDecimal actualCommission;
                // 流水返利
                BigDecimal turnoverRebate;
                // 累计负盈利
                BigDecimal lastNegativeProfit;

                // 佣金报表统计
                // 1、统计下级有效会员
                List<MemberReportVO> memberReportPOList =
                    getMemberReport(rebateReportPO.getAgentId(), countDate);
                Long efficientMember =
                    memberReportPOList.stream()
                        .filter(
                            memberReportPO -> memberReportPO.getEfficient() == NumberConstant.ONE)
                        .count();
                rebateReportPO.setEfficientMember(efficientMember.intValue());
                // 2、统计返水
                BigDecimal waterAmount =
                    memberReportPOList.stream()
                        .map(MemberReportVO::getRebateAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                // 3、统计红利
                BigDecimal dividendAmount =
                    memberReportPOList.stream()
                        .map(MemberReportVO::getDividendAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                // 4、统计公司总输赢
                List<PlatformFeeVO> platformFeePOList =
                    getPlatformFee(rebateReportPO.getAgentId(), countDate);
                BigDecimal gameWin =
                    platformFeePOList.stream()
                        .map(PlatformFeeVO::getWinAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                rebateReportPO.setGameWin(gameWin);
                // 5、统计平台费用
                BigDecimal platformFee =
                    platformFeePOList.stream()
                        .map(PlatformFeeVO::getGameFee)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                // 6、统计公司成本：公司成本 = 平台费用 + 红利 + 返水
                BigDecimal totalCost = waterAmount.add(dividendAmount).add(platformFee);
                rebateReportPO.setTotalCost(totalCost);
                // 7、统计公司净盈利：公司净盈利 = 公司总输赢 - 公司成本
                BigDecimal netProfit = gameWin.subtract(totalCost);
                // 8、上月累计负盈利
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

                // 计算实际佣金
                // 1、获取分红配置
                RebateConfig rebateConfigPO =
                    rebateConfigService.getRebateConfigByParam(
                        rebateReportPO.getPlanId(), netProfit, efficientMember.intValue());
                if (BeanUtil.isEmpty(rebateConfigPO)) {
                  return;
                }
                BigDecimal agentProportion = rebateConfigPO.getLowerCommission();
                BigDecimal turnoverProportion = rebateConfigPO.getTurnoverCommission();
                // 2、下级代理佣金
                BigDecimal subAgentCommission = BigDecimal.ZERO; // 逻辑需要补上
                BigDecimal agentCommission =
                    subAgentCommission
                        .multiply(agentProportion)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                // 3、下级会员佣金 = (公司净盈利 + 上月累计负盈利) * 佣金比例
                BigDecimal memberProportion =
                    rebateConfigPO.getCommission() == null
                        ? BigDecimal.ZERO
                        : rebateConfigPO.getCommission();
                BigDecimal memberCommission =
                    netProfit
                        .add(lastNegativeProfit)
                        .multiply(memberProportion)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                // 4、流水返利 = 总计有效投注 * 返利占比
                BigDecimal totalValidAmount =
                    platformFeePOList.stream()
                        .map(PlatformFeeVO::getValidAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                turnoverRebate =
                    totalValidAmount
                        .multiply(turnoverProportion)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                // 5、调整金额
                BigDecimal adjustmentAmount = BigDecimal.ZERO;
                RebateReportDTO rebateReportDTO = new RebateReportDTO();
                rebateReportDTO.setAgentName(agentName);
                rebateReportDTO.setCountDate(countDate);
                List<RebateReportVO> rebateReportVOS = getRebateReport(rebateReportDTO);
                if (rebateReportVOS.size() == 1) {
                  adjustmentAmount = rebateReportVOS.get(0).getAdjustmentAmount();
                }
                if (memberCommission.compareTo(BigDecimal.ZERO) <= 0) {
                  // 下级会员佣金为负数或不满足佣金要求 实际佣金 = 下级代理佣金 + 流水返利 + 佣金调整
                  actualCommission = agentCommission.add(turnoverRebate).add(adjustmentAmount);
                } else {
                  // 下级会员佣金为正数 实际佣金 = 下级会员佣金 + 下级代理佣金 + 流水返利 + 佣金调整
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
    // 统计下级有效会员
    List<MemberReportVO> memberReportPOList =
        rebateReportMapper.getSubMemberReport(agentId, countMonth);
    if (!StringUtils.isEmpty(memberReportPOList)) {
      // 获取有效会员配置
      AgentConfig agentConfig = agentConfigService.getAgentConfig();
      BigDecimal limitRecharge = agentConfig.getRechargeAmountLimit();
      BigDecimal limitValidAmount = agentConfig.getValidAmountLimit();
      memberReportPOList.forEach(
          memberReportPO -> {
            // 满足有效会员配置，计入标识
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
    // 统计下级有效会员
    IPage<MemberReportVO> memberReportPOList =
        rebateReportMapper.pageSubMemberReport(page, agentId, countMonth);
    if (!StringUtils.isEmpty(memberReportPOList.getRecords())) {
      // 获取有效会员配置
      AgentConfig agentConfig = agentConfigService.getAgentConfig();
      BigDecimal limitRecharge = agentConfig.getRechargeAmountLimit();
      BigDecimal limitValidAmount = agentConfig.getValidAmountLimit();
      memberReportPOList
          .getRecords()
          .forEach(
              memberReportPO -> {
                // 满足有效会员配置，计入标识
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
    // 统计下级有效会员
    List<MemberReportVO> memberReportPOList = getMemberReport(agentId, countMonth);
    // 统计红利
    BigDecimal dividendAmount =
        memberReportPOList.stream()
            .map(MemberReportVO::getDividendAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    allCostVO.add(
        new CompanyCostVO() {
          {
            setCountMonth(countMonth);
            setItemName("红利");
            setFee(dividendAmount);
          }
        });
    // 统计返水
    BigDecimal waterAmount =
        memberReportPOList.stream()
            .map(MemberReportVO::getRebateAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    allCostVO.add(
        new CompanyCostVO() {
          {
            setCountMonth(countMonth);
            setItemName("返水");
            setFee(waterAmount);
          }
        });
    // 公司总输赢
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

    // 0未结算 1风控审核 2财务审核 3结算
    RebateReportVO report = rebateReportMapper.getReportByReportId(reportId, null);
    if (Objects.isNull(report)) {
      throw new ServiceException("参数错误");
    } else if (currentStatus != report.getStatus() + 1) {
      throw new ServiceException("请按照流程操作");
    } else if (report.getAccountStatus() == NumberConstant.ZERO) {
      throw new ServiceException("该代理账号已停用");
    } else if (currentStatus == NumberConstant.THREE) {
      // 结算
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
      throw new ServiceException("佣金报表不存在");
    }
    return rebateReportVOS.get(0);
  }

  @Override
  public int batchReviewOrSettlement(Integer currentStatus, String countDate) {
    // 批量结算
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
    // 实际佣金
    BigDecimal actualCommission;
    // 下级会员佣金（负数计为零）
    BigDecimal memberCommission = rebateReportVO.getMemberCommission();
    // 下级代理佣金
    BigDecimal agentCommission = rebateReportVO.getAgentCommission();
    // 流水返利
    BigDecimal turnoverCommission = rebateReportVO.getTurnoverRebate();
    // 佣金调整
    BigDecimal adjustmentAmount = rebateReportVO.getAdjustmentAmount();
    if (memberCommission.compareTo(BigDecimal.ZERO) <= 0) {
      // 下级会员佣金为负数 实际佣金 = 下级代理佣金 + 流水返利 + 佣金调整
      actualCommission = agentCommission.add(turnoverCommission).add(adjustmentAmount);
    } else {
      // 下级会员佣金为正数 实际佣金 = 下级会员佣金 + 下级代理佣金 + 流水返利 + 佣金调整
      actualCommission =
          memberCommission.add(agentCommission).add(turnoverCommission).add(adjustmentAmount);
    }
    return rebateReportMapper.updateActualCommission(reportId, actualCommission);
  }
}
