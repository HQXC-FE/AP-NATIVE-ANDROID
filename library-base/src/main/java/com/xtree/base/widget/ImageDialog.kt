package com.xtree.base.widget

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.lxj.xpopup.core.CenterPopupView
import com.xtree.base.R
import com.xtree.base.databinding.DialogAwCodeBinding
import com.xtree.base.mvvm.loadImageAuthentication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class ImageDialog(context: Context, private var mUrl: String, var needToken: Boolean, var mCallBack: ICallBack? = null) : CenterPopupView(context) {
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
        //KLog.i(mUrl)
        binding = DialogAwCodeBinding.bind(findViewById(R.id.ll_root))

        if (needToken) {
            loadImageAuthentication(mUrl, binding.ivCode)
        } else {
            //"https://jxpicture.b1k3q6.com/2024/12/09/极限救援H5.webp"
            //加载首页公共弹窗
            loadImageWithRetry(context, mUrl, binding.ivCode)
        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }


    private fun loadImageWithRetry(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>, isFirstResource: Boolean
            ): Boolean {
                loadImageWithOkHttp(url, imageView)
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
        }).into(imageView)
    }

    fun loadImageWithOkHttp(url: String, imageView: ImageView) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 第二次失败，直接关闭弹窗
                GlobalScope.launch(Dispatchers.Main) {
                    //ToastUtils.showLong("关闭弹窗")
                    this@ImageDialog.dismiss() // 关闭弹窗
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val inputStream = response.body?.byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()

                    // 切换到主线程更新UI
                    GlobalScope.launch(Dispatchers.Main) {
                        imageView.setImageBitmap(bitmap)
                    }
                } else {
                    // 第二次失败，直接关闭弹窗
                    GlobalScope.launch(Dispatchers.Main) {
                        //ToastUtils.showLong("关闭弹窗")
                        this@ImageDialog.dismiss() // 关闭弹窗
                    }
                }
            }
        })


    }
}
