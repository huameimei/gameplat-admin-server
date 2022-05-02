package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.vo.MemberTransferRecordVO;
import com.gameplat.model.entity.member.MemberTransferRecord;

import java.util.List;

public interface MemberTransferRecordService extends IService<MemberTransferRecord> {

  IPage<MemberTransferRecordVO> queryPage(PageDTO<MemberTransferRecord> page, Integer type);

  List<MemberTransferRecord> getBySerialNo(String serialNo);

  List<String> getContent(String serialNo, String endTime, String startTime);
}
