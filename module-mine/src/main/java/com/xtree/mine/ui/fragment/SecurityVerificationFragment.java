package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.ProfileVo;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentSecurityVerificationBinding;
import com.xtree.mine.ui.viewmodel.VerifyViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.ArrayList;

import com.xtree.base.base.BaseFragment;
import com.xtree.base.utils.SPUtils;

@Route(path = RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY)
public class SecurityVerificationFragment extends BaseFragment<FragmentSecurityVerificationBinding, VerifyViewModel> {
    private static final String ARG_TYPE = "type";
    private static final String ARG_TOKEN_SIGN = "tokenSign";
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> tabList = new ArrayList<>();
    private FragmentStateAdapter mAdapter;
    private String typeName;
    private String typeName2; // 绑定手机邮箱后,跳转的类型
    private String tokenSign;
    ProfileVo mProfileVo;
    private String phone; // 异地登录验证用
    private String email; // 异地登录验证用

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.readCache();
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));

        binding.ivwMsg.setOnClickListener(v -> {
            // 消息
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG);
        });

        mAdapter = new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        };

        binding.vpMain.setAdapter(mAdapter);
        binding.vpMain.setUserInputEnabled(true); // ViewPager2 左右滑动

        new TabLayoutMediator(binding.tblType, binding.vpMain, (tab, position) -> {
            tab.setText(tabList.get(position));
        }).attach();
    }

    @Override
    public void initData() {

        if (getArguments() != null) {
            typeName = getArguments().getString(ARG_TYPE);
            tokenSign = getArguments().getString(ARG_TOKEN_SIGN);
            phone = getArguments().getString("phone");
            email = getArguments().getString("email");
        }
        CfLog.i("****** typeName: " + typeName + ", tokenSign: " + tokenSign);
        fragmentList.clear();
        tabList.clear();

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
        Bundle bundle = getArguments();
        // 如果手机和邮箱都没有绑定的情况下, 此时应该是先去绑定
        if (mProfileVo != null && !mProfileVo.is_binding_phone && !mProfileVo.is_binding_email) {
            if (!Constant.BIND_PHONE.equals(typeName) && !Constant.BIND_EMAIL.equals(typeName)) {
                typeName = Constant.BIND; // 绑定手机/邮箱
                typeName2 = getArguments().getString(ARG_TYPE); // 绑定后的跳转
                bundle.putString("type", typeName);
                bundle.putString("type2", typeName2);
                CfLog.i("****** type: " + typeName + ", type2: " + typeName2);
            }
        }

        //BindPhoneFragment bindPhoneFragment = BindPhoneFragment.newInstance(typeName, tokenSign);
        //BindEmailFragment bindEmailFragment = BindEmailFragment.newInstance(typeName, tokenSign);
        BindPhoneFragment bindPhoneFragment = BindPhoneFragment.newInstance(bundle);
        BindEmailFragment bindEmailFragment = BindEmailFragment.newInstance(bundle);

        if (mProfileVo != null && mProfileVo.is_binding_phone && mProfileVo.is_binding_email) {
            if (Constant.UPDATE_PHONE.equals(typeName)) {
                Bundle bundle2 = new Bundle(getArguments());
                bundle2.putString("type", Constant.VERIFY_BIND_PHONE); // 更新手机,使用邮箱验证
                bindEmailFragment = BindEmailFragment.newInstance(bundle2);
            }
            if (Constant.UPDATE_EMAIL.equals(typeName)) {
                Bundle bundle2 = new Bundle(getArguments());
                bundle2.putString("type", Constant.VERIFY_BIND_EMAIL); // 更新邮箱,使用手机验证
                bindPhoneFragment = BindPhoneFragment.newInstance(bundle2);
            }
        }

        String txtPhone = getString(R.string.txt_phone_num);
        String txtEmail = getString(R.string.txt_email_addr);
        if (Constant.BIND.equals(typeName) || TextUtils.isEmpty(typeName)) {
            fragmentList.add(bindEmailFragment);
            fragmentList.add(0, bindPhoneFragment);
            tabList.add(txtEmail);
            tabList.add(0, txtPhone);
        } else if (Constant.BIND_PHONE.equals(typeName) || Constant.VERIFY_BIND_EMAIL.equals(typeName) || Constant.VERIFY_BIND_PHONE2.equals(typeName)) {
            fragmentList.add(bindPhoneFragment);
            tabList.add(txtPhone);
            binding.tblType.setVisibility(View.GONE);
        } else if (Constant.BIND_EMAIL.equals(typeName) || Constant.VERIFY_BIND_PHONE.equals(typeName) || Constant.VERIFY_BIND_EMAIL2.equals(typeName)) {
            fragmentList.add(bindEmailFragment);
            tabList.add(txtEmail);
            binding.tblType.setVisibility(View.GONE);
        } else {
            // 去验证
            if ((Constant.VERIFY_LOGIN.equals(typeName) && !TextUtils.isEmpty(email)) || (mProfileVo != null && mProfileVo.is_binding_email)) {
                fragmentList.add(bindEmailFragment);
                tabList.add(txtEmail);
            }
            if ((Constant.VERIFY_LOGIN.equals(typeName) && !TextUtils.isEmpty(phone)) || (mProfileVo != null && mProfileVo.is_binding_phone)) {
                fragmentList.add(0, bindPhoneFragment);
                tabList.add(0, txtPhone);
            }
            if (fragmentList.size() == 1) {
                binding.tblType.setVisibility(View.GONE);
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_security_verification;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public VerifyViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(VerifyViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataProfile.observe(this, vo -> {
            mProfileVo = vo;
        });

    }

}
