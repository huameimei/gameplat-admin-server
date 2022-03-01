package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.ChatGifEditDTO;
import com.gameplat.admin.model.vo.ChatGifVO;
import com.gameplat.model.entity.chart.ChatGif;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

public interface ChatGifService extends IService<ChatGif> {

  /** 分页列表 */
  IPage<ChatGifVO> page(PageDTO<ChatGif> page, String name);

  /** 增 */
  void add(MultipartFile file, String name) throws Exception;

  /** 增 */
  String upload(MultipartFile file, ChatGif chatGif) throws Exception;

  /** 删 */
  void remove(@PathVariable Integer id);

  /** 改 */
  void edit(ChatGifEditDTO dto);

  int findMD5(String md5);
}
