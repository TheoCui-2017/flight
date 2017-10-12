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
@Table(name = "city", catalog = "OTA",
		indexes={@Index(columnList="code",name="KEY_code"),  
				@Index(columnList="country",name="KEY_country"),
				@Index(columnList="area",name="KEY_area")
})
public class city implements Serializable{

	private static final long serialVersionUID = -2036781637183612975L;
	private int id;
	private String name;
	private String code;
	private String country;
	private String area;
	private String querys;
	private Date create_at;
	private Date update_at;

	public city() {
		super();
	}
	
	public city(int id, String name, String code, String country, String area, String querys, Date create_at, Date update_at) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.country = country;
		this.area = area;
		this.querys = querys;
		this.create_at = create_at;
		this.update_at = update_at;
	}
	
	public city(String name, String code, String country, String area, String querys, Date create_at) {
		super();
		this.name = name;
		this.code = code;
		this.country = country;
		this.area = area;
		this.querys = querys;
		this.create_at = create_at;
	}
	
	public void reset(String name, String code, String country, String area, String querys, Date update_at) {
		this.name = name;
		this.code = code;
		this.country = country;
		this.area = area;
		this.querys = querys;
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
	
	@Column(name = "name", length = 200, nullable = false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "code", length = 20, nullable = false)
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "country", length = 200, nullable = false)
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	
	@Column(name = "area", length = 255, nullable = false)
	public String getArea() {
		return area;
	}
	
	public void setArea(String area) {
		this.area = area;
	}
	
	@Column(name = "querys", length = 255, nullable = false)
	public String getQuerys() {
		return querys;
	}
	
	public void setQuerys(String querys) {
		this.querys = querys;
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
