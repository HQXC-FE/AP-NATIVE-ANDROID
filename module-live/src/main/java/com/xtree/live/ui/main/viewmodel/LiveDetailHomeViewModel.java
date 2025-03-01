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
import com.blankj.utilcode.util.UriUtils;
import com.blankj.utilcode.util.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hjq.gson.factory.GsonFactory;
import com.xtree.base.net.HttpCallBack;
import com.xtree.live.LiveConfig;
import com.xtree.live.R;
import com.xtree.live.chat.InOutRoomHelper;
import com.xtree.live.chat.RequestUtils;
import com.xtree.live.data.AdsBean;
import com.xtree.live.data.LiveRepository;
import com.xtree.live.data.source.httpnew.LiveRep;
import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.message.ConversationMessage;
import com.xtree.live.message.ConversationScrollButtonState;
import com.xtree.live.message.DeliverStatus;
import com.xtree.live.message.MessageRecord;
import com.xtree.live.message.RoomType;
import com.xtree.live.message.SystemMessageRecord;
import com.xtree.live.message.inroom.InRoomData;
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

    public MutableLiveData<List<MessageRecord>> listMessageRecord = new MutableLiveData<List<MessageRecord>>();
    public MutableLiveData<Pair<Boolean, List<MessageRecord>>> pairMessageRecord = new MutableLiveData<>();
    public MutableLiveData<LiveRoomBean> liveRoomData = new MutableLiveData<>();
    public MutableLiveData<List<SystemMessageRecord>> listSystemMessageRecord = new MutableLiveData<>();
    public MutableLiveData<InRoomData> inRoomDataMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<AdsBean>> listAdBeanMutable = new MutableLiveData<>();


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
//        BaseObserver<JsonElement> observer = new BaseObserver<>() {
//            @Override
//            public void onSuccess(JsonElement data, String msg) {
//                saveVid(roomType, data);
//                mvpView().onSendText(true, msg);
//                mvpView().onProcessReceiveMessage(createMessageTextPending(roomType, msgType, AppConfig.getUserId(), mvpView().getRoomVid(), seed, text, DeliverStatus.STATUS_COMPLETE));
//                onProcessReceiveAnchorMessage(roomType, data);
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                if (msg.equals(WordUtil.getString(R.string.you_are_ban))) {
//                    msg = WordUtil.getString(R.string.disallow_send_message_for_ban);
//                } else {
//                    mvpView().onProcessReceiveMessage(createMessageTextPending(roomType, msgType, AppConfig.getUserId(), mvpView().getRoomVid(), seed, text, DeliverStatus.STATUS_FAILED));
//                }
//                mvpView().onSendText(false, msg);
//            }
//
//            @Override
//            public void onError(Pair<Integer, String> error) {
//                mvpView().onSendText(false, error.second);
//                mvpView().onProcessReceiveMessage(createMessageTextPending(roomType, msgType, AppConfig.getUserId(), mvpView().getRoomVid(), seed, text, DeliverStatus.STATUS_FAILED));
//            }
//
//            @Override
//            public void onFinish() {
//                if (onFinish != null) onFinish.run();
//            }
//        };
//        if (roomType == RoomType.PAGE_CHAT_PRIVATE_ANCHOR) {
//
//            LiveRep.getInstance().sendToAnchor(RequestUtils.getRequestBody(map)).subscribe(callback);
//        } else if (roomType == RoomType.PAGE_CHAT_PRIVATE) {
//            addSubscription(getApiStores().sendToAssistant(getRequestBody(map)), observer);
//        } else {
//            addSubscription(getApiStores().sendMessage(getRequestBody(map)), observer);
//        }
    }


    public void sendEmojiGif(@RoomType int roomType, String picture) {
//        String sender = LiveConfig.getUserId();
//        String vid = mvpView().getRoomVid();
//        String seed = EncryptUtils.encryptMD5ToString(sender + vid + System.currentTimeMillis());
//        sendEmojiGif(roomType, seed, picture, null);
    }

    private void sendEmojiGif(@RoomType int roomType, String seed, String picture, @Nullable Runnable onFinish) {
//        Map<String, Object> map = sendRequestMap(roomType, seed, 5);
//        map.put("pic", picture);
//        BaseObserver<JsonElement> observer = new BaseObserver<>() {
//            @Override
//            public void onSuccess(JsonElement data, String msg) {
//                saveVid(roomType, data);
//                mvpView().onSendEmojiGif(true, msg);
//                mvpView().onProcessReceiveMessage(createMessageImagePending(roomType, 5, AppConfig.getUserId(), mvpView().getRoomVid(), seed, picture, DeliverStatus.STATUS_COMPLETE));
//                onProcessReceiveAnchorMessage(roomType, data);
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                if (msg.equals(WordUtil.getString(R.string.you_are_ban))) {
//                    msg = WordUtil.getString(R.string.disallow_send_message_for_ban);
//                } else {
//                    mvpView().onProcessReceiveMessage(createMessageImagePending(roomType, 5, AppConfig.getUserId(), mvpView().getRoomVid(), seed, picture, DeliverStatus.STATUS_FAILED));
//                }
//                mvpView().onSendEmojiGif(false, msg);
//            }
//
//            @Override
//            public void onError(Pair<Integer, String> error) {
//                mvpView().onSendEmojiGif(false, error.second);
//                mvpView().onProcessReceiveMessage(createMessageImagePending(roomType, 5, AppConfig.getUserId(), mvpView().getRoomVid(), seed, picture, DeliverStatus.STATUS_FAILED));
//            }
//
//            @Override
//            public void onFinish() {
//                if (onFinish != null) onFinish.run();
//            }
//        };
//        if (roomType == RoomType.PAGE_CHAT_PRIVATE_ANCHOR) {
//            addSubscription(getApiStores().sendToAnchor(getRequestBody(map)), observer);
//        } else if (roomType == RoomType.PAGE_CHAT_PRIVATE) {
//            addSubscription(getApiStores().sendToAssistant(getRequestBody(map)), observer);
//        } else {
//            addSubscription(getApiStores().sendMessage(getRequestBody(map)), observer);
//        }
    }


    public void sendPhoto(@RoomType int roomType, File pic) {
//        String vid = mvpView().getRoomVid();
//        String sender = AppConfig.getUserId();
//        String seed = EncryptUtils.encryptMD5ToString(sender + vid + System.currentTimeMillis());
//        sendPhoto(roomType, seed, pic, null);
    }


    private void sendPhoto(int roomType, String seed, File pic, @Nullable Runnable onFinish) {
//        String vid = mvpView().getRoomVid();
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("pic", seed + ".jpg", RequestBody.create(pic, MediaType.parse("image/*")));
//        Map<String, RequestBody> bodyMap = new HashMap<>();
//        Map<String, Object> map = sendRequestMap(roomType, seed, 2);
//        String timestamp = "" + ApiClient.getTimestamp();
//        String key = Helper.productSign(timestamp, sendMessageApiPathList(roomType));
//        String sign = RequestUtils.getApiFrontSign(map, key);
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            Object value = entry.getValue();
//            if (value != null)
//                bodyMap.put(entry.getKey(), RequestBody.create(value.toString(), MediaType.parse("text/plain")));
//        }
//        BaseObserver<JsonElement> observer = new BaseObserver<>() {
//            @Override
//            public void onSuccess(JsonElement data, String msg) {
//                saveVid(roomType, data);
//                mvpView().onSendPhoto(true, msg);
//                Uri uri = file2Uri(pic);
//                if (uri != null) {
//                    mvpView().onProcessReceiveMessage(createMessageImagePending(roomType, 2, AppConfig.getUserId(), vid, seed, uri.toString(), DeliverStatus.STATUS_COMPLETE));
//                }
//                onProcessReceiveAnchorMessage(roomType, data);
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                mvpView().onSendPhoto(false, msg);
//                Uri uri = file2Uri(pic);
//                if (uri != null) {
//                    mvpView().onProcessReceiveMessage(createMessageImagePending(roomType, 2, AppConfig.getUserId(), vid, seed, uri.toString(), DeliverStatus.STATUS_FAILED));
//                }
//            }
//
//            @Override
//            public void onError(Pair<Integer, String> error) {
//                mvpView().onSendPhoto(false, error.second);
//                Uri uri = file2Uri(pic);
//                if (uri != null) {
//                    mvpView().onProcessReceiveMessage(createMessageImagePending(roomType, 2, AppConfig.getUserId(), vid, seed, uri.toString(), DeliverStatus.STATUS_FAILED));
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                if (onFinish != null) onFinish.run();
//            }
//        };
//        if (roomType == RoomType.PAGE_CHAT_PRIVATE_ANCHOR) {
//            addSubscription(getApiStores().sendToAnchor(timestamp, sign, filePart, bodyMap), observer);
//        } else if (roomType == RoomType.PAGE_CHAT_PRIVATE) {
//            addSubscription(getApiStores().sendToAssistant(timestamp, sign, filePart, bodyMap), observer);
//        } else {
//            addSubscription(getApiStores().sendMessage(timestamp, sign, filePart, bodyMap), observer);
//        }
    }

    private void saveVid(int roomType, JsonElement data) {
//        if (roomType != RoomType.PAGE_CHAT_PRIVATE_ANCHOR) return;
//        if (data instanceof JsonObject) {
//            String vid = JsonUtil.getString((JsonObject) data, "vid");
//            if (!TextUtils.isEmpty(vid) && !Objects.equals(mvpView().getRoomVid(), vid)) {
//                mvpView().setRoomVid(vid);
//                pin(roomType, vid);
//                InOutRoomHelper.inRoom(vid);
//                refreshChatHistory(roomType);
//            }
//        }
    }

    private void onProcessReceiveAnchorMessage(int roomType, JsonElement data) {
//        if (roomType != RoomType.PAGE_CHAT_PRIVATE_ANCHOR) return;
//        if (!(data instanceof JsonObject)) return;
//        JsonObject json = (JsonObject) data;
//        String message = JsonUtil.getString(json, "msg");
//        if (TextUtils.isEmpty(message)) return;
//        MessageRecord messageRecord = JsonUtil.fromJson(GsonFactory.getSingletonGson(), JsonUtil.getString(json, "msg"), MessageRecord.class);
//        if (messageRecord == null) return;
//        ConversationMessage conversationMessage = new ConversationMessage(messageRecord, mvpView().uid());
//        conversationMessage.setDeliveryStatus(DeliverStatus.STATUS_COMPLETE);
//        mvpView().onProcessReceiveMessage(conversationMessage);
    }


    /**
     * 发送中 text
     */
    @NonNull
    private ConversationMessage createMessageTextPending(@RoomType int roomType, int msgType, String sender, String vid, String seed, String text, @DeliverStatus int status) {
//        MessageRecord messageSend = new MessageRecord();
//        messageSend.setType(roomType);
//        messageSend.setVid(vid);
//        messageSend.setText(text);
//        messageSend.setSeed(seed);
//        messageSend.setSender(sender);
//        messageSend.setTime(TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
//        UserInfo userBean = AppConfig.getUserBean();
//        if (userBean != null) {
//            messageSend.setSenderNickname(userBean.getUserNickname());
//            messageSend.setAvatar(userBean.getAvatar());
//            messageSend.setSenderExp(userBean.getExp());
//            messageSend.setSenderType(2);
//        } else {
//            messageSend.setSenderType(0);
//        }
//        messageSend.setMsgType(msgType);
//        ConversationMessage conversationMessage = new ConversationMessage(messageSend, mvpView().uid());
//        conversationMessage.setDeliveryStatus(status);
//        if (DeliverStatus.STATUS_PENDING == status)
//            pendingMessageRecordList.add(messageSend);
//        return conversationMessage;
        return null;
    }

    @NonNull
    private ConversationMessage createMessageImagePending(int roomType, int msgType, String sender, String vid, String seed, String path, @DeliverStatus int status) {
//        MessageRecord messageSend = new MessageRecord();
//        messageSend.setType(roomType);
//        messageSend.setVid(vid);
//        messageSend.setSeed(seed);
//        messageSend.setSender(sender);
//        messageSend.setPic(path);
//        messageSend.setMsgType(msgType);
//        messageSend.setTime(TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
//        UserInfo userBean = AppConfig.getUserBean();
//        if (userBean != null) {
//            messageSend.setSenderNickname(userBean.getUserNickname());
//            messageSend.setAvatar(userBean.getAvatar());
//            messageSend.setSenderExp(userBean.getExp());
//            messageSend.setSenderType(2);
//        } else {
//            messageSend.setSenderType(0);
//        }
//        if (DeliverStatus.STATUS_PENDING == status)
//            pendingMessageRecordList.add(messageSend);
//        ConversationMessage conversationMessage = new ConversationMessage(messageSend, mvpView().uid());
//        conversationMessage.setDeliveryStatus(status);
//        return conversationMessage;
        return null;
    }

    /**
     * 发送pending消息
     */
    public void postPendingMessages(@Nullable Runnable runnable) {
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
                        () -> postPendingMessages(runnable));
                break;
            case 5:
                sendEmojiGif(messageRecord.getType(),
                        messageRecord.getSeed(),
                        messageRecord.getPic(),
                        () -> postPendingMessages(runnable)
                );
                break;
            case 2:
                File file = UriUtils.uri2File(Uri.parse(messageRecord.getPic()));
                if (FileUtils.isFileExists(file)) {
                    sendPhoto(messageRecord.getType(),
                            messageRecord.getSeed(),
                            file,
                            () -> postPendingMessages(runnable));
                }
                break;
        }
    }

    public void sendMessage(MessageRecord messageRecord) {
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
                        messageRecord.getSeed(),
                        messageRecord.getPic(),
                        null
                );
                break;
            case 2:
                File file = UriUtils.uri2File(Uri.parse(messageRecord.getPic()));
                if (FileUtils.isFileExists(file)) {
                    sendPhoto(messageRecord.getType(),
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

        map.put("seed", seed);
        map.put("sender", LiveConfig.getUserId());
        map.put("text", "");
        map.put("msgType", msgType);
        map.put("vid", vid);
        map.put("channel_code", channelCode);
        if (roomType == RoomType.PAGE_CHAT_PRIVATE_ANCHOR)
            map.put("anchorId", "" + uid);

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


}
