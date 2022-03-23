package com.gameplat.admin.controller.open.member;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.CleanAccountDTO;
import com.gameplat.admin.model.dto.MemberAddDTO;
import com.gameplat.admin.model.dto.MemberContactCleanDTO;
import com.gameplat.admin.model.dto.MemberContactUpdateDTO;
import com.gameplat.admin.model.dto.MemberEditDTO;
import com.gameplat.admin.model.dto.MemberPwdUpdateDTO;
import com.gameplat.admin.model.dto.MemberQueryDTO;
import com.gameplat.admin.model.dto.MemberResetRealNameDTO;
import com.gameplat.admin.model.dto.MemberTransformDTO;
import com.gameplat.admin.model.dto.MemberWithdrawPwdUpdateDTO;
import com.gameplat.admin.model.vo.MemberBalanceVO;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.MemberVO;
import com.gameplat.admin.service.GameAdminService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.MemberTransformService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.ip.IpAddressParser;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.base.common.util.ServletUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.lang.Assert;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "会员管理")
@RestController
@RequestMapping("/api/admin/member/")
public class MemberController {

  @Autowired private MemberService memberService;

  @Autowired private MemberTransformService memberTransformService;

  @Autowired(required = false)
  private GameAdminService gameAdminService;

  @ApiOperation(value = "会员列表")
  @GetMapping("/list")
  public IPage<MemberVO> list(PageDTO<Member> page, MemberQueryDTO dto) {
    return memberService.queryPage(page, dto);
  }

  @ApiOperation(value = "会员详情")
  @GetMapping("/info/{id}")
  public MemberInfoVO info(@PathVariable Long id) {
    return memberService.getInfo(id);
  }

  @ApiOperation(value = "会员详情")
  @GetMapping("/getAccount")
  public MemberInfoVO memberInfo(@RequestParam String account) {
    return memberService.getMemberInfo(account);
  }

  @ApiOperation(value = "添加会员")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('member:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "添加会员")
  public void add(@Validated @RequestBody MemberAddDTO dto, HttpServletRequest request) {
    String userAgentHeader = request.getHeader(Header.USER_AGENT.getValue());
    UserAgent userAgent = UserAgentUtil.parse(userAgentHeader);

    dto.setRegisterOs(userAgent.getOs().getName());
    dto.setRegisterBrowser(userAgent.getBrowser().getName().concat(userAgent.getEngineVersion()));

    dto.setRegisterUserAgent(userAgentHeader);
    dto.setRegisterIp(IpAddressParser.getIpAddress(request));
    dto.setRegisterHost(ServletUtils.getRequestDomain(request));

    memberService.add(dto);
  }

  @ApiOperation(value = "编辑会员")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('member:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "编辑会员信息")
  public void update(@Validated @RequestBody MemberEditDTO dto) {
    memberService.update(dto);
  }

  @ApiOperation(value = "启用会员")
  @PutMapping("/enable")
  @PreAuthorize("hasAuthority('member:enable')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "启用会员")
  public void enable(@RequestBody List<Long> ids) {
    memberService.enable(ids);
  }

  @ApiOperation(value = "禁用会员")
  @PutMapping("/disable")
  @PreAuthorize("hasAuthority('member:disable')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "禁用会员")
  public void disable(@RequestBody List<Long> ids) {
    memberService.disable(ids);
  }

  @ApiOperation("清空联系方式")
  @PostMapping("/clearContact")
  @PreAuthorize("hasAuthority('member:clearContact')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "清空联系方式")
  public void clearContact(@RequestBody MemberContactCleanDTO dto) {
    memberService.clearContact(dto);
  }

  @ApiOperation("转代理")
  @PostMapping("/transform")
  @PreAuthorize("hasAuthority('member:transform')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "转代理")
  public void transform(@RequestBody MemberTransformDTO dto) {
    memberTransformService.transform(dto);
  }

  @ApiOperation("恢复转代理数据")
  @PostMapping("/recover/transform/{serialNo}")
  @PreAuthorize("hasAuthority('member:recoverTransform')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "恢复转代理数据")
  public void recoverTransform(@PathVariable String serialNo) {
    memberTransformService.recover(serialNo);
  }

  @ApiOperation("根据账号获取会员信息")
  @PostMapping("/getByAccount/{account}")
  public void getByAccount(@PathVariable String account) {
    memberService.getByAccount(account).orElseThrow(() -> new ServiceException("账号信息不存在！"));
  }

  @ApiOperation("更新会员联系方式")
  @PutMapping("/updateContact")
  @PreAuthorize("hasAuthority('member:updateContact')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "修改会员#{dto.id}联系方式")
  public void updateContact(@Validated @RequestBody MemberContactUpdateDTO dto) {
    memberService.updateContact(dto);
  }

  @ApiOperation("重置会员登录密码")
  @PutMapping("/resetPassword")
  @PreAuthorize("hasAuthority('member:resetPassword')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "重置会员#{dto.id}登录密码")
  public void resetPassword(@Validated @RequestBody MemberPwdUpdateDTO dto) {
    memberService.resetPassword(dto);
  }

  @ApiOperation("重置会员提现密码")
  @PutMapping("/resetWithdrawPassword")
  @PreAuthorize("hasAuthority('member:resetWithdrawPassword')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "重置会员#{dto.id}提现密码")
  public void resetWithdrawPassword(@Validated @RequestBody MemberWithdrawPwdUpdateDTO dto) {
    memberService.resetWithdrawPassword(dto);
  }

  @ApiOperation("重置会员真实姓名")
  @PutMapping("/resetRealName")
  @PreAuthorize("hasAuthority('member:resetRealName')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "重置会员#{dto.id}真实姓名")
  public void resetRealName(@Validated @RequestBody MemberResetRealNameDTO dto) {
    memberService.resetRealName(dto);
  }

  @ApiOperation("修改会员提现状态")
  @PutMapping("/changeWithdrawFlag/{id}/{flag}")
  @PreAuthorize("hasAuthority('member:changeWithdrawFlag')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "修改会员提现状态")
  public void changeWithdrawFlag(@PathVariable Long id, @PathVariable String flag) {
    memberService.changeWithdrawFlag(id, flag);
  }

  @SneakyThrows
  @ApiOperation(value = "导出会员列表")
  @PreAuthorize("hasAuthority('member:export')")
  @GetMapping(value = "/exportList", produces = "application/vnd.ms-excel")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "导出会员列表")
  public void exportList(MemberQueryDTO dto, HttpServletResponse response) {
    List<MemberVO> member = memberService.queryList(dto);
    ExportParams exportParams = new ExportParams("会员账号列表导出", "会员账号列表");
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = myExcel.xls");

    try (Workbook workbook = ExcelExportUtil.exportExcel(exportParams, MemberVO.class, member)) {
      workbook.write(response.getOutputStream());
    }
  }

  @ApiOperation(value = "添加用户或添加下级时 彩票投注返点数据集")
  @GetMapping("/getRebateOptionsForAdd")
  public List<Map<String, String>> getRebateOptionsForAdd(
      @RequestParam(required = false) String agentAccount) {
    return memberService.getRebateForAdd(agentAccount);
  }

  @ApiOperation(value = "编辑用户时 彩票投注返点数据集")
  @GetMapping("/getRebateOptionsForEdit")
  public List<Map<String, String>> getRebateOptionsForEdit(
      @RequestParam(required = false) String agentAccount) {
    return memberService.getRebateForEdit(agentAccount);
  }

  /**
   * @param ids 会员id
   * @param state 状态
   */
  @ApiOperation(value = "批量更改日工资")
  @PostMapping("/updateDaySalary")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "修改会员工资状态")
  public void updateDaySalary(
      @RequestParam(required = true) String ids, @RequestParam(required = true) Integer state) {
    memberService.updateDaySalary(ids, state);
  }

  /**
   * 人工扣款查询会员
   *
   * @param account 用户名
   */
  @GetMapping("findMemberBalance")
  public MemberBalanceVO findMemberBalance(
      @RequestParam(value = "account", required = true) String account) {
    // 额度回收
    gameAdminService.recyclingAmountByAccount(account);
    return BeanUtils.map(memberService.getMemberInfo(account), MemberBalanceVO.class);
  }

  /**
   * 返回推广会员
   *
   * @param page
   * @param dto
   * @return
   */
  @GetMapping("findTGMemberBalance")
  @ApiOperation("返回推广会员")
  public IPage<MemberBalanceVO> findTGMemberBalance(PageDTO<Member> page, MemberQueryDTO dto) {
    dto.setUserType("P");
    return memberService.findTGMemberBalance(page, dto);
  }

  /**
   * @param dto 推广会员账号
   */
  @ApiOperation("清除推广会员余额")
  @PostMapping("findTGMemberBalance")
  @PreAuthorize("hasAuthority('system:TGMember:clear')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "清除推广会员#{dto.userNames}余额")
  public void updateTGClearMember(@RequestBody CleanAccountDTO dto) {
    Assert.notNull(dto.getIsCleanAll(), "是否清理全部不能为空！");
    Assert.notNull(dto.getUserType(), "会员类型不能为空！");
    // 如果不是清理全部
    if (ObjectUtil.equals(dto.getIsCleanAll(), 0)) {
      Assert.notNull(dto.getUserNames(), "会员不能为空！");
    }
    memberService.updateTGClearMember(dto);
  }
}
