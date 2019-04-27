package com.obatis.net;

import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;

public class ResponseResult {

	private int code = HttpStatus.SC_NOT_FOUND;
	private String result;
	private CookieStore cookieStore;
	private Exception exception;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public CookieStore getCookieStore() {
		return cookieStore;
	}
	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
}
