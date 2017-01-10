package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by it on 17/1/6.
 */

public class UIScrollView extends ScrollView {
    public UIScrollView(Context context) {
        super(context);
    }

    public UIScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
