package com.gameplat.admin.controller.open.chat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ChatGif;
import com.gameplat.admin.model.dto.ChatGifEditDTO;
import com.gameplat.admin.model.vo.ChatGifVO;
import com.gameplat.admin.service.ChatGifService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author lily
 * @description
 * @date 2022/2/13
 */

@Api(tags = "聊天室Gif管理")
@RestController
@RequestMapping("/api/admin/chat/gif")
public class ChatGifController {

    @Autowired
    private ChatGifService chatGifService;

    @ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('chat:gif:page')")
    public IPage<ChatGifVO> page(PageDTO<ChatGif> page, String name){
        return chatGifService.page(page, name);
    }

    @ApiOperation(value = "增/上传图片")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('chat:gif:add')")
    public void add(@RequestPart MultipartFile file, String name) throws Exception {
        chatGifService.add(file, name);
    }

    @ApiOperation(value = "上传图片")
    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('chat:gif:upload')")
    public String upload(@RequestPart MultipartFile file) throws Exception {
        return chatGifService.upload(file, null);
    }

    @ApiOperation(value = "删")
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('chat:gif:remove')")
    public void remove(@PathVariable Integer id){
        chatGifService.remove(id);
    }

    @ApiOperation(value = "改")
    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('chat:gif:edit')")
    public void edit(ChatGifEditDTO dto){
        chatGifService.edit(dto);
    }


}
