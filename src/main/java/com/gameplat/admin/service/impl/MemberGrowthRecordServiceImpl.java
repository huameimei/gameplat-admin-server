package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGrowthRecordConvert;
import com.gameplat.admin.mapper.MemberGrowthRecordMapper;
import com.gameplat.admin.model.domain.MemberGrowthRecord;
import com.gameplat.admin.model.dto.MemberGrowthRecordDTO;
import com.gameplat.admin.model.vo.MemberGrowthRecordVO;
import com.gameplat.admin.service.MemberGrowthRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lily
 * @description vip成长记录业务处理层
 * @date 2021/11/24
 */

@Service
@RequiredArgsConstructor
public class MemberGrowthRecordServiceImpl extends ServiceImpl<MemberGrowthRecordMapper, MemberGrowthRecord> implements MemberGrowthRecordService {

    @Autowired
    private MemberGrowthRecordConvert recordConvert;

    @Autowired private MemberGrowthRecordMapper recordMapper;

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
}
