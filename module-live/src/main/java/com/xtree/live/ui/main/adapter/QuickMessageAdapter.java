package com.xtree.live.ui.main.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xtree.live.R;

import java.util.List;

public class QuickMessageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int clickPosition = -1;
    private List<String> messageList;

    public QuickMessageAdapter(List<String> messageList) {
        super(R.layout.item_quick_reply);
        this.messageList = messageList;
    }

    public void setClickPosition(int clickPosition) {
        this.clickPosition = clickPosition;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String s) {
        int position = helper.getAdapterPosition();
        TextView tvMsg = helper.getView(R.id.tv_msg);
        tvMsg.setText(s);
        tvMsg.setSelected(clickPosition == position);
    }
}
