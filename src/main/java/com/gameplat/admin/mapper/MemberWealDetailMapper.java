package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.member.MemberWealDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberWealDetailMapper extends BaseMapper<MemberWealDetail> {

  int batchSave(@Param("list") List<MemberWealDetail> list);
}
