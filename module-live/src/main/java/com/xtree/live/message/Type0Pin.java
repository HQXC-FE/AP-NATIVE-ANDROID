package com.xtree.live.message;

import android.graphics.Outline;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xtree.live.R;
import com.xtree.live.message.inroom.InRoomData;
import com.xtree.live.uitl.GlideLoader;
import com.xtree.live.uitl.HtmlHelper;
import com.xtree.live.uitl.JsonUtil;
import com.xtree.live.uitl.WordUtil;
import com.xtree.live.widge.PinView;

public class Type0Pin extends BasePin {
    private final int IMAGE_SIZE = ConvertUtils.dp2px(38);
    private final ViewOutlineProvider imageOutlineProvider = new ViewOutlineProvider() {
        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), ConvertUtils.dp2px(4));
        }
    };

    private final ImageView mPinImageView;
    private final TextView mPinTopTitle, mPinTopText;
    private final ImageView mExpand;

    public Type0Pin(PinView root) {
        super(root);
        mPinImageView = mInsertView.findViewById(R.id.pin_top_image);
        mPinTopTitle = mInsertView.findViewById(R.id.pin_top_title);
        mPinTopText = mInsertView.findViewById(R.id.pin_top_text);
        mExpand = mInsertView.findViewById(R.id.pin_expand);
    }

    private String mTopTextString;

    @Override
    public void setPinData(InRoomData data) {
        if (mPinView.getPin() != this) {
            mPinView.removeAllViews();
            mPinView.addView(mInsertView);
        }
        JsonElement pinObj = data.getPinObj();
        if (pinObj == null) return;
        if (!(pinObj instanceof JsonObject)) return;
        JsonObject json = (JsonObject) pinObj;
        String link = JsonUtil.getString(json, "link");
        if (TextUtils.isEmpty(link)) {
            mPinTopText.setOnClickListener(null);
        } else {
            mPinTopText.setOnClickListener(v -> {
                jumpToBrowser(getContext(), link);
            });
        }

        String text = JsonUtil.getString(json, "text");
        if (!TextUtils.isEmpty(text)) {
            if (!text.equals(mTopTextString)) {
                mTopTextString = text;
                Spanned spanned = HtmlHelper.applyHtmlStr(foldSpace(mTopTextString), mPinTopText);
                mExpand.setSelected(true);
                mPinTopText.setMaxLines(1);
                mPinTopText.setMovementMethod(null);
                mPinTopText.setText(spanned);
                mExpand.setOnClickListener(v -> {
                    if (!mExpand.isSelected()) {
                        mExpand.setSelected(true);
                        mPinTopText.setMaxLines(1);
                        mPinTopText.setMovementMethod(null);
                    } else {
                        mExpand.setSelected(false);
                        mPinTopText.setMovementMethod(new ScrollingMovementMethod());
                        mPinTopText.setMaxHeight(ScreenUtils.getAppScreenHeight() / 3);
                    }
                });
                mPinTopText.post(() -> {
                    float width = mPinTopText.getPaint().measureText(spanned, 0, spanned.length());
                    if (width > mPinTopText.getWidth()) {
                        mExpand.setVisibility(View.VISIBLE);
                    } else {
                        mExpand.setVisibility(View.GONE);
                    }
                });
            }
        } else {
            mExpand.setVisibility(View.GONE);
            mPinTopText.setText("1张图片");
        }

        String titleStr = JsonUtil.getString(json, "title");
        String title = TextUtils.isEmpty(titleStr) ? WordUtil.getString(R.string.chat_room_pin_head) : titleStr;

        if (mPinTopTitle.getText() != null || !mPinTopTitle.getText().equals(title)) {
            mPinTopTitle.setText(title);
        }
        String url = JsonUtil.getString(json, "pic");
        if (TextUtils.isEmpty(url)) {
            mPinImageView.setVisibility(View.GONE);
            mPinImageView.setOnClickListener(null);
            return;
        }
        if (url.equals(mPinImageView.getTag())) return;

        mPinImageView.setVisibility(View.VISIBLE);
        mPinImageView.setOutlineProvider(imageOutlineProvider);
        mPinImageView.setClipToOutline(true);

        mPinImageView.setTag(url);
//        mPinImageView.setOnClickListener(view -> PhotoViewActivity.forward(getContext(), new BNCObject(url,url.replace(BNC.SUFFIX, ""))));
        GlideLoader.loadPinImage(getContext(), url, mPinImageView, IMAGE_SIZE,IMAGE_SIZE);
    }

    @Override
    View inflate(ViewGroup root) {
        return LayoutInflater.from(getContext()).inflate(R.layout.view_pin_type_1, root, true);
    }
}

