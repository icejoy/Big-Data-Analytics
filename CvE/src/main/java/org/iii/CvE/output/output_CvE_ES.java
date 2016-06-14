package org.iii.CvE.output;

import java.util.ArrayList;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.json.JSONObject;

@SuppressWarnings("resource")
public class output_CvE_ES implements output_CvE_Interface
{
	String INDEX, type, key1 = "cve_id", key2 = "published_date";
	Client client;
	Settings settings;
	ArrayList<JSONObject> CVE_JsonList;
	ArrayList<String> ESID;

	public void output(String[] elasticsearch, ArrayList<String> cve,
			ArrayList<String> pub, ArrayList<String> esid)
	{
		settings(elasticsearch);
		StringList_To_JsonList(cve, pub,esid);
		Save();
	}

	public void settings(String[] elasticsearch)
	{
		settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", elasticsearch[0]).build();
		client = new TransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(
						elasticsearch[1], Integer.parseInt(elasticsearch[2])));
		INDEX = elasticsearch[3];
		type = elasticsearch[4];
		key1 = elasticsearch[5];
		key2 = elasticsearch[6];
	}

	public void StringList_To_JsonList(ArrayList<String> cve,
			ArrayList<String> pub, ArrayList<String> esid)
	{
		for (int i = 0; i < cve.size(); i++)
		{
			CVE_JsonList.add(new JSONObject());
			CVE_JsonList.get(CVE_JsonList.size() - 1).put(key1, cve.get(i));
			CVE_JsonList.get(CVE_JsonList.size() - 1).put(key2, pub.get(i));
		}
		if (esid.size() > 0)
		{
			ESID = new ArrayList<String>(esid);
		}
		else
		{
			ESID = null;
		}
	}

	@SuppressWarnings("unused")
	public void Save()
	{
		// Index all json to ES
		if (ESID.size() > 0)
		{
			for (int i = 0; i < CVE_JsonList.size(); i++)
			{
				try
				{
					IndexResponse response = client
							.prepareIndex(INDEX, type, ESID.get(i))
							.setSource(CVE_JsonList.get(i).toString()).get();
				}
				catch (Exception e)
				{
					System.out.println(e);
					client.close();
				}
			}
		}
		else
		{
			for (JSONObject cveid : CVE_JsonList)
			{
				try
				{
					IndexResponse response = client.prepareIndex(INDEX, type)
							.setSource(cveid.toString()).get();
				}
				catch (Exception e)
				{
					System.out.println(e);
					client.close();
				}
			}
		}

		client.close();
	}

}
