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
@Table(name = "privilege", catalog = "OTA", 
	indexes={@Index(columnList="username",name="KEY_username"),
		@Index(columnList="uri",name="KEY_uri")
})
public class privilege implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -463081291081504376L;
	private int id;
	private String username;
	private String uri;
	private Date create_at;

	public privilege() {
		super();
	}
	
	public privilege(int id, String username, String uri, Date create_at, Date update_at) {
		super();
		this.id = id;
		this.username = username;
		this.uri = uri;
		this.create_at = create_at;
	}
	
	public privilege(String username, String uri, Date create_at) {
		super();
		this.username = username;
		this.uri = uri;
		this.create_at = create_at;
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
	
	@Column(name = "username", length = 200, nullable = false)
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "uri", length = 200, nullable = false)
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	@Column(name = "create_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreate_at() {
		return create_at;
	}
	
	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}
}
