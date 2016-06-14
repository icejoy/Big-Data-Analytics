package org.iii.CvE.ES;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram.Interval;

public class CVE_ES_Connector {
	
	private Client client;
	private String INDEX = "cve";
	private String TYPE = "logs";
	
	
	@SuppressWarnings("resource")
	public CVE_ES_Connector(String host, int port, String index, String type) {

		client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(host, port));
		INDEX = index;
		TYPE = type;
		
	}
	
	public DateHistogram getDateDistribution(String timeField, long from, long to, String query, Interval interval)
	{
		@SuppressWarnings("deprecation")
		SearchRequestBuilder search = client.prepareSearch(INDEX)
		        .setTypes(TYPE)
		        .setSearchType(SearchType.COUNT)
		        .setQuery(
		        		QueryBuilders.boolQuery()
		        		.must(QueryBuilders.queryString(query))
		        		.must(QueryBuilders.rangeQuery(timeField).from(from).to(to))
		        );
		
		@SuppressWarnings("rawtypes")
		AggregationBuilder aggregation =
		        AggregationBuilders
		                .dateHistogram("agg")
		                .field(timeField)
		                .minDocCount(0)
		                .extendedBounds(from, to)
		                .interval(interval);
		
		SearchResponse result = search.addAggregation(aggregation).execute().actionGet();
		
		return result.getAggregations().get("agg");
	}
	
	public void close()
	{
		client.close();
	}

	public List<String> getDateList(String timeField, long from, long to, Interval interval) {
		
		List<String> list = new ArrayList<String>();
		
		DateHistogram aggs = getDateDistribution(timeField, from, to ,"*" , interval);
		
		for( Bucket b : aggs.getBuckets())
		{
			list.add(b.getKeyAsText().toString());
		}
		
		return list;
	}

	public List<Date> getDateListDate(String timeField, long from, long to, Interval interval) {
		
		List<Date> list = new ArrayList<Date>();
		
		DateHistogram aggs = getDateDistribution(timeField, from, to ,"*" , interval);
		
		for( Bucket b : aggs.getBuckets())
		{
			list.add(new Date(b.getKeyAsNumber().longValue()));
		}
		
		return list;
		
	}

}
