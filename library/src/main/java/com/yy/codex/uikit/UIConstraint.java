package com.yy.codex.uikit;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Created by cuiminghui on 2017/1/4.
 */

enum UIConstraintLayoutRelate {
    RelateToGroup,
    RelateToPrevious,
}

public class UIConstraint {

    public boolean disabled = false;

    public UIConstraintLayoutRelate aligmentRelate = UIConstraintLayoutRelate.RelateToGroup;
    public boolean centerHorizontally = false;
    public boolean centerVertically = false;

    public UIConstraintLayoutRelate sizeRelate = UIConstraintLayoutRelate.RelateToGroup;
    public String width = null;
    public String height = null;

    public UIConstraintLayoutRelate pinRelate = UIConstraintLayoutRelate.RelateToGroup;
    public String top = null;
    public String left = null;
    public String right = null;
    public String bottom = null;

    private boolean needsLayout = true;
    private CGRect lastSuperviewFrame = new CGRect(0, 0, 0, 0);
    private CGRect lastPreviousViewFrame = new CGRect(0, 0, 0, 0);

    public void setNeedsLayout() {
        needsLayout = true;
    }

    public CGRect requestFrame(UIView myView, UIView superView, UIView previousView) {
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
            if (sizeRelate == UIConstraintLayoutRelate.RelateToPrevious) {
                w = requestValue(0, previousView != null ? previousView.getFrame().size.getWidth() : 0.0, formula);
            }
            else {
                w = requestValue(0, superView != null ? superView.getFrame().size.getWidth() : 0.0, formula);
            }
        }
        else {
            CGSize iSize = myView.intrinsicContentSize();
            if (iSize.getWidth() > 0.0) {
                w = Math.ceil(iSize.getWidth());
            }
        }
        if (height != null) {
            String formula = height;
            if (sizeRelate == UIConstraintLayoutRelate.RelateToPrevious) {
                h = requestValue(0, previousView != null ? previousView.getFrame().size.getHeight() : 0.0, formula);
            }
            else {
                h = requestValue(0, superView != null ? superView.getFrame().size.getHeight() : 0.0, formula);
            }
        }
        else {
            if (width != null && !Double.isNaN(w)) {
                myView.setMaxWidth(w);
            }
            CGSize iSize = myView.intrinsicContentSize();
            if (iSize.getHeight() > 0.0) {
                h = Math.ceil(iSize.getHeight());
            }
        }
        if (centerHorizontally && aligmentRelate == UIConstraintLayoutRelate.RelateToGroup) {
            cx = superView != null ? superView.getFrame().size.getWidth() / 2.0 : 0.0;
        }
        else if (centerHorizontally && aligmentRelate == UIConstraintLayoutRelate.RelateToPrevious) {
            cx = previousView != null ? previousView.getCenter().getX() : 0.0;
        }
        if (centerVertically && aligmentRelate == UIConstraintLayoutRelate.RelateToGroup) {
            cy = superView != null ? superView.getFrame().size.getHeight() / 2.0 : 0.0;
        }
        else if (centerVertically && aligmentRelate == UIConstraintLayoutRelate.RelateToPrevious) {
            cy = previousView != null ? previousView.getCenter().getY() : 0.0;
        }
        if (top != null) {
            String formula = top;
            double t = 0.0;
            if (pinRelate == UIConstraintLayoutRelate.RelateToGroup) {
                t = requestValue(0, superView != null ? superView.getFrame().size.getHeight() : 0.0, formula);
            }
            else if (pinRelate == UIConstraintLayoutRelate.RelateToPrevious) {
                t = requestValue(0, previousView != null ? previousView.getFrame().size.getHeight() : 0.0, formula);
                if (previousView != null) {
                    t = previousView.getFrame().origin.getY() + t;
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
            if (pinRelate == UIConstraintLayoutRelate.RelateToGroup) {
                t = requestValue(0, superView != null ? superView.getFrame().size.getHeight() : 0.0, formula);
            }
            else if (pinRelate == UIConstraintLayoutRelate.RelateToPrevious) {
                t = requestValue(0, previousView != null ? previousView.getFrame().size.getHeight() : 0.0, formula);
                if (previousView != null) {
                    t = previousView.getFrame().origin.getY() + previousView.getFrame().size.getHeight() - t;
                }
            }
            if (centerVertically) {
                cy -= t;
            }
            else if (pinRelate == UIConstraintLayoutRelate.RelateToPrevious) {
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
            if (pinRelate == UIConstraintLayoutRelate.RelateToGroup) {
                t = requestValue(0, superView != null ? superView.getFrame().size.getWidth() : 0.0, formula);
            }
            else if (pinRelate == UIConstraintLayoutRelate.RelateToPrevious) {
                t = requestValue(0, previousView != null ? previousView.getFrame().size.getWidth() : 0.0, formula);
                if (previousView != null) {
                    t = previousView.getFrame().origin.getX() + t;
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
            if (pinRelate == UIConstraintLayoutRelate.RelateToGroup) {
                t = requestValue(0, superView != null ? superView.getFrame().size.getWidth() : 0.0, formula);
            }
            else if (pinRelate == UIConstraintLayoutRelate.RelateToPrevious) {
                t = requestValue(0, previousView != null ? previousView.getFrame().size.getWidth() : 0.0, formula);
                if (previousView != null) {
                    t = previousView.getFrame().origin.getX() + previousView.getFrame().size.getWidth() - t;
                }
            }
            if (centerHorizontally) {
                cx -= t;
            }
            else if (pinRelate == UIConstraintLayoutRelate.RelateToPrevious) {
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
            newFrame = newFrame.setX(newFrame.origin.getX() - newFrame.size.getWidth() / 2.0);
        }
        if (!Double.isNaN(cy)) {
            newFrame = newFrame.setY(newFrame.origin.getY() - newFrame.size.getHeight() / 2.0);
        }
        if (!Double.isNaN(mx) && Double.isNaN(x)) {
            if (pinRelate == UIConstraintLayoutRelate.RelateToGroup) {
                newFrame = newFrame.setX((superView != null ? superView.getFrame().size.getWidth() : 0.0) - mx - newFrame.size.getWidth());
            }
            else {
                newFrame = newFrame.setX((previousView != null ? previousView.getFrame().size.getWidth() : 0.0) - mx - newFrame.size.getWidth());
            }
        }
        if (!Double.isNaN(my) && Double.isNaN(y)) {
            if (pinRelate == UIConstraintLayoutRelate.RelateToGroup) {
                newFrame = newFrame.setY((superView != null ? superView.getFrame().size.getHeight() : 0.0) - my - newFrame.size.getHeight());
            }
            else {
                newFrame = newFrame.setY((previousView != null ? previousView.getFrame().size.getHeight() : 0.0) - my - newFrame.size.getHeight());
            }
        }
        if (!Double.isNaN(mx) && !Double.isNaN(x)) {
            if (pinRelate == UIConstraintLayoutRelate.RelateToGroup) {
                newFrame = newFrame.setWidth((superView != null ? superView.getFrame().size.getWidth() : 0.0) - mx - x);
            }
            else {
                newFrame = newFrame.setWidth((previousView != null ? previousView.getFrame().size.getWidth() : 0.0) - mx - (x - (previousView != null ? previousView.getFrame().origin.getX() : 0.0)));
            }
        }
        if (!Double.isNaN(my) && !Double.isNaN(y)) {
            if (pinRelate == UIConstraintLayoutRelate.RelateToGroup) {
                newFrame = newFrame.setHeight((superView != null ? superView.getFrame().size.getHeight() : 0.0) - my - y);
            }
            else {
                newFrame = newFrame.setHeight((previousView != null ? previousView.getFrame().size.getHeight() : 0.0) - my - (y - (previousView != null ? previousView.getFrame().origin.getY() : 0.0)));
            }
        }
        if (xorw) {
            newFrame = newFrame.setX(newFrame.origin.getX() - newFrame.size.getWidth());
        }
        if (yorh) {
            newFrame = newFrame.setY(newFrame.origin.getY() - newFrame.size.getHeight());
        }
        if (Double.isNaN(newFrame.origin.getX())) {
            newFrame = newFrame.setX(0.0);
        }
        if (Double.isNaN(newFrame.origin.getY())) {
            newFrame = newFrame.setY(0.0);
        }
        if (Double.isNaN(newFrame.size.getWidth())) {
            newFrame = newFrame.setWidth(0.0);
        }
        if (Double.isNaN(newFrame.size.getHeight())) {
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
