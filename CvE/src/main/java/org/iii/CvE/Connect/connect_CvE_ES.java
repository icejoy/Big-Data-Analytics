package org.iii.CvE.Connect;

import java.util.ArrayList;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;

public class connect_CvE_ES implements connect_CvE_Interface
{
	Client client;
	Settings settings;
	String INDEX = "cve";
	String TYPE = "log";
	String key1 = "cve_id";
	String key2 = "published_date";
	ArrayList<String> CVEID_List = new ArrayList<String>();
	ArrayList<String> Published_List = new ArrayList<String>();
	ArrayList<String> ESID_List = new ArrayList<String>();

	public void connect(String CluserName, String host, int port, String index,
			String type, String key1, String key2)
	{
		init();
		set_ES_Connector(CluserName, host, port, index, type, key1, key2);
		read();
	}

	public void init()
	{
		CVEID_List.clear();
		Published_List.clear();
	}

	@SuppressWarnings("resource")
	public void set_ES_Connector(String CluserName, String host, int port,
			String index, String type, String k1, String k2)
	{
		settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", CluserName).build();
		client = new TransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(host, port));

		INDEX = index;
		TYPE = type;
		key1 = k1;
		key2 = k2;
	}

	public void read()
	{
		// int count=0;
		SearchResponse response = client.prepareSearch(INDEX)
				.setScroll(new TimeValue(60000)).setSize(100).execute()
				.actionGet();
		while (true)
		{
			// count+=response.getHits().getHits().length;
			for (SearchHit hit : response.getHits().getHits())
			{
				CVEID_List.add(hit.getSource().get(key1).toString());
				Published_List.add(hit.getSource().get(key2).toString());
				ESID_List.add(hit.getId());
				// System.out.println("cveid : "+hit.getSource().get("cve_id"));
				// System.out.println("published_date : "+hit.getSource().get("published_date"));
			}
			response = client.prepareSearchScroll(response.getScrollId())
					.setScroll(new TimeValue(50000)).execute().actionGet();

			// Break condition: No hits are returned
			if (response.getHits().getHits().length == 0)
			{
				break;
			}
		}
		// System.out.println("count:"+count);
	}

	public ArrayList<String> get_CVEID_List()
	{
		return CVEID_List;
	}

	public ArrayList<String> get_Published_Date_List()
	{
		return Published_List;
	}

	public ArrayList<String> get_ESID_List()
	{
		return ESID_List;
	}
}
