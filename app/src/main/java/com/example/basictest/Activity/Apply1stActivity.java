package com.example.basictest.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.basictest.utils.OkManager;
import com.example.basictest.utils.SpUtils;
import com.example.basictest.Class.bankEntity;
import com.example.basictest.Class.bankListResponse;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.Gson;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import top.androidman.SuperButton;

import static com.example.basictest.utils.FileUtils.getPath;
import static com.example.basictest.utils.FileUtils.getRealPathFromURI;

public class Apply1stActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    Intent intent;
    @BindView(R.id.topbar_apply1)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.spinner_apply)
    Spinner spinner;

    @BindView(R.id.sbtn_apply1)
    SuperButton sbtn_apply1;
    @BindView(R.id.sbtn_apply1_next)
    SuperButton sbtn_apply1_next;


    private String token;

    private ArrayAdapter<String> adapter;

    //定义银行名银行id字典
    private Map<String,Integer> map=new HashMap<>();
    //银行id
    private int coId;
    private int REQUEST_CODE=1;

    String TAG="aa";
    private String path,filename;
    private File file;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply1st);
        ButterKnife.bind(this);
        verifyStoragePermissions(Apply1stActivity.this);
        token=SpUtils.getInstance(this).getString("token",null);
        initTopBar();
        initSpinner();
        spinner.setOnItemSelectedListener(this);
        initBtn();

    }




    @SuppressLint("ResourceAsColor")
    private void initTopBar() {
        mTopBar.setBackgroundAlpha(255);
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置标题名
        mTopBar.setTitle("系统设置");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
    }


    //初始化spinner
    private void initSpinner(){
        String header="Bearer "+token;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .addHeader("Authorization",header)
                .url(netConstant.getBankURL())
                .get()//默认就是GET请求，可以不写
                .build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Response response = call.execute();
                    String res=response.body().string();
                    Log.d("a", "onResponse: " + res);
                    bankListResponse list=new Gson().fromJson(res, bankListResponse.class);
                    List<bankEntity> data=list.getRows();
                    List<String> datas=new ArrayList<String>();
                    for (int i=0;i<data.size();i++){
                        datas.add(data.get(i).getCoName());
                        map.put(data.get(i).getCoName(),data.get(i).getId());
                        Log.d("a",datas.get(i));
                    }
                    adapter=new ArrayAdapter<String>(Apply1stActivity.this,R.layout.item_spinner,R.id.tv_spinner,datas);
                    //分线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinner.setAdapter(adapter);
                        }
                    });
                }
                catch (IOException e){

                }
            }
        }).start();
    }

    //取得路径 文件名
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.w(TAG,"返回的数据："+data);
        if (resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            //使用第三方应用打开
            if ("file".equalsIgnoreCase(uri.getScheme())){
                path = uri.getPath();
                file = new File(path);
                filename = file.getName();
                Log.w(TAG,"getName==="+filename);
                Toast.makeText(this,path+"11111",Toast.LENGTH_SHORT).show();
                return;
            }
            //4.4以后
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                // 获取文件路径
                path = getPath(this, uri);
                Log.w(TAG,path);
                file = new File(path);
                // 获得文件名
                filename = file.getName();
                // 这里是为了选中文件后，编辑框内容变成我选中的文件名
                // 直接 mc_annex.setText(file.getName()); 也行
                Log.w(TAG,"getName==="+filename);
                Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(this,uri);
                Log.w(TAG,path);
                Toast.makeText(Apply1stActivity.this, path+"222222", Toast.LENGTH_SHORT).show();
            }
        }



    }



    private void initBtn(){
        sbtn_apply1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select PDF"),REQUEST_CODE);
            }
        });


        sbtn_apply1_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upload1();
                upload();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String bankStr=(String)spinner.getItemAtPosition(i);
        coId=map.get(bankStr);
        Log.d("a",coId+"");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void upload1(){
        if (file.exists()){
            OkManager okManager=OkManager.getInstance();
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("eContractFile",filename,RequestBody.create(MediaType.parse("multipart/form-data"),file))
                    .addFormDataPart("coId",coId+"")
                    .build();
            okManager.postFile(Apply1stActivity.this,netConstant.getUploadEContractURL(),requestBody,new okhttp3.Callback(){

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "onFailure: ",e);

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseBody = response.body().string();
                    final JSONObject obj = JSON.parseObject(responseBody);
                    Log.e(TAG,obj.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 对返回结果进行操作
                        }
                    });
                }
            });
        }else {
            Log.d(TAG, "文件不存在");

        }
    }


    private void upload(){
        HttpRequest.build(Apply1stActivity.this,netConstant.getUploadEContractURL())
                .setMediaType(baseokhttp3.MediaType.parse("application/pdf"))
                .addHeaders("Authorization","Bearer "+SpUtils.getInstance(Apply1stActivity.this).getString("token",null))
                .addHeaders("Content-Type","multipart/form-data")
                .addParameter("eContractFile",file)
                .addParameter("coId",coId)
                .setResponseListener(new ResponseListener() {
                    @Override
                    public void onResponse(String main, Exception error) {
                        if (error == null) {
                            Log.e(TAG,main);
                        } else {
                            Log.e(TAG,"错误",error);
                        }
                    }
                })
                .doPost();

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }
}