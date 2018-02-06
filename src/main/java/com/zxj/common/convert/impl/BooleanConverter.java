package com.zxj.common.convert.impl;

import com.zxj.common.convert.AbstractConverter;

/**
 * 波尔转换器
 * @author Looly
 *
 */
public class BooleanConverter extends AbstractConverter<Boolean>{

	@Override
	protected Boolean convertInternal(Object value) {
		if(boolean.class == value.getClass()){
			return (boolean) value;
		}
		String valueStr = convertToStr(value);
		return PrimitiveConverter.parseBoolean(valueStr);
	}
}
