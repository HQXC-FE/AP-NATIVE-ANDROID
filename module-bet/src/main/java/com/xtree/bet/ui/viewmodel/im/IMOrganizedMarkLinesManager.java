package com.xtree.bet.ui.viewmodel.im;

import com.xtree.bet.bean.response.im.MarketLine;
import com.xtree.bet.bean.response.im.MatchInfo;
import com.xtree.bet.bean.response.im.Sport;

import java.util.*;

public final class IMOrganizedMarkLinesManager {

    public static final IMOrganizedMarkLinesManager shared = new IMOrganizedMarkLinesManager();

    private IMOrganizedMarkLinesManager() {
    }

    public void organizedMarkLinesWith(Sport imSport, MatchInfo imSportEvents) {
        if (imSport.getSportId() == 1) {
            soccerOrganizedMarkLinesWith(imSportEvents);
        } else if (imSport.getSportId() == 2) {
            basketballOrganizedMarkLinesWith(imSportEvents);
        } else {
            defaultOrganizedMarkLinesWith(imSportEvents);
        }
    }

    // 足球规则
    public static class SoccerRule {
        public static final Set<Integer> ahou = new HashSet<>(Arrays.asList(1, 2, 35, 54, 160, 161));
        public static final Set<Integer> cs = new HashSet<>(Arrays.asList(6, 62, 158, 162));
        public static final Set<Integer> corner = new HashSet<>(Arrays.asList(2, 3, 5, 6, 7, 8));
        public static final Set<Integer> bookings = new HashSet<>(Collections.singletonList(0));
        public static final Set<Integer> goals = new HashSet<>(Arrays.asList(7, 60, 61, 18, 16, 17, 21, 13, 14, 15, 22, 23));
        public static final Set<Integer> halves = new HashSet<>(Arrays.asList(2, 3));
        public static final Set<Integer> period = new HashSet<>(Arrays.asList(47, 48, 49, 50, 51, 52, 53, 54));
        public static final Set<Integer> specials;

        static {
            Set<Integer> set = new HashSet<>(Arrays.asList(18, 21, 13, 8, 22, 23, 44, 45, 78, 79, 80, 36, 26, 16, 17, 14, 15, 46, 63, 64, 65, 55, 72, 73, 74, 75, 76, 77, 40, 41, 39, 28, 29, 30, 33, 34, 47, 58, 59, 35, 19, 20, 26, 27, 24, 25, 159));
            specials = new TreeSet<>(set); // 有序
        }
    }

    private void soccerOrganizedMarkLinesWith(MatchInfo imSportEvents) {
        for (MarketLine marketLine : imSportEvents.getMarketLines()) {
            if (SoccerRule.ahou.contains(marketLine.getBetTypeId())) {
                marketLine.setBetTypeGroupName("h");
            } else if (SoccerRule.cs.contains(marketLine.getBetTypeId())) {
                marketLine.setBetTypeGroupName("cs");
            } else if ((SoccerRule.corner.contains(marketLine.getBetTypeId()) && imSportEvents.getGroundTypeId() == 2) || marketLine.getBetTypeName().toLowerCase().contains("corner") || marketLine.getBetTypeName().contains("角球")) {
                marketLine.setBetTypeGroupName("c");
            } else if (imSportEvents.getEventGroupId() == 3 && (marketLine.getBetTypeName().toLowerCase().contains("booking") || marketLine.getBetTypeName().contains("获牌"))) {
                marketLine.setBetTypeGroupName("b");
            } else if (SoccerRule.goals.contains(marketLine.getBetTypeId())) {
                marketLine.setBetTypeGroupName("s");
            } else if (SoccerRule.halves.contains(marketLine.getPeriodId())) {
                marketLine.setBetTypeGroupName("f");
            } else if (SoccerRule.period.contains(marketLine.getBetTypeId())) {
                marketLine.setBetTypeGroupName("period");
            } else if (SoccerRule.specials.contains(marketLine.getBetTypeId())) {
                marketLine.setBetTypeGroupName("i");
            }
        }
    }

    // 篮球规则
    public static class BasketballRule {
        //全场
        public static final Set<Integer> fulltime = new HashSet<>(Collections.singletonList(1));
        //半场
        public static final Set<Integer> halves = new HashSet<>(Arrays.asList(2, 3));
        //球队
        public static final Set<Integer> team = new HashSet<>(Arrays.asList(93, 94));
    }

    private void basketballOrganizedMarkLinesWith(MatchInfo imSportEvents) {
        for (MarketLine marketLine : imSportEvents.getMarketLines()) {
            if (BasketballRule.fulltime.contains(marketLine.getPeriodId())) {
                marketLine.setBetTypeGroupName("ft");
            } else if (BasketballRule.halves.contains(marketLine.getPeriodId())) {
                marketLine.setBetTypeGroupName("f");
            } else if (marketLine.getBetTypeName().contains("节") || marketLine.getBetTypeName().toLowerCase().contains("quarter")) {
                marketLine.setBetTypeGroupName("q");
            } else if (BasketballRule.team.contains(marketLine.getBetTypeId())) {
                marketLine.setBetTypeGroupName("team");
            }
        }
    }

    // 默认规则
    public static class DefaultRule {

        //让球&大小
        public static final Set<Integer> ahou = new HashSet<>(Arrays.asList(1, 2));

        //赌赢
        public static final Set<Integer> ml = new HashSet<>(Collections.singletonList(4));
    }

    private void defaultOrganizedMarkLinesWith(MatchInfo imSportEvents) {
        for (MarketLine marketLine : imSportEvents.getMarketLines()) {
            if (DefaultRule.ahou.contains(marketLine.getBetTypeId())) {
                marketLine.setBetTypeGroupName("h");
            } else if (DefaultRule.ml.contains(marketLine.getBetTypeId()) || marketLine.getBetTypeName().contains("独赢") || marketLine.getBetTypeName().toLowerCase().contains("1x2")) {
                marketLine.setBetTypeGroupName("ml");
            }
        }
    }
}
