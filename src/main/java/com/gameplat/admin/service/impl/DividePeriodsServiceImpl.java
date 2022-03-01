package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.TrueFalse;
import com.gameplat.admin.convert.DividePeriodsConvert;
import com.gameplat.admin.enums.BlacklistConstant;
import com.gameplat.admin.mapper.*;
import com.gameplat.admin.model.domain.BizBlacklist;
import com.gameplat.admin.model.domain.proxy.DivideDetail;
import com.gameplat.admin.model.domain.proxy.DividePeriods;
import com.gameplat.admin.model.domain.proxy.DivideSummary;
import com.gameplat.admin.model.domain.proxy.RecommendConfig;
import com.gameplat.admin.model.dto.DividePeriodsDTO;
import com.gameplat.admin.model.dto.DividePeriodsQueryDTO;
import com.gameplat.admin.model.vo.DivideGameReportVO;
import com.gameplat.admin.model.vo.DividePeriodsVO;
import com.gameplat.admin.service.DividePeriodsService;
import com.gameplat.admin.service.RecommendConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.lang.Assert;
import com.gameplat.redis.redisson.DistributedLocker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@SuppressWarnings("all")
public class DividePeriodsServiceImpl extends ServiceImpl<DividePeriodsMapper, DividePeriods> implements DividePeriodsService {
    @Autowired
    private DividePeriodsMapper dividePeriodsMapper;
    @Autowired
    private DividePeriodsConvert periodsConvert;
    @Autowired
    private RecommendConfigService recommendConfigService;
    @Autowired
    private DivideDetailMapper detailMapper;
    @Autowired
    private DivideSummaryMapper summaryMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private DistributedLocker distributedLocker;
    @Autowired
    private GameBetDailyReportMapper betDailyReportMapper;
    @Autowired
    private BizBlacklistMapper blacklistMapper;

    @Override
    public IPage<DividePeriodsVO> queryPage(PageDTO<DividePeriods> page, DividePeriodsQueryDTO dto) {
        QueryWrapper<DividePeriods> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        return dividePeriodsMapper.selectPage(page, queryWrapper).convert(periodsConvert::toVo);
    }

    /**
     * 期数新增
     * @param dto
     */
    @Override
    public void add(DividePeriodsDTO dto) {
        Assert.isTrue(StrUtil.isNotBlank(dto.getStartDate()),"期数起始时间不能为空！");
        Assert.isTrue(StrUtil.isNotBlank(dto.getEndDate()),"期数截止时间不能为空！");
        DividePeriods saveObj = periodsConvert.toEntity(dto);
        saveObj.setStartDate(DateUtil.parse(saveObj.getStartDate()).toDateStr());
        saveObj.setEndDate(DateUtil.parse(saveObj.getEndDate()).toDateStr());
        RecommendConfig recommendConfig = recommendConfigService.getRecommendConfig();
        Integer divideModel = recommendConfig.getDivideModel();
        divideModel = ObjectUtil.defaultIfNull(divideModel, 3);
        saveObj.setDivideType(divideModel);
        Assert.isTrue(this.save(saveObj),"添加失败！");
    }

    /**
     * 期数编辑
     * @param dto
     */
    @Override
    public void edit(DividePeriodsDTO dto) {
        Assert.isTrue(StrUtil.isNotBlank(dto.getStartDate()),"期数起始时间不能为空！");
        Assert.isTrue(StrUtil.isNotBlank(dto.getEndDate()),"期数截止时间不能为空！");
        DividePeriods editObj = periodsConvert.toEntity(dto);
        editObj.setStartDate(DateUtil.parse(dto.getStartDate()).toDateStr());
        editObj.setEndDate(DateUtil.parse(dto.getEndDate()).toDateStr());
        Assert.isTrue(this.updateById(editObj),"编辑失败！");
    }

    /**
     * 期数删除
     * @param ids
     */
    @Override
    @Transactional
    public void delete(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids),"参数为空！");
        String[] idArr = ids.split(",");
        for (String id: idArr) {
            DividePeriods dividePeriods = dividePeriodsMapper.selectById(id);
            if (dividePeriods.getGrantStatus() == 2) {
                throw new ServiceException("已派发不能删除！");
            }
            QueryWrapper<DivideDetail> deleteDetailWrapper = new QueryWrapper();
            deleteDetailWrapper.eq("periods_id", Long.valueOf(id));
            detailMapper.delete(deleteDetailWrapper);

            QueryWrapper<DivideSummary> deleteSummaryWrapper = new QueryWrapper();
            deleteSummaryWrapper.eq("periods_id", Long.valueOf(id));
            summaryMapper.delete(deleteSummaryWrapper);

            Assert.isTrue(this.removeById(id),("删除期数:" + id + "失败！"));
        }
    }

    /**
     * 期数结算
     * @param dto
     */
    @Override
    public void settle(DividePeriodsDTO dto) {
        Assert.isTrue(dto.getId() != null,"期数主键ID参数缺失！");
        // 外层先锁住  防止重复点击
        String key = "divide:settle:" + dto.getId();
        Boolean isLock = redisTemplate.hasKey(key);
        Assert.isTrue(!isLock,"您的操作过于频繁！请稍后重试！");
        distributedLocker.lock(key);
        try {
            // 结算 分红汇总，分红详情
            this.settleOther(dto.getId());

            Assert.isTrue(this.lambdaUpdate()
                    .set(DividePeriods::getSettleStatus,2)
                    .eq(DividePeriods::getId, dto.getId())
                    .update(new DividePeriods()),"修改期数结算状态失败！");
        } catch (Exception e) {
            throw new ServiceException("期数结算失败！");
        } finally {
            distributedLocker.unlock(key);
        }
    }

    @Async
    public void settleOther(Long periodsId) {
        DividePeriods periods = this.getById(periodsId);
        if (BeanUtil.isEmpty(periods) || periods.getGrantStatus() == 2) {
            return;
        }
        // 删除分红详情
        QueryWrapper<DivideDetail> deleteDetailWrapper = new QueryWrapper();
        deleteDetailWrapper.eq("periods_id", periodsId);
        detailMapper.delete(deleteDetailWrapper);
        // 删除分红汇总
        QueryWrapper<DivideSummary> deleteSummaryWrapper = new QueryWrapper();
        deleteSummaryWrapper.eq("periods_id", periodsId);
        summaryMapper.delete(deleteSummaryWrapper);

        // 分红模式 1 固定  2 裂变  3 层层代 4 平级
        Integer divideModel = periods.getDivideType();
        RecommendConfig recommendConfig = recommendConfigService.getRecommendConfig();
        Integer isIncludeAgent = recommendConfig.getIsIncludeAgent();
        List<DivideGameReportVO> reportForDivide
                = betDailyReportMapper.findReportForDivide(periods.getStartDate(), periods.getEndDate());
        reportForDivide = reportForDivide.stream().filter(
                item -> StrUtil.isNotBlank(item.getUserPaths())
        ).collect(Collectors.toList());
        // 获取业务 分红黑名单 集合
        QueryWrapper<BizBlacklist> queryBizWrapper = new QueryWrapper();
        queryBizWrapper.like("types", BlacklistConstant.BizBlacklistType.DL_RATIO.getValue());
        List<BizBlacklist> bizBlacklists = blacklistMapper.selectList(queryBizWrapper);
        // 会员账号业务黑名单
        List<BizBlacklist> accountBlacks = bizBlacklists.stream().filter(item -> item.getTargetType() == TrueFalse.FALSE.getValue()).collect(Collectors.toList());
        // 用户层级业务黑名单
        List<BizBlacklist> userLevelBlacks = bizBlacklists.stream().filter(item -> item.getTargetType() == TrueFalse.TRUE.getValue()).collect(Collectors.toList());
    }
}
