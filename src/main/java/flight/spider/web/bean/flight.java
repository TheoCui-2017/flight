package flight.spider.web.bean;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "flight", catalog = "OTA",
		indexes={@Index(columnList="departure_code",name="KEY_departure_code"),  
        		@Index(columnList="arrival_code",name="KEY_arrival_code"),
        		@Index(columnList="departure_time",name="KEY_departure_time"),
        		@Index(columnList="seat_type",name="KEY_seat_type"),
        		@Index(columnList="source",name="KEY_source"),
        		@Index(columnList="unique_flag",name="KEY_unique_flag"),
        		@Index(columnList="search_flag",name="KEY_search_flag"),
        		@Index(columnList="stopover",name="KEY_stopover"),
        		@Index(columnList="round",name="KEY_round")
        })
public class flight implements Serializable{

	private static final long serialVersionUID = -2036781637183612975L;
	private int id;
	private String departure_code;
	private String arrival_code;
	private String departure_time;
	private String arrival_time;
	private int duration;
	private int seat_number;
	private int seat_type;
	private String airline_code;
	private String source;
	private String unique_flag;
	private Date create_at;
	private Date update_at;
	private String totalPrice;
	private String search_flag;
	private String currency_unit;
	private String depart_airport_code;
	private String arrival_airport_code;
	private boolean round;
	private int stay_days;
	private String stopover;
	
	public flight() {
		super();
	}
	
	public flight(int id, String departure_code, String arrival_code,
			String departure_time, String arrival_time, int duration,
			int seat_number, int seat_type, String airline_code, String source,
			String unique_flag, String totalPrice, String search_flag, String currency_unit,
			String depart_airport_code, String arrival_airport_code, boolean round, int stay_days,
			String stopover, Date create_at, Date update_at) {
		super();
		this.id = id;
		this.departure_code = departure_code;
		this.arrival_code = arrival_code;
		this.departure_time = departure_time;
		this.arrival_time = arrival_time;
		this.duration = duration;
		this.seat_number = seat_number;
		this.seat_type = seat_type;
		this.airline_code = airline_code;
		this.source = source;
		this.unique_flag = unique_flag;
		this.totalPrice = totalPrice;
		this.search_flag = search_flag;
		this.create_at = create_at;
		this.update_at = update_at;
		this.currency_unit = currency_unit;
		this.depart_airport_code = depart_airport_code;
		this.arrival_airport_code = arrival_airport_code;
		this.round = round;
		this.stopover = stopover;
		this.stay_days = stay_days;
	}
	
	public flight(String departure_code, String arrival_code,
			String departure_time, String arrival_time, int duration,
			int seat_number, int seat_type, String airline_code, String source,
			String unique_flag, String totalPrice, String search_flag, String currency_unit, 
			String depart_airport_code, String arrival_airport_code, boolean round, 
			int stay_days, String stopover, Date create_at) {
		super();
		this.departure_code = departure_code;
		this.arrival_code = arrival_code;
		this.departure_time = departure_time;
		this.arrival_time = arrival_time;
		this.duration = duration;
		this.seat_number = seat_number;
		this.seat_type = seat_type;
		this.airline_code = airline_code;
		this.source = source;
		this.unique_flag = unique_flag;
		this.totalPrice = totalPrice;
		this.search_flag = search_flag;
		this.currency_unit = currency_unit;
		this.depart_airport_code = depart_airport_code;
		this.arrival_airport_code = arrival_airport_code;
		this.round = round;
		this.stay_days = stay_days;
		this.stopover = stopover;
		this.create_at = create_at;
	}
	
	public void reset(String departure_code, String arrival_code,
			String departure_time, String arrival_time, int duration,
			int seat_number, int seat_type, String airline_code, String source,
			String unique_flag, String totalPrice, String search_flag, String currency_unit, 
			String depart_airport_code, String arrival_airport_code,boolean round, 
			int stay_days, String stopover, Date update_at) {
		this.departure_code = departure_code;
		this.arrival_code = arrival_code;
		this.departure_time = departure_time;
		this.arrival_time = arrival_time;
		this.duration = duration;
		this.seat_number = seat_number;
		this.seat_type = seat_type;
		this.airline_code = airline_code;
		this.source = source;
		this.unique_flag = unique_flag;
		this.totalPrice = totalPrice;
		this.search_flag = search_flag;
		this.currency_unit = currency_unit;
		this.depart_airport_code = depart_airport_code;
		this.arrival_airport_code = arrival_airport_code;
		this.round = round;
		this.stay_days = stay_days;
		this.stopover = stopover;
		this.update_at = update_at;
	}

	@GenericGenerator(name = "generator", strategy = "native")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "depart_airport_code", length = 100, nullable = false)
	public String getDepart_airport_code() {
		return depart_airport_code;
	}
	
	public void setDepart_airport_code(String depart_airport_code) {
		this.depart_airport_code = depart_airport_code;
	}
	
	@Column(name = "arrival_airport_code", length = 100, nullable = false)
	public String getArrival_airport_code() {
		return arrival_airport_code;
	}
	
	public void setArrival_airport_code(String arrival_airport_code) {
		this.arrival_airport_code = arrival_airport_code;
	}
	
	
	@Column(name = "departure_code", length = 100, nullable = false)
	public String getDeparture_code() {
		return departure_code;
	}
	
	public void setDeparture_code(String departure_code) {
		this.departure_code = departure_code;
	}
	
	@Column(name = "arrival_code", length = 100, nullable = false)
	public String getArrival_code() {
		return arrival_code;
	}
	
	public void setArrival_code(String arrival_code) {
		this.arrival_code = arrival_code;
	}
	
	@Column(name = "round", nullable = false)
	public boolean getRound() {
		return round;
	}
	
	public void setRound(boolean round) {
		this.round = round;
	}
	
	@Column(name = "departure_time", length = 100, nullable = false)
	public String getDeparture_time() {
		return departure_time;
	}
	
	public void setDeparture_time(String departure_time) {
		this.departure_time = departure_time;
	}
	
	@Column(name = "arrival_time", length = 100, nullable = false)
	public String getArrival_time() {
		return arrival_time;
	}
	
	public void setArrival_time(String arrival_time) {
		this.arrival_time = arrival_time;
	}
	
	@Column(name = "duration", nullable = false, columnDefinition = "bigint")
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	@Column(name = "seat_number", nullable = false, columnDefinition = "bigint")
	public int getSeat_number() {
		return seat_number;
	}
	
	public void setSeat_number(int seat_number) {
		this.seat_number = seat_number;
	}
	
	@Column(name = "stay_days", nullable = false, columnDefinition = "int")
	public int getStay_days() {
		return stay_days;
	}
	
	public void setStay_days(int stay_days) {
		this.stay_days = stay_days;
	}
	
	@Column(name = "seat_type", nullable = false, columnDefinition = "bigint")
	public int getSeat_type() {
		return seat_type;
	}
	
	public void setSeat_type(int seat_type) {
		this.seat_type = seat_type;
	}
	
	@Column(name = "airline_code", length = 100, nullable = false)
	public String getAirline_code() {
		return airline_code;
	}
	
	public void setAirline_code(String airline_code) {
		this.airline_code = airline_code;
	}
	
	@Column(name = "source", length = 255, nullable = false)
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	@Column(name = "unique_flag", length = 255, nullable = false)
	public String getUnique_flag() {
		return unique_flag;
	}
	
	public void setUnique_flag(String unique_flag) {
		this.unique_flag = unique_flag;
	}
	
	@Column(name = "create_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreate_at() {
		return create_at;
	}
	
	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}
	
	@Column(name = "update_at", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdate_at() {
		return update_at;
	}
	
	public void setUpdate_at(Date update_at) {
		this.update_at = update_at;
	}

	@Column(name = "totalPrice", length = 255, nullable = false)
	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Column(name = "stopover", length = 200, nullable = true)
	public String getStopover() {
		return stopover;
	}

	public void setStopover(String stopover) {
		this.stopover = stopover;
	}
	
	@Column(name = "search_flag", length = 255, nullable = false)
	public String getSearch_flag() {
		return search_flag;
	}

	public void setSearch_flag(String search_flag) {
		this.search_flag = search_flag;
	}
	
	@Column(name = "currency_unit", length = 100, nullable = false)
	public String getCurrency_unit() {
		return currency_unit;
	}

	public void setCurrency_unit(String currency_unit) {
		this.currency_unit = currency_unit;
	}
}
