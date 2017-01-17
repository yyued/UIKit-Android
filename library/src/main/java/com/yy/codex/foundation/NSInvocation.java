package com.yy.codex.foundation;

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

    public void invoke(Object[] arguments) {
        Object target = getTarget();
        if (target != null) {
            invoke(target, arguments);
        }
    }

    public void invoke(@Nullable Object target, @Nullable Object[] arguments) {
        if (target == null || mSelector == null) {
            return;
        }
        Class clazz = target.getClass();
        String[] components = mSelector.endsWith(":") ? (mSelector+"_").split(":") : mSelector.split(":");
        boolean found = false;
        while (!found && clazz != null) {
            try {
                if (components.length == 1) {
                    Method method = clazz.getDeclaredMethod(mSelector);
                    boolean accessible = true;
                    if (!method.isAccessible()) {
                        accessible = false;
                        method.setAccessible(true);
                    }
                    method.invoke(target);
                    if (!accessible) {
                        method.setAccessible(false);
                    }
                }
                else if (arguments != null) {
                    if (components.length == 2 && arguments.length >= 1) {
                        Method method = clazz.getDeclaredMethod(components[0], arguments[0].getClass());
                        boolean accessible = true;
                        if (!method.isAccessible()) {
                            accessible = false;
                            method.setAccessible(true);
                        }
                        method.invoke(target, arguments[0]);
                        if (!accessible) {
                            method.setAccessible(false);
                        }
                    }
                    else if (components.length == 3 && arguments.length >= 2) {
                        Method method = clazz.getDeclaredMethod(components[0], arguments[0].getClass(), arguments[1].getClass());
                        boolean accessible = true;
                        if (!method.isAccessible()) {
                            accessible = false;
                            method.setAccessible(true);
                        }
                        method.invoke(target, arguments[0], arguments[1]);
                        if (!accessible) {
                            method.setAccessible(false);
                        }
                    }
                    else if (components.length == 4 && arguments.length >= 3) {
                        Method method = clazz.getDeclaredMethod(components[0], arguments[0].getClass(), arguments[1].getClass(), arguments[2].getClass());
                        boolean accessible = true;
                        if (!method.isAccessible()) {
                            accessible = false;
                            method.setAccessible(true);
                        }
                        method.invoke(target, arguments[0], arguments[1], arguments[2]);
                        if (!accessible) {
                            method.setAccessible(false);
                        }
                    }
                    else if (components.length == 5 && arguments.length >= 4) {
                        Method method = clazz.getDeclaredMethod(components[0], arguments[0].getClass(), arguments[1].getClass(), arguments[2].getClass(), arguments[3].getClass());
                        boolean accessible = true;
                        if (!method.isAccessible()) {
                            accessible = false;
                            method.setAccessible(true);
                        }
                        method.invoke(target, arguments[0], arguments[1], arguments[2], arguments[3]);
                        if (!accessible) {
                            method.setAccessible(false);
                        }
                    }
                    else if (components.length == 6 && arguments.length >= 5) {
                        Method method = clazz.getDeclaredMethod(components[0], arguments[0].getClass(), arguments[1].getClass(), arguments[2].getClass(), arguments[3].getClass(), arguments[4].getClass());
                        boolean accessible = true;
                        if (!method.isAccessible()) {
                            accessible = false;
                            method.setAccessible(true);
                        }
                        method.invoke(target, arguments[0], arguments[1], arguments[2], arguments[3], arguments[4]);
                        if (!accessible) {
                            method.setAccessible(false);
                        }
                    }
                    else if (components.length == 7 && arguments.length >= 6) {
                        Method method = clazz.getDeclaredMethod(components[0], arguments[0].getClass(), arguments[1].getClass(), arguments[2].getClass(), arguments[3].getClass(), arguments[4].getClass(), arguments[5].getClass());
                        boolean accessible = true;
                        if (!method.isAccessible()) {
                            accessible = false;
                            method.setAccessible(true);
                        }
                        method.invoke(target, arguments[0], arguments[1], arguments[2], arguments[3], arguments[4], arguments[5]);
                        if (!accessible) {
                            method.setAccessible(false);
                        }
                    }
                }
                found = true;
            }
            catch (Exception e) {
                if (e instanceof NoSuchMethodException) {
                    clazz = clazz.getSuperclass();
                }
            }
        }

    }

}
