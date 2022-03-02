package com.gameplat.admin.controller.open.member;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.model.dto.GoldCoinDescUpdateDTO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.member.MemberGoldCoinRecord;
import com.gameplat.admin.model.dto.MemberGoldCoinRecordQueryDTO;
import com.gameplat.admin.model.vo.MemberGoldCoinRecordVO;
import com.gameplat.admin.service.MemberGoldCoinRecordService;
import com.gameplat.model.entity.member.MemberGrowthConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author lily
 * @description
 * @date 2022/3/1
 */
@Api(tags = "VIP金币明细")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/coin")
public class MemberGoldCoinRecordController {

    @Autowired
    private MemberGoldCoinRecordService memberGoldCoinRecordService;
    @Autowired
    private MemberGrowthConfigService memberGrowthConfigService;

    /** 增 */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询VIP金币明细")
    @PreAuthorize("hasAuthority('member:coin:page')")
    public IPage<MemberGoldCoinRecordVO> page(PageDTO<MemberGoldCoinRecord> page, MemberGoldCoinRecordQueryDTO dto){
        return memberGoldCoinRecordService.page(page, dto);
    }

    /** 增 */
    @PostMapping("/add")
    @ApiOperation(value = "后台添加VIP金币明细")
    @PreAuthorize("hasAuthority('member:coin:add')")
    public void add(Long memberId, Integer amount){
        memberGoldCoinRecordService.add(memberId, amount);
    }

    @GetMapping("/goldCoinDescList")
    @ApiOperation(value = "后台获取金币说明配置")
    public MemberGrowthConfig goldCoinDesc(String language) {
        if (StrUtil.isBlank(language)){
            language = LanguageEnum.app_zh_CN.getCode();
        }
        return memberGrowthConfigService.getGoldCoinDesc(language);

    }

    @PutMapping("/updateGoldCoinDesc.json")
    @ApiOperation(value = "后台修改金币说明配置")
    public void updateGoldCoinDesc(GoldCoinDescUpdateDTO dto) {
        if (StrUtil.isBlank(dto.getLanguage())){
            dto.setLanguage(LanguageEnum.app_zh_CN.getCode());
        }
        if (dto.getId()==null){
           throw new ServiceException("id不能为空！");
        }
        memberGrowthConfigService.updateGoldCoinDesc(dto);
    }
}
