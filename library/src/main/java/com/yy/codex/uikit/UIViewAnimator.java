package com.yy.codex.uikit;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuiminghui on 2017/1/16.
 */

public class UIViewAnimator {

    @Nullable
    static private HashMap<UIView, HashMap<String, UIViewAnimatorState>> sAnimationState = null;

    static void addAnimationState(@NonNull UIView view, @NonNull String aKey, double originValue, double finalValue) {
        if (sAnimationState == null) {
            return;
        }
        if (originValue == finalValue) {
            return;
        }
        if (sAnimationState.get(view) == null) {
            sAnimationState.put(view, new HashMap<String, UIViewAnimatorState>());
        }
        UIViewAnimatorState<Number> log = new UIViewAnimatorState<>();
        log.valueType = 1;
        log.originValue = originValue;
        log.finalValue = finalValue;
        sAnimationState.get(view).put(aKey, log);
    }

    static private void resetAnimationState() {
        sAnimationState = new HashMap<>();
    }

    static public UIViewAnimation linear(@NonNull Runnable animations) {
        return linear(0.25, animations, null);
    }

    static public UIViewAnimation linear(double duration, @NonNull Runnable animations, @Nullable final Runnable completion) {
        final UIViewAnimation animation = new UIViewAnimation();
        resetAnimationState();
        animations.run();
        final int[] aniCount = {0};
        for (final Map.Entry<UIView, HashMap<String, UIViewAnimatorState>> viewProps: sAnimationState.entrySet()) {
            for (final Map.Entry<String, UIViewAnimatorState> animateProp: viewProps.getValue().entrySet()) {
                UIViewAnimatorState log = animateProp.getValue();
                if (log.valueType == 1) {
                    aniCount[0]++;
                    viewProps.getKey().animate(animateProp.getKey(), (float)((double)log.originValue));
                    ValueAnimator animator = ValueAnimator.ofFloat((float)((double)log.originValue), (float)((double)log.finalValue));
                    animator.setDuration((long) (duration * 1000));
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                            if (animation.isCancelled()) {
                                return;
                            }
                            float currentValue = (float)valueAnimator.getAnimatedValue();
                            viewProps.getKey().animate(animateProp.getKey(), currentValue);
                        }
                    });
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (animation.isCancelled()) {
                                return;
                            }
                            aniCount[0]--;
                            if (aniCount[0] <= 0) {
                                animation.markFinished();
                            }
                            if (aniCount[0] <= 0 && completion != null) {
                                completion.run();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    animator.setTarget(viewProps.getKey());
                    animator.start();
                }
            }
        }
        if (aniCount[0] <= 0) {
            animation.markFinished();
            if (completion != null) {
                completion.run();
            }
        }
        sAnimationState = null;
        return animation;
    }

    static public UIViewAnimation spring(@NonNull Runnable animations) {
        return spring(animations, null);
    }

    static public UIViewAnimation spring(@NonNull Runnable animations, @Nullable final Runnable completion) {
        final UIViewAnimation animation = new UIViewAnimation();
        resetAnimationState();
        animations.run();
        final int[] aniCount = {0};
        SpringSystem system = SpringSystem.create();
        for (final Map.Entry<UIView, HashMap<String, UIViewAnimatorState>> viewProps: sAnimationState.entrySet()) {
            for (final Map.Entry<String, UIViewAnimatorState> animateProp: viewProps.getValue().entrySet()) {
                final UIViewAnimatorState log = animateProp.getValue();
                if (log.valueType == 1) {
                    aniCount[0]++;
                    viewProps.getKey().animate(animateProp.getKey(), (float)((double)log.originValue));
                    Spring spring = system.createSpring();
                    spring.setCurrentValue((float)((double)log.originValue));
                    spring.addListener(new SpringListener() {
                        @Override
                        public void onSpringUpdate(@NonNull Spring spring) {
                            if (animation.isCancelled()) {
                                return;
                            }
                            float currentValue = (float)spring.getCurrentValue();
                            viewProps.getKey().animate(animateProp.getKey(), currentValue);
                        }

                        @Override
                        public void onSpringAtRest(@NonNull Spring spring) {
                            if (animation.isCancelled()) {
                                return;
                            }
                            float currentValue = (float)spring.getCurrentValue();
                            viewProps.getKey().animate(animateProp.getKey(), currentValue);
                            aniCount[0]--;
                            if (aniCount[0] <= 0 && completion != null) {
                                completion.run();
                            }
                        }
                        @Override
                        public void onSpringActivate(Spring spring) {
                        }

                        @Override
                        public void onSpringEndStateChange(Spring spring) {
                        }
                    });
                    spring.setEndValue((float)((double)log.finalValue));
                }
            }
        }
        if (aniCount[0] <= 0) {
            animation.markFinished();
            if (completion != null) {
                completion.run();
            }
        }
        sAnimationState = null;
        return animation;
    }

    static public UIViewAnimation springWithOptions(double tension, double friction, double velocity, @NonNull Runnable animations, @Nullable final Runnable completion) {
        final UIViewAnimation animation = new UIViewAnimation();
        resetAnimationState();
        animations.run();
        final int[] aniCount = {0};
        SpringSystem system = SpringSystem.create();
        for (final Map.Entry<UIView, HashMap<String, UIViewAnimatorState>> viewProps: sAnimationState.entrySet()) {
            for (final Map.Entry<String, UIViewAnimatorState> animateProp: viewProps.getValue().entrySet()) {
                UIViewAnimatorState log = animateProp.getValue();
                if (log.valueType == 1) {
                    aniCount[0]++;
                    viewProps.getKey().animate(animateProp.getKey(), (float)((double)log.originValue));
                    Spring spring = system.createSpring();
                    spring.setCurrentValue((float)((double)log.originValue));
                    SpringConfig config = new SpringConfig(tension, friction);
                    spring.setSpringConfig(config);
                    spring.setVelocity(velocity);
                    spring.addListener(new SpringListener() {
                        @Override
                        public void onSpringUpdate(@NonNull Spring spring) {
                            if (animation.isCancelled()) {
                                return;
                            }
                            float currentValue = (float)spring.getCurrentValue();
                            viewProps.getKey().animate(animateProp.getKey(), currentValue);
                        }

                        @Override
                        public void onSpringAtRest(@NonNull Spring spring) {
                            if (animation.isCancelled()) {
                                return;
                            }
                            float currentValue = (float)spring.getCurrentValue();
                            viewProps.getKey().animate(animateProp.getKey(), currentValue);
                            aniCount[0]--;
                            if (aniCount[0] <= 0) {
                                animation.markFinished();
                            }
                            if (aniCount[0] <= 0 && completion != null) {
                                completion.run();
                            }
                        }

                        @Override
                        public void onSpringActivate(Spring spring) {
                        }

                        @Override
                        public void onSpringEndStateChange(Spring spring) {
                        }
                    });
                    spring.setEndValue((float)((double)log.finalValue));
                }
            }
        }
        if (aniCount[0] <= 0) {
            animation.markFinished();
            if (completion != null) {
                completion.run();
            }
        }
        sAnimationState = null;
        return animation;
    }

}
