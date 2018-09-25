/*
 * Copyright (c) 2015 Samsung Electronics Co., Ltd. All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that 
 * the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice, 
 *       this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright notice, 
 *       this list of conditions and the following disclaimer in the documentation and/or 
 *       other materials provided with the distribution. 
 *     * Neither the name of Samsung Electronics Co., Ltd. nor the names of its contributors may be used to endorse or 
 *       promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.samsung.android.sdk.accessory.example.helloaccessory.consumer.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;


import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.LinearProgressButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.R;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.models.User;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.register.RegisterListActivity;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.util.ProgressGenerator;

import java.util.Timer;
import java.util.TimerTask;


public class ConsumerActivity extends BaseActivity {
    private boolean mIsBound = false;
    //private ListView mMessageListView;
    private ConsumerService mConsumerService = null;
    private Button checkheart, register;
    private LinearProgressButton btnMorph1;
    private static TextView heartrate;
    private int mMorphCounter1 = 0;
    private static int counter=0;
    private DatabaseReference mDatabase;
    private TimerTask tt;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mIsBound = bindService(new Intent(ConsumerActivity.this, ConsumerService.class), mConnection, Context.BIND_AUTO_CREATE);
        btnMorph1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMorphButton1Clicked(btnMorph1);
            }
        });


        morphToSquare(btnMorph1, 0);

    }
    protected void init() {
        //  circularbutton=findViewById(R.id.progressbutton);
        btnMorph1 = (LinearProgressButton) findViewById(R.id.btnMorph1);
        //checkheart= findViewById(R.id.checkheart);
        heartrate = (TextView) findViewById(R.id.heartrate);
        register = findViewById(R.id.register);

    }
    @Override
    protected void onDestroy(){
        if(mIsBound){
            mIsBound = bindService(new Intent(ConsumerActivity.this, ConsumerService.class), mConnection, Context.BIND_AUTO_CREATE);
        }
        super.onDestroy();
    }
    private void onMorphButton1Clicked(final LinearProgressButton btnMorph) {
        if (mMorphCounter1 == 0) {
            if (mIsBound == true && mConsumerService != null) {
                    if(mConsumerService.findPeers()){
                        mMorphCounter1++;
                        simulateProgress1(btnMorph);

                        tt=new TimerTask() {
                            @Override
                            public void run() {
                                if(mConsumerService.sendData("Hello Accessory!")){
                                    Log.d("CA","ok");
                                }else {
                                    Log.d("CA","fail");
                                }
                                counter++;
                            }
                        };
                        Timer timer= new Timer();
                        timer.schedule(tt,0,1800*60);

                    }
            }else{
                        Toast.makeText(getApplicationContext(), "연결실패 블루투스 연결, gear연결 확인해주세요", Toast.LENGTH_LONG).show();
                    }

           // morphToSquare(btnMorph, integer(R.integer.mb_animation));
        } else if (mMorphCounter1 == 1) {
            if (mIsBound == true && mConsumerService != null) {
                if (mConsumerService.closeConnection() == false) {
                    mMorphCounter1 = 0;
                    updateTextView("연결이 끊어졌습니다.");
                    Log.d("aaa","disconnect");
                    morphToSquare(btnMorph, integer(R.integer.mb_animation));
                    Toast.makeText(getApplicationContext(), "연결해제", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
/*
    @Override
    protected void onDestroy() {
        // Clean up connections
        if (mIsBound == true && mConsumerService != null) {
            if (mConsumerService.closeConnection() == false) {

            }
        }
        // Un-bind service
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
        super.onDestroy();
    }
*/
    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.register: {
                startActivity(new Intent(ConsumerActivity.this, RegisterListActivity.class));
                break;
            }
            default:
        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mConsumerService = ((ConsumerService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mConsumerService = null;
            mIsBound = false;
        }
    };



    private void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .width(dimen(R.dimen.mb_width_70))
                .height(dimen(R.dimen.mb_height_30))
                .color(color(R.color.backgroundcolor))
                .colorPressed(color(R.color.backgroundcolor))
                .text(getString(R.string.mb_ready));
        btnMorph.morph(square);
    }

    private void morphToSuccess(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer(R.integer.mb_animation))
                .cornerRadius(dimen(R.dimen.mb_height_30))
                .width(dimen(R.dimen.mb_height_30))
                .height(dimen(R.dimen.mb_height_30))
                .color(color(R.color.mb_green))
                .colorPressed(color(R.color.mb_green_dark))
                .icon(R.drawable.ic_done);
        btnMorph.morph(circle);
    }

    private void morphToFailure(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_red))
                .colorPressed(color(R.color.mb_red_dark))
                .icon(R.drawable.ic_lock);
        btnMorph.morph(circle);
    }


    private void simulateProgress1(@NonNull final LinearProgressButton button) {
        int progressColor = color(R.color.mb_purple);
        int color = color(R.color.mb_gray);
        int progressCornerRadius = dimen(R.dimen.mb_corner_radius_4);
        int width = dimen(R.dimen.mb_width_70);
        int height = dimen(R.dimen.mb_height_30);
        int duration = integer(R.integer.mb_animation);

        ProgressGenerator generator = new ProgressGenerator(new ProgressGenerator.OnCompleteListener() {
            @Override
            public void onComplete() {
                morphToSuccess(button);
                button.unblockTouch();
            }
        });
        button.blockTouch(); // prevent user from clicking while button is in progress
        button.morphToProgress(color, progressColor, progressCornerRadius, width, height, duration);
        generator.start(button);
    }

    public static void updateTextView(final String str) {
        heartrate.setText(str);
    }
    private void transserver(){
        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e("connectserver", "User " + userId + " is unexpectedly null");
                            Toast.makeText(ConsumerActivity.this,
                                    "유저 오류 재로그인 부탁드립니다.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            showProgressDialog();
                            // Write new post
                            Log.d("aaaa","good");
                            Future uploading = Ion.with(ConsumerActivity.this)
                                    .load("http://35.171.86.176:8080/upload/"+getUid())
                                    .setMultipartParameter("author",user.username)
                                    .asString()
                                    .withResponse()
                                    .setCallback(new FutureCallback<Response<String>>() {
                                        @Override
                                        public void onCompleted(Exception e, Response<String> result) {
                                            Toast.makeText(ConsumerActivity.this,
                                                    "전송완료.",
                                                    Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("connectCancel", "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        Log.d("woo","2 ok");

        //cursor.close();

    }
}

