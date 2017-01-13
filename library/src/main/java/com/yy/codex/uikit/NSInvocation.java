package com.yy.codex.uikit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * Created by cuiminghui on 2017/1/10.
 */

public class NSInvocation {

    @Nullable private WeakReference<Object> mTarget;
    @Nullable private String mSelector;

    public NSInvocation(@NonNull Object target, @NonNull String selector) {
        this.mTarget = new WeakReference<>(target);
        this.mSelector = selector;
    }

    @Nullable
    public Object getTarget() {
        Object target = this.mTarget.get();
        if (target != null) {
            return target;
        }
        return null;
    }

    public void setTarget(Object target) {
        this.mTarget = new WeakReference<Object>(target);
    }

    @NonNull
    public String getSelector() {
        return mSelector;
    }

    public void setSelector(@NonNull String selector) {
        this.mSelector = selector;
    }

    public void invoke(Object[] arguments) throws Exception {
        Object target = getTarget();
        if (target != null) {
            invoke(target, arguments);
        }
    }

    public void invoke(@Nullable Object target, @Nullable Object[] arguments) throws Exception {
        if (target == null || mSelector == null) {
            throw new Exception("Null target or null selector.");
        }
        Class clazz = target.getClass();
        String[] coms = mSelector.endsWith(":") ? (mSelector+"_").split(":") : mSelector.split(":");
        if (coms.length == 1) {
            Method method = clazz.getDeclaredMethod(mSelector);
            method.invoke(target);
        }
        else if (arguments != null) {
            if (coms.length == 2 && arguments.length >= 1) {
                Method method = clazz.getDeclaredMethod(coms[0], arguments[0].getClass());
                method.invoke(target, arguments[0]);
            }
            else if (coms.length == 3 && arguments.length >= 2) {
                Method method = clazz.getDeclaredMethod(coms[0], arguments[0].getClass(), arguments[1].getClass());
                method.invoke(target, arguments[0], arguments[1]);
            }
            else if (coms.length == 4 && arguments.length >= 3) {
                Method method = clazz.getDeclaredMethod(coms[0], arguments[0].getClass(), arguments[1].getClass(), arguments[2].getClass());
                method.invoke(target, arguments[0], arguments[1], arguments[2]);
            }
            else if (coms.length == 5 && arguments.length >= 4) {
                Method method = clazz.getDeclaredMethod(coms[0], arguments[0].getClass(), arguments[1].getClass(), arguments[2].getClass(), arguments[3].getClass());
                method.invoke(target, arguments[0], arguments[1], arguments[2], arguments[3]);
            }
            else if (coms.length == 6 && arguments.length >= 5) {
                Method method = clazz.getDeclaredMethod(coms[0], arguments[0].getClass(), arguments[1].getClass(), arguments[2].getClass(), arguments[3].getClass(), arguments[4].getClass());
                method.invoke(target, arguments[0], arguments[1], arguments[2], arguments[3], arguments[4]);
            }
            else if (coms.length == 7 && arguments.length >= 6) {
                Method method = clazz.getDeclaredMethod(coms[0], arguments[0].getClass(), arguments[1].getClass(), arguments[2].getClass(), arguments[3].getClass(), arguments[4].getClass(), arguments[5].getClass());
                method.invoke(target, arguments[0], arguments[1], arguments[2], arguments[3], arguments[4], arguments[5]);
            }
        }
    }

}
