package com.qivay.configuration;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @ClassName: WebLogAspect
 * @Description: 拦截日志类（AOP）
 */

@Aspect
@Order(5)
@Component
public class WebLogAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.qivay.*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(null != attributes){
            HttpServletRequest request = attributes.getRequest();
            String param = Arrays.toString(joinPoint.getArgs());

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();



            // 记录下请求内容
            logger.info("*************************AOP请求日志拦截START***************************");
            //logger.info("* USERNAME : "+UserService.getCurrentName());
            logger.info("* URL : " + request.getRequestURL().toString());
            //logger.info("* URI : " + uri);
            //logger.info("* HTTP_METHOD : " + methodType);
            //logger.info("* IP : " + ip);
            logger.info("* CLASS_METHOD : " + signature.getDeclaringTypeName() + "." + signature.getName());
            logger.info("* ARGS : " + param);

            Method method = signature.getMethod();

            logger.info("**********************************************************************");
        }
    }

    /**
     * 请求之后的响应信息
     *
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret){
        // 处理完请求，返回内容
        logger.info("***************************拦截器END*****************************");
        logger.info("RESPONSE : " + ret);
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
        logger.info("***********************************************************");
    }

    /**
     * 请求之后的报错日志
     * @param e
     */
    @AfterThrowing(pointcut="execution(* com.qivay..*(..))", throwing = "e")
    public void doAfterThrowing(Throwable e) {
        logger.error("拦截异常打印："+e.toString());
    }

}