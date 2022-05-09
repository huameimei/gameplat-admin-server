package com.gameplat.admin.controller.open.payment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.bean.UserWithdrawLimitInfo;
import com.gameplat.admin.model.vo.MemberWithdrawDictDataVo;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.sys.SysDictData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "会员提现限制")
@RestController
@RequestMapping("/api/admin/thirdParty/userWithdrawLimit")
public class MemberWithdrawLimitController {

  @Autowired private SysDictDataService dictDataService;

  @ApiOperation("删除")
  @DeleteMapping("/remove/{timesForWithdrawal}")
  @PreAuthorize("hasAuthority('thirdParty:memberWithdrawLimit:remove')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'删除提现次数限制:' + #timesForWithdrawal")
  public void remove(@PathVariable Integer timesForWithdrawal) {
    dictDataService.delete(
        DictTypeEnum.USER_WITHDRAW_LIMIT.getValue(),
        DictDataEnum.WITHDRAW_LIMIT.getLabel() + timesForWithdrawal);
  }

  @ApiOperation("添加/修改")
  @PostMapping("/addOrEdit")
  @PreAuthorize("hasAuthority('thirdParty:memberWithdrawLimit:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "新增或修改提现次数限制")
  public void addOrEdit(@RequestBody UserWithdrawLimitInfo limitInfo) {
    String dictType = DictTypeEnum.USER_WITHDRAW_LIMIT.getValue();
    String dictLabel = DictDataEnum.WITHDRAW_LIMIT.getLabel() + limitInfo.getTimesForWithdrawal();
    dictDataService.addOrUpdateUserWithdrawLimit(dictType, dictLabel, limitInfo);
  }

  @ApiOperation("查询")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:memberWithdrawLimit:view')")
  public IPage<MemberWithdrawDictDataVo> queryPage(Page<SysDictData> page) {
    return dictDataService.queryWithdrawPage(page);
  }
}
