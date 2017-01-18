package com.example.asmarasusanto.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityTeman extends Activity {

    private static final String TAG = ActivityTeman.class.getSimpleName();

    // Movies json url
    private static final String url = "http://cdo.ionsmart.co/bismillahepros/Api/api/getalluser";
    private ProgressDialog pDialog;
    private ArrayList<Teman> temanList = new ArrayList<>();
    private ListView listView;
    private PopupWindow pwindo;
    private TemanListAdapter adapter;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    Button Dprofile;
    Button Dchat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teman);

        listView = (ListView) findViewById(R.id.list);
        adapter = new TemanListAdapter(this, temanList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout ll = (LinearLayout) view; // get the parent layout view
                TextView tv = (TextView) ll.findViewById(R.id.namateman); // get the child text view
                final String nama_detail = tv.getText().toString();

                TextView tv2 = (TextView) ll.findViewById(R.id.statusteman); // get the child text view
                final String status_detail = tv2.getText().toString();

                NetworkImageView thumbnail = (NetworkImageView) ll.findViewById(R.id.thumbnail); // get the child text view
                final String gambar_detail = thumbnail.toString();


                LayoutInflater inflater = (LayoutInflater) ActivityTeman.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.temandetail,(ViewGroup) findViewById(R.id.detaillist));
//                PopupWindow pw = new PopupWindow(layout);layout

                TextView dbprofile = (TextView) layout.findViewById(R.id.detailnama);
                dbprofile.setText(nama_detail);

                TextView setstatus = (TextView) layout.findViewById(R.id.detailstatus);
                setstatus.setText(status_detail);

                pwindo = new PopupWindow(layout,550, 500, true);
                pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

//                try {
//
//                    LayoutInflater inflater = (LayoutInflater) ActivityTeman.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View layout = inflater.inflate(R.layout.temandetail,(ViewGroup) findViewById(R.id.detaillist));
//
//                    TextView dbprofile = (TextView) findViewById(R.id.detailnama);
//                    dbprofile.setText(nama_detail);
//
//                    TextView setstatus = (TextView) findViewById(R.id.detailstatus);
//                    setstatus.setText(status_detail);
//
//                    pwindo = new PopupWindow(layout, 300, 370, true);
//                    pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Teman teman = new Teman();
                                teman.setIndex(obj.getString("id"));
                                teman.setNamateman(obj.getString("nama"));
                                teman.setUrlFoto(obj.getString("gambar"));
                                teman.setStatusteman(obj.getString("level"));

                                temanList.add(teman);
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(),
                                        "Data tidak bisa diambilf", Toast.LENGTH_LONG).show();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

//    @Overridz
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }z
}
