package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberWealConfig;
import com.gameplat.admin.model.dto.MemberWealConfigAddDTO;
import com.gameplat.admin.model.dto.MemberWealConfigEditDTO;
import com.gameplat.admin.model.vo.MemberWealConfigVO;

public interface MemberWealConfigService extends IService<MemberWealConfig> {

    /** 增 */
    void addWealConfig(MemberWealConfigAddDTO dto);

    /** 删 */
    void removeWealConfig(Long id);

    /** 改 */
    void updateWealConfig(MemberWealConfigEditDTO dto);

    /** 查 */
    IPage<MemberWealConfigVO> page(PageDTO<MemberWealConfig> page, String language);
}
