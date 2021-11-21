package com.gameplat.admin.mapper.activity;

import com.gameplat.admin.model.domain.activity.MemberLobbyDiscount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lyq
 * @Description 数据层
 * @date 2020-08-20 11:27:34
 */
@Mapper
public interface MemberLobbyDiscountDao {

    List<MemberLobbyDiscount> findLobbyDiscountList(MemberLobbyDiscount memberLobbyDiscount);

    List<MemberLobbyDiscount> findLobbyDiscountListByLobbyId(Long lobbyId);

    int saveLobbyDiscount(List<MemberLobbyDiscount> memberLobbyDiscount);

    int updateLobbyDiscount(List<MemberLobbyDiscount> memberLobbyDiscount);

    int deleteBatchLobbyDiscount(List<Long> lobbyDiscountIds);

    int deleteBatchLobbyDiscountByLobbyId(List<Long> lobbyId);

    List<MemberLobbyDiscount> findLobbyDiscountListByLobbyIds(List<Long> list);
}
