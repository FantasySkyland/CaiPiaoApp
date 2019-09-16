package com.example.caipiaoapp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.base.base.BaseRecyclerAdapter;
import com.example.caipiaoapp.R;
import com.example.caipiaoapp.activity.DLTActivity;
import com.example.caipiaoapp.bean.TypeBean;

import butterknife.BindView;

/**
 * @author : sklyand
 * @email :
 * @time : 2019/9/16 10:10
 * @describe ：
 */
public class CaiPiaoTypeAdapter extends BaseRecyclerAdapter<TypeBean.ResultBean> {

    @Override
    protected int getItemViewType(int position, TypeBean.ResultBean resultBean) {
        return R.layout.item_caipiao_type;
    }

    @Override
    protected ViewHolder<TypeBean.ResultBean> onCreateViewHolder(View root, ViewGroup parent, int viewType) {
        return new CaiPiaoTypeViewHolder(root);
    }

    public class CaiPiaoTypeViewHolder extends BaseRecyclerAdapter.ViewHolder<TypeBean.ResultBean>{
        @BindView(R.id.tv_type)
        TextView tv_type;
        public CaiPiaoTypeViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(TypeBean.ResultBean resultBean) {
            tv_type.setText(resultBean.getLottery_name());
            tv_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DLTActivity.start(v.getContext(),resultBean.getLottery_id());
//                    switch (resultBean.getLottery_id()){
//                        case "ssq":
//                            break;
//                        case "dlt":
//                            DLTActivity.start(v.getContext(),resultBean.getLottery_id());
//                            break;
//                        case "qlc":
//                            break;
//                        case "fcsd":
//                            break;
//                        case "pls":
//                            break;
//                        case "plw":
//                            break;
//                    }
                }
            });
        }
    }
}
