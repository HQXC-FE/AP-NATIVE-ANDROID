package com.xtree.service.popwindows;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lxj.xpopup.core.PositionPopupView;
import com.lxj.xpopup.enums.DragOrientation;

import project.tqyb.com.library_service.R;

public class ToastPopup extends PositionPopupView {
    public OnClickListener clickCallback;
    private String content;

    public ToastPopup(@NonNull Context context, @NonNull String content, @Nullable OnClickListener clickCallback) {
        super(context);
        this.content = content;
        this.clickCallback = clickCallback;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_toast_msg;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tvContent = findViewById(R.id.tvContent);
        if (clickCallback != null) {
            tvContent.setOnClickListener(clickCallback);
        }
        String txt = content.replace("<\\ span><\\ div><br\\/>", "<\\ div>");
        txt = txt.replace("<br/>", "");
        tvContent.setText(txt);
    }

    @Override
    protected DragOrientation getDragOrientation() {
        return DragOrientation.DragToLeft;
    }
}

