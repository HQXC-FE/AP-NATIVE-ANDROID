package com.xtree.live.message;

import static com.xtree.live.uitl.MessageUtils.completeImagePath;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xtree.live.LiveConfig;
import com.xtree.live.inter.MessageConstant;
import com.xtree.live.socket.BNC;
import com.xtree.live.uitl.HtmlImageGetter;
import com.xtree.live.uitl.TimeUtil;

import java.util.Objects;

public class ConversationMessage implements MultiItemEntity, MessageConstant, BNC {


    private MessageRecord messageRecord;

    private Recipient fromRecipient;
    private long dateTimeSent;
    private long dateTimeReceived;
    private int itemType;
    private int readReceiptCount;
    private int deliveryStatus;

    /**
     * 显示的时间
     */
    private String sendTime;
    private String sendDate;
    private String sendDateTime;

    private boolean isSentByMe;

    private int uid;

    private boolean needBreakLine;

    // 0初始状态 1 加载成功 loading 2 加载失败 3

    public final static int FLAG_IMAGE_STATUS_INIT = 0;
    public final static int FLAG_IMAGE_STATUS_SUCCESS = 1;
    public final static int FLAG_IMAGE_STATUS_LOADING = 2;
    public final static int FLAG_IMAGE_STATUS_FAILURE = 3;
    private int imageLoadStatus;
    private CharSequence welcomeText;
    private final static String CONTAINS_URL = "[\\s\\S]*(http|https)://[\\s\\S]*";

    public ConversationMessage(MessageRecord messageRecord) {
        this(messageRecord, 0);
    }

    public ConversationMessage(MessageRecord messageRecord, int uid) {
        this.uid = uid;
        setMessageRecord(messageRecord);
    }

    public void setMessageRecord(MessageRecord messageRecord) {
        this.messageRecord = messageRecord;
//        if(AppManager.debuggable()){
//            LogUtil.d("ConversationMessage", "userId:" + AppConfig.getUserId());
//            LogUtil.d("ConversationMessage", "sender:" + messageRecord.getSender());
//        }
        isSentByMe = Objects.equals(LiveConfig.getUserId(), messageRecord.getSender());
        setTime(messageRecord.getTime());

        this.fromRecipient = new Recipient(messageRecord);
        this.itemType = readMsgType(messageRecord.getType(), messageRecord.getMsgType());
        if (this.itemType == MessageItemType.MSG_GLOBAL_TEXT) {
            this.needBreakLine = !TextUtils.isEmpty(messageRecord.getText()) &&
                    !(messageRecord.getText().startsWith("\r\n") || messageRecord.getText().startsWith("\n\r") || messageRecord.getText().startsWith("\n") || messageRecord.getText().startsWith("\r")) &&
                    messageRecord.getText().matches(CONTAINS_URL);
        }
    }

    public void setTime(String time) {
        sendDateTime = time;
        long timestamp = TimeUtil.getDateToTimestamp(time);
        dateTimeSent = timestamp;
        dateTimeReceived = timestamp;

        if (timestamp != 0) {
            sendDate = TimeUtil.getMonthAndDayFromTimestamp(timestamp);
            sendTime = TimeUtil.getHourAndMinuteFromTimestamp(timestamp);
        } else {
            sendDate = "";
            sendTime = "";
        }
    }

    public MessageRecord getMessageRecord() {
        return messageRecord;
    }

    /**
     * 和上一条属于同一个用户发送
     */
    private boolean commonSenderWithLast;
    /**
     * 和下一条属于同一个用户发送
     */
    private boolean commonSenderWithNext;

    public boolean isCommonSenderWithLast() {
        return commonSenderWithLast;
    }

    public void setCommonSenderWithLast(boolean commonSenderWithLast) {
        this.commonSenderWithLast = commonSenderWithLast;
    }

    public boolean isCommonSenderWithNext() {
        return commonSenderWithNext;
    }

    public void setCommonSenderWithNext(boolean commonSenderWithNext) {
        this.commonSenderWithNext = commonSenderWithNext;
    }


    public void setDeliveryStatus(@DeliverStatus int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public @DeliverStatus int getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getSendDateTime() {
        return sendDateTime;
    }

    @Override
    public int getItemType() {
        return this.itemType;
    }

    public boolean isShowUsername() {
        if (RoomType.PAGE_CHAT_PRIVATE == messageRecord.getType() || RoomType.PAGE_CHAT_PRIVATE_ANCHOR == messageRecord.getType()) return false;
        return true;
    }

    public boolean isDelivered() {
        return deliveryStatus == DeliverStatus.STATUS_COMPLETE;
    }

    public boolean isFailed() {
        return deliveryStatus == DeliverStatus.STATUS_FAILED;
    }

    public boolean isRetrying() {
        return deliveryStatus == DeliverStatus.STATUS_RETRYING;
    }

    public boolean isPending() {
        return deliveryStatus == DeliverStatus.STATUS_PENDING;
    }


    public boolean isRemoteRead() {
        return readReceiptCount > 0;
    }

    public @NonNull CharSequence getNickname() {
        return messageRecord.getSenderNickname();
    }

    public boolean isEnterRoomMessage() {
        return MessageActionType.ACTION_SYSTEM.equals(messageRecord.getAction()) && MessageItemType.MSG_GLOBAL_TEXT == getItemType() && messageRecord.getText().contains(ENTER_ROOM);
    }

    public boolean isThanksForGift() {
        return MessageItemType.MSG_GLOBAL_GIFT == getItemType();
    }


    public boolean isSameMessage(ConversationMessage message) {
        return messageRecord.equals(message.messageRecord);
    }


    public boolean areItemsTheSame(ConversationMessage message) {
        return messageRecord.equals(message.messageRecord);
    }

    public boolean areContentsTheSame(ConversationMessage message) {
        return deliveryStatus == message.deliveryStatus &&
                Objects.equals(sendTime, message.sendTime) &&
                Objects.equals(messageRecord.getText(), message.messageRecord.getText()) &&
                Objects.equals(messageRecord.imagePath(), message.messageRecord.imagePath()) &&
                Objects.equals(messageRecord.getAvatar(), message.messageRecord.getAvatar()) &&
                Objects.equals(messageRecord.getTitle(), message.messageRecord.getTitle()) &&
                Objects.equals(messageRecord.getLink(), message.messageRecord.getLink()) &&
                Objects.equals(messageRecord.getSenderNickname(), message.messageRecord.getSenderNickname());
    }


    public int readMsgType(@RoomType int roomType, int msgType) {
        if (roomType == RoomType.PAGE_CHAT_GLOBAL) {
            if (msgType == -1) return MessageItemType.MSG_GLOBAL_GIFT;
            if (1 == msgType || 0 == msgType) return MessageItemType.MSG_GLOBAL_TEXT;
            if (2 == msgType || 5 == msgType) return MessageItemType.MSG_GLOBAL_IMG;
            if (3 == msgType || 6 == msgType) return MessageItemType.MSG_GLOBAL_BANNER;
            return MessageItemType.MSG_GLOBAL_TEXT;
        }
        switch (msgType) {
            case 0:
            case 1:
                return isSentByMe ? MessageItemType.MSG_OUTGOING_TEXT : MessageItemType.MSG_INCOMING_TEXT;
            case 5:
            case 2:
                return isSentByMe ? MessageItemType.MSG_OUTGOING_IMG : MessageItemType.MSG_INCOMING_IMG;
            case 3:
                return MessageItemType.MSG_INCOMING_BANNER;
            case 6:
                return MessageItemType.MSG_GLOBAL_BANNER;
            default:
                return MessageItemType.MSG_INCOMING_TEXT;
        }
    }

    public boolean isSameMessageWithDelete(MessageDelete messageDelete) {
        return (!TextUtils.isEmpty(messageRecord.getMsgId()) && Objects.equals(messageRecord.getMsgId(), messageDelete.getMsgId())) ||
                (!TextUtils.isEmpty(messageRecord.getSeed()) && Objects.equals(messageRecord.getSeed(), messageDelete.getMsgSeed()));
    }

    public @NonNull Recipient getFromRecipient() {
        return fromRecipient;
    }

    public Spanned convertBannerTitle(TextView textView) {
        return convertHtmlStr(messageRecord.getTitle(), textView);
    }

    public Spanned convertBannerContent(TextView textView) {
        return convertHtmlStr(messageRecord.getText(), textView);
    }

    public Spanned convertTextMessage(TextView textView) {
        return convertHtmlStr(messageRecord.getText(), textView);
    }


    public Spanned convertHtmlStr(String s, TextView text) {
        String formattedHtml = s.replace(" ", "&nbsp;")
                .replace("\r\n", "<br />")
                .replace("\r", "<br />")
                .replace("\n", "<br />")
                .replace("<img&nbsp;src=", "<img src=");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Android N (API 24)及以上版本
            return Html.fromHtml(formattedHtml, Html.FROM_HTML_MODE_LEGACY,
                    new HtmlImageGetter(text), null);
        } else {
            // Android N之前的版本
            return Html.fromHtml(formattedHtml, new HtmlImageGetter(text), null);
        }
    }

    public @Nullable Intent bannerJumpIntent(Context context) {
        if (TextUtils.isEmpty(messageRecord.getLink())) return null;
        return new Intent(Intent.ACTION_VIEW, Uri.parse(messageRecord.getLink()));
    }

    public String getBannerImage() {
        return completeImagePath(messageRecord.imagePath());
    }


    public String getOriginalTextMessage() {
        return TextUtils.isEmpty(messageRecord.getText()) ? "" : messageRecord.getText();
    }

    public boolean needBreakLine() {
        return needBreakLine;
    }


    @NonNull
    @Override
    public String imagePath() {
        return messageRecord.imagePath();
    }

    @NonNull
    @Override
    public String objectKey() {
        return messageRecord.objectKey();
    }

    public boolean isFromAnchor() {
        return Objects.equals("" + uid, fromRecipient.getId());
    }

    public boolean isSameMessage(MessageRecord messageRecord) {
        return this.messageRecord.equals(messageRecord);
    }

    public long getDateTimeSent() {
        return dateTimeSent;
    }


    public boolean isSameSender(ConversationMessage message) {
        return !TextUtils.isEmpty(fromRecipient.getId()) && Objects.equals(fromRecipient.getId(), message.fromRecipient.getId());
    }

    public boolean isSentByMe() {
        return isSentByMe;
    }

    public String getSendDate() {
        return sendDate;
    }

    public String getSendTime() {
        return sendTime;
    }


    public boolean isGlobalAdMessage(@RoomType int roomType) {
        return roomType == RoomType.PAGE_CHAT_GLOBAL && 3 == messageRecord.getMsgType();
    }


    public boolean isBelong2Room(String vid) {
        return !TextUtils.isEmpty(vid) && Objects.equals(vid, messageRecord.getVid());
    }

    public String getGiftName() {
        return messageRecord.getText();
    }

    public boolean isMessageFromTourist(){
        return fromRecipient.getUserType() == UserType.TOURIST;
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConversationMessage conversation = (ConversationMessage) obj;
        return messageRecord.equals(conversation.messageRecord);
    }

    public boolean isSameSender(String senderId){
        return Objects.equals(fromRecipient.getId(), senderId);
    }

    private Point imageOriginalSize;

    public Point getImageOriginalSize() {
        return imageOriginalSize;
    }

    public void setImageOriginalSize(Point imageOriginalSize) {
        this.imageOriginalSize = imageOriginalSize;
    }

    public CharSequence getWelcomeText(){
        return this.welcomeText == null ? CHAT_ROOM_WELCOME : this.welcomeText;
    }

    public void setWelcomeText(String welcomeText){
        this.welcomeText = welcomeText;
    }

    public int getImageLoadStatus() {
        return imageLoadStatus;
    }

    public void setImageLoadFailed(int imageLoadFailed) {
        this.imageLoadStatus = imageLoadFailed;
    }

    public boolean isImageLoadFailed() {
        return imageLoadStatus == FLAG_IMAGE_STATUS_FAILURE;
    }
}
