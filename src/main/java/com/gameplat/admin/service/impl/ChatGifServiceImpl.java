package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ChatGifConvert;
import com.gameplat.admin.mapper.ChatGifMapper;
import com.gameplat.admin.model.dto.ChatGifEditDTO;
import com.gameplat.admin.model.vo.ChatGifVO;
import com.gameplat.admin.service.ChatGifService;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.OssService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.compent.oss.config.FileConfig;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.chart.ChatGif;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 聊天室Gif管理
 *
 * @author lily
 * @date 2022/2/13
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ChatGifServiceImpl extends ServiceImpl<ChatGifMapper, ChatGif>
    implements ChatGifService {

  @Autowired private ChatGifConvert chatGifConvert;

  @Autowired private ConfigService configService;

  @Autowired private OssService ossService;

  @Override
  public IPage<ChatGifVO> page(PageDTO<ChatGif> page, String name) {
    return this.lambdaQuery()
        .like(StringUtils.isNotNull(name), ChatGif::getName, name)
        .orderByDesc(ChatGif::getCreateTime)
        .page(page)
        .convert(chatGifConvert::toVo);
  }

  @Override
  @SneakyThrows
  public void add(MultipartFile file, String name) {
    ChatGif chatGif = new ChatGif();
    String url = upload(file, chatGif);
    try {
      BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
      if (bufferedImage != null) {
        chatGif.setHeight(bufferedImage.getHeight());
        chatGif.setWidth(bufferedImage.getWidth());
      }
    } catch (IOException e) {
      log.info("异常原因", e);
      throw new ServiceException("此图片已破损");
    }

    chatGif.setName(name);
    chatGif.setStoreFileName(file.getOriginalFilename());
    chatGif.setFileUrl(url);
    chatGif.setMd5(SecureUtil.md5(file.getInputStream()));
    Assert.isTrue(this.save(chatGif), "保存图片失败!");
  }

  @Override
  public String upload(MultipartFile file, ChatGif chatGif) throws Exception {
    // 上传图片
    FileConfig fileConfig =
        configService.getDefaultConfig(DictTypeEnum.FILE_CONFIG, FileConfig.class);
    if (ObjectUtil.isNotNull(chatGif)) {
      chatGif.setServiceProvider(fileConfig.getProvider());
    }

    String md5 = SecureUtil.md5(file.getInputStream());
    if (this.findMd5(md5) > 0) {
      throw new ServiceException("不允许相同图片");
    }

    // 上传成功，返回图片地址
    return ossService.upload(file);
  }

  @Override
  public void remove(Integer id) {
    ChatGif chatGif = this.getById(id);
    if (ObjectUtil.isEmpty(chatGif)) {
      throw new ServiceException("存储位置未知");
    }

    // 删库
    super.removeById(id);
    Assert.isTrue(ossService.remove(chatGif.getStoreFileName()), "删除文件失败!");
  }

  @Override
  public void edit(ChatGifEditDTO dto) {
    this.updateById(chatGifConvert.toEntity(dto));
  }

  @Override
  public long findMd5(String md5) {
    return this.lambdaQuery().eq(ChatGif::getMd5, md5).count();
  }
}
