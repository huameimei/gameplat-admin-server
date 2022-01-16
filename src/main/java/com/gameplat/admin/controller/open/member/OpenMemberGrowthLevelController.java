package com.gameplat.admin.controller.open.member;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.model.dto.GrowthLevelLogoEditDTO;
import com.gameplat.admin.model.dto.MemberGrowthConfigEditDto;
import com.gameplat.admin.model.dto.MemberGrowthLevelEditDto;
import com.gameplat.admin.model.vo.MemberConfigLevelVO;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.admin.service.MemberGrowthLevelService;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lily
 * @description 用户成长等级
 * @date 2021/11/20
 */

@Api(tags = "VIP等级配置")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/growthLevel")
public class OpenMemberGrowthLevelController {

    @Autowired
    private MemberGrowthLevelService levelService;

    @Autowired
    private MemberGrowthConfigService configService;

    @ApiOperation(value = "VIP配置和VIP等级列表/查询logo配置列表")
    @GetMapping("/config")
    @PreAuthorize("hasAuthority('member:growthLevel:config')")
    public MemberConfigLevelVO getLevelConfig(@ApiParam(name = "language", value = "语言", required = false)
                                              @RequestParam(required = false) String language) {
        try {
            if (StrUtil.isBlank(language)) {
                language = LanguageEnum.app_zh_CN.getCode();
            }
            //经验值描述
            MemberGrowthConfigVO config = configService.findOneConfig(language);
            //最高等级
            Integer limitLevel = config.getLimitLevel();
            if (limitLevel == null) {
                limitLevel = 50;
            }
            //用户成长等级配置
            List<MemberGrowthLevelVO> levels = levelService.findList(limitLevel, language);
            return new MemberConfigLevelVO() {{
                setConfigVO(config);
                setLevelVO(levels);
            }};
        } catch (Exception e) {
            throw new ServiceException("获取成长等级和配置失败:" + e);
        }
    }

    @ApiOperation(value = "修改VIP配置")
    @PreAuthorize("hasAuthority('member:growthLevel:edit')")
    @PutMapping("/update")
    public void update(@ApiParam(name = "修改VIP配置入参", value = "传入json格式", required = true)
                       @RequestBody MemberGrowthConfigEditDto configEditDto) {

        if (ObjectUtils.isEmpty(configEditDto.getId())) {
            throw new ServiceException(" 编号不能为空");
        }
        configEditDto.setUpdateBy(GlobalContextHolder.getContext().getUsername());
        if (StrUtil.isBlank(configEditDto.getLanguage())) {
            configEditDto.setLanguage(LanguageEnum.app_zh_CN.getCode());
        }
        configService.updateGrowthConfig(configEditDto);
    }


    @ApiOperation(value = "后台批量修改VIP等级")
    @PreAuthorize("hasAuthority('member:growthLevel:updateLevel')")
    @PutMapping("/updateLevel")
    public void batchUpdateLevel(@RequestBody JSONObject obj) {
        String language = obj.get("language").toString();
        language = StrUtil.isBlank(language) ? "zh-CN" : language;
        Object levels = obj.get("levels");
        JSONArray jsonArray = JSONUtil.parseArray(levels);
        List<MemberGrowthLevelEditDto> list = jsonArray.toList(MemberGrowthLevelEditDto.class);
        levelService.batchUpdateLevel(list, language);
    }

    @ApiOperation(value = "VIP等级列表")
    @GetMapping("/vipList")
    @PreAuthorize("hasAuthority('member:growthLevel:config')")
    public List<MemberGrowthLevelVO> vipList(@ApiParam(name = "language", value = "语言")
                                                 @RequestParam(required = false) String language) {
        try {
            if (StrUtil.isBlank(language)) {
                language = LanguageEnum.app_zh_CN.getCode();
            }
            //经验值描述
            MemberGrowthConfigVO config = configService.findOneConfig(language);
            //最高等级
            Integer limitLevel = config.getLimitLevel();
            if (limitLevel == null) {
                limitLevel = 50;
            }
            //用户成长等级配置
            return levelService.findList(limitLevel, language);
        } catch (Exception e) {
            throw new ServiceException("获取VIP等级列表失败:" + e);
        }
    }

    @ApiOperation(value = "修改logo配置")
    @PutMapping("/updateLogo")
    @PreAuthorize("hasAuthority('member:growthLevel:updateLogo')")
    public void updateLogo(@Validated @RequestBody GrowthLevelLogoEditDTO dto) {
        levelService.updateLogo(dto);
    }
}
