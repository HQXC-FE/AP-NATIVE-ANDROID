package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemBonusPoolBinding;
import com.xtree.mine.vo.BonusPoolReportItemVo;

public class BonusPoolAdapter extends CachedAutoRefreshAdapter<BonusPoolReportItemVo> {
    Context ctx;
    ItemBonusPoolBinding binding;

    public BonusPoolAdapter(Context context) {
        ctx = context;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_bonus_pool, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        BonusPoolReportItemVo vo = get(position);
        binding = ItemBonusPoolBinding.bind(holder.itemView);

        binding.tvwDate.setText(vo.getDate());
        binding.tvwName.setText(vo.getParrentName());
        binding.tvwOpenNumber.setText(vo.getCode());
        binding.tvwReturnMoney.setText(vo.getPoolPrice());
        binding.tvwEasterNumber.setText(vo.getEastereggCode());
        binding.tvwTouchMember.setText(vo.getUsername());
    }
}
