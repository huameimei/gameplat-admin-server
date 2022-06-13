package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberLevelVO;
import com.gameplat.admin.model.vo.PayTypeVO;
import com.gameplat.admin.service.MemberLevelService;
import com.gameplat.admin.service.PayTypeService;
import com.gameplat.admin.service.RechargeConfigService;
import com.gameplat.base.common.util.EasyExcelUtil;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.recharge.RechargeConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Tag(name = "会员层级")
@Validated
@RestController
@RequestMapping("/api/admin/member/level")
public class MemberLevelController {

  @Autowired private MemberLevelService memberLevelService;

  @Autowired private RechargeConfigService rechargeConfigService;

  @Autowired private PayTypeService payTypeService;

  @Operation(summary = "获取会员层级列表")
  @GetMapping("/list")
  //  @PreAuthorize("hasAuthority('member:level:view')")
  public List<MemberLevelVO> getList() {
    return memberLevelService.getList();
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('member:level:add')")
  public void add(@RequestBody @Validated MemberLevelAddDTO dto) {
    memberLevelService.add(dto);
  }

  @Operation(summary = "修改")
  @PostMapping("/update")
  @PreAuthorize("hasAuthority('member:level:edit')")
  public void update(@RequestBody @Validated MemberLevelEditDTO dto) {
    memberLevelService.update(dto);
  }

  @Operation(summary = "删除")
  @PostMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('member:level:remove')")
  public void delete(@PathVariable Long id) {
    memberLevelService.delete(id);
  }

  @Operation(summary = "锁定层级")
  @PostMapping("/lock/{id}")
  @PreAuthorize("hasAuthority('member:level:lock')")
  public void lock(@PathVariable Long id) {
    memberLevelService.lock(id);
  }

  @Operation(summary = "解锁层级")
  @PostMapping("/unlock/{id}")
  @PreAuthorize("hasAuthority('member:level:unlock')")
  public void unlock(@PathVariable Long id) {
    memberLevelService.unlock(id);
  }

  @Operation(summary = "启用")
  @PostMapping("/enable/{id}")
  @PreAuthorize("hasAuthority('member:level:enable')")
  public void enable(@PathVariable Long id) {
    memberLevelService.enable(id);
  }

  @Operation(summary = "禁用")
  @PostMapping("/disable/{id}")
  @PreAuthorize("hasAuthority('member:level:disable')")
  public void disable(@PathVariable Long id) {
    memberLevelService.disable(id);
  }

  @Operation(summary = "启用提现")
  @PostMapping("/enable/withdraw/{id}")
  @PreAuthorize("hasAuthority('member:level:enableWithdraw')")
  public void enableWithdraw(@PathVariable Long id) {
    memberLevelService.enableWithdraw(id);
  }

  @Operation(summary = "禁用提现")
  @PostMapping("/disable/withdraw/{id}")
  @PreAuthorize("hasAuthority('member:level:disableWithdraw')")
  public void disableWithdraw(@PathVariable Long id) {
    memberLevelService.disableWithdraw(id);
  }

  @Operation(summary = "批量分层")
  @PostMapping("/batchAllocate")
  @PreAuthorize("hasAuthority('member:level:ocate')")
  public void batchAllocate(@Valid @RequestBody List<MemberLevelAllocateDTO> dtos) {
    memberLevelService.batchAllocate(dtos);
  }

  @Operation(summary = "根据输入账号分层")
  @PostMapping("/allocateByUserNames")
  @PreAuthorize("hasAuthority('member:level:allocateByUserNames')")
  public void allocateByUserNames(@Valid @RequestBody MemberLevelAllocateByUserNameDTO dto) {
    memberLevelService.allocateByUserNames(dto);
  }

  @Operation(summary = "根据上传文件分层")
  @PostMapping("/allocateByFile")
  @PreAuthorize("hasAuthority('member:level:allocateByFile')")
  public void allocateByFile(
      @RequestParam("levelValue") Integer levelValue, @RequestPart MultipartFile file)
      throws IOException {
    List<MemberLevelFileDTO> list =
        EasyExcelUtil.readExcel(file.getInputStream(), MemberLevelFileDTO.class);
    memberLevelService.allocateByFile(levelValue, list);
  }

  @Operation(summary = "根据筛选条件分层")
  @PostMapping("/allocateByCondition")
  @PreAuthorize("hasAuthority('member:level:allocateByCondition')")
  public void allocateByCondition(@Valid @RequestBody MemberLevelAllocateByConditionDTO dto) {
    memberLevelService.allocateByCondition(dto);
  }

  @Operation(summary = "获取全部会员层级")
  @GetMapping("/queryAll")
  @PreAuthorize("hasAuthority('member:level:queryAll')")
  public List<RechargeConfig> queryAll(Integer memberLevel) {
    return rechargeConfigService.queryAll(memberLevel, null, null);
  }

  @Operation(summary = "新增充值层级限制")
  @PostMapping("/saveRechargeConfig")
  @PreAuthorize("hasAuthority('member:level:saveRechargeConfig')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.MEMBER,
      desc = "'新增充值层级限制payType=' + #rechargeConfig.payType")
  public void add(RechargeConfig rechargeConfig) {
    rechargeConfigService.add(rechargeConfig);
  }

  @Operation(summary = "获取支付方式")
  @PostMapping("/findPayTypesList")
  //  @PreAuthorize("hasAuthority('thirdParty:payTypes:list')")
  public List<PayTypeVO> findPayTypes(String name) {
    return payTypeService.queryList(name);
  }
}
