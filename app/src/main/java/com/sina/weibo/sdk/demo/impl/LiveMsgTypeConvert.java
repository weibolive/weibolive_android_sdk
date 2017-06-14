package com.sina.weibo.sdk.demo.impl;

import com.sina.weibo.sdk.demo.dispatchmessage.IMsgTypeConvert;
import com.sina.weibo.sdk.demo.dispatchmessage.MessageType;

/**
 * Created by junliang on 2017/5/7.
 */

public class LiveMsgTypeConvert implements IMsgTypeConvert {

    @Override
    public int convertType(int[] sourceType) {
        int targetType = -1;

        if (sourceType.length == 0) {
            return targetType;
        }

        if (sourceType[0] == MessageType.SYSTEM && sourceType.length > 1) {
            targetType = sourceType[0] * 10 + sourceType[1];
        } else {
            targetType = sourceType[0] ;
        }

        return targetType;
    }
}
