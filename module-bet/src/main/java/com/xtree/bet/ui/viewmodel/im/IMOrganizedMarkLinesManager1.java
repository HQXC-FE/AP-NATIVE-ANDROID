package com.xtree.bet.ui.viewmodel.im;

import com.xtree.bet.bean.response.im.EventListRsp;
import com.xtree.bet.bean.response.im.MarketLine;
import com.xtree.bet.bean.response.im.RecommendedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class IMOrganizedMarkLinesManager1 {

    public static final IMOrganizedMarkLinesManager1 shared = new IMOrganizedMarkLinesManager1();

    private IMOrganizedMarkLinesManager1() {
    }

    public void organizedMarkLinesWith(EventListRsp.Sport imSport, RecommendedEvent imSportEvents, Map<String, List<MarketLine>> organizedArray) {
        if (imSport.getSportId() == 1) {
            soccerOrganizedMarkLinesWith(imSportEvents, organizedArray);
        } else if (imSport.getSportId() == 2) {
            basketballOrganizedMarkLinesWith(imSportEvents, organizedArray);
        } else {
            defaultOrganizedMarkLinesWith(imSportEvents, organizedArray);
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

    private void soccerOrganizedMarkLinesWith(RecommendedEvent imSportEvents, Map<String, List<MarketLine>> organizedArray) {
        for (MarketLine marketLine : imSportEvents.getMarketLines()) {
            if (SoccerRule.ahou.contains(marketLine.getBetTypeId())) {
                organizedArray.computeIfAbsent("h", s -> new ArrayList<>()).add(marketLine);
            }
            if (SoccerRule.cs.contains(marketLine.getBetTypeId())) {
                organizedArray.computeIfAbsent("cs", k -> new ArrayList<>()).add(marketLine);
            }
            if ((SoccerRule.corner.contains(marketLine.getBetTypeId()) && imSportEvents.getGroundTypeId() == 2) || marketLine.getBetTypeName().toLowerCase().contains("corner") || marketLine.getBetTypeName().contains("角球")) {
                organizedArray.computeIfAbsent("c", k -> new ArrayList<>()).add(marketLine);
            }
            if (imSportEvents.getEventGroupId() == 3 && (marketLine.getBetTypeName().toLowerCase().contains("booking") || marketLine.getBetTypeName().contains("获牌"))) {
                organizedArray.computeIfAbsent("b", k -> new ArrayList<>()).add(marketLine);
            }
            if (SoccerRule.goals.contains(marketLine.getBetTypeId())) {
                organizedArray.computeIfAbsent("s", k -> new ArrayList<>()).add(marketLine);
            }
            if (SoccerRule.halves.contains(marketLine.getPeriodId())) {
                organizedArray.computeIfAbsent("f", k -> new ArrayList<>()).add(marketLine);
            }
            if (SoccerRule.period.contains(marketLine.getBetTypeId())) {
                organizedArray.computeIfAbsent("period", k -> new ArrayList<>()).add(marketLine);
            }
            if (SoccerRule.specials.contains(marketLine.getBetTypeId())) {
                organizedArray.computeIfAbsent("i", k -> new ArrayList<>()).add(marketLine);
            }
        }
    }

    // 篮球规则
    public static class BasketballRule {
        public static final Set<Integer> fulltime = new HashSet<>(Collections.singletonList(1));
        public static final Set<Integer> halves = new HashSet<>(Arrays.asList(2, 3));
        public static final Set<Integer> team = new HashSet<>(Arrays.asList(93, 94));
    }

    private void basketballOrganizedMarkLinesWith(RecommendedEvent imSportEvents, Map<String, List<MarketLine>> organizedArray) {
        for (MarketLine marketLine : imSportEvents.MarketLines) {
            if (BasketballRule.fulltime.contains(marketLine.getPeriodId())) {
                organizedArray.computeIfAbsent("ft", k -> new ArrayList<>()).add(marketLine);
            }
            if (BasketballRule.halves.contains(marketLine.getPeriodId())) {
                organizedArray.computeIfAbsent("f", k -> new ArrayList<>()).add(marketLine);
            }
            if (marketLine.getBetTypeName().contains("节") || marketLine.getBetTypeName().toLowerCase().contains("quarter")) {
                organizedArray.computeIfAbsent("q", k -> new ArrayList<>()).add(marketLine);
            }
            if (BasketballRule.team.contains(marketLine.getBetTypeId())) {
                organizedArray.computeIfAbsent("team", k -> new ArrayList<>()).add(marketLine);
            }
        }
    }

    // 默认规则
    public static class DefaultRule {
        public static final Set<Integer> ahou = new HashSet<>(Arrays.asList(1, 2));
        public static final Set<Integer> ml = new HashSet<>(Collections.singletonList(4));
    }

    private void defaultOrganizedMarkLinesWith(RecommendedEvent imSportEvents, Map<String, List<MarketLine>> organizedArray) {
        for (MarketLine marketLine : imSportEvents.MarketLines) {
            if (DefaultRule.ahou.contains(marketLine.getBetTypeId())) {
                organizedArray.computeIfAbsent("h", k -> new ArrayList<>()).add(marketLine);
            }
            if (DefaultRule.ml.contains(marketLine.getBetTypeId()) || marketLine.getBetTypeName().contains("独赢") || marketLine.getBetTypeName().toLowerCase().contains("1x2")) {
                organizedArray.computeIfAbsent("ml", k -> new ArrayList<>()).add(marketLine);
            }
        }
    }
}
