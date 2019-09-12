package com.example.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

import com.example.base.R;

import butterknife.ButterKnife;

/**
 * @author : sklyand
 * @email : zhengdengyao@51yryc.com
 * @time : 2019/7/24 16:37
 * @describe ：
 */
public abstract class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        this(context, R.style.Dialog_Default);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected  void init(){
        View root  = LayoutInflater.from(getContext()).inflate(setContentLayout(),null);
        ButterKnife.bind(this,root);
        initView(root);
        setContentView(root);
    }

    protected abstract @LayoutRes int setContentLayout();

    protected abstract void initView(View root);


}
