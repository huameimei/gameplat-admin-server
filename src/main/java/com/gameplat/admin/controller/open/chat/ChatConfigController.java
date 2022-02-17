package com.gameplat.admin.controller.open.chat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ChatGif;
import com.gameplat.admin.model.vo.ChatGifVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lily
 * @description 聊天室配置
 * @date 2022/2/15
 */

@Api(tags = "聊天配置")
@RestController
@RequestMapping("/api/admin/chat/config")
public class ChatConfigController {

}
