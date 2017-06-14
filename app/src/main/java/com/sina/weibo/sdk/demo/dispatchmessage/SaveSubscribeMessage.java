package com.sina.weibo.sdk.demo.dispatchmessage;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Created by wangjie on 2017/4/1.
 * <p>
 * 用来保存注册者所提供相应信息的对象。
 */

public class SaveSubscribeMessage implements Serializable {
    private Method method;
    private int messageType;
    private Object subscriber;
    private Class<?> NeedClazz;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Object getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Object subscriber) {
        this.subscriber = subscriber;
    }

    public Class<?> getNeedClazz() {
        return NeedClazz;
    }

    public void setNeedClazz(Class<?> needClazz) {
        NeedClazz = needClazz;
    }

    @Override
    public String toString() {
        return "SaveSubscribeMessage{" +
                "method=" + method +
                ", messageType=" + messageType +
                ", NeedClazz=" + NeedClazz +
                '}';
    }

}
