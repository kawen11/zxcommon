package com.zxj.common.benchmark;

/**
 * The most used operations. If you need advanced ones, see {@link BenchmarkRunner}
 */
public class Benchmark {
	
	public static BenchmarkJobResult benchmark(BenchmarkTarget target, int concurrency, int numOfTests) throws Exception {
		BenchmarkRunner runner = new BenchmarkRunner();
		BenchmarkJob job = new BenchmarkJob();
		
		BenchmarkJobOptions options = new BenchmarkJobOptions();
		options.setConcurrency(concurrency);		
		options.setNumOfTests(numOfTests);
		job.setOptions(options);
		job.setTarget(target);

		runner.run(job);
		return job.getResult();
	}

}
