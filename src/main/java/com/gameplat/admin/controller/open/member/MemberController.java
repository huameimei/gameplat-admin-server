package com.gameplat.admin.controller.open.member;

import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.dto.MemberAddDTO;
import com.gameplat.admin.model.dto.MemberContactCleanDTO;
import com.gameplat.admin.model.dto.MemberContactUpdateDTO;
import com.gameplat.admin.model.dto.MemberEditDTO;
import com.gameplat.admin.model.dto.MemberQueryDTO;
import com.gameplat.admin.model.dto.MemberTransformDTO;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.MemberVO;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.MemberTransformService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.ip.IpAddressParser;
import com.gameplat.base.common.util.ServletUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/member/")
public class MemberController {

  @Autowired private MemberService memberService;

  @Autowired private MemberTransformService memberTransformService;

  @GetMapping("/list")
  public IPage<MemberVO> list(PageDTO<Member> page, MemberQueryDTO dto) {
    return memberService.queryPage(page, dto);
  }

  @GetMapping("/info/{id}")
  public MemberInfoVO info(@PathVariable Long id) {
    return memberService.getInfo(id);
  }

  @GetMapping("/getAccount")
  public MemberInfoVO memberInfo(@RequestParam String account) {
    return memberService.getMemberInfo(account);
  }

  @PostMapping("/add")
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

  @PutMapping("/edit")
  public void update(@Validated @RequestBody MemberEditDTO dto) {
    memberService.update(dto);
  }

  @PutMapping("/enable")
  public void enable(@RequestBody List<Long> ids) {
    memberService.enable(ids);
  }

  @PutMapping("/disable")
  public void disable(@RequestBody List<Long> ids) {
    memberService.disable(ids);
  }

  @PostMapping("/clearContact")
  public void clearContact(@RequestBody MemberContactCleanDTO dto) {
    memberService.clearContact(dto);
  }

  @PostMapping("/transform")
  public void transform(@RequestBody MemberTransformDTO dto) {
    memberTransformService.transform(dto);
  }

  @PostMapping("/recover/transform/{serialNo}")
  public void recoverTransform(@PathVariable String serialNo) {
    memberTransformService.recover(serialNo);
  }

  @PostMapping("/getByAccount/{account}")
  public void getByAccount(@PathVariable String account) {
    memberService.getByAccount(account).orElseThrow(() -> new ServiceException("账号信息不存在！"));
  }

  @PutMapping("/updateContact")
  public void updateContact(@Validated @RequestBody MemberContactUpdateDTO dto) {
    memberService.updateContact(dto);
  }
}
