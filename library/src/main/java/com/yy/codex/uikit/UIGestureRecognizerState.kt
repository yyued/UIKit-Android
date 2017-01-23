package com.yy.codex.uikit

/**
 * Created by cuiminghui on 2017/1/11.
 */

enum class UIGestureRecognizerState {
    Possible, // the recognizer has not yet recognized its gesture, but may be evaluating touch events. this is the default state
    Began, // the recognizer has received touches recognized as the gesture. the action method will be called at the next turn of the run loop
    Changed, // the recognizer has received touches recognized as a change to the gesture. the action method will be called at the next turn of the run loop
    Ended, // the recognizer has received touches recognized as the end of the gesture. the action method will be called at the next turn of the run loop and the recognizer will be reset to Possible
    Cancelled, // the recognizer has received touches resulting in the cancellation of the gesture. the action method will be called at the next turn of the run loop. the recognizer will be reset to Possible
    Failed, // the recognizer has received a touch sequence that can not be recognized as the gesture. the action method will not be called and the recognizer will be reset to Possible
}
