package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.GameMemberReportMapper;
import com.gameplat.admin.model.domain.MemberDayReport;
import com.gameplat.admin.model.dto.MemberDayReportDto;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.MemberDayReportVo;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameMemberReportService;
import com.gameplat.base.common.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author kb
 * @Date 2022/2/8 18:27
 * @Version 1.0
 */

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
@Log4j2
public class GameMemberReportServiceImpl extends ServiceImpl <GameMemberReportMapper,MemberDayReport> implements GameMemberReportService {


    private final GameMemberReportMapper gameMemberReportMapper;


    @Override
    public PageDtoVO<MemberDayReportVo> findSumMemberDayReport(Page<MemberDayReportVo> page, MemberDayReportDto memberDayReportDto) {
        //查询充提报表
        Page<MemberDayReport> memberDayReportPage = gameMemberReportMapper.findMemberDayReportPage(page, memberDayReportDto);
        List<MemberDayReportVo> memberDayReportVos = BeanUtils.mapList(memberDayReportPage.getRecords(), MemberDayReportVo.class);

        PageDtoVO<MemberDayReportVo> pageDtoVO = new PageDtoVO<>();
        //查询总计
        Map<String, Object> sumMemberDayReport = gameMemberReportMapper.findSumMemberDayReport(memberDayReportDto);
        log.info("充提总计：{}",sumMemberDayReport);

        Page<MemberDayReportVo> resultPage = new Page<>();
        resultPage.setRecords(memberDayReportVos);
        pageDtoVO.setOtherData(sumMemberDayReport);
        pageDtoVO.setPage(resultPage);
        pageDtoVO.getPage().setTotal(memberDayReportPage.getTotal());

        return pageDtoVO;
    }
}
