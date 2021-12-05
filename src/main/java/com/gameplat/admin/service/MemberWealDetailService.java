package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberWealDetail;
import com.gameplat.admin.model.dto.MemberWealDetailDTO;
import com.gameplat.admin.model.vo.MemberWealDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberWealDetailService extends IService<MemberWealDetail> {

    IPage<MemberWealDetailVO> findWealDetailList(PageDTO<MemberWealDetail> page, MemberWealDetailDTO queryDTO);

    void removeWealDetail(Long wealId);

    int batchSave(@Param("list") List<MemberWealDetail> list);

    List<MemberWealDetail> findSatisfyMember (MemberWealDetail wealDetail);

    void updateByWealStatus (Long id, Integer status);
}