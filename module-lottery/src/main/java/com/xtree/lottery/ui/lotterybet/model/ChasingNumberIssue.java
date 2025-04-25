package com.xtree.lottery.ui.lotterybet.model;

import com.xtree.lottery.data.source.vo.IssueVo;

public class ChasingNumberIssue {
    public ChasingNumberRequestModel chasingNumberRequestModel;
    public IssueVo issueVo;

    public ChasingNumberRequestModel getChasingNumberRequestModel() {
        return chasingNumberRequestModel;
    }

    public void setChasingNumberRequestModel(ChasingNumberRequestModel chasingNumberRequestModel) {
        this.chasingNumberRequestModel = chasingNumberRequestModel;
    }

    public IssueVo getIssueVo() {
        return issueVo;
    }

    public void setIssueVo(IssueVo issueVo) {
        this.issueVo = issueVo;
    }

    public ChasingNumberIssue(ChasingNumberRequestModel chasingNumberRequestModel, IssueVo issueVo) {
        this.chasingNumberRequestModel = chasingNumberRequestModel;
        this.issueVo = issueVo;
    }
}
