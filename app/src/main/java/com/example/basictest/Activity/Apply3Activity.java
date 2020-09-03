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
    @BindView(R.id.tv_apply3_file_name2)
    TextView tv_apply3_file_name2;

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
    private Bitmap handWritingBitmap = null;
    private String src = null;
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
        tv_apply3_name3.setText(name);
        tv_apply3_bank.setText(bank);
        tv_apply3_file_name1.setText(contractName);
        tv_apply3_file_name2.setText(contractName);
        tv_apply3_file_no.setText(contractNo);
        tv_apply3_money.setText(money);
        tv_apply3_content1.setText(getResources().getString(R.string.gaozhishu));
        tv_apply3_content2.setText(Html.fromHtml(" &emsp;&emsp;借款人于<b><u>"+time+"</u></b>与<b><u>"+daikuanren+"</u></b>\n" +
                " 签订了编号： <u>"+contractNo+"&emsp《"+contractName+"》</u>（以下简称“合同”），并自愿向\n" +
                " 山东省青岛市琴岛公证处申请办理赋予上述合同强制执行效力公证。借款人已完全知悉强制\n" +
                " 执行效力公证的法律意义和法律后果，现自愿承诺<br/>\n" +
                "&emsp;&emsp;<u> 在本合同经山东省青岛市琴岛公证处公证并赋予强制执行效力后，若借款人不履行或不完全履行本合同约定的还款义务，则贷款人有权向原公证机构申请执行证书，并凭原公证书及执行证书向有管辖权的人民法院申请强制执行，借款人自愿接受人民法院的强制执行。</u><br/>\n" +
                " &emsp;&emsp;<u> 借款人承担贷款人为实现债权所发生的一切费用，包括但不限于催收费用、邮寄费、保全费、公告费、执行费、律师费、公证费、差旅费、交通费、通讯费、鉴定费、拍卖费及一切实现债权所支出的费用。律师费在本人应还款数额（欠款本金及利息）的1%以内，本人无异议。</u><br/>\n" +
                "  &emsp;&emsp;<u>上述合同中关于公证赋予合同强制执行效力的约定与本承诺书不一致的，以本承诺书为准。 </u><br/>\n" +
                "  &emsp;&emsp;本承诺书作为上述合同的组成部分。 "));
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
                                Toast.makeText(mContext,"请检查输入是否有误",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(mContext,"请检查输入是否有误",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(mContext,"请填写好您的信息",Toast.LENGTH_SHORT).show();
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
                    //延时
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //要延时的程序
                            uploadPdf(mContext,json);
                        }
                    }, 2000); //8000为毫秒单位

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
                        Toast.makeText(mContext,"请检查输入是否有误",Toast.LENGTH_SHORT).show();
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