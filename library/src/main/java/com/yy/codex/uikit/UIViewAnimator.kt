package com.yy.codex.uikit

import android.animation.Animator
import android.animation.ValueAnimator

import com.facebook.rebound.Spring
import com.facebook.rebound.SpringConfig
import com.facebook.rebound.SpringListener
import com.facebook.rebound.SpringSystem

import java.util.HashMap

/**
 * Created by cuiminghui on 2017/1/16.
 */

object UIViewAnimator {

    private var sAnimationState: HashMap<UIView, HashMap<String, UIViewAnimatorState<*>>> = hashMapOf()

    fun addAnimationState(view: UIView, aKey: String, originValue: Double, finalValue: Double) {
        if (sAnimationState == null) {
            return
        }
        if (originValue == finalValue) {
            return
        }
        if (sAnimationState[view] == null) {
            sAnimationState.put(view, HashMap<String, UIViewAnimatorState<*>>())
        }
        val log = UIViewAnimatorState<Number>()
        log.valueType = 1
        log.originValue = originValue
        log.finalValue = finalValue
        sAnimationState[view]?.let {
            it.put(aKey, log)
        }
    }

    private fun resetAnimationState() {
        sAnimationState = hashMapOf()
    }

    fun linear(animations: Runnable): UIViewAnimation {
        return linear(0.25, animations, null)
    }

    fun linear(duration: Double, animations: Runnable, completion: Runnable?): UIViewAnimation {
        val animation = UIViewAnimation()
        resetAnimationState()
        animations.run()
        val aniCount = intArrayOf(0)
        for ((key, value) in sAnimationState) {
            for ((key1, log) in value) {
                if (log.valueType == 1) {
                    aniCount[0]++
                    key.animate(key1, (log.originValue as Double).toFloat())
                    val animator = ValueAnimator.ofFloat((log.originValue as Double).toFloat(), (log.finalValue as Double).toFloat())
                    animator.duration = (duration * 1000).toLong()
                    animator.addUpdateListener(ValueAnimator.AnimatorUpdateListener { valueAnimator ->
                        if (animation.isCancelled) {
                            return@AnimatorUpdateListener
                        }
                        val currentValue = valueAnimator.animatedValue as Float
                        key.animate(key1, currentValue)
                    })
                    animator.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {

                        }

                        override fun onAnimationEnd(animator: Animator) {
                            if (animation.isCancelled) {
                                return
                            }
                            aniCount[0]--
                            if (aniCount[0] <= 0) {
                                animation.markFinished()
                            }
                            if (aniCount[0] <= 0 && completion != null) {
                                completion.run()
                            }
                        }

                        override fun onAnimationCancel(animator: Animator) {

                        }

                        override fun onAnimationRepeat(animator: Animator) {

                        }
                    })
                    animator.setTarget(key)
                    animator.start()
                }
            }
        }
        if (aniCount[0] <= 0) {
            animation.markFinished()
            completion?.run()
        }
        sAnimationState = hashMapOf()
        return animation
    }

    @JvmOverloads fun spring(animations: Runnable, completion: Runnable? = null): UIViewAnimation {
        val animation = UIViewAnimation()
        resetAnimationState()
        animations.run()
        val aniCount = intArrayOf(0)
        val system = SpringSystem.create()
        for ((key, value) in sAnimationState) {
            for ((key1, log) in value) {
                if (log.valueType == 1) {
                    aniCount[0]++
                    key.animate(key1, (log.originValue as Double).toFloat())
                    val spring = system.createSpring()
                    spring.currentValue = (log.originValue as Double).toFloat().toDouble()
                    spring.addListener(object : SpringListener {
                        override fun onSpringUpdate(spring: Spring) {
                            if (animation.isCancelled) {
                                return
                            }
                            val currentValue = spring.currentValue.toFloat()
                            key.animate(key1, currentValue)
                        }

                        override fun onSpringAtRest(spring: Spring) {
                            if (animation.isCancelled) {
                                return
                            }
                            val currentValue = spring.currentValue.toFloat()
                            key.animate(key1, currentValue)
                            aniCount[0]--
                            if (aniCount[0] <= 0 && completion != null) {
                                completion.run()
                            }
                        }

                        override fun onSpringActivate(spring: Spring) {}

                        override fun onSpringEndStateChange(spring: Spring) {}
                    })
                    spring.endValue = (log.finalValue as Double).toFloat().toDouble()
                }
            }
        }
        if (aniCount[0] <= 0) {
            animation.markFinished()
            completion?.run()
        }
        sAnimationState = hashMapOf()
        return animation
    }

    fun springWithOptions(tension: Double, friction: Double, velocity: Double, animations: Runnable, completion: Runnable?): UIViewAnimation {
        val animation = UIViewAnimation()
        resetAnimationState()
        animations.run()
        val aniCount = intArrayOf(0)
        val system = SpringSystem.create()
        for ((key, value) in sAnimationState) {
            for ((key1, log) in value) {
                if (log.valueType == 1) {
                    aniCount[0]++
                    key.animate(key1, (log.originValue as Double).toFloat())
                    val spring = system.createSpring()
                    spring.currentValue = (log.originValue as Double).toFloat().toDouble()
                    val config = SpringConfig(tension, friction)
                    spring.springConfig = config
                    spring.velocity = velocity
                    spring.addListener(object : SpringListener {
                        override fun onSpringUpdate(spring: Spring) {
                            if (animation.isCancelled) {
                                return
                            }
                            val currentValue = spring.currentValue.toFloat()
                            key.animate(key1, currentValue)
                        }

                        override fun onSpringAtRest(spring: Spring) {
                            if (animation.isCancelled) {
                                return
                            }
                            val currentValue = spring.currentValue.toFloat()
                            key.animate(key1, currentValue)
                            aniCount[0]--
                            if (aniCount[0] <= 0) {
                                animation.markFinished()
                            }
                            if (aniCount[0] <= 0 && completion != null) {
                                completion.run()
                            }
                        }

                        override fun onSpringActivate(spring: Spring) {}

                        override fun onSpringEndStateChange(spring: Spring) {}
                    })
                    spring.endValue = (log.finalValue as Double).toFloat().toDouble()
                }
            }
        }
        if (aniCount[0] <= 0) {
            animation.markFinished()
            completion?.run()
        }
        sAnimationState = hashMapOf()
        return animation
    }

    var decayDeceleration = 0.997

    fun decay(animationView: UIView, animationKey: String, fromValue: Double, velocity: Double, completion: Runnable?): UIViewAnimation {
        val animation = UIViewAnimation()
        val startTime = System.currentTimeMillis()
        val deceleration = decayDeceleration
        val finalValue = fromValue + velocity / (1.0 - deceleration) * (1 - Math.exp(-(1 - deceleration) * 999999999))
        val valueAnimator = ValueAnimator.ofInt(0, 1)
        valueAnimator.duration = 16
        valueAnimator.repeatCount = 9999999
        valueAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener { valueAnimator ->
            if (animation.isCancelled) {
                valueAnimator.cancel()
                return@AnimatorUpdateListener
            }
            val now = System.currentTimeMillis()
            val value = fromValue + velocity / (1.0 - deceleration) * (1 - Math.exp(-(1 - deceleration) * (now - startTime)))
            animationView.animate(animationKey, value.toFloat())
            if (Math.abs(finalValue - value) < 0.1) {
                valueAnimator.cancel()
                animation.markFinished()
                completion?.run()
            }
        })
        valueAnimator.start()
        return animation
    }

    fun decayToValue(animationView: UIView, animationKey: String, fromValue: Double, toValue: Double, completion: Runnable?): UIViewAnimation {
        val deceleration = decayDeceleration
        val velocity = (toValue / (1 - Math.exp(-(1 - deceleration) * 999999999)) - fromValue) * (1.0 - deceleration)
        return decay(animationView, animationKey, fromValue, velocity, completion)
    }

    class UIViewAnimationDecayBoundsOptions {
        var fromValue: Double = 0.toDouble()
        var velocity: Double = 0.toDouble()
        var allowBounds = true
        var alwaysBounds = false
        var topBounds: Double = 0.toDouble()
        var bottomBounds: Double = 0.toDouble()
        var viewBounds: Double = 0.toDouble()
    }

    fun decayBounds(animationView: UIView, animationKey: String, options: UIViewAnimationDecayBoundsOptions, completion: Runnable?): UIViewAnimation {
        if (!options.allowBounds && (options.fromValue <= options.topBounds || options.fromValue >= options.bottomBounds)) {
            return UIViewAnimation()
        }
        if (options.bottomBounds - options.topBounds < options.viewBounds) {
            if (options.allowBounds && options.alwaysBounds) {
                options.bottomBounds = options.topBounds
            } else {
                return UIViewAnimation()
            }
        }
        val deceleration = decayDeceleration
        val finalValue = options.fromValue + options.velocity / (1.0 - deceleration) * (1 - Math.exp(-(1 - deceleration) * 999999999))
        var backStartValue: Double
        val backStarted = BooleanArray(1)
        val backEndValue: Double
        if (finalValue < options.topBounds) {
            var tmpFinalValue = finalValue
            if (options.fromValue < options.topBounds) {
                tmpFinalValue = options.topBounds + options.velocity / (1.0 - deceleration) * (1 - Math.exp(-(1 - deceleration) * 999999999))
            }
            backStartValue = options.topBounds - (options.topBounds - tmpFinalValue) / 12.0
            if (options.fromValue < options.topBounds) {
                backStartValue += options.fromValue - options.topBounds
            }
            backEndValue = options.topBounds
            if (options.fromValue < options.topBounds && Math.abs(options.velocity) < 1.0) {
                backStarted[0] = true
            }
        } else if (finalValue > options.bottomBounds) {
            var tmpFinalValue = finalValue
            if (options.fromValue > options.bottomBounds) {
                tmpFinalValue = options.bottomBounds + options.velocity / (1.0 - deceleration) * (1 - Math.exp(-(1 - deceleration) * 999999999))
            }
            backStartValue = (tmpFinalValue - options.bottomBounds) / 12.0 + options.bottomBounds
            if (options.fromValue > options.bottomBounds) {
                backStartValue += options.fromValue - options.bottomBounds
            }
            backEndValue = options.bottomBounds
            if (options.fromValue > options.bottomBounds && Math.abs(options.velocity) < 1.0) {
                backStarted[0] = true
            }
        } else {
            return decay(animationView, animationKey, options.fromValue, options.velocity, completion)
        }
        val startTime = System.currentTimeMillis()
        val animation = UIViewAnimation()
        val valueAnimator = ValueAnimator.ofInt(0, 1)
        valueAnimator.duration = 16
        valueAnimator.repeatCount = 9999999
        val finalBackStartValue = backStartValue
        val finalBackEndValue = backEndValue
        valueAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener { valueAnimator ->
            if (animation.isCancelled) {
                valueAnimator.cancel()
                return@AnimatorUpdateListener
            }
            var decayValue = options.fromValue + options.velocity / (1.0 - deceleration) * (1 - Math.exp(-(1 - deceleration) * (System.currentTimeMillis() - startTime)))
            if (options.fromValue < options.topBounds) {
                decayValue = options.topBounds + options.velocity / (1.0 - deceleration) * (1 - Math.exp(-(1 - deceleration) * (System.currentTimeMillis() - startTime)))
            }
            if (options.fromValue > options.bottomBounds) {
                decayValue = options.bottomBounds + options.velocity / (1.0 - deceleration) * (1 - Math.exp(-(1 - deceleration) * (System.currentTimeMillis() - startTime)))
            }
            if (decayValue < options.topBounds) {
                decayValue = options.topBounds - (options.topBounds - decayValue) / 3
                if (options.fromValue < options.topBounds) {
                    decayValue -= options.topBounds - options.fromValue
                }
            } else if (decayValue > options.bottomBounds) {
                decayValue = (decayValue - options.bottomBounds) / 3 + options.bottomBounds
                if (options.fromValue > options.bottomBounds) {
                    decayValue += options.fromValue - options.bottomBounds
                }
            }
            if (!options.allowBounds) {
                if (decayValue <= options.topBounds) {
                    animationView.animate(animationKey, options.topBounds.toFloat())
                    valueAnimator.cancel()
                    animation.markFinished()
                    return@AnimatorUpdateListener
                } else if (decayValue >= options.bottomBounds) {
                    animationView.animate(animationKey, options.bottomBounds.toFloat())
                    valueAnimator.cancel()
                    animation.markFinished()
                    return@AnimatorUpdateListener
                }
            } else if (backStarted[0]) {
                val system = SpringSystem.create()
                val spring = system.createSpring()
                spring.currentValue = finalBackStartValue
                val config = SpringConfig(120.0, 20.0)
                spring.springConfig = config
                spring.addListener(object : SpringListener {
                    override fun onSpringUpdate(spring: Spring) {
                        if (animation.isCancelled) {
                            return
                        }
                        val currentValue = spring.currentValue.toFloat()
                        animationView.animate(animationKey, currentValue)
                    }

                    override fun onSpringAtRest(spring: Spring) {
                        if (animation.isCancelled) {
                            return
                        }
                        completion?.run()
                    }

                    override fun onSpringActivate(spring: Spring) {

                    }

                    override fun onSpringEndStateChange(spring: Spring) {

                    }
                })
                spring.endValue = finalBackEndValue
                valueAnimator.cancel()
            } else if (!backStarted[0]) {
                if (finalValue < options.topBounds && decayValue < finalBackStartValue && !backStarted[0]) {
                    backStarted[0] = true
                } else if (finalValue > options.bottomBounds && decayValue > finalBackStartValue && !backStarted[0]) {
                    backStarted[0] = true
                }
            }
            animationView.animate(animationKey, decayValue.toFloat())
        })
        valueAnimator.start()
        return animation
    }

}
