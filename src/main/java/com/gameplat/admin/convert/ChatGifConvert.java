package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.ChatGifAddDTO;
import com.gameplat.admin.model.dto.ChatGifEditDTO;
import com.gameplat.admin.model.vo.ChatGifVO;
import com.gameplat.model.entity.chart.ChatGif;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatGifConvert {

  ChatGifVO toVo(ChatGif entity);

  ChatGif toEntity(ChatGifAddDTO dto);

  ChatGif toEntity(ChatGifEditDTO dto);
}
