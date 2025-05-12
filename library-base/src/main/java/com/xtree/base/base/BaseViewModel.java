package com.xtree.base.base;

import android.app.Application;
import android.os.Bundle;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.trello.rxlifecycle4.LifecycleProvider;
import com.xtree.base.bus.event.SingleLiveData;
import com.xtree.base.http.BaseResponse;
import com.xtree.base.http.BusinessException;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.HttpCallBackCacheManager;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.KLog;
import com.xtree.base.utils.RxUtils;
import com.xtree.base.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.sentry.Sentry;

/**
 * Created by goldze on 2017/6/15.
 */
public class BaseViewModel<M extends BaseModel> extends AndroidViewModel implements IBaseViewModel, Consumer<Disposable> {

    public final static int ONFINISH_REFRESH = 1;
    public final static int ONFINISH_LOAD_MORE = 2;
    public final static int ON_LOAD_MORE_WITH_NO_MORE_DATA = 3;
    public final static int ONFINISH_REFRESH_FAILED = 4;
    public final static int ONFINISH_LOAD_MORE_FAILED = 5;
    public final static int ONFINISH_NO_MORE = 6;
    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private final CompositeDisposable mCompositeDisposable;
    protected M model;
    private UIChangeLiveData uc;
    //弱引用持有
    private WeakReference<LifecycleProvider> lifecycle;

    public BaseViewModel(@NonNull Application application) {
        this(application, null);
    }

    public BaseViewModel(@NonNull Application application, M model) {
        super(application);
        this.model = model;
        mCompositeDisposable = new CompositeDisposable();
    }

    public void setModel(BaseModel model) {
        this.model = (M) model;
    }

    public void addSubscribe(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    public void removeSubscribe(Disposable disposable) {
        mCompositeDisposable.remove(disposable);
    }

    public CompositeDisposable getmCompositeDisposable() {
        return mCompositeDisposable;
    }


    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    public void injectLifecycleProvider(LifecycleProvider lifecycle) {
        this.lifecycle = new WeakReference<>(lifecycle);
    }

    public LifecycleProvider getLifecycleProvider() {
        return lifecycle.get();
    }

    public UIChangeLiveData getUC() {
        if (uc == null) {
            uc = new UIChangeLiveData();
        }
        return uc;
    }

    public void showDialog() {
        showDialog("请稍后...");
    }

    public void showDialog(String title) {
        uc.showDialogEvent.postValue(title);
    }

    public void dismissDialog() {
        uc.dismissDialogEvent.call();
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CLASS, clz);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        uc.startActivityEvent.postValue(params);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName, null);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public void startContainerActivity(String canonicalName, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CANONICAL_NAME, canonicalName);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        uc.startContainerActivityEvent.postValue(params);
    }

    /**
     * 关闭界面
     */
    public void finish() {
        uc.finishEvent.call();
    }

    /**
     * 返回上一层
     */
    public void onBackPressed() {
        uc.onBackPressedEvent.call();
    }

    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void registerRxBus() {
    }

    @Override
    public void removeRxBus() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (model != null) {
            model.onCleared();
        }
        //ViewModel销毁时会执行，同时取消所有异步任务
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    public void finishLoadMore(boolean success) {
        if (success) {
            uc.smartRefreshListenerEvent.postValue(ONFINISH_LOAD_MORE);
        } else {
            uc.smartRefreshListenerEvent.postValue(ONFINISH_LOAD_MORE_FAILED);
        }
    }

    public void finishRefresh(boolean success) {
        if (success) {
            uc.smartRefreshListenerEvent.postValue(ONFINISH_REFRESH);
        } else {
            uc.smartRefreshListenerEvent.postValue(ONFINISH_REFRESH_FAILED);
        }
    }

    public void loadMoreWithNoMoreData() {
        uc.smartRefreshListenerEvent.postValue(ON_LOAD_MORE_WITH_NO_MORE_DATA);
    }

    @Override
    public void accept(Disposable disposable) throws Exception {
        addSubscribe(disposable);
    }

    //通用的 API 请求方法，不需要错误回调
    protected <T> void execute(Flowable<BaseResponse<T>> flowable, Consumer<T> onSuccess) {
        nativeExecute(flowable, onSuccess, null, true);
    }

    //通用的 API 请求方法
    protected <T> void execute(Flowable<BaseResponse<T>> flowable, Consumer<T> onSuccess, Consumer<Throwable> onError) {
        nativeExecute(flowable, onSuccess, onError, true);
    }

    //带本地缓存的通用的 API 请求方法
    protected <T> void execute(Flowable<BaseResponse<T>> flowable, Type type, String key, Consumer<T> onSuccess, Consumer<Throwable> onError) {
        // 本地缓存数据流
        Flowable localFlowable = HttpCallBackCacheManager.getInstance().getCache(key, type).onErrorResumeNext(Flowable.empty()); // 本地读取失败时返回空流

        Flowable remoteFlowable = flowable.doOnNext(data -> {
            HttpCallBackCacheManager.getInstance().saveCache(key, data);
        }).doOnCancel(() -> {
            nativeExecute(flowable, onSuccess, onError, true);
        });
        nativeExecute(Flowable.concat(localFlowable, remoteFlowable), onSuccess, onError, true);
    }

    //带本地缓存和异常信息的通用的 API 请求方法
    protected <T> void execute(Flowable<BaseResponse<T>> flowable, Type type, String key, BiConsumer<T, BusinessException> onSuccess, Consumer<Throwable> onError) {
        // 本地缓存数据流
        Flowable localFlowable = HttpCallBackCacheManager.getInstance().getCache(key, type).onErrorResumeNext(Flowable.empty()); // 本地读取失败时返回空流

        Flowable remoteFlowable = flowable.doOnNext(data -> {
            HttpCallBackCacheManager.getInstance().saveCache(key, data);
        }).doOnCancel(() -> {
            execute(flowable, onSuccess, onError);
        });
        execute(Flowable.concat(localFlowable, remoteFlowable), onSuccess, onError);
    }

    private <T> void nativeExecute(Flowable<BaseResponse<T>> flowable, Consumer<T> onSuccess, @Nullable Consumer<Throwable> onError, Boolean finishCloseLoading) {
        Disposable disposable = (Disposable) flowable.compose(RxUtils.schedulersTransformer()).compose(RxUtils.exceptionTransformer()).subscribeWith(new HttpCallBack<T>() {
            @Override
            public void onResult(T o) {
                try {
                    onSuccess.accept(o);  // 成功回调
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError);  // 安全处理 onError
                }
            }

            @Override
            public void onResult(T t, BusinessException ex) {
                CfLog.e("protected <T> void execute -- >" + ex.toString());
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                try {
                    onError.accept(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError);  // 安全处理 onError
                }
            }

            @Override
            public void onFail(BusinessException t) {
                super.onFail(t);
                try {
                    onError.accept(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError);  // 安全处理 onError
                }
            }

            @Override
            public void onComplete() {
                if (finishCloseLoading) {
                    super.onComplete();
                }
            }
        });
        addSubscribe(disposable);  // 将 Disposable 添加到管理
    }

    //可返回null data的通用api方法
    protected <T> void execute(Flowable<BaseResponse<T>> flowable, BiConsumer<T, BusinessException> onSuccess, Consumer<Throwable> onError) {
        Disposable disposable = (Disposable) flowable.compose(RxUtils.schedulersTransformer()).compose(RxUtils.exceptionTransformer()).subscribeWith(new HttpCallBack<T>() {
            @Override
            public void onResult(T o) {
            }

            @Override
            public void onResult(T o, BusinessException ex) {
                try {
                    onSuccess.accept(o, ex);  // 成功回调
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError);  // 安全处理 onError
                }
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                try {
                    onError.accept(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError);  // 安全处理 onError
                }
            }

            @Override
            public void onFail(BusinessException t) {
                super.onFail(t);
                try {
                    onError.accept(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError);  // 安全处理 onError
                }
            }
        });
        addSubscribe(disposable);  // 将 Disposable 添加到管理
    }

    //原样返回T的扩展api方法
    protected <T> void executeExt(Flowable<T> flowable, Consumer<T> onSuccess, Consumer<Throwable> onError) {
        Disposable disposable = (Disposable) flowable.compose(RxUtils.schedulersTransformer()).compose(RxUtils.exceptionTransformer()).subscribeWith(new HttpCallBack<T>() {
            @Override
            public void onResult(T o) {
                try {
                    onSuccess.accept(o);  // 成功回调
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError);  // 安全处理 onError
                }
            }

            @Override
            public void onResult(T t, BusinessException ex) {
                CfLog.e("protected <T> void executeExt -- >" + ex.toString());
            }

//                    @Override
//                    public void onResult(T o, BusinessException ex) {
//                        try {
//                            onSuccess.accept(o, ex);  // 成功回调
//                        } catch (Exception e) {
//                            logError(e);  // 记录日志
//                            safeOnError(e, onError);  // 安全处理 onError
//                        }
//                    }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                try {
                    onError.accept(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError);  // 安全处理 onError
                }
            }

            @Override
            public void onFail(BusinessException t) {
                super.onFail(t);
                try {
                    onError.accept(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError);  // 安全处理 onError
                }
            }
        });
        addSubscribe(disposable);  // 将 Disposable 添加到管理
    }

    /**
     * 两个任务同时执行和返回结果，第一个是主任务，第二个是辅任务，辅任务可以为空(顺序不能颠倒了）
     *
     * @param flowable1
     * @param flowable2
     * @param onSuccess
     * @param onError1
     * @param onError2
     * @param <T>
     * @param <U>
     */
    protected <T, U> void execute(Flowable<BaseResponse<T>> flowable1, Flowable<BaseResponse<U>> flowable2, BiConsumer<Pair<T, BusinessException>, U> onSuccess, Consumer<Throwable> onError1, Consumer<Throwable> onError2) {

        // 创建一个 CountDownLatch，等待两个任务完成
        CountDownLatch latch = new CountDownLatch(2);

        // 使用 AtomicReference 存储两个结果
        AtomicReference<Pair<T, BusinessException>> result1 = new AtomicReference<>();
        AtomicReference<U> result2 = new AtomicReference<>();

        // 执行第一个主任务
        Disposable disposable1 = (Disposable) flowable1.compose(RxUtils.schedulersTransformer()).compose(RxUtils.exceptionTransformer()).subscribeWith(new HttpCallBack<T>() {
            @Override
            public void onResult(T o) {
            }

            @Override
            public void onResult(T o, BusinessException ex) {
                try {
                    result1.set(new Pair<>(o, ex));
                    latch.countDown();
                    checkAndInvokeCallback(latch, result1, result2, onSuccess);
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError1);  // 安全处理 onError
                }
            }

            @Override
            public void onError(Throwable t) {
                try {
                    onError1.accept(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError1);  // 安全处理 onError
                }
            }

            @Override
            public void onFail(BusinessException t) {
                try {
                    onError1.accept(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError1);  // 安全处理 onError
                }
            }
        });

        // 执行第二个辅任务
        Disposable disposable2 = (Disposable) flowable2.compose(RxUtils.schedulersTransformer()).compose(RxUtils.exceptionTransformer()).subscribeWith(new HttpCallBack<U>() {
            @Override
            public void onResult(U o) {
            }

            @Override
            public void onResult(U o, BusinessException ex) {
                try {
                    result2.set(o);
                    latch.countDown();
                    checkAndInvokeCallback(latch, result1, result2, onSuccess);
                    onError2.accept(ex);
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError2);  // 安全处理 onError
                }
            }

            @Override
            public void onError(Throwable t) {
                try {
                    result2.set(null);
                    latch.countDown();
                    checkAndInvokeCallback(latch, result1, result2, onSuccess);
                    onError2.accept(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError2);  // 安全处理 onError
                }
            }

            @Override
            public void onFail(BusinessException t) {
                try {
                    result2.set(null);
                    latch.countDown();
                    checkAndInvokeCallback(latch, result1, result2, onSuccess);
                    onError2.accept(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    logError(e);  // 记录日志
                    safeOnError(e, onError2);  // 安全处理 onError
                }
            }
        });

        addSubscribe(disposable1);
        addSubscribe(disposable2);

    }

    // 定义检查和触发回调的方法
    private <T, U> void checkAndInvokeCallback(CountDownLatch latch, AtomicReference<Pair<T, BusinessException>> result1, AtomicReference<U> result2, BiConsumer<Pair<T, BusinessException>, U> onSuccess) throws Exception {
        if (latch.getCount() == 0) { // 检查是否两个任务都完成
            onSuccess.accept(result1.get(), result2.get());
        }
    }

    // 安全处理 onError 的方法
    private void safeOnError(Throwable t, @Nullable Consumer<Throwable> onError) {
        try {
            if (onError != null) {
                onError.accept(t);  // 处理错误回调
            }
        } catch (Exception e) {
            e.printStackTrace();
            logError(e);  // 记录 onError 过程中发生的异常
        }
    }

    // 日志记录方法
    // 日志记录方法
    private void logError(Throwable t) {
        KLog.e("error: " + t.getMessage());
        ToastUtils.showShort("error:", t.getMessage());
        Sentry.captureException(t);
    }

    public static final class ParameterField {
        public static String CLASS = "CLASS";
        public static String CANONICAL_NAME = "CANONICAL_NAME";
        public static String BUNDLE = "BUNDLE";
    }

    public final class UIChangeLiveData extends SingleLiveData {
        private final MutableLiveData<Void> reStartActivity = new MutableLiveData<>();
        private final MutableLiveData<Void> noWebData = new MutableLiveData<>();
        private SingleLiveData<String> showDialogEvent;
        private SingleLiveData<Void> dismissDialogEvent;
        private SingleLiveData<Map<String, Object>> startActivityEvent;
        private SingleLiveData<Map<String, Object>> startContainerActivityEvent;
        private SingleLiveData<Void> finishEvent;
        private SingleLiveData<Integer> smartRefreshListenerEvent;
        private SingleLiveData<Void> onBackPressedEvent;

        public SingleLiveData<String> getShowDialogEvent() {
            return showDialogEvent = createLiveData(showDialogEvent);
        }

        public SingleLiveData<Void> getDismissDialogEvent() {
            return dismissDialogEvent = createLiveData(dismissDialogEvent);
        }

        public SingleLiveData<Map<String, Object>> getStartActivityEvent() {
            return startActivityEvent = createLiveData(startActivityEvent);
        }

        public SingleLiveData<Map<String, Object>> getStartContainerActivityEvent() {
            return startContainerActivityEvent = createLiveData(startContainerActivityEvent);
        }

        public SingleLiveData<Void> getFinishEvent() {
            return finishEvent = createLiveData(finishEvent);
        }

        public SingleLiveData<Integer> getSmartRefreshListenerEvent() {
            return smartRefreshListenerEvent = createLiveData(smartRefreshListenerEvent);
        }

        public SingleLiveData<Void> getOnBackPressedEvent() {
            return onBackPressedEvent = createLiveData(onBackPressedEvent);
        }


        private <T> SingleLiveData<T> createLiveData(SingleLiveData<T> liveData) {
            if (liveData == null) {
                liveData = new SingleLiveData<>();
            }
            return liveData;
        }

        @Override
        public void observe(LifecycleOwner owner, Observer observer) {
            super.observe(owner, observer);
        }
    }
}
