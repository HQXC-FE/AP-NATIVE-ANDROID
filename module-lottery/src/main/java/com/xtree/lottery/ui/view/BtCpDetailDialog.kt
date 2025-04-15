package com.xtree.lottery.ui.view

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.xtree.base.utils.CfLog
import com.xtree.base.widget.LoadingDialog
import com.xtree.base.widget.MsgDialog
import com.xtree.base.widget.TipDialog
import com.xtree.lottery.R
import com.xtree.lottery.data.Injection
import com.xtree.lottery.data.source.vo.LotteryOrderVo
import com.xtree.lottery.databinding.DialogLotteryBtCpDetailBinding
import com.xtree.lottery.ui.adapter.getLotteryStatus
import com.xtree.lottery.ui.viewmodel.LotteryViewModel
import me.xtree.mvvmhabit.utils.Utils

class BtCpDetailDialog private constructor(context: Context) : BottomPopupView(context) {
    //private Context ctx;
    lateinit var owner: LifecycleOwner

    //ReportViewModel viewModel;
    lateinit var binding: DialogLotteryBtCpDetailBinding
    private var id: String? = null // D20240203-192***CED
    private lateinit var viewModel: LotteryViewModel
    override fun onCreate() {
        super.onCreate()
        initView()
        initData()
        initViewObservable()
        LoadingDialog.show(context) // loading
        requestData()
    }

    private fun initView() {
        binding = DialogLotteryBtCpDetailBinding.bind(findViewById(R.id.ll_root))
        binding.ivwClose.setOnClickListener { dismiss() }
    }

    private fun initData() {
        viewModel = LotteryViewModel((Utils.getContext() as Application), Injection.provideMainRepository(false))
    }

    private fun initViewObservable() {
        viewModel.liveDataBtCpDetail.observe(owner) { vo: LotteryOrderVo ->
            CfLog.i()
            setView(vo)
        }

        viewModel.liveDataCancelOrder.observe(owner) {
            if (TextUtils.equals(it.msg_detail, "撤单成功")) {
                requestData()
            }
        }
    }


    private fun setView(t: LotteryOrderVo) {
        val vo = t.project
        binding.tvwWriteTime.text = vo.writetime
        binding.tvwProjectId.text = t.id //
        binding.tvwUsername.text = t.username //
        binding.tvwLotteryName.text = t.lottery //
        binding.tvwIssue.text = vo.issue
        binding.tvwStatus.text = getLotteryStatus(context, vo)
        binding.tvwMethodName.text = t.method //
        binding.tvwNoCode.text = t.nocode //
        binding.tvwTotalPrice.text = vo.totalprice
        binding.tvwBonus.text = vo.bonus
        binding.tvwMultiple.text = vo.multiple
        binding.tvwModes.text = vo.modes
        binding.tvwCode.text = vo.code
        if (t.is_cancelable) {
            binding.tvCancelOrder.visibility = View.VISIBLE
        } else {
            binding.tvCancelOrder.visibility = View.GONE
        }
        binding.tvBetAgain.setOnClickListener {
            var popupView: BasePopupView? = null
            val dialog =
                MsgDialog(
                    context,
                    "",
                    "确定再来一注吗？",
                    object : TipDialog.ICallBack {
                        override fun onClickLeft() {
                            popupView?.dismiss()
                        }

                        override fun onClickRight() {
                            popupView?.dismiss()
                            viewModel.copyBet(binding.tvBetAgain, t.id)
                        }
                    })

            popupView = XPopup.Builder(context)
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(dialog).show()
        }
        binding.tvCancelOrder.setOnClickListener {
            var popupView: BasePopupView? = null
            val dialog =
                MsgDialog(
                    context,
                    "",
                    "确定要撤销订单吗？",
                    object : TipDialog.ICallBack {
                        override fun onClickLeft() {
                            popupView?.dismiss()
                        }

                        override fun onClickRight() {
                            popupView?.dismiss()
                            viewModel.cancelOrder(t.id)
                        }
                    })

            popupView = XPopup.Builder(context)
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(dialog).show()
        }
    }

    private fun requestData() {
        viewModel.getBtCpOrderDetail(id) // D20240203-192***CED
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_lottery_bt_cp_detail
    }

    override fun getMaxHeight(): Int {
        //return super.getMaxHeight();
        return XPopupUtils.getScreenHeight(context) * 80 / 100
    }

    companion object {
        fun newInstance(ctx: Context, owner: LifecycleOwner, id: String?): BtCpDetailDialog {
            val dialog = BtCpDetailDialog(ctx)
            //dialog.ctx = ctx;
            dialog.owner = owner
            dialog.id = id
            return dialog
        }
    }
}
