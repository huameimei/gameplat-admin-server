package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberDeviceConvert;
import com.gameplat.admin.mapper.MemberDeviceMapper;
import com.gameplat.admin.model.dto.MemberDeviceQueryDTO;
import com.gameplat.admin.model.vo.MemberDeviceVO;
import com.gameplat.admin.service.MemberDeviceService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.member.MemberDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author aBen
 * @date 2022/4/9 0:18
 * @desc
 */
@Service
public class MemberDeviceServiceImpl extends ServiceImpl<MemberDeviceMapper, MemberDevice>
        implements MemberDeviceService {

  @Autowired
  private MemberDeviceMapper memberDeviceMapper;

  @Autowired
  private MemberService memberService;

  @Autowired
  private MemberDeviceConvert memberDeviceConvert;

  private static final int YES_DETAIL = 1;

  private static final int NO_DETAIL = 0;

  @Override
  public IPage<MemberDeviceVO> findList(Page<MemberDevice> page, MemberDeviceQueryDTO queryDTO) {
    if (StringUtils.isNotEmpty(queryDTO.getUsername())) {
      memberService.getByAccount(queryDTO.getUsername()).orElseThrow(() -> new ServiceException("会员账号不存在"));
    }

    IPage<MemberDeviceVO> list;
    if (queryDTO.getIsDetail() == NO_DETAIL) {
      list = memberDeviceMapper.findList(page, queryDTO);
    } else if (queryDTO.getIsDetail() == YES_DETAIL) {
      QueryWrapper<MemberDevice> query = Wrappers.query();
      query.select("max( last_login_time ) lastLoginTime, device_client_id deviceClientId, username");
      query.gt("length(device_client_id)", 0);
      query.likeRight(StringUtils.isNotEmpty(queryDTO.getDeviceClientId()), "device_client_id", queryDTO.getDeviceClientId())
              .groupBy("user_id").orderByDesc("last_login_time");
      list = memberDeviceMapper.selectPage(page, query).convert(memberDeviceConvert::toVo);
    } else {
      throw new ServiceException("标识传参错误");
    }

    return list;
  }
}
