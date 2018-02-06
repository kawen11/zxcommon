package com.zxj.common.benchmark;


import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * run a test
 */
public class BenchmarkRunner {

    public void run(final BenchmarkJob job) throws Exception {
        // validate and init data
        if (job == null)
            throw new Exception("job cannot be null");
        if (job.getTarget() == null)
            throw new Exception("the test target cannot be null. What you want to test? ");
        if (job.getOptions() == null) {
            job.setOptions(BenchmarkJobOptions.defaultOptions());
        }
        if (job.getOptions().getConcurrency() <= 0)
            throw new Exception("concurrency should be > 0");
        if (job.getOptions().getNumOfTests() <= 0)
            throw new Exception("numOfTests should be > 0");
        job.setResult(new BenchmarkJobResult());

        // create a thread pool
        BenchmarkJobOptions options = job.getOptions();
        BenchmarkJobResult result = job.getResult();
        ExecutorService threadPool = Executors.newFixedThreadPool(
                options.getConcurrency(), new BenchmarkThreadFactory());

        System.out.println("Test started.");

        // start working
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 1; i <= options.getNumOfTests(); i++) {
            FbCall call = new FbCall(job);
            threadPool.submit(call);
        }

        try {
            threadPool.shutdown();
            System.out.println("Awaiting termination...");
            threadPool.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            // foreget about it
        }
        stopWatch.stop();
        System.out.println("Test completed.");

        // fill results
        result.setTimeTakenForTests(stopWatch.getTime());
        result.setConcurrency(options.getConcurrency());
    }

    private static class FbCall implements Callable<Void> {
        private BenchmarkJob job;

        public FbCall(BenchmarkJob job) {
            this.job = job;
        }

        @Override
        public Void call() throws Exception {
            try {
                doIt();
            } catch (Exception e) {
                System.err.print("Failed to invoke a single test. ");
                e.printStackTrace();
            }
            return null;
        }

        private void doIt() {
            boolean successful = false;

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            try {
                successful = job.getTarget().invoke();
            } catch (Exception e) {
                successful = false;
            }
            stopWatch.stop();
            if (successful) {
                job.getResult().getSuccessfulTests().incrementAndGet();
            } else {
                job.getResult().getFailedTests().incrementAndGet();
            }

            job.getResult()
                    .addSingleTestResult(successful, stopWatch.getTime());
            int results = job.getResult().getNumOfTests();
            if (results != 0 && !job.getOptions().isQuiet()
                    && (job.getResult().getNumOfTests() % 100 == 0)) {
                System.err.printf("%d/%d are done\n", results, job.getOptions()
                        .getNumOfTests());
            }
        }

    }

    /**
     * copied from jdk's source code
     */
    private static class BenchmarkThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        private BenchmarkThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
                    .getThreadGroup();
            namePrefix = "fb-runnner-pool-" + poolNumber.getAndIncrement()
                    + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix
                    + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
