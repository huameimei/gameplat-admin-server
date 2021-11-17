package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.Notice;
import com.gameplat.admin.model.domain.SpreadConfig;
import com.gameplat.admin.model.dto.NoticeAddDTO;
import com.gameplat.admin.model.dto.NoticeEditDTO;
import com.gameplat.admin.model.dto.NoticeUpdateStatusDTO;
import com.gameplat.admin.model.dto.SpreadConfigAddDTO;
import com.gameplat.admin.model.vo.NoticeVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoticeConvert {

  NoticeVO toVo(Notice notice);

  Notice toEntity(NoticeAddDTO noticeAddDTO);

  Notice toEntity(NoticeEditDTO noticeEditDTO);

  Notice toEntity(NoticeUpdateStatusDTO noticeUpdateStatusDTO);
}
