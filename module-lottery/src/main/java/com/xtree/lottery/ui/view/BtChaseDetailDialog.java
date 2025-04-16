package com.xtree.lottery.ui.view;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.lottery.R;
import com.xtree.lottery.data.Injection;
import com.xtree.lottery.data.source.vo.LotteryChaseDetailVo;
import com.xtree.lottery.data.source.vo.Task;
import com.xtree.lottery.databinding.DialogBtChaseDetailBinding;
import com.xtree.lottery.ui.adapter.ChasingDetailAdapter;
import com.xtree.lottery.ui.viewmodel.LotteryViewModel;

import me.xtree.mvvmhabit.utils.Utils;

public class BtChaseDetailDialog extends BottomPopupView {

    //private Context ctx;
    LifecycleOwner owner;
    private String id; // D20240203-192***CED

    //ReportViewModel viewModel;
    DialogBtChaseDetailBinding binding;
    private LotteryViewModel viewModel;

    private BtChaseDetailDialog(@NonNull Context context) {
        super(context);
    }

    public static BtChaseDetailDialog newInstance(Context ctx, LifecycleOwner owner, String id) {
        BtChaseDetailDialog dialog = new BtChaseDetailDialog(ctx);
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
        binding = DialogBtChaseDetailBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
    }

    private void initData() {
        viewModel = new LotteryViewModel((Application) Utils.getContext(), Injection.provideMainRepository(false));
    }

    private void initViewObservable() {
        viewModel.liveDataBtChaseDetail.observe(owner, vo -> {
            CfLog.i();
            setView(vo);
        });
    }

    private void setView(LotteryChaseDetailVo t) {
        Task vo = t.getTask();

        binding.tvwProjectId.setText(vo.getTaskid());
        binding.tvwWriteTime.setText(vo.getBegintime());
        binding.tvStartIssue.setText(vo.getBeginissue()); //
        binding.tvwUsername.setText(vo.getUsername()); //
        binding.tvwLotteryName.setText(vo.getCnname()); //
        binding.tvwMethodName.setText(vo.getMethodname()); //玩法
        binding.tvwModes.setText(vo.getModes()); //模式

        binding.tvChaseIssue.setText(vo.getIssuecount());
        binding.tvCompleteIssue.setText(vo.getFinishedcount());
        binding.tvCancelIssue.setText(vo.getCancelcount());
        binding.tvFinishPrice.setText(vo.getFinishprice());
        binding.tvCancelPrice.setText(vo.getCancelprice());
        String task;
        if (TextUtils.equals("0", vo.getStoponwin())) {
            task = "否";
        } else {
            task = "是";
        }
        binding.tvwTask.setText(task);

        String status;
        if (TextUtils.equals("0", vo.getStatus())) {
            status = "进行中";
        } else if (TextUtils.equals("1", vo.getStatus())) {
            status = "已取消";
        } else if (TextUtils.equals("2", vo.getStatus())) {
            status = "已完成";
        } else {
            status = "";
        }
        binding.tvwStatus.setText(status);
        binding.tvwCode.setText(vo.getCodes());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false; // 禁止垂直滚动
            }
        };

        binding.rvChase.setLayoutManager(layoutManager);
        binding.rvChase.setAdapter(new ChasingDetailAdapter(t.getATaskdetail(), owner));
    }

    private void requestData() {
        viewModel.getBtChaseDetailDetail(id); // D20240203-192***CED
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bt_chase_detail;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 80 / 100);
    }

}
