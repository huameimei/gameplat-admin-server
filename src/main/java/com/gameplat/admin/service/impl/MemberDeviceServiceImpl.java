package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberDeviceConvert;
import com.gameplat.admin.mapper.MemberDeviceMapper;
import com.gameplat.admin.model.dto.MemberDeviceQueryDTO;
import com.gameplat.admin.model.vo.MemberDeviceVO;
import com.gameplat.admin.service.MemberDeviceService;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.member.MemberDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberDeviceServiceImpl extends ServiceImpl<MemberDeviceMapper, MemberDevice>
    implements MemberDeviceService {

  @Autowired private MemberDeviceConvert memberDeviceConvert;

  @Override
  public IPage<MemberDeviceVO> page(Page<MemberDevice> page, MemberDeviceQueryDTO queryDTO) {
    return this.lambdaQuery()
        .eq(
            ObjectUtil.isNotEmpty(queryDTO.getUsername()),
            MemberDevice::getUsername,
            queryDTO.getUsername())
        .eq(
            ObjectUtil.isNotEmpty(queryDTO.getDeviceClientId()),
            MemberDevice::getDeviceClientId,
            queryDTO.getDeviceClientId())
        .page(page)
        .convert(memberDeviceConvert::toVo);
  }

  @Override
  public void delete(Long id) {
    Assert.isTrue(this.removeById(id), "删除用户设备新失败!");
  }
}
