package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.yy.codex.foundation.NSLog;

/**
 * Created by adi on 17/1/19.
 */

public class UISlider extends UIControl {

    private UIView thumbView;
    private UIView trackView;
    private UIView trackPassedView;
    private double value; // range: 0.0 ~ 1.0
    private UISliderCallback callback;


    public UISlider(@NonNull Context context, @NonNull View view) {
        super(context, view);
    }

    public UISlider(@NonNull Context context) {
        super(context);
    }

    public UISlider(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public UISlider(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UISlider(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void defaultValue(){
        value = 0.5;
//        trackView.setFrame();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void init() {
        super.init();
        defaultValue();

        trackView = new UIView(getContext());
        trackView.setWantsLayer(true);
        trackView.getLayer().setBackgroundColor(new UIColor(0xb7/255.0, 0xb7/255.0, 0xb7/255.0, 1));
        trackView.getLayer().setCornerRadius(2);

        trackPassedView = new UIView(getContext());
        trackPassedView.setWantsLayer(true);
        trackPassedView.getLayer().setBackgroundColor(new UIColor(0x10/255.0, 0x6a/255.0, 1, 1));
        trackPassedView.getLayer().setCornerRadius(2);

        thumbView = new UIView(getContext());
        thumbView.setWantsLayer(true);
        thumbView.getLayer().setBackgroundColor(UIColor.whiteColor);
        thumbView.getLayer().setCornerRadius(14).setBorderWidth(0.5).setBorderColor(new UIColor(0x00/255.0, 0x00/255.0, 0x00/255.0, 0.15));
//        thumbView.setElevation(3);
//        thumbView.setTranslationZ(8);
//        thumbView.setOutlineProvider(new ViewOutlineProvider() {
//            @Override
//            public void getOutline(View view, Outline outline) {
//                int shapeSize = 28 * 2;
//                outline.setRoundRect(0, 0, shapeSize, shapeSize, shapeSize/2);
//            }
//        });

        addSubview(trackView);
        addSubview(trackPassedView);
        addSubview(thumbView);


    }


    @Override
    public void layoutSubviews() {
        super.layoutSubviews();
        NSLog.warn(getFrame());

        double frameW = getFrame().size.width;
        trackView.setFrame(new CGRect(0, 14, frameW, 4));
        trackPassedView.setFrame(new CGRect(0, 14, frameW * value, 4));
        thumbView.setFrame(new CGRect(frameW * value - 28/2, 2, 28, 28));
    }

    @Override
    protected void onEvent(Event event) {
        super.onEvent(event);
        switch (event){

        }
    }

    public void onSlide(UISliderCallback callback){
        this.callback = callback; // callback.handle(this.value);
    }

}
