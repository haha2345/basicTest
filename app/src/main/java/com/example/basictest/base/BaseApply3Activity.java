package com.example.basictest.base;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basictest.Activity.Apply1stActivity;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.example.basictest.utils.SpUtils;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.org.bjca.signet.component.core.activity.SignetCoreApi;
import cn.org.bjca.signet.component.core.activity.SignetToolApi;
import cn.org.bjca.signet.component.core.bean.results.FindBackUserResult;
import cn.org.bjca.signet.component.core.bean.results.GetUserListResult;
import cn.org.bjca.signet.component.core.bean.results.RegisterResult;
import cn.org.bjca.signet.component.core.bean.results.SignDataResult;
import cn.org.bjca.signet.component.core.bean.results.SignImageResult;
import cn.org.bjca.signet.component.core.callback.FindBackUserCallBack;
import cn.org.bjca.signet.component.core.callback.RegisterCallBack;
import cn.org.bjca.signet.component.core.callback.SetSignImageCallBack;
import cn.org.bjca.signet.component.core.callback.SignDataCallBack;
import cn.org.bjca.signet.component.core.enums.IdCardType;
import cn.org.bjca.signet.component.core.enums.RegisterType;
import cn.org.bjca.signet.component.core.enums.SetSignImgType;

public class BaseApply3Activity extends AppCompatActivity {

    private PdfDocument doc;
    private PdfDocument.PageInfo pageInfo;
    private PdfDocument.Page page;
    public String msspId,activeCode,signId;
    private String token;
    private File uploadFile;


    //加载框
    private ProgressDialog progressDialog;


    //生成pdf 内容+签字
    public void setupPdf(LinearLayout lv_apply3,LinearLayout layout){
        doc=new PdfDocument();
        pageInfo = new PdfDocument.PageInfo.Builder(lv_apply3.getWidth(), lv_apply3.getHeight(), 1)
                .create();
        page = doc.startPage(pageInfo);
        lv_apply3.draw(page.getCanvas());
        doc.finishPage(page);

        //签字
        //往pdf添加组件
        pageInfo=new PdfDocument.PageInfo.Builder(layout.getWidth(),layout.getHeight(),1).create();
        page=doc.startPage(pageInfo);
        layout.draw(page.getCanvas());
        doc.finishPage(page);
        //设置路径
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        try {
            uploadFile=new File(file.getAbsolutePath()+"/"+new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date())+"ad.pdf");
            doc.writeTo(new FileOutputStream(uploadFile));
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


//显示加载框
    public void showProgressDialog(Context mContext, String text) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage(text);	//设置内容
        progressDialog.setCancelable(false);//点击屏幕和按返回键都不能取消加载框
        progressDialog.show();

        //设置超时自动消失
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dismissProgressDialog()){

                }
            }
        }, 60000);//超时时间60秒
    }
    //取消加载框
    public Boolean dismissProgressDialog() {
        if (progressDialog != null){
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                return true;//取消成功
            }
        }
        return false;//已经取消过了，不需要取消
    }

    //本地查找是否有证(13.获取本地用户列表）
    public void getNativeUserList(Context con,String name,String idcard,String phone){//name为上一页面传进来的姓名
        GetUserListResult listResult=SignetToolApi.getUserList(getBaseContext());
        Log.d("证件数",listResult.getUserListMap().size()+"");
        if (listResult.getUserListMap().containsValue(name)){
            Log.d("本地用户",listResult.getUserListMap().toString());
            msspId=getKey(listResult.getUserListMap(),name);
            Log.d("获得mssid",msspId);
            dismissProgressDialog();
            //如果有证，直接签名
        }else {
            //本地没证，api获取状态
            getUserState(con,name,idcard,phone);
        }
    }


    //api获取用户状态
    public void getUserState(final Context con, final String name, final String idcard, final String phone){
        token= SpUtils.getInstance(this).getString("token",null);
        String jsonStr="{\n" +
                "    \"idCardType\":\"SF\",\n" +
                "    \"idCard\":\""+idcard+"\"}";
        HttpRequest.build(getBaseContext(), netConstant.getUserInfoandStateURL())
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+token)
                .setJsonParameter(jsonStr)
                .setJsonResponseListener(new JsonResponseListener() {
                    @Override
                    public void onResponse(JsonMap main, Exception error) {
                        Log.d("aa",main.toString());
                        if (main.getString("code").equals("200")){
                            JsonMap data=main.getJsonMap("data");
                            if (data.getString("keyID").equals("null")){
                                //如果keyid不存在，说明该用户没有注册，调用注册sdk
                                dismissProgressDialog();
                                Log.d("getUserState","sda");
                                getNewRegiterInfo(con,name,idcard,phone);
                            }else {
                                //keyid存在，调用重新注册sdk
                                dismissProgressDialog();
                                Log.d("getUserState","找回密码");
                                findRegister(con,name,idcard);

                            }
                        }else {
                            Log.d("获取用户状态","失败");
                        }
                    }
                })
                .doPost();
    }


    //调用注册SDK
    private void newRegister(Context con,String code){
        SignetCoreApi.useCoreFunc(new RegisterCallBack(con,code,RegisterType.COORDINATE) {
            @Override
            public void onRegisterResult(RegisterResult registerResult) {
                Log.d("msspID",registerResult.getMsspID());
            }
        });
    }

    //找回证书SDK
    private void findRegister(Context con,String name,String idCard){
        SignetCoreApi.useCoreFunc(new FindBackUserCallBack(con,name,idCard, IdCardType.SF) {
            @Override
            public void onFindBackResult(FindBackUserResult findBackUserResult) {
                Log.d("msspID",findBackUserResult.toString());
            }
        });
    }

    //调用ca添加用户接口 api 并且调用注册
    private void getNewRegiterInfo(final Context con, String name, String idcard, String phone){

        String jsonStr="{\n" +
                "    \"name\":\""+name+"\",\n" +
                "    \"idType\":\"SF\",\n" +
                "    \"idCardNum\":\""+idcard+"\",\n" +
                "    \"mobile\":\""+phone+"\"\n" +
                "}";
        HttpRequest.build(getBaseContext(), netConstant.getAddTrustUserV2URL())
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+token)
                .setJsonParameter(jsonStr)
                .setJsonResponseListener(new JsonResponseListener() {
                    @Override
                    public void onResponse(JsonMap main, Exception error) {
                        Log.e("getNewRegiterInfo",main.toString());
                        if (main.getString("code").equals("200")){
                            JsonMap data=main.getJsonMap("data");
                            msspId=data.getString("msspId");
                            activeCode=data.getString("userQrCode");

                            //调用注册
                            newRegister(con,activeCode);

                            Log.d("activeCode",activeCode);
                        }else {
                            Log.d("添加用户接口调用","失败");
                        }
                    }
                })
                .doPost();

    }
    //手写签名
    public void handWriting(Context con){
        SignetCoreApi.useCoreFunc(
                new SetSignImageCallBack(con, msspId, SetSignImgType.SET_HANDWRITING) {

                    @Override
                    public void onSetSignImageResult(SignImageResult setSignImageResult) {
                        String src=setSignImageResult.getSignImageSrc();
                        Log.d("手写",setSignImageResult.getErrMsg());
                        Log.d("手写",setSignImageResult.getErrCode());
                    }

                });
    }



    //获取map中的key
    public String getKey(HashMap<String,String> map, String value){
        String key = null;
        //Map,HashMap并没有实现Iteratable接口.不能用于增强for循环.
        for(String getKey: map.keySet()){
            if(map.get(getKey).equals(value)){
                key = getKey;
            }
        }
        return key;
        //这个key肯定是最后一个满足该条件的key.
    }


    //上传pdf
    public void uploadPdf(Context con){
        HttpRequest.build(con,netConstant.getCloudSealUploadDocWithKeyIDURL())
                .setMediaType(baseokhttp3.MediaType.parse("application/pdf"))
                .addHeaders("Authorization","Bearer "+token)
                .addHeaders("Content-Type","multipart/form-data")
                .addParameter("notifyLetterFile",uploadFile)
                .addParameter("msspId",msspId)
                .setJsonResponseListener(new JsonResponseListener() {
                    @Override
                    public void onResponse(JsonMap main, Exception error) {
                        if (error!=null){
                            Log.d("上传","连接失败",error);
                        }else {
                            if (main.getString("code").equals("200")){
                                //上传成功
                                JsonMap jsonMap=main.getJsonMap("data");
                                signId=jsonMap.getString("signId");
                                Log.d("上传",signId);
                                dismissProgressDialog();
                            }else {
                                Log.e("上传",main.getString("msg"));
                                Log.e("上传",main.getString("code"));

                            }
                        }
                    }
                })
                .doPost();
    }
//电子签章
    public void signPdf(Context con){
        SignetCoreApi.useCoreFunc(new SignDataCallBack(con,msspId,signId) {
            @Override
            public void onSignDataResult(SignDataResult result) {
                Log.d("sign",result.getSignDataInfos().toString());
                Log.d("sign",result.getSignDataJobId());
            }
        });

    }
}
