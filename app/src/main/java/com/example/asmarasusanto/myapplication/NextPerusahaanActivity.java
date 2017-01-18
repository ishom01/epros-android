package com.example.asmarasusanto.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NextPerusahaanActivity extends AppCompatActivity {

    private static final String TAG = NextPerusahaanActivity.class.getSimpleName();;
    private EditText Acara;
    private EditText Menerima;
    private EditText JangkaWaktu;
    private EditText TimbalBalik;
    private EditText Sponsor;
    private Button Save;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_perusahaan);

        Acara = (EditText) findViewById(R.id.acara);
        Menerima = (EditText) findViewById(R.id.menerima);
        JangkaWaktu = (EditText) findViewById(R.id.jangka);
        TimbalBalik = (EditText) findViewById(R.id.balik);
        Sponsor = (EditText) findViewById(R.id.sponsor);
        Save = (Button) findViewById(R.id.simpan);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        SessionManager session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        Save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String acara = Acara.getText().toString().trim();
                String menerima = Menerima.getText().toString().trim();
                String jangkawaktu = JangkaWaktu.getText().toString().trim();
                String timbalbalik = TimbalBalik.getText().toString().trim();
                String sponsor = Sponsor.getText().toString().trim();

                if (!acara.isEmpty() && !menerima.isEmpty() && !jangkawaktu.isEmpty() && !timbalbalik.isEmpty() && !sponsor.isEmpty()) {
                    Register(acara, menerima, jangkawaktu, timbalbalik, sponsor);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }
    private void Register(final String acara, final String menerima, final String jangkawaktu, final String timbalbalik, final String sponsor) {

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        HashMap<String, String> regis = db.getRegister();

        String name = regis.get("uid");

        Uri builturi = Uri.parse(AppConfig.URL_NEXT_PERUSAHAAN) .buildUpon()
                .appendQueryParameter("id",name).build();

        String  url = builturi.toString();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL

                        // Launch login activity
                        Intent intent = new Intent(NextPerusahaanActivity.this,
                                GetLokasiActivity.class);
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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("acara", acara);
                params.put("menerima", menerima);
                params.put("jangka_waktu", jangkawaktu);
                params.put("timbal_balik", timbalbalik);
                params.put("sponsor", sponsor);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
