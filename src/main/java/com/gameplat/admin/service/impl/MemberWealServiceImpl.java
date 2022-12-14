package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.TrueFalse;
import com.gameplat.admin.convert.MemberWealConvert;
import com.gameplat.admin.convert.MessageInfoConvert;
import com.gameplat.admin.enums.BlacklistConstant;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.enums.MemberWealRewordEnums;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.*;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberWealVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.IPUtils;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.blacklist.BizBlacklist;
import com.gameplat.model.entity.member.*;
import com.gameplat.model.entity.message.Message;
import com.gameplat.model.entity.recharge.RechargeOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.gameplat.common.enums.TranTypes.*;
import static java.util.stream.Collectors.toList;

/**
 * ????????????????????????
 *
 * @author lily
 * @date 2021/11/22
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberWealServiceImpl extends ServiceImpl<MemberWealMapper, MemberWeal>
        implements MemberWealService {
    @Autowired
    private MemberWealConvert wealConvert;

    @Autowired
    private MemberWealMapper memberWealMapper;

    @Autowired
    private MemberGrowthRecordMapper memberGrowthRecordMapper;

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberBlackListService blackListService;

    @Autowired
    private MemberWealDetailService wealDetailService;

    @Autowired
    private MemberGrowthConfigMapper growthConfigMapper;

    @Autowired
    private MemberGrowthLevelMapper growthLevelMapper;

    @Autowired
    private ValidWithdrawService validWithdrawService;

    @Autowired
    private MemberWealRewordService wealRewordService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private MemberBillService memberBillService;

    @Autowired
    private MessageInfoConvert messageInfoConvert;

    @Autowired
    private MemberService memberService;

    @Autowired
    private GameBetDailyReportService gameBetDailyReportService;

  @Autowired private BizBlacklistMapper blacklistMapper;

    @Override
    public IPage<MemberWealVO> findMemberWealList(IPage<MemberWeal> page, MemberWealDTO queryDTO) {
        return this.lambdaQuery()
                .like(ObjectUtils.isNotEmpty(queryDTO.getName()), MemberWeal::getName, queryDTO.getName())
                .eq(
                        ObjectUtils.isNotEmpty(queryDTO.getStatus()),
                        MemberWeal::getStatus,
                        queryDTO.getStatus())
                .eq(ObjectUtils.isNotEmpty(queryDTO.getType()), MemberWeal::getType, queryDTO.getType())
                .ge(
                        ObjectUtils.isNotEmpty(queryDTO.getStartDate()),
                        MemberWeal::getCreateTime,
                        queryDTO.getStartDate())
                .le(
                        ObjectUtils.isNotEmpty(queryDTO.getEndDate()),
                        MemberWeal::getCreateTime,
                        queryDTO.getEndDate())
                .orderByDesc(MemberWeal::getCreateTime)
                .page(page)
                .convert(wealConvert::toVo);
    }

    @Override
    public void addMemberWeal(MemberWealAddDTO dto) {
        if (StringUtils.isBlank(dto.getName())) {
            throw new ServiceException("?????????????????????");
        }
        if (dto.getType() == null) {
            throw new ServiceException("?????????????????????");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new ServiceException("?????????????????????");
        }
        if (dto.getMinRechargeAmount() == null) {
            throw new ServiceException("?????????????????????????????????");
        }
        if (dto.getMinBetAmount() == null) {
            throw new ServiceException("?????????????????????????????????");
        }
        dto.setStatus(0);

        Assert.isTrue(this.save(wealConvert.toEntity(dto)), "????????????!");
    }

    @Override
    public void updateMemberWeal(MemberWealEditDTO dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new ServiceException("id????????????!");
        }
        Assert.isTrue(this.updateById(wealConvert.toEntity(dto)), "????????????!");
    }

    @Override
    public void deleteMemberWeal(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new ServiceException("id????????????!");
        }
        Assert.isTrue(this.removeById(id), "????????????!");
    }

    @Override
    public void settleWeal(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new ServiceException("id????????????!");
        }
        MemberWeal memberWeal = memberWealMapper.selectById(id);
        if (memberWeal == null) {
            throw new ServiceException("??????????????????!");
        }
        // ????????????
        Integer type = memberWeal.getType();
        // 0 ?????????????????????????????????   1 VIP????????????????????????????????????
        Integer wealModel = memberWeal.getModel();
        // ????????????
        Integer status = memberWeal.getStatus();
        if (status != 0 && status != 1) {
            throw new IllegalArgumentException("??????????????????,???????????????");
        }
        // ???????????????????????????????????????
        List<MemberWealDetail> memberSalaryInfoList =
                memberGrowthRecordMapper.getMemberSalaryInfo(type);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // ???????????????????????????????????????????????????
        List<String> rechargeAccountList = new ArrayList<>();
        // ?????????????????????????????????????????????(???????????????)
        List<String> betAccountList = new ArrayList<>();

        // ????????????????????????????????????   ?????? ???????????????  VIP????????????
        if ((type == 1 || type == 2) && wealModel == 1) {
            //???????????????????????????????????????????????????
            rechargeAccountList = rechargeOrderMapper.getWealVipRecharge(
                    type,
                    formatter.format(memberWeal.getStartDate()),
                    formatter.format(memberWeal.getEndDate()));
            // ?????????????????????
            betAccountList = gameBetDailyReportService.getWealVipValid(
                    type,
                    DateUtil.parse(formatter.format(memberWeal.getStartDate()),"yyyy-MM-dd").toDateStr(),
                    DateUtil.parse(formatter.format(memberWeal.getEndDate()),"yyyy-MM-dd").toDateStr()
            );
        } else {
            if (memberWeal.getMinRechargeAmount() != null
                    && memberWeal.getMinRechargeAmount().compareTo(new BigDecimal("0")) > 0) {
                rechargeAccountList =
                        rechargeOrderMapper.getSatisfyRechargeAccount(
                                String.valueOf(memberWeal.getMinRechargeAmount()),
                                formatter.format(memberWeal.getStartDate()),
                                formatter.format(memberWeal.getEndDate()));
            } else {
                rechargeAccountList =
                        memberSalaryInfoList.stream().map(MemberWealDetail::getUserName).collect(toList());
            }

            if (memberWeal.getMinBetAmount() != null
                    && memberWeal.getMinBetAmount().compareTo(new BigDecimal("0")) > 0) {
                betAccountList = gameBetDailyReportService.getSatisfyBetAccount(
                        String.valueOf(memberWeal.getMinBetAmount()),
                        DateUtil.parse(formatter.format(memberWeal.getStartDate()),"yyyy-MM-dd").toDateStr(),
                        DateUtil.parse(formatter.format(memberWeal.getEndDate()),"yyyy-MM-dd").toDateStr()
                );
            } else {
                betAccountList =
                        memberSalaryInfoList.stream().map(MemberWealDetail::getUserName).collect(toList());
            }
        }

        // ????????????????????????????????????&&????????????????????????????????????????????????
        List<String> intersectionList =
                rechargeAccountList.stream().filter(betAccountList::contains).collect(toList());
        if (type == 3) {
            // ????????????
            // ?????????????????????????????????????????????
            if (CollectionUtil.isNotEmpty(intersectionList)) {
                List<Member> users = memberMapper.findByUserNameList(intersectionList);
                intersectionList =
                        users.stream()
                                .filter(
                                        item ->
                                                item.getBirthday() != null
                                                        && DateUtil.parse(DateUtil.format(item.getBirthday(), "MM-dd"), "MM-dd")
                                                        .compareTo(
                                                                DateUtil.parse(
                                                                        DateUtil.format(memberWeal.getStartDate(), "MM-dd"),
                                                                        "MM-dd"))
                                                        >= 0
                                                        && DateUtil.parse(DateUtil.format(item.getBirthday(), "MM-dd"), "MM-dd")
                                                        .compareTo(
                                                                DateUtil.parse(
                                                                        DateUtil.format(memberWeal.getEndDate(), "MM-dd"),
                                                                        "MM-dd"))
                                                        <= 0)
                                .map(Member::getAccount)
                                .filter(intersectionList::contains)
                                .collect(toList());
            }
        }

        // ???????????????????????????????????????????????? intersectionList ?????????????????????
        List<String> finalIntersectionList1 = intersectionList;
        List<MemberWealDetail> finalIntersectionList = new ArrayList<>();
        finalIntersectionList =
                memberSalaryInfoList.stream()
                        .filter(
                                item ->
                                        finalIntersectionList1.stream()
                                                .map(String::toString)
                                                .collect(Collectors.toList())
                                                .contains(item.getUserName()))
                        .collect(Collectors.toList());

        List<MemberWealDetail> list = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(finalIntersectionList)) {
      finalIntersectionList = this.checkBizBlacks(type, finalIntersectionList);
            // ???????????????????????????
            int totalUserCount = 0;
            BigDecimal totalPayMoney = new BigDecimal("0");
            for (MemberWealDetail memberWealDetail : finalIntersectionList) {
                MemberWealDetail model = new MemberWealDetail();
                model.setWealId(id);
                model.setUserId(memberWealDetail.getUserId());
                model.setUserName(memberService.getById(memberWealDetail.getUserId()).getAccount());
                model.setLevel(memberInfoService.lambdaQuery()
                        .eq(MemberInfo::getMemberId, memberWealDetail.getUserId())
                        .one()
                        .getVipLevel());
                model.setRewordAmount(memberWealDetail.getRewordAmount());
                model.setStatus(1);
                model.setCreateBy(GlobalContextHolder.getContext().getUsername());
                model.setCreateTime(DateUtil.date());
                list.add(model);
                totalUserCount++;
                totalPayMoney = totalPayMoney.add(memberWealDetail.getRewordAmount());
            }
            memberWeal.setTotalUserCount(totalUserCount);
            memberWeal.setTotalPayMoney(totalPayMoney);
        }else{
            memberWeal.setTotalUserCount(0);
            memberWeal.setTotalPayMoney(new BigDecimal(0));
        }

        // ??????????????????????????????????????????????????????????????????
        wealDetailService.removeWealDetail(id);
        if (CollectionUtil.isNotEmpty(list)) {
            wealDetailService.batchSave(list);
        }
        memberWeal.setSettleTime(new Date());
        memberWeal.setStatus(1);
        memberWeal.setUpdateBy(GlobalContextHolder.getContext().getUsername());
        memberWeal.setUpdateTime(new Date());
        if (!this.updateById(memberWeal)) {
            throw new ServiceException("????????????!");
        }
    }

    /**
     * ????????????
     */
    @Override
    public void distributeWeal(Long wealId, HttpServletRequest request) {
        try {
            MemberWealDetail memberWealDetail = new MemberWealDetail();
            memberWealDetail.setWealId(wealId);
            // ???????????????????????????????????????
            List<MemberWealDetail> list = wealDetailService.findSatisfyMember(memberWealDetail);
            String serialNumber = IdWorker.getIdStr();
            // ????????????id???????????????
            MemberWeal memberWeal = memberWealMapper.selectById(wealId);
            if (memberWeal == null) {
                throw new ServiceException("??????????????????!");
            }
            if (CollectionUtil.isNotEmpty(list)) {
                // ?????????????????????????????????
                list = list.stream().filter(item -> item.getStatus() == 1).collect(toList());
                // 0:???????????? 1????????????  2????????????  3??????????????? 4???????????????
                Integer type = memberWeal.getType();
        BigDecimal wealRewordDama =
            memberWeal.getWealRewordDama() == null
                ? BigDecimal.ONE
                : memberWeal.getWealRewordDama();
                // ?????????????????????
                MemberGrowthConfig growthConfig =
                        growthConfigMapper.findOneConfig(LanguageEnum.app_zh_CN.getCode());
                // ?????????????????????????????????  ?????????????????????
                Integer limitLevel = growthConfig.getLimitLevel();
                if (limitLevel == null) {
                    limitLevel = 50;
                }
                // ????????????????????????
                List<MemberGrowthLevel> levels =
                        growthLevelMapper.findList(limitLevel + 1, LanguageEnum.app_zh_CN.getCode());
                // ??????7000?????????
                int pageSize = 7000;
                // ??????????????????????????????
                int size = list.size();
                log.info("??????????????????????????????:" + size);
                int part = size / pageSize;
                for (int j = 1; j <= part + 1; j++) {
                    int fromIndex = (j - 1) * pageSize;
                    int toIndex = j * pageSize;
                    // ???????????????
                    if (size < toIndex) {
                        toIndex = size;
                    }
                    log.info("??????fromIndex???toIndex??????{}?????????{}???", fromIndex, toIndex);
                    List<MemberWealDetail> pageList = list.subList(fromIndex, toIndex);
                    // ????????????
                    if (CollectionUtil.isNotEmpty(pageList)) {
                        log.info("??????{},???{}???????????????,??????:{}", wealId, j, pageList.size());
                        for (MemberWealDetail item : pageList) {
                            try {
                                if (item.getStatus() != 1) {
                                    return;
                                }
                                int sourceType = 0;
                                Integer level = item.getLevel();
                                if (level >= levels.size()) {
                                    level = levels.size() - 1;
                                }
                                String content = "";
                                // ?????????
                                if (1 == type) {
                                    sourceType = TranTypes.WEEK_WEAL.getValue();
                                    content = "??????????????????????????? ??????:" + item.getRewordAmount() + "????????????";
                                    // ?????????
                                } else if (2 == type) {
                                    sourceType = TranTypes.MONTH_WEAL.getValue();
                                    content = "??????????????????????????? ??????:" + item.getRewordAmount() + "????????????";
                                    // ????????????
                                } else if (3 == type) {
                                    sourceType = TranTypes.BIRTH_WEAL.getValue();
                                    content = "????????????????????????????????? ??????:" + item.getRewordAmount() + "????????????";
                                    // ????????????
                                } else if (4 == type) {
                                    sourceType = TranTypes.RED_ENVELOPE_WEAL.getValue();
                                    content = "??????????????????????????? ??????:" + item.getRewordAmount() + "????????????";
                                }
                                // ??????????????????
                                Member member = memberMapper.selectById(item.getUserId());
                                if (BeanUtil.isEmpty(member)) {
                                    return;
                                }
                                // ????????????
                                MemberWealRewordAddDTO memberWealReword = new MemberWealRewordAddDTO();
                                memberWealReword.setUserId(member.getId());
                                memberWealReword.setUserName(member.getAccount());
                                memberWealReword.setOldLevel(item.getLevel());
                                memberWealReword.setCurrentLevel(item.getLevel());
                                memberWealReword.setRewordAmount(item.getRewordAmount());
                                memberWealReword.setUserType(member.getUserType());
                                memberWealReword.setParentId(member.getParentId());
                                memberWealReword.setParentName(member.getParentName());
                                memberWealReword.setAgentPath(member.getSuperPath());
                                // 0 ????????????  1????????????  2????????????  3???????????????  4???????????????
                                memberWealReword.setType(type);
                                memberWealReword.setSerialNumber(serialNumber);
                                // ????????????
                                if (BooleanEnum.YES.match(growthConfig.getIsAutoPayReword())) {

                                    //????????????
                                    BigDecimal beforeBalance = memberInfoService.lambdaQuery()
                                            .eq(MemberInfo::getMemberId, item.getUserId())
                                            .one()
                                            .getBalance();

                                    //??????????????????
                                    memberInfoService.updateBalance(item.getUserId(), item.getRewordAmount());

                                    // ??????????????????????????????
                                    content = content.replaceAll("????????????", "");
                                    String sourceId = String.valueOf(IdWorker.getId());
                  BigDecimal bigDecimal = item.getRewordAmount().setScale(2, RoundingMode.HALF_UP);
                  // ???????????????
                  RechargeOrder rechargeOrder = new RechargeOrder();
                  rechargeOrder.setMemberId(member.getId());
                  rechargeOrder.setAmount(BigDecimal.ZERO);
                  rechargeOrder.setNormalDml(BigDecimal.ZERO);
                  rechargeOrder.setDiscountAmount(bigDecimal);
                  rechargeOrder.setDiscountDml(bigDecimal.multiply(wealRewordDama));
                  rechargeOrder.setRemarks(member.getAccount() + content + "???????????????");
                  rechargeOrder.setAccount(member.getAccount());
                  validWithdrawService.addRechargeOrder(rechargeOrder);
                                    // ??????????????????
                                    MemberBill memberBill = new MemberBill();
                                    memberBill.setMemberId(member.getId());
                                    memberBill.setAccount(member.getAccount());
                                    memberBill.setMemberPath(member.getSuperPath());
                                    memberBill.setTranType(sourceType);
                                    memberBill.setOrderNo(sourceId);
                                    memberBill.setAmount(item.getRewordAmount());
                                    memberBill.setBalance(beforeBalance);
                                    memberBill.setRemark(content);
                                    memberBill.setContent(content);
                                    memberBill.setOperator("system");
                                    memberBillService.save(memberBill);

                                    // ?????????
                                    memberWealReword.setStatus(2);
                                    memberWealReword.setDrawTime(DateTime.now());
                                    // ?????????????????????????????????  ????????????????????????
                                } else {
                                    // ?????????????????? ????????????
                                    memberWealReword.setStatus(1);
                                }
                                // 6.5 ?????? ???????????????
                                MessageInfoAddDTO dto = new MessageInfoAddDTO();
                                Message message = messageInfoConvert.toEntity(dto);

                                message.setTitle("VIP????????????");
                                message.setContent(content);
                                message.setCategory(PushMessageEnum.MessageCategory.SYS_SEND.getValue());
                                message.setPosition(PushMessageEnum.MessageCategory.CATE_DEF.getValue());
                                message.setShowType(PushMessageEnum.MessageShowType.SHOW_DEF.value());
                                message.setPopsCount(PushMessageEnum.PopCount.POP_COUNT_DEF.getValue());
                                message.setPushRange(PushMessageEnum.UserRange.SOME_MEMBERS.getValue());
                                message.setLinkAccount(member.getAccount());
                                message.setType(1);
                                message.setStatus(1);
                                message.setCreateBy("System");
                                messageMapper.saveReturnId(message);

                                // ??????????????????
                                memberWealReword.setParentId(member.getParentId());
                                memberWealReword.setParentName(member.getParentName());
                                memberWealReword.setAgentPath(member.getSuperPath());
                                memberWealReword.setUserType(member.getUserType());
                                wealRewordService.insertMemberWealReword(memberWealReword);
                                // ???????????????????????? ????????????
                                wealDetailService.updateByWealStatus(item.getWealId(), 2);
                            } catch (Exception e) {
                                // fixme ??????????????????
                            }
                        }
                    }
                }
            }
            log.info("????????????????????????!????????????:{}", DateTime.now());
            // ?????????????????????
            // ???????????????????????? ???????????????????????? ????????? ??????
            memberWeal.setStatus(2);
            memberWeal.setPayTime(DateTime.now());
            memberWeal.setSerialNumber(serialNumber);
            if (!this.updateById(memberWeal)) {
                throw new ServiceException("????????????");
            }
            ;
            log.info("??????:{}????????????!", wealId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

  /**
   * ????????????
   */
  @Override
  public void recycleWeal(Long wealId, HttpServletRequest request) {
    // ????????????????????????????????????-->???????????????????????????????????????  ??????????????????????????????
    MemberWeal memberWeal = memberWealMapper.selectById(wealId);
    // ????????????
    TranTypes tranType = this.getWealType(memberWeal.getType());
    // ?????????
    String serialNumber = memberWeal.getSerialNumber();
    List<MemberWealReword> rewordList =
            wealRewordService.findList(
                    new MemberWealRewordDTO() {
                      {
                        setSerialNumber(serialNumber);
                      }
                    });
    // ??????ip
    String ipAddress = IPUtils.getIpAddress(request);
    if (CollectionUtil.isNotEmpty(rewordList)) {
      // ????????????????????????????????????, ??????7000?????????
      int pageSize = 7000;
      int size = rewordList.size();
      log.info("??????????????????????????????:" + size);

      int part = size / pageSize;
      for (int j = 1; j <= part + 1; j++) {
        int fromIndex = (j - 1) * pageSize;
        int toIndex = j * pageSize;
        if (size < toIndex) {
          // ???????????????
          toIndex = size;
        }
        log.info("??????fromIndex???toIndex??????{}?????????{}???", fromIndex, toIndex);
        List<MemberWealReword> pageList = rewordList.subList(fromIndex, toIndex);
        // ????????????
        if (CollectionUtil.isNotEmpty(pageList)) {
          log.info("????????????{},???{}???????????????,??????:{}", wealId, j, pageList.size());
          for (MemberWealReword reword : pageList) {
            log.info("???????????????????????????{}", reword.getStatus());
            try {
              // ?????????
              if (reword.getStatus() == 2) {
                log.info("??????????????????");
                // ??????????????????
                Member member = memberMapper.selectById(reword.getUserId());
                // ????????????
                BigDecimal negate = reword.getRewordAmount().negate();

                BigDecimal currentBalance = memberInfoService.getById(member.getId()).getBalance();
                //????????????
                memberInfoService.updateBalance(member.getId(),negate);

                // ??????????????????
                MemberBill memberBill = new MemberBill();
                memberBill.setMemberId(member.getId());
                memberBill.setAccount(member.getAccount());
                memberBill.setMemberPath(member.getSuperPath());
                memberBill.setTranType(tranType.getValue());
                memberBill.setOrderNo(RandomUtil.randomNumbers(22));
                memberBill.setAmount(negate);
                memberBill.setBalance(currentBalance);
                memberBill.setRemark(tranType.getDesc());
                memberBill.setContent(tranType.getDesc());
                memberBill.setOperator("system");
                memberBillService.save(memberBill);
                log.info("??????????????????");
                //
              }
              // ???????????????
              reword.setStatus(3);
              // ????????????
              reword.setInvalidTime(DateTime.now());
              // ??????????????????
              wealRewordService.updateWealRecord(reword);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
    log.info("????????????????????????!????????????:{}", DateTime.now());

    // ??????????????????(??????)
    wealDetailService.updateByWealStatus(memberWeal.getId(), 3);

    // ??????????????????
    memberWeal.setStatus(3);
    if (!this.updateById(memberWeal)) {
      throw new ServiceException("????????????");
    }
  }

  private TranTypes getWealType(Integer type) {
    if (ObjectUtil.equal(
            MemberWealRewordEnums.MemberWealRewordStatus.MONTH_WEAL.getValue(), type)) {
      return MONTH_WEAL_RECYCLE;
    }

    if (ObjectUtil.equal(
            MemberWealRewordEnums.MemberWealRewordStatus.BIRTH_WEAL.getValue(), type)) {
      return BIRTH_WEAL_RECYCLE;
    }

    if (ObjectUtil.equal(
            MemberWealRewordEnums.MemberWealRewordStatus.RED_MONTH_WEAL.getValue(), type)) {
      return RED_ENVELOPE_WEAL_RECYCLE;
    }
    if (ObjectUtil.equal(MemberWealRewordEnums.MemberWealRewordStatus.WEEK_WEAL.getValue(), type)) {
      return WEEK_WEAL_RECYCLE;
    }
    return WEEK_WEAL_RECYCLE;
  }

  public List<MemberWealDetail> checkBizBlacks(
      Integer wealType, List<MemberWealDetail> finalIntersectionList) {
    // ???????????? ??????????????? ??????
    QueryWrapper<BizBlacklist> queryBizWrapper = new QueryWrapper<>();
    String bizBlackTypes = "";
    if (wealType == 0) {
      bizBlackTypes = BlacklistConstant.BizBlacklistType.LEVEL_UPGRADE_REWARD.getValue();
    } else if (wealType == 1) {
      bizBlackTypes = BlacklistConstant.BizBlacklistType.LEVEL_WAGE_WEEK.getValue();
    } else if (wealType == 2) {
      bizBlackTypes = BlacklistConstant.BizBlacklistType.LEVEL_WAGE_MONTH.getValue();
    } else if (wealType == 3) {
      bizBlackTypes = BlacklistConstant.BizBlacklistType.LEVEL_BIRTH.getValue();
    } else if (wealType == 4) {
      bizBlackTypes = BlacklistConstant.BizBlacklistType.LEVEL_RED_ENV.getValue();
    }
    if (StrUtil.isNotBlank(bizBlackTypes)) {
      queryBizWrapper.like("types", bizBlackTypes);
      List<BizBlacklist> bizBlacklists = blacklistMapper.selectList(queryBizWrapper);
      // ???????????????????????????
      List<BizBlacklist> accountBlackList =
          bizBlacklists.stream()
              .filter(item -> item.getTargetType() == TrueFalse.FALSE.getValue())
              .collect(Collectors.toList());
      // ???????????????????????????
      List<BizBlacklist> userLevelBlackList =
          bizBlacklists.stream()
              .filter(item -> item.getTargetType() == TrueFalse.TRUE.getValue())
              .collect(Collectors.toList());
      List<String> accountBlacks = new ArrayList<>();
      List<String> userLevelBlacks = new ArrayList<>();

      if (CollectionUtil.isNotEmpty(accountBlackList)) {
        accountBlacks =
            accountBlackList.stream().map(BizBlacklist::getTarget).collect(Collectors.toList());
        List<String> finalAccountBlacks = accountBlacks;
        finalIntersectionList.removeIf(e -> finalAccountBlacks.contains(e.getUserName()));
      }
      if (CollectionUtil.isNotEmpty(userLevelBlackList)) {
        userLevelBlacks =
            userLevelBlackList.stream().map(BizBlacklist::getTarget).collect(Collectors.toList());
        for (int i = 0; i <= finalIntersectionList.size() - 1; i++) {
          MemberWealDetail memberWealDetail = finalIntersectionList.get(i);
          Integer userLevel = memberMapper.getUserLevel(memberWealDetail.getUserName());
          if (userLevel == -999) {
            continue;
          }
          boolean fl = userLevelBlacks.contains(userLevel.toString());
          if (fl == true) {
            finalIntersectionList.remove(i);
          }
        }
      }
    }

    return finalIntersectionList;
  }
}
