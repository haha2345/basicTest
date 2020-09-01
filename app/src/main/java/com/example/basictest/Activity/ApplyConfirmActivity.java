package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.basictest.R;
import com.example.basictest.utils.ValueConvertUtil;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplyConfirmActivity extends AppCompatActivity {

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
    @BindView(R.id.confirm_check)
    CheckBox confirm_check;
    @BindView(R.id.btn_confirm_next)
    Button btn_confirm_next;
    private File uploadFile;
    private String path,pdfStr;
    private String loanCode,loanName,loanUserName,loanUserMobile,loanUserIdnum,loanFromTo,loanTime,contentInfo,money,lilv;
    private String name_bank,bank_no,get_function,id_type,locate,mail;
    private Long coId,loanMoney;
    private Double loanRatio;
    private String TAG="ApplyConfirmActivity";


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
            //Log.d(TAG, "initData: "+loanCode);

        }
        if (path!=null)
            Log.d(TAG, "initData: path:"+path);

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
        String value = str.substring(prePos);
        return value;
    }

}