package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberWealReword;
import com.gameplat.admin.model.dto.MemberWealRewordAddDTO;
import com.gameplat.admin.model.dto.MemberWealRewordDTO;
import com.gameplat.admin.model.vo.MemberWealRewordVO;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public interface MemberWealRewordService extends IService<MemberWealReword> {

    /**
     * 获取VIP福利记录列表 分页
     */
    IPage<MemberWealRewordVO> findWealRewordList(IPage<MemberWealReword> page, MemberWealRewordDTO queryDTO);

    /**
     * 导出VIP福利记录列表
     */
    void exportWealReword(MemberWealRewordDTO queryDTO, HttpServletResponse response);

    /**
     * 审核  当会员领取了奖励之后  需要审核 审核通过之后
     */
//    void check(MemberWealRewordCheckDTO dto, HttpServletRequest request);

    /**
     * 新增福利记录
     */
    void insert(MemberWealRewordAddDTO dto);

    /**
     * 获取VIP福利记录列表 不分页
     */
    List<MemberWealReword> findList(MemberWealRewordDTO queryDTO);

    /**
     * 修改VIP福利记录
     */
    void updateWealRecord (MemberWealReword entity);
}
