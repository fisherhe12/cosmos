package org.cosmos.common.http.converter.abs;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * <p>
 * Convert the HTTP body to Steam related data type.
 * </p>
 *
 * @author Robert Lee
 * @version 1.0
 * @since Jul 15, 2015
 */

public abstract class AbstractStreamConverter<T> extends AbstractConverter<T> {

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
		InputStream result = resp2Stream(response);
		Charset charset = getCharset(response);
		return doConstructObject(result, charset);
	}

	protected abstract T doConstructObject(InputStream is, Charset charset)
			throws IOException;

	protected InputStream resp2Stream(HttpResponse response)
			throws UnsupportedOperationException, IOException {
		StatusLine statusLine = response.getStatusLine();
		HttpEntity entity = response.getEntity();

		if (statusLine.getStatusCode() >= 300) {
			throw new HttpResponseException(statusLine.getStatusCode(),
					statusLine.getReasonPhrase());
		}
		if (entity == null) {
			throw new ClientProtocolException("Response contains no content");
		}

		InputStream is = entity.getContent();
		return is;
	}

}
