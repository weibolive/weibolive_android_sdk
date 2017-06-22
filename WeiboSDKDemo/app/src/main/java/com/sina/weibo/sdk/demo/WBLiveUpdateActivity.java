package com.sina.weibo.sdk.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.demo.WBCreateCallback;
import com.weibo.live.WeiboLiveUpdate;

public class WBLiveUpdateActivity extends Activity {
    public static final String TAG = "weibo_live_update";
    private Oauth2AccessToken mAccessToken;
    private WeiboLiveUpdate mUpdateApi;
    private String title;
    private String summary;
    private String isPublished;
    private String imageUrl;
    private String isStop;
    private String replayUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wblive_update);

        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mUpdateApi = new WeiboLiveUpdate(getApplicationContext(),Constants.APP_KEY,mAccessToken);

        findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWidgetView();
                String alert = checkInputLegal();

                if (!alert.equals("")) {
                    new AlertDialog.Builder(WBLiveUpdateActivity.this).setMessage(alert)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
                }else if(mAccessToken == null || mAccessToken.getToken().equals("")){
                    new AlertDialog.Builder(WBLiveUpdateActivity.this).setMessage("请先授权登录")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
                }else{
                    mUpdateApi.setId(WBCreateCallback.LIVEID);
                    mUpdateApi.setTitle(title);
                    mUpdateApi.setSummary(summary);
                    mUpdateApi.setPublished(isPublished);
                    mUpdateApi.setImage(imageUrl);
                    mUpdateApi.setStop(isStop);
                    mUpdateApi.setReplayUrl(replayUrl);
                    mUpdateApi.UpdateLive(new RequestListener() {
                        @Override
                        public void onComplete(String s) {
                            new AlertDialog.Builder(WBLiveUpdateActivity.this).setMessage(s)
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    }).show();
                            Log.d(TAG,s);
                        }

                        @Override
                        public void onWeiboException(WeiboException e) {
                            new AlertDialog.Builder(WBLiveUpdateActivity.this).setMessage(e.getMessage())
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
    }

    private void setWidgetView(){
        EditText titleContent = (EditText)findViewById(R.id.titleUpdate);
        EditText summaryContent = (EditText)findViewById(R.id.summaryUpdate);
        EditText publishContent = (EditText)findViewById(R.id.publishUpdate);
        EditText imageContent = (EditText)findViewById(R.id.imageUpdate);
        EditText stopContent = (EditText)findViewById(R.id.stopUpdate);
        EditText replayContent = (EditText)findViewById(R.id.replayUrlUpdate);
        title = titleContent.getText().toString();
        summary = summaryContent.getText().toString();
        isPublished = publishContent.getText().toString();
        isPublished = isPublished.equals("1")?"1":"2";
        imageUrl = imageContent.getText().toString();
        isStop = stopContent.getText().toString();
        isStop = isStop.equals("1")?"1":"0";
        replayUrl = replayContent.getText().toString();
    }

    private String checkInputLegal(){
        String alertS = "";
        if(WBCreateCallback.LIVEID.equals("")){
            alertS = "You need to create a livestream first,";
        }
        if(summary.length() >=260){
            alertS += "summary is too long.";
        }
        return alertS;
    }
}
