package com.example.caipiaoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.base.base.BaseActivity;
import com.example.base.rx.RxEvent;
import com.example.base.util.LogUtils;
import com.example.base.util.ToastUtils;
import com.example.base.util.cacheUtil.CacheFile;
import com.example.caipiaoapp.Constans;
import com.example.caipiaoapp.adapter.HistoryAdapter;
import com.example.caipiaoapp.R;
import com.example.caipiaoapp.bean.SSQBallBean;
import com.example.caipiaoapp.bean.SSQHistoryDataBean;

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
    private String[] blueBalls;
    private String[] redBalls;
    private String id;

    public static void start(Context context,String id) {
        Intent intent = new Intent(context, AnalysisActivity.class);
        intent.putExtra("id",id);
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
        id = getIntent().getStringExtra("id");
        CacheFile cacheFile = new CacheFile();
        SSQHistoryDataBean ssqHistoryDataBean = cacheFile.getCacheClass(id+Constans.CACHE_KEY, SSQHistoryDataBean.class);
        ssqBallBeans = ssqHistoryDataBean.getBallBeans();
        tvDataInfo.setText(String.format(Locale.ENGLISH, "共%d期，最新期数：%s,最后期数：%s"
                , ssqBallBeans.size(), ssqBallBeans.get(0).getQiShu(), ssqBallBeans.get(ssqBallBeans.size() - 1).getQiShu()));
        historyAdapter.replace(ssqHistoryDataBean.getLotteryResListBeans());

        initBalls();
    }

    private void  initBalls(){
        redBalls = new String[35];
        blueBalls = new String[12];
        for (int i = 0; i< blueBalls.length; i++){
            if (i<9){
                blueBalls[i] = "0"+(i+1);
            }else {
                blueBalls[i] = String.valueOf(i+1);
            }

        }
        for (int i = 0; i< redBalls.length; i++){
            if (i<9){
                redBalls[i] = "0"+(i+1);
            }else {
                redBalls[i] = String.valueOf(i+1);
            }
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
        String lastFirst = ssqBallBeans.get(0).getBlueBalls().get(0);
        String lastSecond = ssqBallBeans.get(0).getBlueBalls().get(1);
        List<String> groups = new ArrayList<>();
        for (int i=0;i<9;i++){
            if (!lastFirst.equals(blueBalls[i])){
                for (int j=9;j<blueBalls.length;j++){

                    count++;
                    LogUtils.d("groups:"+blueBalls[i]+","+blueBalls[j]);
                }
            }


        }
        LogUtils.d("count:"+count);
    }
    /**
     * 策略一
     */
    private void strategy2(){
        //排除上次出现的篮球
        int count  = 0;
        String lastFirst = ssqBallBeans.get(0).getBlueBalls().get(0);
        String lastSecond = ssqBallBeans.get(0).getBlueBalls().get(1);
        List<String> groups = new ArrayList<>();
        for (int i=0;i<9;i++){
            if (!lastFirst.equals(blueBalls[i])){
                for (int j=0;j<9;j++){
                    if (i!=j){
                        count++;
                        LogUtils.d("groups:"+blueBalls[i]+","+blueBalls[j]);
                    }
                }
            }


        }
        LogUtils.d("count:"+count);
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

    private int startIndex = 0;
    @OnClick(R.id.bt_analysis1)
    public void onViewClicked() {
//        String lastSecondBlue0 = ssqBallBeans.get(startIndex).getBlueBalls().get(1);
//        String lastSecondBlue1 = ssqBallBeans.get(startIndex+1).getBlueBalls().get(1);
//        String lastSecondBlue2 = ssqBallBeans.get(startIndex+2).getBlueBalls().get(1);
        int correct = 0;
        int error = 0;
        int count = 0;
        int fistCount = 0;
        for (int i=0;i<ssqBallBeans.size()-4;i++){
            String lastFirstBlue = ssqBallBeans.get(i).getBlueBalls().get(0);
            String lastSecondBlue = ssqBallBeans.get(i).getBlueBalls().get(1);
//            if (lastSecondBlue.startsWith("0")) {
//                count++;
//            }
//            if (lastFirstBlue.startsWith("0")){
//                fistCount++;
//            }
//            if (lastSecondBlue.startsWith("0")&&lastFirstBlue.startsWith("0")){
//                correct++;
//            }
            String lastSecondBlue0 = ssqBallBeans.get(i+1).getBlueBalls().get(1);
            String lastSecondBlue1 = ssqBallBeans.get(i+2).getBlueBalls().get(1);
            String lastSecondBlue2 = ssqBallBeans.get(i+3).getBlueBalls().get(1);

            if (lastSecondBlue0.startsWith("0")&&!lastSecondBlue2.startsWith("0")){
              if (lastSecondBlue.startsWith("0")){
                  correct++;
              }else {
                  error++;
              }
            }
//            else if (!lastSecondBlue0.startsWith("0")&&!lastSecondBlue1.startsWith("0")){
//                if (lastSecondBlue.startsWith("0")){
//                    correct++;
//                }else {
//                    error++;
//                }
//            }
//            else if (!lastSecondBlue0.startsWith("0")&&lastSecondBlue1.startsWith("0")&&lastSecondBlue2.startsWith("0")){
//                if (!lastSecondBlue.startsWith("0")){
//                    correct++;
//                }else {
//                    error++;
//                }
//            }
        }
        LogUtils.d("correct："+correct+"   error:"+error+"   count:"+count);
//        if (lastSecondBlue0.startsWith("0")&&lastSecondBlue1.startsWith("0")){
//            strategy2();
//        }else if (!lastSecondBlue0.startsWith("0")&&!lastSecondBlue1.startsWith("0")){
//            strategy1();
//        }else {
//            ToastUtils.showShortToast("不玩");
//        }
        //strategy1();

    }
}
