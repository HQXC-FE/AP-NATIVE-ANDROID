package com.xtree.lottery.ui.activity

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.xtree.lottery.data.config.Lottery
import com.xtree.base.utils.CfLog
import com.xtree.lottery.BR
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.IssueVo
import com.xtree.lottery.data.source.vo.MethodMenus
import com.xtree.lottery.data.source.vo.UserMethodsVo
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
class LotteryActivity : BaseActivity<ActivityLotteryBinding, LotteryViewModel>(), ParentChildCommunication {
    public var mIndex = 0
    public var mIssues = ArrayList<IssueVo>()
    private lateinit var lotteryBetsFragment: LotteryBetsFragment
    private var methodMenus: MethodMenus? = null
    lateinit var lottery: Lottery
    var userMethods = ArrayList<UserMethodsVo>()
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
        //binding.vpLottery.offscreenPageLimit = 1 预加载  viewModel默认不预加载

        binding.tlLottery.addTab(binding.tlLottery.newTab().setText("近期开奖"))
        binding.tlLottery.addTab(binding.tlLottery.newTab().setText("彩种投注"))
        binding.tlLottery.addTab(binding.tlLottery.newTab().setText("盘口玩法"))
        binding.tlLottery.addTab(binding.tlLottery.newTab().setText("投注记录"))
        binding.tlLottery.addTab(binding.tlLottery.newTab().setText("追号记录"))
        binding.tlLottery.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpLottery.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        fragmentList.add(RecentLotteryFragment.newInstance(lottery.id))
        lotteryBetsFragment = LotteryBetsFragment.newInstance(lottery)
        fragmentList.add(lotteryBetsFragment)
        fragmentList.add(LotteryHandicapFragment.newInstance(lottery))
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
        binding.tlLottery.getTabAt(1)?.select()
    }

    override fun initData() {
        viewModel.getMethodMenus(lottery.alias)
        viewModel.getCurrentIssue(lottery.id)
        viewModel.getTrackingIssue(lottery.id)
    }

    override fun initViewObservable() {
        viewModel.liveDataMethodMenus.observe(this) {
            methodMenus = it
        }
        viewModel.liveDataCurrentIssue.observe(this) {

        }
        viewModel.liveDataListIssue.observe(this) {
            mIssues = it
            countDownTimer(0)
        }
    }

    //倒计时的方式
    private fun countDownTimer(index: Int) {
        mIndex = index
        mIssues[index].apply {
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
                    binding.tvTime.text = String.format(" %02d : %02d : %02d ", hours, minutes, seconds)

                }

                override fun onFinish() {
                    ToastUtils.showLong("当前销售已截至，请进入下一期购买")
                    CfLog.w("倒计时结束了...")
                    countDownTimer(index + 1)
                    EventBus.getDefault()
                        .post(EventVo(EventConstant.EVENT_TIME_FINISH, ""))
                }
            }

            timer?.start()
        }
    }


    private var timer: CountDownTimer? = null

    override fun onDestroy() {
        super.onDestroy()

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