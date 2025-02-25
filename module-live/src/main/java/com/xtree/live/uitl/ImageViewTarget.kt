package com.xtree.live.uitl

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.transition.Transition

open class ImageViewTarget(view: ImageView, private var mImagePath:String? = null, private var tag:String? = null) : ImageViewTarget<Drawable>(view) {
    constructor(imageView: ImageView, imageUrl: String?) : this(imageView, imageUrl, null)
    private val loggable = false
    override fun onLoadStarted(placeholder: Drawable?) {
        super.onLoadStarted(placeholder)

        if(loggable) {
            Log.d(tag ?: TAG, "onLoadStarted")
        }
    }

    fun getImagePath():String? = mImagePath

    override fun onLoadCleared(placeholder: Drawable?) {
        super.onLoadCleared(placeholder)
        if(loggable){

        }
    }

    override fun setResource(resource: Drawable?) {
        getView().setImageDrawable(resource)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        super.onLoadFailed(errorDrawable)
        if(loggable) Log.w(tag ?: TAG, "onLoadFailed\n${Integer.toHexString(System.identityHashCode(view))}\n$mImagePath ")
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        if(loggable){
            logResourceInfo(resource)
        }
        super.onResourceReady(resource, transition)
    }

    private fun logResourceInfo(resource: Drawable) {
        val imageView = getView()

        if (imageView is TargetImageView) {
            Log.d(tag ?: TAG, "oldPath :${imageView.imagePath}")
            val oldDrawable: Drawable? = imageView.drawable
            oldDrawable?.let {
                Log.d(tag ?: TAG, "oldDrawable width:${oldDrawable.intrinsicWidth} height:${oldDrawable.intrinsicHeight}")
            }

            imageView.imagePath = mImagePath
            Log.d(tag ?: TAG, "newPath :${imageView.imagePath}")
            Log.d(tag ?: TAG, "drawable width:${resource.intrinsicWidth} height:${resource.intrinsicHeight}")
        }

        if (resource is BitmapDrawable) {
            Log.w(tag ?: TAG, "=====================start========================")
            Log.d(tag ?: TAG, "$mImagePath")
            val bitmap = resource.bitmap
            Log.d(tag ?: TAG, "byteCount:${bitmap.getByteCount()}")
            Log.d(tag ?: TAG, "allocationByteCount:${bitmap.getAllocationByteCount()}")
            Log.d(tag ?: TAG, "size:${bitmap.getWidth()} ,${bitmap.getHeight()}")
            Log.d(tag ?: TAG, "density:${bitmap.getDensity()}")
            Log.d(tag ?: TAG, "config:${bitmap.getConfig()}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(tag ?: TAG, "colorSpace:${bitmap.getColorSpace()}")
            }
            Log.w(tag ?: TAG, "=====================end==========================")
        }

        if (resource is GifDrawable) {
            Log.d(tag ?: TAG, "byteCount:" + resource.size)
            Log.d(tag ?: TAG, "size:${resource.intrinsicWidth} ,${resource.intrinsicHeight}")
        }
    }

    companion object {
        const val TAG = "ImageViewTarget"
    }
}
