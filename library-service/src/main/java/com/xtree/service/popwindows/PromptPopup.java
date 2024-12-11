package com.xtree.service.popwindows;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.core.PositionPopupView;
import com.lxj.xpopup.enums.DragOrientation;

import project.tqyb.com.library_service.R;

public class PromptPopup extends BottomPopupView {
    public OnClickListener cancelClick;
    public OnClickListener confirmClick;
    private String content;

    public PromptPopup(@NonNull Context context, @NonNull String content, @Nullable OnClickListener cancelClick, @Nullable OnClickListener confirmClick) {
        super(context);
        this.content = content;
        this.cancelClick = cancelClick;
        this.confirmClick = confirmClick;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_prompt_msg;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tvContent = findViewById(R.id.tvContent);
        String txt = content.replace("<\\ span><\\ div><br\\/>", "<\\ div>");
        txt = txt.replace("<br/>", "");
        tvContent.setText(txt);
        findViewById(R.id.tvCancel).setOnClickListener(view -> {
            dismiss();
            if (cancelClick != null) {
                cancelClick.onClick(view);
            }
        });

        findViewById(R.id.ivClose).setOnClickListener(view -> {
            dismiss();
            if (cancelClick != null) {
                cancelClick.onClick(view);
            }
        });
        findViewById(R.id.tvConfirm).setOnClickListener(view -> {
            dismiss();
            if (confirmClick != null) {
                confirmClick.onClick(view);
            }
        });
    }
}

