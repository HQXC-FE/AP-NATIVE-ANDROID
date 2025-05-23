package com.xtree.lottery.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.XPopup
import com.xtree.base.widget.LoadingDialog
import com.xtree.lottery.BR
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.TraceInfoVo
import com.xtree.lottery.databinding.FragmentCharseReportBinding
import com.xtree.lottery.ui.adapter.TraceInfoAdapter
import com.xtree.lottery.ui.view.BtChaseDetailDialog
import com.xtree.lottery.ui.viewmodel.LotteryViewModel
import com.xtree.lottery.ui.viewmodel.factory.AppViewModelFactory
import me.xtree.mvvmhabit.base.BaseFragment

/**
 * 追号记录
 */
class ChaseBetReportFragment : BaseFragment<FragmentCharseReportBinding, LotteryViewModel>() {

    private lateinit var mAdapter: TraceInfoAdapter

    companion object {
        fun newInstance(): ChaseBetReportFragment {
            val fragment = ChaseBetReportFragment()
            val bundle = Bundle()
            //fragment传参数，谷歌官方建议使用setArguments
            //使用有参构造函数传参数，依附的Activity重建时，Fragment会调取无参构造函数重建，没有无参构造就会闪退
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initImmersionBar() {

    }


    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_charse_report
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
        mAdapter = TraceInfoAdapter(ArrayList())
        binding.rvReport.adapter = mAdapter
        mAdapter.addChildClickViewIds(R.id.tv_bet_details)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_bet_details -> {
                    val dialog: BtChaseDetailDialog =
                        BtChaseDetailDialog.newInstance(context, viewLifecycleOwner, (mAdapter.getItem(position) as TraceInfoVo.Bean).taskid)
                    XPopup.Builder(context).asCustom(dialog).show()
                }
            }
        }

        binding.btRefresh.setOnClickListener {
            requestData()
        }
        mAdapter.setEmptyView(R.layout.layout_no_data)
    }

    override fun initViewObservable() {
        viewModel.liveDataTraceinfo.observe(viewLifecycleOwner) {
            mAdapter.setList(ArrayList(it.list))
            binding.rvReport.scrollToPosition(0)
        }
    }

    fun requestData() {
        LoadingDialog.show(context)
        viewModel.getTraceinfo()
    }

}