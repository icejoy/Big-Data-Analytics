

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeMap;

import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram.Interval;
import org.iii.CvE.ES.CVE_ES_Connector;
import org.iii.CvE.Utils.CVEXmlReader;
import org.json.JSONException;
import org.json.JSONObject;


public class Generate_CvE_Date 
{
	static Date ES_FROM ;
	static Date ES_TO ;
	static Date CVE_FROM ;
	static Date CVE_TO ;
	
	static String CVE_FILE = "allitems-cvrf-year-2016.xml";
	static String RESULT_FILE = "result1.tsv";
	static String RESULT_PUBLISH_FILE = "result_publish1.tsv";
	static Interval INTERVAL = Interval.DAY;
	
	static CVE_ES_Connector ES = new CVE_ES_Connector("localhost", 9300, "cve", "logs");
	static CVEXmlReader CVER = new CVEXmlReader();
	static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
	static SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd");
	
	static List<String> DATE_LIST = new ArrayList<String>();
	static List<Date> DATE_LIST_DATE = new ArrayList<Date>();
	static List<String> CVE_LIST = new ArrayList<String>();
	static ArrayList<JSONObject> CVE_DETAIL;
	static TreeMap<String,HashMap<String, Long>> CVE_DATE_MAP = new TreeMap<String,HashMap<String, Long>>();
	
	static{
		
		try {
			
			SDF_DATE.setTimeZone(TimeZone.getTimeZone("GMT"));
			ES_FROM = SDF.parse("2016-05-12 00:00:00+0000");
			ES_TO = SDF.parse("2016-05-28 00:00:00+0000");
			CVE_FROM = SDF.parse("2016-05-16 00:00:00+0000");
			CVE_TO = SDF.parse("2016-05-23 00:00:00+0000");
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void setCVE_LIST() {
		
		for(JSONObject cve: CVE_DETAIL)
		{
			CVE_LIST.add(cve.getString("cve_id"));
		}
		
	}
	
	private static void setDATE_LIST() {
		
		DATE_LIST = ES.getDateList("@timestamp", ES_FROM.getTime(), ES_TO.getTime(), INTERVAL);
		DATE_LIST_DATE = ES.getDateListDate("@timestamp", ES_FROM.getTime(), ES_TO.getTime(), INTERVAL);
		
	}
	
	private static void setCVE_DATE_MAP() {
		int count=0;
        for(JSONObject cve: CVE_DETAIL)
        {
        	HashMap<String,Long> dateCountMap = new HashMap<String,Long>(); 
        	
        	String cve_id = cve.getString("cve_id");
        	DateHistogram result = ES.getDateDistribution("@timestamp", ES_FROM.getTime(), ES_TO.getTime(), "text:\""+cve_id+"\"", INTERVAL);
        	
        	for(Bucket b : result.getBuckets())
        	{
        		count+=b.getDocCount();
        		dateCountMap.put(b.getKeyAsText().string(), b.getDocCount());
        	}
        	
        	CVE_DATE_MAP.put(cve_id, dateCountMap);
        }
        System.out.println(count);
	}
	
	private static void dumpResult() throws IOException {
		
		File file = new File(RESULT_FILE);
		if(!file.exists())
			file.createNewFile();
		
		FileWriter writer = new FileWriter(file);
		
		writer.write("name\t"+StringUtils.join(DATE_LIST,"\t")+"\n");
		
		for(Entry<String, HashMap<String, Long>> entry : CVE_DATE_MAP.entrySet())
		{
			List<String> tmp = new ArrayList<String>();
			tmp.add(entry.getKey());
			
			for(String date : DATE_LIST)
			{
				tmp.add(entry.getValue().get(date).toString());
			}
			writer.write(StringUtils.join(tmp,"\t")+"\n");
			
			
		}
		
		writer.close();
		
	}	
	private static void dumpResultPublishedDate() throws IOException {
		
		File file = new File(RESULT_PUBLISH_FILE);
		if(!file.exists())
			file.createNewFile();
		
		FileWriter writer = new FileWriter(file);
		
		writer.write("name\t"+StringUtils.join(DATE_LIST,"\t")+"\n");
		
		for(JSONObject cve : CVE_DETAIL)
		{
			List<String> tmp = new ArrayList<String>();
			tmp.add(cve.getString("cve_id"));
			
			for(Date date : DATE_LIST_DATE)
			{
				try {
					Date pdate = SDF_DATE.parse(cve.getString("published_date"));
					if(date.equals(pdate))
					{
						tmp.add("1");
					}
					else
					{
						tmp.add("0");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			writer.write(StringUtils.join(tmp,"\t")+"\n");
			
			
		}
		
		writer.close();
		
	}
	
    public static void main( String[] args ) throws IOException 
    {
    	
    	CVE_DETAIL = CVER.Run(CVE_FROM, CVE_TO);
        
        setCVE_LIST();
        setDATE_LIST();
        setCVE_DATE_MAP();
        
        System.out.println(CVE_DATE_MAP);
        
        dumpResult();
        dumpResultPublishedDate();
        
    }


	


}
