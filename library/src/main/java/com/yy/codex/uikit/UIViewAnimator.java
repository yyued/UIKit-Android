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

    static public double decayDeceleration = 0.997;

    static public UIViewAnimation decay(final UIView animationView, final String animationKey, final double fromValue, final double velocity, @Nullable final Runnable completion) {
        final UIViewAnimation animation = new UIViewAnimation();
        final long startTime = System.currentTimeMillis();
        final double deceleration = decayDeceleration;
        final double finalValue = fromValue + (velocity / (1.0 - deceleration)) * (1 - Math.exp(-(1 - deceleration) * (999999999)));
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1);
        valueAnimator.setDuration(16);
        valueAnimator.setRepeatCount(9999999);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (animation.isCancelled()) {
                    valueAnimator.cancel();
                    return;
                }
                long now = System.currentTimeMillis();
                double value = fromValue + (velocity / (1.0 - deceleration)) * (1 - Math.exp(-(1 - deceleration) * (now - startTime)));
                animationView.animate(animationKey, (float) value);
                if (Math.abs(finalValue - value) < 0.1) {
                    valueAnimator.cancel();
                    animation.markFinished();
                    if (completion != null) {
                        completion.run();
                    }
                }
            }
        });
        valueAnimator.start();
        return animation;
    }

    static public UIViewAnimation decayToValue(final UIView animationView, final String animationKey, final double fromValue, final double toValue, @Nullable final Runnable completion) {
        final double deceleration = decayDeceleration;
        double velocity = (toValue / (1 - Math.exp(-(1 - deceleration) * (999999999))) - fromValue) * (1.0 - deceleration);
        return decay(animationView, animationKey, fromValue, velocity, completion);
    }

    static public class UIViewAnimationDecayBoundsOptions {
        double fromValue;
        double velocity;
        boolean allowBounds = true;
        boolean alwaysBounds = false;
        double topBounds;
        double bottomBounds;
        double viewBounds;
    }

    static public UIViewAnimation decayBounds(final UIView animationView, final String animationKey, final UIViewAnimationDecayBoundsOptions options, @Nullable final Runnable completion) {
        if (options.bottomBounds - options.topBounds < options.viewBounds) {
            if (options.allowBounds && options.alwaysBounds) {
                options.bottomBounds = options.topBounds;
            }
            else {
                return new UIViewAnimation();
            }
        }
        final double deceleration = decayDeceleration;
        final double finalValue = options.fromValue + (options.velocity / (1.0 - deceleration)) * (1 - Math.exp(-(1 - deceleration) * (999999999)));
        double backStartValue;
        final boolean[] backStarted = new boolean[1];
        final double backEndValue;
        if (finalValue < options.topBounds) {
            double tmpFinalValue = finalValue;
            if (options.fromValue < options.topBounds) {
                tmpFinalValue = options.topBounds + (options.velocity / (1.0 - deceleration)) * (1 - Math.exp(-(1 - deceleration) * (999999999)));
            }
            backStartValue = options.topBounds - (options.topBounds - tmpFinalValue) / 12.0;
            if (options.fromValue < options.topBounds) {
                backStartValue += (options.fromValue - options.topBounds);
            }
            backEndValue = options.topBounds;
            if (options.fromValue < options.topBounds && Math.abs(options.velocity) < 1.0) {
                backStarted[0] = true;
            }
        }
        else if (finalValue > options.bottomBounds) {
            double tmpFinalValue = finalValue;
            if (options.fromValue > options.bottomBounds) {
                tmpFinalValue = options.topBounds + (options.velocity / (1.0 - deceleration)) * (1 - Math.exp(-(1 - deceleration) * (999999999)));
            }
            backStartValue = (finalValue - options.bottomBounds) / 12.0 + options.bottomBounds;
            if (options.fromValue > options.bottomBounds) {
                backStartValue += (options.fromValue - options.bottomBounds);
            }
            backEndValue = options.bottomBounds;
            if (options.fromValue > options.bottomBounds && Math.abs(options.velocity) < 1.0) {
                backStarted[0] = true;
            }
        }
        else {
            return decay(animationView, animationKey, options.fromValue, options.velocity, completion);
        }
        final long startTime = System.currentTimeMillis();
        final UIViewAnimation animation = new UIViewAnimation();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1);
        valueAnimator.setDuration(16);
        valueAnimator.setRepeatCount(9999999);
        final double finalBackStartValue = backStartValue;
        final double finalBackEndValue = backEndValue;
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (animation.isCancelled()) {
                    valueAnimator.cancel();
                    return;
                }
                double decayValue = options.fromValue + (options.velocity / (1.0 - deceleration)) * (1 - Math.exp(-(1 - deceleration) * (System.currentTimeMillis() - startTime)));
                if (options.fromValue < options.topBounds) {
                    decayValue = options.topBounds + (options.velocity / (1.0 - deceleration)) * (1 - Math.exp(-(1 - deceleration) * (System.currentTimeMillis() - startTime)));
                }
                if (options.fromValue > options.bottomBounds) {
                    decayValue = options.bottomBounds + (options.velocity / (1.0 - deceleration)) * (1 - Math.exp(-(1 - deceleration) * (System.currentTimeMillis() - startTime)));
                }
                if (decayValue < options.topBounds) {
                    decayValue = options.topBounds - (options.topBounds - decayValue) / 3;
                    if (options.fromValue < options.topBounds) {
                        decayValue += (options.fromValue - options.topBounds);
                    }
                }
                else if (decayValue > options.bottomBounds) {
                    decayValue = (decayValue - options.bottomBounds) / 3 + options.bottomBounds;
                    if (options.fromValue < options.topBounds) {
                        decayValue += (options.fromValue - options.bottomBounds);
                    }
                }
                if (backStarted[0]) {
                    SpringSystem system = SpringSystem.create();
                    Spring spring = system.createSpring();
                    spring.setCurrentValue(finalBackStartValue);
                    SpringConfig config = new SpringConfig(120.0, 20.0);
                    spring.setSpringConfig(config);
                    spring.addListener(new SpringListener() {
                        @Override
                        public void onSpringUpdate(Spring spring) {
                            if (animation.isCancelled()) {
                                return;
                            }
                            float currentValue = (float)spring.getCurrentValue();
                            animationView.animate(animationKey, currentValue);
                        }

                        @Override
                        public void onSpringAtRest(Spring spring) {
                            if (animation.isCancelled()) {
                                return;
                            }
                            if (completion != null) {
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
                    spring.setEndValue(finalBackEndValue);
                    valueAnimator.cancel();
                }
                else if (!backStarted[0]) {
                    if (finalValue < options.topBounds && decayValue < finalBackStartValue && !backStarted[0]) {
                        backStarted[0] = true;
                    }
                    else if (finalValue > options.bottomBounds && decayValue > finalBackStartValue && !backStarted[0]) {
                        backStarted[0] = true;
                    }
                }
                else if (!options.allowBounds) {
                    if (decayValue < options.topBounds) {
                        valueAnimator.cancel();
                    }
                    else if (decayValue > options.bottomBounds) {
                        valueAnimator.cancel();
                    }
                }
                animationView.animate(animationKey, (float) decayValue);
            }
        });
        valueAnimator.start();
        return animation;
    }

}
