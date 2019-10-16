package com.zxj.common.utils;

import java.io.ByteArrayOutputStream;  
import java.lang.Character.UnicodeBlock;
import java.util.Base64;
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  
  
/** 
 * 该类处理字符串的转码，可以处理字符串到二进制字符、16进制字符、unicode字符、base64字符之间的转换 
 * @author zxjiang
 * 
 */  
public class TranscodeUtil {  
  
    /** 
     * 将字符串转换成unicode码 
     * @param str 要转码的字符串 
     * @return 返回转码后的字符串 
     */  
    public static String strToUnicodeStr(String str) {  
        StringBuffer buffer = new StringBuffer();  
        for (int i = 0; i < str.length(); i++) {  
            char ch = str.charAt(i);  
            UnicodeBlock ub = UnicodeBlock.of(ch);  
            if (ub == UnicodeBlock.BASIC_LATIN) {//英文及数字等  
                buffer.append(ch);  
            } else if ((int)ch > 255) {  
                buffer.append("\\u" + Integer.toHexString((int)ch));  
            } else {  
                buffer.append("\\" + Integer.toHexString((int)ch));  
            }  
        }  
        return buffer.toString();  
    }  
  
    /** 
     * 将unicode码反转成字符串 
     * @param unicodeStr unicode码 
     * @return 返回转码后的字符串 
     */  
    public static String unicodeStrToStr(String unicodeStr) {  
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");  
        Matcher matcher = pattern.matcher(unicodeStr);  
        char ch;  
        while (matcher.find()) {  
            ch = (char) Integer.parseInt(matcher.group(2), 16);  
            unicodeStr = unicodeStr.replace(matcher.group(1), ch + "");  
        }  
        return unicodeStr;  
    }  
  
    /** 
     * 将字符串通过base64转码 
     * @param str 要转码的字符串 
     * @return 返回转码后的字符串 
     */  
    public static String strToBase64Str(String str) {  
        return new String(encode(str.getBytes()));  
    }  
  
    /** 
     * 将base64码反转成字符串 
     * @param base64Str base64码 
     * @return 返回转码后的字符串 
     */  
    public static String base64StrToStr(String base64Str) {  
        char[] dataArr = new char[base64Str.length()];  
        base64Str.getChars(0, base64Str.length(), dataArr, 0);  
        return new String(decode(dataArr));  
    }  
  
    /** 
     * 将字节数组通过base64转码 
     * @param byteArray 字节数组 
     * @return 返回转码后的字符串 
     */  
    public static String byteArrayToBase64Str(byte byteArray[]) {  
        return new String(encode(byteArray));  
    }  
  
    /** 
     * 将base64码转换成字节数组 
     * @param base64Str base64码 
     * @return 返回转换后的字节数组 
     */  
    public static byte[] base64StrToByteArray(String base64Str) {  
        char[] dataArr = new char[base64Str.length()];  
        base64Str.getChars(0, base64Str.length(), dataArr, 0);  
        return decode(dataArr);  
    }  
  
    /** 
     * 将一个字节数组转换成base64的字符数组 
     * @param data 字节数组 
     * @return base64字符数组 
     */  
    private static char[] encode(byte[] data) {  
        char[] out = new char[((data.length + 2) / 3) * 4];  
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {  
            boolean quad = false;  
            boolean trip = false;  
            int val = (0xFF & (int) data[i]);  
            val <<= 8;  
            if ((i + 1) < data.length) {  
                val |= (0xFF & (int) data[i + 1]);  
                trip = true;  
            }  
            val <<= 8;  
            if ((i + 2) < data.length) {  
                val |= (0xFF & (int) data[i + 2]);  
                quad = true;  
            }  
            out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];  
            val >>= 6;  
            out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];  
            val >>= 6;  
            out[index + 1] = alphabet[val & 0x3F];  
            val >>= 6;  
            out[index + 0] = alphabet[val & 0x3F];  
        }  
        return out;  
    }  
  
    /** 
     * 将一个base64字符数组解码成一个字节数组 
     * @param data base64字符数组 
     * @return 返回解码以后的字节数组 
     */  
    private static byte[] decode(char[] data) {  
        int len = ((data.length + 3) / 4) * 3;  
        if (data.length > 0 && data[data.length - 1] == '=') --len;  
        if (data.length > 1 && data[data.length - 2] == '=') --len;  
        byte[] out = new byte[len];  
        int shift = 0;  
        int accum = 0;  
        int index = 0;  
        for (int ix = 0; ix < data.length; ix++) {  
            int value = codes[data[ix] & 0xFF];  
            if (value >= 0) {  
                accum <<= 6;  
                shift += 6;  
                accum |= value;  
                if (shift >= 8) {  
                    shift -= 8;  
                    out[index++] = (byte) ((accum >> shift) & 0xff);  
                }  
            }  
        }  
        if (index != out.length)  
            throw new Error("miscalculated data length!");  
        return out;  
    }  
  
    /** 
     * base64字符集 0..63 
     */  
    static private char[] alphabet =  
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="  
        .toCharArray();  
  
    /** 
     * 初始化base64字符集表 
     */  
    static private byte[] codes = new byte[256];  
  
    static {  
        for (int i = 0; i < 256; i++) codes[i] = -1;  
        for (int i = 'A'; i <= 'Z'; i++) codes[i] = (byte) (i - 'A');  
        for (int i = 'a'; i <= 'z'; i++) codes[i] = (byte) (26 + i - 'a');  
        for (int i = '0'; i <= '9'; i++) codes[i] = (byte) (52 + i - '0');  
        codes['+'] = 62;  
        codes['/'] = 63;  
    }  
  
    /** 
     * 16进制数字字符集 
     */  
    private static String hexString = "0123456789ABCDEF";  
  
    /** 
     * 将字符串编码成16进制数字,适用于所有字符（包括中文） 
     * @param str 字符串 
     * @return 返回16进制字符串 
     */  
    public static String strToHexStr(String str) {  
        // 根据默认编码获取字节数组  
        byte[] bytes = str.getBytes();  
        StringBuilder sb = new StringBuilder(bytes.length * 2);  
        // 将字节数组中每个字节拆解成2位16进制整数  
        for (int i = 0; i < bytes.length; i++) {  
            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));  
            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));  
        }  
        return sb.toString();  
    }  
  
    /** 
     * 将16进制数字解码成字符串,适用于所有字符（包括中文） 
     * @param hexStr 16进制字符串 
     * @return 返回字符串 
     */  
    public static String hexStrToStr(String hexStr) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream(  
            hexStr.length() / 2);  
        // 将每2位16进制整数组装成一个字节  
        for (int i = 0; i < hexStr.length(); i += 2)  
            baos.write((hexString.indexOf(hexStr.charAt(i)) << 4 | hexString  
                        .indexOf(hexStr.charAt(i + 1))));  
        return new String(baos.toByteArray());  
    }  
  
    /** 
     * 将字节数组转换成16进制字符串 
     * @param byteArray 要转码的字节数组 
     * @return 返回转码后的16进制字符串 
     */  
    public static String byteArrayToHexStr(byte byteArray[]) {  
        StringBuffer buffer = new StringBuffer(byteArray.length * 2);  
        int i;  
        for (i = 0; i < byteArray.length; i++) {  
            if (((int) byteArray[i] & 0xff) < 0x10)//小于十前面补零  
                buffer.append("0");  
            buffer.append(Long.toString((int) byteArray[i] & 0xff, 16));  
        }  
        return buffer.toString();  
    }  
  
    /** 
     * 将16进制字符串转换成字节数组 
     * @param hexStr 要转换的16进制字符串 
     * @return 返回转码后的字节数组 
     */  
    public static byte[] hexStrToByteArray(String hexStr) {  
        if (hexStr.length() < 1)  
            return null;  
        byte[] encrypted = new byte[hexStr.length() / 2];  
        for (int i = 0; i < hexStr.length() / 2; i++) {  
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);//取高位字节  
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);//取低位字节  
            encrypted[i] = (byte) (high * 16 + low);  
        }  
        return encrypted;  
    }  
  
    /** 
     * 将字符串转换成二进制字符串，以空格相隔 
     * @param str 字符串 
     * @return 返回二进制字符串 
     */  
    public static String strToBinStr(String str) {  
        char[] chars=str.toCharArray();  
        StringBuffer result = new StringBuffer();  
        for(int i=0; i<chars.length; i++) {  
            result.append(Integer.toBinaryString(chars[i]));  
            result.append(" ");  
        }  
        return result.toString();  
    }  
  
    /** 
     * 将二进制字符串转换成Unicode字符串 
     * @param binStr 二进制字符串 
     * @return 返回字符串 
     */  
    public static String binStrToStr(String binStr) {  
        String[] tempStr=strToStrArray(binStr);  
        char[] tempChar=new char[tempStr.length];  
        for(int i=0; i<tempStr.length; i++) {  
            tempChar[i]=binstrToChar(tempStr[i]);  
        }  
        return String.valueOf(tempChar);  
    }  
  
    /** 
     * 将二进制字符串转换为char 
     * @param binStr 二进制字符串 
     * @return 返回字符 
     */  
    private static char binstrToChar(String binStr) {  
        int[] temp=binstrToIntArray(binStr);  
        int sum=0;  
        for(int i=0; i<temp.length; i++) {  
            sum += temp[temp.length-1-i]<<i;  
        }  
        return (char)sum;  
    }  
  
    /** 
     * 将初始二进制字符串转换成字符串数组，以空格相隔 
     * @param str 二进制字符串 
     * @return 返回字符串数组 
     */  
    private static String[] strToStrArray(String str) {  
        return str.split(" ");  
    }  
  
    /** 
     * 将二进制字符串转换成int数组 
     * @param binStr 二进制字符串 
     * @return 返回int数组 
     */  
    private static int[] binstrToIntArray(String binStr) {  
        char[] temp=binStr.toCharArray();  
        int[] result=new int[temp.length];  
        for(int i=0; i<temp.length; i++) {  
            result[i]=temp[i]-48;  
        }  
        return result;  
    }  
    
    public static void main(String[] args) {
    	String str = "rO0ABXNyACJjb20uY3JlZGl0ZWFzZS5zaWEucG9qby5TSUFNZXNzYWdlAAAAAAAAAAECAA9JABBtZXNzYWdlSGlzdG9yeUlkTAAKYWNjZXB0VGltZXQAFExqYXZhL3NxbC9UaW1lc3RhbXA7TAAMYnVzaW5lc3NDb2RldAASTGphdmEvbGFuZy9TdHJpbmc7TAALY3VycmVudERhdGV0ABBMamF2YS9sYW5nL0xvbmc7TAAIZXJyb3JOdW1xAH4AA0wACWdyb3VwQ29kZXEAfgACTAAJbWVzc2FnZUlkcQB+AAJMAA9tZXNzYWdlSW5mb0Nsb2JxAH4AAkwAC21lc3NhZ2VUeXBlcQB+AAJMAAlwb2pvUGFyYW10ABJMamF2YS9sYW5nL09iamVjdDtMAAlxdWV1ZU5hbWVxAH4AAkwAEXJlY2VpdmVRdWVuZW5OYW1lcQB+AAJMAAhzZW5kVGltZXEAfgABTAAIc2VydmVySXBxAH4AAkwAB3RpbWVvdXR0ABNMamF2YS9sYW5nL0ludGVnZXI7eHAAAAAAcHQAG2pzcHRfYXN5bl9zaW5nbGVUcmFkZUZvckh5bHNyAA5qYXZhLmxhbmcuTG9uZzuL5JDMjyPfAgABSgAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAFtv53L9XBwcHQKBAo8cmVxPiAKICA8cmVxdWVzdEhlYWRlcj4gCiAgICA8cmVxX3N5c19pZD4wQTwvcmVxX3N5c19pZD4gIAogICAgPHNlcnZfc3lzX2lkPjBCPC9zZXJ2X3N5c19pZD4gIAogICAgPHNlcnZpY2VfaWQ+MjAxNTEwMDM8L3NlcnZpY2VfaWQ+ICAKICAgIDxyZWNlaXZlX3RpbWU+MjAxOTEwMTIxODU4MDI8L3JlY2VpdmVfdGltZT4gIAogICAgPGRlc2NyaXB0aW9uPuaUr+S7mOexu+Wei+OAkHBtdFRwPTAz44CR5Lqk5piT5oql5paHPC9kZXNjcmlwdGlvbj4gIAogICAgPGlzX2FjdHVhbD5ZPC9pc19hY3R1YWw+ICAKICAgIDxpc19iYXRjaD5OPC9pc19iYXRjaD4gIAogICAgPHN5c19ub2RlX2ZsYWc+MTwvc3lzX25vZGVfZmxhZz4gCiAgPC9yZXF1ZXN0SGVhZGVyPiAgCiAgPHJlcXVlc3RNc2c+IAogICAgPFBtdElEPiAKICAgICAgPEZybXRWcnNuPjEuMDwvRnJtdFZyc24+ICAKICAgICAgPFZlcnNpb25Obz4xLjA2PC9WZXJzaW9uTm8+ICAKICAgICAgPFBtdFRwPjAzPC9QbXRUcD4gIAogICAgICA8UG10V2F5PjAxPC9QbXRXYXk+ICAKICAgICAgPENhcFRtPjIwMTkxMDEyMTg1ODAyPC9DYXBUbT4gIAogICAgICA8VHhuVHA+MTE8L1R4blRwPiAgCiAgICAgIDxCaXpUcD4wMTwvQml6VHA+ICAKICAgICAgPE9yZGVySWQ+MTkxMjE4NTUxNDk4MjQ8L09yZGVySWQ+ICAKICAgICAgPFJldkR0PjIwMTkxMDEyMTg1ODAyPC9SZXZEdD4gIAogICAgICA8U2lnbkluZm8+OGUwYWJmM2QwOTE2MTM0MDE2YjkyMmViOWVkM2Y0ZmU8L1NpZ25JbmZvPiAgCiAgICAgIDxQbXRDaG5UcD5USElSRDwvUG10Q2huVHA+ICAKICAgICAgPENobk5vdGlVcmw+aHR0cDovLzEwLjEwMC4xNDAuNTk6ODA4MC9yZWdyZXNzaW9uL25vdGljZTwvQ2huTm90aVVybD4gIAogICAgICA8UHJvY01kPjAxPC9Qcm9jTWQ+ICAKICAgICAgPE1zZ0lEPjJmMmU5YjE5LTQ1NDgtNDRmYi1hMThhLThlN2JkNjEwNDMxYjwvTXNnSUQ+ICAKICAgICAgPFRyYW5UbT4yMDE5MTAxMjE4NTczMjwvVHJhblRtPiAgCiAgICAgIDxMb2NhbFRtPjIwMTkxMDEyMTg1NzMyPC9Mb2NhbFRtPiAgCiAgICAgIDxNc2dTdHM+MTU8L01zZ1N0cz4gIAogICAgICA8Q2hrU3RzPjAxPC9DaGtTdHM+ICAKICAgICAgPFR4SWQ+Q0VYMTkxMDEyMTg1NzMyNDE4OTQ0MjAxODE4PC9UeElkPiAgCiAgICAgIDxDYXJkVHA+MDE8L0NhcmRUcD4gIAogICAgICA8TnNBY2NTdHRsRHQ+MjAxOTA4MzAwMDAwMDA8L05zQWNjU3R0bER0PiAKICAgIDwvUG10SUQ+ICAKICAgIDxDaG5sPiAKICAgICAgPEFjcUlkPjAxPC9BY3FJZD4gIAogICAgICA8Q2hubElEPk8wMDE8L0NobmxJRD4gIAogICAgICA8UHJvZEluZm8+IAogICAgICAgIDxNZXJjaElEPjAwMDAxMDU8L01lcmNoSUQ+ICAKICAgICAgICA8UHJvZElEPm9yZGVydG9vbDwvUHJvZElEPiAgCiAgICAgICAgPFByZFF0eT4xMDwvUHJkUXR5PiAgCiAgICAgICAgPFByZEluZm8+5paw57uT566XLea1i+ivleW8gOWPkTwvUHJkSW5mbz4gCiAgICAgIDwvUHJvZEluZm8+ICAKICAgICAgPEFjY3RTeXNGbGc+MDI8L0FjY3RTeXNGbGc+ICAKICAgICAgPEFQUElkPjExMTwvQVBQSWQ+IAogICAgPC9DaG5sPiAgCiAgICA8RGJ0cj4gCiAgICAgIDxDb250cmFjdE5vPjYyMjAxMDAwMDAwMDAwMDAxMTwvQ29udHJhY3RObz4gIAogICAgICA8SURObz40OTk1OGM1ZmQ0Y2EwMWE0YTMwNTBjM2EyYzU4NzA4YzU0NDM5YWFlMzYyY2IxYmE8L0lETm8+ICAKICAgICAgPElEVHlwZT4xMTE8L0lEVHlwZT4gIAogICAgICA8Q29udGFjdE5vPjQ0YjljMjg3MTcxYTJlYzBmMjdkNmE3NzlkZDcyYmQ4PC9Db250YWN0Tm8+ICAKICAgICAgPE5tPmIzYWMyZGZkNTJhN2Y1OWU3MDViZTA1NjQyMjBkMjllPC9ObT4gCiAgICA8L0RidHI+ICAKICAgIDxDZHRyPiAKICAgICAgPENvbnRyYWN0Tm8+NjIyMDEwMDAwMDAwMDAwMDExPC9Db250cmFjdE5vPiAKICAgIDwvQ2R0cj4gIAogICAgPFR4bkFtdEluZm8+IAogICAgICA8SW50ckJrU3R0bG1BbXQgQ2N5PSJDTlkiPjAuMDE8L0ludHJCa1N0dGxtQW10PiAKICAgIDwvVHhuQW10SW5mbz4gIAogICAgPEV4dGVuc2lvbj4gCiAgICAgIDxSZW1hcms+5YiY546J5p+xPC9SZW1hcms+IAogICAgPC9FeHRlbnNpb24+ICAKICAgIDxEYnRyQWNjdD4gCiAgICAgIDxJc3NyQ2Q+MDEwMjwvSXNzckNkPiAgCiAgICAgIDxBY2N0RmxnPjA8L0FjY3RGbGc+ICAKICAgICAgPElkPjYyMTIyNjAyMDAwMDE2OTA3OTc8L0lkPiAgCiAgICAgIDxObT5iM2FjMmRmZDUyYTdmNTllNzA1YmUwNTY0MjIwZDI5ZTwvTm0+ICAKICAgICAgPFRwPjA8L1RwPiAKICAgIDwvRGJ0ckFjY3Q+ICAKICAgIDxSb3V0SW5mbz4gCiAgICAgIDxDZHRySWQ+SFlMPC9DZHRySWQ+ICAKICAgICAgPFN3aXRjaExpc3Q+W3siZXhNZXJjaElkIjoiMDAxMDUzMTEwMDAwMDAxIiwiaWZVc2VkIjoiMSIsInR1bm5lbElkIjoiSFlMIn1dPC9Td2l0Y2hMaXN0PgogICAgPC9Sb3V0SW5mbz4gIAogICAgPENkdHJBY2N0PgogICAgICA8RXhNZXJjaElkPjAwMTA1MzExMDAwMDAwMTwvRXhNZXJjaElkPgogICAgPC9DZHRyQWNjdD4KICA8L3JlcXVlc3RNc2c+IAo8L3JlcT4KdAAAcHB0ACRjYzc3NWI1YS1mYjBkLTRlZmUtODIwMi0yOGJmYjYxNjEwODFwcHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cQB+AAkAAAEs";
		System.out.println(Base64.getEncoder().encodeToString(TranscodeUtil.base64StrToByteArray(str)));
	}
} 
