package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.component.MemberQueryCondition;
import com.gameplat.admin.config.TenantConfig;
import com.gameplat.admin.constant.SystemConstant;
import com.gameplat.admin.convert.MemberConvert;
import com.gameplat.admin.enums.MemberEnums;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.MemberLevelVO;
import com.gameplat.admin.model.vo.MemberVO;
import com.gameplat.admin.model.vo.MessageDistributeVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.util.TableIndexUtils;
import com.gameplat.model.entity.game.GameTransferInfo;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.spread.SpreadLinkInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

  @Autowired private MemberMapper memberMapper;

  @Autowired private MemberConvert memberConvert;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private MemberQueryCondition memberQueryCondition;

  @Autowired private PasswordService passwordService;

  @Autowired private MemberRemarkService memberRemarkService;

  @Autowired private OnlineUserService onlineUserService;

  @Autowired private TenantConfig tenantConfig;

  @Autowired private GameTransferInfoService gameTransferInfoService;

  @Autowired private SpreadLinkInfoService spreadLinkInfoService;

  @Override
  public IPage<MemberVO> queryPage(Page<Member> page, MemberQueryDTO dto) {
    return memberMapper
        .queryPage(page, memberQueryCondition.builderQueryWrapper(dto))
        .convert(this::setOnlineStatus);
  }

  @Override
  public IPage<MessageDistributeVO> pageMessageDistribute(Page<Member> page, MemberQueryDTO dto) {
    return memberMapper
        .queryPage(page, memberQueryCondition.builderQueryWrapper(dto))
        .convert(this::setOnlineStatus)
        .convert(memberConvert::toVo);
  }

  @Override
  public List<MemberVO> queryList(MemberQueryDTO dto) {
    return memberMapper.queryList(memberQueryCondition.builderQueryWrapper(dto));
  }

  @Override
  public MemberInfoVO getInfo(Long id) {
    return memberMapper.getMemberInfo(id);
  }

  @Override
  public MemberInfoVO getMemberInfo(String account) {
    return memberMapper.getMemberInfoByAccount(account);
  }

  @Override
  public void add(MemberAddDTO dto) {
    this.preAddCheck(dto);

    Member member = memberConvert.toEntity(dto);
    member.setRegisterSource(MemberEnums.RegisterSource.BACKEND.value());
    member.setPassword(passwordService.encode(member.getPassword(), dto.getAccount()));

    // 设置上级
    this.setMemberParent(member);

    // 保存会员和会员详情
    Assert.isTrue(this.save(member), "新增会员失败!");

    MemberInfo memberInfo =
        MemberInfo.builder().memberId(member.getId()).rebate(dto.getRebate()).build();
    Assert.isTrue(memberInfoService.save(memberInfo), "新增会员失败!");
  }

  @Override
  public void update(MemberEditDTO dto) {
    MemberInfo memberInfo =
        MemberInfo.builder().memberId(dto.getId()).rebate(dto.getRebate()).build();

    // 更新会员信息和会员详情
    Assert.isTrue(
        this.updateById(memberConvert.toEntity(dto)) && memberInfoService.updateById(memberInfo),
        "修改会员信息失败!");
  }

  @Override
  public void enable(List<Long> ids) {
    this.changeStatus(ids, MemberEnums.Status.ENABlED.value());
  }

  @Override
  public void disable(List<Long> ids) {
    this.changeStatus(ids, MemberEnums.Status.DISABLED.value());
    // TODO 查询是否有进入第三方游戏，第三方游戏踢线并回收额度
  }

  @Override
  public void clearContact(MemberContactCleanDTO dto) {
    this.update()
        .func(query -> dto.getFields().forEach(field -> query.set(field, null)))
        .between("create_time", dto.getStartTime(), dto.getEndTime())
        .update(new Member());
  }

  @Override
  public Member getById(Long id) {
    return this.lambdaQuery()
        .eq(Member::getId, id)
        .oneOpt()
        .orElseThrow(() -> new ServiceException("会员信息不存在!"));
  }

  @Override
  public Optional<Member> getByAccount(String account) {
    return this.lambdaQuery().eq(Member::getAccount, account).oneOpt();
  }

  @Override
  public Optional<Member> getAgentByAccount(String account) {
    return this.lambdaQuery()
        .eq(Member::getAccount, account)
        .eq(Member::getUserType, MemberEnums.Type.AGENT.value())
        .oneOpt();
  }

  @Override
  public Optional<String> getSupperPath(String account) {
    return this.lambdaQuery()
        .select(Member::getSuperPath)
        .eq(Member::getAccount, account)
        .oneOpt()
        .map(Member::getSuperPath);
  }

  @Override
  public List<Member> getByParentName(String parentName) {
    return this.lambdaQuery().eq(Member::getParentName, parentName).list();
  }

  @Override
  public List<String> findAccountByUserLevelIn(List<String> levelsLists) {
    return this.lambdaQuery()
        .select(Member::getAccount)
        .in(Member::getUserLevel, levelsLists)
        .list()
        .stream()
        .map(Member::getAccount)
        .collect(Collectors.toList());
  }

  @Override
  public void updateContact(MemberContactUpdateDTO dto) {
    if (!this.updateById(memberConvert.toEntity(dto))) {
      throw new ServiceException("更新会员联系方式失败!");
    }
  }

  @Override
  public void resetPassword(MemberPwdUpdateDTO dto) {
    Member member = this.getById(dto.getId());
    String password = passwordService.encode(dto.getPassword(), member.getAccount());

    Assert.isTrue(
        this.lambdaUpdate()
            .set(Member::getPassword, password)
            .eq(Member::getId, member.getId())
            .update(new Member()),
        "重置会员密码失败!");

    // 更新会员备注
    if (StringUtils.isNotBlank(dto.getRemark())) {
      memberRemarkService.update(dto.getId(), dto.getRemark());
    }
  }

  @Override
  public void resetWithdrawPassword(MemberWithdrawPwdUpdateDTO dto) {
    Member member = this.getById(dto.getId());
    String password = passwordService.encryptCashPassword(dto.getPassword());
    Assert.isTrue(
        memberInfoService
            .lambdaUpdate()
            .set(MemberInfo::getCashPassword, password)
            .eq(MemberInfo::getMemberId, member.getId())
            .update(new MemberInfo()),
        "重置会员提现密码失败!");
  }

  @Override
  public void changeWithdrawFlag(Long id, String flag) {
    Assert.isTrue(
        this.lambdaUpdate()
            .set(Member::getWithdrawFlag, flag)
            .eq(Member::getId, id)
            .update(new Member()),
        "修改会员提现状态失败!");
  }

  @Override
  public void resetRealName(MemberResetRealNameDTO dto) {
    Member member = this.getById(dto.getId());
    Assert.isTrue(
        this.lambdaUpdate()
            .set(Member::getRealName, dto.getRealName())
            .eq(Member::getId, member.getId())
            .update(new Member()),
        "重置会员真实姓名失败!");
  }

  @Override
  public List<Member> getListByUserLevel(List<String> userLevelList) {
    return Optional.ofNullable(userLevelList)
        .filter(CollectionUtil::isNotEmpty)
        .map(e -> this.lambdaQuery().in(Member::getUserLevel, e).list())
        .orElse(null);
  }

  @Override
  public List<Member> getListByAgentAccount(String agentAccout) {
    return memberMapper.getListByAgentAccout(agentAccout);
  }

  @Override
  public void updateRealName(Long memberId, String realName) {
    Assert.isTrue(
        this.lambdaUpdate().set(Member::getRealName, realName).eq(Member::getId, memberId).update(),
        "修改会员真实姓名失败!");
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_REMARK_CACHE, key = "#memberIds", multi = true)
  public void updateRemark(List<Long> memberIds, String remark) {
    Assert.isTrue(
        this.lambdaUpdate()
            .in(Member::getId, memberIds)
            .set(Member::getRemark, remark)
            .update(new Member()),
        "批量修改会员备注失败!");
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_REMARK_CACHE, key = "#memberId")
  public void updateRemark(Long memberId, String remark) {
    this.updateRemark(Collections.singletonList(memberId), remark);
  }

  public void preAddCheck(MemberAddDTO dto) {
    Assert.isTrue(this.isExist(dto.getAccount()), "用户名已存在!");
  }

  /**
   * 检查用户名是否存在
   *
   * @param account String
   * @return boolean
   */
  private boolean isExist(String account) {
    return this.lambdaQuery().eq(Member::getAccount, account).exists();
  }

  /**
   * 设置在线状态
   *
   * @param vo MemberVO
   * @return MemberVO
   */
  private MemberVO setOnlineStatus(MemberVO vo) {
    vo.setOnline(onlineUserService.isOnline(vo.getAccount()));
    return vo;
  }

  /**
   * 设置会员上级
   *
   * @param member Member
   */
  private void setMemberParent(Member member) {
    String parentName =
        StringUtils.getIfEmpty(
            member.getParentName(), () -> this.getMemberRoot(member.getUserType()));

    Member parent =
        this.getByAccount(parentName).orElseThrow(() -> new ServiceException("代理账号不存在!"));

    member.setParentId(parent.getId());
    member.setParentName(parent.getAccount());
    member.setSuperPath(parent.getSuperPath().concat(member.getAccount()).concat("/"));

    if (MemberEnums.Type.AGENT.match(member.getUserType())) {
      member.setAgentLevel(parent.getAgentLevel() + 1);
    }

    // 更新下级人数
    memberMapper.updateLowerNumByAccount(parent.getAccount(), 1);
  }

  /**
   * 根据会员类型获取根代理
   *
   * @param userType String
   * @return String
   */
  private String getMemberRoot(String userType) {
    return MemberEnums.Type.TEST.match(userType)
        ? SystemConstant.DEFAULT_TEST_ROOT
        : SystemConstant.DEFAULT_WEB_ROOT;
  }

  /**
   * 批量修改会员状态
   *
   * @param ids List
   * @param status int
   */
  private void changeStatus(List<Long> ids, int status) {
    Assert.notEmpty(ids, "会员ID不能为空");

    Assert.isTrue(
        this.lambdaUpdate()
            .set(Member::getStatus, status)
            .in(Member::getId, ids)
            .update(new Member()),
        "批量启用失败!");
  }

  @Override
  public Member getMemberAndFillGameAccount(String account) {
    Member member = this.getByAccount(account).orElseThrow(() -> new ServiceException("会员信息不存在!"));
    if (StringUtils.isBlank(member.getGameAccount())) {
      Assert.notNull(tenantConfig.getTenantCode(), "平台编码未配置，请联系客服");
      // 固定13位
      StringBuffer gameAccount =
          new StringBuffer(tenantConfig.getTenantCode()).append(member.getId());
      String suffix = RandomUtil.randomString(13 - gameAccount.length());
      member.setGameAccount(gameAccount.append(suffix).toString());
      Assert.isTrue(this.updateById(member), "添加会员游戏账号信息!");
    }
    // 会员余额存在哪个游戏中
    if (ObjectUtil.isNull(gameTransferInfoService.getInfoByMemberId(member.getId()))) {
      GameTransferInfo gameTransferInfo = new GameTransferInfo();
      gameTransferInfo.setPlatformCode(TransferTypesEnum.SELF.getCode());
      gameTransferInfo.setAccount(member.getAccount());
      gameTransferInfo.setMemberId(member.getId());
      gameTransferInfoService.saveOrUpdate(gameTransferInfo);
    }
    return member;
  }

  @Override
  public Integer getMaxLevel() {
    return memberMapper.getMaxLevel();
  }

  /**
   * 获取开启了工资的代理
   *
   * @param list
   * @return
   */
  @Override
  public List<Member> getOpenSalaryAgent(List<Integer> list) {
    return memberMapper.getOpenSalaryAgent(list);
  }

  @Override
  public List<Member> getListByAccountList(List<String> accountList) {
    return Optional.ofNullable(accountList)
        .filter(CollectionUtil::isNotEmpty)
        .map(e -> this.lambdaQuery().in(Member::getAccount, e).list())
        .orElse(null);
  }

  /**
   * 获取各个充值层级下会员数量和锁定会员数量
   *
   * @return
   */
  @Override
  public List<MemberLevelVO> getUserLevelAccountNum() {
    return memberMapper.getUserLevelAccountNum();
  }

  @Override
  public Integer getUserLevelTotalAccountNum(Integer userLevel) {
    return memberMapper.getUserLevelTotalAccountNum(userLevel);
  }

  /**
   * 获取代理线下的会员账号信息
   *
   * @param memberQueryDTO MemberQueryDTO
   * @return List
   */
  @Override
  public List<Member> getMemberListByAgentAccount(MemberQueryDTO memberQueryDTO) {
    return memberMapper.getMemberListByAgentAccount(memberQueryDTO);
  }

  /**
   * 添加账号或添加下级时 彩票投注返点下拉
   *
   * @param agentAccount
   * @return
   */
  @Override
  public JSONArray getRebateForAdd(String agentAccount) {
    BigDecimal max = new BigDecimal("9").setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal min = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
    if (StrUtil.isNotBlank(agentAccount)) {
      BigDecimal userRebate = memberInfoService.findUserRebate(agentAccount);
      max = ObjectUtils.isNull(userRebate) ? max : userRebate.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    JSONArray jsonArray = new JSONArray();
    BigDecimal rebate = max;
    Integer base = 1800;
    BigDecimal value = BigDecimal.ZERO;
    String text = "";
    DecimalFormat format = new DecimalFormat("0.00#");
    while (rebate.compareTo(min) >= 0) {
      JSONObject jsonObject = new JSONObject();
      value = rebate;
      Integer baseData = base + value.multiply(BigDecimal.valueOf(20L)).intValue();
      text = rebate.toString().concat("% ---- ").concat(baseData.toString());
      jsonObject.set("value", format.format(value));
      jsonObject.set("text", text);
      jsonArray.add(jsonObject);
      rebate = rebate.subtract(BigDecimal.valueOf(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    return jsonArray;
  }

  /**
   * 编辑用户时 彩票投注返点数据集
   *
   * @param agentAccount
   * @return
   */
  @Override
  public JSONArray getRebateForEdit(String agentAccount) {
    BigDecimal min = BigDecimal.ZERO;
    BigDecimal max = BigDecimal.ZERO;
    if (StrUtil.isBlank(agentAccount)) {
      min = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
      max = new BigDecimal("9").setScale(2, BigDecimal.ROUND_HALF_UP);
    } else {
      // 查询出父级的账号
      MemberInfoVO memberInfoByAccount = memberMapper.getMemberInfoByAccount(agentAccount);
      BigDecimal userRebate = memberInfoService.findUserRebate(memberInfoByAccount.getParentName());
      max =
          ObjectUtils.isNull(userRebate)
              ? new BigDecimal("9").setScale(2, BigDecimal.ROUND_HALF_UP)
              : userRebate.setScale(2, BigDecimal.ROUND_HALF_UP);
      // 获取直属下级的最大返点等级
      BigDecimal userLowerMaxRebate = memberInfoService.findUserLowerMaxRebate(agentAccount);
      min =
          ObjectUtils.isNull(userLowerMaxRebate)
              ? new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP)
              : userLowerMaxRebate.setScale(2, BigDecimal.ROUND_HALF_UP);
      // 再获取此账号推广码的最大返点等级
      LambdaQueryWrapper<SpreadLinkInfo> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper
          .eq(SpreadLinkInfo::getAgentAccount, agentAccount)
          .orderByDesc(SpreadLinkInfo::getRebate)
          .last("limit 1")
          .select(SpreadLinkInfo::getRebate);
      SpreadLinkInfo linkMinRebateObj = spreadLinkInfoService.getOne(queryWrapper);
      BigDecimal linkMinRebate = BigDecimal.ZERO;
      if (BeanUtil.isEmpty(linkMinRebateObj)) {
        linkMinRebate = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
      } else {
        linkMinRebate =
            BigDecimal.valueOf(linkMinRebateObj.getRebate()).setScale(2, BigDecimal.ROUND_HALF_UP);
      }

      min = min.compareTo(linkMinRebate) >= 0 ? min : linkMinRebate;
    }

    JSONArray jsonArray = new JSONArray();
    BigDecimal rebate = max;
    Integer base = 1800;
    BigDecimal value = BigDecimal.ZERO;
    String text = "";
    DecimalFormat format = new DecimalFormat("0.00#");
    while (rebate.compareTo(min) >= 0) {
      JSONObject jsonObject = new JSONObject();
      value = rebate;
      Integer baseData = base + value.multiply(BigDecimal.valueOf(20L)).intValue();
      text = rebate.toString().concat("% ---- ").concat(baseData.toString());
      jsonObject.set("value", format.format(value));
      jsonObject.set("text", text);
      jsonArray.add(jsonObject);
      rebate = rebate.subtract(BigDecimal.valueOf(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    return jsonArray;
  }
}
