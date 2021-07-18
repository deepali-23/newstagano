package com.example.newproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class decode_ extends AppCompatActivity {
    ImageView decodeImage;
    Button decodeButton;
    EditText secretKey;
    TextView showMessage;
    private static final int Image_pick_code = 1000;
    private static final int permission_code_ = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);
        decodeImage = findViewById(R.id.decode_msg);
        secretKey=findViewById(R.id.editText);
        showMessage=findViewById(R.id.showmessage);
        //handle image clicks..
        decodeImage.setOnClickListener(new View.OnClickListener() {
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

        decodeButton=findViewById(R.id.btn_decode);
        decodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decode();
            }
        });


    }
    private void pickImageFromGallery() {
        //intent to pick image
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, Image_pick_code);
    }

    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Image_pick_code) {
            //set image to image view
            decodeImage.setImageURI(data.getData());
        }
    }
    private  void decode()
    {String message = "";
        int c=0;
        Bitmap bmap;
        BitmapDrawable bmapD = (BitmapDrawable) decodeImage.getDrawable();
        bmap = bmapD.getBitmap();
        int p,q;
        String d;
        String ans="";
        String m="";
//    Bitmap operation = Bitmap.createBitmap(bmap.getWidth(), bmap.getHeight(), bmap.getConfig());
        int size=bmap.getPixel(bmap.getWidth()-1,bmap.getHeight()-1);
        size= Color.red(size);
        for (int i = 0; i < bmap.getWidth(); i++) {
            for (int j = 0; j < bmap.getHeight()-3; j++) {
                if(size>0) {
                    size--;
                    m = "";
                    p = bmap.getPixel(i, j);
                    q = Color.red(p);
                    d = Integer.toBinaryString(q);
                    m += d.charAt(d.length() - 1);

                    q = Color.green(p);
                    d = Integer.toBinaryString(q);

                    m += d.charAt(d.length() - 1);

                    q = Color.blue(p);
                    d = Integer.toBinaryString(q);
                    m += d.charAt(d.length() - 1);


                    j++;
                    p = bmap.getPixel(i, j);
                    q = Color.red(p);
                    d = Integer.toBinaryString(q);

                    m += d.charAt(d.length() - 1);


                    q = Color.green(p);
                    d = Integer.toBinaryString(q);

                    m += d.charAt(d.length() - 1);


                    q = Color.blue(p);
                    d = Integer.toBinaryString(q);

                    m += d.charAt(d.length() - 1);


                    j++;
                    p = bmap.getPixel(i, j);
                    q = Color.red(p);
                    d = Integer.toBinaryString(q);
                    m += d.charAt(d.length() - 1);
                    int a = Integer.parseInt(m, 2);
                    char z = (char) a;
                    c++;
                    ans += z;
                }



            }
        }

        String key=secretKey.getText().toString();
        int length=key.length()-1;
        int n=ans.length()-1;
        int flag=0;

        if(key.length()==0) {
            flag = 1;
        }
        else {
            for (int i = 0; i <= length; i++, n--) {
                if ((ans.charAt(i) == (key.charAt(length-i)) && ans.charAt(i) == ans.charAt(n))) {
                    continue;
                }
                else {
                    flag = 1;
                    break;
                }
            }
        }
        if(flag==0) {

            String decoded=ans.substring(key.length(),ans.length()-key.length());
            decodeButton.setText(decoded);
            Toast.makeText(decode_.this, decoded, Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(decode_.this, "Invalid secret key", Toast.LENGTH_LONG).show();
        }
    }





}

