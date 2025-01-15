package com.xtree.lottery.ui.view;

import static com.xtree.lottery.ui.adapter.CpReportAdapterKt.getLotteryStatus;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.lottery.R;
import com.xtree.lottery.data.Injection;
import com.xtree.lottery.data.source.vo.LotteryOrderVo;
import com.xtree.lottery.databinding.DialogLotteryBtCpDetailBinding;
import com.xtree.lottery.ui.viewmodel.LotteryViewModel;

import me.xtree.mvvmhabit.utils.Utils;

public class BtCpDetailDialog extends BottomPopupView {

    //private Context ctx;
    LifecycleOwner owner;
    //ReportViewModel viewModel;
    DialogLotteryBtCpDetailBinding binding;
    private String id; // D20240203-192***CED
    private LotteryViewModel viewModel;

    private BtCpDetailDialog(@NonNull Context context) {
        super(context);
    }

    public static BtCpDetailDialog newInstance(Context ctx, LifecycleOwner owner, String id) {
        BtCpDetailDialog dialog = new BtCpDetailDialog(ctx);
        //dialog.ctx = ctx;
        dialog.owner = owner;
        dialog.id = id;

        return dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
        initData();
        initViewObservable();

        LoadingDialog.show(getContext()); // loading
        requestData();
    }

    private void initView() {
        binding = DialogLotteryBtCpDetailBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
    }

    private void initData() {
        viewModel = new LotteryViewModel((Application) Utils.getContext(), Injection.provideMainRepository(false));
    }

    private void initViewObservable() {
        viewModel.liveDataBtCpDetail.observe(owner, vo -> {
            CfLog.i();
            setView(vo);
        });
    }

    private void setView(LotteryOrderVo t) {
        LotteryOrderVo vo = t.project;

        binding.tvwWriteTime.setText(vo.writetime);
        binding.tvwProjectId.setText(t.id); //
        binding.tvwUsername.setText(t.username); //
        binding.tvwLotteryName.setText(t.lottery); //

        binding.tvwIssue.setText(vo.issue);
        binding.tvwStatus.setText(getLotteryStatus(getContext(), vo));
        binding.tvwMethodName.setText(t.method); //
        binding.tvwNoCode.setText(t.nocode); //

        binding.tvwTotalPrice.setText(vo.totalprice);
        binding.tvwBonus.setText(vo.bonus);

        binding.tvwMultiple.setText(vo.multiple);
        binding.tvwModes.setText(vo.modes);
        binding.tvwCode.setText(vo.code);

    }

    private void requestData() {
        viewModel.getBtCpOrderDetail(id); // D20240203-192***CED
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_lottery_bt_cp_detail;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 80 / 100);
    }

}
