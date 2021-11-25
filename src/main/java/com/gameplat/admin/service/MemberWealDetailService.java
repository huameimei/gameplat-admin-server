package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberWealDetail;
import com.gameplat.admin.model.domain.MemberWealReword;
import com.gameplat.admin.model.dto.MemberWealRewordDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberWealDetailService extends IService<MemberWealDetail> {

//    IPage<MemberWealDetailVO> findWealDetailList(IPage<MemberWealDetail> page, MemberWealDetailDTO queryDTO);

    void removeWealDetail(Long wealId);

    int batchSave(@Param("list") List<MemberWealDetail> list);
}
