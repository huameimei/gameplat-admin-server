package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.context.JSONObjectContext;
import com.gameplat.admin.convert.ChatGifConvert;
import com.gameplat.admin.mapper.ChatGifMapper;
import com.gameplat.admin.model.dto.ChatGifEditDTO;
import com.gameplat.admin.model.vo.ChatGifVO;
import com.gameplat.admin.service.ChatGifService;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.compent.oss.FileStorageProvider;
import com.gameplat.common.compent.oss.FileStorageStrategyContext;
import com.gameplat.common.compent.oss.config.FileConfig;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.common.util.FileUtils;
import com.gameplat.model.entity.chart.ChatGif;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Map;

/**
 * @author lily
 * @description 聊天室Gif管理
 * @date 2022/2/13
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ChatGifServiceImpl extends ServiceImpl<ChatGifMapper, ChatGif>
    implements ChatGifService {

  @Autowired private ChatGifConvert chatGifConvert;

  @Autowired private ConfigService configService;

  @Autowired private FileStorageStrategyContext fileStorageStrategyContext;

  /** 分页列表 */
  @Override
  public IPage<ChatGifVO> page(PageDTO<ChatGif> page, String name) {
    return this.lambdaQuery()
        .like(StringUtils.isNotNull(name), ChatGif::getName, name)
        .orderByDesc(ChatGif::getCreateTime)
        .page(page)
        .convert(chatGifConvert::toVo);
  }

  /** 增 */
  @Override
  public void add(MultipartFile file, String name) throws Exception {
    ChatGif chatGif = new ChatGif();
    String url = upload(file, chatGif);
    File file1 = new File(file.getOriginalFilename());
    try {
      FileUtils.copyInputStreamToFile(file.getInputStream(), file1);
    } catch (IOException e) {
      log.info("异常原因{}", e);
    }
    // 会在本地产生临时文件，用完后需要删除
    if (file1.exists()) {
      file1.delete();
    }
    BufferedImage bufferedImage = null;
    try {
      // 通过MultipartFile得到InputStream，从而得到BufferedImage
      bufferedImage = ImageIO.read(file.getInputStream());
      if (bufferedImage != null) {
        chatGif.setHeight(bufferedImage.getHeight());
        chatGif.setWidth(bufferedImage.getWidth());
      }
    } catch (IOException e) {
      log.info("异常原因{}", e);
      throw new ServiceException("此图片已破损");
    }
    chatGif.setName(name);
    if (bufferedImage != null) {
      chatGif.setHeight(bufferedImage.getHeight());
      chatGif.setWidth(bufferedImage.getWidth());
    }
    chatGif.setStoreFileName(file.getOriginalFilename());
    chatGif.setFileUrl(url);
    chatGif.setMd5(SecureUtil.md5(file.getInputStream()));

    // 如果是异步上传，如果响应时间太长，request关闭，也就获取不到请求头的token，也就拿不到LoginAppUser对象
    // 所以需要在异步线程将上传人加入到本地线程，在保存上传记录时，上传者从本地线程中拿值
    String userName = JSONObjectContext.getJSONObjectContext("userName");
    if (StringUtils.isNotEmpty(userName)) {
      chatGif.setCreateBy(userName);
    }
    save(chatGif);
  }

  @Override
  public String upload(MultipartFile file, ChatGif chatGif) throws Exception {
    // 上传图片
    FileConfig fileConfig =
        configService.getDefaultConfig(DictTypeEnum.FILE_CONFIG, FileConfig.class);
    FileStorageProvider fileStorageProvider = fileStorageStrategyContext.getProvider(fileConfig);
    if (ObjectUtil.isNotNull(chatGif)) {
      chatGif.setServiceProvider(fileConfig.getProvider());
    }

    String md5 = SecureUtil.md5(file.getInputStream());
    if (findMD5(md5) > 0) {
      throw new ServiceException("不允许相同图片");
    }
    // 上传成功，返回图片地址
    return fileStorageProvider.upload(
        file.getInputStream(), file.getContentType(), file.getOriginalFilename());
  }

  /** 删 */
  @Override
  public void remove(Integer id) {
    ChatGif chatGif = this.getById(id);
    if (ObjectUtil.isEmpty(chatGif)) {
      throw new ServiceException("存储位置未知");
    }
    // 删储服务器图片
    FileConfig fileConfig =
        configService.getDefaultConfig(DictTypeEnum.FILE_CONFIG, FileConfig.class);
    FileStorageProvider fileStorageProvider = fileStorageStrategyContext.getProvider(fileConfig);
    fileStorageProvider.delete(chatGif.getStoreFileName());
    // 删库
    super.removeById(id);
  }

  /** 改 */
  @Override
  public void edit(ChatGifEditDTO dto) {
    this.updateById(chatGifConvert.toEntity(dto));
  }

  @Override
  public int findMD5(String md5) {
    QueryWrapper<ChatGif> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("count(*) as count").eq("md5", md5);
    Map<String, Object> map = this.getMap(queryWrapper);
    return Integer.parseInt(map.get("count").toString());
  }
}
