package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.GameTransferRecord;
import com.gameplat.admin.model.dto.GameTransferRecordQueryDTO;
import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.admin.model.vo.PageDtoVO;

public interface GameTransferRecordService extends IService<GameTransferRecord> {

  PageDtoVO<GameTransferRecord> queryGameTransferRecord(Page<GameTransferRecord> page, GameTransferRecordQueryDTO dto);

  void fillOrders(OperGameTransferRecordDTO liveTransferRecord);

  boolean findTransferRecordCount(GameTransferRecord dto);
}
