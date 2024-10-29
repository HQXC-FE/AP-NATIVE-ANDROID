package com.xtree.live.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.live.R;
import com.xtree.live.data.source.response.AnchorSortResponse;
import com.xtree.live.databinding.ItemLiveBroadcasterBinding;

/**
 * 主播列表Adapter
 */
public class BroadcasterAdapter extends CachedAutoRefreshAdapter<AnchorSortResponse> {
    Context ctx;
    ItemLiveBroadcasterBinding binding;
    public BroadcasterAdapter(Context ctx) {
        this.ctx = ctx;
    }
    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_live_broadcaster, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {

        AnchorSortResponse vo =  get(position);

        binding = ItemLiveBroadcasterBinding.bind(holder.itemView);
/*

        Glide.with(ctx).load(vo.avatar).placeholder(R.mipmap.lv_broadcaster_header_default_off).into(binding.ivLiveBroadcasterHeader.ivLiveOnHeader);
        binding.tvLiveBroadcasterName.setText(vo.user_nickname);
        binding.tvLiveBroadcasterFollowers.setText(vo.attention + "关注");
        //是否有正在直播(1:是,0:否)
        if (vo.is_live == 1){
            binding.ivLiveBroadcasterHeader.ivIsLive.setVisibility(View.VISIBLE);
        }else{
            binding.ivLiveBroadcasterHeader.ivIsLive.setVisibility(View.GONE);
        }
*/


    }
}
