package com.example.caipiaoapp;

import android.graphics.Color;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.base.base.BaseRecyclerAdapter;
import com.example.caipiaoapp.util.StringUtil;

import java.util.Locale;

import butterknife.BindView;

/**
 * Created by zdy On 2019/9/14.
 */
public class HistoryAdapter extends BaseRecyclerAdapter<HistoryBean.ResultBean.LotteryResListBean> {


    @Override
    protected int getItemViewType(int position, HistoryBean.ResultBean.LotteryResListBean resultBean) {
        return R.layout.item_history;
    }

    @Override
    protected ViewHolder<HistoryBean.ResultBean.LotteryResListBean> onCreateViewHolder(View root, ViewGroup parent, int viewType) {
        return new HistoryViewHolder(root);
    }

    public class HistoryViewHolder extends BaseRecyclerAdapter.ViewHolder<HistoryBean.ResultBean.LotteryResListBean>{
        @BindView(R.id.tv_data)
        TextView tv_data;
        public HistoryViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(HistoryBean.ResultBean.LotteryResListBean resultBean) {
            StringBuilder stringBuilder = new StringBuilder(resultBean.getLottery_res());
            stringBuilder.insert(0,"红球：");
            stringBuilder.insert(18,"     蓝球：");
            SpannableString spannableString = new SpannableString(String.format(Locale.ENGLISH,"%s       期数：%s",stringBuilder.toString(),resultBean.getLottery_no()));
            if ("0".equals(stringBuilder.substring(26,27))){
                StringUtil.setForegroundColorSpan(spannableString,26,27, Color.parseColor("#0873d2"));
            }
            if ("0".equals(stringBuilder.substring(29,30))){
                StringUtil.setForegroundColorSpan(spannableString,29,30, Color.parseColor("#0873d2"));
            }
            tv_data.setText(spannableString);
        }
    }
}
