package com.example.base.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.base.R;

import butterknife.ButterKnife;

/**
 * @Author zhengdengyao
 * @Created 2018/12/11 0011
 * @Description
 */
public abstract class BaseDialogFragment extends DialogFragment
        implements DialogInterface.OnKeyListener,DialogInterface.OnShowListener,
        FocusDialog.FocusChangeListener{
    private static final String TAG = "BaseDialogFragment";
    protected DialogFragmentCommonListener commonListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArgs(getArguments());
        init();
    }

    protected  void initArgs(Bundle arguments){

    }
    protected  void init(){

    }
    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FocusDialog dialog = getFocusDialog();
        View root = LayoutInflater.from(getActivity()).inflate(getLayoutRes(),null);
        initRoot(root);
        ButterKnife.bind(this,root);
        initDialog(dialog);
        initWindow(dialog);
        dialog.setContentView(root);
        dialog.setOnKeyListener(this);
        dialog.setOnShowListener(this);
        dialog.setFocusChangeListener(this);
        dialog.setOnDismissListener(this);
        return dialog;
    }

    protected void initWindow(Dialog dialog){
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow!=null){
            //GameUIUtils.hideNavigationBar(dialogWindow);
            dialogWindow.setGravity(Gravity.TOP);
            dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.FILL_PARENT;
            lp.height = WindowManager.LayoutParams.FILL_PARENT;
            dialogWindow.setAttributes(lp);
        }
    }
    /**
     * @return 创建dialog
     */
    protected void initRoot(View root){

    }
    /**
     * @return 创建dialog
     */
    protected FocusDialog getFocusDialog(){
        return new FocusDialog(getActivity(), R.style.BaseDialog);
    }


    public boolean isShowing(){
        if (getDialog()!=null&&getDialog().isShowing()){
            return true;
        }else {
            return false;
        }
    }


    @Override
    public void onShow(DialogInterface dialog) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

    }
    public interface DialogFragmentCommonListener{
        void onDismiss();
        void onBackPress();
    }

    public DialogFragmentCommonListener getCommonListener() {
        return commonListener;
    }

    public void setCommonListener(DialogFragmentCommonListener commonListener) {
        this.commonListener = commonListener;
    }

    /**
     * 初始化dialog
     */
    protected abstract void initDialog(Dialog dialog);

    /**
     * @return dialog的布局id
     */
    protected abstract @LayoutRes int getLayoutRes();

    /**
     * @param manager
     * @param tag
     * 这里做个判断防止多次添加造成崩溃
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        if (manager.findFragmentByTag(tag)==null&&!isAdded()){
            super.show(manager, tag);
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()){
            if (getFragmentManager()!=null){
                super.dismiss();
            }
            if (commonListener!=null){
                commonListener.onDismiss();
            }
        }
    }
}
