package com.obatis.net.factory;

import com.obatis.constant.NormalCommonConstant;
import com.obatis.constant.http.DefaultHttpConstant;
import com.obatis.net.HttpResponseResult;
import com.obatis.validate.ValidateTool;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
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

	private static PoolingHttpClientConnectionManager manager = null;

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
		// 设置连接池的最大连接数
		manager.setMaxTotal(150);
		// 一个路由的最大连接数，尽量保证性能
		manager.setDefaultMaxPerRoute(manager.getMaxTotal());
	}

	private static CloseableHttpClient buildHttpClient(CookieStore cookie) {

		HttpClientBuilder builder = HttpClients.custom();
		builder.setConnectionManagerShared(true);
		builder.setConnectionManager(manager);
		if(cookie != null) {
			builder.setDefaultCookieStore(cookie);
		}
		return builder.build();
	}


	protected static HttpResponseResult load(String url, Map<String, Object> params, String method, CookieStore cookie, Map<String, Object> headers) {

		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		if (url.endsWith("?")) {
			url = url.substring(0, url.length() - 1);
		}

		HttpUriRequest request = setRequestParam(url, params, method, headers);
		return load(request, cookie);
	}

	// 以下代码为连接
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
			HttpClientContext context = new HttpClientContext();
			context.setCookieStore(cookie);
			
			// 执行请求
			response = client.execute(request, context);
			
			entity = response.getEntity();
			content = EntityUtils.toString(entity, NormalCommonConstant.CHARSET_UTF8);
			status = response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (entity != null) {
				// 关闭数据流
				EntityUtils.consumeQuietly(entity);
			}
			if (response != null) {
				try {
					response.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (request != null) {
				if (HttpStatus.SC_OK != status) {
					// 中止请求连接
					try {
						request.abort();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			if(client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		HttpResponseResult result = new HttpResponseResult();
		result.setStatus(status);
		result.setResult(content);
		result.setCookie(cookie);
		return result;
	}

	private static RequestConfig setRequestTimeOutConfig() {
		/**
		 * 设置 HTTP 请求超时
		 */
		Builder builder = RequestConfig.custom();
		builder.setConnectTimeout(TIME_OUT);
		builder.setConnectionRequestTimeout(TIME_OUT);
		builder.setSocketTimeout(TIME_OUT);
		return builder.build();
	}

	private static HttpUriRequest setRequestParam(String url, Map<String, Object> params, String method, Map<String, Object> headers) {

		List<NameValuePair> param = new ArrayList<>();

		if (params != null) {
			Set<Map.Entry<String, Object>> entrySet = params.entrySet();
			for (Map.Entry<String, Object> e : entrySet) {
				NameValuePair pair = new BasicNameValuePair(e.getKey(), (e.getValue() != null) ? e.getValue().toString() : "");
				param.add(pair);
			}
		}

		RequestBuilder builder = null;
		if (DefaultHttpConstant.REQ_METHOD_POST.equals(method)) {
			// post请求
			builder = RequestBuilder.post(url);
		} else {
			// get请求
			builder = RequestBuilder.get(url);
		}
		
		// 设置header
		if(headers != null) {
			for (Map.Entry<String, Object> entry : headers.entrySet()) {
				if(ValidateTool.isEmpty(entry.getValue())) {
					continue;
				}
				builder.addHeader(entry.getKey(), entry.getValue().toString());
			}
		}
		
		// 设置超时时间
		builder.setConfig(setRequestTimeOutConfig());
		// 设置编码
		builder.setCharset(Charset.forName(NormalCommonConstant.CHARSET_UTF8));
		
		// 设置参数
		if (param.size() > 0) {
			builder.addParameters(param.toArray(new BasicNameValuePair[param.size()]));
		}
		return builder.build();
	}
}