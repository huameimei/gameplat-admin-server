package com.gameplat.admin.mapper.activity;

import com.gameplat.admin.model.domain.activity.MemberActivityLobby;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lyq
 * @Description 数据层
 * @date 2020-08-14 14:50:01
 */
@Mapper
public interface MemberActivityLobbyDao {

    List<MemberActivityLobby> findActivityLobbyList(MemberActivityLobby memberActivityLobby);

    int saveActivityLobby(MemberActivityLobby memberActivityLobby);

    int updateActivityLobby(MemberActivityLobby memberActivityLobby);

    int deleteActivityLobby(List<Long> id);

    MemberActivityLobby findActivityLobbyById(Long id);

    List<MemberActivityLobby> findActivityLobbyListByTime(String time);

    List<MemberActivityLobby> findNewActivity(String time);

    List<MemberActivityLobby> findAllValid(String time);

    List<Long> findExpireLobby();

    List<Long> findExpireRed();
}
