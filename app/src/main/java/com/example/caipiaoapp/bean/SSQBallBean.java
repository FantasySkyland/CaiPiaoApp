package com.example.caipiaoapp.bean;

import java.util.List;

/**
 * @author : sklyand
 * @email :
 * @time : 2019/9/12 16:55
 * @describe ：
 */
public class SSQBallBean {
    private String qiShu;

    public String getQiShu() {
        return qiShu;
    }

    public void setQiShu(String qiShu) {
        this.qiShu = qiShu;
    }
    private List<String> redBalls;
    private List<String> blueBalls;

    public List<String> getRedBalls() {
        return redBalls;
    }

    public void setRedBalls(List<String> redBalls) {
        this.redBalls = redBalls;
    }

    public List<String> getBlueBalls() {
        return blueBalls;
    }

    public void setBlueBalls(List<String> blueBalls) {
        this.blueBalls = blueBalls;
    }
}
