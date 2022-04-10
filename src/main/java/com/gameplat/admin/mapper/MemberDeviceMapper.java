package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.MemberDeviceQueryDTO;
import com.gameplat.admin.model.vo.MemberDeviceVO;
import com.gameplat.model.entity.member.MemberDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author aBen
 * @date 2022/4/9 0:18
 * @desc
 */
@Mapper
public interface MemberDeviceMapper extends BaseMapper<MemberDevice> {

  IPage<MemberDeviceVO> findList(Page<MemberDevice> page, @Param("dto") MemberDeviceQueryDTO queryDTO);

}
