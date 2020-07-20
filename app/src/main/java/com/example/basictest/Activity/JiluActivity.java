package com.example.basictest.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.basictest.Adapter.JiluFragmentPagerAdapter;
import com.example.basictest.Fragments.JiluFragment;
import com.example.basictest.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JiluActivity extends AppCompatActivity {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;

    private Map<ContentPage, View> mPageMap = new HashMap<>();
    private ContentPage mDestPage = ContentPage.Item1;

    private List<Fragment> fragments;



//    private PagerAdapter mPagerAdapter=new PagerAdapter() {
//        @Override
//        public int getCount() {
//            return ContentPage.SIZE;
//        }
//
//        @Override
//        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//            return view==object;
//        }
//
//        @NonNull
//        @Override
//        public Object instantiateItem(@NonNull ViewGroup container, int position) {
//            ContentPage page = ContentPage.getPage(position);
//            View view = getPageView(page);
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            container.addView(view, params);
//            return view;
//        }
//
//        @Override
//        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            container.removeView((View)object);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jilu);
        ButterKnife.bind(this);
        initTopBar();
        initViews();
        initTabAndPager();
    }


    private void initViews(){

        fragments=new ArrayList<>();
        fragments.add(new JiluFragment(3));
        fragments.add(new JiluFragment(4));
        fragments.add(new JiluFragment(2));
        fragments.add(new JiluFragment(8));
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
        mTopBar.setTitle("公正记录");
    }

    private void initTabAndPager() {

        int indicatorHeight = QMUIDisplayHelper.dp2px(this, 2);
        mContentViewPager.setAdapter(new JiluFragmentPagerAdapter(getSupportFragmentManager(),fragments));
        mContentViewPager.setCurrentItem(mDestPage.getPosition(), false);
        QMUITabBuilder builder = mTabSegment.tabBuilder();
        mTabSegment.setIndicator(new QMUITabIndicator(
                indicatorHeight, false, true));
        mTabSegment.addTab(builder.setText(getString(R.string.tabSegment_item_1_title)).build(this));
        mTabSegment.addTab(builder.setText(getString(R.string.tabSegment_item_2_title)).build(this));
        mTabSegment.addTab(builder.setText(getString(R.string.tabSegment_item_3_title)).build(this));
        mTabSegment.addTab(builder.setText(getString(R.string.tabSegment_item_4_title)).build(this));

        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {

            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
            }

            @Override
            public void onDoubleTap(int index) {
                mTabSegment.clearSignCountView(index);
            }
        });
    }


    private View getPageView(ContentPage page) {
        View view = mPageMap.get(page);
        if (view == null) {
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setTextColor(ContextCompat.getColor(this, R.color.app_color_description));

            if (page == ContentPage.Item1) {
                textView.setText(R.string.tabSegment_item_1_content);
            } else if (page == ContentPage.Item2) {
                textView.setText(R.string.tabSegment_item_2_content);
            }else if (page == ContentPage.Item3) {
                textView.setText(R.string.tabSegment_item_1_content);
            }else if (page == ContentPage.Item4) {
                textView.setText(R.string.tabSegment_item_2_content);
            }

            view = textView;
            mPageMap.put(page, view);
        }
        return view;
    }


    public enum ContentPage {
        Item1(0),
        Item2(1),
        Item3(2),
        Item4(3);

        public static final int SIZE = 4;
        private final int position;

        ContentPage(int pos) {
            position = pos;
        }

        public static ContentPage getPage(int position) {
            switch (position) {
                case 0:
                    return Item1;
                case 1:
                    return Item2;
                case 2:
                    return Item3;
                case 3:
                    return Item4;
                default:
                    return Item1;
            }
        }

        public int getPosition() {
            return position;
        }
    }
}