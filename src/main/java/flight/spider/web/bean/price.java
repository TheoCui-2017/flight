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
@Table(name = "price", catalog = "OTA",
		indexes={@Index(columnList="unique_flag",name="KEY_unique_flag"),  
			@Index(columnList="passenger_type",name="KEY_passenger_type")
		}
)
public class price implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4411443343634124838L;
	private int id;
	private float fare;
	private float tax;
	private String passenger_type;
	private Date create_at;
	private Date update_at;
	private String unique_flag;
	private String carryOn;
	private String firstBaggage;
	private String secondBaggage;
	private String currency_unit;
	private String baggage_url;

	public price() {
		super();
	}

	public price(int id, float fare, float tax,
			String passenger_type, Date create_at,
			Date update_at, String unique_flag, String carryOn, String firstBaggage, String secondBaggage, String currency_unit, String  baggage_url) {
		super();
		this.id = id;
		this.fare = fare;
		this.tax = tax;
		this.passenger_type = passenger_type;
		this.create_at = create_at;
		this.update_at = update_at;
		this.unique_flag = unique_flag;
		this.carryOn = carryOn;
		this.firstBaggage = firstBaggage;
		this.secondBaggage = secondBaggage;
		this.currency_unit = currency_unit;
		this.baggage_url =  baggage_url;
	}

	public price(float fare, float tax,
			String passenger_type, Date create_at,
			String unique_flag, String carryOn, String firstBaggage, String secondBaggage, String currency_unit, String baggage_url) {
		super();
		this.fare = fare;
		this.tax = tax;
		this.passenger_type = passenger_type;
		this.create_at = create_at;
		this.unique_flag = unique_flag;
		this.carryOn = carryOn;
		this.firstBaggage = firstBaggage;
		this.secondBaggage = secondBaggage;
		this.currency_unit = currency_unit;
		this.baggage_url =baggage_url;
	}
	
	public void reset(float fare, float tax, String passenger_type, 
			String unique_flag, String carryOn, String firstBaggage, String secondBaggage, String currency_unit, String baggage_url, Date update_at) {
		this.fare = fare;
		this.tax = tax;
		this.passenger_type = passenger_type;
		this.unique_flag = unique_flag;
		this.carryOn = carryOn;
		this.firstBaggage = firstBaggage;
		this.secondBaggage = secondBaggage;
		this.update_at = update_at;
		this.currency_unit = currency_unit;
		this.baggage_url =baggage_url;
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

	@Column(name = "fare", nullable = false, columnDefinition = "float")
	public float getFare() {
		return fare;
	}

	public void setFare(float fare) {
		this.fare = fare;
	}

	@Column(name = "tax", nullable = false, columnDefinition = "float")
	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	@Column(name = "passenger_type", length = 100, nullable = false)
	public String getPassenger_type() {
		return passenger_type;
	}

	public void setPassenger_type(String passenger_type) {
		this.passenger_type = passenger_type;
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
	
	@Column(name = "unique_flag", length = 255, nullable = false)
	public String getUnique_flag() {
		return unique_flag;
	}

	public void setUnique_flag(String unique_flag) {
		this.unique_flag = unique_flag;
	}
	
	@Column(name = "carryOn", length = 100, nullable = false)
	public String getCarryOn() {
		return carryOn;
	}

	public void setCarryOn(String carryOn) {
		this.carryOn = carryOn;
	}

	@Column(name = "firstBaggage", length = 100, nullable = false)
	public String getFirstBaggage() {
		return firstBaggage;
	}

	public void setFirstBaggage(String firstBaggage) {
		this.firstBaggage = firstBaggage;
	}

	@Column(name = "secondBaggage", length = 100, nullable = false)
	public String getSecondBaggage() {
		return secondBaggage;
	}

	public void setSecondBaggage(String secondBaggage) {
		this.secondBaggage = secondBaggage;
	}
	
	@Column(name = "currency_unit", length = 100, nullable = false)
	public String getCurrency_unit() {
		return currency_unit;
	}

	public void setCurrency_unit(String currency_unit) {
		this.currency_unit = currency_unit;
	}
	
	@Column(name = "baggage_url", length = 500)
	public String getBaggage_url() {
		return baggage_url;
	}

	public void setBaggage_url(String baggage_url) {
		this.baggage_url = baggage_url;
	}
}

