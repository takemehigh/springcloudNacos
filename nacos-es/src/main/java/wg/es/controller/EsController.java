package wg.es.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.mapper.ObjectMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregator;
import org.elasticsearch.search.aggregations.metrics.TopHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wg.es.vo.SearchVo;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(value = "ES测试")
public class EsController {

    Logger logger = LoggerFactory.getLogger(EsController.class);

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @ApiOperation(value = "查看所有索引", notes = "")
    @GetMapping("/test")
    public String testEs() throws IOException {
        GetAliasesRequest request = new GetAliasesRequest();

        GetAliasesResponse res =restHighLevelClient.indices().getAlias(request, RequestOptions.DEFAULT);

        Map resMap = res.getAliases();

        String str = JSONObject.toJSONString(resMap);
        return str;
    }

    @ApiOperation(value = "测试搜索", notes = "")
    @PostMapping("/testSearch")
    @ApiParam(value = "desc of param" , required=false )
    public Object testSearch(@RequestBody(required = false) SearchVo searchVo) throws IOException {

        SearchRequest searchRequest = new SearchRequest("test");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
       /* searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        searchSourceBuilder.query()*/

        /*boolQuery.should(QueryBuilders.boolQuery().must(""))*/

        if(searchVo==null){
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        }
        else{
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            //BoolQueryBuilder orQuery = QueryBuilders.boolQuery();
            if(StringUtils.isNotEmpty(searchVo.getContent())){
                boolQuery.must(QueryBuilders.matchQuery("content",searchVo.getContent()));
            }
            if(StringUtils.isNotEmpty(searchVo.getContent2())){
                boolQuery.must(QueryBuilders.matchQuery("content2",searchVo.getContent2()));
            }
            if(StringUtils.isNotEmpty(searchVo.getContent3())){
                boolQuery.must(QueryBuilders.matchQuery("content3",searchVo.getContent3()));

            }
            if(StringUtils.isNotEmpty(searchVo.getExtend1())){
                boolQuery.must(QueryBuilders.matchQuery("extend1",searchVo.getExtend1()));

            }
            if(StringUtils.isNotEmpty(searchVo.getExtend2())){
                boolQuery.must(QueryBuilders.matchQuery("extend2",searchVo.getExtend2()));

            }
            if(ObjectUtils.isNotEmpty(searchVo.getId())){
                boolQuery.must(QueryBuilders.termQuery("id",searchVo.getId()));
            }
            searchSourceBuilder.query(boolQuery);
        }

        AggregationBuilder agg = AggregationBuilders.terms("contentStr")
                .field("content")
                .subAggregation(AggregationBuilders.topHits("group").size(1));
        //searchSourceBuilder.query(QueryBuilders.boolQuery().sho);
        searchSourceBuilder.aggregation(agg);
        logger.info("聚合条件{}",agg.toString());

        logger.info("查询条件{}",searchSourceBuilder.toString());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);

        Map<String,Aggregation> aggMap=searchResponse.getAggregations().getAsMap();

        ParsedTerms contentStr = (ParsedTerms)aggMap.get("contentStr");

        List<Terms.Bucket> bucketList= (List<Terms.Bucket>) contentStr.getBuckets();
        //contentStr
        List list =new ArrayList();
        for(Terms.Bucket bucket:contentStr.getBuckets()){
            TopHits topHits = bucket.getAggregations().get("group");
            SearchHits hits = topHits.getHits();
            SearchHit[] hitArray = hits.getHits();
            // 因为top_hits的siez=1所以不进行遍历直接取第一条数据
            SearchHit hit = hitArray[0];
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            list.add(sourceMap);

        }
        /*

        SearchHits hits = searchResponse.getHits();
        TotalHits totalHits = hits.getTotalHits();
        SearchHit[] hitslist = hits.getHits();
        List<Map> resultList= Arrays.stream(hitslist).map(e->{
            return e.getSourceAsMap();
        }).collect(Collectors.toList());

// the total number of hits, must be interpreted in the context of totalHits.relation
        long numHits = totalHits.value;
// whether the number of hits is accurate (EQUAL_TO) or a lower bound of the total (GREATER_THAN_OR_EQUAL_TO)
        TotalHits.Relation relation = totalHits.relation;
        float maxScore = hits.getMaxScore();
*/

        return list;
    }

    @PostMapping("/testAdd")
    @ApiParam(value = "desc of param" , required=true )
    @ApiOperation(value = "测试新增", notes = "")
    public Object testAdd(@RequestBody SearchVo searchVo) throws IOException {


        IndexRequest ir = new IndexRequest("test");
        String string = JSON.toJSONString(searchVo);

        ir.source(string, XContentType.JSON);

        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        builder.field("extend2","cnm");
        builder.endObject();
        ir.source(builder);

        IndexResponse indexResponse = restHighLevelClient.index(ir, RequestOptions.DEFAULT);
        return indexResponse.getResult();
    }


}
