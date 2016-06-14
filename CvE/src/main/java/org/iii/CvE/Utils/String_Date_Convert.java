package org.iii.CvE.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class String_Date_Convert
{
	Date date;
	String str;
	SimpleDateFormat sdf;

	public String_Date_Convert()
	{
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public void set_Date(Date d)
	{
		date = d;
	}

	public void set_String(String s)
	{
		str = s;
	}

	public void String_To_Date()
	{
		try
		{
			date = sdf.parse(str);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void Date_To_String()
	{
		str=sdf.format(date);
	}
	
	public Date get_Date()
	{return date;}
	
	public String get_String()
	{return str;}
}
