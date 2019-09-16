package com.example.caipiaoapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.base.base.BaseActivity;
import com.example.base.retrofit.RetrofitServiceCreator;
import com.example.base.rx.RxEvent;
import com.example.base.util.ToastUtils;
import com.example.caipiaoapp.Constans;
import com.example.caipiaoapp.R;
import com.example.caipiaoapp.adapter.CaiPiaoTypeAdapter;
import com.example.caipiaoapp.bean.TypeBean;
import com.example.caipiaoapp.net.CaiPiaoApi;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author : sklyand
 * @email :
 * @time : 2019/9/16 10:02
 * @describe ：
 */
public class CaiPiaoTypeActivity extends BaseActivity {
    @BindView(R.id.rv_type)
    RecyclerView rvType;

    private CaiPiaoTypeAdapter caiPiaoTypeAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_type;
    }

    @Override
    protected void initWidget() {
        rvType.setLayoutManager(new LinearLayoutManager(this));
        caiPiaoTypeAdapter = new CaiPiaoTypeAdapter();
        rvType.setAdapter(caiPiaoTypeAdapter);
    }

    @Override
    protected void initData() {
        Gson gson = new Gson();
        TypeBean typeBean = gson.fromJson(typeStr,TypeBean.class);
        caiPiaoTypeAdapter.replace(typeBean.getResult());
        //getData();
    }

    private void getData() {
        CaiPiaoApi caiPiaoApi = RetrofitServiceCreator.createServcie(CaiPiaoApi.class);
        caiPiaoApi.getType(Constans.APP_KEY).enqueue(new Callback<TypeBean>() {
            @Override
            public void onResponse(Call<TypeBean> call, Response<TypeBean> response) {
                caiPiaoTypeAdapter.replace(response.body().getResult());
            }

            @Override
            public void onFailure(Call<TypeBean> call, Throwable t) {
                ToastUtils.showShortToast("获取数据失败："+t.getMessage());
            }
        });
    }

    @Override
    public void handleDefaultEvent(RxEvent event) {

    }

    String typeStr = "{\n" +
            "    \"reason\": \"查询成功\",\n" +
            "    \"result\": [\n" +
            "        {\n" +
            "            \"lottery_id\": \"ssq\",\n" +
            "            \"lottery_name\": \"双色球\",\n" +
            "            \"lottery_type_id\": \"1\",\n" +
            "            \"remarks\": \"每周二、四、日开奖\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"lottery_id\": \"dlt\",\n" +
            "            \"lottery_name\": \"超级大乐透\",\n" +
            "            \"lottery_type_id\": \"2\",\n" +
            "            \"remarks\": \"每周一、三、六开奖\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"lottery_id\": \"qlc\",\n" +
            "            \"lottery_name\": \"七乐彩\",\n" +
            "            \"lottery_type_id\": \"1\",\n" +
            "            \"remarks\": \"每周一、三、五开奖\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"lottery_id\": \"fcsd\",\n" +
            "            \"lottery_name\": \"福彩3D\",\n" +
            "            \"lottery_type_id\": \"1\",\n" +
            "            \"remarks\": \"每日开奖\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"lottery_id\": \"qxc\",\n" +
            "            \"lottery_name\": \"七星彩\",\n" +
            "            \"lottery_type_id\": \"2\",\n" +
            "            \"remarks\": \"每周二、五、日开奖\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"lottery_id\": \"pls\",\n" +
            "            \"lottery_name\": \"排列3\",\n" +
            "            \"lottery_type_id\": \"2\",\n" +
            "            \"remarks\": \"每日开奖\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"lottery_id\": \"plw\",\n" +
            "            \"lottery_name\": \"排列5\",\n" +
            "            \"lottery_type_id\": \"2\",\n" +
            "            \"remarks\": \"每日开奖\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"error_code\": 0\n" +
            "}";
}
