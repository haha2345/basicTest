package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basictest.Class.User;
import com.example.basictest.base.BaseActivity;
import com.example.basictest.utils.Utils;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.google.gson.JsonObject;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_forgetPhone;
    private EditText et_forgetVcode;
    private TextView btn_forget_getvcode;
    private Button btn_forget_next;
    private TextView tv_forget_test;

    private String username;
    private String vcode;
    private String uuid;

    private User user = null;
    private Utils utils = null;

    private QMUITipDialog qmuiTipDialog;

    private String jsonStr;

    private String TAG = "ForgetPwdActivity";
    MyCountDownTimer2 myCountDownTimer;
    Intent intent = null;
    @BindView(R.id.topbar_forget)
    QMUITopBarLayout mTopBar;
    private QMUITipDialog tipDialog;
    private Context mContext = ForgetPwdActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        initViews();
        initTopBar();
        myCountDownTimer = new MyCountDownTimer2(60000, 1000);
        qmuiTipDialog=new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("请稍后")
                .create();

    }

    @SuppressLint("ResourceAsColor")
    private void initTopBar() {
        mTopBar.setBackgroundAlpha(255);
        mTopBar.addLeftImageButton(R.drawable.back, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        //设置标题名
        mTopBar.setTitle("修改密码");

    }

    private void initViews() {
        et_forgetPhone = findViewById(R.id.et_forgetPhone);
        et_forgetVcode = findViewById(R.id.et_forgetVcode);
        btn_forget_getvcode = findViewById(R.id.btn_forget_getvcode);
        btn_forget_next = findViewById(R.id.btn_forget_next);
        tv_forget_test = findViewById(R.id.tv_forget_test);
        utils = new Utils();
        btn_forget_next.setOnClickListener(this);
        btn_forget_getvcode.setOnClickListener(this);
        et_forgetVcode.addTextChangedListener(new TextWatcher() {

            CharSequence enterword;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                enterword=s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (enterword.length()==6){
                    btn_forget_next.setEnabled(true);
                }else {
                    btn_forget_next.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forget_getvcode:
                username = et_forgetPhone.getText().toString();
                if (username.isEmpty()) {
                    getTipDialog(QMUITipDialog.Builder.ICON_TYPE_INFO, "请输入用户名").show();
                    delayCloseTip();
                    break;
                }
                if (isTelphoneValid(username)) {
                    //测试qmui的提示框
                    //获取uuid和用户名
//                    btn_forget_next.setEnabled(true)
                    qmuiTipDialog.show();
                    getuuid();
                    myCountDownTimer.start();
                }else {
                    getTipDialog(QMUITipDialog.Builder.ICON_TYPE_INFO, "请输入正确的手机号").show();
                    delayCloseTip();
                }
                break;
            case R.id.btn_forget_next:
                vcode = et_forgetVcode.getText().toString();
                username=et_forgetPhone.getText().toString();

                if (username.isEmpty()) {
                    getTipDialog(QMUITipDialog.Builder.ICON_TYPE_INFO, "请输入手机号").show();
                    delayCloseTip();
                    break;
                }
                if (isTelphoneValid(username)&&(et_forgetVcode.getText().length()==6)) {
                    //checkVcode();
                    intent = new Intent(ForgetPwdActivity.this, ChangePwdActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("uuid", uuid);
                    intent.putExtra("vcode", vcode);
                    startActivity(intent);
                }else {
                    getTipDialog(QMUITipDialog.Builder.ICON_TYPE_INFO, "请检查输入").show();
                    delayCloseTip();
                }

                break;
        }

    }

    //get uuid
    private void getuuid() {
        HttpRequest.GET(ForgetPwdActivity.this, netConstant.getGetVcodeURL() + "?mobile=" + username, null, new ResponseListener() {
            @Override
            public void onResponse(String response, Exception error) {
                qmuiTipDialog.dismiss();
                if (error == null) {
                    //先判断是否正常
                    if (response.contains("200")) {
                        //解析json
                        JsonObject resultJson = utils.getJson(response);
                        //提取uuid
                        uuid = resultJson.get("uuid").getAsString();

                        //线程中无法直接使用toast
                        //测试用
                        utils.showToastInThread(ForgetPwdActivity.this, "已发送验证码，注意查收");
                    }
                } else {
                    Toast.makeText(ForgetPwdActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 校验账号不能为空且必须是中国大陆手机号（宽松模式匹配）
    private boolean isTelphoneValid(String account) {
        if (account == null) {
            return false;
        }
        // 首位为1, 第二位为3-9, 剩下九位为 0-9, 共11位数字
        String pattern = "^[1]([3-9])[0-9]{9}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(account);
        return m.matches();
    }
    //倒计时函数
    private class MyCountDownTimer2 extends CountDownTimer {

        public MyCountDownTimer2(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            btn_forget_getvcode.setClickable(false);
            btn_forget_getvcode.setText(l / 1000 + "秒后可再发送");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            btn_forget_getvcode.setText("重新获取");
            //设置可点击
            btn_forget_getvcode.setClickable(true);
        }
    }

    public QMUITipDialog getTipDialog(int type, String str) {
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(type)
                .setTipWord(str)
                .create();
        return tipDialog;
    }
    //1.5s后关闭tipDIalog
    public void delayCloseTip(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //要延时的程序
                tipDialog.dismiss();
            }
        },1500);
    }

}