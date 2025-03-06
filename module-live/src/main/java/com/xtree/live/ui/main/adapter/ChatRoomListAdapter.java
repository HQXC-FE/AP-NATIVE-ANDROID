package com.xtree.live.ui.main.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xtree.live.LiveConfig;
import com.xtree.live.R;
import com.xtree.live.message.ChatRoomInfo;
import com.xtree.live.message.ChatRoomLastMsg;
import com.xtree.live.message.RoomType;
import com.xtree.live.uitl.GlideLoader;
import com.xtree.live.uitl.HtmlHelper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import me.xtree.mvvmhabit.utils.Utils;

public class ChatRoomListAdapter extends BaseQuickAdapter<ChatRoomInfo, BaseViewHolder> {

    private final SimpleDateFormat todayFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final SimpleDateFormat ThisYearFormat = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat outThisYearFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private final int USER_ICON_SIZE = ConvertUtils.dp2px(36);

    public ChatRoomListAdapter() {
        super(R.layout.item_live_chat_room_list);
        addChildClickViewIds(R.id.pinChatRoom);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ChatRoomInfo item, @NonNull List<?> payloads) {
        for (Object payload : payloads) {
            if(payload instanceof Integer){
                int payloadId = (int) payload;
                if(payloadId == R.id.tv_text){
                    setLastMsg(holder, item);
                    setBadge(holder, item);
                }else if(payloadId == R.id.tv_online){
                    setOnline(holder, item);
//                } else if(payloadId == R.id.tv_badge){
//                    setBadge(holder, item);
                } else if(payloadId == R.id.pinChatRoom){
                    holder.getView(R.id.pinChatRoom).setSelected(item.getIsPin() == 1);
                }else if(payloadId == 1){
                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                    holder.itemView.startAnimation(shake);
                }
            }
        }
    }


    private void animateView(View view) {
        float density = view.getContext().getResources().getDisplayMetrics().density;
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        view.setTranslationY(0);
        view.animate().translationY(300 * density).setInterpolator(bounceInterpolator).setDuration(1000);
    }

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChatRoomInfo item) {
        helper.setText(R.id.tv_user, TextUtils.isEmpty(item.getName()) ? "unknown" : item.getName());
        ImageView flagGroup = helper.getView(R.id.flagGroup);
        if(item.getRoomType() == RoomType.PAGE_CHAT_GROUP){
            flagGroup.setVisibility(View.VISIBLE);
            String imagePath = item.getPic();
            GlideLoader.loadGroupCircleImageDefault(Glide.with(getContext()), imagePath, helper.getView(R.id.iv_user), USER_ICON_SIZE, USER_ICON_SIZE);
        }else {
            flagGroup.setVisibility(View.GONE);
            GlideLoader.loadUserCircleImageDefault(Glide.with(getContext()), item.getPic(), helper.getView(R.id.iv_user), USER_ICON_SIZE, USER_ICON_SIZE);
        }
        setPinChatRoom(helper, item);
        setBadge(helper, item);
        setOnline(helper, item);
        setLastMsg(helper, item);
    }

    private void setPinChatRoom(@NonNull BaseViewHolder holder,ChatRoomInfo item) {
        if(LiveConfig.isLogin()){
            holder.setGone(R.id.pinChatRoom, false);
            holder.getView(R.id.pinChatRoom).setSelected(item.getIsPin() == 1);
            holder.getView(R.id.pinChatRoom).setOnClickListener(view->{
                listListener.onPinChatRoom(item);
//                mPresenter.pinChatRoom(item);
            });
        }else {
            holder.setGone(R.id.pinChatRoom, true);
        }
    }

    private void setLastMsg(@NonNull BaseViewHolder helper, ChatRoomInfo item) {
        ChatRoomLastMsg lastMsg = item.getLastMsg();
        if (lastMsg == null) return;
        if (!TextUtils.isEmpty(lastMsg.getText())) {
            helper.setText(R.id.tv_text, HtmlHelper.applyHtmlStr(item.getLastMsg().getText(), helper.getView(R.id.tv_text)));
        } else if(!TextUtils.isEmpty(lastMsg.getPic())){
            helper.setText(R.id.tv_text, Utils.getContext().getString(R.string.represent_pic));
        }else {
            helper.setText(R.id.tv_text, "");
        }
        helper.setText(R.id.tv_time, lastMsg.getCreationTime());
    }

    private static void setBadge(@NonNull BaseViewHolder helper, ChatRoomInfo item) {
        helper.setGone(R.id.img_badge, item.getUnread() <= 0);
    }

    private static void setOnline(@NonNull BaseViewHolder helper, ChatRoomInfo item) {
        helper.setVisible(R.id.ll_online, item.getRoomType() == RoomType.PAGE_CHAT_PRIVATE);
        if (item.getRoomType() == RoomType.PAGE_CHAT_PRIVATE) {
            helper.setImageResource(R.id.iv_online, item.getIsOnline() == 1 ? R.drawable.bg_chat_online : R.drawable.bg_chat_offline);
            helper.setText(R.id.tv_online, item.getIsOnline() == 1 ? R.string.chat_online : R.string.chat_offline);
            helper.setTextColorRes(R.id.tv_online, item.getIsOnline() == 1 ? R.color.onLineColor: R.color.offLineColor);
        }
    }

    private chatRoolListListener listListener;

    public void setListListener(chatRoolListListener listListener) {
        this.listListener = listListener;
    }

    public chatRoolListListener getListListener() {
        return listListener;
    }

    public interface chatRoolListListener {
        void onPinChatRoom(ChatRoomInfo item);
    }

}
