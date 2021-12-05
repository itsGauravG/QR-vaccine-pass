package com.example.vaccinedose;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private EditText email , password;
    private Button login;
    private TextView reg;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.l_email);
        password = (EditText) findViewById(R.id.l_pass);
        login = (Button)findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();
        reg= (TextView) findViewById(R.id.txt_reg);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String txt_email = email.getText().toString().trim();
            String txt_pass = password.getText().toString().trim();

            if(txt_email.isEmpty() || txt_pass.isEmpty()){
                Toast.makeText(MainActivity.this,"One of the fileds are empty",Toast.LENGTH_SHORT).show();
            }
            else{
                auth.signInWithEmailAndPassword(txt_email,txt_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       Toast.makeText(MainActivity.this,"Logged in ",Toast.LENGTH_SHORT).show();
                       Intent i =new Intent(MainActivity.this,DashboardActivity.class);
                       startActivity(i);
                       finish();
                   }

                    }
                });


            }



            }
        });

    }
}