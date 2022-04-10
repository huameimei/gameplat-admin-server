package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.MemberDeviceQueryDTO;
import com.gameplat.admin.model.vo.MemberDeviceVO;
import com.gameplat.model.entity.member.MemberDevice;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author aBen
 * @date 2022/4/9 0:18
 * @desc
 */

public interface MemberDeviceService {

  IPage<MemberDeviceVO> findList(Page<MemberDevice> page, MemberDeviceQueryDTO queryDTO);

}
