package com.example.asmarasusanto.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {

    private Button add_room;
    private EditText nama_room;

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> room_list = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private String namauser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        add_room = (Button) findViewById(R.id.addroom);
        nama_room = (EditText) findViewById(R.id.namaroom);
        listView = (ListView) findViewById(R.id.room_list);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,room_list);

        listView.setAdapter(arrayAdapter);

        usernamerequest();

        add_room.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put(nama_room.getText().toString(), "");
                root.updateChildren(map);
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                room_list.clear();
                room_list.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RoomChatActivity.class);
                intent.putExtra("room_name", ((TextView)view).getText().toString());
                intent.putExtra("user_name",namauser);
                startActivity(intent);
            }
        });

    }

    private void usernamerequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter name : ");

        final EditText input = new EditText(this);

        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                namauser = input.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                usernamerequest();
            }
        });

        builder.show();
    }
}
