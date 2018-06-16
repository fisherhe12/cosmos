package org.cosmos.common.http.converter;

import org.cosmos.common.http.converter.abs.AbstractStrConverter;

import java.math.BigDecimal;


/**
 * <p>
 * Convert the http body to BigDecimal object.
 * </p>
 *
 * @author Robert Lee
 * @version 1.0
 * @since Jul 15, 2015
 */
public class BigDecimalConverter extends AbstractStrConverter<BigDecimal> {

	@Override
	protected BigDecimal doConstructObject(String str) {
		return new BigDecimal(str);
	}

}
