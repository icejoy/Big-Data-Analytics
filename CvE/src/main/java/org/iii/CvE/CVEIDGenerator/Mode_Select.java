package org.iii.CvE.CVEIDGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Element;
import org.iii.CvE.Connect.connect_CvE_Factory;
import org.iii.CvE.Connect.connect_CvE_Interface;
import org.iii.CvE.Input.input_CvE_Factory;
import org.iii.CvE.Input.input_CvE_Interface;
import org.iii.CvE.Parse.parser_CvE_Factory;
import org.iii.CvE.Parse.parser_CvE_Interface;
import org.iii.CvE.output.output_CvE_Factory;
import org.iii.CvE.output.output_CvE_Interface;

public class Mode_Select
{
	long d1, d2;

	input_CvE_Interface ici;
	connect_CvE_Interface cci;
	ArrayList<parser_CvE_Interface> pci;
	ArrayList<output_CvE_Interface> oci;

	String Calculate_String = "";
	String[] str;
	String[] command;
	String[] elasticsearch = new String[7];

	public Mode_Select(String[] s)
	{
		// -ms 0or1 -i filepath -d date_from date_to -o filepath
		// CluserName, host, port, index, type, key1, key2
		str = s;
		// init
		input_CvE_Factory iCF = new input_CvE_Factory();
		this.ici = iCF.get_CvE_input();
		connect_CvE_Factory cCF = new connect_CvE_Factory(str[1]);
		this.cci = cCF.get_CvE_connect();
		parser_CvE_Factory pCF = new parser_CvE_Factory(str[1]);
		this.pci = pCF.get_CvE_parser();
		output_CvE_Factory oCF = new output_CvE_Factory();
		this.oci = oCF.get_CvE_output();
	}

	public boolean isNumeric(String str)
	{
		Pattern pattern = Pattern.compile("[0-1]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches())
		{
			return false;
		}
		return true;
	}

	public boolean CheckPrompt()
	{
		// java -jar xxx.jar -ms 0or1 -d 2016-01-01 2016-05-31 -o
		System.out.println("檢查指令中");
		try
		{
			if (str[0].toLowerCase().equals("-ms")
					|| str[0].toLowerCase().equals("ms"))
			{
				if (isNumeric(str[1]))
				{
					if (str[2].toLowerCase().equals("-d"))
					{
						if (str[5].toLowerCase().equals("-o"))
						{
							System.out.println("檢查完畢");
							Run();
							System.out.println("執行完畢");
							return true;
						}
						else
						{
							return error();
						}
					}
					else
					{
						return error();
					}
				}
				else
				{
					return error();
				}
			}
			else
			{
				return error();
			}
		}
		catch (Exception e)
		{
			return error();
		}
	}

	public boolean error()
	{
		System.out
				.println("指令錯誤，請遵循java -jar xxx.jar -ms 0or1 -i inputPath -d 2016-01-01 2016-05-31 -o outputPath");
		return false;
	}

	public void Run()
	{
		boolean NeverEnd = false;
		// -ms 0or1 -i filepath -d date_from date_to -o filepath
		NeverEnd = State_NeverEnd(str[1]);

		do
		{
			/*----------input----------*/
			// read http cve file
			Calculate_Time_Start("get CVE content from input path");
			this.ici.input(str[3]);
			Calculate_Time_End();

			/*----------connect----------*/
			try
			{
				// connect es to get cve
				Calculate_Time_Start("try link ES to get CVE on ES");
				// CluserName, host, port, index, type, key1, key2
				get_ES_Settings();
				this.cci.connect(elasticsearch[0], elasticsearch[1],
						Integer.parseInt(elasticsearch[2]), elasticsearch[3],
						elasticsearch[4], elasticsearch[5], elasticsearch[6]);
				Calculate_Time_End();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("can not cahnge CVE on ES");
				System.out.println("because ES settings have some error");
			}

			/*----------parse----------*/
			Calculate_Time_Start("start to parse");
			// [0] Extract
			// [1] Check
			// [2] DateInterval

			// parse http cve content
			this.pci.get(0).parse(this.ici.get_FileContent(), null, null, null,
					null);
			
			// init check
			this.pci.get(1).init();
			// set check cve1 pub1 from http cve
			this.pci.get(1).parse(null, "haved", null,
					this.pci.get(0).get_CVEID(),
					this.pci.get(0).get_PUBLISHED());
			// set check cve2 pub2 from es cve
			this.pci.get(1).parse(null, null, "haved",
					this.cci.get_CVEID_List(),
					this.cci.get_Published_Date_List());
			// set check esid from es cve
			this.pci.get(1).parse(null, "haved", "haved",
					this.cci.get_ESID_List(), null);
			// set check
			this.pci.get(1).parse(null, null, null, null, null);
			
			
			Calculate_Time_End();

		}
		while (NeverEnd);
	}

	public Boolean State_NeverEnd(String str)
	{

		if (str.equals("0"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@SuppressWarnings("resource")
	public void get_ES_Settings()
	{
		// CluserName, host, port, index, type, key1, key2
		Scanner scanner = new Scanner(System.in);
		System.out
				.println("請輸入ElasticSearch的：cluser name,host,port,index,type,key1,key2");
		System.out.println("請以空白鍵區分");
		elasticsearch[0] = scanner.next();
		elasticsearch[1] = scanner.next();
		elasticsearch[2] = scanner.next();
		elasticsearch[3] = scanner.next();
		elasticsearch[4] = scanner.next();
		elasticsearch[5] = scanner.next();
		elasticsearch[6] = scanner.next();
	}

	public void Calculate_Time_Start(String str)
	{
		Calculate_String = str;
		System.out.println("開始" + Calculate_String);
		d1 = new Date().getTime();
	}

	public void Calculate_Time_End()
	{
		System.out.println(Calculate_String + "完畢");
		d2 = new Date().getTime();
		System.out.println("共花費：" + (d2 - d1) / 1000 + "秒" + "\r\n");
	}

	public static void main(String[] args)
	{
		// java -jar xxx.jar -ms 0or1 -i filepath -d date_from date_to -o
		// filepath
		// CluserName, host, port, index, type, key1, key2
		try
		{
			args = new String[]
			{ "-ms", "0", "-d", "2016-05-01", "2016-05-31", "-o",
					"result_test.tsv" };
			if (args[0].equals("-h"))
			{
				System.out
						.println("輸入指令應為：java -jar xxx.jar -ms 0or1 -i filepath -d date_from date_to -o filepath");
				System.out
						.println("-ms : mode select,0 is online CVE parse; 1 is once CVE parse\r\n"
								+ "-i : input\r\n"
								+ "-d : date interval\r\n"
								+ "-o output\r\n"
								+ "if you select -ms is 0\r\n"
								+ "please keyin your elasticsearch\r\n"
								+ "clusername host port index type key1 and key2\r\n"
								+ "for example\r\n"
								+ "you can keyin :\r\n"
								+ "java -jar Mode_Select.jar -ms 0 -i https://cve.mitre.org/data/downloads/allitems-cvrf-year-2016.xml -d 2016-06-01 2016-07-01 -o elasticserch\r\n"
								+ "or\r\n"
								+ "java -jar Mode_Select.jar -ms 0 -i https://cve.mitre.org/data/downloads/allitems-cvrf-year-2016.xml -d null null -o elasticserch\r\n"
								+ "when you keyin your output is elasticsearch, please keyin your elasticsearch settings\r\n"
								// +
								// "TwitterDB 192.168.30.60 55555 cve log cve_id published_date");
								+ "elasticsearch localhost 9300 cve log cve_id published_date");
			}
			else
			{
				Mode_Select MS = new Mode_Select(args);
				MS.CheckPrompt();
				System.out.println("ＤＯＮＥ");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
