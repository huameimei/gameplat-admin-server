package com.gameplat.admin.component;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gameplat.admin.model.dto.MemberQueryDTO;
import com.gameplat.common.enums.MemberEnums;
import com.gameplat.model.entity.member.Member;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 会员查询搜索条件构造
 *
 * @author robben
 */
@Component
public class MemberQueryCondition {

  /**
   * 获取会员查询条件
   *
   * @param dto MemberQueryDTO
   * @return QueryWrapper
   */
  public QueryWrapper<Member> builderQueryWrapper(MemberQueryDTO dto) {
    QueryWrapper<Member> query = Wrappers.query();
    query
        .eq(ObjectUtils.isNotEmpty(dto.getId()), "t1.id", dto.getId())
        .eq(ObjectUtils.isNotEmpty(dto.getNickname()), "t1.nickname", dto.getNickname())
        .eq(ObjectUtils.isNotNull(dto.getUserType()), "t1.user_type", dto.getUserType())
        .eq(ObjectUtils.isNotEmpty(dto.getParentName()), "t1.parent_name", dto.getParentName())
        .eq(ObjectUtils.isNotNull(dto.getStatus()), "t1.status", dto.getStatus())
        .eq(ObjectUtils.isNotEmpty(dto.getPhone()), "t1.phone", dto.getPhone())
        .eq(ObjectUtils.isNotEmpty(dto.getQq()), "t1.qq", dto.getQq())
        .eq(ObjectUtils.isNotEmpty(dto.getUserLevel()), "t1.user_level", dto.getUserLevel())
        .func(
            ObjectUtils.isNotEmpty(dto.getAgentLevel()),
            q ->
                q.eq("t1.agent_level", dto.getAgentLevel())
                    .eq("t1.user_type", MemberEnums.Type.AGENT.value()))
        .eq(ObjectUtils.isNotEmpty(dto.getVipLevel()), "t2.vip_level", dto.getVipLevel())
        .in(ObjectUtils.isNotEmpty(dto.getLevels()), "t2.vip_level", dto.getLevels())
        .eq(
            ObjectUtils.isNotEmpty(dto.getInvitationCode()),
            "t2.invitation_code",
            dto.getInvitationCode())
        .func(ObjectUtils.isNotEmpty(dto.getAccount()), q -> this.builderAccountQuery(query, dto))
        .func(
            ObjectUtils.isNotEmpty(dto.getAgentAccount()),
            q -> this.builderAgentAccountQuery(query, dto))
        .func(ObjectUtils.isNotEmpty(dto.getRealName()), q -> this.builderRealNameQuery(query, dto))
        .func(ObjectUtils.isNotEmpty(dto.getRemark()), q -> this.builderRemarkQuery(query, dto))
        .func(
            ObjectUtils.isNotEmpty(dto.getRegisterIp()),
            q -> this.builderRegisterIpQuery(query, dto))
        .func(
            ObjectUtils.isNotEmpty(dto.getLastLoginIp()),
            q -> this.builderLastLoginIpQuery(query, dto))
        // 余额范围搜索
        .ge(ObjectUtils.isNotNull(dto.getBalanceFrom()), "t2.balance", dto.getBalanceFrom())
        .le(ObjectUtils.isNotNull(dto.getBalanceTo()), "t2.balance", dto.getBalanceTo())
        // 最后登陆时间
        .between(
            ObjectUtils.isNotEmpty(dto.getLastLoginTimeFrom()),
            "t2.last_login_time",
            dto.getLastLoginTimeFrom(),
            dto.getLastLoginTimeTo())
        // 注册时间
        .between(
            ObjectUtils.isNotEmpty(dto.getRegisterTimeFrom()),
            "t1.create_time",
            dto.getRegisterTimeFrom(),
            dto.getRegisterTimeTo())
        // 最近多少天登陆过
        .ge(
            ObjectUtils.isNotNull(dto.getDayOfLogin()),
            "t2.last_login_time",
            this.getDateDiff(dto.getDayOfLogin()))
        // 最近多少天未登陆
        .func(
            ObjectUtils.isNotNull(dto.getDayOfNotLogin()),
            q -> this.builderDayOfNotLoginQuery(q, dto))
        // 累计充值次数范围
        .ge(
            ObjectUtils.isNotEmpty(dto.getRechTimesFrom()),
            "t2.total_rech_times",
            dto.getRechTimesFrom())
        .le(
            ObjectUtils.isNotEmpty(dto.getRechTimesTo()),
            "t2.total_rech_times",
            dto.getRechTimesTo())
        // 最近充值时间
        .between(
            ObjectUtils.isNotEmpty(dto.getLastRechTimeFrom()),
            "t2.last_rech_time",
            dto.getLastRechTimeFrom(),
            dto.getLastRechTimeTo())
        // 充值金额范围
        .ge(
            ObjectUtils.isNotEmpty(dto.getRechAmountFrom()),
            "t2.total_rech_amount",
            dto.getRechAmountFrom())
        .le(
            ObjectUtils.isNotEmpty(dto.getRechAmountTo()),
            "t2.total_rech_amount",
            dto.getRechAmountTo())
        // 超过多少天未充值
        .func(
            ObjectUtils.isNotNull(dto.getDayOfNoRecha()),
            q -> this.builderDayOfNotRechQuery(query, dto))
        // 累计提现次数范围
        .ge(
            ObjectUtils.isNotEmpty(dto.getWithdrawTimesFrom()),
            "t2.total_withdraw_times",
            dto.getWithdrawTimesFrom())
        .le(
            ObjectUtils.isNotEmpty(dto.getWithdrawTimesTo()),
            "t2.total_withdraw_times",
            dto.getWithdrawTimesTo())
        // 累计提现金额范围
        .ge(
            ObjectUtils.isNotEmpty(dto.getWithdrawAmountFrom()),
            "t2.total_withdraw_amount",
            dto.getWithdrawAmountFrom())
        // 最近提现时间
        .between(
            ObjectUtils.isNotEmpty(dto.getLastWithdrawTimeFrom()),
            "t2.last_withdraw_time",
            dto.getLastWithdrawTimeFrom(),
            dto.getLastWithdrawTimeTo())
        // 未首冲
        .eq(Boolean.TRUE.equals(dto.getNoFirstRech()), "t2.total_rech_times", 0)
        // 未首提
        .eq(Boolean.TRUE.equals(dto.getNoFirstWithdraw()), "t2.total_withdraw_times", 0)
        // 未二次充值
        .lt(Boolean.TRUE.equals(dto.getNotTwiceRech()), "t2.total_rech_times", 2)
        // 按创建时间默认降序
        .orderByDesc("t1.create_time");

    return query;
  }

  private Date getDateDiff(Integer diff) {
    return DateUtil.offsetDay(new Date(), null == diff ? 0 : -diff);
  }

  /**
   * 账号查询
   *
   * @param queryWrapper QueryWrapper
   * @param dto MemberQueryDTO
   */
  private void builderAccountQuery(QueryWrapper<Member> queryWrapper, MemberQueryDTO dto) {
    if (Boolean.TRUE.equals(dto.getAccountFuzzy())) {
      queryWrapper.like("t1.account", dto.getAccount());
    } else {
      queryWrapper.eq("t1.account", dto.getAccount());
    }
  }

  /**
   * 多久未充值
   *
   * @param queryWrapper QueryWrapper
   * @param dto MemberQueryDTO
   */
  private void builderDayOfNotRechQuery(QueryWrapper<Member> queryWrapper, MemberQueryDTO dto) {
    queryWrapper.and(
        query ->
            query
                .isNull("t2.last_rech_time")
                .or()
                .le("t2.last_rech_time", this.getDateDiff(dto.getDayOfNoRecha())));
  }

  /**
   * 多久未登录
   *
   * @param queryWrapper QueryWrapper
   * @param dto MemberQueryDTO
   */
  private void builderDayOfNotLoginQuery(QueryWrapper<Member> queryWrapper, MemberQueryDTO dto) {
    queryWrapper.and(
        query ->
            query
                .isNull("t2.last_login_time")
                .or()
                .le("t2.last_login_time", this.getDateDiff(dto.getDayOfNotLogin())));
  }

  /**
   * 真实姓名
   *
   * @param queryWrapper QueryWrapper
   * @param dto MemberQueryDTO
   */
  private void builderRealNameQuery(QueryWrapper<Member> queryWrapper, MemberQueryDTO dto) {
    if (Boolean.TRUE.equals(dto.getRealNameFuzzy())) {
      queryWrapper.like("t1.real_name", dto.getRealName());
    } else {
      queryWrapper.eq("t1.real_name", dto.getRealName());
    }
  }

  /**
   * 注册IP
   *
   * @param queryWrapper QueryWrapper
   * @param dto MemberQueryDTO
   */
  private void builderRegisterIpQuery(QueryWrapper<Member> queryWrapper, MemberQueryDTO dto) {
    if (Boolean.TRUE.equals(dto.getRegisterIpFuzzy())) {
      queryWrapper.like("t1.register_ip", dto.getRegisterIp());
    } else {
      queryWrapper.eq("t1.register_ip", dto.getRegisterIp());
    }
  }

  /**
   * 最后登陆IP
   *
   * @param queryWrapper QueryWrapper
   * @param dto MemberQueryDTO
   */
  private void builderLastLoginIpQuery(QueryWrapper<Member> queryWrapper, MemberQueryDTO dto) {
    if (Boolean.TRUE.equals(dto.getLastLoginIpFuzzy())) {
      queryWrapper.like("t2.last_login_ip", dto.getLastLoginIp());
    } else {
      queryWrapper.eq("t2.last_login_ip", dto.getLastLoginIp());
    }
  }

  /**
   * 备注
   *
   * @param queryWrapper QueryWrapper
   * @param dto MemberQueryDTO
   */
  private void builderRemarkQuery(QueryWrapper<Member> queryWrapper, MemberQueryDTO dto) {
    if (Boolean.TRUE.equals(dto.getRemarkFuzzy())) {
      queryWrapper.like("t1.remark", dto.getRemark());
    } else {
      queryWrapper.eq("t1.remark", dto.getRemark());
    }
  }

  /**
   * 代理账号查询
   *
   * @param queryWrapper QueryWrapper
   * @param dto MemberQueryDTO
   */
  private void builderAgentAccountQuery(QueryWrapper<Member> queryWrapper, MemberQueryDTO dto) {
    if (Boolean.TRUE.equals(dto.getSubordinateOnly())) {
      // 直属下级
      queryWrapper.eq("t1.parent_name", dto.getAgentAccount());
    } else {
      queryWrapper.like("t1.super_path", dto.getAgentAccount());
    }
  }
}
