package com.sina.weibo.sdk.demo.dispatchmessage;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by wangjie on 2017/3/31.
 * <p>
 * 注解方法所能接收相应的参数字段
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageSubscribe {
    int messageType() default 0;

    Class<?> classType();
}
