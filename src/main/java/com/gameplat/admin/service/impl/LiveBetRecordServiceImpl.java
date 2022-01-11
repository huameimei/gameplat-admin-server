package com.gameplat.admin.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.enums.LiveGame;
import com.gameplat.admin.mapper.LiveBetRecordMapper;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.domain.LiveBetRecord;
import com.gameplat.admin.model.dto.LiveBetRecordQueryDTO;
import com.gameplat.admin.model.vo.LiveBetRecordVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.LiveBetRecordService;
import com.gameplat.admin.service.game.GameApi;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.game.LiveGameResult;
import com.gameplat.elasticsearch.page.PageResponse;
import com.gameplat.elasticsearch.service.IBaseElasticsearchService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class LiveBetRecordServiceImpl implements LiveBetRecordService {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private LiveBetRecordMapper liveBetRecordMapper;

    @Resource
    private IBaseElasticsearchService elasticsearchService;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public GameApi getGameApi(String liveCode) {
        GameApi api = applicationContext.getBean(liveCode + GameApi.Suffix, GameApi.class);
        TransferTypesEnum tt = TransferTypesEnum.get(liveCode);
        // 1代表是否额度转换
        if (tt == null || tt.getType() != 1) {
            throw new ServiceException("游戏未接入");
        }
        return api;
    }

    @Override
    public PageDtoVO<LiveBetRecordVO> queryPageBetRecord(Page<LiveBetRecordVO> page, LiveBetRecordQueryDTO dto) {
        //TODO 调用ES服务
        PageDtoVO<LiveBetRecordVO> pageDtoVO = new PageDtoVO();
        Page resultPage = new Page();
        //创建一个要搜索的索引库
        SearchSourceBuilder builder = new SearchSourceBuilder();
        String[] includes = new String[]{"billNo","account","gameType","betTime","betAmount","validAmount",
            "winAmount","gameKind","settle","gmtBetTime","statTime","createTime","betContent"};
        builder.fetchSource(includes,new String[]{});
        Integer pageNum = NumberUtil.parseInt(String.valueOf(page.getCurrent()));
        Integer pageSize = NumberUtil.parseInt(String.valueOf(page.getSize()));
        PageResponse result = elasticsearchService.search(dto.getLiveCode(),builder,LiveBetRecord.class,pageNum,pageSize);
        resultPage.setRecords(result.getList());
        Map<String, Object> otherData = new HashMap<>();
        otherData.put("totalData", null);
        pageDtoVO.setPage(resultPage);
        pageDtoVO.setOtherData(otherData);
        return pageDtoVO;
    }

    @Override
    public LiveGameResult getGameResult(String liveCode, String billNo) throws Exception {
        // TODO 直接连游戏查询结果
        GameApi gameApi = getGameApi(liveCode);
        LiveGameResult liveGameResult = gameApi.getGameResult(billNo);;
        if (StringUtils.isBlank(liveGameResult.getData())) {
            throw new ServiceException(LiveGame.getName(liveCode) + "暂不支持查看游戏结果");
        }
        return liveGameResult;
    }

    @Override
    public List<ActivityStatisticItem> xjAssignMatchDml(Map map) {
        List<ActivityStatisticItem> activityStatisticItemVOList = new ArrayList<>();
        //根据用户集合批量查询所有xj的注单信息
        List<LiveBetRecord> xjBetRecordList = liveBetRecordMapper.queryGameBetRecords(map);
        if (CollectionUtils.isNotEmpty(xjBetRecordList)) {
            //过滤出指定比赛的注单信息
            List<LiveBetRecord> xjAssignMatchBetRecordList = xjBetRecordList.stream().filter(
                    item -> JSONObject.parseObject(item.getBetContent().replace("[", "").replace("]", "")).getInteger("eventId").equals(map.get("matchId"))).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(xjAssignMatchBetRecordList)) {
                //根据账号对指定比赛的注单信息进行分组
                Map<String, List<LiveBetRecord>> userGroupMap = xjAssignMatchBetRecordList.stream().collect(Collectors.groupingBy(LiveBetRecord::getAccount));
                List<String> userNameList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty((List<String>) map.get("userNameList"))) {
                    userNameList = (List<String>) map.get("userNameList");
                } else {
                    for (String userName : userGroupMap.keySet()) {
                        userNameList.add(userName);
                    }
                }

                for (String userName : userNameList) {
                    List<LiveBetRecord> liveBetRecordList = userGroupMap.get(userName);
                    //计算该用户这场比赛的总打码量
                    BigDecimal validAmount = BigDecimal.ZERO;
                    if (CollectionUtils.isNotEmpty(liveBetRecordList)) {
                        for (LiveBetRecord record : liveBetRecordList) {
                            validAmount = validAmount.add((record.getValidAmount() == null ? BigDecimal.ZERO : new BigDecimal(record.getValidAmount())));
                        }
                    }
                    ActivityStatisticItem activityStatisticItemVO = new ActivityStatisticItem();
                    activityStatisticItemVO.setUserName(userName);
                    activityStatisticItemVO.setValidAmount(validAmount);
                    activityStatisticItemVOList.add(activityStatisticItemVO);
                }
            }
        }

        return activityStatisticItemVOList;
    }
}
