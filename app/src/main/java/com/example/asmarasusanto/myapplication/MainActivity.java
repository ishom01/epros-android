package com.example.asmarasusanto.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private static final String TAG = NextPerusahaanActivity.class.getSimpleName();
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_profile = (Button) findViewById(R.id.Profile);
        Button btn_proposal = (Button) findViewById(R.id.Proposal);
        Button btn_chat = (Button) findViewById(R.id.chat);
        Button btn_teman = (Button) findViewById(R.id.Teman);
        Button logout = (Button) findViewById(R.id.logout);


        db = new SQLiteHandler(getApplicationContext());

        getUser();


        btn_profile.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getUser();
            }
        });

        btn_proposal.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,
                        UploadProposalActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_teman.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,ActivityTeman.class);
                startActivity(intent);
                finish();
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,
                        ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                session = new SessionManager(getApplicationContext());
                session.setLogin(false);

                db.deleteLogin();
                db.deleteUsers();

                // Launching the login activity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    private void getUser() {
        String tag_string_req = "req_register";


        HashMap<String, String> login = db.getLogin();

        String name = login.get("uid");

        Uri builturi = Uri.parse(AppConfig.URL_GET_USER) .buildUpon()
                .appendQueryParameter("id",name).build();

        String  url = builturi.toString();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String username = user.getString("username");
                        String email = user.getString("email");
                        String password = user.getString("password");
                        String authkey = user.getString("authKey");
                        String statusemail = user.getString("statusemail");
                        String statusbukti = user.getString("statusbukti");
                        String statussms = user.getString("statussms");
                        String statusupdate = user.getString("statusupdate");
                        String level = user.getString("level");
                        String nama = user.getString("nama");
                        String phone = user.getString("phone");
                        String alamat = user.getString("alamat");
                        String tanggal = user.getString("tanggal");
                        String cabang = user.getString("cabang");
                        String gambar = user.getString("gambar");
                        String latitude = user.getString("latitude");
                        String longitude = user.getString("longitude");
                        String acara = user.getString("acara");
                        String menerima = user.getString("menerima");
                        String jangka_waktu = user.getString("jangka_Waktu");
                        String timbal_balik = user.getString("timbal_balik");
                        String sponsor = user.getString("sponsor");


                        // Inserting row in users table
                        db.addUser(uid,username,password,authkey,level,statusemail, statussms,statusbukti,statusupdate,
                                nama, phone, alamat, tanggal, cabang, gambar, latitude,longitude, acara,
                                menerima, jangka_waktu, timbal_balik, sponsor) ;

                        db.deleteRegister();

//                        // Launch login activity
//                        Intent intent = new Intent(MainActivity.this,
//                                ProfileActivity.class);
//                        startActivity(intent);
//                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
