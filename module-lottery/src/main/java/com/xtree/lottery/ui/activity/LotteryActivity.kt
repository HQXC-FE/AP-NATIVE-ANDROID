package com.xtree.lottery.ui.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.gson.Gson
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.net.fastest.ChangeH5LineUtil
import com.xtree.base.net.fastest.FastestTopDomainUtil
import com.xtree.base.net.fastest.TopSpeedDomainFloatingWindows
import com.xtree.base.utils.CfLog
import com.xtree.base.vo.EventConstant
import com.xtree.base.vo.EventVo
import com.xtree.lottery.BR
import com.xtree.lottery.R
import com.xtree.lottery.data.LotteryDetailManager
import com.xtree.lottery.data.config.Lottery
import com.xtree.lottery.data.source.vo.IssueVo
import com.xtree.lottery.data.source.vo.MethodMenus
import com.xtree.lottery.databinding.ActivityLotteryBinding
import com.xtree.lottery.inter.ParentChildCommunication
import com.xtree.lottery.ui.fragment.ChaseBetReportFragment
import com.xtree.lottery.ui.fragment.LotteryReportFragment
import com.xtree.lottery.ui.fragment.RecentLotteryFragment
import com.xtree.lottery.ui.lotterybet.LotteryBetsFragment
import com.xtree.lottery.ui.lotterybet.LotteryHandicapFragment
import com.xtree.lottery.ui.lotterybet.viewmodel.LotteryBetConfirmViewModel
import com.xtree.lottery.ui.viewmodel.LotteryViewModel
import com.xtree.lottery.ui.viewmodel.factory.AppViewModelFactory
import com.xtree.lottery.utils.LotteryEventConstant
import com.xtree.lottery.utils.LotteryEventVo
import me.xtree.mvvmhabit.base.BaseActivity
import me.xtree.mvvmhabit.utils.KLog
import me.xtree.mvvmhabit.utils.SPUtils
import me.xtree.mvvmhabit.utils.ToastUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 彩票详情
 */
class LotteryActivity : BaseActivity<ActivityLotteryBinding, LotteryViewModel>(),
    ParentChildCommunication {
    private var isTab0Enabled = false
    private var currentIssue: IssueVo? = null
    private lateinit var lotteryBetsFragment: LotteryBetsFragment
    private var methodMenus: MethodMenus? = null
    lateinit var lottery: Lottery
    private val fragmentList = ArrayList<Fragment>()
    private var mTopSpeedDomainFloatingWindows: TopSpeedDomainFloatingWindows? = null
    private var mIsDomainSpeedChecked = false

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_lottery
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): LotteryViewModel {
        val viewModel = ViewModelProvider(this)[LotteryViewModel::class.java]
        val factory = AppViewModelFactory.getInstance(this.application)
        viewModel.setModel(factory.getmRepository())
        return viewModel
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomDensity()
        EventBus.getDefault().register(this)
        FastestTopDomainUtil.instance.start()
        ChangeH5LineUtil.instance.start()
    }

    private fun setCustomDensity() {
        // 获取当前屏幕的 DisplayMetrics
        val metrics = resources.displayMetrics

        // 设计稿的宽度和高度 (例如 812dp 宽, 375dp 高)
//        float designWidthDp = 812f;
        val designWidthDp = 375f

        // 计算 density，使用屏幕的宽度
//        float heightRatio = metrics.heightPixels / designHeightDp;
        val widthRatio = metrics.widthPixels / designWidthDp
        // 取宽高比例中较小的值进行适配
//        float targetDensity = (widthRatio < heightRatio) ? widthRatio : heightRatio;
        val targetDensity = widthRatio

        // 计算 scaledDensity
        val calculatedScaledDensity = targetDensity * (metrics.scaledDensity / metrics.density)

        // 应用新的 density 和 scaledDensity
        metrics.density = targetDensity
        metrics.scaledDensity = calculatedScaledDensity
        metrics.densityDpi = (targetDensity * 160).toInt() // 通常使用 160 作为基准 dpi
    }

    override fun initView() {
        if (Build.VERSION.SDK_INT >= 33) {
            lottery =
                intent?.getParcelableExtra("Lottery", Lottery::class.java) ?: (Gson().fromJson(
                    SPUtils.getInstance().getString("Lottery"),
                    Lottery::class.java
                ))
            //userMethods = intent.getParcelableArrayListExtra("userMethods", UserMethodsVo::class.java)!!
        } else {
            lottery = intent?.getParcelableExtra<Lottery>("Lottery") ?: (Gson().fromJson(
                SPUtils.getInstance().getString("Lottery"),
                Lottery::class.java
            ))
            //userMethods = intent.getParcelableArrayListExtra("userMethods")!!
        }
        SPUtils.getInstance().put("Lottery", Gson().toJson(lottery))
        binding.tvBack.text = lottery.name
        binding.tvBack.setOnClickListener { finish() }

        binding.vpLottery.isUserInputEnabled = false
        //binding.vpLottery.offscreenPageLimit = 1 预加载  ViewPager2默认不预加载

        binding.tlLottery.addTab(binding.tlLottery.newTab().setText("近期开奖"))
        binding.tlLottery.addTab(binding.tlLottery.newTab().setText("彩种投注"))
        if (lottery.handicap) {//是否显示盘口玩法
            binding.tlLottery.addTab(binding.tlLottery.newTab().setText("盘口玩法"))
        }
        if (TextUtils.equals("mmc", lottery.alias)) {
            binding.tvTitle.visibility = View.GONE
            binding.tvTime.visibility = View.GONE
        } else {
            binding.tvTitle.visibility = View.VISIBLE
            binding.tvTime.visibility = View.VISIBLE
        }
        binding.tlLottery.addTab(binding.tlLottery.newTab().setText("投注记录"))
        binding.tlLottery.addTab(binding.tlLottery.newTab().setText("追号记录"))
        binding.tlLottery.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpLottery.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewModel.lotteryLiveData.value = lottery

        fragmentList.add(RecentLotteryFragment.newInstance())
        lotteryBetsFragment = LotteryBetsFragment.newInstance(lottery)
        fragmentList.add(lotteryBetsFragment)
        if (lottery.handicap) {//是否显示盘口玩法
            fragmentList.add(LotteryHandicapFragment.newInstance(lottery))
        }
        fragmentList.add(LotteryReportFragment())
        fragmentList.add(ChaseBetReportFragment())

        val mAdapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getItemCount(): Int {
                return fragmentList.size
            }
        }
        binding.vpLottery.adapter = mAdapter
        val tab0 = (binding.tlLottery.getChildAt(0) as ViewGroup).getChildAt(0)
        tab0.isEnabled = false
        binding.tlLottery.getTabAt(1)?.select()

        mTopSpeedDomainFloatingWindows = TopSpeedDomainFloatingWindows(
            this@LotteryActivity
        )
        mTopSpeedDomainFloatingWindows?.show()
    }

    fun setTab0Enable() {
        val tab0 = (binding.tlLottery.getChildAt(0) as ViewGroup).getChildAt(0)
        tab0.isEnabled = true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EventVo) {
        when (event.event) {
            EventConstant.EVENT_TOP_SPEED_FINISH -> {
                CfLog.e("EVENT_TOP_SPEED_FINISH竞速完成。。。")
                mTopSpeedDomainFloatingWindows?.refresh()
                mIsDomainSpeedChecked = true
            }

            EventConstant.EVENT_TOP_SPEED_FAILED -> mTopSpeedDomainFloatingWindows!!.onError()

//            EventConstant.EVENT_LOG_OUT -> refreshRechargeFloatingWindows();
        }
    }

    override fun initData() {
        //viewModel.getMethodMenus(lottery.alias)
        if (!"mmc".equals(lottery.alias)) {
            viewModel.getCurrentIssue(lottery.id)
        }
    }


    /**
     * 启动充提记录悬浮窗
     */
    var mRechargeFloatingWindows: Any? = null
    private var isFloating = false
    private fun refreshRechargeFloatingWindows() {
        if (!isFloating) {
            CfLog.i("rechargeFloatingWindows.show")
            try {
                val myClass = Class.forName("com.xtree.home.ui.custom.view.RechargeFloatingWindows")
                val constructor = myClass.getConstructor(Context::class.java)
                mRechargeFloatingWindows = constructor.newInstance(this)
                val method = mRechargeFloatingWindows?.javaClass?.getMethod("show")
                method?.invoke(mRechargeFloatingWindows)
            } catch (e: Exception) {
                CfLog.e(e.message)
            }
            isFloating = true
        }
        if (TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN))) {
            try {
                val method = mRechargeFloatingWindows?.javaClass?.getMethod("removeView")
                method?.invoke(mRechargeFloatingWindows)
                isFloating = false
            } catch (e: Exception) {
                CfLog.e(e.message)
            }

        }
    }

    override fun initViewObservable() {
        viewModel.liveDataMethodMenus.observe(this) {
            methodMenus = it
        }
        viewModel.liveDataCurrentIssue.observe(this) {
            currentIssue = it
            currentIssue?.apply {
                viewModel.getTrackingIssue(lottery.id)
            }
        }
        viewModel.liveDataListIssue.observe(this) {
            LotteryDetailManager.mIssues = it
            if (it.isNotEmpty()) {
                for (i in it.indices) {
                    if (it[i].issue == currentIssue?.issue) {
                        countDownTimer(i)
                        break
                    }
                }

            }
        }
        viewModel.liveDataCloseLotteryDetail.observe(this) {
            finish()
        }
    }

    //倒计时的方式
    private fun countDownTimer(index: Int) {
        if (index >= LotteryDetailManager.mIssues.size) {
            return
        }
        LotteryDetailManager.mIndex = index
        LotteryDetailManager.mIssues[index].apply {

            viewModel.currentIssueLiveData.value = this
            val viewmodel = ViewModelProvider(
                this@LotteryActivity
            ).get(
                LotteryBetConfirmViewModel::class.java
            )
            viewmodel.chasingNumberParams.value = null//清空追号
            if (issue.contains("-")) {
                binding.tvTitle.text = issue.split("-")[1].plus("期")
            } else {
                binding.tvTitle.text = issue.plus("期")
            }

            val countdown =
                viewModel.dateToStamp(saleend) - System.currentTimeMillis()
            KLog.i("countdown", countdown)

            timer = object : CountDownTimer(countdown, 1000L) {

                override fun onTick(millisUntilFinished: Long) {
                    CfLog.w("当时计数：$millisUntilFinished")

                    val hours = millisUntilFinished / (1000 * 60 * 60)
                    val minutes = millisUntilFinished % (1000 * 60 * 60) / (1000 * 60)
                    val seconds = millisUntilFinished % (1000 * 60) / 1000
                    binding.tvTime.text =
                        String.format(" %02d : %02d : %02d ", hours, minutes, seconds)

                }

                override fun onFinish() {
                    ToastUtils.showLong("当前销售已截至，请进入下一期购买")
                    CfLog.w("倒计时结束了...")
                    countDownTimer(index + 1)
                    EventBus.getDefault()
                        .post(LotteryEventVo(LotteryEventConstant.EVENT_TIME_FINISH, ""))
                    viewModel.getRecentLottery()
                }
            }

            timer?.start()
        }
    }


    private var timer: CountDownTimer? = null

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        //清空当前彩种的共享数据
        LotteryDetailManager.clearData()
        timer?.cancel()

        if (mTopSpeedDomainFloatingWindows != null) {
            mTopSpeedDomainFloatingWindows!!.removeView()
        }
    }

    override fun onResume() {
        super.onResume()
        setCustomDensity()
//        refreshRechargeFloatingWindows()
    }

    // Activity 提供方法供 Fragment 调用
    override fun onFragmentSendData(data: ArrayList<IssueVo>) {

    }

    // Activity 主动调用 Fragment 的方法
    override fun onActivitySendData(data: ArrayList<IssueVo>) {
        lotteryBetsFragment.onFragmentSendData(data)
    }
}