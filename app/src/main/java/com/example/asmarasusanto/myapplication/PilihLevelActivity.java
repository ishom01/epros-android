package com.example.asmarasusanto.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PilihLevelActivity extends AppCompatActivity {
    private Button btnPerusahaan;
    private Button btnKomunitas;
    private Button btnInstitut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_level);

        btnPerusahaan = (Button) findViewById(R.id.perusahaan);
        btnKomunitas = (Button) findViewById(R.id.komunitas);
        btnInstitut = (Button) findViewById(R.id.institut);


        btnPerusahaan.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RPerusahaanActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnKomunitas.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        PilihLevelActivity.class);
                startActivity(i);
                finish();
            }
        });


        btnInstitut.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        PilihLevelActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
