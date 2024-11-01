package com.xtree.live.ui.main.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TagUtils;
import com.xtree.live.R;
import com.xtree.live.data.source.response.AnchorSortResponse;

import me.xtree.mvvmhabit.utils.SPUtils;

public class AttentionListAdapter extends BaseAdapter {
    private Context mContext ;
    private AnchorSortResponse anchorSortResponse ;
    private LayoutInflater layoutInflater ;

    public AttentionListAdapter (Context context , final AnchorSortResponse anchorSortResponse){
        super();
        this.mContext = context ;
        this.anchorSortResponse = anchorSortResponse;
        this.layoutInflater =LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return anchorSortResponse.data.size();
    }

    @Override
    public Object getItem(int position) {
        return anchorSortResponse.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){

            convertView = layoutInflater.inflate(R.layout.item_live_broadcaster , parent  , false);
            holder = new ViewHolder();
            holder.nameText = convertView.findViewById(R.id.tvLiveBroadcasterName);
            holder.headerImage = convertView.findViewById(R.id.iv_live_header);
            holder.attentionText = convertView.findViewById(R.id.tvLiveBroadcasterFollowers);
            holder.isLiveImage = convertView.findViewById(R.id.ivIsLive);
            holder.signText = convertView.findViewById(R.id.tv_anchor_signature);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        AnchorSortResponse.AnchorResponse vo = anchorSortResponse.data.get(position);
        if (TextUtils.equals("1",vo.is_live)){
            //直播中
            holder.isLiveImage.setVisibility(View.VISIBLE);
        }else{
            holder.isLiveImage.setVisibility(View.GONE);
        }
        holder.nameText.setText(vo.user.user_nickname);
        holder.attentionText.setText(vo.user.attention + "关注");
/*
        GlideUrl glideUrl = new GlideUrl(imageDownUrl, new LazyHeaders.Builder()
                .addHeader("Content-Type", "application/vnd.sc-api.v1.json")
                .addHeader("Authorization", "bearer " + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN))
                .addHeader("Cookie", cookie)
                .addHeader("UUID", TagUtils.getDeviceId(getContext()))
                .build());*/
    /*    GlideUrl glideUrl = new GlideUrl("https://zhibo-apps.oxldkm.com/avatar.png", new LazyHeaders.Builder()
                .addHeader("Content-Type", "application/vnd.sc-api.v1.json").build());
        Glide.with(mContext).load("https://zhibo-apps.oxldkm.com/avatar.png")
                .placeholder(R.mipmap.lv_broadcaster_header_default_on)
                .error(R.mipmap.lv_broadcaster_header_default_on).into(holder.headerImage);
*/
        Uri uri = Uri.parse("\"https://zhibo-apps.oxldkm.com/avatar.png\"");
        Glide.with(mContext).load(uri).into(holder.headerImage);

        return convertView;
    }

    class ViewHolder {
        TextView nameText;//直播用户名称
        ImageView headerImage;//头像
        TextView  attentionText ;//关注人数
        ImageView isLiveImage;//直播状态
        TextView  signText ;//签名

    }
}
