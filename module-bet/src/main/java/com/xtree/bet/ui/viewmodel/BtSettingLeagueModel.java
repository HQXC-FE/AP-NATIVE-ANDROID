package com.xtree.bet.ui.viewmodel;

import com.xtree.bet.bean.ui.League;

import java.util.List;

/**
 * Created by marquis
 */

public interface BtSettingLeagueModel {

    /**
     * 获取联赛列表
     */
    void getOnSaleLeagues(int sportId, int type, List<Long> leagueIdList);

    void getLeagueAreaList(List<League> leagueList, boolean isSearch, List<Long> leagueIdList);

}
