package com.yy.codex.uikit.sample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider

import com.yy.codex.foundation.NSLog
import com.yy.codex.uikit.CGRect
import com.yy.codex.uikit.UIColor
import com.yy.codex.uikit.UIControl
import com.yy.codex.uikit.UISlider
import com.yy.codex.uikit.UISwitch
import com.yy.codex.uikit.UIView

/**
 * Created by adi on 17/1/18.
 */

class TestUIComponent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TestComponent(this))
    }
}

internal class TestComponent : UIView {
    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun init() {
        super.init()

        val uiSwitch = UISwitch(context)
        uiSwitch.frame = CGRect(10.0, 10.0, 52.0, 44.0)
        //        uiSwitch.setOn(true);
        addSubview(uiSwitch)

        val uiSlider = UISlider(context)
        uiSlider.frame = CGRect(10.0, 100.0, 200.0, 32.0)
        uiSlider.setValue(0.3)
        uiSlider.onSlide({ value -> NSLog.warn(value) })
        addSubview(uiSlider)


        val view1 = TestXView(context)
        view1.frame = CGRect(10.0, 200.0, 100.0, 100.0)
        //        view1.setBackgroundColor(UIColor.orangeColor);
        addSubview(view1)
    }
}

internal class TestXView : UIView {

    constructor(context: Context, view: View) : super(context, view) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = UIColor.yellowColor.toInt()
        paint.setShadowLayer(1f, 50f, 50f, UIColor.greenColor.toInt())
        canvas.drawRoundRect(RectF(30f, 30f, 130f, 130f), 50f, 50f, paint)
    }

    override fun init() {
        super.init()
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

}
