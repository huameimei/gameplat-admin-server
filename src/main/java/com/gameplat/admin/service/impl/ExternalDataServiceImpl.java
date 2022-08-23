package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gameplat.admin.mapper.*;
import com.gameplat.admin.model.vo.ExternalDataVo;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.service.ExternalDataService;
import com.gameplat.admin.service.PasswordService;
import com.gameplat.base.common.util.EasyExcelUtil;
import com.gameplat.common.enums.MemberEnums;
import com.gameplat.model.entity.member.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
// @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ExternalDataServiceImpl implements ExternalDataService {

  @Autowired private MemberMapper memberMapper;

  @Autowired private MemberLevelMapper memberLevelMapper;

  @Autowired private MemberRemarkMapper memberRemarkMapper;

  @Autowired private MemberInfoMapper memberInfoMapper;

  @Autowired private MemberGrowthLevelMapper memberGrowthLevelMapper;

  @Autowired private MemberGrowthStatisMapper memberGrowthStatisMapper;

  @Autowired private MemberGrowthRecordMapper memberGrowthRecordMapper;

  @Autowired private MemberRwReportMapper memberRwReportMapper;

  @Autowired private MemberBankMapper memberBankMapper;

  @Autowired private PasswordService passwordService;

  /**
   * 开始处理外部数据导入
   *
   * @param username
   * @param file
   * @param request
   */
  @Override
  @Async
  public void dealData(String createBy, MultipartFile file, HttpServletRequest request) {
    // 1. 解析Excel
    try {
      List<ExternalDataVo> paramlist =
          EasyExcelUtil.readExcel(file.getInputStream(), ExternalDataVo.class);
      log.info("共需导入{}条数据", paramlist.size());
      if (CollectionUtil.isEmpty(paramlist)) {
        return;
      }
      MemberInfoVO webRoot = memberMapper.getMemberInfoByAccount("webRoot");
      String webRootAgentPath = webRoot.getSuperPath();
      webRootAgentPath =
          webRootAgentPath.endsWith("/") ? webRootAgentPath : webRootAgentPath.concat("/");

      List<MemberGrowthLevel> memberGrowthLevels =
          memberGrowthLevelMapper.selectList(new QueryWrapper<>());
      Map<Integer, MemberGrowthLevel> levelMap =
          memberGrowthLevels.stream()
              .collect(
                  Collectors.toMap(
                      MemberGrowthLevel::getLevel, MemberGrowthLevel -> MemberGrowthLevel));

      List<String> repeatAccount = new ArrayList<>();
      // 每次执行50000条
      Integer pageSize = 50000;
      Integer size = paramlist.size();
      Integer part = size / pageSize;
      for (int j = 1; j <= part + 1; j++) {
        Integer fromIndex = (j - 1) * pageSize;
        Integer toIndex = j * pageSize;
        // 如果总共只需要分一批执行
        if (size < toIndex) {
          toIndex = size;
        }
        log.info("当前fromIndex和toIndex：（{}），（{}）", fromIndex, toIndex);
        List<ExternalDataVo> pageSubList = paramlist.subList(fromIndex, toIndex);
        if (CollectionUtil.isNotEmpty(pageSubList)) {
          for (ExternalDataVo externalDataVo : pageSubList) {
            if (StrUtil.isBlank(externalDataVo.getAccount())) {
              continue;
            }
            if (isRepeatAccount(externalDataVo.getAccount()) > 0) {
              repeatAccount.add(externalDataVo.getAccount());
              continue;
            }
            Member saveMember = new Member();
            externalDataVo.setAccount(externalDataVo.getAccount().toLowerCase());
            saveMember.setAccount(externalDataVo.getAccount());
            saveMember.setRegisterType(6);
            saveMember.setStatus(1);
            if (StrUtil.isNotBlank(externalDataVo.getRealname())) {
              Integer repeatRealName = isRepeatRealName(externalDataVo.getRealname());
              if (repeatRealName <= 0) {
                saveMember.setRealName(externalDataVo.getRealname());
              }
            }
            if (StrUtil.isNotBlank(externalDataVo.getPhone())) {
              Integer phone = isRepeatPhone(externalDataVo.getPhone());
              if (phone <= 0) {
                saveMember.setPhone(externalDataVo.getPhone());
                saveMember.setDialCode("0086");
              }
            }
            saveMember.setUserLevel(dealUserLevel(externalDataVo.getUserLevelName()));
            saveMember.setUserType(externalDataVo.getUserType());
            if (StrUtil.isNotBlank(externalDataVo.getDlLevel())
                && StrUtil.isNotBlank(externalDataVo.getParentName())
                && StrUtil.isNotBlank(externalDataVo.getSuperPath())) {
              saveMember.setAgentLevel(
                  externalDataVo.getUserType().equalsIgnoreCase(MemberEnums.Type.MEMBER.value())
                      ? 0
                      : Integer.valueOf(externalDataVo.getDlLevel()));
              saveMember.setParentName(externalDataVo.getParentName());
              saveMember.setSuperPath(externalDataVo.getSuperPath());
            } else {
              saveMember.setAgentLevel(
                  externalDataVo.getUserType().equalsIgnoreCase(MemberEnums.Type.MEMBER.value())
                      ? 0
                      : 2);
              saveMember.setParentId(webRoot.getId());
              saveMember.setParentName(webRoot.getAccount());
              saveMember.setSuperPath(webRootAgentPath);
              saveMember.setLanguage("zh_cn");
            }
            saveMember.setWechat(externalDataVo.getWx());
            saveMember.setQq(externalDataVo.getQq());
            saveMember.setEmail(externalDataVo.getYx());
            saveMember.setBirthday(DateUtil.parse(externalDataVo.getBirth()));
            saveMember.setRegisterSource(0);
            saveMember.setRegisterIp(externalDataVo.getRegisterIp());
            saveMember.setCreateBy(createBy);
            if (StrUtil.isNotBlank(externalDataVo.getRegisterTime())) {
              saveMember.setCreateTime(DateUtil.parse(externalDataVo.getRegisterTime()));
            } else {
              saveMember.setCreateTime(DateTime.now());
            }
            saveMember.setRemark(externalDataVo.getRemark());
            memberMapper.insert(saveMember);
            // 处理备注
            if (StrUtil.isNotBlank(externalDataVo.getRemark())) {
              saveRemark(
                  saveMember.getId(),
                  externalDataVo.getAccount(),
                  externalDataVo.getRemark(),
                  createBy);
            }

            MemberInfo saveInfoObj = new MemberInfo();
            saveInfoObj.setMemberId(saveMember.getId());
            saveInfoObj.setUserMode(1);
            saveInfoObj.setRebate("9");
            saveInfoObj.setBalance(new BigDecimal(externalDataVo.getBalance()));
            if (StrUtil.isNotBlank(externalDataVo.getRechAmount())) {
              saveInfoObj.setFirstRechTime(saveMember.getCreateTime());
              saveInfoObj.setFirstRechAmount(new BigDecimal(externalDataVo.getRechAmount()));
              saveInfoObj.setLastRechTime(saveMember.getCreateTime());
              saveInfoObj.setLastRechAmount(new BigDecimal(externalDataVo.getRechAmount()));
              saveInfoObj.setTotalRechTimes(
                  externalDataVo.getRechCount() == null ? 1 : externalDataVo.getRechCount());
              saveInfoObj.setTotalRechAmount(new BigDecimal(externalDataVo.getRechAmount()));
            }
            if (StrUtil.isNotBlank(externalDataVo.getWithAmount())) {
              saveInfoObj.setFirstWithdrawTime(saveMember.getCreateTime());
              saveInfoObj.setFirstWithdrawAmount(new BigDecimal(externalDataVo.getWithAmount()));
              saveInfoObj.setLastWithdrawTime(saveMember.getCreateTime());
              saveInfoObj.setLastWithdrawAmount(new BigDecimal(externalDataVo.getWithAmount()));
              saveInfoObj.setTotalWithdrawTimes(
                  externalDataVo.getWithCount() == null ? 1 : externalDataVo.getWithCount());
              saveInfoObj.setTotalWithdrawAmount(new BigDecimal(externalDataVo.getWithAmount()));
            }
            // 处理VIP
            dealVip(levelMap, saveMember, saveInfoObj, externalDataVo.getVipLevel(), createBy);

            // 处理充提
            if (ObjectUtil.isNotNull(externalDataVo.getRechCount())
                || StrUtil.isNotBlank(externalDataVo.getRechAmount())
                || ObjectUtil.isNotNull(externalDataVo.getWithCount())
                || StrUtil.isNotBlank(externalDataVo.getWithAmount())) {}
            {
              // 处理充提
              dealRw(
                  saveMember,
                  externalDataVo.getRechAmount(),
                  externalDataVo.getRechCount(),
                  externalDataVo.getWithAmount(),
                  externalDataVo.getWithCount());
            }
            memberInfoMapper.insert(saveInfoObj);

            // 处理银行卡
            dealMemberBank(
                saveMember,
                externalDataVo.getBankCardNo(),
                externalDataVo.getBankCardName(),
                externalDataVo.getCardNo(),
                externalDataVo.getNetwork(),
                createBy);
          }

          try {
            log.info("开始分批睡眠执行插入用户数据,当前批次：第{}批", j);
            Thread.sleep(1500);
          } catch (InterruptedException e) {
            log.info("睡眠时发生错误了!");
            e.printStackTrace();
          }
        }
      }

      // 最后将上级ID 上级名称 上级代理路径 填充正确

    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }

  /** 将注册类型为-1 的用户查询出来，然后将他的明文密码加密更新进去 并修改注册类型为3 */
  @Override
  @Async
  public void enPswd() {
    LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<Member>();
    queryWrapper.eq(Member::getRegisterType, -1);
    List<Member> members = memberMapper.selectList(queryWrapper);
    log.info("共有{}个会员需要处理", members.size());
    for (Member member : members) {
      if (StrUtil.isBlank(member.getPassword())) {
        continue;
      }
      String encode =
          passwordService.encode(member.getPassword(), member.getAccount().toLowerCase());
      member.setPassword(encode);
      member.setRegisterType(3);
      int i = memberMapper.updateById(member);
    }
  }

  public Integer isRepeatAccount(String account) {
    // 先校验是否存在
    QueryWrapper<Member> countWrapper = new QueryWrapper<>();
    countWrapper.eq("account", account);
    Long aLong = memberMapper.selectCount(countWrapper);
    return aLong == null ? 0 : aLong.intValue();
  }

  public Integer isRepeatRealName(String realname) {
    // 先校验是否存在
    QueryWrapper<Member> countWrapper = new QueryWrapper<>();
    countWrapper.eq("real_name", realname);
    Long aLong = memberMapper.selectCount(countWrapper);
    return aLong == null ? 0 : aLong.intValue();
  }

  public Integer isRepeatPhone(String phone) {
    // 先校验是否存在
    QueryWrapper<Member> countWrapper = new QueryWrapper<>();
    countWrapper.eq("phone", phone);
    Long aLong = memberMapper.selectCount(countWrapper);
    return aLong == null ? 0 : aLong.intValue();
  }

  public void saveRemark(Long memberId, String account, String remark, String createBy) {
    MemberRemark saveObj = new MemberRemark();
    saveObj.setMemberId(memberId);
    saveObj.setAccount(account);
    saveObj.setContent(remark);
    saveObj.setCreateBy(createBy);
    saveObj.setCreateTime(DateTime.now());
    memberRemarkMapper.insert(saveObj);
  }

  public Integer dealUserLevel(String levelName) {
    if (StrUtil.isBlank(levelName)) {
      // 返回默认层级
      return 0;
    }
    MemberLevel levelLike = memberLevelMapper.getLevelLike(levelName);
    if (BeanUtil.isEmpty(levelLike)) {
      // 新增
      MemberLevel saveObj = new MemberLevel();
      saveObj.setLevelName(levelName);
      saveObj.setLevelValue(memberLevelMapper.getMaxLevelValue() + 1);
      memberLevelMapper.insert(saveObj);
      return saveObj.getLevelValue();
    } else {
      return levelLike.getLevelValue();
    }
  }

  public void dealVip(
      Map<Integer, MemberGrowthLevel> levelMap,
      Member member,
      MemberInfo saveInfoObj,
      String vipLevel,
      String createBy) {
    if (StrUtil.isBlank(vipLevel) || ("0").equals(vipLevel)) {
      saveInfoObj.setVipLevel(0);
      saveInfoObj.setVipGrowth(0L);
      return;
    }
    Long growth = levelMap.get((Integer.valueOf(vipLevel) - 1)).getGrowth();
    saveInfoObj.setVipLevel(Integer.valueOf(vipLevel));
    saveInfoObj.setVipGrowth(growth);

    // 插入汇总和记录
    MemberGrowthStatis memberGrowthStatis = new MemberGrowthStatis();
    memberGrowthStatis.setMemberId(member.getId());
    memberGrowthStatis.setAccount(member.getAccount());
    memberGrowthStatis.setVipLevel(Integer.valueOf(vipLevel));
    memberGrowthStatis.setVipGrowth(growth);
    memberGrowthStatis.setBackGrowth(growth);
    memberGrowthStatis.setRechargeGrowth(0L);
    memberGrowthStatis.setSignGrowth(0L);
    memberGrowthStatis.setDamaGrowth(0L);
    memberGrowthStatis.setInfoGrowth(0L);
    memberGrowthStatis.setBindGrowth(0L);
    memberGrowthStatis.setDemoteGrowth(0L);
    memberGrowthStatis.setCreateBy(createBy);
    memberGrowthStatis.setCreateTime(DateTime.now());
    memberGrowthStatisMapper.insert(memberGrowthStatis);

    // 再增加一条VIP成长值变动记录
    MemberGrowthRecord saveRecord = new MemberGrowthRecord();
    saveRecord.setUserId(member.getId());
    saveRecord.setUserName(member.getAccount());
    saveRecord.setKindCode("plat");
    saveRecord.setKindName(
        "{\"en-US\": \"platform\", \"in-ID\": \"peron\", \"th-TH\": \"แพลตฟอร์ม\", \"vi-VN\": \"nền tảng\", \"zh-CN\": \"平台\"}");
    saveRecord.setType(3);
    saveRecord.setOldLevel(0);
    saveRecord.setCurrentLevel(Integer.valueOf(vipLevel));
    saveRecord.setChangeMult(BigDecimal.ONE);
    saveRecord.setChangeGrowth(growth);
    saveRecord.setOldGrowth(0L);
    saveRecord.setCurrentGrowth(growth);
    saveRecord.setCreateBy(createBy);
    saveRecord.setCreateTime(DateTime.now());
    saveRecord.setUpdateBy(createBy);
    saveRecord.setUpdateTime(DateTime.now());
    saveRecord.setRemark("迁移新增");
    memberGrowthRecordMapper.insert(saveRecord);
  }

  public void dealRw(
      Member member, String rechAmount, Integer rechCount, String withAmount, Integer withCount) {
    BigDecimal rechargeAmount =
        StrUtil.isNotBlank(rechAmount) ? new BigDecimal(rechAmount) : BigDecimal.ZERO;
    BigDecimal withdrawAmount =
        StrUtil.isNotBlank(withAmount) ? new BigDecimal(withAmount) : BigDecimal.ZERO;
    Integer rechargeCount = rechCount == null ? 0 : rechCount;
    Integer withdrawCount = withCount == null ? 0 : withCount;
    MemberRwReport saveRwObj = new MemberRwReport();
    saveRwObj.setMemberId(member.getId());
    saveRwObj.setAccount(member.getAccount());
    saveRwObj.setParentAccount(member.getParentName());
    saveRwObj.setSuperPath(member.getSuperPath());
    saveRwObj.setStatTime(DateUtil.parse(member.getCreateTime().toString()).toDateStr());
    saveRwObj.setAddTime(member.getCreateTime());
    saveRwObj.setWithdrawMoney(withdrawAmount);
    saveRwObj.setWithdrawCount(withdrawCount);
    saveRwObj.setCounterFee(BigDecimal.ZERO);
    saveRwObj.setFirstWithdrawMoney(withdrawAmount);
    saveRwObj.setFirstWithdrawType(1);
    saveRwObj.setOnlineMoney(rechargeAmount);
    saveRwObj.setOnlineCount(rechargeCount);
    saveRwObj.setFirstRechMoney(rechargeAmount);
    saveRwObj.setFirstRechType(2);
    memberRwReportMapper.insert(saveRwObj);
  }

  public void dealMemberBank(
      Member member,
      String cardNo,
      String cardName,
      String vCardNo,
      String network,
      String createBy) {
    List<String> split = StrUtil.split(cardNo, ",");
    for (String cn : split) {
      MemberBank saveBankObj = new MemberBank();
      saveBankObj.setMemberId(member.getId());
      saveBankObj.setCardNo(cn);
      if (StrUtil.isBlank(member.getRealName())) {
        saveBankObj.setCardHolder(member.getAccount());
      } else {
        saveBankObj.setCardHolder(member.getRealName());
      }
      String curreny = "CNY";
      if (cardName.equalsIgnoreCase("USDT")) {
        cardName = "USDT";
        curreny = "USDT";
        saveBankObj.setType("V");
      } else if (cardName.equalsIgnoreCase("GOP")) {
        cardName = "GoPay";
        curreny = "GoPay";
        saveBankObj.setType("V");
      } else if (cardName.equalsIgnoreCase("OKPAY")) {
        cardName = "OkPay";
        curreny = "OkPay";
        saveBankObj.setType("V");
      } else {
        saveBankObj.setType("B");
      }
      saveBankObj.setBankName(cardName);
      saveBankObj.setNetwork(cardName);
      saveBankObj.setCurrency(curreny);
      saveBankObj.setCreateBy(createBy);
      saveBankObj.setCreateTime(member.getCreateTime());
      saveBankObj.setUpdateBy(createBy);
      saveBankObj.setUpdateTime(DateTime.now());
      memberBankMapper.insert(saveBankObj);
    }

    List<String> splitV = StrUtil.split(vCardNo, ",");
    for (String vc : splitV) {
      MemberBank saveBankObj = new MemberBank();
      saveBankObj.setMemberId(member.getId());
      saveBankObj.setCardNo(vc);
      if (StrUtil.isBlank(member.getRealName())) {
        saveBankObj.setCardHolder(member.getAccount());
      } else {
        saveBankObj.setCardHolder(member.getRealName());
      }
      String curreny = "USDT";
      if (cardName.equalsIgnoreCase("USDT")) {
        cardName = "USDT";
        curreny = "USDT";
      } else if (cardName.equalsIgnoreCase("GOP")) {
        cardName = "GoPay";
        curreny = "GoPay";
      } else if (cardName.equalsIgnoreCase("OKPAY")) {
        cardName = "OkPay";
        curreny = "OkPay";
      }
      saveBankObj.setType("V");
      saveBankObj.setBankName(cardName);
      saveBankObj.setNetwork(StrUtil.isNotBlank(network) ? network : cardName);
      saveBankObj.setCurrency(curreny);
      saveBankObj.setCreateBy(createBy);
      saveBankObj.setCreateTime(member.getCreateTime());
      saveBankObj.setUpdateBy(createBy);
      saveBankObj.setUpdateTime(DateTime.now());
      memberBankMapper.insert(saveBankObj);
    }
  }
}
