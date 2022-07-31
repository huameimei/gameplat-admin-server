package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityInfoConvert;
import com.gameplat.admin.convert.ActivityLobbyConvert;
import com.gameplat.admin.enums.SysBannerInfoEnum;
import com.gameplat.admin.mapper.ActivityInfoMapper;
import com.gameplat.admin.model.dto.ActivityInfoAddDTO;
import com.gameplat.admin.model.dto.ActivityInfoQueryDTO;
import com.gameplat.admin.model.dto.ActivityInfoUpdateDTO;
import com.gameplat.admin.model.dto.ActivityInfoUpdateSortDTO;
import com.gameplat.admin.model.vo.ActivityInfoVO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.model.entity.activity.ActivityInfo;
import com.gameplat.model.entity.activity.ActivityLobby;
import com.gameplat.model.entity.activity.ActivityType;
import com.gameplat.model.entity.sys.SysBannerInfo;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 活动业务类
 *
 * @author kenvin
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo>
    implements ActivityInfoService {

  @Autowired private ActivityInfoConvert activityInfoConvert;

  @Autowired private ActivityLobbyConvert activityLobbyConvert;

  @Autowired private SysBannerInfoService sysBannerInfoService;

  @Lazy @Autowired private ActivityTypeService activityTypeService;

  @Autowired private ActivityLobbyService activityLobbyService;

  @Autowired private ConfigService configService;

  @Autowired private SysDomainService sysDomainService;

  @Override
  public IPage<ActivityInfoVO> list(PageDTO<ActivityInfo> page, ActivityInfoQueryDTO dto) {
    LambdaQueryChainWrapper<ActivityInfo> queryWrapper = this.lambdaQuery();
    queryWrapper
        .eq(dto.getType() != null && dto.getType() != 0, ActivityInfo::getType, dto.getType())
        .eq(
            dto.getApplyType() != null && dto.getApplyType() != 0,
            ActivityInfo::getApplyType,
            dto.getApplyType())
        .eq(
            dto.getValidStatus() != null && dto.getValidStatus() != 0,
            ActivityInfo::getValidStatus,
            dto.getValidStatus())
        .eq(dto.getStatus() != null, ActivityInfo::getStatus, dto.getStatus())
        .eq(
            dto.getActivityLobbyId() != null && dto.getActivityLobbyId() != 0,
            ActivityInfo::getActivityLobbyId,
            dto.getActivityLobbyId())
        .eq(StringUtils.isNotBlank(dto.getCreateBy()), ActivityInfo::getCreateBy, dto.getCreateBy())
        // 按照时间倒序排列
        .orderByDesc(Lists.newArrayList(ActivityInfo::getCreateTime, ActivityInfo::getId))
        // 按排序sort正序排列
        .orderByAsc(Lists.newArrayList(ActivityInfo::getSort));

    IPage<ActivityInfoVO> page1 = queryWrapper.page(page).convert(activityInfoConvert::toVo);
    // String imgUrl = sysDomainService.getImageDomain();
    if (CollectionUtils.isNotEmpty(page1.getRecords())) {
      for (ActivityInfoVO activityInfoVO : page1.getRecords()) {
        // 查询类型
        ActivityType activityType = activityTypeService.getById(activityInfoVO.getType());
        if (activityType != null) {
          activityInfoVO.setTypeCode(activityType.getTypeCode());
          activityInfoVO.setTypeName(activityType.getTypeName());
        }
        // 活动大厅名称
        ActivityLobby activityLobby =
                activityLobbyService.getById(activityInfoVO.getActivityLobbyId());
        if (activityLobby != null) {
          activityInfoVO.setActivityLobbyName(activityLobby.getTitle());
        }
        //处理数据展示全路径
        // getAllActiveUrl(activityInfoVO,imgUrl);
      }
    }
    return page1;
  }

  @Override
  public ActivityInfoVO detail(Long id) {
    // String imgUrl = sysDomainService.getImageDomain();
    ActivityInfoVO rst=Optional.ofNullable(this.getById(id))
            .map(activityInfoConvert::toVo)
            .orElseThrow(() -> new ServiceException("该活动不存在"));
    // return getAllActiveUrl(rst,imgUrl);
    return rst;
  }

  @Override
  public void add(ActivityInfoAddDTO dto) {
    ActivityInfo activityInfo = activityInfoConvert.toEntity(dto);
    // 校验是否绑定活动大厅
    Long activityLobbyId = activityInfo.getActivityLobbyId();
    if (activityLobbyId != null && activityLobbyId != 0 && activityLobbyId != -1) {
      Long count = this.lambdaQuery().eq(ActivityInfo::getActivityLobbyId, activityLobbyId).count();
      if (count != null && count > 0) {
        throw new ServiceException("该活动大厅已被绑定，请重新选择活动大厅");
      }
    }

    //只保存相对路径
   // checkActiveUrl(activityInfo);
    if (this.saveActivityInfo(activityInfo)) {
      if (null != activityInfo.getId() && activityInfo.getId() > 0) {
        // 保存活动显示的图片
        SysBannerInfo banner = new SysBannerInfo();
        banner.setBannerType(configService.getInteger(DictDataEnum.ACTIVITY));
        banner.setChildType(activityInfo.getId());
        banner.setChildName(activityInfo.getTitle());
        banner.setLanguage(LocaleContextHolder.getLocale().toLanguageTag());
        banner.setStatus(SysBannerInfoEnum.Status.VALID.getValue());

        List<SysBannerInfo> bannerList = sysBannerInfoService.getByBanner(banner);
        bannerList.forEach(
            sysBannerInfo -> {
              if (sysBannerInfo.getStatus() != 0
                  && !activityInfo.getStatus().equals(sysBannerInfo.getStatus())) {
                sysBannerInfo.setStatus(SysBannerInfoEnum.Status.INVALID.getValue());
                sysBannerInfoService.saveSysBannerInfo(sysBannerInfo);
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
    queryWrapper.eq(ActivityInfo::getActivityLobbyId, activityLobbyId).eq(ActivityInfo::getId, id);
    Long count = queryWrapper.count();
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
    queryWrapper.eq(ActivityInfo::getStatus, BooleanEnum.YES.value());
    List<ActivityInfo> activityInfoList = queryWrapper.list();
    // 查询关联数据
    List<Long> activityTypeIdList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(activityInfoList)) {
      for (ActivityInfo activityInfo1 : activityInfoList) {
        activityTypeIdList.add(activityInfo1.getType());
      }
    }

    List<ActivityType> activityTypeList = activityTypeService.findByTypeIdList(activityTypeIdList);
    Map<Long, ActivityType> activityTypeMap = new HashMap<>(activityTypeList.size());
    if (CollectionUtils.isNotEmpty(activityTypeList)) {
      for (ActivityType activityType : activityTypeList) {
        activityTypeMap.put(activityType.getId(), activityType);
      }
    }

    List<ActivityInfoVO> activityInfoVOList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(activityInfoList)) {
      // String imgUrl = sysDomainService.getImageDomain();
      for (ActivityInfo activityInfo1 : activityInfoList) {
        ActivityInfoVO activityInfoVO = activityInfoConvert.toVo(activityInfo1);
        //处理数据展示全路径
        // getAllActiveUrl(activityInfoVO,imgUrl);
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
    queryWrapper.eq(
        activityInfo.getStatus() != null, ActivityInfo::getStatus, activityInfo.getStatus());
    return queryWrapper.list();
  }

  @Override
  public List<ActivityInfo> findByTypeId(Long typeId) {
    return this.lambdaQuery().eq(ActivityInfo::getType, typeId).list();
  }

  @Override
  public void update(ActivityInfoUpdateDTO dto) {
    ActivityInfo activityInfo = activityInfoConvert.toEntity(dto);
    //checkActiveUrl(activityInfo);
    activityInfo.setLanguage(LocaleContextHolder.getLocale().toLanguageTag());
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
    // --todo,需添加判断活动状态

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

  @Override
  public List<ActivityLobbyVO> findUnboundLobbyList(boolean isBind) {
    List<ActivityLobby> activityLobbyList =
        activityLobbyService
            .lambdaQuery()
            .eq(ActivityLobby::getStatus, BooleanEnum.YES.value())
            .orderByDesc(Lists.newArrayList(ActivityLobby::getCreateTime, ActivityLobby::getId))
            .list();
    if (CollectionUtils.isEmpty(activityLobbyList)) {
      return new ArrayList<>();
    }
    // 未绑定列表
    List<ActivityLobbyVO> unBoundLobbyList = new ArrayList<>();
    // 已绑定列表
    List<ActivityLobbyVO> boundLobbyList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(activityLobbyList)) {
      for (ActivityLobby activityLobby : activityLobbyList) {
        // 查询是否已被绑定活动发布记录
        Long count =
            this.lambdaQuery().eq(ActivityInfo::getActivityLobbyId, activityLobby.getId()).count();
        ActivityLobbyVO activityLobbyVO = activityLobbyConvert.toVo(activityLobby);
        if (count > 0) {
          boundLobbyList.add(activityLobbyVO);
        } else {
          unBoundLobbyList.add(activityLobbyVO);
        }
      }
    }
    if (isBind) {
      return boundLobbyList;
    } else {
      return unBoundLobbyList;
    }
  }

  /**
   * 检查url格式 只存绝对路径
   * @param activityInfo
   * @return
   */
  public ActivityInfo checkActiveUrl(ActivityInfo activityInfo) {
    if(StringUtils.isNotEmpty(activityInfo.getAppListPic())&&activityInfo.getAppListPic().startsWith("http")){
      activityInfo.setAppListPic(subStringUrl(activityInfo.getAppListPic()));
    }

    if(StringUtils.isNotEmpty(activityInfo.getAppPopupPic())&&activityInfo.getAppPopupPic().startsWith("http")){
      activityInfo.setAppPopupPic(subStringUrl(activityInfo.getAppPopupPic()));
    }

    if(StringUtils.isNotEmpty(activityInfo.getH5ListPic())&&activityInfo.getH5ListPic().startsWith("http")){
      activityInfo.setH5ListPic((subStringUrl(activityInfo.getH5ListPic())));
    }

    if(StringUtils.isNotEmpty(activityInfo.getPcListPic())&&activityInfo.getPcListPic().startsWith("http")){
      activityInfo.setPcListPic((subStringUrl(activityInfo.getPcListPic())));
    }

    if(StringUtils.isNotEmpty(activityInfo.getPcPopupPic())&&activityInfo.getPcPopupPic().startsWith("http")){
      activityInfo.setPcPopupPic((subStringUrl(activityInfo.getPcPopupPic())));
    }
    return activityInfo;
  }

  /**
   * 截取链接
   * @param url
   * @return
   */
  public String subStringUrl(String url) {
    for (int i = 0; i < 3; i++) {
      url = url.substring(url.indexOf("/") + 1);
    }
    return url;
  }

  /**
   * 展示全路径
   * @param activityInfo
   * @return
   */
  public ActivityInfoVO getAllActiveUrl(ActivityInfoVO activityInfo,String imgUrl) {
    if(!StringUtils.isNotEmpty(activityInfo.getAppListPic())&&activityInfo.getAppListPic().startsWith("http")){
      activityInfo.setAppListPic(imgUrl+"/"+ activityInfo.getAppListPic());
    }

    if(!StringUtils.isNotEmpty(activityInfo.getAppPopupPic())&&activityInfo.getAppPopupPic().startsWith("http")){
      activityInfo.setAppPopupPic(imgUrl+"/"+activityInfo.getAppPopupPic());
    }

    if(!StringUtils.isNotEmpty(activityInfo.getH5ListPic())&&activityInfo.getH5ListPic().startsWith("http")){
      activityInfo.setH5ListPic(imgUrl+"/"+activityInfo.getH5ListPic());
    }

    if(!StringUtils.isNotEmpty(activityInfo.getPcListPic())&&activityInfo.getPcListPic().startsWith("http")){
      activityInfo.setPcListPic(imgUrl+"/"+activityInfo.getPcListPic());
    }

    if(!StringUtils.isNotEmpty(activityInfo.getPcPopupPic())&&activityInfo.getPcPopupPic().startsWith("http")){
      activityInfo.setPcPopupPic(imgUrl+"/"+activityInfo.getPcPopupPic());
    }
    return activityInfo;
  }
}
