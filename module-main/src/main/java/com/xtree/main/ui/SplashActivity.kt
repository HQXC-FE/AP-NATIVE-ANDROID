package com.xtree.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.net.RetrofitClient
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

    private val DELAY_MILLIS: Long = 100L // 延长时间
    private var mSavedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSavedInstanceState = savedInstanceState

        if (BuildConfig.DEBUG) {
            ToastUtils.showLong("Debug Model")
        }
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
        binding?.root?.postDelayed({ inMain() }, DELAY_MILLIS)
    }

    private fun init() {
        val api = getString(R.string.domain_api) // 不能为空,必须正确
        val url = getString(R.string.domain_url) // 如果为空或者不正确,转用API的

        if (api.startsWith("http://") || api.startsWith("https://")) {
            DomainUtil.setApiUrl(api)
        }

        if (url.startsWith("http://") || url.startsWith("https://")) {
            DomainUtil.setDomainUrl(url)
        } else {
            DomainUtil.setDomainUrl(api)
        }
    }

    override fun initViewObservable() {
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