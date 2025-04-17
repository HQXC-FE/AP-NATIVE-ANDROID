package com.xtree.lottery.ui.activity

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.xtree.base.utils.CfLog
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
import com.xtree.lottery.ui.viewmodel.LotteryViewModel
import com.xtree.lottery.ui.viewmodel.factory.AppViewModelFactory
import com.xtree.lottery.utils.EventConstant
import com.xtree.lottery.utils.EventVo
import me.xtree.mvvmhabit.base.BaseActivity
import me.xtree.mvvmhabit.utils.KLog
import me.xtree.mvvmhabit.utils.ToastUtils
import org.greenrobot.eventbus.EventBus

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

    override fun initView() {
        if (Build.VERSION.SDK_INT >= 33) {
            lottery = intent.getParcelableExtra("Lottery", Lottery::class.java)!!
            //userMethods = intent.getParcelableArrayListExtra("userMethods", UserMethodsVo::class.java)!!
        } else {
            lottery = intent.getParcelableExtra<Lottery>("Lottery")!!
            //userMethods = intent.getParcelableArrayListExtra("userMethods")!!
        }
        binding.tvBack.text = lottery.name
        binding.tvBack.setOnClickListener { finish() }

        binding.vpLottery.isUserInputEnabled = false
        //binding.vpLottery.offscreenPageLimit = 1 预加载  ViewPager2默认不预加载

        binding.tlLottery.addTab(binding.tlLottery.newTab().setText("近期开奖"))
        binding.tlLottery.addTab(binding.tlLottery.newTab().setText("彩种投注"))
        if (lottery.handicap) {//是否显示盘口玩法
            binding.tlLottery.addTab(binding.tlLottery.newTab().setText("盘口玩法"))
        }
        if ("mmc".equals(lottery.alias)) {
            binding.tvTitle.visibility = View.GONE;
            binding.tvTime.visibility = View.GONE;
        } else {
            binding.tvTitle.visibility = View.GONE;
            binding.tvTime.visibility = View.GONE;
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
    }

    fun setTab0Enable() {
        val tab0 = (binding.tlLottery.getChildAt(0) as ViewGroup).getChildAt(0)
        tab0.isEnabled = true
    }

    override fun initData() {
        //viewModel.getMethodMenus(lottery.alias)
        if (!"mmc".equals(lottery.alias)) {
            viewModel.getCurrentIssue(lottery.id)
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
                        .post(EventVo(EventConstant.EVENT_TIME_FINISH, ""))
                    viewModel.getRecentLottery()
                }
            }

            timer?.start()
        }
    }


    private var timer: CountDownTimer? = null

    override fun onDestroy() {
        super.onDestroy()
        //清空当前彩种的共享数据
        LotteryDetailManager.clearData()
        timer?.cancel()
    }

    // Activity 提供方法供 Fragment 调用
    override fun onFragmentSendData(data: ArrayList<IssueVo>) {

    }

    // Activity 主动调用 Fragment 的方法
    override fun onActivitySendData(data: ArrayList<IssueVo>) {
        lotteryBetsFragment.onFragmentSendData(data)
    }
}