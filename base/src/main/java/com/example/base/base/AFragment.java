package com.example.base.base;



import com.example.base.dialog.LoginProgressDialog;
import com.trello.navi2.component.support.NaviFragment;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.navi.NaviLifecycle;

/**
 * @author : sklyand
 * @email : zhengdengyao@51yryc.com
 * @time : 2019/7/18 15:47
 * @describe ：
 */
public abstract class AFragment extends NaviFragment {

    protected LoginProgressDialog progressDialog;

    protected final LifecycleProvider<ActivityEvent> mProvider
            = NaviLifecycle.createActivityLifecycleProvider(this);

    /**
     * 显示加载进度dialog
     */
    public void showProgressDialog(String text) {
        if (getContext()==null){
            return;
        }
        if (progressDialog == null){
            progressDialog = new LoginProgressDialog(getContext());
        }
        if (progressDialog.isShowing()) return;
        progressDialog.show(text);
    }

    /**
     * 隐藏加载进度dialog
     */
    public void hideProgressDialog() {
        if (getContext()==null){
            return;
        }
        if (progressDialog == null){
            return;
        }
        if (!progressDialog.isShowing()) return;
        progressDialog.dismiss();
    }

}
