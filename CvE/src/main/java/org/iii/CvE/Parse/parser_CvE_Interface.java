package org.iii.CvE.Parse;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

public interface parser_CvE_Interface
{
	public void init();
	
	public void parse(List<Element> list,String s1,String s2,ArrayList<String> cve,ArrayList<String> pub);
	
	// get CVEID
	public ArrayList<String> get_CVEID();

	// get Published Date
	public ArrayList<String> get_PUBLISHED();
	
	// get ID
	public ArrayList<String> get_ID();
}
