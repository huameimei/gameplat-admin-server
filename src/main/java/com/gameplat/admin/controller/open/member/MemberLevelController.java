package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.MemberLevelAddDTO;
import com.gameplat.admin.model.dto.MemberLevelAllocateByConditionDTO;
import com.gameplat.admin.model.dto.MemberLevelAllocateByUserNameDTO;
import com.gameplat.admin.model.dto.MemberLevelAllocateDTO;
import com.gameplat.admin.model.dto.MemberLevelEditDTO;
import com.gameplat.admin.model.dto.MemberLevelFileDTO;
import com.gameplat.admin.model.vo.MemberLevelVO;
import com.gameplat.admin.service.MemberLevelService;
import com.gameplat.admin.service.RechargeConfigService;
import com.gameplat.base.common.util.EasyExcelUtil;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.recharge.RechargeConfig;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/admin/member/level")
public class MemberLevelController {

  @Autowired private MemberLevelService memberLevelService;

  @Autowired private RechargeConfigService rechargeConfigService;

  @GetMapping("/list")
  public List<MemberLevelVO> getList() {
    return memberLevelService.getList();
  }

  @PostMapping("/add")
  public void add(@RequestBody @Validated MemberLevelAddDTO dto) {
    memberLevelService.add(dto);
  }

  @PutMapping("/update")
  public void update(@RequestBody @Validated MemberLevelEditDTO dto) {
    memberLevelService.update(dto);
  }

  @DeleteMapping("/delete/{id}")
  public void delete(@PathVariable Long id) {
    memberLevelService.delete(id);
  }

  @PostMapping("/lock/{id}")
  public void lock(@PathVariable Long id) {
    memberLevelService.lock(id);
  }

  @PostMapping("/unlock/{id}")
  public void unlock(@PathVariable Long id) {
    memberLevelService.unlock(id);
  }

  @PostMapping("/enable/{id}")
  public void enable(@PathVariable Long id) {
    memberLevelService.enable(id);
  }

  @PostMapping("/disable/{id}")
  public void disable(@PathVariable Long id) {
    memberLevelService.disable(id);
  }

  @PostMapping("/enable/withdraw/{id}")
  public void enableWithdraw(@PathVariable Long id) {
    memberLevelService.enableWithdraw(id);
  }

  @PostMapping("/disable/withdraw/{id}")
  public void disableWithdraw(@PathVariable Long id) {
    memberLevelService.disableWithdraw(id);
  }

  @PostMapping("/batchAllocate")
  public void batchAllocate(@Valid @RequestBody List<MemberLevelAllocateDTO> dtos) {
    memberLevelService.batchAllocate(dtos);
  }

  @ApiOperation(value = "根据输入账号分层")
  @PostMapping("/allocateByUserNames")
  public void allocateByUserNames(@Valid @RequestBody MemberLevelAllocateByUserNameDTO dto) {
    memberLevelService.allocateByUserNames(dto);
  }

  @ApiOperation(value = "根据上传文件分层")
  @PostMapping("/allocateByFile")
  public void allocateByFile(
      @RequestParam("levelValue") Integer levelValue, @RequestPart MultipartFile file)
      throws IOException {
    List<MemberLevelFileDTO> list =
        EasyExcelUtil.readExcel(file.getInputStream(), MemberLevelFileDTO.class);
    memberLevelService.allocateByFile(levelValue, list);
  }

  @ApiOperation(value = "根据筛选条件分层")
  @PostMapping("/allocateByCondition")
  public void allocateByCondition(@Valid @RequestBody MemberLevelAllocateByConditionDTO dto) {
    memberLevelService.allocateByCondition(dto);
  }

  @GetMapping("/queryAll")
  @PreAuthorize("hasAuthority('member:level:queryAll')")
  public List<RechargeConfig> queryAll(Integer memberLevel) {
    return rechargeConfigService.queryAll(memberLevel);
  }

  @PostMapping("/saveRechargeConfig")
  @PreAuthorize("hasAuthority('member:level:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.MEMBER,
      desc = "'新增充值层级限制payType=' + #rechargeConfig.payType")
  public void add(RechargeConfig rechargeConfig) {
    rechargeConfigService.add(rechargeConfig);
  }
}
