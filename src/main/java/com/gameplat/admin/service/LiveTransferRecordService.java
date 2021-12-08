package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.LiveTransferRecord;
import com.gameplat.admin.model.dto.LiveTransferRecordQueryDTO;
import com.gameplat.admin.model.dto.OperLiveTransferRecordDTO;
import com.gameplat.admin.model.vo.PageDtoVO;

public interface LiveTransferRecordService extends IService<LiveTransferRecord> {

  PageDtoVO<LiveTransferRecord> queryLiveTransferRecord(Page<LiveTransferRecord> page, LiveTransferRecordQueryDTO dto);

  void fillOrders(OperLiveTransferRecordDTO liveTransferRecord);
}
