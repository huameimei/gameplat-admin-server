package com.gameplat.admin.controller.open.member;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.GoldCoinDescUpdateDTO;
import com.gameplat.admin.model.dto.MemberGoldCoinRecordQueryDTO;
import com.gameplat.admin.model.vo.MemberGoldCoinRecordVO;
import com.gameplat.admin.service.MemberGoldCoinRecordService;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.member.MemberGoldCoinRecord;
import com.gameplat.model.entity.member.MemberGrowthConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


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

  @Autowired private MemberGoldCoinRecordService memberGoldCoinRecordService;

  @Autowired private MemberGrowthConfigService memberGrowthConfigService;

  @GetMapping("/page")
  @ApiOperation(value = "分页查询VIP金币明细")
  @PreAuthorize("hasAuthority('member:coin:page')")
  public IPage<MemberGoldCoinRecordVO> page(
      PageDTO<MemberGoldCoinRecord> page, MemberGoldCoinRecordQueryDTO dto) {
    return memberGoldCoinRecordService.page(page, dto);
  }

    /** 增 */
    @PostMapping("/add")
    @ApiOperation(value = "后台添加VIP金币明细")
    @PreAuthorize("hasAuthority('member:coin:add')")
    public void add(String account, Integer amount){
        log.info("系统增加金币：" + account + ",金币数：" + amount);
        if (amount == null) {
            throw new ServiceException("增加金币数量不能为空");
        }
        if (StringUtils.isBlank(account) ) {
            throw new ServiceException("玩家不能为空");
        }
        String[] accountArray = Convert.toStrArray(account);
        memberGoldCoinRecordService.addGoldCoin(accountArray, amount);
    }

    @ApiOperation("上传excel修改会员金币数量")
    @PostMapping(value = "/importAddGoldCoin")
    @PreAuthorize("hasAuthority('member:coin:import')")
    public void importAddGoldCoin(@RequestPart(value = "file",required = false) MultipartFile file) throws IOException {
        if (file ==null ) {
            throw new ServiceException("导入文件不能为空");
        }
//        try {
            memberGoldCoinRecordService.importAddGoldCoin(file);
//        }catch (Exception e){
//            e.printStackTrace();
//            log.info("批量上传异常：{}",e);
//            throw new ServiceException("批量导入玩家增加金币失败！");
//        }

    }

  @GetMapping("/goldCoinDescList")
  @ApiOperation(value = "后台获取金币说明配置")
  public MemberGrowthConfig goldCoinDesc() {
    return memberGrowthConfigService.getGoldCoinDesc();
  }

  @PutMapping("/updateGoldCoinDesc")
  @ApiOperation(value = "后台修改金币说明配置")
  public void updateGoldCoinDesc(GoldCoinDescUpdateDTO dto) {
    if (dto.getId() == null) {
      throw new ServiceException("id不能为空！");
    }
    memberGrowthConfigService.updateGoldCoinDesc(dto);
  }
}
