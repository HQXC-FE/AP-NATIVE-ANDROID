package com.xtree.live.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.drake.brv.BR;
import com.xtree.live.R;
import com.xtree.live.databinding.FragmentChatRoomAssistBinding;
import com.xtree.live.inter.EnterRoomBridge;
import com.xtree.live.inter.KickUserInterface;
import com.xtree.live.message.ChatBarMode;
import com.xtree.live.message.ExpandLiveInfo;
import com.xtree.live.message.inroom.ChatRoom;
import com.xtree.live.ui.main.viewmodel.LiveDetailHomeViewModel;
import com.xtree.live.uitl.ActionGetter;
import com.xtree.live.uitl.DialogUtil;
import com.xtree.live.uitl.WordUtil;

import me.xtree.mvvmhabit.base.BaseFragment;

//主播助理
public class ChatRoomContainerFragment extends BaseFragment<FragmentChatRoomAssistBinding, LiveDetailHomeViewModel> implements EnterRoomBridge, ExpandLiveInfo, KickUserInterface {

    public static ChatRoomContainerFragment newInstance(@ChatBarMode int chatBarMode, String pmSourceType, String pmSourceTypeStr) {
        ChatRoomContainerFragment fragment = new ChatRoomContainerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("chatbar_mode", chatBarMode);
        bundle.putString("pm_source_type", pmSourceType);
        bundle.putString("pm_source_type_str", pmSourceTypeStr);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_chat_room_assist;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    private int chatbarMode() {
        return getSavedArguments().getInt("chatbar_mode", ChatBarMode.CHATBAR_MODE_NONE);
    }

    private String pmSourceType() {
        return getSavedArguments().getString("pm_source_type");
    }

    private String pmSourceTypeStr() {
        return getSavedArguments().getString("pm_source_type_str");
    }

    @Override
    public void initView() {

        intoChatList();

    }

    @Override
    public void intoChatRoom(int roomType, String vid, int uid, ChatRoom chatRoom) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.main_root, ChatFragment.newInstance(roomType, uid, vid, chatbarMode(), chatRoom, pmSourceType(), pmSourceTypeStr()))
                .commitAllowingStateLoss();
    }

    public void intoChatList() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.main_root, ChatRoomListFragment.newInstance())
                .commitAllowingStateLoss();
        showLiveInfo();
    }

    @Override
    public boolean kickOut() {
        DialogUtil.showSimpleTipDialog(requireActivity(), null, WordUtil.getString(R.string.kick_out_user_tips), false, v -> {
            intoChatList();
        });
        return true;
    }

    @Override
    public void showLiveInfo() {
        ExpandLiveInfo expand = ActionGetter.getExpandLiveInfo(this);
        if (expand != null) expand.showLiveInfo();
    }

    @Override
    public void hideLiveInfo() {
        ExpandLiveInfo expand = ActionGetter.getExpandLiveInfo(this);
        if (expand != null) expand.hideLiveInfo();
    }
}
