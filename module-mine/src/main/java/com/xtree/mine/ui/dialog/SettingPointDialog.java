package com.xtree.mine.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.ListDialog;
import com.xtree.mine.R;
import com.xtree.mine.vo.MemberUserInfoVo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

public class SettingPointDialog extends BottomPopupView {
    MemberUserInfoVo memberUserInfoVo;
    int maxHeight = 70; // 最大高度百分比 10-100
    double maxPoint;
    List<String> stringList;
    ProfileVo mProfileVo;
    DecimalFormat df = new DecimalFormat("#.#");

    ImageView ivwClose;
    TextView tvwUserAccount;
    TextView tvwUserPoint;
    TextView tvwUserSetPoint;
    TextView tvwUserPointWarning;
    Button btnConfirm;
    BasePopupView ppw = null;
    Context context;
    ICallback callback;

    ItemTextBinding binding2;

    public interface ICallback {
        void onClick(String point);
    }

    public SettingPointDialog(@NonNull Context context) {
        super(context);
    }

    public SettingPointDialog(@NonNull Context context, MemberUserInfoVo memberUserInfoVo, ICallback callback) {
        super(context);
        this.context = context;
        this.memberUserInfoVo = memberUserInfoVo;
        this.callback = callback;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
        initView();
    }

    private void initView() {
        ivwClose = findViewById(R.id.ivw_close);
        tvwUserAccount = findViewById(R.id.tvw_user_account);
        tvwUserPoint = findViewById(R.id.tvw_user_point);
        tvwUserSetPoint = findViewById(R.id.tvw_user_set_point);
        tvwUserPointWarning = findViewById(R.id.tvw_user_point_warning);
        btnConfirm = findViewById(R.id.btn_confirm);

        ivwClose.setOnClickListener(v -> dismiss());
        tvwUserAccount.setText(memberUserInfoVo.username);
        tvwUserPoint.setText(df.format(Double.parseDouble(memberUserInfoVo.userpoint) * 100) + "%");
        tvwUserSetPoint.setText(df.format(Double.parseDouble(memberUserInfoVo.userpoint) * 100) + "%");

        stringList = new ArrayList<>();

        maxPoint = Double.parseDouble(mProfileVo.rebate_percentage.replace("%", ""));

        for (double i = maxPoint; i > 0; i = i - 0.1) {
            stringList.add(df.format(i) + "%");
        }

        /*stringList.add("0");*/

        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<String>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                String txt = get(position);
                binding2.tvwTitle.setText(txt);

                binding2.tvwTitle.setOnClickListener(v -> {
                    tvwUserSetPoint.setText(txt);
                    String warning = String.format(context.getString(R.string.txt_lottery_point_warning), df.format(maxPoint - Double.parseDouble(txt.replace("%", ""))) + "%");
                    tvwUserPointWarning.setText(warning);
                    ppw.dismiss();
                });

            }
        };
        adapter.addAll(stringList);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), "", adapter));
        tvwUserSetPoint.setOnClickListener(v -> ppw.show());

        String warning = String.format(context.getString(R.string.txt_lottery_point_warning), df.format(maxPoint - (Double.parseDouble(memberUserInfoVo.userpoint) * 100)) + "%");
        tvwUserPointWarning.setText(warning);

        btnConfirm.setOnClickListener(v -> {
            callback.onClick(tvwUserSetPoint.getText().toString().replace("%", ""));
            dismiss();
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_set_point;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        if (maxHeight < 5 || maxHeight > 100) {
            maxHeight = 40;
        }
        return (XPopupUtils.getScreenHeight(getContext()) * maxHeight / 100);
    }

}
