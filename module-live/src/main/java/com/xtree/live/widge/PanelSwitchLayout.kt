package com.xtree.live.widge

import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.AttributeSet
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import com.xtree.live.inter.Constants
import com.xtree.live.inter.ContentScrollMeasurer
import com.xtree.live.inter.DeviceRuntime
import com.xtree.live.inter.IContentContainer
import com.xtree.live.inter.IPanelView
import com.xtree.live.inter.OnEditFocusChangeListener
import com.xtree.live.inter.OnKeyboardStateListener
import com.xtree.live.inter.OnPanelChangeListener
import com.xtree.live.inter.OnViewClickListener
import com.xtree.live.inter.PanelContainer
import com.xtree.live.inter.PanelHeightMeasurer
import com.xtree.live.inter.TriggerViewClickInterceptor
import com.xtree.live.inter.ViewAssertion
import com.xtree.live.uitl.DisplayUtil
import com.xtree.live.uitl.DisplayUtil.getAppRealHeight
import com.xtree.live.uitl.DisplayUtil.getLocationOnWindow
import com.xtree.live.uitl.DisplayUtil.isPortrait
import com.xtree.live.uitl.KeyboardHeightCompat
import com.xtree.live.uitl.LogFormatter
import com.xtree.live.uitl.PanelUtil
import com.xtree.live.uitl.PanelUtil.getKeyBoardHeight
import com.xtree.live.uitl.isSystemInsetsAnimationSupport
import kotlin.math.min

class PanelSwitchLayout : LinearLayout, ViewAssertion {

    private var viewClickListeners: MutableList<OnViewClickListener>? = null
    private var panelChangeListeners: MutableList<OnPanelChangeListener>? = null
    private var keyboardStatusListeners: MutableList<OnKeyboardStateListener>? = null
    private var editFocusChangeListeners: MutableList<OnEditFocusChangeListener>? = null

    private lateinit var contentContainer: IContentContainer
    private lateinit var panelContainer: PanelContainer
    private lateinit var window: Window

    private var windowInsetsRootView: View? = null // 用于Android 11以上，通过OnApplyWindowInsetsListener获取键盘高度
    private var triggerViewClickInterceptor: TriggerViewClickInterceptor? = null
    private val contentScrollMeasurers = mutableListOf<ContentScrollMeasurer>()
    private val panelHeightMeasurers = HashMap<Int, PanelHeightMeasurer>()

    private var panelId = Constants.PANEL_NONE
    private var lastPanelId = Constants.PANEL_NONE
    @IdRes
    private var inputLayoutId :Int = 0
    private var lastPanelHeight = -1
    private var animationSpeed = 200 //standard
    private var enableAndroid11KeyboardFeature = true   // 是否启用 Android 11 键盘动画方案，目前发现 dialog，popupWindow等子窗口场景不支持键盘动画
    private var contentScrollOutsizeEnable = true

    private var deviceRuntime: DeviceRuntime? = null
    private var realBounds: Rect? = null
    private var keyboardStateRunnable = Runnable { toKeyboardState(false) }

    private var doingCheckout = false

    private val retryCheckoutKbRunnable = CheckoutKbRunnable()

    internal fun getContentContainer() = contentContainer

    // 针对Android 11以上开启键盘动画特性，高度获取失败时，对外提供兼容方案
    var softInputHeightCalculatorOnStart: ((animation: WindowInsetsAnimationCompat, bounds: WindowInsetsAnimationCompat.BoundsCompat) -> Int)? = null
    var softInputHeightCalculatorOnProgress: ((insets: WindowInsetsCompat, runningAnimations: MutableList<WindowInsetsAnimationCompat>) -> Int)? = null

    inner class CheckoutKbRunnable : Runnable {
        var retry = false
        var delay: Long = 0L
        override fun run() {
            val result = checkoutPanel(Constants.PANEL_KEYBOARD)
            if (!result && panelId != Constants.PANEL_KEYBOARD && retry) {
                this@PanelSwitchLayout.postDelayed(this, delay)
            }
            retry = false
        }
    }


    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        initView(attrs, defStyleAttr, 0)
    }

    @TargetApi(21)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, com.xtree.live.R.styleable.PanelSwitchLayout, defStyleAttr, 0)
        animationSpeed = typedArray.getInteger(com.xtree.live.R.styleable.PanelSwitchLayout_animationSpeed, animationSpeed)
        enableAndroid11KeyboardFeature = typedArray.getBoolean(com.xtree.live.R.styleable.PanelSwitchLayout_android11KeyboardFeature, true)
        inputLayoutId = typedArray.getResourceId(com.xtree.live.R.styleable.PanelSwitchLayout_inputLayoutId, 0)
        typedArray.recycle()
    }

    internal fun setTriggerViewClickInterceptor(interceptor: TriggerViewClickInterceptor?) {
        this.triggerViewClickInterceptor = interceptor
    }

    internal fun setContentScrollOutsizeEnable(enable: Boolean) {
        this.contentScrollOutsizeEnable = enable
    }

    internal fun isContentScrollOutsizeEnable() = contentScrollOutsizeEnable

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        tryBindKeyboardChangedListener()
    }

    fun recycle() {
        removeCallbacks(retryCheckoutKbRunnable)
        removeCallbacks(keyboardStateRunnable)
        contentContainer.getInputActionImpl().recycler()
        if (hasAttachLister) {
            releaseKeyboardChangedListener()
        }
    }

    private fun checkoutKeyboard(retry: Boolean = true, delay: Long = 200L) {
        this@PanelSwitchLayout.removeCallbacks(retryCheckoutKbRunnable)
        retryCheckoutKbRunnable.retry = retry
        retryCheckoutKbRunnable.delay = delay
        retryCheckoutKbRunnable.run()
    }

    private fun initListener() {
        /**
         * 1. if current currentPanelId is None,should show keyboard
         * 2. current currentPanelId is not None or KeyBoard that means some panel is showing,hide it and show keyboard
         */
        contentContainer.getInputActionImpl().setEditTextClickListener(OnClickListener { v ->
            notifyViewClick(v)
            checkoutKeyboard()
        })
        contentContainer.getInputActionImpl().setEditTextFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            notifyEditFocusChange(v, hasFocus)
            checkoutKeyboard()
        })
        contentContainer.getResetActionImpl().setResetCallback(Runnable {
            hookSystemBackByPanelSwitcher()
        })

        /**
         * save panel that you want to use these to checkout
         */
        val array = panelContainer.panelSparseArray
        for (i in 0 until array.size()) {
            val panelView = array[array.keyAt(i)]
            val keyView = contentContainer.findTriggerView(panelView.getBindingTriggerViewId())
            keyView?.setOnClickListener(object : OnClickListener {
                override fun onClick(v: View) {
                    triggerViewClickInterceptor?.let {
                        if (it.intercept(v.id)) {
                            return
                        }
                    }
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - preClickTime <= Constants.PROTECT_KEY_CLICK_DURATION) {
                        return
                    }
                    notifyViewClick(v)
                    val targetId = panelContainer.getPanelId(panelView)
                    if (panelId == targetId && panelView.isTriggerViewCanToggle() && panelView.isShowing()) {
                        checkoutKeyboard(false)
                    } else {
                        checkoutPanel(targetId)
                    }
                    preClickTime = currentTime
                }
            })
        }
    }

    internal fun bindListener(
        viewClickListeners: MutableList<OnViewClickListener>, panelChangeListeners: MutableList<OnPanelChangeListener>,
        keyboardStatusListeners: MutableList<OnKeyboardStateListener>, editFocusChangeListeners: MutableList<OnEditFocusChangeListener>
    ) {
        this.viewClickListeners = viewClickListeners
        this.panelChangeListeners = panelChangeListeners
        this.keyboardStatusListeners = keyboardStatusListeners
        this.editFocusChangeListeners = editFocusChangeListeners
    }

    internal fun setScrollMeasurers(mutableList: MutableList<ContentScrollMeasurer>) {
        contentScrollMeasurers.addAll(mutableList)
    }

    internal fun setPanelHeightMeasurers(mutableList: MutableList<PanelHeightMeasurer>) {
        for (panelHeightMeasurer in mutableList) {
            panelHeightMeasurers[panelHeightMeasurer.getPanelTriggerId()] = panelHeightMeasurer
        }
    }

    private var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private var hasAttachLister = false


    /**
     * 针对 Android Q 场景判断，
     * 设备开启虚拟手势导航栏如 MIUI12时，正常情况下使用 navigationBarBackground 判断可见时是正确的
     * 如果同时采用 SYSTEM_UL_FLAG_LAYOUT_HIDE_NAVIGATION 绘制到导航栏下面，那么在 Layout Inspector 上 navigationBarBackground 布局是不存在的，但是 getWindowVisibleDisplayFrame 并没有包含这部分高度。
     * 所以针对 AndroidQ 采用 rootWindowInsets 来获取这部分可视导航栏的高度并在计算软键盘高度的时候需要加回去。
     */
    private fun getAndroidQNavHIfNavIsInvisible(isNavigationBarShow: Boolean, window: Window): Int {
        return if (!isNavigationBarShow && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && DisplayUtil.hasSystemUIFlag(window, View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)) {
            val inset = window.decorView.rootView.rootWindowInsets
            inset.stableInsetBottom
        } else 0
    }



    private var lastContentHeight: Int? = null
    private var lastNavigationBarShow: Boolean? = null
    private var lastKeyboardHeight: Int = 0
    private val minLimitOpenKeyboardHeight by lazy { KeyboardHeightCompat.getMinLimitHeight() }
    private var minLimitCloseKeyboardHeight: Int = 0
    private var keyboardAnimationFeature = false     // 是否使用Android 11键盘动画特性

    internal fun bindWindow(window: Window, windowInsetsRootView: View?) {
        this.window = window
        this.windowInsetsRootView = windowInsetsRootView
        keyboardAnimationFeature = enableAndroid11KeyboardFeature && supportKeyboardAnimation()
        if (keyboardAnimationFeature) {
            // 通过监听键盘动画，修改translationY线上面板
            keyboardChangedAnimation()
        } else {
            // 通过获取键盘高度，触发onLayout修改面板高度
            deviceRuntime = DeviceRuntime(context, window)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            deviceRuntime?.let {
                contentContainer.getInputActionImpl().updateFullScreenParams(it.isFullScreen, panelId, getCompatPanelHeight(panelId))
                keyboardChangedListener30Impl()
                hasAttachLister = true
            }
        }
    }


    /**
     * Android 11 ViewCompat.setWindowInsetsAnimationCallback
     */
    private fun keyboardChangedAnimation() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        var hasSoftInput = false
        var transitionY = 0f
        val callback = object : WindowInsetsAnimationCompat.Callback(
            DISPATCH_MODE_CONTINUE_ON_SUBTREE
        ) {

            override fun onStart(animation: WindowInsetsAnimationCompat, bounds: WindowInsetsAnimationCompat.BoundsCompat): WindowInsetsAnimationCompat.BoundsCompat {
                val typeFlag = animation.typeMask.and(WindowInsetsCompat.Type.ime())
                val insetsCompat = ViewCompat.getRootWindowInsets(window.decorView)
                hasSoftInput = insetsCompat?.isVisible(WindowInsetsCompat.Type.ime()) ?: false

                if (hasSoftInput && typeFlag != 0) {
                    val navigationBarH = insetsCompat?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom ?: 0
                    val imeH = insetsCompat?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0
                    var keyboardH = if (imeH != 0) imeH else bounds.upperBound.bottom
                    if (keyboardH == 0) {

                        keyboardH = softInputHeightCalculatorOnStart?.invoke(animation, bounds)?: 0
                    }
                    val realKeyboardH = keyboardH - navigationBarH

                    val panelHeight = panelContainer.layoutParams.height
                    if (realKeyboardH > 0 && panelHeight != realKeyboardH) {
                        panelContainer.layoutParams.height = realKeyboardH
                        lastKeyboardHeight = realKeyboardH
                        PanelUtil.setKeyBoardHeight(context, realKeyboardH)
                    }
                    // 当键盘高度小于已偏移的高度时，调整回键盘高度
                    if (keyboardH > 0 && hasSoftInput) {
                        val maxSoftInputTop = window.decorView.bottom - keyboardH
                        val location = getLocationOnWindow(this@PanelSwitchLayout)
                        val floatInitialBottom = location[1] + height
                        val maxOffset = (maxSoftInputTop - floatInitialBottom).toFloat()
                        if (panelContainer.translationY < maxOffset) {
                            updatePanelStateByAnimation(-maxOffset.toInt())
                        }
                    }
                }
                return bounds
            }

            override fun onProgress(insets: WindowInsetsCompat, runningAnimations: MutableList<WindowInsetsAnimationCompat>): WindowInsetsCompat {
                if (isPanelState(panelId)) {
                } else {
                    val logFormatter = LogFormatter.setUp()
                    logFormatter.addContent(value = "keyboard animation progress")
                    // 找到键盘（IME）动画
                    val imeAnimation = runningAnimations.find { it.typeMask.and(WindowInsetsCompat.Type.ime()) != 0 }
                    val fraction = imeAnimation?.fraction ?: return insets                          // 动画进度 【0,1】
                    var softInputHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom    // 键盘高度，这个高度包含了导航栏
                    if (hasSoftInput && fraction != 0f && softInputHeight == 0) { // 部分手机在键盘动画执行过程中无法获取到键盘高度
                        // 这里提供一个键盘计算的方法，业务方可以自行兼容
                        softInputHeight = softInputHeightCalculatorOnProgress?.invoke(insets, runningAnimations) ?: 0
                    }
                    val softInputTop = window.decorView.bottom - softInputHeight            // 键盘top位于当前window的位置
                    logFormatter.addContent("fraction", "$fraction")
                    logFormatter.addContent("softInputHeight", "$softInputHeight")
                    logFormatter.addContent("decorView.bottom", "${window.decorView.bottom}")
                    val location = getLocationOnWindow(this@PanelSwitchLayout)
                    val floatInitialBottom = height + location[1]                           // PanelSwitchLayout控件位于当前window的Bottom
                    val compatPanelHeight = getCompatPanelHeight(panelId)
                    if (hasSoftInput) { // 键盘显示时
                        if (softInputTop < floatInitialBottom) {
                            val offset = (softInputTop - floatInitialBottom).toFloat()
                            if (panelContainer.translationY > offset) {
                                panelContainer.translationY = offset
                                contentContainer.translationContainer(contentScrollMeasurers, compatPanelHeight, offset)
                                logFormatter.addContent("translationY", "$offset")
                                transitionY = offset
                            }
                        }
                    } else {// 键盘关闭时
                        // 有些设备隐藏键盘时，softInputHeight的值一直等于0，此时用 fraction 来计算偏移量
                        if (softInputHeight > 0) {
                            val offset = min(softInputTop - floatInitialBottom, 0).toFloat()
                            panelContainer.translationY = offset
                            contentContainer.translationContainer(contentScrollMeasurers, compatPanelHeight, offset)
                            logFormatter.addContent("translationY", "$offset")
                        } else {
                            val offset = min(transitionY - transitionY * (fraction + 0.5f), 0f)
                            panelContainer.translationY = offset
                            contentContainer.translationContainer(contentScrollMeasurers, compatPanelHeight, offset)
                            logFormatter.addContent("translationY", "$offset")
                        }
                    }
                    logFormatter.log("onProgress")
                }
                return insets
            }
        }
        ViewCompat.setWindowInsetsAnimationCallback(window.decorView, callback)
    }


    /**
     * 是否支持Android 11 方案获取键盘高度
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
    private fun supportKeyboardFeature(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    /**
     * 是否支持键盘过渡动画
     */
    private fun supportKeyboardAnimation(): Boolean {
        if (!this::window.isInitialized) {
            return false
        }
        return window.decorView.isSystemInsetsAnimationSupport()
    }

    private  var lastImeHeight :Int = -1
    private var isLayoutSizeChanged = false
    /**
     * Android 11 监听键盘变化
     */
    private fun keyboardChangedListener30Impl() {
        if (!this::window.isInitialized) {
            return
        }

        val rootView = windowInsetsRootView ?: window.decorView.rootView
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            // 键盘
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            // 导航
            val hasNavigation = insets.isVisible(WindowInsetsCompat.Type.navigationBars())
            val navigationH = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            val statusBarH = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val hasStatusBar = insets.isVisible(WindowInsetsCompat.Type.statusBars())
            val systemBarH = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            val hasSystemBar = insets.isVisible(WindowInsetsCompat.Type.statusBars())

            val displayCutoutH = insets.getInsets(WindowInsetsCompat.Type.displayCutout()).top
            val hasDisplayCutout = insets.isVisible(WindowInsetsCompat.Type.displayCutout())

            Log.d("#WindowInsetsListener",
                "imeVisible $imeVisible \nimeHeight : $imeHeight \n" +
                        "hasNavigation : $hasNavigation \n navigationH : $navigationH \n" +
                        "hasStatusBar : $hasStatusBar \n statusBarH : $statusBarH \n" +
                        "hasSystemBar : $hasSystemBar \n systemBarH : $systemBarH \n"+
                        "hasDisplayCutout : $hasDisplayCutout \ndisplayCutoutH : $displayCutoutH"
            )
            if(imeVisible && imeHeight - navigationH > 0)PanelUtil.setKeyBoardHeight(context, imeHeight - navigationH)
            if(isKeyboardState() && !imeVisible){
                checkoutPanel(Constants.PANEL_NONE,false)
            }
            if(lastImeHeight==-1 || lastImeHeight != imeHeight )isLayoutSizeChanged = true
            lastImeHeight = imeHeight
            isRequestLayout = true
            notifyKeyboardState(imeVisible)
            requestLayout()
            ViewCompat.onApplyWindowInsets(view, insets)
        }
        ViewCompat.requestApplyInsets(rootView)
    }



    /**
     * 键盘高度发生变化时，同步键盘高度
     */
    private fun trySyncKeyboardHeight(keyboardHeight: Int) {
        Log.d(TAG, "trySyncKeyboardHeight: $keyboardHeight")
        if (lastKeyboardHeight > 0 && keyboardHeight > 0) {
            // 采用键盘过渡动画的方案需要同步高度，采用 onLayout 方案的不需要通过这个方法进行同步
            if (keyboardAnimationFeature && panelContainer.translationY != 0F) {
                updatePanelStateByAnimation(keyboardHeight)
            }
        }
    }


    fun tryBindKeyboardChangedListener() {
        if (hasAttachLister) {
            return
        }
        if (!this::window.isInitialized) {
            return
        }
        keyboardChangedListener30Impl()
        hasAttachLister = true
    }


    private fun releaseKeyboardChangedListener() {
        if (!this::window.isInitialized) {
            return
        }
        val rootView = windowInsetsRootView ?: window.decorView.rootView
        ViewCompat.setOnApplyWindowInsetsListener(rootView, null)
        hasAttachLister = false
    }


    private fun notifyViewClick(view: View) {
        viewClickListeners?.let {
            for (listener in it) {
                listener.onClickBefore(view)
            }
        }
    }

    private fun notifyKeyboardState(visible: Boolean) {
        keyboardStatusListeners?.let {
            for (listener in it) {
                listener.onKeyboardChange(visible, if (visible) getKeyBoardHeight(context) else 0)
            }
        }
    }

    private fun notifyEditFocusChange(view: View, hasFocus: Boolean) {
        editFocusChangeListeners?.let {
            for (listener in it) {
                listener.onFocusChange(view, hasFocus)
            }
        }
    }

    private fun notifyPanelChange(panelId: Int) {
        panelChangeListeners?.let {
            for (listener in it) {
                when (panelId) {
                    Constants.PANEL_NONE -> {
                        listener.onNone()
                    }
                    Constants.PANEL_KEYBOARD -> {
                        listener.onKeyboard()
                    }
                    else -> {
                        listener.onPanel(panelContainer.getPanelView(panelId))
                    }
                }
            }
        }
    }

    private fun notifyPanelSizeChange(panelView: IPanelView?, portrait: Boolean, oldWidth: Int, oldHeight: Int, width: Int, height: Int) {
        panelChangeListeners?.let {
            for (listener in it) {
                listener.onPanelSizeChange(panelView, portrait, oldWidth, oldHeight, width, height)
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        assertView()
        initListener()
    }

    override fun assertView() {
        if (childCount != 2) {
            throw RuntimeException("PanelSwitchLayout -- PanelSwitchLayout should has two children,the first is ContentContainer,the other is PanelContainer！")
        }
        val firstView = getChildAt(0)
        val secondView = getChildAt(1)
        if (firstView !is IContentContainer) {
            throw RuntimeException("PanelSwitchLayout -- the first view isn't a IContentContainer")
        }
        contentContainer = firstView
        if (secondView !is PanelContainer) {
            throw RuntimeException("PanelSwitchLayout -- the second view is a ContentContainer, but the other isn't a PanelContainer！")
        }
        panelContainer = secondView
    }

    private fun getContentContainerTop(scrollOutsideHeight: Int): Int {
        val result = if (contentScrollOutsizeEnable) {
            if (isResetState()) 0 else -scrollOutsideHeight
        } else 0

        return result;
    }

    private fun getContentContainerHeight(allHeight: Int, paddingTop: Int, scrollOutsideHeight: Int): Int {
        return allHeight - paddingTop -
                if (!contentScrollOutsizeEnable && !isResetState()) scrollOutsideHeight else 0
    }

    private fun getCompatPanelHeight(panelId: Int): Int {
        var  result = getKeyBoardHeight(context)
        if(isPanelState(panelId)){
            if(isInMultiWindowMode() || result < minLimitOpenKeyboardHeight){
                if(this::window.isInitialized){
                    val preComputeContainerHeight = preComputeContainerHeight(window)
                    result = preComputeContainerHeight
                }
            }
        }
        return result
    }


    private fun preComputeContainerHeight(window: Window):Int{
        val height = getAppRealHeight(window)
        return height / 3
    }



    private fun isInMultiWindowMode():Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            (context as Activity).isInMultiWindowMode
        } else {
            // 在 Android N (API 24) 之前，多窗口模式不支持，始终返回 false
            false
        }
    }

    private val rect = Rect()
    private lateinit var layoutRect : Rect

    private fun isLayoutChanged(l: Int, t: Int, r: Int, b: Int): Boolean {
        if(!this::layoutRect.isInitialized){
            layoutRect = Rect()
        }
        val change = layoutRect.run {
            layoutRect.left != l || layoutRect.top != top || layoutRect.right != r || layoutRect.bottom != b
        }
        layoutRect.left = l
        layoutRect.top = t
        layoutRect.right = r
        layoutRect.bottom = b
        return change
    }
    /**
     * @param changedue
     * @param l
     * @param t
     * @param r
     * @param b
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val visibility = visibility
        if (visibility != View.VISIBLE) {

            return
        }
        // 这里是使用键盘过渡动画方案
        if (keyboardAnimationFeature) {
            super.onLayout(changed, l, t, r, b)
            val compatPanelHeight = getCompatPanelHeight(panelId)
            if (panelId != Constants.PANEL_NONE && compatPanelHeight != 0) {
                val translationY = panelContainer.translationY
                contentContainer.translationContainer(contentScrollMeasurers, compatPanelHeight, translationY)
            }
            return
        }
        val logFormatter = LogFormatter.setUp()
        /**
         * 当还没有进行输入法高度获取时，由于兼容性测试之后设置的默认高度无法兼容所有机型
         * 为了业务能100%兼容，开放设置每个面板的默认高度，待输入法高度获取之后统一高度。
         */

        val window = getWindow()


        val paddingTop = paddingTop


        val windowInset = ViewCompat.getRootWindowInsets(window.decorView)!!
        val navigationBarH = windowInset.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
        val hasNavigation = windowInset.isVisible(WindowInsetsCompat.Type.navigationBars())
        val statusBarH = windowInset.getInsets(WindowInsetsCompat.Type.statusBars()).top
        val systemBarH = windowInset.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
        val displayCutout = windowInset.getInsets(WindowInsetsCompat.Type.displayCutout()).top
        val imeVisible = windowInset.isVisible(WindowInsetsCompat.Type.ime())
        //包含了navigationBar高度
        val imeHeight = windowInset.getInsets(WindowInsetsCompat.Type.ime()).bottom

        //包含statusBar
        val localLocation = getLocationOnWindow(this)

        window.decorView.getWindowVisibleDisplayFrame(rect)
        //不包含systemBar
        var allHeight = rect.height()

        //visible部分
        var switchVisibleHeight = allHeight - localLocation[1] + statusBarH

        //总共高度
        val contentContainerHeight = b - t
        var compatPanelHeight = contentContainerHeight - switchVisibleHeight
        if(imeHeight == 0 || !imeVisible)compatPanelHeight = 0
        if(isPanelState() && !imeVisible)compatPanelHeight = getCompatPanelHeight(panelId)

        var contentContainerTop = -compatPanelHeight

        contentContainerTop += paddingTop
        var contentContainerBottom = contentContainerHeight + contentContainerTop

        val panelContainerTop = contentContainerBottom
        val panelContainerBottom = panelContainerTop + compatPanelHeight
        //计算实际bounds
        val changeBounds =  isBoundChange(l, contentContainerTop, r, panelContainerBottom)
        if (Constants.DEBUG) {
            logFormatter.addContent(value = "界面每一次 layout 的信息回调")
            val state = when (panelId) {
                Constants.PANEL_NONE -> "收起所有输入源"
                Constants.PANEL_KEYBOARD -> "显示键盘输入"
                else -> "显示面板输入"
            }
            logFormatter.addContent("displayCutout", "$displayCutout")
            logFormatter.addContent("currentPanelState", "$state")
            logFormatter.addContent("AppRealHeight", "${getAppRealHeight(window)}")
            logFormatter.addContent("getWindowVisibleDisplayFrame", "${rect.height()}")
            logFormatter.addContent("imeVisible", "$imeVisible")
            logFormatter.addContent("imeHeight", "$imeHeight")
            logFormatter.addContent("systemBarH", "$systemBarH")
            logFormatter.addContent("navigationBarH", "$navigationBarH")
            logFormatter.addContent("statusBarH", "$statusBarH")
            logFormatter.addContent("allHeight", "$allHeight")
            logFormatter.addContent("localLocation[y]", "${localLocation[1]}")
            logFormatter.addContent("paddingTop", "$paddingTop")
            logFormatter.addContent("keyboardH", "${getKeyBoardHeight(context)}")
            logFormatter.addContent("ContentContainerTop", "$contentContainerTop")
            logFormatter.addContent("ContentContainerH", "$contentContainerHeight")
            logFormatter.addContent("PanelContainerTop", "$panelContainerTop")
            logFormatter.addContent("PanelContainerH", "$compatPanelHeight")
            logFormatter.addContent("changeBounds", "$changeBounds")
            logFormatter.addContent("isLayoutSizeChanged", "$isLayoutSizeChanged")
        }


        if(isLayoutSizeChanged){
            isLayoutSizeChanged = false
            if (changeBounds) {
                val reverseResetState = reverseResetState()
                logFormatter.addContent("reverseResetState", "$reverseResetState")
                if (reverseResetState) {
                    setTransition(animationSpeed.toLong(), panelId)
                    logFormatter.addContent("setTransition", "true")
                }
            } else {
                //如果功能面板的互相切换，则需要判断是否存在高度不一致，如果不一致则需要过渡
                if (lastPanelHeight != -1 && lastPanelHeight != compatPanelHeight) {
                    setTransition(animationSpeed.toLong(), panelId)
                    logFormatter.addContent("setTransition", "true")
                }
            }
        }

        //处理第一个view contentContainer
        run {
            contentContainer.layoutContainer(
                l, contentContainerTop, r, contentContainerBottom,
                contentScrollMeasurers, compatPanelHeight, contentScrollOutsizeEnable, isResetState(), changed
            )
            logFormatter.addContent("contentContainer Layout", "($l,$contentContainerTop,$r,$contentContainerBottom)")
            contentContainer.changeContainerHeight(contentContainerHeight)
        }

        //处理第二个view panelContainer
        run {
            panelContainer.layout(l, panelContainerTop, r, panelContainerBottom)
            logFormatter.addContent("panelContainer Layout", "($l,$panelContainerTop,$r,$panelContainerBottom)")
            panelContainer.changeContainerHeight(compatPanelHeight)
        }
        this.lastPanelHeight = compatPanelHeight;
        contentContainer.getInputActionImpl().updateFullScreenParams(isFullScreen(window), panelId, compatPanelHeight)
        logFormatter.log("$TAG#onLayout")
    }



    private fun getWindow(): Window {
        return (context as Activity).window
    }
    private fun isFullScreen(window: Window): Boolean {
        val fullScreenFlag = WindowManager.LayoutParams.FLAG_FULLSCREEN
        return window.attributes.flags and fullScreenFlag == fullScreenFlag
    }

    override fun requestLayout() {
        isRequestLayout = false
        super.requestLayout()
    }
    private fun isBoundChange(l: Int, t: Int, r: Int, b: Int): Boolean {
        val change = realBounds == null || realBounds!!.run {
            this.left != l || this.top != top || this.right != r || this.bottom != b
        }
        realBounds = Rect(l, t, r, b)
        return change
    }

    internal fun isPanelState() = isPanelState(panelId)

    internal fun isKeyboardState() = isKeyboardState(panelId)

    internal fun isResetState() = isResetState(panelId)

    private fun isPanelState(panelId: Int) = !isResetState(panelId) && !isKeyboardState(panelId)

    private fun isKeyboardState(panelId: Int) = panelId == Constants.PANEL_KEYBOARD

    private fun isResetState(panelId: Int) = panelId == Constants.PANEL_NONE

    private fun reverseResetState(): Boolean = (isResetState(lastPanelId) && !isResetState(panelId))
            || (!isResetState(lastPanelId) && isResetState(panelId))


    @TargetApi(19)
    private fun setTransition(duration: Long, panelId: Int) {
        val changeBounds = ChangeBounds()
        changeBounds.duration = duration
        TransitionManager.beginDelayedTransition(this, changeBounds)
    }


    /**
     * This will be called when User press System Back Button.
     * 1. if keyboard is showing, should be hide;
     * 2. if you want to hide panel(exclude keyboard),you should call it before [android.support.v7.app.AppCompatActivity.onBackPressed] to hook it.
     *
     * @return if need hook
     */
    internal fun hookSystemBackByPanelSwitcher(): Boolean {
        if (!isResetState()) {
            //模仿系统输入法隐藏，如果直接掉  checkoutPanel(Constants.PANEL_NONE)，可能导致隐藏时上层 recyclerview 因为 layout 导致界面出现短暂卡顿。
            if (isKeyboardState()) {
                contentContainer.getInputActionImpl().hideKeyboard(true, true)
            } else {
                checkoutPanel(Constants.PANEL_NONE)
            }
            return true
        }
        return false
    }

    @JvmOverloads
    internal fun toKeyboardState(async: Boolean = false) {
        if (async) {
            post(keyboardStateRunnable)
        } else {
            contentContainer.getInputActionImpl().requestKeyboard()
        }
    }

    private var isRequestLayout = false
    /**
     * @param panelId
     * @return
     */
    internal fun checkoutPanel(panelId: Int, checkoutKeyboard: Boolean = true): Boolean {
        if (doingCheckout) {
            return false
        }
        doingCheckout = true

        if (panelId == this.panelId) {
            doingCheckout = false
            return false
        }

        when (panelId) {
            Constants.PANEL_NONE -> {
                postDelayed({ if(isRequestLayout) {
                    requestLayout()
                } }, 10)
                contentContainer.getInputActionImpl().hideKeyboard(checkoutKeyboard, true)
                contentContainer.getResetActionImpl().enableReset(false)
                if (keyboardAnimationFeature) {
                    updatePanelStateByAnimation(0)
                }
                this.lastPanelId = this.panelId
                this.panelId = panelId
                if(isPanelState(this.lastPanelId))isLayoutSizeChanged = true
                notifyPanelChange(this.panelId)
                isRequestLayout = true
                doingCheckout = false
            }

            Constants.PANEL_KEYBOARD -> {
                if (checkoutKeyboard) {
                    if (!contentContainer.getInputActionImpl().showKeyboard()) {
                        doingCheckout = false
                        return false
                    }
                }
                contentContainer.getResetActionImpl().enableReset(true)
                this.lastPanelId = this.panelId
                this.panelId = panelId
                notifyPanelChange(this.panelId)
                isRequestLayout = true
                doingCheckout = false
            }
            else -> {
                val size = Pair(measuredWidth - paddingLeft - paddingRight, getCompatPanelHeight(panelId))
                val oldSize = panelContainer.showPanel(panelId, size)
                if (size.first != oldSize.first || size.second != oldSize.second) {
                    notifyPanelSizeChange(panelContainer.getPanelView(panelId), isPortrait(context), oldSize.first, oldSize.second, size.first, size.second)
                }
                isRequestLayout = true
                contentContainer.getInputActionImpl().hideKeyboard(true, false)
                contentContainer.getResetActionImpl().enableReset(true)
                postDelayed({
                    if (isRequestLayout) {
                        requestLayout()
                    }
                }, 10)
                // Android 11 修改偏移量来显示面板
                if (keyboardAnimationFeature) {
                    val compatPanelHeight = getCompatPanelHeight(panelId)
                    updatePanelStateByAnimation(compatPanelHeight)
                }
                this.lastPanelId = this.panelId
                this.panelId = panelId
                notifyPanelChange(this.panelId)
                isLayoutSizeChanged = true
                doingCheckout = false
            }
        }

        return true
    }

    private fun translatePanelName( panelId:Int):String{
        return when{
            isPanelState(panelId)->"PanelState";
            isKeyboardState(panelId)->"KeyboardState";
            isResetState(panelId)->"NoneState"
            else->"unknowState"
        }
    }
    /**
     * 更新面板状态
     * @param expectHeight 期望高度
     */
    private fun updatePanelStateByAnimation(expectHeight: Int) {
        Log.d(TAG, "updatePanelStateByAnimation: $expectHeight")
        val translationY = panelContainer.translationY
        val targetY = -expectHeight.toFloat()
        if (translationY != targetY) {
            val compatPanelHeight = getCompatPanelHeight(panelId)
            val animation = ValueAnimator.ofFloat(translationY, targetY)
                .setDuration(animationSpeed.toLong())
            animation.addUpdateListener {
                val y = it.animatedValue as? Float ?: 0F
                panelContainer.translationY = y
                contentContainer.translationContainer(contentScrollMeasurers, compatPanelHeight, y)
            }
            animation.start()
        }
        // 尝试对其键盘高度
        val panelHeight = panelContainer.layoutParams.height
        if (expectHeight > 0 && panelHeight != expectHeight) {
            panelContainer.layoutParams.height = expectHeight
        }
    }


    companion object {
        const val TAG = "PanelSwitchLayout"
        private var preClickTime: Long = 0
    }
}
