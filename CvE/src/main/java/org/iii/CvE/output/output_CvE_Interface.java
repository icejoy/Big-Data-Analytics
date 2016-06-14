package org.iii.CvE.output;

import java.util.ArrayList;

public interface output_CvE_Interface
{
	public void output(String[] setting, ArrayList<String> cve,
			ArrayList<String> pub,ArrayList<String> esid);

	public void settings(String[] setting);

	public void StringList_To_JsonList(ArrayList<String> cve,
			ArrayList<String> pub, ArrayList<String> esid);

	public void Save();
}
