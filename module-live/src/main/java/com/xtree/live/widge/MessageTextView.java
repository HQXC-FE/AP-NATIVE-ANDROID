package com.xtree.live.widge;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xtree.live.R;
import com.xtree.live.message.ConversationMessage;
import com.xtree.live.message.MessageItemType;
import com.xtree.live.uitl.HtmlHelper;
import com.xtree.live.uitl.WordUtil;

public class MessageTextView extends androidx.appcompat.widget.AppCompatTextView {
    private final static int[] mDesignationBorderColors = new int[]{
            WordUtil.getColor(R.color.designation0_border_color),
            WordUtil.getColor(R.color.designation1_border_color),
            WordUtil.getColor(R.color.designation2_border_color),
            WordUtil.getColor(R.color.designation3_border_color),
            WordUtil.getColor(R.color.designation4_border_color),
            WordUtil.getColor(R.color.designation5_border_color),
            WordUtil.getColor(R.color.designation6_border_color),
            WordUtil.getColor(R.color.designation7_border_color),
            WordUtil.getColor(R.color.designation8_border_color),
            WordUtil.getColor(R.color.designation9_border_color),

    };

    private final static int[] mDesignationColors = new int[]{
            WordUtil.getColor(R.color.designation0_bg_color),
            WordUtil.getColor(R.color.designation1_bg_color),
            WordUtil.getColor(R.color.designation2_bg_color),
            WordUtil.getColor(R.color.designation3_bg_color),
            WordUtil.getColor(R.color.designation4_bg_color),
            WordUtil.getColor(R.color.designation5_bg_color),
            WordUtil.getColor(R.color.designation6_bg_color),
            WordUtil.getColor(R.color.designation7_bg_color),
            WordUtil.getColor(R.color.designation8_bg_color),
            WordUtil.getColor(R.color.designation9_bg_color),

    };


    private final static int[] mDesignationTextColors = new int[]{
            WordUtil.getColor(R.color.designation0_text_color),
            WordUtil.getColor(R.color.designation1_text_color),
            WordUtil.getColor(R.color.designation2_text_color),
            WordUtil.getColor(R.color.designation3_text_color),
            WordUtil.getColor(R.color.designation4_text_color),
            WordUtil.getColor(R.color.designation5_text_color),
            WordUtil.getColor(R.color.designation6_text_color),
            WordUtil.getColor(R.color.designation7_text_color),
            WordUtil.getColor(R.color.designation8_text_color),
            WordUtil.getColor(R.color.designation9_text_color),
    };

    private final static int[] mLevelBgColors = new int[]{
            WordUtil.getColor(R.color.level1_bg_color),
            WordUtil.getColor(R.color.level1_bg_color),
            WordUtil.getColor(R.color.level2_bg_color),
            WordUtil.getColor(R.color.level3_bg_color),
            WordUtil.getColor(R.color.level4_bg_color),
            WordUtil.getColor(R.color.level5_bg_color),
            WordUtil.getColor(R.color.level6_bg_color),
            WordUtil.getColor(R.color.level7_bg_color),
            WordUtil.getColor(R.color.level8_bg_color),
            WordUtil.getColor(R.color.level9_bg_color),
            WordUtil.getColor(R.color.level10_bg_color),
    };

    private final static int[] mLevelTextColors = new int[]{
            WordUtil.getColor(R.color.level1_text_color),
            WordUtil.getColor(R.color.level1_text_color),
            WordUtil.getColor(R.color.level2_text_color),
            WordUtil.getColor(R.color.level3_text_color),
            WordUtil.getColor(R.color.level4_text_color),
            WordUtil.getColor(R.color.level5_text_color),
            WordUtil.getColor(R.color.level6_text_color),
            WordUtil.getColor(R.color.level7_text_color),
            WordUtil.getColor(R.color.level8_text_color),
            WordUtil.getColor(R.color.level9_text_color),
            WordUtil.getColor(R.color.level10_text_color),
    };

    int[] mLevelDrawables = new int[]{
            R.mipmap.ic_level_1,
            R.mipmap.ic_level_1,
            R.mipmap.ic_level_2,
            R.mipmap.ic_level_3,
            R.mipmap.ic_level_4,
            R.mipmap.ic_level_5,
            R.mipmap.ic_level_6,
            R.mipmap.ic_level_7,
            R.mipmap.ic_level_8,
            R.mipmap.ic_level_9,
            R.mipmap.ic_level_10,
    };

    int[] mDesignationDrawables = new int[]{
            R.mipmap.ic_designaton_1,
            R.mipmap.ic_designaton_1,
            R.mipmap.ic_designaton_2,
            R.mipmap.ic_designaton_3,
            R.mipmap.ic_designaton_4,
            R.mipmap.ic_designaton_5,
            R.mipmap.ic_designaton_6,
            R.mipmap.ic_designaton_7,
            R.mipmap.ic_designaton_8,
            R.mipmap.ic_designaton_9,
    };

    private Context mContext;
    public MessageTextView(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public MessageTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public MessageTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }


    public void setMessage(ConversationMessage message) {
        setOnLongClickListener(null);
        CharSequence nickName = message.getNickname();

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (message.isEnterRoomMessage()) {
            ssb.append(message.getWelcomeText());
            ssb.append(getSpace(ConvertUtils.dp2px(4)));
            if(!message.isMessageFromTourist()){
                SpannableString titleSpan = getTitleSpan(message);
                ssb.append(titleSpan);
                ssb.append(getSpace(ConvertUtils.dp2px(4)));
            }
            appendUserName(nickName, ssb);
            ssb.append(getSpace(ConvertUtils.dp2px(4)));
            ssb.append(ConversationMessage.ENTER_ROOM);
            setText(ssb);
            setTextColor(ConversationMessage.TEXT_COLOR_TERTIARY);
        } else if (message.isThanksForGift()) {
            ssb.append(ConversationMessage.CHAT_ROOM_THANKS);
            ssb.append(getSpace(ConvertUtils.dp2px(4)));
            appendUserName(nickName, ssb);
            ssb.append(getSpace(ConvertUtils.dp2px(4)));
            ssb.append(ConversationMessage.CHAT_ROOM_GIFTED);
            ssb.append(message.getGiftName());
            setText(ssb);
            setTextColor(ConversationMessage.TEXT_COLOR_TERTIARY);
        } else if(message.isShowUsername()){
            if (MessageItemType.MSG_GLOBAL_TEXT == message.getItemType() ||
                    MessageItemType.MSG_GLOBAL_BANNER == message.getItemType() ||
                    MessageItemType.MSG_GLOBAL_IMG == message.getItemType()) {
                if(!message.isMessageFromTourist()){
                    SpannableString titleSpan = getTitleSpan(message);
                    ssb.append(titleSpan);
                    ssb.append(getSpace(ConvertUtils.dp2px(4)));
                }
            }

            if(MessageItemType.MSG_GLOBAL_TEXT == message.getItemType() || MessageItemType.MSG_GLOBAL_IMG == message.getItemType()){
                if (!TextUtils.isEmpty(nickName))nickName += "：";
            }

            if(!TextUtils.isEmpty(nickName)){
                appendUserName(nickName, ssb);
            }

            if(MessageItemType.MSG_GLOBAL_TEXT == message.getItemType() || MessageItemType.MSG_GLOBAL_IMG == message.getItemType()){
                String messageStr = message.getOriginalTextMessage();
                if(message.needBreakLine()){
                    ssb.append("\n");
                }
                if(!TextUtils.isEmpty(messageStr)){
                    ssb.append(HtmlHelper.applyHtmlStr(messageStr, this));
                    setOnLongClickListener(view -> {
                        copy(messageStr);
                        return true;
                    });
                }
//                setTextAppearance(R.style.ChatItem_UserName);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 对于 Android 6.0 (API 23) 及更高版本
                    setTextAppearance(R.style.ChatItem_UserName);
                } else {
                    // 对于 Android 6.0 以下版本
                    setTextAppearance(mContext, R.style.ChatItem_UserName);
                }
                setAutoLinkMask(Linkify.WEB_URLS);
                setLinkTextColor(ConversationMessage.TEXT_COLOR_CHAT_LINK);

                setTextColor(ConversationMessage.TEXT_COLOR_SECONDARY);
            }else {
//                setTextAppearance(R.style.ChatItem_UserName2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 对于 Android 6.0 (API 23) 及更高版本
                    setTextAppearance(R.style.ChatItem_UserName2);
                } else {
                    // 对于 Android 6.0 以下版本
                    setTextAppearance(mContext, R.style.ChatItem_UserName2);
                }

                setTextColor(ConversationMessage.TEXT_COLOR_SECONDARY);
            }
            setText(ssb);
        }else {
            setVisibility(View.GONE);
        }
    }

    private static void appendUserName(CharSequence nickName, SpannableStringBuilder ssb) {
        if(TextUtils.isEmpty(nickName))nickName = "  ";
        ssb.append(nickName);
        ssb.setSpan(new ForegroundColorSpan(ConversationMessage.CHAT_USER_COLOR), ssb.length() - nickName.length(), ssb.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    @NonNull
    private static SpannableString getSpace(int width) {
        SpaceSpan span = new SpaceSpan(width);
        String spaceStr = " ";
        SpannableString ss = new SpannableString(spaceStr);
        ss.setSpan(span, 0, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void copy(String message) {
        try {
            if (!TextUtils.isEmpty(message)) {
                ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText("message", message));

                ToastUtils.showShort("复制成功");
            }
        } catch (Exception e) {
        }
    }

    @NonNull
    private SpannableString getUsernameSpan(CharSequence username) {
        SpannableString usernameSpan = new SpannableString(username);
        TagSpan span = new TagSpan();
        span.paddingL = getPaddingLeft();
        span.paddingR = getPaddingRight();
        span.paddingT = getPaddingTop();
        span.paddingB = getPaddingBottom();
        span.textColor = ConversationMessage.CHAT_USER_COLOR;
        usernameSpan.setSpan(span, 0, username.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return usernameSpan;
    }

    private final int DESIGNATION_ANCHOR_TEXT_COLOR = WordUtil.getColor(R.color.designation_anchor_text_color);
    private final int DESIGNATION_ANCHOR_BORDER_COLOR = WordUtil.getColor(R.color.designation_anchor_border_color);
    private final int DESIGNATION_ANCHOR_BG_COLOR = WordUtil.getColor(R.color.designation_anchor_bg_color);
    private SpannableString getTitleSpan(ConversationMessage message) {
        TagSpan tag = new TagSpan();
        tag.paddingL = ConvertUtils.dp2px(6);
        tag.paddingR = ConvertUtils.dp2px(6);

        if (message.isFromAnchor()) {
            tag.iconRes = R.mipmap.ic_level_anchor;
            tag.textColor = DESIGNATION_ANCHOR_TEXT_COLOR;
            tag.borderColor = DESIGNATION_ANCHOR_BORDER_COLOR;
            tag.bgColor = DESIGNATION_ANCHOR_BG_COLOR;
            String text = ConversationMessage.LIVE_ANCHOR;
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(tag, 0, text.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
            return spannableString;
        } else if (!TextUtils.isEmpty(message.getFromRecipient().getDesignation())) {
            tag.iconRes = mDesignationDrawables[Math.min(Math.max(0, message.getFromRecipient().getDesignationColor()), mDesignationDrawables.length - 1)];
            tag.textColor = mDesignationTextColors[Math.min(Math.max(0, message.getFromRecipient().getDesignationColor()), mDesignationTextColors.length - 1)];
            tag.borderColor = mDesignationBorderColors[Math.min(Math.max(0, message.getFromRecipient().getDesignationColor()), mDesignationBorderColors.length - 1)];
            tag.bgColor = mDesignationColors[Math.min(Math.max(0, message.getFromRecipient().getDesignationColor()), mDesignationColors.length - 1)];
            String text = message.getFromRecipient().getDesignation();
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(tag, 0, text.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
            return spannableString;
        } else {
            int levelInt = message.getFromRecipient().getLevel();
            if (levelInt == 0) levelInt = 1;
            String text = "Lv." + levelInt;
            tag.iconRes = mLevelDrawables[Math.min(Math.max(0, levelInt), mLevelDrawables.length - 1)];
            tag.textColor = mLevelTextColors[Math.min(Math.max(0, levelInt), mLevelTextColors.length - 1)];
            tag.bgColor = mLevelBgColors[Math.min(Math.max(0, levelInt), mLevelBgColors.length - 1)];
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(tag, 0, text.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    }

    public @ColorInt int getTitleBackgroundColor(ConversationMessage message){
        if (message.isFromAnchor()) {
            return DESIGNATION_ANCHOR_BG_COLOR;
        } else if (!TextUtils.isEmpty(message.getFromRecipient().getDesignation())) {
            return mDesignationColors[Math.min(Math.max(0, message.getFromRecipient().getDesignationColor()), mDesignationColors.length - 1)];
        } else {
            int levelInt = message.getFromRecipient().getLevel();
            if (levelInt == 0) levelInt = 1;
            return mLevelBgColors[Math.min(Math.max(0, levelInt), mLevelBgColors.length - 1)];
        }
    }
}

