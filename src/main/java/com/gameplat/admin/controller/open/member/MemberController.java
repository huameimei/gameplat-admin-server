package com.gameplat.admin.controller.open.member;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberBalanceVO;
import com.gameplat.admin.model.vo.MemberContactVo;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.MemberVO;
import com.gameplat.admin.service.GameAdminService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.MemberTransferAgentService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.ip.IpAddressParser;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.base.common.util.ServletUtils;
import com.gameplat.common.constant.CacheKey;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.util.Convert;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.Member;
import com.gameplat.security.SecurityUserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Tag(name = "会员管理")
@RestController
@RequestMapping("/api/admin/member/")
@Log4j2
public class MemberController {

  @Autowired private MemberService memberService;

  @Autowired private MemberTransferAgentService memberTransferAgentService;

  @Autowired private GameAdminService gameAdminService;

  @Autowired
  private RedisTemplate redisTemplate;

  @CreateCache(name = CachedKeys.MEMBER_FUND_PWD_ERR_COUNT, expire = -1)
  private Cache<String, Integer> memberFundPwdErrCount;

  @Operation(summary = "会员列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('member:view')")
  public IPage<MemberVO> list(PageDTO<Member> page, MemberQueryDTO dto) {
    IPage<MemberVO> iPage = memberService.queryPage(page, dto);
    //重写玩家在线状态
    iPage.getRecords().forEach(m -> {
      if (m.getOnline()) {
        m.setOnline(redisTemplate.hasKey(CacheKey.getOnlineUserKey(m.getAccount())));
      }
    });
    return iPage;
  }

  @Operation(summary = "会员详情")
  @GetMapping("/info/{id}")
  @PreAuthorize("hasAuthority('member:info:view')")
  public MemberInfoVO info(@PathVariable Long id) {
    return memberService.getMemberInfo(id);
  }


  @Operation(summary = "会员详情")
  @GetMapping("/dateils/{id}")
  @PreAuthorize("hasAuthority('member:info:view')")
  public MemberInfoVO getMemberDateils(@PathVariable Long id) {
    return memberService.getMemberDateils(id);
  }

  @Operation(summary = "会员联系方式")
  @GetMapping("/contact/{id}")
  @PreAuthorize("hasAuthority('member:contact:view')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'会员管理-->会员联系方式' + #id" )
  public MemberContactVo memberAccount(@PathVariable Long id) {
    MemberContactVo detail = memberService.getMemberDetail(id);
    detail.setRealName("");
    return detail;
  }

  @Operation(summary = "会员详情")
  @GetMapping("/getAccount")
  public MemberInfoVO memberInfo(@RequestParam String account) {
    return memberService.getMemberInfo(account);
  }

  @Operation(summary = "添加会员")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('member:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'添加会员'")
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

  @Operation(summary = "编辑会员")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('member:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'编辑会员信息'")
  public void update(@Validated @RequestBody MemberEditDTO dto) {
    memberService.update(dto);
  }

  @Operation(summary = "启用会员")
  @PostMapping("/enable")
  @PreAuthorize("hasAuthority('member:enable')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'启用会员'")
  public void enable(@RequestBody List<Long> ids) {
    memberService.enable(ids);
  }

  @Operation(summary = "禁用会员")
  @PostMapping("/disable")
  @PreAuthorize("hasAuthority('member:disable')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'禁用会员'")
  public void disable(@RequestBody List<Long> ids) {
    memberService.disable(ids);
  }

  @Operation(summary = "清空联系方式")
  @PostMapping("/clearContact")
  @PreAuthorize("hasAuthority('member:clearContact')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'清空联系方式'")
  public void clearContact(@RequestBody MemberContactCleanDTO dto) {
    memberService.clearContact(dto);
  }

  @Operation(summary = "转代理")
  @PostMapping("/transform")
  @PreAuthorize("hasAuthority('member:transform')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'转代理'")
  public void transform(@RequestBody MemberTransformDTO dto) {
    memberTransferAgentService.transform(dto);
  }

  @Operation(summary = "恢复转代理数据")
  @PostMapping("/recover/transform/{serialNo}")
  @PreAuthorize("hasAuthority('member:recoverTransform')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'恢复转代理数据'")
  public void recoverTransform(@PathVariable String serialNo) {
    //    memberTransferAgentService.recover(serialNo);
  }

  @Operation(summary = "根据账号获取会员信息")
  @PostMapping("/getByAccount/{account}")
  public void getByAccount(@PathVariable String account) {
    memberService.getByAccount(account).orElseThrow(() -> new ServiceException("'账号信息不存在！'"));
  }

  @Operation(summary = "更新会员联系方式")
  @PostMapping("/updateContact")
  @PreAuthorize("hasAuthority('member:updateContact')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'修改会员'+#{dto.id}+'联系方式'")
  public void updateContact(@Validated @RequestBody MemberContactUpdateDTO dto) {
    memberService.updateContact(dto);
  }

  @Operation(summary = "重置会员登录密码")
  @PostMapping("/resetPassword")
  @PreAuthorize("hasAuthority('member:resetPassword')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'重置会员'+#{dto.id}+'登录密码'")
  public void resetPassword(@Validated @RequestBody MemberPwdUpdateDTO dto) {
    memberService.resetPassword(dto);
  }

  @Operation(summary = "重置会员提现密码")
  @PostMapping("/resetWithdrawPassword")
  @PreAuthorize("hasAuthority('member:resetWithdrawPassword')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'重置会员'+#{dto.id}+'提现密码'")
  public void resetWithdrawPassword(@Validated @RequestBody MemberWithdrawPwdUpdateDTO dto) {
    memberService.resetWithdrawPassword(dto);
  }

  @Operation(summary = "重置会员真实姓名")
  @PostMapping("/resetRealName")
  @PreAuthorize("hasAuthority('member:resetRealName')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'重置会员'+#{dto.id}+'真实姓名'")
  public void resetRealName(@Validated @RequestBody MemberResetRealNameDTO dto) {
    memberService.resetRealName(dto);
  }

  @Operation(summary = "修改会员提现状态")
  @PostMapping("/changeWithdrawFlag/{id}/{flag}")
  @PreAuthorize("hasAuthority('member:changeWithdrawFlag')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'修改会员提现状态'")
  public void changeWithdrawFlag(@PathVariable Long id, @PathVariable String flag) {
    memberService.changeWithdrawFlag(id, flag);
  }

  @Operation(summary = "导出会员列表")
  @PreAuthorize("hasAuthority('member:export')")
  @GetMapping(value = "/exportList")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'导出会员列表'")
  public void exportList(MemberQueryDTO dto, HttpServletResponse response) {
    memberService.exportMembersReport(dto, response);
    //    ExportParams exportParams = new ExportParams("会员账号列表导出", "会员账号列表");
    //    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = myExcel.xls");
    //
    //    try (Workbook workbook = ExcelExportUtil.exportExcel(exportParams, MemberVO.class,
    // member)) {
    //      workbook.write(response.getOutputStream());
    //    }
  }

  @Operation(summary = "添加用户或添加下级时 彩票投注返点数据集")
  @GetMapping("/getRebateOptionsForAdd")
  public List<Map<String, String>> getRebateOptionsForAdd(
      @RequestParam(required = false) String agentAccount) {
    return memberService.getRebateForAdd(agentAccount);
  }

  @Operation(summary = "编辑用户时 彩票投注返点数据集")
  @GetMapping("/getRebateOptionsForEdit")
  public List<Map<String, String>> getRebateOptionsForEdit(
      @RequestParam(required = false) String agentAccount) {
    return memberService.getRebateForEdit(agentAccount);
  }

  @Operation(summary = "批量更改日工资")
  @PostMapping("/updateDaySalary")
  @PreAuthorize("hasAuthority('member:updateDaySalary')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'修改会员工资状态'")
  public void updateDaySalary(
      @RequestParam(required = true) String ids, @RequestParam Integer state) {
    memberService.updateDaySalary(ids, state);
  }

  @GetMapping("findMemberBalance")
  public MemberBalanceVO findMemberBalance(@RequestParam(value = "account") String account) {
    gameAdminService.recyclingAmountByAccount(account);
    return BeanUtils.map(memberService.getMemberInfo(account), MemberBalanceVO.class);
  }

  @GetMapping("findPromoteMemberBalance")
  @Operation(summary = "返回推广会员")
  public IPage<MemberBalanceVO> findPromoteMemberBalance(PageDTO<Member> page, MemberQueryDTO dto) {
    return memberService.findPromoteMemberBalance(page, dto);
  }

  @Operation(summary = "清除推广会员余额")
  @PostMapping("clearPromoteMemberBalance")
  @PreAuthorize("hasAuthority('system:member:clearPromoteMemberBalance')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.MEMBER,
      desc = "'清除推广会员'+#{dto.userNames}+'余额'")
  public void clearPromoteMemberBalance(@RequestBody CleanAccountDTO dto) {
    Assert.notNull(dto.getIsCleanAll(), "是否清理全部不能为空！");
    Assert.notNull(dto.getUserType(), "会员类型不能为空！");
    // 如果不是清理全部
    if (ObjectUtil.equals(dto.getIsCleanAll(), 0)) {
      Assert.notNull(dto.getUserNames(), "会员不能为空！");
    }
    memberService.clearPromoteMemberBalance(dto);
  }

  @Operation(summary = "解除登录限制")
  @PostMapping("releaseLoginLimit/{id}")
  @PreAuthorize("hasAuthority('system:member:releaseLoginLimit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'解除会员'+#{id}+'登录限制'")
  public void releaseLoginLimit(@PathVariable Long id) {
    memberService.releaseLoginLimit(id);
  }

  @Operation(summary = "查看会员真实资料")
  @GetMapping("getMemberDetail/{id}")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'查看会员真实资料'+#{id}")
  @PreAuthorize("hasAuthority('system:member:contact:detail')")
  public MemberContactVo getMemberDetail(@PathVariable(required = true) long id) {
    return memberService.getMemberDetail(id);
  }


  @Operation(summary = "解除提现密码限制")
  @PostMapping("releaseWithLimit/{id}")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'解除提现密码限制'+#{id}")
  @PreAuthorize("hasAuthority('system:member:releaseWithLimit')")
  public void releaseWithLimit(@PathVariable(required = true) long id) {
    log.info("操作人：{}", SecurityUserHolder.getUsername());
    boolean remove = memberFundPwdErrCount.remove(Convert.toStr(id));
    log.info("解除提现密码限制：{}", remove);
  }


  @Operation(summary = "批量转层级")
  @PostMapping("batchLevel")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'批量转层级'+#{dto.level}")
  @PreAuthorize("hasAuthority('system:member:batchLevel')")
  public void batchLevel(@RequestBody MemberLevelBatchDTO dto) {
    log.info("批量操作人：{}", SecurityUserHolder.getUsername());
    memberService.batchLevel(dto);
  }

  @Operation(summary = "会员转成代理")
  @PostMapping("/changeToAgent")
  @PreAuthorize("hasAuthority('member:changeToAgent')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'会员转变成代理'")
  public void changeToAgent(@RequestBody MemberTransformDTO dto) {
    memberTransferAgentService.changeToAgent(dto.getId());
  }

  @Operation(summary = "会员解除设备限制")
  @PostMapping("/memberDevice")
  @PreAuthorize("hasAuthority('member:memberDevice')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'会员解除设备限制'")
  public void memberDevice(@RequestParam(required = true) String username) {
    memberTransferAgentService.memberDevice(username);
  }
}
