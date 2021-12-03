package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MemberBill;
import com.gameplat.admin.model.vo.MemberBillVO;

import java.util.List;

public interface MemberBillMapper extends BaseMapper<MemberBill> {

    IPage<MemberBillVO> findyByTableIndex(PageDTO<MemberBill> page, String account,
                                          String orderNo,
                                          List<Integer> tranTypes,
                                          String beginTime,
                                          String endTime,
                                          Integer tableIndex);

    List<MemberBillVO> findyByTableIndex(String account,
                                          String orderNo,
                                          List<Integer> tranTypes,
                                          String beginTime,
                                          String endTime,
                                          Integer tableIndex);

}
