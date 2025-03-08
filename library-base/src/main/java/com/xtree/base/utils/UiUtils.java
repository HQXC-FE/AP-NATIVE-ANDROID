package com.xtree.base.utils;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.utils.DensityUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

public class UiUtils {

    public static void addRecycleViewVDecoration(Context context, RecyclerView rv,int sizeDp,int color){
        rv.addItemDecoration(new VerticalDividerItemDecoration.Builder(context)
                .size(DensityUtil.dip2px(context,sizeDp))
                .color(color)
                .build()
        );
    }

    public static void addRecycleViewVDecoration(Context context, RecyclerView rv,int sizeDp,int color,int marginDp){
        rv.addItemDecoration(new VerticalDividerItemDecoration.Builder(context)
                .size(DensityUtil.dip2px(context,sizeDp))
                .color(color)
                .margin(DensityUtil.dip2px(context,marginDp))
                .build()
        );
    }

    public static void addRecycleViewVDecoration(Context context, RecyclerView rv,int sizeDp,int color,int topMarginDp,int bottomMarginDp){
        rv.addItemDecoration(new VerticalDividerItemDecoration.Builder(context)
                .size(DensityUtil.dip2px(context,sizeDp))
                .color(color)
                .margin(DensityUtil.dip2px(context,topMarginDp),DensityUtil.dip2px(context,bottomMarginDp))
                .build()
        );
    }

    public static void addRecycleViewHDecoration(Context context, RecyclerView rv,int sizeDp,int color){
        rv.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .size(DensityUtil.dip2px(context,sizeDp))
                .color(color)
                .build()
        );
    }

    public static void addRecycleViewHDecoration(Context context, RecyclerView rv,int sizeDp,int color,int marginDp){
        rv.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .size(DensityUtil.dip2px(context,sizeDp))
                .color(color)
                .margin(DensityUtil.dip2px(context,marginDp))
                .build()
        );
    }

    public static void addRecycleViewHDecoration(Context context, RecyclerView rv,int sizeDp,int color,int leftMarginDp,int rightMarginDp){
        rv.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .size(DensityUtil.dip2px(context,sizeDp))
                .color(color)
                .margin(DensityUtil.dip2px(context,leftMarginDp),DensityUtil.dip2px(context,rightMarginDp))
                .build()
        );
    }

    public static void addRecycleViewGridDecoration(Context context,RecyclerView rv,int spanCount,int spacingDp,boolean includeEdge){
        rv.addItemDecoration(
                new GridSpacingItemDecoration(spanCount, DensityUtil.dip2px(context, spacingDp), includeEdge)
        );
    }

}
