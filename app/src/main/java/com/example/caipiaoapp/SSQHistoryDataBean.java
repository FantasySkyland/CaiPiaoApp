package com.example.caipiaoapp;

import java.util.List;

/**
 * @author : sklyand
 * @email :
 * @time : 2019/9/12 16:51
 * @describe ：
 */
public class SSQHistoryDataBean {

    private List<HistoryBean.ResultBean.LotteryResListBean> lotteryResListBeans;

    public List<HistoryBean.ResultBean.LotteryResListBean> getLotteryResListBeans() {
        return lotteryResListBeans;
    }

    public void setLotteryResListBeans(List<HistoryBean.ResultBean.LotteryResListBean> lotteryResListBeans) {
        this.lotteryResListBeans = lotteryResListBeans;
    }

    private List<SSQBallBean> ballBeans;

    public List<SSQBallBean> getBallBeans() {
        return ballBeans;
    }

    public void setBallBeans(List<SSQBallBean> ballBeans) {
        this.ballBeans = ballBeans;
    }
}
