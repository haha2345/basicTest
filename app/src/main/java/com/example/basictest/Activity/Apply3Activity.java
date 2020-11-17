package com.example.basictest.Activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.example.basictest.R;
import com.example.basictest.base.BaseApply3Activity;
import com.example.basictest.constant.netConstant;
import com.example.basictest.utils.SpUtils;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;


import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.org.bjca.signet.component.core.activity.SignetCoreApi;

import cn.org.bjca.signet.component.core.bean.results.SignImageResult;

import cn.org.bjca.signet.component.core.callback.SetSignImageCallBack;

import cn.org.bjca.signet.component.core.enums.SetSignImgType;

public class Apply3Activity extends BaseApply3Activity {
    private QMUITipDialog tipDialog;
    @BindView(R.id.tv_apply3_name)
    TextView tv_apply3_name;
    @BindView(R.id.tv_apply3_name1)
    TextView tv_apply3_name1;
    @BindView(R.id.tv_apply3_name2)
    TextView tv_apply3_name2;
    @BindView(R.id.tv_apply3_bank)
    TextView tv_apply3_bank;
    @BindView(R.id.lv_apply3)
    LinearLayout lv_apply3;
    //初始化下面两个界面
    @BindView(R.id.lv_apply3_auto)
    LinearLayout lv_apply3_auto;
    @BindView(R.id.rv_apply3_auto)
    RelativeLayout rv_apply3_auto;
    @BindView(R.id.iv_apply3_yes)
    ImageView iv_apply3_yes;
    @BindView(R.id.im_apply_auto)
    ImageView im_apply_auto;
    @BindView(R.id.tv_apply3_auto_date)
    TextView tv_apply3_auto_date;
    @BindView(R.id.tv_apply3_reauto)
    TextView tv_apply3_reauto;
    @BindView(R.id.iv_apply3_yes1)
    ImageView iv_apply3_yes1;
    //生成pdf的组件
    @BindView(R.id.sign_image)
    ImageView sign_image;
    @BindView(R.id.sign_date)
    TextView sign_date;
    @BindView(R.id.re_sign)
    RelativeLayout re_sign;

    @BindView(R.id.lv_apply3_record)
    LinearLayout lv_apply3_record;
    //这是录像成功后的界面
    @BindView(R.id.rv_apply3_record)
    RelativeLayout rv_apply3_record;
    @BindView(R.id.im_apply_record)
    ImageView im_apply_record;
    @BindView(R.id.tv_apply3_re_record)
    TextView tv_apply3_re_record;
    @BindView(R.id.tv_apply3_record_date)
    TextView tv_apply3_record_date;
    @BindView(R.id.tv_apply3_content1)
    TextView tv_apply3_content1;
    @BindView(R.id.tv_apply3_content2)
    TextView tv_apply3_content2;
    @BindView(R.id.tv_apply3_file_name1)
    TextView tv_apply3_file_name1;
    @BindView(R.id.tv_apply3_file_no)
    TextView tv_apply3_file_no;
    @BindView(R.id.tv_apply3_money)
    TextView tv_apply3_money;
    @BindView(R.id.tv_apply3_name3)
    TextView tv_apply3_name3;
    //    @BindView(R.id.tv_apply3_file_name2)
//    TextView tv_apply3_file_name2;
    @BindView(R.id.tv_apply3_content3)
    TextView tv_apply3_content3;

    @BindView(R.id.et_apply3_name)
    EditText et_apply3_name;
    @BindView(R.id.et_apply3_locate)
    EditText et_apply3_locate;
    @BindView(R.id.et_apply3_phone)
    EditText et_apply3_phone;
    @BindView(R.id.et_apply3_mail)
    EditText et_apply3_mail;
    @BindView(R.id.et_apply3_chuanzhen)
    EditText et_apply3_chuanzhen;
    @BindView(R.id.et_apply3_guding)
    EditText et_apply3_guding;
    @BindView(R.id.checkbox_apply3)
    CheckBox checkbox_apply3;


    //初始化按钮
    @BindView(R.id.sbtn_apply3_next)
    Button sbtn_apply3_next;

    @BindView(R.id.topbar_apply3)
    QMUITopBarLayout mTopBar;
    private String autoDate= null,recordDate= null;

    private Bundle bundle;

    private Intent intent;
    private String money,contractName,contractNo,contractTime,daikuanren;
    private String et_name,et_phone,et_mail,et_locate,et_chuanzhen,et_guding;
    private String name = null,
            bank = null,
            videoPath = null,
            imagePath = null,
            phone = null,
            idcard = null;
    private Context mContext = Apply3Activity.this;
    //    手写签名图片
    private Bitmap handWritingBitmap = null;
    private String src = null;
    //    checkbox状态 需记录
    private String checkState="false";
    private String time;

//    int flag=0;
//    private List<EditText> editTextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply3);
        ButterKnife.bind(this);
        bundle=getIntent().getExtras();
        initTopBar();
        initView();
//        initList();
        initBtn();
        if (checkState.equals("false")){
            initCheckBox();
        }
        //第一步，检测是否有证
        if (idcard != null) {
            showProgressDialog(mContext, "请稍后。。。");
            getNativeUserList(mContext, name, idcard, phone);
            //第二步，添加证书
        } else {
            getTipDialog(3, "请检查上一步是否有问题");
            delayCloseTip();
            finish();
        }

//        initView();

        //从别的页面跳转回来不会调用onCreate，只会调用onRestart、onStart、onResume
    }
    /*
        private void initList(){
            editTextList=new ArrayList<>();
            editTextList.add(et_apply3_name);
            editTextList.add(et_apply3_locate);
            editTextList.add(et_apply3_phone);
            editTextList.add(et_apply3_mail);
            editTextList.add(et_apply3_chuanzhen);
            editTextList.add(et_apply3_guding);


        }*/
    //初始化页面+数据
    private void initView() {


        //从bundle中获取信息
        /**初始化编辑框中的数据*/
        et_chuanzhen = bundle.getString("et_chuanzhen");
        et_guding = bundle.getString("et_guding");
        et_locate = bundle.getString("et_locate");
        et_mail = bundle.getString("et_mail");
        et_name = bundle.getString("et_name");
        et_phone = bundle.getString("et_phone");

        /**初始化文本信息*/
        name = bundle.getString("name");
        bank = SpUtils.getInstance(this).getString("bank", null);
        idcard = bundle.getString("idcard");
        phone = bundle.getString("phone");



        money = bundle.getString("contractmoney");
        contractName = bundle.getString("contractname");
        contractNo = bundle.getString("contractno");
        daikuanren = bundle.getString("daikuanren");
//        获取到的时间格式是yyyy-MM-dd
        contractTime = bundle.getString("contracttime");
//        时间转码
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date=format.parse(contractTime);
            time = new SimpleDateFormat("yyyy年MM月dd日").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //获取复选框的状态
        checkState=bundle.getString("checkstate");
        if (checkState==null) {
            checkState = "false";
        }
        else {
            if (checkState.equals("true")){
                checkbox_apply3.setChecked(true);
            }else {
                checkbox_apply3.setChecked(false);
            }
        }

/*        name = getIntent().getStringExtra("name");
        bank = SpUtils.getInstance(this).getString("bank", null);
        idcard = getIntent().getStringExtra("idcard");
        phone = getIntent().getStringExtra("phone");*/
        autoDate=SpUtils.getInstance(this).getString("autodate", null);
        recordDate=SpUtils.getInstance(this).getString("recorddate", null);

        if (et_phone!=null){
            et_apply3_phone.setText(et_phone);
        }
        if (et_guding!=null){
            et_apply3_guding.setText(et_guding);
        }
        if (et_chuanzhen!=null){
            et_apply3_chuanzhen.setText(et_chuanzhen);
        }
        if (et_mail!=null){
            et_apply3_mail.setText(et_mail);
        }
        if (et_locate!=null){
            et_apply3_locate.setText(et_locate);
        }
        if (et_name!=null){
            et_apply3_name.setText(et_name);
        }

        tv_apply3_name.setText(name);
        tv_apply3_name1.setText(name);
        tv_apply3_name2.setText(name);
        tv_apply3_name3.setText(Html.fromHtml(name+""+contractName));
        tv_apply3_bank.setText(bank);
        tv_apply3_file_name1.setText(contractName);
//        tv_apply3_file_name2.setText(contractName);
        tv_apply3_file_no.setText(contractNo);
        tv_apply3_money.setText(money+"元");
        tv_apply3_content1.setText(Html.fromHtml("公证处依据《中华人民共和国公证法》第27条第二款之规定，现将你申办赋予债权文书强制执行效力公证的法律意义、法律后果及相关的一些法律术语告知和解释如下：<br/>\n" +
                "1、公证的含义：公证是公证机构根据自然人、法人或者其他组织的申请，依照法定程序对民事法律行为、有法律意义的事实和文书的真实性、合法性予以证明的活动。<br/>\n" +
                "2、《公证法》第44条规定：提供虚假证明材料、骗取公证书，违反治安管理的，依法给予治安管理处罚；构成犯罪的，依法追究刑事责任。<br/>\n" +
                "3、在办理公证过程中，您主要享有以下权利:<br/>\n" +
                "⑴如果你认为承办公证员以及公证机构的其他参与办理该项公证的人员与你或与你申办的公证事项有利害关系，你有权要求他们回避；<br/>\n" +
                "⑵你有权要求公证机构和公证员保守在办证过程中所知悉的秘密或隐私；<br/>\n" +
                "⑶你有权要求公证机构自受理之日起15个工作日内出具公证书，但因不可抗力、补充证明材料或者需要核实有关情况所需的时间除外；<br/>\n" +
                "⑷在办理公证过程中，你对办理公证的相关材料内容和办理公证的法律意义、法律后果有不清楚之处，你有权要求公证员进行告知和解释；<br/>\n" +
                "⑸你有权要求公证员修改询问笔录，并在修改处按指印确认；<br/>\n" +
                "⑹在符合法律援助条件时，你有权申请减、免公证费；<br/>\n" +
                "⑺在公证机构出具公证书之前，你有权撤回公证申请；<br/>\n" +
                "⑻如您对出具的公证书认为有错误的，请自收到公证书之日起六十日内，向本处书面复查申请，并提供相关证明材料。<br/>\n" +
                "4、在办理公证过程中，您应当承担以下义务:<br/>\n" +
                "⑴你有义务向公证机构做出如实的陈述和提供合法、真实、充分的证据。如果你不能提供真实、合法、充分的证据，依据《公证程序规则》第48条六款的规定，公证机构不予办理公证。如果因为你所做的陈述不真实，或者提供的材料虚假，或者隐瞒、遗漏重要事实，公证机构有权拒绝出具公证书，或者撤销已经出具的公证书，并要求你承担相应的法律责任；<br/>\n" +
                "⑵你有义务按照国家规定的标准交纳公证费以及支付办证过程中发生的翻译费、副本费等费用。对您申请的强制执行公证，特向您告知如下：<br/>\n" +
                "1、法律意义：根据法律、法规的规定，当事人申请办理赋予强制执行效力的公证书后，就放弃了通过诉讼（或仲裁）解决纠纷的权利，使得债权人提前取得了类似法院判决一样的执行依据。在债务人不履行合同，或不完全履行合同时，债权人不必再向法院提起诉讼，可以直接申请法院对债务人、担保人强制执行，为此请各方认真考虑申请办理本公证后所承担的法律后果。<br/>\n" +
                "2、申请强制执行的期限：申请执行的期间为二年，申请执行时效的中止、中断，适用法律有关诉讼时效中止、中断的规定（在诉讼时效期间的最后六个月内，因不可抗力或者其他障碍不能行使请求权的，诉讼时效中止。从中止时效的原因消除之日起，诉讼时效期间继续计算。诉讼时效因提起诉讼、当事人一方提出要求或者同意履行义务而中断，从中断时起，诉讼时效期间重新计算）。前款规定的期间，从法律文书规定履行期间的最后一日起计算；法律文书规定分期履行的，从规定的每次履行期间的最后一日起计算；法律文书未规定履行期间的，从法律文书生效之日起计算。如发生债务人逾期未履行或未完全履行到期债务情况时，请债权人在法定的期限内，向人民法院申请强制执行，逾期申请的自行承担法律后果。<br/>\n" +
                "3、《执行证书》的申办：债务人逾期未履行或未完全履行到期债务时，债权人有权向公证机构申请出具《执行证书》，启动强制执行程序。申请时债权人应当如实向公证机构提交《借款凭证》（借据）及公证机构要求的相关材料，由公证机构根据相关规定或债权人、债务人及担保人约定的方式核实债务履行情况。公证机构按照当事人约定的方式进行核实时，无法与债务人（包括担保人）取得联系时，或债务人（包括担保人）未按约定方式回复，或债务人（包括担保人）回复时提出异议但未能提出充分证明材料，不影响公证机构按照法律程序出具执行证书。经公证机构核实，未发现有证据证明债务人已全部或部分履行到期债务时，公证机构有权依据赋予强制执行效力债权文书的公证书和甲方提交的相关材料出具《执行证书》。如果公证机构核实时发现有证据对债权人的主张进行抗辩，经公证机构审查确认，则公证机构有权酌情认定债权债务的真实数额以及其他事实并出具《执行证书》，或者不予出具《执行证书》。在公证机构不予出具《执行证书》时，各方即可向有管辖权的人民法院申请诉讼解决债务纠纷。<br/>\n" +
                "4、债务人、担保人愿意接受强制执行的承诺：根据您所签订的债权文书的约定，如果发生债务人逾期未履行或未完全履行到期债务的情况，债务人、担保人应自愿接受人民法院根据公证处出具的执行证书依法采取的强制执行措施，并承担债权人为实现债权所发生的一切费用（包括但不限于催收费用、邮寄费、保全费、公告费、执行费、律师费、公证费、差旅费、交通费、通讯费、鉴定费、拍卖费及一切实现债权所支出的费用）。<br/>\n" +
                "5、按照2008年12月8日最高人民法院关于强制执行的批复，经公证的以给付为内容并载明债务人愿意接受强制执行承诺的债权文书依法具有强制执行效力。债权人或者债务人对该债权文书的内容有争议直接到人民法院提起民事诉讼的，人民法院不予受理。<br/>\n" +
                "6、有下列情况的，本处不能出具执行证书：1.据以发生抵押贷款的基础交易行为无效或被撤销；2.贷款人不能正常履约， 借款人、抵押人提出抗辩；3.借款人、抵押人身份不真实；4.借款人、抵押人死亡，未办理完毕继承手续或保证人法人资格终止；5.抵押人不是上述抵押物的合法所有人；6.公证书未送达给借款人、抵押人。<br/>\n" +
                "青岛市琴岛公证处<br/>\n" +
                "本处特别提示：请您在本告知书上签字（按指印）之前务必认真阅读本告知书。如果您有疑问或者异议，可以要求本处工作人员作出解答；如果您阅读本告知书有困难，可以要求本处工作人员向您宣读。经您签字或盖章、按指印确认后，本告知书将存入本处的公证档案，作为您已经知悉本告知书所载明内容，并承担相应义务的证据。<br/>"));
        tv_apply3_content2.setText(Html.fromHtml("&ensp; &ensp; &ensp; &ensp; " +
                " &emsp;&emsp;借款人于<b><u>"+time+"</u></b>与<b><u>"+daikuanren+"</u></b>\n" +
                " 签订了编号： <u>"+contractNo+"&emsp《"+contractName+"》</u>（以下简称“合同”），并自愿向\n" +
                " &ensp;山东省青岛市琴岛公证处申请办理赋予上述合同强制执行效力公证。借款人已完全知悉强制\n" +
                " 执行效力公证的法律意义和法律后果，现自愿承诺：<br/>\n" +
                "&emsp;&emsp;<u> 在本合同经山东省青岛市琴岛公证处公证并赋予强制执行效力后，若借款人不履行或不完全履行本合同约定的还款义务，则贷款人有权向原公证机构申请执行证书，并凭原公证书及执行证书向有管辖权的人民法院申请强制执行，借款人自愿接受人民法院的强制执行。</u><br/>\n" +
                " &emsp;&emsp;<u> 借款人承担贷款人为实现债权所发生的一切费用，包括但不限于催收费用、邮寄费、保全费、公告费、执行费、律师费、公证费、差旅费、交通费、通讯费、鉴定费、拍卖费及一切实现债权所支出的费用。律师费在本人应还款数额（欠款本金及利息）的1%以内，本人无异议。</u><br/>\n" +
                "  &emsp;&emsp;<u>上述合同中关于公证赋予合同强制执行效力的约定与本承诺书不一致的，以本承诺书为准。 </u><br/>\n" +
                "  &emsp;&emsp;本承诺书作为上述合同的组成部分。 "));
        tv_apply3_content3.setText(Html.fromHtml("青岛市琴岛公证处：<br/>\n" +
                "&emsp;&emsp;根据《最高人民法院关于进一步推进案件繁简分流优化司法资源配置的若干意见》（法发〔2016〕21号）规定，“当事人在纠纷发生之前约定送达地址的，人民法院可以将该地址作为送达诉讼文书的确认地址。当事人起诉或者答辩时应当依照规定填写送达地址确认书。积极运用电子方式送达；当事人同意电子送达的，应当提供并确认传真号、电子信箱、微信号等电子送达地址……”，本人知悉上述规定的法律含义，并同意公证处参照该规定送达与公证事项有关文书。本人根据上述规定，确认本人公证申请表中所填联系方式为本人的送达地址。<br/>\n" +
                "&emsp;&emsp;本人不可撤销的承诺如下：如本人所申办的公证事项发生补充材料、中（终）止公证、复查、司法诉讼等情况，以上任一联系方式（包括手机短信）作为公证机构或人民法院送达（包括但不限于复查文书、诉讼文书等）法律文书地址。如本人联系方式发生变化，未告知公证机构的，不以该变化作为上述法律文书未获送达的抗辩理由。<br/>\n" +
                "&emsp;&emsp;本处特别提示：请您在本文件上签字（按指印）之前务必认真阅读本文件。如果您有疑问或者异议，可以要求本处工作人员作出解答；如果您阅读有困难，可以要求本处工作人员向您宣读。经您签字确认后，本承诺书将存入本处的公证档案，作为您已经知悉本承诺书所载明内容，并愿意承担相应义务的证据。"));
        //取录像信息
        src = bundle.getString("basestr");
        imagePath = bundle.getString("imagepath");
        videoPath = bundle.getString("videopath");
/*        src = getIntent().getStringExtra("base64str");
        imagePath = getIntent().getStringExtra("imagepath");
        videoPath = getIntent().getStringExtra("videopath");*/
        if (imagePath != null) {
            getRecord();
        }
        if (src != null) {
            handWritingBitmap = base64ToBitmap(src);
            getAuto();
        }

        setOnFocusChangeErrMsg(et_apply3_chuanzhen,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_apply3_guding,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_apply3_locate,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_apply3_mail,"mail","邮箱格式错误");
        setOnFocusChangeErrMsg(et_apply3_name,"confirm","不能为空");
        setOnFocusChangeErrMsg(et_apply3_phone,"phone","手机号格式错误");
    }


    //获取editbox中的数据
    private void getEditboxContent(){
        et_name=et_apply3_name.getText().toString().trim();
        et_mail=et_apply3_mail.getText().toString().trim();
        et_locate=et_apply3_locate.getText().toString().trim();
        et_phone=et_apply3_phone.getText().toString().trim();
        et_chuanzhen=et_apply3_chuanzhen.getText().toString().trim();
        et_guding=et_apply3_guding.getText().toString().trim();
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
        mTopBar.setTitle("赋强公证申请");
    }

    //按钮判断
    @Override
    protected void onResume() {
        super.onResume();
        //若二者都有即可进行下一步
        Log.d("看看check状态", "onResume: "+checkState);
        if (imagePath != null && src != null) {
            if (checkState.equals("true")) {
                sbtn_apply3_next.setEnabled(true);
                checkbox_apply3.setChecked(true);
                checkbox_apply3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        getEditboxContent();
                        if (isChecked) {
                            if (!et_name.isEmpty()&&!et_guding.isEmpty()&&!et_locate.isEmpty()&&!et_chuanzhen.isEmpty()&&isEmail(et_mail)&&isTelphoneValid(et_phone)){
                                sbtn_apply3_next.setEnabled(true);
                            }else {
                                checkbox_apply3.setChecked(false);
                                sbtn_apply3_next.setEnabled(false);
                                et_apply3_name.requestFocus();
                                ToastUtils.showShort("请检查输入是否有误");
//                                Toast.makeText(mContext,"请检查输入是否有误",Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            sbtn_apply3_next.setEnabled(false);
                        }
                    }
                });
            }else{
                sbtn_apply3_next.setEnabled(false);
                checkbox_apply3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        getEditboxContent();
                        if (isChecked) {
                            if (!et_name.isEmpty()&&!et_guding.isEmpty()&&!et_locate.isEmpty()&&!et_chuanzhen.isEmpty()&&isEmail(et_mail)&&isTelphoneValid(et_phone)){
                                sbtn_apply3_next.setEnabled(true);
                            }else {
                                checkbox_apply3.setChecked(false);
                                sbtn_apply3_next.setEnabled(false);
                                et_apply3_name.requestFocus();
                                ToastUtils.showShort("请检查输入是否有误");
//                                Toast.makeText(mContext,"请检查输入是否有误",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            sbtn_apply3_next.setEnabled(false);
                        }
                    }
                });
            }
        } else {
            sbtn_apply3_next.setEnabled(false);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    /**
     * 检测输入框是否为空*/
    private boolean checkNull(EditText editText){
        if (editText.getText().toString().trim().isEmpty()){
            editText.requestFocus();
            ToastUtils.showShort("请填写好您的信息");
//            Toast.makeText(mContext,"请填写好您的信息",Toast.LENGTH_SHORT).show();
            return true;
        }else
            return false;
    }
    /**
     * 按钮初始化
     * */
    private void initBtn() {

//        下一步
        sbtn_apply3_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getEditboxContent();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("arriveName", et_name);
                jsonObject.put("arriveAddress", et_locate);
                jsonObject.put("arriveMobile", et_phone);
                jsonObject.put("arriveMail", et_mail);
                jsonObject.put("arriveFax", et_chuanzhen);
                jsonObject.put("arriveTel", et_guding);
                String json = JSONObject.toJSONString(jsonObject);
//                这是generateNotifyAndUploadDoc接口要用的字段
                JSONObject jsonObject1=jsonObject;
                jsonObject1.put("userName",name);
                jsonObject1.put("coName",bank);
                jsonObject1.put("loanCode",contractNo);
                jsonObject1.put("loanName",contractName);
                jsonObject1.put("loanMoney",money+"元");
                jsonObject1.put("loanTime",time);
                jsonObject1.put("loanBank",daikuanren);
                String json1=JSONObject.toJSONString(jsonObject1);
/*                String json="    \"arriveName\":\"" +et_name+ "\",\n" +
                        "    \"arriveAddress\":\"" + et_locate + "\",\n" +
                        "    \"arriveMobile\":\"" + et_phone + "\",\n" +
                        "    \"arriveMail\":\"" + et_mail + "\",\n" +
                        "    \"arriveFax\":\"" + et_chuanzhen + "\",\n" +
                        "    \"arriveTel\":\"" + et_guding + "\"\n" +
                        "}";*/
//                if (!et_name.isEmpty()&&!et_guding.isEmpty()&&!et_locate.isEmpty()&&!et_chuanzhen.isEmpty()&&isEmail(et_mail)&&isTelphoneValid(et_phone)){
                showProgressDialog(mContext, "加载中");
                re_sign.setVisibility(View.VISIBLE);
                setupPdf(lv_apply3);
                re_sign.setVisibility(View.INVISIBLE);
                generateNotifyAndUploadDoc(mContext,json,json1);
                //延时
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            //要延时的程序
////                            uploadPdf(mContext,json);
//                        }
//                    }, 2000); //8000为毫秒单位

/*                }else {
                    et_apply3_name.requestFocus();
                    Toast.makeText(mContext,"请检查输入是否有误",Toast.LENGTH_SHORT).show();
                }*/


            }

        });
//        手写签名
        lv_apply3_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoDate=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis()));
                SpUtils.getInstance(mContext).setString("autodate",autoDate);
                handWriting(mContext);
            }
        });
        //点击录像
        lv_apply3_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordDate=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis()));
                SpUtils.getInstance(mContext).setString("recorddate",recordDate);
                getEditboxContent();
                //在视频公正页面和录像页面显示的文字
                String shiyan="我同意赋予"+contractName+"强制执行效力；我承诺一旦不按约定偿还债务，"+bank+"可要求公证处出具执行证书，法院可依据执行证书执行我本人的财产。";
                intent = new Intent(mContext, ShipingongzhenActivity.class);
/*                intent.putExtra("basestr", src);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("idcard", idcard); */
                bundle.putString("basestr", src);
                bundle.putString("checkstate", checkState);
                bundle.putString("et_chuanzhen", et_chuanzhen);
                bundle.putString("et_guding", et_guding);
                bundle.putString("et_locate", et_locate);
                bundle.putString("et_mail", et_mail);
                bundle.putString("et_name", et_name);
                bundle.putString("et_phone", et_phone);
                bundle.putString("shiyan",shiyan);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

//        重新签名
        tv_apply3_reauto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoDate=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis()));
                SpUtils.getInstance(mContext).setString("autodate",autoDate);
                reAuto();
            }
        });
//重新录像
        tv_apply3_re_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getEditboxContent();
                videoPath=SpUtils.getInstance(mContext).getString("videopath",null);
                imagePath=SpUtils.getInstance(mContext).getString("imagepath",null);
                File video=new File(videoPath);
                File image=new File(imagePath);
                deleteFile(video);
                deleteFile(image);
                recordDate=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis()));
                SpUtils.getInstance(mContext).setString("recorddate",recordDate);
                reRecord();
            }
        });

        im_apply_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(mContext, VideoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initCheckBox(){
        checkbox_apply3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                getEditboxContent();
                if(isChecked){
                    if (!et_name.isEmpty()&&!et_guding.isEmpty()&&!et_locate.isEmpty()&&!et_chuanzhen.isEmpty()&&isEmail(et_mail)&&isTelphoneValid(et_phone)){
                        checkState="true";
                    }else {
                        checkbox_apply3.setChecked(false);
                        checkState="false";
                        et_apply3_name.requestFocus();
                        ToastUtils.showShort("请检查输入是否有误");
//                        Toast.makeText(mContext,"请检查输入是否有误",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    checkState="false";
                }

            }
        });
    }
    //如果正常录像调用此方法
    private void getRecord() {


        iv_apply3_yes1.setVisibility(View.VISIBLE);
        rv_apply3_record.setVisibility(View.VISIBLE);
        lv_apply3_record.setVisibility(View.INVISIBLE);
        tv_apply3_record_date.setText(recordDate);
        im_apply_record.setImageURI(getImageContentUri(mContext, new File(imagePath)));
        sign_date.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis())));

    }

    //重新录像
    private void reRecord() {
        iv_apply3_yes1.setVisibility(View.INVISIBLE);
        rv_apply3_record.setVisibility(View.INVISIBLE);
        lv_apply3_record.setVisibility(View.VISIBLE);
        intent = new Intent(mContext, ShipingongzhenActivity.class);
/*        intent.putExtra("basestr", src);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        intent.putExtra("idcard", idcard); */
        bundle.putString("checkstate", checkState);
        bundle.putString("basestr", src);
        bundle.putString("et_chuanzhen", et_chuanzhen);
        bundle.putString("et_guding", et_guding);
        bundle.putString("et_locate", et_locate);
        bundle.putString("et_mail", et_mail);
        bundle.putString("et_name", et_name);
        bundle.putString("et_phone", et_phone);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //手写签名
    public void handWriting(Context con) {
        SignetCoreApi.useCoreFunc(
                new SetSignImageCallBack(con, msspId, SetSignImgType.SET_HANDWRITING) {

                    @Override
                    public void onSetSignImageResult(SignImageResult setSignImageResult) {
                        src = setSignImageResult.getSignImageSrc();
//                        Log.d("shouxie", src);
//                        Log.d("手写", setSignImageResult.getErrMsg());
//                        Log.d("手写", setSignImageResult.getErrCode());
                        handWritingBitmap = base64ToBitmap(src);
                        if (setSignImageResult.getErrCode() != "0x11000001") {
                            getAuto();
                        }
                    }

                });
    }

    private void getAuto() {

        tv_apply3_auto_date.setText(autoDate);
        iv_apply3_yes.setVisibility(View.VISIBLE);
        rv_apply3_auto.setVisibility(View.VISIBLE);
        lv_apply3_auto.setVisibility(View.INVISIBLE);
        im_apply_auto.setImageBitmap(handWritingBitmap);
        //设置签名
//        sign_image.setImageBitmap(handWritingBitmap);
        //暂时这么写
    }

    private void reAuto() {
        iv_apply3_yes.setVisibility(View.INVISIBLE);
        rv_apply3_auto.setVisibility(View.INVISIBLE);
        lv_apply3_auto.setVisibility(View.VISIBLE);
        handWriting(mContext);
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
                            if (inputType == "mail") {
                                if (isEmail(inputStr)) {
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

    /**判断邮箱是否正确*/
    private boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //要延时的程序
                if (tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
            }
        }, 1500); //8000为毫秒单位
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                //要延时的程序
//                if (tipDialog.isShowing()){
//                    tipDialog.dismiss();
//                }
//
//            }
//        },1500);
    }

}