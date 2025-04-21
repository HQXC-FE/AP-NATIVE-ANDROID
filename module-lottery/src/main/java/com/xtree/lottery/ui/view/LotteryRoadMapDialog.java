package com.xtree.lottery.ui.view;


import static com.xtree.lottery.utils.LotteryEventConstant.EVENT_TIME_FINISH;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lxj.xpopup.core.CenterPopupView;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.RetrofitClient;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.lottery.R;
import com.xtree.lottery.data.source.LotteryApiService;
import com.xtree.lottery.data.source.vo.RecentLotteryVo;
import com.xtree.lottery.databinding.DialogRoadMapBinding;
import com.xtree.lottery.ui.view.model.RoadMapDLItemModel;
import com.xtree.lottery.ui.view.model.RoadMapZLItemModel;
import com.xtree.lottery.utils.EventVo;
import com.xtree.lottery.utils.RoadMapDataUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class LotteryRoadMapDialog extends CenterPopupView implements View.OnClickListener {


    //private Context ctx;
    LifecycleOwner owner;
    private int id; // lotteryId
    private int mTabIndex = 0;
    private String mTabTitle = "";
    Disposable disposable;
    DialogRoadMapBinding binding;

    private TextView tv_choose_weishu;

    //整合玩法
    private String mZhWeishu = "万位";
    private String mZhplayType = "dx";

    //龙虎斗玩法
    private String mLongHuPlayType = "0,1";

    //牛牛玩法
    private String mNNplayType = "dx";

    private List<RecentLotteryVo> mRecentLotteryVos = new ArrayList();
    private List<RoadMapZLItemModel> roadMapZLItemModelList = new ArrayList<>();
    private ZltAdapter zltAdapter;
    private DLAdapter dlAdapter;

    private List<RoadMapDLItemModel> roadMapDLItemModels = new ArrayList<>();

    private LotteryRoadMapDialog(@NonNull Context context) {
        super(context);
    }

    public static LotteryRoadMapDialog newInstance(Context ctx, LifecycleOwner owner, int id, int tabPosition, String title) {
        LotteryRoadMapDialog dialog = new LotteryRoadMapDialog(ctx);
        //dialog.ctx = ctx;
        dialog.owner = owner;
        dialog.id = id;
        dialog.mTabIndex = tabPosition;
        dialog.mTabTitle = title;
        return dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        if (!checkSupport()) {
            ToastUtils.showShort("暂不支持该玩法");
            dismiss();
            return;
        }
        EventBus.getDefault().register(this);
        initView();


        //todo 可以优化成外部直接把数据带入
        LoadingDialog.show(getContext()); // loading
        requestData();
    }

    private boolean checkSupport() {
        List<String> titles = new ArrayList<>();
        titles.add("整合");
        titles.add("龙虎斗");
        titles.add("百家乐");
        titles.add("牛牛");

        if (titles.contains(mTabTitle)) {
            return true;
        }
        return false;
    }

    private void initView() {
        binding = DialogRoadMapBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
        findViewById(R.id.ll_chose_weishu).setOnClickListener(this);

        findViewById(R.id.tv_dx).setOnClickListener(this);
        findViewById(R.id.tv_ds).setOnClickListener(this);
        findViewById(R.id.tv_w_vs_qian).setOnClickListener(this);
        findViewById(R.id.tv_w_vs_bai).setOnClickListener(this);
        findViewById(R.id.tv_w_vs_shi).setOnClickListener(this);
        findViewById(R.id.tv_w_vs_ge).setOnClickListener(this);
        findViewById(R.id.tv_q_vs_bai).setOnClickListener(this);
        findViewById(R.id.tv_q_vs_shi).setOnClickListener(this);
        findViewById(R.id.tv_q_vs_ge).setOnClickListener(this);
        findViewById(R.id.tv_b_vs_shi).setOnClickListener(this);
        findViewById(R.id.tv_b_vs_ge).setOnClickListener(this);
        findViewById(R.id.tv_s_vs_ge).setOnClickListener(this);
        tv_choose_weishu = findViewById(R.id.tv_choose_weishu);
        makeTobTabUi();
        makeDefaultSelectUi();
        zltAdapter = new ZltAdapter(R.layout.item_roadmap_rv, roadMapZLItemModelList);
        binding.rvZhulu.setAdapter(zltAdapter);
        StaggeredGridLayoutManager zltLayoutManager = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.HORIZONTAL);
        binding.rvZhulu.setLayoutManager(zltLayoutManager);


        dlAdapter = new DLAdapter(R.layout.item_roadmap_rv, roadMapDLItemModels);
        binding.rvDalu.setAdapter(dlAdapter);
        StaggeredGridLayoutManager dlLayoutManager = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.HORIZONTAL);
        binding.rvDalu.setLayoutManager(dlLayoutManager);
    }

    private int lastSelectId = -1;

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ll_chose_weishu) {
            showPopupWindow(tv_choose_weishu);
        } else {

            if (v.getId() == R.id.tv_dx) {
                binding.tvXiao.setText("小");
                binding.tvDa.setText("大");
                if (mTabTitle.equals("整合")) {
                    mZhplayType = "dx";
                } else if (mTabTitle.equals("牛牛")) {
                    mNNplayType = "dx";
                }

            } else if (v.getId() == R.id.tv_ds) {
                binding.tvXiao.setText("单");
                binding.tvDa.setText("双");
                if (mTabTitle.equals("整合")) {
                    mZhplayType = "ds";
                } else if (mTabTitle.equals("牛牛")) {
                    mNNplayType = "ds";
                }
            } else if (v.getId() == R.id.tv_w_vs_qian) {
                mLongHuPlayType = "0,1";
            } else if (v.getId() == R.id.tv_w_vs_bai) {
                mLongHuPlayType = "0,2";
            } else if (v.getId() == R.id.tv_w_vs_shi) {
                mLongHuPlayType = "0,3";
            } else if (v.getId() == R.id.tv_w_vs_ge) {
                mLongHuPlayType = "0,4";
            } else if (v.getId() == R.id.tv_q_vs_bai) {
                mLongHuPlayType = "1,2";
            } else if (v.getId() == R.id.tv_q_vs_shi) {
                mLongHuPlayType = "1,3";
            } else if (v.getId() == R.id.tv_q_vs_ge) {
                mLongHuPlayType = "1,4";
            } else if (v.getId() == R.id.tv_b_vs_shi) {
                mLongHuPlayType = "2,3";
            } else if (v.getId() == R.id.tv_b_vs_ge) {
                mLongHuPlayType = "2,4";
            } else if (v.getId() == R.id.tv_s_vs_ge) {
                mLongHuPlayType = "3,4";
            }
            v.setSelected(true);
            if (lastSelectId != -1 && lastSelectId != v.getId()) {
                findViewById(lastSelectId).setSelected(false);
                refreshUi();
            }
            lastSelectId = v.getId();
        }

    }

    private void makeTobTabUi() {
        if (mTabTitle.equals("整合")) { //整合
            findViewById(R.id.ll_chose_weishu).setVisibility(VISIBLE);
            findViewById(R.id.ll_dxds).setVisibility(VISIBLE);
        } else if (mTabTitle.equals("龙虎斗")) { //龙虎斗
            findViewById(R.id.hsv_longhu_tab).setVisibility(VISIBLE);
        } else if (mTabTitle.equals("牛牛")) { //牛牛
            findViewById(R.id.ll_dxds).setVisibility(VISIBLE);
        } else if (mTabTitle.equals("百家乐")) { //百家乐
            findViewById(R.id.tv_zxduizi).setVisibility(VISIBLE);
        }
    }

    private void makeDefaultSelectUi() {
        if (mTabTitle.equals("整合")) {
            binding.tvXiao.setText("小");
            binding.tvDa.setText("大");
            findViewById(R.id.tv_dx).setSelected(true);
            lastSelectId = R.id.tv_dx;
        } else if (mTabTitle.equals("龙虎斗")) {
            binding.tvXiao.setText("龙");
            binding.tvDa.setText("虎");
            findViewById(R.id.tv_w_vs_qian).setSelected(true);
            lastSelectId = R.id.tv_w_vs_qian;
        } else if (mTabTitle.equals("牛牛")) {
            binding.tvXiao.setText("小");
            binding.tvDa.setText("大");
            findViewById(R.id.tv_dx).setSelected(true);
            lastSelectId = R.id.tv_dx;
        } else if (mTabTitle.equals("百家乐")) {
            binding.tvXiao.setText("庄");
            binding.tvDa.setText("闲");
        }
    }


    private void setView(ArrayList<RecentLotteryVo> data) {
        //   Log.e("roadmap ", "数据源 " + new Gson().toJson(data));
        if (data == null || data.isEmpty()) {
            ToastUtils.showShort("暂无历史开奖数据");
            dismiss();
            return;
        }
        if (data.get(0).getSplit_code().size() != 5) {
            ToastUtils.showShort("路线图暂不支持该彩种");
            dismiss();
            return;
        }
        mRecentLotteryVos.clear();
        mRecentLotteryVos.addAll(data);
        refreshUi();
    }

    private void refreshUi() {
        //   ToastUtils.showShort("refreshUi");
        if (mTabTitle.equals("整合")) {
            roadMapZLItemModelList = RoadMapDataUtils.makeZongHengZLData(mZhWeishu, mZhplayType, mRecentLotteryVos);
        } else if (mTabTitle.equals("龙虎斗")) {
            roadMapZLItemModelList = RoadMapDataUtils.makeLongHuZLData(mRecentLotteryVos, mLongHuPlayType);
        } else if (mTabTitle.equals("牛牛")) {
            roadMapZLItemModelList = RoadMapDataUtils.makeNiuNiuZLData(mRecentLotteryVos, mNNplayType);
        } else if (mTabTitle.equals("百家乐")) {
            roadMapZLItemModelList = RoadMapDataUtils.makeBjlZLData(mRecentLotteryVos);
        }

        Collections.reverse(roadMapZLItemModelList);
        roadMapDLItemModels = RoadMapDataUtils.makeResultDLData(roadMapZLItemModelList);
        zltAdapter.setList(roadMapZLItemModelList);
        dlAdapter.setList(roadMapDLItemModels);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventVo event) {
        switch (event.getEvent()) {
            case EVENT_TIME_FINISH:
                requestData();
                break;
        }
    }

    private void requestData() {
        //    ToastUtils.showShort("彩种id "+ id);
        disposable = (Disposable) RetrofitClient.getInstance().
                create(LotteryApiService.class)
                .getRecentLottery(id)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ArrayList<RecentLotteryVo>>() {
                    @Override
                    public void onResult(ArrayList<RecentLotteryVo> list) {
                        setView(list);
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtils.showShort("error, " + t.toString());
                        CfLog.e("error, " + t.toString());
                        LotteryRoadMapDialog.this.dismiss();
                        super.onError(t);
                    }
                });
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_road_map;
    }


    private void showPopupWindow(View anchorView) {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_roadmap_choose_weishu, null);
        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(anchorView, -10, 5);
        TextView tv_choose_weishu_w = popupView.findViewById(R.id.tv_choose_weishu_w);
        TextView tv_choose_weishu_q = popupView.findViewById(R.id.tv_choose_weishu_q);
        TextView tv_choose_weishu_bai = popupView.findViewById(R.id.tv_choose_weishu_bai);
        TextView tv_choose_weishu_shi = popupView.findViewById(R.id.tv_choose_weishu_shi);
        TextView tv_choose_weishu_g = popupView.findViewById(R.id.tv_choose_weishu_g);
        backTypeWeishu(popupWindow, tv_choose_weishu_w);
        backTypeWeishu(popupWindow, tv_choose_weishu_q);
        backTypeWeishu(popupWindow, tv_choose_weishu_bai);
        backTypeWeishu(popupWindow, tv_choose_weishu_shi);
        backTypeWeishu(popupWindow, tv_choose_weishu_g);
    }

    private void backTypeWeishu(PopupWindow popupWindow, TextView tvWeiShu) {
        tvWeiShu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_choose_weishu.setText(tvWeiShu.getText().toString());
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                if (mTabTitle.equals("整合")) {
                    mZhWeishu = tv_choose_weishu.getText().toString();
                    refreshUi();
                }

            }
        });
    }


    class ZltAdapter extends BaseQuickAdapter<RoadMapZLItemModel, BaseViewHolder> {

        public ZltAdapter(int layoutResId, @Nullable List<RoadMapZLItemModel> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, RoadMapZLItemModel roadMapZLItemModel) {
            baseViewHolder.getView(R.id.tv_result).setBackgroundResource(roadMapZLItemModel.getTextBgDrawableId());
            baseViewHolder.setText(R.id.tv_result, roadMapZLItemModel.getText());
            if (getItemPosition(roadMapZLItemModel) == getItemCount() - 1) {
                baseViewHolder.getView(R.id.tv_result).
                        startAnimation(AnimationUtils.loadAnimation(getContext()
                                , R.anim.anim_alapha_loop));
            }else {
                baseViewHolder.getView(R.id.tv_result).clearAnimation();
            }
        }
    }


    class DLAdapter extends BaseQuickAdapter<RoadMapDLItemModel, BaseViewHolder> {

        public DLAdapter(int layoutResId, @Nullable List<RoadMapDLItemModel> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, RoadMapDLItemModel roadMapDLItemModel) {
            if (roadMapDLItemModel.getShowColorType() == 1) {
                baseViewHolder.getView(R.id.tv_result).setBackgroundResource(R.drawable.ic_blue_circle);
            } else if (roadMapDLItemModel.getShowColorType() == 2) {
                baseViewHolder.getView(R.id.tv_result).setBackgroundResource(R.drawable.ic_red_circle);
            } else if (roadMapDLItemModel.getShowColorType() == 3) {
                baseViewHolder.getView(R.id.tv_result).setBackgroundResource(R.drawable.ic_green_circle);
            } else if (roadMapDLItemModel.getShowColorType() == 4) {
                baseViewHolder.getView(R.id.tv_result).setBackgroundResource(R.drawable.ic_grey_circle);
            } else if (roadMapDLItemModel.getShowColorType() == 5) {
                baseViewHolder.getView(R.id.tv_result).setBackgroundResource(0);
            }

            if (getItemPosition(roadMapDLItemModel) == getItemCount() - 1) {
                baseViewHolder.getView(R.id.tv_result).
                        startAnimation(AnimationUtils.loadAnimation(getContext()
                                , R.anim.anim_alapha_loop));
            }else {
                baseViewHolder.getView(R.id.tv_result).clearAnimation();
            }

        }
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
