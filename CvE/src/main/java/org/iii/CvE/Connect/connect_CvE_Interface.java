package org.iii.CvE.Connect;

import java.util.ArrayList;

public interface connect_CvE_Interface
{
	public void connect(String CluserName, String host, int port, String index,
			String type, String key1, String key2);

	public void set_ES_Connector(String CluserName, String host, int port,
			String index, String type, String key1, String key2);

	public void read();

	public ArrayList<String> get_CVEID_List();

	public ArrayList<String> get_Published_Date_List();

	public ArrayList<String> get_ESID_List();
}