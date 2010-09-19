package org.springframework.social.oauth;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.URIException;
import org.springframework.http.HttpMethod;

/**
 * An implementation of {@link ClientRequest} that delegates to a wrapped
 * {@link HttpMethodBase} from Commons HTTP.
 * 
 * @author Craig Walls
 */
public class CommonsClientRequest implements ClientRequest {
	private final HttpMethodBase methodBase;

	public CommonsClientRequest(HttpMethodBase methodBase) {
		this.methodBase = methodBase;
	}

	public HttpMethodBase getMethodBase() {
		return methodBase;
	}

	public void addHeader(String headerName, String headerValue) {
		methodBase.addRequestHeader(headerName, headerValue);
	}

	public void setParameter(String parameterName, String parameterValue) {
		String queryString = methodBase.getQueryString();

		if (queryString == null) {
			queryString = parameterName + "=" + parameterValue;
		} else {
			queryString += "&" + parameterName + "=" + parameterValue;
		}
		methodBase.setQueryString(queryString);
	}

	public Map<String, String> getQueryParameters() {
		HashMap<String, String> paramMap = new HashMap<String, String>();

		String queryString = methodBase.getQueryString();
		if (queryString != null) {
			String[] paramPairs = queryString.split("[\\?|\\&]");

			for (String paramPair : paramPairs) {
				String[] split = paramPair.split("\\=");
				String value = split.length > 1 ? split[1] : null;
				paramMap.put(split[0], value);
			}
		}

		return Collections.unmodifiableMap(paramMap);
	}

	public HttpMethod getHttpMethod() {
		return HttpMethod.valueOf(methodBase.getName());
	}

	public URI getURI() {
		try {
			return URI.create(new String(methodBase.getURI().getRawURI()));
		} catch (URIException shouldNotHappen) {
			return null;
		}
	}
}