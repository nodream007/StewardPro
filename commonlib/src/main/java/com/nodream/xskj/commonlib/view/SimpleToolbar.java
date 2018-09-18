package com.nodream.xskj.commonlib.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nodream.xskj.commonlib.R;

/**
 * @author HuangFusheng
 * 自定义Toolbar
 */
public class SimpleToolbar extends Toolbar {
    /**
     * 左侧Title
     */
    private TextView mTxtLeftTitle;
    /**
     * 左侧图标
     */
    private TextView mTxtLeftImg;
    /**
     * 中间Title
     */
    private TextView mTxtMiddleTitle;
    /**
     * 右侧Title
     */
    private TextView mTxtRightTitle;
    /**
     * 右侧图标
     */
    private TextView mTxtRightImg;

    public SimpleToolbar(Context context) {
        this(context,null);
    }

    public SimpleToolbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SimpleToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_simple_toolbar, this);
        mTxtLeftTitle = findViewById(R.id.txt_left_title);
        mTxtLeftImg = findViewById(R.id.txt_left_img);
        mTxtMiddleTitle = findViewById(R.id.txt_main_title);
        mTxtRightTitle = findViewById(R.id.txt_right_title);
        mTxtRightImg = findViewById(R.id.txt_right_img);
    }


    //设置中间title的内容
    public void setMainTitle(String text) {
        this.setTitle(" ");
        mTxtMiddleTitle.setVisibility(View.VISIBLE);
        mTxtMiddleTitle.setText(text);
    }

    //设置中间title的内容文字的颜色
    public void setMainTitleColor(int color) {
        mTxtMiddleTitle.setTextColor(color);
    }

    //设置title左边文字
    public void setLeftTitleText(String text) {
        mTxtLeftTitle.setVisibility(View.VISIBLE);
        mTxtLeftImg.setVisibility(View.GONE);
        mTxtLeftTitle.setText(text);
    }

    //设置title左边文字颜色
    public void setLeftTitleColor(int color) {
        mTxtLeftTitle.setTextColor(color);
    }

    //设置title左边图标
    public void setLeftTitleDrawable(int res, boolean isDefault) {
        mTxtLeftImg.setVisibility(View.VISIBLE);
        mTxtLeftTitle.setVisibility(View.GONE);
        if (!isDefault) {
            Drawable dwLeft = ContextCompat.getDrawable(getContext(), res);
            dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
            mTxtLeftImg.setCompoundDrawables(dwLeft, null, null, null);
        }
    }
    //设置title左边图标
    public void setLeftImgVisible() {
        mTxtLeftImg.setVisibility(View.VISIBLE);
        mTxtLeftTitle.setVisibility(View.GONE);
    }
    //设置title左边点击事件
    public void setLeftTitleClickListener(OnClickListener onClickListener){
        mTxtLeftTitle.setOnClickListener(onClickListener);
    }

    //设置title左边点击事件
    public void setLeftImgClickListener(OnClickListener onClickListener){
        mTxtLeftImg.setOnClickListener(onClickListener);
    }

    //设置title右边文字
    public void setRightTitleText(String text) {
        mTxtRightTitle.setVisibility(View.VISIBLE);
        mTxtRightImg.setVisibility(View.GONE);
        mTxtRightTitle.setText(text);
    }
    //设置右边空间隐藏
    public void setRightGone() {
        mTxtRightTitle.setVisibility(View.GONE);
        mTxtRightImg.setVisibility(View.GONE);

    }

    //设置title右边文字颜色
    public void setRightTitleColor(int color) {
        mTxtRightTitle.setTextColor(color);
    }

    //设置title右边图标
    public void setRightImgDrawable(int res, boolean isDefault) {
        mTxtRightImg.setVisibility(View.VISIBLE);
        mTxtRightTitle.setVisibility(View.GONE);
        if (!isDefault) {
            Drawable dwRight = ContextCompat.getDrawable(getContext(), res);
            dwRight.setBounds(0, 0, dwRight.getMinimumWidth(), dwRight.getMinimumHeight());
            mTxtRightImg.setCompoundDrawables(null, null, dwRight, null);
        }
    }

    //设置title右边点击事件
    public void setRightTitleClickListener(OnClickListener onClickListener){
        mTxtRightTitle.setOnClickListener(onClickListener);
    }

    //设置title右边点击事件
    public void setRightImgClickListener(OnClickListener onClickListener){
        mTxtRightImg.setOnClickListener(onClickListener);
    }
}
