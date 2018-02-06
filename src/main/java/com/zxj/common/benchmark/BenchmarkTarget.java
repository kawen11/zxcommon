package com.zxj.common.benchmark;

/**
 * The java code block to test
 */
public interface BenchmarkTarget {
    /**
     * put the code inside this callback method. If you have any parameters,
     * please set them as member variables
     *
     * @return successful?
     */
    boolean invoke() ;
}