package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityTypeConvert;
import com.gameplat.admin.mapper.ActivityTypeMapper;
import com.gameplat.admin.model.dto.ActivityTypeAddDTO;
import com.gameplat.admin.model.dto.ActivityTypeQueryDTO;
import com.gameplat.admin.model.dto.ActivityTypeUpdateDTO;
import com.gameplat.admin.model.vo.ActivityTypeVO;
import com.gameplat.admin.service.ActivityInfoService;
import com.gameplat.admin.service.ActivityTypeService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.model.entity.activity.ActivityInfo;
import com.gameplat.model.entity.activity.ActivityType;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动类型
 *
 * @author kenvin
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ActivityTypeServiceImpl extends ServiceImpl<ActivityTypeMapper, ActivityType>
    implements ActivityTypeService {

  @Autowired private ActivityTypeConvert activityTypeConvert;

  @Autowired private ActivityInfoService activityInfoService;

  @Override
  public List<ActivityType> findByTypeIdList(List<Long> activityTypeIdList) {
    if (CollectionUtils.isEmpty(activityTypeIdList)) {
      return new ArrayList<>();
    }
    return this.lambdaQuery().in(ActivityType::getId, activityTypeIdList).list();
  }

  @Override
  public IPage<ActivityTypeVO> list(PageDTO<ActivityType> page, ActivityTypeQueryDTO dto) {
    LambdaQueryChainWrapper<ActivityType> queryChainWrapper = this.lambdaQuery();
    queryChainWrapper
        .eq(ActivityType::getLanguage, dto.getLanguage())
        .eq(dto.getTypeStatus() != null, ActivityType::getTypeStatus, dto.getTypeStatus());

    return queryChainWrapper.page(page).convert(activityTypeConvert::toVo);
  }

  @Override
  public void add(ActivityTypeAddDTO dto) {
    // 查询是否已经添加
    ActivityType activityType =
        this.getByTypeNameAndLanguage(dto.getTypeName(), dto.getLanguage(), null);
    if (activityType != null) {
      throw new ServiceException("活动类型名字或者活动类型已存在");
    }
    ActivityType activityType1 = activityTypeConvert.toEntity(dto);
    if (activityType1.getTypeStatus() == null) {
      activityType1.setTypeStatus(BooleanEnum.YES.value());
    }
    this.save(activityType1);
  }

  @Override
  public void update(ActivityTypeUpdateDTO dto) {
    // 查询是否已经添加
    ActivityType activityType =
        this.getByTypeNameAndLanguage(dto.getTypeName(), dto.getLanguage(), dto.getId());
    if (activityType != null) {
      throw new ServiceException("活动类型名字或者活动类型已存在");
    }
    ActivityType activityType1 = activityTypeConvert.toEntity(dto);
    if (activityType1.getTypeStatus() == null) {
      activityType1.setTypeStatus(BooleanEnum.YES.value());
    }
    this.updateById(activityType1);
  }

  @Override
  public void remove(String ids) {
    String[] idArr = ids.split(",");
    // 遍历是否有活动关联
    StringBuilder stringBuilder = new StringBuilder();
    List<Long> idList = new ArrayList<>();
    for (String idStr : idArr) {
      Long id = Long.parseLong(idStr);
      List<ActivityInfo> activityInfoList = activityInfoService.findByTypeId(id);
      if (CollectionUtils.isNotEmpty(activityInfoList)) {
        ActivityType activityType = this.getById(id);
        if (StringUtils.isBlank(stringBuilder)) {
          stringBuilder.append(activityType.getTypeName());
        } else {
          stringBuilder.append(",").append(activityType.getTypeName());
        }
      }
      idList.add(id);
    }
    if (StringUtils.isNotBlank(stringBuilder)) {
      String msg = stringBuilder.toString();
      if (msg.startsWith(",")) {
        msg = msg.substring(1);
      }

      msg = "活动模块【" + msg + "】下有活动,不能删除";
      throw new ServiceException(msg);
    }
    if (CollectionUtils.isNotEmpty(idList)) {
      this.removeByIds(idList);
    }
  }

  @Override
  public List<ActivityTypeVO> listAll() {
    String language = LocaleContextHolder.getLocale().toLanguageTag();
    if(!language.contains("zh-CN,en-US,in-ID,th-TH,vi-VN")){
      language = "zh-CN";
    }
    List<ActivityType> list =
        this.lambdaQuery()
            .eq(ActivityType::getTypeStatus, BooleanEnum.YES.value())
            .eq(ActivityType::getLanguage, language)
            .list();

    List<ActivityTypeVO> activityTypeVOList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(list)) {
      for (ActivityType type : list) {
        activityTypeVOList.add(activityTypeConvert.toVo(type));
      }
    }
    return activityTypeVOList;
  }

  /**
   * 查询是否已经存在
   *
   * @param typeName String
   * @param language String
   * @return Long
   */
  private ActivityType getByTypeNameAndLanguage(String typeName, String language, Long id) {
    return this.lambdaQuery()
        .eq(ActivityType::getTypeName, typeName)
        .eq(ActivityType::getLanguage, language)
        .ne(id != null, ActivityType::getId, id)
        .one();
  }
}
