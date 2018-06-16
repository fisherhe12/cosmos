package org.cosmos.common.http.converter.abs;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;

/**
 * <p>
 * Convert the HTTP body to json related data type.
 * </p>
 *
 * @author Robert Lee
 * @version 1.0
 * @since Jul 15, 2015
 */

public abstract class AbstractJsonConverter<T> extends AbstractStrConverter<T> {

	protected void checkFormat(HttpResponse response)
			throws ClientProtocolException {
		HttpEntity entity = response.getEntity();
		ContentType contentType = ContentType.get(entity);
		if (contentType != null
				&& !contentType.equals(ContentType.APPLICATION_JSON)) {
			throw new ClientProtocolException("Unexpected content type:"
					+ contentType);
		}
	}

}
