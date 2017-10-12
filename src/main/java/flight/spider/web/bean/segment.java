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
@Table(name = "segment", catalog = "OTA", indexes={@Index(columnList="unique_flag",name="KEY_unique_flag")})
public class segment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1972879043342653720L;
	private int id;
	private String departure_code;
	private String arrival_code;
	private String departure_time;
	private String arrival_time;
	private int duration;
	private String airline_code;
	private int stop_duration;
	private String meal_type;
	private Date create_at;
	private Date update_at;
	private String plane_code;
	private String plane;
	private String unique_flag;
	private int flight_num;

	public segment() {
		super();
	}

	public segment(int id, String departure_code,
			String arrival_code, String departure_time, String arrival_time,
			int duration, String airline_code, int stop_duration,
			String meal_type, Date create_at, Date update_at, String plane_code, String plane, String unique_flag, int flight_num) {
		super();
		this.id = id;
		this.departure_code = departure_code;
		this.arrival_code = arrival_code;
		this.departure_time = departure_time;
		this.arrival_time = arrival_time;
		this.duration = duration;
		this.airline_code = airline_code;
		this.stop_duration = stop_duration;
		this.meal_type = meal_type;
		this.create_at = create_at;
		this.update_at = update_at;
		this.plane_code = plane_code;
		this.plane = plane;
		this.unique_flag = unique_flag;
		this.flight_num = flight_num;
	}
	
	public segment(String departure_code,
			String arrival_code, String departure_time, String arrival_time,
			int duration, String airline_code, int stop_duration,
			String meal_type, Date create_at, String plane_code, String plane, String unique_flag, int flight_num) {
		super();
		this.departure_code = departure_code;
		this.arrival_code = arrival_code;
		this.departure_time = departure_time;
		this.arrival_time = arrival_time;
		this.duration = duration;
		this.airline_code = airline_code;
		this.stop_duration = stop_duration;
		this.meal_type = meal_type;
		this.create_at = create_at;
		this.plane_code = plane_code;
		this.plane = plane;
		this.unique_flag = unique_flag;
		this.flight_num = flight_num;
	}
	
	public void reset(String departure_code,
			String arrival_code, String departure_time, String arrival_time,
			int duration, String airline_code, int stop_duration,
			String meal_type, String plane_code, String plane, String unique_flag, int flight_num, Date update_at) {
		this.departure_code = departure_code;
		this.arrival_code = arrival_code;
		this.departure_time = departure_time;
		this.arrival_time = arrival_time;
		this.duration = duration;
		this.airline_code = airline_code;
		this.stop_duration = stop_duration;
		this.meal_type = meal_type;
		this.plane_code = plane_code;
		this.plane = plane;
		this.unique_flag = unique_flag;
		this.flight_num = flight_num;
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

	@Column(name = "airline_code", length = 100, nullable = false)
	public String getAirline_code() {
		return airline_code;
	}

	public void setAirline_code(String airline_code) {
		this.airline_code = airline_code;
	}
	
	@Column(name = "stop_duration", nullable = false, columnDefinition = "bigint")
	public int getStop_duration() {
		return stop_duration;
	}

	public void setStop_duration(int stop_duration) {
		this.stop_duration = stop_duration;
	}

	@Column(name = "meal_type", length = 100, nullable = false)
	public String getMeal_type() {
		return meal_type;
	}

	public void setMeal_type(String meal_type) {
		this.meal_type = meal_type;
	}

	@Column(name = "create_at")
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

	@Column(name = "plane_code", length = 255, nullable = false)
	public String getPlane_code() {
		return plane_code;
	}

	public void setPlane_code(String plane_code) {
		this.plane_code = plane_code;
	}
	
	@Column(name = "plane", length = 255, nullable = false)
	public String getPlane() {
		return plane;
	}

	public void setPlane(String plane) {
		this.plane = plane;
	}
	
	@Column(name = "unique_flag", length = 255, nullable = false)
	public String getUnique_flag() {
		return unique_flag;
	}

	public void setUnique_flag(String unique_flag) {
		this.unique_flag = unique_flag;
	}
	
	@Column(name = "flight_num", nullable = false, columnDefinition = "bigint")
	public int getFlight_num() {
		return flight_num;
	}

	public void setFlight_num(int flight_num) {
		this.flight_num = flight_num;
	}
}
