package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberGrowthConfig;
import com.gameplat.admin.model.dto.MemberGrowthConfigEditDto;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;

public interface MemberGrowthConfigService extends IService<MemberGrowthConfig> {

    /**
     * 只查询一条
     */
    MemberGrowthConfigVO findOneConfig(String language);

    /**
     * 修改成长值配置
     * */
    void updateGrowthConfig(MemberGrowthConfigEditDto configEditDto);
}
