package com.xtree.live.ui.main.model.hot;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/9/11.
 * Describe: 热门列表数据模型
 */
public class LiveHotItemModel extends BindModel {
    public ObservableField<String> mainScore = new ObservableField<>();//主队比分
    public ObservableField<String> visitorScore = new ObservableField<>();//客队比分
    public ObservableField<String> mainTeamIcon = new ObservableField<>();//主队图标
    public ObservableField<String> visitorTeamIcon = new ObservableField<>();//客队图标
    public ObservableField<String> mainTeamName = new ObservableField<>();//主队名称
    public ObservableField<String> visitorTeamName = new ObservableField<>();//客队名称
    public ObservableField<String> leagueName = new ObservableField<>();//联赛名称
    public ObservableInt isGoingOn = new ObservableInt(-1);//是否已开赛
    public ObservableField<String> stage = new ObservableField<>();//赛事阶段
    private String matchId;//	比赛id
    private String userNickname;//	用户昵称

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
