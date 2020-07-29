package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.basictest.R;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    private PdfDocument doc;
    private PdfDocument.PageInfo pageInfo;
    private PdfDocument.Page page;
    @BindView(R.id.sbtn_apply3_next)
    Button btn;
    @BindView(R.id.lv_apply3)
    LinearLayout lv_apply3;
    @BindView(R.id.re_sign)
    RelativeLayout re_sign;
    private File uploadFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply3);
        ButterKnife.bind(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                re_sign.setVisibility(View.VISIBLE);
                setupPdf(lv_apply3);
                re_sign.setVisibility(View.INVISIBLE);
            }
        });


    }

    //生成pdf 内容+签字
    public void setupPdf(LinearLayout lv_apply3) {
        doc = new PdfDocument();
        PdfDocument.PageInfo pageInfo =new PdfDocument.PageInfo.Builder((int) (lv_apply3.getWidth()*0.35f), (int) (lv_apply3.getHeight()*0.35f), 1)
                .create();


        page = doc.startPage(pageInfo);

        Canvas canvas=page.getCanvas();
        canvas.scale(0.35f,0.35f);
        lv_apply3.draw(canvas);
        doc.finishPage(page);

        //设置路径
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        try {
            uploadFile = new File(file.getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "测试.pdf");
            doc.writeTo(new FileOutputStream(uploadFile));
            //应该弹一个对话框
            Log.d("生成pdf", "成功");

        } catch (IOException e) {
            Log.d("生成pdf", "失败");
            e.printStackTrace();
        }
        doc.close();
    }
}