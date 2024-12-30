package com.xtree.base.mvvm

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.GONE
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.xtree.base.R
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter
import com.xtree.base.mvvm.recyclerview.BindModel
import com.xtree.base.net.HeaderInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.xtree.mvvmhabit.utils.ToastUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 *Created by KAKA on 2024/3/8.
 *Describe: MVVM扩展函数
 */

@BindingAdapter(
    value = ["layoutManager", "itemData", "itemViewType", "onBindListener", "dividerDrawableId", "viewPool","spanCount"],
    requireAll = false
)
fun RecyclerView.init(
    layoutManager: RecyclerView.LayoutManager?,
    itemData: List<BindModel>?,
    itemViewType: List<Int>?,
    onBindListener: BaseDatabindingAdapter.onBindListener?,
    dividerDrawableId: Int?,
    viewPool: RecyclerView.RecycledViewPool?,
    spanCount: Int?,
) {

    if (itemData == null || itemViewType == null) {
        return
    }

    viewPool?.let { setRecycledViewPool(it) }

    adapter?.run {

        if (itemData == models) {
            models = itemData
        } else {
            (bindingAdapter as BaseDatabindingAdapter).run {
                clearHeader(false)
                clearFooter(false)
                initData(itemData, itemViewType)
            }
        }
    } ?: run {
        when (layoutManager) {
            null -> if (this.layoutManager == null) spanCount?.run { grid(spanCount) } ?: run { linear() }
            else -> this.layoutManager = layoutManager
        }

        dividerDrawableId?.let {
            divider {
                setDrawable(it)
                startVisible = false
                endVisible = true
            }
        }

        val mAdapter = BaseDatabindingAdapter().run {
            initData(itemData, itemViewType)
            onBind {
                onBindListener?.onBind(this, this.itemView.rootView, getItemViewType())

                itemView.rootView.setOnClickListener {
                    onBindListener?.onItemClick(modelPosition, layoutPosition, getItemViewType())
                }

//                 itemDifferCallback = object : ItemDifferCallback {
//
//                     override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
//                         return oldItem == newItem
//                     }
//
//                     override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
//                         return (oldItem as BindModel).getItemId() == (newItem as BindModel).getItemId()
//                     }
//
//                     override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
//                         return true
//                     }
//                 }
            }
            adapter = this
        }
    }
}


@BindingAdapter(
    value = ["setSelectedListener", "tabs"],
    requireAll = false
)
fun TabLayout.init(setSelectedListener: OnTabSelectedListener?, tabs: List<String>?) {
    tabs?.let {
        removeAllTabs()
        for (tab in it) {
            addTab(newTab().apply { text = tab })
        }
    } ?: run {
        visibility = GONE
    }

    setSelectedListener?.run {
        addOnTabSelectedListener(setSelectedListener)
    }
}

@BindingAdapter(
    value = ["onRefreshLoadMoreListener", "onLoadMoreListener"],
    requireAll = false
)
fun SmartRefreshLayout.init(onRefreshLoadMoreListener: OnRefreshLoadMoreListener?, onLoadMoreListener: OnLoadMoreListener?) {
    onRefreshLoadMoreListener?.let { setOnRefreshListener(it) }
    onLoadMoreListener?.let { setOnLoadMoreListener(it) }
}

@BindingAdapter(
    value = ["textChangedListener"],
    requireAll = false
)
fun EditText.init(textChangedListener: TextWatcher?) {
    textChangedListener?.let { addTextChangedListener(it) }
}

@SuppressLint("CheckResult")
@BindingAdapter(
    value = ["imageUrl", "placeholder", "error", "fallback", "loadWidth", "loadHeight", "cacheEnable"],
    requireAll = false
)
fun setImageUrl(
    view: ImageView,
    source: Any? = null,
    placeholder: Drawable? = null,
    error: Drawable? = null,
    fallback: Drawable? = null,
    width: Int? = -1,
    height: Int? = -1,
    cacheEnable: Boolean? = true
) {
// 计算位图尺寸，如果位图尺寸固定，加载固定大小尺寸的图片，如果位图未设置尺寸，那就加载原图，Glide加载原图时，override参数设置 -1 即可。
    val widthSize = (if ((width ?: 0) > 0) width else view.width) ?: -1
    val heightSize = (if ((height ?: 0) > 0) height else view.height) ?: -1
    // 根据定义的 cacheEnable 参数来决定是否缓存
    val diskCacheStrategy = if (cacheEnable == true) DiskCacheStrategy.AUTOMATIC else DiskCacheStrategy.NONE
    // 设置编码格式，在Android 11(R)上面使用高清无损压缩格式 WEBP_LOSSLESS ， Android 11 以下使用PNG格式，PNG格式时会忽略设置的 quality 参数。
    val encodeFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.PNG
    val glide = Glide.with(view.context)
        .asDrawable()
        .load(source)
        .placeholder(placeholder)
        .error(error)
        .fallback(fallback)
        .thumbnail(0.33f)
        .skipMemoryCache(false)
        .sizeMultiplier(0.5f)
        .format(DecodeFormat.PREFER_ARGB_8888)
        .encodeFormat(encodeFormat)
        .encodeQuality(80)
        .diskCacheStrategy(diskCacheStrategy)
        .transition(DrawableTransitionOptions.withCrossFade())
    width?.let {
        if (it > 0) {
            glide.override(widthSize, heightSize)
        }
    }
    glide.into(view)
}

fun String?.plusDomainOrNot(domain: String): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    var target = this
    if (!this.startsWith("http")) {
        val separator = when {
            domain.endsWith("/") && target.startsWith("/") -> {
                target = target.substring(1)
                ""
            }

            domain.endsWith("/") || target.startsWith("/") -> ""
            else -> "/"
        }
        target = domain + separator + target
    }
    return target
}

/**
 * 加载图片时，需要鉴权
 */
fun loadImageAuthentication(url: String, imageView: ImageView) {
    if (!isNetworkAvailable(imageView.context)) {
        // 没有网络，直接显示错误图片
        GlobalScope.launch(Dispatchers.Main) {
            ToastUtils.showLong("网络不可用，请检查网络连接")
            imageView.setImageResource(R.mipmap.error_image)
        }
        return
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
        .build()

    val request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // 加载失败
            GlobalScope.launch(Dispatchers.Main) {
                imageView.setImageResource(R.mipmap.error_image)
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
                // 加载失败
                GlobalScope.launch(Dispatchers.Main) {
                    imageView.setImageResource(R.mipmap.error_image)
                }
            }
        }
    })
}

private fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        @Suppress("DEPRECATION")
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}