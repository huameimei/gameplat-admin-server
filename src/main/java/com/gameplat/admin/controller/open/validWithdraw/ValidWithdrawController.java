package com.gameplat.admin.controller.open.validwithdraw;

import cn.hutool.core.util.ObjectUtil;
import com.gameplat.admin.model.dto.ValidWithdrawDto;
import com.gameplat.admin.model.vo.ValidateDmlBeanVo;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.admin.service.ValidWithdrawService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.model.bean.limit.MemberWithdrawLimit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author kb @Date 2022/2/20 14:54 @Version 1.0
 */
@Tag(name = "打码量")
@RestController
@RequestMapping("/api/admin/validWithdraw")
@RequiredArgsConstructor
public class ValidWithdrawController {

  private final ValidWithdrawService validWithdrawService;

  private final LimitInfoService limitInfoService;

  @Operation(summary = "查询")
  @GetMapping(value = "findVaildWithdraw")
  @PreAuthorize("hasAuthority('funds:validWithdraw:view')")
  public ValidateDmlBeanVo findVaildWithdraw(@RequestParam("username") String name) {
    if (StringUtils.isEmpty(name)) {
      throw new ServiceException("用户名不能为空！");
    }
    MemberWithdrawLimit memberWithdrawLimit =
        limitInfoService.get(LimitEnums.MEMBER_WITHDRAW_LIMIT);
    return validWithdrawService.validateByMemberId(memberWithdrawLimit, name, true);
  }

  @Operation(summary = "调整单条打码量记录")
  @PreAuthorize("hasAuthority('funds:validWithdraw:edit')")
  @PostMapping("/updateValidWithdraw")
  public void updateValidWithdraw(@Validated @RequestBody ValidWithdrawDto dto) {
    validWithdrawService.updateValidWithdraw(dto);
  }

  @Operation(summary = "调整会员打码量")
  @PreAuthorize("hasAuthority('funds:validWithdraw:operate')")
  @PostMapping("/operateValidWithdraw")
  public void operateValidWithdraw(@Validated @RequestBody ValidWithdrawDto dto) {
    if (StringUtils.isBlank(dto.getUsername())) {
      throw new ServiceException("用户名不能为空！");
    }
    if (ObjectUtils.isEmpty(dto.getMormDml())) {
      throw new ServiceException("调整打码量不能为空！");
    }
    validWithdrawService.operateValidWithdraw(dto);
  }

  @SneakyThrows
  @Operation(summary = "清除会员打码量记录")
  @PostMapping("/delValidWithdraw")
  @PreAuthorize("hasAuthority('funds:validWithdraw:remove')")
  public void delValidWithdraw(@RequestParam("member") String member) {
    if (StringUtils.isEmpty(member)) {
      throw new ServiceException("会员账号不能空！");
    }
    validWithdrawService.delValidWithdraw(member);
  }
}
