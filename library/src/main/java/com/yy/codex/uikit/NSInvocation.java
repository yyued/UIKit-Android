package com.yy.codex.uikit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by cuiminghui on 2017/1/10.
 */

public class NSInvocation {

    private Object target;
    private String selector;

    public NSInvocation(Object target, String selector) {
        this.target = target;
        this.selector = selector;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public void invoke(Object[] arguments) throws Exception {
        invoke(target, arguments);
    }

    public void invoke(Object target, Object[] arguments) throws Exception {
        if (target == null || selector == null) {
            throw new Exception("Null target or null selector.");
        }
        Class clazz = target.getClass();
        String[] coms = selector.endsWith(":") ? (selector+"_").split(":") : selector.split(":");
        if (coms.length == 1) {
            Method method = clazz.getDeclaredMethod(selector);
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
