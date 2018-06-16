package org.cosmos.common.http.converter;


import org.cosmos.common.http.converter.abs.AbstractStrConverter;

/**
 * <p>
 * Convert the http body to float object.
 * </p>
 *
 * @author Robert Lee
 * @version 1.0
 * @since Jul 15, 2015
 */

public class FloatConverter extends AbstractStrConverter<Float> {

	@Override
	protected Float doConstructObject(String str) {
		return new Float(str);
	}

}
