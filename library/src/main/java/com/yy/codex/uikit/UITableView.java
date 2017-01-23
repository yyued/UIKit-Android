package com.yy.codex.uikit;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by it on 17/1/22.
 */

public class UITableView extends UIScrollView {

    public interface UITableViewDelegate {
        void scrollViewDidScroll(UIScrollView scrollView);
        void scrollViewWillBeginDragging(UIScrollView scrollView);
        void scrollViewDidEndDragging(UIScrollView scrollView, boolean willDecelerate);
        void scrollViewWillBeginDecelerating(UIScrollView scrollView);
        void scrollViewDidEndDecelerating(UIScrollView scrollView);
    }

    public UITableView(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public UITableView(@NonNull Context context) {
        super(context);
    }

    public UITableView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public UITableView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UITableView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        super.init();
    }
}
