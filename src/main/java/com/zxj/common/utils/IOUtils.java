package com.zxj.common.utils;

import co.paralleluniverse.fibers.Suspendable;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zxj.common.config.StringPool;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * Created by zxj on 16/11/24.
 */
public class IOUtils extends org.apache.commons.io.IOUtils {

    /**
     * logger
     */
    static Logger logger = LoggerFactory.getLogger(IOUtils.class);

    /**
     * stream 转为 字符串
     *
     * @param inputStream
     * @param strCharset
     * @return
     * @throws IOException
     */
    public static String stream2String(InputStream inputStream, String strCharset) throws IOException {
        Charset charset = Charsets.toCharset(strCharset);
        return stream2String(inputStream, charset);
    }

    /**
     * stream 转为 字符串
     *
     * @param inputStream
     * @param charset
     * @return
     * @throws IOException
     */
    @Suspendable
    public static String stream2String(InputStream inputStream, Charset charset) throws IOException {
        if (inputStream == null)
            return StringPool.Symbol.EMPTY;
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(inputStream, writer, charset);
            return writer.toString();
        } catch (IOException e) {
            logger.error("read stream to string error", e);
            throw e;
        } finally {
            closeQuietly(inputStream);
        }
    }

    /**
     * 读取 classpath 下的文件
     *
     * @param filePath
     * @param charset
     * @return
     */
    public static String readClassPathResource(String filePath, String charset) throws IOException {
        InputStream resourceAsStream = IOUtils.class.getResourceAsStream(filePath);
        if (resourceAsStream != null) {
            return stream2String(resourceAsStream, charset);
        }
        return null;
    }
}
