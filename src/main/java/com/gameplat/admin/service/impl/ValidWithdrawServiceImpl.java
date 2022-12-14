package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.config.SysTheme;
import com.gameplat.admin.convert.ValidWithdrawConvert;
import com.gameplat.admin.mapper.ValidWithdrawMapper;
import com.gameplat.admin.model.bean.GameBetRecordSearchBuilder;
import com.gameplat.admin.model.dto.GameVaildBetRecordQueryDTO;
import com.gameplat.admin.model.dto.ValidWithdrawDto;
import com.gameplat.admin.model.dto.ValidWithdrawOperateDto;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.SysUserAuthService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.admin.service.ValidWithdrawService;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.common.model.bean.limit.MemberWithdrawLimit;
import com.gameplat.elasticsearch.page.PageResponse;
import com.gameplat.elasticsearch.service.IBaseElasticsearchService;
import com.gameplat.model.entity.ValidWithdraw;
import com.gameplat.model.entity.recharge.RechargeOrder;
import com.gameplat.model.entity.sys.SysUser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ValidWithdrawServiceImpl extends ServiceImpl<ValidWithdrawMapper, ValidWithdraw>
    implements ValidWithdrawService {

  @Autowired(required = false)
  private ValidWithdrawMapper validWithdrawMapper;

  @Autowired(required = false)
  private ValidWithdrawConvert validWithdrawConvert;

  @Resource private IBaseElasticsearchService baseElasticsearchService;

  @Resource private SysTheme sysTheme;

  @Resource
  private SysUserService sysUserService;

  @Autowired private MemberService memberService;

  @Override
  public void addRechargeOrder(RechargeOrder rechargeOrder) {
    ValidWithdraw validWithdraw = new ValidWithdraw();
    validWithdraw.setMemberId(rechargeOrder.getMemberId());
    validWithdraw.setAccount(rechargeOrder.getAccount());
    validWithdraw.setType(0);
    validWithdraw.setRechId(
        ObjectUtil.isNull(rechargeOrder.getId()) ? "" : rechargeOrder.getId().toString());
    validWithdraw.setRechMoney(rechargeOrder.getPayAmount());
    validWithdraw.setDiscountMoney(rechargeOrder.getDiscountAmount());
    validWithdraw.setDiscountDml(rechargeOrder.getDiscountDml());
    validWithdraw.setMormDml(rechargeOrder.getNormalDml());
    validWithdraw.setRemark(rechargeOrder.getRemarks());
    validWithdraw.setCreateTime(new Date());
    deleteByUserId(rechargeOrder.getMemberId(), 1);
    updateTypeByUserId(rechargeOrder.getMemberId(), validWithdraw.getCreateTime());
    this.save(validWithdraw);
  }

  @Override
  public void remove(Long memberId, Date cashDate) {
    // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????status???1
    LambdaQueryWrapper<ValidWithdraw> query = Wrappers.lambdaQuery();
    query.eq(ValidWithdraw::getMemberId, memberId).gt(ValidWithdraw::getCreateTime, cashDate);
    if (this.count(query) > 0) {
      this.remove(query);
    } else {
      this.lambdaUpdate()
          .set(ValidWithdraw::getStatus, 1)
          .eq(ValidWithdraw::getMemberId, memberId)
          .le(ValidWithdraw::getCreateTime, cashDate)
          .update();
    }
  }

  @Override
  public int saveValidWithdraw(ValidWithdraw validWithdraw) {
    // ????????????id??????
    ValidWithdraw query = new ValidWithdraw();
    query.setMemberId(validWithdraw.getMemberId());
    ValidWithdraw validWithdraw1 = validWithdrawMapper.findValidWithdraw(query);

    int save = validWithdrawMapper.save(validWithdraw);
    if (save > 0) {
      if (validWithdraw1 != null) {
        validWithdraw1.setUpdateTime(new Date());
        validWithdraw1.setEndTime(new Date());
        validWithdrawMapper.updateByUserId(validWithdraw1);
      }
    }
    return save;
  }

  @Override
  public ValidateDmlBeanVo validateByMemberId(
      MemberWithdrawLimit memberWithdrawLimit, String name, boolean ignoreOuted)
      throws ServiceException {
    Integer status = ignoreOuted ? 0 : null;
    // ???????????????????????????
    countAccountValidWithdraw(name);
    List<ValidWithdraw> validWithdraws =
        this.queryByMemberIdAndAddTimeLessThanOrEqualToAndStatus(name, status);
    if (validWithdraws.stream().map(ValidWithdraw::getStatus).anyMatch(s -> 0 == s)) {

      // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
      validWithdraws =
          validWithdraws.stream().filter(v -> 0 == v.getStatus()).collect(Collectors.toList());
      return validateValidWithdraws(
          name,
          validWithdraws,
          toFix(2, safetyGetDecimal(memberWithdrawLimit.getRelaxQuota())),
          memberWithdrawLimit);
    }
    return passAll(name, validWithdraws, memberWithdrawLimit.getRelaxQuota());
  }

  private static BigDecimal safetyGetDecimal(BigDecimal d) {
    return Optional.ofNullable(d).orElse(BigDecimal.ZERO);
  }

  @Override
  public void updateValidWithdraw(ValidWithdrawDto dto) {
    // ?????????????????????????????????
    Assert.isTrue(this.updateById(validWithdrawConvert.toEntity(dto)), "???????????????????????????!");
  }

  @Override
  public void operateValidWithdraw(ValidWithdrawOperateDto dto) {

    MemberInfoVO memberInfo = memberService.getMemberInfo(dto.getUsername());
    if (ObjectUtils.isEmpty(memberInfo)) {
      throw new ServiceException("?????????????????????");
    }
    dto.setUserId(memberInfo.getId());
    // ???????????????????????????????????????
    List<ValidWithdraw> validWithdraws =
      this.queryByMemberIdAndAddTimeLessThanOrEqualToAndStatus(dto.getUsername(), 0);

    // true ??????????????? false???????????????
    boolean isAdd = dto.getMormDml().compareTo(BigDecimal.ZERO) > 0;

    // ???????????????????????????????????????????????????
    if (CollectionUtil.isEmpty(validWithdraws)) {
      if (!isAdd) {
        throw new ServiceException("??????????????????????????????");
      }
      // ?????????????????????????????????
      ValidWithdraw vo = validWithdrawConvert.toValidWithdraw(dto);
      vo.setRechMoney(BigDecimal.ZERO);
      vo.setDiscountMoney(BigDecimal.ZERO);
      vo.setCreateTime(new Date());
      vo.setType(0);
      vo.setStatus(0);
      Assert.isTrue(this.save(vo), "???????????????????????????!");
      return;
    }
    // ????????????????????????????????????, ???????????????
    ValidWithdraw validWithdraw = validWithdraws.stream()
      .filter(Objects::nonNull)
      .filter(o ->o.getStatus().equals(0))
      .findFirst().orElse(null);
    if (ObjectUtils.isEmpty(validWithdraw)) {
      throw new ServiceException("????????????????????????????????????");
    }
    validWithdraw.setRemark(dto.getRemarks());
    validWithdraw.setMormDml(dto.getMormDml().add(validWithdraw.getMormDml()));
    this.updateById(validWithdraw);
  }

  @Override
  public void delValidWithdraw(String member) {
    this.deleteByUserName(member, 0);
  }

  @Override
  public void rollGameRebateDml(String remark) {
    LambdaUpdateWrapper<ValidWithdraw> updateWrapper = Wrappers.lambdaUpdate();
    updateWrapper
        .eq(ValidWithdraw::getRemark, remark)
        .set(ValidWithdraw::getDiscountMoney, 0)
        .set(ValidWithdraw::getDiscountDml, 0);
    validWithdrawMapper.update(null, updateWrapper);
  }

  /** ??????????????????????????????????????? int ??????????????????????????? num ?????????????????? ???????????? */
  private static BigDecimal toFix(int digital, BigDecimal num) {
    return num.setScale(digital, RoundingMode.HALF_UP);
  }

  @Override
  public void countAccountValidWithdraw(String username) {
    List<ValidAccoutWithdrawVo> validWithdraw =
        validWithdrawMapper.findAccountValidWithdraw(username);
    if (CollUtil.isEmpty(validWithdraw)) {
      return;
    }
    String indexName = ContextConstant.ES_INDEX.BET_RECORD_ + sysTheme.getTenantCode();
    log.info("???????????????{}", indexName);
    validWithdraw.forEach(
        a -> {
          GameVaildBetRecordQueryDTO dto =
              new GameVaildBetRecordQueryDTO() {
                {
                  setAccount(a.getAccount());
                  setBeginTime(a.getCreateTime());
                  setEndTime(a.getEndTime());
                  setState("1");
                  setTimeType(1);
                }
              };
          QueryBuilder builder = GameBetRecordSearchBuilder.buildBetRecordSearch(dto);
          log.info("?????????????????????{}", JSON.toJSONString(builder));
          // todo betTime
          SortBuilder<FieldSortBuilder> sortBuilder =
                  SortBuilders.fieldSort("betTime").order(SortOrder.DESC);
          PageResponse<GameBetValidRecordVo> result =
              baseElasticsearchService.search(
                  builder, indexName, GameBetValidRecordVo.class, 0, 9999, sortBuilder);
          if (ObjectUtil.isNotEmpty(result.getList())) {
            ValidAccoutWithdrawVo validAccoutWithdrawVo = new ValidAccoutWithdrawVo();
            validAccoutWithdrawVo.setId(a.getId());
            // ????????????????????????????????????
            List<GameBetValidRecordVo> gameBetValidRecordVoList = result.getList();
            // ??????????????????
            BigDecimal vaildAmount =
                gameBetValidRecordVoList.stream()
                    .map(GameBetValidRecordVo::getValidAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(Convert.toBigDecimal(1000), 2, RoundingMode.HALF_UP);
            log.info("?????????????????????:{}", vaildAmount);
            validAccoutWithdrawVo.setVaildAmount(vaildAmount);
            // ??????????????????????????????
            Map<String, List<GameBetValidRecordVo>> map =
                gameBetValidRecordVoList.stream()
                    .collect(Collectors.groupingBy(GameBetValidRecordVo::getGameKind));
            // ?????????????????????
            // ?????????????????????
            List<Object> vailList = new ArrayList<>();
            map.keySet()
                .forEach(
                    b -> {
                      JSONObject jsonObject = new JSONObject();
                      List<GameBetValidRecordVo> list = map.get(b);
                      // ??????????????????????????????gameKind,gameName,betAmount???
                      // ??????????????????????????????
                      BigDecimal betAmount =
                          list.stream()
                              .map(GameBetValidRecordVo::getValidAmount)
                              .reduce(BigDecimal.ZERO, BigDecimal::add)
                              .divide(Convert.toBigDecimal(1000), 2, RoundingMode.HALF_UP);
                      String gameName = list.get(0).getGameKindName();
                      jsonObject.put("vaildBetAmount", betAmount);
                      jsonObject.put("gameKind", b);
                      jsonObject.put("gameName", gameName);
                      vailList.add(jsonObject);
                    });
            validAccoutWithdrawVo.setBetContext(JSONArray.toJSONString(vailList));

            validWithdrawMapper.updateByBetId(validAccoutWithdrawVo);
          }
        });
  }

  private ValidateDmlBeanVo passAll(
      String username, List<ValidWithdraw> validWithdraws, BigDecimal relaxQuota) {
    ValidateDmlBeanVo validateDmlBean = new ValidateDmlBeanVo();

    BigDecimal count = getCount(validWithdraws);
    log.info("???????????????:{}", count);
    validateDmlBean.setRemainRequiredDml(BigDecimal.ZERO);
    validateDmlBean.setUsername(username);
    // ???????????????
    BigDecimal requireDml =
        validWithdraws.stream()
            .map(ValidWithdraw::getDmlClaim)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    log.info("????????????????????????{}", requireDml);
    // ???????????????
    validateDmlBean.setRequireDML(requireDml);
    // ????????????
    log.info("???????????????{}", relaxQuota);
    validateDmlBean.setRelaxQuota(relaxQuota);

    BigDecimal sumBetAmount =
        validWithdraws.stream()
            .map(ValidWithdraw::getBetAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    log.info("?????????????????????{}", sumBetAmount);
    // ???????????????
    validateDmlBean.setSumAllDml(sumBetAmount);
    // ???????????? ??????-true????????????false
    validateDmlBean.setAllDmlPass(true);

    // ??????????????????
    BigDecimal rechMoney =
        validWithdraws.stream()
            .map(ValidWithdraw::getRechMoney)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    log.info("??????????????????????????????{}", rechMoney);
    // ??????????????????
    validateDmlBean.setYetWithdraw(BigDecimal.ZERO);
    validateDmlBean.setRows(BeanUtils.mapList(validWithdraws, ValidWithdrawVO.class));
    return validateDmlBean;
  }

  /**
   * @param username String
   * @param validWithdraws List
   * @param relaxQuota ????????????
   * @return ValidateDmlBeanVo
   */
  private ValidateDmlBeanVo validateValidWithdraws(
      String username,
      List<ValidWithdraw> validWithdraws,
      BigDecimal relaxQuota,
      MemberWithdrawLimit memberWithdrawLimit) {
    ValidateDmlBeanVo validateDmlBean = new ValidateDmlBeanVo();
    boolean allDmlPass = true;
    log.info("???????????????{}", relaxQuota);
    BigDecimal count = getCount(validWithdraws);
    log.info("???????????????:{}", count);
    if (count.compareTo(Convert.toBigDecimal(0)) < 0) {
      // ???????????????-????????????
      BigDecimal subtract = count.abs().subtract(relaxQuota);
      if (subtract.compareTo(Convert.toBigDecimal(0)) > -1) {
        allDmlPass = false;
      }
      // ?????????????????????
      if (subtract.compareTo(BigDecimal.ZERO) < 1) {
        validateDmlBean.setRemainRequiredDml(BigDecimal.ZERO);
      } else {
        validateDmlBean.setRemainRequiredDml(subtract);
      }
    } else {
      // ?????????????????????
      validateDmlBean.setRemainRequiredDml(BigDecimal.ZERO);
    }
    validateDmlBean.setUsername(username);
    // ???????????????
    BigDecimal requireDml =
        validWithdraws.stream()
            .map(ValidWithdraw::getDmlClaim)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    log.info("????????????????????????{}", requireDml);
    // ???????????????
    validateDmlBean.setRequireDML(requireDml);
    // ????????????
    validateDmlBean.setRelaxQuota(relaxQuota);

    BigDecimal sumBetAmount =
        validWithdraws.stream()
            .map(ValidWithdraw::getBetAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    log.info("?????????????????????{}", sumBetAmount);
    // ???????????????
    validateDmlBean.setSumAllDml(sumBetAmount);
    // ???????????? ??????-true????????????false
    validateDmlBean.setAllDmlPass(allDmlPass);

    // ??????????????????
    BigDecimal rechMoney =
        validWithdraws.stream()
            .map(ValidWithdraw::getRechMoney)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    log.info("??????????????????????????????{}", rechMoney);

    // ?????????????????????????????????  0 ?????????????????????  1??????????????????  2 ????????????????????? */
    Integer whetherDeductAdministrationCost =
        memberWithdrawLimit.getWhetherDeductAdministrationCost();
    if (ObjectUtil.equal(whetherDeductAdministrationCost, 0)) {
      BigDecimal disAmount =
          validWithdraws.stream()
              .map(ValidWithdraw::getDiscountDml)
              .reduce(BigDecimal.ZERO, BigDecimal::add);
      log.info("?????????????????????{}", disAmount);
      BigDecimal fixedBalance = memberWithdrawLimit.getFixedBalance();
      log.info("??????????????????????????????{}", fixedBalance);
      BigDecimal deductAmount =
          disAmount.multiply(fixedBalance.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
      log.info("?????????????????????{}", deductAmount);
      validateDmlBean.setSumAllDeduct(deductAmount);
    } else if (ObjectUtil.equal(whetherDeductAdministrationCost, 1)) {
      BigDecimal moneyRatio = memberWithdrawLimit.getAdministrationCostDeductPayMoneyRatio();
      log.info("??????????????????????????????{}", moneyRatio);
      BigDecimal multiply =
          validateDmlBean
              .getRemainRequiredDml()
              .multiply(moneyRatio.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
      log.info("????????????????????????{}", multiply);
      validateDmlBean.setSumAllDeduct(multiply);
    } else if (ObjectUtil.equal(whetherDeductAdministrationCost, 2)) {
      validateDmlBean.setSumAllDeduct(BigDecimal.ZERO);
    }
    validateDmlBean.setYetWithdraw(BigDecimal.ZERO);
    validateDmlBean.setRows(BeanUtils.mapList(validWithdraws, ValidWithdrawVO.class));
    return validateDmlBean;
  }

  private BigDecimal getCount(List<ValidWithdraw> validWithdrawList) {
    validWithdrawList = validWithdrawList.stream().sorted(Comparator.comparing(ValidWithdraw::getId)).collect(Collectors.toList());
    BigDecimal validAmount = new BigDecimal(0);
    if (CollUtil.isNotEmpty(validWithdrawList)) {
      for (ValidWithdraw validWithdraw : validWithdrawList) {
        BigDecimal difference = validWithdraw.getBetAmount().subtract(validWithdraw.getDmlClaim());
        validAmount =
            (difference.compareTo(new BigDecimal(0)) > -1
                    && validAmount.compareTo(new BigDecimal(0)) > -1)
                ? new BigDecimal(0).add(validAmount)
                : (difference.add(validAmount).compareTo(new BigDecimal(0)) > -1
                    ? new BigDecimal(0)
                    : difference.add(validAmount));
      }
    }
    return validAmount;
  }

  private List<ValidWithdraw> queryByMemberIdAndAddTimeLessThanOrEqualToAndStatus(
      String account, Integer status) {
    return this.lambdaQuery()
        .eq(ValidWithdraw::getAccount, account)
        .eq(ValidWithdraw::getStatus, status)
        .orderByAsc(ValidWithdraw::getType)
        .orderByDesc(ValidWithdraw::getId)
        .list();
  }

  private void deleteByUserId(Long memberId, Integer status) {
    LambdaQueryWrapper<ValidWithdraw> query = Wrappers.lambdaQuery();
    query.eq(ValidWithdraw::getMemberId, memberId).eq(ValidWithdraw::getStatus, status);
    this.remove(query);
  }

  private void deleteByUserName(String member, Integer status) {
    LambdaQueryWrapper<ValidWithdraw> query = Wrappers.lambdaQuery();
    query.eq(ValidWithdraw::getAccount, member).eq(ValidWithdraw::getStatus, status);
    this.remove(query);
  }

  private void updateTypeByUserId(Long memberId, Date createTime) {
    this.lambdaUpdate()
        .set(ValidWithdraw::getType, 1)
        .set(ValidWithdraw::getEndTime, createTime)
        .eq(ValidWithdraw::getMemberId, memberId)
        .eq(ValidWithdraw::getType, 0)
        .update(new ValidWithdraw());
  }
}
