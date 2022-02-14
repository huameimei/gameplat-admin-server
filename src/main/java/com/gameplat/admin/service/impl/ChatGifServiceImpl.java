package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ChatGifConvert;
import com.gameplat.admin.mapper.ChatGifMapper;
import com.gameplat.admin.model.domain.ChatGif;
import com.gameplat.admin.model.dto.ChatGifEditDTO;
import com.gameplat.admin.model.vo.ChatGifVO;
import com.gameplat.admin.service.ChatGifService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.base.common.util.UUIDUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lily
 * @description 聊天室Gif管理
 * @date 2022/2/13
 */

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ChatGifServiceImpl extends ServiceImpl<ChatGifMapper, ChatGif> implements ChatGifService {

    @Autowired
    private ChatGifConvert chatGifConvert;

    /** 分页列表 */
    @Override
    public IPage<ChatGifVO> page(PageDTO<ChatGif> page, String name) {
        IPage<ChatGifVO> list = this.lambdaQuery()
                .like(StringUtils.isNotNull(name), ChatGif::getName, name)
                .orderByDesc(ChatGif::getCreateTime)
                .page(page)
                .convert(chatGifConvert::toVo);
        return list;

    }

    /** 增 */
    @Override
    public void add(MultipartFile file, String name) {
        if (!isImage(file)){
            throw new ServiceException("不支持此格式！");
        }
        //重命名
        String fKey = this.getRandomName(file.getOriginalFilename());
    }

    /** 删 */
    @Override
    public void remove(Integer id) {

    }

    /** 改 */
    @Override
    public void edit(ChatGifEditDTO dto) {
        this.updateById(chatGifConvert.toEntity(dto));
    }

    /** 判断文件是否是图片 */
    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (StringUtils.isBlank(contentType) || !contentType.matches("image/(bmp|jpg|jpeg|png|gif)")) {
            return false;
        }
        return true;
    }

    /** 生成文件UUID名称 */
    public static String getRandomName(String fileName){
        int index = fileName.lastIndexOf(".");
        //获取后缀名
        String suffix = fileName.substring(index);
        return UUIDUtils.getUUID32()+suffix;
    }
}
