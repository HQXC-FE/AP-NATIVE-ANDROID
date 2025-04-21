package com.xtree.bet.bean.ui;

import java.util.List;

public class ScoreIm implements Score{
    private String period;
    private List<Integer> scores;
    public ScoreIm(String period, List<Integer> scores){
        this.period = period;
        this.scores = scores;
    }

    /**
     * 获取玩法
     * @return
     */
    @Override
    public String getPeriod() {
        return period;
    }

    /**
     * 获取比分
     * @return
     */
    @Override
    public List<Integer> getScores() {
        return scores;
    }
}
