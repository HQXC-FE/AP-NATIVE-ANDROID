package com.xtree.live.gift;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xtree.live.R;
import com.xtree.live.model.GiftBean;
import com.xtree.live.uitl.GlideLoader;
import com.xtree.live.uitl.WordUtil;

import java.util.List;

public class GiftAdapter extends BaseQuickAdapter<GiftBean, BaseViewHolder> {

    private final SelectCallback mCallback;
    private int mPosition = -1;

    public GiftAdapter(@Nullable List<GiftBean> data, SelectCallback callback) {
        super(R.layout.item_new_gift, data);
        mCallback = callback;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GiftBean item, @NonNull List<?> payloads) {
        if(payloads.contains("selectStatus")){
            helper.itemView.setSelected(helper.getBindingAdapterPosition() == mPosition);
        }
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GiftBean item) {
        helper.itemView.setSelected(helper.getBindingAdapterPosition() == mPosition);
        helper.setText(R.id.giftPrice, WordUtil.getString(R.string.diamond_expense, item.getNeedcoin()));
        helper.setText(R.id.giftName, String.valueOf(item.getGiftname()));
        GlideLoader.loadGift(getContext(), item.getGifticon(), helper.getView(R.id.giftIcon), -1, -1);
        helper.itemView.setOnClickListener(view -> {
            view.setSelected(true);
            int oldPosition = mPosition;
            mPosition = helper.getBindingAdapterPosition();
            if(oldPosition >= 0  && oldPosition < getItemCount()) notifyItemChanged(oldPosition, "selectStatus");
            mCallback.onSelect(item);
        });
    }

    public interface SelectCallback {
        void onSelect(GiftBean bean);
    }
}

