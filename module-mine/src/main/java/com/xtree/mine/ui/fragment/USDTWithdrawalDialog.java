package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBankWithdrawalUsdtBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.WithdrawVo.WithdrawalInfoVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalListVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalSubmitVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalVerifyVo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * USDT虚拟币提款
 */
public class USDTWithdrawalDialog extends BottomPopupView implements USDTFruitHorRecyclerViewAdapter.IUSDTFruitHorCallback {
    ChooseWithdrawViewModel viewModel;
    ItemTextBinding binding2;
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)
    private LifecycleOwner owner;
    private BasePopupView ppwError = null; // 底部弹窗 (显示错误信息)
    private
    @NonNull
    DialogBankWithdrawalUsdtBinding binding;
    private ProfileVo mProfileVo;
    private USDTFruitHorRecyclerViewAdapter recyclerViewAdapter;//顶部选项卡adapter
    private String wtype;
    private WithdrawalInfoVo.UserBankInfo selectorBankInfo;//选中的支付地址
    private ArrayList<WithdrawalInfoVo.UserBankInfo> bankInfoList;//提款地址
    private ArrayList<WithdrawalListVo> listVo;
    private WithdrawalInfoVo infoVo;
    private WithdrawalVerifyVo verifyVo;
    private WithdrawalSubmitVo submitVo;
    private WithdrawalListVo changVo;//切换的Vo
    private BasePopupView errorPopView;

    public USDTWithdrawalDialog(@NonNull Context context) {
        super(context);
    }

    public static USDTWithdrawalDialog newInstance(Context context,
                                                   LifecycleOwner owner,
                                                   final String wtype,
                                                   ArrayList<WithdrawalListVo> listVo,
                                                   final WithdrawalInfoVo infoVo) {
        USDTWithdrawalDialog dialog = new USDTWithdrawalDialog(context);
        dialog.owner = owner;
        dialog.wtype = wtype;
        dialog.listVo = listVo;
        dialog.infoVo = infoVo;
        dialog.bankInfoList = new ArrayList<>();
        for (int i = 0; i < dialog.infoVo.user_bank_info.size(); i++) {
            WithdrawalInfoVo.UserBankInfo bankInfo = dialog.infoVo.user_bank_info.get(i);
            if (!TextUtils.isEmpty(infoVo.chain) && infoVo.chain.toUpperCase().contains(bankInfo.usdt_type.toUpperCase())) {
                dialog.bankInfoList.add(bankInfo);
            }
        }
        return dialog;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bank_withdrawal_usdt;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 90 / 100);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initData();
        initView();
        hideKeyBoard();
        initViewObservable();
        /*requestData();*/

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);

    }

    private void initView() {
        binding = DialogBankWithdrawalUsdtBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.tvwTitle.setText(getContext().getString(R.string.txt_withdrawal_usdt_title));
        initNoticeView();
        //注册监听
        initListener();
        refreshTopUI(listVo);
    }

    private void initData() {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initListener() {
        hideKeyBoard();
        //提款金额输入框与提款金额显示View
        binding.etInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.tvInfoWithdrawalAmountShow.setText(s.toString());
                //换算到账个数
                //换算到账个数
                String temp = s.toString();
                if (temp != null && !TextUtils.isEmpty(temp)) {
                    Double f1 = Double.parseDouble(temp);
                    Double f2 = Double.parseDouble(infoVo.rate);
                    DecimalFormat df = new DecimalFormat("0.00");
                    //实际到账个数
                    binding.tvInfoActualNumberShow.setText(df.format(f1 / f2));
                    //提款金额
                    binding.tvInfoWithdrawalAmountShow.setText(temp);
                } else if (TextUtils.isEmpty(temp)) {
                    binding.tvInfoActualNumberShow.setText("0");
                    //提款金额
                    binding.tvInfoWithdrawalAmountShow.setText("0");
                } else {
                    binding.tvInfoActualNumberShow.setText("0");
                    //提款金额
                    binding.tvInfoWithdrawalAmountShow.setText("0");
                }
            }
        });
        //点击下一步
        binding.ivNext.setOnClickListener(v -> {
            if (binding.etInputMoney.getText().length() > 9) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (TextUtils.isEmpty(binding.etInputMoney.getText().toString())) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (Double.valueOf(binding.etInputMoney.getText().toString()) > Double.valueOf(infoVo.max_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (Double.valueOf(binding.etInputMoney.getText().toString()) < Double.valueOf(infoVo.min_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (TextUtils.isEmpty(binding.tvBindAddress.getText().toString().trim())) {
                ToastUtils.showLong(R.string.txt_select_withdrawal_address);
            } else {
                hideKeyBoard();
                String money = binding.etInputMoney.getText().toString().trim();
                String realCount = binding.tvInfoActualNumberShow.getText().toString().trim();
                requestVerify(binding.etInputMoney.getText().toString().trim(), selectorBankInfo);
            }
        });

    }

    private void initViewObservable() {
        // 验证当前渠道信息
        viewModel.verifyVoMutableLiveData.observe(owner, vo -> {
            verifyVo = vo;
            CfLog.e("verifyVoMutableLiveData=" + vo.toString());
            if (verifyVo != null) {
                refreshVerifyUI(verifyVo);
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }

        });
        // 验证当前渠道信息 错误信息
        viewModel.verifyVoErrorData.observe(owner, vo -> {
            final String message = vo;
            if (message != null && !TextUtils.isEmpty(message)) {
                showErrorDialog(message);
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });

        //提款完成申请
        viewModel.submitVoMutableLiveData.observe(owner, vo -> {
            submitVo = vo;
            if (submitVo != null) {
                refreshSubmitUI(submitVo, null);
            }

        });
        //提款完成申请 错误信息
        viewModel.submitVoErrorData.observe(owner, vo -> {
            final String message = vo;
            if (message != null && !TextUtils.isEmpty(message)) {
                refreshSubmitUI(null, message);
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });
        //获取当前渠道详情
        viewModel.withdrawalInfoVoMutableLiveData.observe(owner, vo -> {
            infoVo = vo;
          /*  CfLog.e("withdrawalInfoVoMutableLiveData=" + vo.toString());
            if (!TextUtils.isEmpty(infoVo.message) && !TextUtils.equals("success", infoVo.message)) {
                ToastUtils.showError(infoVo.message);
            } else*/
            bankInfoList.clear();
            if (infoVo != null && !infoVo.user_bank_info.isEmpty()) {
                for (int i = 0; i < infoVo.user_bank_info.size(); i++) {
                    WithdrawalInfoVo.UserBankInfo bankInfo = infoVo.user_bank_info.get(i);
                    if (!TextUtils.isEmpty(infoVo.chain) && infoVo.chain.toUpperCase().contains(bankInfo.usdt_type.toUpperCase())) {
                        bankInfoList.add(bankInfo);
                    }
                }
                //业务正常 刷新页面
                refreshChangeUI(changVo, infoVo);
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });
        //获取当前渠道详情 错误信息
        viewModel.bankInfoVoErrorData.observe(owner, vo -> {
            final String message = vo;
            if (message != null && !TextUtils.isEmpty(message)) {
                showErrorDialog(message);
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });

        //获取当前渠道详情 错误信息
        viewModel.withdrawalListErrorData.observe(owner, vo -> {
            final String message = vo;
            if (message != null && !TextUtils.isEmpty(message)) {
                showErrorDialog(message);
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
            bankInfoList.clear();
            //业务正常 刷新页面
            refreshChangeUI(changVo, infoVo);
        });

    }

    /**
     * 初始化顶部公共区域UI
     */
    private void initNoticeView() {
        //顶部公告区域
        String formatStr = getContext().getResources().getString(R.string.txt_withdraw_top_tip);
        String count, userCount, totalAmount;
        count = "<font color=#0C0319>" + infoVo.day_total_count + "</font>";
        userCount = "<font color=#F35A4E>" + infoVo.day_used_count + "</font>";
        totalAmount = "<font color=#F35A4E>" + infoVo.day_rest_amount + "</font>";
        String textTipSource = String.format(formatStr, count, userCount, totalAmount);
        binding.tvNotice.setText(HtmlCompat.fromHtml(textTipSource, HtmlCompat.FROM_HTML_MODE_LEGACY));

        //binding.tvUserNameShow.setText(cashMoYuVo.user.username);
        if (infoVo.user_bank_info != null && !infoVo.user_bank_info.isEmpty()) {
            if (infoVo.user_bank_info.get(0).user_name != null) {
                binding.tvUserNameShow.setText(infoVo.user_bank_info.get(0).user_name);
            }
        } else if (mProfileVo != null) {
            final String name = StringUtils.splitWithdrawUserName(mProfileVo.username);
            binding.tvUserNameShow.setText(name);
        }

        binding.tvWithdrawalTypeShow.setText(infoVo.user_bank_info.get(0).usdt_type);//提款类型
        String rate = infoVo.rate;//汇率
        //tv_info_exchange_rate
        binding.tvInfoExchangeRateShow.setText(rate);
        binding.tvWithdrawalAmountShow.setText(infoVo.quota);//提款余额
        String temp = infoVo.min_money + "元,最高" + infoVo.max_money + "元";
        binding.tvWithdrawalSingleShow.setText(temp); //单笔提现金额

    }

    /**
     * 依据顶部卡片刷新提币地址
     *
     * @param changVo
     * @param infoVo
     */
    private void refreshChangeUI(WithdrawalListVo changVo, WithdrawalInfoVo infoVo) {
        //根据传入列表的地址数据判断提币数组数据 TRC情况下 只显示trc地址
        if (!bankInfoList.isEmpty()) {
            String showAddress = bankInfoList.get(0).usdt_type + "--" + bankInfoList.get(0).account;
            CfLog.e("设置默认选中的提币地址=" + showAddress);
            //设置默认选中的提币地址
            selectorBankInfo = bankInfoList.get(0);
            binding.tvBindAddress.setText(showAddress);
        } else {
            selectorBankInfo = null;
            binding.tvBindAddress.setText(" ");
        }

        //刷新提款类型
        if (changVo.name.contains("提款")) {
            binding.tvWithdrawalTypeShow.setText(changVo.name);
        } else {
            binding.tvWithdrawalTypeShow.setText(changVo.name + "提款");
        }

        String rate = infoVo.rate;//汇率
        //tv_info_exchange_rate
        binding.tvInfoExchangeRateShow.setText(rate);
        binding.tvWithdrawalAmountShow.setText(infoVo.quota);//提款余额
        String temp = infoVo.min_money + "元,最高" + infoVo.max_money + "元";
        binding.tvWithdrawalSingleShow.setText(temp); //单笔提现金额

        //点击USDT收款地址
        binding.tvBindAddress.setOnClickListener(v -> {
            showCollectionDialog(bankInfoList);
        });
    }

    /**
     * 初始化选项卡
     *
     * @param listVo
     */
    private void refreshTopUI(ArrayList<WithdrawalListVo> listVo) {
        binding.tvSetWithdrawalRequest.setSelected(true);
        binding.tvConfirmWithdrawalRequest.setSelected(false);
        binding.tvOverWithdrawalRequest.setSelected(false);
        for (WithdrawalListVo vo : listVo) {
            vo.flag = false;
        }
        listVo.get(0).flag = true;
        recyclerViewAdapter = new USDTFruitHorRecyclerViewAdapter(getContext(), listVo, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvShowChooseCard.setLayoutManager(layoutManager);
        binding.rvShowChooseCard.addItemDecoration(new USDTFruitHorRecyclerViewAdapter.SpacesItemDecoration(10));
        binding.rvShowChooseCard.setAdapter(recyclerViewAdapter);
        binding.rvShowChooseCard.setItemAnimator(new DefaultItemAnimator());
        callbackWithUSDTFruitHor(listVo.get(0));

    }

    /**
     * 刷新 提款确认页面信息
     *
     * @param verifyVo
     */
    private void refreshVerifyUI(final WithdrawalVerifyVo verifyVo) {
        //刷新顶部进度条颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvSetWithdrawalRequest.setSelected(true);
            binding.tvConfirmWithdrawalRequest.setSelected(true);
        }

        binding.llSetRequestView.setVisibility(View.GONE);
        binding.llVirtualConfirmView.setVisibility(View.VISIBLE);
        //用户名
        String userName = verifyVo.user_bank_info.user_name;
        String nickName = verifyVo.user_bank_info.nickname;
        String proUserName = mProfileVo.username;
        if (!TextUtils.isEmpty(userName)) {
            binding.tvConfirmUserName.setText(StringUtils.splitWithdrawUserName(userName));
        } else if (!TextUtils.isEmpty(nickName)) {
            binding.tvConfirmUserName.setText(StringUtils.splitWithdrawUserName(nickName));
        } else if (!TextUtils.isEmpty(proUserName)) {
            binding.tvConfirmUserName.setText(StringUtils.splitWithdrawUserName(proUserName));
        }
        //可提款金额
        binding.tvConfirmWithdrawalTypeShow.setText(verifyVo.quota);

        //提款类型
        binding.tvWithdrawalAmountTypeShow.setText(changVo.name);

        //实际提款金额
        binding.tvWithdrawalActualArrivalShow.setText(verifyVo.money_real);
        //汇率
        binding.tvWithdrawalExchangeRateShow.setText(infoVo.rate);
        //提币地址
        binding.tvWithdrawalAddressShow.setText(verifyVo.user_bank_info.account);
        //提款确定页面下一步
        binding.ivConfirmNext.setOnClickListener(v -> {
            requestSubmit(verifyVo);
        });
        //提款确定页面上一步
        binding.ivConfirmPrevious.setOnClickListener(v -> {
            //刷新顶部进度条颜色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvSetWithdrawalRequest.setSelected(true);
                binding.tvConfirmWithdrawalRequest.setSelected(false);
            }
            binding.llSetRequestView.setVisibility(View.VISIBLE);
            binding.llVirtualConfirmView.setVisibility(View.GONE);
        });

    }

    /**
     * 刷新 提款完成页面
     *
     * @param submitVo
     */
    private void refreshSubmitUI(final WithdrawalSubmitVo submitVo, final String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvSetWithdrawalRequest.setSelected(true);
            binding.tvConfirmWithdrawalRequest.setSelected(true);
            binding.tvOverWithdrawalRequest.setSelected(true);
        }

        binding.llVirtualConfirmView.setVisibility(GONE);
        binding.llOverApply.setVisibility(VISIBLE);

        if (submitVo != null) {
            if (submitVo != null && submitVo.message != null && !TextUtils.isEmpty(submitVo.message)) {
                if (TextUtils.equals("账户提款申请成功", submitVo.message)) {
                    binding.ivOverApply.setVisibility(VISIBLE);
                    binding.ivOverApply.setBackgroundResource(R.drawable.ic_over_apply);
                    binding.tvOverMsg.setVisibility(VISIBLE);
                    binding.tvOverMsg.setText(submitVo.message);

                } else if (TextUtils.equals("请刷新后重试", submitVo.message)) {
                    binding.tvOverMsg.setVisibility(VISIBLE);
                    binding.tvOverMsg.setText(submitVo.message);
                    binding.ivOverApply.setVisibility(VISIBLE);
                    binding.ivOverApply.setBackgroundResource(R.drawable.ic_over_apply_err);
                } else {
                    binding.tvOverMsg.setVisibility(VISIBLE);
                    binding.tvOverMsg.setText(submitVo.message);
                    binding.ivOverApply.setVisibility(VISIBLE);
                    binding.ivOverApply.setBackgroundResource(R.drawable.ic_over_apply_err);
                }
            }
        } else if (message != null && TextUtils.isEmpty(message)) {
            if (TextUtils.equals("账户提款申请成功", message)) {
                binding.ivOverApply.setVisibility(VISIBLE);
                binding.ivOverApply.setBackgroundResource(R.drawable.ic_over_apply);
                binding.tvOverMsg.setVisibility(VISIBLE);
                binding.tvOverMsg.setText(submitVo.message);

            } else if (TextUtils.equals("请刷新后重试", message)) {
                binding.tvOverMsg.setVisibility(VISIBLE);
                binding.tvOverMsg.setText(submitVo.message);
                binding.ivOverApply.setVisibility(VISIBLE);
                binding.ivOverApply.setBackgroundResource(R.drawable.ic_over_apply_err);
            } else {
                binding.tvOverMsg.setVisibility(VISIBLE);
                binding.tvOverMsg.setText(submitVo.message);
                binding.ivOverApply.setVisibility(VISIBLE);
                binding.ivOverApply.setBackgroundResource(R.drawable.ic_over_apply_err);
            }
        }

        //继续提款
        binding.ivContinueConfirmNext.setOnClickListener(v -> {
            dismiss();
        });
        //返回
        binding.ivContinueConfirmPrevious.setOnClickListener(v -> {
            dismiss();
        });
    }

    /**
     * 显示异常Dialog
     */
    private void showErrorDialog(String showMessage) {
        if (showMessage == null) {
            return;
        }
        errorPopView = new XPopup.Builder(getContext())
                .asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), showMessage, false, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        errorPopView.dismiss();
                        //callBack.closeDialog();
                    }

                    @Override
                    public void onClickRight() {
                        errorPopView.dismiss();
                        // callBack.closeDialog();
                    }
                }));
        errorPopView.show();
    }

    /**
     * 显示提币地址列表
     *
     * @param
     */
    private void showCollectionDialog(ArrayList<WithdrawalInfoVo.UserBankInfo> infoArrayList) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<WithdrawalInfoVo.UserBankInfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                WithdrawalInfoVo.UserBankInfo vo = get(position);

                String showMessage = vo.usdt_type + "--" + vo.account;

                binding2.tvwTitle.setText(showMessage);
                binding2.tvwTitle.setOnClickListener(v -> {
                    binding.tvBindAddress.setText(showMessage);
                    //设置选中的提款地址
                    selectorBankInfo = vo;
                    ppw.dismiss();
                });
            }
        };
        adapter.clear();
        adapter.addAll(infoArrayList);
        String selectString = getContext().getString(R.string.txt_select_add);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), selectString, adapter, 40));
        ppw.show();
    }

    /**
     * 关闭键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(this.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /*显示错误信息*/
    private void refreshError(String message) {
        binding.llTop.setFocusableInTouchMode(true);
        binding.llVirtualTop.setVisibility(View.GONE);
        binding.llSetRequestView.setVisibility(View.GONE);

        binding.llVirtualConfirmView.setVisibility(View.GONE);
        binding.llOverApply.setVisibility(View.GONE);
        binding.etInputMoney.clearFocus();
        // hideKeyBoard();
        binding.tvShowNumberErrorMessage.setVisibility(View.VISIBLE);
        binding.tvShowNumberErrorMessage.setText(message);
    }


    /**
     * 设置提款 请求 下一步
     */
    private void requestVerify(final String money, final WithdrawalInfoVo.UserBankInfo selectorBankInfo) {
        LoadingDialog.show(getContext());
        HashMap<String, Object> map = new HashMap<>();
        map.put("bank_id", selectorBankInfo.id);
        map.put("money", money);
        map.put("wtype", wtype);
        map.put("nonce", UuidUtil.getID24());
        CfLog.e("requestVerify -->" + map);
        viewModel.postWithdrawalVerify(map);
    }

    /**
     * 设置提款 完成申请
     */
    private void requestSubmit(final WithdrawalVerifyVo verifyVo) {
        LoadingDialog.show(getContext());
        HashMap<String, Object> map = new HashMap<>();
        map.put("bank_id", verifyVo.user_bank_info.id);
        map.put("money", verifyVo.money);
        map.put("wtype", wtype);
        map.put("nonce", UuidUtil.getID24());

        CfLog.e("requestSubmit -->" + map);

        viewModel.postWithdrawalSubmit(map);

    }

    /**
     * 顶部点击回调
     *
     * @param selectVo
     */
    @Override
    public void callbackWithUSDTFruitHor(WithdrawalListVo selectVo) {

        CfLog.e("callbackWithUSDTFruitHor=" + selectVo.toString());
        final String title = selectVo.title;
        final String selectorType = selectVo.type;
        changVo = selectVo;
        wtype = selectVo.name;
        //获取当前选中的提款详情
        viewModel.getWithdrawalInfo(selectVo.name);

    }

    /**
     * 显示异常信息的弹窗
     *
     * @param message
     */
    private void showErrorMessage(final String message) {
        if (ppwError == null) {
            final String title = getContext().getString(R.string.txt_kind_tips);
            ppwError = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, message, true, new TipDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    ppwError.dismiss();

                }

                @Override
                public void onClickRight() {
                    ppwError.dismiss();

                }
            }));

        }
        ppwError.show();
    }
}