package com.yy.codex.uikit

import android.content.Context
import android.view.MotionEvent
import com.yy.codex.foundation.lets

/**
 * Created by cuiminghui on 2017/3/9.
 */

internal fun UITableViewCell._initEditingTouches() {
    val editingPanGesture = UIPanGestureRecognizer(this, "onEditingPanned:")
    this._editingPanGesture = editingPanGesture
    editingPanGesture.stealer = true
    editingPanGesture.delegate = object : UIGestureRecognizer.Delegate {
        override fun shouldBegin(gestureRecognizer: UIGestureRecognizer): Boolean {
            return Math.abs((gestureRecognizer as UIPanGestureRecognizer).translation().y) < 8.0
        }
    }
    addGestureRecognizer(editingPanGesture)
}

internal fun UITableViewCell._initEditingView() {
    _editingView = UITableViewCellActionView(context)
}

internal fun UITableViewCell._enableEditingView() {
    _editingPanGesture?.enabled = false
    _editingView.clearViews()
    lets(_tableView, _indexPath) { tableView, indexPath ->
        tableView.delegate()?.editActionsForRow(tableView, indexPath)?.let {
            _editingPanGesture?.enabled = it.size > 0
        }
    }
}

internal fun UITableViewCell._resetEditingView() {
    _editingView.clearViews()
    lets(_tableView, _indexPath) { tableView, indexPath ->
        tableView.delegate()?.editActionsForRow(tableView, indexPath)?.let {
            _editingView.resetViews(it.map {
                return@map it.requestActionView(context, indexPath)
            })
        }
    }
}

internal fun UITableViewCell._onEditingPanned(sender: UIPanGestureRecognizer) {
    when (sender.state) {
        UIGestureRecognizerState.Began -> {
            _resetEditingView()
        }
        UIGestureRecognizerState.Changed -> {
            layoutSubviews()
        }
        UIGestureRecognizerState.Ended, UIGestureRecognizerState.Cancelled, UIGestureRecognizerState.Failed -> {
            editing = _editingPanGesture?.velocity()?.x ?: 0.0 < 20.0 && (_editingPanGesture?.velocity()?.x ?: 0.0 < -200.0 || -Math.ceil(_editingPanGesture?.translation()?.x ?: 0.0) > _editingView.contentWidth * 2 / 3)
            UIViewAnimator.springWithBounciness(1.0, 20.0, Runnable { _updateFrames() }, null)
            if (editing) {
                val maskView = UITableViewCellActionMaskView(context)
                maskView.addGestureRecognizer(UITapGestureRecognizer(this, "_endEditing"))
                maskView.touchesView = _editingView
                maskView.frame = _tableView?.frame ?: CGRect(0, 0, 0, 0)
                _tableView?.superview?.addSubview(maskView)
                this._editingMaskView = maskView
            }
        }
    }
}

internal class UITableViewCellActionView(context: Context) : UIView(context) {

    var contentWidth = 0.0

    fun clearViews() {
        subviews.forEach(UIView::removeFromSuperview)
    }

    fun resetViews(views: List<UITableViewRowActionView>) {
        views.forEach {
            addSubview(it)
        }
        contentWidth = views.sumByDouble { it.contentWidth }
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        if (contentWidth <= 0.0) {
            return
        }
        val percentage = frame.width / contentWidth
        var currentX = 0.0
        subviews.forEachIndexed { idx, subview ->
            val subview = (subview as? UITableViewRowActionView) ?: return@forEachIndexed
            subview.frame = CGRect(currentX, 0.0, subview.contentWidth * percentage + 1.0, frame.height)
            currentX += subview.contentWidth * percentage
        }
    }

}

private class UITableViewCellActionMaskView(context: Context): UIView(context) {

    internal var touchesView: UIView? = null

    override fun hitTest(point: CGPoint, event: MotionEvent): UIView? {
        touchesView?.let {
            if (UIViewHelpers.pointInside(it, this.convertPoint(point, it))) {
                return null
            }
        }
        return super.hitTest(point, event)
    }

}