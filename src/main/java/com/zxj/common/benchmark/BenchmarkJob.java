package com.zxj.common.benchmark;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A job made up of a number of concurrent tests
 */
public class BenchmarkJob {

    private BenchmarkTarget target;

    private BenchmarkJobOptions options;

    private BenchmarkJobResult result;

    public BenchmarkTarget getTarget() {
        return target;
    }

    public void setTarget(BenchmarkTarget target) {
        this.target = target;
    }

    public BenchmarkJobOptions getOptions() {
        return options;
    }

    public void setOptions(BenchmarkJobOptions options) {
        this.options = options;
    }

    public BenchmarkJobResult getResult() {
        return result;
    }

    public void setResult(BenchmarkJobResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
