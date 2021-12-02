package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityInfoConvert;
import com.gameplat.admin.mapper.ActivityInfoMapper;
import com.gameplat.admin.model.domain.ActivityInfo;
import com.gameplat.admin.model.domain.ActivityType;
import com.gameplat.admin.model.domain.SysBannerInfo;
import com.gameplat.admin.model.dto.ActivityInfoAddDTO;
import com.gameplat.admin.model.dto.ActivityInfoDTO;
import com.gameplat.admin.model.vo.ActivityInfoVO;
import com.gameplat.admin.service.ActivityInfoService;
import com.gameplat.admin.service.ActivityTypeService;
import com.gameplat.admin.service.SysBannerInfoService;
import com.gameplat.common.exception.ServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
@Service
public class ActivityInfoServiceImpl
        extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements ActivityInfoService {

    @Autowired
    private ActivityInfoConvert activityInfoConvert;

    @Autowired
    private SysBannerInfoService sysBannerInfoService;

    @Autowired
    private ActivityTypeService activityTypeService;

    /**
     * 列表查询
     *
     * @param page
     * @param activityInfoDTO
     * @return
     */
    @Override
    public IPage<ActivityInfoVO> list(PageDTO<ActivityInfo> page, ActivityInfoDTO activityInfoDTO) {
        LambdaQueryChainWrapper<ActivityInfo> queryWrapper = this.lambdaQuery();
        queryWrapper.eq(activityInfoDTO.getType() == null && activityInfoDTO.getType() == 0
                , ActivityInfo::getType, activityInfoDTO.getType())
                .eq(activityInfoDTO.getApplyType() == null && activityInfoDTO.getApplyType() == 0
                        , ActivityInfo::getApplyType, activityInfoDTO.getApplyType())
                .eq(activityInfoDTO.getValidStatus() == null && activityInfoDTO.getValidStatus() == 0
                        , ActivityInfo::getValidStatus, activityInfoDTO.getValidStatus())
                .eq(activityInfoDTO.getStatus() == null && activityInfoDTO.getStatus() == 0
                        , ActivityInfo::getStatus, activityInfoDTO.getStatus())
                .eq(activityInfoDTO.getActivityLobbyId() == null && activityInfoDTO.getActivityLobbyId() == 0
                        , ActivityInfo::getActivityLobbyId, activityInfoDTO.getActivityLobbyId());

        return queryWrapper.page(page).convert(activityInfoConvert::toVo);
    }

    @Override
    public ActivityInfoVO detail(Long id) {
        ActivityInfo activityInfo = this.getById(id);
        ActivityInfoVO activityInfoVO = activityInfoConvert.toVo(activityInfo);
        return activityInfoVO;
    }

    @Override
    public void add(ActivityInfoAddDTO activityInfoAddDTO, String country) {
        ActivityInfo activityInfo = activityInfoConvert.toEntity(activityInfoAddDTO);
        if (this.saveActivityInfo(activityInfo)) {
            if (null != activityInfo.getId() && activityInfo.getId() > 0) {
                //保存活动显示的图片
                SysBannerInfo banner = new SysBannerInfo();
                banner.setChildType(activityInfo.getId());
                banner.setLanguage(country);
                banner.setStatus(1);
                List<SysBannerInfo> bannerList = sysBannerInfoService.getByBanner(banner);
                bannerList.forEach(sysBannerInfo -> {
                    if (sysBannerInfo.getStatus() != 0 && !activityInfo.getStatus().equals(sysBannerInfo.getStatus())) {
                        sysBannerInfo.setStatus(0);
                        sysBannerInfoService.save(sysBannerInfo);
                    }
                });
            }
        } else {
            throw new ServiceException("未选择对象或对象无效");
        }
    }

    @Override
    public void checkActivityLobbyId(Long activityLobbyId, Integer id) {
        LambdaQueryChainWrapper<ActivityInfo> queryWrapper = this.lambdaQuery();
        queryWrapper.eq(ActivityInfo::getActivityLobbyId, activityLobbyId)
                .eq(ActivityInfo::getId, id);
        int count = queryWrapper.count();
        if (count > 0) {
            throw new ServiceException("您绑定的活动大厅已经绑定其他活动,请选择其他活动大厅绑定此活动发布");
        }
    }

    @Override
    public boolean saveActivityInfo(ActivityInfo activityInfo) {
        if (null != activityInfo.getId() && activityInfo.getId() > 0) {
            return this.updateById(activityInfo);
        }
        return this.save(activityInfo);
    }

    @Override
    public List<ActivityInfoVO> getAllSysActivityWithRule() {
        ActivityInfo activityInfo = new ActivityInfo();
        LambdaQueryChainWrapper<ActivityInfo> queryWrapper = this.lambdaQuery();
        queryWrapper.eq(activityInfo.getStatus() != null && activityInfo.getStatus() != 0, ActivityInfo::getStatus, 1)
                .ne(ActivityInfo::getActivityRuleId, null)
                .gt(ActivityInfo::getActivityRuleId, 0);
        List<ActivityInfo> activityInfoList = queryWrapper.list();
        //查询关联数据
        List<Long> activityTypeIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(activityInfoList)) {
            for (ActivityInfo activityInfo1 : activityInfoList) {
                activityTypeIdList.add(activityInfo1.getType());
            }
        }

        List<ActivityType> activityTypeList = activityTypeService.findByTypeIdList(activityTypeIdList);
        Map<Long, ActivityType> activityTypeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(activityTypeList)) {
            for (ActivityType activityType : activityTypeList) {
                activityTypeMap.put(activityType.getId(), activityType);
            }
        }

        List<ActivityInfoVO> activityInfoVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(activityInfoList)) {
            for (ActivityInfo activityInfo1 : activityInfoList) {
                ActivityInfoVO activityInfoVO = activityInfoConvert.toVo(activityInfo1);
                ActivityType activityType = activityTypeMap.get(activityInfo1.getType());
                if (activityType != null) {
                    activityInfoVO.setTypeCode(activityType.getTypeCode());
                    activityInfoVO.setTypeName(activityType.getTypeName());
                }
                activityInfoVOList.add(activityInfoVO);
            }
        }
        return activityInfoVOList;
    }

    @Override
    public List<ActivityInfo> list(ActivityInfo activityInfo) {
        LambdaQueryChainWrapper<ActivityInfo> queryWrapper = this.lambdaQuery();
        queryWrapper.eq(activityInfo.getStatus() != null && activityInfo.getStatus() != 0
                , ActivityInfo::getStatus, activityInfo.getStatus())
//                .eq()
        ;
        return queryWrapper.list();
    }

    @Override
    public List<ActivityInfo> findByTypeId(Long typeId) {
        return this.lambdaQuery().eq(ActivityInfo::getType, typeId).list();
    }


}
