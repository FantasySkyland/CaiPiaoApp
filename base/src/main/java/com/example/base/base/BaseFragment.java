package com.example.base.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.base.rx.RxBus;
import com.example.base.rx.RxEvent;
import com.example.base.rx.RxUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author zhengdengyao
 * @Created 2018/12/11 0011
 * @Description
 */
public abstract class BaseFragment extends AFragment {

    protected View mRoot;
    protected Unbinder mRootUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mRoot == null) {
            int layId = getContentLayoutId();
            // 初始化当前的跟布局，但是不在创建时就添加到container里边
            View root = inflater.inflate(layId, container, false);
            mRootUnbinder = ButterKnife.bind(this, root);
            initWidget(root);
            mRoot = root;
            registerDefaultEvent();
        } else {
            if (mRoot.getParent() != null) {
                // 把当前Root从其父控件中移除，防止出错
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        super.onViewCreated(view, savedInstanceState);
    }
    /**
     * 初始化相关参数,外部传入的参数
     */
    protected void initArgs(Bundle bundle) {

    }

    /**
     * 得到当前界面的资源文件id
     * @return 资源文件id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initWidget(View root);
    /**
     * 初始化数据
     */
    protected abstract void initData();
    /**
     * 返回按键触发时调用
     *
     * @return 返回True代表我已处理返回逻辑，Activity不用自己finish。
     * 返回False代表我没有处理逻辑，Activity自己走自己的逻辑
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 注册rxbus订阅事件
     */
    public void registerDefaultEvent() {
        RxBus.getInstance().toFlowable(RxEvent.class).compose(RxUtils.rxSchedulerHelper())
                .compose(mProvider.bindToLifecycle())
                .subscribe(event -> handleDefaultEvent(event));
    }


    /**
     * 处理默认订阅事件
     *
     * @param event
     */
    public abstract void handleDefaultEvent(RxEvent event);
}
