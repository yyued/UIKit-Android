package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/3/10.
 */

abstract class UIScrollViewDelegateObject: UIScrollView.Delegate {

    override fun didScroll(scrollView: UIScrollView) {}
    override fun willBeginDragging(scrollView: UIScrollView) {}
    override fun didEndDragging(scrollView: UIScrollView, willDecelerate: Boolean) {}
    override fun willBeginDecelerating(scrollView: UIScrollView) {}
    override fun didEndDecelerating(scrollView: UIScrollView) {}

}