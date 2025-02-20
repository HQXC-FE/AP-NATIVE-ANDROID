package com.xtree.live.chat;

import android.text.TextUtils;
import android.util.Log;

import com.xtree.live.BuildConfig;
import com.xtree.live.message.Message;
import com.xtree.live.message.MessageMsg;

import java.util.HashMap;
import java.util.Map;

public class Counter {
    private final long interval;
    private int sumAtInterval;
    private long lastTime;
    private long messageTotal = 0;
    private final String tag;

    private HashMap<String, Integer> senderMessageCountMap;
    public Counter(String tag, long interval) {
        this.tag = tag;
        this.interval = interval;
        lastTime = -1;
    }

    public void count(Message msg){
        if(!BuildConfig.DEBUG)return;
        if(!(msg instanceof MessageMsg))return;
        MessageMsg message = (MessageMsg) msg;
        countMessageInterval(message);
        if(message.getSenderUserType() != 2)return;
        if(!TextUtils.isEmpty(message.getComment()))return;
        countRealTotal();
        countRealSender(message);
    }

    private void countRealTotal() {
        messageTotal++;
    }

    private void countRealSender(MessageMsg message) {
        if(senderMessageCountMap == null){
            senderMessageCountMap = new HashMap<>();
        }
        String senderNickname = message.getSenderNickname();
        Integer messageCount = senderMessageCountMap.get(senderNickname);
        if(messageCount == null){
            Log.d(tag, "real sender nickName: " + message.getSenderNickname());
            Log.d(tag, "real sender count: " + senderMessageCountMap.keySet().size());
            messageCount = 1;
        }else {
            messageCount ++;
        }
        senderMessageCountMap.put(senderNickname, messageCount);
    }

    private void countMessageInterval(MessageMsg message){
        if(lastTime > 0 && System.currentTimeMillis() - lastTime > interval){
            Log.d(tag, "message sum: " + sumAtInterval +  "    时间：" + (System.currentTimeMillis() - lastTime));
            sumAtInterval = 1;
            lastTime = System.currentTimeMillis();
        }else {
            if(lastTime ==-1){
                lastTime = System.currentTimeMillis();
            }
            sumAtInterval ++;
        }
    }

    public void exitCount(){
        if(!BuildConfig.DEBUG)return;
        if(senderMessageCountMap != null){
            Log.d(tag, "sender total:" + senderMessageCountMap.keySet().size());
            Log.d(tag, "real sender message detail: \n");
            for (Map.Entry<String, Integer> entry : senderMessageCountMap.entrySet()) {
                Log.d(tag, entry.getKey() + ":" + (entry.getValue() != null ? entry.getValue() : 0));
            }
        }
        Log.d(tag, "message total: " + messageTotal);
    }
}

