package com.xtree.live.uitl;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.tabs.TabLayout;
import com.xtree.base.utils.DomainUtil;
import com.xtree.live.R;
import com.xtree.live.socket.BNC;

public class GlideLoader {
    // 设置图像的加载中以及加载失败图片
    public static void loadLiveActivityHeadBackground(RequestManager requestManager, String path, ImageView mImageView) {
//        requestManager
//                .load(completeImagePath(path))
//                .placeholder(R.drawable.ic_live_activity_head_background)
//                .dontAnimate()
//                .error(R.drawable.ic_live_activity_head_background)
//                .into(new ImageViewTarget(mImageView, path, "LIVE_ACTIVITY_HEAD"));
    }

    public static void loadCirclePlayerPhoto(boolean isHomeTeam, RequestManager requestManager, String path, ImageView imageView, int width, int height) {
//        int placeHolderRes = isHomeTeam ? R.drawable.svg_football_blue_ream_uniform : R.drawable.svg_football_red_ream_uniform;
//        requestManager
//                .load(completeImagePath(path))
//                .placeholder(placeHolderRes)
//                .circleCrop()
//                .override(width, height)
//                .dontAnimate()
//                .error(placeHolderRes)
//                .into(new ImageViewTarget(imageView, path));
    }

    public static void loadPlayerPhoto(boolean isHomeTeam, RequestManager requestManager, String path, ImageView imageView, int width, int height) {
//        int placeHolderRes = isHomeTeam ? R.drawable.svg_football_blue_ream_uniform : R.drawable.svg_football_red_ream_uniform;
//        requestManager
//                .load(completeImagePath(path))
//                .placeholder(placeHolderRes)
//                .override(width, height)
//                .dontAnimate()
//                .error(placeHolderRes)
//                .into(new ImageViewTarget(imageView, path));
    }

    public static void loadPinImage(Context context, String path, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(completeImagePath(path))
                .override(width, height)
                .placeholder(R.drawable.ic_image_placeholder_loading)
                .error(R.drawable.ic_image_placeholder_fail)
                .into(new ImageViewTarget(imageView, path, "PIN_IMAGE"));
    }

    public static void loadGift(Context context, String path, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(completeImagePath(path))
                .override(width, height)
                .placeholder(R.drawable.ic_image_placeholder_loading)
                .error(R.drawable.ic_image_placeholder_fail)
                .into(new ImageViewTarget(imageView, path, "GIFT_IMAGE"));
    }

    public static void loadChatBannerImage(RequestManager requestManager, BNC source, ImageViewTarget target) {
        requestManager
                .load(BNC.loadImageObject(source.imagePath(), source.objectKey()))
                .placeholder(R.drawable.ic_image_placeholder_loading)
                .error(R.drawable.ic_image_placeholder_fail)
                .into(target);
    }

    public static void loadChatImage(RequestManager requestManager, BNC source, ImageViewTarget target, int width, int height, boolean skipMemory) {
        requestManager
                .load(BNC.loadImageObject(source.imagePath(), source.objectKey()))
                .override(width, height)
                .skipMemoryCache(skipMemory)
                .placeholder(R.drawable.message_image_placeholder)
                .error(R.drawable.message_image_placeholder)
                .fallback(R.drawable.message_image_placeholder)
                .into(target);
    }

    public static void loadGroupCircleImageDefault(RequestManager requestManager, String path, ImageView mImageView, int width, int height) {
        requestManager
                .load(completeImagePath(path))
                .override(width, height)
                .placeholder(R.drawable.ic_default_profile_group)
                .dontAnimate()
                .error(R.drawable.ic_default_profile_group)
                .transform(new CircleCrop())
                .into(mImageView);
    }

    public static void loadGroupCircleImageDefault(RequestManager requestManager, String path, ImageViewTarget target, int width, int height) {
        requestManager
                .load(completeImagePath(path))
                .override(width, height)
                .placeholder(R.drawable.ic_default_profile_group)
                .dontAnimate()
                .error(R.drawable.ic_default_profile_group)
                .transform(new CircleCrop())
                .into(target);
    }

    public static void loadUserCircleImageDefault(RequestManager requestManager, String path, ImageView mImageView, int width, int height) {
        requestManager
                .load(completeImagePath(path))
                .placeholder(R.mipmap.ic_default_profile)
                .override(width, height)
                .dontAnimate()
                .error(R.mipmap.ic_default_profile)
                .transform(new CircleCrop())
                .into(mImageView);
    }

    public static void loadEmojiGif(RequestManager requestManager, String path, ImageView mImageView, int width, int height) {
        requestManager
                .load(completeImagePath(path))
                .override(width, height)
                .placeholder(R.drawable.ic_image_placeholder_loading)
                .error(R.drawable.ic_image_placeholder_fail)
                .into(new ImageViewTarget(mImageView, path, "EMOJI_GIF_IMAGE"));
    }

    // 设置用户图像的加载中以及加载失败图片
    public static void loadUserImageDefault(RequestManager requestManager, String path, ImageView mImageView, int width, int height) {
        requestManager
                .load(completeImagePath(path))
                .placeholder(R.mipmap.ic_default_profile)
                .override(width, height)
                .centerCrop()
                .dontAnimate()
                .error(R.mipmap.ic_default_profile)
                .into(new ImageViewTarget(mImageView, path, "USER_ICON_IMAGE"));
    }




    public static void loadLiveImageDefault(RequestManager requestManager, String path, ImageView mImageView, int width, int height) {
        requestManager
                .load(completeImagePath(path))
                .override(width, height)
                .placeholder(R.mipmap.ic_default_live_thumb)
                .error(R.mipmap.ic_default_live_thumb)
                .into(new ImageViewTarget(mImageView, path, "LIVE_THUMB_IMAGE"));
    }

    // 设置图像的加载中以及加载失败图片
    public static void loadBannerForeground(RequestManager requestManager, String path, ImageView mImageView) {
        requestManager
                .load(completeImagePath(path))
                .placeholder(R.mipmap.ic_default_live_thumb)
                .dontAnimate()
                .error(R.mipmap.ic_default_live_thumb)
                .into(new ImageViewTarget(mImageView, path, "THEME_BANNER_FOREGROUND"));
    }


    //推荐页 加载banner前景
    public static void loadRecommendBannerForeground(RequestManager requestManager, String path, ImageView mImageView) {
        requestManager
                .load(completeImagePath(path))
                .transition(withCrossFade())
                .dontAnimate()
                .into(new ImageViewTarget(mImageView, path, "RECOMMEND_BANNER_FOREGROUND"));
    }


    public static void loadVideoPauseCover(RequestManager requestManager, String path, ImageView image) {
        requestManager
                .load(completeImagePath(path))
                .centerCrop()
                .placeholder(R.mipmap.ic_live_ads_resource)
                .onlyRetrieveFromCache(true)
                .dontAnimate()
                .error(R.mipmap.ic_live_ads_resource)
                .into(new ImageViewTarget(image, path));
    }


    public static void loadThemeCover(RequestManager requestManager, String path, ImageView imageView, int width, int height) {
        requestManager
                .load(completeImagePath(path))
                .override(width, height)
                .placeholder(R.mipmap.ic_default_live_thumb)
                .centerCrop()
                .dontAnimate()
                .error(R.mipmap.ic_default_live_thumb)
                .into(new ImageViewTarget(imageView, path, "THEME_THUMB_IMAGE"));
    }

    public static String completeImagePath(String path) {
        if (!TextUtils.isEmpty(path) && path.startsWith("content://")) {
            return path;
        }
        return completeUrl(path);
    }


    public static boolean isSVG(@Nullable String path) {
        return !TextUtils.isEmpty(path) && path.toLowerCase().endsWith(".svg");
    }

    public static String completeUrl(String path){
        if (!TextUtils.isEmpty(path) && !path.startsWith("https") && !path.startsWith("http")) {
            path = DomainUtil.getApiUrl() + path;
        }
        return path;
    }

}

