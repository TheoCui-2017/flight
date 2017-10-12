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
@Table(name = "leg", catalog = "OTA",
		indexes={@Index(columnList="depart_city_code",name="KEY_depart_city_code"),  
				@Index(columnList="arrival_city_code",name="KEY_arrival_city_code")
})
public class leg implements Serializable{

	private static final long serialVersionUID = -2036781637183612975L;
	private int id;
	private String depart_city;
	private String depart_city_code;
	private String arrival_city;
	private String arrival_city_code;
	private String area;
	private Date create_at;
	private Date update_at;

	public leg() {
		super();
	}
	
	public leg(int id, String depart_city, String depart_city_code, String arrival_city, String arrival_city_code, String area, Date create_at, Date update_at) {
		super();
		this.id = id;
		this.depart_city = depart_city;
		this.depart_city_code = depart_city_code;
		this.arrival_city = arrival_city;
		this.arrival_city_code = arrival_city_code;
		this.area = area;
		this.create_at = create_at;
		this.update_at = update_at;
	}
	
	public leg(String depart_city, String depart_city_code, String arrival_city, String arrival_city_code, String area, Date create_at) {
		super();
		this.depart_city = depart_city;
		this.depart_city_code = depart_city_code;
		this.arrival_city = arrival_city;
		this.arrival_city_code = arrival_city_code;
		this.area = area;
		this.create_at = create_at;
	}
	
	public void reset(String depart_city, String depart_city_code, String arrival_city, String arrival_city_code, String area, Date update_at) {
		this.depart_city = depart_city;
		this.depart_city_code = depart_city_code;
		this.arrival_city = arrival_city;
		this.arrival_city_code = arrival_city_code;
		this.area = area;
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
	
	@Column(name = "depart_city", length = 200, nullable = false)
	public String getDepartCity() {
		return depart_city;
	}
	
	public void setDepartCity(String depart_city) {
		this.depart_city = depart_city;
	}
	
	@Column(name = "depart_city_code", length = 20, nullable = false)
	public String getDepartCityCode() {
		return depart_city_code;
	}
	
	public void setDepartCityCode(String depart_city_code) {
		this.depart_city_code = depart_city_code;
	}
	
	@Column(name = "arrival_city", length = 200, nullable = false)
	public String getArrivalCity() {
		return arrival_city;
	}
	
	public void setArrivalCity(String arrival_city) {
		this.arrival_city = arrival_city;
	}
	
	@Column(name = "arrival_city_code", length = 20, nullable = false)
	public String getArrivalCityCode() {
		return arrival_city_code;
	}
	
	public void setArrivalCityCode(String arrival_city_code) {
		this.arrival_city_code = arrival_city_code;
	}
	
	@Column(name = "area", length = 300, nullable = false)
	public String getArea() {
		return area;
	}
	
	public void setArea(String area) {
		this.area = area;
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
}
