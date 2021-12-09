package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityTypeConvert;
import com.gameplat.admin.mapper.ActivityTypeMapper;
import com.gameplat.admin.model.domain.ActivityInfo;
import com.gameplat.admin.model.domain.ActivityType;
import com.gameplat.admin.model.dto.ActivityTypeAddDTO;
import com.gameplat.admin.model.dto.ActivityTypeDTO;
import com.gameplat.admin.model.dto.ActivityTypeQueryDTO;
import com.gameplat.admin.model.dto.ActivityTypeUpdateDTO;
import com.gameplat.admin.model.vo.ActivityTypeVO;
import com.gameplat.admin.service.ActivityInfoService;
import com.gameplat.admin.service.ActivityTypeService;
import com.gameplat.base.common.exception.ServiceException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityTypeServiceImpl extends ServiceImpl<ActivityTypeMapper, ActivityType>
        implements ActivityTypeService {

    @Autowired
    private ActivityTypeConvert activityTypeConvert;

    @Autowired
    private ActivityInfoService activityInfoService;


    @Override
    public List<ActivityType> findByTypeIdList(List<Long> activityTypeIdList) {
        if (CollectionUtils.isEmpty(activityTypeIdList)) {
            return new ArrayList<>();
        }
        return this.lambdaQuery().in(ActivityType::getId, activityTypeIdList).list();
    }

    @Override
    public IPage<ActivityTypeVO> list(PageDTO<ActivityType> page, ActivityTypeQueryDTO activityTypeQueryDTO) {
        LambdaQueryChainWrapper<ActivityType> queryChainWrapper = this.lambdaQuery();
        if (activityTypeQueryDTO.getTypeStatus() == null) {
            activityTypeQueryDTO.setTypeStatus(1);
        }
        queryChainWrapper.eq(ActivityType::getLanguage, activityTypeQueryDTO.getLanguage())
                .eq(ActivityType::getTypeStatus, activityTypeQueryDTO.getTypeStatus());

        return queryChainWrapper.page(page).convert(activityTypeConvert::toVo);
    }

    @Override
    public void add(ActivityTypeAddDTO activityTypeAddDTO) {
        //查询是否已经添加
        ActivityType activityType = this.getByTypeNameAndLanguage(activityTypeAddDTO.getTypeName(), activityTypeAddDTO.getLanguage());
        if (activityType != null) {
            throw new ServiceException("活动类型名字或者活动类型已存在");
        }
        ActivityType activityType1 = activityTypeConvert.toEntity(activityTypeAddDTO);
        if (activityType1.getTypeStatus() == null) {
            activityType1.setTypeStatus(1);
        }
        this.save(activityType1);
    }

    @Override
    public void update(ActivityTypeUpdateDTO activityTypeUpdateDTO) {
        //查询是否已经添加
        ActivityType activityType = this.getByTypeNameAndLanguage(activityTypeUpdateDTO.getTypeName(), activityTypeUpdateDTO.getLanguage());
        if (activityType != null) {
            throw new ServiceException("活动类型名字或者活动类型已存在");
        }
        ActivityType activityType1 = activityTypeConvert.toEntity(activityTypeUpdateDTO);
        if (activityType1.getTypeStatus() == null) {
            activityType1.setTypeStatus(1);
        }
        this.updateById(activityType1);
    }

    @Override
    public void remove(String ids) {
        String[] idArr = ids.split(",");
        //遍历是否有活动关联
        for (String idStr : idArr) {
            Long id = Long.parseLong(idStr);
            List<ActivityInfo> activityInfoList = activityInfoService.findByTypeId(id);
            if (CollectionUtils.isNotEmpty(activityInfoList)) {
                throw new ServiceException("该活动坂块下面有活动，无法删除");
            }
            this.removeById(id);
        }
    }

    /**
     * 查询是否已经存在
     *
     * @param typeName
     * @param language
     * @return
     */
    private ActivityType getByTypeNameAndLanguage(String typeName, String language) {
        return this.lambdaQuery().eq(ActivityType::getTypeName, typeName)
                .eq(ActivityType::getLanguage, language).one();
    }
}
