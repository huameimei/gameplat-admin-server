package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberWeal;
import com.gameplat.admin.model.dto.MemberWealAddDTO;
import com.gameplat.admin.model.dto.MemberWealDTO;
import com.gameplat.admin.model.vo.MemberWealVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lily
 * @description 福利发放
 * @date 2021/11/20
 */

public interface MemberWealService  extends IService<MemberWeal> {

    /**
     * 获取福利列表
     */
    IPage<MemberWealVO> findMemberWealList(IPage<MemberWeal> page, MemberWealDTO queryDTO);

    /**
     * 添加福利
     */
    void addMemberWeal(MemberWealAddDTO dto);

}
