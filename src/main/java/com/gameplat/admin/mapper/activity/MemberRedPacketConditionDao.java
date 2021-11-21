package com.gameplat.admin.mapper.activity;


import com.gameplat.admin.model.domain.activity.MemberRedPacketCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 沙漠
 * @since 2020-06-16
 */
@Mapper
public interface MemberRedPacketConditionDao {

    /**
     * 根据红包雨id查询（升序）
     *
     * @param redPacketId
     * @return
     */
    List<MemberRedPacketCondition> findRedPacketConditionList(Long redPacketId);

    /**
     * 根据红包雨id查询（降序）
     *
     * @param redPacketId
     * @return
     */
    List<MemberRedPacketCondition> findRedPacketConditionDescList(Long redPacketId);

    /**
     * 新增红包雨条件
     *
     * @param memberRedPacketCondition
     * @return
     */
    int saveRedPacketCondition(List<MemberRedPacketCondition> memberRedPacketCondition);

    /**
     * 修改红包雨条件
     *
     * @param memberRedPacketCondition
     * @return
     */
    int updateRedPacketCondition(List<MemberRedPacketCondition> memberRedPacketCondition);

    /**
     * 删除红包雨条件
     *
     * @param ids
     * @return
     */
    int deleteRedPacketCondition(Long[] ids);

    /**
     * 删除红包雨ID
     *
     * @param redPacketId
     * @return
     */
    int deleteRedPacketId(Long redPacketId);
}
