package com.gameplat.admin.mapper;


import com.gameplat.admin.model.domain.Activity;
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
public interface ActivityDao {

    /**
     * 查找活动
     *
     * @param activity
     * @return
     */
    List<Activity> findList(Activity activity);

    /**
     * 添加活动
     *
     * @param memberGift
     * @return
     */
    int insert(Activity memberGift);

    /**
     * 编辑活动
     */
    int update(Activity memberGift);

    /**
     * 删除活动
     *
     * @param ids
     * @return
     */
    int deleteByIds(Long[] ids);

}
