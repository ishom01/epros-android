package com.example.asmarasusanto.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class UploadProposalActivity extends AppCompatActivity implements View.OnClickListener {
    NotificationCompat.Builder builder;
    NotificationManager nm;

    private static final String TAG = UploadProposalActivity.class.getSimpleName();
    //Declaring views
    private Button buttonChoose;
    private Button buttonUpload;

    private EditText editText;
    private EditText namaAcara;
    private EditText keterangan;
    private EditText kepada;
    private EditText tanggalAcara;


    public static final String UPLOAD_URL = "http://internetfaqs.net/AndroidPdfUpload/upload.php";


    //Pdf request code
    private int PICK_PDF_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;


    //Uri to store the image uri
    private Uri filePath;
    private String selectedFilePath;
    private SQLiteHandler db;
    private TextView tvFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_proposal);

        db = new SQLiteHandler(getApplicationContext());

        //Initializing views
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        tvFileName = (TextView) findViewById(R.id.tv_file_name);


        namaAcara = (EditText) findViewById(R.id.acara);
        keterangan = (EditText) findViewById(R.id.ket_proposal);
        kepada = (EditText) findViewById(R.id.penerima);
        tanggalAcara = (EditText) findViewById(R.id.tanggalacara);

        builder = new NotificationCompat.Builder(this);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        //Requesting storage permission
        requestStoragePermission();

        //Setting clicklistener
        buttonChoose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                showFileChooser();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                uploadMultipart();
            }
        });

//        buttonChoose.setOnClickListener(this);
//        buttonUpload.setOnClickListener(this);
    }



    /*
    * This is the method responsible for pdf upload
    * We need the full pdf path and the name for the pdf in this method
    * */

    public void uploadMultipart() {
        //getting name for the image
        String pengirim = kepada.getText().toString().trim();
        String tanggal = tanggalAcara.getText().toString().trim();
        String namaacara = namaAcara.getText().toString().trim();
        String k_acara = keterangan.getText().toString().trim();


        //getting the actual path of the image
//        String path = FilePath.getPath(this, filePath);

        if (selectedFilePath == null && pengirim.isEmpty() && tanggal.isEmpty() && namaacara.isEmpty() && k_acara.isEmpty()) {

            Toast.makeText(this, "Mohon pastikan tidak ada Form yang kosong", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                HashMap<String, String> user = db.getUserDetails();

                String username = user.get("username");

                Uri builturi = Uri.parse(AppConfig.URL_CEK_PROPOSAL) .buildUpon()
                        .appendQueryParameter("username",username).build();

                String  url = builturi.toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, url)
                        .addFileToUpload(selectedFilePath, "proposal") //Adding file
                        .addParameter("namaacara", namaacara) //Adding text parameter to the request
                        .addParameter("k_pengirim", k_acara) //Adding text parameter to the request
                        .addParameter("date",tanggal) //Adding text parameter to the request
                        .addParameter("ke", pengirim) //Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        cekProposal();
    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST );
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Uri selectedFileUri = data.getData();
            selectedFilePath = FilePath.getPath(this,selectedFileUri);
            Log.i(TAG,"Selected File Path:" + selectedFilePath);
            tvFileName.setText(selectedFilePath);
        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if (v == buttonUpload) {
            uploadMultipart();
        }
    }

    private void cekProposal() {
        String tag_string_req = "req_register";

        String filename = selectedFilePath.substring(selectedFilePath.lastIndexOf("/")+1);

        Uri builturi = Uri.parse(AppConfig.URL_CEK_ISI) .buildUpon()
                .appendQueryParameter("filename",filename).build();

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
                        String pesan = jObj.getString("message");

                        Intent intent = new Intent(getApplicationContext(), UploadProposalActivity.class);
                        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notify=new Notification.Builder
                                (getApplicationContext()).setContentTitle("asdasd").setContentText(pesan).
                                setContentTitle("Upload Proposal Sukses").setContentIntent(pIntent).setSmallIcon(R.drawable.pdf).build();

                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                        notif.notify(0, notify);

                    } else {
                        // User successfully stored in MySQL
                        String pesan = jObj.getString("message");

                        Intent intent = new Intent(getApplicationContext(), UploadProposalActivity.class);
                        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notify=new Notification.Builder
                                (getApplicationContext()).setContentTitle("asdasd").setContentText(pesan).
                                setContentTitle("Format Proposal Salah").setContentIntent(pIntent).setSmallIcon(R.drawable.pdf).build();

                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                        notif.notify(0, notify);

//                        Intent intent = new Intent(UploadProposalActivity.this,ActivityTeman.class);
//                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
//
//                        builder.setContentTitle("Upload Proposal Success")
//                                .setContentText(pesan)
//                                .setSmallIcon(R.drawable.pdf)
//                                .setWhen(System.currentTimeMillis())
//                                .setContentIntent(pendingIntent); // add this
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
