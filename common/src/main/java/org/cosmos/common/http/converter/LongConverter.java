package org.cosmos.common.http.converter;


import org.cosmos.common.http.converter.abs.AbstractStrConverter;

/**
 * <p>
 * Convert the http body to long variable.
 * </p>
 *
 * @author Robert Lee
 * @version 1.0
 * @since Jul 15, 2015
 */

public class LongConverter extends AbstractStrConverter<Long> {

	@Override
	protected Long doConstructObject(String str) {
		return new Long(str);
	}

}
