package org.iii.CvE.Utils;

import java.util.ArrayList;

import org.json.JSONObject;

public class List_Merge_Json
{
	String key1, key2;
	ArrayList<String> list1, list2;
	ArrayList<JSONObject> Json_List = new ArrayList<JSONObject>();

	public void set_list1_keyValue(ArrayList<String> l, String keyValue)
	{
		list1 = null;
		list1 = new ArrayList<String>(l);
		key1 = keyValue;
	}

	public void set_list2_keyValue(ArrayList<String> l, String keyValue)
	{
		list2=null;
		list2 = new ArrayList<String>(l);
		key2 = keyValue;
	}

	public void Merge()
	{
		Json_List.clear();
		if (list1.size() != list2.size())
		{
			System.out.println("list1 and list2 size not same");
		}
		else
		{
			for(int i=0;i<list1.size();i++)
			{
				Json_List.add(new JSONObject());
				Json_List.get(Json_List.size()-1).put(key1, list1.get(i));
				Json_List.get(Json_List.size()-1).put(key2, list2.get(i));
			}
		}
	}

	public ArrayList<JSONObject> get_Merge_Result()
	{return Json_List;}
}
