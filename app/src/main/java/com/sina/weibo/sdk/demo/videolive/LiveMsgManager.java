package com.sina.weibo.sdk.demo.videolive;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sina.sinalivesdk.WBIMLiveClient;
import com.sina.sinalivesdk.interfaces.WBIMLiveConnListener;
import com.sina.sinalivesdk.interfaces.WBIMLiveListener;
import com.sina.sinalivesdk.interfaces.WBIMLiveValueCallBack;
import com.sina.sinalivesdk.models.ForbiddenUserListModel;
import com.sina.sinalivesdk.models.JoinRoomModel;
import com.sina.sinalivesdk.models.PushMessageModel;
import com.sina.sinalivesdk.models.RoomUserListModel;
import com.sina.sinalivesdk.models.UserModel;
import com.sina.sinalivesdk.request.AddToCartRequest;
import com.sina.sinalivesdk.request.ExitRoomRequest;
import com.sina.sinalivesdk.request.FetchRoomForbiddenListRequest;
import com.sina.sinalivesdk.request.FetchRoomUserListRequest;
import com.sina.sinalivesdk.request.FollowAnchorRequest;
import com.sina.sinalivesdk.request.ForBidMsgRequest;
import com.sina.sinalivesdk.request.JoinRoomRequest;
import com.sina.sinalivesdk.request.LikeRequest;
import com.sina.sinalivesdk.request.RewardRequest;
import com.sina.sinalivesdk.request.SendMsgRequest;
import com.sina.sinalivesdk.request.ShareRequest;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.demo.AppContext;
import com.sina.weibo.sdk.demo.dispatchmessage.DispatchMessageEventBus;
import com.sina.weibo.sdk.demo.dispatchmessage.IMsgTypeConvert;
import com.sina.weibo.sdk.demo.dispatchmessage.MessageType;
import com.sina.weibo.sdk.demo.impl.LiveMsgTypeConvert;

/**
 * Created by WangJie on 2017/6/12.
 */

public class LiveMsgManager {
    private boolean isNeedCallBack = false;

    private IMsgTypeConvert mMsgTypeConverter;
    public static LiveMsgManager mInstance = new LiveMsgManager();
    SharedPreferences preferences = AppContext.getAppContext().getSharedPreferences("config", Context.MODE_PRIVATE);

    class InternalConnListener implements WBIMLiveConnListener {

        @Override
        public void onConnected() {
            Log.e("tag", "onConnected: ----success=");
        }

        @Override
        public void onDisconnected(final int code, final String desc) {
            Log.e("tag", "onDisconnected: ----code=" + code + ",desc=" + desc);
        }
    }


    private LiveMsgManager() {
        initClient();
        mMsgTypeConverter = new LiveMsgTypeConvert();
    }

    private void initClient() {
        initData(getUser(), getBundle());
    }


    private void initData(UserModel user, Bundle bundle) {

        WBIMLiveClient.getInstance().init(AppContext.getAppContext(), user, bundle);
        WBIMLiveClient.getInstance().addMessageListener(new InternalMessageListener());
        WBIMLiveClient.getInstance().setConnListener(new InternalConnListener());
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
//        bundle.putString(com.sina.sinalivesdk.util.Constants.KEY_AUTHORIZATION, Constants.KEY_AUTHORIZATION);
//        bundle.putString(com.sina.sinalivesdk.util.Constants.KEY_APPKEY, Constants.APP_KEY);
//        bundle.putBoolean(com.sina.sinalivesdk.util.Constants.KEY_NEED_SSL, false);
        return bundle;
    }

    private UserModel getUser() {
        UserModel user = new UserModel();
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(AppContext.getAppContext());
        user.setAccess_token(accessToken.getToken());
        user.setUid(Long.valueOf(accessToken.getUid()));
        Log.e("tag", "token= " + accessToken.getToken() + ",uid=" + accessToken.getUid());
        return user;
    }

    /**
     * 前后台切换添加连接状态检查
     *
     * @param isStart
     * @param roomId
     */
    public void isPushConnectionAvailable(boolean isStart, String roomId) {
        if (isStart) {//
            if (!WBIMLiveClient.getInstance().getConnectorManager().isPushConnectionAvailable()) {
                joinRoom(roomId);
            }
        }
    }

    /**
     * 更新用户，如果用户uid没有更新，则实际不调用该方法
     */
    public void updateUser() {
        UserModel userModel = getUser();
        String uid = userModel.getUid() + "";
        String lastUid = preferences.getString("last_init_uid", uid);

        if (!uid.equals(lastUid)) {
//            WBIMLiveClient.getInstance().updateUser(userModel);
            WBIMLiveClient.getInstance().onSwitchUser(AppContext.getAppContext(), userModel, getBundle());
        }
        preferences.edit().putString("last_init_uid", uid).apply();

    }

    public void registMsgCallBack() {
        isNeedCallBack = true;
    }

    public void unregistMsgCallBack() {
        isNeedCallBack = false;
    }

    /**
     * 加入房间
     *
     * @param roomId
     */
    public void joinRoom(String roomId) {
        Log.e("tag", "roomId= " + roomId);
        registMsgCallBack();
        JoinRoomRequest request = new JoinRoomRequest();
        request.setRoom_id(roomId);
        WBIMLiveClient.getInstance().getChatRoomManager().joinLiveRoom(request, new WBIMLiveValueCallBack<JoinRoomModel>() {
            @Override
            public void onError(int code, String desc, String requestId) {
                Log.e("tag", "onJoinRoomFailed:---code=" + code + ",desc=" + desc + ",requestId=" + requestId);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AppContext.getAppContext(), "加入房间失败了", Toast.LENGTH_LONG).show();

                    }
                });

            }

            @Override
            public void onSuccess(JoinRoomModel joinRoomModel, String requestId) {
                Log.e("tag", "onJoinRoomSuccess:  ,joinRoomModel=" + joinRoomModel.toString() + ",requestId=" + requestId);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AppContext.getAppContext(), "加入房间成功了", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    /**
     * 退出房间
     *
     * @param roomId
     */
    public void exitRoom(String roomId) {
        if (TextUtils.isEmpty(roomId)) return;
        ExitRoomRequest request = new ExitRoomRequest();
        request.setRoom_id(roomId);
        WBIMLiveClient.getInstance().getChatRoomManager().exitLiveRoom(request, new WBIMLiveValueCallBack<Integer>() {
            @Override
            public void onError(int i, String s, String s1) {
                Log.e("tag", "onExitRoomFailed:---code=" + i + ",desc=" + s + ",requestId=" + s1);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AppContext.getAppContext(), "退出房间失败了", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onSuccess(Integer integer, String s) {
                Log.e("tag", "onJoinRoomSuccess:  ,joinRoomModel=" + integer.toString() + ",requestId=" + s);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AppContext.getAppContext(), "退出房间成功了", Toast.LENGTH_LONG).show();
                        DispatchMessageEventBus.getDefault().post(MessageType.CUSTOM_EXIT_ROOM, "");
                    }
                });

            }
        });
        unregistMsgCallBack();
    }

    /**
     * 获取房间用户列表
     *
     * @param roomId
     * @param count
     * @param cursor
     * @param roleFilter
     * @param infoFilter
     */

    public void fetchLiveRoomUserList(String roomId, int count, int cursor, String[] roleFilter, String[] infoFilter) {
        FetchRoomUserListRequest request = new FetchRoomUserListRequest();
        request.setCount(count);
        request.setCursor(cursor);
        request.setRole_filter(roleFilter);
        request.setMember_info_filter(infoFilter);
        request.setRoom_id(roomId);
        WBIMLiveClient.getInstance().getChatRoomManager().fetchLiveRoomUserList(request, new WBIMLiveValueCallBack<RoomUserListModel>() {
            @Override
            public void onError(int i, String s, String s1) {

            }

            @Override
            public void onSuccess(RoomUserListModel roomUserListModel, String s) {

            }
        });
    }

    /**
     * 获取房间禁言成员列表
     *
     * @param roomId
     */
    public void fetchRoomForbiddenListRequest(String roomId) {
        FetchRoomForbiddenListRequest request = new FetchRoomForbiddenListRequest();
        request.setRoom_id(roomId);
        WBIMLiveClient.getInstance().getChatRoomManager().fetchLiveRoomForbiddenList(request, new WBIMLiveValueCallBack<ForbiddenUserListModel>() {
            @Override
            public void onError(int i, String s, String s1) {

            }

            @Override
            public void onSuccess(ForbiddenUserListModel forbiddenUserListModel, String s) {

            }
        });
    }

    /**
     * 发送消息
     *
     * @param roomId
     * @param content
     * @param offset
     * @param type
     */

    public void sendMsg(String roomId, String content, long offset, int type) {
        SendMsgRequest request = new SendMsgRequest();
        request.setContent(content);
        request.setOffset(offset);
        request.setRoom_id(roomId);
        request.setType(type);
        WBIMLiveClient.getInstance().getMsgManager().sendMessage(request, new WBIMLiveValueCallBack<PushMessageModel>() {
            @Override
            public void onError(int i, String s, String s1) {
                Log.e("tag", "onError: " + "s=" + s + ",s1=" + s1);
            }

            @Override
            public void onSuccess(PushMessageModel pushMessageModel, String s) {
                Log.e("tag", "onSuccess: " + pushMessageModel.toString());
            }
        });
    }

    /**
     * 点赞
     *
     * @param roomId
     * @param praiseNum
     */

    public void sendLike(String roomId, int praiseNum) {
        LikeRequest request = new LikeRequest();
        request.setRoom_id(roomId);
        request.setInc_praises(praiseNum);
        WBIMLiveClient.getInstance().getMsgManager().like(request, new WBIMLiveValueCallBack<Integer>() {
            @Override
            public void onError(int i, String s, String s1) {

            }

            @Override
            public void onSuccess(Integer integer, String s) {

            }
        });
    }

    /**
     * 关注主播
     *
     * @param roomId
     * @param ownerId
     * @param offset
     */

    public void focusAnchor(String roomId, long ownerId, long offset) {
        FollowAnchorRequest request = new FollowAnchorRequest();
        request.setOffset(offset);
        request.setOwner_id(ownerId);
        request.setRoom_id(roomId);
        WBIMLiveClient.getInstance().getMsgManager().followAnchor(request, new WBIMLiveValueCallBack<Integer>() {
            @Override
            public void onError(int i, String s, String s1) {

            }

            @Override
            public void onSuccess(Integer integer, String s) {

            }
        });
    }

    /**
     * 禁言／取消禁言
     *
     * @param room_id
     */

    public void setAllowUserChat(String room_id, String uid, String user_system, String nickname, String avatar, boolean isForbidMsg) {
        ForBidMsgRequest request = new ForBidMsgRequest();
        UserModel userModel = new UserModel();
        userModel.setUid(Long.parseLong(uid));
        userModel.setUser_system(user_system);
        userModel.setNickname(nickname);
        userModel.setAvatar(avatar);

        request.setMembers(new UserModel[]{userModel});
        request.setShut_time(isForbidMsg ? 24 * 3600 : 0);
        request.setRoom_id(room_id);
        WBIMLiveClient.getInstance().getMsgManager().forBidMessage(request, new WBIMLiveValueCallBack<Integer>() {
            @Override
            public void onError(int i, String s, String s1) {

            }

            @Override
            public void onSuccess(Integer integer, String s) {

            }
        });
    }

    /**
     * 分享
     *
     * @param room_id
     * @param usrName
     */

    public void share(String room_id, String usrName) {
        ShareRequest request = new ShareRequest();
        request.setRoom_id(room_id);
        request.setExtension("");
        request.setContent(usrName + "分享了直播");
        WBIMLiveClient.getInstance().getMsgManager().share(request, new WBIMLiveValueCallBack<Integer>() {
            @Override
            public void onError(int i, String s, String s1) {

            }

            @Override
            public void onSuccess(Integer integer, String s) {

            }
        });
    }

    /**
     * 加入购物车
     *
     * @param room_id
     * @param pname
     * @param usrName
     */

    public void addToCart(String room_id, String pname, String usrName) {
        try {
            AddToCartRequest request = new AddToCartRequest();
            request.setContent(usrName + "添加了" + pname + "到购物车");
            request.setExtension("");
            request.setRoom_id(room_id);
            WBIMLiveClient.getInstance().getMsgManager().addToCart(request, new WBIMLiveValueCallBack<Integer>() {
                @Override
                public void onError(int i, String s, String s1) {

                }

                @Override
                public void onSuccess(Integer integer, String s) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打赏
     *
     * @param room_id
     * @param userName
     * @param price
     */

    public void sendReward(String room_id, String userName, String price) {
        RewardRequest request = new RewardRequest();
        request.setExtension("");
        request.setRoom_id(room_id);
        WBIMLiveClient.getInstance().getMsgManager().reward(request, new WBIMLiveValueCallBack<Integer>() {
            @Override
            public void onError(int i, String s, String s1) {

            }

            @Override
            public void onSuccess(Integer integer, String s) {

            }
        });
    }


    public void onMsgCallBack(final int msgType, final String data, final int sourceMsgType) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.e("tag", "data newmessage= " + data);
                DispatchMessageEventBus.getDefault().post(msgType, data);
            }
        });
    }

    class InternalMessageListener implements WBIMLiveListener {

        @Override
        public boolean onNewMessage(final int msgType, final PushMessageModel model, String requestId) {

            if (null != model) {
                try {
                    int msgtype = 0;
                    if (null != mMsgTypeConverter) {
                        msgtype = mMsgTypeConverter.convertType(model.getMsg_type(), model.getSys_msg_type());
                    }
                    if (isNeedCallBack)
                        onMsgCallBack(msgtype, model.getRawData(), msgType);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

    }
}
