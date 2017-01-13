package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Rect;
import android.view.WindowManager;

/**
 * Created by cuiminghui on 2017/1/13.
 */

public class UIScreen {

    public static UIScreen mainScreen = new UIScreen();

    private Context mContext;

    public void setContext(Context context) {
        mContext = context;
    }

    public double scale() {
        if (mContext != null) {
            return mContext.getResources().getDisplayMetrics().scaledDensity;
        }
        else {
            return 1.0;
        }
    }

    public CGRect bounds() {
        if (mContext != null && scale() > 0.0) {
            WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
            Rect rect = new Rect();
            windowManager.getDefaultDisplay().getRectSize(rect);
            return new CGRect(rect.left / scale(), rect.top / scale(), rect.width() / scale(), rect.height() / scale());
        }
        else {
            return new CGRect(0, 0, 0, 0);
        }
    }

}
