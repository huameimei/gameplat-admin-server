package com.gameplat.admin.mapper.activity;


import com.gameplat.admin.model.domain.activity.MemberActivity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 活动表 Mapper 接口
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Mapper
public interface MemberActivityDao {

    /**
     * 查找活动
     *
     * @param memberActivity
     * @return
     */
    List<MemberActivity> findList(MemberActivity memberActivity);

    /**
     * 添加活动
     *
     * @param memberGift
     * @return
     */
    int insert(MemberActivity memberGift);

    /**
     * 编辑活动
     */
    int update(MemberActivity memberGift);

    /**
     * 删除活动
     *
     * @param ids
     * @return
     */
    int deleteByIds(Long[] ids);

}
