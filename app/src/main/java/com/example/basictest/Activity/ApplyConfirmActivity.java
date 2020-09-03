package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.basictest.R;
import com.example.basictest.base.BaseActivity;
import com.example.basictest.constant.netConstant;
import com.example.basictest.utils.SpUtils;
import com.example.basictest.utils.Utils;
import com.example.basictest.utils.ValueConvertUtil;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplyConfirmActivity extends BaseActivity {

    private Bundle bundle;
    @BindView(R.id.topbar_apply_confirm)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.et_confirm_no)
    EditText et_confirm_no;
    @BindView(R.id.et_confirm_money)
    EditText et_confirm_money;
    @BindView(R.id.et_confirm_lilv)
    EditText et_confirm_lilv;
    @BindView(R.id.et_confirm_name_bank)
    EditText et_confirm_name_bank;
    @BindView(R.id.et_confirm_bank_no)
    EditText et_confirm_bank_no;
    @BindView(R.id.et_confirm_function)
    EditText et_confirm_function;
    @BindView(R.id.et_confirm_during)
    EditText et_confirm_during;
    @BindView(R.id.et_confirm_name)
    EditText et_confirm_name;
    @BindView(R.id.et_confirm_idcard_type)
    EditText et_confirm_idcard_type;
    @BindView(R.id.et_confirm_idcard)
    EditText et_confirm_idcard;
    @BindView(R.id.et_confirm_loacte)
    EditText et_confirm_loacte;
    @BindView(R.id.et_confirm_phone)
    EditText et_confirm_phone;
    @BindView(R.id.et_confirm_mail)
    EditText et_confirm_mail;
    @BindView(R.id.et_confirm_huanqian)
    EditText et_confirm_huanqian;
    @BindView(R.id.et_confirm_yongtu)
    EditText et_confirm_yongtu;
    @BindView(R.id.confirm_check)
    CheckBox confirm_check;
    @BindView(R.id.btn_confirm_next)
    Button btn_confirm_next;
    private File uploadFile;
    private String path,pdfStr;
    private String loanCode,loanName,loanUserName,loanUserMobile,loanUserIdnum,loanFromTo,loanTime,contentInfo,money,lilv,daikuanren,yongtu,huanqian;
    private String name_bank,bank_no,get_function,id_type,locate,mail,caseId,caseCode,userId;
    private Long coId,loanMoney;
    private Double loanRatio;
    private String TAG="ApplyConfirmActivity";
    private ProgressDialog progressDialog;
    private Context mContext=ApplyConfirmActivity.this;
    Utils utils=new Utils();
    QMUITipDialog tipDialog;
    private List<EditText> editTextList;
    int flag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_confirm);
        ButterKnife.bind(this);
        initTopBar();
        initData();
        initView();
    }

    private void initView() {
        et_confirm_no.setText(loanCode);
        et_confirm_money.setText(new StringBuilder().append("¥").append(loanMoney).append("（").append(money).append("）").toString());
        et_confirm_lilv.setText(lilv);
        et_confirm_name_bank.setText(name_bank);
        et_confirm_bank_no.setText(bank_no);
        et_confirm_function.setText(get_function);
        et_confirm_during.setText(loanFromTo);
        et_confirm_name.setText(loanUserName);
        et_confirm_idcard_type.setText(id_type);
        et_confirm_idcard.setText(loanUserIdnum);
        et_confirm_loacte.setText(locate);
        et_confirm_phone.setText(loanUserMobile);
        et_confirm_mail.setText(mail);
        et_confirm_huanqian.setText(huanqian);
        et_confirm_yongtu.setText(yongtu);

        editTextList=new ArrayList<>();
        editTextList.add(et_confirm_no);
        editTextList.add(et_confirm_money);
        editTextList.add(et_confirm_lilv);
        editTextList.add(et_confirm_name_bank);
        editTextList.add(et_confirm_bank_no);
        editTextList.add(et_confirm_function);
        editTextList.add(et_confirm_during);
        editTextList.add(et_confirm_name);
        editTextList.add(et_confirm_idcard_type);
        editTextList.add(et_confirm_idcard);
        editTextList.add(et_confirm_loacte);
        editTextList.add(et_confirm_phone);
        editTextList.add(et_confirm_mail);
        editTextList.add(et_confirm_yongtu);
        editTextList.add(et_confirm_huanqian);


        initCheckBox();

        btn_confirm_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(mContext,"请稍后");
                getEditBoxContent();
                upload();
            }
        });
        setOnFocusChangeErrMsg(et_confirm_no,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_confirm_money,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_confirm_lilv,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_confirm_name_bank,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_confirm_bank_no,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_confirm_function,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_confirm_during,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_confirm_name,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_confirm_idcard_type,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_confirm_idcard,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_confirm_loacte,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_confirm_phone,"phone","格式不正确");
        setOnFocusChangeErrMsg(et_confirm_mail,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_confirm_yongtu,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_confirm_huanqian,"confirm","不能为空");

    }

    private void getEditBoxContent() {
        loanCode=et_confirm_no.getText().toString().trim();
        loanUserName=et_confirm_name.getText().toString().trim();
        loanUserMobile=et_confirm_phone.getText().toString().trim();
        loanUserIdnum=et_confirm_idcard.getText().toString().trim();
        loanMoney= Long.parseLong(extraAttr(et_confirm_money.getText().toString().trim(),"¥","（"));
        String temp=et_confirm_lilv.getText().toString().trim();
        loanRatio= Double.valueOf(extraAttr(temp,"日利率","%"));
        loanFromTo=et_confirm_during.getText().toString().trim();
        Map<String,String> map=new HashMap<>();
        map.put("贷款合同号",loanCode);
        map.put("coId", String.valueOf(coId));
        map.put("合同名",loanName);
        map.put("借款人",loanUserName);
        map.put("手机号码",loanUserMobile);
        map.put("证件号码",loanUserIdnum);
        map.put("贷款金额", String.valueOf(loanMoney*100));
        map.put("最终利率", String.valueOf(loanRatio));
        map.put("贷款期限",loanFromTo);
        map.put("放/还款账户户名",name_bank);
        map.put("放/还款账户账号获卡号",bank_no);
        map.put("提款方式",get_function);
        map.put("证件种类",id_type);
        map.put("家庭住址",locate);
        map.put("电子邮件地址",mail);
        map.put("还款方式",huanqian);
        map.put("贷款用途",yongtu);
        map.put("loanTime",loanTime);
        contentInfo= JSON.toJSONString(map);
    }

    private boolean checkNull(EditText editText){
        if (editText.getText().toString().trim().isEmpty()){
            editText.requestFocus();
            Toast.makeText(mContext,"请填写好您的信息",Toast.LENGTH_SHORT).show();
            return true;
        }else
            return false;
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
        mTopBar.setTitle("确认合同信息");

    }

    private void initData(){
        bundle = getIntent().getExtras();
        if (bundle!=null){
            path=bundle.getString("firstuploadfilepath");
            coId=bundle.getLong("coid");
            //初始化文件
            uploadFile=new File(path);
            pdfStr=bundle.getString("pdfstr");
            //解析pdf
            loanCode=extraAttr(pdfStr,"贷款合同号：","“惠民贷”");
            loanName=extraAttr(pdfStr,"请认真阅读《","》，特别");
            loanUserName=extraAttr(pdfStr,"借款人：","证件种类");
            loanUserMobile=extraAttr(pdfStr,"手机号码：","电子邮件地址");
            loanUserIdnum=extraAttr(pdfStr,"证件号码：","家庭地址");
            loanFromTo=extraAttr(pdfStr,"贷款期限：","。");
            loanTime=extraAttr(pdfStr,"签署日期：");
            name_bank=extraAttr(pdfStr,"放款/还款账户户名：","2.2");
            bank_no=extraAttr(pdfStr,"账户账号或卡号：","2.3");
            get_function=extraAttr(pdfStr,"3提款方式：","2.4");
            id_type=extraAttr(pdfStr,"证件种类：","证件号码");
            locate=extraAttr(pdfStr,"家庭地址：","工作单位地址");
            mail=extraAttr(pdfStr,"电子邮件地址：","其他联系方式");
            money=extraAttr(pdfStr,"人民币（大写）","元。");
            loanMoney=Long.parseLong(ValueConvertUtil.formatAmount(money));
            loanRatio=Double.parseDouble(extraAttr(pdfStr,"放款利率：日利率","%"));
            lilv=extraAttr(pdfStr,"放款利率：","第二条");
            daikuanren=extraAttr(pdfStr,"贷款人： ","通讯地址");
            yongtu=extraAttr(pdfStr,"贷款用途：","第三条");
            huanqian=extraAttr(pdfStr,"还款方式：","3.2贷款期限");

            //Log.d(TAG, "initData: "+loanCode);

        }



    }

    private void initCheckBox(){
        confirm_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                for (EditText editText : editTextList) {
                    if (checkNull(editText)){
                        flag=0;
                        break;
                    }else
                        flag=1;
                }
                if(isChecked){
                    if (flag==1){
                        btn_confirm_next.setEnabled(true);
                    }else
                        btn_confirm_next.setEnabled(false);
                }else{
                    btn_confirm_next.setEnabled(false);
                }

            }
        });
    }

    //上传
    private void upload(){
        HttpRequest.build(ApplyConfirmActivity.this, netConstant.getUploadEContractURL())
                .setMediaType(baseokhttp3.MediaType.parse("application/pdf"))
                .addHeaders("Authorization","Bearer "+ SpUtils.getInstance(ApplyConfirmActivity.this).getString("token",null))
                .addHeaders("Content-Type","multipart/form-data")
                .addParameter("eContractFile",uploadFile)
                .addParameter("coId",coId)
                .addParameter("loanCode",loanCode)
                .addParameter("loanName",loanName)
                .addParameter("loanUserName",loanUserName)
                .addParameter("loanUserMobile",loanUserMobile)
                .addParameter("loanUserIdnum",loanUserIdnum)
                .addParameter("loanMoney",loanMoney*100)
                .addParameter("loanRatio",loanRatio)
                .addParameter("loanFromTo",loanFromTo)
                .addParameter("loanTime",loanTime)
                .addParameter("contentInfo",contentInfo)
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
                                Log.d("获取到的上传信息",caseId+"  "+caseCode+"  "+userId);
                                /**
                                 * 跳转*/
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


    private void setOnFocusChangeErrMsg(final EditText editText, final String inputType, final String errMsg) {
        editText.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        String inputStr = editText.getText().toString();
                        if (!hasFocus) {
                            if (inputType == "phone") {
                                if (isTelphoneValid(inputStr)) {
                                    editText.setError(null);
                                } else {
                                    editText.setError(errMsg);
                                }
                            }
                            if (inputType.equals("confirm")){
                                if (editText.getText().toString().trim().equals("")){
                                    editText.setError(errMsg);
                                }else {
                                    editText.setError(null);
                                }
                            }
                        }
                    }
                }
        );
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
    private void jumpToApply2() {
        Intent intent=new Intent(mContext,Apply2edActivity.class);
        bundle.putString("caseid", caseId);
        bundle.putString("userid", userId);
        bundle.putString("casecode", caseCode);
        bundle.putString("contractmoney", String.valueOf("¥"+loanMoney+".00"));
        bundle.putString("contractname", loanName);
        bundle.putString("contractno", loanCode);
        bundle.putString("contracttime", loanTime);
        bundle.putString("daikuanren", daikuanren);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    //提取String中需要的字符串
    private String extraAttr(String str, String pre, String end) {
        int prePos, endPos;
        prePos = str.indexOf(pre);
        str = str.substring(prePos);
        endPos = str.indexOf(end);
        String value = str.substring(pre.length(), endPos).trim();
        value = value.replace(" ", "");
        value = value.replace("，", "");
        value = value.replace("。", "");
        return value;
    }
    private String extraAttr(String str, String pre) {
        int prePos;
        prePos = str.indexOf(pre);
        String value = str.substring(prePos+pre.length());
        value = value.replace(" ", "");
        value = value.replace("\n", "");
        return value;
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
        }, 2500);
    }
}