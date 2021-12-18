package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGrowthRecordConvert;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.mapper.MemberGrowthRecordMapper;
import com.gameplat.admin.model.bean.ActivityMemberInfo;
import com.gameplat.admin.model.domain.MemberGrowthRecord;
import com.gameplat.admin.model.domain.MemberGrowthStatis;
import com.gameplat.admin.model.dto.MemberGrowthRecordDTO;
import com.gameplat.admin.model.dto.MemberGrowthStatisDTO;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.admin.model.vo.MemberGrowthRecordVO;
import com.gameplat.admin.service.MemberGrowthLevelService;
import com.gameplat.admin.service.MemberGrowthRecordService;
import java.util.List;
import java.util.Map;

import com.gameplat.base.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lily
 * @description vip成长记录业务处理层
 * @date 2021/11/24
 */

@Service
@RequiredArgsConstructor
public class MemberGrowthRecordServiceImpl extends ServiceImpl<MemberGrowthRecordMapper, MemberGrowthRecord> implements MemberGrowthRecordService {

    @Autowired private MemberGrowthRecordConvert recordConvert;
    @Autowired private MemberGrowthRecordMapper memberGrowthRecordMapper;
    @Autowired private MemberGrowthLevelService growthLevelService;

    @Override
    public IPage<MemberGrowthRecordVO> findRecordList(PageDTO<MemberGrowthRecord> page, MemberGrowthRecordDTO dto) {

        IPage<MemberGrowthRecordVO> result = this.lambdaQuery()
                .like(ObjectUtils.isNotEmpty(dto.getUserName()), MemberGrowthRecord::getUserName, dto.getUserName())
                .eq(ObjectUtils.isNotEmpty(dto.getType()), MemberGrowthRecord::getType, dto.getType())
                .ge(ObjectUtils.isNotEmpty(dto.getStartTime()), MemberGrowthRecord::getCreateTime, dto.getStartTime())
                .le(ObjectUtils.isNotEmpty(dto.getEndTime()), MemberGrowthRecord::getCreateTime, dto.getEndTime())
                .orderByDesc(MemberGrowthRecord::getCreateTime)
                .page(page)
                .convert(recordConvert::toVo);

        List<MemberGrowthRecordVO> list = result.getRecords();
        for (MemberGrowthRecordVO vo : list) {
            JSONObject jsonKindName = JSONObject.parseObject(vo.getKindName());
            vo.setKindName(jsonKindName.getString(dto.getLanguage()));
        }

        return result;
    }

    @Override
    public Integer dealUpLevel(Integer afterGrowth, MemberGrowthConfigVO memberGrowthConfig) {
        //todo 1.先获取所有成长值等级
        Integer limitLevel = memberGrowthConfig.getLimitLevel();
        if (limitLevel == null){
            limitLevel = 50;
        }
        List<MemberGrowthLevelVO> levels = growthLevelService.findList(limitLevel + 1, LanguageEnum.app_zh_CN.getCode());
        MemberGrowthLevelVO maxGrowthLevel = levels.get(levels.size() - 1);
        //如果比最大等级所需升级成长值还要大  则直接返回最大等级
        if (afterGrowth >= maxGrowthLevel.getGrowth()) {
            return maxGrowthLevel.getLevel();
        }
        for (int i = 0; i < levels.size(); i++) {
            if (afterGrowth < levels.get(i).getGrowth()) {
                return levels.get(i).getLevel();
            }
        }
        throw new ServiceException("计算成长等级失败！");
    }

    @Override
    public List<ActivityMemberInfo> findActivityMemberInfo(Map map) {
        return null;
    }

    @Override
    public List<MemberGrowthRecord> findRecordGroupBy(MemberGrowthRecord entity) {
        return memberGrowthRecordMapper.findRecordGroupBy(entity);
    }
}
