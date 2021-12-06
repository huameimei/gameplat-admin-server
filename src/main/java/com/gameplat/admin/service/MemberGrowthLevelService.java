package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberGrowthLevel;
import com.gameplat.admin.model.dto.MemberGrowthLevelEditDto;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;

import java.util.List;

/**
 * @author Lily
 */

public interface MemberGrowthLevelService extends IService<MemberGrowthLevel> {

    /**
     * 查询所有等级
     */
    List<MemberGrowthLevelVO> findList(Integer limitLevel, String language);

    /**
     * 批量修改VIP等级
     */
    void batchUpdateLevel(List<MemberGrowthLevelEditDto> list,  String language);


}
