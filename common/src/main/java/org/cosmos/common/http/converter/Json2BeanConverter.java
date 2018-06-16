package org.cosmos.common.http.converter;

import com.alibaba.fastjson.JSON;
import org.cosmos.common.http.converter.abs.AbstractJsonConverter;

/**
 * <p>
 * Convert the http body to any bean object.
 * </p>
 *
 * @author Robert Lee
 * @version 1.0
 * @since Jul 15, 2015
 */

public class Json2BeanConverter<T> extends AbstractJsonConverter<T> {

	private Class<T> clazz;

	public Json2BeanConverter(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	protected T doConstructObject(String str) {
		return JSON.parseObject(str, clazz);
	}

}
