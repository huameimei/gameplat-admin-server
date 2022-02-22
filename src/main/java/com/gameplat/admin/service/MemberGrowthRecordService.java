package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.ActivityMemberInfo;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberGrowthConfig;
import com.gameplat.admin.model.domain.MemberGrowthRecord;
import com.gameplat.admin.model.domain.MemberGrowthStatis;
import com.gameplat.admin.model.dto.MemberGrowthChangeDto;
import com.gameplat.admin.model.dto.MemberGrowthRecordDTO;
import com.gameplat.admin.model.dto.MemberGrowthStatisDTO;
import com.gameplat.admin.model.vo.GrowthScaleVO;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberGrowthRecordVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface MemberGrowthRecordService extends IService<MemberGrowthRecord> {

    IPage<MemberGrowthRecordVO> findRecordList(PageDTO<MemberGrowthRecord> page, MemberGrowthRecordDTO dto);

    Integer dealUpLevel(Long afterGrowth, MemberGrowthConfig memberGrowthConfig);

    List<MemberGrowthRecord> findRecordGroupBy(MemberGrowthRecord entity);

    /** 修改单个会员成长值 */
    void editMemberGrowth(MemberGrowthChangeDto dto, HttpServletRequest request);

    Boolean insertMemberGrowthRecord(MemberGrowthRecord userGrowthRecord);

    GrowthScaleVO progressBar(Integer level, Long memberId);
    /** 处理升级 */
    void dealPayUpReword(Integer beforeLevel, Integer afterLevel, MemberGrowthConfig growthConfig, Member member, HttpServletRequest request);
}
