package com.samsung.android.sdk.accessory.example.helloaccessory.consumer.register;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.R;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.main.BaseActivity;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.models.MyMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<MyMember> result;
    private UserAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FloatingActionButton fab;
    private TextView emptyText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_list);
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("user-member").child(getUid());
        Log.d("regia",getUid());

        emptyText=findViewById(R.id.text_no_data);
        result = new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.user_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        fab=findViewById(R.id.fab_new_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterListActivity.this,RegisterActivity.class));
            }
        });
        recyclerView.setLayoutManager(llm);


        adapter=new UserAdapter(result);
        recyclerView.setAdapter(adapter);
        updateList();
        checkIfEmpty();
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
         switch(item.getItemId()){
             case 0:
                 removeUser(item.getGroupId());
                 break;
             case 1:
                 changeUser(item.getGroupId());
                 break;
         }
         return super.onContextItemSelected(item);
    }

    private void updateList(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    result.add(dataSnapshot.getValue(MyMember.class));
                    adapter.notifyDataSetChanged();
                    checkIfEmpty();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MyMember member=dataSnapshot.getValue(MyMember.class);
                int index= getItemIndex(member);
                result.set(index,member);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                MyMember member=dataSnapshot.getValue(MyMember.class);

                int index= getItemIndex(member);
                result.remove(index);
                adapter.notifyItemChanged(index);
                checkIfEmpty();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private int getItemIndex(MyMember member){
        int index=-1;
        for(int i =0;i<result.size();i++){
            if (result.get(i).key.equals(member.key)) {
                index = i;
                break;
            }
        }
        return index;
    }
    private void createResult(){
        for(int i=0;i<20;i++){
            result.add(new MyMember("asd","asd","asd","asd","asd"));
        }
    }
    private void removeUser(int position){
        reference.child(result.get(position).key).removeValue();
    }
    private void changeUser(int position){
        MyMember member = result.get(position);
        member.phonenumber="01093617230";
        Map<String,Object> memberValues=member.toMap();
        Map<String,Object> newMember= new HashMap<>();

        newMember.put(member.key,memberValues);
        reference.updateChildren(newMember);
    }
    private void checkIfEmpty(){
        if(result.size()==0){
            recyclerView.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        }
    }
}
