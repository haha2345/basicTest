package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.basictest.R;

public class ShipingongzhenActivity extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipingongzhen);
        btn=findViewById(R.id.btn_shipingongzheng);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShipingongzhenActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });
    }
}