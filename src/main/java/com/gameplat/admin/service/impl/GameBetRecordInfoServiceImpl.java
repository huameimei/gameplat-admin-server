package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.convert.GameBetRecordConvert;
import com.gameplat.admin.mapper.LiveBetRecordMapper;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.doc.GameBetRecord;
import com.gameplat.admin.model.domain.LiveBetRecord;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetRecordInfoService;
import com.gameplat.admin.service.GameConfigService;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.GamePlatformEnum;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.game.GameBizBean;
import com.gameplat.common.game.LiveGameResult;
import com.gameplat.common.game.api.GameApi;
import com.gameplat.elasticsearch.page.PageResponse;
import com.gameplat.elasticsearch.service.IBaseElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameBetRecordInfoServiceImpl implements GameBetRecordInfoService {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private LiveBetRecordMapper liveBetRecordMapper;

    @Resource
    private IBaseElasticsearchService baseElasticsearchService;

    @Resource
    private GameBetRecordConvert gameBetRecordConvert;

    @Resource
    private GameConfigService gameConfigService;

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
    public PageDtoVO<GameBetRecordVO> queryPageBetRecord(Page<GameBetRecordVO> page, GameBetRecordQueryDTO dto) {
        QueryBuilder builder = GameBetRecord.buildBetRecordSearch(dto);
        // todo betTime
        SortBuilder<FieldSortBuilder> sortBuilder = SortBuilders.fieldSort("betTime.keyword").order(SortOrder.DESC);
        // todo kgsit
        String indexName = ContextConstant.ES_INDEX.BET_RECORD_ + "kgsit";
        PageResponse<GameBetRecord> result = baseElasticsearchService.search(builder, indexName, GameBetRecord.class,
                (int) page.getCurrent(), (int) page.getSize(), sortBuilder);

        List<GameBetRecordVO> betRecordList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(result.getList())) {
            result.getList().forEach(o -> betRecordList.add(gameBetRecordConvert.toVo(o)));
        }

        Page<GameBetRecordVO> resultPage = new Page<>();
        resultPage.setRecords(betRecordList);

        Map<String, Object> otherData = new HashMap<>(8);
        // todo es 合计
        otherData.put("totalData", null);

        PageDtoVO<GameBetRecordVO> pageDtoVO = new PageDtoVO<>();
        pageDtoVO.setPage(resultPage);
        pageDtoVO.setOtherData(otherData);

        return pageDtoVO;
    }

    @Override
    public LiveGameResult getGameResult(GameBetRecordQueryDTO dto) throws Exception {
        // TODO 直接连游戏查询结果
        GameApi gameApi = getGameApi(dto.getPlatformCode());
        GameBizBean gameBizBean = new GameBizBean();
        gameBizBean.setOrderNo(dto.getBillNo());
        gameBizBean.setConfig(gameConfigService.queryGameConfigInfoByPlatCode(dto.getPlatformCode()));
        LiveGameResult liveGameResult = gameApi.getGameResult(gameBizBean);
        if (StringUtils.isBlank(liveGameResult.getData())) {
            throw new ServiceException(GamePlatformEnum.getName(dto.getPlatformCode()) + "暂不支持查看游戏结果");
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
