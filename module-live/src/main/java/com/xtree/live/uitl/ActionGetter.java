package com.xtree.live.uitl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xtree.live.inter.EnterRoomBridge;
import com.xtree.live.inter.GiftViewMarginBottomListener;
import com.xtree.live.inter.KickUserInterface;
import com.xtree.live.inter.PVid;
import com.xtree.live.inter.SearchContent;
import com.xtree.live.inter.UnreadChanged;
import com.xtree.live.message.ExpandLiveInfo;

public class ActionGetter {

    public static @Nullable UnreadChanged getUnreadChanged(@NonNull Fragment fragment){
        Fragment parentFragment = fragment.getParentFragment();
        if(parentFragment != null){
            if(parentFragment instanceof UnreadChanged){
                return (UnreadChanged) parentFragment;
            }else {
                return getUnreadChanged(parentFragment);
            }
        }else {
            if (fragment.getActivity() instanceof UnreadChanged){
                return  (UnreadChanged) fragment.getActivity();
            }else {
                return null;
            }
        }
    }

    public static @Nullable EnterRoomBridge getEnterRoomBridge(@NonNull Fragment fragment) {
        Fragment parentFragment = fragment.getParentFragment();
        if(parentFragment != null){
            if(parentFragment instanceof EnterRoomBridge){
                return (EnterRoomBridge) parentFragment;
            }else {
                return getEnterRoomBridge(parentFragment);
            }
        }else {
            if (fragment.getActivity() instanceof EnterRoomBridge){
                return  (EnterRoomBridge) fragment.getActivity();
            }else {
                return null;
            }
        }
    }

    public static @Nullable ExpandLiveInfo getExpandLiveInfo(@NonNull Fragment fragment) {
        Fragment parentFragment = fragment.getParentFragment();
        if(parentFragment != null){
            if(parentFragment instanceof ExpandLiveInfo){
                return (ExpandLiveInfo) parentFragment;
            }else {
                return getExpandLiveInfo(parentFragment);
            }
        }else {
            if (fragment.getActivity() instanceof ExpandLiveInfo){
                return  (ExpandLiveInfo) fragment.getActivity();
            }else {
                return null;
            }
        }
    }


    public static @Nullable SearchContent getSearchContent(@NonNull Fragment fragment) {
        Fragment parentFragment = fragment.getParentFragment();
        if(parentFragment != null){
            if(parentFragment instanceof SearchContent){
                return (SearchContent) parentFragment;
            }else {
                return getSearchContent(parentFragment);
            }
        }else {
            if (fragment.getActivity() instanceof SearchContent){
                return  (SearchContent) fragment.getActivity();
            }else {
                return null;
            }
        }
    }


    public static @Nullable PVid getPVid(@NonNull Fragment fragment) {
        Fragment parentFragment = fragment.getParentFragment();
        if(parentFragment != null){
            if(parentFragment instanceof PVid){
                return (PVid) parentFragment;
            }else {
                return getPVid(parentFragment);
            }
        }else {
            if (fragment.getActivity() instanceof PVid){
                return  (PVid) fragment.getActivity();
            }else {
                return null;
            }
        }
    }

    public static @Nullable GiftViewMarginBottomListener getGiftViewMarginListener(@NonNull Fragment fragment) {
        Fragment parentFragment = fragment.getParentFragment();
        if(parentFragment != null){
            if(parentFragment instanceof GiftViewMarginBottomListener){
                return (GiftViewMarginBottomListener) parentFragment;
            }else {
                return getGiftViewMarginListener(parentFragment);
            }
        }else {
            if (fragment.getActivity() instanceof GiftViewMarginBottomListener){
                return  (GiftViewMarginBottomListener) fragment.getActivity();
            }else {
                return null;
            }
        }
    }

    public static @Nullable KickUserInterface getKickUserAction(@NonNull Fragment fragment) {
        Fragment parentFragment = fragment.getParentFragment();
        if(parentFragment != null){
            if(parentFragment instanceof KickUserInterface){
                return (KickUserInterface) parentFragment;
            }else {
                return getKickUserAction(parentFragment);
            }
        }else {
            if (fragment.getActivity() instanceof KickUserInterface){
                return  (KickUserInterface) fragment.getActivity();
            }else {
                return null;
            }
        }
    }
}

