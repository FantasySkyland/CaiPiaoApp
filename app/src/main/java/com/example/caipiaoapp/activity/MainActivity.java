package com.example.caipiaoapp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.base.base.BaseActivity;
import com.example.base.retrofit.RetrofitServiceCreator;
import com.example.base.rx.RxEvent;
import com.example.base.util.cacheUtil.CacheFile;
import com.example.caipiaoapp.CaiPiaoApi;
import com.example.caipiaoapp.HistoryAdapter;
import com.example.caipiaoapp.HistoryBean;
import com.example.caipiaoapp.R;
import com.example.caipiaoapp.SSQBallBean;
import com.example.caipiaoapp.SSQHistoryDataBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R.id.tv_lottery_no)
    TextView tvLotteryNo;
    @BindView(R.id.bt_saveData)
    Button btSaveData;
    @BindView(R.id.bt_analysis)
    Button btAnalysis;
    private int page = 1;
    private int pageSize = 50;
    @BindView(R.id.bt_getData)
    Button btGetData;
    @BindView(R.id.rv_data)
    RecyclerView rvData;

    private CaiPiaoApi caiPiaoApi;
    private HistoryAdapter historyAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        rvData.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new HistoryAdapter();
        rvData.setAdapter(historyAdapter);
        caiPiaoApi = RetrofitServiceCreator.createServcie(CaiPiaoApi.class);
        smartRefresh.setOnRefreshListener(this);
        smartRefresh.setOnLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        //getData("dlt", true);
    }


    public void getData(String lottery_id, boolean refresh) {
        if (refresh) {
            page = 1;
        } else {
            page++;
        }
        caiPiaoApi.getHistory("9b697954e6142a378d3d2f43e2047c18", lottery_id,
                page, 50).enqueue(new Callback<HistoryBean>() {
            @Override
            public void onResponse(Call<HistoryBean> call, Response<HistoryBean> response) {
                HistoryBean historyBean = response.body();
                if (refresh) {
                    smartRefresh.setEnableLoadMore(true);
                    historyAdapter.replace(historyBean.getResult().getLotteryResList());
                } else {
                    historyAdapter.add(historyBean.getResult().getLotteryResList());
                }
                if (historyBean.getResult().getLotteryResList() == null || historyBean.getResult().getLotteryResList().size() < pageSize) {
                    smartRefresh.setEnableLoadMore(false);
                }
                smartRefresh.finishLoadMore();
                smartRefresh.finishLoadMore();
            }

            @Override
            public void onFailure(Call<HistoryBean> call, Throwable t) {
                smartRefresh.finishLoadMore();
                smartRefresh.finishLoadMore();
            }
        });
    }

    @Override
    public void handleDefaultEvent(RxEvent event) {

    }


    private void update(){
        CacheFile cacheFile = new CacheFile();
        SSQHistoryDataBean ssqHistoryDataBean = cacheFile.getCacheClass("SSQHistoryDataBean", SSQHistoryDataBean.class);
         long cacheLatest = 0;
        if (ssqHistoryDataBean!=null){
              cacheLatest = Long.valueOf(ssqHistoryDataBean.getLotteryResListBeans().get(0).getLottery_no());
        }
        if (cacheLatest>0){
            caiPiaoApi.getHistory("9b697954e6142a378d3d2f43e2047c18", "dlt",
                    1, 50).enqueue(new Callback<HistoryBean>() {
                @Override
                public void onResponse(Call<HistoryBean> call, Response<HistoryBean> response) {
                    HistoryBean historyBean = response.body();
                    List<HistoryBean.ResultBean.LotteryResListBean> lotteryResList
                            = historyBean.getResult().getLotteryResList();
                    long no = Long.valueOf(lotteryResList.get(0).getLottery_no());
                    long cacheLatest = Long.valueOf(ssqHistoryDataBean.getLotteryResListBeans().get(0).getLottery_no());
                    long dis = no - cacheLatest;
                    if (dis>0){
                        List<HistoryBean.ResultBean.LotteryResListBean> addList = new ArrayList<>();
                        List<SSQBallBean> addList2 = new ArrayList<>();
                        for (int i =0 ;i<dis;i++){
                            HistoryBean.ResultBean.LotteryResListBean lotteryResListBean = lotteryResList.get(i);
                            addList.add(lotteryResListBean);
                            SSQBallBean ssqBallBean = new SSQBallBean();
                            String[] strings = lotteryResListBean.getLottery_res().split(",");
                            List<String> redBalls = new ArrayList<>();
                            List<String> blueBalls = new ArrayList<>();
                            for (int j = 0; j < strings.length; j++) {
                                if (j < 5) {
                                    redBalls.add(strings[i]);
                                } else {
                                    blueBalls.add(strings[i]);
                                }
                            }
                            ssqBallBean.setQiShu(lotteryResListBean.getLottery_no());
                            ssqBallBean.setRedBalls(redBalls);
                            ssqBallBean.setBlueBalls(blueBalls);
                            addList2.add(ssqBallBean);
                        }
                        ssqHistoryDataBean.getBallBeans().addAll(0,addList2);
                        ssqHistoryDataBean.getLotteryResListBeans().addAll(0,addList);
                        CacheFile.saveClass("SSQHistoryDataBean", ssqHistoryDataBean);
                    }
                }

                @Override
                public void onFailure(Call<HistoryBean> call, Throwable t) {

                }
            });
        }

    }
    /**
     * 保存数据
     */
    private void saveData() {
        List<HistoryBean.ResultBean.LotteryResListBean> lotteryResListBeans = historyAdapter.getItems();
        List<SSQBallBean> ballBeans = new ArrayList<>();
        for (HistoryBean.ResultBean.LotteryResListBean lotteryResListBean : lotteryResListBeans) {
            SSQBallBean ssqBallBean = new SSQBallBean();
            String[] strings = lotteryResListBean.getLottery_res().split(",");
            List<String> redBalls = new ArrayList<>();
            List<String> blueBalls = new ArrayList<>();
            for (int i = 0; i < strings.length; i++) {
                if (i < 5) {
                    redBalls.add(strings[i]);
                } else {
                    blueBalls.add(strings[i]);
                }
            }
            ssqBallBean.setQiShu(lotteryResListBean.getLottery_no());
            ssqBallBean.setRedBalls(redBalls);
            ssqBallBean.setBlueBalls(blueBalls);
            ballBeans.add(ssqBallBean);
        }
        SSQHistoryDataBean ssqHistoryDataBean = new SSQHistoryDataBean();
        ssqHistoryDataBean.setBallBeans(ballBeans);
        ssqHistoryDataBean.setLotteryResListBeans(lotteryResListBeans);
        CacheFile.saveClass("SSQHistoryDataBean", ssqHistoryDataBean);
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getData("dlt", false);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData("dlt", true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_getData, R.id.bt_saveData,R.id.bt_analysis,R.id.bt_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_getData:
                getData("dlt", true);
                break;
            case R.id.bt_saveData:
                saveData();
                break;
            case R.id.bt_analysis:
                AnalysisActivity.start(MainActivity.this);
            case R.id.bt_update:
                update();
                break;
        }
    }

}
