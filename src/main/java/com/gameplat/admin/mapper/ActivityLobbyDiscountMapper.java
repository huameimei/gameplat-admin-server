package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.activity.ActivityLobbyDiscount;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lyq<br>
 *     @Description 数据层
 * @date 2020-08-14 14:50:01
 */
public interface ActivityLobbyDiscountMapper extends BaseMapper<ActivityLobbyDiscount> {
    @Select("select * from activity_lobby_discount where lobby_id = #{activityLobbyId}")
    List<ActivityLobbyDiscount> selectByActivityLobbyId(Long activityLobbyId);
}
