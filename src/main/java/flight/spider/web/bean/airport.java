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
@Table(name = "airport", catalog = "OTA",
		indexes={@Index(columnList="cityId",name="KEY_CityId"),  
				@Index(columnList="code",name="KEY_Code")
})
public class airport implements Serializable{

	private static final long serialVersionUID = -2036781637183612975L;
	private int id;
	private int cityId;
	private String name;
	private String code;
	private String querys;
	private Date create_at;
	private Date update_at;

	public airport() {
		super();
	}
	
	public airport(int id, int cityId, String name, String code, String querys, Date create_at, Date update_at) {
		super();
		this.id = id;
		this.cityId = cityId;
		this.name = name;
		this.code = code;
		this.querys = querys;
		this.create_at = create_at;
		this.update_at = update_at;
	}
	
	public airport(int cityId, String name, String code, String querys, Date create_at) {
		super();
		this.cityId = cityId;
		this.name = name;
		this.code = code;
		this.querys = querys;
		this.create_at = create_at;
	}
	
	public void reset(int cityId, String name, String code, String querys, Date update_at) {
		this.cityId = cityId;
		this.name = name;
		this.code = code;
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
	
	@Column(name = "cityId", length = 200, nullable = false)
	public int getCityId() {
		return cityId;
	}
	
	public void setCityId(int cityId) {
		this.cityId = cityId;
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
