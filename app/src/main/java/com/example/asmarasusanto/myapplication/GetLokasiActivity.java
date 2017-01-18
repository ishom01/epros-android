package com.example.asmarasusanto.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetLokasiActivity extends AppCompatActivity {
    private static final String TAG = GetLokasiActivity.class.getSimpleName();
    private Button search;
    int PLACE_PICKER_REQUEST = 1;
    Context context = this;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_lokasi);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Intent intent;
        try {
            intent = builder.build((Activity) context);
            startActivityForResult(intent,PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }

//        search =(Button) findViewById(R.id.cari);
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//                Intent intent;
//                try {
//                    intent = builder.build((Activity) context);
//                    startActivityForResult(intent,PLACE_PICKER_REQUEST);
//                }catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e){
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST){
            if (resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(data, this);
                String latitude = String.format("%s", place.getLatLng().latitude);
                String longitude = String.format("%s", place.getLatLng().longitude);
                String alamat = String.format("%s", place.getAddress());
                db = new SQLiteHandler(getApplicationContext());

                Lokasi (latitude, longitude, alamat);
//                Toast.makeText(this, latitude, Toast.LENGTH_LONG).show();

            }
        }
    }

    private void Lokasi(final String latitude, final String longitude, final String alamat) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";


        HashMap<String, String> regis = db.getRegister();

        String name = regis.get("uid");

        Uri builturi = Uri.parse(AppConfig.URL_GET_LOKASI) .buildUpon()
                .appendQueryParameter("id",name).build();

        String  url = builturi.toString();

        StringRequest strReq = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        // Inserting row in users table

                        // Launch login activity
                        Intent intent = new Intent(GetLokasiActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
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
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("alamat", alamat);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
