package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityInfoConvert;
import com.gameplat.admin.enums.SysBannerInfoEnum;
import com.gameplat.admin.mapper.ActivityInfoMapper;
import com.gameplat.admin.model.domain.ActivityInfo;
import com.gameplat.admin.model.domain.ActivityLobby;
import com.gameplat.admin.model.domain.ActivityType;
import com.gameplat.admin.model.domain.SysBannerInfo;
import com.gameplat.admin.model.dto.ActivityInfoAddDTO;
import com.gameplat.admin.model.dto.ActivityInfoQueryDTO;
import com.gameplat.admin.model.dto.ActivityInfoUpdateDTO;
import com.gameplat.admin.model.dto.ActivityInfoUpdateSortDTO;
import com.gameplat.admin.model.vo.ActivityInfoVO;
import com.gameplat.admin.service.ActivityInfoService;
import com.gameplat.admin.service.ActivityLobbyService;
import com.gameplat.admin.service.ActivityTypeService;
import com.gameplat.admin.service.SysBannerInfoService;
import com.gameplat.base.common.exception.ServiceException;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kenvin
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

    @Autowired
    private ActivityLobbyService activityLobbyService;

    /**
     * 列表查询
     *
     * @param page
     * @param activityInfoQueryDTO
     * @return
     */
    @Override
    public IPage<ActivityInfoVO> list(PageDTO<ActivityInfo> page, ActivityInfoQueryDTO activityInfoQueryDTO) {
        LambdaQueryChainWrapper<ActivityInfo> queryWrapper = this.lambdaQuery();
        queryWrapper.eq(activityInfoQueryDTO.getType() != null && activityInfoQueryDTO.getType() != 0
                , ActivityInfo::getType, activityInfoQueryDTO.getType())
                .eq(activityInfoQueryDTO.getApplyType() != null && activityInfoQueryDTO.getApplyType() != 0
                        , ActivityInfo::getApplyType, activityInfoQueryDTO.getApplyType())
                .eq(activityInfoQueryDTO.getValidStatus() != null && activityInfoQueryDTO.getValidStatus() != 0
                        , ActivityInfo::getValidStatus, activityInfoQueryDTO.getValidStatus())
                .eq(activityInfoQueryDTO.getStatus() != null
                        , ActivityInfo::getStatus, activityInfoQueryDTO.getStatus())
                .eq(activityInfoQueryDTO.getActivityLobbyId() != null && activityInfoQueryDTO.getActivityLobbyId() != 0
                        , ActivityInfo::getActivityLobbyId, activityInfoQueryDTO.getActivityLobbyId())
                .orderByDesc(Lists.newArrayList(ActivityInfo::getCreateTime, ActivityInfo::getId));
        IPage<ActivityInfoVO> page1 = queryWrapper.page(page).convert(activityInfoConvert::toVo);
        if (CollectionUtils.isNotEmpty(page1.getRecords())) {
            for (ActivityInfoVO activityInfoVO : page1.getRecords()) {
                //查询类型
                ActivityType activityType = activityTypeService.getById(activityInfoVO.getType());
                if (activityType != null) {
                    activityInfoVO.setTypeCode(activityType.getTypeCode());
                    activityInfoVO.setTypeName(activityType.getTypeName());
                }
                //活动大厅名称
                ActivityLobby activityLobby = activityLobbyService.getById(activityInfoVO.getActivityLobbyId());
                if (activityLobby != null) {
                    activityInfoVO.setActivityLobbyName(activityLobby.getTitle());
                }
            }
        }
        return page1;
    }

    @Override
    public ActivityInfoVO detail(Long id) {
        ActivityInfo activityInfo = this.getById(id);
        if (activityInfo == null) {
            throw new ServiceException("该活动不存在");
        }
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
                banner.setStatus(SysBannerInfoEnum.Status.VALID.getValue());

                List<SysBannerInfo> bannerList = sysBannerInfoService.getByBanner(banner);
                bannerList.forEach(sysBannerInfo -> {
                    if (sysBannerInfo.getStatus() != 0 && !activityInfo.getStatus().equals(sysBannerInfo.getStatus())) {
                        sysBannerInfo.setStatus(SysBannerInfoEnum.Status.INVALID.getValue());
                        sysBannerInfoService.save(sysBannerInfo);
                    }
                });
            }
        } else {
            throw new ServiceException("未选择对象或对象无效");
        }
    }

    @Override
    public void checkActivityLobbyId(Long activityLobbyId, Long id) {
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
    public List<ActivityInfoVO> getAllActivity() {
        LambdaQueryChainWrapper<ActivityInfo> queryWrapper = this.lambdaQuery();
        queryWrapper.eq(ActivityInfo::getStatus, 1);
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
        ;
        return queryWrapper.list();
    }

    @Override
    public List<ActivityInfo> findByTypeId(Long typeId) {
        return this.lambdaQuery().eq(ActivityInfo::getType, typeId).list();
    }

    @Override
    public void update(ActivityInfoUpdateDTO activityInfoUpdateDTO, String country) {
        ActivityInfo activityInfo = activityInfoConvert.toEntity(activityInfoUpdateDTO);
        if (!this.saveActivityInfo(activityInfo)) {
            throw new ServiceException("修改组合活动失败！");
        }
    }

    @Override
    public void delete(String ids) {
        String[] idArr = ids.split(",");
        List<Long> idList = new ArrayList<>();
        for (String idStr : idArr) {
            idList.add(Long.parseLong(idStr));
        }
        this.removeByIds(idList);
    }

    @Override
    public void updateSort(ActivityInfoUpdateSortDTO activityInfoUpdateSortDTO) {
        ActivityInfo activityInfo = this.getById(activityInfoUpdateSortDTO.getId());
        if (activityInfo == null) {
            throw new ServiceException("该活动不存在");
        }
        LambdaUpdateWrapper<ActivityInfo> activityInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        activityInfoLambdaUpdateWrapper
                .eq(ActivityInfo::getId, activityInfoUpdateSortDTO.getId())
                .set(ActivityInfo::getSort, activityInfoUpdateSortDTO.getSort());
        boolean result = this.update(activityInfoLambdaUpdateWrapper);
        if (!result) {
            throw new ServiceException("更新活动失败");
        }
    }


}
