package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberWeal;
import com.gameplat.admin.model.dto.MemberWealAddDTO;
import com.gameplat.admin.model.dto.MemberWealDTO;
import com.gameplat.admin.model.dto.MemberWealEditDTO;
import com.gameplat.admin.model.vo.MemberWealVO;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lily
 * @description 福利发放
 * @date 2021/11/20
 */

public interface MemberWealService extends IService<MemberWeal> {

    /**
     * 获取福利列表
     */
    IPage<MemberWealVO> findMemberWealList(IPage<MemberWeal> page, MemberWealDTO queryDTO);

    /**
     * 添加福利
     */
    void addMemberWeal(MemberWealAddDTO dto);

    /**
     * 修改福利
     */
    void updateMemberWeal(MemberWealEditDTO dto);

    /**
     * 删除福利
     */
    void deleteMemberWeal(Long id);

    /**
     * 结算
     */
    void settleWeal(Long id);

    /**
     * 福利派发
     */
    void distributeWeal(Long wealId, HttpServletRequest request);

    /**
     * 福利回收
     */
    void recycleWeal(Long wealId, HttpServletRequest request);



}
