package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberWealConfig;
import com.gameplat.admin.model.dto.MemberWealConfigAddDTO;
import com.gameplat.admin.model.dto.MemberWealConfigEditDTO;

public interface MemberWealConfigService extends IService<MemberWealConfig> {

    /** 增 */
    void addWealConfig(MemberWealConfigAddDTO dto);

    /** 删 */
    void removeWealConfig(Long id);

    /** 改 */
    void updateWealConfig(MemberWealConfigEditDTO dto);
}
