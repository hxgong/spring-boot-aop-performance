package sample.aop.monitor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class PerformanceMonitor {

	private static final Logger logger = LoggerFactory
			.getLogger(PerformanceMonitor.class);

	@Pointcut(value = "execution(public * *(..))")
	public void anyPublicMethod() {
	}

	@Around("anyPublicMethod() && @annotation(ProfileExecution)")
	public Object profileExecuteMethod(ProceedingJoinPoint jointPoint)
			throws Throwable {
		Signature signature = jointPoint.getSignature();
		String methodName = signature.toShortString();

		StopWatch stopWatch = new StopWatch(
				PerformanceMonitor.class.getName());
		stopWatch.start(methodName);

		Object retVal = jointPoint.proceed();

		stopWatch.stop();
		logger.info(stopWatch.prettyPrint());
		
		return retVal;

	}
}
