package com.xtree.live.message;

import android.animation.LayoutTransition;
import android.graphics.Outline;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.xtree.live.R;
import com.xtree.live.message.inroom.InRoomData;
import com.xtree.live.message.inroom.InRoomExtra;
import com.xtree.live.message.inroom.InRoomLink;
import com.xtree.live.uitl.HtmlHelper;
import com.xtree.live.widge.MarqueeTextView;
import com.xtree.live.widge.PinView;

import java.util.List;

public class Type1Pin extends BasePin {
    private final TextView mPinLinkText1, mPinLinkText2, mPinLinkText3;
    private final TextView mPinLinkJump1, mPinLinkJump2, mPinLinkJump3;
    private final LinearLayout mPinLinkLayout2, mPinLinkLayout3;
    private final MarqueeTextView mMarqueeMessage;
    private final ImageView mExpandView;
    private final ImageView mPrettyAnchor;
    private final LinearLayout mPinBodyContainer, mPinHeaderContainer;



    private String marqueeString;
    public Type1Pin(PinView root) {
        super(root);
        mPinBodyContainer = findViewById(R.id.pin_ads_body_container);
        mPinHeaderContainer = findViewById(R.id.pin_ads_header_container);
        mPrettyAnchor  = findViewById(R.id.pin_ads_pretty_anchor);
        mPrettyAnchor.setClipToOutline(true);
        mPrettyAnchor.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), CORNER_SIZE);
            }
        });

        mPinLinkLayout2 = findViewById(R.id.pin_ad_link_layout2);
        mPinLinkLayout3 = findViewById(R.id.pin_ad_link_layout3);



        mMarqueeMessage = findViewById(R.id.pin_ads_marquee_message);
        mMarqueeMessage.setRndDuration(100);
        mMarqueeMessage.setScrollMode(MarqueeTextView.SCROLL_FOREVER);
        mMarqueeMessage.setScrollFirstDelay(200);



        mExpandView = findViewById(R.id.pin_expand);
        mExpandView.setSelected(false);
        mExpandView.setOnClickListener(view -> {
            if (!mExpandView.isSelected()) {
                mExpandView.setSelected(true);
                mPinBodyContainer.setVisibility(View.GONE);
                mPrettyAnchor.setImageDrawable(null);
                mMarqueeMessage.pauseScroll();
            } else {
                mExpandView.setSelected(false);
                mMarqueeMessage.resumeScroll();
                mPrettyAnchor.setImageResource(R.mipmap.ic_pin_anchor_avatar);
                mPinBodyContainer.setVisibility(View.VISIBLE);
                mPrettyAnchor.setVisibility(View.VISIBLE);
            }
        });
        mPinLinkText1 = findViewById(R.id.pin_ad_link_text1);
        mPinLinkText2 = findViewById(R.id.pin_ad_link_text2);
        mPinLinkText3 = findViewById(R.id.pin_ad_link_text3);

        mPinLinkJump1 = findViewById(R.id.pin_ad_link_jump1);
        mPinLinkJump2 = findViewById(R.id.pin_ad_link_jump2);
        mPinLinkJump3 = findViewById(R.id.pin_ad_link_jump3);
        mPinLinkJump1.setOnClickListener(v -> jumpToBrowser(getContext(), mPinLinkJump1.getTag()));
        mPinLinkJump2.setOnClickListener(v -> jumpToBrowser(getContext(), mPinLinkJump2.getTag()));
        mPinLinkJump3.setOnClickListener(v -> jumpToBrowser(getContext(), mPinLinkJump3.getTag()));
    }



    @Override
    public void setPinData(InRoomData data) {
        if (mPinView.getPin() != this) {
            mPinView.removeAllViews();
            mPinView.addView(mInsertView);
        }
        InRoomExtra extraBean = data.getExtra();
        String text = foldSpace(data.getPinData());
        if (!TextUtils.isEmpty(text)) {
            mMarqueeMessage.setVisibility(View.VISIBLE);
            if(!text.equals(marqueeString)){
                marqueeString = text;
                mMarqueeMessage.setText(HtmlHelper.applyHtmlStr(marqueeString, mMarqueeMessage));
                mMarqueeMessage.startScroll();
            }
        } else {
            mMarqueeMessage.stopScroll();
            mMarqueeMessage.setVisibility(View.GONE);
        }

        List<InRoomLink> beans = extraBean.getBeans();

        InRoomLink linkBean1 = null, linkBean2 = null, linkBean3 = null;
        if (beans != null && !beans.isEmpty()) {
            for (int index = 0; index < beans.size(); index++) {
                switch (index) {
                    case 0:
                        linkBean1 = beans.get(index);
                        break;
                    case 1:
                        linkBean2 = beans.get(index);
                        break;
                    case 2:
                        linkBean3 = beans.get(index);
                        break;
                }
            }
        }
        mPinLinkLayout2.setVisibility(linkBean2 != null ? View.VISIBLE : View.GONE);
        mPinLinkLayout3.setVisibility(linkBean3 != null ? View.VISIBLE : View.GONE);
        if (linkBean1 != null) {
            mPinLinkText1.setText(linkBean1.getTextLink());
            String jumpUrl = foldSpace(linkBean1.getTextLinkUrl());
            mPinLinkJump1.setText(jumpUrl);
            mPinLinkJump1.setTag(jumpUrl);
        }
        if (linkBean2 != null) {
            mPinLinkText2.setText(linkBean2.getTextLink());
            mPinLinkJump2.setTag(foldSpace(linkBean2.getTextLinkUrl()));
        }
        if (linkBean3 != null) {
            mPinLinkText3.setText(linkBean3.getTextLink());
            mPinLinkJump3.setTag(foldSpace(linkBean3.getTextLinkUrl()));
        }
        if(mPinBodyContainer.getVisibility() == View.VISIBLE){
            mPinBodyContainer.setVisibility(TextUtils.isEmpty(text) && linkBean2 == null && linkBean3 == null ? View.GONE : View.VISIBLE);
        }

        if(this.mPinView.getLayoutTransition() == null){
            this.mPinView.setLayoutTransition(new LayoutTransition());
        }
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        super.onPause(owner);
        if(mMarqueeMessage.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(mMarqueeMessage.getText())){
            //pasue 和 resume 会闪烁
//            mMarqueeMessage.pauseScroll();
        }
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        super.onResume(owner);
        if(mMarqueeMessage.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(mMarqueeMessage.getText())){
//            mMarqueeMessage.resumeScroll();
        }

    }

    @Override
    View inflate(ViewGroup root) {
        return LayoutInflater.from(root.getContext()).inflate(R.layout.view_pin_type_2, root, true);
    }
}
