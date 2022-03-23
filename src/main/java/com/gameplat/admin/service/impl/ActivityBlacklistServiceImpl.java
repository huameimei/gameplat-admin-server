package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityBlacklistConvert;
import com.gameplat.admin.enums.ActivityInfoEnum;
import com.gameplat.admin.mapper.ActivityBlacklistMapper;
import com.gameplat.admin.model.dto.ActivityBlacklistAddDTO;
import com.gameplat.admin.model.dto.ActivityBlacklistQueryDTO;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.service.ActivityBlacklistService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.ip.IpAddressParser;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.activity.ActivityBlacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动黑名单业务处理
 *
 * @author kenvin
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ActivityBlacklistServiceImpl
    extends ServiceImpl<ActivityBlacklistMapper, ActivityBlacklist>
    implements ActivityBlacklistService {

  @Autowired private ActivityBlacklistConvert activityBlacklistConvert;

  @Autowired private MemberService memberService;

  @Override
  public IPage<ActivityBlacklist> list(
      PageDTO<ActivityBlacklist> page, ActivityBlacklistQueryDTO dto) {
    return this.lambdaQuery()
        .like(
            StringUtils.isNotBlank(dto.getLimitedContent()),
            ActivityBlacklist::getLimitedContent,
            dto.getLimitedContent())
        .eq(
            dto.getLimitedType() != null && dto.getLimitedType() != 0,
            ActivityBlacklist::getLimitedType,
            dto.getLimitedType())
        .eq(
            dto.getActivityId() != null && dto.getActivityId() != 0,
            ActivityBlacklist::getActivityId,
            dto.getActivityId())
        .page(page);
  }

  @Override
  public boolean add(ActivityBlacklistAddDTO dto) {
    if (ActivityInfoEnum.BlackList.MEMBER.match(dto.getLimitedType())) {
      MemberInfoVO memberInfo = memberService.getMemberInfo(dto.getLimitedContent());
      if (StringUtils.isNull(memberInfo)) {
        throw new ServiceException("限制内容框中输入的会员账号不存在");
      }
    } else if (ActivityInfoEnum.BlackList.IP.match(dto.getLimitedType())) {
      if (!IpAddressParser.inputIsIpAddress(dto.getLimitedContent())) {
        throw new ServiceException("限制内容框中输入的IP地址无效");
      }
    }

    ActivityBlacklist activityBlacklist = activityBlacklistConvert.toEntity(dto);
    activityBlacklist.setCreateTime(DateUtil.getNowTime());
    return this.save(activityBlacklist);
  }

  @Override
  public void remove(String ids) {
    String[] idArr = ids.split(",");
    List<Long> idList = new ArrayList<>();
    for (String idStr : idArr) {
      idList.add(Long.parseLong(idStr));
    }
    this.removeByIds(idList);
  }
}
