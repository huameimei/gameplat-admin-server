package com.gameplat.admin.mapper.activity;


import com.gameplat.admin.model.domain.activity.MemberChargeDiscount;
import com.gameplat.admin.model.dto.activity.MemberChargeDiscountDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 首充优惠表 Mapper 接口
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Mapper
public interface MemberChargeDiscountDao {

    /**
     * 查找优惠
     *
     * @param memberChargeDiscount
     * @return
     */
    List<MemberChargeDiscount> findList(MemberChargeDiscount memberChargeDiscount);

    /**
     * 批量添加优惠
     *
     * @param list
     * @return
     */
    int insertBatch(List<MemberChargeDiscount> list);

    /**
     * 批量更新
     *
     * @param list
     * @return
     */
    int updateBatch(List<MemberChargeDiscount> list);

    /**
     * 根据id批量删除
     *
     * @param ids
     * @return
     */
    int deleteByIds(Long[] ids);

    /**
     * 根据首充id查询优惠
     *
     * @param memberChargeDiscountDTO
     * @return
     */
    List<MemberChargeDiscount> findChargeDiscountByChargeId(MemberChargeDiscountDTO memberChargeDiscountDTO);

    /**
     * 删除首充ID
     *
     * @param ChargeId
     * @return
     */
    int deleteChargeId(Long ChargeId);
}
