package org.iii.CvE.Utils;

import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONObject;
import org.dom4j.*;
import org.dom4j.io.*;

public class CVEXmlReader
{
	Date From, To;
	String FilePath;
	SimpleDateFormat sdf;
	ArrayList<Date> Published = new ArrayList<Date>();
	ArrayList<String> CVEID = new ArrayList<String>();
	ArrayList<JSONObject> CVE_JsonList = new ArrayList<JSONObject>();

	public CVEXmlReader()
	{
		FilePath = "https://cve.mitre.org/data/downloads/allitems-cvrf-year-2016.xml";
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public ArrayList<String> get_CVEID()
	{
		return CVEID;
	}

	public ArrayList<Date> get_CVE_Published()
	{
		return Published;
	}

	public ArrayList<JSONObject> get_CVE_JsonList()
	{
		return CVE_JsonList;
	}

	public void set_DateRange(Date from, Date to)
	{
		From = from;
		To = to;
		System.out.println("from: " + From);
		System.out.println("to: " + To);
	}

	public void Filter_PublishedDate(boolean TF, ArrayList<String> id,
			ArrayList<Date> pub)
	{
		CVE_JsonList.clear();
		for (int i = 0; i < pub.size(); i++)
		{
			if (!TF
					|| (TF && (From.equals(pub.get(i)) || To.equals(pub.get(i)) || (From
							.before(pub.get(i)) && To.after(pub.get(i))))))
			{
				CVE_JsonList.add(new JSONObject());
				CVE_JsonList.get(CVE_JsonList.size() - 1).put("cve_id",
						id.get(i));
				CVE_JsonList.get(CVE_JsonList.size() - 1).put("published_date",
						(String) sdf.format(pub.get(i)));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void ReadXML()
	{
		long d1,d2;
		System.out.println("\r\n"+"正在讀取 CVE XML");
//		System.out.println("現在時間："+new Date());
		d1=new Date().getTime();
		try
		{
			SAXReader reader = new SAXReader();
			Document doc = reader.read(FilePath);
			Element element = doc.getRootElement();
			List<Element> list = element.elements();
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
								Published.add(String_To_Date(list2.get(1)
										.getText()));
							}
							break;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("已讀完 CVE XML");
//		System.out.println("現在時間："+new Date());
		d2=new Date().getTime();
		System.out.println("共花費："+(d2-d1)/1000+"秒"+"\r\n");
	}

	public Date String_To_Date(String str)
	{
		Date date;
		try
		{
			date = sdf.parse(str);
			return date;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<JSONObject> Run(Date from, Date to)
	{
		ReadXML();
		set_DateRange(from, to);
		Filter_PublishedDate(true, CVEID, Published);
		return get_CVE_JsonList();
	}

	// public static void main(String arge[]) {
	// CVEXmlReader ml = new CVEXmlReader("allitems-cvrf-year-2016.xml");
	//
	// ml.ReadXML();
	// ml.get_DateRange(ml.String_To_Date("2016-05-14"),
	// ml.String_To_Date("2016-05-17"));
	// ml.Filter_PublishedDate();
	// System.out.println(ml.CVE_JsonList.toString());
	//
	// }
}