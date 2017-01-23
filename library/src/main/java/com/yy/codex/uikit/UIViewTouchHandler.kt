package com.yy.codex.uikit

import android.view.MotionEvent

import java.lang.ref.WeakReference

/**
 * Created by saiakirahui on 2017/1/14.
 */

class UIViewTouchHandler internal constructor(private val view: UIView) {

    private var mHitTestedView: UIView? = null
    private var mEventID: Long = 0
    private var mHash: DoubleArray? = null

    internal fun onTouchEvent(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val touchPoint = CGPoint(event.x / UIScreen.mainScreen.scale(), event.y / UIScreen.mainScreen.scale())
            mHitTestedView = view.hitTest(touchPoint, event)
            mEventID = System.currentTimeMillis()
        }
        mHitTestedView?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mHash = null
                    it.touchesBegan(requestTouches(event), UIEvent())
                }
                MotionEvent.ACTION_UP -> it.touchesEnded(requestTouches(event), UIEvent())
                MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_POINTER_DOWN -> {
                    val cHash = requestHash(event)
                    if (mHash != null && isHashSame(mHash!!, cHash)) {
                        return
                    }
                    mHash = cHash
                    it.touchesMoved(requestTouches(event), UIEvent())
                }
            }
        }
    }

    internal fun requestTouches(event: MotionEvent): List<UITouch> {
        val hitTestedView = mHitTestedView ?: return emptyList()
        val pointerCount = event.pointerCount
        val touches: MutableList<UITouch> = mutableListOf()
        val offsetX = (event.rawX - event.getX(0)) / UIScreen.mainScreen.scale()
        val offsetY = (event.rawY - event.getY(0)) / UIScreen.mainScreen.scale()
        for (i in 0..pointerCount - 1) {
            val x = event.getX(i) / UIScreen.mainScreen.scale()
            val y = event.getY(i) / UIScreen.mainScreen.scale()
            val convertedPoint = view.convertPoint(CGPoint(x, y), mHitTestedView!!)
            val rawPoint = CGPoint(x + offsetX, y + offsetY)
            touches.add(UITouch(hitTestedView, convertedPoint, rawPoint, requestPhase(event), mEventID))
        }
        return touches.toList()
    }

    internal fun requestPhase(event: MotionEvent): UITouch.Phase {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return UITouch.Phase.Began
            MotionEvent.ACTION_UP -> return UITouch.Phase.Ended
            MotionEvent.ACTION_POINTER_UP -> return UITouch.Phase.Ended
            MotionEvent.ACTION_MOVE -> return UITouch.Phase.Moved
            MotionEvent.ACTION_POINTER_DOWN -> return UITouch.Phase.Began
            else -> return UITouch.Phase.Ended
        }
    }

    internal fun requestHash(event: MotionEvent): DoubleArray {
        val count = event.pointerCount
        val hash = DoubleArray(count * 2)
        for (i in 0..count - 1) {
            hash[i] = event.getX(i).toDouble()
            hash[i + 1] = event.getY(i).toDouble()
        }
        return hash
    }

    internal fun isHashSame(hashA: DoubleArray, hashB: DoubleArray): Boolean {
        if (hashA.size == hashB.size) {
            return hashA.indices.none { java.lang.Double.compare(hashA[it], hashB[it]) != 0 }
        }
        return false
    }

}
