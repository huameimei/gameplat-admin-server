package com.gameplat.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGrowthLevelConvert;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.mapper.MemberGrowthLevelMapper;
import com.gameplat.admin.model.domain.MemberGrowthLevel;
import com.gameplat.admin.model.domain.MemberGrowthStatis;
import com.gameplat.admin.model.dto.MemberGrowthLevelEditDto;
import com.gameplat.admin.model.dto.MemberGrowthStatisDTO;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.admin.service.MemberGrowthLevelService;
import com.gameplat.admin.service.MemberGrowthStatisService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired private MemberGrowthStatisService growthStatisService;

    @Autowired private MemberGrowthConfigService growthConfigService;

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

    /**
     * 批量修改VIP等级
     */
    @Override
    public void batchUpdateLevel(List<MemberGrowthLevelEditDto> list, String language) {
        list.stream().forEach(item -> {
            String oldName = this.lambdaQuery().eq(ObjectUtils.isNotEmpty(item.getLevel()), MemberGrowthLevel::getLevel, item.getLevel()).one().getLevelName();
            JSONObject jsonObject = JSONUtil.parseObj(oldName);
            jsonObject.put(language,item.getLevelName());
            item.setLevelName(jsonObject.toString());
        });
        int count = levelMapper.batchUpdateLevel(list);
        //重新计算会员的等级
        if(count > 0) {
            //获取到所有VIP汇总数据
            List<MemberGrowthStatis> staticList = growthStatisService.findList(new MemberGrowthStatisDTO());
            MemberGrowthConfigVO growthConfig = growthConfigService.findOneConfig(LanguageEnum.app_zh_CN.getCode());
            for (MemberGrowthStatis userStatis : staticList) {
                //得到重新计算后的等级
                Integer newLevel = growthStatisService.dealUpLevel(userStatis.getGrowth(), growthConfig);
                userStatis.setLevel(newLevel);
                growthStatisService.insertOrUpdate(userStatis);
            }
        }
    }
}
