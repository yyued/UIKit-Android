package com.yy.codex.uikit

import com.yy.codex.foundation.lets

/**
 * Created by cuiminghui on 2017/3/7.
 */

private val FINGER_VELOCITY = 300.0

internal fun UIScrollView._initScroller() {
    _windowSizePoint = CGPoint(0.0, 0.0)
    _panGestureRecognizer = UIPanGestureRecognizer(this, "handlePan:")
    _panGestureRecognizer?.let {
        addGestureRecognizer(it)
    }
}

internal fun UIScrollView._scrollerTouchBegan() {
    if (!scrollEnabled) {
        return
    }
    val nowDecelerating = decelerating
    _bounceAnimationCancelled = decelerating && _checkOutOfBounds()
    _currentAnimationY?.let(UIViewAnimation::cancel)
    _currentAnimationY = null
    _currentAnimationX?.let(UIViewAnimation::cancel)
    _currentAnimationX = null
    tracking = true
    decelerating = false
    if (nowDecelerating) {
        _showScrollIndicator()
    }
}

internal fun UIScrollView._scrollerTouchEnded() {
    if (!scrollEnabled) {
        return
    }
    if (_bounceAnimationCancelled && _checkOutOfBounds()) {
        _setContentOffsetWithSpring(_requestBoundsPoint(contentOffset))
    }
    if (!dragging) {
        _hideScrollIndicator()
    }
}

internal fun UIScrollView._scrollerHandlePan(panGestureRecognizer: UIPanGestureRecognizer) {
    if (!scrollEnabled) {
        return
    }
    val originY = -panGestureRecognizer.translation().y
    val originX = -panGestureRecognizer.translation().x
    if (!dragging) {
        /* Began */
        dragging = true
        _trackingPoint = CGPoint(originX, originY)
        panGestureRecognizer.setTranslation(contentOffset)
        delegate?.let { it.scrollViewWillBeginDragging(this) }
        _showScrollIndicator()
    }
    else if (dragging && panGestureRecognizer.state === UIGestureRecognizerState.Changed) {
        /* Move */
        val offset = _computeMovePoint(CGPoint(originX, originY), pagingEnabled)
        lets(_trackingPoint, _windowSizePoint) { trackingPoint, windowSizePoint ->
            _verticalMoveDistance = originY + Math.abs(trackingPoint.y) - windowSizePoint.y
            _horizontalMoveDistance = originX + Math.abs(trackingPoint.x) - windowSizePoint.x
        }
        setContentOffset(offset)
    }
    else if (panGestureRecognizer.state === UIGestureRecognizerState.Ended) {
        /* Ended */
        dragging = false
        tracking = false
        decelerating = true
        delegate?.let {
            it.scrollViewDidEndDragging(this, false)
        }
        val velocity = panGestureRecognizer.velocity()
        if (pagingEnabled) {
            _computeScrollPagingPoint(velocity)
            _windowSizePoint?.let {
                _setContentOffsetWithSpring(it)
            }
        } else {
            _currentAnimationX?.let { it.cancel() }
            _currentAnimationY?.let { it.cancel() }
            val xOptions = UIViewAnimator.UIViewAnimationDecayBoundsOptions()
            xOptions.allowBounds = bounces
            xOptions.alwaysBounds = alwaysBounceHorizontal
            xOptions.fromValue = contentOffset.x
            xOptions.velocity = -velocity.x / 1000.0
            xOptions.topBounds = 0.0
            xOptions.bottomBounds = contentSize.width - frame.size.width
            xOptions.viewBounds = frame.size.width
            _currentAnimationX = UIViewAnimator.decayBounds(this, "contentOffset.x", xOptions, null)
            val yOptions = UIViewAnimator.UIViewAnimationDecayBoundsOptions()
            yOptions.allowBounds = bounces
            yOptions.alwaysBounds = alwaysBounceVertical
            yOptions.fromValue = contentOffset.y
            yOptions.velocity = -velocity.y / 1000.0
            yOptions.topBounds = 0.0
            yOptions.bottomBounds = contentSize.height + contentInset.bottom - frame.size.height
            yOptions.viewBounds = frame.size.height
            _currentAnimationY = UIViewAnimator.decayBounds(this, "contentOffset.y", yOptions, Runnable {
                decelerating = false
                _hideScrollIndicator()
            })
        }
        _horizontalMoveDistance = 0.0
        _verticalMoveDistance = 0.0
    }
}

internal fun UIScrollView._setContentOffset(contentOffset: CGPoint, animated: Boolean = false) {
    val oldValue = this.contentOffset
    val self = this
    this.contentOffset = contentOffset
    if (animated) {
        _currentAnimationX?.let { it.cancel() }
        _currentAnimationY?.let { it.cancel() }
        _currentAnimationX = UIViewAnimator.linear(0.25, Runnable {
            UIViewAnimator.addAnimationState(self, "contentOffset.x", oldValue.x, this.contentOffset.x)
            UIViewAnimator.addAnimationState(self, "contentOffset.y", oldValue.y, this.contentOffset.y)
        }, null)
        _currentAnimationY = _currentAnimationX
    } else {
        scrollTo((this.contentOffset.x * UIScreen.mainScreen.scale()).toInt(), (this.contentOffset.y * UIScreen.mainScreen.scale() - contentInset.top).toInt())
        delegate?.let {
            it.scrollViewDidScroll(this)
        }
        UIViewAnimator.addAnimationState(self, "contentOffset.x", oldValue.x, this.contentOffset.x)
        UIViewAnimator.addAnimationState(self, "contentOffset.y", oldValue.y, this.contentOffset.y)
        _updateScrollIndicator()
    }
}

internal fun UIScrollView._scrollToVisible(visibleRect: CGRect, animated: Boolean) {
    val leftVisible = visibleRect.x < contentOffset.x + frame.width
    val topVisible = visibleRect.y < contentOffset.y + frame.height
    var computedOffsetX = contentOffset.x
    var computedOffsetY = contentOffset.y
    if (!leftVisible) {
        computedOffsetX = if (visibleRect.width < frame.width) visibleRect.x + visibleRect.width - frame.width else visibleRect.x
    }
    if (!topVisible) {
        computedOffsetY = if (visibleRect.height < frame.height) visibleRect.y + visibleRect.height - frame.height else visibleRect.y
    }
    if (!leftVisible || !topVisible) {
        setContentOffset(CGPoint(computedOffsetX, computedOffsetY), animated)
    }
}

internal fun UIScrollView._setContentOffsetWithSpring(contentOffset: CGPoint) {
    _currentAnimationX?.let { it.cancel() }
    _currentAnimationY?.let { it.cancel() }
    _currentAnimationX = UIViewAnimator.springWithOptions(120.0, 20.0, Runnable { setContentOffset(contentOffset, false) }, null)
    _currentAnimationY = _currentAnimationX
}

private fun UIScrollView._checkOutOfBounds(): Boolean {
    if (contentOffset.x < 0.0 || contentOffset.y < 0.0) {
        return true
    }
    else if (contentOffset.x > contentSize.width - frame.size.width && contentSize.width > 0 ||
             contentOffset.y > contentSize.height - frame.size.height && contentSize.height > 0) {
        return true
    }
    return false
}

private fun UIScrollView._computeScrollPagingPoint(velocity: CGPoint) {
    var verticalPageCurrentIndex = Math.round((_windowSizePoint?.y ?: 0.0) / frame.size.height).toInt()
    var horizontalPageCurrentIndex = Math.round((_windowSizePoint?.x ?: 0.0) / frame.size.width).toInt()
    var moveOffsetX = horizontalPageCurrentIndex * frame.size.width
    var moveOffsetY = verticalPageCurrentIndex * frame.size.height
    if (Math.abs(_horizontalMoveDistance) > _fingerHorizontalMoveDistance || Math.abs(_verticalMoveDistance) > _fingerVerticalMoveDistance || Math.abs(velocity.x) > FINGER_VELOCITY || Math.abs(velocity.y) > FINGER_VELOCITY) {
        verticalPageCurrentIndex = if (_verticalMoveDistance > 0) ++verticalPageCurrentIndex else --verticalPageCurrentIndex
        horizontalPageCurrentIndex = if (_horizontalMoveDistance > 0) ++horizontalPageCurrentIndex else --horizontalPageCurrentIndex
        if (verticalPageCurrentIndex < 0) {
            verticalPageCurrentIndex = 0
        }
        if (horizontalPageCurrentIndex < 0) {
            horizontalPageCurrentIndex = 0
        }

        moveOffsetX = horizontalPageCurrentIndex * frame.size.width
        moveOffsetY = verticalPageCurrentIndex * frame.size.height
    }
    val offset = _computeMovePoint(CGPoint(moveOffsetX, moveOffsetY), pagingEnabled)
    _windowSizePoint = offset
}

private fun UIScrollView._computeMovePoint(point: CGPoint, pagingEnabled: Boolean): CGPoint {
    val y = _computeY(point.y)
    val x = _computeX(point.x)
    return CGPoint(x, y)
}

private fun UIScrollView._computeY(y: Double): Double {
    return _computeXY(y, false)
}

private fun UIScrollView._computeX(x: Double): Double {
    return _computeXY(x, true)
}

private fun UIScrollView._computeXY(xOry: Double, isX: Boolean): Double {
    val contentSizeWidth = contentSize.width
    val contentSizeHeight = contentSize.height

    val thisWidth = frame.size.width
    val thisHeight = frame.size.height

    val calculateContentSizeValue = if (isX) contentSizeWidth else contentSizeHeight
    val calculateThisValue = if (isX) thisWidth else thisHeight

    val mAlwaysBounceOrientation = if (isX) alwaysBounceHorizontal else alwaysBounceVertical

    var retValue = xOry
    val deltaBottom = calculateContentSizeValue + contentInset.bottom + contentInset.top - calculateThisValue
    val over = xOry - deltaBottom

    if (calculateContentSizeValue < calculateThisValue) {
        retValue = 0.0
        if (bounces && mAlwaysBounceOrientation) {
            retValue = xOry / 3.0// add
        }
    } else {
        // out of top
        if (xOry < 0.0) {
            retValue = 0.0
            if (bounces) {
                // can Bounces
                retValue = xOry / 3.0
            }
        }

        //out of bottom
        if (xOry > Math.abs(calculateContentSizeValue + contentInset.bottom + contentInset.top - calculateThisValue)) {
            retValue = deltaBottom
            if (bounces) {
                // can Bounces
                retValue = deltaBottom + over / 3.0
            }
        }
    }
    return retValue
}

private fun UIScrollView._requestBoundsPoint(point: CGPoint): CGPoint {
    return CGPoint(_requestBoundsX(point.x), _requestBoundsY(point.y))
}

private fun UIScrollView._requestBoundsX(x: Double): Double {
    if (x < 0.0) {
        return 0.0
    }
    else if (contentSize.width < frame.width) {
        return 0.0
    }
    else if (x > contentSize.width - frame.size.width && contentSize.width > 0) {
        return contentSize.width - frame.size.width
    }
    else {
        return 0.0
    }
}

private fun UIScrollView._requestBoundsY(y: Double): Double {
    if (y < 0.0) {
        return 0.0
    }
    else if (contentSize.height < frame.height) {
        return 0.0
    }
    else if (y > contentSize.height - frame.size.height && contentSize.height > 0) {
        return contentSize.height - frame.size.height
    }
    else {
        return 0.0
    }
}