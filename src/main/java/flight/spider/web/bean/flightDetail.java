package flight.spider.web.bean;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import flight.spider.web.utility.finance;
public class flightDetail {

	private boolean round;
	private flight flightInfo;
	private List<segment> segments;
	private price priceInfo;
	
	public flightDetail(){
		super();
	}
	
	public void setRound(boolean round){
		this.round = round;
	}
	
	public void setFlightInfo(flight flightInfo) {
		this.flightInfo = flightInfo;
	}
	
	public void setSegments(List<segment> segments) {
		this.segments = segments;
	}
	
	public void setPriceInfo(price priceInfo) {
		this.priceInfo = priceInfo;
	}
	
	// 单程／往返
	public boolean getRound(){
		return round;
	}
	
	//航班
	public String getAirLine(){
		String airline = "";
		String airlineOW = "";
		String airlineRT = "";
		for(segment seg:segments){
			if(round){
				if(seg.getUnique_flag().substring(0, 1).equals("1")){
					airlineOW += seg.getAirline_code()+seg.getFlight_num();
				}
				else{
					airlineRT += seg.getAirline_code()+seg.getFlight_num();
				}
			}
			else{
				airline+=seg.getAirline_code()+seg.getFlight_num();
			}
		}
		if(round){
			airline += airlineOW +"/" + airlineRT;
		}
		return airline;
	}
	
	//来源
	public String getSource(){
		return flightInfo.getSource();
	}
	
	//出发时间
	public String getGoTime(){
		String goTime = "";
		if(round){
			goTime = flightInfo.getDeparture_time().split("\\$")[0];
		}
		else{
			goTime = flightInfo.getDeparture_time();
		}
		return goTime;
	}
	
	//返程时间
	public String getBackTime(){
		String backTime = "";
		if(round){
			backTime = flightInfo.getDeparture_time().split("\\$")[1];
		}
		return backTime;
	}
	
	// 出发日期
	public String getGoDate(){
		String goTime = flightInfo.getDeparture_time();
		String goDate = goTime.toLowerCase().split("t")[0];
		if(round){
			goDate = goTime.toLowerCase().split("\\$")[0].split("t")[0];
		}
		return goDate;
	}
	
	// 返程日期
	public String getBackDate(){
		String backDate = "";
		if(round){
			backDate = flightInfo.getDeparture_time().toLowerCase().split("\\$")[1].split("t")[0];
		}
		return backDate;
	}
	
	//直飞/中转
	public String getTransferOrDirect(){
		String str = "直飞";
		if(round){
			String strOW = "直飞";
			String strRT = "直飞";
			String OW = "";
			String RT = "";
			String transferCities = getTransferCities();
			if(transferCities.split("\\/").length == 0){
				OW = "";
				RT = "";
			}else if(transferCities.split("\\/").length == 1){
				OW = transferCities.split("\\/")[0];
				RT = "";
			}
			else{
				OW = transferCities.split("\\/")[0];
				RT = transferCities.split("\\/")[1];
			}
			if(OW.length() > 0){
				strOW = "中转";
			}
			if(RT.length() > 0){
				strRT = "中转";
			}
			str = strOW + "/" + strRT;
		}
		else{
			if(getTransferCities().length() > 0){
				str = "中转";
			}
		}
		return str;
	}
	
	//航司代码
	public String getAirlineCodes(){
		String airlineCode = flightInfo.getAirline_code();		
		if(round){
			String airlineCodeOW = flightInfo.getAirline_code().split("\\$")[0];
			String airlineCodeRT = flightInfo.getAirline_code().split("\\$")[1];
			airlineCode = airlineCodeOW + "/" + airlineCodeRT;
		}
		return airlineCode;
	}
	
	//舱位等级
	public String getSeatType(){
		int seat_type = flightInfo.getSeat_type();
		if(seat_type == 3)
			return "头等舱";
		else if(seat_type == 2)
			return "商务舱";
		else
			return "经济舱";
	}
	
	//人民币售价
	public String getTotalPrice(){
		synchronized(finance.currency_rate) {  
			Double rate = finance.currency_rate.get(flightInfo.getCurrency_unit());
			if(rate != null){
				Double price = rate*Double.valueOf(flightInfo.getTotalPrice());
				DecimalFormat df = new DecimalFormat("#.00");  
				return df.format(price);
			}
			else{
				return "0";
			}
		}
	}
	
	//原币种
	public String getCurrency(){
		return flightInfo.getCurrency_unit();
	}
	
	//原币售价
	public String getOriginPrice(){
		return flightInfo.getTotalPrice();
	}
	
	//显示票价
	public float getFare(){
		return priceInfo==null?0:priceInfo.getFare();
	}
	
	//显示税费
	public float getTax(){
		return priceInfo==null?0:priceInfo.getTax();
	}
	
	//预定费用
	public String getOrderPrice(){
		return flightInfo.getTotalPrice();
	}
	
	//显示燃油费
	public float getFuelPrice(){
		return 0;
	}
	
	//留学生特价
	public float specialPrice(){
		return 0;
	}
	
	//中转城市
	public String getTransferCities(){
		String transferCities = flightInfo.getStopover();		
		if(round){
			String transferCitiesOW = flightInfo.getStopover().split("\\$")[0];
			String transferCitiesRT = flightInfo.getStopover().split("\\$")[1];
			if(transferCitiesOW.equals("0")){
				transferCitiesOW = "";
			}
			if(transferCitiesRT.equals("0")){
				transferCitiesRT = "";
			}
			transferCities = transferCitiesOW + "/" + transferCitiesRT;
		}
		return transferCities;
	}
	
	//总时间
	public int getDuration(){
		return flightInfo.getDuration();
	}
	
	public Date getUpdateTime(){
		if(flightInfo.getUpdate_at()!=null)
			return flightInfo.getUpdate_at();
		else
			return flightInfo.getCreate_at();
	}
}
