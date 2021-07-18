package com.example.newproject;



import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

public class encode_activity extends AppCompatActivity {
    ImageView img_encode;
    Button encode_button;
    EditText msgText;
    EditText secretKeyText;
    private static final int Image_pick_code = 1000;
    private static final int permission_code_ = 1001;
    private ProgressDialog save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);
        //views..
        img_encode = findViewById(R.id.img_encode);
        ActivityCompat.requestPermissions(encode_activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);

        //handle image clicks..
        img_encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check run time permissions..
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        //permission not granted,request it..
                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission..
                        requestPermissions(permission, permission_code_);
                    } else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                } else {
                    //system os is less then marshmalloow
                    pickImageFromGallery();
                }
            }
        });
        encode_button = findViewById(R.id.encode_btn);
        encode_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                encodeData();
            }
        });
        msgText = findViewById(R.id.enter_msg_id);
        secretKeyText = findViewById(R.id.enter_key_id);

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void encodeData () {

        String message = msgText.getText().toString();

        String secretKey = secretKeyText.getText().toString();
        //Check if message or secret key is empty or not
        if (message.length()==0 ||secretKey.length()==0) {
            Toast.makeText(encode_activity.this, "Enter message and secret key ", Toast.LENGTH_SHORT).show();
        }
        else {
            Bitmap bmap;
            //convert image to bitmap
            BitmapDrawable bmapD = (BitmapDrawable) img_encode.getDrawable();
            bmap = bmapD.getBitmap();
            Bitmap operation = Bitmap.createBitmap(bmap.getWidth(), bmap.getHeight(), bmap.getConfig());


            // storing reversesecretkey+message+secretkey
            StringBuilder secretKeyReverse=new StringBuilder();
            secretKeyReverse.append(secretKey);
            secretKeyReverse.reverse();
            message=secretKeyReverse+message+secretKey;

            String d = new String();
            String m = new String();
            int messageIndex = 0;
//                int l = 0;

            //Use to store pixels
            int p, r, b, g;
            for (int i = 0; i < bmap.getWidth(); i++) {
                for (int j = 0; j < bmap.getHeight() - 3; j++) {
                    if (messageIndex < message.length()) {
                        String binary = Integer.toBinaryString((int) (message.charAt(messageIndex)));
                        if(binary.length()<7)
                        {   while (binary.length()!=7)
                            binary='0'+binary;

                        }

                        //first pixel
                        p=bmap.getPixel(i,j);

                        r= Color.red(p);
                        d=Integer.toBinaryString(r);
                        d=d.substring(0,d.length()-1);
                        d+=binary.charAt(0);
                        m+=binary.charAt(0);
                        r=Integer.parseInt(d,2);

                        g=Color.green(p);
                        d=Integer.toBinaryString(g);
                        d=d.substring(0,d.length()-1);
                        d+=binary.charAt(1);
                        m+=binary.charAt(1);
                        g=Integer.parseInt(d,2);


                        b=Color.blue(p);
                        d=Integer.toBinaryString(b);
                        d=d.substring(0,d.length()-1);
                        d+=binary.charAt(2);
                        m+=binary.charAt(2);
                        b=Integer.parseInt(d,2);

                        operation.setPixel(i,j,Color.rgb(r,g,b));

                        //2nd pixel
                        j++;
                        p=bmap.getPixel(i,j);
                        r= Color.red(p);
                        d=Integer.toBinaryString(r);
                        d=d.substring(0,d.length()-1);
                        d+=binary.charAt(3);
                        m+=binary.charAt(3);
                        r=Integer.parseInt(d,2);

                        g=Color.green(p);
                        d=Integer.toBinaryString(g);
                        d=d.substring(0,d.length()-1);
                        d+=binary.charAt(4);
                        m+=binary.charAt(4);
                        g=Integer.parseInt(d,2);


                        b=Color.blue(p);
                        d=Integer.toBinaryString(b);
                        d=d.substring(0,d.length()-1);
                        d+=binary.charAt(5);
                        m+=binary.charAt(5);
                        b=Integer.parseInt(d,2);

                        operation.setPixel(i,j,Color.rgb(r,g,b));

                        //3rd pixel
                        j++;
                        p=bmap.getPixel(i,j);
                        r= Color.red(p);
                        d=Integer.toBinaryString(r);
                        d=d.substring(0,d.length()-1);
                        d+=binary.charAt(6);
                        m+=binary.charAt(6);
                        r=Integer.parseInt(d,2);

                        g=Color.green(p);
                        b=Color.blue(p);
                        operation.setPixel(i,j,Color.rgb(r,g,b));
                        messageIndex++;
                    }
                    else {
                        p=bmap.getPixel(i,j);
                        r= Color.red(p);
                        b= Color.blue(p);
                        g= Color.green(p);
                        operation.setPixel(i,j,Color.rgb(r,g,b));
                    }
                }
            }
            //storing final length on image
            p=bmap.getPixel(bmap.getWidth()-1,bmap.getHeight()-1);
            r=Color.red(p);
            g=Color.red(p);
            b=Color.red(p);
            r=message.length();
            operation.setPixel(bmap.getWidth()-1,bmap.getHeight()-1,Color.rgb(r,g,b));

            //saving image
            img_encode.setImageBitmap(operation);
            Date currentTime = Calendar.getInstance().getTime();
            String s1=currentTime.toString();
            File file = new File(Environment.getExternalStorageDirectory()+File.separator+ File.separator
                    +s1 + ".PNG"); // the File to save ,
            ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
            File dir= contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File folder=new File(contextWrapper.getExternalMediaDirs()+File.separator+"Image stagnography");
            if(!folder.exists())
                folder.mkdirs();
            try {
                OutputStream fOut;
                fOut = new FileOutputStream(file);
                operation.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream
                Intent i=new Intent(encode_activity.this,MainActivity.class);
                startActivity(i);
            } catch (Exception e) {
                Toast.makeText(encode_activity.this,e.getMessage()+"something wrong",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void pickImageFromGallery(){
        //intent to pick image
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, Image_pick_code);
    }

    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults) {

        //handle result of picked image.


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case permission_code_: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Image_pick_code) {
            //set image to image view
            img_encode.setImageURI(data.getData());
        }
    }
}


