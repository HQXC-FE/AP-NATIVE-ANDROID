package com.xtree.bet.bean.ui;


import androidx.annotation.NonNull;

import com.xtree.bet.bean.response.im.WagerSelection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CategoryIm implements Category {
    private final List<PlayType> playTypeList = new ArrayList<>();
    /**
     * marketName : 热门
     * orderNo : 1
     * plays : [128,1,2,131,3,4,68,132,5,6,135,7,136,137,12,13,333,14,15,335,17,18,19,20,27,28,32,33,225,34,101,102,104,237,114,126,127]
     * id : 48
     */

    /**
     * 玩法集名称
     */
    private String marketName;

    /**
     * 排序编号
     */
    private int orderNo;
    /**
     * 玩法集id
     */
    private String id;
    /**
     * 玩法id
     */
    private List<Integer> plays;

    public CategoryIm(String marketName) {
        this.marketName = marketName;
    }

    @Override
    public List<PlayType> getPlayTypeList() {
//        return (playTypeList);
        return mergePlayTypes(playTypeList);
    }


    public List<PlayType> mergePlayTypes(List<PlayType> playTypeList) {
        Map<String, PlayTypeIm> mergedMap = new LinkedHashMap<>();

        for (PlayType playType : playTypeList) {
            if (!(playType instanceof PlayTypeIm)) continue;

            PlayTypeIm playTypeIm = (PlayTypeIm) playType;
            String name = playTypeIm.getPlayTypeName();

            if (!mergedMap.containsKey(name)) {
                mergedMap.put(name, playTypeIm);
            } else {
                PlayTypeIm existing = mergedMap.get(name);

                assert existing != null;
                List<WagerSelection> existingSelections = getWagerSelections(existing, playTypeIm);

                existing.getPlayTypeInfo().setWagerSelections(existingSelections);
            }
        }

        return new ArrayList<>(mergedMap.values());
    }

    @NonNull
    private static List<WagerSelection> getWagerSelections(PlayTypeIm existing, PlayTypeIm playTypeIm) {
        List<WagerSelection> existingSelections = existing.getPlayTypeInfo().getWagerSelections();
        List<WagerSelection> currentSelections = playTypeIm.getPlayTypeInfo().getWagerSelections();

        if (existingSelections == null) {
            existingSelections = new ArrayList<>();
        }

        if (currentSelections != null) {
            // 用 Set 去重合并 WagerSelection
            Set<WagerSelection> mergedSet = new LinkedHashSet<>(existingSelections);
            mergedSet.addAll(currentSelections);
            existingSelections = new ArrayList<>(mergedSet);
        }
        return existingSelections;
    }


    @Override
    public void addPlayTypeList(PlayType playType) {
        playTypeList.add(playType);
    }

    @Override
    public int getSort() {
        return 0;
    }

    public String getName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getPlays() {
        return plays;
    }

    public void setPlays(List<Integer> plays) {
        this.plays = plays;
    }
}
