package com.xtree.base.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.lxj.xpopup.core.CenterPopupView
import com.xtree.base.R
import com.xtree.base.databinding.DialogAwCodeBinding
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.utils.CfLog
import com.xtree.base.utils.TagUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.xtree.mvvmhabit.utils.SPUtils
import me.xtree.mvvmhabit.utils.ToastUtils


class ImageDialog(context: Context, private var url: String, var needToken: Boolean, var mCallBack: ICallBack? = null) : CenterPopupView(context) {
    lateinit var binding: DialogAwCodeBinding

    interface ICallBack {
        fun onClose()
    }

    override fun onCreate() {
        super.onCreate()
        initView()
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_aw_code
    }

    private fun initView() {
        binding = DialogAwCodeBinding.bind(findViewById(R.id.ll_root))

        if (needToken) {
            var cookie = ("auth=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN)
                    + ";" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_COOKIE_NAME)
                    + "=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID)
                    + ";")
            cookie = "auth-expires-in=604800; userPasswordCheck=lowPass; $cookie"
            CfLog.e("cookie: $cookie")

            val glideUrl = GlideUrl(
                url, LazyHeaders.Builder()
                    .addHeader("Content-Type", "application/vnd.sc-api.v1.json")
                    .addHeader("Authorization", "bearer " + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN))
                    .addHeader("Cookie", cookie)
                    .addHeader("UUID", TagUtils.getDeviceId(context))
                    .build()
            )
            Glide.with(this)
                .load(glideUrl).error(R.mipmap.me_icon_name)
                .into(binding.ivCode)
        } else {
            //加载首页公共弹窗
            loadImageWithRetry(context, url, binding.ivCode)
        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }


    fun loadImageWithRetry(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                        // 第一次失败时重试
                        GlobalScope.launch(Dispatchers.Main) {
                            Glide.with(context)
                                .load(url)
                                .error(
                                    // 如果再次失败，关闭弹窗
                                    GlobalScope.launch(Dispatchers.Main) {
                                        ToastUtils.showLong("关闭弹窗")
                                        this@ImageDialog.dismiss() // 关闭弹窗
                                    }
                                )
                                .into(imageView)
                        }

                    return true // 阻止默认错误占位符显示
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return false // 返回 false，继续正常处理
                }
            })
            .into(imageView)
    }


}
