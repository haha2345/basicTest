package com.example.basictest.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.basictest.base.BaseActivity;
import com.example.basictest.utils.OkManager;
import com.example.basictest.utils.PdfToTxt;
import com.example.basictest.utils.SpUtils;
import com.example.basictest.Class.bankEntity;
import com.example.basictest.Class.bankListResponse;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.example.basictest.utils.Utils;

import com.example.basictest.utils.ValueConvertUtil;
import com.google.gson.Gson;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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

import static com.example.basictest.R.drawable.pdf;
import static com.example.basictest.utils.FileUtils.getPath;


public class Apply1stActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private Bundle bundle = new Bundle();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public ProgressDialog progressDialog;
    private Context mContext=Apply1stActivity.this;

    QMUITipDialog tipDialog;
    Intent intent;
    @BindView(R.id.topbar_apply1)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.spinner_apply)
    Spinner spinner;

    @BindView(R.id.sbtn_apply1)
    SuperButton sbtn_apply1;
    @BindView(R.id.sbtn_apply1_next)
    Button sbtn_apply1_next;
    @BindView(R.id.checkBox_apply1_1)
    CheckBox checkbox_apply1;
    @BindView(R.id.tv_apply1_name)
    TextView tv_apply1_name;

    Utils utils=new Utils();

    private String token;
    private String pdfStr;

    private ArrayAdapter<String> adapter;

    //定义银行名银行id字典
    private Map<String,Integer> map=new HashMap<>();
    //银行id
    private int coId;
    private int REQUEST_CODE=1;

    String TAG="aa";
    private String caseId,caseCode,userId;
    private String path,filename;
    private File file;
    Uri uri;
    //判断是否获取到文件路径
    //获取pdf标志位
    private int flag=0;
    //checkbox标志位
    private int flag1=0;

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
        initCheckBox();
        PDFBoxResourceLoader.init(getApplicationContext());
    }




    @SuppressLint("ResourceAsColor")
    private void initTopBar() {
        mTopBar.setBackgroundAlpha(255);
        mTopBar.addLeftImageButton(R.drawable.back,R.id.topbar_right_change_button)
        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //设置标题名
        mTopBar.setTitle("赋强公证申请");

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
                    if (list.getCode()==200){
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
                    }else if (list.getCode()==401){
                        breaker(mContext);
                    }else {
                        showToast(mContext, list.getMsg());
                    }

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


        if (resultCode == Activity.RESULT_OK) {
            Log.w(TAG,"返回的数据："+data.getScheme());
            uri = data.getData();
            //使用第三方应用打开
            if ("file".equalsIgnoreCase(uri.getScheme())){
                path = uri.getPath();
                file = new File(path);
                filename = file.getName();
                Log.w(TAG,"getName==="+filename);
                //取到文件名改textview
                try {
                    afterGetFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast.makeText(this,path+"11111",Toast.LENGTH_SHORT).show();
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
                try {
                    afterGetFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                //Toast.makeText(Apply1stActivity.this, path+"222222", Toast.LENGTH_SHORT).show();
            }
        }



    }


    private void initCheckBox(){
        checkbox_apply1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    flag1=1;
                    if (flag==1){
                        Log.d("检查框","已获取到文件名");
                        sbtn_apply1_next.setEnabled(true);
                    }

                }else{
                    flag1=0;
                    sbtn_apply1_next.setEnabled(false);
                }

            }
        });
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
                showProgressDialog(mContext,"请稍后");
                upload();
            }
        });
    }


    //Spinner的重载 获取合作银行id 储存银行名
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String bankStr=(String)spinner.getItemAtPosition(i);
        SpUtils.getInstance(this).setString("bank",bankStr);
        coId=map.get(bankStr);
        Log.d("a",coId+"");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void upload(){
        HttpRequest.build(Apply1stActivity.this,netConstant.getUploadEContractURL())
                .setMediaType(baseokhttp3.MediaType.parse("application/pdf"))
                .addHeaders("Authorization","Bearer "+SpUtils.getInstance(Apply1stActivity.this).getString("token",null))
                .addHeaders("Content-Type","multipart/form-data")
                .addParameter("eContractFile",file)
                .addParameter("coId",coId)
                .setJsonResponseListener(new JsonResponseListener() {
                    @Override
                    public void onResponse(JsonMap main, Exception error) {
                        if (error == null) {
                            Log.d(TAG,main.getString("data"));
                            if (main.getString("code").equals("200")){
                                dismissProgressDialog();
                                //获取保存caseid userid case码
                                JsonMap result=main.getJsonMap("data");
                                caseId=result.getString("id");
                                userId=result.getString("userId");
                                caseCode=result.getString("caseCode");
                                SpUtils.getInstance(mContext).setString("caseId",caseId,1800);
                                SpUtils.getInstance(mContext).setString("userId",userId,1800);
                                SpUtils.getInstance(mContext).setString("caseCode",caseCode,1800);
                                SpUtils.getInstance(mContext).setString("uploadfilename",filename,1800);
                                Log.d("获取到的上传信息",caseId+"  "+caseCode+"  "+userId);
                                jumpToApply2();
                            }else if (main.getString("code").equals("401")){
                                dismissProgressDialog();
                                breaker(mContext);
                            }
                            else {
                                dismissProgressDialog();
                                getTipDialog(3,main.getString("msg")).show();
                                delayCloseTip();
                                //utils.showToastInThread(mContext,main.getString("msg"));
                            }

                        } else {
                            dismissProgressDialog();
                            utils.showToastInThread(mContext,"连接错误");
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

    private void afterGetFile() throws Exception {


        //判断后缀是否为pdf
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".pdf")) {
            tv_apply1_name.setText(filename);
            Resources resources = Apply1stActivity.this.getResources();
            Drawable drawable = resources.getDrawable(pdf);
            sbtn_apply1.setIcon(drawable);
            sbtn_apply1.setPressed(false);
            pdfStr=PdfToTxt.readPdf(path);
            //测试pdf提取功能
            Log.d(TAG, "afterGetFile: "+pdfStr);
            String rmb=extraAttr(pdfStr,"人民币（大写）","元。");
            Log.d(TAG, "afterGetFile: "+extraAttr(pdfStr,"放款利率：日利率","，"));
            Log.d(TAG, "afterGetFile: "+ rmb);
            Log.d(TAG, "afterGetFile: "+ ValueConvertUtil.formatAmount(rmb));
//        sbtn_apply1.setEnabled(false);
            flag=1;
            if (flag1==1){
                sbtn_apply1_next.setEnabled(true);
            }
        }else {
            getTipDialog(3,"文件格式不对，请重新选择文件").show();
            delayCloseTip();
        }

//        sbtn_apply1.setClickable(false);

    }
    private void jumpToApply2(){
        intent=new Intent(mContext,Apply2edActivity.class);
        bundle.putString("userid", userId);
        bundle.putString("casecode", caseCode);
        bundle.putString("uploadfilename", filename);
        bundle.putString("caseid", caseId);
        intent.putExtras(bundle);
        startActivity(intent);

    }
    //获得需要的变量
    private void getAttr(){

    }
    //显示加载框
    public void showProgressDialog(Context mContext, String text) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage(text);    //设置内容
        progressDialog.setCancelable(false);//点击屏幕和按返回键都不能取消加载框
        progressDialog.show();

        //设置超时自动消失
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dismissProgressDialog()) {

                }
            }
        }, 60000);//超时时间60秒
    }

    //取消加载框
    public Boolean dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                return true;//取消成功
            }
        }
        return false;//已经取消过了，不需要取消
    }
    public QMUITipDialog getTipDialog(int type, String str) {
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(type)
                .setTipWord(str)
                .create();
        return tipDialog;
    }

    //1.5s后关闭tipDIalog
    public void delayCloseTip() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //要延时的程序
                tipDialog.dismiss();
            }
        }, 1500);
    }

    //提取String中需要的字符串
    private String extraAttr(String str,String pre,String end){
        int prePos,endPos;
        prePos=str.indexOf(pre);
        str=str.substring(prePos);
        endPos=str.indexOf(end);
        String value=str.substring(pre.length(),endPos).trim();
        value=value.replace(" ","");
        value=value.replace("，","");
        value=value.replace("元","圆");
        value=value.replace("。","");
        return value;
    }

}