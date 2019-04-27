package com.obatis.net;

import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;

public class HttpResponseResult {

	private int status = HttpStatus.SC_NOT_FOUND;
	private String result;
	private CookieStore cookie;

	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public CookieStore getCookie() {
		return cookie;
	}
	public void setCookie(CookieStore cookie) {
		this.cookie = cookie;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
