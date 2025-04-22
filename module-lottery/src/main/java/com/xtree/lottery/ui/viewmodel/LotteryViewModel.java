package com.xtree.lottery.ui.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.lottery.data.LotteryRepository;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.request.LotteryCopyBetRequest;
import com.xtree.lottery.data.source.vo.CancelOrderVo;
import com.xtree.lottery.data.source.vo.IssueVo;
import com.xtree.lottery.data.source.vo.LotteryChaseDetailVo;
import com.xtree.lottery.data.source.vo.LotteryOrderVo;
import com.xtree.lottery.data.source.vo.LotteryReportVo;
import com.xtree.lottery.data.source.vo.MethodMenus;
import com.xtree.lottery.data.source.vo.RecentLotteryVo;
import com.xtree.lottery.data.source.vo.TraceInfoVo;
import com.xtree.lottery.databinding.DialogLotteryBetConfirmBindingImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class LotteryViewModel extends BaseViewModel<LotteryRepository> {
    //livedata只有在STARTED（onStart()到onPause()）和RESUMED才会接收数据
    //处于不可见但未销毁时，livedata会存储数据，观察者页面可见时，再传递给它
    //SingleLiveData，只有一名观察者会收到更改通知。
    public MutableLiveData<ArrayList<RecentLotteryVo>> liveDataRecentList = new MutableLiveData<>();
    public SingleLiveData<MethodMenus> liveDataMethodMenus = new SingleLiveData<>();
    public MutableLiveData<IssueVo> liveDataCurrentIssue = new SingleLiveData<>();
    public MutableLiveData<Boolean> liveDataCloseLotteryDetail = new SingleLiveData<>();
    public MutableLiveData<ArrayList<IssueVo>> liveDataListIssue = new SingleLiveData<>();
    public MutableLiveData<LotteryReportVo> liveDataCpReport = new MutableLiveData<>(); // 投注记录-列表(彩票)
    public MutableLiveData<LotteryOrderVo> liveDataBtCpDetail = new MutableLiveData<>(); // 投注记录-详情(彩票)
    public SingleLiveData<CancelOrderVo> liveDataCancelOrder = new SingleLiveData<>(); // 取消订单
    public MutableLiveData<TraceInfoVo> liveDataTraceinfo = new MutableLiveData<>(); // 追号记录-列表(彩票)
    public MutableLiveData<LotteryChaseDetailVo> liveDataBtChaseDetail = new MutableLiveData<>(); // 追号记录-详情(彩票)
    public MutableLiveData<CancelOrderVo> liveDataCancelTask = new MutableLiveData<>(); // 追号终止
    //当前期号
    public MutableLiveData<IssueVo> currentIssueLiveData = new MutableLiveData<>();
    //彩票信息
    public MutableLiveData<Lottery> lotteryLiveData = new MutableLiveData<>();

    private BasePopupView popupView;

    public LotteryViewModel(@NonNull Application application) {
        super(application, null);
    }

    public LotteryViewModel(@NonNull Application application, LotteryRepository repository) {
        super(application, repository);
    }

    public void getRecentLottery() {
        Disposable disposable = (Disposable) model.getApiService().getRecentLottery(lotteryLiveData.getValue().getId())
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ArrayList<RecentLotteryVo>>() {
                    @Override
                    public void onResult(ArrayList<RecentLotteryVo> list) {
                        liveDataRecentList.setValue(list);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getMethodMenus(String alias) {
        Disposable disposable = (Disposable) model.getApiService().getMethodMenus(alias)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MethodMenus>() {
                    @Override
                    public void onResult(MethodMenus data) {
                        liveDataMethodMenus.setValue(data);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getCurrentIssue(int id) {
        Disposable disposable = (Disposable) model.getApiService().getCurrentIssue(id)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<IssueVo>() {
                    @Override
                    public void onResult(IssueVo data) {
                        liveDataCurrentIssue.setValue(data);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }

                    @Override
                    public void onFail30103(BusinessException t) {
                        super.onFail30103(t);
                        ToastUtils.showLong(t.message);
                        liveDataCloseLotteryDetail.setValue(true);
                    }
                });
        addSubscribe(disposable);
    }

    public void getTrackingIssue(int id) {
        Disposable disposable = (Disposable) model.getApiService().getTrackingIssue(id)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ArrayList<IssueVo>>() {
                    @Override
                    public void onResult(ArrayList<IssueVo> data) {
                        liveDataListIssue.setValue(data);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getCpReport() {
        // 获取日历实例
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1); // 昨天

        // 获取昨天到今天的数据
        String startTime = TimeUtils.longFormatString(calendar.getTimeInMillis(), "yyyy-MM-dd") + " 00:00:00";
        String endTime = TimeUtils.longFormatString(System.currentTimeMillis(), "yyyy-MM-dd") + " 23:59:59";

        // 创建参数map
        HashMap<String, String> map = new HashMap<>();

        // 获取用户信息
        String userId = SPUtils.getInstance().getString(SPKeyGlobal.USER_ID);  // 2888826
        String userName = SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME);  // testkite1002

        // 设置请求参数
        map.put("userid", userId);
        map.put("username", userName);
        map.put("controller", "gameinfo");
        map.put("action", "newgamelist");
        map.put("starttime", startTime);
        map.put("endtime", endTime);
        map.put("lotteryid", "0");
        map.put("methodid", "0");
        map.put("ischild", "0");
        map.put("p", "1");
        map.put("pn", "500");
        map.put("client", "m");
        Disposable disposable = (Disposable) model.getApiService().getCpReport(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LotteryReportVo>() {
                    @Override
                    public void onResult(LotteryReportVo vo) {
                        CfLog.d("******");
                        liveDataCpReport.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getBtCpOrderDetail(String id) {
        Disposable disposable = (Disposable) model.getApiService().getBtCpOrderDetail(id)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LotteryOrderVo>() {
                    @Override
                    public void onResult(LotteryOrderVo vo) {
                        CfLog.d("******");
                        liveDataBtCpDetail.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void cancelOrder(String id) {
        Disposable disposable = (Disposable) model.getApiService().cancelOrder(id)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<CancelOrderVo>() {
                    @Override
                    public void onResult(CancelOrderVo vo) {
                        CfLog.d("******");
                        liveDataCancelOrder.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getTraceinfo() {
        // 获取日历实例
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 15); // 15天前的数据

        // 获取15天前到今天的数据
        String startTime = TimeUtils.longFormatString(calendar.getTimeInMillis(), "yyyy-MM-dd") + " 00:00:00";
        String endTime = TimeUtils.longFormatString(System.currentTimeMillis(), "yyyy-MM-dd") + " 23:59:59";

        // 创建参数map
        HashMap<String, String> map = new HashMap<>();

        // 设置请求参数
        map.put("controller", "report");
        map.put("action", "traceinfo");
        map.put("starttime", startTime);
        map.put("endtime", endTime);
        map.put("lotteryid", "0");
        map.put("methodid", "0");
        map.put("p", "1");
        map.put("pn", "500");
        map.put("client", "m");
        Disposable disposable = (Disposable) model.getApiService().getTraceinfo(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<TraceInfoVo>() {
                    @Override
                    public void onResult(TraceInfoVo vo) {
                        liveDataTraceinfo.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getBtChaseDetailDetail(String id) {
        Disposable disposable = (Disposable) model.getApiService().getBtChaseDetailDetail(id)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LotteryChaseDetailVo>() {
                    @Override
                    public void onResult(LotteryChaseDetailVo vo) {
                        CfLog.d("******");
                        liveDataBtChaseDetail.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 追号终止
     */
    public void cancelTask(String id, List list) {
        Map<String, Object> params = new HashMap<>();
        params.put("taskid", list);
        params.put("id", id);
        params.put("nonce", UuidUtil.getID24());
        Disposable disposable = (Disposable) model.getApiService().cancelTask(params)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<CancelOrderVo>() {
                    @Override
                    public void onResult(CancelOrderVo vo) {
                        CfLog.d("******");
                        liveDataCancelTask.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 再来一注
     */
    public void copyBet(View view, String id) {

        LotteryCopyBetRequest lotteryCopyBetRequest = new LotteryCopyBetRequest();
        lotteryCopyBetRequest.setId(id);
        lotteryCopyBetRequest.setPlay_source(6);
        lotteryCopyBetRequest.setNonce(UuidUtil.getID24());
        model.copyBet(lotteryCopyBetRequest).subscribe(new HttpCallBack<Object>() {
            @Override
            public void onResult(Object response) {

                MsgDialog dialog = new MsgDialog(view.getContext(), "", "投注成功", true, new TipDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {

                    }

                    @Override
                    public void onClickRight() {
                        if (popupView != null) {
                            popupView.dismiss();
                        }
                        LoadingDialog.show(view.getContext());
                        getCpReport();
                    }
                });

                popupView = new XPopup.Builder(view.getContext())
                        .dismissOnTouchOutside(true)
                        .dismissOnBackPressed(true)
                        .asCustom(dialog).show();
            }

            @Override
            public void onFail(BusinessException t) {
                super.onFail(t);
                Context realContext = view.getContext();
                while (realContext instanceof ContextWrapper && !(realContext instanceof Activity)) {
                    realContext = ((ContextWrapper) realContext).getBaseContext();
                }

                while (realContext instanceof ContextWrapper && !(realContext instanceof Activity)) {
                    realContext = ((ContextWrapper) realContext).getBaseContext();
                }

                if (realContext instanceof Activity) {
                    Activity activity = (Activity) realContext;
                    // 继续你的逻辑
                    MsgDialog dialog = new MsgDialog(activity, "", t.message, true, new TipDialog.ICallBack() {
                        @Override
                        public void onClickLeft() {

                        }

                        @Override
                        public void onClickRight() {
                            if (popupView != null) {
                                popupView.dismiss();
                            }
                        }
                    });

                    popupView = new XPopup.Builder(activity)
                            .dismissOnTouchOutside(true)
                            .dismissOnBackPressed(true)
                            .asCustom(dialog).show();
                } else {
                    ToastUtils.showError(t.message);
                }

            }
        });
    }

    /*
     * 将时间转换为时间戳
     */
    public long dateToStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        return date.getTime();
    }
}
