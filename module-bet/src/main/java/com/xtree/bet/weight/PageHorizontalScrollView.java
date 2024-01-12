package com.xtree.bet.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

public class PageHorizontalScrollView extends HorizontalScrollView {
    /**
     * 数据定义
     */
    private int subChildCount = 0;
    private ViewGroup firstChild = null;
    private int downX = 0;
    private int currentPage = 0;
    private List<Integer> viewList = new ArrayList<Integer>();

    private OnPageSelectedListener onPageSelectedListener;

    public void setOnPageSelectedListener(OnPageSelectedListener onPageSelectedListener) {
        this.onPageSelectedListener = onPageSelectedListener;
    }

    /**
     * 构造方法
     * @author caizhiming
     */
    public PageHorizontalScrollView(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public PageHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public PageHorizontalScrollView(Context context) {
        super(context);
        init();
    }
    private void init() {
        setHorizontalScrollBarEnabled(false);//设置原有的滚动无效
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildInfo();
    }
    /**
     * 获取子视图信息
     * @author caizhiming
     */
    public void getChildInfo() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                firstChild = (ViewGroup) getChildAt(0);
                if (firstChild != null) {
                    subChildCount = firstChild.getChildCount();
                    for (int i = 0; i < subChildCount; i++) {
                        if (((View) firstChild.getChildAt(i)).getWidth() > 0) {
                            viewList.add(((View) firstChild.getChildAt(i)).getLeft());
                        }
                    }
                }
            }
        }, 100);

    }
    /**
     * 触摸监听时间
     * @author caizhiming
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (Math.abs((ev.getX() - downX)) > getWidth() / 4) {
                    if (ev.getX() - downX > 0) {
                        smoothScrollToPrePage();
                    } else {
                        smoothScrollToNextPage();
                    }
                } else {
                    smoothScrollToCurrent();
                }
                return true;
            }
        }
        return super.onTouchEvent(ev);
    }
    /**
     * 滑动到当前页
     * @author caizhiming
     */
    private void smoothScrollToCurrent() {
        smoothScrollTo(viewList.get(currentPage), 0);
        if(onPageSelectedListener != null){
            onPageSelectedListener.onPageSelected(currentPage);
        }
    }
    /**
     * 滑动到下一页
     * @author caizhiming
     */
    private void smoothScrollToNextPage() {
        if (currentPage < subChildCount - 1) {
            currentPage++;
            smoothScrollTo(getMeasuredWidth(), 0);
            if(onPageSelectedListener != null){
                onPageSelectedListener.onPageSelected(currentPage);
            }
        }
    }
    /**
     * 滑动到上一页
     * @author caizhiming
     */
    private void smoothScrollToPrePage() {
        if (currentPage > 0) {
            currentPage--;
            smoothScrollTo(0, 0);
            if(onPageSelectedListener != null){
                onPageSelectedListener.onPageSelected(currentPage);
            }
        }
    }
    /**
     * 滑动到下一页
     * @author caizhiming
     */
    public void nextPage() {
        smoothScrollToNextPage();
    }
    /**
     * 滑动到上一页
     * @author caizhiming
     */
    public void prePage() {
        smoothScrollToPrePage();
    }
    /**
     * 跳转到指定的页面
     *
     * @param page
     * @author caizhiming
     */
    public boolean gotoPage(int page) {
        if (page > 0 && page < subChildCount - 1) {
            smoothScrollTo(viewList.get(page), 0);
            currentPage = page;
            return true;
        }
        return false;
    }

    public interface OnPageSelectedListener{
        void onPageSelected(int currentPage);
    }
}
