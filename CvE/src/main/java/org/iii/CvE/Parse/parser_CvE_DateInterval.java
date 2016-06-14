package org.iii.CvE.Parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.Element;
import org.iii.CvE.Utils.String_Date_Convert;

public class parser_CvE_DateInterval implements parser_CvE_Interface
{
	Date From, To;
	String_Date_Convert sdc;
	boolean Have_From = false, Have_To = false;
	ArrayList<String> CVEID = new ArrayList<String>();
	ArrayList<String> Published = new ArrayList<String>();

	public parser_CvE_DateInterval()
	{
		sdc = new String_Date_Convert();
	}

	public void init()
	{
		CVEID.clear();
		Published.clear();
	}

	public void parse(List<Element> list, String s1, String s2,
			ArrayList<String> cve, ArrayList<String> pub)
	{
		set_Interval(s1, s2);
		Extract_Interval(cve, pub);
	}

	public void set_Interval(String from, String to)
	{
		if (!from.equals("null"))
		{
			Have_From = true;
			sdc.set_String(from);
			sdc.String_To_Date();
			From = sdc.get_Date();
		}

		if (!to.equals("null"))
		{
			Have_To = true;
			sdc.set_String(to);
			sdc.String_To_Date();
			To = sdc.get_Date();
		}
	}

	public void Extract_Interval(ArrayList<String> cveid, ArrayList<String> pub)
	{
		init();
		Date date;
		for (int i = 0; i < pub.size(); i++)
		{
			sdc.set_String(pub.get(i));
			sdc.String_To_Date();
			date = sdc.get_Date();
			if ((!Have_From && !Have_To)
					|| (!Have_From && Have_To && To.after(date))
					|| (Have_From && From.before(date) && !Have_To)
					|| (Have_From && Have_To && ((From.equals(date) || To
							.equals(date)) || (From.before(date) && To
							.after(date)))))
			{
				CVEID.add(cveid.get(i));
				Published.add(pub.get(i));
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
	{return null;}
}
