package com.xtree.live.glide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.CheckResult;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;

import java.io.File;
import java.net.URL;
import java.util.List;

public class GlideRequest<TranscodeType> extends RequestBuilder<TranscodeType> implements Cloneable {
    GlideRequest(@NonNull Class<TranscodeType> transcodeClass, @NonNull RequestBuilder<?> other) {
        super(transcodeClass, other);
    }

    GlideRequest(@NonNull Glide glide, @NonNull RequestManager requestManager,
                 @NonNull Class<TranscodeType> transcodeClass, @NonNull Context context) {
        super(glide, requestManager ,transcodeClass, context);
    }

    @Override
    @CheckResult
    @NonNull
    protected GlideRequest<File> getDownloadOnlyRequest() {
        return new GlideRequest<>(File.class, this).apply(DOWNLOAD_ONLY_OPTIONS);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> sizeMultiplier(@FloatRange(from = 0.0, to = 1.0) float value) {
        return (GlideRequest<TranscodeType>) super.sizeMultiplier(value);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> useUnlimitedSourceGeneratorsPool(boolean flag) {
        return (GlideRequest<TranscodeType>) super.useUnlimitedSourceGeneratorsPool(flag);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> useAnimationPool(boolean flag) {
        return (GlideRequest<TranscodeType>) super.useAnimationPool(flag);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> onlyRetrieveFromCache(boolean flag) {
        return (GlideRequest<TranscodeType>) super.onlyRetrieveFromCache(flag);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> diskCacheStrategy(@NonNull DiskCacheStrategy strategy) {
        return (GlideRequest<TranscodeType>) super.diskCacheStrategy(strategy);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> priority(@NonNull Priority priority) {
        return (GlideRequest<TranscodeType>) super.priority(priority);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> placeholder(@Nullable Drawable drawable) {
        return (GlideRequest<TranscodeType>) super.placeholder(drawable);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> placeholder(@DrawableRes int id) {
        return (GlideRequest<TranscodeType>) super.placeholder(id);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> fallback(@Nullable Drawable drawable) {
        return (GlideRequest<TranscodeType>) super.fallback(drawable);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> fallback(@DrawableRes int id) {
        return (GlideRequest<TranscodeType>) super.fallback(id);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> error(@Nullable Drawable drawable) {
        return (GlideRequest<TranscodeType>) super.error(drawable);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> error(@DrawableRes int id) {
        return (GlideRequest<TranscodeType>) super.error(id);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> theme(@Nullable Resources.Theme theme) {
        return (GlideRequest<TranscodeType>) super.theme(theme);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> skipMemoryCache(boolean skip) {
        return (GlideRequest<TranscodeType>) super.skipMemoryCache(skip);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> override(int width, int height) {
        return (GlideRequest<TranscodeType>) super.override(width, height);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> override(int size) {
        return (GlideRequest<TranscodeType>) super.override(size);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> signature(@NonNull Key key) {
        return (GlideRequest<TranscodeType>) super.signature(key);
    }

    /**
     */
    @NonNull
    @CheckResult
    public <Y> GlideRequest<TranscodeType> set(@NonNull Option<Y> option, @NonNull Y y) {
        return (GlideRequest<TranscodeType>) super.set(option, y);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> decode(@NonNull Class<?> clazz) {
        return (GlideRequest<TranscodeType>) super.decode(clazz);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> encodeFormat(@NonNull Bitmap.CompressFormat format) {
        return (GlideRequest<TranscodeType>) super.encodeFormat(format);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> encodeQuality(@IntRange(from = 0, to = 100) int value) {
        return (GlideRequest<TranscodeType>) super.encodeQuality(value);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> frame(@IntRange(from = 0) long value) {
        return (GlideRequest<TranscodeType>) super.frame(value);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> format(@NonNull DecodeFormat format) {
        return (GlideRequest<TranscodeType>) super.format(format);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> disallowHardwareConfig() {
        return (GlideRequest<TranscodeType>) super.disallowHardwareConfig();
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> downsample(@NonNull DownsampleStrategy strategy) {
        return (GlideRequest<TranscodeType>) super.downsample(strategy);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> timeout(@IntRange(from = 0) int value) {
        return (GlideRequest<TranscodeType>) super.timeout(value);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> optionalCenterCrop() {
        return (GlideRequest<TranscodeType>) super.optionalCenterCrop();
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> centerCrop() {
        return (GlideRequest<TranscodeType>) super.centerCrop();
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> optionalFitCenter() {
        return (GlideRequest<TranscodeType>) super.optionalFitCenter();
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> fitCenter() {
        return (GlideRequest<TranscodeType>) super.fitCenter();
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> optionalCenterInside() {
        return (GlideRequest<TranscodeType>) super.optionalCenterInside();
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> centerInside() {
        return (GlideRequest<TranscodeType>) super.centerInside();
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> optionalCircleCrop() {
        return (GlideRequest<TranscodeType>) super.optionalCircleCrop();
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> circleCrop() {
        return (GlideRequest<TranscodeType>) super.circleCrop();
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> transform(@NonNull Transformation<Bitmap> transformation) {
        return (GlideRequest<TranscodeType>) super.transform(transformation);
    }

    /**
     */
    @NonNull
    @CheckResult
    @SuppressWarnings({
            "unchecked",
            "varargs"
    })
    public GlideRequest<TranscodeType> transform(@NonNull Transformation<Bitmap>... transformations) {
        return (GlideRequest<TranscodeType>) super.transform(transformations);
    }

    /**
     */
    @Deprecated
    @NonNull
    @CheckResult
    @SuppressWarnings({
            "unchecked",
            "varargs"
    })
    public GlideRequest<TranscodeType> transforms(@NonNull Transformation<Bitmap>... transformations) {
        return (GlideRequest<TranscodeType>) super.transforms(transformations);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> optionalTransform(@NonNull Transformation<Bitmap> transformation) {
        return (GlideRequest<TranscodeType>) super.optionalTransform(transformation);
    }

    /**
     */
    @NonNull
    @CheckResult
    public <Y> GlideRequest<TranscodeType> optionalTransform(@NonNull Class<Y> clazz,
                                                             @NonNull Transformation<Y> transformation) {
        return (GlideRequest<TranscodeType>) super.optionalTransform(clazz, transformation);
    }

    /**
     */
    @NonNull
    @CheckResult
    public <Y> GlideRequest<TranscodeType> transform(@NonNull Class<Y> clazz,
                                                     @NonNull Transformation<Y> transformation) {
        return (GlideRequest<TranscodeType>) super.transform(clazz, transformation);
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> dontTransform() {
        return (GlideRequest<TranscodeType>) super.dontTransform();
    }

    /**
     */
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> dontAnimate() {
        return (GlideRequest<TranscodeType>) super.dontAnimate();
    }

    /**
     */
    @NonNull
    public GlideRequest<TranscodeType> lock() {
        return (GlideRequest<TranscodeType>) super.lock();
    }

    /**
     */
    @NonNull
    public GlideRequest<TranscodeType> autoClone() {
        return (GlideRequest<TranscodeType>) super.autoClone();
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> apply(@NonNull BaseRequestOptions<?> options) {
        return (GlideRequest<TranscodeType>) super.apply(options);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> transition(@NonNull TransitionOptions<?, ? super TranscodeType> options) {
        return (GlideRequest<TranscodeType>) super.transition(options);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> listener(@Nullable RequestListener<TranscodeType> listener) {
        return (GlideRequest<TranscodeType>) super.listener(listener);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> addListener(@Nullable RequestListener<TranscodeType> listener) {
        return (GlideRequest<TranscodeType>) super.addListener(listener);
    }

    @Override
    @NonNull
    public GlideRequest<TranscodeType> error(@Nullable RequestBuilder<TranscodeType> builder) {
        return (GlideRequest<TranscodeType>) super.error(builder);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> error(Object o) {
        return (GlideRequest<TranscodeType>) super.error(o);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> thumbnail(@Nullable RequestBuilder<TranscodeType> builder) {
        return (GlideRequest<TranscodeType>) super.thumbnail(builder);
    }

    @Override
    @NonNull
    @CheckResult
    @SafeVarargs
    @SuppressWarnings("varargs")
    public final GlideRequest<TranscodeType> thumbnail(@Nullable RequestBuilder<TranscodeType>... builders) {
        return (GlideRequest<TranscodeType>) super.thumbnail(builders);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> thumbnail(@Nullable List<RequestBuilder<TranscodeType>> list) {
        return (GlideRequest<TranscodeType>) super.thumbnail(list);
    }

    @Override
    @Deprecated
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> thumbnail(float sizeMultiplier) {
        return (GlideRequest<TranscodeType>) super.thumbnail(sizeMultiplier);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable Object o) {
        return (GlideRequest<TranscodeType>) super.load(o);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable Bitmap bitmap) {
        return (GlideRequest<TranscodeType>) super.load(bitmap);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable Drawable drawable) {
        return (GlideRequest<TranscodeType>) super.load(drawable);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable String string) {
        return (GlideRequest<TranscodeType>) super.load(string);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable Uri uri) {
        return (GlideRequest<TranscodeType>) super.load(uri);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable File file) {
        return (GlideRequest<TranscodeType>) super.load(file);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@RawRes @DrawableRes @Nullable Integer id) {
        return (GlideRequest<TranscodeType>) super.load(id);
    }

    @Override
    @Deprecated
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable URL url) {
        return (GlideRequest<TranscodeType>) super.load(url);
    }

    @Override
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable byte[] bytes) {
        return (GlideRequest<TranscodeType>) super.load(bytes);
    }

    @Override
    @CheckResult
    public GlideRequest<TranscodeType> clone() {
        return (GlideRequest<TranscodeType>) super.clone();
    }
}
