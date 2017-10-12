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
@Table(name = "currency", catalog = "OTA",indexes={@Index(columnList="code",name="KEY_code")})
public class currency implements Serializable{

	private static final long serialVersionUID = -2036781637183612975L;
	private int id;
	private String code;
	private String name;
	private String rate;
	private Date create_at;
	private Date update_at;

	public currency() {
		super();
	}
	
	public currency(int id, String code, String name, String rate, Date create_at, Date update_at) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.rate = rate;
		this.create_at = create_at;
		this.update_at = update_at;
	}
	
	public currency(String code, String name, String rate, Date create_at) {
		super();
		this.code = code;
		this.name = name;
		this.rate = rate;
		this.create_at = create_at;
	}
	
	public void reset(String code, String name, String rate, Date update_at) {
		this.code = code;
		this.name = name;
		this.rate = rate;
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
	
	@Column(name = "code", length = 20, nullable = false)
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "rate", length = 50, nullable = false)
	public String getRate() {
		return rate;
	}
	
	public void setRate(String rate) {
		this.rate = rate;
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
