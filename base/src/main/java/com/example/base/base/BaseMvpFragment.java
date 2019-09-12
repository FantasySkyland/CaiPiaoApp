package com.example.base.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.base.rx.RxEvent;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * @author : sklyand
 * @email : zhengdengyao@51yryc.com
 * @time : 2019/7/6 11:14
 * @describe ：
 */
public abstract class BaseMvpFragment<Presenter extends BaseContract.Presenter>
        extends BaseFragment implements BaseContract.View<Presenter>{
    protected Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setPresenter(initPresenter(),mProvider);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setPresenter(Presenter presenter, LifecycleProvider<ActivityEvent> provider) {
        this.presenter = presenter;
        if (this.presenter!=null){
            presenter.setProvider(provider);
        }
    }

    @Override
    public void handleDefaultEvent(RxEvent event) {

    }

    @Override
    public Context getAContext() {
        return getContext();
    }
    /**
     * @return 初始化presenter
     */
    protected abstract Presenter initPresenter();
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter!=null){
            presenter.destroy();
        }
    }
}
