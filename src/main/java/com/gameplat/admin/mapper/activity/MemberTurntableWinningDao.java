package com.gameplat.admin.mapper.activity;


import com.gameplat.admin.model.domain.activity.MemberTurntableWinning;
import com.gameplat.admin.model.vo.activity.MemberTurntableWinRecordVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 转盘中奖记录 Mapper 接口
 * </p>
 *
 * @author 沙漠
 * @since 2020-06-08
 */
@Mapper
public interface MemberTurntableWinningDao {

    /**
     * 获奖列表
     *
     * @param memberTurntableWinning
     * @return
     */
    List<MemberTurntableWinning> findList(MemberTurntableWinning memberTurntableWinning);

    int insert(MemberTurntableWinning memberTurntableWinning);

    /**
     * 根据转盘id查询用户中奖纪录
     *
     * @param turntableId
     * @return
     */
    List<MemberTurntableWinRecordVO> findMemberTurntableWinRecordList(Long turntableId);
}
