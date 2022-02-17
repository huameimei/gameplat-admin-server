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
import com.gameplat.admin.model.domain.ChatGif;
import com.gameplat.admin.model.dto.ChatGifEditDTO;
import com.gameplat.admin.model.vo.ChatGifVO;
import com.gameplat.admin.service.ChatGifService;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.compent.oss.FileStorageEnum;
import com.gameplat.common.compent.oss.FileStorageProvider;
import com.gameplat.common.compent.oss.FileStorageStrategyContext;
import com.gameplat.common.compent.oss.config.FileConfig;
import com.gameplat.common.constant.FileConstant;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.common.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

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

    @Autowired
    private ConfigService configService;

    @Autowired
    private FileStorageStrategyContext fileStorageStrategyContext;

    /** 分页列表 */
    @Override
    public IPage<ChatGifVO> page(PageDTO<ChatGif> page, String name) {
        IPage<ChatGifVO> pageList = this.lambdaQuery()
                .like(StringUtils.isNotNull(name), ChatGif::getName, name)
                .orderByDesc(ChatGif::getCreateTime)
                .page(page)
                .convert(chatGifConvert::toVo);
        FileConfig fileConfig = configService.getDefaultConfig(DictTypeEnum.FILE_CONFIG, FileConfig.class);
        for (int i = 0; i < pageList.getRecords().size(); i++) {
            if (!pageList.getRecords().get(i).getFileUrl().startsWith("http") || !pageList.getRecords().get(i).getFileUrl().startsWith("https")) {
                pageList.getRecords().get(i).setFileUrl(fileConfig.getEndpoint()+"/" + pageList.getRecords().get(i).getFileUrl());
            }
        }
        return pageList;
    }

    /** 增 */
    @Override
    public void add(MultipartFile file, String name) throws IOException {
        if (!isImage(file)){
            throw new ServiceException("不支持此格式！");
        }
        //重新生成文件名称
        String fKey = FileUtils.getRandomName(file.getOriginalFilename());

        String fType = FileConstant.PICTURE;

        //上传图片
        FileConfig fileConfig = configService.getDefaultConfig(DictTypeEnum.FILE_CONFIG, FileConfig.class);
        FileStorageProvider fileStorageProvider = fileStorageStrategyContext.getProvider(fileConfig);
        //上传成功，返回图片地址
        String url = fileStorageProvider.upload(file.getResource().getFile());
        fType = FilenameUtils.getBaseName(url);
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
        ChatGif chatGif = new ChatGif();
        BufferedImage bufferedImage = null;
        try {
            // 通过MultipartFile得到InputStream，从而得到BufferedImage
            bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage!=null){
                chatGif.setHeight(bufferedImage.getHeight());
                chatGif.setWidth(bufferedImage.getWidth());
            }
        } catch (IOException e) {
            log.info("异常原因{}", e);
            throw new ServiceException("此图片已破损");
        }
//        // 特殊处理本地服务器
//        if(FileStorageEnum.LOCAL.getValue() == fileConfig.getProvider()){
//            //服务器名称
//            chatGif.setServiceFileName(DyDataSourceContextHolder.getTenant() + "/" + fType + fKey);
//        }else{
//            chatGif.setServiceFileName(fType + fKey);
//        }
        chatGif.setServiceFileName(fType + fKey);  //picture/randomName
        chatGif.setName(name);
        if (bufferedImage!=null){
            chatGif.setHeight(bufferedImage.getHeight());
            chatGif.setWidth(bufferedImage.getWidth());
        }
        chatGif.setFileUrl(url);
        chatGif.setFileType(FileUtils.getFileType(fKey));
        //存储文件名
        chatGif.setStoreFileName(fKey);
        chatGif.setMd5(SecureUtil.md5(file.getInputStream()));
        chatGif.setServiceProvider(fileConfig.getProvider());

        //如果是异步上传，如果响应时间太长，request关闭，也就获取不到请求头的token，也就拿不到LoginAppUser对象
        //所以需要在异步线程将上传人加入到本地线程，在保存上传记录时，上传者从本地线程中拿值
        String userName = JSONObjectContext.getJSONObjectContext("userName");
        if(StringUtils.isNotEmpty(userName)){
            chatGif.setCreateBy(userName);
        }
        save(chatGif);
    }

    /** 删 */
    @Override
    public void remove(Integer id) {
        ChatGif chatGif = this.getById(id);
        if(ObjectUtil.isEmpty(chatGif)){
            throw new ServiceException("存储位置未知");
        }
        // 服务器名称
        String serviceFileName = chatGif.getServiceFileName();
        //本地
        if(FileStorageEnum.LOCAL.getValue() == chatGif.getServiceProvider()){
            //存储文件名
            String fType = FileUtils.getFileTypeName(chatGif.getStoreFileName());
            // serviceFileName =  ossPath + "/" + tenantId + "/" + fType + sysOss.getStoreFileName();
        }
        //删储服务器图片
        FileConfig fileConfig = configService.getDefaultConfig(DictTypeEnum.FILE_CONFIG, FileConfig.class);
        FileStorageProvider fileStorageProvider = fileStorageStrategyContext.getProvider(fileConfig);
        fileStorageProvider.delete(serviceFileName);
        //删库
        this.remove(id);
    }

    /** 改 */
    @Override
    public void edit(ChatGifEditDTO dto) {
        this.updateById(chatGifConvert.toEntity(dto));
    }

    @Override
    public int findMD5(String md5) {
        QueryWrapper<ChatGif> queryWrapper = new QueryWrapper<>();
        queryWrapper.select( "count(*) as count").eq("md5", md5);
        Map<String, Object> map = this.getMap(queryWrapper);
        return Integer.parseInt(map.get("count").toString());
    }

    /** 判断文件是否是图片 */
    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (StringUtils.isBlank(contentType) || !contentType.matches("image/(bmp|jpg|jpeg|png|gif)")) {
            return false;
        }
        return true;
    }



}
