package com.sina.weibo.sdk.demo.videolive;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.demo.AppContext;
import com.sina.weibo.sdk.demo.R;
import com.sina.weibo.sdk.demo.WBCreateCallback;
import com.sina.weibo.sdk.demo.dispatchmessage.DispatchMessageEventBus;
import com.sina.weibo.sdk.demo.dispatchmessage.MessageSubscribe;
import com.sina.weibo.sdk.demo.dispatchmessage.MessageType;

/**
 * Created by bj-m-206333a on 2017/6/13.
 */

public class PlayActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int REQUEST_READ_PHONE_STATE = 100;

    TextView tv_show_content;
    EditText edit_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        DispatchMessageEventBus.getDefault().register(this);

        tv_show_content = (TextView) findViewById(R.id.tv_show_content);
        edit_content = (EditText) findViewById(R.id.edit_content);
        findViewById(R.id.btn_join_room).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(PlayActivity.this, Manifest.permission.READ_PHONE_STATE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PlayActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                } else {
                    //TODO
//                    Toast.makeText(PlayActivity.this, "拒绝该权限，就无法加入直播间了", Toast.LENGTH_LONG).show();
                    LiveMsgManager.mInstance.joinRoom(WBCreateCallback.ROOMID);

                }
            }
        });
        findViewById(R.id.btn_exit_room).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveMsgManager.mInstance.exitRoom(WBCreateCallback.ROOMID);
            }
        });
        findViewById(R.id.btn_send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_content.getText().toString().trim())) {
                    Toast.makeText(AppContext.getAppContext(), "聊天内容不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                LiveMsgManager.mInstance.sendMsg(WBCreateCallback.ROOMID, edit_content.getText().toString(), 100, 0);
                edit_content.setText("");
            }
        });
        findViewById(R.id.btn_send_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveMsgManager.mInstance.sendLike(WBCreateCallback.ROOMID, 0);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    LiveMsgManager.mInstance.joinRoom(WBCreateCallback.ROOMID);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 收到赞消息
     *
     * @param object 消息
     */
    @MessageSubscribe(messageType = MessageType.FAVOR, classType = Object.class)
    public void onReceiveCommentPraise(Object object) {
        tv_show_content.setText(object.toString());
    }

    /**
     * 收到评论消息
     *
     * @param object 消息
     */
    @MessageSubscribe(messageType = MessageType.COMMENT, classType = Object.class)
    public void onReceiveComment(Object object) {
        tv_show_content.setText(object.toString());
    }

    /**
     * 收到退出房间消息
     *
     * @param object 消息
     */
    @MessageSubscribe(messageType = MessageType.CUSTOM_EXIT_ROOM, classType = Object.class)
    public void onReceiveExitRoom(Object object) {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DispatchMessageEventBus.getDefault().unregister(this);
    }
}
