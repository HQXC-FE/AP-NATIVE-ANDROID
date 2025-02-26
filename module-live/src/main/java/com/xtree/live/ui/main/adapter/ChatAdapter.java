package com.xtree.live.ui.main.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.DiffUtil;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xtree.live.R;
import com.xtree.live.message.ConversationMessage;
import com.xtree.live.message.MessageItemType;
import com.xtree.live.socket.BNCObject;
import com.xtree.live.uitl.GlideLoader;
import com.xtree.live.uitl.ImageInfoParser;
import com.xtree.live.uitl.ImageViewTarget;
import com.xtree.live.widge.MessageTextView;

import java.util.List;

import me.xtree.mvvmhabit.utils.ToastUtils;

@SuppressLint("SetTextI18n")
public class ChatAdapter extends BaseMultiItemQuickAdapter<ConversationMessage, BaseViewHolder> implements UpFetchModule, LoadMoreModule {

    private final static int MINI_IMAGE_WIDTH = ConvertUtils.dp2px(120);
    private final static int MINI_IMAGE_HEIGHT = MINI_IMAGE_WIDTH;

    private final static int CORNER = ConvertUtils.dp2px(8);
    private final RequestManager mGlideRequest;
    private final ViewOutlineProvider imageOutlineProvider = new ViewOutlineProvider() {
        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), CORNER);
        }
    };

    private final ViewOutlineProvider outlineGlobalContainerProvider = new ViewOutlineProvider() {
        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), CORNER);
        }
    };

    public ChatAdapter(@Nullable List<ConversationMessage> data, RequestManager glideRequest) {
        super(data);
        super.setDiffCallback(new DiffUtil.ItemCallback<ConversationMessage>() {
            @Override
            public boolean areItemsTheSame(@NonNull ConversationMessage oldItem, @NonNull ConversationMessage newItem) {
                return oldItem.areItemsTheSame(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull ConversationMessage oldItem, @NonNull ConversationMessage newItem) {
                return oldItem.areContentsTheSame(newItem);
            }
        });
        mGlideRequest = glideRequest;
        addChildClickViewIds(R.id.message_retry_send);
        addItemType(MessageItemType.MSG_OUTGOING_TEXT, R.layout.message_item_outgoing_text);
        addItemType(MessageItemType.MSG_OUTGOING_IMG, R.layout.message_item_outgoing_image);

        addItemType(MessageItemType.MSG_GLOBAL_GIFT, R.layout.message_item_global_text);
        addItemType(MessageItemType.MSG_GLOBAL_TEXT, R.layout.message_item_global_text);
        addItemType(MessageItemType.MSG_GLOBAL_BANNER, R.layout.message_item_global_banner);
        addItemType(MessageItemType.MSG_GLOBAL_IMG, R.layout.message_item_global_image);

        addItemType(MessageItemType.MSG_INCOMING_BANNER, R.layout.message_item_incoming_banner);
        addItemType(MessageItemType.MSG_INCOMING_TEXT, R.layout.message_item_incoming_text);
        addItemType(MessageItemType.MSG_INCOMING_IMG, R.layout.message_item_incoming_image);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        switch (helper.getItemViewType()) {
            case MessageItemType.MSG_GLOBAL_GIFT:
            case MessageItemType.MSG_GLOBAL_TEXT:
                bindGlobalText(helper, bean);
                break;
            case MessageItemType.MSG_GLOBAL_IMG:
                bindGlobalImage(helper, bean);
                break;
            case MessageItemType.MSG_GLOBAL_BANNER:
                bindGlobalBanner(helper, bean);
                break;
            case MessageItemType.MSG_OUTGOING_TEXT:
                bindOutgoingText(helper, bean);
                break;
            case MessageItemType.MSG_OUTGOING_IMG:
                bindOutgoingImage(helper, bean);
                break;

            case MessageItemType.MSG_INCOMING_BANNER:
                bindIncomingBanner(helper, bean);
                break;
            case MessageItemType.MSG_INCOMING_TEXT:
                bindIncomingText(helper, bean);
                break;
            case MessageItemType.MSG_INCOMING_IMG:
                bindIncomingImage(helper, bean);
                break;

        }
    }

    /**
     * 广场文本消息
     *
     * @param helper
     * @param bean
     */
    private void bindGlobalText(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        applyUser(helper, bean);
        helper.itemView.setOutlineProvider(outlineGlobalContainerProvider);
        helper.itemView.setClipToOutline(true);
    }


    /**
     * 广场图片消息
     *
     * @param helper
     * @param bean
     */
    private void bindGlobalImage(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        applyUser(helper, bean);

        View container = helper.getView(R.id.message_content_container);
        View message_image = helper.getView(R.id.message_image);
        MessageTextView message_user = helper.getView(R.id.message_user);
        int textWidth = (int) message_user.getPaint().measureText(message_user.getText(), 0, message_user.getText().length());
        int availableWidth = getItemContentAvailableWidth(helper) - getViewMarginPadding(message_image);
        int imageMaxWidth = availableWidth - getViewMarginPadding(message_user) - textWidth;
        LinearLayout.LayoutParams lLayoutParams = (LinearLayout.LayoutParams) container.getLayoutParams();
        if (imageMaxWidth < MINI_IMAGE_WIDTH) {
            lLayoutParams.topMargin = ConvertUtils.dp2px(8);
            lLayoutParams.gravity = Gravity.END;
            container.setLayoutParams(lLayoutParams);
            LinearLayout itemView = (LinearLayout) helper.itemView;
            itemView.setOrientation(LinearLayout.VERTICAL);
            applyImage(helper, bean, availableWidth);
        } else {
            lLayoutParams.topMargin = 0;
            lLayoutParams.gravity = Gravity.START;
            container.setLayoutParams(lLayoutParams);
            LinearLayout itemView = (LinearLayout) helper.itemView;
            itemView.setOrientation(LinearLayout.HORIZONTAL);
            applyImage(helper, bean, imageMaxWidth);
        }

        helper.itemView.setOutlineProvider(outlineGlobalContainerProvider);
        helper.itemView.setClipToOutline(true);
    }

    public int getViewMarginPadding(View v) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return layoutParams.rightMargin + layoutParams.leftMargin + v.getPaddingStart() + v.getPaddingEnd();
    }

    public int getItemContentAvailableWidth(@NonNull BaseViewHolder helper) {
        return ScreenUtils.getAppScreenWidth() - getViewMarginPadding(getRecyclerView()) - getViewMarginPadding(helper.itemView);
    }


    private void bindOutgoingText(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        applyUserAvatar(helper, bean);
        applyText(helper, bean);
        applySendTime(helper, bean);
        applySendStatus(helper, bean);
    }


    private void bindOutgoingImage(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        applyUserAvatar(helper, bean);
        Guideline message_guide_line = helper.getView(R.id.message_guide_line);
        View message_content_container = helper.getView(R.id.message_content_container);
        int availableImageWidth = getItemContentAvailableWidth(helper)
                - getGuideLineMargin(message_guide_line)
                - getViewMarginPadding(message_content_container) -
                ConvertUtils.dp2px(24);
        applyImage(helper, bean,availableImageWidth);
        applySendTime(helper, bean);
        applyContentContainer(helper, bean);
        applySendStatus(helper, bean);
    }

    public int getGuideLineMargin(Guideline guideline){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
        return layoutParams.guideEnd + layoutParams.guideBegin + layoutParams.leftMargin + layoutParams.rightMargin;
    }


    /**
     * 广场广告
     *
     * @param helper
     * @param bean
     */
    private void bindGlobalBanner(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        applyUser(helper, bean);
        applyBanner(helper, bean);
    }


    private void bindIncomingBanner(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        applyUserAvatar(helper, bean);
        applyBanner(helper, bean);
    }

    private void bindIncomingImage(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        applyUserAvatar(helper, bean);
        applyUser(helper, bean);
        Guideline message_guide_line = helper.getView(R.id.message_guide_line);
        View message_content_container = helper.getView(R.id.message_content_container);
        int availableImageWidth = getItemContentAvailableWidth(helper)
                - getGuideLineMargin(message_guide_line)
                - getViewMarginPadding(message_content_container) -
                ConvertUtils.dp2px(24);
        applyImage(helper, bean,availableImageWidth);
        applySendTime(helper, bean);
        applyContentContainer(helper, bean);
    }

    private void bindIncomingText(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        applyUserAvatar(helper, bean);
        applyUser(helper, bean);
        applySendTime(helper, bean);
        applyText(helper, bean);
    }

    /**
     * 形状
     *
     * @param helper
     * @param bean
     */
    private void applyContentContainer(@NonNull BaseViewHolder helper, ConversationMessage bean) {

    }

    /**
     * 用户名
     *
     * @param helper
     * @param bean
     */
    private void applyUser(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        MessageTextView message_user = helper.getView(R.id.message_user);
        if (bean.isShowUsername()) {
            message_user.setVisibility(View.VISIBLE);
            message_user.setMessage(bean);
        }else {
            message_user.setVisibility(View.GONE);
        }

    }

    /**
     * 用户头像
     *
     * @param helper
     * @param bean
     */
    private void applyUserAvatar(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        ImageView message_user_avatar = helper.getView(R.id.message_user_avatar);
        GlideLoader.loadUserCircleImageDefault(mGlideRequest, bean.getFromRecipient().getProfileAvatar(), message_user_avatar, -1, -1);
    }

    /**
     * 发送时间
     *
     * @param helper
     * @param bean
     */
    private void applySendTime(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        TextView message_send_time = helper.getView(R.id.message_send_time);
        if(!TextUtils.isEmpty(bean.getSendTime())){
            message_send_time.setVisibility(View.VISIBLE);
            message_send_time.setText(bean.getSendTime());
        }else {
            message_send_time.setVisibility(View.GONE);
        }

    }

    /**
     * 广告
     *
     * @param helper
     * @param bean
     */

    private void applyBanner(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        ImageView message_banner_image = helper.getView(R.id.message_banner_image);
        TextView message_banner_title = helper.getView(R.id.message_banner_title);
        TextView message_banner_text = helper.getView(R.id.message_banner_text);
        ContentLoadingProgressBar message_loading_image = helper.getView(R.id.message_banner_loading_image);
        GlideLoader.loadChatBannerImage(mGlideRequest,bean,new ImageViewTarget(message_banner_image, bean.getBannerImage()) {

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                message_loading_image.show();
                super.onLoadStarted(placeholder);
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                message_loading_image.hide();
                super.onResourceReady(resource, transition);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                message_loading_image.hide();
                super.onLoadCleared(placeholder);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                message_loading_image.hide();
                super.onLoadFailed(errorDrawable);
            }
        });
        message_banner_title.setText(bean.convertBannerTitle(message_banner_title));
        message_banner_text.setText(bean.convertBannerContent(message_banner_text));
        helper.itemView.setOnClickListener(null);
        Intent intent = bean.bannerJumpIntent(helper.itemView.getContext());
        if (intent != null) {
            helper.itemView.setOnClickListener(v -> {
                try {
                    helper.itemView.getContext().startActivity(intent);
                } catch (Exception e) {

                }
            });
        }
    }


    /**
     * 文本
     *
     * @param helper
     * @param bean
     */
    private void applyText(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        TextView message_text = helper.getView(R.id.message_text);
        message_text.setVisibility(View.VISIBLE);
        message_text.setText(bean.convertTextMessage(message_text));
//        message_text.requestLayout();
        message_text.setTag(bean.getOriginalTextMessage());
        message_text.setOnLongClickListener(view -> {
            copyToClipboard(view);
            return false;
        });
    }

    /**
     * 图片
     *
     * @param helper
     * @param bean
     */
    private void applyImage(@NonNull BaseViewHolder helper, ConversationMessage bean, int availableImageWidth) {
        ContentLoadingProgressBar message_loading_image = helper.getView(R.id.message_loading_image);
        ImageView message_image = helper.getView(R.id.message_image);
        ImageView message_retry_load_image = helper.getView(R.id.message_retry_load_image);
        message_image.setOutlineProvider(imageOutlineProvider);
        message_image.setClipToOutline(true);
//        message_image.setOnClickListener(view -> PhotoViewActivity.forward(getContext(), new BNCObject(bean)));
        loadImage(mGlideRequest, message_image, message_loading_image, message_retry_load_image, bean,availableImageWidth);
    }

    /**
     * 发送状态
     *
     * @param helper
     * @param bean
     */
    private void applySendStatus(@NonNull BaseViewHolder helper, ConversationMessage bean) {
        ContentLoadingProgressBar message_send_loading = helper.getView(R.id.message_send_loading);
        ImageView message_retry_send = helper.getView(R.id.message_retry_send);
        if (bean.isPending()) {
            message_retry_send.setVisibility(View.GONE);
            message_send_loading.show();
        } else if (bean.isFailed()){
            message_send_loading.hide();
            message_retry_send.setVisibility(View.VISIBLE);
        }else if(bean.isRetrying()){
            message_send_loading.show();
            message_retry_send.setVisibility(View.GONE);
        }else {
            message_send_loading.hide();
            message_retry_send.setVisibility(View.GONE);
        }
    }


    private void copyToClipboard(View view) {
        try {
            String s = view.getTag().toString();
            if (!TextUtils.isEmpty(s)) {
                ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText("message", s));
                ToastUtils.showShort(getContext().getString(R.string.copy_success));
            }
        } catch (Exception e) {
        }
    }




    public  void loadImage(RequestManager requestManager, ImageView imageView, ContentLoadingProgressBar progressBar, ImageView retry,ConversationMessage bean, int maxImageWidth) {
        //预设imageView大小
        String imageUrl = bean.imagePath();
        Point imageSize = bean.getImageOriginalSize();
        if(imageSize == null){
            imageSize = ImageInfoParser.resolveImageSizeFromUri(imageUrl, MINI_IMAGE_WIDTH, MINI_IMAGE_HEIGHT);
            bean.setImageOriginalSize(imageSize);
        }
        int availableImageWidth = maxImageWidth;
        int availableImageHeight = (int) (ScreenUtils.getAppScreenHeight() * 0.33334f);
        Point imageViewDimensions = ImageInfoParser.calculateImageViewDimensions(imageSize.x, imageSize.y, availableImageWidth,availableImageHeight, MINI_IMAGE_WIDTH, MINI_IMAGE_HEIGHT);
//        if(AppManager.debuggable()){
//            LogUtil.d("ChatAdapter#Size", "rawWidth:" + imageSize.x +" rawHeight:" + imageSize.y + "  maxWidth: " + availableImageWidth + " miniWidth:" + MINI_IMAGE_WIDTH + " maxHeight: " + availableImageHeight + " miniHeight:" + MINI_IMAGE_HEIGHT + " image width:" + imageViewDimensions.x + "  height:" + imageViewDimensions.y);
//        }
        if(imageView.getWidth() != imageViewDimensions.x || imageView.getHeight() != imageViewDimensions.y){
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = imageViewDimensions.x;
            layoutParams.height = imageViewDimensions.y;
            imageView.setLayoutParams(layoutParams);
        }
        if(bean.isImageLoadFailed()){
            setImageRetryStatus(true, requestManager, imageView, progressBar, retry, bean, imageSize, imageViewDimensions);
            return;
        }
        loadImageImpl(requestManager, imageView, progressBar, retry, bean, imageSize, imageViewDimensions, false);
    }

    private  void loadImageImpl(RequestManager requestManager, ImageView imageView, ContentLoadingProgressBar progressBar, ImageView retry, ConversationMessage bean, Point imageSize, Point imageViewDimensions, boolean skipMemory) {
        int imageWith = Math.min(imageSize.x, imageViewDimensions.x);
        int imageHeight = Math.min(imageSize.y, imageViewDimensions.y);
        GlideLoader.loadChatImage(requestManager, bean, new ImageViewTarget(imageView, bean.imagePath(), "CHAT_IMAGE"){

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                progressBar.show();
                setImageRetryStatus(false, requestManager, imageView, progressBar, retry, bean, imageSize, imageViewDimensions);
                super.onLoadStarted(placeholder);
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                progressBar.hide();
                bean.setImageLoadFailed(ConversationMessage.FLAG_IMAGE_STATUS_SUCCESS);
                setImageRetryStatus(false, requestManager, imageView, progressBar, retry, bean, imageSize, imageViewDimensions);
                super.onResourceReady(resource, transition);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                progressBar.hide();
                super.onLoadCleared(placeholder);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                progressBar.hide();
                bean.setImageLoadFailed(ConversationMessage.FLAG_IMAGE_STATUS_FAILURE);
                setImageRetryStatus(true, requestManager, imageView, progressBar, retry, bean, imageSize, imageViewDimensions);
                super.onLoadFailed(errorDrawable);
            }
        }, imageWith, imageHeight, skipMemory);
    }

    private  void setImageRetryStatus(boolean retryStatus, RequestManager requestManager, ImageView imageView, ContentLoadingProgressBar progressBar, ImageView retry, ConversationMessage bean, Point imageSize, Point imageViewDimensions) {
        if(retryStatus){
            retry.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.message_image_placeholder);
            retry.setOnClickListener(v->{
                v.setVisibility(View.GONE);
                loadImageImpl(requestManager, imageView, progressBar, retry,  bean, imageSize, imageViewDimensions, true);
            });
        }else {
            retry.setVisibility(View.GONE);
            retry.setOnClickListener(null);
        }
    }


    public boolean canJumpToPosition(int absolutePosition) {
        if (absolutePosition < 0) {
            return false;
        }

        if (absolutePosition > super.getItemCount()) {
            return false;
        }

        if (!isRangeAvailable(absolutePosition - 10, absolutePosition + 5)) {
            getItem(absolutePosition);
            return false;
        }

        return true;
    }

    protected final boolean isRangeAvailable(int start, int end) {
        if (end <= start || start >= getItemCount() || end <= 0) {
            return false;
        }

        int clampedStart = Math.max(0, start);
        int clampedEnd   = Math.min(getItemCount(), end);

        for (int i = clampedStart; i < clampedEnd; i++) {
            if (super.getItem(i) == null) {
                return false;
            }
        }

        return true;
    }

}
