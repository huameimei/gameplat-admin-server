package com.gameplat.admin.model.dto;

import com.gameplat.admin.enums.TimeTypeEnum;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import lombok.Data;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.Serializable;

/**
 * @Author kb
 * @Date 2022/3/1 22:15
 * @Version 1.0
 */
@Data
public class GameKGLotteryDto implements Serializable {


    /**
     * 账号
     */
    private String account;

    /**
     * 代理账号
     */
    private String superAccount;

    /**
     * 是否包含下级
     */
    private boolean flag;

    /**
     * 彩票类型
     */
    private int lotteryType;

    /**
     * 彩种
     */
    private String lotteryCode;


    /**
     * 开始时间
     */
    private String startTime;


    /**
     * 结束时间
     */
    private String endTime;


    private String agentPath;


    public static BoolQueryBuilder buildBetRecordSearch(GameKGLotteryDto dto) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(dto.getAccount())) {
            builder.must(QueryBuilders.termQuery("account", dto.getAccount()));
        }
        if (dto.isFlag()) {
            builder.must(QueryBuilders.matchQuery("superPath", dto.getSuperAccount()));
        } else {
            builder.must(QueryBuilders.termQuery("superPath", dto.getSuperAccount()));
        }
        if (dto.getLotteryType() > 0) {
            builder.must(QueryBuilders.termQuery("lotteryType", dto.getLotteryType()));
        }
        if (StringUtils.isNotEmpty(dto.getLotteryCode())) {
            builder.must(QueryBuilders.termQuery("gameKind", dto.getLotteryCode()));
        }
        String keyword = "betTime.keyword";
        builder.must(QueryBuilders.rangeQuery(keyword)
                .from(dto.getStartTime())
                .to(dto.getEndTime() == null ? "now" : dto.getEndTime())
                .format(DateUtils.DATE_TIME_PATTERN));

        return builder;
    }
}
