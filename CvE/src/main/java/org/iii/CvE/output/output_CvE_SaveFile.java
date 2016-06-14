package org.iii.CvE.output;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.json.JSONObject;

public class output_CvE_SaveFile implements output_CvE_Interface
{
	String FilePath, key1 = "cve_id", key2 = "published_date";
	ArrayList<JSONObject> CVE_JsonList;

	public void output(String[] setting, ArrayList<String> cve,
			ArrayList<String> pub,ArrayList<String> esid)
	{
		settings(setting);
		StringList_To_JsonList(cve, pub,esid);
		Save();
	}

	public void settings(String[] setting)
	{
		FilePath = setting[0];
		key1 = setting[1];
		key2 = setting[2];
	}

	public void StringList_To_JsonList(ArrayList<String> cve,
			ArrayList<String> pub,ArrayList<String> esid)
	{
		for (int i = 0; i < cve.size(); i++)
		{
			CVE_JsonList.add(new JSONObject());
			CVE_JsonList.get(CVE_JsonList.size() - 1).put(key1, cve.get(i));
			CVE_JsonList.get(CVE_JsonList.size() - 1).put(key2, pub.get(i));
		}
	}

	public void Save()
	{
		try
		{
			File file = new File(FilePath);
			if (!file.exists())
			{
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file);
			for (JSONObject cveid : CVE_JsonList)
			{
				writer.write(cveid.getString(key1) + "\t");
				writer.write(cveid.getString(key2) + "\n");
			}
			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
