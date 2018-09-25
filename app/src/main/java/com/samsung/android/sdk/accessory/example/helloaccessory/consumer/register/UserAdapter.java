package com.samsung.android.sdk.accessory.example.helloaccessory.consumer.register;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.R;
import com.samsung.android.sdk.accessory.example.helloaccessory.consumer.models.MyMember;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private List<MyMember> list;
    public UserAdapter(List<MyMember> list){
        this.list=list;
    }
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        MyMember member=list.get(position);
        holder.name.setText(member.name+"");
        holder.phoneNumber.setText(member.phonenumber);

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
                menu.add(holder.getAdapterPosition(),0,0,"보호자 삭제");
                menu.add(holder.getAdapterPosition(),1,0,"보호자 수정");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView phoneNumber, name;
        public UserViewHolder(View itemView){

            super(itemView);
            phoneNumber=(TextView)itemView.findViewById(R.id.phone_number);
            name=(TextView)itemView.findViewById(R.id.text_name);
        }
    }
}
