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
@Table(name = "scheduleJob", catalog = "OTA", indexes={@Index(columnList="legid",name="KEY_legid")})
public class scheduleJob implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5172809699144983371L;
	
	private int id;
	private int legid;
	private int scheduleid;
	private Date create_at;

	public scheduleJob() {
		super();
	}
	
	public scheduleJob(int id, int legid, int scheduleid, Date create_at) {
		super();
		this.id = id;
		this.legid = legid;
		this.scheduleid = scheduleid;
		this.create_at = create_at;
		
	}
	
	public scheduleJob(int legid,int scheduleid, Date create_at) {
		super();
		this.legid = legid;
		this.scheduleid = scheduleid;
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
	
	@Column(name = "legid", nullable = false, columnDefinition = "int")
	public int getLegid() {
		return legid;
	}

	public void setLegid(int legid) {
		this.legid = legid;
	}
	
	@Column(name = "scheduleid", nullable = false)
	public int getScheduleid() {
		return scheduleid;
	}

	public void setScheduleid(int scheduleid) {
		this.scheduleid = scheduleid;
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
