package com.gameplat.admin.controller.open.member;

import cn.hutool.core.util.StrUtil;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.model.vo.MemberConfigLevelVO;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.admin.service.MemberGrowthLevelService;
import com.gameplat.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lily
 * @description 用户成长等级
 * @date 2021/11/20
 */

@Api(tags = "用户成长值等级API")
@Slf4j
@RestController
@RequestMapping("/api/member/growthLevel")
public class OpenMemberGrowthLevelController {

    @Autowired private MemberGrowthLevelService levelService;

    @Autowired private MemberGrowthConfigService configService;

    @ApiOperation(value = "获取成长等级和配置")
    @GetMapping("/config")
    @PreAuthorize("hasAuthority('member:growthLevel:config')")
    public MemberConfigLevelVO getLevelConfig(@RequestParam(required = false) String language) {
        try {
            if (StrUtil.isBlank(language)){
                language = LanguageEnum.app_zh_CN.getCode();
            }
            //经验值描述
            MemberGrowthConfigVO config = configService.findOneConfig(language);
            //最高等级
            Integer limitLevel = config.getLimitLevel();
            if (limitLevel == null){
                limitLevel = 50;
            }
            //用户成长等级配置
            List<MemberGrowthLevelVO> levels = levelService.findList(limitLevel, language);
            return new MemberConfigLevelVO(){{
                setConfigVO(config);
                setLevelVO(levels);
            }};
        } catch (Exception e) {
            throw new ServiceException("获取成长等级和配置失败!"+e);
        }
    }
}
