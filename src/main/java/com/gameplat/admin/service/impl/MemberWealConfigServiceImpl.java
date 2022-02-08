package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberWealConfigConvert;
import com.gameplat.admin.mapper.MemberWealConfigMapper;
import com.gameplat.admin.model.domain.MemberWealConfig;
import com.gameplat.admin.model.dto.MemberWealConfigAddDTO;
import com.gameplat.admin.model.dto.MemberWealConfigEditDTO;
import com.gameplat.admin.model.vo.MemberWealConfigVO;
import com.gameplat.admin.service.MemberWealConfigService;
import com.gameplat.base.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lily
 * @description 会员权益
 * @date 2022/1/15
 */

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberWealConfigServiceImpl extends ServiceImpl<MemberWealConfigMapper, MemberWealConfig> implements MemberWealConfigService {

    private final MemberWealConfigConvert memberWealConfigConvert;

    /** 增 */
    @Override
    public void addWealConfig(MemberWealConfigAddDTO dto) {
        this.save(memberWealConfigConvert.toEntity(dto));
    }

    /** 删 */
    @Override
    public void removeWealConfig(Long id) {
        this.removeById(id);
    }

    /** 改 */
    @Override
    public void updateWealConfig(MemberWealConfigEditDTO dto) {
        this.updateById(memberWealConfigConvert.toEntity(dto));
    }

    @Override
    public IPage<MemberWealConfig> page(PageDTO<MemberWealConfig> page, String language){
        if (StringUtils.isEmpty(language)){
            language = "zh-cn";
        }
        return
            this.lambdaQuery()
                    .eq(MemberWealConfig::getLanguage, language)
                    .page(page);
    }

}
