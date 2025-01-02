package com.xtree.live.ui.main.fragment;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentChatBinding;
import com.xtree.live.ui.main.model.chat.LiveWebSocketViewModel;
import com.xtree.live.ui.main.service.LiveWebSocketService;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;
import com.xtree.service.message.MessageData;
import com.xtree.service.message.MessageType;
import com.xtree.service.message.PushServiceConnection;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.sentry.Sentry;
import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

@Route(path = RouterFragmentPath.Live.PAGER_LIVE_CHAT)
public class ChatFragment extends BaseFragment<FragmentChatBinding, LiveViewModel> {
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> tabList = new ArrayList<>();
    private FragmentStateAdapter mAdapter;
    private PushServiceConnection pushServiceConnection;
    private Observer<Object> pushObserver;
    private String url;
    private LiveWebSocketViewModel liveWebSocketViewModel;
    int i = 0;

    @Override
    public void initView() {
        url = "https://zhibo-apis.oxldkm.com" + "/wss/?xLiveToken=" + "e3e4812409ba683babeb566b9e31493cbf6ac755225903bd890e043d99af46f8fb0f842b5d30fc6ccb299000f3586fdb";
        //协议转换
        if (url.startsWith("https")) {
            url = url.replaceFirst("https", "wss");
        } else if (url.startsWith("http")) {
            url = url.replaceFirst("http", "ws");
        }

        mAdapter = new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        };

        binding.vpMain.setAdapter(mAdapter);
        binding.vpMain.setUserInputEnabled(true); // ViewPager2 左右滑动

        new TabLayoutMediator(binding.tblType, binding.vpMain, (tab, position) -> {
            tab.setText(tabList.get(position));
        }).attach();

        Fragment bindMsgFragment = new Fragment();
        Fragment bindMsgPersonFragment = new Fragment();

        String txtSquare = getString(R.string.txt_live_chat_square);
        String txtBetting = getString(R.string.txt_live_chat_betting);
        String txtPrivate = getString(R.string.txt_live_chat_private);
        String txtMsgAssistant = getString(R.string.txt_live_chat_assistant);

        fragmentList.add(bindMsgFragment);
        fragmentList.add(bindMsgPersonFragment);
        fragmentList.add(bindMsgFragment);
        fragmentList.add(bindMsgPersonFragment);
        tabList.add(txtSquare);
        tabList.add(txtBetting);
        tabList.add(txtPrivate);
        tabList.add(txtMsgAssistant);

        mAdapter.notifyDataSetChanged();

        initPushService();
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
    public LiveViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(LiveViewModel.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (pushServiceConnection.isBound()) {
            getActivity().unbindService(pushServiceConnection);
        }
        if (pushObserver != null && liveWebSocketViewModel != null) {
            liveWebSocketViewModel.getWsTokenLiveData.removeObserver(pushObserver);
        }
    }

    private void initPushService() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
        liveWebSocketViewModel = new ViewModelProvider(this, factory).get(LiveWebSocketViewModel.class);
        Messenger replyMessenger = new Messenger(new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                MessageType.Output outputType = MessageType.Output.fromCode(msg.what);
                switch (outputType) {
                    case OBTAIN_LINK:
                        CfLog.e(i++ + "");
                        long checkInterval = SPUtils.getInstance().getLong(SPKeyGlobal.WS_CHECK_INTERVAL, 10);
                        long retryNumber = SPUtils.getInstance().getLong(SPKeyGlobal.WS_RETRY_NUMBER, 3);
                        long retryWaitingTime = SPUtils.getInstance().getLong(SPKeyGlobal.WS_RETRY_WAITING_TIME, 300);
                        long expireTime = SPUtils.getInstance().getLong(SPKeyGlobal.WS_EXPIRE_TIME, 90);

                        Bundle obj = new Bundle();
                        obj.putString("url", url);
                        obj.putLong("checkInterval", checkInterval);
                        obj.putLong("retryNumber", retryNumber);
                        obj.putLong("retryWaitingTime", retryWaitingTime);
                        obj.putLong("expireTime", expireTime);
                        obj.putString("action", "sub");
                        obj.putString("vid", "25CF6942CB31DBFB888D7EBD18DE09D3");
                        pushServiceConnection.sendMessageToService(MessageType.Input.LINK, obj);
                        break;
                    case REMOTE_MSG://后端的消息
                        CfLog.e(i++ + "");
                        if (msg.getData() != null) {
                            CfLog.i("receiving class: " + MessageData.class.getName());
                            try {
                                msg.getData().setClassLoader(getActivity().getClassLoader());
                                //                                getActivity().handleRemoteMessage(msg.getData().getParcelable("data"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Sentry.captureException(e);
                            }
                        }
                        break;
                }
            }
        });
        pushServiceConnection = new PushServiceConnection(replyMessenger);
        // 绑定 Service
        Intent intent = new Intent(getContext(), LiveWebSocketService.class);
        getActivity().bindService(intent, pushServiceConnection, Context.BIND_AUTO_CREATE);

        long checkInterval = SPUtils.getInstance().getLong(SPKeyGlobal.WS_CHECK_INTERVAL, 10);
        long retryNumber = SPUtils.getInstance().getLong(SPKeyGlobal.WS_RETRY_NUMBER, 3);
        long retryWaitingTime = SPUtils.getInstance().getLong(SPKeyGlobal.WS_RETRY_WAITING_TIME, 300);
        long expireTime = SPUtils.getInstance().getLong(SPKeyGlobal.WS_EXPIRE_TIME, 90);

        Bundle obj = new Bundle();
        obj.putString("url", url);
        obj.putLong("checkInterval", checkInterval);
        obj.putLong("retryNumber", retryNumber);
        obj.putLong("retryWaitingTime", retryWaitingTime);
        obj.putLong("expireTime", expireTime);
        obj.putString("action", "sub");
        obj.putString("vid", "2A5B1B927ADFFAFF3AAC969DF7C22AB5");
        pushServiceConnection.sendMessageToService(MessageType.Input.LINK, obj);
        liveWebSocketViewModel.getWsTokenLiveData.observeForever(pushObserver);
    }
}
