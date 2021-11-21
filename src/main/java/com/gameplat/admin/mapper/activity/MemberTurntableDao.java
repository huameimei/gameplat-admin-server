package com.gameplat.admin.mapper.activity;

import com.gameplat.admin.model.domain.activity.MemberTurntable;
import com.gameplat.admin.model.dto.activity.MemberTurntableDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 转盘表 Mapper 接口
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Mapper
public interface MemberTurntableDao {

    /**
     * 查询转盘列表
     *
     * @param memberTurntableDTO
     * @return
     */
    List<MemberTurntable> findTurntableList(MemberTurntableDTO memberTurntableDTO);

    /**
     * 新增转盘
     *
     * @param memberTurntable
     * @return
     */
    int saveTurntable(MemberTurntable memberTurntable);

    /**
     * 修改转盘
     *
     * @param memberTurntable
     * @return
     */
    int updataTurntable(MemberTurntable memberTurntable);

    /**
     * 查询单个转盘
     *
     * @param memberTurntableDTO
     * @return
     */
    MemberTurntable findTurntable(MemberTurntableDTO memberTurntableDTO);

    /**
     * 查询平台类型、展示位置重复值
     *
     * @param type
     * @param display
     * @return
     */
    int findRepetTypeAndDisplay(@Param("type") String type, @Param("display") String display);

    /**
     * 根据id查询
     *
     * @param turntableId
     * @return
     */
    MemberTurntable findTurntableById(Long turntableId);

    /**
     * 查询红包雨状态
     *
     * @param ids
     * @return
     */
    List<Integer> findTurntableStatus(List<Long> ids);

    /**
     * 转盘下线
     *
     * @param turntableId
     * @return
     */
    int updateTurntableStatus(Long turntableId);

    /**
     * 删除转盘
     *
     * @param turntableId
     * @return
     */
    int deleteTurntable(Long turntableId);

    /**
     * 让到达活动时间的转盘活动上线
     */
    int online();

    /**
     * 让已过期的活动下线
     */
    int offline();
}
