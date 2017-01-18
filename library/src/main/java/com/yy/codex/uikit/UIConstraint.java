package com.yy.codex.uikit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Created by cuiminghui on 2017/1/4.
 */

public class UIConstraint {

    public static UIConstraint center() {
        UIConstraint constraint = new UIConstraint();
        constraint.centerHorizontally = true;
        constraint.centerVertically = true;
        return constraint;
    }

    public static UIConstraint full() {
        UIConstraint constraint = new UIConstraint();
        constraint.centerHorizontally = true;
        constraint.centerVertically = true;
        constraint.width = "100%";
        constraint.height = "100%";
        return constraint;
    }

    public enum LayoutRelate {
        RelateToGroup,
        RelateToPrevious,
    }

    public boolean disabled = false;

    @NonNull
    public LayoutRelate alignmentRelate = LayoutRelate.RelateToGroup;
    public boolean centerHorizontally = false;
    public boolean centerVertically = false;

    @NonNull
    public LayoutRelate sizeRelate = LayoutRelate.RelateToGroup;
    @Nullable
    public String width = null;
    @Nullable
    public String height = null;

    @NonNull
    public LayoutRelate pinRelate = LayoutRelate.RelateToGroup;
    @Nullable
    public String top = null;
    @Nullable
    public String left = null;
    @Nullable
    public String right = null;
    @Nullable
    public String bottom = null;

    private boolean needsLayout = true;
    @NonNull
    private CGRect lastSuperviewFrame = new CGRect(0, 0, 0, 0);
    @NonNull
    private CGRect lastPreviousViewFrame = new CGRect(0, 0, 0, 0);

    public void setNeedsLayout() {
        needsLayout = true;
    }

    @NonNull
    public CGRect requestFrame(@NonNull UIView myView, @Nullable UIView superView, @Nullable UIView previousView) {
        if (disabled) {
            return myView.getFrame();
        }
        if ((superView != null && lastSuperviewFrame.equals(superView.getFrame())) &&
            (previousView != null && lastPreviousViewFrame.equals(previousView.getFrame())) &&
            !needsLayout) {
            return myView.getFrame();
        }
        needsLayout = false;
        lastSuperviewFrame = superView != null ? superView.getFrame() : new CGRect(0,0,0,0);
        lastPreviousViewFrame = previousView != null ? previousView.getFrame() : new CGRect(0,0,0,0);
        double x = Double.NaN, y = Double.NaN, mx = Double.NaN, my = Double.NaN, cx = Double.NaN, cy = Double.NaN;
        double w = Double.NaN, h = Double.NaN;
        boolean xorw = false, yorh = false;
        if (width != null) {
            String formula = width;
            if (sizeRelate == LayoutRelate.RelateToPrevious) {
                w = requestValue(0, previousView != null ? previousView.getFrame().size.width : 0.0, formula);
            }
            else {
                w = requestValue(0, superView != null ? superView.getFrame().size.width : 0.0, formula);
            }
        }
        else {
            CGSize iSize = myView.intrinsicContentSize();
            if (iSize.width > 0.0) {
                w = Math.ceil(iSize.width);
            }
        }
        if (height != null) {
            String formula = height;
            if (sizeRelate == LayoutRelate.RelateToPrevious) {
                h = requestValue(0, previousView != null ? previousView.getFrame().size.height : 0.0, formula);
            }
            else {
                h = requestValue(0, superView != null ? superView.getFrame().size.height : 0.0, formula);
            }
        }
        else {
            if (width != null && !Double.isNaN(w)) {
                myView.setMaxWidth(w);
            }
            CGSize iSize = myView.intrinsicContentSize();
            if (iSize.height > 0.0) {
                h = Math.ceil(iSize.height);
            }
        }
        if (centerHorizontally && alignmentRelate == LayoutRelate.RelateToGroup) {
            cx = superView != null ? superView.getFrame().size.width / 2.0 : 0.0;
        }
        else if (centerHorizontally && alignmentRelate == LayoutRelate.RelateToPrevious) {
            cx = previousView != null ? previousView.getCenter().x : 0.0;
        }
        if (centerVertically && alignmentRelate == LayoutRelate.RelateToGroup) {
            cy = superView != null ? superView.getFrame().size.height / 2.0 : 0.0;
        }
        else if (centerVertically && alignmentRelate == LayoutRelate.RelateToPrevious) {
            cy = previousView != null ? previousView.getCenter().y : 0.0;
        }
        if (top != null) {
            String formula = top;
            double t = 0.0;
            if (pinRelate == LayoutRelate.RelateToGroup) {
                t = requestValue(0, superView != null ? superView.getFrame().size.height : 0.0, formula);
            }
            else if (pinRelate == LayoutRelate.RelateToPrevious) {
                t = requestValue(0, previousView != null ? previousView.getFrame().size.height : 0.0, formula);
                if (previousView != null) {
                    t = previousView.getFrame().origin.y + t;
                }
            }
            if (centerVertically) {
                cy += t;
            }
            else {
                y = t;
            }
        }
        if (bottom != null) {
            String formula = bottom;
            double t = 0.0;
            if (pinRelate == LayoutRelate.RelateToGroup) {
                t = requestValue(0, superView != null ? superView.getFrame().size.height : 0.0, formula);
            }
            else if (pinRelate == LayoutRelate.RelateToPrevious) {
                t = requestValue(0, previousView != null ? previousView.getFrame().size.height : 0.0, formula);
                if (previousView != null) {
                    t = previousView.getFrame().origin.y + previousView.getFrame().size.height - t;
                }
            }
            if (centerVertically) {
                cy -= t;
            }
            else if (pinRelate == LayoutRelate.RelateToPrevious) {
                y = t;
                yorh = true;
            }
            else {
                my = t;
            }
        }
        if (left != null) {
            String formula = left;
            double t = 0.0;
            if (pinRelate == LayoutRelate.RelateToGroup) {
                t = requestValue(0, superView != null ? superView.getFrame().size.width : 0.0, formula);
            }
            else if (pinRelate == LayoutRelate.RelateToPrevious) {
                t = requestValue(0, previousView != null ? previousView.getFrame().size.width : 0.0, formula);
                if (previousView != null) {
                    t = previousView.getFrame().origin.x + t;
                }
            }
            if (centerHorizontally) {
                cx += t;
            }
            else {
                x = t;
            }
        }
        if (right != null) {
            String formula = right;
            double t = 0.0;
            if (pinRelate == LayoutRelate.RelateToGroup) {
                t = requestValue(0, superView != null ? superView.getFrame().size.width : 0.0, formula);
            }
            else if (pinRelate == LayoutRelate.RelateToPrevious) {
                t = requestValue(0, previousView != null ? previousView.getFrame().size.width : 0.0, formula);
                if (previousView != null) {
                    t = previousView.getFrame().origin.x + previousView.getFrame().size.width - t;
                }
            }
            if (centerHorizontally) {
                cx -= t;
            }
            else if (pinRelate == LayoutRelate.RelateToPrevious) {
                x = t;
                xorw = true;
            }
            else {
                mx = t;
            }
        }
        CGRect newFrame = new CGRect(
                !Double.isNaN(cx) ? cx : (!Double.isNaN(x) ? x : 0.0),
                !Double.isNaN(cy) ? cy : (!Double.isNaN(y) ? y : 0.0),
                !Double.isNaN(w) ? w : 0.0,
                !Double.isNaN(h) ? h : 0.0
        );
        if (!Double.isNaN(cx)) {
            newFrame = newFrame.setX(newFrame.origin.x - newFrame.size.width / 2.0);
        }
        if (!Double.isNaN(cy)) {
            newFrame = newFrame.setY(newFrame.origin.y - newFrame.size.height / 2.0);
        }
        if (!Double.isNaN(mx) && Double.isNaN(x)) {
            if (pinRelate == LayoutRelate.RelateToGroup) {
                newFrame = newFrame.setX((superView != null ? superView.getFrame().size.width : 0.0) - mx - newFrame.size.width);
            }
            else {
                newFrame = newFrame.setX((previousView != null ? previousView.getFrame().size.width : 0.0) - mx - newFrame.size.width);
            }
        }
        if (!Double.isNaN(my) && Double.isNaN(y)) {
            if (pinRelate == LayoutRelate.RelateToGroup) {
                newFrame = newFrame.setY((superView != null ? superView.getFrame().size.height : 0.0) - my - newFrame.size.height);
            }
            else {
                newFrame = newFrame.setY((previousView != null ? previousView.getFrame().size.height : 0.0) - my - newFrame.size.height);
            }
        }
        if (!Double.isNaN(mx) && !Double.isNaN(x)) {
            if (pinRelate == LayoutRelate.RelateToGroup) {
                newFrame = newFrame.setWidth((superView != null ? superView.getFrame().size.width : 0.0) - mx - x);
            }
            else {
                newFrame = newFrame.setWidth((previousView != null ? previousView.getFrame().size.width : 0.0) - mx - (x - (previousView != null ? previousView.getFrame().origin.x : 0.0)));
            }
        }
        if (!Double.isNaN(my) && !Double.isNaN(y)) {
            if (pinRelate == LayoutRelate.RelateToGroup) {
                newFrame = newFrame.setHeight((superView != null ? superView.getFrame().size.height : 0.0) - my - y);
            }
            else {
                newFrame = newFrame.setHeight((previousView != null ? previousView.getFrame().size.height : 0.0) - my - (y - (previousView != null ? previousView.getFrame().origin.y : 0.0)));
            }
        }
        if (xorw) {
            newFrame = newFrame.setX(newFrame.origin.x - newFrame.size.width);
        }
        if (yorh) {
            newFrame = newFrame.setY(newFrame.origin.y - newFrame.size.height);
        }
        if (Double.isNaN(newFrame.origin.x)) {
            newFrame = newFrame.setX(0.0);
        }
        if (Double.isNaN(newFrame.origin.y)) {
            newFrame = newFrame.setY(0.0);
        }
        if (Double.isNaN(newFrame.size.width)) {
            newFrame = newFrame.setWidth(0.0);
        }
        if (Double.isNaN(newFrame.size.height)) {
            newFrame = newFrame.setHeight(0.0);
        }
        return newFrame;
    }

    public double requestValue(double origin, double length, String formula) {
        formula = formula.replace("%", "/100.0*" + length);
        try {
            Expression e = new ExpressionBuilder(formula).build();
            return e.evaluate() + origin;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public boolean sizeCanFit() {
        return !centerHorizontally && !centerHorizontally && width == null && height == null &&
                top != null && left != null && bottom != null && right != null;
    }

}
