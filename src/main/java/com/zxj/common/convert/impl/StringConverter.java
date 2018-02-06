package com.zxj.common.convert.impl;

import com.zxj.common.convert.AbstractConverter;

/**
 * 字符串转换器
 * @author Looly
 *
 */
public class StringConverter extends AbstractConverter<String>{

	@Override
	protected String convertInternal(Object value) {
		return convertToStr(value);
	}

}
