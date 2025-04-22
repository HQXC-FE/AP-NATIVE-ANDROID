package com.xtree.lottery.ui.lotterybet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.xtree.lottery.R
import com.xtree.lottery.data.LotteryDetailManager
import com.xtree.lottery.databinding.DialogChasingNumberBinding
import com.xtree.lottery.ui.adapter.ChasingAdapter
import com.xtree.lottery.ui.lotterybet.model.ChasingNumberRequestModel
import com.xtree.lottery.ui.lotterybet.viewmodel.LotteryBetConfirmViewModel
import com.xtree.lottery.ui.lotterybet.viewmodel.LotteryBetsViewModel
import com.xtree.lottery.ui.lotterybet.viewmodel.LotteryOrderViewModel
import com.xtree.lottery.utils.LotteryEventConstant
import com.xtree.lottery.utils.LotteryEventVo
import me.xtree.mvvmhabit.base.BaseDialogFragment
import me.xtree.mvvmhabit.utils.ToastUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.pow


/**
 * Created by Daniel
 * Describe: 追号弹窗
 */
open class LotteryChasingNumberFragment private constructor() : BaseDialogFragment<DialogChasingNumberBinding, LotteryOrderViewModel>() {
    private var money = 0.0
    private var betNums = ""
    private lateinit var chasingAdapter: ChasingAdapter
    private var checkPosition = 0
    var allMoney = 0.0

    override fun initView() {
        binding.ivClose.setOnClickListener { dismissAllowingStateLoss() }
        binding.tvCancel.setOnClickListener { dismissAllowingStateLoss() }
        initRv()

        binding.tlChasing.addTab(binding.tlChasing.newTab().setText("利润率追号"))
        binding.tlChasing.addTab(binding.tlChasing.newTab().setText("同倍追号"))
        binding.tlChasing.addTab(binding.tlChasing.newTab().setText("翻倍追号"))
        binding.tlChasing.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                checkPosition = tab.position
                when (tab.position) {
                    0 -> {
                        binding.plus3.visibility = View.GONE
                        binding.plus4.visibility = View.GONE
                        binding.plus5.visibility = View.VISIBLE
                    }

                    1 -> {
                        binding.plus3.visibility = View.GONE
                        binding.plus4.visibility = View.GONE
                        binding.plus5.visibility = View.GONE
                    }

                    2 -> {
                        binding.plus3.visibility = View.VISIBLE
                        binding.plus4.visibility = View.VISIBLE
                        binding.plus5.visibility = View.GONE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // 设置初始值
        binding.plus1.setNumber(1, "追号倍数", 99999)
        binding.plus2.setNumber(10, "追号期数", 200)
        binding.plus3.setNumber(2, "翻倍倍数", 10)
        binding.plus4.setNumber(1, "间隔期数", 200)
        binding.plus5.setNumber(50, "最低收益", 100000, true)

        binding.ivGenerate.setOnClickListener {
            val plus1 = binding.plus1.getNumber()
            val plus2 = binding.plus2.getNumber()
            val plus3 = binding.plus3.getNumber()
            val plus4 = binding.plus4.getNumber()
            val plus5 = binding.plus5.getNumber()
            val issues = if (LotteryDetailManager.mIssues.size < LotteryDetailManager.mIndex + 200) {
                LotteryDetailManager.mIssues.subList(LotteryDetailManager.mIndex, LotteryDetailManager.mIndex + 200)
            } else {
                LotteryDetailManager.mIssues.subList(LotteryDetailManager.mIndex, LotteryDetailManager.mIssues.size)
            }
            // 使用 map 深拷贝每个对象
            val newList = issues.map { it.copy() }
            allMoney = 0.0
            when (checkPosition) {

                0 -> {//利润率追号
                    ToastUtils.showLong("已生成最低利润率：" + plus5 + "，起始倍数：" + plus1 + "，追号：" + plus2 + "期的投注方案")
                    for (i in 0 until plus2) {
                        newList[i].apply {
                            multiple = plus1
                            amount = money
                            allMoney += amount
                        }
                    }
                }

                1 -> {//同倍追号
                    ToastUtils.showLong("已生成同倍倍数：" + plus1 + "，追号：" + plus2 + "期的投注方案")
                    for (i in 0 until plus2) {
                        newList[i].apply {
                            multiple = plus1
                            amount = money
                            allMoney += amount
                        }
                    }
                }

                2 -> {//翻倍追号
                    ToastUtils.showLong("已生成间隔" + plus4 + "x" + plus3 + "，起始倍数：" + plus1 + "，追号：" + plus2 + "期的投注方案")
                    for (i in 0 until plus2) {
                        newList[i].apply {
                            val quotient = i / plus4//获取商值
                            //追号倍数*翻倍倍数的商值方
                            multiple = plus1 * plus3.toDouble().pow(quotient.toDouble()).toInt()
                            amount = multiple * money
                            allMoney += amount
                        }
                    }
                }
            }
            chasingAdapter.checkedPosition = plus2 - 1
            chasingAdapter.setList(newList)
            changeResultText(plus2)
        }

    }

    private fun changeResultText(plus2: Int) {
        val html = "<p>单期注数：<font color=\"#EB5428\">" + betNums + "</font>  追号总期数：<font color=\"#EB5428\">" + plus2 +
                "</font>            追号总金额：<font color=\"#EB5428\">" + allMoney + "</font> </p>"
        binding.tvResult.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun initRv() {
        if (activity != null) {
            betNums = requireArguments().getString("betNums")!!
            money = requireArguments().getString("money")!!.toDouble()
            val issues = if (LotteryDetailManager.mIssues.size < LotteryDetailManager.mIndex + 200) {
                LotteryDetailManager.mIssues.subList(LotteryDetailManager.mIndex, LotteryDetailManager.mIndex + 200)
            } else {
                LotteryDetailManager.mIssues.subList(LotteryDetailManager.mIndex, LotteryDetailManager.mIssues.size)
            }
            // 使用 map 深拷贝每个对象  使newList和旧list无关联
            val newList = issues.map { it.copy() }.toMutableList()
            binding.rvChasingNumber.setHasFixedSize(true)
            binding.rvChasingNumber.layoutManager = LinearLayoutManager(requireContext())
            chasingAdapter = ChasingAdapter(newList, money, changeNumber = {
                allMoney -= it
                changeResultText(binding.plus2.getNumber())
            })
            binding.rvChasingNumber.adapter = chasingAdapter

            binding.tvSavePlan.setOnClickListener {
                val hashMap = HashMap<String, Int>()
                for (i in chasingAdapter.data) {
                    if (i.isCheck) {
                        hashMap[i.issue] = i.multiple
                    }
                }
                val chasingNumberRequestModel = ChasingNumberRequestModel()
                chasingNumberRequestModel.parmes = hashMapOf<String, Any>().apply {
                    put("lt_trace_issues", hashMap)
                    put("lt_trace_count_input", chasingAdapter.data.size)
                    put("lt_trace_if", "yes")
                    put("lt_trace_money", allMoney.toString())
                    put(
                        "lt_trace_stop", if (binding.tvStopChasing.isChecked) {
                            "yes"
                        } else {
                            "no"
                        }
                    )
                }
                val viewmodel = ViewModelProvider(
                    requireActivity()
                ).get(
                    LotteryBetConfirmViewModel::class.java
                )
                viewmodel.chasingNumberParams.value = chasingNumberRequestModel
                dismissAllowingStateLoss()
            }


        } else {
            dismissAllowingStateLoss()
        }
    }

    override fun initContentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.dialog_chasing_number
    }

    override fun initViewModel(): LotteryOrderViewModel {
        val viewModel = ViewModelProvider(requireActivity()).get(LotteryOrderViewModel::class.java)
        viewModel.betsViewModel = ViewModelProvider(requireActivity()).get(LotteryBetsViewModel::class.java)
        return viewModel
    }

    override fun initData() {}
    override fun initViewObservable() {
        super.initViewObservable()
    }

    override fun onClick(v: View) {}
    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        val params = window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = params
        val decorView = window?.decorView
        decorView?.background = ColorDrawable(Color.TRANSPARENT)
    }

    companion object {
        /**
         * 启动弹窗
         *
         * @param activity 获取FragmentManager
         */
        @JvmStatic
        fun show(activity: FragmentActivity, betNums: String, moneyNums: String) {
            val fragment = LotteryChasingNumberFragment()
            val bundle = Bundle()
            bundle.putString("betNums", betNums)
            bundle.putString("money", moneyNums)
            //fragment传参数，谷歌官方建议使用setArguments
            //使用有参构造函数传参数，依附的Activity重建时，Fragment会调取无参构造函数重建，没有无参构造就会闪退
            fragment.arguments = bundle
            fragment.show(activity.supportFragmentManager, LotteryChasingNumberFragment::class.java.name)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LotteryEventVo) {
        when (event.event) {
            LotteryEventConstant.EVENT_TIME_FINISH -> {

                val issues = if (LotteryDetailManager.mIssues.size < LotteryDetailManager.mIndex + 200) {
                    LotteryDetailManager.mIssues.subList(LotteryDetailManager.mIndex, LotteryDetailManager.mIndex + 200)
                } else {
                    LotteryDetailManager.mIssues.subList(LotteryDetailManager.mIndex, LotteryDetailManager.mIssues.size)
                }
                // 使用 map 深拷贝每个对象
                val newList = issues.map { it.copy() }
                chasingAdapter.checkedPosition = -1
                chasingAdapter.setList(newList)
                allMoney = 0.0
                changeResultText(0)
            }
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

}