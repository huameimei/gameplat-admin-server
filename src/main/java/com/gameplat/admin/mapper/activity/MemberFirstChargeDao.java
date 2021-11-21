package com.gameplat.admin.mapper.activity;

import com.gameplat.admin.model.domain.activity.MemberFirstCharge;
import com.gameplat.admin.model.dto.activity.MemberFirstChargeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * <p>
 * 每日首充表 Mapper 接口
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Mapper
public interface MemberFirstChargeDao {

    /**
     * 获取每日首充配置
     *
     * @param type
     * @return
     */
    MemberFirstCharge getConfig(int type);

    /**
     * 添加每日首充
     *
     * @param memberFirstCharge
     * @return
     */
    int insert(MemberFirstCharge memberFirstCharge);

    /**
     * 编辑每日首充
     */
    int update(MemberFirstCharge memberFirstCharge);

    /**
     * 根据id删除
     *
     * @param chargeId
     * @return
     */
    int deleteFirstCharge(Long chargeId);

    /**
     * 查询每日首充列表
     *
     * @param memberFirstChargeDTO
     * @return
     */
    List<MemberFirstCharge> findFirstChargeList(MemberFirstChargeDTO memberFirstChargeDTO);

    /**
     * 根据id查询每日首充
     *
     * @param chargeId
     * @return
     */
    MemberFirstCharge findFirstChargeById(Long chargeId);

    /**
     * 强制下线每日首充
     *
     * @param chargeId
     * @return
     */
    int updateFirstChargeStatus(Long chargeId);
}
