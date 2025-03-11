package com.xtree.live.ui.main.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.live.BR;
import com.xtree.live.LiveConfig;
import com.xtree.live.R;
import com.xtree.live.chat.Counter;
import com.xtree.live.chat.InOutRoomHelper;
import com.xtree.live.data.AdsBean;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentChatBinding;
import com.xtree.live.databinding.MergeChatRoomToolbarBinding;
import com.xtree.live.inter.ChatView;
import com.xtree.live.inter.EnterRoomBridge;
import com.xtree.live.inter.GiftViewMarginBottomListener;
import com.xtree.live.inter.IPanelView;
import com.xtree.live.inter.KickUserInterface;
import com.xtree.live.inter.MessageConstant;
import com.xtree.live.inter.OnEmojiGifClickedObserver;
import com.xtree.live.inter.OnPanelChangeListener;
import com.xtree.live.inter.PVid;
import com.xtree.live.message.ChatBarMode;
import com.xtree.live.message.ChatPageInfo;
import com.xtree.live.message.ConversationMessage;
import com.xtree.live.message.ConversationScrollButtonState;
import com.xtree.live.message.DeliverStatus;
import com.xtree.live.message.EmojiModel;
import com.xtree.live.message.ExpandLiveInfo;
import com.xtree.live.message.GroupRoom;
import com.xtree.live.message.MessageActionType;
import com.xtree.live.message.MessageBan;
import com.xtree.live.message.MessageClearHistory;
import com.xtree.live.message.MessageDelete;
import com.xtree.live.message.MessageItemType;
import com.xtree.live.message.MessageKickUser;
import com.xtree.live.message.MessageMsg;
import com.xtree.live.message.MessageOnline;
import com.xtree.live.message.MessageOpen;
import com.xtree.live.message.MessagePin;
import com.xtree.live.message.MessageRead;
import com.xtree.live.message.MessageRecord;
import com.xtree.live.message.MessageVid;
import com.xtree.live.message.RoomType;
import com.xtree.live.message.SimpleMessageListener;
import com.xtree.live.message.SystemMessageRecord;
import com.xtree.live.message.inroom.ChatRoom;
import com.xtree.live.message.inroom.InRoomData;
import com.xtree.live.message.inroom.PrivateRoom;
import com.xtree.live.socket.ChatWebSocketManager;
import com.xtree.live.ui.main.adapter.ChatAdapter;
import com.xtree.live.ui.main.viewmodel.LiveDetailHomeViewModel;
import com.xtree.live.uitl.ActionGetter;
import com.xtree.live.uitl.CommonUtil;
import com.xtree.live.uitl.MessageUtils;
import com.xtree.live.uitl.PanelSwitchHelper;
import com.xtree.live.uitl.WordUtil;
import com.xtree.live.widge.ChatLoadMoreView;
import com.xtree.live.widge.PanelView;
import com.xtree.live.widge.SmoothScrollingLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.OnEmojiconBackspaceListener;
import io.github.rockerhieu.emojicon.OnEmojiconClickedListener;
import io.github.rockerhieu.emojicon.emoji.Emojicon;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.schedulers.Schedulers;
import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

@Route(path = RouterFragmentPath.Live.PAGER_LIVE_CHAT)
public class ChatFragment extends BaseFragment<FragmentChatBinding, LiveDetailHomeViewModel> implements ChatView, OnEmojiconClickedListener,
        OnEmojiconBackspaceListener, OnEmojiGifClickedObserver, ExpandLiveInfo {


    int roomType;
    int mUid;
    String mVid;
    int chatBarMode;
    ChatRoom roomInfo;
    String pm_source_type;
    String pm_source_type_str;

    public static ChatFragment newInstance(@RoomType int roomType, int uid, String vid, @ChatBarMode int chatBarMode, @NonNull ChatRoom roomInfo, String pmSourceType, String pmSourceTypeStr) {
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("room_type", roomType);
        bundle.putInt("uid", uid);
        bundle.putInt("chatbar_mode", chatBarMode);
        bundle.putParcelable("room_info", roomInfo);
        bundle.putString("vid", vid);
        bundle.putString("pm_source_type", pmSourceType);
        bundle.putString("pm_source_type_str", pmSourceTypeStr);
        fragment.setArguments(bundle);
        return fragment;
    }

    private boolean isVisibleUser;
    private PanelSwitchHelper mPanelSwitchHelper;
    private ChatAdapter mAdapter;
    private SmoothScrollingLinearLayoutManager mLayoutManager;
    private final ChatPageInfo mPageInfo = new ChatPageInfo();
    private boolean mCanDisplayBanner = true;
    private int mPanelHeight;

    private final Handler mHandler = new Handler();

    private MergeChatRoomToolbarBinding mChatbarBinding;
    /**
     * 输入面板是否展开
     */
    private boolean mIsInputPanelExpand;

    //>0 上滑动 <0下滑
    private int mScrollDy = 0;
    private final int SCROLLED_PAST_BUTTON_THRESHOLD = 4;
    private int mScrolledPastButtonThreshold = SCROLLED_PAST_BUTTON_THRESHOLD;
    private static final int SCROLL_ANIMATION_THRESHOLD = 50;

    public boolean isGlobal() {
        return RoomType.PAGE_CHAT_GLOBAL == roomType;
    }

    public boolean isGroup() {
        return RoomType.PAGE_CHAT_GROUP == roomType;
    }

    public boolean isAssistance() {
        return RoomType.PAGE_CHAT_PRIVATE == roomType;
    }

    public boolean isAnchor() {
        return RoomType.PAGE_CHAT_PRIVATE_ANCHOR == roomType;
    }

    public boolean isPrivate() {
        return RoomType.PAGE_CHAT_PRIVATE == Math.abs(roomType);
    }

    private final Counter mCounter = new Counter("main_counter", 1000);
    private final SimpleMessageListener messageListener = new SimpleMessageListener() {

        @Override
        public void onReceiveMessageOnline(MessageOnline message) {
            super.onReceiveMessageOnline(message);
            if (RoomType.PAGE_CHAT_PRIVATE == message.getRoomType() && isAssistance() && message.getUserid() > 0) {
                ChatRoom room = roomInfo;
                if (room instanceof PrivateRoom && ((PrivateRoom) room).assistant == message.getUserid()) {
                    //在线离线
                    if (message.getText() == 1) {
                        mChatbarBinding.ivOnline.setImageResource(R.drawable.bg_chat_online);
                        mChatbarBinding.tvOnline.setText(R.string.chat_online);
                        mChatbarBinding.tvOnline.setTextColor(ContextCompat.getColor(Utils.getContext(), R.color.onLineColor));
                    } else {
                        mChatbarBinding.ivOnline.setImageResource(R.drawable.bg_chat_offline);
                        mChatbarBinding.tvOnline.setText(R.string.chat_offline);
                        mChatbarBinding.tvOnline.setTextColor(ContextCompat.getColor(Utils.getContext(), R.color.offLineColor));
                    }
                }
            }
        }

        @Override
        public void onReceiveMessageOpen(MessageOpen message) {
            //发送pending消息
            viewModel.postPendingMessages(() -> {
                        viewModel.enterRoom(roomType, mUid, mVid, wholeChatList());
//                if (isVisible() && isResumed()) {
//                    viewModel.enterRoom(roomType, mUid, mVid, wholeChatList());
//                }
                    }
                    , mUid, mVid);
        }

        @Override
        public void onReceiveMessageSend(MessageMsg message) {
            super.onReceiveMessageSend(message);
            mCounter.count(message);
            onReceiveMessageMsg(message);
        }

        private final List<MessageRecord> mBufferMessages = new LinkedList<>();

        //消息拉去间隔时常
        private static final int PULL_INTERVAL_MS = 1000;
        //列表中最大数量
        private static final int MAX_COUNT_IN_LIST = 5000;
        //列表中最大数量
        private static final int LEFT_COUNT_IN_LIST = 500;

        @Override
        public void onReceiveMessageClearHistory(MessageClearHistory message) {
            if (roomType == message.getRoomType() && getRoomVid().equals(message.getVid())) {
//                viewModel.processChatHistoryData(false, true, new ArrayList<>());
                onProcessChatHistories(false, true, new ArrayList<>());

            }
        }

        @Override
        public void onReceiveMessageMsg(MessageMsg message) {
            //房间不一致  且不是全局广告
            if (!isSameConversationRoom(message) && !isGlobalAdMessage(message)) return;
            //处理助理已离开消息
            if (processAgentHadLeftNotice(message)) return;

            int removeCount = MAX_COUNT_IN_LIST - LEFT_COUNT_IN_LIST;
            if (mAdapter.getData().size() > MAX_COUNT_IN_LIST) {
                List<ConversationMessage> list = mAdapter.getData();
                int originSize = list.size();
                for (int i = 0; i < removeCount; i++) {
                    list.remove(list.size() - 1);
                }
                mAdapter.notifyItemRangeRemoved(originSize - removeCount, removeCount);
            }

            if (mBufferMessages.isEmpty()) {
                mHandler.postDelayed(() -> {
//
                    Single<List<ConversationMessage>> single = filterMessageList(false, new ArrayList<>(mBufferMessages));
                    single.as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) ChatFragment.this)))
                            .subscribe(list -> onProcessReceiveMessageList(list));

                    mBufferMessages.clear();
                }, PULL_INTERVAL_MS);
            }
            mBufferMessages.add(0, createMessageRecord(message));
        }

        @NonNull
        private MessageRecord createMessageRecord(MessageMsg message) {
            MessageRecord record = new MessageRecord();
            record.setDesignation(message.getDesignation());
            record.setDesignationColor(message.getDesignationColor());
            record.setAvatar(message.getAvatar());
            record.setSender(message.getSender());
            record.setSenderNickname(message.getSenderNickname());
            record.setSenderExp(message.getSenderExp());
            record.setSenderType(message.getSenderUserType());
            record.setLink(message.getLink());
            record.setPic(message.getPic());
            record.setPicBnc(message.getPicBnc());
            record.setTitle(message.getTitle());
            record.setText(message.getText());

            record.setVid(message.getVid());
            record.setSeed(message.getSeed());
            record.setSeedsy(message.getSeedsy());
            record.setType(message.getRoomType());
            record.setMsgType(message.getMsgType());
            record.setMsgId(message.getMsgId());


            record.setTime(message.getTime());

            record.setAction(message.getAction());
            return record;
        }

        @Override
        public void onReceiveMessageRead(MessageRead message) {
            super.onReceiveMessageRead(message);
            if (isPrivate() && isSameConversationRoom(message)) {
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    ConversationMessage conversation = mAdapter.getData().get(i);
                    conversation.getMessageRecord().setReadCount(1);
                }
            }
        }

        @Override
        public void onReceiveMessageDelete(MessageDelete message) {
            doDelMsg(message);
        }

        @Override
        public void onReceiveMessagePin(MessagePin message) {
            if (isGroup()) {
                pinData(message);
            }
        }

        @Override
        public void onReceiveMessageBan(MessageBan message) {
            doBanUser(message);
        }

        @Override
        public void onReceiveMessageKickUser(MessageKickUser message) {
            if (Objects.equals(getRoomVid(), message.getVid()) && Objects.equals(LiveConfig.getUserId(), message.getUserid())) {
                KickUserInterface kickUser = ActionGetter.getKickUserAction(ChatFragment.this);
                if (kickUser == null || !kickUser.kickOut()) {
                    ToastUtils.showShort(R.string.kick_out_user_tips);
                }
            }
        }
    };

    private boolean isMute() {
        ChatRoom room = roomInfo;
        if (room instanceof GroupRoom) {
            return ((GroupRoom) room).roomMute == 1;
        }
        return false;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_chat;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public LiveDetailHomeViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(LiveDetailHomeViewModel.class);
    }

    @Override
    public void initView() {
        roomType = getSavedArguments().getInt("room_type", RoomType.PAGE_CHAT_UNKNOW);
        roomInfo = getSavedArguments().getParcelable("room_info");
        chatBarMode = getSavedArguments().getInt("chatbar_mode", ChatBarMode.CHATBAR_MODE_NONE);
        mVid = getSavedArguments().getString("vid", "");
        mUid = getSavedArguments().getInt("uid");
        pm_source_type = getSavedArguments().getString("pm_source_type", "0");
        pm_source_type_str = getSavedArguments().getString("pm_source_type_str", "0");

        mChatbarBinding = MergeChatRoomToolbarBinding.bind(binding.getRoot());
        binding.addBtn.setOnClickListener(view -> {
            if (isAnchor() || (!TextUtils.isEmpty(mVid) && !isGlobal())) {
                ToastUtils.showShort("暂未开通上传图片功能");
//                photoPicker.showPicker(requireActivity());
            }
        });

        viewModel.setParamas(mUid, mVid, pm_source_type, pm_source_type_str);

        ViewGroup.LayoutParams layoutParams = binding.chatbar.getLayoutParams();
        int visible = View.GONE;
        if (chatBarMode == ChatBarMode.CHATBAR_MODE_HIGH) {
            TypedValue typedValue = new TypedValue();
            requireActivity().getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true);
            layoutParams.height = requireActivity().getResources().getDimensionPixelSize(typedValue.resourceId);
            visible = View.VISIBLE;
        } else if (chatBarMode == ChatBarMode.CHATBAR_MODE_LOW) {
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            visible = View.VISIBLE;
        } else if (chatBarMode == ChatBarMode.CHATBAR_MODE_NONE) {
            binding.chatbar.setVisibility(View.VISIBLE);
        }
        binding.chatbar.setVisibility(visible);
        binding.chatbar.setLayoutParams(layoutParams);
        setCharBar();

        mAdapter = new ChatAdapter(new ArrayList<>(), Glide.with(Utils.getContext()));
        mAdapter.getUpFetchModule().setUpFetchEnable(true);
        mAdapter.getLoadMoreModule().setLoadMoreView(new ChatLoadMoreView());
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            viewModel.loadMoreChatHistory(lastMsgId(), roomType, uid(), mVid);
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.message_retry_send) {
                Object obj = adapter.getItem(position);
                if (obj instanceof ConversationMessage) {
                    ConversationMessage message = (ConversationMessage) obj;
                    message.setDeliveryStatus(DeliverStatus.STATUS_PENDING);
                    mAdapter.notifyItemChanged(position);
                    viewModel.sendMessage(message.getMessageRecord(), mUid, mVid);
                }
            }
        });
        mLayoutManager = new SmoothScrollingLinearLayoutManager(getContext(), true);
        binding.chatList.setLayoutManager(mLayoutManager);
        binding.chatList.setAdapter(mAdapter);
        binding.chatList.setItemAnimator(null);
        binding.editText.setUseSystemDefault(true);
        binding.adsPage.setVisibility(View.GONE);
        showPinView();

        binding.send.setOnClickListener(view -> {
            if (isGlobal() && !LiveConfig.isLogin()) {
                ToastUtils.showShort(getString(R.string.chat_send_message_no_login));
                return;
            }
            if (TextUtils.isEmpty(binding.editText.getText()) || CommonUtil.isContentBlank(binding.editText.getText())) {
                ToastUtils.showShort(getString(R.string.chat_send_message_hint));
                return;
            }

            String vid = getRoomVid();
            if (!isAnchor() && TextUtils.isEmpty(vid)) {
                ToastUtils.showShort(getString(R.string.input_params_error));
                return;
            }

            String text = binding.editText.getText().toString();
            if (text.length() > 500) {
                ToastUtils.showShort(getString(R.string.chat_send_message_size_too_big));
                return;
            }
            //msg_type 0 飘弹幕 1 普通消息  广场用户发消息需要飘弹幕出来// roomType() == PAGE_CHAT_GLOBAL ? 0 : 1, roomType(), ,
            sendText(text);
            binding.editText.setText("");
        });


        if (binding.chatList.getItemDecorationCount() == 0) {
            binding.chatList.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);

                    outRect.top = ConvertUtils.dp2px(8);
                    int position = parent.getChildAdapterPosition(view);
                    if (mAdapter.getItemCount() == position + 1) {
                        outRect.top = binding.clPin.getPinHeight();
                    }
                }
            });
        }
        binding.chatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    binding.tvDate.show();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.tvDate.hide();
                    mScrollDy = 0;
                    readMessageIDLE();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollDy = dy;
                if (mAdapter.getData().isEmpty()) {
                    return;
                }
                if (isFullScreen(mLayoutManager)) {
                    setLastVisibleDate();
                }
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if (isScrolledToBottom()) {
                    mScrolledPastButtonThreshold = SCROLLED_PAST_BUTTON_THRESHOLD;
                    viewModel.setShowScrollButtonsForScrollPosition(false, true, 0);

                } else if (isScrolledPastButtonThreshold()) {

                    int unreadCount = viewModel.unreadCount() + Math.min(firstVisibleItemPosition - viewModel.unreadCount(), 0);
                    viewModel.setShowScrollButtonsForScrollPosition(true, false, unreadCount);

                } else {

                    int unreadCount = viewModel.unreadCount() + Math.min(firstVisibleItemPosition - viewModel.unreadCount(), 0);
                    boolean shouldScrollToBottom = shouldScrollToBottom();
                    viewModel.setShowScrollButtonsForScrollPosition(false, shouldScrollToBottom, unreadCount);

                }
            }
        });

//        viewModel.getComposite().add(viewModel.scrollButtonState.subscribe(this::presentScrollButtons, any -> {
//        }, () -> {
//        }));

        binding.fabDown.setOnClickListener(view -> {
            binding.chatList.stopScroll();
            scrollToPosition(0);
        });
        muteRoom(isMute());

        //设计稿以及主流都不容许发图片，这里也不容许。防止图片广告
//        if (isGlobal()) {
//            binding.addBtn.setVisibility(View.GONE);
//        } else {
//            binding.addBtn.setVisibility(View.VISIBLE);
//        }

        if (isGlobal() && mCanDisplayBanner) {
            mCanDisplayBanner = false;
            viewModel.getAdList();
        }
        initEmojiPanel();

        loopInvokeInRoomLog();
        ChatWebSocketManager.getInstance().registerMessageListener(messageListener);

        getLifecycle().addObserver(binding.clPin);
        binding.llSendText.post(() -> {
            involveSvgaViewMargin(binding.llSendText.getHeight());
        });

        viewModel.enterRoom(roomType, mUid, mVid, wholeChatList());

    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        // 获取历史消息
        viewModel.listMessageRecord.observe(this, beans -> {
            if (beans != null && !beans.isEmpty()) {
                MessageRecord record = beans.get(0);
                if (!Objects.equals(record.getVid(), getRoomVid())) {
                    setRoomVid(record.getVid());
                    onGetChatHistory(true, beans);
                    return;
                }
            }
            if (beans == null || beans.isEmpty()) {
                onProcessChatHistories(true, false, new ArrayList<>());
                return;
            } else {
                Single<List<ConversationMessage>> single = filterMessageList(false, beans);
                single.as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) this)))
                        .subscribe(this::onProcessReceiveMessageList);
            }

        });

        //自动获取消息
        viewModel.pairMessageRecord.observe(this, booleanListPair -> {
            boolean isRefresh = booleanListPair.first;
            List<MessageRecord> beans = booleanListPair.second;
            if (isRefresh) {
                mPageInfo.reset();
            }
            if (beans != null && !beans.isEmpty()) {
                mPageInfo.nextPage(beans.get(beans.size() - 1).getMsgId());
            }

            if (beans == null || beans.isEmpty()) {
                onProcessChatHistories(false, isRefresh, new ArrayList<>());
                return;
            } else {
                Single<List<ConversationMessage>> single = filterMessageList(isRefresh, beans);
                single.as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) this)))
                        .subscribe(this::onProcessReceiveMessageList);
            }

            mAdapter.getLoadMoreModule().loadMoreComplete();
            hideLoading();
        });

        viewModel.liveRoomData.observe(this, liveRoomBean -> {
            if (isPrivate()) {
                ChatWebSocketManager.getInstance().setInRoom(mVid, roomType);
                PVid pVid = ActionGetter.getPVid(this);
                if (pVid != null) {
                    pVid.setPVid(liveRoomBean.getVid());
                }
            }
        });

        viewModel.listSystemMessageRecord.observe(this, list -> {
            if (list == null || list.isEmpty()) return;
            mHandler.removeCallbacks(runnable);
            welcomeMessages.clear();
            welcomeMessages.addAll(list);
            index = 0;
            postWelcomeMessage();
        });

        viewModel.inRoomDataMutableLiveData.observe(this, bean -> {
            if (isGlobal()) {
                if (mCanDisplayBanner) {
                    mCanDisplayBanner = false;
                    viewModel.getAdList();
                }
                setPinData(bean);
            } else if (isGroup()) {
                setPinData(bean);
            }
        });

        viewModel.listAdBeanMutable.observe(this, beans -> {

            if (beans == null || beans.isEmpty()) return;
            Observable<AdsBean> observable = Observable.fromIterable(beans)
                    .subscribeOn(Schedulers.newThread())
                    .filter(bean -> !TextUtils.isEmpty(bean.getImgAddress())
                            && !TextUtils.isEmpty(bean.getAdId())
                            && bean.getAdId().equals("006"))
                    .take(1);

            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) this)))
                    .subscribe(this::onProcessAdsData, Throwable::printStackTrace);

        });

        viewModel.conversationMessageMutableLiveData.observe(this, this::onProcessReceiveMessage);

    }

    private void initInputPanel() {
        if (mPanelSwitchHelper == null) {
            mPanelSwitchHelper = new PanelSwitchHelper.Builder(this)
                    .addEditTextFocusChangeListener((view, hasFocus) -> {

//                        if (hasFocus) {
//                            scrollToBottom();
//                        }
                    })
                    //可选
                    .addViewClickListener(view -> {
//                        if(view == null)return;
//                        if(view.getId() == R.id.edit_text || view.getId() == R.id.emotion_btn){
//                            scrollToBottom();
//                        }

                    })
                    .addKeyboardStateListener((visible, height) -> {
                        if(visible){
                            involveSvgaViewMargin(binding.llSendText.getHeight() + height);
                        }
                    })
                    //可选
                    .addPanelChangeListener(new OnPanelChangeListener() {

                        @Override
                        public void onKeyboard() {

                            binding.emotionBtn.setSelected(false);
                            mIsInputPanelExpand = true;
                            hideLiveInfo();
                        }

                        @Override
                        public void onNone() {

                            binding.emotionBtn.setSelected(false);
                            binding.emotionBtn.postDelayed(() -> {
                                mIsInputPanelExpand = false;
                            }, 500);
                            showLiveInfo();
                            involveSvgaViewMargin(binding.llSendText.getHeight());
                        }

                        @Override
                        public void onPanel(IPanelView view) {
                            if (view instanceof PanelView) {
                                binding.emotionBtn.setSelected(((PanelView) view).getId() == R.id.panel_emotion);
                                mIsInputPanelExpand = true;
                                hideLiveInfo();
                                involveSvgaViewMargin(mPanelHeight + binding.llSendText.getHeight());
                            }
                        }

                        @Override
                        public void onPanelSizeChange(IPanelView panelView, boolean portrait, int oldWidth, int oldHeight, int width, int height) {
                            if (panelView instanceof PanelView) {
                                if (((PanelView) panelView).getId() == R.id.panel_emotion) {
                                    View pagerView = binding.getRoot().findViewById(R.id.fl_emoji_icons);
                                    int viewPagerSize = (int) (height);
                                    ViewGroup.LayoutParams layoutParams = pagerView.getLayoutParams();
                                    layoutParams.width = width;
                                    layoutParams.height = viewPagerSize;
                                    pagerView.setLayoutParams(layoutParams);
                                }
                                WindowInsetsCompat insets = ViewCompat.getRootWindowInsets(requireActivity().getWindow().getDecorView());
                                int navigationBarH = insets == null ? 0 : insets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.navigationBars()).bottom;

                                mPanelHeight = navigationBarH + height;
                            }
                        }
                    })
                    .logTrack(false)             //output log
                    .build();
            binding.chatList.setPanelSwitchHelper(mPanelSwitchHelper);
        }
    }

    public void setCharBar() {
        mChatbarBinding.ivChatBack.setOnClickListener(view -> {
            onBackPressed();
        });
        if (chatBarMode != ChatBarMode.CHATBAR_MODE_NONE) {
            ChatRoom room = roomInfo;
            int isOnline = 0;
            int visible = View.GONE;
            String userName = room.name;

            if (room instanceof PrivateRoom) {
                PrivateRoom pRoom = (PrivateRoom) room;
                isOnline = pRoom.isOnline;
                visible = View.VISIBLE;
            } else if (room instanceof GroupRoom) {
                GroupRoom gRoom = (GroupRoom) room;
                isOnline = gRoom.isOnline;
            }
            mChatbarBinding.llOnline.setVisibility(visible);
            if (isOnline == 1) {
                mChatbarBinding.ivOnline.setImageResource(R.drawable.bg_chat_online);
                mChatbarBinding.tvOnline.setText(R.string.chat_online);
                mChatbarBinding.tvOnline.setTextColor(ContextCompat.getColor(Utils.getContext(), R.color.onLineColor));
            } else {
                mChatbarBinding.ivOnline.setImageResource(R.drawable.bg_chat_offline);
                mChatbarBinding.tvOnline.setText(R.string.chat_offline);
                mChatbarBinding.tvOnline.setTextColor(ContextCompat.getColor(Utils.getContext(), R.color.offLineColor));
            }
            //只有在标题为空时才重置标题 避免getRoomByVid获取到空 虚拟房间时标题时空
            if (TextUtils.isEmpty(mChatbarBinding.tvUserName.getText())) {
                mChatbarBinding.tvUserName.setText(userName);
            }
        }
    }

    void showPinView() {
        if (isGlobal() || isGroup()) {
            binding.clPin.setVisibility(View.VISIBLE);
        } else {
            binding.clPin.setVisibility(View.GONE);
        }
    }

    private void readMessageIDLE() {
        if (isPrivate()) {
            int position = getListLayoutManager().findFirstVisibleItemPosition();
            if (position >= 0 && position < mAdapter.getData().size()) {
                readMessage(mAdapter.getData().get(position));
            }
        }
    }

    private boolean isFullScreen(LinearLayoutManager llm) {
        return (llm.findLastCompletelyVisibleItemPosition() + 1) != mAdapter.getItemCount() ||
                llm.findFirstCompletelyVisibleItemPosition() != 0;
    }

    private void setLastVisibleDate() {
        int position = mLayoutManager.findLastVisibleItemPosition();
        int start = mAdapter.getHeaderLayoutCount();
        int end = mAdapter.getItemCount() - 1;
        if (position >= 0 && position >= start && position < end) {
            ConversationMessage bean = mAdapter.getItem(position);
            binding.tvDate.setText(bean.getSendDate());
        }
    }

    private boolean isScrolledToBottom() {
        return !binding.chatList.canScrollVertically(1);
    }

    private boolean isScrolledPastButtonThreshold() {
        return mLayoutManager.findFirstVisibleItemPosition() > mScrolledPastButtonThreshold;
    }

    private boolean shouldScrollToBottom() {
        return isScrolledToBottom() || mLayoutManager.findFirstVisibleItemPosition() <= 0;
    }

    public void scrollToPosition(int position) {
        int needScroll = getListLayoutManager().findFirstVisibleItemPosition() - position;
        if (needScroll > 0 && needScroll < SCROLL_ANIMATION_THRESHOLD) {

            binding.chatList.smoothScrollToPosition(position);
        } else {

            binding.chatList.scrollToPosition(position);
        }
    }

    public void muteRoom(boolean isMute) {
        binding.addBtn.setEnabled(!isMute);
        binding.emotionBtn.setEnabled(!isMute);
        binding.editText.setEnabled(!isMute);
        binding.send.setEnabled(!isMute);
        if (isMute) {
            binding.editText.setHint(WordUtil.getString(Utils.getContext(), R.string.mute_room_hint));
            binding.editText.setGravity(Gravity.CENTER);
        } else {
            binding.editText.setHint("");
            binding.editText.setGravity(Gravity.START);
        }
    }

    private void initEmojiPanel() {
        ArrayList<String> list = new ArrayList<>();
        list.add(WordUtil.getString(R.string.emoji_text));
        list.add(WordUtil.getString(R.string.emoji_gif));
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_emoji_icons, EmojiPagerFragment.newInstance(), "emojiFragment")
                .commitAllowingStateLoss();
    }


    private final Runnable mLoopInvokeInRoomLogRunnable = this::loopInvokeInRoomLog;

    private void loopInvokeInRoomLog() {
        if (isGlobal()) {
            if (isVisibleUser) getLiveInroomLog();
            mHandler.removeCallbacks(mLoopInvokeInRoomLogRunnable);
            mHandler.postDelayed(mLoopInvokeInRoomLogRunnable, 60000);
        }
    }

    public void getLiveInroomLog() {
        viewModel.getLiveInroomLog(mVid);
    }

    private void involveSvgaViewMargin(int svgaViewMarginBottom) {

        GiftViewMarginBottomListener listener = ActionGetter.getGiftViewMarginListener(this);
        if (listener != null) {
            listener.onMarginBottom(svgaViewMarginBottom);
        }
    }

    public boolean isSameConversationRoom(MessageVid vid) {
        return getRoomVid() != null && Objects.equals(getRoomVid(), vid.getVid());
    }

    public boolean isGlobalAdMessage(MessageMsg msg) {
        return isGlobal() && 3 == msg.getMsgType();
    }


    /**
     * 处理助理已离开消息
     */
    private boolean processAgentHadLeftNotice(MessageMsg bean) {
        if (bean.getFd() == 0 && WordUtil.getString(R.string.when_agnent_left_sender_nickname).contentEquals(bean.getSenderNickname())) {
            //显示snackBar
            String text = bean.getText();
            if (TextUtils.isEmpty(text)) {
                text = WordUtil.getString(R.string.when_agnent_left_tips_message);
            }
            ToastUtils.showShort(text);
            return true;
        }
        return false;
    }

    private SmoothScrollingLinearLayoutManager getListLayoutManager() {
        return (SmoothScrollingLinearLayoutManager) binding.chatList.getLayoutManager();
    }

    private void readMessage(ConversationMessage conversation) {
        if (isVisibleUser && isPrivate() && !TextUtils.isEmpty(getRoomVid())) {
            MessageRecord record = conversation.getMessageRecord();
            if (!Objects.equals(LiveConfig.getUserId(), record.getSender()) && record.getReadCount() == 0) {
                record.setReadCount(1);
                viewModel.readMessage(record.getMsgId());
            }
        }
    }

    private void readMessage(List<ConversationMessage> recieveMessageList) {
        if (recieveMessageList == null || recieveMessageList.isEmpty() || !isPrivate()) return;
        ConversationMessage conversation = recieveMessageList.get(0);
        readMessage(conversation);
    }

    private void setPinData(InRoomData newBean) {
        binding.clPin.setPinData(newBean);
    }

    private void presentScrollButtons(ConversationScrollButtonState scrollButtonState) {

        binding.fabDown.setUnreadCount(scrollButtonState.getUnreadCount());
        binding.fabDown.setShown(scrollButtonState.getShowScrollButtons());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        if (isMute()) return;
        EmojiconsFragment.backspace(binding.editText);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        if (isMute()) return;
        EmojiconsFragment.input(binding.editText, emojicon);
    }

    @Override
    public void onEmojiconLongClicked(Emojicon emojicon) {
//长按emoji表情
    }

    @Override
    public void showLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity<?, ?>) getActivity()).showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity<?, ?>) getActivity()).hideLoading();
    }

    @Override
    public void showLoading(long delay) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity<?, ?>) getActivity()).showLoading(delay);
        }
    }

    @Override
    public void onGetChatHistory(boolean isRefresh, @Nullable List<MessageRecord> beans) {
        if (isRefresh) {
            mPageInfo.reset();
        }
        if (beans != null && !beans.isEmpty()) {
            mPageInfo.nextPage(beans.get(beans.size() - 1).getMsgId());
        }

        if (beans == null || beans.isEmpty()) {
            onProcessChatHistories(false, isRefresh, new ArrayList<>());
            return;
        } else {
            Single<List<ConversationMessage>> single = filterMessageList(isRefresh, beans);
            single.as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) this)))
                    .subscribe(this::onProcessReceiveMessageList);
        }

        mAdapter.getLoadMoreModule().loadMoreComplete();
    }


    @Override
    public void pinData(MessagePin bean) {
        if (!TextUtils.isEmpty(getRoomVid()) && Objects.equals(getRoomVid(), bean.getVid())) {
            binding.clPin.setPinData(bean.parseInRoomData());
        }
    }

    @Override
    public void doDelMsg(MessageDelete delete) {
        if (isSameConversationRoom(delete)) {
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                ConversationMessage record = mAdapter.getData().get(i);
                if (record.isSameMessageWithDelete(delete)) {
                    mAdapter.removeAt(i);
                    return;
                }
            }
        }
    }

    @Override
    public void doTobUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProcessChatHistories(boolean isUpdateFresh, boolean isRefresh, List<ConversationMessage> chatHistories) {
        if (isRefresh) {
            mAdapter.setList(chatHistories);
            scrollToPosition(0);
            readMessage(chatHistories);
        } else if (!chatHistories.isEmpty()) {
            if (isUpdateFresh) {
                mAdapter.addData(0, chatHistories);
                viewModel.addUnreadCount(chatHistories.size());
            } else {
                mAdapter.addData(chatHistories);
            }
        }
    }

    @Override
    public void onProcessReceiveMessageList(List<ConversationMessage> recieveMessageList) {

        binding.chatList.setItemAnimator(null);
        if (recieveMessageList.size() > SCROLLED_PAST_BUTTON_THRESHOLD) {
            mScrolledPastButtonThreshold = 1;
        }
        mAdapter.addData(0, recieveMessageList);
        if (isScrolledToBottom() || (binding.chatList.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING && mScrollDy > 0)) {
            mLayoutManager.smoothScrollToPosition(binding.chatList, 0, recieveMessageList.size());
            readMessage(recieveMessageList);
        } else {
            viewModel.addUnreadCount(recieveMessageList.size());
        }
    }

    private final DefaultItemAnimator mItemAnimator = new DefaultItemAnimator();

    @Override
    public void onProcessReceiveMessage(ConversationMessage message) {
        mHandler.post(() -> {
            List<ConversationMessage> beans = wholeChatList();
            int deliveryStatusChangedIndex = beans.indexOf(message);

            int headerCount = mAdapter.getHeaderLayoutCount();
            if (deliveryStatusChangedIndex > -1) {
                if (deliveryStatusChangedIndex == headerCount) {
                    ConversationMessage itemBean = beans.get(deliveryStatusChangedIndex);
                    //更新时间
                    if (message.isDelivered()) {
                        itemBean.setTime(message.getMessageRecord().getTime());
                        //删除消息使用
                        itemBean.getMessageRecord().setMsgId(message.getMessageRecord().getMsgId());
                    }
                    itemBean.setDeliveryStatus(message.getDeliveryStatus());
                    mAdapter.notifyItemChanged(headerCount);
                } else {
                    ConversationMessage itemBean = beans.remove(deliveryStatusChangedIndex - headerCount);
                    if (message.isDelivered()) {
                        itemBean.setTime(message.getMessageRecord().getTime());
                        itemBean.getMessageRecord().setMsgId(message.getMessageRecord().getMsgId());
                    }
                    itemBean.setDeliveryStatus(message.getDeliveryStatus());
                    binding.chatList.setItemAnimator(mItemAnimator);
                    mAdapter.getData().add(headerCount, itemBean);
                    mAdapter.notifyItemMoved(deliveryStatusChangedIndex, headerCount);
                    mAdapter.notifyItemChanged(headerCount);
                }
                binding.chatList.scrollToPosition(headerCount);
                return;
            } else {
                mAdapter.addData(headerCount, message);
            }
            //自己发送的消息则chatList滑动到最新位置
            int position = mLayoutManager.findFirstVisibleItemPosition();
            if (message.isSentByMe()) {
                if (position > SCROLL_ANIMATION_THRESHOLD) {
                    binding.chatList.scrollToPosition(headerCount);
                } else {
                    binding.chatList.smoothScrollToPosition(headerCount);
                }
            } else {
                if (position == headerCount) {
                    binding.chatList.scrollToPosition(headerCount);
                    readMessage(message);
                } else if (position > headerCount) {
                    viewModel.addUnreadCount(1);
                }
            }
        });
    }

    public void onProcessAdsData(AdsBean bean) {
        binding.image.setOnClickListener(view -> doTobUrl(bean.getAdvertisingUrl()));
        binding.ivCloseAds.setOnClickListener(view -> binding.groupAds.setVisibility(View.GONE));
        Glide.with(this)
                .asBitmap()
                .load(MessageUtils.replaceDefaultUrl(bean.getImgAddress()))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .listener(new RequestListener<Bitmap>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, @NonNull Target<Bitmap> target, boolean isFirstResource) {
                        binding.groupAds.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Bitmap resource, @NonNull Object model, Target<Bitmap> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        float imageWidth = resource.getWidth();
                        float imageHeight = resource.getHeight();

                        ViewGroup.LayoutParams layoutParams = binding.image.getLayoutParams();
                        float imageViewWidth = ScreenUtils.getAppScreenWidth() * 1.0f;
                        layoutParams.width = (int) imageViewWidth;
                        layoutParams.height = (int) (imageViewWidth / imageWidth * imageHeight);
                        binding.image.setLayoutParams(layoutParams);
                        binding.groupAds.setVisibility(View.VISIBLE);
                        Glide.with(ChatFragment.this)
                                .load(bean.getImgAddress())
                                .diskCacheStrategy(DiskCacheStrategy.DATA)
                                .override(layoutParams.width, layoutParams.height)
                                .into(binding.image);
                        return false;
                    }
                })
                .preload();
    }

    @Override
    public String lastMsgId() {
        return mPageInfo.lastMsgId;
    }

    @Override
    public String defaultLastMsgId() {
        return "0";
    }

    @Override
    public String getRoomVid() {
        return mVid;
    }

    @Override
    public int uid() {
        return mUid;
    }

    @Override
    public void setRoomVid(String roomVid) {
        if (isPrivate()) {
            ChatWebSocketManager.getInstance().setInRoom(mVid, roomType);
            PVid pVid = ActionGetter.getPVid(this);
            if (pVid != null) {
                pVid.setPVid(roomVid);
            }
        }
    }

    @NonNull
    @Override
    public List<ConversationMessage> wholeChatList() {
        return mAdapter.getData();
    }

    @Override
    public void doBanUser(MessageBan ban) {
        if (isSameConversationRoom(ban)) {
            ListIterator<ConversationMessage> iterator = mAdapter.getData().listIterator();
            while (iterator.hasNext()) {
                ConversationMessage record = iterator.next();
                int index = Math.max(iterator.nextIndex() - 1, 0);
                if (record.isSameSender(ban.getUseId())) {
                    iterator.remove();
                    mAdapter.notifyItemRemoved(index);
                }
            }
        }
    }

    @Override
    public void onEmojiGifClicked(@NonNull EmojiModel model) {
        if (isMute()) return;
        if (isGlobal() && !LiveConfig.isLogin()) {
            ToastUtils.showShort(getString(R.string.chat_send_message_no_login));
            return;
        }
        sendEmojiGif(model.getEmojiUrl());
    }

    @Override
    public void onEmojiGifLongClicked(@NonNull EmojiModel model) {
        if (isMute()) return;
    }

    @Override
    public void onQuickReplyClicked(String msg) {
        // 快捷短语回复
        viewModel.sendText(roomType, mUid, mVid, msg);
    }

    @Override
    public void sendText(String text) {
        viewModel.sendText(roomType, mUid, mVid, text);
    }

    @Override
    public void onSendText(boolean success, String msg) {
        if (!success) ToastUtils.showShort(msg);
    }

    @Override
    public void onSendEmojiGif(boolean success, String msg) {
        if (!success) ToastUtils.showShort(msg);
    }

    @Override
    public void sendPhoto(File pic) {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(getString(R.string.network_error));
            return;
        }
        viewModel.sendPhoto(roomType, mUid, mVid, pic);
    }

    @Override
    public void onSendPhoto(boolean success, String msg) {
        if (!success) ToastUtils.showShort(msg);
    }

    @Override
    public void sendDanmu(int giftId, int uid, int level, String text) {
        viewModel.sendDanmu(giftId, uid, level, text);
    }

    @Override
    public void onSendDanmu(boolean success, String msg) {
        if (!success) ToastUtils.showShort(msg);
    }

    //上次发送emoji时间
    private long mLastSendEmojiGifTime = 0;

    @Override
    public void sendEmojiGif(String picture) {
        if (mLastSendEmojiGifTime == 0 || SystemClock.elapsedRealtime() - mLastSendEmojiGifTime > 3000) {
            mLastSendEmojiGifTime = SystemClock.elapsedRealtime();
            viewModel.sendEmojiGif(roomType, mUid, mVid, picture);
        } else {
            ToastUtils.showShort(WordUtil.getString(R.string.send_message_too_quickly));
        }
    }

    @NonNull
    @Override
    public String pmSourceType() {
        return pm_source_type;
    }

    @NonNull
    @Override
    public String pmSourceTypeStr() {
        return pm_source_type_str;
    }

    @Override
    public void showLiveInfo() {
        showPinView();
        ExpandLiveInfo expand = ActionGetter.getExpandLiveInfo(this);
        if (expand != null) expand.showLiveInfo();
    }

    @Override
    public void hideLiveInfo() {
        binding.clPin.setVisibility(View.GONE);
        ExpandLiveInfo expand = ActionGetter.getExpandLiveInfo(this);
        if (expand != null) expand.hideLiveInfo();
    }

    private final List<SystemMessageRecord> welcomeMessages = new ArrayList<>();
    Runnable runnable = this::postWelcomeMessage;
    ConversationMessage pinSystemWelcome;
    int index = 0;

    private void postWelcomeMessage() {
        if (isVisibleUser) {
            if (welcomeMessages.isEmpty()) return;
            int newIndex = index % welcomeMessages.size();
            SystemMessageRecord record = welcomeMessages.get(newIndex);
            if (pinSystemWelcome == null) {
                pinSystemWelcome = new ConversationMessage(new MessageRecord(), uid());
                pinSystemWelcome.setWelcomeText("");
            }
            MessageRecord messageRecord = pinSystemWelcome.getMessageRecord();
            messageRecord.setText(MessageConstant.ENTER_ROOM);
            messageRecord.setUserId(record.getUserId());
            messageRecord.setSenderType(record.getSenderUserType());
            messageRecord.setSenderExp(record.getSenderExp());
            messageRecord.setSenderNickname(record.getSenderNickname());
            messageRecord.setDesignation(record.getDesignation());
            messageRecord.setDesignationColor(record.getDesignationColor());
            messageRecord.setTime(record.getCreateTime());
            messageRecord.setAction(MessageActionType.ACTION_SYSTEM);
            messageRecord.setMsgType(MessageItemType.MSG_GLOBAL_TEXT);

            pinSystemWelcome.setMessageRecord(messageRecord);

            int duration = 2000;
            binding.clPin.setSystemText(pinSystemWelcome, duration / 2);
            index = newIndex + 1;
            mHandler.postDelayed(runnable, duration);
        } else {

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isVisibleUser = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onPause() {
        super.onPause();
        ChatWebSocketManager.getInstance().setInRoom("", RoomType.PAGE_CHAT_UNKNOW);
        if(isGlobal()){
            InOutRoomHelper.leaveRoom(getRoomVid());
        }
        binding.panelSwitchLayout.recycle();
        mPanelSwitchHelper = null;
    }

    private Single<List<ConversationMessage>> filterMessageList(boolean isRefresh, List<MessageRecord> histories) {
        return Observable.fromIterable(histories)
                .map(bean -> {
                    ConversationMessage conversationMessage = new ConversationMessage(bean, mUid);
                    conversationMessage.setDeliveryStatus(DeliverStatus.STATUS_COMPLETE);
                    return conversationMessage;
                })
                .onErrorReturn(throwable -> null)
                .filter(conversationMessage -> conversationMessage != null)
                .toList()
                .map(beanList -> {
                    //过滤重复消息
                    ArrayList<ConversationMessage> newBeans = new ArrayList<>();
                    for (ConversationMessage bean : beanList) {
                        if (!newBeans.contains(bean)) {
                            if (!newBeans.isEmpty()) {
                                ConversationMessage last = newBeans.get(newBeans.size() - 1);
                                if (last.isSameSender(bean)) {
                                    last.setCommonSenderWithNext(true);
                                    bean.setCommonSenderWithLast(true);
                                }
                            }
                            newBeans.add(bean);
                        }
                    }
                    return newBeans;
                })
                .flattenAsFlowable((Function<List<ConversationMessage>, Iterable<ConversationMessage>>) conversationMessages -> conversationMessages)
                .filter((AppendOnlyLinkedArrayList.NonThrowingPredicate<ConversationMessage>) conversationMessage -> {
                    if (isRefresh) return true;
                    List<ConversationMessage> beanList = wholeChatList();
                    int size = Math.min(500, beanList.size());
                    for (int i = 0; i < size; i++) {
                        ConversationMessage bean = beanList.get(i);
                        if (bean.isSameMessage(conversationMessage)) {
                            if (TextUtils.isEmpty(bean.getMessageRecord().getMsgId())) {
                                bean.getMessageRecord().setMsgId(conversationMessage.getMessageRecord().getMsgId());
                            }
                            if (bean.isPending()) {
                                //更新状态变化
                                onProcessReceiveMessage(conversationMessage);
                            }
                            return false;
                        }
                    }
                    return true;
                })
                .toSortedList((o1, o2) -> (int) (o2.getDateTimeSent() - o1.getDateTimeSent()));
    }

    @Override
    public boolean onBackPressed() {
        if (mPanelSwitchHelper != null && mPanelSwitchHelper.hookSystemBackByPanelSwitcher()) {
            return false;
        }
        if (mIsInputPanelExpand) return false;
        EnterRoomBridge bridge = ActionGetter.getEnterRoomBridge(this);
        if (bridge == null) return true;
        bridge.intoChatList();
        return false;
    }

}
