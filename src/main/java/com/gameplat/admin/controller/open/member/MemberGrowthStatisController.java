package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.mapper.MemberInfoMapper;
import com.gameplat.admin.model.dto.MemberGrowthStatisDTO;
import com.gameplat.admin.model.vo.MemberGrowthStatisVO;
import com.gameplat.admin.model.vo.TestVO;
import com.gameplat.admin.service.MemberGrowthRecordService;
import com.gameplat.admin.service.MemberGrowthStatisService;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberGrowthStatis;
import com.gameplat.model.entity.member.MemberInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author lily
 * @description
 * @date 2022/3/25
 */


@Api(tags = "VIP成长值汇总数据")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/growthStatis")
public class MemberGrowthStatisController {

    @Autowired
    private MemberGrowthStatisService memberGrowthStatisService;

    @Autowired
    private MemberInfoMapper memberInfoMapper;

    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private MemberGrowthRecordService memberGrowthRecordService;

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "查询")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('member:growthStatis:get')")
    public IPage<MemberGrowthStatisVO> page(PageDTO<MemberGrowthStatis> page, MemberGrowthStatisDTO dto) {
        return memberGrowthStatisService.findStatisList(page, dto);
    }

    @GetMapping("/test")
    public void test() {

        List<TestVO> list = memberInfoMapper.getTest(null, null, null);

        for (TestVO memberInfo : list) {

            Long memberId = memberInfo.getMemberId();
            Member member = memberService.getById(memberInfo.getMemberId());
            MemberInfo one = memberInfoService.lambdaQuery().eq(MemberInfo::getMemberId, memberId).one();


            Long signGrowth = 0L;
            Long rechargeGrowth = 0L;
            Long damaGrowth = 0L;
            Long backGrowth = 0L;
            Long demoteGrowth = 0L;

            List<TestVO> recharge = memberInfoMapper.getTest(0, memberInfo.getMemberId(), null);


            for (TestVO testVO : recharge) {
                rechargeGrowth = rechargeGrowth + testVO.getChanges();
            }

            List<TestVO> sign = memberInfoMapper.getTest(1, memberInfo.getMemberId(), null);
            for (TestVO testVO : sign) {
                signGrowth = signGrowth + testVO.getChanges();
            }

            List<TestVO> dama = memberInfoMapper.getTest(2, memberInfo.getMemberId(), null);
            for (TestVO testVO : dama) {
                damaGrowth = damaGrowth + testVO.getChanges();
            }

            List<TestVO> back = memberInfoMapper.getTest1(memberInfo.getMemberId());
            for (TestVO testVO : back) {
                backGrowth = backGrowth + testVO.getChanges();
            }

            List<TestVO> demote = memberInfoMapper.getTest(3, memberInfo.getMemberId(), "未达到保底系统降级");
            for (TestVO testVO : demote) {
                demoteGrowth = demoteGrowth + testVO.getChanges();
            }

            MemberGrowthStatis entity = new MemberGrowthStatis();
            entity.setMemberId(memberInfo.getMemberId());
            entity.setAccount(member.getAccount());
            entity.setVipLevel(one.getVipLevel());
            entity.setVipGrowth(one.getVipGrowth());
            entity.setRechargeGrowth(rechargeGrowth);
            entity.setSignGrowth(signGrowth);
            entity.setDamaGrowth(damaGrowth);
            entity.setBackGrowth(backGrowth);
            entity.setDemoteGrowth(demoteGrowth);
            memberGrowthStatisService.save(entity);

        }



    }

}
