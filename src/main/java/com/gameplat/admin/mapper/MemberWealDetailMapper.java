package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberWealDetail;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberWealDetailMapper extends BaseMapper<MemberWealDetail> {

    int batchSave(@Param("list") List<MemberWealDetail> list);
}
