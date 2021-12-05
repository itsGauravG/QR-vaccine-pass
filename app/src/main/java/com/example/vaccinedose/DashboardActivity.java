package com.example.vaccinedose;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {
    private Button upload_certi , gen_qr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        upload_certi = (Button) findViewById(R.id.upld_certi);
        gen_qr = (Button) findViewById(R.id.gen_qr);



        upload_certi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,CertificateActivity.class);
                startActivity(i);
            }
        });

        gen_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,QRActivity.class);
                startActivity(i);
            }
        });

    }
}