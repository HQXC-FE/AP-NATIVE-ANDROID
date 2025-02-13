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
            add(new CommissionsRulesModel("1000", "0","25%"));
            add(new CommissionsRulesModel("1001-10000","3", "28%"));
            add(new CommissionsRulesModel("10001-50000","4", "30%"));
            add(new CommissionsRulesModel("50001-100000","5","32%"));
            add(new CommissionsRulesModel("100001-200000","6", "34%"));
            add(new CommissionsRulesModel("200001-500000", "7","36%"));
            add(new CommissionsRulesModel( "500001-1000000", "8","38%"));
            add(new CommissionsRulesModel( "1000001-2000000", "10","45%"));
            add(new CommissionsRulesModel( "200万以上", "20","50%"));
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