package org.cosmos.common.http.converter.abs;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * <p>
 * Convert the HTTP body to string related data type.
 * </p>
 *
 * @author Robert Lee
 * @version 1.0
 * @since Jul 15, 2015
 */

public abstract class AbstractStrConverter<T> extends AbstractConverter<T> {

	protected void checkFormat(HttpResponse response)
			throws ClientProtocolException {
		HttpEntity entity = response.getEntity();
		ContentType contentType = ContentType.getOrDefault(entity);
		if (!contentType.equals(ContentType.DEFAULT_TEXT)) {
			throw new ClientProtocolException("Unexpected content type:"
					+ contentType);
		}
	}

	protected T doConvert(HttpResponse response) throws
      IOException {
		String result = resp2String(response);
		return doConstructObject(result);
	}

	protected String resp2String(HttpResponse response)
			throws IOException {
		StatusLine statusLine = response.getStatusLine();
		HttpEntity entity = response.getEntity();

		if (statusLine.getStatusCode() >= 300) {
			throw new HttpResponseException(statusLine.getStatusCode(),
					statusLine.getReasonPhrase());
		}
		if (entity == null) {
			throw new ClientProtocolException("Response contains no content");
		}

		Charset charset = getCharset(response);

		String str = new String(EntityUtils.toByteArray(entity), charset);
		return str;
	}

	protected abstract T doConstructObject(String str);

}
