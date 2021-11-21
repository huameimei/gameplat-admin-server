package com.gameplat.admin.mapper.activity;


import com.gameplat.admin.model.domain.activity.MemberPrize;
import com.gameplat.admin.model.dto.activity.MemberActivityPrizeDTO;
import com.gameplat.admin.model.dto.activity.MemberPrizeDTO;
import com.gameplat.admin.model.vo.activity.MemberActivityPrizeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 转盘奖项表 Mapper 接口
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Mapper
public interface MemberPrizeDao {

    /**
     * 查询转盘奖项列表
     *
     * @param memberPrizeDTO
     * @return
     */
    List<MemberPrize> findPrizeList(MemberPrizeDTO memberPrizeDTO);

    /**
     * 新增奖品
     *
     * @param memberPrize
     * @return
     */
    int savePrize(MemberPrize memberPrize);

    /**
     * 修改奖品
     *
     * @param memberPrize
     * @return
     */
    int updatePrize(MemberPrize memberPrize);

    /**
     * 删除奖品
     *
     * @param ids
     * @return
     */
    int deletePrize(List<Long> ids);

    /**
     * 查询活动奖品
     *
     * @param memberActivityPrizeDTO
     * @return
     */
    List<MemberActivityPrizeVO> findActivityPrizeList(MemberActivityPrizeDTO memberActivityPrizeDTO);


    MemberActivityPrizeVO findPrizeById(@Param("prizeId") Long prizeId);
}
