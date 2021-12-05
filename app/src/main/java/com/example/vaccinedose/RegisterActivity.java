package com.example.vaccinedose;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText email , password , confpass;
    private Button signup;
    public FirebaseAuth auth;
    public static String txt_email , txt_pass;
    public final String url = "https://doseauth.000webhostapp.com/user_reg.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.r_email);
        password = (EditText) findViewById(R.id.r_pass);
        confpass = (EditText) findViewById(R.id.r_conf_pass);
        signup = (Button)findViewById(R.id.signup);
        auth = FirebaseAuth.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt_email= email.getText().toString().trim();
                txt_pass = password.getText().toString().trim();
                String txt_conf_pass = confpass.getText().toString().trim();

                if(txt_email.isEmpty() || txt_pass.isEmpty() || txt_conf_pass.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"One of the fileds are empty",Toast.LENGTH_SHORT).show();
                }
                else if(!(txt_pass.equals(txt_conf_pass))){
                    Toast.makeText(RegisterActivity.this,"Password dose not match",Toast.LENGTH_SHORT).show();
                }
                else if(txt_pass.equals(txt_conf_pass) && txt_pass.length()<6){
                    Toast.makeText(RegisterActivity.this,"Please choose a strong password",Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.createUserWithEmailAndPassword(txt_email,txt_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Registered successfully",Toast.LENGTH_SHORT).show();
                                insertData();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this,"User already exists",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });
    }

    private void insertData(){
        //insert into sql
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("true")){
                    Intent intent = new Intent(RegisterActivity.this,DashboardActivity.class);
                    startActivity(intent);

                }

                else {
                    Toast.makeText(RegisterActivity.this,"Please try again",Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();
                param.put("email",txt_email);
                param.put("password",txt_pass);


                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(request);


    }

}