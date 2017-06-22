package com.sina.weibo.sdk.demo.dispatchmessage;

import java.util.HashMap;

/**
 * Created by wangjie on 2017/4/13.
 * <p>
 * 目前为止，所有互动SDK提供的用户消息类型
 */

public class MessageType {
    /**
     * 消息类型说明
     */
    public static final int COMMENT = 1;// 聊天消息
    public static final int FAVOR = 2;// 点赞消息
    public static final int LIGHTUP = 3;// 点亮主播消息
    public static final int SHUTUP = 4;// 禁言消息
    public static final int GIFT = 5;// 礼物消息
    public static final int NOTICE = 6;// 公告消息
    public static final int SHARE = 7;//分享直播消息
    public static final int FOCUS = 8;//关注主播消息
    public static final int ADD_TO_CART = 9;//加入购物车消息【已废弃】
    public static final int PRODUCT = 10;//商品消息【已废弃】
    public static final int LIVE_STATE_CHANGE = 11;//直播变更消息（房间的任何状态变更都会进行push）
    public static final int JOIN_OR_EXIT_ROOM = 12;//加入/退出房间消息
    public static final int REWARD = 13;//打赏消息
    public static final int ADMIN_CHANGE = 14;//管理员变更消息
    public static final int SYSTEM = 15;//系统消息（该消息"sender_info"字段为空,"send_info":{}）【开发中】
    public static final int STICK = 16;//置顶/取消置顶评论消息 【开发中】
    public static final int CUSTOM = 100;//自定义消息类型
    public static final int CUSTOM_EXIT_ROOM= 101;//自定义退出消息类型

    /**
     * 系统消息子类型说明（sys_msg_type）
     */

    public static final int SYSTEM_SHOWCASE = 151;//橱窗
    public static final int SYSTEM_FLOW_STATE = 152;//流状态
    public static final int SYSTEM_TOP_UP_ISSUED = 153;//充值下发
    public static final int SYSTEM_REVIEW = 154;//审核消息
    public static final int SYSTEM_ACTIVITY_STANDARD = 155;//活动角标
    public static final int SYSTEM_USERS_LIST = 156;//用户列表
    public static final int SYSTEM_HOST_GOLD_COIN_CHANGE = 157;//主播金币变化
    public static final int SYSTEM_MASK_HEAD = 160;//mask头部信息（图文直播
    public static final int SYSTEM_DISCUSS = 161;// 实时讨论（图文直播，包含，体育赛况，微博，系统通知)
    public static final int SYSTEM_OTHER = 1599;//其他
    /**
     * 这里可以定义扩展的字段，根据自己的业务而定。
     */
    public static final int COMMENT_LIKE = 26;//评论点赞消息类型
    public static final int PLAYBACK_GIFTS = 27;//回放送礼


    /**
     * 播放控制
     */
    public static final int PLAY_CONTROLLER = 0x100000;

    /**
     * 开始分享
     */
    public static final int START_SHARE = 0x200000;


    private HashMap<Integer, Integer> messageTypeMap;

    public MessageType() {
        if (messageTypeMap == null) messageTypeMap = new HashMap<>();
        productData();
    }

    private void productData() {
        messageTypeMap.put(COMMENT, COMMENT);
        messageTypeMap.put(FAVOR, FAVOR);
        messageTypeMap.put(LIGHTUP, LIGHTUP);
        messageTypeMap.put(SHUTUP, SHUTUP);
        messageTypeMap.put(GIFT, GIFT);
        messageTypeMap.put(NOTICE, NOTICE);
        messageTypeMap.put(SHARE, SHARE);
        messageTypeMap.put(FOCUS, FOCUS);
        messageTypeMap.put(ADD_TO_CART, ADD_TO_CART);
        messageTypeMap.put(PRODUCT, PRODUCT);
        messageTypeMap.put(LIVE_STATE_CHANGE, LIVE_STATE_CHANGE);
        messageTypeMap.put(JOIN_OR_EXIT_ROOM, JOIN_OR_EXIT_ROOM);
        messageTypeMap.put(REWARD, REWARD);
        messageTypeMap.put(ADMIN_CHANGE, ADMIN_CHANGE);
        messageTypeMap.put(STICK, STICK);
        messageTypeMap.put(CUSTOM, CUSTOM);
        messageTypeMap.put(SYSTEM, SYSTEM);
        messageTypeMap.put(SYSTEM_SHOWCASE, SYSTEM_SHOWCASE);
        messageTypeMap.put(SYSTEM_FLOW_STATE, SYSTEM_FLOW_STATE);
        messageTypeMap.put(SYSTEM_TOP_UP_ISSUED, SYSTEM_TOP_UP_ISSUED);
        messageTypeMap.put(SYSTEM_REVIEW, SYSTEM_REVIEW);
        messageTypeMap.put(SYSTEM_ACTIVITY_STANDARD, SYSTEM_ACTIVITY_STANDARD);
        messageTypeMap.put(SYSTEM_USERS_LIST, SYSTEM_USERS_LIST);
        messageTypeMap.put(SYSTEM_HOST_GOLD_COIN_CHANGE, SYSTEM_HOST_GOLD_COIN_CHANGE);
        messageTypeMap.put(SYSTEM_MASK_HEAD, SYSTEM_MASK_HEAD);
        messageTypeMap.put(SYSTEM_DISCUSS, SYSTEM_DISCUSS);
        messageTypeMap.put(SYSTEM_OTHER, SYSTEM_OTHER);
        messageTypeMap.put(COMMENT_LIKE, COMMENT_LIKE);
        messageTypeMap.put(PLAYBACK_GIFTS, PLAYBACK_GIFTS);
        messageTypeMap.put(PLAY_CONTROLLER,PLAY_CONTROLLER);
        messageTypeMap.put(START_SHARE,START_SHARE);
        messageTypeMap.put(CUSTOM_EXIT_ROOM,CUSTOM_EXIT_ROOM);
    }

    public HashMap<Integer, Integer> getMessageTypeMap() {
        return messageTypeMap;
    }

}
