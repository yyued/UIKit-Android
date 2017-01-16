package com.yy.codex.uikit;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

/**
 * Created by cuiminghui on 2017/1/16.
 */

class UIViewHelpers {

    static public UIView hitTest(@NonNull UIView view, @NonNull CGPoint point, @NonNull MotionEvent event) {
        UIView[] views = view.getSubviews();
        if (!view.isUserInteractionEnabled() && !(view.getAlpha() > 0)) {
            return null;
        }
        if (pointInside(view, point)) {
            for (UIView subview: views) {
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
        double h = view.getFrame().size.getHeight();
        double w = view.getFrame().size.getWidth();
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
        CGPoint convertPoint;
        UIView toViewSuperView = toView;
        UIView superView = view;
        do {
            convertPoint = convertPointToSubView(view, point, toViewSuperView);
            toViewSuperView = toViewSuperView.getSuperview();
        } while (toViewSuperView != view && toViewSuperView != null);
        if (toViewSuperView == null) {
            do {
                convertPoint = convertPointToSubView(view, point, superView);
                superView = superView.getSuperview();
            } while (superView != toViewSuperView && superView != null);
        }
        if (superView == null) {
            toViewSuperView = toView;
            superView = view;
            do {
                UIView innerToViewSuperView = toViewSuperView.getSuperview();
                UIView innerSuperView = superView.getSuperview();
                if (innerToViewSuperView == superView) {
                    break;
                }
                convertPoint = convertPointToSuperView(view, convertPoint, superView);
                if (innerToViewSuperView != null) {
                    toViewSuperView = innerToViewSuperView;
                }
                if (innerSuperView != null) {
                    superView = innerSuperView;
                }
            } while (toViewSuperView != superView);
            if (toViewSuperView != null && superView != null) {
                double toX = toView.getFrame().origin.getX();
                double toY = toView.getFrame().origin.getY();
                return new CGPoint(convertPoint.getX() - toX, convertPoint.getY() - toY);
            }
        }
        return convertPoint;
    }

    @NonNull
    static private CGPoint convertPointToSuperView(@NonNull UIView view, @NonNull CGPoint point, @NonNull UIView superView) {
        double x = superView.getFrame().origin.getX();
        double y = superView.getFrame().origin.getY();
        return new CGPoint(point.getX() + x, point.getY() + y);
    }

    @NonNull
    static private CGPoint convertPointToSubView(@NonNull UIView view, @NonNull CGPoint point, @NonNull UIView subView) {
        double x = subView.getFrame().origin.getX();
        double y = subView.getFrame().origin.getY();
        return new CGPoint(point.getX() - x, point.getY() - y);
    }

}
