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

package com.samsung.android.sdk.accessory.example.helloaccessory.consumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.LinearProgressButton;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.util.ProgressGenerator;


public class ConsumerActivity extends BaseActivity {
    private boolean mIsBound = false;
    //private ListView mMessageListView;
    private ConsumerService mConsumerService = null;
    private Button checkheart, register;
    private LinearProgressButton btnMorph1;
    private int mMorphCounter1 = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButton();
        mIsBound = bindService(new Intent(ConsumerActivity.this, ConsumerService.class), mConnection, Context.BIND_AUTO_CREATE);
        btnMorph1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMorphButton1Clicked(btnMorph1);
            }
        });

        morphToSquare(btnMorph1, 0);
    }
    protected void initButton(){
      //  circularbutton=findViewById(R.id.progressbutton);
        btnMorph1=(LinearProgressButton)findViewById(R.id.btnMorph1);
        checkheart= findViewById(R.id.checkheart);
        register=findViewById(R.id.register);
    }
    private void onMorphButton1Clicked(final LinearProgressButton btnMorph) {
        if (mMorphCounter1 == 0) {
            if (mIsBound == true && mConsumerService != null) {
                mConsumerService.findPeers();
                Toast.makeText(getApplicationContext(), "연결완료", Toast.LENGTH_LONG).show();
                mMorphCounter1++;
                morphToSquare(btnMorph, integer(R.integer.mb_animation));
            }

           // morphToSquare(btnMorph, integer(R.integer.mb_animation));
        } else if (mMorphCounter1 == 1) {
            if (mIsBound == true && mConsumerService != null) {
                if (mConsumerService.closeConnection() == false) {
                    Toast.makeText(getApplicationContext(), "연결해제", Toast.LENGTH_LONG).show();
                    mMorphCounter1 = 0;
                    simulateProgress1(btnMorph);
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
            case R.id.checkheart: {
                /*
                if (mIsBound == true && mConsumerService != null) {
                    if (mConsumerService.closeConnection() == false) {
                        updateTextView("Disconnected");
                        Toast.makeText(getApplicationContext(), "AlreadyDisconnected", Toast.LENGTH_LONG).show();
                        mMessageAdapter.clear();
                    }
                }
                break;
                */
                if(mIsBound == true && mConsumerService != null){
                    startActivity(new Intent(ConsumerActivity.this, HeartCheck.class));
                }else{
                    Toast.makeText(getApplicationContext(),"기기 연결을 해야합니다" , Toast.LENGTH_LONG).show();
                }

            }
            case R.id.register: {

                break;
            }
            default:
        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mConsumerService = ((ConsumerService.LocalBinder) service).getService();
          //  updateTextView("onServiceConnected");
            Toast.makeText(mConsumerService, "Connect Complete", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mConsumerService = null;
            mIsBound = false;
       //     updateTextView("onServiceDisconnected");
            Toast.makeText(mConsumerService, "DisConnect Complete", Toast.LENGTH_SHORT).show();
        }
    };


    private void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .width(dimen(R.dimen.mb_width_120))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_blue))
                .colorPressed(color(R.color.mb_blue_dark))
                .text(getString(R.string.mb_ready));
        btnMorph.morph(square);
    }

    private void morphToSuccess(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer(R.integer.mb_animation))
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
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
        int width = dimen(R.dimen.mb_width_200);
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

    }
    /*
    private class MessageAdapter extends BaseAdapter {
        private static final int MAX_MESSAGES_TO_DISPLAY = 20;
        private List<Message> mMessages;

        public MessageAdapter() {
            mMessages = Collections.synchronizedList(new ArrayList<Message>());
        }

        void addMessage(final Message msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mMessages.size() == MAX_MESSAGES_TO_DISPLAY) {
                        mMessages.remove(0);
                        mMessages.add(msg);
                    } else {
                        mMessages.add(msg);
                    }
                    notifyDataSetChanged();
                 //   mMessageListView.setSelection(getCount() - 1);
                }
            });
        }

        void clear() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMessages.clear();
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getCount() {
            return mMessages.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View messageRecordView = null;
            if (inflator != null) {
                messageRecordView = inflator.inflate(R.layout.message, null);
                TextView tvData = (TextView) messageRecordView.findViewById(R.id.tvData);
                Message message = (Message) getItem(position);
                tvData.setText(message.data);
            }
            return messageRecordView;
        }
    }

    private static final class Message {
        String data;

        public Message(String data) {
            super();
            this.data = data;
        }
    }

}
*/