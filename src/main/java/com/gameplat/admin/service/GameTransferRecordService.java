package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GameTransferRecordQueryDTO;
import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.model.entity.game.GameTransferRecord;
import java.util.List;

public interface GameTransferRecordService extends IService<GameTransferRecord> {

  PageDtoVO<GameTransferRecord> queryGameTransferRecord(
      Page<GameTransferRecord> page, GameTransferRecordQueryDTO dto);

  void fillOrders(OperGameTransferRecordDTO gameTransferRecord);

  boolean findTransferRecordCount(GameTransferRecord dto);

  List<GameTransferRecord> findPlatformCodeList(Long id);

  void saveTransferRecord(GameTransferRecord transferRecord);
}
