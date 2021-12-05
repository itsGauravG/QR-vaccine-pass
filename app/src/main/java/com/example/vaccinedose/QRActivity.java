package com.example.vaccinedose;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRActivity extends AppCompatActivity {
    private ImageView qrimg ;
    private Button generate,save;
    Bitmap bitmap ;
    QRGEncoder qrgEncoder;
    FirebaseAuth auth;
    String txt_email ;
    Boolean is_qrgen=false;
    OutputStream outputStream;
    public final String url = "https://doseauth.000webhostapp.com/qr_code.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);

        qrimg = (ImageView) findViewById(R.id.qr_img);
        generate=(Button) findViewById(R.id.gen_qr_btn);
        save = (Button) findViewById(R.id.save_qr);
        auth = FirebaseAuth.getInstance();

        ActivityCompat.requestPermissions(QRActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(QRActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            txt_email = auth.getCurrentUser().getEmail().toString();
            if(is_qrgen==false) {
                get_qr_status();
            }


            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(is_qrgen==true){
                    save_qr();
                }

            }
        });

    }

    private void get_qr_status(){
        //insert into sql
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // below line is for getting
                // the windowmanager service.
                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

                // initializing a variable for default display.
                Display display = manager.getDefaultDisplay();

                // creating a variable for point which
                // is to be displayed in QR Code.
                Point point = new Point();
                display.getSize(point);

                // getting width and
                // height of a point
                int width = point.x;
                int height = point.y;

                // generating dimension from width and height.
                int dimen = width < height ? width : height;
                dimen = dimen * 3 / 4;

                if(response.equals("false")){
                    Toast.makeText(QRActivity.this,"You have not uploaded your Vaccine Certificate",Toast.LENGTH_SHORT).show();

                }

                else if(response.equals("1")){
                    qrgEncoder = new QRGEncoder("1", null, QRGContents.Type.TEXT, dimen);
                    try {
                        // getting our qrcode in the form of bitmap.
                        bitmap = qrgEncoder.encodeAsBitmap();
                        // the bitmap is set inside our image
                        // view using .setimagebitmap method.
                        qrimg.setImageBitmap(bitmap);
                        is_qrgen=true;
                        Toast.makeText(QRActivity.this,"QR Generated",Toast.LENGTH_SHORT).show();

                    } catch (WriterException e) {
                        // this method is called for
                        // exception handling.
                        Log.e("Tag", e.toString());
                    }


                }
                else if(response.equals("2")){

                    qrgEncoder = new QRGEncoder("2", null, QRGContents.Type.TEXT, dimen);
                    try {

                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrimg.setImageBitmap(bitmap);
                        is_qrgen=true;
                        Toast.makeText(QRActivity.this,"QR Generated",Toast.LENGTH_SHORT).show();
                    } catch (WriterException e) {
                        Log.e("Tag", e.toString());
                    }


                }
                else {
                    Toast.makeText(QRActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QRActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();
                param.put("email",txt_email);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(QRActivity.this);
        queue.add(request);



    }

    private void save_qr(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) qrimg.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        File filepath = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File dir = new File(filepath.getAbsolutePath() + "/myQRCodes/");
        dir.mkdirs();
        File file = new File (dir,System.currentTimeMillis()+".jpg");

        try{
            outputStream = new FileOutputStream(file);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        Toast.makeText(QRActivity.this,"Image saved to internal storage",Toast.LENGTH_SHORT).show();
        try{
            outputStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }

        try{
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }


}