package com.samsung.android.sdk.accessory.example.helloaccessory.consumer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.ServiceConnection;
import android.widget.Toast;

public class HeartCheck extends BaseActivity {
    private TextView checkrate;
    private boolean mIsBound = false;
    //private ListView mMessageListView;
    private ConsumerService mConsumerService = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_check);
        mIsBound = bindService(new Intent(HeartCheck.this, ConsumerService.class), mConnection, Context.BIND_AUTO_CREATE);
        if (mIsBound == true && mConsumerService != null) {
            if (mConsumerService.sendData("Hello Accessory!")) {
            } else {
                Toast.makeText(getApplicationContext(),"hello accessory" , Toast.LENGTH_LONG).show();
            }
        }
        init();


    }
    protected void init(){
        checkrate=(TextView)findViewById(R.id.textView);
    }
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mConsumerService = ((ConsumerService.LocalBinder) service).getService();
           // updateTextView("onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mConsumerService = null;
            mIsBound = false;
          //  updateTextView("onServiceDisconnected");
        }
    };
    public void showtextview(String value){//텍스트뷰에 onreceive에서 가져온값을 넣
        checkrate.setText(value);
    }

}
