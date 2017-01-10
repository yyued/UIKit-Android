package com.yy.codex.uikit.sample;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by it on 17/1/5.
 */

public class TestLayout extends ScrollView {
    GestureDetector gestureDetector;

    private Scroller mScroller;
    private boolean flag=true;

    public TestLayout(Context context) {
        super(context);
        mScroller=new Scroller(context);
    }

    public TestLayout(Context context, AttributeSet attrs) {

        super(context, attrs);
        mScroller=new Scroller(context);
    }

    public TestLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller=new Scroller(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mScroller=new Scroller(context);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    //    @Override
//    public void computeScroll() {
//        super.computeScroll();
//        if(mScroller.computeScrollOffset()){
//            scrollTo(mScroller.getCurrX(), 0);
//            //使其再次调用computeScroll()直至滑动结束,即不满足if条件
//            postInvalidate();
//        }
//    }
//
//    public void beginScroll(){
//        if (flag) {
//            mScroller.startScroll(0, 0, -100, 0, 2500);
//            flag = false;
//        } else {
//            mScroller.startScroll(0, 0, 0, 0, 1500);
//            flag = true;
//        }
//        //调用invalidate();使其调用computeScroll()
////        invalidate();
//    }
}
