package flight.spider.web.processor;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


public class expedia_orbitzProcessor implements PageProcessor {
	
	public sqlServerPipeline pipeline;
		
	public String departCode = "BJS";
	public String arrivalCode = "PAR";
	public String goDate = "01/01/2017";
	public String backDate = "01/08/2017";
	public String cabinClass = "ECO";
	public String passenger = "ADT";
	public String BASE_URL = "www.expedia.com";
	public int thread_num = 20;
	public boolean round = false;
	public String C = "";
    
	public expedia_orbitzProcessor(){
		super();
	}
	
	public expedia_orbitzProcessor(String departCode, String arrivalCode, String goDate, String backDate, String cabinClass, String passenger, String BASE_URL, boolean round){
		this.departCode = departCode;
		this.arrivalCode = arrivalCode;
		this.goDate = goDate;
		this.backDate = backDate;
		this.cabinClass = cabinClass;
		this.passenger = passenger;
		this.BASE_URL = BASE_URL;
		this.round = round;
	}
	
	public void  init(String departCode, String arrivalCode, String goDate, String backDate, String cabinClass, String passenger, String BASE_URL, boolean round){
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
            .setSleepTime(10)//使用代理了之后,代理会通过切换IP来防止反扒。同时,使用代理本身qps降低了,所以这个可以小一些
            .setCycleRetryTimes(3)//这个重试会换IP重试,是setRetryTimes的上一层的重试,不要怕三次重试解决一切问题。。
            .setUseGzip(true)//注意调整超时时间
    		//.setHttpProxy(new HttpHost("202.118.9.154",8998))
    		//.setHttpProxyPool(Lists.newArrayList(new String[]{"","","211.72.221.85","80"}, new String[]{"","","61.172.193.104","80"}),true)
    		.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36");
    
    public void process(Page page) {
    	if(C == "" && page.getUrl().toString().contains("Flights-Search")){
    		// 获取参数C
    		C = page.getHtml().xpath("//div[@id='originalContinuationId']/allText()").toString();
    		if(C == null){
    			System.out.println("claw index failed!!!");
    			if(page.getUrl().toString().split("timer=").length <= 4){
    				System.out.println("retry begining!!!");
    				long time = new Date().getTime();
    				String retryUrl = page.getUrl().toString() + "&timer=" + time; 
    				page.addTargetRequest(retryUrl);
    			}
    			else{
    				System.out.println("page index retry failed!!!");
    			}
    		}else{
    			// 加入待抓取url列表
    			page.addTargetRequest("https://" + BASE_URL+ "/Flight-Search-Paging?c="+C+"&is=1&sp=asc&cz=200&cn=0");
    		}
    	}
    	else if(page.getUrl().toString().contains("Flight-Search-Paging")){
    		// 解析json数据
    		JSONArray flagsArray = new JSONArray();
    		JSONObject content = (JSONObject) JSONObject.parse(page.getJson().jsonPath("content").toString());
    		JSONObject offers = content.getJSONObject("offers");
    		JSONObject legs = content.getJSONObject("legs");
    		JSONArray index = content.getJSONArray("index");
    		
    		if(index != null){
    			JSONObject flightInfo = new JSONObject();
	    		// 遍历index
	    		for(Object key : index){
	    			JSONArray legIds = offers.getJSONObject(key.toString()).getJSONArray("legIds");
	    			String unique_flag = BASE_URL.toLowerCase() + "-" + cabinClass.toLowerCase() + "-" + key.toString();
	    			String dateFormat = goDate.split("\\/")[2] + "-" + goDate.split("\\/")[0] + "-" + goDate.split("\\/")[1];
	    			String search_flag = BASE_URL.toLowerCase() + "-" +  cabinClass.toLowerCase() + "-" + dateFormat + "-" + departCode + "-" + arrivalCode;
	    			
	    			String currency = offers.getJSONObject(key.toString()).getJSONObject("price").getString("currencyCode");
	    			String price = offers.getJSONObject(key.toString()).getJSONObject("price").getString("totalPriceAsDecimalString");
	    			
	    			// 保存flag和unique_flag，为了拼后面的url
	    			JSONObject unique_flags = new JSONObject();
	    			if(price.length() > 0){
	    				unique_flags.put("flag",key.toString());
	    				unique_flags.put("unique_flag",unique_flag);
	    				unique_flags.put("price", price);
	    				flagsArray.add(unique_flags);
	    			}
	    			
	    			JSONArray flightInfoArr = new JSONArray();
	    			int size = 0;
	    			for(Object legId : legIds){
	    				JSONObject legInfo = legs.getJSONObject(legId.toString());
	    				String departAirportCode = legInfo.getJSONObject("departureLocation").getString("airportCode");
	    				String arrivalAirportCode = legInfo.getJSONObject("arrivalLocation").getString("airportCode");
	    				String airlineCode = legInfo.getJSONObject("carrierSummary").getJSONArray("airlineCodes").get(0).toString();
	    				// duration
		    			int duration = Integer.parseInt(legInfo.getJSONObject("duration").getString("hours"))*60 + Integer.parseInt(legInfo.getJSONObject("duration").getString("minutes"));
		    			String departureTime = legInfo.getJSONObject("departureTime").getString("isoStr");
		    			String arrivalTime = legInfo.getJSONObject("arrivalTime").getString("isoStr");
		    			
		    			// 中转机场代码
		    			String stopover = "";
				    	int segmentDuration = 0;
		    			
		    			// flight segment
		    			JSONArray segments = new JSONArray();
		    			JSONArray timelines = legInfo.getJSONArray("timeline");
		    			int len = 0;
		    			for(Object timeline : timelines){
		    				JSONObject flightSegment = new JSONObject();
		    				JSONObject flights = (JSONObject) timeline;
		    				if(flights.getString("type").equals("Segment")){
		    					flightSegment.put("departureAirport", flights.getJSONObject("departureAirport").getString("code"));
		    					flightSegment.put("arrivalAirport", flights.getJSONObject("arrivalAirport").getString("code"));
		    					flightSegment.put("departureTime", flights.getJSONObject("departureTime").getString("isoStr").split("\\.000")[0]);
		    					flightSegment.put("arrivalTime", flights.getJSONObject("arrivalTime").getString("isoStr").split("\\.000")[0]);
		    					flightSegment.put("duration", Integer.parseInt(flights.getJSONObject("duration").getString("hours"))*60+Integer.parseInt(flights.getJSONObject("duration").getString("minutes")));
		    					flightSegment.put("airlineCode", flights.getJSONObject("carrier").getString("airlineCode"));
		    					flightSegment.put("flightNumber", flights.getJSONObject("carrier").getString("flightNumber"));
		    					flightSegment.put("plane", flights.getJSONObject("carrier").getString("plane"));
		    					flightSegment.put("planeCode", flights.getJSONObject("carrier").getString("planeCode"));
		    					flightSegment.put("meal", "");
		    					flightSegment.put("layoverDuration", 0);
		    					segments.add(flightSegment);
		    					
		    					// 航司代码
					    		if(Integer.parseInt(flights.getJSONObject("duration").getString("hours"))*60+Integer.parseInt(flights.getJSONObject("duration").getString("minutes")) >= segmentDuration){
					    			airlineCode = flights.getJSONObject("carrier").getString("airlineCode");
					    		}
		    					// 中转城市
					    		stopover += "-" + flights.getJSONObject("arrivalAirport").getString("code");
		    					len++;
		    				}
		    				else if(flights.getString("type").equals("Layover")){
		    					((JSONObject)segments.get(len-1)).put("layoverDuration", Integer.parseInt(flights.getJSONObject("duration").getString("hours"))*60+Integer.parseInt(flights.getJSONObject("duration").getString("minutes")));
		    				}
		    			}
		    			
		    			String zone = departureTime.split("\\.000")[1];
		    			// 去除时区
		    			departureTime = departureTime.split("\\.000")[0];
		    			arrivalTime = arrivalTime.split("\\.000")[0];
		    			
		    			int stay_days = 0;
				    	if(round){
				    		stay_days = toolUtil.compareDate(backDate,goDate,"MM/dd/yyyy");
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
		    			
		    			// put to singleFlight
		    			JSONObject singleFlight = new JSONObject(); 
		    			if(size == 0){
		    				singleFlight.put("departureCode", departCode);
		    				singleFlight.put("arrivalCode", arrivalCode);
		    			}else{
		    				singleFlight.put("departureCode", arrivalCode);
		    				singleFlight.put("arrivalCode", departCode);
		    			}
		    			singleFlight.put("stopover", stopover);
		    			singleFlight.put("stay_days",stay_days);
		    			singleFlight.put("departAirportCode",departAirportCode);
		    			singleFlight.put("arrivalAirportCode", arrivalAirportCode);
		    			singleFlight.put("duration", duration);
		    			singleFlight.put("departureTime", departureTime);
		    			singleFlight.put("arrivalTime", arrivalTime);
		    			singleFlight.put("currency", currency);
		    			singleFlight.put("priceTotal", price);
		    			singleFlight.put("airlineCode", airlineCode);
		    			singleFlight.put("source", BASE_URL);
		    			singleFlight.put("seat", 0);
		    			singleFlight.put("search_flag", search_flag);
		    			singleFlight.put("cabinClass", cabinClass);
		    			singleFlight.put("segment", segments);
		    			
		    			if(price.length() > 0){
		    				flightInfoArr.add(singleFlight);
		    			}
		    			
		    			size++;
	    			}
	    			JSONObject flightIndex = new JSONObject();
			    	flightIndex.put("flightInfo", flightInfoArr);
	    			
	    			flightInfo.put(unique_flag, flightIndex);
	    		}
    			page.putField("flightInsert", flightInfo);
    			
	    		for(Object flagIndex : flagsArray){	  
	    			String flag = ((JSONObject) flagIndex).getString("flag");
	    			String unique = ((JSONObject) flagIndex).getString("unique_flag");
	    			String price = ((JSONObject) flagIndex).getString("price");
	    			page.addTargetRequest("https://" + BASE_URL+"/Flight-Search-Details?c="+C+"&offerId="+java.net.URLEncoder.encode(flag)+"&unique_flag="+java.net.URLEncoder.encode(unique)+"&price="+price);
	    		}
    		}else{
    			System.out.println("claw failed!!!");
    			if(page.getUrl().toString().split("timer=").length <= 2){
    				System.out.println("retry begining!!!");
    				long time = new Date().getTime();
    				String retryUrl = page.getUrl().toString() + "&timer=" + time; 
    				page.addTargetRequest(retryUrl);
    			}
    			else{
    				System.out.println("Flights-Search retry failed!!!");
    				JSONObject flightInfo = new JSONObject();
    				page.putField("flightInsert", flightInfo);
    			}
    		}
    	}
    	else if(page.getUrl().toString().contains("Flight-Search-Details")){
    		// 获取税金、座位数
    		JSONObject flight =  new JSONObject();
            JSONObject flightInfo =  new JSONObject();
            // flag
            String flag = java.net.URLDecoder.decode(page.getUrl().toString().split("unique_flag=")[1]);
            if(flag.contains("&")){
            	flag = flag.split("&")[0];
            }
            // priceTotal
            String priceTotal =  page.getUrl().toString().split("price=")[1];
            if(priceTotal.contains("&")){
            	priceTotal = priceTotal.split("&")[0];
            }
            // currency
            String currencyInfo = page.getHtml().xpath("//script[@id='seatMapInfo']").toString();
            if(currencyInfo != null){
            	String currency = page.getHtml().xpath("//script[@id='seatMapInfo']").toString().split("currencyCode\":")[1].split("\"")[1];
                flightInfo.put("currency", currency);
            }
            String prices = page.getHtml().xpath("//div[@class='module-inner-compact']/script[@class='ajaxExecutable']").toString();
            if(prices != null){
            	// taxesAndFees
                String tax = prices.split("taxesAndFees\":")[1].split("\"")[1].split("\\$")[1].replace(",", "");
                // flightPrice
                String fare = prices.split("flightPrice\":")[1].split("\"")[1].split("\\$")[1].replace(",", "");
                // unitleft
                String unitleft = prices.split("unitsLeft\":")[1].split(",")[0];
                flightInfo.put("tax", tax);
                flightInfo.put("fare", fare);
                flightInfo.put("passenger", passenger);
                flightInfo.put("seat", unitleft);
            }       
            // Meals by flight number
            List<String> segments = page.getHtml().xpath("//li[contains(@id,'airline-name-automation-label-0')]/allText()").all();
            List<String> meals = page.getHtml().xpath("//li[contains(@id,'planetype-automation-label-0')]/allText()").all();
            JSONObject flightmealCode = new JSONObject();
            for(int i=0; i<segments.size(); i++){
            	String segment = segments.get(i);
            	String flightNum = "";
            	Pattern p = Pattern.compile("[0-9]+");
            	Matcher m = p.matcher(segment);
            	if(m.find()){
            		flightNum = m.group();
            	}
            	String[] mealInfo = meals.get(i).split("\\|");
            	String meal = "";
            	for(int j=1; j<mealInfo.length;j++){
            		meal += mealInfo[j].replaceAll("&nbsp;", "").replaceAll("\\t", "").trim();
            		if(j<mealInfo.length-1){
            			meal += ",";
            		}
            	}
            	flightmealCode.put(flightNum, meal);
            }
            flightInfo.put("segment", flightmealCode);
            flightInfo.put("round", round);
            flightInfo.put("priceTotal", priceTotal);
            flight.put(flag, flightInfo);
            if(flightInfo.getString("seat") == null || flightInfo.getString("fare") == null || flightInfo.getString("tax") == null){
            	System.out.println("Flight-Search-Details failed!!!!");
            	if(page.getUrl().toString().split("timer=").length <= 2){
    				System.out.println("retry begining!!!");
    				long time = new Date().getTime();
    				String retryUrl = page.getUrl().toString() + "&timer=" + time; 
    				page.addTargetRequest(retryUrl);
    			}
    			else{
    				System.out.println("Flight-Search-Details retry failed!!!");
    				page.putField("flightUpdate", flight);
    			}
            }
            else{
            	page.putField("flightUpdate", flight);
            }
        
            // 获取行李信息url加入待抓取队列
            String bagInfo = page.getHtml().xpath("//section[@class='col']/script[2]").toString();
            if(bagInfo != null){
            	String paramsStr = "[" + bagInfo.split("segments: \\[")[1].split("\\]")[0] + "]";
            	String farebasis = bagInfo.split("airFareBasisCodes: \\[")[1].split("\\]")[0].trim();
            	JSONArray params = (JSONArray) JSONArray.parse(paramsStr);
            	JSONArray newParams = new JSONArray();
            	int index = 1;
            	for(Object indexParams : params){
            		JSONObject objectParams = (JSONObject) indexParams;
            		objectParams.put("segmentnumber", index);
            		objectParams.put("farebasis", farebasis);
            		index++;
            		newParams.add(objectParams);
            	}
            	// request
            	Request bagInfoUrl = new Request("https://" + BASE_URL+"/api/flight/bagfeesmcfilterbyacv2?offerId="+java.net.URLEncoder.encode(flag));
            	bagInfoUrl.putExtra("flight_spider_json", newParams.toJSONString());
            	bagInfoUrl.setMethod(HttpConstant.Method.POST);
            	//page.addTargetRequest(bagInfoUrl);
            }
    	}
    	else if(page.getUrl().toString().contains("bagfeesmcfilterbyacv2")){
    		// flag
            String flag = java.net.URLDecoder.decode(page.getUrl().toString().split("offerId=")[1]);
            JSONObject flightBag =  new JSONObject();
            JSONObject bags = new JSONObject();
            // 行李信息
            JSONObject baggageFeeInfo = (JSONObject) JSONObject.parse(page.getJson().jsonPath("baggageFeeInfo").toString());
            if(baggageFeeInfo != null){
	            bags.put("carryOn",baggageFeeInfo.getJSONObject("feeBreakdown").getJSONArray("carryOn").get(0).toString());
	            bags.put("firstCheckedBag",baggageFeeInfo.getJSONObject("feeBreakdown").getJSONArray("firstCheckedBag").get(0).toString());
	            bags.put("secondCheckedBag",baggageFeeInfo.getJSONObject("feeBreakdown").getJSONArray("secondCheckedBag").get(0).toString());
	        }
            flightBag.put(flag, bags);
            page.putField("priceUpdate", flightBag);
    	}
    }

    public Site getSite() {
        return site;
    }
     
    public void test() {
    	Spider spider = Spider.create(this);

    	String filter = "passengers=adults:1";
    	if(passenger == "ADL"){
    		filter = "passengers=adults:1";
    	}
    	String cabin = "";
    	if(cabinClass == "ECO"){
    		cabin = "economy";
    	}
    	else if(cabinClass == "BUS"){
    		cabin = "business";
    	}
    	else if(cabinClass == "FST"){
    		cabin = "first";
    	}
    	else if(cabinClass == "PEC"){
    		cabin = "premium";
    	}
    	else {
    		cabin = "economy";
    	}
    	String leg1 = "=from:"+departCode+",to:"+arrivalCode+",departure:"+goDate+"TANYT";
    	String trip = "oneway";
    	if(round){
    		leg1 += "&leg2=" + "from:"+arrivalCode+",to:"+departCode+",departure:"+backDate+"TANYT";
    		trip = "roundtrip";
    	}
    	String url = "https://" + BASE_URL + "/Flights-Search?trip="
    			+trip+
    			"&leg1"+leg1+"&"+filter+"&options=cabinclass:"+cabin+"&mode=search";
    	spider
    	.addUrl(url)
    	.setDownloader(new HttpClient2Downloader())
    	.addPipeline(pipeline)
    	.thread(this.thread_num).run();
    }
}