package com.obatis.net.factory;

import com.obatis.constant.http.DefaultHttpConstant;
import com.obatis.net.HttpResponseResult;
import org.apache.http.client.CookieStore;

import java.util.Map;

public class HttpHandler {
	
	private HttpHandler(){}

	/**
	 * 设置http请求超时时间，只需设置一次即可，不用每次调用都设置，默认150秒
	 * @param timeout
	 * @throws Exception 
	 */
	public static void setConnectionTimeOut(int timeout) throws RuntimeException {
		if(timeout > 0) {
			HttpHandleFactory.SETTING_TIME_OUT = timeout;
		} else {
			throw new RuntimeException("error:timeout value invalid");
		}
	}
	
	/**
	 * HTTP POST 方式请求。
	 * @param url HTTP 请求url地址
	 * @return
	 */
	public static HttpResponseResult post(String url) {
		return post(url, null);
	}
	
	/**
	 * HTTP POST 方式请求。
	 * @param url HTTP 请求url地址
	 * @param params 请求参数
	 * @return
	 */
	public static HttpResponseResult post(String url, Map<String, Object> params) {
		return post(url, params, null, null);
	}

	/**
	 * HTTP POST 方式请求，支持传入 Cookie 信息。
	 * @param url HTTP 请求url地址
	 * @param params 请求参数
	 * @param cookie cookie信息
	 * @return
	 */
	public static HttpResponseResult post(String url, Map<String, Object> params, CookieStore cookie) {
		return post(url, params, cookie, null);
	}

	/**
	 * HTTP POST 方式请求，支持传入 Header 信息。
	 * @param url HTTP 请求url地址
	 * @param params 请求参数
	 * @param headers header信息
	 * @return
	 */
	public static HttpResponseResult post(String url, Map<String, Object> params, Map<String, Object> headers) {
		return post(url, params, null, headers);
	}
	
	/**
	 * HTTP POST 方式请求
	 * @param url HTTP 请求url地址
	 * @param params 请求参数
	 * @param cookie cookie信息
	 * @param headers header信息
	 * @return
	 */
	public static HttpResponseResult post(String url, Map<String, Object> params, CookieStore cookie, Map<String, Object> headers) {
		return HttpHandleFactory.load(url, params, DefaultHttpConstant.REQ_METHOD_POST, cookie, headers);
	}
	
	/**
	 * HTTP GET 方式请求。
	 * @param url HTTP 请求url地址
	 * @return
	 */
	public static HttpResponseResult get(String url) {
		return get(url, null);
	}
	
	/**
	 * HTTP GET 方式请求。
	 * @param url HTTP 请求url地址
	 * @param params 请求参数
	 * @return
	 */
	public static HttpResponseResult get(String url, Map<String, Object> params) {
		return get(url, params, null, null);
	}

	/**
	 * HTTP GET 方式请求，支持传入cookie。
	 * @param url HTTP 请求url地址
	 * @param params 请求参数
	 * @param cookie  cookie信息
	 * @return
	 */
	public static HttpResponseResult get(String url, Map<String, Object> params, CookieStore cookie) {
		return get(url, params, cookie, null);
	}

	/**
	 * HTTP GET 方式请求，支持传入Header。
	 * @param url HTTP 请求url地址
	 * @param params 请求参数
	 * @param headers header信息
	 * @return
	 */
	public static HttpResponseResult get(String url, Map<String, Object> params, Map<String, Object> headers) {
		return get(url, params, null, headers);
	}
	
	/**
	 * HTTP GET 方式请求。
	 * @param url HTTP 请求url地址
	 * @param params 请求参数
	 * @param cookie  cookie信息
	 * @param headers header信息
	 * @return
	 */
	public static HttpResponseResult get(String url, Map<String, Object> params, CookieStore cookie, Map<String, Object> headers) {
		return HttpHandleFactory.load(url, params, DefaultHttpConstant.REQ_METHOD_GET, cookie, headers);
	}
}
