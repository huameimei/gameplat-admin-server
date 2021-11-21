package com.gameplat.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGrowthLevelConvert;
import com.gameplat.admin.mapper.MemberGrowthLevelMapper;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.model.domain.MemberGrowthLevel;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.admin.service.MemberGrowthLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lily
 * @description
 * @date 2021/11/20
 */

@Service
@RequiredArgsConstructor
public class MemberGrowthLevelServiceImpl extends ServiceImpl<MemberGrowthLevelMapper, MemberGrowthLevel> implements MemberGrowthLevelService {

    @Autowired private MemberGrowthLevelConvert levelConvert;

    @Autowired private MemberGrowthLevelMapper levelMapper;

    /**
     * 查询所有等级
     */
    @Override
    public List<MemberGrowthLevelVO> findList(Integer limitLevel, String language) {

        if (StrUtil.isBlank(language)) {
            language = LanguageEnum.app_zh_CN.getCode();
        }
        List<MemberGrowthLevel> list = levelMapper.findList(limitLevel+1, language);
        List<MemberGrowthLevelVO> voList = new ArrayList<>();
        for (MemberGrowthLevel level:list) {
            MemberGrowthLevelVO memberGrowthLevelVO = levelConvert.toVo(level);
            voList.add(memberGrowthLevelVO);
        }
        return voList;
    }
}
