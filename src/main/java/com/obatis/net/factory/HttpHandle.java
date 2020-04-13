package com.obatis.net.factory;

import com.obatis.constant.http.HttpConstant;
import com.obatis.convert.JsonCommonConvert;
import com.obatis.net.HttpResponseResult;
import org.apache.http.client.CookieStore;

import java.util.HashMap;
import java.util.Map;

public class HttpHandle {
	
	private HttpHandle(){}

	/**
	 * 设置http请求超时时间，只需设置一次即可，不用每次调用都设置，默认150秒
	 * @param timeout
	 * @throws Exception 
	 */
	public static void setTimeOut(int timeout) throws RuntimeException {
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
	 * HTTP POST json 方式请求。Content-Type 属性为 application/json
	 * @param url HTTP 请求url地址
	 * @return
	 */
	public static HttpResponseResult postJson(String url) {
		return postJson(url, null);
	}
	
	/**
	 * HTTP POST 方式请求。
	 * @param url    HTTP 请求url地址
	 * @param params 请求参数
	 * @return
	 */
	public static HttpResponseResult post(String url, Map<String, Object> params) {
		return post(url, params, null, null);
	}

	/**
	 * HTTP POST json 方式请求。Content-Type 属性为 application/json
	 * @param url       HTTP 请求url地址
	 * @param params    请求参数
	 * @return
	 */
	public static HttpResponseResult postJson(String url, Map<String, Object> params) {
		return postJson(url, params, null, null);
	}

	/**
	 * HTTP POST 方式请求，支持传入 Cookie 信息。
	 * @param url    HTTP 请求url地址
	 * @param params 请求参数
	 * @param cookie cookie信息
	 * @return
	 */
	public static HttpResponseResult post(String url, Map<String, Object> params, CookieStore cookie) {
		return post(url, params, cookie, null);
	}

	/**
	 * HTTP POST 方式请求，支持传入 Cookie 信息。Content-Type 属性为 application/json
	 * @param url    HTTP 请求url地址
	 * @param params 请求参数
	 * @param cookie cookie信息
	 * @return
	 */
	public static HttpResponseResult postJson(String url, Map<String, Object> params, CookieStore cookie) {
		return postJson(url, params, cookie, null);
	}

	/**
	 * HTTP POST 方式请求，支持传入 Header 信息。
	 * @param url     HTTP 请求url地址
	 * @param params  请求参数
	 * @param headers header信息
	 * @return
	 */
	public static HttpResponseResult post(String url, Map<String, Object> params, Map<String, Object> headers) {
		return post(url, params, null, headers);
	}

	/**
	 * HTTP POST 方式请求，支持传入 Header 信息。Content-Type 属性为 application/json
	 * @param url     HTTP 请求url地址
	 * @param params  请求参数
	 * @param headers header信息
	 * @return
	 */
	public static HttpResponseResult postJson(String url, Map<String, Object> params, Map<String, Object> headers) {
		return postJson(url, params, null, headers);
	}
	
	/**
	 * HTTP POST 方式请求
	 * @param url     HTTP 请求url地址
	 * @param params  请求参数
	 * @param cookie  cookie信息
	 * @param headers header信息
	 * @return
	 */
	public static HttpResponseResult post(String url, Map<String, Object> params, CookieStore cookie, Map<String, Object> headers) {
		return HttpHandleFactory.load(url, params, HttpConstant.METHOD_POST, cookie, headers, HttpRequestConstant.ContentType.NORMAL);
	}

	/**
	 * HTTP POST 方式请求, Content-Type 属性为 application/json
	 * @param url     HTTP 请求url地址
	 * @param params  请求参数
	 * @param cookie  cookie信息
	 * @param headers header信息
	 * @return
	 */
	public static HttpResponseResult postJson(String url, Map<String, Object> params, CookieStore cookie, Map<String, Object> headers) {
		return HttpHandleFactory.load(url, params, HttpConstant.METHOD_POST, cookie, headers, HttpRequestConstant.ContentType.JSON);
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
	 * HTTP GET 方式请求。Content-Type 属性为 application/json
	 * @param url HTTP 请求url地址
	 * @return
	 */
	public static HttpResponseResult getJson(String url) {
		return getJson(url, null);
	}
	
	/**
	 * HTTP GET 方式请求。
	 * @param url     HTTP 请求url地址
	 * @param params  请求参数
	 * @return
	 */
	public static HttpResponseResult get(String url, Map<String, Object> params) {
		return get(url, params, null, null);
	}

	/**
	 * HTTP GET 方式请求。Content-Type 属性为 application/json
	 * @param url     HTTP 请求url地址
	 * @param params  请求参数
	 * @return
	 */
	public static HttpResponseResult getJson(String url, Map<String, Object> params) {
		return getJson(url, params, null, null);
	}

	/**
	 * HTTP GET 方式请求，支持传入cookie。
	 * @param url     HTTP 请求url地址
	 * @param params  请求参数
	 * @param cookie  cookie信息
	 * @return
	 */
	public static HttpResponseResult get(String url, Map<String, Object> params, CookieStore cookie) {
		return get(url, params, cookie, null);
	}

	/**
	 * HTTP GET 方式请求，支持传入cookie。Content-Type 属性为 application/json
	 * @param url     HTTP 请求url地址
	 * @param params  请求参数
	 * @param cookie  cookie信息
	 * @return
	 */
	public static HttpResponseResult getJson(String url, Map<String, Object> params, CookieStore cookie) {
		return getJson(url, params, cookie, null);
	}

	/**
	 * HTTP GET 方式请求，支持传入Header。
	 * @param url     HTTP 请求url地址
	 * @param params  请求参数
	 * @param headers header信息
	 * @return
	 */
	public static HttpResponseResult get(String url, Map<String, Object> params, Map<String, Object> headers) {
		return get(url, params, null, headers);
	}

	/**
	 * HTTP GET 方式请求，支持传入Header。Content-Type 属性为 application/json
	 * @param url     HTTP 请求url地址
	 * @param params  请求参数
	 * @param headers header信息
	 * @return
	 */
	public static HttpResponseResult getJson(String url, Map<String, Object> params, Map<String, Object> headers) {
		return getJson(url, params, null, headers);
	}
	
	/**
	 * HTTP GET 方式请求。
	 * @param url     HTTP 请求url地址
	 * @param params  请求参数
	 * @param cookie  cookie信息
	 * @param headers header信息
	 * @return
	 */
	public static HttpResponseResult get(String url, Map<String, Object> params, CookieStore cookie, Map<String, Object> headers) {
		return HttpHandleFactory.load(url, params, HttpConstant.METHOD_GET, cookie, headers, HttpRequestConstant.ContentType.NORMAL);
	}

	/**
	 * HTTP GET 方式请求。Content-Type 属性为 application/json
	 * @param url     HTTP 请求url地址
	 * @param params  请求参数
	 * @param cookie  cookie信息
	 * @param headers header信息
	 * @return
	 */
	public static HttpResponseResult getJson(String url, Map<String, Object> params, CookieStore cookie, Map<String, Object> headers) {
		return HttpHandleFactory.load(url, params, HttpConstant.METHOD_GET, cookie, headers, HttpRequestConstant.ContentType.JSON);
	}

//	public static void main(String[] args) {
//		String data = "{\"name\" : \"xiaoming\", \"info\":{\"age\":11}}";
//		String test = "adb";
//		System.out.println(test);
//		System.out.println(data);
//		System.out.println(JsonCommonConvert.objConvertJson(data));
//	}

	public static void main(String[] args) {

		Map<String, Object> params = new HashMap<>();
		params.put("sn", "3175051481");
		params.put("key", "20181029CWXT0MD2J12");
		params.put("mapType", "baidu");

		HttpResponseResult result = HttpHandle.get("http://www.008gps.com/api/Tracking.aspx", params);
		System.out.println(JsonCommonConvert.objConvertJson(result));
	}
}
