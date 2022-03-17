package com.gameplat.admin.service;



import com.gameplat.admin.model.dto.LiveDomainParamDTO;


/**
 * 注单业务层
 * @author aguai
 * @since 2020-08-14
 */
public interface ILiveDomainService {

    //获取直播流量统计
    String getLiveDomainTrafficData(LiveDomainParamDTO param);

    //获取直播域名统计
    Object getLiveDomainList(LiveDomainParamDTO param);

}
