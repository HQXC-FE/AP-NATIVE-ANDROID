package com.xtree.base.net.fastest

import com.alibaba.android.arouter.utils.TextUtils
import com.drake.net.Get
import com.drake.net.Net
import com.drake.net.scope.AndroidScope
import com.drake.net.utils.runMain
import com.drake.net.utils.scopeNet
import com.google.gson.Gson
import com.xtree.base.BuildConfig
import com.xtree.base.R
import com.xtree.base.net.RetrofitClient
import com.xtree.base.utils.AESUtil
import com.xtree.base.utils.CfLog
import com.xtree.base.utils.DomainUtil
import com.xtree.base.utils.TagUtils
import com.xtree.base.vo.Domain
import com.xtree.base.vo.EventConstant
import com.xtree.base.vo.EventVo
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.xtree.mvvmhabit.bus.event.SingleLiveData
import me.xtree.mvvmhabit.http.NetworkUtil
import me.xtree.mvvmhabit.utils.ToastUtils
import me.xtree.mvvmhabit.utils.Utils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.CancellationException


class FastestTopDomainUtil private constructor() {
    init {
        mThirdApiDomainList = ArrayList()
        mCurApiDomainList = ArrayList()
        mThirdDomainList = ArrayList()
        mTopSpeedDomainList = ArrayList()
        allTest = ArrayList()
    }

    companion object {
        @JvmStatic
        val instance: FastestTopDomainUtil by lazy { FastestTopDomainUtil() }

        /**
         * 当前预埋域名列表
         */
        private lateinit var mThirdApiDomainList: MutableList<String>
        private lateinit var mCurApiDomainList: MutableList<String>
        private lateinit var mThirdDomainList: MutableList<String>
        @set:Synchronized
        @get:Synchronized
        private lateinit var mTopSpeedDomainList: MutableList<TopSpeedDomain>

        @set:Synchronized
        @get:Synchronized
        var mIsFinish: Boolean = true
        val fastestDomain = SingleLiveData<TopSpeedDomain>()
        //是否第一次测速
        var isFirstTime: Boolean = true
        private lateinit var timerObservable: Observable<Long>

        val showTip = SingleLiveData<Boolean>()

        @set:Synchronized
        @get:Synchronized
        private lateinit var allTest: MutableList<TopSpeedDomain>
    }

    lateinit var thirdApiScopeNet : AndroidScope
    lateinit var apiScopeNet: AndroidScope

    fun start() {
        if (!NetworkUtil.isNetworkAvailable(Utils.getContext())) {
            runMain { ToastUtils.showShort("网络不可用，请检查您的手机网络是否开启") }
            EventBus.getDefault().post(EventVo(EventConstant.EVENT_TOP_SPEED_FAILED, ""))
            return
        }
        if (mIsFinish) {
            CfLog.e("=====开始线路测速========")

            Net.cancelGroup(FASTEST_GOURP_NAME)

            if (::thirdApiScopeNet.isInitialized && thirdApiScopeNet.isActive) {
                thirdApiScopeNet.cancel()
            }
            if (::apiScopeNet.isInitialized && apiScopeNet.isActive) {
                apiScopeNet.cancel()
            }

            mIsFinish = false
            index = 0
            mCurApiDomainList.clear()
            mThirdApiDomainList.clear()
            mThirdDomainList.clear()
            mTopSpeedDomainList.clear()
            allTest.clear()
            setThirdFasterDomain()
            setFasterApiDomain()

            getThirdFastestDomain(false)


            TagUtils.tagEvent(
                Utils.getContext(),
                TagUtils.EVENT_FASTEST,
                TagUtils.KEY_FASTEST_START
            )
        }
    }

    fun isFinish(): Boolean {
        return mIsFinish
    }

    fun getTopSpeedDomain(): MutableList<TopSpeedDomain> {
        return mTopSpeedDomainList
    }

    private fun addApiDomainList(list: List<String>) {
        list.forEachIndexed { _, s ->
            run {
                if (!TextUtils.isEmpty(s)) {
                    mCurApiDomainList.add(s)
                }
            }
        }
    }

    private fun addThirdDomainList(domainList: List<String>) {
        domainList.forEachIndexed { _, s ->
            run {
                if (!TextUtils.isEmpty(s)) {
                    mThirdDomainList.add(s)
                }
            }
        }
    }

    private fun getFastestApiDomain(isThird: Boolean) {

        apiScopeNet = scopeNet(Dispatchers.IO) {

            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            var curTime: Long = System.currentTimeMillis()
            val domainTasks = LineHelpUtil.makeUpApiListWithBMP(mCurApiDomainList).map { host ->
                Get<Response>(host, block = {
//                    setGroup(FASTEST_GOURP_NAME)
                    FASTEST_BLOCK(this)
                })
            }
            CfLog.i("line make domainTasks size  + ${domainTasks.size}")
            try {
                val jobs = mutableListOf<Job>()

                val mutex = Mutex()
                domainTasks.forEach {
                    val job = launch(Dispatchers.IO) {
                        try {
                            val result = it.await()
                            mutex.withLock {

                                if (isFirstTime) {
                                    return@launch
                                }

                                val fullUrl = result.request.url.toString()
                                CfLog.i("line make api get + $fullUrl")
                                var topSpeedDomain = TopSpeedDomain()
                                var url = ""
                                if (fullUrl.contains(FASTEST_API)) {
                                    url = fullUrl.replace(FASTEST_API, "")
                                    topSpeedDomain.type = 0
                                } else if (fullUrl.contains(FASTEST_API_BMP)) {
                                    url = LineHelpUtil.removePointBmpAndAfter(fullUrl)
                                    topSpeedDomain.type = 1
                                }
                                topSpeedDomain.url = url
                                topSpeedDomain.speedSec = System.currentTimeMillis() - curTime
                                topSpeedDomain.speedScore =
                                    FastestMonitorCache.getFastestScore(topSpeedDomain)
                                CfLog.e("line make 域名：api------$url---${topSpeedDomain.speedSec}")
                                //      mCurApiDomainList.remove(url)

                                //debug模式 显示所有测速线路 release模式 只显示4条
                                if (mTopSpeedDomainList.none { it.url == topSpeedDomain.url } && mTopSpeedDomainList.size < 4 || BuildConfig.DEBUG) {
                                    topSpeedDomain.isRecommend = 1;
                                    mTopSpeedDomainList.add(topSpeedDomain)
                                    mTopSpeedDomainList.sort()
                                    DomainUtil.setApiUrl(mTopSpeedDomainList[0].url)
                                    fastestDomain.postValue(mTopSpeedDomainList[0])
                                    EventBus.getDefault()
                                        .post(EventVo(EventConstant.EVENT_TOP_SPEED_FINISH, ""))
                                }

//                                if (!BuildConfig.DEBUG) {
//                                    if (mTopSpeedDomainList.size >= 4 && !mIsFinish) {
////                                        Net.cancelGroup(FASTEST_GOURP_NAME)
//                                        mIsFinish = true
//                                    }
//                                }

                                CfLog.e("line make add " + Gson().toJson(topSpeedDomain))
                                allTest.add(topSpeedDomain)

                                // 下面这段代码会造成 它之后的代码阻塞导致allTest就一个数据 独立出来
                                val response = Gson().fromJson(
                                    result.body?.string(),
                                    FastestDomainResponse::class.java
                                )
                                response?.timestamp?.let {
                                    CfLog.e("line make 域名：api------$it")
                                    topSpeedDomain.curCTSSec = it - (curTime / 1000)
                                    fastestDomain.postValue(mTopSpeedDomainList[0])
                                }

                            }
                        } catch (e: Exception) {
                            try {
                                it.cancel()
                            } catch (e: CancellationException) {
                                CfLog.e(e.toString())
                            }
                        }
                    }
                    jobs.add(job)
                }

                jobs.joinAll()
                mIsFinish = true

                //第一次测速初始化线程池，不加入计算
                if (isFirstTime) {
                    isFirstTime = false
                    delay(200)
                    getFastestApiDomain(true)
                    return@scopeNet
                }

                //如果请求后没有可用测速结果则测速失败
                if (mTopSpeedDomainList.size < 4) {
                    EventBus.getDefault()
                        .post(EventVo(EventConstant.EVENT_TOP_SPEED_FAILED, ""))
                }

                //保存测速结果
                FastestMonitorCache.saveFastestScore()

                val highSpeedList = mutableListOf<List<String>>()
                val dateFormat = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                ).format(Date(curTime))

                //对同一个域名的 接口测速和资源测速 进行数据上的融合
                allTest = LineHelpUtil.getUploadList(allTest)

                allTest.forEach {
                    //如果符合上传规则加入上传信息集合
                    val check = FastestMonitorCache.check(it)
                    if (check) {
                        val infoList =
                            listOf<String>(
                                it.url ?: "",
                                it.speedSec.toString(),
                                dateFormat,
                                it.speedScore.toString(),
                                it.isRecommend.toString(),
                                it.speedSecBmp.toString()
                            )
                        highSpeedList.add(infoList)
                        FastestMonitorCache.put(it.apply { lastUploadMonitor = curTime })
                    }
                }

                CfLog.e("line make highSpeedList " + Gson().toJson(highSpeedList))

                if (highSpeedList.isNotEmpty()) {
                    val request: Request = Request.Builder()
                        .url(DomainUtil.getApiUrl() + FASTEST_MONITOR_API)
                        .post(cjson("device_type" to "9", "data" to highSpeedList))
                        .build()
                    RetrofitClient.getInstance().okHttpClient.newCall(request).enqueue(object :
                        Callback {
                        override fun onFailure(call: Call, e: IOException) {
                        }

                        override fun onResponse(call: Call, response: Response) {
                        }
                    })
                }

            } catch (e: Exception) {
                CfLog.e(e.toString())
                TagUtils.tagEvent(
                    Utils.getContext(),
                    TagUtils.EVENT_FASTEST,
                    TagUtils.KEY_FASTEST_ERROR,
                    e.message
                )

                if (e !is CancellationException) {
                    if (isThird) { // 失败
                        mIsFinish = true
                        EventBus.getDefault()
                            .post(EventVo(EventConstant.EVENT_TOP_SPEED_FAILED, ""))
                    } else {
                        getThirdFastestDomain(true)
                    }
                }
            }
        }
    }

    var index: Int = 0

    /**
     * 三方域名存储地址竞速
     * @param needClear 是否删除清除本地预埋的竞速地址
     */
    private fun getThirdFastestDomain(needClear: Boolean) {
        if (needClear) {
            mCurApiDomainList.clear()
        }
        if (index < mThirdDomainList.size && !TextUtils.isEmpty(mThirdDomainList[index])) {

            thirdApiScopeNet = scopeNet(Dispatchers.IO) {

                try {
                    val data = Get<String>(mThirdDomainList[index], block = FASTEST_BLOCK).await()

                    var domainJson = AESUtil.decryptData(
                        data,
                        "wnIem4HOB2RKzhiqpaqbZuxtp7T36afAHH88BUht/2Y="
                    )
                    val domain: Domain = Gson().fromJson(domainJson, Domain::class.java)

                    if (!domain.api.isNullOrEmpty()) {
                        mCurApiDomainList.clear()
                    }

                    domain.api.forEachIndexed { _, domain ->
                        run {
                            if (!mCurApiDomainList.contains(domain)) {
                                mCurApiDomainList.add(domain)
                            }
                        }
                    }
                    getFastestApiDomain(true)
                    CfLog.e("getThirdFastestDomain success")

                    TagUtils.tagEvent(
                        Utils.getContext(),
                        TagUtils.EVENT_FASTEST,
                        TagUtils.KEY_FASTEST_GETTHIRDDOMAIN,
                        mThirdDomainList[index]
                    )

                } catch (e: Exception) {
                    CfLog.e("getThirdFastestDomain fail")
                    TagUtils.tagEvent(
                        Utils.getContext(),
                        TagUtils.EVENT_FASTEST,
                        TagUtils.KEY_FASTEST_GETTHIRDDOMAIN_ERROR,
                        mThirdDomainList[index]
                    )

                    index++
                    getThirdFastestDomain(needClear)
                }
            }
        } else if (mCurApiDomainList.isEmpty()) {
            mIsFinish = true
            EventBus.getDefault().post(EventVo(EventConstant.EVENT_TOP_SPEED_FAILED, ""))
        } else {
            getFastestApiDomain(true)
        }
    }

    /**
     * 线路竞速
     */
    private fun setFasterApiDomain() {
        val apis = Utils.getContext().getString(R.string.domain_api_list) // 不能为空,必须正确
        val apiList = listOf(*apis.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
        addApiDomainList(apiList)
//        if (mCurApiDomainList.size >= 4) {
//            getFastestApiDomain(false)
//        } else {
//            getThirdFastestDomain(false)
//        }
    }

    /**
     * 获取
     */
    private fun setThirdFasterDomain() {
        val urls = Utils.getContext().getString(R.string.domain_url_list_third)
        val list = listOf(*urls.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
        addThirdDomainList(list)
    }
}