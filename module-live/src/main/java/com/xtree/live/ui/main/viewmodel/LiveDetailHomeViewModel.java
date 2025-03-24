package com.xtree.live.ui.main.viewmodel;

import static com.xtree.live.chat.Subscription.getComposite;

import android.app.Application;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UriUtils;
import com.blankj.utilcode.util.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hjq.gson.factory.GsonFactory;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.live.LiveConfig;
import com.xtree.live.R;
import com.xtree.live.chat.InOutRoomHelper;
import com.xtree.live.chat.RequestUtils;
import com.xtree.live.data.AdsBean;
import com.xtree.live.data.LiveRepository;
import com.xtree.live.data.source.httpnew.LiveRep;
import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.message.ConversationMessage;
import com.xtree.live.message.ConversationScrollButtonState;
import com.xtree.live.message.DeliverStatus;
import com.xtree.live.message.MessageRecord;
import com.xtree.live.message.RoomType;
import com.xtree.live.message.SystemMessageRecord;
import com.xtree.live.message.inroom.InRoomData;
import com.xtree.live.model.AccumulatedRechargeRes;
import com.xtree.live.uitl.JsonUtil;
import com.xtree.live.uitl.RxStore;
import com.xtree.live.uitl.WordUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.xtree.mvvmhabit.base.BaseApplication;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

// 这里必须要使用 原来的 LiveRepository ，不然即使使用 BaseModel也会崩溃。没用使用也不能删掉，否则崩溃
public class LiveDetailHomeViewModel extends BaseViewModel<LiveRepository> {

    public LiveDetailHomeViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveDetailHomeViewModel(@NonNull Application application, LiveRepository model) {
        super(application, model);
    }
    private int uid;
    private String vid,pmSourceType,pmSourceTypeStr;

    public MutableLiveData<List<MessageRecord>> listMessageRecord = new MutableLiveData<List<MessageRecord>>();
    public MutableLiveData<Pair<Boolean, List<MessageRecord>>> pairMessageRecord = new MutableLiveData<>();
    public MutableLiveData<LiveRoomBean> liveRoomData = new MutableLiveData<>();
    public MutableLiveData<List<SystemMessageRecord>> listSystemMessageRecord = new MutableLiveData<>();
    public MutableLiveData<InRoomData> inRoomDataMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<AdsBean>> listAdBeanMutable = new MutableLiveData<>();
    public MutableLiveData<ConversationMessage> conversationMessageMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<AccumulatedRechargeRes> accumulatedRechargeResMutableLiveData = new MutableLiveData<>();


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }

    public final int DEFAULT_LIMIT = 50;
    private List<MessageRecord> pendingMessageRecordList;

    public Flowable<ConversationScrollButtonState> scrollButtonState;
    private RxStore<ConversationScrollButtonState> scrollButtonStateStore;

    @Override
    public void onCreate() {
        super.onCreate();
        scrollButtonStateStore = new RxStore<>(new ConversationScrollButtonState(), Schedulers.computation());
        scrollButtonStateStore.addTo(getComposite());
        scrollButtonState = scrollButtonStateStore.getStateFlowable()
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread());
        pendingMessageRecordList = new ArrayList<>();
    }

    public void setShowScrollButtonsForScrollPosition(boolean showScrollButtonsForScrollPosition, boolean willScrollToBottomOnNewMessage, int unreadCount) {
        scrollButtonStateStore.update(state -> state.copy(showScrollButtonsForScrollPosition, willScrollToBottomOnNewMessage, unreadCount));
    }

    public void addUnreadCount(int unreadCount) {
        scrollButtonStateStore.update(state -> state.copy(state.getShowScrollButtonsForScrollPosition(), state.getWillScrollToBottomOnNewMessage(), state.getUnreadCount() + unreadCount));
    }

    public int unreadCount() {
        return scrollButtonStateStore.getState().getUnreadCount();
    }

    private boolean isUpdatingChatHistory = false;

    public void updateChatHistory(@RoomType int type, int uid, String vid) {
        if (lackHistoryParams(type, uid, vid)) return;
        if (isUpdatingChatHistory) return;
        isUpdatingChatHistory = true;

        HttpCallBack<List<MessageRecord>> callBack = new HttpCallBack<List<MessageRecord>>() {
            @Override
            public void onResult(List<MessageRecord> messageRecords) {
                listMessageRecord.postValue(messageRecords);
            }
        };

        if (type == RoomType.PAGE_CHAT_PRIVATE_ANCHOR) {
            LiveRep.getInstance().getAnchorChatHistory(uid, "0", DEFAULT_LIMIT).subscribe(callBack);
        } else {
            LiveRep.getInstance().getChatHistory(type, vid, "0", DEFAULT_LIMIT).subscribe(callBack);
        }

    }

    //这个参数主要用于过滤 baseAdapter的bug notifyDataSet 调用onBindViewHolder 会触发loadMore
    private String mRequestMsgId;
    private boolean isAutoLoadMore = true;

    public void loadMoreChatHistory(String lastMsgId, @RoomType int type, int uid, String vid) {
        Log.d("api", "mRequestMsgId:" + mRequestMsgId + "\nlastMsgId:" + lastMsgId);
        if (Objects.equals(mRequestMsgId, lastMsgId)) return;
        Log.d("api", "isAutoLoadMore:" + isAutoLoadMore);
        if (!isAutoLoadMore) return;
        getChatHistoryWithAutoLoad(false, lastMsgId, type, uid, vid, null);
    }

    public void refreshChatHistory(@RoomType int type, int uid, String vid) {

        isAutoLoadMore = true;
        getChatHistoryWithAutoLoad(true, "0", type, uid, vid, null);
    }

    private boolean isGettingChatHistory;

    private void getChatHistoryWithAutoLoad(boolean isRefresh, String lastMsgId, @RoomType int type, int uid, String vid, @Nullable Runnable autoload) {

        if (lackHistoryParams(type, uid, vid)) return;
        if (isGettingChatHistory) return;
        isGettingChatHistory = true;

        HttpCallBack<List<MessageRecord>> callBack = new HttpCallBack<List<MessageRecord>>() {
            @Override
            public void onResult(List<MessageRecord> data) {
                int size = data.size();
                boolean isLoadEnd = size == 0;

                mRequestMsgId = lastMsgId;
                isAutoLoadMore = !isLoadEnd;
                pairMessageRecord.postValue(new Pair<>(isRefresh, data));
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                isAutoLoadMore = false;
                pairMessageRecord.postValue(new Pair<>(isRefresh, null));
            }

            @Override
            public void onFail(BusinessException t) {
                super.onFail(t);
                isAutoLoadMore = false;
                pairMessageRecord.postValue(new Pair<>(isRefresh, null));
            }

            @Override
            public void onComplete() {
                super.onComplete();
                isGettingChatHistory = false;
                if (autoload != null) autoload.run();

            }
        };

        if (type == RoomType.PAGE_CHAT_PRIVATE_ANCHOR) {
            LiveRep.getInstance().getAnchorChatHistory(uid, lastMsgId, DEFAULT_LIMIT).subscribe(callBack);
        } else {
            LiveRep.getInstance().getChatHistory(type, vid, lastMsgId, DEFAULT_LIMIT).subscribe(callBack);
        }

    }

    private boolean lackHistoryParams(int type, int uid, String vid) {
        if (type != RoomType.PAGE_CHAT_PRIVATE_ANCHOR && TextUtils.isEmpty(vid)) return true;
        return type == RoomType.PAGE_CHAT_PRIVATE_ANCHOR && uid == 0;
    }


    public void getLiveInroomLog(String vid) {

        LiveRep.getInstance().getLiveInroomLog(vid, 50)
                .subscribe(new HttpCallBack<List<SystemMessageRecord>>() {
                    @Override
                    public void onResult(List<SystemMessageRecord> systemMessageRecords) {
                        listSystemMessageRecord.postValue(systemMessageRecords);
                    }
                });
    }

    /**
     * 进入主播私聊房间
     * 1 判断 是否获取vid  否则 inviteRoom 获取到 直接走inRoom
     * 广场不需要inRoom
     */
    public void enterRoom(@RoomType int roomType, int uid, String vid, List<ConversationMessage> list) {

        if (roomType != RoomType.PAGE_CHAT_PRIVATE_ANCHOR && TextUtils.isEmpty(vid)) {
            if (roomType == RoomType.PAGE_CHAT_GLOBAL) {
                requestGlobeVid(uid, () -> {
                    InOutRoomHelper.inRoom(vid);
                    pin(RoomType.PAGE_CHAT_GLOBAL, vid);
                    refreshChatHistory(RoomType.PAGE_CHAT_GLOBAL, uid, vid);
                });
            }
            return;
        }
        pin(roomType, vid);
        if (list.isEmpty()) {
            InOutRoomHelper.inRoom(vid);
            refreshChatHistory(roomType, uid, vid);
        } else {
            InOutRoomHelper.inRoom(vid);
            //漏消息无所谓
            updateChatHistory(roomType, uid, vid);
        }
    }

    public void requestGlobeVid(int uid, @NonNull Runnable runnable) {

        LiveRep.getInstance().getLiveDetail(uid).subscribe(new HttpCallBack<LiveRoomBean>() {
            @Override
            public void onResult(LiveRoomBean liveRoomBean) {
                liveRoomData.postValue(liveRoomBean);
                runnable.run();
            }

        });

    }

    public void pin(@RoomType int roomType, String vid) {
        if (TextUtils.isEmpty(vid)) return;
        if (roomType == RoomType.PAGE_CHAT_PRIVATE_ANCHOR) return;
        if (roomType == RoomType.PAGE_CHAT_PRIVATE) return;
        JsonObject json = new JsonObject();
        json.addProperty("room_type", roomType);
        json.addProperty("vid", vid);

        LiveRep.getInstance().pin(RequestUtils.getRequestBody(json))
                .subscribe(new HttpCallBack<InRoomData>() {
                    @Override
                    public void onResult(InRoomData inRoomData) {
                        inRoomDataMutableLiveData.postValue(inRoomData);
                    }
                });
    }


    public void getAdList() {
        LiveRep.getInstance().getAdList().subscribe(new HttpCallBack<List<AdsBean>>() {
            @Override
            public void onResult(List<AdsBean> adsBeans) {
                listAdBeanMutable.postValue(adsBeans);
            }
        });

    }

    @NonNull
    private static ArrayList<String> sendMessageApiPathList(int roomType) {
        return new ArrayList<String>() {{
            add("api");
            add("chat");
            if (roomType == RoomType.PAGE_CHAT_PRIVATE_ANCHOR) {
                add("sendToAnchor");
            } else if (roomType == RoomType.PAGE_CHAT_PRIVATE) {
                add("sendToAssistant");
            } else {
                add("sendMessage");
            }

        }};
    }

    private Uri file2Uri(final File file) {
        if (!FileUtils.isFileExists(file)) return null;
        String authority = BaseApplication.getInstance().getPackageName() + ".fileProvider";
        return FileProvider.getUriForFile(Utils.getApp(), authority, file);
    }

    private void deleteFile(File file) {
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.d("ChatPresenter", "---delete file---");
            }
        }
    }

    public void sendDanmu(int giftId, int uid, int level, String text) {

    }

    public void sendText(int roomType,int uid, String vid,String text) {
        String sender = LiveConfig.getUserId();;
        String seed = EncryptUtils.encryptMD5ToString(sender + vid + System.currentTimeMillis());
        sendText(roomType,uid,vid, seed, text, null);
    }

    private void sendText(@RoomType int roomType,int uid,String vid, String seed, String text, @Nullable Runnable onFinish) {
//        是否主播私聊
        int msgType = roomType == RoomType.PAGE_CHAT_GLOBAL ? 0 : 1;
        Map<String, Object> map = sendRequestMap(roomType,uid,vid, seed, msgType);
        map.put("text", text);

        HttpCallBack<JsonElement> callBack = new HttpCallBack<JsonElement>() {

            @Override
            public void onResult(JsonElement data) {
                saveVid(roomType, data);
                conversationMessageMutableLiveData.postValue(createMessageTextPending(roomType, msgType, LiveConfig.getUserId(), vid, seed, text, DeliverStatus.STATUS_COMPLETE));
                onProcessReceiveAnchorMessage(roomType, data);
            }

            @Override
            public void onFail(BusinessException t) {
                super.onFail(t);
                if(!t.message.isEmpty() && t.message.equals(WordUtil.getString(R.string.you_are_ban))) {
                    ToastUtils.showShort(WordUtil.getString(R.string.disallow_send_message_for_ban));
                } else {
                    conversationMessageMutableLiveData.postValue(createMessageTextPending(roomType, msgType, LiveConfig.getUserId(), vid, seed, text, DeliverStatus.STATUS_COMPLETE));

                }
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                conversationMessageMutableLiveData.postValue(createMessageTextPending(roomType, msgType, LiveConfig.getUserId(), vid, seed, text, DeliverStatus.STATUS_COMPLETE));

            }

            @Override
            public void onComplete() {
                super.onComplete();
                if (onFinish != null) onFinish.run();
            }
        };
        if (roomType == RoomType.PAGE_CHAT_PRIVATE_ANCHOR) {
            LiveRep.getInstance().sendToAnchor(RequestUtils.getRequestBody(map)).subscribe(callBack);
        } else if (roomType == RoomType.PAGE_CHAT_PRIVATE) {
            LiveRep.getInstance().sendToAssistant(RequestUtils.getRequestBody(map)).subscribe(callBack);
        } else {
            LiveRep.getInstance().sendMessage(RequestUtils.getRequestBody(map)).subscribe(callBack);
        }
    }


    public void sendEmojiGif(@RoomType int roomType,int uid ,String vid, String picture) {
        String sender = LiveConfig.getUserId();
        String seed = EncryptUtils.encryptMD5ToString(sender + vid + System.currentTimeMillis());
        sendEmojiGif(roomType, uid,vid,seed, picture, null);
    }

    private void sendEmojiGif(@RoomType int roomType,int uid, String vid, String seed, String picture, @Nullable Runnable onFinish) {
        Map<String, Object> map = sendRequestMap(roomType,uid,vid, seed, 5);
        map.put("pic", picture);

        HttpCallBack<JsonElement> callBack = new HttpCallBack<JsonElement>() {

            @Override
            public void onResult(JsonElement data) {
                saveVid(roomType, data);
                conversationMessageMutableLiveData.postValue(createMessageTextPending(roomType, 5, LiveConfig.getUserId(), vid, seed, picture, DeliverStatus.STATUS_COMPLETE));
                onProcessReceiveAnchorMessage(roomType, data);
            }

            @Override
            public void onFail(BusinessException t) {
                super.onFail(t);
                if(!t.message.isEmpty() && t.message.equals(WordUtil.getString(R.string.you_are_ban))) {
                    ToastUtils.showShort(WordUtil.getString(R.string.disallow_send_message_for_ban));
                } else {
                    conversationMessageMutableLiveData.postValue(createMessageTextPending(roomType, 5, LiveConfig.getUserId(), vid, seed, picture, DeliverStatus.STATUS_COMPLETE));
                }
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                conversationMessageMutableLiveData.postValue(createMessageTextPending(roomType, 5, LiveConfig.getUserId(), vid, seed, picture, DeliverStatus.STATUS_COMPLETE));
            }

            @Override
            public void onComplete() {
                super.onComplete();
                if (onFinish != null) onFinish.run();
            }
        };

        if (roomType == RoomType.PAGE_CHAT_PRIVATE_ANCHOR) {
            LiveRep.getInstance().sendToAnchor(RequestUtils.getRequestBody(map)).subscribe(callBack);
        } else if (roomType == RoomType.PAGE_CHAT_PRIVATE) {
            LiveRep.getInstance().sendToAssistant(RequestUtils.getRequestBody(map)).subscribe(callBack);
        } else {
            LiveRep.getInstance().sendMessage(RequestUtils.getRequestBody(map)).subscribe(callBack);
        }
    }


    public void sendPhoto(@RoomType int roomType,int uid,String vid, File pic) {
        String sender = LiveConfig.getUserId();
        String seed = EncryptUtils.encryptMD5ToString(sender + vid + System.currentTimeMillis());
        sendPhoto(roomType,uid,vid, seed, pic, null);
    }


    private void sendPhoto(int roomType,int uid,String vid, String seed, File pic, @Nullable Runnable onFinish) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("pic", seed + ".jpg", RequestBody.create(pic, MediaType.parse("image/*")));
        Map<String, RequestBody> bodyMap = new HashMap<>();
        Map<String, Object> map = sendRequestMap(roomType,uid,vid, seed, 2);
//        String timestamp = "" + ApiClient.getTimestamp();
//        String key = Helper.productSign(timestamp, sendMessageApiPathList(roomType));
//        String sign = RequestUtils.getApiFrontSign(map, key);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if(value != null)bodyMap.put(entry.getKey(), RequestBody.create(value.toString(), MediaType.parse("text/plain")));
        }


        HttpCallBack<JsonElement> callBack = new HttpCallBack<JsonElement>() {
            @Override
            public void onResult(JsonElement data) {
                saveVid(roomType, data);

                Uri uri = file2Uri(pic);
                if (uri != null) {
                    conversationMessageMutableLiveData.postValue(createMessageTextPending(roomType, 2, LiveConfig.getUserId(), vid, seed, uri.toString(), DeliverStatus.STATUS_COMPLETE));
                }
                onProcessReceiveAnchorMessage(roomType, data);
            }

            @Override
            public void onFail(BusinessException t) {
                super.onFail(t);
                Uri uri = file2Uri(pic);
                if (uri != null) {
                    conversationMessageMutableLiveData.postValue(createMessageTextPending(roomType, 2, LiveConfig.getUserId(), vid, seed, uri.toString(), DeliverStatus.STATUS_COMPLETE));
                }
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                Uri uri = file2Uri(pic);
                if (uri != null) {
                    conversationMessageMutableLiveData.postValue(createMessageTextPending(roomType, 2, LiveConfig.getUserId(), vid, seed, uri.toString(), DeliverStatus.STATUS_COMPLETE));
                }
            }
        };

        if (roomType == RoomType.PAGE_CHAT_PRIVATE_ANCHOR) {
            LiveRep.getInstance().sendToAnchor(bodyMap,filePart).subscribe(callBack);
        } else if (roomType == RoomType.PAGE_CHAT_PRIVATE) {
            LiveRep.getInstance().sendToAssistant(bodyMap,filePart).subscribe(callBack);
        } else {
            LiveRep.getInstance().sendMessage(bodyMap,filePart).subscribe(callBack);
        }
    }

    private void saveVid(int roomType, JsonElement data) {
        if (roomType != RoomType.PAGE_CHAT_PRIVATE_ANCHOR) return;
        if (data instanceof JsonObject) {
            String vid = JsonUtil.getString((JsonObject) data, "vid");
            if (!TextUtils.isEmpty(vid) && !Objects.equals(this.vid, vid)) {
//                mvpView().setRoomVid(vid);
                pin(roomType, vid);
                InOutRoomHelper.inRoom(vid);
                refreshChatHistory(roomType,uid,vid);
            }
        }
    }

    private void onProcessReceiveAnchorMessage(int roomType, JsonElement data) {
        if (roomType != RoomType.PAGE_CHAT_PRIVATE_ANCHOR) return;
        if (!(data instanceof JsonObject)) return;
        JsonObject json = (JsonObject) data;
        String message = JsonUtil.getString(json, "msg");
        if (TextUtils.isEmpty(message)) return;
        MessageRecord messageRecord = JsonUtil.fromJson(GsonFactory.getSingletonGson(), JsonUtil.getString(json, "msg"), MessageRecord.class);
        if (messageRecord == null) return;
        ConversationMessage conversationMessage = new ConversationMessage(messageRecord, this.uid);
        conversationMessage.setDeliveryStatus(DeliverStatus.STATUS_COMPLETE);
        conversationMessageMutableLiveData.postValue(conversationMessage);
    }


    /**
     * 发送中 text
     */
    @NonNull
    private ConversationMessage createMessageTextPending(@RoomType int roomType, int msgType, String sender, String vid, String seed, String text, @DeliverStatus int status) {
        MessageRecord messageSend = new MessageRecord();
        messageSend.setType(roomType);
        messageSend.setVid(vid);
        messageSend.setText(text);
        messageSend.setSeed(seed);
        messageSend.setSender(sender);
        messageSend.setTime(TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
//        UserInfo userBean = AppConfig.getUserBean();
//        if (userBean != null) {
//            messageSend.setSenderNickname(userBean.getUserNickname());
//            messageSend.setAvatar(userBean.getAvatar());
//            messageSend.setSenderExp(userBean.getExp());
//            messageSend.setSenderType(2);
//        } else {
//            messageSend.setSenderType(0);
//        }
        String nickName = SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME);
        if(!TextUtils.isEmpty(nickName)){
            messageSend.setSenderNickname(nickName);
            messageSend.setSenderType(2);
        } else {
            messageSend.setSenderType(0);
        }

        messageSend.setMsgType(msgType);
        ConversationMessage conversationMessage = new ConversationMessage(messageSend,uid);
        conversationMessage.setDeliveryStatus(status);
        if (DeliverStatus.STATUS_PENDING == status)
            pendingMessageRecordList.add(messageSend);
        return conversationMessage;

    }

    @NonNull
    private ConversationMessage createMessageImagePending(int roomType, int msgType, String sender, String vid, String seed, String path, @DeliverStatus int status) {
        MessageRecord messageSend = new MessageRecord();
        messageSend.setType(roomType);
        messageSend.setVid(vid);
        messageSend.setSeed(seed);
        messageSend.setSender(sender);
        messageSend.setPic(path);
        messageSend.setMsgType(msgType);
        messageSend.setTime(TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
//        UserInfo userBean = AppConfig.getUserBean();
//        if (userBean != null) {
//            messageSend.setSenderNickname(nickName);
//            messageSend.setAvatar(userBean.getAvatar());
//            messageSend.setSenderExp(userBean.getExp());
//            messageSend.setSenderType(2);
//        } else {
//            messageSend.setSenderType(0);
//        }

        String nickName = SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME);
        if(!TextUtils.isEmpty(nickName)){
            messageSend.setSenderNickname(nickName);
            messageSend.setSenderType(2);
        } else {
            messageSend.setSenderType(0);
        }

        if (DeliverStatus.STATUS_PENDING == status)
            pendingMessageRecordList.add(messageSend);
        ConversationMessage conversationMessage = new ConversationMessage(messageSend, uid);
        conversationMessage.setDeliveryStatus(status);
        return conversationMessage;

    }

    /**
     * 发送pending消息
     */
    public void postPendingMessages(@Nullable Runnable runnable,int uid,String vid) {
        if (pendingMessageRecordList.isEmpty()) {
            if (runnable != null) runnable.run();
            return;
        }
        MessageRecord messageRecord = pendingMessageRecordList.remove(0);
        switch (messageRecord.getMsgType()) {
            case 0:
            case 1:
                sendText(messageRecord.getType(),
                        uid,vid,
                        messageRecord.getSeed(),
                        messageRecord.getText(),
                        () -> postPendingMessages(runnable,uid,vid));
                break;
            case 5:
                sendEmojiGif(messageRecord.getType(),
                        uid,vid,
                        messageRecord.getSeed(),
                        messageRecord.getPic(),
                        () -> postPendingMessages(runnable,uid,vid)
                );
                break;
            case 2:
                File file = UriUtils.uri2File(Uri.parse(messageRecord.getPic()));
                if (FileUtils.isFileExists(file)) {
                    sendPhoto(messageRecord.getType(),
                            uid,vid,
                            messageRecord.getSeed(),
                            file,
                            () -> postPendingMessages(runnable,uid,vid));
                }
                break;
        }
    }

    public void sendMessage(MessageRecord messageRecord,int uid,String vid) {
        switch (messageRecord.getMsgType()) {
            case 0:
            case 1:
                sendText(messageRecord.getType(),
                        uid,vid,
                        messageRecord.getSeed(),
                        messageRecord.getText(),
                        null);
                break;
            case 5:
                sendEmojiGif(messageRecord.getType(),
                        uid,vid,
                        messageRecord.getSeed(),
                        messageRecord.getPic(),
                        null
                );
                break;
            case 2:
                File file = UriUtils.uri2File(Uri.parse(messageRecord.getPic()));
                if (FileUtils.isFileExists(file)) {
                    sendPhoto(messageRecord.getType(),
                            uid,vid,
                            messageRecord.getSeed(),
                            file,
                            null);
                }
                break;
        }
    }

    @NonNull
    private Map<String, Object> sendRequestMap(int roomType,int uid,String vid, String seed, int msgType) {
        String channelCode = LiveConfig.getChannelCode();
        Map<String, Object> map = new HashMap<>();
        map.put("text", "");
        //消息类型 0弹幕 1文字 2图片 3文字+图片 4一键登录 5图片切片上传 6图片切片上传(广告内容)
        map.put("msgType", msgType);
        map.put("seed", seed);
        map.put("sender", LiveConfig.getUserId());
        map.put("vid", vid);
        map.put("msg_type", msgType);
        map.put("color", "#000");
        map.put("type", Math.abs(roomType) + "");
        map.put("channel_code", channelCode);
        String pmSourceType = this.pmSourceType;
        if (!TextUtils.isEmpty(pmSourceType)) {
            map.put("pm_source_type", pmSourceType);
        }
        String pmSourceTypeStr = this.pmSourceTypeStr;
        if (!TextUtils.isEmpty(pmSourceTypeStr)) {
            map.put("pm_source_type_str", pmSourceTypeStr);
        }

        if (roomType == RoomType.PAGE_CHAT_PRIVATE_ANCHOR){
            map.put("anchorId", "" + uid);}

        if (roomType == RoomType.PAGE_CHAT_PRIVATE)map.put("assignId", "" + uid);

        return map;
    }

    private void deleteUnsentText(String sender, String text) {
        //删除消息
        JsonObject object = new JsonObject();
        object.addProperty("sender", sender);
        object.addProperty("text", text);
    }


    public void readMessage(String msgId) {
//        addSubscription(getApiStores().readMessage(mvpView().getRoomVid(), msgId, AppConfig.getChannel()), new NothingObserver());
    }


    public void setParamas(int mUid, String mVid, String pmSourceType, String pmSourceTypeStr) {
        this.uid = mUid;
        this.vid = mVid;
        this.pmSourceType = pmSourceType;
        this.pmSourceTypeStr = pmSourceTypeStr;
    }

    public void getAmount() {
        LiveRepository.getInstance().getAmount()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new HttpCallBack<AccumulatedRechargeRes>() {
                    @Override
                    public void onResult(AccumulatedRechargeRes data) {
                        if (data != null) {
                            accumulatedRechargeResMutableLiveData.postValue(data);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }

}
