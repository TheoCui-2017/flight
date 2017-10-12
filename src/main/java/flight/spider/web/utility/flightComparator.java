package flight.spider.web.utility;

import java.util.Comparator;

import flight.spider.web.bean.flight;

public class flightComparator implements Comparator<flight>{

	public int compare(flight f1, flight f2) {
		Double f1_price = Double.parseDouble(f1.getTotalPrice());
		Double f2_price = Double.parseDouble(f2.getTotalPrice());
		return f1_price.compareTo(f2_price);
    	
    }  
}
