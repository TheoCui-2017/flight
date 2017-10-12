package flight.spider.web.utility;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class toolUtil {
	
	public  static String getFetureDate(int past, String format_exp) {  
    	Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);  
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(format_exp);  
        String result = format.format(today);
        return result;  
    }
	
	public static String getFetureDateForDate(String date, int past, String formatDate){
		String result = "";
		if(date != null){
			SimpleDateFormat format = new SimpleDateFormat(formatDate);
			try {
				Date newDate = format.parse(date);
				long time = newDate.getTime() + 3600*24*1000*past;
				result = format.format(time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static int compareDate(String date1, String date2, String formatDate){
		int diff = 0;
		if(date1 != null && date2 != null){
			SimpleDateFormat format = new SimpleDateFormat(formatDate);
			try {
				Date newDate1 = format.parse(date1);
				Date newDate2 = format.parse(date2);
				long milsecsDiff = (newDate1.getTime() - newDate2.getTime())/1000;
				int milsecs = (int)(milsecsDiff);
				diff = milsecs / (3600*24);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return diff;
	}
	
	public  static String formatDate(String date, String format_exp) {  
    	if(date != null && date.length() > 0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date newdate;
			try {
				newdate = sdf.parse(date);
				SimpleDateFormat format = new SimpleDateFormat(format_exp);  
			    String result = format.format(newdate);
			    return result;  
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		return date;
    }
	
	public  static Date formatDate(String date) {  
		Date d = null;
    	if(date != null){
			SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
			try {
				d = format.parse(date); 
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		return d;
    }
	
	public static String getIp() {  
        String localip = null;// 本地IP，如果没有配置外网IP则返回它  
        String netip = null;// 外网IP  
        try {  
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface  
                    .getNetworkInterfaces();  
            InetAddress ip = null;  
            boolean finded = false;// 是否找到外网IP  
            while (netInterfaces.hasMoreElements() && !finded) {  
                NetworkInterface ni = netInterfaces.nextElement();  
                Enumeration<InetAddress> address = ni.getInetAddresses();  
                while (address.hasMoreElements()) {  
                    ip = address.nextElement();  
//                  System.out.println(ni.getName() + ";" + ip.getHostAddress()  
//                          + ";ip.isSiteLocalAddress()="  
//                          + ip.isSiteLocalAddress()  
//                          + ";ip.isLoopbackAddress()="  
//                          + ip.isLoopbackAddress());  
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()  
                            && ip.getHostAddress().indexOf(":") == -1) {// 外网IP  
                        netip = ip.getHostAddress();  
                        finded = true;  
                        break;  
                    } else if (ip.isSiteLocalAddress()  
                            && !ip.isLoopbackAddress()  
                            && ip.getHostAddress().indexOf(":") == -1) {// 内网IP  
                        localip = ip.getHostAddress();  
                    }  
                }  
            }  
        } catch (SocketException e) {  
            e.printStackTrace();  
        }  
        if (netip != null && !"".equals(netip)) {  
            return netip;  
        } else {  
            return localip;  
        }  
    }  

	
	public static JSONObject flightObjToJson(List<Object[]> list, String start, String end, int dimension, List<String> airlineCodes){
		JSONObject json = new JSONObject(true);
		int days = compareDate(end, start, "yyyy-MM-dd");
		double num =  (double)days/dimension;
		int dimensionNum = (int)Math.ceil(num);
		String startDate = start;
		String endDate = "";
		
		for(int i=0; i<dimensionNum; i++){
			if(toolUtil.compareDate(end, startDate, "yyyy-MM-dd") <= dimension){
				endDate = end;
			}
			else{
				endDate = toolUtil.getFetureDateForDate(startDate, dimension, "yyyy-MM-dd");
			}
			JSONArray flightInfo = new JSONArray();
			String realStartDate = "";
			for(Object[] s : list){
				String departTime = s[2].toString();
				boolean between = beBetween(departTime,startDate, endDate);
				JSONObject flight = new JSONObject();
				if(between){
					realStartDate = startDate;
					flight.put("startDate", startDate);
					flight.put("airline_code", s[0].toString());
					flight.put("price", s[1].toString());
					flightInfo.add(flight);
				}
			}
			JSONArray avgArr = new JSONArray();
			for(String airline_code : airlineCodes){
				double price = 0;
				int len = 0;
				double avg = 0;
				JSONObject code_price = new JSONObject();
				for(Object flight : flightInfo){
					JSONObject flightIndex = (JSONObject)flight;
					if(airline_code.toLowerCase().equals(flightIndex.getString("airline_code").toLowerCase())){
						BigDecimal a = new BigDecimal(flightIndex.getString("price"));
						BigDecimal b = new BigDecimal(Double.toString(price));
						price = a.add(b).doubleValue();
						len ++;
					}
				}
				finance financeService = (finance)springContextUtil.getBean("finance");
				double rate = financeService.getRate("USD");
				if(len != 0){
					avg = (double) price/len;
					avg = Double.valueOf(financeService.toRMBPrice(rate, Double.toString(avg)));
				}
				code_price.put(airline_code, avg);
				avgArr.add(code_price);
			}
			if(realStartDate.length() > 0){
				json.put(realStartDate, avgArr);
			}
			startDate = endDate;
		}
		return json;
	}
	
	public static boolean beBetween(String time, String start, String end){
		boolean result = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			Date timeDate = formatT.parse(time);
			long timeNum = timeDate.getTime();
			Date startDate = format.parse(start);
			long startNum = startDate.getTime();
			Date endDate = format.parse(end);
			long endNum = endDate.getTime();
			if(timeNum > startNum && timeNum < endNum){
				result = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return result;
		
	}
}
