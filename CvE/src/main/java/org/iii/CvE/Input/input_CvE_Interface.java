package org.iii.CvE.Input;

import java.util.List;

import org.dom4j.Element;

public interface input_CvE_Interface
{	
	public void input(String path);
	
	// read XML
	public void read_File();
	
	public List<Element> get_FileContent();
}
