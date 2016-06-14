package org.iii.CvE.Parse;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

public class parser_CvE_Extract implements parser_CvE_Interface
{
	ArrayList<String> CVEID = new ArrayList<String>();
	ArrayList<String> Published = new ArrayList<String>();

	public parser_CvE_Extract()
	{
		init();
	}

	public void init()
	{
		CVEID.clear();
		Published.clear();
	}

	public void parse(List<Element> list, String s1, String s2,
			ArrayList<String> cve, ArrayList<String> pub)
	{
		Extract_CVEID_Published(list);
	}

	@SuppressWarnings("unchecked")
	public void Extract_CVEID_Published(List<Element> list)
	{
		List<Element> list1, list2;
		for (int i = 0; i < list.size(); i++)
		{
			if ("Vulnerability".equals(list.get(i).getName()))
			{
				list1 = list.get(i).elements();
				for (int j = 0; j < list1.size(); j++)
				{
					if ("Notes".equals(list1.get(j).getName()))
					{
						list2 = list1.get(j).elements();
						if (list2.size() > 2)
						{
							CVEID.add(list1.get(0).getText());
							Published.add(list2.get(1).getText());
						}
						break;
					}
				}
			}
		}
	}

	public ArrayList<String> get_CVEID()
	{
		return CVEID;
	}

	public ArrayList<String> get_PUBLISHED()
	{
		return Published;
	}

	public ArrayList<String> get_ID()
	{
		return null;
	}
}
