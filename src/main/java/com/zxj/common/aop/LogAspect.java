package com.zxj.common.aop;

import org.apache.commons.io.output.StringBuilderWriter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zxj.common.beantrace.BeanTraces;

@Service
public class LogAspect {

    /**
     * Get a logger in the context of the class instead of the
     * context of the interceptor. Otherwise every log message
     * will look like it's coming from here.
     *
     * @param jp JoinPoint
     * @return Logger
     */
    protected Logger getLog(JoinPoint jp) {
        return LoggerFactory.getLogger(jp.getTarget().getClass());
    }


    /**
     * doAfter
     *
     * @param jp JoinPoint
     */
    public void doAfter(JoinPoint jp) {
    }

    /**
     * doAround
     *
     * @param pjp ProceedingJoinPoint
     * @return method return value
     * @throws Throwable
     */
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Logger log = getLog(pjp);
        StringBuilderWriter sbw = null;
        if (log.isDebugEnabled()) {
            sbw = BeanTraces.getStringBuilderWriter();
            sbw.write("Bean=");
            BeanTraces.printBeanTraceAscii(pjp.getTarget(), sbw);
            sbw.write("\nMethod=" + pjp.getSignature());
            sbw.write("\nArgs=");
            BeanTraces.printBeanTraceAscii(pjp.getArgs(), sbw);
        }
        long time = System.currentTimeMillis();
        Object retVal = pjp.proceed();
        time = System.currentTimeMillis() - time;
        if (log.isDebugEnabled()) {
            if (sbw != null) {
                sbw.write("\nprocess time: " + time + " ms\n\nRETURN=");
                BeanTraces.printBeanTraceAscii(retVal, sbw);
                log.debug(sbw.getBuilder().toString());
            }
        }
        return retVal;
    }

    /**
     * doBefore
     *
     * @param jp JoinPoint
     */
    public void doBefore(JoinPoint jp) {

    }

    /**
     * catch exception
     *
     * @param jp JoinPoint
     * @param ex Throwable
     */
    public void doThrowing(JoinPoint jp, Throwable ex) {
        Logger log = getLog(jp);
        if (log.isErrorEnabled()) {
            log.error(jp.getSignature().toString(), ex);
        }
    }
}