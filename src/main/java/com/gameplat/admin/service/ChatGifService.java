package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ChatGif;
import com.gameplat.admin.model.dto.ChatGifEditDTO;
import com.gameplat.admin.model.vo.ChatGifVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ChatGifService extends IService<ChatGif> {

    /** 分页列表 */
    IPage<ChatGifVO> page(PageDTO<ChatGif> page, String name);

    /** 增 */
    void add(MultipartFile file, String name) throws IOException;

    /** 删 */
    void remove(@PathVariable Integer id);

    /** 改 */
    void edit(ChatGifEditDTO dto);

    int findMD5(String md5);
}
