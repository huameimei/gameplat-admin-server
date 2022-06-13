package com.gameplat.admin.controller.open.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.service.MemberYubaoService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.MemberYubaoInterest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/admin/finance/yubao")
public class YuBaoController {

    @Resource
    private MemberYubaoService memberYubaoService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('finance:yubao:list')")
    public IPage<MemberYubaoInterest> list(String account, String startDate, String endDate, PageDTO<MemberYubaoInterest> page) {
        return memberYubaoService.queryYubaoInterest(account, startDate, endDate, page);
    }

    /**
     * 结算余额宝
     */
    @RequestMapping(value = "/settle", method = RequestMethod.GET)
    public void settle() {
        memberYubaoService.settle();
    }

    /**
     * 回收用户余额宝
     */
    @RequestMapping(value = "/recycle", method = RequestMethod.POST)
    @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'回收余额宝，会员账号:' + #account + '，回收金额：'+ #money + '元'")
    @PreAuthorize("hasAuthority('finance:yubao:recycle')")
    public void recycle(String account,Long memberId, Double money) {
        memberYubaoService.recycle(account,memberId, money);
    }
}
