package com.gameplat.admin.mapper.activity;


import com.gameplat.admin.model.dto.activity.ActivityBlackListDTO;
import com.gameplat.admin.model.vo.activity.ActivityBlackListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lyq
 * @Description 数据层
 * @date 2020-08-20 11:30:39
 */
@Mapper
public interface ActivityBlackListDao {

    List<ActivityBlackListVO> findActivityBlackList(ActivityBlackListDTO dto);

    void insertActivityBlackList(ActivityBlackListDTO dto);

    void deleteActivityBlackList(List<Long> id);

    Integer checkActivityBlack(ActivityBlackListDTO dto);

}
