package com.example.base.base;

import android.content.Context;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * @Author zhengdengyao
 * @Created 2019/1/4 0004
 * @Description mvp基本契约，presenter 和view的接口在这里定义
 */
public interface BaseContract {
    interface Presenter<View>{
        void attachView(View view);
        void setProvider(LifecycleProvider<ActivityEvent> provider);
        void destroy();
    }

    interface View<Presenter>{
        void setPresenter(Presenter presenter, LifecycleProvider<ActivityEvent> provider);

        Context getAContext();
    }
}
