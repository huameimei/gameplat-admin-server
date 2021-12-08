package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberVipSignStatis;
import com.gameplat.admin.model.dto.MemberVipSignStatisDTO;
import com.gameplat.admin.model.vo.MemberVipSignStatisVO;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lily
 * @description VIP会员签到汇总
 * @date 2021/11/24
 */

public interface MemberVipSignStatisService  extends IService<MemberVipSignStatis> {

    /**
     * 查询VIP会员签到记录列表
     */
    IPage<MemberVipSignStatisVO> findSignList(IPage<MemberVipSignStatis> page, MemberVipSignStatisDTO queryDTO);

    /**
     * 导出签名
     */
    void exportSignStatis(MemberVipSignStatisDTO queryDTO, HttpServletResponse response);
}
