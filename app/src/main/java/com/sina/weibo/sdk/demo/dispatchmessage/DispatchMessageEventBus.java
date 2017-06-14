package com.sina.weibo.sdk.demo.dispatchmessage;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangjie on 2017/3/31.
 * <p>
 * 自定义消息注解框架。
 */

public class DispatchMessageEventBus {
    public static final String TAG = DispatchMessageEventBus.class.getSimpleName();
    private static DispatchMessageEventBus defaultInstance;
    //    private List<SoftReference<SaveSubscribeMessage>> subscribeMessageList;
    private List<SaveSubscribeMessage> subscribeMessageList;
    private Gson gson;
    private MessageType messageType;

    public static DispatchMessageEventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (DispatchMessageEventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new DispatchMessageEventBus();
                }
            }
        }
        return defaultInstance;
    }

    private DispatchMessageEventBus() {
        if (messageType == null) messageType = new MessageType();
        if (subscribeMessageList == null) subscribeMessageList = new ArrayList<>();

    }

    /**
     * 使用方必须得提前调用此方法进行注册，才能使用该注解框架。
     *
     * @param subscriber
     */
    public void register(Object subscriber) {
        if (subscriber == null) {
            Log.e(TAG, "订阅者不能为null,请检查 ");
            return;
        }
        Class<?> subscriberClass = subscriber.getClass();
        Method[] methods = subscriberClass.getMethods();


        for (Method method : methods) {
             /*
             * 判断方法中是否有指定注解类型的注解
             */
            boolean hasAnnotation = method.isAnnotationPresent(MessageSubscribe.class);
            if (hasAnnotation) {
                /*
                 * 根据注解类型返回方法的指定类型注解
                 */
                MessageSubscribe annotation = method.getAnnotation(MessageSubscribe.class);
                Log.e("tag", "register: " + "method = " + method.getName()
                        + " ; id = " + annotation.messageType() + " ; description = "
                        + annotation.classType());
                SaveSubscribeMessage saveSubscribeMessage = new SaveSubscribeMessage();
                saveSubscribeMessage.setMethod(method);
                saveSubscribeMessage.setNeedClazz(annotation.classType());
                saveSubscribeMessage.setMessageType(annotation.messageType());
                saveSubscribeMessage.setSubscriber(subscriber);

                addRegisterObject(saveSubscribeMessage);
            }

        }
    }

    /**
     * 添加新注册数据的对象到集合里。
     *
     * @param saveSubscribeMessage
     */
    private void addRegisterObject(SaveSubscribeMessage saveSubscribeMessage) {
        if (subscribeMessageList == null) return;
        if (saveSubscribeMessage != null) {
            deleteOldRegisterObject(saveSubscribeMessage);
//            subscribeMessageList.add(new SoftReference<>(saveSubscribeMessage));
            subscribeMessageList.add(saveSubscribeMessage);
        }
    }

    /**
     * 遍历集合的数据是否包含注册的对象，若包含就将其移除，为了避免重复添加无用对象到集合里，导致内存泄漏
     *
     * @param saveSubscribeMessage
     */
    private void deleteOldRegisterObject(SaveSubscribeMessage saveSubscribeMessage) {
        for (int i = 0; i < subscribeMessageList.size(); i++) {
//            SoftReference<SaveSubscribeMessage> subscribeMessageSoftReference = subscribeMessageList.get(i);
            SaveSubscribeMessage subscribeMessageSoftReference = subscribeMessageList.get(i);
//            SaveSubscribeMessage message = subscribeMessageSoftReference.get();
            SaveSubscribeMessage message = subscribeMessageSoftReference;
            if (message == null) continue;
            Log.e(TAG, "deleteOldRegisterObject: message" + message.toString());
            if (message.toString().equals(saveSubscribeMessage.toString())) {
                subscribeMessageList.remove(i);
                i--;
            }
        }
    }

    /**
     * Unregisters the given subscriber from all event classes.
     */
    public synchronized void unregister(Object subscriber) {
        if (subscriber == null) {
            Log.e(TAG, "订阅者已经是null,还有必要解除注册吗？请检查");
            return;
        }
        if (subscribeMessageList == null || subscribeMessageList.size() == 0) {
            Log.e(TAG, "Subscriber to unregister was not registered before: " + subscriber.getClass());
            return;
        }

        for (int i = 0; i < subscribeMessageList.size(); i++) {
//            SoftReference<SaveSubscribeMessage> subscribeMessageSoftReference = subscribeMessageList.get(i);
            SaveSubscribeMessage subscribeMessageSoftReference = subscribeMessageList.get(i);
//            SaveSubscribeMessage saveSubscribeMessage = subscribeMessageSoftReference.get();
            SaveSubscribeMessage saveSubscribeMessage = subscribeMessageSoftReference;
            if (saveSubscribeMessage == null) continue;
            if (saveSubscribeMessage.getSubscriber().equals(subscriber)) {
                subscribeMessageList.remove(i);
                i--;
            }
        }

    }

    /**
     * 互动SDK下发数据可以调用该方法，把数据转发给消息订阅者
     */

    public void post(int type, String messageContent) {
        if (subscribeMessageList == null || subscribeMessageList.size() == 0 || messageType == null) {
            Log.e(TAG, "post: " + "please register DispatchMessageEventBus before user post method.");
            return;
        }

//        for (SoftReference<SaveSubscribeMessage> subscribeMessageSoftReference : subscribeMessageList) {
        for (SaveSubscribeMessage subscribeMessageSoftReference : subscribeMessageList) {
//            SaveSubscribeMessage saveSubscribeMessage = subscribeMessageSoftReference.get();
            SaveSubscribeMessage saveSubscribeMessage = subscribeMessageSoftReference;
            HashMap<Integer, Integer> messageTypeMap = messageType.getMessageTypeMap();
            if (saveSubscribeMessage == null) {
                Log.e(TAG, "post: " + "互动下发的用户消息类型，和注册的消息类型匹配失败，请检查");
                continue;
            }
            Log.e(TAG, "post: message" + saveSubscribeMessage.toString());

            Integer subscribeMessageType = messageTypeMap.get(saveSubscribeMessage.getMessageType());
            if (subscribeMessageType != null && subscribeMessageType == type)
                invokeMethod(saveSubscribeMessage, messageContent);
        }

    }

    private void invokeMethod(SaveSubscribeMessage saveSubscribeMessage, String messageContent) {
        Method method = null;
        Object subscriber = null;

        try {
            subscriber = saveSubscribeMessage.getSubscriber();
            method = saveSubscribeMessage.getMethod();
            if (null == gson) gson = new Gson();

            Class<?> needClazz = saveSubscribeMessage.getNeedClazz();
//            Object needObject = JSON.parseObject(messageContent, needClazz);
            Object needObject = gson.fromJson(messageContent, needClazz);
            try {
                method.invoke(subscriber, needObject);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (JsonSyntaxException e) {
            if (method != null && subscriber != null) try {
                method.invoke(subscriber, "");
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
        } catch (JsonParseException e) {
            Log.e(TAG, "invokeMethod:----JsonParseException= " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
