package flight.spider.web.model;

import org.json.JSONObject;

public class ReturnValue <T> {

	private boolean status;
	private String error;
	private T data;

	public ReturnValue() {
		this.status = false;
		this.error = "";
		this.data = null;
	}

	public JSONObject toJson() {
		return new JSONObject(this);
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
