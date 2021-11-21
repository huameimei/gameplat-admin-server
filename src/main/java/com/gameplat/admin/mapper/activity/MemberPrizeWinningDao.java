package com.gameplat.admin.mapper.activity;

import com.gameplat.admin.model.domain.activity.MemberPrizeWinning;
import com.gameplat.admin.model.vo.activity.MemberTurntableWinRecordVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * <p>
 * 奖品中奖记录表 Mapper 接口
 * </p>
 *
 * @author 沙漠
 * @since 2020-06-23
 */
@Mapper
public interface MemberPrizeWinningDao {

    /**
     * 获奖列表
     *
     * @param memberPrizeWinning
     * @return
     */
    List<MemberPrizeWinning> findList(MemberPrizeWinning memberPrizeWinning);

    int insert(MemberPrizeWinning memberPrizeWinning);

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    int insertBatch(List<MemberPrizeWinning> list);

    /**
     * 根据转盘id查询用户中奖纪录
     *
     * @param turntableId
     * @return
     */
    List<MemberTurntableWinRecordVO> findMemberTurntableWinRecordList(Long turntableId);

}
