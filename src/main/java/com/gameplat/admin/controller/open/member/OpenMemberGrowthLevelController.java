package com.gameplat.admin.controller.open.member;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.model.dto.MemberGrowthConfigEditDto;
import com.gameplat.admin.model.vo.MemberConfigLevelVO;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.admin.service.MemberGrowthLevelService;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lily
 * @description 用户成长等级
 * @date 2021/11/20
 */

@Api(tags = "用户成长值等级API")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/growthLevel")
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
            throw new ServiceException("获取成长等级和配置失败:"+e);
        }
    }

    @ApiOperation(value = "后台修改成长值配置")
    @PreAuthorize("hasAuthority('member:growthLevel:edit')")
    @PutMapping("/update")
    public void update(@RequestBody MemberGrowthConfigEditDto configEditDto) {

        if(ObjectUtils.isEmpty(configEditDto.getId())){
            throw new ServiceException(" 编号不能为空");
        }
        configEditDto.setUpdateBy(GlobalContextHolder.getContext().getUsername());
        if (StrUtil.isBlank(configEditDto.getLanguage())){
           configEditDto.setLanguage(LanguageEnum.app_zh_CN.getCode());
        }
        configService.updateGrowthConfig(configEditDto);
    }
}
