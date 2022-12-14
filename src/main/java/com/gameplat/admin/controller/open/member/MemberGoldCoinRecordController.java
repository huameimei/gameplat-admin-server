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
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.MemberGoldCoinRecord;
import com.gameplat.model.entity.member.MemberGrowthConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * VIP金币
 *
 * @author lily
 */
@Tag(name = "VIP金币明细")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/coin")
public class MemberGoldCoinRecordController {

  @Autowired private MemberGoldCoinRecordService memberGoldCoinRecordService;

  @Autowired private MemberGrowthConfigService memberGrowthConfigService;

  @GetMapping("/page")
  @Operation(summary = "分页查询VIP金币明细")
  @PreAuthorize("hasAuthority('member:coin:view')")
  public IPage<MemberGoldCoinRecordVO> page(
      PageDTO<MemberGoldCoinRecord> page, MemberGoldCoinRecordQueryDTO dto) {
    return memberGoldCoinRecordService.page(page, dto);
  }

  /** 增 */
  @PostMapping("/add")
  @Operation(summary = "后台添加VIP金币明细")
  @PreAuthorize("hasAuthority('member:coin:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'VIP金币明细-->后台添加VIP金币明细account:' + #account + 'amount:' + #amount" )
  public void add(String account, Integer amount) {
    log.info("系统增加金币：" + account + ",金币数：" + amount);
    if (amount == null) {
      throw new ServiceException("增加金币数量不能为空");
    }
    if (StringUtils.isBlank(account)) {
      throw new ServiceException("玩家不能为空");
    }
    String[] accountArray = Convert.toStrArray(account);
    memberGoldCoinRecordService.addGoldCoin(accountArray, amount);
  }

  @Operation(summary = "上传文件修改会员金币数量")
  @SneakyThrows
  @PostMapping("/importAddGoldCoin")
  @PreAuthorize("hasAuthority('member:coin:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'VIP金币明细-->上传文件修改会员金币数量:'" )
  public void importAddGoldCoin(@RequestPart(value = "file", required = false) MultipartFile file) {
    if (file == null) {
      throw new ServiceException("导入文件不能为空");
    }

    memberGoldCoinRecordService.importAddGoldCoin(file);
  }

  @GetMapping("/goldCoinDescList")
  @Operation(summary = "后台获取金币说明配置")
  public MemberGrowthConfig goldCoinDesc() {
    return memberGrowthConfigService.getGoldCoinDesc();
  }

  @PostMapping("/updateGoldCoinDesc")
  @Operation(summary = "后台修改金币说明配置")
  @PreAuthorize("hasAuthority('member:coin:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'VIP金币明细-->后台修改金币说明配置:' + #dto" )
  public void updateGoldCoinDesc(GoldCoinDescUpdateDTO dto) {
    if (dto.getId() == null) {
      throw new ServiceException("id不能为空！");
    }
    memberGrowthConfigService.updateGoldCoinDesc(dto);
  }
}
