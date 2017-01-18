package com.example.asmarasusanto.myapplication;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RoomChatActivity extends AppCompatActivity {


    private ArrayList<Message> mMessages;
    private MessagesAdapter mAdapter;
    private Button send;
    private EditText input_msg;
    private TextView conversation;
    private String username;
    private String room_name;
    private DatabaseReference root;
    private String tmp;
    private ListView mListView;
    private String mRecipient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);

        mListView = (ListView)findViewById(R.id.message_list);
        mMessages = new ArrayList<>();
        mAdapter = new MessagesAdapter(this, mMessages);
        mListView.setAdapter(mAdapter);
        
        send = (Button) findViewById(R.id.kirim);
        input_msg = (EditText) findViewById(R.id.pesaninput);
//        conversation = (TextView) findViewById(R.id.con);
//
        username = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();



        setTitle("Room - "+room_name);

        root = FirebaseDatabase.getInstance().getReference().child(room_name);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                tmp = root.push().getKey();
                root.updateChildren(map);
                DatabaseReference message_root = root.child(tmp);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name", username);
                map2.put("msg", input_msg.getText().toString());

                message_root.updateChildren(map2);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String chat_msg, chat_username;
    private void append_chat_conversation(DataSnapshot dataSnapshot) {
//        Set<String> set = new HashSet<String>();
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){
            Message pesan = new Message();
            chat_msg = (String) (((DataSnapshot) i.next()).getValue());
            chat_username = (String) ((((DataSnapshot) i.next()).getValue()));
            pesan.setSender(chat_username);
            pesan.setText(chat_msg);
            mAdapter.add(pesan);
        }
    }

    private class MessagesAdapter extends ArrayAdapter<Message> {

        MessagesAdapter(RoomChatActivity roomChatActivity, ArrayList<Message> mMessages) {
            super(RoomChatActivity.this, R.layout.message, R.id.message,mMessages);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            Message message = getItem(position);

            TextView nameView = (TextView)convertView.findViewById(R.id.message);
            nameView.setText(message.getText());

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)nameView.getLayoutParams();

            int sdk = Build.VERSION.SDK_INT;
            if (message.getSender().equals(username)){
                if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                    nameView.setBackground(getDrawable(R.drawable.bubble_right_green));
                } else{
                    nameView.setBackgroundDrawable(getDrawable(R.drawable.bubble_right_green));
                }
                layoutParams.gravity = Gravity.RIGHT;
            }else{
                if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                    nameView.setBackground(getDrawable(R.drawable.bubble_left_gray));
                } else{
                    nameView.setBackgroundDrawable(getDrawable(R.drawable.bubble_left_gray));
                }
                layoutParams.gravity = Gravity.LEFT;
            }

            nameView.setLayoutParams(layoutParams);


            return convertView;
        }
    }
}
