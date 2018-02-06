package com.zxj.common.convert.impl;

import com.zxj.common.convert.AbstractConverter;
import com.zxj.common.utils.StringUtils;

/**
 * 字符转换器
 */
public class CharacterConverter extends AbstractConverter<Character> {

    @Override
    protected Character convertInternal(Object value) {
        if (char.class == value.getClass()) {
            return (char) value;
        } else {
            final String valueStr = convertToStr(value);
            if (StringUtils.isNotBlank(valueStr)) {
                try {
                    return valueStr.charAt(0);
                } catch (Exception e) {
                    //Ignore Exception
                }
            }
        }
        return null;
    }

}
