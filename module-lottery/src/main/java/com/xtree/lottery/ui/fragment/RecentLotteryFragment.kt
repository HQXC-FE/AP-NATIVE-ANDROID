package com.xtree.lottery.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.xtree.base.widget.LoadingDialog
import com.xtree.lottery.BR
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.RecentLotteryVo
import com.xtree.lottery.databinding.FragmentRecentLotteryBinding
import com.xtree.lottery.rule.RecentlyEntryRule
import com.xtree.lottery.rule.betting.data.RulesEntryData
import com.xtree.lottery.ui.adapter.RecentAdapter
import com.xtree.lottery.ui.viewmodel.LotteryViewModel
import com.xtree.lottery.ui.viewmodel.factory.AppViewModelFactory
import com.xtree.lottery.utils.EventConstant
import com.xtree.lottery.utils.EventVo
import me.xtree.mvvmhabit.base.BaseFragment
import me.xtree.mvvmhabit.utils.KLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 近期开奖
 */
class RecentLotteryFragment : BaseFragment<FragmentRecentLotteryBinding, LotteryViewModel>() {

    private var currentData: RulesEntryData? = null
    private var mList = ArrayList<RecentLotteryVo>()
    private lateinit var mAdapter: RecentAdapter

    companion object {
        fun newInstance(): RecentLotteryFragment {
            val fragment = RecentLotteryFragment()
            val bundle = Bundle()
            //fragment传参数，谷歌官方建议使用setArguments
            //使用有参构造函数传参数，依附的Activity重建时，Fragment会调取无参构造函数重建，没有无参构造就会闪退
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun initImmersionBar() {

    }


    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_recent_lottery
    }


    override fun initVariableId(): Int {
        return BR.model
    }

    override fun initViewModel(): LotteryViewModel {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        //val factory = AppViewModelFactory.getInstance(requireActivity().application)
        //return ViewModelProvider(requireActivity(), factory)[LotteryViewModel::class.java]
        val viewModel = ViewModelProvider(requireActivity())[LotteryViewModel::class.java]
        val factory = AppViewModelFactory.getInstance(requireActivity().application)
        viewModel.setModel(factory.getmRepository())
        return viewModel
    }

    override fun initView() {
        binding.rvRecent.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = RecentAdapter(ArrayList())
        binding.rvRecent.adapter = mAdapter
        mAdapter.setEmptyView(R.layout.layout_no_data)
        viewModel.getRecentLottery()
        binding.btRefresh.setOnClickListener {
            LoadingDialog.show(context)
            viewModel.getRecentLottery()
        }
    }

    override fun initViewObservable() {
        viewModel.liveDataRecentList.observe(viewLifecycleOwner) {
            mList = it
            if (currentData != null) {
                setList()
                binding.rvRecent.scrollToPosition(0)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: EventVo) {
        when (event.event) {
            EventConstant.EVENT_TIME_FINISH -> {
                //viewModel.getRecentLottery()
            }

            EventConstant.EVENT_RULES_ENTRY_DATA -> {
                currentData = event.data as RulesEntryData
                KLog.i("RulesEntryData1", Gson().toJson(currentData))
                setList()
            }
        }
    }

    private fun setList() {
        synchronized(this) {
            val result = RecentlyEntryRule.getInstance().startEngine(currentData, mList)
            if (result.title.isEmpty()) {
                binding.tvStatusTop.visibility = View.GONE
            } else {
                binding.tvStatusTop.visibility = View.VISIBLE
                binding.tvStatusTop.text = result.title
            }
            //KLog.i("RulesEntryData2", Gson().toJson(result))
            mAdapter.setList(result.histories)
        }
    }

}