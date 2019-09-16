package com.example.caipiaoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.example.base.base.BaseActivity;
import com.example.base.rx.RxEvent;
import com.example.base.util.LogUtils;
import com.example.base.util.cacheUtil.CacheFile;
import com.example.caipiaoapp.HistoryAdapter;
import com.example.caipiaoapp.R;
import com.example.caipiaoapp.SSQBallBean;
import com.example.caipiaoapp.SSQHistoryDataBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnalysisActivity extends BaseActivity {
    @BindView(R.id.bt_analysis1)
    Button btAnalysis1;
    @BindView(R.id.tv_data_info)
    TextView tvDataInfo;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.rv_data)
    RecyclerView rvData;
    private List<SSQBallBean> ssqBallBeans;
    private HistoryAdapter historyAdapter;
    private int[] blueBalls;
    private int[] redBalls;

    public static void start(Context context) {
        Intent intent = new Intent(context, AnalysisActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_analysis;
    }

    @Override
    protected void initWidget() {
        rvData.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new HistoryAdapter();
        rvData.setAdapter(historyAdapter);

    }

    @Override
    protected void initData() {
        CacheFile cacheFile = new CacheFile();
        SSQHistoryDataBean ssqHistoryDataBean = cacheFile.getCacheClass("SSQHistoryDataBean", SSQHistoryDataBean.class);
        ssqBallBeans = ssqHistoryDataBean.getBallBeans();
        tvDataInfo.setText(String.format(Locale.ENGLISH, "共%d期，最新期数：%s,最后期数：%s"
                , ssqBallBeans.size(), ssqBallBeans.get(0).getQiShu(), ssqBallBeans.get(ssqBallBeans.size() - 1).getQiShu()));
        historyAdapter.replace(ssqHistoryDataBean.getLotteryResListBeans());

        initBalls();
    }

    private void  initBalls(){
        blueBalls = new int[35];
        redBalls = new int[12];
        for (int i = 0; i< blueBalls.length; i++){
            blueBalls[i] = i+1;
        }
        for (int i = 0; i< redBalls.length; i++){
            redBalls[i] = i+1;
        }
    }
    private void getBlueAllGroup(int min,int max){

    }


    /**
     * 策略一
     */
    private void strategy1(){
        //排除上次出现的篮球
        int count  = 0;
        List<Integer> smallCount = new ArrayList<>();
        for (int i =0 ;i<ssqBallBeans.size();i++){
            SSQBallBean ssqBallBean = ssqBallBeans.get(i);
            if (ssqBallBean.getBlueBalls().get(0).startsWith("0")&&i!=ssqBallBeans.size()-1){
                count++;
            }else {
                LogUtils.d("大于十的间隔："+count);
                smallCount.add(count);
                count = 0;

            }
        }
        int all = 0;
        int max = 0;


    }
    /**
     * 获取某个数字的d!阶乘
     */
    private long getC(int n,int m){
        long n1 = getN(n);
        long m1 = getN(m);
        long m2 = getN((n-m));

        return n1/(m1*m2);
    }
    /**
     * 获取某个数字的d!阶乘
     */
    private long getN(int d){

        if (d<=0){
            return 0;
        }
        long result = d;
        long count = d;
        while (count>1){
            count = count-1;
            result = result*count;
        }
        return result;
    }

    @Override
    public void handleDefaultEvent(RxEvent event) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_analysis1)
    public void onViewClicked() {
        strategy1();

    }
}
