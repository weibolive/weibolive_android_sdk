package com.sina.weibo.sdk.demo;

import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.graphics.Bitmap;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.weibo.live.WeiboLiveCreate;
import com.sina.weibo.sdk.demo.WBCreateCallback;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;

import flexjson.JSONDeserializer;

public class WBLiveCreateActivity extends Activity {
    public static final String TAG = "weibo_live_create";
    private Oauth2AccessToken mAccessToken;
    private WeiboLiveCreate mCreateApi;
    private String title;
    private String width;
    private String height;
    private String summary = null;
    private String isPublished = null;
    private String imageUrl = null;
    private String isReplay = null;
    private String isPanoLive = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wblive_create);

        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mCreateApi = new WeiboLiveCreate(getApplicationContext(),Constants.APP_KEY,mAccessToken);
        mCreateApi.setAc(WBLiveCreateActivity.this);


        findViewById(R.id.createButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setWidgetView();
                String alert = checkInputLegal();
                if(!alert.equals("")){
                    new AlertDialog.Builder(WBLiveCreateActivity.this).setMessage(alert)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
                }else if(mAccessToken == null || mAccessToken.getToken().equals("")){
                    new AlertDialog.Builder(WBLiveCreateActivity.this).setMessage("请先授权登录")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
                }else{
                    mCreateApi.setTitle(title);
                    mCreateApi.setHeight(height);
                    mCreateApi.setWidth(width);
                    mCreateApi.setSummary(summary);
                    mCreateApi.setPublished(isPublished);
                    mCreateApi.setImage(imageUrl);
                    mCreateApi.setReplay(isReplay);
                    mCreateApi.setPanoLive(isPanoLive);
                    mCreateApi.createLive(new RequestListener() {
                        @Override
                        public void onComplete(String s) {
                            Log.d(TAG, s);
                            HashMap<String, String> result = new HashMap<String, String>();
                            try {
                                result = new JSONDeserializer<HashMap<String, String>>().deserialize(s);
                                for (HashMap.Entry<String, String> entry : result.entrySet()) {
                                    if (entry.getKey().equals("id")) {
                                        WBCreateCallback.LIVEID = entry.getValue();
                                    } else if (entry.getKey().equals("room_id")) {
                                        WBCreateCallback.ROOMID = entry.getValue();
                                    } else if (entry.getKey().equals("url")) {
                                        WBCreateCallback.URL = entry.getValue();
                                    }
                                }
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                            new AlertDialog.Builder(WBLiveCreateActivity.this).setMessage(s)
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    }).show();
                        }

                        @Override
                        public void onWeiboException(WeiboException e) {
                            new AlertDialog.Builder(WBLiveCreateActivity.this).setMessage(e.getMessage())
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    }).show();
                            Log.d(TAG,e.getMessage());
                            Log.d(TAG,e.getStackTrace().toString());
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

    }

    private void setWidgetView(){
        EditText titleContent = (EditText)findViewById(R.id.titleContent);
        EditText heightContent = (EditText)findViewById(R.id.heightContent);
        EditText widthContent = (EditText)findViewById(R.id.widthContent);
        EditText summaryContent = (EditText)findViewById(R.id.summaryContent);
        EditText publishContent = (EditText)findViewById(R.id.publishContent);
        EditText imageContent = (EditText)findViewById(R.id.imageUrl);
        EditText replayContent = (EditText)findViewById(R.id.replayContent);
        EditText panoContent = (EditText)findViewById(R.id.panoContent);
        title = titleContent.getText().toString();
        height = heightContent.getText().toString();
        width = widthContent.getText().toString();
        summary = summaryContent.getText().toString();
        isPublished = publishContent.getText().toString();
        isPublished = isPublished.equals("1")?"1":"0";
        imageUrl = imageContent.getText().toString();
        isReplay = replayContent.getText().toString();
        isReplay = isReplay.equals("0")?"0":"1";
        isPanoLive = panoContent.getText().toString();
        isPanoLive = isPanoLive.equals("1")?"1":"0";
    }

    private String checkInputLegal(){
        String alertS = "";
        if(title.isEmpty()||width.isEmpty()||height.isEmpty()){
            alertS = "necessary value is empty,";
        }
        if(summary.length() >=260){
            alertS += "summary is too long.";
        }
        return alertS;

    }
}
