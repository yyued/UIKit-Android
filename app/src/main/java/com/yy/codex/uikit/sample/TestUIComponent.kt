package com.yy.codex.uikit.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by adi on 17/1/18.
 */

class TestUIComponent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(TestUISwitch(this))
//        setContentView(TestUISlider(this))
//        setContentView(TestProgressView(this))
//        setContentView(TestUIPageControl(this))
//        setContentView(TestActivityIndicator(this))
//        setContentView(TestUIStepper(this))
//        setContentView(TestSegmentedControl(this))
        setContentView(TestSegmentedControl(this))
    }
}
