package com.xtree.live.ui.main.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.google.android.material.color.MaterialColors;
import com.xtree.live.BR;
import com.xtree.live.LiveConfig;
import com.xtree.live.R;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentChatRoomListBinding;
import com.xtree.live.inter.EnterRoomBridge;
import com.xtree.live.inter.UnreadChanged;
import com.xtree.live.message.ChatRoomInfo;
import com.xtree.live.message.ChatRoomLastMsg;
import com.xtree.live.message.GroupRoom;
import com.xtree.live.message.MessageBan;
import com.xtree.live.message.MessageClearHistory;
import com.xtree.live.message.MessageDelete;
import com.xtree.live.message.MessageKickUser;
import com.xtree.live.message.MessageMsg;
import com.xtree.live.message.MessageOnline;
import com.xtree.live.message.RoomType;
import com.xtree.live.message.SimpleMessageListener;
import com.xtree.live.message.inroom.PrivateRoom;
import com.xtree.live.socket.ChatWebSocketManager;
import com.xtree.live.ui.main.adapter.ChatRoomListAdapter;
import com.xtree.live.ui.main.viewmodel.LiveDetailHomeViewModel;
import com.xtree.live.uitl.ActionGetter;
import com.xtree.live.uitl.SoftKeyBoardDetector;
import com.xtree.live.uitl.UnreadUtils;
import com.xtree.live.uitl.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseFragment;


public class ChatRoomListFragment extends BaseFragment<FragmentChatRoomListBinding, LiveDetailHomeViewModel> {

    public static ChatRoomListFragment newInstance() {
        return newInstance(true);
    }

    public static ChatRoomListFragment newInstance(boolean showSearch) {
        ChatRoomListFragment fragment = new ChatRoomListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("show_search", showSearch);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ChatRoomListAdapter mAdapter;
    private SoftKeyBoardDetector softKeyBoardDetector;
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        softKeyBoardDetector = new SoftKeyBoardDetector(requireActivity());
        return R.layout.fragment_chat_room_list;
    }

    @Override
    public int initVariableId() {
        return BR.m;
    }

    @Override
    public LiveDetailHomeViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(requireActivity().getApplication());
        return new ViewModelProvider(this, factory).get(LiveDetailHomeViewModel.class);
    }

    @Override
    public void initView() {

        mAdapter = new ChatRoomListAdapter();
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ChatRoomInfo bean = (ChatRoomInfo) adapter.getData().get(position);
            if (getParentFragment() instanceof EnterRoomBridge) {
                EnterRoomBridge bridge = ActionGetter.getEnterRoomBridge(this);
                if (bridge == null) return;
                if (bean.getRoomType() == RoomType.PAGE_CHAT_GROUP) {
                    GroupRoom chatRoom = new GroupRoom(bean.getRoomType(), bean.getVid(), bean.getName());
                    chatRoom.isOnline = bean.getIsOnline();
                    chatRoom.roomMute = bean.getRoomMute();
                    bridge.intoChatRoom(bean.getRoomType(), bean.getVid(), bean.getAnchorId(), chatRoom);
                } else if (bean.getRoomType() == RoomType.PAGE_CHAT_PRIVATE) {
                    PrivateRoom chatRoom = new PrivateRoom(bean.getRoomType(), bean.getVid(), bean.getName(), bean.getUserId());
                    chatRoom.isOnline = bean.getIsOnline();
                    bridge.intoChatRoom(bean.getRoomType(), bean.getVid(), bean.getUserId(), chatRoom);
                }

            }
        });
        binding.rvChat.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.rvChat.setAdapter(mAdapter);
        binding.swipeLayout.setColorSchemeColors(MaterialColors.getColor(binding.swipeLayout, R.attr.colorAccent));
        binding.swipeLayout.setOnRefreshListener(this::getChatRoomList);
        if (true) {
            binding.clAssistantSearch.setVisibility(View.VISIBLE);
            binding.clAssistantMessage.setVisibility(View.GONE);
            binding.llAssistantSearch.setOnClickListener(view -> {
                if (binding.btnAssistantSearch.getVisibility() == View.GONE) {
                    binding.etAssistantSearch.requestFocus();
                }
            });
            binding.etAssistantSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String nickname = binding.etAssistantSearch.getText().toString();
                    binding.etAssistantSearch.setText("");
                    binding.etAssistantSearch.clearFocus();
                    if (!TextUtils.isEmpty(nickname)) {
//                        mvpPresenter.searchAssistant(nickname);
                    }
                }
                return false;
            });
            binding.etAssistantSearch.setOnFocusChangeListener((view, focus) -> {
                if (focus) {
                    binding.btnAssistantSearch.setVisibility(View.VISIBLE);
                } else {
                    binding.btnAssistantSearch.setVisibility(View.GONE);
                    KeyboardUtils.hideSoftInput(getActivity());
                }
            });
            binding.btnAssistantSearch.setOnClickListener(view -> {
                String nickname = binding.etAssistantSearch.getText().toString().trim();
                binding.etAssistantSearch.setText("");
                binding.etAssistantSearch.clearFocus();
                if (!TextUtils.isEmpty(nickname)) {
//                    mvpPresenter.searchAssistant(nickname);
                }
            });
        } else {
            binding.clAssistantMessage.setVisibility(View.GONE);
            binding.clAssistantSearch.setVisibility(View.GONE);
        }
        ChatWebSocketManager.getInstance().registerMessageListener(messageListener);

        binding.swipeLayout.setRefreshing(true);
        getChatRoomList();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        // 键盘侦测
        softKeyBoardDetector.setOnSoftKeyBoardChangeListener(new SoftKeyBoardDetector.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {

            }

            @Override
            public void keyBoardHide(int height) {
                binding.etAssistantSearch.clearFocus();
            }
        });

        List<ChatRoomInfo> list =  mAdapter.getData();
        if(!list.isEmpty()){
            for (int i = 0; i < list.size(); i++) {
                ChatRoomInfo source = list.get(i);
                int unreadCount = UnreadUtils.getUnreadCount(source.getVid());
                if(unreadCount != source.getUnread()){
                    source.setUnread(unreadCount);
//                    mAdapter.notifyItemChanged(i,R.id.tv_badge);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 键盘侦测
        softKeyBoardDetector.setOnSoftKeyBoardChangeListener(null);
    }

    private final SimpleMessageListener messageListener = new SimpleMessageListener() {
        @Override
        public void onReceiveUnreadMessage(MessageMsg message) {
            //通知父容器更新未读消息数量
            unreadChanged();
        }

        @Override
        public void onReceiveMessageMsg(MessageMsg message) {
            if(message.getRoomType() == RoomType.PAGE_CHAT_GLOBAL)return;
            if(findAndChangeItem(message) >= 0)return;
            getChatRoomList();
        }

        @Override
        public void onReceiveMessageBan(MessageBan message) {
            doBanUser(message);
        }

        @Override
        public void onReceiveMessageClearHistory(MessageClearHistory message) {
            clearHistory(message);
        }

        @Override
        public void onReceiveMessageDelete(MessageDelete message) {
            deleteMsg(message);
        }

        @Override
        public void onReceiveMessageOnline(MessageOnline message) {
            online(message);
        }

        @Override
        public void onReceiveMessageKickUser(MessageKickUser message) {
            List<ChatRoomInfo> dataList = mAdapter.getData();
            for (int i = 0; i < dataList.size(); i++) {
                ChatRoomInfo chatRoom = dataList.get(i);
                if(Objects.equals(message.getVid(), chatRoom.getVid()) && Objects.equals(message.getUserid(), LiveConfig.getUserId())){
                    mAdapter.remove(chatRoom);
                    UnreadUtils.clearUnread(message.getVid());
                    unreadChanged();
                    break;
                }
            }
        }
    };

    private void online(MessageOnline online){
        List<ChatRoomInfo> dataList = mAdapter.getData();
        for (int i = 0; i < dataList.size(); i++) {
            ChatRoomInfo chatRoom = dataList.get(i);
            if(chatRoom.getRoomType() == RoomType.PAGE_CHAT_PRIVATE &&  chatRoom.getUserId() > 0  && chatRoom.getUserId() == online.getUserid()){
                chatRoom.setIsOnline(online.getText());
                mAdapter.notifyItemChanged(i,R.id.tv_online);
            }
        }
    }

    private void clearHistory(MessageClearHistory clear){
        List<ChatRoomInfo> dataList = mAdapter.getData();
        for (int i = 0; i < dataList.size(); i++) {
            ChatRoomInfo chatRoom = dataList.get(i);
            ChatRoomLastMsg lastMsg = chatRoom.getLastMsg();
            if (lastMsg != null &&
                    !TextUtils.isEmpty(clear.getVid()) && Objects.equals(chatRoom.getVid(), clear.getVid())) {
                lastMsg.setPic("");
                lastMsg.setText("");
                mAdapter.notifyItemChanged(i,R.id.tv_text);
                return;
            }
        }
    }
    private void deleteMsg(MessageDelete delete){
        List<ChatRoomInfo> dataList = mAdapter.getData();
        for (int i = 0; i < dataList.size(); i++) {
            ChatRoomInfo chatRoom = dataList.get(i);
            ChatRoomLastMsg lastMsg = chatRoom.getLastMsg();
            if (lastMsg != null &&
                    !TextUtils.isEmpty(delete.getVid()) && Objects.equals(chatRoom.getVid(), delete.getVid()) &&
                    !TextUtils.isEmpty(lastMsg.getSeed()) && Objects.equals(delete.getMsgSeed(), lastMsg.getSeed()) ) {
                lastMsg.setPic("");
                lastMsg.setText("");
                mAdapter.notifyItemChanged(i,R.id.tv_text);
                return;
            }
        }
    }
    private void doBanUser(MessageBan ban) {
        List<ChatRoomInfo> dataList = mAdapter.getData();
        for (int i = 0; i < dataList.size(); i++) {
            ChatRoomInfo chatRoom = dataList.get(i);
            ChatRoomLastMsg lastMsg = chatRoom.getLastMsg();
            if (lastMsg != null &&
                    !TextUtils.isEmpty(ban.getVid()) && Objects.equals(chatRoom.getVid(), ban.getVid()) &&
                    !TextUtils.isEmpty(lastMsg.getSender()) && Objects.equals(ban.getUseId(), lastMsg.getSender()) ) {
                lastMsg.setPic("");
                lastMsg.setText("");
                lastMsg.setCreationTime("");
                mAdapter.notifyItemChanged(i,R.id.tv_text);
                return;
            }
        }
    }

//    @Override
    public void onGetChatRoomList(List<ChatRoomInfo> beans) {
        binding.swipeLayout.setRefreshing(false);
        if (beans == null) return;
        mAdapter.setList(beans);
        if (beans.isEmpty()) {
            mAdapter.setEmptyView(getEmptyView());
        }
        unreadChanged();
    }

//    @Override
    public void onRefreshDone() {
        binding.swipeLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnPublishPinChatRoomEvent(ChatRoomInfo chatRoomInfo){
//        if(isVisibleUser()){
//            onPinChatRoom(chatRoomInfo);
//        }else {
//            for (int i = 0; i < mAdapter.getData().size(); i++) {
//                ChatRoomInfo tmp = mAdapter.getData().get(i);
//                if(Objects.equals(tmp.getVid(), chatRoomInfo.getVid())){
//                    tmp.setIsPin(chatRoomInfo.getIsPin());
//                    tmp.setPinTime(chatRoomInfo.getPinTime());
//                    onPinChatRoom(tmp);
//                    break;
//                }
//            }
//        }
    }

//    @Override
    public void onPinChatRoom(ChatRoomInfo chatRoomInfo) {
        int index = mAdapter.getData().indexOf(chatRoomInfo);
        if(index >= 0){
            mAdapter.notifyItemChanged(mAdapter.getHeaderLayoutCount() + index, R.id.pinChatRoom);
            if(chatRoomInfo.getIsPin() == 1){
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    ChatRoomInfo tmp = mAdapter.getData().get(i);
                    if(tmp != chatRoomInfo &&  tmp.getIsPin() == 1 && chatRoomInfo.getPinTime() > tmp.getPinTime()){
                        swapItemView(i, index, false);
                        return;
                    }
                }
                swapItemView(0, index, false);
            }else if(chatRoomInfo.getIsPin() == 0){
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    ChatRoomInfo tmp = mAdapter.getData().get(i);
                    if(tmp != chatRoomInfo && tmp.getIsPin() == 0 && chatRoomInfo.getUpdateTime() > tmp.getUpdateTime()){

                        swapItemView(i, index, false);
                        return;
                    }
                }
                swapItemView(mAdapter.getData().size(), index, false);
            }
        }
    }

    private void swapItemView(int to, int from, boolean anim) {
        int count = mAdapter.getHeaderLayoutCount();
        int fromIndex = count + from;
        if(from > to){
            mAdapter.getData().add(to, mAdapter.getData().remove(from));
            int toIndex = count + to;
            scrollToPosition(fromIndex, toIndex);
            if(anim)playSearchResultAnimation(count + to);
        }else if(from < to){
            mAdapter.getData().add(to - 1, mAdapter.getData().remove(from));
            int toIndex = count + to- 1;
            mAdapter.notifyItemMoved(fromIndex, toIndex);
            //做动画
            if(anim)playSearchResultAnimation(toIndex);
        }
    }

    private void scrollToPosition(int fromIndex, int toIndex) {
        int topIndex = getListLayoutManager().findFirstCompletelyVisibleItemPosition();
        int bottomIndex = getListLayoutManager().findLastCompletelyVisibleItemPosition();
        mAdapter.notifyItemMoved(fromIndex, toIndex);
        if(Math.min(topIndex, toIndex) == toIndex || Math.max(bottomIndex, toIndex) == toIndex){
            mAdapter.getRecyclerView().scrollToPosition(toIndex);
        }
    }

    private LinearLayoutManager getListLayoutManager() {
        return (LinearLayoutManager) binding.rvChat.getLayoutManager();
    }

    private int findAndChangeItem(MessageMsg message) {
        List<ChatRoomInfo> dataList = mAdapter.getData();
        int changedIndex = -1;
        int pinStartIndex = -1;
        int normalStartIndex = -1;
        boolean isPinChatRoom = false;
        for (int i = 0; i < dataList.size(); i++) {
            ChatRoomInfo bean = dataList.get(i);
            boolean isPinChatRoomTmp = bean.getIsPin() == 1;
            if (Objects.equals(bean.getVid(), message.getVid())) {
                isPinChatRoom = isPinChatRoomTmp;
                changedIndex = i;
                ChatRoomLastMsg last = new ChatRoomLastMsg();
                last.setText(message.getText());
                last.setPic(message.getPic());
                last.setCreationTime(message.getTime());
                bean.setLastMsg(last);
                bean.setUnread(Math.max(UnreadUtils.getUnreadCount(bean.getVid()), 0));
            }
            if(pinStartIndex == -1 && isPinChatRoomTmp)pinStartIndex = i;
            if(normalStartIndex == -1 && !isPinChatRoomTmp)normalStartIndex = i;
        }
        if (changedIndex >= 0) {
            int startIndex = mAdapter.getHeaderLayoutCount();
            mAdapter.notifyItemChanged(changedIndex + startIndex, R.id.tv_text);
            if(changedIndex > 0){
                if(!isPinChatRoom && normalStartIndex != changedIndex){
                    mAdapter.getData().add(normalStartIndex, mAdapter.getData().remove(changedIndex));
                    mAdapter.notifyItemMoved(changedIndex + startIndex, normalStartIndex + startIndex);
//                    mBinding.rvChat.scrollToPosition(normalStartIndex + startIndex);
                }
            }
        }
        return changedIndex;
    }


    private void getChatRoomList() {
//        mvpPresenter.getChatRoomList("1,2");
    }



    private void unreadChanged() {
        //通知父容器更新未读消息数量
        UnreadChanged unreadChanged = ActionGetter.getUnreadChanged(this);
        if (unreadChanged != null) unreadChanged.unreadChanged();
    }


//    @Override
//    public void onDelayHiddenSearchItem() {
//        Animation animateOut = AnimationUtils.loadAnimation(getContext(), R.anim.anim_fade_out);
//        animateOut.setDuration(250);
//        ViewUtil.animateOut(binding.clAssistantMessage, animateOut);
//    }
//    @Override
//    public void onGetAnchorAssistant(@Nullable ChatRoomInfo chatRoomInfo) {
//        if(chatRoomInfo != null){
//            int index = mAdapter.getData().indexOf(chatRoomInfo);
//            if(index >= 0){
//                ChatRoomInfo infoInList = mAdapter.getData().get(index);
//                if(infoInList.getIsPin() == 1){
//                    mAdapter.getRecyclerView().scrollToPosition(mAdapter.getHeaderLayoutCount() + index);
//                    playSearchResultAnimation(mAdapter.getHeaderLayoutCount() + index);
//                }else {
//                    for (int i = 0; i < mAdapter.getData().size(); i++) {
//                        ChatRoomInfo tmp = mAdapter.getData().get(i);
//                        if(tmp.getIsPin() == 0){//第一个非pin
//                            if(Objects.equals(tmp.getVid(), chatRoomInfo.getVid())){
//                                //做动画
//                                playSearchResultAnimation(i);
//                                break;
//                            }else {
//                                swapItemView(i, index, true);
//                                break;
//                            }
//                        }
//                    }
//                }
//            }else {
//                int unPinIndex = -1;
//                for (int i = 0; i < mAdapter.getData().size(); i++) {
//                    ChatRoomInfo tmp = mAdapter.getData().get(i);
//                    if(tmp.getIsPin() == 0){//第一个非pin
//                        unPinIndex = i;
//                        break;
//                    }
//                }
//                if(unPinIndex == -1){
//                    mAdapter.addData(chatRoomInfo);
//                    mAdapter.getRecyclerView().scrollToPosition(mAdapter.getData().size());
//                    //做动画
//                    playSearchResultAnimation(unPinIndex);
//                }else {
//                    mAdapter.addData(unPinIndex, chatRoomInfo);
//                    mAdapter.getRecyclerView().scrollToPosition(unPinIndex);
//                    //做动画
//                    playSearchResultAnimation(unPinIndex);
//                }
//            }
//        }
//        Animation animateIn = AnimationUtils.loadAnimation(getContext(), R.anim.anim_fade_enter);
//        animateIn.setDuration(250);
//        ViewUtil.animateIn(binding.clAssistantMessage, animateIn);
//        binding.tvAssistantMessageFail.setVisibility(chatRoomInfo != null ? View.GONE :View.VISIBLE);
//        binding.tvAssistantMessageSuccess.setVisibility(chatRoomInfo != null ? View.VISIBLE : View.GONE);
////        mvpPresenter.delayHiddenSearchItem();
//    }

    private void playSearchResultAnimation(int index){
//        mAdapter.notifyItemChanged(mAdapter.getHeaderLayoutCount() + index, 1);
    }


    public boolean onBackPressed() {
        if (!KeyboardUtils.isSoftInputVisible(requireActivity())) return true;
        KeyboardUtils.hideSoftInput(requireActivity());
        return false;
    }

    protected View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_empty, null, false);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        ChatWebSocketManager.getInstance().unregisterMessageListener(messageListener);
        super.onDestroyView();
    }
}
