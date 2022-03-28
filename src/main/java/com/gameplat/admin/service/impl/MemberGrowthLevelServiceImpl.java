package com.gameplat.admin.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGrowthLevelConvert;
import com.gameplat.admin.mapper.MemberGrowthLevelMapper;
import com.gameplat.admin.model.dto.GrowthLevelLogoEditDTO;
import com.gameplat.admin.model.dto.MemberGrowthLevelEditDto;
import com.gameplat.admin.model.vo.MemberConfigLevelVO;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.member.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author lily
 * @description
 * @date 2021/11/20
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberGrowthLevelServiceImpl
    extends ServiceImpl<MemberGrowthLevelMapper, MemberGrowthLevel>
    implements MemberGrowthLevelService {

  public static final String KIND_NAME =
      "{\"en-US\": \"platform\", \"in-ID\": \"peron\", \"th-TH\": \"แพลตฟอร์ม\", \"vi-VN\": \"nền tảng\", \"zh-CN\": \"平台\"}";
  @Autowired private MemberGrowthLevelConvert levelConvert;
  @Autowired private MemberGrowthLevelMapper levelMapper;
  @Autowired private MemberGrowthConfigService growthConfigService;
  @Autowired private MemberGrowthRecordService memberGrowthRecordService;
  @Autowired private MemberInfoService memberInfoService;
  @Autowired private MemberGrowthConfigService memberGrowthConfigService;
  @Autowired private MemberLoanService memberLoanService;
  @Autowired private MemberService memberService;
  @Autowired private MemberGrowthStatisService memberGrowthStatisService;

  /** 查询所有等级 */
  @Override
  public List<MemberGrowthLevelVO> findList(Integer limitLevel) {
    List<MemberGrowthLevel> list =
        levelMapper.findList(limitLevel + 1, LocaleContextHolder.getLocale().toLanguageTag());
    List<MemberGrowthLevelVO> voList = new ArrayList<>();
    for (MemberGrowthLevel level : list) {
      MemberGrowthLevelVO memberGrowthLevelVO = levelConvert.toVo(level);
      voList.add(memberGrowthLevelVO);
    }
    return voList;
  }

  @Override
  public List<MemberGrowthLevel> getList(Integer limitLevel) {
    return levelMapper.findList(limitLevel + 1, LocaleContextHolder.getLocale().toLanguageTag());
  }

  @Override
  public MemberGrowthLevel getLevel(Integer level) {
    return this.lambdaQuery().eq(MemberGrowthLevel::getLevel, level).one();
  }

  /** 批量修改VIP等级 */
  @Override
  public void batchUpdateLevel(List<MemberGrowthLevelEditDto> list, HttpServletRequest request) {
    String language = LocaleContextHolder.getLocale().toLanguageTag();
    list.forEach(
        item -> {
          String oldName =
              this.lambdaQuery()
                  .eq(
                      ObjectUtils.isNotEmpty(item.getLevel()),
                      MemberGrowthLevel::getLevel,
                      item.getLevel())
                  .one()
                  .getLevelName();
          JSONObject jsonObject = JSONUtil.parseObj(oldName);
          jsonObject.put(language, item.getLevelName());
          item.setLevelName(jsonObject.toString());
        });
    int count = levelMapper.batchUpdateLevel(list);
    // 重新计算会员的等级
    if (count > 0) {
      // 获取到所有VIP汇总数据
      List<MemberGrowthRecord> recordList =
          memberGrowthRecordService.findRecordGroupBy(new MemberGrowthRecord());
      MemberGrowthConfig growthConfig = growthConfigService.getOneConfig();
      for (MemberGrowthRecord userRecord : recordList) {
        // 得到重新计算后的等级
        Integer newLevel =
                memberGrowthStatisService.dealUpLevel(userRecord.getCurrentGrowth(), growthConfig);
        // 更新借呗表额度
        BigDecimal loanMoney =
            this.lambdaQuery().eq(MemberGrowthLevel::getLevel, newLevel).one().getLoanMoney();
        int money = loanMoney.compareTo(BigDecimal.ZERO);
        if (money > 0 && list.get(0).getLevel() != 0) {
          memberLoanService.editOrUpdate(
              new MemberLoan() {
                {
                  setLoanMoney(loanMoney);
                  setMemberId(userRecord.getUserId());
                  setAccount(userRecord.getUserName());
                }
              });
        }
        // 如果等级有所变化，就添加一条变动记录
        if (!newLevel.equals(userRecord.getCurrentLevel())) {

          MemberGrowthStatis memberGrowthStatis =
                  memberGrowthStatisService.findOrInsert(userRecord.getUserId(), userRecord.getUserName());

          LambdaUpdateWrapper<MemberInfo> wrapper = new LambdaUpdateWrapper<>();

          MemberGrowthRecord record = new MemberGrowthRecord();
          record.setUserId(userRecord.getUserId());
          record.setUserName(userRecord.getUserName());
          record.setKindCode("plat");
          record.setKindName(KIND_NAME);
          record.setType(3);
          record.setOldLevel(userRecord.getCurrentLevel());
          record.setCurrentLevel(newLevel);
          record.setChangeMult(new BigDecimal("1"));
          record.setOldGrowth(userRecord.getCurrentGrowth());

          Long currentGrowth = userRecord.getCurrentGrowth();
          if (newLevel < userRecord.getCurrentLevel()) {
            if(newLevel == 0){
              currentGrowth = 0L;
            }else {
              currentGrowth =
                  this.lambdaQuery()
                      .eq(MemberGrowthLevel::getLevel, newLevel - 1)
                      .one()
                      .getGrowth();
            }

            Long changeGrowth = currentGrowth - userRecord.getCurrentGrowth();

            record.setCurrentGrowth(currentGrowth);
            record.setChangeGrowth(changeGrowth);

            memberGrowthStatis.setBackGrowth(memberGrowthStatis.getBackGrowth() + changeGrowth);

          }

          else if ((newLevel > userRecord.getCurrentLevel())) {
            // 修改余额
            BigDecimal upReward = BigDecimal.ZERO;
            // 下级
            Integer lastLevel = userRecord.getCurrentLevel();
            for (int i = 0; i < newLevel - userRecord.getCurrentLevel(); i++) {
              lastLevel = lastLevel + 1;
              upReward =
                  upReward.add(
                      this.lambdaQuery()
                          .eq(MemberGrowthLevel::getLevel, lastLevel)
                          .one()
                          .getUpReward());
            }
            record.setCurrentGrowth(userRecord.getCurrentGrowth());
            record.setChangeGrowth(0L);

            wrapper.set(
                MemberInfo::getBalance,
                memberInfoService
                    .lambdaQuery()
                    .eq(MemberInfo::getMemberId, userRecord.getUserId())
                    .one()
                    .getBalance()
                    .add(upReward));

            Member member = memberService.getById(userRecord.getUserId());
            // 处理升级
            memberGrowthStatisService.dealPayUpReword(
                userRecord.getCurrentLevel(), newLevel, growthConfig, member);
          }
          record.setCreateBy(GlobalContextHolder.getContext().getUsername());
          record.setCreateTime(new Date());
          record.setRemark("VIP等级变动所需成长值变动");
          Assert.isTrue(memberGrowthRecordService.save(record), "操作失败!");

          memberGrowthStatis.setVipGrowth(currentGrowth);
          memberGrowthStatis.setVipLevel(newLevel);
          memberGrowthStatisService.insertOrUpdate(memberGrowthStatis);

          wrapper
              .set(MemberInfo::getVipLevel, newLevel)
              .set(MemberInfo::getVipGrowth, currentGrowth)
              .eq(MemberInfo::getMemberId, userRecord.getUserId());
          memberInfoService.update(wrapper);


        }
      }
    }
  }

  /** 修改logo */
  @Override
  public void updateLogo(GrowthLevelLogoEditDTO dto) {
    this.updateById(levelConvert.toEntity(dto));
  }

  @Override
  public MemberConfigLevelVO getLevelConfig() {
    MemberGrowthConfigVO config = memberGrowthConfigService.findOneConfig();
    int limitLevel = Optional.ofNullable(config.getLimitLevel()).orElse(50);
    List<MemberGrowthLevelVO> levels = this.findList(limitLevel);
    return new MemberConfigLevelVO() {
      {
        setConfigVO(config);
        setLevelVO(levels);
      }
    };
  }

  @Override
  public void batchUpdateLevel(JSONObject obj, HttpServletRequest request) {
    Object levels = obj.get("levels");
    JSONArray jsonArray = JSONUtil.parseArray(levels);
    List<MemberGrowthLevelEditDto> list = jsonArray.toList(MemberGrowthLevelEditDto.class);
    this.batchUpdateLevel(list, request);
  }

  @Override
  public List<MemberGrowthLevelVO> vipList() {
    return this.findList(memberGrowthConfigService.getLimitLevel());
  }
}
