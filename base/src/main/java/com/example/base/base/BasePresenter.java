package com.example.base.base;

import com.example.base.retrofit.ApiService;
import com.example.base.retrofit.RetrofitServiceCreator;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * @Author zhengdengyao
 * @Created 2019/1/4 0004
 * @Description
 */
public abstract class BasePresenter<View extends BaseContract.View> implements BaseContract.Presenter<View>{
    protected View view;
    protected LifecycleProvider<ActivityEvent> mProvider = null;
    protected ApiService api;
    public BasePresenter(View view) {
        api = RetrofitServiceCreator.createServcie(ApiService.class);
        attachView(view);
    }


    protected View getView() {
        return view;
    }

    protected void setView(View view) {
        this.view = view;
    }

    @Override
    public void attachView(View view) {
        setView(view);
    }

    @Override
    public void setProvider(LifecycleProvider<ActivityEvent> provider) {
        this.mProvider = provider;
    }

    @Override
    public void destroy() {
        if (view!=null){
            view = null;
        }
        if (mProvider!=null){
            mProvider = null;
        }
        onDestroy();
    }

    /**
     * 让子类必须重写这个方法，进行资源释放，避免内存泄漏
     */
     protected abstract void onDestroy();
}
