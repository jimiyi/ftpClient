package com.best.oasis.express.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by yiwei on 2017/3/11.
 */
public class SpringContext {
    private volatile static ApplicationContext context = null;
    private final static String SPRING_FILE = "classpath:spring-config.xml";

    public static Object getBean(String name)
    {
        synchronized (SpringContext.class) {
            if(context == null)
            {
                context = new FileSystemXmlApplicationContext(SPRING_FILE);
            }
        }
        return context.getBean(name);

    }
}
