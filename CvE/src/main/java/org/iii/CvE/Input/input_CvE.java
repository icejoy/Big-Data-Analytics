package org.iii.CvE.Input;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class input_CvE implements input_CvE_Interface
{
	List<Element> list;
	String CVE_FilePath = "https://cve.mitre.org/data/downloads/"
			+ "allitems-cvrf-year-2016.xml";

	public void input(String path)
	{
		if (!path.equals("null"))
		{
			CVE_FilePath = path;
		}
		read_File();
	}

	@SuppressWarnings("unchecked")
	public void read_File()
	{
		try
		{
			SAXReader reader = new SAXReader();
			Document doc = reader.read(CVE_FilePath);
			Element element = doc.getRootElement();
			list = element.elements();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public List<Element> get_FileContent()
	{
		return list;
	}
}
