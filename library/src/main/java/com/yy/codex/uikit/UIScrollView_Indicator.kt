package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/3/7.
 */

internal fun UIScrollView._initScrollIndicator() {
    _horizontalScrollIndicator = UIView(context)
    _horizontalScrollIndicator.wantsLayer = true
    _horizontalScrollIndicator.setBackgroundColor(UIColor(0x7f, 0x7f, 0x7f))
    _horizontalScrollIndicator.layer.cornerRadius = 1.0
    _verticalScrollIndicator = UIView(context)
    _verticalScrollIndicator.wantsLayer = true
    _verticalScrollIndicator.setBackgroundColor(UIColor(0x7f, 0x7f, 0x7f))
    _verticalScrollIndicator.layer.cornerRadius = 1.0
    addSubview(_horizontalScrollIndicator)
    addSubview(_verticalScrollIndicator)
    _hideScrollIndicator()
}

internal fun UIScrollView._showScrollIndicator() {
    _updateScrollIndicator()
    _scrollIndicatorHideAnimation?.cancel()
    if (showsHorizontalScrollIndicator && contentSize.width > 0 && frame.width > 0 && contentSize.width > frame.width) {
        _horizontalScrollIndicator.alpha = 1.0f
    }
    if (showsVerticalScrollIndicator && contentSize.height > 0 && frame.height > 0 && contentSize.height > frame.height) {
        _verticalScrollIndicator.alpha = 1.0f
    }
    bringSubviewToFront(_horizontalScrollIndicator)
    bringSubviewToFront(_verticalScrollIndicator)
}

internal fun UIScrollView._updateScrollIndicator() {
    if (contentSize.width > 0 && frame.width > 0) {
        _horizontalScrollIndicator.frame = CGRect(scrollX / UIScreen.mainScreen.scale() + (contentOffset.x / (contentSize.width - frame.width)) * (frame.width- (frame.width / contentSize.width) * frame.width), frame.height - 4.0, (frame.width / contentSize.width) * frame.width, 2.0)
    }
    if (contentSize.height > 0 && frame.height > 0) {
        _verticalScrollIndicator.frame = CGRect(frame.width - 4.0, scrollY / UIScreen.mainScreen.scale() + (contentOffset.y / (contentSize.height - frame.height)) * (frame.height - (frame.height / contentSize.height) * frame.height), 2.0, (frame.height / contentSize.height) * frame.height)
    }
}

internal fun UIScrollView._hideScrollIndicator() {
    _scrollIndicatorHideAnimation?.cancel()
    _scrollIndicatorHideAnimation = UIViewAnimator.linear(0.35, Runnable {
        _horizontalScrollIndicator.alpha = 0.0f
        _verticalScrollIndicator.alpha = 0.0f
    }, null)
}