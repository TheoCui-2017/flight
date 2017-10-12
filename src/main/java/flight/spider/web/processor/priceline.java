package flight.spider.web.processor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import flight.spider.web.pipeline.sqlServerPipeline;
import flight.spider.web.utility.toolUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClient2Downloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;


public class priceline implements PageProcessor {
	
	public sqlServerPipeline pipeline;
	
	public String departCode = "BJS";
	public String arrivalCode = "PAR";
	public String goDate = "2017-01-01";
	public String backDate = "2017-01-08";
	public String cabinClass = "ECO";
	public String passenger = "ADT";
	public String BASE_URL = "www.priceline.com";
	public int thread_num = 20;
	public boolean round = false;
	public priceline(){
		super();
	}
	
	public priceline(String departCode, String arrivalCode, String goDate, String backDate, String cabinClass, String passenger, String BASE_URL, boolean round){
		this.departCode = departCode;
		this.arrivalCode = arrivalCode;
		this.goDate = goDate;
		this.backDate = backDate;
		this.cabinClass = cabinClass;
		this.passenger = passenger;
		this.BASE_URL = BASE_URL;
		this.round = round;
	}
	
	public void init(String departCode, String arrivalCode, String goDate, String backDate, String cabinClass, String passenger, String BASE_URL, boolean round){
		this.departCode = departCode;
		this.arrivalCode = arrivalCode;
		this.goDate = goDate;
		this.backDate = backDate;
		this.cabinClass = cabinClass;
		this.passenger = passenger;
		this.BASE_URL = BASE_URL;
		this.round = round;
	}
	
	public void setPileline(sqlServerPipeline pipeline){
		this.pipeline = pipeline;
	}
	
	public void setThreadNum(int thread_num){
		this.thread_num = thread_num;
	}
	
	
    private Site site = Site.me().setRetryTimes(3) //就我的经验,这个重试一般用处不大
            .setTimeOut(20000)//在使用代理的情况下,这个需要设置,可以考虑调大线程数目
            .setSleepTime(2000)//使用代理了之后,代理会通过切换IP来防止反扒。同时,使用代理本身qps降低了,所以这个可以小一些
            .setCycleRetryTimes(3)//这个重试会换IP重试,是setRetryTimes的上一层的重试,不要怕三次重试解决一切问题。。
            .setUseGzip(true)//注意调整超时时间
            //.setHttpProxy(new HttpHost("202.118.9.154",8998))
            //.setHttpProxyPool( Lists.newArrayList(new String[]{"","","14.122.106.92","9999"}, new String[]{"","","175.0.155.245","9797"}),true)
            .addHeader("User-Agent", "Priceline 12.1 rv:121000051 (iPhone; iPhone OS 8.1.2; en_US)")
            .addHeader("Accept-Encoding", "gzip")
            .addHeader("Content-Type", "application/json");
    
    @Override
    public void process(Page page) {
    	try{
			// 解析json数据
		    JSONObject content = (JSONObject) JSONObject.parse(page.getJson().jsonPath("airSearchRsp").toString());
		    JSONArray pricedItinerarys = content.getJSONArray("pricedItinerary");
		    JSONArray slices = content.getJSONArray("slice");
		    JSONArray segments = content.getJSONArray("segment");
		    JSONArray equipments = content.getJSONArray("equipment");
		   
		    // equipments to hashmap
		    HashMap<String , String> planeInfo = new HashMap<String , String>(); 
		    for(Object equipment : equipments){
		    	JSONObject equipmentJson = (JSONObject)  equipment;
		    	String code = equipmentJson.getString("code");
		    	String name = equipmentJson.getString("name");
		    	planeInfo.put(code, name);
		    }
		    
		    // slices to hashmap
		    HashMap<String , JSONObject> sclieInfo = new HashMap<String , JSONObject>(); 
		    for(Object slice : slices){
		    	JSONObject sliceJson = (JSONObject)  slice;
		    	String uniqueSliceId = sliceJson.getString("uniqueSliceId");
		    	JSONObject sliceSingle = new JSONObject();
		    	sliceSingle.put("duration", sliceJson.getString("duration"));
		    	JSONArray segmentIds = new JSONArray();
		    	// segmentId
		    	JSONArray segmentInfos = sliceJson.getJSONArray("segment");
		    	for(Object segmentInfo : segmentInfos){
		    		JSONObject segment = (JSONObject)  segmentInfo;
		    		segmentIds.add(segment.getString("uniqueSegId"));
		    	}
		    	sliceSingle.put("segmentIds", segmentIds);
		    	sclieInfo.put(uniqueSliceId, sliceSingle);
		    }
		    
		    // segments to hashmap
		    HashMap<String , JSONObject> segmentAll = new HashMap<String , JSONObject>(); 
		    for(Object segment : segments){
		    	JSONObject segmentIndex = (JSONObject)  segment;
		    	String uniqueSegId = segmentIndex.getString("uniqueSegId");
		    	JSONObject segmentSingleInfo = new JSONObject();
		    	segmentSingleInfo.put("departureTime", segmentIndex.getString("departDateTime"));
		    	segmentSingleInfo.put("arrivalTime", segmentIndex.getString("arrivalDateTime"));
		    	segmentSingleInfo.put("departureAirport", segmentIndex.getString("origAirport"));
		    	segmentSingleInfo.put("arrivalAirport", segmentIndex.getString("destAirport"));
		    	segmentSingleInfo.put("duration", segmentIndex.getString("duration"));
		    	segmentSingleInfo.put("planeCode", segmentIndex.getString("equipmentCode"));
		    	segmentSingleInfo.put("plane", planeInfo.get(segmentIndex.getString("equipmentCode")));
		    	segmentSingleInfo.put("flightNumber", segmentIndex.getString("flightNumber"));
		    	segmentSingleInfo.put("airlineCode", segmentIndex.getString("marketingAirline"));
		    	segmentSingleInfo.put("meal", "");
		    	int layoverDuration = 0;
		    	segmentSingleInfo.put("layoverDuration", layoverDuration);
		    	segmentAll.put(uniqueSegId, segmentSingleInfo);
		    }
		    
		    // 整合结果
		    JSONObject flightInfo = new JSONObject();
		    for(Object pricedItinerary : pricedItinerarys){
		    	JSONObject pricedItineraryIndex = (JSONObject)  pricedItinerary;
		    	int seats = Integer.parseInt(pricedItineraryIndex.getString("numSeats"));
		    	String currency = pricedItineraryIndex.getJSONObject("pricingInfo").getString("currencyCode");
		    	String airlineCode = pricedItineraryIndex.getJSONObject("pricingInfo").getString("ticketingAirline");
		    	String fare = pricedItineraryIndex.getJSONObject("pricingInfo").getString("baseFare");
		    	String tax = pricedItineraryIndex.getJSONObject("pricingInfo").getString("totalTaxes");
		    	String price = pricedItineraryIndex.getJSONObject("pricingInfo").getString("totalFare");
		    	String baggageURL = pricedItineraryIndex.getString("baggageURL");
		    	
		    	String flag = BASE_URL.toLowerCase() + "-" + cabinClass.toLowerCase() + "-";
		    	String search_flag = BASE_URL.toLowerCase() + "-" +  cabinClass.toLowerCase() + "-" + goDate + "-" + departCode + "-" + arrivalCode;
		    	
		    	JSONArray flightInfoArr = new JSONArray();
		    	JSONArray sliceInfos = pricedItineraryIndex.getJSONArray("slice");
		    	int size = 0;
		    	for(Object sliceInfo : sliceInfos){
			    	String sliceId = ((JSONObject)sliceInfo).getString("uniqueSliceId");
			    	JSONObject scliceFlight = sclieInfo.get(sliceId);
			    	String duration = scliceFlight.getString("duration");
			    	String segmentFirstId = scliceFlight.getJSONArray("segmentIds").get(0).toString();
			    	String segmentLastId = scliceFlight.getJSONArray("segmentIds").get(scliceFlight.getJSONArray("segmentIds").size()-1).toString();
			    	String departAirportCode = segmentAll.get(segmentFirstId).getString("departureAirport");
			    	String arrivalAirportCode = segmentAll.get(segmentLastId).getString("arrivalAirport");
			    	String departDateTime = segmentAll.get(segmentFirstId).getString("departureTime");
			    	String arrivalDateTime = segmentAll.get(segmentLastId).getString("arrivalTime");
			    	String flightPath = "";
			    	JSONArray segmentsFlight = new JSONArray();
			    	JSONArray segmentIds = scliceFlight.getJSONArray("segmentIds");
			    	String stopover = "";
			    	String mainAirlineCode = "";
			    	int segmentDuration = 0;
			    	int len = 0;
				    String tmpTime = "";
				    String tmpSegId = "";
			    	for(Object segmentId : segmentIds){
			    		
			    		// 航司代码
			    		if(Integer.parseInt(segmentAll.get(segmentId).getString("duration")) >= segmentDuration){
			    			segmentDuration = Integer.parseInt(segmentAll.get(segmentId).getString("duration"));
			    			mainAirlineCode = segmentAll.get(segmentId).getString("airlineCode");
			    		}
			    		
			    		// 中转机场代码
			    		stopover += "-" + segmentAll.get(segmentId).getString("arrivalAirport");
			    		
			    		flightPath += "coach-" + segmentAll.get(segmentId).getString("departureAirport").toLowerCase() + "-"
			    				+ segmentAll.get(segmentId).getString("arrivalAirport").toLowerCase() + "-"
			    				+ segmentAll.get(segmentId).getString("airlineCode").toLowerCase() + "-"
			    				+ segmentAll.get(segmentId).getString("flightNumber").toLowerCase();
			    		if(len < segmentIds.size() -1){
			    			flightPath += "-";
			    		}
			    		// layoverDuration
			    		if( len != 0 && len < segmentIds.size()) {
			    			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
				    		String segmentDepart = segmentAll.get(segmentId).getString("departureTime");
				    		Date dateDepart = df.parse(segmentDepart);
				    		Date dateArrival = df.parse(tmpTime);
				    		int layoverDuration = (int) (dateDepart.getTime() - dateArrival.getTime())/(1000 * 60);
				    		((JSONObject)segmentAll.get(tmpSegId)).put("layoverDuration", layoverDuration);
			    		}
			    		tmpTime = segmentAll.get(segmentId).getString("arrivalTime");
			    		tmpSegId = segmentId.toString();
			    		// flight segment
			    		segmentsFlight.add(segmentAll.get(segmentId));
			    		
			    		len ++;
			    	}
			    	
			    	flag += departDateTime.toLowerCase() + "-" + flightPath + ";";
			    	
			    	int stay_days = 0;
			    	if(round){
			    		stay_days = toolUtil.compareDate(backDate,goDate,"yyyy-MM-dd");
			    	}
			    	
			    	// 中转城市
			    	if(stopover.length() > 0){
						String str = stopover.replaceFirst("-", "");
						if(str.length() > 3){
							stopover = str.substring(0, str.length()-4);
						}
						else{
							stopover = str.substring(0, str.length()-3);
						}
					}
			    	
			    	JSONObject flightInfoIndex = new JSONObject();
			    	
			    	if(size == 0){
				    	flightInfoIndex.put("departureCode", departCode);
				    	flightInfoIndex.put("arrivalCode", arrivalCode);
			    	}
			    	else{
			    		flightInfoIndex.put("departureCode", arrivalCode);
				    	flightInfoIndex.put("arrivalCode", departCode);
			    	}
			    	flightInfoIndex.put("stopover", stopover);
			    	flightInfoIndex.put("stay_days", stay_days);
			    	flightInfoIndex.put("departAirportCode", departAirportCode);
			    	flightInfoIndex.put("arrivalAirportCode", arrivalAirportCode);
			    	flightInfoIndex.put("duration", duration);
			    	flightInfoIndex.put("departureTime", departDateTime);
			    	flightInfoIndex.put("arrivalTime", arrivalDateTime);
			    	flightInfoIndex.put("currency", currency);
			    	flightInfoIndex.put("priceTotal", price);
			    	flightInfoIndex.put("airlineCode", mainAirlineCode);
			    	flightInfoIndex.put("source", BASE_URL);
			    	flightInfoIndex.put("seat", seats);
			    	flightInfoIndex.put("search_flag", search_flag);
			    	flightInfoIndex.put("cabinClass", cabinClass);
			    	flightInfoIndex.put("segment", segmentsFlight);
			    	
			    	flightInfoArr.add(flightInfoIndex);
			    	size++;
		    	}
		    	JSONObject flightIndex = new JSONObject();
		    	flightIndex.put("flightInfo", flightInfoArr);
		    	//flight price 
		    	JSONObject priceFlight = new JSONObject();
		    	priceFlight.put("currency",currency);
		    	priceFlight.put("fare",fare);
		    	priceFlight.put("tax",tax);
		    	priceFlight.put("passenger",passenger);
		    	priceFlight.put("baggageURL",baggageURL);
		    	flightIndex.put("priceInfo", priceFlight);
		    	
		        flightInfo.put(flag, flightIndex);
		    }
		    page.putField("flightInsert", flightInfo);
    	}catch(Exception e){
    		e.printStackTrace();
    		System.out.println(this.departCode+","+this.arrivalCode+","+this.goDate+","+this.backDate+","+this.round+","+this.passenger);
    		System.out.println(page.getHtml());
    	}
    }

    @Override
    public Site getSite() {
        return site;
    }
    
    
    public void test11() {
    	String itineraryType = "ONE_WAY";
    	String sliceRefId = "1";
    	String trip = "{\"origins\":[{\"type\":\"AIRPORT\",\"location\":\""
					+departCode+
					"\"}],\"id\":1,\"destinations\":[{\"type\":\"AIRPORT\",\"location\":\""
					+arrivalCode+
					"\"}],\"departDate\":\""
					+goDate+
					"\"}";
    	if(round){
    		itineraryType = "ROUND_TRIP";
    		sliceRefId = "1,2";
    		trip = "{\"origins\":[{\"type\":\"AIRPORT\",\"location\":\""
    				+departCode+
					"\"}],\"id\":1,\"destinations\":[{\"type\":\"AIRPORT\",\"location\":\""
					+arrivalCode+
					"\"}],\"departDate\":\""
					+goDate+
					"\"},{\"origins\":[{\"type\":\"AIRPORT\",\"location\":\""
					+arrivalCode+
					"\"}],\"id\":2,\"destinations\":[{\"type\":\"AIRPORT\",\"location\":\""
					+departCode+
					"\"}],\"departDate\":\""
					+backDate+
					"\"}";
    	}
    	Spider spider = Spider.create(this);
		  
		Request pricelineUrl = new Request("https://"+BASE_URL+"/pws/v0/fly/c/airSearch");
		String jsonStr = "{\"airSearchReq\":{\"trip\":[{\"slice\":["
		+trip+
		"]}],\"sortPrefs\":[{\"type\":\"PRICE\",\"order\":\"ASC\",\"priority\":1},{\"type\":\"DEPARTTIME\",\"order\":\"ASC\",\"priority\":2},{\"type\":\"TRAVELTIME\",\"order\":\"ASC\",\"priority\":3}],\"includeSliceSummary\":\"false\",\"includeFilteredTripSummary\":\"false\",\"passenger\":[{\"type\":\""
		+passenger+
		"\",\"numOfPax\":1}],\"itineraryType\":\""
		+itineraryType+
		"\",\"searchOptions\":{\"excludedCarriers\":[],\"carrierPref\":[],\"cabinClass\":\""
		+cabinClass+
		"\"},\"requestId\":\"S8etnBdSgBImOz5Qds8k\",\"includeFullTripSummary\":\"false\",\"displayParameters\":{\"lowerBound\":1,\"sliceRefId\":["
		+sliceRefId+
		"],\"upperBound\":10000}}}";
		JSONObject json =  (JSONObject) JSONObject.parse(jsonStr);
		pricelineUrl.putExtra("flight_spider_json", json.toJSONString());
		pricelineUrl.setMethod(HttpConstant.Method.POST);

		spider
		.addRequest(pricelineUrl)
		.addPipeline(pipeline)
		.setDownloader(new HttpClient2Downloader())
		.thread(this.thread_num).run();
    }
}