package com.xtree.lottery.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.Gson
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.net.RetrofitClient
import com.xtree.base.router.RouterActivityPath
import com.xtree.base.vo.ProfileVo
import com.xtree.lottery.BR
import com.xtree.lottery.R
import com.xtree.lottery.data.LotteryDataManager
import com.xtree.lottery.data.config.lotteries
import com.xtree.lottery.data.source.vo.Data
import com.xtree.lottery.databinding.ActivityMainLtBinding
import com.xtree.lottery.ui.adapter.LotteryAdapter
import com.xtree.lottery.ui.view.PrizeInfo
import com.xtree.lottery.ui.view.PrizeNoticeView
import com.xtree.lottery.ui.viewmodel.LotteryViewModel
import com.xtree.lottery.ui.viewmodel.factory.AppViewModelFactory
import com.xtree.lottery.utils.LotteryEventConstant
import com.xtree.lottery.utils.LotteryEventVo
import com.xtree.lottery.utils.LotteryPolling
import me.xtree.mvvmhabit.base.BaseActivity
import me.xtree.mvvmhabit.utils.SPUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 彩种选择页面
 */
@Route(path = RouterActivityPath.Lottery.PAGER_LOTTERY_HOME)
class LotterySelectActivity : BaseActivity<ActivityMainLtBinding, LotteryViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main_lt
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initView() {
        binding.ivBack.setOnClickListener { finish() }
        binding.rvLottery.setHasFixedSize(true)
        binding.rvLottery.layoutManager = LinearLayoutManager(this)
    }

    override fun initData() {
        val adapter = LotteryAdapter(lotteries) {
            if (LotteryDataManager.staticLotteryMethodsData != null) {
                val intent = Intent(this, LotteryActivity::class.java)
                intent.putExtra("Lottery", it)
                startActivity(intent)
            }
        }
        binding.rvLottery.adapter = adapter
        LotteryPolling.startPollingWithOkHttp()
    }

    override fun initViewObservable() {

    }

    override fun initViewModel(): LotteryViewModel? {
        val factory: AppViewModelFactory =
            AppViewModelFactory.getInstance(
                application
            )
        return ViewModelProvider(this, factory)[LotteryViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        LotteryPolling.stop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LotteryEventVo) {
        when (event.event) {
            LotteryEventConstant.EVENT_PRIZI_N0TICE -> {
                val prizeNotice = PrizeNoticeView(binding.layout1)
                val data = event.data as Data
                prizeNotice.showPrize(
                    PrizeInfo(
                        bonus = data.bonus,
                        issue = data.issue
                    )
                )
            }
        }
    }

    /**
     * 正式环境
     */
    fun setLoginSucc() {
        SPUtils.getInstance().put(
            SPKeyGlobal.USER_TOKEN,
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC93d3cudTJzNHM3Lnh5elwvYXBpXC9hdXRoXC9sb2dpbiIsImlhdCI6MTczMzMwMzM0MCwiZXhwIjoxNzMzOTA4MTQwLCJuYmYiOjE3MzMzMDMzNDAsImp0aSI6IndoN25SemZkczlwWjhCYWQiLCJzdWIiOiI1MjIyNDAyIiwicHJ2IjoiZmNjNDk1M2ZlZDEzOWE4MTdiOTkzNGE2NzY0MjM5YmQ0MDVkYzBlMSIsInVzZXJuYW1lIjoiaml1aGFvMDAxQGUyIiwic2Vzc2lkIjoiODQxZTRjZGZkZmRmZjBiYzIyYjAzNWY4OGQ3OGRhMzczODQxOTRkYzA4NzAwNDc2ZDQ3NDUyNTY0YWEzMzM2MyJ9.YTIyYjRhYjA3MGI3M2Y0NjgwZWZkMzlhM2U1MTRlZDEwZDc2YzNkZjNiMWFmOWRkYzUzZDM5ZTBlZTRmYmM1ZA",
        )
        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN_TYPE, "bearer")
        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_SESSID, "841e4cdfdfdff0bc22b035f88d78da37384194dc08700476d47452564aa33363")
        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_COOKIE_NAME, "_sessionHandler")
        SPUtils.getInstance().put(SPKeyGlobal.USER_NAME, "jiuhao001") // 用户名
        // 解决登录后,首页显示为未登录,过2秒才显示登录名和金额的问题
        SPUtils.getInstance()
            .put(SPKeyGlobal.HOME_PROFILE, Gson().toJson(ProfileVo("jiuhao001", "***")))
        RetrofitClient.init()
    }

    /**
     * 测试环境
     */
    fun setLoginSuccTest() {
        SPUtils.getInstance().put(
            SPKeyGlobal.USER_TOKEN,
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9wcmV5aWFuZzJ0ZXN0Lm94bGRrbS5jb21cL2FwaVwvYXV0aFwvbG9naW4iLCJpYXQiOjE3MjQyMjAzNjksImV4cCI6MTcyNDgyNTE2OSwibmJmIjoxNzI0MjIwMzY5LCJqdGkiOiI2Y2dhMGgxSUMxOHdiS25OIiwic3ViIjoiMjc5MjQyOCIsInBydiI6ImZjYzQ5NTNmZWQxMzlhODE3Yjk5MzRhNjc2NDIzOWJkNDA1ZGMwZTEiLCJ1c2VybmFtZSI6ImRhbmllbDJAZTIiLCJzZXNzaWQiOiJiMTgzOWVlNDEwZTljY2RiZTE2ZWIyOTJlMDRlODRkZjQ0OWQ5MTlkOWYzNTc2NGZjM2VjNWRjNTkxMjZkNmEyIn0.YzM4YjliNjYzMTk4ZjA4MGFiOWI5NjQ2ZjJmM2Y2YWY4ZTM0NDIwNTMyMGNkZGI5YmUwZTg0MzQwODNmODM5OQ"
        );
        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN_TYPE, "bearer")
        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_SESSID, "b1839ee410e9ccdbe16eb292e04e84df449d919d9f35764fc3ec5dc59126d6a2")
        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_COOKIE_NAME, "_sessionHandler")
        SPUtils.getInstance().put(SPKeyGlobal.USER_NAME, "daniel2") // 用户名
        // 解决登录后,首页显示为未登录,过2秒才显示登录名和金额的问题
        SPUtils.getInstance()
            .put(SPKeyGlobal.HOME_PROFILE, Gson().toJson(ProfileVo("daniel2", "***")))
        RetrofitClient.init()
    }
}