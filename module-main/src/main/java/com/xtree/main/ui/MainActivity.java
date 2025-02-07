package com.xtree.main.ui;

import static com.xtree.base.vo.EventConstant.EVENT_CHANGE_TO_ACT;
import static com.xtree.base.vo.EventConstant.EVENT_TOP_SPEED_FAILED;
import static com.xtree.base.vo.EventConstant.EVENT_TOP_SPEED_FINISH;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;
import com.xtree.base.net.fastest.ChangeH5LineUtil;
import com.xtree.base.net.fastest.FastestTopDomainUtil;
import com.xtree.base.net.fastest.TopSpeedDomainFloatingWindows;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.EventVo;
import com.xtree.base.widget.MenuItemView;
import com.xtree.main.BR;
import com.xtree.main.R;
import com.xtree.main.databinding.ActivityMainBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;
import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.utils.ConvertUtils;

/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterActivityPath.Main.PAGER_MAIN)
public class MainActivity extends BaseActivity<ActivityMainBinding, BaseViewModel> {
    private List<Fragment> mFragments;
    private Fragment showFragment;
    private TopSpeedDomainFloatingWindows mTopSpeedDomainFloatingWindows;
    private NavigationController navigationController;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(MainActivity.this)
                .navigationBarColor(me.xtree.mvvmhabit.R.color.default_navigation_bar_color)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    public void initView() {
        mTopSpeedDomainFloatingWindows = new TopSpeedDomainFloatingWindows(MainActivity.this);
        mTopSpeedDomainFloatingWindows.show();
    }

    @Override
    public void initData() {
        //初始化Fragment
        initFragment();
        //初始化底部Button
        initBottomTab();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        FastestTopDomainUtil.getInstance().start();
        ChangeH5LineUtil.getInstance().start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mTopSpeedDomainFloatingWindows != null) {
            mTopSpeedDomainFloatingWindows.removeView();
        }
    }

    private void initFragment() {
        //ARouter拿到多Fragment(这里需要通过ARouter获取，不能直接new,因为在组件独立运行时，宿主app是没有依赖其他组件，所以new不到其他组件的Fragment)
        Fragment homeFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Home.PAGER_HOME).navigation();
        Fragment activityFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Activity.PAGER_ACTIVITY).navigation();
        //Fragment adFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Home.AD).navigation();
        Fragment rechargeFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Recharge.PAGER_RECHARGE).navigation();
        Fragment mineFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Mine.PAGER_MINE).navigation();
        mFragments = new ArrayList<>();
        mFragments.add(homeFragment);
        mFragments.add(activityFragment);
        //mFragments.add(adFragment);
        mFragments.add(rechargeFragment);
        mFragments.add(mineFragment);
        showFragment = mFragments.get(0);
        if (showFragment != null) {
            //默认选中第一个
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, showFragment);
            transaction.commitNow();
        }
    }

    //创建一个Item
    private BaseTabItem newItem(int drawable, int drawableSelect, String text) {
        MenuItemView normalItemView = new MenuItemView(this);
        normalItemView.initialize(drawable, drawable, text);
        normalItemView.setDefaultDrawable(getResources().getDrawable(drawable));
        normalItemView.setSelectedDrawable(getResources().getDrawable(drawableSelect));
        normalItemView.setTextDefaultColor(getResources().getColor(R.color.clr_txt_black_cm));
        normalItemView.setTextCheckedColor(getResources().getColor(R.color.colorPrimary));
        normalItemView.setIconTopMargin(ConvertUtils.dp2px(12f));
        normalItemView.setTextTopMarginOnIcon(ConvertUtils.dp2px(1.5f));
        return normalItemView;
    }

    /**
     * 圆形tab
     */
    //private BaseTabItem newRoundItem(int drawable, String text) {
    //    SpecialMenuItemView mainTab = new SpecialMenuItemView(this);
    //    mainTab.initialize(drawable, drawable, text);
    //    mainTab.setTextDefaultColor(getResources().getColor(R.color.colorPrimary));
    //    mainTab.setTextCheckedColor(getResources().getColor(R.color.colorPrimary));
    //    return mainTab;
    //}

    private void initBottomTab() {
        navigationController = binding.pagerBottomTab.custom()
                .addItem(newItem(R.mipmap.mn_hm_unselected, R.mipmap.mn_hm_selected, getString(R.string.txt_pg_home)))
                .addItem(newItem(R.mipmap.mn_dc_unselected, R.mipmap.mn_dc_selected, getString(R.string.txt_pg_discount)))
                //.addItem(newRoundItem(R.mipmap.ic_tab_main, getString(R.string.txt_main_game)))
                .addItem(newItem(R.mipmap.mn_rc_unselected, R.mipmap.mn_rc_selected, getString(R.string.txt_pg_recharge)))
                .addItem(newItem(R.mipmap.mn_psn_unselected, R.mipmap.mn_psn_selected, getString(R.string.txt_pg_mine)))
                .build();
        //底部按钮的点击事件监听
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                Fragment currentFragment = mFragments.get(index);
                if (currentFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    if (!currentFragment.isAdded()) {
                        transaction.add(R.id.frameLayout, currentFragment);
                    }
                    //使用hide和show后，不再执行fragment生命周期方法
                    //需要刷新时，使用onHiddenChanged代替
                    transaction.hide(showFragment).show(currentFragment);
                    showFragment = currentFragment;
                    //transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

            @Override
            public void onRepeat(int index) {
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventVo event) {
        switch (event.getEvent()) {
            case EVENT_CHANGE_TO_ACT:
                CfLog.i("Change to activity");
                navigationController.setSelect(1);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.hide(showFragment).show(mFragments.get(1));
                showFragment = mFragments.get(1);
                transaction.commitAllowingStateLoss();
                break;
            case EVENT_TOP_SPEED_FINISH:
                CfLog.e("EVENT_TOP_SPEED_FINISH竞速完成。。。");
                mTopSpeedDomainFloatingWindows.refresh();
                break;
            case EVENT_TOP_SPEED_FAILED:
                mTopSpeedDomainFloatingWindows.onError();
                break;
        }
    }
}
