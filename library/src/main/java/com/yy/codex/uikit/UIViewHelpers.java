package com.yy.codex.uikit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

/**
 * Created by cuiminghui on 2017/1/16.
 */

class UIViewHelpers {

    @Nullable
    static public UIView hitTest(@NonNull UIView view, @NonNull CGPoint point, @NonNull MotionEvent event) {
        UIView[] views = view.getSubviews();
        if (!view.isUserInteractionEnabled() && !(view.getAlpha() > 0)) {
            return null;
        }
        if (pointInside(view, point)) {
            for (UIView subview: views) {
                if (!subview.isUserInteractionEnabled() || subview.getAlpha() <= 0) {
                    continue;
                }
                CGPoint convertedPoint = view.convertPoint(point, subview);
                UIView hitTestView = subview.hitTest(convertedPoint, event);
                if (hitTestView != null) {
                    return hitTestView;
                }
            }
            return view;
        }
        return null;
    }

    static public boolean pointInside(@NonNull UIView view, @NonNull CGPoint point) {
        double h = view.getFrame().getSize().getHeight();
        double w = view.getFrame().getSize().getWidth();
        double touchX = point.getX();
        double touchY = point.getY();
        if (touchY <= h && touchX <= w && touchY >= 0 && touchX >= 0) {
            return true;
        }
        return false;
    }

    @NonNull
    static public CGPoint convertPoint(@NonNull UIView view, @NonNull CGPoint point, @NonNull UIView toView) {
        if (view == toView) {
            return point;
        }
        UIView viewRoot = view;
        double viewX = viewRoot.getFrame().getOrigin().getX();
        double viewY = viewRoot.getFrame().getOrigin().getY();
        while (viewRoot.getSuperview() != null) {
            viewRoot = viewRoot.getSuperview();
            viewX += viewRoot.getFrame().getOrigin().getX();
            viewY += viewRoot.getFrame().getOrigin().getY();
        }
        UIView toViewRoot = toView;
        double toViewX = toViewRoot.getFrame().getOrigin().getX();
        double toViewY = toViewRoot.getFrame().getOrigin().getY();
        while (toViewRoot.getSuperview() != null) {
            toViewRoot = toViewRoot.getSuperview();
            toViewX += toViewRoot.getFrame().getOrigin().getX();
            toViewY += toViewRoot.getFrame().getOrigin().getY();
        }
        if (viewRoot != toViewRoot) {
            return new CGPoint(0, 0);
        }
        return new CGPoint(viewX - toViewX + point.getX(), viewY - toViewY + point.getY());
    }

}
