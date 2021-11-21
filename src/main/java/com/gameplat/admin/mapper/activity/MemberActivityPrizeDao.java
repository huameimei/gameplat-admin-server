package com.gameplat.admin.mapper.activity;

import com.gameplat.admin.model.domain.activity.MemberActivityPrize;
import com.gameplat.admin.model.vo.activity.AppActivityPrizeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 活动奖品表 Mapper 接口
 * </p>
 *
 * @author 沙漠
 * @since 2020-06-05
 */
@Mapper
public interface MemberActivityPrizeDao {

    /**
     * 批量新增活动奖品
     *
     * @param memberActivityPrize
     * @return
     */
    int saveBatchActivityPrize(List<MemberActivityPrize> memberActivityPrize);

    /**
     * 批量修改活动奖品
     *
     * @param memberActivityPrize
     * @return
     */
    int updateBatchActivityPrize(List<MemberActivityPrize> memberActivityPrize);

    /**
     * 批量删除活动奖品
     *
     * @param ids
     * @return
     */
    int deleteBatchActivityPrize(Long[] ids);

    /**
     * 查询活动奖品
     *
     * @param memberActivityPrize
     * @return
     */
    List<AppActivityPrizeVO> findActivityPrizeList(MemberActivityPrize memberActivityPrize);

    /**
     * 删除活动的奖品
     *
     * @param activityId
     * @param type
     * @return
     */
    int deleteActivityPrize(@Param("activityId") Long activityId, @Param("type") Integer type);
}
