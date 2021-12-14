package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityRedPacketConvert;
import com.gameplat.admin.mapper.ActivityRedPacketMapper;
import com.gameplat.admin.model.domain.ActivityRedPacketCondition;
import com.gameplat.admin.model.vo.MemberActivityPrizeVO;
import com.gameplat.admin.model.domain.ActivityPrize;
import com.gameplat.admin.model.domain.ActivityRedPacket;
import com.gameplat.admin.model.dto.ActivityRedPacketAddDTO;
import com.gameplat.admin.model.dto.ActivityRedPacketDiscountDTO;
import com.gameplat.admin.model.dto.ActivityRedPacketQueryDTO;
import com.gameplat.admin.model.dto.ActivityRedPacketUpdateDTO;
import com.gameplat.admin.model.vo.ActivityRedPacketVO;
import com.gameplat.admin.service.ActivityPrizeService;
import com.gameplat.admin.service.ActivityRedPacketConditionService;
import com.gameplat.admin.service.ActivityRedPacketService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动红包雨业务处理
 *
 * @author admin
 */
@Service
public class ActivityRedPacketServiceImpl extends ServiceImpl<ActivityRedPacketMapper, ActivityRedPacket>
        implements ActivityRedPacketService {

    @Autowired
    private ActivityRedPacketConvert activityRedPacketConvert;

    @Autowired
    private ActivityPrizeService activityPrizeService;

    @Autowired
    private ActivityRedPacketConditionService activityRedPacketConditionService;


    @Override
    public IPage<ActivityRedPacketVO> redPacketList(PageDTO<ActivityRedPacket> page, ActivityRedPacketQueryDTO activityRedPacketQueryDTO) {
        return this.lambdaQuery()
                .eq(activityRedPacketQueryDTO.getPacketType() != null
                        , ActivityRedPacket::getPacketType, activityRedPacketQueryDTO.getPacketType())

                .page(page).convert(activityRedPacketConvert::toVo);
    }

    @Override
    public void add(ActivityRedPacketAddDTO activityRedPacketAddDTO) {
        ActivityRedPacket activityRedPacket = activityRedPacketConvert.toEntity(activityRedPacketAddDTO);
        this.save(activityRedPacket);
    }

    @Override
    public void edit(ActivityRedPacketUpdateDTO activityRedPacketUpdateDTO) {
        ActivityRedPacket activityRedPacket = activityRedPacketConvert.toEntity(activityRedPacketUpdateDTO);
        this.updateById(activityRedPacket);
    }

    @Override
    public void updateStatus(Long packetId) {
        if (packetId == null || packetId == 0) {
            throw new ServiceException("packetId不能为空");
        }
        ActivityRedPacket activityRedPacket = this.getById(packetId);
        if (activityRedPacket == null) {
            throw new ServiceException("该红包雨配置不存在");
        }
        if (activityRedPacket.getStatus() == 0) {
            throw new ServiceException("该数据已下线");
        }
        if (activityRedPacket.getStatus() == 2) {
            throw new ServiceException("该数据未上线");
        }

        LambdaUpdateChainWrapper<ActivityRedPacket> updateChainWrapper
                = lambdaUpdate().set(ActivityRedPacket::getStatus, 0)
                .eq(ActivityRedPacket::getPacketId, packetId);
        if (!this.update(updateChainWrapper)) {
            throw new ServiceException("更新红包雨状态失败");
        }
    }

    @Override
    public void delete(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException("ids不能为空");
        }
        String[] idArr = ids.split(",");
        List<Long> idList = new ArrayList<>();
        for (String idStr : idArr) {
            idList.add(Long.parseLong(idStr));
        }
        this.removeByIds(idList);
    }

    @Override
    public Object discountList(ActivityRedPacketDiscountDTO activityRedPacketDiscountDTO) {
        if (activityRedPacketDiscountDTO.getType() == 1) {
            MemberActivityPrizeVO memberActivityPrizeBean = new MemberActivityPrizeVO();
            memberActivityPrizeBean.setActivityId(activityRedPacketDiscountDTO.getId());
            memberActivityPrizeBean.setType(1);
            List<MemberActivityPrizeVO> prizeBeanList =
                    activityPrizeService.findActivityPrizeList(memberActivityPrizeBean);
            return prizeBeanList;
        } else if (activityRedPacketDiscountDTO.getType() == 2) {
            return activityRedPacketConditionService.lambdaQuery()
                    .eq(ActivityRedPacketCondition::getRedPacketId, activityRedPacketDiscountDTO.getId())
                    .list();
        }
        return new ArrayList<>();
    }


}
