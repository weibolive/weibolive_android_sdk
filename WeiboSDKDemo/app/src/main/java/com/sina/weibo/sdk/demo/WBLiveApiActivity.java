package com.sina.weibo.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.graphics.Bitmap;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.weibo.live.WeiboLiveCreate;
import com.weibo.live.WeiboLiveDelete;
import com.weibo.live.WeiboLiveGetInfo;
import com.weibo.live.WeiboLiveUpdate;
import com.weibo.live.WeiboLiveInteract;
import com.sina.weibo.sdk.demo.WBCreateCallback;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;

import flexjson.JSONDeserializer;

/**
 * Created by jinya3 on 2017/5/15.
 */

public class WBLiveApiActivity extends Activity{
    public static final String TAG = "weibo_live";

    private Oauth2AccessToken mAccessToken;
    //private WeiboLiveCreate mCreateApi;
    //private WeiboLiveUpdate mUpdateApi;
    private WeiboLiveDelete mDeleteApi;
    private WeiboLiveGetInfo mGetInfoApi;
    private WeiboLiveInteract mInteractApi;
//    private String title = "I am a test-title";
//    private String width = "320";
//    private String height = "640";
//    private String summary = "Test summary from weibo live com.weibo.live.sdk.demo";
//    private String isPublished = "0";
    private String imageUrl ="";
//    private String isReplay = "1";
//    private String isPanoLive = "0";
//    private String liveId = "230916b08204ae3f5d803a6a75d72ff675940e";
//    private String rtmpUrl = "rtmp://ps.live.weibo.com/alicdn/b08204ae3f5d803a6a75d72ff675940e?auth_key=1495710112-0-0-11e32b9ffa0f8116f7906d65151f8640";
//    private String roomId = "1042097:b08204ae3f5d803a6a75d72ff675940e";
//    private String isStop = "0";
//    private String replayUrl = "";
    private String isReturnDetail = "0";
    private long uid = 1596255485;
//    private String pageUrl;     //直播落地页
//    private int liveStatus;     //直播状态
//    private String createTime;  //直播创建时间
//    private int liveViews;      //实时在线人数
//    private int totalViews;
//    private int totalStars;
//    private byte[] pic = new byte[10*1024*1024];
//    private int isMark = 1;
//    private int isOriSupport = 1;
    private long timeStamp = new Date().getTime();
    private int msgType = 1;
    private String msgContent = "testMsgContent";
    private String nickName = "testNickName";
    private String avatarUrl = "http://ww3.sinaimg.cn/thumbnail/946308c5jw1dv288whtylj.jpg";
    private String sign = "123456";
    private String extension = "";
    private long offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        //mCreateApi = new WeiboLiveCreate(getApplicationContext(),Constants.APP_KEY,mAccessToken);
        //mUpdateApi = new WeiboLiveUpdate(getApplicationContext(),Constants.APP_KEY,mAccessToken);
        mDeleteApi = new WeiboLiveDelete(getApplicationContext(),Constants.APP_KEY,mAccessToken);
        mGetInfoApi = new WeiboLiveGetInfo(getApplicationContext(),Constants.APP_KEY,mAccessToken);
        mInteractApi = new WeiboLiveInteract(getApplicationContext(),Constants.APP_KEY,mAccessToken);
        //mCreateApi.setAc(WBLiveApiActivity.this);

        this.findViewById(R.id.createLive).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBLiveApiActivity.this,WBLiveCreateActivity.class));
            }
        });

        this.findViewById(R.id.updateLive).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBLiveApiActivity.this,WBLiveUpdateActivity.class));
            }
        });

        findViewById(R.id.deleteLive).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAccessToken != null && !mAccessToken.getToken().equals("")){
                    mDeleteApi.setId(WBCreateCallback.LIVEID);
                    mDeleteApi.deleteLive(new RequestListener() {
                        @Override
                        public void onComplete(String s) {
                            Log.d(TAG,s);
                            new AlertDialog.Builder(WBLiveApiActivity.this).setMessage(s)
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    }).show();
                        }

                        @Override
                        public void onWeiboException(WeiboException e) {
                            new AlertDialog.Builder(WBLiveApiActivity.this).setMessage(e.getMessage())
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    }).show();
                            Log.d(TAG,e.getMessage());
                        }
                    });
                }
            }
        });

        findViewById(R.id.getInfo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAccessToken != null && !mAccessToken.getToken().equals("")){
                    mGetInfoApi.setId(WBCreateCallback.LIVEID);
                    mGetInfoApi.setDetail(isReturnDetail);
                    mGetInfoApi.getInfoLive(new RequestListener() {
                        @Override
                        public void onComplete(String s) {
                            new AlertDialog.Builder(WBLiveApiActivity.this).setMessage(s)
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    }).show();
                        }

                        @Override
                        public void onWeiboException(WeiboException e) {
                            new AlertDialog.Builder(WBLiveApiActivity.this).setMessage(e.getMessage())
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    }).show();
                        }
                    });
                }
            }
        });

        /*findViewById(R.id.interact).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInteractApi != null && !mAccessToken.getToken().equals("")){
                    mInteractApi.setRoomId(WBCreateCallback.ROOMID);
                    mInteractApi.setTimeStamp(timeStamp);
                    mInteractApi.setMsgType(msgType);
                    mInteractApi.setMsgContent(msgContent);
                    mInteractApi.setUid(uid);
                    mInteractApi.setNickName(nickName);
                    mInteractApi.setAvatar(avatarUrl);
                    //mInteractApi.setSign(sign);
                    mInteractApi.setExtension(extension);
                    mInteractApi.setOffset(offset);
                    mInteractApi.imUpload(new RequestListener() {
                        @Override
                        public void onComplete(String s) {

                        }

                        @Override
                        public void onWeiboException(WeiboException e) {

                        }
                    });
                }
            }
        });*/
    }

    private static class createBean<T> {
        //private T result;
        private T id;
        private T url;
        private T room_id;

        public T getId() {
            return id;
        }

        public void setId(T id) {
            this.id = id;
        }

        public T getUrl() {
            return url;
        }

        public void setUrl(T url) {
            this.url = url;
        }

        public T getRoom_id() {
            return room_id;
        }

        public void setRoom_id(T room_id) {
            this.room_id = room_id;
        }
    }
}
