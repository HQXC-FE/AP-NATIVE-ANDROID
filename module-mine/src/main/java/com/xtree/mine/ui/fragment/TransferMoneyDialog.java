package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogTransferMoneyBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

@Route(path = RouterFragmentPath.Mine.PAGER_MEMBER_TRANSFER)
public class TransferMoneyDialog extends BaseFragment<DialogTransferMoneyBinding, MineViewModel> {
    private static final String ARG_USERNAME = "userName";
    private static final String ARG_USERID = "userId";
    private static final String ARG_VERIFY = "verify";
    //LifecycleOwner owner;
    //DialogTransferMoneyBinding binding;
    //MineViewModel viewModel;
    String checkCode;
    String username;
    String userid;
    BasePopupView ppw = null;

    ItemTextBinding binding2;

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        viewModel.liveDataSendMoney.observe(this, vo -> {
            if (vo.msg_type.equals("3")) {
                viewModel.getBalance();
            }
            ToastUtils.showLong(vo.message);
            getActivity().finish();
        });

        viewModel.liveDataBalance.observe(this, vo -> {
            binding.tvwUserBalance.setText(vo.balance);
        });
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.getBalance();
    }

    @Override
    public void initView() {
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
            userid = getArguments().getString(ARG_USERID);
            checkCode = getArguments().getString(ARG_VERIFY);
        }

        binding.tvwUserAccount.setText(username);

        binding.tvwTransfer.setOnClickListener(v -> showChooseDialog());

        binding.ivwClose.setOnClickListener(v -> getActivity().finish());
        binding.btnCancel.setOnClickListener(v -> getActivity().finish());
        binding.btnConfirm.setOnClickListener(v -> {
            if (binding.etUserMoney.getText().toString().isEmpty() || Double.parseDouble(binding.etUserMoney.getText().toString()) == 0.0) {
                ToastUtils.showLong(R.string.text_null_or_zero);
                return;
            }

            String content = String.format(getContext().getString(R.string.txt_check_transfer), binding.etUserMoney.getText().toString());
            String txtRight = getContext().getString(R.string.text_confirm);
            String txtLeft = getContext().getString(R.string.text_cancel);
            ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), "", content, txtLeft, txtRight, new MsgDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    ppw.dismiss();
                }

                @Override
                public void onClickRight() {
                    checkPassword();
                    ppw.dismiss();
                }
            }));
            ppw.show();
        });
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_transfer_money;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MineViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MineViewModel.class);
    }

    private void showChooseDialog() {
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
                    binding.tvwTransfer.setText(txt);
                    ppw.dismiss();
                });
            }
        };

        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.txt_input_output_transfer));
        list.add(getString(R.string.txt_lottery_bonus));
        list.add(getString(R.string.txt_lottery_active_money));
        list.add(getString(R.string.txt_day_money));
        list.add(getString(R.string.txt_real_bonus));
        list.add(getString(R.string.txt_agent_money));
        list.add(getString(R.string.txt_third_active_money));
        adapter.addAll(list);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), "", adapter));
        ppw.show();
    }

    private void checkOrderType(HashMap<String, String> map) {
        if (binding.tvwTransfer.getText().toString().equals(getString(R.string.txt_input_output_transfer))) {
            map.put("ordertype", "1");
        } else if (binding.tvwTransfer.getText().toString().equals(getString(R.string.txt_lottery_bonus))) {
            map.put("ordertype", "42");
        } else if (binding.tvwTransfer.getText().toString().equals(getString(R.string.txt_lottery_active_money))) {
            map.put("ordertype", "52");
        } else if (binding.tvwTransfer.getText().toString().equals(getString(R.string.txt_day_money))) {
            map.put("ordertype", "72");
        } else if (binding.tvwTransfer.getText().toString().equals(getString(R.string.txt_real_bonus))) {
            map.put("ordertype", "204");
        } else if (binding.tvwTransfer.getText().toString().equals(getString(R.string.txt_agent_money))) {
            map.put("ordertype", "323");
        } else if (binding.tvwTransfer.getText().toString().equals(getString(R.string.txt_third_active_money))) {
            map.put("ordertype", "383");
        } else {
            map.put("ordertype", "1");
        }
    }

    private void checkPassword() {
        String money = binding.etUserMoney.getText().toString();

        LoadingDialog.show(getContext());

        HashMap<String, String> map = new HashMap<>();
        map.put("flag", "confirm");
        map.put("check", checkCode);
        map.put("money", money);
        map.put("uid", userid);
        map.put("nonce", UuidUtil.getID16());
        checkOrderType(map);
        viewModel.sendMoney(map);
    }
}