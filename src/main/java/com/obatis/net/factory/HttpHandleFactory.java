package com.obatis.net.factory;

import com.obatis.constant.CharsetConstant;
import com.obatis.constant.http.HttpConstant;
import com.obatis.convert.JsonCommonConvert;
import com.obatis.net.HttpResponseResult;
import com.obatis.tools.ValidateTool;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpHandleFactory {

	/**
	 * 默认的HTPTP 超时时间
	 */
	private static final int DEFAULT_TIME_OUT = 150000;
	protected static int SETTING_TIME_OUT = DEFAULT_TIME_OUT;
	protected static int TIME_OUT = SETTING_TIME_OUT;
	
	private HttpHandleFactory() {
	}

	private static PoolingHttpClientConnectionManager manager;

	static {
		SSLContext context = null;
		try {
			context = SSLContext.getDefault();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		final HostnameVerifier verifier = new DefaultHostnameVerifier();
		final Registry<ConnectionSocketFactory> register = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(context, verifier)).build();
		manager = new PoolingHttpClientConnectionManager(register);
		/**
		 * 设置连接池的最大连接数，设置最大连接数为1000个
		 */
		manager.setMaxTotal(1000);
		/**
		 * 为了保证 http发起的性能问题，设置http路由的最大数量
		 */
		manager.setDefaultMaxPerRoute(manager.getMaxTotal());
	}

	private static CloseableHttpClient buildHttpClient(CookieStore cookie) {

		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManagerShared(true);
		builder.setConnectionManager(manager);
		if(cookie != null) {
			builder.setDefaultCookieStore(cookie);
		}
		return builder.build();
	}

	/**
	 * 对外提供接口，发起 http 请求
	 * @param url
	 * @param params
	 * @param method
	 * @param cookie
	 * @param headers
	 * @return
	 */
	protected static HttpResponseResult load(String url, Map<String, Object> params, String method, CookieStore cookie, Map<String, Object> headers, HttpRequestConstant.ContentType contentType) {

		if(contentType.equals(HttpRequestConstant.ContentType.JSON)) {
			if (headers == null) {
				headers = new HashMap<>();
			}
			headers.put(HttpRequestConstant.CONTENT_TYPE_KEY, HttpRequestConstant.CONTENT_TYPE_JSON);
		}

		HttpUriRequest request = setRequestParam(url, params, method, headers);
		return load(request, cookie);
	}

	/**
	 * 发起 http 请求连接
	 * @param request
	 * @param cookie
	 * @return
	 */
	private static HttpResponseResult load(HttpUriRequest request, CookieStore cookie) {

		CloseableHttpClient client = null;
		String content = null;
		int status = 404;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			
			if(cookie == null) {
				cookie = new BasicCookieStore();
			}
			client = buildHttpClient(cookie);

			response = client.execute(request);

			entity = response.getEntity();
			content = EntityUtils.toString(entity, CharsetConstant.CHARSET_UTF8);
			status = response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/**
			 * 释放 http 请求连接
			 */
			if (entity != null) {
				EntityUtils.consumeQuietly(entity);
			}
			if (response != null) {
				try {
					response.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			/**
			 * 关闭客户端发起的 http 连接
			 */
			if(client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (request != null) {
				/**
				 * 中止释放非正常请求的 http 连接
				 */
				if (HttpStatus.SC_OK != status) {
					try {
						request.abort();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		HttpResponseResult result = new HttpResponseResult();
		result.setStatus(status);
		result.setResult(content);
		result.setCookie(cookie);
		return result;
	}

	/**
	 * http 超时设置
	 * @return
	 */
	private static RequestConfig setRequestTimeOutConfig() {
		// 设置 HTTP 请求超时
		Builder builder = RequestConfig.custom();
		builder.setConnectTimeout(TIME_OUT);
		builder.setConnectionRequestTimeout(TIME_OUT);
		builder.setSocketTimeout(TIME_OUT);
		return builder.build();
	}

	/**
	 * 处理 http 请求的参数
	 * @param url
	 * @param params
	 * @param method
	 * @param headers
	 * @return
	 */
	private static HttpUriRequest setRequestParam(String url, Map<String, Object> params, String method, Map<String, Object> headers) {

		RequestBuilder builder;
		/**
		 * 判断发起的请求是否为 post，进行 RequestBuilder 的初始化
		 */
		if (HttpConstant.METHOD_POST.equals(method)) {
			builder = RequestBuilder.post(url);

			if(params != null && !params.isEmpty()) {
				builder.setEntity(new StringEntity(JsonCommonConvert.objConvertJson(params), CharsetConstant.CHARSET_UTF8));
			}
		} else {

			// get 形式请求参数封装

			builder = RequestBuilder.get(url);

			if (params != null && !params.isEmpty()) {
				Set<Map.Entry<String, Object>> entrySet = params.entrySet();
				for (Map.Entry<String, Object> e : entrySet) {
					builder.addParameter(e.getKey(), (e.getValue() != null) ? e.getValue().toString() : "");
				}
			}
		}

		if(headers != null) {
			for (Map.Entry<String, Object> entry : headers.entrySet()) {
				if(ValidateTool.isEmpty(entry.getValue())) {
					continue;
				}
				builder.addHeader(entry.getKey(), entry.getValue().toString());
			}
		}

		/**
		 * 设置 http 请求的配置信息
		 */
		builder.setConfig(setRequestTimeOutConfig());
		builder.setCharset(Charset.forName(CharsetConstant.CHARSET_UTF8));

		return builder.build();
	}

}