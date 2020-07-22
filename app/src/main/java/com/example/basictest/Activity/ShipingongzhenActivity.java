package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.basictest.R;

public class ShipingongzhenActivity extends AppCompatActivity {

    Button btn;
    String src;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipingongzhen);
        src=getIntent().getStringExtra("basestr");
        btn=findViewById(R.id.btn_shipingongzheng);
        iv=findViewById(R.id.iv_aa);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShipingongzhenActivity.this,CameraActivity.class);
                intent.putExtra("basesrc",src);
                startActivity(intent);
            }
        });
    }
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}