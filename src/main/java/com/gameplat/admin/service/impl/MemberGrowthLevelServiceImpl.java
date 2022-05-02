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
import com.gameplat.admin.model.dto.MemberGrowthStatisDTO;
import com.gameplat.admin.model.vo.LogoConfigVO;
import com.gameplat.admin.model.vo.MemberConfigLevelVO;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.admin.service.*;
import com.gameplat.model.entity.member.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

  @Resource(name = "memberGrowthRecordServiceImpl")
  @Lazy
  private MemberGrowthRecordService memberGrowthRecordService;

  @Autowired private MemberInfoService memberInfoService;
  @Autowired private MemberGrowthConfigService memberGrowthConfigService;
  @Autowired private MemberLoanService memberLoanService;
  @Autowired private MemberService memberService;

  @Resource(name = "memberGrowthStatisServiceImpl")
  @Lazy
  private MemberGrowthStatisService memberGrowthStatisService;

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

  /** 批量修改VIP等级 此情况升级不派发升级奖励 不增加打码量 不发消息 */
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
      List<MemberGrowthStatis> memberGrowthStatisList =
          memberGrowthStatisService.findList(new MemberGrowthStatisDTO());

      MemberGrowthConfig growthConfig = growthConfigService.getOneConfig();

      for (MemberGrowthStatis memberGrowthStatis : memberGrowthStatisList) {
        // 旧等级
        Integer oldLevel = memberGrowthStatis.getVipLevel();
        // 旧成长值
        Long growth = memberGrowthStatis.getVipGrowth();

        // 得到重新计算后的等级
        Integer newLevel =
            memberGrowthStatisService.dealUpLevel(memberGrowthStatis.getVipGrowth(), growthConfig);

        // 如果等级有所变化，就添加一条变动记录
        if (!newLevel.equals(oldLevel)) {

          memberGrowthStatis.setVipLevel(newLevel);
          // 修改等级汇总
          memberGrowthStatisService.insertOrUpdate(memberGrowthStatis);

          // 添加成长记录
          MemberGrowthRecord memberGrowthRecord = new MemberGrowthRecord();
          memberGrowthRecord.setUserId(memberGrowthStatis.getMemberId());
          memberGrowthRecord.setUserName(memberGrowthStatis.getAccount());
          memberGrowthRecord.setKindCode("plat");
          memberGrowthRecord.setKindName(KIND_NAME);
          memberGrowthRecord.setType(3);
          memberGrowthRecord.setOldLevel(oldLevel);
          memberGrowthRecord.setCurrentLevel(newLevel);
          memberGrowthRecord.setChangeMult(new BigDecimal("1"));
          memberGrowthRecord.setOldGrowth(growth);
          memberGrowthRecord.setCurrentGrowth(growth);
          memberGrowthRecord.setChangeGrowth(0L);
          memberGrowthRecord.setCreateBy("system");
          memberGrowthRecord.setRemark("后台修改成长值等级配置，会员等级发生变化");
          memberGrowthRecordService.save(memberGrowthRecord);

          // 更新会员表
          LambdaUpdateWrapper<MemberInfo> wrapper = new LambdaUpdateWrapper<>();
          wrapper
              .set(MemberInfo::getVipLevel, newLevel)
              .eq(MemberInfo::getMemberId, memberGrowthStatis.getMemberId());
          memberInfoService.update(wrapper);

          // 更新借呗表额度
          BigDecimal loanMoney =
              this.lambdaQuery().eq(MemberGrowthLevel::getLevel, newLevel).one().getLoanMoney();

          int money = loanMoney.compareTo(BigDecimal.ZERO);

          if (money > 0 && list.get(0).getLevel() != 0) {
            memberLoanService.editOrUpdate(
                new MemberLoan() {
                  {
                    setLoanMoney(loanMoney);
                    setMemberId(memberGrowthStatis.getMemberId());
                    setAccount(memberGrowthStatis.getAccount());
                  }
                });
          }
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

  @Override
  public List<LogoConfigVO> getLogoConfig() {
    return this.lambdaQuery()
        .select(
            MemberGrowthLevel::getId,
            MemberGrowthLevel::getLevel,
            MemberGrowthLevel::getMobileVipImage,
            MemberGrowthLevel::getWebVipImage,
            MemberGrowthLevel::getMobileReachBackImage,
            MemberGrowthLevel::getMobileUnreachBackImage,
            MemberGrowthLevel::getMobileReachVipImage,
            MemberGrowthLevel::getMobileUnreachVipImage,
            MemberGrowthLevel::getWebReachVipImage,
            MemberGrowthLevel::getWebUnreachVipImage,
            MemberGrowthLevel::getUpdateBy,
            MemberGrowthLevel::getUpdateTime)
        .list()
        .stream()
        .map(e -> levelConvert.toLogoVo(e))
        .collect(Collectors.toList());
  }
}
