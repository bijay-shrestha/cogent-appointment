package com.cogent.cogentappointment.persistence.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext CONTEXT = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }

    public static <T> T getBean(Class<T> beanName) {
        return CONTEXT.getBean(beanName);
    }
}

