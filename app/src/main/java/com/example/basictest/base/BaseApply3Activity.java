package com.example.basictest.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseApply3Activity extends AppCompatActivity {

    private PdfDocument doc;
    private PdfDocument.PageInfo pageInfo;
    private PdfDocument.Page page;
    //生成pdf
    public void setupPdf(LinearLayout lv_apply3,LinearLayout layout){
        doc=new PdfDocument();
        pageInfo = new PdfDocument.PageInfo.Builder(lv_apply3.getWidth(), lv_apply3.getHeight(), 1)
                .create();
        page = doc.startPage(pageInfo);
        lv_apply3.draw(page.getCanvas());
        doc.finishPage(page);

        //往pdf添加组件
        pageInfo=new PdfDocument.PageInfo.Builder(layout.getWidth(),layout.getHeight(),1).create();
        page=doc.startPage(pageInfo);
        layout.draw(page.getCanvas());
        doc.finishPage(page);
        //设置路径
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        try {
            doc.writeTo(new FileOutputStream(new File(file.getAbsolutePath()+"/"+new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date())+"ad.pdf")));
            //应该弹一个对话框
            Log.d("生成pdf","成功");
        } catch (IOException e) {
            Log.d("生成pdf","失败");
            e.printStackTrace();
        }
        doc.close();
    }

    //路径转uri
    public static Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


}
