package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
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
import com.gameplat.admin.model.vo.GameBetValidRecordVo;
import com.gameplat.admin.model.vo.ValidAccoutWithdrawVo;
import com.gameplat.admin.model.vo.ValidWithdrawVO;
import com.gameplat.admin.model.vo.ValidateDmlBeanVo;
import com.gameplat.admin.service.ValidWithdrawService;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.model.bean.limit.MemberWithdrawLimit;
import com.gameplat.elasticsearch.page.PageResponse;
import com.gameplat.elasticsearch.service.IBaseElasticsearchService;
import com.gameplat.model.entity.ValidWithdraw;
import com.gameplat.model.entity.recharge.RechargeOrder;
import lombok.extern.log4j.Log4j2;
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

  private static BigDecimal safetyGetDecimal(BigDecimal d) {
    return Optional.ofNullable(d).orElse(BigDecimal.ZERO);
  }

  /** 直接取消指定小数点后的数据 int 返回小数点后的位数 num 要处理的数字 四舍五入 */
  public static BigDecimal toFix(int digital, BigDecimal num) {
    return num.setScale(digital, RoundingMode.HALF_UP);
  }

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
    // 申请提现至出款期间如果有新的充值，出款时直接删除申请提现之前的打码量，而不是更新status为1
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

  public void updateTypeByUserId(Long memberId, Date createTime) {
    this.lambdaUpdate()
        .set(ValidWithdraw::getType, 1)
        .set(ValidWithdraw::getEndTime, createTime)
        .eq(ValidWithdraw::getMemberId, memberId)
        .eq(ValidWithdraw::getType, 0)
        .update(new ValidWithdraw());
  }

  @Override
  public int saveValidWithdraw(ValidWithdraw validWithdraw) {
    // 根据会员id查找
    ValidWithdraw query = new ValidWithdraw();
    query.setMemberId(validWithdraw.getMemberId());
    ValidWithdraw validWithdraw1 = validWithdrawMapper.findValidWithdraw(query);

    int save = validWithdrawMapper.save(validWithdraw);
    if (save > 0) {
      if (validWithdraw1 != null) {
        validWithdraw1.setUpdateTime(new Date());
        validWithdrawMapper.updateByUserId(validWithdraw1);
      }
    }
    return save;
  }

  public List<ValidWithdraw> queryByMemberIdAndAddTimeLessThanOrEqualToAndStatus(
      String account, Integer status) {
    return this.lambdaQuery()
        .eq(ValidWithdraw::getAccount, account)
        /* .le(ValidWithdraw::getCreateTime, maxTime)*/
        .eq(ValidWithdraw::getStatus, status)
        .orderByAsc(ValidWithdraw::getType)
        .orderByDesc(ValidWithdraw::getId)
        .list();
  }

  @Override
  public ValidateDmlBeanVo validateByMemberId(
      MemberWithdrawLimit memberWithdrawLimit, String name, boolean ignoreOuted)
      throws ServiceException {
    Integer status = ignoreOuted ? 0 : null;
    // 计算未完成的打码量
    countAccountValidWithdraw(name);
    List<ValidWithdraw> validWithdraws =
        this.queryByMemberIdAndAddTimeLessThanOrEqualToAndStatus(name, status);
    if (validWithdraws.stream().map(ValidWithdraw::getStatus).anyMatch(s -> 0 == s)) {

      // 有未出款打码量时，过滤掉已出款记录，防止打码量操作并发时充值没有正常删除掉已出款记录
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

  private ValidateDmlBeanVo passAll(
      String username, List<ValidWithdraw> validWithdraws, BigDecimal relaxQuota) {
    ValidateDmlBeanVo validateDmlBean = new ValidateDmlBeanVo();

    BigDecimal count = getCount(validWithdraws);
    log.info("打码量计算:{}", count);
    validateDmlBean.setRemainRequiredDml(BigDecimal.ZERO);
    validateDmlBean.setUsername(username);
    // 常态打码量
    BigDecimal requireDml =
        validWithdraws.stream()
            .map(ValidWithdraw::getDmlClaim)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    log.info("总共要求打码量：{}", requireDml);
    // 要求打码量
    validateDmlBean.setRequireDML(requireDml);
    // 放宽额度
    log.info("放宽额度：{}", relaxQuota);
    validateDmlBean.setRelaxQuota(relaxQuota);

    BigDecimal sumBetAmount =
        validWithdraws.stream()
            .map(ValidWithdraw::getBetAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    log.info("总投注打码量：{}", sumBetAmount);
    // 有效投注额
    validateDmlBean.setSumAllDml(sumBetAmount);
    // 总打码量 通过-true，不通过false
    validateDmlBean.setAllDmlPass(true);

    // 常态打码金额
    BigDecimal rechMoney =
        validWithdraws.stream()
            .map(ValidWithdraw::getRechMoney)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    log.info("总共需要常态打码量：{}", rechMoney);
    // 需要扣除金额
    // validateDmlBean.setSumAllDeduct(sumAllDeduct);
    validateDmlBean.setYetWithdraw(BigDecimal.ZERO);
    validateDmlBean.setRows(BeanUtils.mapList(validWithdraws, ValidWithdrawVO.class));
    return validateDmlBean;
  }

  /**
   * @param username String
   * @param validWithdraws List
   * @param relaxQuota 放宽额度
   * @return ValidateDmlBeanVo
   */
  private ValidateDmlBeanVo validateValidWithdraws(
      String username,
      List<ValidWithdraw> validWithdraws,
      BigDecimal relaxQuota,
      MemberWithdrawLimit memberWithdrawLimit) {
    ValidateDmlBeanVo validateDmlBean = new ValidateDmlBeanVo();
    boolean allDmlPass = true;
    log.info("放宽额度：{}", relaxQuota);
    BigDecimal count = getCount(validWithdraws);
    log.info("打码量计算:{}", count);
    if (count.compareTo(Convert.toBigDecimal(0)) < 0) {
      // 剩余打码量-放宽额度
      BigDecimal subtract = count.abs().subtract(relaxQuota);
      if (subtract.compareTo(Convert.toBigDecimal(0)) > -1) {
        allDmlPass = false;
      }
      // 提现需打码金额
      if (subtract.compareTo(BigDecimal.ZERO) < 1) {
        validateDmlBean.setRemainRequiredDml(BigDecimal.ZERO);
      } else {
        validateDmlBean.setRemainRequiredDml(subtract);
      }
    } else {
      // 提现需打码金额
      validateDmlBean.setRemainRequiredDml(BigDecimal.ZERO);
    }
    validateDmlBean.setUsername(username);
    // 常态打码量
    BigDecimal requireDML =
        validWithdraws.stream()
            .map(ValidWithdraw::getDmlClaim)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    log.info("总共要求打码量：{}", requireDML);
    // 要求打码量
    validateDmlBean.setRequireDML(requireDML);
    // 放宽额度
    validateDmlBean.setRelaxQuota(relaxQuota);

    BigDecimal sumBetAmount =
        validWithdraws.stream()
            .map(ValidWithdraw::getBetAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    log.info("总投注打码量：{}", sumBetAmount);
    // 有效投注额
    validateDmlBean.setSumAllDml(sumBetAmount);
    // 总打码量 通过-true，不通过false
    validateDmlBean.setAllDmlPass(allDmlPass);

    // 常态打码金额
    BigDecimal rechMoney =
        validWithdraws.stream()
            .map(ValidWithdraw::getRechMoney)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    log.info("总共需要常态打码量：{}", rechMoney);

    // 需要扣除金额提现手续费  0 只扣除优惠费用  1只收行政费用  2 不扣除全部费用 */
    Integer whetherDeductAdministrationCost =
        memberWithdrawLimit.getWhetherDeductAdministrationCost();
    if (ObjectUtil.equal(whetherDeductAdministrationCost, 0)) {
      BigDecimal disAmount =
          validWithdraws.stream()
              .map(ValidWithdraw::getDiscountDml)
              .reduce(BigDecimal.ZERO, BigDecimal::add);
      log.info("扣除优惠费用：{}", disAmount);
      BigDecimal fixedBalance = memberWithdrawLimit.getFixedBalance();
      log.info("扣除优惠费用百分比：{}", fixedBalance);
      BigDecimal deductAmount =
          disAmount.multiply(fixedBalance.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
      log.info("扣除优惠金额：{}", deductAmount);
      validateDmlBean.setSumAllDeduct(deductAmount);
    } else if (ObjectUtil.equal(whetherDeductAdministrationCost, 1)) {
      BigDecimal moneyRatio = memberWithdrawLimit.getAdministrationCostDeductPayMoneyRatio();
      log.info("只收行政费用百分比：{}", moneyRatio);
      BigDecimal multiply =
          validateDmlBean
              .getRemainRequiredDml()
              .multiply(moneyRatio.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
      log.info("只收取行政费用：{}", multiply);
      validateDmlBean.setSumAllDeduct(multiply);
    } else if (ObjectUtil.equal(whetherDeductAdministrationCost, 2)) {
      validateDmlBean.setSumAllDeduct(BigDecimal.ZERO);
    }
    validateDmlBean.setYetWithdraw(BigDecimal.ZERO);
    validateDmlBean.setRows(BeanUtils.mapList(validWithdraws, ValidWithdrawVO.class));
    return validateDmlBean;
  }

  public BigDecimal getCount(List<ValidWithdraw> validWithdrawList) {
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

  /** 计算打码量 */
  @Override
  public void countAccountValidWithdraw(String username) {
    List<ValidAccoutWithdrawVo> validWithdraw =
        validWithdrawMapper.findAccountValidWithdraw(username);
    if (StringUtils.isEmpty(validWithdraw)) {
      return;
    }
    String indexName = ContextConstant.ES_INDEX.BET_RECORD_ + sysTheme.getTenantCode();
    validWithdraw.forEach(
        a -> {
          GameVaildBetRecordQueryDTO dto =
              new GameVaildBetRecordQueryDTO() {
                {
                  setAccount(a.getAccount());
                  setBeginTime(a.getCreateTime());
                  setEndTime(a.getEndTime());
                  setState("1");
                }
              };
          QueryBuilder builder = GameBetRecordSearchBuilder.buildBetRecordSearch(dto);
          // todo betTime
          SortBuilder<FieldSortBuilder> sortBuilder =
              SortBuilders.fieldSort("betTime").order(SortOrder.DESC);
          PageResponse<GameBetValidRecordVo> result =
              baseElasticsearchService.search(
                  builder, indexName, GameBetValidRecordVo.class, 0, 9999, sortBuilder);
          if (StringUtils.isNotEmpty(result.getList())) {
            ValidAccoutWithdrawVo validAccoutWithdrawVo = new ValidAccoutWithdrawVo();
            validAccoutWithdrawVo.setId(a.getId());
            // 会员一笔打码量的投注记录
            List<GameBetValidRecordVo> gameBetValidRecordVoList = result.getList();
            // 总有效投注额
            BigDecimal vaildAmount =
                gameBetValidRecordVoList.stream()
                    .map(GameBetValidRecordVo::getValidAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(Convert.toBigDecimal(1000), 2, RoundingMode.HALF_UP);
            log.info("每笔总投注记录:{}", vaildAmount);
            validAccoutWithdrawVo.setVaildAmount(vaildAmount);
            // 根据游戏类型进行分类
            Map<String, List<GameBetValidRecordVo>> map =
                gameBetValidRecordVoList.stream()
                    .collect(Collectors.groupingBy(GameBetValidRecordVo::getGameKind));
            // 初始化投注内容
            // 初始化投注内容
            List<Object> vailList = new ArrayList<>();
            map.keySet()
                .forEach(
                    b -> {
                      JSONObject jsonObject = new JSONObject();
                      List<GameBetValidRecordVo> list = map.get(b);
                      // 添加一个游戏的详情（gameKind,gameName,betAmount）
                      // 根据游戏类型进行分组
                      BigDecimal betAmount =
                          list.stream()
                              .map(GameBetValidRecordVo::getValidAmount)
                              .reduce(BigDecimal.ZERO, BigDecimal::add)
                              .divide(Convert.toBigDecimal(1000), 2, RoundingMode.HALF_UP);
                      String gameName = list.get(0).getGameName();
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

  @Override
  public void updateValidWithdraw(ValidWithdrawDto dto) {
    // 更新会员信息和会员详情
    Assert.isTrue(this.updateById(validWithdrawConvert.toEntity(dto)), "修改打码量信息失败!");
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
}
