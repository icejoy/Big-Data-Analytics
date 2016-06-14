import org.iii.CvE.Connect.connect_CvE_ES;


public class test
{
	public static void main(String[] args)
	{
		connect_CvE_ES cce = new connect_CvE_ES();
		cce.set_ES_Connector("TwitterDB","192.168.30.60", 55555, "cve", "log");
		cce.read("cve_id","published_date");
//		cce.getDateDistribution("");
	}
}
