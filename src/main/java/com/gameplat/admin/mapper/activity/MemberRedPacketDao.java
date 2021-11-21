package com.gameplat.admin.mapper.activity;

import com.gameplat.admin.model.domain.activity.MemberRedPacket;
import com.gameplat.admin.model.dto.activity.MemberRedPacketDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * <p>
 * 红包雨 Mapper 接口
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Mapper
public interface MemberRedPacketDao {

    /**
     * 获取配置
     */
    MemberRedPacket getConfig();

    /**
     * 添加红包雨
     *
     * @param memberRedPacket
     * @return
     */
    int saveRedPacket(MemberRedPacket memberRedPacket);

    /**
     * 编辑红包雨
     */
    int updateRedPacket(MemberRedPacket memberRedPacket);

    /**
     * 红包雨列表
     *
     * @param memberRedPacketDTO
     * @return
     */
    List<MemberRedPacket> findRedPacketList(MemberRedPacketDTO memberRedPacketDTO);

    /**
     * 删除红包雨
     *
     * @param packetId
     * @return
     */
    int deleteRedPacket(Long packetId);

    /**
     * 查询红包雨状态
     *
     * @param ids
     * @return
     */
    List<Integer> findRedPacketStatus(List<Long> ids);

    /**
     * 根据id查询红包雨
     *
     * @param packetId
     * @return
     */
    MemberRedPacket findRedPacketById(Long packetId);

    /**
     * 强制下线
     *
     * @param packetId
     * @return
     */
    int updateRedPacketStatus(Long packetId);

    /**
     * 红包雨上线
     */
    int online();

    /**
     * 红包雨下线
     */
    int offline();

    /**
     * 获取到达推送时间的红包雨
     *
     * @return
     */
    List<MemberRedPacket> getArriveRain(int type);

    List<MemberRedPacket> getCurrentTimeRedPacket(int type);

}
