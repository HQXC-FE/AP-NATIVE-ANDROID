package com.xtree.lottery.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.xtree.base.router.RouterFragmentPath
import com.xtree.base.widget.LoadingDialog
import com.xtree.base.widget.MsgDialog
import com.xtree.base.widget.TipDialog
import com.xtree.lottery.BR
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.LotteryOrderVo
import com.xtree.lottery.databinding.FragmentLotteryReportBinding
import com.xtree.lottery.ui.adapter.CpReportAdapter
import com.xtree.lottery.ui.view.BtCpDetailDialog
import com.xtree.lottery.ui.viewmodel.LotteryViewModel
import com.xtree.lottery.ui.viewmodel.factory.AppViewModelFactory
import me.xtree.mvvmhabit.base.BaseFragment

/**
 * 投注记录
 */
class LotteryReportFragment : BaseFragment<FragmentLotteryReportBinding, LotteryViewModel>() {

    private lateinit var mAdapter: CpReportAdapter

    companion object {
        fun newInstance(): LotteryReportFragment {
            val fragment = LotteryReportFragment()
            val bundle = Bundle()
            //fragment传参数，谷歌官方建议使用setArguments
            //使用有参构造函数传参数，依附的Activity重建时，Fragment会调取无参构造函数重建，没有无参构造就会闪退
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initImmersionBar() {

    }

    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.fragment_lottery_report
    }

    override fun initVariableId(): Int {
        return BR.model
    }

    override fun initViewModel(): LotteryViewModel {
        val viewModel = ViewModelProvider(requireActivity())[LotteryViewModel::class.java]
        val factory = AppViewModelFactory.getInstance(requireActivity().application)
        viewModel.setModel(factory.getmRepository())
        return viewModel
    }

    override fun initView() {
        binding.rvReport.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = CpReportAdapter(ArrayList())
        binding.rvReport.adapter = mAdapter
        mAdapter.addChildClickViewIds(R.id.tv_bet_again, R.id.tv_bet_details)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_bet_again -> {
                    var popupView: BasePopupView? = null
                    val dialog =
                        MsgDialog(
                            view.context,
                            "",
                            "确定再来一注吗？",
                            object : TipDialog.ICallBack {
                                override fun onClickLeft() {
                                    popupView?.dismiss()
                                }

                                override fun onClickRight() {
                                    popupView?.dismiss()
                                    viewModel.copyBet(view,(adapter.getItem(position) as LotteryOrderVo).projectid)
                                }
                            })

                    popupView = XPopup.Builder(view.context)
                        .dismissOnTouchOutside(true)
                        .dismissOnBackPressed(true)
                        .asCustom(dialog).show()
                }

                R.id.tv_bet_details -> {
                    val dialog: BtCpDetailDialog =
                        BtCpDetailDialog.newInstance(
                            requireContext(),
                            viewLifecycleOwner,
                            (adapter.getItem(position) as LotteryOrderVo).projectid
                        )
                    XPopup.Builder(context).asCustom(dialog).show()
                }
            }
        }
        LoadingDialog.show(requireContext())
        requestDataCP()
        binding.btMore.setOnClickListener {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_BT_REPORT) // 投注记录
        }
        binding.btRefresh.setOnClickListener {
            LoadingDialog.show(context)
            requestDataCP()
        }
        mAdapter.setEmptyView(R.layout.layout_no_data)
    }

    override fun initViewObservable() {
        viewModel.liveDataCpReport.observe(viewLifecycleOwner) {
            mAdapter.setList(it.aProject)
            binding.rvReport.scrollToPosition(0)
        }
    }

    private fun requestDataCP() {
        viewModel.getCpReport()
    }

}