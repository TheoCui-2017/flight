package flight.spider.web.utility;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;

import flight.spider.web.bean.currency;
import flight.spider.web.service.flightInfoService;

@Repository
public class finance{
	
	@Autowired
	flightInfoService flightService;
	
	public static Map<String,Double> currency_rate = new HashMap<String,Double>();
	
	@PostConstruct
	void init(){
		// 判断map是否已存在
		if(currency_rate.isEmpty()){
			// 获取需要初始化的货币
			List<currency> currencyDetails = flightService.getCurrencys();
			for(currency currencyDetail :currencyDetails){
				String code = currencyDetail.getCode();
				String rateStr =currencyDetail.getRate();
				if(rateStr != null && !rateStr.equals("") && rateStr.length() != 0){
					Double rate = Double.valueOf(currencyDetail.getRate());
					currency_rate.put(code, rate);
				}
				else{
					updateSingleRate(code, "CNY", null);
				}
			}
		}
	}
	
	// 定时更新
	public void updateRate(){
		// 获取货币
		for(String  key : currency_rate.keySet()){
			String tcur = "CNY";
			updateSingleRate(key, tcur, null);
		}
	}
	
	// 更新单种货币汇率
	public void updateSingleRate(String scur, String tcur, String rate){
		synchronized(currency_rate){
			try {
				if(rate == "" || rate == null){
					rate = getRate(scur, tcur);
				}
				if(rate != "" && rate != null){
					// update rate
					flightService.updateRateByCode(scur,rate);
					// update map
					currency_rate.put(scur,Double.valueOf(rate));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 获取汇率
	public String getRate(String scur, String tcur) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("http://api.k780.com:88/?app=finance.rate&scur="+scur+"&tcur="+tcur+"&appkey=22188&sign=de87aa6202253a673683dfa6e5be57e9&format=json");
		String body = "";
		String tempString = null;
		CloseableHttpResponse response= httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		InputStream instream = entity.getContent();  
		BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
		while((tempString = reader.readLine()) != null){
			body+=tempString;
		}
		reader.close();
		JSONObject json = (JSONObject) JSONObject.parse(body);
		String rate = "";
		if(json.getInteger("success") == 1){
			rate = json.getJSONObject("result").getString("rate");
		}
		return rate;
	}
	
	public double getRate(String currency){
		synchronized(finance.currency_rate) {  
			Double rate = finance.currency_rate.get(currency);
			return rate;
		}
	}
	
	public String toRMBPrice(Double num, String priceStr){
		if(num != null){
			Double price = num*Double.valueOf(priceStr);
			DecimalFormat df = new DecimalFormat("#.00");  
			return df.format(price);
		}
		else{
			return "0";
		}
	}
	
}
