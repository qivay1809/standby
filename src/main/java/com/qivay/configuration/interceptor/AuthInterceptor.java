package com.qivay.configuration.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.qivay.service.UpdateNoticeService;
import org.apache.tomcat.util.buf.MessageBytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaohefei on 20200618
 * 分布式服务维护时，启用该服务
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private UpdateNoticeService updateNoticeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);

        String uri = getUri(request);//request.getRequestURI();
        JSONObject notice = updateNoticeService.UpdateNotice("updateNotice");
        logger.info("================拦截请求：" + uri + "==================");

        PrintWriter out = response.getWriter();
        JSONObject object = new JSONObject();
        object.put("code", 100001);
        object.put("msg", notice.getString("title"));
        object.put("obj", notice.getString("text"));
        out.print(object);
        return false;
    }

    private String getUri(HttpServletRequest request) {
        try {
            Object a = findCoyoteRequest(request);
            Field coyoteRequest = a.getClass().getDeclaredField("coyoteRequest");
            coyoteRequest.setAccessible(true);
            Object b = coyoteRequest.get(a);

            Field uriMB = b.getClass().getDeclaredField("uriMB");
            uriMB.setAccessible(true);
            MessageBytes c = (MessageBytes) uriMB.get(b);
            return c.getString();
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            return "未知";
        }
        //return "未知";
    }

    //根据Field获得对应的Class
    private Class getClassByName(Class classObject, String name) {
        Map<Class, List<Field>> fieldMap = new HashMap<>();
        Class returnClass = null;
        Class tempClass = classObject;
        while (tempClass != null) {
            fieldMap.put(tempClass, Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }

        for (Map.Entry<Class, List<Field>> entry : fieldMap.entrySet()) {
            for (Field f : entry.getValue()) {
                if (f.getName().equals(name)) {
                    returnClass = entry.getKey();
                    break;
                }
            }
        }
        return returnClass;
    }

    //递归遍历父类寻找coyoteRequest Field
    private Object findCoyoteRequest(Object request) throws Exception {
        Class a = getClassByName(request.getClass(), "request");
        Field request1 = a.getDeclaredField("request");
        request1.setAccessible(true);
        Object b = request1.get(request);
        if (getClassByName(b.getClass(), "coyoteRequest") == null) {
            return findCoyoteRequest(b);
        } else {
            return b;
        }
    }
}
