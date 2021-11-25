package com.gameplat.admin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.ActivityDistribute;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.ActivityDistributeDTO;
import com.gameplat.admin.model.vo.ActivitySendVO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lyq
 * @Description 数据层
 * @date 2020-08-20 11:30:39
 */
@Mapper
public interface ActivityDistributeDao extends BaseMapper<ActivityDistribute> {

    int deleteByPrimaryKey(Long distributeId);

    int insert(ActivityDistribute record);

    int insertSelective(ActivityDistribute record);

    ActivityDistribute selectByPrimaryKey(Long distributeId);

    int updateByPrimaryKeySelective(ActivityDistribute record);

    int updateByPrimaryKey(ActivityDistribute record);

    List<ActivitySendVO> findUserActivityPayList();

    List<ActivityDistribute> findActivityDistributeList(ActivityDistributeDTO activityDistribute);

    int batchInsertActivityDistribute(List<ActivityDistribute> list);

    int updateActivityDistributeStatus(ActivityDistribute activityDistribute);

    List<ActivityDistribute> findBatchActivityDistributeList(List<Long> distributeIds);

    BigDecimal findDistributeSubtotal(Integer pageSize, Integer pageNum);

    BigDecimal findDistributeAggregate(ActivityDistributeDTO activityDistribute);

    List<ActivityDistribute> findUniques(List<ActivityDistribute> adUniques);

    List<String> findRedPacketName();

    List<String> findLobbyName();
}
