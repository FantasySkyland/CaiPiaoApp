package com.example.caipiaoapp.dialog;

import android.content.Context;
import android.view.View;

import com.example.base.dialog.BaseDialog;
import com.example.caipiaoapp.R;

/**
 * @author : sklyand
 * @email :
 * @time : 2019/9/12 11:31
 * @describe ：
 */
public class WaitingDialogStyle1 extends BaseDialog {
    public WaitingDialogStyle1(Context context) {
        super(context);
    }

    @Override
    protected int setContentLayout() {
        return R.layout.dialog_wait_stytle1;
    }

    @Override
    protected void initView(View root) {
        setCancelable(false);
    }
}
