package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.ClickUtil;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentBonusPoolBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_BONUS_POOL)
public class BonusPoolFragment extends BaseFragment<FragmentBonusPoolBinding, MineViewModel> {
    BonusPoolAdapter adapter;
    private int curPage = 1;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate today;
    LocalDate firstDayOfMonth;
    LocalDate lastDayOfMonth;

    @Override
    public void initView() {
        today = LocalDate.now();
        firstDayOfMonth = today.withDayOfMonth(1);
        lastDayOfMonth = YearMonth.from(today).atEndOfMonth();

        binding.tvwDataRange.setText(firstDayOfMonth.format(formatter) + "~" + lastDayOfMonth.format(formatter));

        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.btnSearch.setOnClickListener(v -> requestData());

        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (ClickUtil.isFastClick()) {
                return;
            }
            binding.refreshLayout.setEnableLoadMore(true);
            binding.refreshLayout.setEnableRefresh(true);
            curPage = 1;
            adapter.clear();
            requestData();
        });

        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            curPage++;
            requestData();
        });
    }

    @Override
    public void initData() {
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setEnableRefresh(false);

        adapter = new BonusPoolAdapter(getContext());
        binding.rcvMain.setAdapter(adapter);
        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bonus_pool;
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
        viewModel.liveDataBonusPoolReport.observe(this, vo -> {
            binding.refreshLayout.finishRefresh();
            binding.refreshLayout.finishLoadMore();

            if (null == vo.getList()) {
                binding.refreshLayout.setEnableLoadMore(false);
                binding.refreshLayout.setEnableRefresh(false);
                binding.tvwNoData.setVisibility(View.VISIBLE);
                return;
            }

            binding.refreshLayout.setEnableLoadMore(vo.getP() != vo.getTotalPage());

            if (vo.getList() != null && !vo.getList().isEmpty()) {
                binding.tvwNoData.setVisibility(View.GONE);
                adapter.addAll(vo.getList());
            }

            binding.tvwReturnMoney.setText(vo.getAmount());
        });
    }

    private void requestData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("p", String.valueOf(curPage));
        map.put("pn", "10");

        viewModel.getBonusPoolReport(map);
    }
}
