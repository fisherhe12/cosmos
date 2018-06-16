package org.cosmos.common.http.converter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.cosmos.common.http.converter.abs.AbstractJsonConverter;

/**
 * <p>
 * Convert the http body to JSON object.
 * </p>
 *
 * @author Robert Lee
 * @version 1.0
 * @since Jul 15, 2015
 */

public class Json2ObjectConverter extends AbstractJsonConverter<JSONObject> {

	@Override
	protected JSONObject doConstructObject(String str) {
		return JSON.parseObject(str);
	}

}
