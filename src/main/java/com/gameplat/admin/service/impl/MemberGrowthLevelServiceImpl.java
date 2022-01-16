package com.gameplat.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGrowthLevelConvert;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.mapper.MemberGrowthLevelMapper;
import com.gameplat.admin.model.domain.*;
import com.gameplat.admin.model.dto.GrowthLevelLogoEditDTO;
import com.gameplat.admin.model.dto.MemberGrowthLevelEditDto;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.admin.service.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lily
 * @description
 * @date 2021/11/20
 */

@Service
@RequiredArgsConstructor
public class MemberGrowthLevelServiceImpl extends ServiceImpl<MemberGrowthLevelMapper, MemberGrowthLevel> implements MemberGrowthLevelService {

    @Autowired private MemberGrowthLevelConvert levelConvert;

    @Autowired private MemberGrowthLevelMapper levelMapper;

    @Autowired private MemberGrowthConfigService growthConfigService;

    @Autowired private MemberGrowthRecordService memberGrowthRecordService;

    @Autowired private MemberService memberService;

    @Autowired private MemberInfoService memberInfoService;

    public static final String kindName = "{\"en-US\": \"platform\", \"in-ID\": \"peron\", \"th-TH\": \"แพลตฟอร์ม\", \"vi-VN\": \"nền tảng\", \"zh-CN\": \"平台\"}";

    /**
     * 查询所有等级
     */
    @Override
    public List<MemberGrowthLevelVO> findList(Integer limitLevel, String language) {

        if (StrUtil.isBlank(language)) {
            language = LanguageEnum.app_zh_CN.getCode();
        }
        List<MemberGrowthLevel> list = levelMapper.findList(limitLevel+1, language);
        List<MemberGrowthLevelVO> voList = new ArrayList<>();
        for (MemberGrowthLevel level:list) {
            MemberGrowthLevelVO memberGrowthLevelVO = levelConvert.toVo(level);
            voList.add(memberGrowthLevelVO);
        }
        return voList;
    }

    @Override
    public List<MemberGrowthLevel> getList(Integer limitLevel, String language) {
        if (StrUtil.isBlank(language)) {
            language = LanguageEnum.app_zh_CN.getCode();
        }
        return levelMapper.findList(limitLevel+1, language);
    }

    @Override
    public MemberGrowthLevel getLevel(Integer level) {
        return this.lambdaQuery()
                .eq(MemberGrowthLevel::getLevel, level)
                .one();
    }

    /**
     * 批量修改VIP等级
     */
    @Override
    public void batchUpdateLevel(List<MemberGrowthLevelEditDto> list, String language) {
        list.stream().forEach(item -> {
            String oldName = this.lambdaQuery().eq(ObjectUtils.isNotEmpty(item.getLevel()), MemberGrowthLevel::getLevel, item.getLevel()).one().getLevelName();
            JSONObject jsonObject = JSONUtil.parseObj(oldName);
            jsonObject.put(language,item.getLevelName());
            item.setLevelName(jsonObject.toString());
        });
        int count = levelMapper.batchUpdateLevel(list);
        //重新计算会员的等级
        if(count > 0) {
            //获取到所有VIP汇总数据
            List<MemberGrowthRecord> recordList = memberGrowthRecordService.findRecordGroupBy(new MemberGrowthRecord());
            MemberGrowthConfig growthConfig = growthConfigService.getOneConfig(LanguageEnum.app_zh_CN.getCode());
            for (MemberGrowthRecord userRecord : recordList) {
                //得到重新计算后的等级
                Integer newLevel = memberGrowthRecordService.dealUpLevel(userRecord.getCurrentGrowth(), growthConfig);
                //如果等级有所变化，就添加一条变动记录
                if(!newLevel.equals(userRecord.getCurrentLevel())){
                    MemberGrowthRecord record = new MemberGrowthRecord();
                    record.setUserId(userRecord.getUserId());
                    record.setUserName(userRecord.getUserName());
                    record.setKindCode("plat");
                    record.setKindName(kindName);
                    record.setType(3);
                    record.setOldLevel(userRecord.getCurrentLevel());
                    record.setCurrentLevel(newLevel);
                    record.setChangeMult(new BigDecimal("1"));
                    record.setOldGrowth(userRecord.getCurrentGrowth());

                    Integer currentGrowth = userRecord.getCurrentGrowth();
                    if (newLevel < userRecord.getCurrentLevel()){
                        currentGrowth = this.lambdaQuery().eq(MemberGrowthLevel::getLevel, newLevel - 1).one().getGrowth();
                        record.setCurrentGrowth(currentGrowth);
                        record.setChangeGrowth(currentGrowth - userRecord.getCurrentGrowth());
                    }else if ((newLevel > userRecord.getCurrentLevel())){
                        record.setCurrentGrowth(userRecord.getCurrentGrowth());
                        record.setChangeGrowth(0);
                    }
                    record.setCreateBy(GlobalContextHolder.getContext().getUsername());
                    record.setCreateTime(new Date());
                    record.setRemark("VIP等级晋级下级所需成长值变动");
                    if (! memberGrowthRecordService.save(record)){
                        throw new ServiceException("操作失败！");
                    };
                    MemberInfo member = new MemberInfo();
                    member.setMemberId(userRecord.getUserId());
                    member.setLevel(newLevel);
                    member.setGrowth(currentGrowth);
                    memberInfoService.updateById(member);
                }
            }
        }
    }

    /** 修改logo */
    @Override
    public void updateLogo(GrowthLevelLogoEditDTO dto){
        this.updateById(levelConvert.toEntity(dto));
    }
}
