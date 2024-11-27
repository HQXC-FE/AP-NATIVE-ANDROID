package com.xtree.main.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.drake.net.Get
import com.drake.net.NetConfig
import com.drake.net.transform.transform
import com.drake.net.utils.fastest
import com.drake.net.utils.scopeNet
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.net.RetrofitClient
import com.xtree.base.net.fastest.FASTEST_BLOCK
import com.xtree.base.net.fastest.FASTEST_GOURP_NAME_H5
import com.xtree.base.net.fastest.FASTEST_H5_API
import com.xtree.base.net.fastest.getFastestAPI
import com.xtree.base.utils.CfLog
import com.xtree.base.utils.DomainUtil
import com.xtree.base.utils.TagUtils
import com.xtree.main.BR
import com.xtree.main.BuildConfig
import com.xtree.main.R
import com.xtree.main.databinding.ActivitySplashBinding
import com.xtree.main.ui.viewmodel.SplashViewModel
import com.xtree.main.ui.viewmodel.factory.AppViewModelFactory
import me.xtree.mvvmhabit.base.BaseActivity
import me.xtree.mvvmhabit.bus.Messenger
import me.xtree.mvvmhabit.utils.SPUtils
import me.xtree.mvvmhabit.utils.ToastUtils

/**
 * 冷启动
 */
class SplashActivity : BaseActivity<ActivitySplashBinding?, SplashViewModel?>() {

    private val MSG_IN_MAIN: Int = 100 // 消息类型
    private val DELAY_MILLIS: Long = 2500L // 延长时间
    private var mSavedInstanceState: Bundle? = null
    private var mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            //super.handleMessage(msg)
            inMain()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSavedInstanceState = savedInstanceState
        setFullScreen()
        if (BuildConfig.DEBUG) {
            ToastUtils.showLong("Debug Model")
        }
    }

    fun setFullScreen(){
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or  // 隐藏状态栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or // 隐藏导航栏
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // 沉浸模式
                )

    }


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_splash
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initView() {
        init()
        initTag()
        setFasterApi()
        setFasterDomain()
    }

    companion object {

        /**
         * 当前预埋域名列表
         */
        lateinit var mCurDomainList: HashSet<String>
        lateinit var mCurApiList: HashSet<String>
    }

    init {
        mCurDomainList = HashSet()
        mCurApiList = HashSet()
    }

    private fun addDomainList(domainList: List<String>) {
        domainList.forEachIndexed { _, s ->
            run {
                mCurDomainList.add(s)
            }
        }
    }

    private fun addApiList(list: List<String>) {
        list.forEachIndexed { _, s ->
            run {
                mCurApiList.add(s)
            }
        }
    }

    private fun getFastestDomain() {
        scopeNet {
            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            val domainTasks = mCurDomainList.map { host ->
                Get<String>(
                    getFastestAPI(host),
                    "the_fastest_line", block = FASTEST_BLOCK)
                .transform { data ->
                    CfLog.i("$host")
                    NetConfig.host = host
                    DomainUtil.setDomainUrl(host)
                    //RetrofitClient.init() // 重置URL
                    viewModel?.reNewViewModel?.postValue(null)
                    data
                }
            }
            try {
                fastest(domainTasks, "the_fastest_line")
            } catch (e: Exception) {
                CfLog.e(e.toString())
                e.printStackTrace()
                viewModel?.noWebData?.postValue(null)
            }
        }
    }

    private fun getFastestApi() {
        scopeNet {
            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            val domainTasks = mCurApiList.map { host ->
                Get<String>(
                    getFastestAPI(host),
                    "the_fastest_api", block = FASTEST_BLOCK)
                .transform { data ->
                    CfLog.i("$host")
                    NetConfig.host = host
                    DomainUtil.setApiUrl(host)
                    RetrofitClient.init() // 重置URL
                    viewModel?.reNewViewModel?.postValue(null)
                    data
                }
            }
            try {
                fastest(domainTasks, "the_fastest_api")
            } catch (e: Exception) {
                CfLog.e(e.toString())
                e.printStackTrace()
                viewModel?.noWebData?.postValue(null)
            }
        }
    }

    private fun init() {
        val api = getString(R.string.domain_api) // 不能为空,必须正确
        val url = getString(R.string.domain_url) // 如果为空或者不正确,转用API的

        if (api.startsWith("http://") || api.startsWith("https://")) {
            DomainUtil.setApiUrl(url)
            RetrofitClient.init() // 重置URL
        }

        if (url.startsWith("http://") || url.startsWith("https://")) {
            DomainUtil.setDomainUrl(url)
            //RetrofitClient.init() // 重置URL
        } else {
            DomainUtil.setDomainUrl(api)
        }
    }

    /**
     * 线路竞速
     */
    private fun setFasterApi() {
        val apis = getString(R.string.domain_api_list) // 不能为空,必须正确
        val apiList = listOf(*apis.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
        addApiList(apiList)
        getFastestApi()
    }

    private fun setFasterDomain() {
        var urls = getString(R.string.domain_url_list) // 如果为空或者不正确,转用API的
        if (urls.length < 10) {
            urls = getString(R.string.domain_api_list) // 如果域名列表为空,就使用API列表
        }
        val list = listOf(*urls.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())

        addDomainList(list)
        getFastestDomain()
    }

    override fun initViewObservable() {
        viewModel?.inMainData?.observe(this) {
            mHandler.sendEmptyMessageDelayed(MSG_IN_MAIN, DELAY_MILLIS)
        }
        viewModel?.reNewViewModel?.observe(this) {
            RetrofitClient.init()
            AppViewModelFactory.init()
            //解除Messenger注册
            Messenger.getDefault().unregister(viewModel)
            if (viewModel != null) {
                viewModel!!.removeRxBus()
            }
            if (binding != null) {
                binding!!.unbind()
            }
            viewModel = null
            initViewDataBinding(mSavedInstanceState)
            viewModel?.setModel(AppViewModelFactory.getInstance(application).getmRepository())
            val token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN)
            if (!TextUtils.isEmpty(token)) {
                CfLog.i("getFBGameTokenApi init")
                viewModel?.getFBGameTokenApi()
                viewModel?.getFBXCGameTokenApi()
                viewModel?.getPMGameTokenApi()
            } else {
                mHandler.sendEmptyMessageDelayed(MSG_IN_MAIN, DELAY_MILLIS)
            }
        }
        viewModel?.noWebData?.observe(this) {
            ToastUtils.showLong("网络异常，请检查手机网络连接情况")
            binding?.root?.postDelayed({ finish() }, 2000)
        }
    }

    private fun initTag() {
        val token = arrayOfNulls<String>(2)
        token[0] = getString(R.string.mixpanel_token)
        token[1] = getString(R.string.ms_secret_key)
        val channel = getString(R.string.channel_name)
        val userId = SPUtils.getInstance().getString(SPKeyGlobal.USER_ID)
        var isTag = resources.getBoolean(R.bool.is_tag) && !BuildConfig.DEBUG
        TagUtils.init(baseContext, token, channel, userId, isTag)
        TagUtils.tagDailyEvent(baseContext)
    }

    /**
     * 进入主页面
     */
    private fun inMain() {

        /*if (TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN))) {
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER)
                .withInt(
                    Constant.LoginRegisterActivity_ENTER_TYPE,
                    Constant.LoginRegisterActivity_LOGIN_TYPE
                )
                .navigation();
            finish();
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish();
        }*/
        mHandler.removeMessages(MSG_IN_MAIN)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun initViewModel(): SplashViewModel? {
        val factory: AppViewModelFactory =
            AppViewModelFactory.getInstance(
                application
            )
        return ViewModelProvider(this, factory)[SplashViewModel::class.java]
    }
}