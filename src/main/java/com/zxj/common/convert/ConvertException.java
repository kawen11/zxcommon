package com.zxj.common.convert;

/**
 * 转换异常
 */
public class ConvertException extends RuntimeException {
    private static final long serialVersionUID = 4730597402855274362L;

    public ConvertException(Throwable e) {
        super(e.getMessage(), e);
    }

    public ConvertException(String message) {
        super(message);
    }
    
    public static void main(String[] args) {
		ConverterRegistry c = ConverterRegistry.getInstance();
	}
}
