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
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentThirdManagementBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_THIRD_MANAGEMENT)
public class ThirdManagementFragment extends BaseFragment<FragmentThirdManagementBinding, MineViewModel> {
    ThirdManagementAdapter adapter;

    private String starttime;
    private String endtime;
    private String userName = "";

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.fvMain.setVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE);
        binding.fvMain.setTopTotal("", "0", getString(R.string.txt__third_return_top), "0.0000");
        binding.fvMain.setDefEdit("代理名称", "", "请输入代理名称");

        binding.fvMain.setQueryListener(v -> {
            if (ClickUtil.isFastClick()) {
                return;
            }
            LoadingDialog.show(getActivity());
            adapter.clear();
            requestData();
        });

        adapter = new ThirdManagementAdapter(getContext());

        binding.rcvMain.setAdapter(adapter);
        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_third_management;
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
        viewModel.liveDataGetThirdManagement.observe(this, vo -> {
            binding.fvMain.setTopTotal("", "0", getString(R.string.txt__third_return_top), vo.getTotalMyselfPrice());

            if (!vo.getList().isEmpty()) {
                binding.tvwNoData.setVisibility(View.GONE);

                adapter.addAll(vo.getList());
            } else {
                binding.tvwNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    private void requestData() {
        starttime = binding.fvMain.getStartDate();
        endtime = binding.fvMain.getEndDate();
        userName = binding.fvMain.getEdit("");

        HashMap<String, String> map = new HashMap<>();
        map.put("start_time", starttime);
        map.put("end_time", endtime);
        map.put("username", userName);

        viewModel.getThirdManagement(map);
    }
}

