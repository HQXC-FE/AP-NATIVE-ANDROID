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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.xtree.base.widget.MsgDialog
import com.xtree.base.widget.TipDialog
import com.xtree.lottery.R
import com.xtree.lottery.data.LotteryDetailManager
import com.xtree.lottery.data.source.request.LotteryBetRequest
import com.xtree.lottery.data.source.request.LotteryBetRequest.BetOrderData
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
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow


/**
 * Created by Daniel
 * Describe: 追号弹窗
 */
open class LotteryChasingNumberFragment private constructor() : BaseDialogFragment<DialogChasingNumberBinding, LotteryOrderViewModel>() {
    private lateinit var orders: List<LotteryBetRequest.BetOrderData>
    private var money = BigDecimal.ZERO
    private var betNums = ""
    private lateinit var chasingAdapter: ChasingAdapter
    private var checkPosition = 0
    private var allMoney = BigDecimal.ZERO
    private var pop: BasePopupView? = null

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
            allMoney = BigDecimal.ZERO
            when (checkPosition) {

                0 -> {//利润率追号

                    val profit = plus5
                    val times = plus1
                    val count = plus2
                    if (count > newList.size) {
                        ToastUtils.showLong("超过最大可追奖期，请重新选择")
                        return@setOnClickListener
                    }

                    if (!checkProfit()) {
                        ToastUtils.showLong("多玩法和浮动奖金无法使用利润率追号")
                        return@setOnClickListener
                    }

                    // 计算中奖金额总和
                    var sumPrize = BigDecimal.ZERO
                    for (order in orders) {
                        // 计算每个订单的中奖金额，并保留两位小数
                        val prize = BigDecimal(order.display.minPrize)
                            .multiply(BigDecimal(order.display.rate))
                            .multiply(BigDecimal(100))  // 乘以100
                            .setScale(2, RoundingMode.FLOOR)  // 向下保留两位小数

                        sumPrize = sumPrize.add(prize)  // 累加中奖金额

                    }

                    // 计算最大利润率
                    val maxProfit = (sumPrize.divide(money, 2, RoundingMode.FLOOR).subtract(BigDecimal(1)))
                        .multiply(BigDecimal(100))
                    // 检查利润率
                    if (maxProfit < BigDecimal(profit)) {
                        ToastUtils.showLong("当前最高利润率为${"%.1f".format(maxProfit)}")
                        return@setOnClickListener
                    }

                    for (i in 0 until count) {
                        var currentTimes = 1

                        // 计算前面已选倍数的总和
                        var sumTimes = BigDecimal.ZERO
                        for (j in 0 until i) {
                            sumTimes = sumTimes.add(BigDecimal(newList[j].multiple))
                        }
                        //(中奖金额*currentTimes)/(投资金额*(前面已选倍数的总和+currentTimes))<=(1+最低利润率/100)
                        // 计算是否满足利润条件
                        while ((sumPrize.multiply(BigDecimal(currentTimes))) / (money.multiply(sumTimes.add(BigDecimal(currentTimes))))
                            <= BigDecimal(1).add(BigDecimal(profit).divide(BigDecimal(100)))
                        ) {
                            currentTimes++
                        }

                        newList[i].multiple = currentTimes
                    }

                    for (i in 0 until plus2) {
                        newList[i].apply {
                            multiple *= times // 所有倍数乘上起始倍数
                            amountBigDecimal = BigDecimal(multiple).multiply(money)
                            allMoney = allMoney.add(amountBigDecimal)
                        }
                    }

                    //ToastUtils.showLong("已生成最低利润率：" + plus5 + "，起始倍数：" + plus1 + "，追号：" + plus2 + "期的投注方案")
                    ToastUtils.showLong("已生成最低利润率：$profit，起始倍数：$times，追号：$count 期的投注方案")
                }

                1 -> {//同倍追号
                    ToastUtils.showLong("已生成同倍倍数：" + plus1 + "，追号：" + plus2 + "期的投注方案")
                    for (i in 0 until plus2) {
                        newList[i].apply {
                            multiple = plus1
                            amountBigDecimal = BigDecimal(multiple).multiply(money)
                            allMoney = allMoney.add(amountBigDecimal)
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
                            amountBigDecimal = BigDecimal(multiple).multiply(money)
                            allMoney = allMoney.add(amountBigDecimal)
                        }
                    }
                }
            }
            chasingAdapter.checkedPosition = plus2 - 1
            chasingAdapter.setList(newList)
            changeResultText(plus2)
        }

    }

    fun checkProfit(): Boolean {
        var result = true

        // 检查是否有浮动奖金组（currentBonus 为 String）
        for (order in orders) {
            if (order.display.currentBonus is String) {
                result = false
                break
            }
        }

        // 检查是否是单一玩法（menuid 是否唯一）
        val menuIdSet = mutableSetOf<String>()
        for (order in orders) {
            menuIdSet.add(order.menuid)
        }
        if (menuIdSet.size != 1) {
            result = false
        }

        // 检查是否有 minPrize 为 null 的情况
        var hasMissingMinPrize = false
        for (order in orders) {
            if (order.display.minPrize == null) {
                hasMissingMinPrize = true
                break
            }
        }
        if (hasMissingMinPrize) {
            result = false
        }

        return result
    }


    private fun changeResultText(plus2: Int) {
        val html = "<p>单期注数：<font color=\"#EB5428\">" + betNums + "</font> 追号总期数：<font color=\"#EB5428\">" + plus2 +
                "</font> 追号总金额：<font color=\"#EB5428\">" + allMoney + "</font> </p>"
        binding.tvResult.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun initRv() {
        if (activity != null) {
            betNums = requireArguments().getString("betNums")!!
            money = BigDecimal(requireArguments().getString("money")!!)
            val jsonString = requireArguments().getString("orders")!!
            val gson = Gson()
            val type = object : TypeToken<List<BetOrderData?>?>() {}.getType()
            orders = gson.fromJson(jsonString, type)

            if (orders.size > 1) {
                showTipDialog("多注单追号倍率需默认为1倍，已默认设置倍率为1")
            }

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
                allMoney = BigDecimal.ZERO
                for (i in 0..chasingAdapter.checkedPosition) {
                    allMoney = allMoney.add(chasingAdapter.data[i].amountBigDecimal)
                }
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
                    put("lt_trace_count_input", hashMap.size)
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
                )[LotteryBetConfirmViewModel::class.java]
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
        fun show(activity: FragmentActivity, betNums: String, moneyNums: String, orders: String) {
            val fragment = LotteryChasingNumberFragment()
            val bundle = Bundle()
            bundle.putString("betNums", betNums)
            bundle.putString("money", moneyNums)
            bundle.putString("orders", orders)
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
                allMoney = BigDecimal.ZERO
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


    /**
     * 提示弹窗
     */
    open fun showTipDialog(msg: String) {
        val dialog = MsgDialog(requireContext(), requireContext().getString(R.string.txt_kind_tips), msg, true, object : TipDialog.ICallBack {
            override fun onClickLeft() {}
            override fun onClickRight() {
                if (pop != null) {
                    pop?.dismiss()
                }
            }
        })
        pop = XPopup.Builder(context)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .asCustom(dialog).show()
    }

}