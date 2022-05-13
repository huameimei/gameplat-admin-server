package com.gameplat.admin.controller.open.validwithdraw;

import com.gameplat.admin.model.dto.ValidWithdrawDto;
import com.gameplat.admin.model.vo.ValidateDmlBeanVo;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.admin.service.ValidWithdrawService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.model.bean.limit.MemberWithdrawLimit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author kb @Date 2022/2/20 14:54 @Version 1.0
 */
@Api(tags = "打码量")
@Slf4j
@RestController
@RequestMapping("/api/admin/validWithdraw")
@RequiredArgsConstructor
public class ValidWithdrawController {

  private final ValidWithdrawService validWithdrawService;

  private final LimitInfoService limitInfoService;

  @ApiOperation("查询")
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

  @ApiOperation(value = "调整单条打码量记录")
  @PreAuthorize("hasAuthority('funds:validWithdraw:edit')")
  @PostMapping("/updateValidWithdraw")
  public void updateValidWithdraw(@Validated @RequestBody ValidWithdrawDto dto) {
    validWithdrawService.updateValidWithdraw(dto);
  }

  @SneakyThrows
  @ApiOperation(value = "清除会员打码量记录")
  @PostMapping("/delValidWithdraw")
  @PreAuthorize("hasAuthority('funds:validWithdraw:remove')")
  public void delValidWithdraw(@RequestParam("member") String member) {
    if (StringUtils.isEmpty(member)) {
      throw new ServiceException("会员账号不能空！");
    }
    validWithdrawService.delValidWithdraw(member);
  }
}
