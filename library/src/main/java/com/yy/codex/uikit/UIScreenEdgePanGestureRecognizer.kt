package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/1/13.
 */

class UIScreenEdgePanGestureRecognizer : UIPanGestureRecognizer {

    enum class Edge {
        Top,
        Left,
        Bottom,
        Right
    }

    var edge = Edge.Left
    var edgeLength = 22.0

    constructor(target: Any, selector: String) : super(target, selector) {}

    constructor(triggerBlock: Runnable) : super(triggerBlock) {}

    override fun touchesBegan(touches: List<UITouch>, event: UIEvent) {
        super.touchesBegan(touches, event)
        if (!checkEdge(touches)) {
            state = UIGestureRecognizerState.Failed
        }
    }

    private fun checkEdge(touches: List<UITouch>): Boolean {
        touches.forEach {
                if (edge == Edge.Left && it.absolutePoint.x < edgeLength) {
                    return true
                } else if (edge == Edge.Right && UIScreen.mainScreen.bounds().size.width - it.absolutePoint.x < edgeLength) {
                    return true
                } else if (edge == Edge.Top && it.absolutePoint.y < edgeLength) {
                    return true
                } else if (edge == Edge.Bottom && UIScreen.mainScreen.bounds().size.height - it.absolutePoint.y < edgeLength) {
                    return true
                }
            }
        return false
    }

}
