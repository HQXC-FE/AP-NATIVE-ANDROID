package com.xtree.mine.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.Constant;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CalenderUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.BrowserActivityX5;
import com.xtree.base.widget.DateTimePickerDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentOfferBinding;
import com.xtree.mine.databinding.ItemOfferBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.OfferItemVo;

import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_OFFER)
public class OfferFragment extends BaseFragment<FragmentOfferBinding, MineViewModel> {
    String mStatus = "";
    String mStartTime = CalenderUtil.cutDay(-29) + " 00:00:00";
    String mEndTime = CalenderUtil.searchToday() + " 23:59:59";
    CachedAutoRefreshAdapter<OfferItemVo> mAdapter;
    ItemOfferBinding binding2;
    TextView statusViews[];
    TextView timeViews[];

    @Override
    public void initView() {
        statusViews = new TextView[]{binding.tvAll, binding.tvReceive, binding.tvReceived, binding.tvExpired};
        timeViews = new TextView[]{binding.tvToday, binding.tvYesterday, binding.tvLast7, binding.tvLast30, binding.tvSetting};
        searchOffer();
        binding.tvStartTime.setText(CalenderUtil.searchToday());
        binding.tvEndTime.setText(CalenderUtil.searchToday());

        mAdapter = new CachedAutoRefreshAdapter<OfferItemVo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_offer, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemOfferBinding.bind(holder.itemView);
                OfferItemVo vo = get(position);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding2.tvTitle.setText(vo.reward_title);
                    binding2.tvMultiple.setText(vo.wagering_requirement);
                    String date = vo.expired_at.split(" ")[0];
                    binding2.tvTime.setText(date);

                    if (vo.show_rule.equals("0")) {
                        binding2.tvActive.setVisibility(View.GONE);
                    } else {
                        binding2.tvActive.setVisibility(View.VISIBLE);
                        binding2.tvActive.setOnClickListener(v -> {
                            String url = DomainUtil.getH5Domain2() + Constant.URL_ACTIVITY + vo.activity_id;
                            BrowserActivityX5.start(getContext(), vo.reward_title, url, true);
                        });
                    }

                    if (vo.operation_status.equals("1")) {
                        binding2.btGet.setText(R.string.txt_offer_receiving);
                        binding2.btGet.setTextColor(getContext().getColor(R.color.clr_white));
                        binding2.btGet.setBackground(getContext().getDrawable(R.drawable.bt_bg_main_c8));

                        binding2.btGet.setOnClickListener(v -> {
                            getOffer(vo.id);
                        });
                    } else if (vo.operation_status.equals("2")) {
                        binding2.btGet.setText(R.string.txt_offer_received);
                        binding2.btGet.setTextColor(getContext().getColor(R.color.clr_grey_13));
                        binding2.btGet.setBackground(null);
                    } else if (vo.operation_status.equals("3")) {
                        binding2.btGet.setText(R.string.txt_offer_expired);
                        binding2.btGet.setTextColor(getContext().getColor(R.color.clr_grey_13));
                        binding2.btGet.setBackground(null);
                    }
                }
            }
        };

        binding.rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMain.setAdapter(mAdapter);

        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.tvStatus.setOnClickListener(v -> {
            binding.tvStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_offer_down, 0);
            binding.tvTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_offer_right, 0);
            binding.clMenu.setVisibility(View.VISIBLE);
            binding.clStatus.setVisibility(View.VISIBLE);
            binding.clTime.setVisibility(View.GONE);
        });

        binding.tvAll.setOnClickListener(v -> {
            setStatus(binding.tvAll);
            mStatus = "";
        });

        binding.tvReceive.setOnClickListener(v -> {
            setStatus(binding.tvReceive);
            mStatus = "1";
        });

        binding.tvReceived.setOnClickListener(v -> {
            setStatus(binding.tvReceived);
            mStatus = "3";
        });

        binding.tvExpired.setOnClickListener(v -> {
            setStatus(binding.tvExpired);
            mStatus = "2";
        });

        binding.tvTime.setOnClickListener(v -> {
            binding.tvTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_offer_down, 0);
            binding.tvStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_offer_right, 0);
            binding.clMenu.setVisibility(View.VISIBLE);
            binding.clStatus.setVisibility(View.GONE);
            binding.clTime.setVisibility(View.VISIBLE);
        });

        binding.tvToday.setOnClickListener(v -> {
            setTime(binding.tvToday);
            mStartTime = CalenderUtil.searchToday() + " 00:00:00";
            mEndTime = CalenderUtil.searchToday() + " 23:59:59";
        });

        binding.tvYesterday.setOnClickListener(v -> {
            setTime(binding.tvYesterday);
            mStartTime = CalenderUtil.cutDay(-1) + " 00:00:00";
            mEndTime = CalenderUtil.searchToday() + " 23:59:59";
        });

        binding.tvLast7.setOnClickListener(v -> {
            setTime(binding.tvLast7);
            mStartTime = CalenderUtil.cutDay(-6) + " 00:00:00";
            mEndTime = CalenderUtil.searchToday() + " 23:59:59";
        });

        binding.tvLast30.setOnClickListener(v -> {
            setTime(binding.tvLast30);
            mStartTime = CalenderUtil.cutDay(-29) + " 00:00:00";
            mEndTime = CalenderUtil.searchToday() + " 23:59:59";
        });

        binding.tvSetting.setOnClickListener(v -> setTime(binding.tvSetting));

        binding.tvStartTime.setOnClickListener(v -> showDatePicker(binding.tvStartTime, getContext().getString(R.string.txt_date_start)));

        binding.tvEndTime.setOnClickListener(v ->
                showDatePicker(binding.tvEndTime, getContext().getString(R.string.txt_date_end))
        );

        binding.btReset.setOnClickListener(v -> {
            setStatus(binding.tvAll);
            setTime(binding.tvToday);
            mStatus = "";
            mStartTime = CalenderUtil.searchToday() + " 00:00:00";
            mEndTime = CalenderUtil.searchToday() + " 23:59:59";
            binding.tvStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_offer_right, 0);
            binding.tvTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_offer_right, 0);
            binding.clMenu.setVisibility(View.GONE);
            binding.clStatus.setVisibility(View.GONE);
            binding.clTime.setVisibility(View.GONE);
        });

        binding.btOk.setOnClickListener(v -> {
            searchOffer();
            binding.tvStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_offer_right, 0);
            binding.tvTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_offer_right, 0);
            binding.clMenu.setVisibility(View.GONE);
            binding.clStatus.setVisibility(View.GONE);
            binding.clTime.setVisibility(View.GONE);
        });

        binding.btSpace.setOnClickListener(v -> {
            binding.tvStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_offer_right, 0);
            binding.tvTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_offer_right, 0);
            binding.clMenu.setVisibility(View.GONE);
            binding.clStatus.setVisibility(View.GONE);
            binding.clTime.setVisibility(View.GONE);
        });

        setTime(binding.tvLast30);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_offer;
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

    @Override
    public void initViewObservable() {
        viewModel.offerVoMutableLiveData.observe(this, vo -> {
            binding.tvOfferWaitMoney.setText(vo.meta.total_pending_amount);
            binding.tvOfferAllMoney.setText(vo.meta.total_redeem_amount);

            if (mAdapter != null) {
                mAdapter.clear();
                if (!vo.data.isEmpty()) {
                    binding.tvNoData.setVisibility(View.GONE);
                    mAdapter.addAll(vo.data);
                } else {
                    binding.tvNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setStatus(TextView tv) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (TextView view : statusViews) {
                if (view == tv) {
                    view.setTextColor(getContext().getColor(R.color.clr_purple_20));
                    view.setBackground(getContext().getDrawable(R.drawable.bg_btn_offer_click));
                    continue;
                }
                view.setTextColor(getContext().getColor(R.color.clr_txt_grey_11));
                view.setBackground(getContext().getDrawable(R.drawable.bg_btn_offer_unclick));
            }
        }
    }

    private void setTime(TextView tv) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (TextView view : timeViews) {
                if (view == tv) {
                    if (view == binding.tvSetting) {
                        binding.tvStartTime.setVisibility(View.VISIBLE);
                        binding.tvSymbol.setVisibility(View.VISIBLE);
                        binding.tvEndTime.setVisibility(View.VISIBLE);
                    }
                    view.setTextColor(getContext().getColor(R.color.clr_purple_20));
                    view.setBackground(getContext().getDrawable(R.drawable.bg_btn_offer_click));
                    continue;
                }
                view.setTextColor(getContext().getColor(R.color.clr_txt_grey_11));
                view.setBackground(getContext().getDrawable(R.drawable.bg_btn_offer_unclick));
                binding.tvStartTime.setVisibility(View.GONE);
                binding.tvSymbol.setVisibility(View.GONE);
                binding.tvEndTime.setVisibility(View.GONE);
            }
        }
    }

    private void searchOffer() {
        HashMap<String, String> map = new HashMap<>();
        map.put("start", mStartTime);
        map.put("end", mEndTime);
        map.put("filter_rewards", mStatus);
        map.put("page", "1");
        map.put("per_page", "30");
        viewModel.getOfferList(map);
    }

    private void getOffer(String key) {
        HashMap<String, String> map = new HashMap<>();
        map.put("nonce", UuidUtil.getID16());

        HashMap<String, String> offerListMap = new HashMap<>();
        offerListMap.put("start", mStartTime);
        offerListMap.put("end", mEndTime);
        offerListMap.put("filter_rewards", mStatus);
        offerListMap.put("page", "1");
        offerListMap.put("per_page", "30");

        viewModel.getOffer(key, map, offerListMap);
    }

    private void showDatePicker(TextView tvw, String title) {
        CfLog.i("****** ");
        new XPopup.Builder(getContext())
                .asCustom(DateTimePickerDialog.newInstance(getContext(), title, "yyyy-MM-dd", -29, date -> {
                    tvw.setText(date);
                    if (tvw == binding.tvStartTime) {
                        mStartTime = date + " 00:00:00";
                    } else {
                        mEndTime = date + " 23:59:59";
                    }
                }))
                .show();
    }
}
