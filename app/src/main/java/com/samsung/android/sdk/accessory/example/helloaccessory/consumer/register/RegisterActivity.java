package com.samsung.android.sdk.accessory.example.helloaccessory.consumer.register;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.R;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.main.BaseActivity;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.models.BPM;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.models.MyMember;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.models.User;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {
    private Toolbar registertoolbar;
    private static final String REQUIRED = "Required";
    public static final String EXTRA_POST_KEY = "post_key";
    private DatabaseReference mDatabase;
    private EditText mTitleField,mBodyField;
    private Button mSubmit;
    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

    }
    private void init(){
        registertoolbar= findViewById(R.id.register_toolbar);
        setSupportActionBar(registertoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mTitleField = findViewById(R.id.field_name);
        mBodyField = findViewById(R.id.field_phone);
        mSubmit=findViewById(R.id.submitbutton);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void submitPost() {
        final String title = mTitleField.getText().toString();
        final String body = mBodyField.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
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
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(RegisterActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewMember(userId, user.username, title, body);
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }
    private void setEditingEnabled(boolean enabled) {
        mTitleField.setEnabled(enabled);
        mBodyField.setEnabled(enabled);
        if (enabled) {
            mSubmit.setVisibility(View.VISIBLE);
        } else {
            mSubmit.setVisibility(View.GONE);
        }
    }
    private void writeNewMember(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        MyMember mymember = new MyMember(userId, username, title, body,key);
        Map<String, Object> postValues = mymember.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/user-bpm/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

}
