package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindHead;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.widget.FilterView;
import com.xtree.mine.R;
import com.xtree.mine.vo.StatusVo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/3/11.
 * Describe: 游戏场馆下级返水头数据
 */
public class GameSubordinaterebateHeadModel extends BindModel implements BindHead {

    //状态
    public ObservableField<StatusVo> state = new ObservableField<>(new StatusVo(0, BaseApplication.getInstance().getString(R.string.txt_all_status)));
    //开始时间
    public ObservableField<String> startDate = new ObservableField<>();
    //结束时间
    public ObservableField<String> endDate = new ObservableField<>();
    //搜索用户名
    public ObservableField<String> userName = new ObservableField<>("");
    //分页索引
    public int p = 1;
    //page count
    public int pn = 20;
    public List<FilterView.IBaseVo> listStatus = new ArrayList<FilterView.IBaseVo>() {
        {
            // 0-所有状态
            add(new StatusVo(0, BaseApplication.getInstance().getString(R.string.txt_all_status)));
            // 1-已到账
            add(new StatusVo(1, BaseApplication.getInstance().getString(R.string.txt_received)));
            // 2-未到账
            add(new StatusVo(2, BaseApplication.getInstance().getString(R.string.txt_unreceived)));
        }
    };
    private onCallBack onCallBack = null;
    //场馆类型
    private RebateAreegmentTypeEnum typeEnum;
    public ObservableField<Boolean> showState = new ObservableField<>(false);

    public GameSubordinaterebateHeadModel() {
        initData();
    }

    public GameSubordinaterebateHeadModel(GameSubordinaterebateHeadModel.onCallBack onCallBack) {
        this.onCallBack = onCallBack;
        initData();
    }

    public void setOnCallBack(GameSubordinaterebateHeadModel.onCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1); // 昨天
        startDate.set(TimeUtils.longFormatString(calendar.getTimeInMillis(), "yyyy-MM-dd"));
        endDate.set(TimeUtils.longFormatString(System.currentTimeMillis(), "yyyy-MM-dd"));
    }

    public void setTypeEnum(RebateAreegmentTypeEnum typeEnum) {
        this.typeEnum = typeEnum;

        //捕鱼返水契约的下级返水需要显示状态筛选框
        switch (typeEnum) {
            case FISH:
                state.set((StatusVo) listStatus.get(1));
                showState.set(true);
                break;
            default:
                state.set((StatusVo) listStatus.get(0));
                showState.set(false);
                break;
        }
    }

    @Override
    public boolean getItemHover() {
        return true;
    }

    @Override
    public void setItemHover(boolean b) {

    }

    public void selectStartDate() {
        if (onCallBack != null) {
            onCallBack.selectStartDate(startDate);
        }
    }

    public void selectEndDate() {
        if (onCallBack != null) {
            onCallBack.selectEndDate(endDate);
        }
    }

    public void selectStatus() {
        if (onCallBack != null) {
            onCallBack.selectStatus(state, listStatus);
        }
    }

    public void check() {
        if (onCallBack != null) {
            p = 1;
            onCallBack.check(userName.get(), startDate.get(), endDate.get());
        }
    }

    public interface onCallBack {
        void selectStartDate(ObservableField<String> startDate);

        void selectEndDate(ObservableField<String> endDate);

        void selectStatus(ObservableField<StatusVo> state, List<FilterView.IBaseVo> listStatus);

        void check(String userName, String startDate, String endDate);
    }
}
