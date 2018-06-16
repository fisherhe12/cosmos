package org.cosmos.common.http.converter;


import org.cosmos.common.http.converter.abs.AbstractStrConverter;

/**
 * <p>
 * Convert the http body to int variable.
 * </p>
 *
 * @author Robert Lee
 * @version 1.0
 * @since Jul 15, 2015
 */

public class IntConverter extends AbstractStrConverter<Integer> {

	@Override
	protected Integer doConstructObject(String str) {
		return new Integer(str);
	}

}
