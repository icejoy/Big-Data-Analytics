package org.iii.CvE.Parse;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

public class parser_CvE_Check implements parser_CvE_Interface
{
	boolean list1_check = false, list2_check = false;
	ArrayList<String> CVE1;// http
	ArrayList<String> CVE2;// es
	ArrayList<String> Pub1;
	ArrayList<String> Pub2;
	ArrayList<String> ESID;
	ArrayList<String> CVE = new ArrayList<String>();
	ArrayList<String> PUB = new ArrayList<String>();

	public void init()
	{
		CVE.clear();
		PUB.clear();
		list1_check = false;
		list2_check = false;
		CVE1 = null;
		CVE2 = null;
		Pub1 = null;
		Pub2 = null;
		ESID = null;
	}

	public void parse(List<Element> list, String s1, String s2,
			ArrayList<String> cve, ArrayList<String> pub)
	{
		if (list1_check && list2_check)
		{
			CHECK();
		}
		else if (s1.equals("haved") && !s2.equals("haved"))
		{
			list1_check = true;
			CVE1 = new ArrayList<String>(cve);
			Pub1 = new ArrayList<String>(pub);
		}
		else if (!s1.equals("haved") && s2.equals("haved"))
		{
			list2_check = true;
			CVE2 = new ArrayList<String>(cve);
			Pub2 = new ArrayList<String>(pub);
		}
		else if (s1.equals("haved") && s2.equals("haved"))
		{
			ESID = new ArrayList<String>(cve);
		}
	}

	public void CHECK()
	{
		int index = 0;
		CVE.clear();
		PUB.clear();
		for (int i = 0; i < CVE1.size() && CVE2.size() > 0; i++)
		{
			index = CVE1.indexOf(CVE1.get(i));
			if (!Pub2.get(i).equals(Pub2.get(index)))
			{
				CVE.add(CVE1.get(i));
				PUB.add(Pub1.get(i));
			}
		}

		list1_check = false;
		list2_check = false;
	}

	public ArrayList<String> get_CVEID()
	{
		return CVE;
	}

	public ArrayList<String> get_PUBLISHED()
	{
		return PUB;
	}

	public ArrayList<String> get_ID()
	{
		return ESID;
	}
}
