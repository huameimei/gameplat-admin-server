package com.gameplat.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.DivideSummaryConvert;
import com.gameplat.admin.mapper.DivideSummaryMapper;
import com.gameplat.admin.model.domain.proxy.DivideSummary;
import com.gameplat.admin.model.dto.DivideSummaryQueryDTO;
import com.gameplat.admin.model.vo.DivideSummaryVO;
import com.gameplat.admin.service.DivideSummaryService;
import com.gameplat.base.common.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
public class DivideSummaryServiceImpl extends ServiceImpl<DivideSummaryMapper, DivideSummary> implements DivideSummaryService {
    @Autowired
    private DivideSummaryMapper summaryMapper;
    @Autowired
    private DivideSummaryConvert summaryConvert;

    @Override
    public IPage<DivideSummaryVO> queryPage(PageDTO<DivideSummary> page, DivideSummaryQueryDTO dto) {
        QueryWrapper<DivideSummary> queryWrapper = new QueryWrapper();
        queryWrapper
                .eq(ObjectUtils.isNotNull(dto.getId()), "id",dto.getId())
                .eq(ObjectUtils.isNotNull(dto.getPeriodsId()), "periods_id",dto.getPeriodsId())
                .eq(ObjectUtils.isNotNull(dto.getUserId()), "user_id",dto.getUserId())
                .eq(ObjectUtils.isNotNull(dto.getAgentLevel()), "agent_level",dto.getAgentLevel())
                .eq(ObjectUtils.isNotNull(dto.getParentId()), "parent_id",dto.getParentId())
                .eq(ObjectUtils.isNotNull(dto.getStatus()), "status",dto.getStatus())
                .eq(StrUtil.isNotBlank(dto.getAccount()), "account",dto.getAccount())
                .eq(StrUtil.isNotBlank(dto.getParentName()), "parent_name",dto.getParentName());
//                .ge(StrUtil.isNotBlank(dto.getStartTime()),"create_time",DateUtil.beginOfDay(DateUtils.parseDate(dto.getStartTime(), DateUtils.DATE_PATTERN)))
//                .le(StrUtil.isNotBlank(dto.getEndTime()),"create_time",DateUtil.endOfDay(DateUtils.parseDate(dto.getEndTime(), DateUtils.DATE_PATTERN)))
        if (StringUtils.isNotBlank(dto.getStartTime())) {
            queryWrapper.apply("create_time >= STR_TO_DATE({0}, '%Y-%m-%d %H:%i:%s')",
                    DateUtil.beginOfDay(DateUtils.parseDate(dto.getStartTime(), DateUtils.DATE_PATTERN)));
        }
        if (StringUtils.isNotBlank(dto.getEndTime())) {
            queryWrapper.apply("create_time <= STR_TO_DATE({0}, '%Y-%m-%d %H:%i:%s')",
                    DateUtil.endOfDay(DateUtils.parseDate(dto.getEndTime(),DateUtils.DATE_PATTERN)));
        }
        queryWrapper.orderByDesc("create_time");
        return summaryMapper.selectPage(page, queryWrapper).convert(summaryConvert::toVo);
    }

    /**
     * 获取最大层级值
     * @param dto
     * @return
     */
    @Override
    public Integer getMaxLevel(DivideSummaryQueryDTO dto) {
        Integer maxLevel = summaryMapper.getMaxLevel(dto.getPeriodsId());
        return maxLevel;
    }
}
