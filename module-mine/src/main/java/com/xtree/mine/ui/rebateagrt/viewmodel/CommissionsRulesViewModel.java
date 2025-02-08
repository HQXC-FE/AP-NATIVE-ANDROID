package com.xtree.mine.ui.rebateagrt.viewmodel;

import static com.xtree.mine.ui.rebateagrt.dialog.CommissionsRulesDialogFragment.COMMISSIONS_MODE;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.drake.brv.BindingAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.model.CommissionsRulesModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by KAKA on 2024/4/1.
 * Describe:
 */
public class CommissionsRulesViewModel extends BaseViewModel<MineRepository> {

    public final MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_commissions_rules);
                }
            });
    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {
            TextView tv1 = view.findViewById(R.id.item_tv1);
            TextView tv2 = view.findViewById(R.id.item_tv2);
            TextView tv3 = view.findViewById(R.id.item_tv3);
            int modelPosition = bindingViewHolder.getModelPosition() +1;

            if (modelPosition % 2 == 0) {
                tv1.setBackgroundColor(getApplication().getResources().getColor(R.color.clr_main_11));
                tv2.setBackgroundColor(getApplication().getResources().getColor(R.color.clr_main_11));
                tv3.setBackgroundColor(getApplication().getResources().getColor(R.color.clr_main_11));
            } else {
                tv1.setBackgroundColor(getApplication().getResources().getColor(R.color.clr_white));
                tv2.setBackgroundColor(getApplication().getResources().getColor(R.color.clr_white));
                tv3.setBackgroundColor(getApplication().getResources().getColor(R.color.clr_white));
            }
        }
        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {

        }
    };
    public final MutableLiveData<String> title1 = new MutableLiveData<>();
    public final MutableLiveData<String> title2 = new MutableLiveData<>();
    public final MutableLiveData<String> title3 = new MutableLiveData<>();

    private final ArrayList<BindModel> com1Models = new ArrayList<BindModel>() {
        {
            add(new CommissionsRulesModel("", "大于10万", "1.5%"));
            add(new CommissionsRulesModel("", "大于30万", "2%"));
        }
    };

    private final ArrayList<BindModel> com2Models = new ArrayList<BindModel>() {
        {
            add(new CommissionsRulesModel("保底", "3","23%"));
            add(new CommissionsRulesModel("10000-50000","3", "25%"));
            add(new CommissionsRulesModel("50000-100,000","4", "27%"));
            add(new CommissionsRulesModel("100000-200000","5","30%"));
            add(new CommissionsRulesModel("200000-500000","6", "33%"));
            add(new CommissionsRulesModel("500000-1000000", "8","35%"));
            add(new CommissionsRulesModel( "100万以上", "10","40%"));
        }
    };

    private final ArrayList<BindModel> agentModels = new ArrayList<BindModel>() {
        {
            add(new CommissionsRulesModel("1", "1", "2%"));
        }
    };
    public final MutableLiveData<String> title = new MutableLiveData<>();
    private WeakReference<FragmentActivity> mActivity = null;

    public CommissionsRulesViewModel(@NonNull Application application) {
        super(application);
    }

    public CommissionsRulesViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void initData(Integer stickyEvent) {
        //init data
        if (stickyEvent == COMMISSIONS_MODE) {
            initCommissionsData();
        } else {
            initAgentData();
        }
    }

    private void initAgentData() {
        title.setValue("全民代理奖励制度");
        title1.setValue("达标（代理）");
        title2.setValue("总亏损");
        title3.setValue("分红");
        datas.setValue(agentModels);
    }

    private void initCommissionsData() {

        int level = SPUtils.getInstance().getInt(SPKeyGlobal.USER_LEVEL);

        if (level == 2) {
            title.setValue("体育分红制度");
            title1.setValue("");
            title2.setValue("团队月净亏损");
            title3.setValue("分红比例");

        } else {
            title.setValue("体育分红制度");
            title1.setValue("月亏损金额");
            title2.setValue("活跃人数");
            title3.setValue("佣金比例");
        }

        if (level == 2) {
            datas.setValue(com1Models);
        } else if (level == 3) {
            datas.setValue(com2Models);
        } else {
            datas.setValue(agentModels);
        }
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }
    }
}