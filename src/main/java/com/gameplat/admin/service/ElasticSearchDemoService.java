package com.gameplat.admin.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameplat.admin.model.dto.MessageDistributeDTO;
import com.gameplat.admin.model.dto.MessageFeedbackAddDTO;
import com.gameplat.admin.model.vo.MessageFeedbackVO;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.elasticsearch.service.IBaseElasticsearchService;
import com.gameplat.model.entity.message.MessageFeedback;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lily
 * @description
 * @date 2022/1/14
 */
@Slf4j
@Service
public class ElasticSearchDemoService {

  @Autowired private IBaseElasticsearchService elasticsearchService;

  @Autowired private RestHighLevelClient client;

  /** 创建索引 */
  public void createIndex(String index) {
    CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
    CreateIndexResponse response = null;
    try {
      response = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      e.printStackTrace();
    }
    log.info("创建索引：{}, index");
  }

  /** 添加数据 */
  public void saves(String index, MessageDistributeDTO dto) {
    try {
      IndexRequest indexRequest = new IndexRequest(index, "_doc", String.valueOf(dto.getId()));
      indexRequest.source(new ObjectMapper().writeValueAsString(dto), XContentType.JSON);

      IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);

      client.indexAsync(
          indexRequest,
          RequestOptions.DEFAULT,
          new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
              String index = indexResponse.getIndex();
              String type = indexResponse.getType();
              String id = indexResponse.getId();
              long version = indexResponse.getVersion();

              log.info("Index: {}, Type: {}, Id: {}, Version: {}", index, type, id, version);

              if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                log.info("写入文档");
              } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                log.info("修改文档");
              }
              ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
              if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                log.warn("部分分片写入成功");
              }
              if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                  String reason = failure.reason();
                  log.warn("失败原因: {}", reason);
                }
              }
            }

            @Override
            public void onFailure(Exception e) {
              log.error(e.getMessage(), e);
            }
          });
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException("es写入数据异常");
    }
  }

  /** 根据id查询反馈内容 */
  public MessageFeedbackVO detail(String index, Long id) {
    GetRequest getRequest = new GetRequest(index, "_doc", String.valueOf(id));
    try {
      GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
      if (getResponse.isExists()) {
        String source = getResponse.getSourceAsString();
        MessageFeedbackVO vo = JSON.parseObject(source, MessageFeedbackVO.class);
        return vo;
      }
    } catch (IOException e) {
      log.error("查看失败！原因: {}", e.getMessage(), e);
    }
    return null;
  }

  /** 根据条件查询反馈内容 */
  public List<MessageFeedbackVO> lists(String index, MessageFeedback entity) {
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    sourceBuilder.sort(new FieldSortBuilder("update_time").order(SortOrder.ASC));
    //        sourceBuilder.query(QueryBuilders.matchAllQuery());
    // 查询对象
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    if (ObjectUtil.isNotEmpty(entity.getId())) {
      boolQueryBuilder.must(QueryBuilders.matchQuery("id", entity.getId()));
    }
    if (ObjectUtil.isNotEmpty(entity.getTitle())) {
      boolQueryBuilder.must(QueryBuilders.matchQuery("title", entity.getTitle()));
    }

    //        if (StringUtils.isNotBlank(entity.getAuthor())) {
    //            boolQueryBuilder.must(QueryBuilders.matchQuery("author", entity.getAuthor()));
    //        }
    //        if (null != entity.getStatus()) {
    //            boolQueryBuilder.must(QueryBuilders.termQuery("status", entity.getStatus()));
    //        }
    //        if (StringUtils.isNotBlank(entity.getSellTime())) {
    //            boolQueryBuilder.must(QueryBuilders.termQuery("sellTime", entity.getSellTime()));
    //        }
    //        if (StringUtils.isNotBlank(entity.getCategories())) {
    //            String[] categoryArr = entity.getCategories().split(",");
    //            List<Integer> categoryList =
    // Arrays.asList(categoryArr).stream().map(e->Integer.valueOf(e)).collect(Collectors.toList());
    //            BoolQueryBuilder categoryBoolQueryBuilder = QueryBuilders.boolQuery();
    //            for (Integer category : categoryList) {
    //                categoryBoolQueryBuilder.should(QueryBuilders.termQuery("category",
    // category));
    //            }
    //            boolQueryBuilder.must(categoryBoolQueryBuilder);
    //        }

    sourceBuilder.query(boolQueryBuilder);

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices(index);
    searchRequest.source(sourceBuilder);

    try {
      SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

      RestStatus restStatus = searchResponse.status();
      if (restStatus != RestStatus.OK) {
        return null;
      }

      List<MessageFeedbackVO> list = new ArrayList<>();
      SearchHits searchHits = searchResponse.getHits();
      for (SearchHit hit : searchHits.getHits()) {
        String source = hit.getSourceAsString();
        MessageFeedbackVO book = JSON.parseObject(source, MessageFeedbackVO.class);
        list.add(book);
      }

      TimeValue took = searchResponse.getTook();
      log.info("查询成功！请求参数: {}, 用时{}毫秒", searchRequest.source().toString(), took.millis());

      return list;
    } catch (IOException e) {
      log.error("查询失败！原因: {}", e.getMessage(), e);
    }

    return null;
  }

  /** 查询所有反馈内容 */
  public List<MessageFeedbackVO> list(String index, MessageFeedback entity) throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices(index);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
    searchRequest.source(searchSourceBuilder);
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    RestStatus restStatus = searchResponse.status();
    if (restStatus != RestStatus.OK) {
      return null;
    }
    List<MessageFeedbackVO> list = new ArrayList<>();
    SearchHits searchHits = searchResponse.getHits();
    for (SearchHit hit : searchHits.getHits()) {
      String sources = hit.getSourceAsString();
      MessageFeedbackVO book = JSON.parseObject(sources, MessageFeedbackVO.class);
      list.add(book);
    }

    TimeValue took = searchResponse.getTook();
    log.info("查询成功！请求参数: {}, 用时{}毫秒", searchRequest.source().toString(), took.millis());

    return list;
  }
}
