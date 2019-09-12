package com.example.caipiaoapp;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.base.base.BaseActivity;
import com.example.base.retrofit.ApiService;
import com.example.base.retrofit.RetrofitServiceCreator;
import com.example.base.rx.RxEvent;
import com.example.base.util.ToastUtils;
import com.example.base.util.cacheUtil.CacheFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    @BindView(R.id.bt_show)
    Button btShow;
    private List<SSQBallBean> ballBeans = new ArrayList<>();
    private long qiShu = 2019106;
    private int count;
    @BindView(R.id.bt_getData)
    Button btGetData;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {

    }

    @Override
    protected void initData() {

    }

    public void getData() {

        ApiService apiService = RetrofitServiceCreator.createServcie(ApiService.class);
        apiService.getHistory("jsonp1532326738107", String.valueOf(qiShu)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStream is = response.body().byteStream();
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                String str = "";
                try {
                    while (((length = is.read(buffer)) != -1)) {
                        result.write(buffer, 0, length);
                    }
                    str = result.toString(StandardCharsets.UTF_8.name());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String[] strings = str.split("\\*");

                if (strings.length >= 10) {
                    List<String> redBalls = new ArrayList<>();
                    List<String> blueBalls = new ArrayList<>();
                    for (int i = 4; i < strings.length - 1; i++) {
                        if (i <= 8) {
                            redBalls.add(strings[i]);
                        } else if (i > 8 && i <= 10) {
                            blueBalls.add(strings[i]);
                        }
                    }
                    SSQBallBean ssqBallBean = new SSQBallBean();
                    ssqBallBean.setQiShu(String.valueOf(qiShu));
                    ssqBallBean.setRedBalls(redBalls);
                    ssqBallBean.setBlueBalls(blueBalls);
                    ballBeans.add(ssqBallBean);
                }
                saveData();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void saveData() {
        SSQHistoryDataBean ssqHistoryDataBean = new SSQHistoryDataBean();
        ssqHistoryDataBean.setBallBeans(ballBeans);
        CacheFile.saveClass("SSQHISTORY", ssqHistoryDataBean);
    }

    @Override
    public void handleDefaultEvent(RxEvent event) {

    }



    @OnClick({R.id.bt_getData, R.id.bt_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_getData:
                getData();
                break;
            case R.id.bt_show:
                CacheFile cacheFile = new CacheFile();
                SSQHistoryDataBean ssqHistoryDataBean = cacheFile.getCacheClass("SSQHISTORY",SSQHistoryDataBean.class);
                if (ssqHistoryDataBean!=null){
                    ToastUtils.showShortToast(ssqHistoryDataBean.getBallBeans().get(0).getQiShu());
                }
                break;
        }
    }
}
