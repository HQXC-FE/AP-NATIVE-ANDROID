package com.xtree.live.ui.main.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.xtree.base.net.HttpCallBack;
import com.xtree.live.LiveConfig;
import com.xtree.live.chat.RequestUtils;
import com.xtree.live.data.LiveRepository;
import com.xtree.live.data.source.httpnew.LiveRep;
import com.xtree.live.data.source.response.SearchChatRoomInfo;
import com.xtree.live.message.ChatRoomInfo;
import com.xtree.live.message.ChatRoomPin;
import com.xtree.live.uitl.UnreadUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class ChatRoomListViewModel extends BaseViewModel<LiveRepository> {
    public ChatRoomListViewModel(@NonNull Application application) {
        super(application);
    }

    public ChatRoomListViewModel(@NonNull Application application, LiveRepository model) {
        super(application, model);
    }

    public MutableLiveData<List<ChatRoomInfo>> chatRoomResponseMutableLiveData = new MutableLiveData<>();//聊天房列表
    public MutableLiveData<Boolean> onRefreshDone = new MutableLiveData<>();
    public MutableLiveData<ChatRoomInfo> chatRoomInfoMutableLiveData = new MutableLiveData<>();//搜索助理

    private final List<ChatRoomInfo> mRoomInfoList = new ArrayList<>();

    public @NonNull List<ChatRoomInfo> getRoomInfoList() {
        return mRoomInfoList;
    }

    public void sortRoomInfoList() {
        if (!mRoomInfoList.isEmpty()) {
            Collections.sort(mRoomInfoList, (o1, o2) -> {
                int first = o2.getPinTime() - o1.getPinTime();
                if (first != 0) return first;
                return Math.toIntExact(o2.getUpdateTime() - o1.getUpdateTime());
            });
        }
        chatRoomResponseMutableLiveData.postValue(mRoomInfoList);
    }


    private boolean gettingChatRoomList;

    public void getChatRoomList(String type) {
        if (gettingChatRoomList) return;
        gettingChatRoomList = true;
        JsonObject json = new JsonObject();
        json.addProperty("type", type);

        LiveRep.getInstance().getChatRoomList(RequestUtils.getRequestBody(json))
                .subscribe(new HttpCallBack<List<ChatRoomInfo>>() {
                    @Override
                    public void onResult(List<ChatRoomInfo> data) {
                        mRoomInfoList.clear();
                        if (data == null) {
                            chatRoomResponseMutableLiveData.postValue(data);
                            return;
                        }
                        mRoomInfoList.addAll(data);
                        //每次从聊天列表中重置未读消息列表
                        UnreadUtils.refreshUnreadMapFromChatRoomList(mRoomInfoList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRoomInfoList.clear();
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        mRoomInfoList.clear();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        gettingChatRoomList = false;
                        sortRoomInfoList();
                        onRefreshDone.postValue(true);
                    }
                });

    }

    public void searchAssistant(String nickName) {
        JsonObject json = new JsonObject();
        json.addProperty("nickName", nickName);

        LiveRep.getInstance().searchAssistant(RequestUtils.getRequestBody(json))
                .subscribe(new HttpCallBack<SearchChatRoomInfo>() {
                    @Override
                    public void onResult(SearchChatRoomInfo data) {
                        ChatRoomInfo newRoomInfo = data.toChatRoomInfo();
                        chatRoomInfoMutableLiveData.postValue(newRoomInfo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        chatRoomInfoMutableLiveData.postValue(null);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        chatRoomInfoMutableLiveData.postValue(null);
                    }
                });
    }


    private boolean isPiningChatRoom = false;

    public void pinChatRoom(ChatRoomInfo item) {
        if (!LiveConfig.isLogin()) return;
        if (isPiningChatRoom) return;
        isPiningChatRoom = true;
        JsonObject json = new JsonObject();
        json.addProperty("status", item.getIsPin() == 0 ? 1 : 0);
        json.addProperty("vid", item.getVid());
        json.addProperty("userId", LiveConfig.getUserId());

        LiveRep.getInstance().pinChatRoom(RequestUtils.getRequestBody(json))
                .subscribe(new HttpCallBack<ChatRoomPin>() {
                    @Override
                    public void onResult(ChatRoomPin data) {
                        if (data.getVid().equals(item.getVid())) {
                            item.setIsPin(data.getIsPin());
                            item.setPinTime(data.getPinTime());
                            EventBus.getDefault().post(item);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        ToastUtils.showShort(t.getMessage());
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        ToastUtils.showShort(t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        isPiningChatRoom = false;
                    }
                });
    }

}


