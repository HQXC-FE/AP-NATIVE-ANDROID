package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemThirdManagementBinding;
import com.xtree.mine.vo.response.ThirdManagementResponse;

public class ThirdManagementAdapter extends CachedAutoRefreshAdapter<ThirdManagementResponse.ThirdManagementResponseDTO> {
    Context ctx;
    ItemThirdManagementBinding binding;

    public interface ICallBack {
        void onClick(String member);
    }

    public ThirdManagementAdapter(Context context) {
        ctx = context;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_third_management, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        ThirdManagementResponse.ThirdManagementResponseDTO vo = get(position);
        binding = ItemThirdManagementBinding.bind(holder.itemView);

        binding.tvwDate.setText(vo.getDate());
        binding.tvwName.setText(vo.getUsername());
        binding.tvwPlatform.setText(vo.getCidCn());
        binding.tvwBonusMoney.setText(vo.getProjectWin());
        binding.tvwReturnMoney.setText(vo.getMyselfPrice());
    }
}
