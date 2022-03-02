package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.vo.ChatLeaderBoardVO;
import com.gameplat.model.entity.chart.ChatLeaderBoard;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lily
 * @description
 * @date 2022/2/16
 */
public interface ChatLeaderBoardMapper extends BaseMapper<ChatLeaderBoard> {

  /** 批量新增 */
  int batchAdd(List<ChatLeaderBoard> list);

  /** 查询 */
  List<ChatLeaderBoardVO> slaveLottWin(
      @Param("gameIds") List<String> gameIds, @Param("createTime") String createTime);

  List<ChatLeaderBoardVO> slaveQueryPushWinReport(
      @Param("roomId") Long roomId,
      @Param("gameIds") List<String> gameIds,
      @Param("createTime") String createTime);
}
