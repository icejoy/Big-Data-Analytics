package org.iii.CvE.Parse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class parser_CvE_Factory
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String Command;

	public parser_CvE_Factory(String command)
	{
		// [0] patch or online
		// [1] input path
		// [2] from
		// [3] to
		// [4] output path
		Command = command;
	}

	public void PRINT()
	{
		switch (Integer.parseInt(Command))
		{
		case 0:
			System.out.println("您選擇的模式為：OnlineCVEID萃取模式");
			System.out.println("開始進入 OnlineCVEID萃取");
			break;
		case 1:
			System.out.println("您選擇的模式為：一次性CVEID萃取模式");
			System.out.println("開始進入 一次性CVEID萃取");
			break;
		default:
			System.out.println("\r\n目前模式僅有：0:OnlineCVEID萃取模式\t1:一次性CVEID萃取模式");
		}
	}

	public ArrayList<parser_CvE_Interface> get_CvE_parser()
	{
		ArrayList<parser_CvE_Interface> pci_list = new ArrayList<parser_CvE_Interface>();
		PRINT();
		pci_list.add(new parser_CvE_Extract());
		pci_list.add(new parser_CvE_Check());
		pci_list.add(new parser_CvE_DateInterval());
		return pci_list;
	}

}
