package flight.spider.web.bean;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "schedule", catalog = "OTA",indexes={@Index(columnList="source",name="KEY_source")})
public class schedule implements Serializable{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4542763615511453647L;
	private int id;
	private String source;
	private String crond_time = "0 0 0/12 * * ?";
	private int concurrency_num = 10;
	private int days = 7;
	private int status = 0;
	private String interval_days="7,14,30";
	private String ips;
	private String start_at;
	private String end_at;
	private Date create_at;
	private Date update_at;

	public schedule() {
		super();
	}
	
	public schedule(int id, String source, String crond_time,
			int concurrency_num, int days, int status, String interval_days, String ips, String start_at, String end_at, Date create_at, Date update_at) {
		super();
		this.id = id;
		this.source = source;
		this.crond_time = crond_time;
		this.concurrency_num = concurrency_num;
		this.days = days;
		this.status = status;
		this.interval_days = interval_days;
		this.ips = ips;
		this.start_at = start_at;
		this.end_at = end_at;
		this.create_at = create_at;
		this.update_at = update_at;
	}
	
	public schedule(String source, String crond_time,
			int concurrency_num, int days, int status, String interval_days, String ips, String start_at, String end_at, Date create_at) {
		super();
		this.source = source;
		this.crond_time = crond_time;
		this.concurrency_num = concurrency_num;
		this.days = days;
		this.status = status;
		this.interval_days = interval_days;
		this.ips = ips;
		this.start_at = start_at;
		this.end_at = end_at;
		this.create_at = create_at;
	}
	
	public schedule( String source, Date create_at) {
		super();
		this.source = source;
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
	
	@Column(name = "source", length = 100, nullable = false)
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	@Column(name = "crond_time", length = 100, nullable = true)
	public String getCrond_time() {
		return crond_time;
	}
	
	public void setCrond_time(String crond_time) {
		this.crond_time = crond_time;
	}
	
	@Column(name = "concurrency_num", nullable = true)
	public int getConcurrency_num() {
		return concurrency_num;
	}
	
	public void setConcurrency_num(int concurrency_num) {
		this.concurrency_num = concurrency_num;
	}
	
	@Column(name = "days", nullable = true)
	public int getDays() {
		return days;
	}
	
	public void setDays(int days) {
		this.days = days;
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

	@Column(name = "status",length=10, nullable = false, columnDefinition = "int")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column(name = "interval_days", length = 100, nullable = false)
	public String getInterval_days() {
		return interval_days;
	}

	public void setInterval_days(String interval_days) {
		this.interval_days = interval_days;
	}

	@Column(name = "ips", length = 100, nullable = true)
	public String getIps() {
		return ips;
	}

	public void setIps(String ips) {
		this.ips = ips;
	}

	@Column(name = "start_at", length = 100, nullable = true)
	public String getStart_at() {
		return start_at;
	}

	public void setStart_at(String start_at) {
		this.start_at = start_at;
	}

	@Column(name = "end_at", length = 100, nullable = true)
	public String getEnd_at() {
		return end_at;
	}

	public void setEnd_at(String end_at) {
		this.end_at = end_at;
	}

	
}
