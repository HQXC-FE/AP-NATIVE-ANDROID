package com.xtree.home.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.home.BR;
import com.xtree.home.R;
import com.xtree.home.databinding.FragmentAdsBinding;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Home.AD)
public class AdsFragment extends BaseFragment<FragmentAdsBinding, HomeViewModel> {

    private String downUrl = "https://www.hiwalletapp.com/download/main";
    @Override
    public void initView() {
        binding.adsAndroidStore.setOnClickListener(v -> {
            openWeb();
        });
        binding.adsiOSStore.setOnClickListener(v -> {
            openWeb();
        });
    }
    private void  openWeb(){
        AppUtil.goBrowser(getContext() , downUrl);
    }
    @Override
    protected void initImmersionBar() {
    }

    /**
     * 使用hide和show后，可见不可见切换时，不再执行fragment生命周期方法，
     * 需要刷新时，使用onHiddenChanged代替
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 显示

        } else {  // 第一次可见，不会执行到这里，只会执行onResume
            //网络数据刷新

        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_ads;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public HomeViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(HomeViewModel.class);
    }

}
