package org.iii.CvE.Connect;

public class connect_CvE_Factory
{
	String Choose = "";

	public connect_CvE_Factory(String choose)
	{
		Choose = choose;
	}

	public connect_CvE_Interface get_CvE_connect()
	{
		if (Choose.equals("0"))
		{
			return new connect_CvE_ES();
		}
		else
		{
			return null;
		}
	}
}
