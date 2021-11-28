package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberGrowthStatis;
import com.gameplat.admin.model.dto.MemberGrowthStatisDTO;
import com.gameplat.admin.model.vo.MemberGrowthStatisVO;


public interface MemberGrowthStatisService extends IService<MemberGrowthStatis> {

    IPage<MemberGrowthStatisVO> findStatisList(PageDTO<MemberGrowthStatis> page, MemberGrowthStatisDTO dto);
}
