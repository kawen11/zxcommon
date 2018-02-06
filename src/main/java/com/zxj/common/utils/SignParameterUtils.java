package com.zxj.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.zxj.common.utils.StringUtils;

/**
 * 签名工具里
 * 
 * @author zxj
 * @date 2017年3月24日 下午2:38:32
 */
public class SignParameterUtils {
	
	private SignParameterUtils() {
		
	}

	/**
	 * 生成待加密字符串,返回a=b&c=d这样的字符串
	 *
	 * @param map
	 * @return
	 */
	public static String createSignText(Map<String, Object> map) {
		if (null == map || map.isEmpty()) {
			throw new RuntimeException("签名参数为空");
		}
		// map转出list
		List<String> params = mapToList(map);
		// 按照英文字母排序
		Collections.sort(params);
		// 参数之间&连接
		return StringUtils.join(params, "&");
	}

	/**
	 * map转数组
	 * 
	 * @param map
	 * @return
	 */
	private static List<String> mapToList(Map<String, Object> map) {
		List<String> params = new ArrayList<>(map.size());
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			params.add(entry.getKey() + "=" + entry.getValue());
		}
		return params;
	}

}
